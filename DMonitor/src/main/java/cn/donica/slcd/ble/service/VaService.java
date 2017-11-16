package cn.donica.slcd.ble.service;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.shell.Shell;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class VaService extends IntentService {
    //开始Va
    private final static String ACTION_PLAY = "cn.donica.slcd.action.PLAY";
    //停止VA
    private final static String ACTION_STOP = "cn.donica.slcd.action.STOP";
    public final static String CMD_PLAY = "/system/bin/mxc-v4l2-tvin -x 0 -ct 27 -cl 0 -cw 720 -ch 480 -ot 0 -ol 0 -ow 1280 -oh 800 -m 2 -tb 1";
    public final static String CMD_STOP = "busybox killall mxc-v4l2-tvin";

    public VaService() {
        super("VaService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        DLog.info("action:" + action);
        if (ACTION_PLAY.equals(action)) {
            executeCMD(CMD_PLAY);
        } else if (ACTION_STOP.equals(action)) {
            executeCMD(CMD_STOP);
        } else {
            DLog.warn("unkonw action!!");
        }
    }

    /**
     * 执行命令
     *
     * @param cmd
     */
    private void executeCMD(String cmd) {
        List<String> result = Shell.SU.run(cmd);
        if (result != null) {
            for (String line : result) {
                DLog.info("out:" + line);
            }
        }
    }
}
