package com.valven.devfest.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.valven.devfest.R;
import com.valven.devfest.model.Speaker;
import com.valven.devfest.util.ImageLoader;

public class SpeakerAdapter extends BaseAdapter{

	private List<Speaker> mData;

	private Context mContext;

	private LayoutInflater mInflater;

	private static class ViewHolder {
		private TextView name;
		private TextView info;
		private ImageView image;
	}

	public SpeakerAdapter(Context context, List<Speaker> data) {
		mContext = context;
		mData = data;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Speaker getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		View view = convertView;
//		if (view == null){
//			view = mInflater.inflate(R.layout.speaker_row, null);
//			holder = new ViewHolder();
//			holder.image = (ImageView)view.findViewById(R.id.image);
//			holder.info = (TextView)view.findViewById(R.id.info);
//			holder.name = (TextView)view.findViewById(R.id.name);
//			view.setTag(holder);
//		} else {
//			holder = (ViewHolder)view.getTag();
//			holder.image.setImageDrawable(null);
//		}
//		Speaker data = getItem(position);
//		holder.name.setText(data.getName());
//		holder.info.setText(data.getInfo());
//		
//		ImageLoader.getInstance().displayImage(data.getImage(), holder.image);
//		
//		return view;
//	}
	
public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
	View view = convertView;
	if (view == null){
		view = mInflater.inflate(R.layout.speaker_row, null);
		holder = new ViewHolder();
		holder.info = (TextView)view.findViewById(R.id.info);
		holder.name = (TextView)view.findViewById(R.id.name);
		view.setTag(holder);
	} else {
		holder = (ViewHolder)view.getTag();
	}
	Speaker data = getItem(position);
	holder.name.setText(data.getName());
	holder.info.setText(data.getInfo());
	
	return view;
}

}
