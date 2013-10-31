package com.valven.devfest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.adapter.SessionAdapter;
import com.valven.devfest.helper.ActivityHelper;
import com.valven.devfest.model.Session;

public class SessionsActivity extends BaseActivity {

	private HallPagerAdapter mDemoCollectionPagerAdapter;
	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// avoid NPEs
		if (!new ActivityHelper(this).onCreate()) {
			return;
		}
		
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_sessions, null);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.sessions);

		((ViewGroup) findViewById(R.id.content)).addView(view);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		mDemoCollectionPagerAdapter = new HallPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		
		if (DevFest.DATA.getHalls().size()<2){
			findViewById(R.id.pager_title_strip).setVisibility(View.GONE);
		}
	}

	// Since this is an object collection, use a FragmentStatePagerAdapter,
	// and NOT a FragmentPagerAdapter.
	public class HallPagerAdapter extends FragmentStatePagerAdapter {
		public HallPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new SessionListFragment();
			Bundle args = new Bundle();
			// Our object is just an integer
			args.putInt(SessionListFragment.ARG_OBJECT, i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return DevFest.DATA.getHalls().size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return DevFest.DATA.getHalls().get(position).getName();
		}
	}

	// Instances of this class are fragments representing a single
	// object in our collection.
	public static class SessionListFragment extends Fragment {
		public static final String ARG_OBJECT = "object";

		private int mPosition;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// The last two arguments ensure LayoutParams are inflated
			// properly.
			View rootView = inflater.inflate(R.layout.fragment_hall, container,
					false);
			Bundle args = getArguments();
			mPosition = args.getInt(ARG_OBJECT);

			ListView list = (ListView) rootView.findViewById(R.id.session_list);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view,
						int position, long id) {
					Session session = ((SessionAdapter) list.getAdapter())
							.getItem(position);
					if (session.isDisabled())
						return;
					Intent intent = new Intent(getActivity(),
							SessionDetailActivity.class);
					Bundle extras = new Bundle();
					extras.putParcelable(SessionDetailActivity.ARG_DATA,
							session);
					intent.putExtras(extras);
					startActivity(intent);
				}
			});
			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();

			ListView list = (ListView) getView()
					.findViewById(R.id.session_list);
			if (list.getAdapter() == null) {
				SessionAdapter adapter = new SessionAdapter(getActivity(),
						DevFest.DATA.getHalls().get(mPosition).getSessions());
				list.setAdapter(adapter);
			} else {
				((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
			}
		}
	}

}
