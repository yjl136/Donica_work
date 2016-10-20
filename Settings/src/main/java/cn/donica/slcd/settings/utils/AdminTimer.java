package cn.donica.slcd.settings.utils;

import android.os.CountDownTimer;
import android.os.Handler;

/**
 * @author liangmingjie
 */
public class AdminTimer extends CountDownTimer {
    private static final String TAG = "AdminTimer";
    private static Handler mHandler;
    public static final int IN_RUNNING = 1001;
    public static int END_RUNNING = 1002;


    /**
     * @param millisInFuture    // 倒计时的时长
     * @param countDownInterval // 间隔时间
     * @param handler           // 通知进度的Handler
     */
    public AdminTimer(long millisInFuture, long countDownInterval, Handler handler) {
        super(millisInFuture, countDownInterval);
        mHandler = handler;
    }

    // 结束
    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
        LogUtil.d(TAG, "停止记时");
        mHandler.obtainMessage(END_RUNNING, "停止记时").sendToTarget();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub
        if (mHandler != null) {
            LogUtil.d(TAG, (millisUntilFinished / 1000) + "s 后停止");
            mHandler.obtainMessage(IN_RUNNING,
                    (millisUntilFinished / 1000) + "s 后停止").sendToTarget();
        }
    }
}
