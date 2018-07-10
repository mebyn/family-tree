package com.mav.famtree;

/**
 * @author melvin
 * @date 09/07/2018
 */
public interface RelationData {

	default Person add(PersonNode node, String personToAdd) {
		return new Person.Builder("", Gender.UNKNOWN).build();
	}

}
