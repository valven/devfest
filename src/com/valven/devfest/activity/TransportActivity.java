package com.valven.devfest.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.Transportation;
import com.valven.devfest.util.ImageLoader;

public class TransportActivity extends BaseActivity {

	private static final String MAP_URL = "http://maps.googleapis.com/maps/api/staticmap?center=%1$s,%2$s&zoom=15&size=%3$sx%4$s&markers=%1$s,%2$s&sensor=false";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.transportation);

		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_transport, null);
		((ViewGroup) findViewById(R.id.content)).addView(view);

		final Transportation data = DevFest.DATA.getTransportation();

		final ImageView map = (ImageView) view.findViewById(R.id.map);
		ViewTreeObserver vto = map.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				int width = map.getMeasuredWidth();
				int height = map.getMeasuredHeight();

				String url = String.format(MAP_URL, data.getLatitude(),
						data.getLongitude(), width, height);
				Log.d("onPreDraw", map.getWidth() + " " + map.getHeight()
						+ "\n" + url);

				ImageLoader.getInstance().displayImage(url, map);

				map.getViewTreeObserver().removeOnPreDrawListener(this);

				return true;
			}
		});
		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchGoogleMaps(getApplicationContext(), data.getLatitude(),
						data.getLongitude());
			}
		});

		((TextView) view.findViewById(R.id.address)).setText(Html.fromHtml(data
				.getAddress()));

	}

	private void launchGoogleMaps(Context context, double latitude,
			double longitude) {
		try {
			String format = "geo:0,0?q=" + Double.toString(latitude) + ","
					+ Double.toString(longitude);
			Uri uri = Uri.parse(format);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			// maps application is unavailable, fallback to browser
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(String.format(
							"http://maps.google.com/maps?q=%1$s,%2$s&z=15",
							latitude, longitude)));
			startActivity(intent);
		}
	}
}
