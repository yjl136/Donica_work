package com.advantech.adv;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.advantech.advfuntest.R;

public class AudioFun {

    public class Audio {
        public boolean lineOut;
        public boolean lineOut_result;
        public boolean micIn;
        public boolean micIn_result;
        public boolean lineIn;
        public boolean lineIn_result;
    }

    private Device dev;
    private RelativeLayout lineOut;
    private RelativeLayout MicIn;
    private RelativeLayout lineIn;
    private LinearLayout backBtn;
    private MediaPlayer mediaPlayer;
    private MediaPlayer recorderPlayer;
    private MediaRecorder mediaRecorder;
    private Audio audio;
    private Activity mainActivity;
    private RecordThread recordThread;
    public Handler mainHandler;
    public ExecCmd execcmd;

    private Thread PlayThread = new Thread(new Runnable() {

        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(mainActivity, R.raw.audio);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    AlertDialog.Builder builder = new Builder(mainActivity);
                    builder.setMessage("刚刚能够听到播放的声音吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("能够",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    audio.lineOut_result = true;
                                    audio.micIn_result = false;
                                    dev.audioMsg += "Mic In Failed\n";
                                    execcmd.writeLog("====================================\n");
                                    execcmd.writeLog(dev.audioMsg);
                                    execcmd.writeLog("====================================\n");
                                    Message msg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("result", false);
                                    msg.setData(bundle);// mes利用Bundle传递数据
                                    mainHandler.sendMessage(msg);// 用activity中的handler发送消息
                                    recordThread.interrupt();
                                }
                            });

                    builder.setNegativeButton("不能",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    audio.lineOut_result = false;
                                    dev.audioMsg += "Line Out Failed,Mic In can't confirm\n";
                                    execcmd.writeLog("====================================\n");
                                    execcmd.writeLog(dev.audioMsg);
                                    execcmd.writeLog("====================================\n");
                                    Message msg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("result", false);
                                    msg.setData(bundle);// mes利用Bundle传递数据
                                    mainHandler.sendMessage(msg);// 用activity中的handler发送消息
                                    recordThread.interrupt();
                                }
                            });
                    builder.create().show();
                }
            });
        }

    });

    public class RecordThread extends Thread {
        private AudioRecord audioRecord;
        private int bs;
        private int SAMPLE_RATE_IN_HZ = 8000;

        public void run() {
            super.run();
            bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bs);
            audioRecord.startRecording();
            // 用于读取的
            byte[] buffer = new byte[bs];
            int count = 0;
            while (!isInterrupted()) {
                int r = audioRecord.read(buffer, 0, bs);
                int v = 0;
                // 将 buffer 内容取出，进行平方和运算
                for (int i = 0; i < buffer.length; i++) {
                    // 这里没有做运算的优化，为了更加清晰的展示代码
                    v += buffer[i] * buffer[i];
                }
                // 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
                // 如果想利用这个数值进行操作，建议用 sendMessage 将其抛出，在 Handler 里进行处理。
                Log.d("spl", String.valueOf(v / (float) r));
                if ((v / (float) r) >= 2000) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == 5) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("result", true);
                    msg.setData(bundle);// mes利用Bundle传递数据
                    mainHandler.sendMessage(msg);// 用activity中的handler发送消息
                    this.interrupt();
                    mediaPlayer.stop();
                    PlayThread.interrupt();
                }
            }
            audioRecord.stop();
        }

        public void pause() {
            // 在调用本线程的 Activity 的 onPause 里调用，以便 Activity 暂停时释放麦克风
            this.interrupt();
        }
    }

    AudioFun() {

        recorderPlayer = new MediaPlayer();
        dev = new Device();
        dev.audioFun = true;
        dev.audioMsg = new String("");

        // 这里添加：读取jSon文件关于Udisk的配置。
        audio = new Audio();
        audio.lineOut = true;
        audio.lineOut_result = false;
        audio.micIn = true;
        audio.micIn_result = false;
        audio.lineIn = false;
        audio.lineIn_result = false;

        recordThread = new RecordThread();
        execcmd = new ExecCmd();
    }

    void startTest(Handler handler, Activity activity) {
        mainHandler = handler;
        mainActivity = activity;
        if (recordThread.isAlive())
            recordThread.interrupt();
        if (PlayThread.isAlive())
            PlayThread.interrupt();
        recordThread.start();
        PlayThread.start();
    }

    public String newFileName() {
        String mFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        Log.e("_____+++yixuan", mFileName);
        return mFileName += "/rcd_Recorde.3gp";
    }

}
