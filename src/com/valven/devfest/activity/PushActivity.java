package com.valven.devfest.activity;

import android.content.Intent;
import android.os.Bundle;

import com.netmera.mobile.NetmeraActivity;

public class PushActivity extends NetmeraActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, SplashActivity.class);
		finish();
		startActivity(intent);
	}
}
