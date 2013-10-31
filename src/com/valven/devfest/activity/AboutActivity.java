package com.valven.devfest.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.helper.ActivityHelper;

public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.about);

		View content = LayoutInflater.from(this).inflate(
				R.layout.activity_about, null);
		((TextView) content.findViewById(R.id.about)).setText(Html
				.fromHtml(DevFest.DATA.getAbout()));

		((ViewGroup) findViewById(R.id.content)).addView(content);
	}
}
