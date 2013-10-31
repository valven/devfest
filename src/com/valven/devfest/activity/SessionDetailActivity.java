package com.valven.devfest.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.listener.FavouriteListener;
import com.valven.devfest.model.Favourites;
import com.valven.devfest.model.Session;
import com.valven.devfest.model.Speaker;
import com.valven.devfest.util.ImageLoader;
import com.valven.devfest.util.Utils;

public class SessionDetailActivity extends BaseActivity {

	public static final String ARG_DATA = "session_data";

	private Session mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_session_detail, null);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		mData = extras.getParcelable(ARG_DATA);
		if (mData != null) {
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(mData.getTitle());

			TextView info = ((TextView) view.findViewById(R.id.info));
			info.setMovementMethod(LinkMovementMethod.getInstance());
			info.setText(Html.fromHtml(mData.getInfo()));

			((TextView) view.findViewById(R.id.start_date)).setText(Utils
					.getTime(mData.getBegin()));
			((TextView) view.findViewById(R.id.end_date)).setText(Utils
					.getTime(mData.getEnd()));

			ImageView add = (ImageView) view.findViewById(R.id.add);
			add.setOnClickListener(new FavouriteListener(mData));
			if (Favourites.isSelected(mData)) {
				add.setImageResource(R.drawable.delete);
			} else {
				add.setImageResource(R.drawable.plus);
			}
		}

		((ViewGroup) findViewById(R.id.content)).addView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();

		LinearLayout sessionList = (LinearLayout) findViewById(R.id.speaker_list);
		sessionList.removeAllViews();

		List<String> speakers = mData.getSpeakers();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (String spk : speakers) {
			final Speaker speaker = DevFest.DATA.getSpeakers().get(spk);
			if (speaker!=null){
				View view = inflater.inflate(R.layout.speaker_row, null);
				((TextView) view.findViewById(R.id.info)).setText(Html
						.fromHtml(speaker.getInfo()));
				((TextView) view.findViewById(R.id.name))
						.setText(speaker.getName());
				ImageLoader.getInstance().displayImage(speaker.getImage(), ((ImageView) view.findViewById(R.id.image)));
	
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								SpeakerDetailActivity.class);
						Bundle extras = new Bundle();
						extras.putParcelable(SpeakerDetailActivity.ARG_DATA, speaker);
						intent.putExtras(extras);
						startActivity(intent);
					}
				});
				sessionList.addView(view);
			}
		}
	}
}
