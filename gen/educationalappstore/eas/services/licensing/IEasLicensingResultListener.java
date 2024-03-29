/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Workspace EAS\\eas-licensing-library\\src\\educationalappstore\\eas\\services\\licensing\\IEasLicensingResultListener.aidl
 */
package educationalappstore.eas.services.licensing;
// service interface

public interface IEasLicensingResultListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements educationalappstore.eas.services.licensing.IEasLicensingResultListener
{
private static final java.lang.String DESCRIPTOR = "educationalappstore.eas.services.licensing.IEasLicensingResultListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an educationalappstore.eas.services.licensing.IEasLicensingResultListener interface,
 * generating a proxy if needed.
 */
public static educationalappstore.eas.services.licensing.IEasLicensingResultListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof educationalappstore.eas.services.licensing.IEasLicensingResultListener))) {
return ((educationalappstore.eas.services.licensing.IEasLicensingResultListener)iin);
}
return new educationalappstore.eas.services.licensing.IEasLicensingResultListener.Stub.Proxy(obj);
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
case TRANSACTION_checkUserAppPermission:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.checkUserAppPermission(_arg0, _arg1, _arg2);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements educationalappstore.eas.services.licensing.IEasLicensingResultListener
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
@Override public void checkUserAppPermission(int response_code, java.lang.String message, java.lang.String signature) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(response_code);
_data.writeString(message);
_data.writeString(signature);
mRemote.transact(Stub.TRANSACTION_checkUserAppPermission, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_checkUserAppPermission = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void checkUserAppPermission(int response_code, java.lang.String message, java.lang.String signature) throws android.os.RemoteException;
}
