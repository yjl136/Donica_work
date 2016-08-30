package cn.donica.slcd.dmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

import cn.donica.slcd.dmanager.aidl.ILogTrapAidl;
import cn.donica.slcd.dmanager.snmp.trap.LogRecordTrap;
import cn.donica.slcd.dmanager.snmp.trap.LogWarnTrap;

public class MainService extends Service {
    private static final String TAG = "MainService";

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    ILogTrapAidl.Stub mBinder = new ILogTrapAidl.Stub() {

        /**
         * @param packageName 应用包名
         * @param appVersionName 应用版本名字
         * @param location 日志触发的代码位置
         * @param tag 日志的标签，默认值有l为launch（启动日志）,s为status（部件的状态日志）,m为maintain（维护日志，一般为机务人员对关键模块操作的日志记录）
         * @param logContent 日志的具体内容
         * @param status 状态信息，默认为null,一般为部件的开启状态，例如WiFi开关，蓝牙开关
         * @throws RemoteException
         */
        @Override
        public void LogRecord(final String packageName, final String appVersionName, final String location, final String tag, final String logContent, final String status) throws RemoteException {
            Log.d(TAG, "MainService...+LogRecord()" + packageName + appVersionName + location + tag + logContent + status);
            LogRecordTrap logSysRecordTrap = new LogRecordTrap();
            try {
                logSysRecordTrap.sendRecord(packageName, appVersionName, location, tag, logContent, status);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            new Thread() {
//                @Override
//                public void run() {
//                    Message msg = new Message();
//                    Bundle bundle = new Bundle();
//                    //这个list用于在budnle中传递 需要传递的ArrayList<Object>
//                    ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
//                    list.add(packageName);
//                    list.add(appVersionName);
//                    list.add(location);
//                    list.add(tag);
//                    list.add(logContent);
//                    list.add(status);
//                    bundle.putParcelableArrayList("list", list);
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//                }
//            }.start();

        }

//            //定义Handler对象
//         Handler   handler =new Handler(){
//                @Override
////当有消息发送出来的时候就执行Handler的这个方法
//                public void handleMessage(Message msg){
//                    super.handleMessage(msg);
//                    Bundle bundle = msg.getData();
//                    ArrayList list = bundle.getParcelableArrayList("list");
//                    String packageName = (String) list.get(0);//强转成你自己定义的list，这样list2就是你传过来的那个list了
//                    String appVersionName = (String) list.get(1);
//                    String location = (String) list.get(2);
//                    String tag = (String) list.get(3);
//                    String logContent = (String) list.get(4);
//                    String status = (String) list.get(5);
//
//                }
//            };

        /**
         * @param level 应用的告警的级别，有i,w,e三种.其中i为info(普通信息)，w为warn（警告信息），e为error（错误信息）
         * @param category 报警信息的分类，有0和1两种。0为软件类信息software，1为硬件类信息hardware
         * @param type  告警信息的类别，有0,1,2,3，4，5六种。
         *                   	0--未知类别（无法判断哪种类别）
         *                   	1--系统告警
         *                   	2--数据库告警
         *                   	3--网络告警
         *                   	4--处理错误告警
         *                   	5--服务质量告警
         * @param logContent 告警信息的具体内容
         * @param location 告警信息的触发代码位置
         * @throws RemoteException
         */
        @Override
        public void LogWarn(String level, int category, int type, String logContent, String location, String tag) throws RemoteException {
            Log.d(TAG, "MainService...+LogWarn()" + level + category + type + logContent + location);
            LogWarnTrap logWarnTrap = new LogWarnTrap();
            try {
                logWarnTrap.sendWarn(level, category, type, logContent, location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}


