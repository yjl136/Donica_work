package cn.donica.slcd.ble.task;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.io.InputStream;
import java.io.OutputStream;

import cn.donica.slcd.ble.cmd.CmdManage;
import cn.donica.slcd.ble.task.entity.TLVEntity;
import cn.donica.slcd.ble.task.entity.TLVSet;
import cn.donica.slcd.ble.task.entity.BlockEntity;
import cn.donica.slcd.ble.task.entity.ResultEntity;
import cn.donica.slcd.ble.utils.DLog;
import cn.donica.slcd.ble.utils.StringUtil;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-11-09 13:40
 * Describe:
 */
public class AsyncTask extends Handler {
    private static Looper looper;
    private WorkHandler workHandler;
    private static AsyncTask task;
    private InputStream is;
    private OutputStream out;
    private TLVSet set;
    private static IStatus service;

    private AsyncTask() {
        super();
        if (looper == null) {
            HandlerThread thread = new HandlerThread("WorkThread");
            thread.start();
            looper = thread.getLooper();
        }
        workHandler = createWorkHandler(looper);
    }

    /**
     * 获取Task实例
     *
     * @return
     */
    public static AsyncTask getInstance(IStatus status) {
        service = status;
        if (task == null) {
            task = new AsyncTask();
        }
        return task;
    }

    //表示正在读取的tlv
    private TLVEntity tlv;

    /**
     * @param data  数据内容
     * @param block 块号
     * @param state 状态
     */
    public void handleBlockReceive(byte[] data, int block, int state) {
        try {
            if (state == ResultEntity.State.BLOCK_READ_SUCCESS && data.length == 22) {
                byte[] blockBytes = StringUtil.subBytes(data, 4, 16);
                DLog.info("block: " + block + "  block data:" + StringUtil.bytes2HexString(blockBytes));
                if (block == 3) {
                    //如果是第三块数据
                    if (blockBytes[0] == (byte) 0xe1 && blockBytes[1] == (byte) 0x10) {
                        //初始化数据集合
                        submit(4, is, out);
                    } else {
                        DLog.error("第三块数据有误");
                    }
                } else if (block == 4) {
                    initTLVSet();
                    initTLV(0, block, blockBytes);
                } else if (block >= 5) {
                    //怎么样来判断有没有结束呢？
                    if (tlv == null) {
                        //为空
                        initTLV(0, block, blockBytes);
                    } else {
                        //不为空,判断当前块号
                        if (tlv.getEndIndex() == block) {
                            //判断是不是最后一个字节
                            if (tlv.getEnd() == 4) {
                                //如果当前块号为最后一块，
                                tlv.addBytes(blockBytes, 0, 4);
                                set.addEntity(tlv);
                                tlv = null;
                                submit(block + 1, is, out);
                            } else {
                                int end = tlv.getEnd();
                                tlv.addBytes(blockBytes, 0, end);
                                set.addEntity(tlv);
                                tlv = null;
                                initTLV(end, block, blockBytes);
                            }
                        } else {
                            //当前块号不是最后一块
                            tlv.addBytes(blockBytes, 0, 4);
                            submit(block + 1, is, out);
                        }
                    }
                }
            } else {
                DLog.info("读块失败");
                service.onFail(ResultEntity.State.BLOCK_READ_ERROR);
            }
        } catch (Exception e) {
            service.onFail(ResultEntity.State.BLOCK_READ_ERROR);
        }

    }

    private void initTLV(int start, int block, byte[] blockBytes) {
        DLog.error("start:" + start + " block:" + block);
        if (blockBytes[start] == (byte) 0xfe) {
            DLog.info("读取完成");
            tlv = null;
            service.onSuccess(set);
            return;
        }
        tlv = new TLVEntity(blockBytes, block, start);
        if (tlv.getLength() + start > 4) {
            //读下一块
            submit(block + 1, is, out);
        } else if (tlv.getLength() + start == 4) {
            set.addEntity(tlv);
            tlv = null;
            submit(block + 1, is, out);
        } else {
            int end = tlv.getEnd();
            set.addEntity(tlv);
            tlv = null;
            initTLV(end, block, blockBytes);
        }
    }


    /**
     * 获取一个v长度
     *
     * @param blocks
     * @return
     */
    private int getTLVLenght(byte[] blocks) {
        if (blocks[1] == (byte) 0xff) {
            //如果是ff表示三个字节表示长度
            return 1;
        } else {
            String hex = Integer.toHexString(blocks[1]);
            return Integer.valueOf(hex, 16);
        }

    }

    /**
     * 检查是否包含指定字节
     *
     * @param src
     * @param b
     * @return
     */
    private boolean contain(byte[] src, byte b) {
        for (int i = 0; i < src.length; i++) {
            if (src[i] == b) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化tlvset
     */
    private void initTLVSet() {
        if (set == null) {
            set = new TLVSet();
        } else {
            set.clear();
        }
    }

    /**
     * 处理结果
     *
     * @param result
     */
    public void handleBlockReceive(ResultEntity result) {
        handleBlockReceive(result.getBuf(), result.getBlock(), result.getState());
    }

    /**
     * 创建work handler
     *
     * @param looper
     * @return
     */
    private WorkHandler createWorkHandler(Looper looper) {
        return new WorkHandler(looper);
    }


    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            BlockEntity entity = (BlockEntity) msg.obj;
            //写
            boolean isSuccess = entity.write();
            if (isSuccess) {
                //睡60ms
                SystemClock.sleep(60);
                //读块内容
                ResultEntity result = entity.read();
                //  DLog.info("result:"+result.toString() );
                handleBlockReceive(result);
            } else {
                //发送通知
                service.onFail(ResultEntity.State.BLOCK_WRITE_ERROR);
            }

        }
    }


    /**
     * @param block 数据块
     * @param is    输入流
     * @param out   输出流
     */
    public void submit(int block, InputStream is, OutputStream out) {
        byte[] cmdBytes = CmdManage.getReadBlock(block);
        BlockEntity entity = new BlockEntity(block, cmdBytes, is, out);
        submit(entity);
        this.is = is;
        this.out = out;
    }

    /**
     * @param entity
     */
    private void submit(BlockEntity entity) {
        Message msg = workHandler.obtainMessage();
        msg.obj = entity;
        workHandler.sendMessage(msg);
    }


    public interface IStatus {
        public void onSuccess(TLVSet set);

        public void onFail(int state);

    }
}
