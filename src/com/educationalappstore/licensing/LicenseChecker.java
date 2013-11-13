package com.educationalappstore.licensing;

import educationalappstore.eas.services.licensing.IEasLicensingService;
import educationalappstore.eas.services.licensing.IEasLicensingResultListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Class to handle EAS license checker service 
 * @author Hoang
 *
 */
public class LicenseChecker implements ServiceConnection{
	public static final String TAG = "EasLicenseChecker";

	private Context mContext;
	
	private String app_key;
	private String app_signature_key;
	
	private LicensePolicy mPolicy; 
	
	private LicenseCheckerCallback mCallback;
	
	private static String SERVICENAME = "educationalappstore.eas.services.licensing.LicenseCheckerService";
	
	LicenseValidator mValidator;
	
    /** The primary interface we will be calling on the service. */
    private IEasLicensingService mService = null;

    /**
     * This implementation is used to receive callbacks from the remote
     * service.
     */
    private IEasLicensingResultListener mServiceCallback = new IEasLicensingResultListener.Stub() {
        /**
         * This is called by the remote service regularly to tell us about
         * new values.  Note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */
		@Override
		public void checkUserAppPermission(int response_code, String message,
				String signature) throws RemoteException {
			String response = "EAS Licensing Service Response: code: " + String.valueOf(response_code) + " --- message: " + message;
			Log.d(TAG, response);
			
			// process response code
			mValidator.ValidateServerRespones(response_code, message, signature);
			cleanupService();
		}
	}; 
	
	/**
	 * 
	 * @param context
	 * @param app_private_key
	 * @param app_signature_key
	 * @param callback
	 */
	public LicenseChecker(Context context, String app_private_key, String app_signature_key, LicenseCheckerCallback callback){
		this.mContext = context;
		this.app_key = app_private_key;
		this.app_signature_key = app_signature_key;		

		this.mCallback = callback;
		this.mPolicy =  new LicensePolicy(context, app_key);
		
		mValidator = new LicenseValidator(app_signature_key, mCallback, mPolicy);

	}
	
	/**
	 * 
	 */
	public void CheckAccess(){
		// check license status in cache. If it is still valid then use it
		if (mPolicy.allowAccess()){
			Log.d(LicenseChecker.TAG, "Allow access from cache");
			mCallback.allow(LicensePolicy.ALLOW_ACCESS_CODE, "");
		} 
		// try to get license status again
		else {
			// if service is not binded
			if (mService == null){
				try {
					// bind service
					Log.d(TAG, "Binding EAS LicenseCheckerService");
					boolean bindResult = mContext.bindService(
							new Intent(LicenseChecker.SERVICENAME), 
							this, 
							Context.BIND_AUTO_CREATE
							);
					// if bind success
					if (bindResult){
						Log.d(TAG, "Bind success");
					} 
					// if bind fail
					else {
						Log.e(TAG, "Could not bind  EAS LicenseCheckerService");
						mValidator.handleError(LicensePolicy.UNABLE_TO_BIND_SERVICE, "Could not bind  EAS LicenseCheckerService");
						cleanupService();
					}
	            } catch (SecurityException e) {
	            	mValidator.handleError(LicensePolicy.BIND_SERVICE_PERMISSION_ERROR, "Missing permission to bind EAS Licensing Service");
					cleanupService();
	            }
			}
			// if service is binded
			else {
				// cal service action
				runCheck();
			}
		}
	}
	
	private void runCheck(){
        // We want to monitor the service for as long as we are
        // connected to it.
        try {
            mService.checkUserAppPermission(app_key, mServiceCallback);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even
            // do anything with it; we can count on soon being
            // disconnected (and then reconnected if it can be restarted)
            // so there is no need to do anything here.
            Log.d(TAG, "EAS Licensing Service has crashed before we could even do anything with it");
            mValidator.handleError(LicensePolicy.SERVICE_EXECUTE_CRASH);
        }
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
        // This is called when the connection with the service has been
        // established, giving us the service object we can use to
        // interact with the service.
		mService = IEasLicensingService.Stub.asInterface(service);
        Log.d(TAG, "Connected to EAS Licensing Service");
        runCheck();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
        // This is called when the connection with the service has been
        // unexpectedly disconnected -- that is, its process crashed.
        cleanupService();
        Log.d(TAG, "Connection to EAS Licensing Service unexpectedly disconnected");
	}
	
	/**
	 * Clear all current cache to force verifying application again
	 */
	public void clearCache(){
		mPolicy.clearCache();
	}
	
    /** Unbinds service if necessary and removes reference to it. */
    private void cleanupService() {
        Log.d(TAG, "Unbinding EAS Licensing Service");
        if (mService != null) {
            try {
                mContext.unbindService(this);
                Log.i(TAG, "Unbinded EAS Licensing Service");
            } catch (IllegalArgumentException e) {
                // Somehow we've already been unbound. This is a non-fatal
                // error.
                Log.e(TAG, "Unable to unbind from licensing service (already unbound)");
            }
            mService = null;
        } else {
            Log.w(TAG, "No EAS Licensing Service availbale to be unbind");
        }
    }

    /**
     * Inform the library that the context is about to be destroyed, so that any
     * open connections can be cleaned up.
     * <p>
     * Failure to call this method can result in a crash under certain
     * circumstances, such as during screen rotation if an Activity requests the
     * license check or when the user exits the application.
     */
    public synchronized void onDestroy() {
        cleanupService();
        //mHandler.getLooper().quit();
    }
}
