package com.valven.devfest.listener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.valven.devfest.R;
import com.valven.devfest.model.Favourites;
import com.valven.devfest.model.Session;

public class FavouriteListener implements OnClickListener{
	private Session mSession;
	
	public FavouriteListener(Session session){
		mSession = session;
	}

	@Override
	public void onClick(View v) {
		ImageView img = (ImageView)v;
		if (Favourites.isSelected(mSession)){
			Favourites.remove(v.getContext(), mSession);
			img.setImageResource(R.drawable.plus);
		} else {
			Favourites.add(v.getContext(), mSession);
			img.setImageResource(R.drawable.delete);
		}
	}
}
