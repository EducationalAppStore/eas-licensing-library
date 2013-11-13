/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Workspace EAS\\eas-licensing-library\\src\\educationalappstore\\eas\\services\\licensing\\IEasLicensingService.aidl
 */
package educationalappstore.eas.services.licensing;
// service interface

public interface IEasLicensingService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements educationalappstore.eas.services.licensing.IEasLicensingService
{
private static final java.lang.String DESCRIPTOR = "educationalappstore.eas.services.licensing.IEasLicensingService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an educationalappstore.eas.services.licensing.IEasLicensingService interface,
 * generating a proxy if needed.
 */
public static educationalappstore.eas.services.licensing.IEasLicensingService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof educationalappstore.eas.services.licensing.IEasLicensingService))) {
return ((educationalappstore.eas.services.licensing.IEasLicensingService)iin);
}
return new educationalappstore.eas.services.licensing.IEasLicensingService.Stub.Proxy(obj);
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
java.lang.String _arg0;
_arg0 = data.readString();
educationalappstore.eas.services.licensing.IEasLicensingResultListener _arg1;
_arg1 = educationalappstore.eas.services.licensing.IEasLicensingResultListener.Stub.asInterface(data.readStrongBinder());
this.checkUserAppPermission(_arg0, _arg1);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements educationalappstore.eas.services.licensing.IEasLicensingService
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
//sample method

@Override public void checkUserAppPermission(java.lang.String app_key, educationalappstore.eas.services.licensing.IEasLicensingResultListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(app_key);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_checkUserAppPermission, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_checkUserAppPermission = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
//sample method

public void checkUserAppPermission(java.lang.String app_key, educationalappstore.eas.services.licensing.IEasLicensingResultListener listener) throws android.os.RemoteException;
}
