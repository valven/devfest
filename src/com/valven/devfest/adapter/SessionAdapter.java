package com.valven.devfest.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.listener.FavouriteListener;
import com.valven.devfest.model.Favourites;
import com.valven.devfest.model.Session;
import com.valven.devfest.model.Speaker;
import com.valven.devfest.util.Utils;

public class SessionAdapter extends BaseAdapter{

	private List<Session> mData;

	private Context mContext;

	private LayoutInflater mInflater;
	
	private OnClickListener mListener;

	static class ViewHolder {
		TextView startDate;
		TextView endDate;
		TextView title;
		TextView info;
		TextView speaker;
		TextView location;
		ImageView add;
	}

	public SessionAdapter(Context context, List<Session> data) {
		mContext = context;
		mData = data;
		mInflater = LayoutInflater.from(mContext);
	}
	
	public void setListener(OnClickListener listener){
		this.mListener = listener;
	}
	
	public void setData(List<Session> data){
		this.mData = data;
		super.notifyDataSetChanged();
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
			view = mInflater.inflate(R.layout.session_row, null);
			holder = new ViewHolder();
			holder.add = (ImageView)view.findViewById(R.id.add);
			holder.startDate = (TextView)view.findViewById(R.id.start_date);
			holder.endDate = (TextView)view.findViewById(R.id.end_date);
			holder.info = (TextView)view.findViewById(R.id.info);
			holder.title = (TextView)view.findViewById(R.id.title);
			holder.speaker = (TextView)view.findViewById(R.id.speaker);
			holder.location = (TextView)view.findViewById(R.id.location);
			holder.location.setVisibility(View.GONE);
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}
		final Session data = getItem(position);
		holder.startDate.setText(Utils.getTime(data.getBegin()));
		holder.endDate.setText(Utils.getTime(data.getEnd()));
		holder.info.setText(data.getInfo().replaceAll("\\<.*?>"," "));
		holder.title.setText(data.getTitle());
		
		List<String> speakers = new ArrayList<String>();
		List<String> ids = data.getSpeakers();
		if (ids!=null){
			for (String id:ids){
				Speaker speaker = DevFest.DATA.getSpeakers().get(id);
				if (speaker!=null){
					speakers.add(speaker.getName());
				}
			}
		}
		holder.speaker.setText(TextUtils.join(" & ", speakers));
		
		if (!data.isDisabled()){
			holder.add.setVisibility(View.VISIBLE);
			if (this.mListener==null){
				holder.add.setOnClickListener(new FavouriteListener(data));
			} else {
				holder.add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new FavouriteListener(data).onClick(v);
						mListener.onClick(v);
					}
				});
			}
		} else {
			holder.add.setVisibility(View.GONE);
		}
		if (Favourites.isSelected(data)){
			holder.add.setImageResource(R.drawable.delete);
		} else {
			holder.add.setImageResource(R.drawable.plus);
		}
		
		return view;
	}

}
