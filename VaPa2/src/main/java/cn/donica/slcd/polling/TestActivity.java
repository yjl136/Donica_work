package cn.donica.slcd.polling;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class TestActivity extends Activity {

    private static final int PICK_FROM_GALLERY = 1;
    //variables declarations here
    VideoView videoView;
    MediaController mc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoView = (VideoView) findViewById(R.id.videoView2);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("http://192.168.23.1/tv/test.mp4"), "video/mp4");
        startActivity(intent);
        TestActivity.this.finish();
    }


}

