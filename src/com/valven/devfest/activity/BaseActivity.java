package com.valven.devfest.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.analytics.tracking.android.EasyTracker;
import com.netmera.mobile.Netmera;
import com.netmera.mobile.NetmeraDeviceDetail;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraPushService;
import com.valven.devfest.R;

public class BaseActivity extends FragmentActivity {

	static final String API_KEY = "ZGoweEpuVTlOVEkyWlRnd056bGxOR0l3TW1ZMFl6UTJOVE0xTnpKaEptRTlaR1YyWm1WemRDWQ==";
	static final String PROJECT_ID = "730435333848";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			boolean registered = NetmeraPushService
					.isRegistered(getApplicationContext());
			Log.d("base", registered + "");

			Netmera.init(this, API_KEY);
			
			try {
				NetmeraDeviceDetail deviceDetail = new NetmeraDeviceDetail(
						getApplicationContext(), PROJECT_ID, PushActivity.class);
				NetmeraPushService.register(deviceDetail);
			} catch (NetmeraException e) {
				e.printStackTrace();
				Log.d("netmera", "NetmeraPushService : " + e.toString());
			}
		} catch (Throwable e) {
		}

		setContentView(R.layout.activity_base);

		findViewById(R.id.leftIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStart(this);
	}
}
