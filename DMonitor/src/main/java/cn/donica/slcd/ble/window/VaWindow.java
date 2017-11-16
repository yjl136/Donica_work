package cn.donica.slcd.ble.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.TextureView;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import java.io.IOException;
import cn.donica.slcd.ble.utils.DLog;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-31 09:31
 * Describe:
 */
public class VaWindow implements TextureView.SurfaceTextureListener {
    private TextureView mTextureView;
    private WindowManager mWindowManager;
    private Camera mCamera;
    private Context mcontext;

    public VaWindow(Context context) {
        this.mcontext = context;
    }

    /**
     * 开启Va
     *
     * @param
     */
    public void startVa() {
        WindowManager windowManager = getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mTextureView == null) {
            mTextureView = new TextureView(mcontext);
            LayoutParams mParams = getLayoutParams(screenWidth, screenHeight);
            DLog.info("startVa");
            mTextureView.setSurfaceTextureListener(this);
            windowManager.addView(mTextureView, mParams);
        }
    }

    @NonNull
    private LayoutParams getLayoutParams(int screenWidth, int screenHeight) {
        LayoutParams mParams = new LayoutParams();
        mParams.x = 0;
        mParams.y = 0;
        mParams.flags = LayoutParams.FLAG_HARDWARE_ACCELERATED | LayoutParams.FLAG_FULLSCREEN;
        mParams.type = LayoutParams.TYPE_PHONE;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.CENTER;
        mParams.width = screenWidth;
        mParams.height = screenHeight;
        return mParams;
    }

    private WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public void stopVa() {
        if (mTextureView != null) {
            WindowManager windowManager = getWindowManager();
            DLog.info("stopVa");
            windowManager.removeView(mTextureView);
            mTextureView = null;
            mCamera = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        DLog.info("onSurfaceTextureAvailable");
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        DLog.info("onSurfaceTextureSizeChanged");
    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        DLog.info("onSurfaceTextureDestroyed");
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        DLog.info("onSurfaceTextureUpdated");
    }
}
