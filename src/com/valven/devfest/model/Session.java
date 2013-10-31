package com.valven.devfest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable {
	private Date begin;
	private Date end;
	private String title;
	private String info;
	private String location;
	private boolean disabled;
	private List<String> speakers;

	public Session() {
	}

	public Session(Parcel in) {
		begin = (Date) in.readSerializable();
		end = (Date) in.readSerializable();
		info = in.readString();
		location = in.readString();
		title = in.readString();
		disabled = in.readInt() == 1;
		speakers = new ArrayList<String>();
		in.readStringList(speakers);
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public List<String> getSpeakers() {
		return speakers;
	}
	
	public void setSpeakers(List<String> speakers) {
		this.speakers = speakers;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(begin);
		dest.writeSerializable(end);
		dest.writeString(info);
		dest.writeString(location);
		dest.writeString(title);
		dest.writeInt(disabled?1:0);
		dest.writeStringList(speakers);
	}

	public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
		public Session createFromParcel(Parcel in) {
			return new Session(in);
		}

		public Session[] newArray(int size) {
			return new Session[size];
		}
	};
}
