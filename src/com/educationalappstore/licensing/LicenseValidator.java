package com.educationalappstore.licensing;

import android.util.Log;

import com.educationalappstore.licensing.utils.MCrypt;

/**
 * Class to handle verify response data from EAS Licensing Service
 * @author Hoang
 *
 */
public class LicenseValidator {
	private String app_signature_key;
	private LicenseCheckerCallback mCallback;
	private LicensePolicy mPolicy;
	private MCrypt mCrypt;
	
	private static String eas_iv = "eduappstoreiv123";
	
	/**
	 * Constructor
	 * @param app_signature_key
	 * @param callback
	 * @param policy
	 */
	public LicenseValidator(String app_signature_key, LicenseCheckerCallback callback, LicensePolicy policy){
		this.app_signature_key = app_signature_key;
		this.mCallback = callback;
		this.mPolicy = policy;
		this.mCrypt = new MCrypt(LicenseValidator.eas_iv, app_signature_key);
	}
	
	/**
	 * 
	 * @param signature
	 * @return
	 */
	private boolean verifySignature(String signature){
		// decode signature using app signature key and compare with private key
		try {
			Log.d(LicenseChecker.TAG, "checking signature");
			String data = new String( mCrypt.decrypt( signature ) );
//			Log.d(LicenseChecker.TAG, "decrypted signature: " + data);
			if (data.contains(mPolicy.getAppKey())) {
				Log.i(LicenseChecker.TAG, "Correct Signature !");
				return true;
			} else {
				Log.w(LicenseChecker.TAG, "Wrong signature !");
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(LicenseChecker.TAG, "decrypt signature error: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * @param response_code
	 * @param message
	 * @param signature
	 */
	public void ValidateServerRespones(int response_code, String message, String signature){
		// if response code is allow or not allow
		if (response_code == LicensePolicy.ALLOW_ACCESS_CODE || response_code == LicensePolicy.DONT_ALLOW_ACCESS_CODE){
			// check signature
			// if signature correct
			if (this.verifySignature(signature)){
				// apply policy for respones data
				mPolicy.processServerResponse(response_code, message);
				// response result to client app
				if (mPolicy.allowAccess()){
					mCallback.allow(LicensePolicy.ALLOW_ACCESS_CODE, "");
				} else {
					mCallback.dontAllow(LicensePolicy.DONT_ALLOW_ACCESS_CODE, "");
				}
			}
			// if signature wrong
			 else {
				// return error to callback
				handleError(LicensePolicy.INVALID_SIGNATURE);
			}
		}
		// if response code is error then dont need to check signature
		else {
			if (message.equalsIgnoreCase("NETWORK_ERROR")){
				handleError(LicensePolicy.NETWORK_ERROR, message);
			} else if (message.equalsIgnoreCase("ERROR_USER_LOGIN")){
				handleError(LicensePolicy.ERROR_USER_LOGIN, message);
			} else {
				handleError(LicensePolicy.ERROR_CODE, message);
			}
		}

	}
	
	/**
	 * 
	 * @param code
	 */
	public void handleError(int code){
		handleError(code, "");
	}
	
	/**
	 * 
	 * @param code
	 * @param message
	 */
	public void handleError(int code, String message){
		// check cache
		// if code in cache is access
		if (mPolicy.getCodeFromCache() == LicensePolicy.ALLOW_ACCESS_CODE){
			// Allow access with error code
			mCallback.allow(code, message);
		} 
		// if cache not available
		else {
			mCallback.error(code, message);
		}
	}
}
