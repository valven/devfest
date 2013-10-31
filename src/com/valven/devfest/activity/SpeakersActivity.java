package com.valven.devfest.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.adapter.SpeakerAdapter;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.Speaker;

public class SpeakersActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_speakers, null);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.speakers);

		ListView list = (ListView) view.findViewById(R.id.speaker_list);
		SpeakerAdapter adapter = new SpeakerAdapter(getApplicationContext(),
				new ArrayList<Speaker>(DevFest.DATA.getSpeakers().values()));
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				Speaker speaker = ((SpeakerAdapter) list.getAdapter())
						.getItem(position);
				Intent intent = new Intent(getApplicationContext(),
						SpeakerDetailActivity.class);
				Bundle extras = new Bundle();
				extras.putParcelable(SpeakerDetailActivity.ARG_DATA, speaker);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});

		((ViewGroup) findViewById(R.id.content)).addView(view);
	}
}
