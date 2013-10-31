package com.valven.devfest.model;

import java.util.List;

public class SponsorGroup {
	private String name;
	private List<Sponsor> sponsors;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Sponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<Sponsor> sponsors) {
		this.sponsors = sponsors;
	}
}
