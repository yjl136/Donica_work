package cn.donica.slcd.ble.activity;

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
import android.view.WindowManager;

import cn.donica.slcd.ble.R;
import cn.donica.slcd.ble.utils.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-10-18 11:12
 * Describe:
 */

public class VaActivity extends Activity implements TextureView.SurfaceTextureListener {
    private final static String ACTION_PA = "cn.donica.slcd.action.PA";
    private final static String ACTION_VA = "cn.donica.slcd.action.VA";
    private final static String VA_KEY = "va";
    private VaReceiver mVaReceiver;
    private TextureView mTextureView;
    private Camera mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
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
            int va = intent.getIntExtra(VA_KEY, 0);
            DLog.info("onReceive va:" + va);
            if (va == 0) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                mTextureView = null;
                VaActivity.this.finish();
                overridePendingTransition(0, 0);
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
            mCamera = null;
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
