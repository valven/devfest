package com.valven.devfest.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.component.TransparentProgressDialog;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.AppData;
import com.valven.devfest.task.Callback;
import com.valven.devfest.task.DataTask;
import com.valven.devfest.util.HttpUtils.HttpRequest;
import com.valven.devfest.util.Utils;

public class MainActivity extends BaseActivity implements Callback {

	private AsyncTask<Object, Void, String> mLoader;
	private TransparentProgressDialog mProgressDialog;
	private Button mSelected;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		LayoutInflater.from(this).inflate(R.layout.activity_main,
				(ViewGroup) findViewById(R.id.content), true);

		findViewById(R.id.leftIcon).setVisibility(View.INVISIBLE);
		findViewById(R.id.leftSplitter).setVisibility(View.INVISIBLE);
		findViewById(R.id.header_logo).setVisibility(View.VISIBLE);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.title_main);

		findViewById(R.id.sessions).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SessionsActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.speakers).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SpeakersActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.about).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.transportation).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								TransportActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.sponsors).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SponsorActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.favourites).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						FavouritesActivity.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.button_ankara).setOnClickListener(mSelectListener);
		// findViewById(R.id.button_eskisehir).setOnClickListener(mSelectListener);
		// TODO complete data, enable button
		findViewById(R.id.button_eskisehir).setEnabled(false);
		findViewById(R.id.button_istanbul).setOnClickListener(mSelectListener);

		String city = Utils.getSelectedCity(getApplicationContext());
		selectCity(city);

	}

	private OnClickListener mSelectListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button)v;
			String city;
			if (getString(R.string.eskisehir).equals(btn.getText())){
				city = "26";
			} else if (getString(R.string.ankara).equals(btn.getText())){
				city = "06";
			} else {
				city = "34";
			}
			if (canChangeCity(city)){
				if (mSelected != null) {
					deselectCity(mSelected);
					mSelected = null;
				}
				
				selectCity(city);
				fetchData(city);
			} else {
				new AlertDialog.Builder(MainActivity.this)
						.setMessage(R.string.city_connection)
						.setCancelable(true)
						.setPositiveButton(android.R.string.ok, null).show();
			}
		}
	};
	
	private boolean canChangeCity(String city){
		if (Utils.checkInternetConnection(getApplicationContext())){
			return true;
		}
		String key = DevFest.ARG_DATA+"_"+city;
		String data = Utils.getSharedPreference(this, key,
				null);
		if (data == null){
			return false;
		} else {
			return true;
		}
	}

	private void selectCity(String city) {
		Button btn=null;
		if ("26".equals(city)) {
			btn = (Button)findViewById(R.id.button_eskisehir);
			((TextView) findViewById(R.id.title))
			.setText(R.string.devfest_eskisehir);
		} else if ("06".equals(city)) {
			btn = (Button)findViewById(R.id.button_ankara);
			((TextView) findViewById(R.id.title))
					.setText(R.string.devfest_ankara);
		} else {
			btn = (Button)findViewById(R.id.button_istanbul);
			((TextView) findViewById(R.id.title))
			.setText(R.string.devfest_istanbul);
		}
		btn.setBackgroundResource(R.drawable.sehir_bg);
		btn.setTextColor(getResources().getColor(R.color.city_active));
		Utils.setSharedPreference(getApplicationContext(), DevFest.ARG_CITY,
				city);
		
		mSelected = btn;
		
	}
	
	private void deselectCity(Button btn) {
		btn.setBackgroundResource(0);
		btn.setTextColor(getResources().getColor(R.color.city_deactive));
	}

	private void fetchData(String city) {
		mProgressDialog = TransparentProgressDialog.show(this, null, null, true, true);
		mProgressDialog.show();
		
		mLoader = new DataTask(this, this);
		String path = Utils.getFilePath(DataTask.DATA_URL, city);
		Log.d("path", path);
		mLoader.execute(path, HttpRequest.GET);
	}

	@Override
	public void handleResponse(JSONObject response) {
		if (mProgressDialog!=null){
			try{
				mProgressDialog.dismiss();
			}catch(Throwable t){
			}
			mProgressDialog = null;
		}
		DevFest.DATA = new AppData(getApplicationContext(), response);
		
	}
}
