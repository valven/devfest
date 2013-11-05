package com.valven.devfest.model;

import java.util.List;

public class Event {
	private String name;
	private List<Hall> halls;

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
