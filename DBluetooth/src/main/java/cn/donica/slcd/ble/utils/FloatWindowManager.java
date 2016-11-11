package cn.donica.slcd.ble.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import cn.donica.slcd.ble.view.FloatView;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-31 09:31
 * Describe:
 */
public class FloatWindowManager {
    private static WindowManager mWindowManager;
    private static FloatView mFloatView;
    private static boolean isFloating;
    private static LayoutParams mParams;


    /**
     * 创建漂浮窗口
     *
     * @param context
     */
    public static void createFloatView(Context context, int viewWidth, int viewHeight) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (mFloatView == null) {
            mFloatView = new FloatView(context);
            if (mParams == null) {
                mParams = new LayoutParams();
                mParams.x = screenWidth / 2 - viewWidth / 2;
                mParams.y = screenHeight / 2 - viewHeight / 2;
                mParams.type = LayoutParams.TYPE_PHONE;
                mParams.format = PixelFormat.RGBA_8888;
                mParams.gravity = Gravity.LEFT | Gravity.TOP;
                mParams.width = viewWidth;
                mParams.height = viewHeight;
            }
            isFloating = true;
            DLog.info("createFloatView");
            windowManager.addView(mFloatView, mParams);
        }
    }

    /**
     * 获取WindowManager
     *
     * @param context
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 更新漂浮窗口
     */
    public static void updateFloatView(String state) {
        if (mFloatView != null) {
            mFloatView.setState(state);
        }
    }

    /**
     * 移除漂浮窗口
     */
    public static void removeFloatView(Context context) {
        if (mFloatView != null) {
            WindowManager windowManager = getWindowManager(context);
            isFloating = false;
            DLog.info("removeFloatView");
            windowManager.removeView(mFloatView);
            mFloatView = null;
        }

    }

    public static boolean isFloating() {
        return isFloating;
    }


}
