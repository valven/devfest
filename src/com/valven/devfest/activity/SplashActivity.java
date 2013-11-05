package com.valven.devfest.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.model.AppData;
import com.valven.devfest.task.Callback;
import com.valven.devfest.task.DataTask;
import com.valven.devfest.util.HttpUtils.HttpRequest;
import com.valven.devfest.util.Utils;

public class SplashActivity extends Activity implements Callback {

	private AsyncTask<Object, Void, String> mLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		if (DevFest.DATA == null) {
			if (!Utils.checkInternetConnection(this)) {
				String data = loadJSONFromCache();
				if (data != null) {
					Log.d("splash", "using data from cache");
					try {
						handleResponse(new JSONObject(data));
					} catch (JSONException e) {
					}
				} else {
					new AlertDialog.Builder(this)
							.setMessage(R.string.no_connection)
							.setCancelable(false)
							.setPositiveButton(android.R.string.ok,
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									}).show();
				}
			} else {
				String city = Utils.getSelectedCity(getApplicationContext());
				doRequest(city);
			}
		} else {
			switchActivity();
		}
	}

	private void doRequest(String city) {
		mLoader = new DataTask(this, this);
		String path = Utils.getFilePath(DataTask.DATA_URL, city);
		Log.d("path", path);
		mLoader.execute(path, HttpRequest.GET);
	}

	private void switchActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(intent);
	}

	@Override
	public void handleResponse(JSONObject response) {
		DevFest.DATA = new AppData(getApplicationContext(), response);
		switchActivity();
	}

	private String loadJSONFromCache() {
		String data = Utils.getSharedPreference(this, Utils.getJsonCacheKey(getApplicationContext()), null);
		return data;
	}
}
