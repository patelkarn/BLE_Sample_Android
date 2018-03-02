package jp.co.aandd.bleSimpleApp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ADSharedPreferences {
	
	private static ADSharedPreferences sharedInstance;
	public static SharedPreferences sharedPreferences;
	
	private static final String KEY_SHARED_PREFERENCES_NAME = "ANDMEDICAL";
	
	public static final String KEY_LOGIN_USER_NAME = "login_username";
	public static final String KEY_USER_ID = "userId";
	public static final String KEY_AUTH_TOKEN = "auth_token";
	public static final String KEY_MEASURING_UNIT = "measuringUnit";
	public static final String KEY_DEVICE_SET_UP_MODE = "deviceSetupMode";
	public static final String KEY_ADD_NEW_USER_VISIBLITY = "addnewuservisiblity";
	public static final String KEY_MANAGER_USER_VISIBILITY = "manageuservisibility";
	public static final String KEY_FROM_MANAGER_VISIBILITY = "frommanagevisibility";
	public static final String KEY_WEIGHT_SCALE_UNITS = "Weight_scale_unit";
	public static final String KEY_GROUP_NAME = "groupName";
	public static final String KEY_LOGIN_EMAIL = "login_email";
	public static final String KEY_LOGIN_PASSWORD = "login_password";
	public static final String KEY_REMENBER_ME = "rememberme";
	
	
	public static final String VALUE_WEIGHT_SCALE_UNITS_KG = "kg";
	public static final String VALUE_WEIGHT_SCALE_UNITS_LBS = "lbs";
	
	public static final String VALUE_DEVICE_SET_UP_MODE_BP = "bp";
	public static final String VALUE_DEVICE_SET_UP_MODE_WS = "weightscale";
	public static final String VALUE_DEVICE_SET_UP_MODE_TM = "thermomerter";
	
	public static final String VALUE_REMENBER_ME_YES = "yes";
	public static final String VALUE_REMENBER_ME_NO = "no";
	
	
	public static String DEFAULT_VALUE_WEIGHAT_SCALE = "weightscale";
	public static String DEFAULT_WEIGHT_SCALE_UNITS = ADSharedPreferences.VALUE_WEIGHT_SCALE_UNITS_LBS;

	
	public static ADSharedPreferences SharedInstance(Context context) {
		if(ADSharedPreferences.sharedInstance != null) {
			return ADSharedPreferences.sharedInstance;
		}
		ADSharedPreferences.sharedInstance = new ADSharedPreferences();
		ADSharedPreferences.sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return ADSharedPreferences.sharedInstance;
	}
	
	public static void releaseInstance() {
		ADSharedPreferences.sharedInstance = null;
	}

	public static String getString(String key, String defValue) {
		return ADSharedPreferences.sharedPreferences.getString(key, defValue);
	}
	
	public static void putString(String key,String value) {
		Editor editor = ADSharedPreferences.sharedPreferences.edit();
	    editor.putString(key, value);
	    editor.commit();
	}
}
