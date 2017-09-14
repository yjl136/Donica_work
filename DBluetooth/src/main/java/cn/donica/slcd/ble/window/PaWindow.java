package cn.donica.slcd.ble.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.ble.view.PaLayout;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-31 09:31
 * Describe:
 */
public class PaWindow {
    private static WindowManager mWindowManager;
    private static PaLayout mPaLayout;
    private Context mContext;

    public PaWindow(Context Context) {
        this.mContext = Context;
    }

    /**
     * 创建漂浮窗口
     */
    public void startPa() {
        WindowManager windowManager = getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mPaLayout == null) {
            mPaLayout = new PaLayout(mContext);
            LayoutParams mParams = getLayoutParams(screenWidth, screenHeight);
            DLog.info("startPa");
            windowManager.addView(mPaLayout, mParams);
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

    /**
     * 获取WindowManager
     *
     * @param
     * @return
     */
    private WindowManager getWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 移除漂浮窗口
     */
    public void stopPa() {
        if (mPaLayout != null) {
            WindowManager windowManager = getWindowManager();
            DLog.info("stopPa");
            windowManager.removeView(mPaLayout);
            mPaLayout = null;
        }
    }
}
