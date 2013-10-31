package com.valven.devfest.task;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.valven.devfest.DevFest;
import com.valven.devfest.model.Response;
import com.valven.devfest.util.HttpUtils;
import com.valven.devfest.util.HttpUtils.HttpRequest;
import com.valven.devfest.util.Utils;

public class DataTask extends AsyncTask<Object, Void, String> {

	private Callback mCallback;

	private Context mContext;

	// request cache timeout is 1 hour
	public static final long TIMEOUT = 1 * 60 * 60 * 1000L;

	public static String DATA_URL = "http://s3.amazonaws.com/valven/devfesttr13/devfest_%s%s_v1.json";

	public DataTask(Context context, Callback callback) {
		mContext = context;
		mCallback = callback;
	}

	private String loadJSONFromCache() {
		String data = Utils.getSharedPreference(mContext, getCacheKey(), null);
		return data;
	}

	private String getCacheKey() {
		return DevFest.ARG_DATA + "_" + Utils.getSelectedCity(mContext);
	}

	@Override
	protected String doInBackground(Object... params) {
		String url = (String) params[0];
		HttpRequest request = (HttpRequest) params[1];
		//
		// Response response = HttpUtils.sendRequest(url, null, null, request);
		// return response;

		String city = Utils.getSelectedCity(mContext);
		long prevRequestDate = Long.valueOf(
				Utils.getSharedPreference(mContext, DevFest.ARG_TIME
						+ city, "0")).longValue();
		if (System.currentTimeMillis() - prevRequestDate > DataTask.TIMEOUT) {
			Response response = HttpUtils.sendRequest(url, null, null,
					request);
			return response == null ? null : response.getContent();
		} else {
			String data = loadJSONFromCache();
			return data;
		}
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);
		
		if (response == null) {
			response = Utils.getSharedPreference(mContext,
					getCacheKey(), null);
			Log.d("task", "using data from cache");
		} else {
			Utils.setSharedPreference(mContext,
					DevFest.ARG_TIME + Utils.getSelectedCity(mContext),
					System.currentTimeMillis() + "");
		}
		
		handleResponse(response);
	}
	
	private void handleResponse(String response){
		JSONObject json=null;
		try {
			json = new JSONObject(response);
			Utils.setSharedPreference(mContext,
					getCacheKey(), response);

		} catch (Exception e) {
			e.printStackTrace();
			
			response = Utils.getSharedPreference(mContext,
					getCacheKey(), null);
			if (response != null){
				Log.w("splash", "json is invalid. using data from cache");
				handleResponse(response);
				return;
			} else {
				Log.w("splash", "json is invalid.");
			}
		}
		
		mCallback.handleResponse(json);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		mCallback.handleResponse(null);
	}

}
