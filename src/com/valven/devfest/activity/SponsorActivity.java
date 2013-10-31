package com.valven.devfest.activity;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.component.OrganizerView;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.SponsorGroup;

public class SponsorActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.sponsors);

		ViewGroup container = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.activity_sponsors, null);
		ViewGroup view = (ViewGroup) container.findViewById(R.id.sponsor_list);

		List<SponsorGroup> groups = DevFest.DATA.getSponsorGroups();

		for (int i = 0; i < groups.size(); i++) {
			OrganizerView organizer = new OrganizerView(this);

			ViewGroup.LayoutParams params = new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			organizer.setLayoutParams(params);

			organizer.setData(groups.get(i));
			view.addView(organizer);
		}

		((ViewGroup) findViewById(R.id.content)).addView(container);
	}
}
