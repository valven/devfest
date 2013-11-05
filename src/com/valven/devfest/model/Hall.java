package com.valven.devfest.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Hall implements Parcelable {
	private String name;
	private List<Session> sessions;
	private boolean _short;

	public Hall() {

	}

	public Hall(Parcel in) {
		name = in.readString();
		sessions = new ArrayList<Session>();
		in.readTypedList(sessions, Session.CREATOR);
		_short = in.readInt() == 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public boolean isShort() {
		return _short;
	}

	public void setShort(boolean _short) {
		this._short = _short;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeTypedList(sessions);
		dest.writeInt(_short?1:0);
	}
	
	public static final Parcelable.Creator<Hall> CREATOR = new Parcelable.Creator<Hall>() {
		public Hall createFromParcel(Parcel in) {
			return new Hall(in);
		}

		public Hall[] newArray(int size) {
			return new Hall[size];
		}
	};
}
