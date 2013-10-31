package com.valven.devfest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valven.devfest.DevFest;
import com.valven.devfest.R;
import com.valven.devfest.util.Utils;

public class Favourites {
	// private Collection<Session> sessions = new ArrayList<Session>();

	private static Map<String, Session> saved = new HashMap<String, Session>();

	private static final String KEY = "favourite_sessions";

	public static void add(Context context, Session session) {
		saved.put(session.getTitle(), session);
		persist(context);
		try {
			addCalendar(context, session);
		} catch (Exception e) {
		}
	}

	public static void remove(Context context, Session session) {
		saved.remove(session.getTitle());
		persist(context);
		try {
			deleteCalendar(context, session);
		} catch (Exception e) {
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void addCalendar(Context context, Session session) {
		ContentValues event = new ContentValues();
		// Calendar in which you want to add Evnet
		event.put("calendar_id", DevFest.CALENDAR_ID);
		// Title of the Event
		event.put("title", session.getTitle());
		// Description of the Event
		event.put("description", session.getInfo());
		// Venue of the Event
		event.put("eventLocation", session.getLocation());
		// Start Date of the Event
		event.put("dtstart", session.getBegin().getTime());
		// End Date of the Event
		event.put("dtend", session.getEnd().getTime());
		// Event is all day
		event.put("allDay", 0);
		// Get current timezone
		event.put("eventTimezone", TimeZone.getDefault().getID());
		// Set alarm on this Event
		event.put("hasAlarm", 1);
		Uri eventsUri = Uri.parse("content://com.android.calendar/events");
		ContentResolver cr = context.getContentResolver();
		Uri uri = cr.insert(eventsUri, event);
		if (uri != null) {
			// event is added
			long eventID = Long.parseLong(uri.getLastPathSegment());

			ContentValues reminders = new ContentValues();
			reminders.put("event_id", eventID);
			reminders.put("method", 1);
			reminders.put("minutes", 10);

			cr.insert(
					Uri.parse("content://com.android.calendar/reminders"),
					reminders);
			Toast.makeText(
					context,
					context.getString(R.string.added_calendar,
							session.getTitle()), Toast.LENGTH_SHORT).show();
		}
	}

	private static void deleteCalendar(Context context, Session session) {
		// getContentResolver().delete(path to the content, want to delete,
		// CONDITION, ARGUMENTS);
		// CONDITION + ARGUMENTS work as where condition to find a particular
		// event.
		int deleted = context.getContentResolver().delete(
				Uri.parse("content://com.android.calendar/events"),
				"calendar_id=? and title=? and description=? ",
				new String[] { String.valueOf(DevFest.CALENDAR_ID),
						session.getTitle(), session.getInfo() });
		if (deleted > 0) {
			Toast.makeText(
					context,
					context.getString(R.string.removed_calendar,
							session.getTitle()), Toast.LENGTH_SHORT).show();
		}

	}

	private static void persist(Context context) {
		Gson gson = new Gson();
		String json = gson.toJson(saved.values());
		Utils.setSharedPreference(context, KEY, json);
	}

	@SuppressWarnings("unchecked")
	public static List<Session> load(Context context) {
		if (saved != null && !saved.isEmpty()) {
			return new ArrayList<Session>(saved.values());
		} else {
			Gson gson = new Gson();
			String data = Utils.getSharedPreference(context, KEY, "");
			Collection<Session> sessions = (Collection<Session>) gson.fromJson(
					data, new TypeToken<Collection<Session>>() {
					}.getType());
			saved = new HashMap<String, Session>();
			if (sessions != null) {
				for (Session session : sessions) {
					saved.put(session.getTitle(), session);
				}
				return new ArrayList<Session>(sessions);
			} else {
				return new ArrayList<Session>();
			}
		}
	}

	public static boolean isSelected(Session session) {
		return saved.containsKey(session.getTitle());
	}
}
