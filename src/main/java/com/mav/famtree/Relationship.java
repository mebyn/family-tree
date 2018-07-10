package com.mav.famtree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author melvin
 * @date 09/07/2018
 */
public enum Relationship implements RelationData {

	HUSBAND("husband", Gender.MALE, Relationship::getSpouse) {
		@Override public Person add(PersonNode node, String personToAdd) {
			return addSpouse(node, personToAdd, getGender());
		}

		@Override public String getMembers(PersonNode node) {
			return getMemberDataProvider().apply(node, getGender());
		}
	},
	WIFE("wife", Gender.FEMALE, Relationship::getSpouse) {
		@Override public Person add(PersonNode node, String personToAdd) {
			return addSpouse(node, personToAdd, getGender());
		}

		@Override public String getMembers(PersonNode node) {
			return getMemberDataProvider().apply(node, getGender());
		}
	},
	SON("son", Gender.MALE, Relationship::getChildren) {
		@Override public Person add(PersonNode node, String personToAdd) {
			return addChild(node, personToAdd, getGender());
		}

		@Override public String getMembers(PersonNode node) {
			return getMemberDataProvider().apply(node, getGender());
		}
	},
	DAUGHTER("daughter", Gender.FEMALE, Relationship::getChildren) {
		@Override public Person add(PersonNode node, String personToAdd) {
			return addChild(node, personToAdd, getGender());
		}

		@Override public String getMembers(PersonNode node) {
			return getMemberDataProvider().apply(node, getGender());
		}
	},
	FATHER("father", Gender.MALE, Relationship::getParent),
	MOTHER("mother", Gender.FEMALE, Relationship::getParent),
	BROTHER("brother", Gender.MALE, Relationship::getSiblings),
	SISTER("sister", Gender.FEMALE, Relationship::getSiblings),
	COUSIN("cousin", Gender.UNKNOWN, (person, gender) -> getCousin(person)),
	GRANDMOTHER("grandmother", Gender.FEMALE, Relationship::getGrandParent),
	GRANDFATHER("grandfather", Gender.MALE, Relationship::getGrandParent),
	GRANDSON("grandson", Gender.MALE, Relationship::getGrandChildren) {
		@Override public String getMembers(PersonNode node) {
			return getMemberDataProvider().apply(node, getGender());
		}
	},
	GRANDDAUGHTER("granddaughter", Gender.FEMALE, Relationship::getGrandChildren) {
		@Override public String getMembers(PersonNode node) {
			return getMemberDataProvider().apply(node, getGender());
		}
	},
	AUNT("aunt", Gender.FEMALE, Relationship::getParentSiblings),
	UNCLE("uncle", Gender.MALE, Relationship::getParentSiblings);

	private static final Map<String, Relationship> map;

	static {
		map = Collections.unmodifiableMap(Arrays.stream(Relationship.values()).collect(Collectors.toConcurrentMap(Relationship::getName, v -> v)));
	}

	private String name;
	private Gender gender;
	private BiFunction<PersonNode, Gender, String> memberDataProvider;

	Relationship(String name, Gender gender, BiFunction<PersonNode, Gender, String> memberDataProvider) {
		this.name = name;
		this.gender = gender;
		this.memberDataProvider = memberDataProvider;
	}

	static Relationship getRelation(String name) {
		return map.get(name.trim().toLowerCase());
	}

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	public BiFunction<PersonNode, Gender, String> getMemberDataProvider() {
		return memberDataProvider;
	}

	private static Person addSpouse(PersonNode node, String spouseToAdd, Gender gender) {
		final Person spouse = new Person.Builder(spouseToAdd, gender).build();
		final PersonNode spouseNode = PersonNode.createNode(spouse);
		node.setSpouse(spouseNode);
		spouseNode.setSpouse(node);
		return spouse;
	}

	private static Person addChild(PersonNode node, String childToAdd, Gender gender) {
		final Person child = new Person.Builder(childToAdd, gender).build();
		final PersonNode childNode = PersonNode.createNode(child);
		childNode.setParent(node);
		node.getChildren().add(childNode);
		return child;
	}

	public String getMembers(PersonNode node) {
		return Optional.ofNullable(node.getParent()).map(p -> getMemberDataProvider().apply(node, getGender())).orElse("No Parent!");
	}

	private static String getSpouse(PersonNode node, Gender gender) {
		String spouseName = gender.equals(Gender.MALE) ? "Husband" : "Wife";
		return Optional.ofNullable(node.getSpouse()).map(spouse ->
				node.getPerson().getGender().equals(gender) ? ("Already the " + spouseName) : spouse.getPerson().getName()
		)
				.orElse("No spouse!");
	}

	private static String getChildren(PersonNode node, Gender gender) {
		String noDescendant = "No " + (gender.equals(Gender.MALE) ? "Sons" : "Daughters");
		return !node.getChildren().isEmpty() ?
				node.getChildren()
						.stream()
						.filter(child -> child.getPerson().getGender().equals(gender))
						.map(child -> child.getPerson().getName())
						.collect(Collectors.joining(",")) : noDescendant;
	}

	private static String getGrandChildren(PersonNode node, Gender gender) {
		if (!node.getChildren().isEmpty()) {
			return node.getChildren()
					.stream()
					.map(PersonNode::getChildren)
					.flatMap(List::stream)
					.filter(grandChild -> grandChild.getPerson().getGender().equals(gender))
					.map(grandChild -> grandChild.getPerson().getName())
					.collect(Collectors.joining(","));
		} else {
			return "No children!";
		}
	}

	private static String getParent(PersonNode node, Gender gender) {
		final PersonNode parent = node.getParent();
		return parent.getPerson().getGender().equals(gender) ? parent.getPerson().getName() : parent.getSpouse().getPerson().getName();
	}

	private static String getSiblings(PersonNode node, Gender gender) {
		final String siblings = node.getParent().getChildren().stream()
				.filter(child -> !child.equals(node) && child.getPerson().getGender().equals(gender))
				.map(child -> child.getPerson().getName())
				.collect(Collectors.joining(","));
		return !siblings.isEmpty() ? siblings : "";
	}

	private static String getGrandParent(PersonNode node, Gender gender) {
		final PersonNode grandParent = Optional.ofNullable(node.getParent().getParent()).orElse(node.getParent().getSpouse().getParent());
		return Optional.ofNullable(grandParent)
				.map(gp -> gp.getPerson().getGender().equals(gender) ?
						gp.getPerson().getName() :
						gp.getSpouse().getPerson().getName())
				.orElse("No grandparent!");
	}

	private static String getParentSiblings(PersonNode node, Gender gender) {
		return getSiblings(node.getParent(), gender);
	}

	private static String getCousin(PersonNode node) {
		final PersonNode parent = node.getParent();
		final PersonNode grandParent = Optional.ofNullable(parent.getParent()).orElse(parent.getSpouse().getParent());
		if (grandParent != null) {
			return grandParent.getChildren().stream()
					.filter(child -> !child.equals(parent) && !child.equals(parent.getSpouse()))
					.map(PersonNode::getChildren)
					.flatMap(List::stream)
					.map(cousin -> cousin.getPerson().getName())
					.collect(Collectors.joining(","));
		} else {
			return "No cousins!";
		}
	}
}
