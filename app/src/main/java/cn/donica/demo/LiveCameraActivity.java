package cn.donica.demo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-13 12:41
 * Describe:
 */

public class LiveCameraActivity extends Activity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        setContentView(mTextureView);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
        for (Camera.Size size : sizes) {
            Log.i("LiveCameraActivity", "width:" + size.width + "   height:" + size.height);
        }
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(1280, 800);
            // mCamera.setParameters(parameters);
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }
}
