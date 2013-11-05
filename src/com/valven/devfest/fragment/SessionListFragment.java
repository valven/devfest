package com.valven.devfest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.valven.devfest.R;
import com.valven.devfest.activity.SessionDetailActivity;
import com.valven.devfest.adapter.EventAdapter;
import com.valven.devfest.adapter.SessionAdapter;
import com.valven.devfest.model.Hall;
import com.valven.devfest.model.Session;

// Instances of this class are fragments representing a single
// object in our collection.
public class SessionListFragment extends Fragment {
	public static final String ARG_OBJECT = "object";

	private Hall mData;
	
	public SessionListFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		mData = args.getParcelable(ARG_OBJECT);
		
		View rootView = inflater.inflate(R.layout.fragment_hall, null);
		ListView list = (ListView) rootView
				.findViewById(R.id.session_list);
		BaseAdapter adapter;
		if (mData.isShort()){
			adapter = new EventAdapter(getActivity(),
					mData.getSessions());
		} else {
			adapter = new SessionAdapter(getActivity(),
					mData.getSessions());
			
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
		}
		list.setAdapter(adapter);

		

		return rootView;
	}

}