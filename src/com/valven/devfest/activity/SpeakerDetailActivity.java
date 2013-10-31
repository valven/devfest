package com.valven.devfest.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valven.devfest.R;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.listener.FavouriteListener;
import com.valven.devfest.model.Favourites;
import com.valven.devfest.model.Session;
import com.valven.devfest.model.Speaker;
import com.valven.devfest.util.ImageLoader;
import com.valven.devfest.util.Utils;

public class SpeakerDetailActivity extends BaseActivity {

	public static final String ARG_DATA = "speaker_data";

	private Speaker mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		View detailView = LayoutInflater.from(this).inflate(
				R.layout.activity_speaker_detail, null);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		mData = extras.getParcelable(ARG_DATA);
		if (mData != null) {
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(mData.getName());

			ImageLoader.getInstance().displayImage(mData.getImage(),
					(ImageView) detailView.findViewById(R.id.image));
			((TextView) detailView.findViewById(R.id.info)).setText(Html
					.fromHtml(mData.getInfo()));

		}

		((ViewGroup) findViewById(R.id.content)).addView(detailView);
	}

	@Override
	protected void onResume() {
		super.onResume();

		LinearLayout sessionList = (LinearLayout) findViewById(R.id.session_list);
		sessionList.removeAllViews();

		List<Session> sessions = mData.getSessions();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (Session session : sessions) {
			View view = inflater.inflate(R.layout.session_row, null);
			((TextView) view.findViewById(R.id.start_date)).setText(Utils
					.getTime(session.getBegin()));
			((TextView) view.findViewById(R.id.end_date)).setText(Utils
					.getTime(session.getEnd()));
			((TextView) view.findViewById(R.id.info)).setText(Html
					.fromHtml(session.getInfo()));
			((TextView) view.findViewById(R.id.title)).setText(session
					.getTitle());
			((TextView) view.findViewById(R.id.location)).setText(session
					.getLocation());
			((TextView) view.findViewById(R.id.speaker)).setVisibility(View.GONE);

			ImageView img = (ImageView) view.findViewById(R.id.add);
			img.setOnClickListener(new FavouriteListener(session));
			if (Favourites.isSelected(session)) {
				img.setImageResource(R.drawable.delete);
			} else {
				img.setImageResource(R.drawable.plus);
			}

			final Session data = session;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),
							SessionDetailActivity.class);
					Bundle extras = new Bundle();
					extras.putParcelable(SessionDetailActivity.ARG_DATA, data);
					intent.putExtras(extras);
					startActivity(intent);
				}
			});
			sessionList.addView(view);
		}
	}
}
