package cn.donica.slcd.pava;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.TextureView;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-18 11:12
 * Describe:
 */

public class VaActivity extends Activity implements TextureView.SurfaceTextureListener {
    private final static String ACTION_VA = "cn.donica.slcd.action.VA";
    private final static String VA_KEY = "va";
    private VaReceiver mVaReceiver;
    private TextureView mTextureView;
    private Camera mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_va);
        mTextureView = (TextureView) findViewById(R.id.vaTv);
        mTextureView.setSurfaceTextureListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_VA);
        mVaReceiver = new VaReceiver();
        registerReceiver(mVaReceiver, filter);
    }

    class VaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            DLog.info("onReceive");
            int va = intent.getIntExtra(VA_KEY, 0);
            if (va == 0) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                mTextureView = null;
                VaActivity.this.finish();
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        DLog.info("onSurfaceTextureAvailable");
        try {
            mCamera = Camera.open();
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (Exception ioe) {
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        DLog.info("onSurfaceTextureSizeChanged");
    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        DLog.info("onSurfaceTextureDestroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        DLog.info("onSurfaceTextureUpdated");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVaReceiver != null) {
            unregisterReceiver(mVaReceiver);
        }
        mTextureView = null;
    }
}
