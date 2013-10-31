package com.valven.devfest.helper;

import android.app.Activity;
import android.content.Intent;

import com.valven.devfest.DevFest;
import com.valven.devfest.activity.SplashActivity;

public class ActivityHelper {
	
	private Activity mActivity;
	
	public ActivityHelper(Activity activity) {
		super();
		this.mActivity = activity;
	}

	public boolean onCreate(){
		if (DevFest.DATA != null){
			return true;
		}
		Intent intent = new Intent(mActivity, SplashActivity.class);
		mActivity.finish();
		mActivity.startActivity(intent);
		
		return false;
	}
}
