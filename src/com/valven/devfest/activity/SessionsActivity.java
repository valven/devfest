package com.valven.devfest.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.adapter.HallPagerAdapter;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.Event;
import com.valven.devfest.model.Hall;

public class SessionsActivity extends BaseActivity {

	private boolean mTabbed;
	private TabHost mTabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.sessions);

		View view;
		if (DevFest.DATA.getHalls() != null
				|| DevFest.DATA.getEvents().size() < 2) {
			mTabbed = false;
			view = LayoutInflater.from(this).inflate(
					R.layout.activity_sessions, null);
		} else {
			mTabbed = true;
			view = LayoutInflater.from(this).inflate(
					R.layout.activity_sessions_tabbed, null);
		}

		((ViewGroup) findViewById(R.id.content)).addView(view);
    }


	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (mTabbed) {
			initTabs();
		} else {
			List<Hall> halls = DevFest.DATA.getHalls();
			if (halls==null){
				halls = DevFest.DATA.getEvents().get(0).getHalls();
			}
			// ViewPager and its adapters use support library
			// fragments, so use getSupportFragmentManager.
			HallPagerAdapter pagerAdapter = new HallPagerAdapter(getSupportFragmentManager(), halls);
			ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
			viewPager.setAdapter(pagerAdapter);

			if (halls.size() < 2) {
				findViewById(R.id.pager_title_strip).setVisibility(View.GONE);
			}
		}
	}

	protected void initTabs() {
		mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost.setup();

		int i=0;
		LayoutInflater inflater = LayoutInflater.from(this);
		for (Event event:DevFest.DATA.getEvents()){
			ViewPager viewPager = (ViewPager)inflater.inflate(R.layout.activity_sessions,
					null);
			viewPager.setId(++i);
			HallPagerAdapter pagerAdapter = new HallPagerAdapter(
					getSupportFragmentManager(), event.getHalls());
			viewPager.setAdapter(pagerAdapter);
			setupTab(mTabHost, viewPager,
					event.getName());
		}

	}

	private static void setupTab(TabHost mTabHost, final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					public View createTabContent(String tag) {
						return view;
					}
				});
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

}
