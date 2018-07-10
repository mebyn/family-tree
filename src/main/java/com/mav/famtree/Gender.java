package com.mav.famtree;

/**
 * @author melvin
 * @date 09/07/2018
 */
public enum Gender {
	
	UNKNOWN("unknown", "M/F"),
	MALE("male", "M"),
	FEMALE("female", "F");

	private final String description;
	private final String abbreviation;

	Gender(String description, String abbreviation) {
		this.description = description;
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public String getAbbreviation() {
		return abbreviation;
	}
}
