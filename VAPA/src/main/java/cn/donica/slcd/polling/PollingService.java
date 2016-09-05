package cn.donica.slcd.polling;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.donica.slcd.utils.AudioUtil;
import cn.donica.slcd.utils.LogUtil;
import cn.donica.slcd.utils.ShellUtils;
import cn.donica.slcd.utils.VolleyRequestUtil;

public class PollingService extends Service {
    private MyThread myThread;
    private JSONArray jsonArray;
    public static final String ACTION = "cn.donica.slcd.service.PollingService";
    private boolean flag = true;

    private Context mContext;
    private WindowManager mWinMng;
    private ScreenSaverView screenView;

    public static final String TAG = "PollingService";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "onCreate()");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.myThread = new MyThread();
        this.myThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        this.flag = false;
        super.onDestroy();
    }

    public void addView() {
        if (screenView == null) {
            screenView = new ScreenSaverView(mContext);
            WindowManager.LayoutParams param = new WindowManager.LayoutParams();
            param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            param.format = PixelFormat.RGBA_8888;
            // mParam.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            param.width = WindowManager.LayoutParams.MATCH_PARENT;
            param.height = WindowManager.LayoutParams.MATCH_PARENT;
            mWinMng.addView(screenView, param);
        }
    }

    public void removeView() {
        if (screenView != null) {
            mWinMng.removeView(screenView);
            screenView = null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    removeView();
                    stopSelf();
                    break;
                case 1:
                    addView();
                    break;
            }
        }
    };

    private class MyThread extends Thread {
        @Override
        public void run() {
            LogUtil.d(TAG, "发送请求");
            jsonArray = VolleyRequestUtil.getJSONResponse(MyApplication.URL);
            if (jsonArray != null) {
                try {
                    JSONObject jsonObject1;
                    jsonObject1 = jsonArray.getJSONObject(0);
                    Log.d(TAG, "jsonObject1" + jsonObject1);
                    int type1 = jsonObject1.getInt("type");
                    int value1 = jsonObject1.getInt("value");
                    String time1 = jsonObject1.getString("time");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                    Log.d(TAG, "jsonObject2" + jsonObject2);
                    int type2 = jsonObject2.getInt("type");
                    int value2 = jsonObject2.getInt("value");
                    String time2 = jsonObject2.getString("time");
                    String videoUrl = jsonObject2.getString("videoUrl");
                    if (type2 == 49) {
                        LogUtil.d(TAG, "value2 =" + value2);
                        LogUtil.d(TAG, "appIsRun = " + MyApplication.appIsRun);
                        if (value2 == 1 && MyApplication.appIsRun != true) {
                            if (videoUrl != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(videoUrl), "video/mp4");
                                startActivity(intent);
                            } else {
                                LogUtil.d(TAG, "ShellAppRunStr");
                                MyApplication.appIsRun = true;
                                String commandStr[] = {MyApplication.ShellAppSuStr, MyApplication.ShellAppRunStr};
                                ShellUtils.execCommand(commandStr, true);
                                AudioUtil.setAudioMute(MyApplication.getContext(), true);
                            }
                        } else if (value2 == 0 && MyApplication.appIsRun) {
                            LogUtil.d(TAG, "ShellKillAllStr");
                            MyApplication.appIsRun = false;
                            ShellUtils.execCommand(MyApplication.ShellKillAllStr, true);
                            AudioUtil.setAudioMute(MyApplication.getContext(), false);
                        }
                    } else if (type2 == 50) {
                        if (value1 == 1) {
                            Message msg = new Message();
                            msg.what = 1;
                            mHandler.sendMessageDelayed(msg, 0);
                            AudioUtil.setAudioMute(MyApplication.getContext(), true);
                        } else if (value2 == 0) {
                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessageDelayed(msg, 0);
                            AudioUtil.setAudioMute(MyApplication.getContext(), false);
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}