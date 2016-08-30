// ILogTrapAidl.aidl
package cn.donica.slcd.dmanager.aidl;

// Declare any non-default types here with import statements

interface ILogTrapAidl {
     void LogRecord(String packageName,String appVersionName,String location,String tag,String logContent,String status);
     void LogWarn(String level, int category, int type, String logContent, String location,String tag);
}
