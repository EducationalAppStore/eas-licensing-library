package com.educationalappstore.licensing;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Policy used by {@link LicenseChecker} to determine whether a user should have
 * access to the application.
 * @author Hoang
 *
 */
public class LicensePolicy {
	public static final int ALLOW_ACCESS_CODE = 1;
	public static final int DONT_ALLOW_ACCESS_CODE = 0;
	public static final int ERROR_CODE = 2;

	public static final int CACHE_NOT_AVAILABLE = -1;
	
	public static final int CACHE_EXPIRED = 3;
	public static final int INVALID_SIGNATURE = 4;
	public static final int UNABLE_TO_BIND_SERVICE = 5;
	public static final int BIND_SERVICE_PERMISSION_ERROR = 6;
	public static final int SERVICE_EXECUTE_CRASH = 7;
	public static final int NETWORK_ERROR = 8;
	public static final int ERROR_USER_LOGIN = 9;
	
	private Context mContext;
	private SharedPreferences mSharedPreferences;

	private String app_key;
	
	private String RESPONSE_KEY;
	private String EXPIRED_KEY;
	
	public static final long SECOND = 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	
	// by default, the cache of the response will be kept in a week
	private static final long DEFAULT_TIME_EXPIRED = 7 * DAY;
	
	private long expired_time;  
	
	public LicensePolicy(Context context, String app_key){
		mContext = context;
		mSharedPreferences = context.getSharedPreferences(LicensePolicy.class.getName(), mContext.MODE_PRIVATE);
		
		this.app_key = app_key;
		
		this.RESPONSE_KEY = app_key + "_response_code";
		this.EXPIRED_KEY = app_key + "_expired_time";
		
		this.expired_time = LicensePolicy.DEFAULT_TIME_EXPIRED;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAppKey(){
		return app_key;
	}
	
	/**
	 * get current cache expired from shared preferences
	 * @return
	 */
	private long getCacheExpiredTime(){
		return mSharedPreferences.getLong(EXPIRED_KEY, 0);
	}
	
	/**
	 * check if current cache is expired.
	 * If no cache found then return expired
	 * @return
	 */
	public boolean isCachedExpired(){
		// get current date in unix time stamp
		long today = new Date().getTime();
		
		return (today > getCacheExpiredTime());
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodeFromCache(){
		return mSharedPreferences.getInt(RESPONSE_KEY, CACHE_NOT_AVAILABLE);
	}
	
	/**
	 * 
	 */
	public void clearCache(){
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.apply();
	}
	
	/**
	 * Process response from server
	 * Save result to Shared Preferences if status is allow
	 * 
	 * @param status
	 * @param message
	 */
	public void processServerResponse(int response_code, String message){
		// only save in cache when respinse code is allow
		if (response_code == ALLOW_ACCESS_CODE){
			// recalculate expired date
			long today = new Date().getTime();
			long expired_date = today + this.expired_time;
//			long expired_date = today + MINUTE;
			
			Log.i(LicenseChecker.TAG, "New cache will expired on " + new Date(expired_date).toGMTString());
			
			// overide old response with new response
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putInt(RESPONSE_KEY, response_code);
			// extends expired time of current response
			editor.putLong(EXPIRED_KEY, expired_date);
			editor.apply();
		}
	}
	
	/**
	 * determine if user has permission to access the app
	 * @return
	 */
	public boolean allowAccess(){
		// get response code in cache
		int code = mSharedPreferences.getInt(RESPONSE_KEY, CACHE_NOT_AVAILABLE);
		long expiredDate = mSharedPreferences.getLong(EXPIRED_KEY, 0);
		
		// only allow access if cache is not expired 
		if (code == ALLOW_ACCESS_CODE && expiredDate >= new Date().getTime())
			return true;
		
		return false;
	}
}
