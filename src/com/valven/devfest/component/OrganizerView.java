package com.valven.devfest.component;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.valven.devfest.R;
import com.valven.devfest.model.Sponsor;
import com.valven.devfest.model.SponsorGroup;
import com.valven.devfest.util.ImageLoader;

public class OrganizerView extends LinearLayout {

	private SponsorGroup mData;

	public OrganizerView(Context context) {
		super(context);
		setOrientation(VERTICAL);
	}

	public void setData(SponsorGroup data) {
		mData = data;

		initComponents();
	}

	private void initComponents() {
		CustomTextView title = new CustomTextView(getContext());
		title.setText(mData.getName().toUpperCase(Locale.getDefault()));
		title.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		title.setCustomFont(getContext(), "Ubuntu-R.ttf");
		title.setTextSize(16);
		title.setTextColor(getResources().getColor(android.R.color.white));
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				8, getResources().getDisplayMetrics());
		title.setPadding((int)padding, (int) padding, 0, (int) padding);
		title.setBackgroundResource(R.drawable.sponsor_header);

		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		title.setLayoutParams(params);

		addView(title);
		Log.d("logo", padding+"");
		//add images of companies
		List<Sponsor> companies = mData.getSponsors();
		LayoutInflater inflater = LayoutInflater.from(getContext());
		for (int i = 0; i < companies.size(); i++) {
			Sponsor sponsor = companies.get(i);
			ImageView image = (ImageView)inflater.inflate(R.layout.organizer_logo, null);
			addView(image);
			
			final String url = sponsor.getUrl();
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(url));
					v.getContext().startActivity(browserIntent);
				}
			});
			
			ImageLoader.getInstance().displayImage(sponsor.getLogo(),
					image);
		}
	}

}
