package com.valven.devfest;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;

import com.valven.devfest.model.AppData;
import com.valven.devfest.model.Favourites;
import com.valven.devfest.util.ImageLoader;

//VALVEN'de calismak/staj yapmak istiyorsan CV'ni ik [at] valven.com adresine ilani gordugun yeri belirterek gonder
public class DevFest extends Application {
	
	public static AppData DATA;
	
	public static int CALENDAR_ID = 0;
	
	public static final String ARG_CITY = "arg_city";

	public static final String ARG_DATA = "json_data";

	public static final String ARG_TIME = "request_time";
	
	@Override
	public void onCreate(){
		ImageLoader.init(getApplicationContext());
		Favourites.load(getApplicationContext());
		
		Cursor cursor = getContentResolver().query(
				Uri.parse("content://com.android.calendar/calendars"),
				new String[] { "_id" }, null, null, null);
		// fetching calendars
		try {
			cursor.moveToFirst();
			CALENDAR_ID = cursor.getInt(0);
		} catch (Exception e){
		}finally{
			try {
				cursor.close();
			} catch (Exception e) {
			}
		}
	}
}
