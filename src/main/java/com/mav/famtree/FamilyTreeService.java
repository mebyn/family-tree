package com.mav.famtree;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author melvin
 * @date 09/07/2018
 */
@Service
public class FamilyTreeService {

	private static final String KEY_PERSON = "person";
	private static final String KEY_RELATION = "relation";
	private static final String SEPARATOR = "=";
	private final FamilyTree familyTree;

	private FamilyTreeService() {
		familyTree = FamilyTree.createTree();
	}

	public void processEntry(String entry) {
		final String[] entries = entry.split(" ");

		final String[] entryA = entries[0].split(SEPARATOR);
		final String[] entryB = entries[1].split(SEPARATOR);
		final EntryPair<String, String> entryPairA = new EntryPair<>(entryA[0].toLowerCase(), entryA[1].toLowerCase());
		final EntryPair<String, String> entryPairB = new EntryPair<>(entryB[0].toLowerCase(), entryB[1].toLowerCase());
		if (entryPairA.getFirstValue().equals(KEY_PERSON) || entryPairA.getSecondValue().equals(KEY_RELATION)) {
			final Optional<PersonNode> node = familyTree.findNode(entryPairA.getSecondValue(), Relationship.getRelation(entryPairA.getFirstValue()));
			final Relationship relation = Relationship.getRelation(entryPairB.getSecondValue());
			final String result = node.map(person -> relation.getName() + "=" + relation.getMembers(person)).orElse("Person does not exist!");
			System.out.println(result);
		} else {
			final Person personAdded = addPerson(entryPairA, entryPairB);
			System.out.format("Welcome to the family %s!\n", personAdded.getName());
		}
	}

	private Person addPerson(EntryPair<String, String> pairA, EntryPair<String, String> pairB) {
		Optional<PersonNode> node = familyTree.findNode(pairA.getSecondValue(), Relationship.getRelation(pairA.getFirstValue()));
		if (node.isPresent()) {
			return Relationship.getRelation(pairB.getFirstValue()).add(node.get(), pairB.getSecondValue());
		} else {
			node = familyTree.findNode(pairB.getSecondValue(), Relationship.getRelation(pairB.getFirstValue()));
			PersonNode nodeB = node.orElseThrow(() -> new NullPointerException("No proper relation was found!"));
			return Relationship.getRelation(pairA.getFirstValue()).add(nodeB, pairA.getSecondValue());
		}
	}

	private class EntryPair<T1, T2> {

		private T1 firstValue;
		private T2 secondValue;

		EntryPair(T1 firstValue, T2 secondValue) {
			this.firstValue = firstValue;
			this.secondValue = secondValue;
		}

		T1 getFirstValue() {
			return firstValue;
		}

		T2 getSecondValue() {
			return secondValue;
		}
	}
}

