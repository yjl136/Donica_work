/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\WorkSpace\\Android_Studio\\Android\\Donica\\DManager\\src\\main\\aidl\\cn\\donica\\slcd\\dmanager\\aidl\\ILogTrapAidl.aidl
 */
package cn.donica.slcd.dmanager.aidl;
// Declare any non-default types here with import statements

public interface ILogTrapAidl extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.donica.slcd.dmanager.aidl.ILogTrapAidl
{
private static final java.lang.String DESCRIPTOR = "cn.donica.slcd.dmanager.aidl.ILogTrapAidl";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.donica.slcd.dmanager.aidl.ILogTrapAidl interface,
 * generating a proxy if needed.
 */
public static cn.donica.slcd.dmanager.aidl.ILogTrapAidl asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.donica.slcd.dmanager.aidl.ILogTrapAidl))) {
return ((cn.donica.slcd.dmanager.aidl.ILogTrapAidl)iin);
}
return new cn.donica.slcd.dmanager.aidl.ILogTrapAidl.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_LogRecord:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
java.lang.String _arg5;
_arg5 = data.readString();
this.LogRecord(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_LogWarn:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
java.lang.String _arg5;
_arg5 = data.readString();
this.LogWarn(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.donica.slcd.dmanager.aidl.ILogTrapAidl
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void LogRecord(java.lang.String packageName, java.lang.String appVersionName, java.lang.String location, java.lang.String tag, java.lang.String logContent, java.lang.String status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeString(appVersionName);
_data.writeString(location);
_data.writeString(tag);
_data.writeString(logContent);
_data.writeString(status);
mRemote.transact(Stub.TRANSACTION_LogRecord, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void LogWarn(java.lang.String level, int category, int type, java.lang.String logContent, java.lang.String location, java.lang.String tag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(level);
_data.writeInt(category);
_data.writeInt(type);
_data.writeString(logContent);
_data.writeString(location);
_data.writeString(tag);
mRemote.transact(Stub.TRANSACTION_LogWarn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_LogRecord = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_LogWarn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void LogRecord(java.lang.String packageName, java.lang.String appVersionName, java.lang.String location, java.lang.String tag, java.lang.String logContent, java.lang.String status) throws android.os.RemoteException;
public void LogWarn(java.lang.String level, int category, int type, java.lang.String logContent, java.lang.String location, java.lang.String tag) throws android.os.RemoteException;
}
