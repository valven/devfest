package com.valven.devfest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.valven.devfest.R;
import com.valven.devfest.adapter.SessionAdapter;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.Favourites;
import com.valven.devfest.model.Session;

public class FavouritesActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_favourites, null);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.favourites);

		ListView list = (ListView) view.findViewById(R.id.favourite_list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				Session session = ((SessionAdapter) list.getAdapter())
						.getItem(position);
				Intent intent = new Intent(getApplicationContext(),
						SessionDetailActivity.class);
				Bundle extras = new Bundle();
				extras.putParcelable(SessionDetailActivity.ARG_DATA, session);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});
		list.setEmptyView(view.findViewById(R.id.empty));
		//
		((ViewGroup) findViewById(R.id.content)).addView(view);
	}

	@Override
	public void onResume() {
		super.onResume();

		ListView list = (ListView) findViewById(R.id.favourite_list);
		if (list.getAdapter() == null) {
			final SessionAdapter adapter = new SessionAdapter(
					getApplicationContext(), Favourites.load(this));
			adapter.setListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					adapter.setData(Favourites.load(getApplicationContext()));
				}
			});
			list.setAdapter(adapter);
		} else {
			((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
		}
	}
}
