package z.hol.android.mc.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManager {
	public static final String PREFERENCE = "mc";
	public static final String PREF_PASS = "pass";

	private SharedPreferences preferences;
	private static SettingManager mSettingManager;
	
	private SettingManager(Context context){
		preferences = context.getSharedPreferences(PREFERENCE, 0);
	}
	
	public static SettingManager get(Context context){
		if (mSettingManager == null){
			mSettingManager = new SettingManager(context);
		}
		return mSettingManager;
	}
	
	public void setPass(String pass){
		preferences.edit().putString(PREF_PASS, pass).commit();
	}
	
	public String getPass(){
		return preferences.getString(PREF_PASS, "000");
	}
	
}
