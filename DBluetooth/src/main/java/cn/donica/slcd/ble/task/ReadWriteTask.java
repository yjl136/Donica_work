package cn.donica.slcd.ble.task;

import android.os.Handler;
import android.os.Message;


/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-10-27 14:39
 * Describe:      读写任务
 */
public class ReadWriteTask implements Runnable {

    protected final int READ_BLOCK_CMD = 0x101;
    protected final int READ_BLOCK = 0x201;
    private Handler handler;
    private int block;

    public ReadWriteTask(Handler handler, int block) {
        this.handler = handler;
        this.block = block;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.obj = block;
        message.what = READ_BLOCK;
        handler.sendMessageDelayed(message, 120);

        Message msg = new Message();
        msg.obj = block;
        msg.what = READ_BLOCK_CMD;
        handler.sendMessage(msg);

    }
}
