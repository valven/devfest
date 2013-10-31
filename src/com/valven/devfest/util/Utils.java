package com.valven.devfest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.valven.devfest.DevFest;

public class Utils {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

	private static final SimpleDateFormat PRINT_DATE_FORMAT = new SimpleDateFormat(
			"HH:mm", Locale.ENGLISH);
	
	public static String getTime(Date time) {
		if (time==null)
			return "00:00";
		else
			return PRINT_DATE_FORMAT.format(time);
	}
	
	public static Date parseDate(String str){
		Date date=null;
		try {
			date = DATE_FORMAT.parse(str);
		} catch (ParseException e) {
		}
		return date;
	}

	public static String nvl(String s1, String s2) {
		return TextUtils.isEmpty(s1) ? s2 : s1;
	}
	
	public static String getJsonCacheKey(Context context) {
		return DevFest.ARG_DATA + "_"
				+ Utils.getSelectedCity(context);
	}
	
	public static String getSelectedCity(Context context){
		return Utils.getSharedPreference(context, DevFest.ARG_CITY, "34");
	}
	
	public static String getFilePath(String base, String city){
		String language = Locale.getDefault().getLanguage();
		if (!"tr".equals(language)){
			language = "";
		}
		return String.format(base, city,language);
	}

	public static boolean checkInternetConnection(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			Log.w("utils", "Error occured while checking internet connection");
			return false;
		}
	}

	public static void setSharedPreference(Context context, String prefName,
			String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		if (value == null) {
			editor.remove(prefName);
		} else {
			editor.putString(prefName, value);
		}
		editor.commit();
	}

	public static String getSharedPreference(Context context, String prefName,
			String defaultValue) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String value = prefs.getString(prefName, defaultValue);
		return value;
	}
}
