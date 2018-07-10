package com.mav.famtree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author melvin
 * @date 09/07/2018
 */
class PersonNode {

	private Person person;
	private PersonNode parent;
	private PersonNode spouse;
	private List<PersonNode> children;

	static PersonNode createNode(Person person) {
		return new PersonNode(person, null, new ArrayList<>());
	}

	private PersonNode(Person person, PersonNode parent, List<PersonNode> children) {
		this.person = person;
		this.parent = parent;
		this.children = children;
	}

	PersonNode getSpouse() {
		return spouse;
	}

	void setSpouse(PersonNode spouse) {
		this.spouse = spouse;
	}

	PersonNode getParent() {
		return parent;
	}

	void setParent(PersonNode parent) {
		if (parent.getSpouse() != null) {
			parent.getSpouse().getChildren().add(this);
		}
		this.parent = parent;
	}

	Person getPerson() {
		return person;
	}

	List<PersonNode> getChildren() {
		return children;
	}
}
