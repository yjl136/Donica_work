package cn.donica.slcd.va;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import cn.donica.slcd.shell.Shell;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class VaService extends IntentService {

    public VaService() {
        super("VaService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Va", "thread:" + Thread.currentThread());
        String cmd = "/system/bin/mxc-v4l2-tvin -x 0 -ct 27 -cl 0 -cw 720 -ch 480 -ot 0 -ol 0 -ow 1280 -oh 800 -m 2 -tb 1";
        executeCMD(cmd);
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
                Log.i("Va", "out:" + line);
            }
        }
    }

}
