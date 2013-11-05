package com.valven.devfest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.valven.devfest.R;
import com.valven.devfest.util.Utils;

public class AppData {
	private List<Event> mEvents;
	private List<Hall> mHalls;
	private Map<String, Speaker> mSpeakers;
	private Map<String, List<Session>> mSpeakerSessions;
	private List<SponsorGroup> mSponsorGroups;
	private Transportation mTransportation;
	private String mAbout;

	public AppData() {
	}

	public AppData(Context context, JSONObject json) {
		if (json.has("type") && "2".equals(json.optString("type"))) {
			parseSessions_v2(context, json);
		} else {
			parseSessions_v1(context, json);
		}

		mSpeakers = new HashMap<String, Speaker>();
		JSONObject speakers = json.optJSONObject("speakers");
		Iterator it = speakers.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			JSONObject spk = speakers.optJSONObject(key);
			Speaker speaker = new Speaker();
			speaker.setKey(key);
			speaker.setImage(spk.optString("image"));
			speaker.setName(spk.optString("name"));
			speaker.setInfo(spk.optString("info"));
			speaker.setSessions(mSpeakerSessions.get(key));

			mSpeakers.put(key, speaker);
		}

		mSponsorGroups = new ArrayList<SponsorGroup>();
		JSONArray sponsors = json.optJSONArray("sponsors");
		for (int i = 0; i < sponsors.length(); i++) {
			JSONObject gObj = sponsors.optJSONObject(i);
			SponsorGroup group = new SponsorGroup();
			group.setName(gObj.optString("title"));

			JSONArray comps = gObj.optJSONArray("companies");
			List<Sponsor> sponsorList = new ArrayList<Sponsor>();
			for (int j = 0; j < comps.length(); j++) {
				JSONObject comp = comps.optJSONObject(j);
				Sponsor sponsor = new Sponsor();
				sponsor.setName("");
				sponsor.setLogo(comp.optString("image"));
				sponsor.setUrl(comp.optString("url"));
				sponsorList.add(sponsor);
			}
			group.setSponsors(sponsorList);

			mSponsorGroups.add(group);
		}

		mTransportation = new Transportation();
		JSONObject location = json.optJSONObject("location");
		mTransportation.setAddress(location.optString("title") + "<br/>"
				+ location.optString("detail"));
		mTransportation.setLatitude(location.optDouble("lat"));
		mTransportation.setLongitude(location.optDouble("long"));

		mAbout = json.optString("about");
	}

	private void parseSessions_v1(Context context,
			JSONObject json) {
		String tba = context.getString(R.string.tba);
		JSONArray halls = json.optJSONArray("halls");
		mHalls = new ArrayList<Hall>();
		for (int i = 0; i < halls.length(); i++) {
			Hall hall = new Hall();
			hall.setName(context.getString(R.string.hall, halls.optString(i)));
			hall.setSessions(new ArrayList<Session>());
			mHalls.add(hall);
		}

		JSONArray sessions = json.optJSONArray("sessions");

		mSpeakerSessions = new HashMap<String, List<Session>>();

		for (int i = 0; i < sessions.length(); i++) {
			JSONObject sess = sessions.optJSONObject(i);
			JSONArray session_halls = sess.optJSONArray("halls");
			Date begin = Utils.parseDate(sess.optString("begin"));
			Date end = Utils.parseDate(sess.optString("end"));
			boolean disabled = sess.optBoolean("disabled", false);
			for (int j = 0; j < session_halls.length(); j++) {
				JSONObject obj = session_halls.optJSONObject(j);
				if (obj != null) {
					Session session = new Session();
					session.setBegin(begin);
					session.setEnd(end);
					session.setDisabled(disabled);

					Hall hall = mHalls.get(j);
					hall.getSessions().add(session);
					session.setLocation(hall.getName());
					session.setTitle(Utils.nvl(obj.optString("title"), tba));
					session.setInfo(Utils.nvl(obj.optString("detail"), tba));

					JSONArray speakers = obj.optJSONArray("speakers");
					List<String> speakerList = new ArrayList<String>();
					for (int k = 0; k < speakers.length(); k++) {
						String name = speakers.optString(k);
						speakerList.add(name);

						List<Session> sessionList = mSpeakerSessions.get(name);
						if (sessionList == null) {
							sessionList = new ArrayList<Session>();
						}
						sessionList.add(session);
						mSpeakerSessions.put(name, sessionList);
					}
					session.setSpeakers(speakerList);
				}
			}
		}
	}

	private void parseSessions_v2(Context context,
			JSONObject json) {
		mSpeakerSessions = new HashMap<String, List<Session>>();
		
		mEvents = new ArrayList<Event>();
		JSONArray events = json.optJSONArray("events");
		for (int i=0;i<events.length();i++){
			Event event = new Event();
			JSONObject eventObj = events.optJSONObject(i);
			
			event.setName(eventObj.optString("name"));
			
			JSONArray hallArray = eventObj.optJSONArray("halls");
			if (hallArray!=null){
				List<Hall> halls = parseHalls(context, hallArray);
				event.setHalls(halls);
			}
			mEvents.add(event);
		}
	}

	private List<Hall> parseHalls(Context context, JSONArray hallArray){
		String tba = context.getString(R.string.tba);
		List<Hall> halls = new ArrayList<Hall>();
		for (int i = 0; i < hallArray.length(); i++) {
			JSONObject hallObj = hallArray.optJSONObject(i);
			Hall hall = new Hall();
			hall.setName(hallObj.optString("name"));
			hall.setShort(hallObj.optBoolean("short", false));

			JSONArray sessionArray = hallObj.optJSONArray("sessions");
			Log.d("sessionArray", sessionArray+"");
			if (sessionArray != null) {
				List<Session> sessions = new ArrayList<Session>();
				for (int j = 0; j < sessionArray.length(); j++) {
					JSONObject sess = sessionArray.optJSONObject(j);

					Date begin = Utils.parseDate(sess.optString("begin"));
					Date end = Utils.parseDate(sess.optString("end"));
					boolean disabled = sess.optBoolean("disabled", false);

					Session session = new Session();
					session.setBegin(begin);
					session.setEnd(end);
					session.setDisabled(disabled);

					sessions.add(session);

					session.setLocation(hall.getName());
					session.setTitle(Utils.nvl(sess.optString("title"), tba));
					session.setInfo(Utils.nvl(sess.optString("detail"), tba));

					JSONArray speakers = sess.optJSONArray("speakers");
					if (speakers != null) {
						List<String> speakerList = new ArrayList<String>();
						for (int k = 0; k < speakers.length(); k++) {
							String name = speakers.optString(k);
							speakerList.add(name);

							List<Session> sessionList = mSpeakerSessions
									.get(name);
							if (sessionList == null) {
								sessionList = new ArrayList<Session>();
							}
							sessionList.add(session);
							mSpeakerSessions.put(name, sessionList);
						}
						session.setSpeakers(speakerList);
					}
				}
				hall.setSessions(sessions);
			}
			halls.add(hall);
		}
		return halls;
	}

	public Map<String, Speaker> getSpeakers() {
		return mSpeakers;
	}

	public Transportation getTransportation() {
		return mTransportation;
	}

	public String getAbout() {
		return mAbout;
	}

	public List<SponsorGroup> getSponsorGroups() {
		return mSponsorGroups;
	}

	public List<Hall> getHalls() {
		return mHalls;
	}

	public List<Event> getEvents() {
		return mEvents;
	}

}
