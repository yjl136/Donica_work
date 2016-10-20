package cn.donica.slcd.polling;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayerAcvitity extends Activity implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    public static final String TAG = "MyVideoPlay";
    private VideoView mVideoView;
    private Uri mUri;
    private int mPositionWhenPaused = -1;

    private MediaController mMediaController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_acvitity);

        //Set the screen to landscape.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mUri = Uri.parse("http://192.168.4.120/tv/test.mp4");
        //Create media controller
        mMediaController = new MediaController(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                PlayerAcvitity.this.finish();
            }
        });
    }

    public void onStart() {
        // Play Video
        if (mVideoView != null && mUri != null) {
            mVideoView.setVideoURI(mUri);
            mVideoView.start();
        } else {
            Toast.makeText(PlayerAcvitity.this, "发生错误", Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }

    public void onPause() {
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();
        super.onPause();
    }

    public void onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        PlayerAcvitity.this.finish();
    }
}