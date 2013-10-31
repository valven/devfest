package com.valven.devfest.model;

import java.util.List;

public class Hall {
	private String name;
	private List<Session> sessions;

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
}
