package com.valven.devfest.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Speaker implements Parcelable {
	private String key;
	private String name;
	private String image;
	private String info;
	private List<Session> sessions;

	public Speaker() {

	}

	public Speaker(Parcel in) {
		key = in.readString();
		name = in.readString();
		image = in.readString();
		info = in.readString();
		sessions = new ArrayList<Session>();
		in.readTypedList(sessions, Session.CREATOR);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(name);
		dest.writeString(image);
		dest.writeString(info);
		dest.writeTypedList(sessions);
	}

	public static final Parcelable.Creator<Speaker> CREATOR = new Parcelable.Creator<Speaker>() {
		public Speaker createFromParcel(Parcel in) {
			return new Speaker(in);
		}

		public Speaker[] newArray(int size) {
			return new Speaker[size];
		}
	};

}
