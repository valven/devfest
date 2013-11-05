package com.valven.devfest.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.valven.devfest.fragment.SessionListFragment;
import com.valven.devfest.model.Hall;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class HallPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<Hall> mData;
	
	public HallPagerAdapter(FragmentManager fm, List<Hall> data) {
		super(fm);
		
		mData = data;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new SessionListFragment();
		Bundle args = new Bundle();
		args.putParcelable(SessionListFragment.ARG_OBJECT, mData.get(i));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mData.get(position).getName();
	}
}