package com.valven.devfest.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.valven.devfest.R;
import com.valven.devfest.model.Session;
import com.valven.devfest.util.Utils;

public class EventAdapter extends BaseAdapter{

	private List<Session> mData;

	private Context mContext;

	private LayoutInflater mInflater;
	
	static class ViewHolder {
		TextView startDate;
		TextView title;
	}

	public EventAdapter(Context context, List<Session> data) {
		mContext = context;
		mData = data;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Session getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		View view = convertView;
		if (view == null){
			view = mInflater.inflate(R.layout.event_row, null);
			holder = new ViewHolder();
			holder.startDate = (TextView)view.findViewById(R.id.start_date);
			holder.title = (TextView)view.findViewById(R.id.title);
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}
		final Session data = getItem(position);
		holder.startDate.setText(Utils.getTime(data.getBegin()));
		holder.title.setText(data.getTitle());
		
		return view;
	}

}
