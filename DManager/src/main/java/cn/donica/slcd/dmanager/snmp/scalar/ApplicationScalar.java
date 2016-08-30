/**
 *
 */
package cn.donica.slcd.dmanager.snmp.scalar;

import java.util.HashMap;
import java.util.List;

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.android.internal.app.IUsageStats;
import com.android.internal.os.PkgUsageStats;

import cn.donica.slcd.dmanager.snmp.ISnmpOID;


/**
 * @author Luke Huang
 */
public class ApplicationScalar implements IScalar {
    private static final String TAG = "ApplicationScalar";
    public Context context;
    private HashMap<String, String> map = null;

    public ApplicationScalar(Context context) {
        this.context = context;
    }

    /**
     * 获取Boolean值的app运行状态
     *
     * @param packageName
     * @return
     */
    public boolean getApplicationState(String packageName) {
        boolean state = false;

        List<RunningAppProcessInfo> runningAppProcessInfoList = getRunningAppProcessInfoList();
        for (RunningAppProcessInfo appProcessInfo : runningAppProcessInfoList) {
            if (appProcessInfo.processName.equals(packageName)) {
                state = true;
                break;
            }
        }
        if (!state) {// if application is not found in application list, then search it in service list
            List<RunningServiceInfo> runningServiceInfos = getRunningServiceInfoList();
            for (RunningServiceInfo serviceInfo : runningServiceInfos) {
                if (serviceInfo.service.getClassName().equals(packageName)) {
                    state = true;
                    break;
                }
            }
        }
        return state;
    }

    private List<RunningAppProcessInfo> getRunningAppProcessInfoList() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningAppProcesses();
    }

    private List<RunningServiceInfo> getRunningServiceInfoList() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningServices(Integer.MAX_VALUE);
    }

    /**
     * 获取long值得app运行时间
     *
     * @param packageName
     * @return
     */
    public long getRunningTime(String packageName) {
        try {
            IUsageStats usageStats = IUsageStats.Stub.asInterface(ServiceManager.getService("usagestats"));
            PkgUsageStats[] pkgUsageStats = usageStats.getAllPkgUsageStats();
            for (int i = 0; i < pkgUsageStats.length; i++) {
                PkgUsageStats pkgUsageStat = pkgUsageStats[i];
                if (pkgUsageStat.packageName.equals(packageName)) {
                    Log.d(TAG, "app运行时间为：" + (pkgUsageStat.usageTime / 1000));
                    return (pkgUsageStat.usageTime / 1000);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 传入oid参数为Key的值，取出Value值app包名packageName
     *
     * @param oid
     * @return
     */
    private String getPackageName(String oid) {
        String packageName = map.get(oid);
        return packageName;
    }

    /**
     * 构造函数初始化APP的运行状态和运行时间的KeyValue对，其中Key为OID,Vaule为APP的包名
     */
    public ApplicationScalar() {
        map = new HashMap<String, String>();
        map.put(ISnmpOID.OID_LAUNCHER_STATE, "cn.donica.slcd.launcher");
        map.put(ISnmpOID.OID_LAUNCHER_RUNNINGTIME, "cn.donica.slcd.launcher");
        map.put(ISnmpOID.OID_UPGRADE_STATE, "cn.donica.slcd.upgrade");
        map.put(ISnmpOID.OID_UPGRADE_RUNNINGTIME, "cn.donica.slcd.upgrade");
        map.put(ISnmpOID.OID_SETTINGS_STATE, "cn.donica.slcd.settings");
        map.put(ISnmpOID.OID_SETTINGS_RUNNINGTIME, "cn.donica.slcd.settings");
        map.put(ISnmpOID.OID_OAM_STATE, "cn.donica.slcd.oam");
        map.put(ISnmpOID.OID_OAM_RUNNINGTIME, "cn.donica.slcd.oam");
        map.put(ISnmpOID.OID_MESSAGE_SERVICE_STATE, "cn.donica.slcd.message");
        map.put(ISnmpOID.OID_MESSAGE_SERVICE_RUNNINGTIME, "cn.donica.slcd.message");

    }

    /**
     * 接口回调函数，传入oid的值，输出app的运行状态及运行时间
     *
     * @param oid
     * @return
     */
    @Override
    public Variable getValue(String oid) {
        String packageName = getPackageName(oid);
        Boolean appState = getApplicationState(packageName);
        long appRunningTiem = getRunningTime(packageName);
        return new OctetString(appState + "," + appRunningTiem);
    }
}
