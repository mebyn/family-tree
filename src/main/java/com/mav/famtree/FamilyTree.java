package com.mav.famtree;

import java.util.Optional;

/**
 * @author melvin
 * @date 09/07/2018
 */
public class FamilyTree {

	private PersonNode rootNode;

	static FamilyTree createTree() {
		return new FamilyTree();
	}

	private FamilyTree() {
	}

	Optional<PersonNode> findNode(String name, Relationship relationship) {
		if (rootNode == null) {
			//Create first node
			final Person rootPerson = new Person.Builder(name, relationship.getGender()).build();
			rootNode = PersonNode.createNode(rootPerson);
			return Optional.of(rootNode);
		}
		return traverseDFSTree(rootNode, name);
	}
	
	private Optional<PersonNode> traverseDFSTree(PersonNode node, final String name) {
		Optional<PersonNode> result = Optional.empty();
		final Person nodePerson = node.getPerson();
		final PersonNode spouseNode = node.getSpouse();
		if ((nodePerson.getName() != null && nodePerson.getName().equals(name))) {
			result = Optional.of(node);
		} else if (spouseNode != null && (spouseNode.getPerson().getName() != null && spouseNode.getPerson().getName().equals(name))) {
			result = Optional.of(node.getSpouse());
		} else if (!node.getChildren().isEmpty()) {
			for (PersonNode childNode : node.getChildren()) {
				result = traverseDFSTree(childNode, name);
				if (result.isPresent()) {
					return result;
				}
			}
		}
		return result;
	}

}
