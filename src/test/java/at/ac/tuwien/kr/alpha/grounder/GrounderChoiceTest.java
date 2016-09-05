package at.ac.tuwien.kr.alpha.grounder;

import at.ac.tuwien.kr.alpha.NoGood;
import at.ac.tuwien.kr.alpha.common.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static at.ac.tuwien.kr.alpha.Util.entriesToMap;
import static at.ac.tuwien.kr.alpha.Util.entry;

/**
 * Represents a small ASP program with guesses {@code { aa :- not bb.  bb :- not aa. }}.
 * Copyright (c) 2016, the Alpha Team.
 */
public class GrounderChoiceTest extends AbstractGrounder {

	private static final int ATOM_AA = 1;
	private static final int ATOM_BB = 2;
	private static final int ATOM_BR1 = 3;
	private static final int ATOM_BR2 = 4;
	private static final int ATOM_EN_BR1 = 5;
	private static final int ATOM_EN_BR2 = 6;
	private static final int ATOM_DIS_BR1 = 7;
	private static final int ATOM_DIS_BR2 = 8;

	private static Map<Integer, String> atomIdToString = Stream.of(
		entry(ATOM_AA, "aa"),
		entry(ATOM_BB, "bb"),
		entry(ATOM_BR1, "_br1"),
		entry(ATOM_BR2, "_br2"),
		entry(ATOM_EN_BR1, "_en_br1"),
		entry(ATOM_EN_BR2, "_en_br2"),
		entry(ATOM_DIS_BR1, "_dis_br1"),
		entry(ATOM_DIS_BR2, "_dis_br2")
	).collect(entriesToMap());

	private static final int RULE_AA = 11; // { -aa, _br1 }
	private static final int BRULE_AA = 12; // { -_br1, -bb }
	private static final int RULE_BB = 13; // { -bb, _br2 }
	private static final int BRULE_BB = 14; // { -_br2, -aa }
	private static final int CHOICE_EN_BR1 = 15; // { -_en_br1 }
	private static final int CHOICE_EN_BR2 = 16; // { -_en_br2 }
	private static final int CHOICE_DIS_BR1 = 17; // { -_dis_br1,  bb}
	private static final int CHOICE_DIS_BR2 = 18; // { -dis_br2, aa }


	private static final Map<Integer, NoGood> NOGOODS = Stream.of(
		entry(RULE_AA, new NoGood(new int[]{-ATOM_AA, ATOM_BR1}, 0)),
		entry(BRULE_AA, new NoGood(new int[]{-ATOM_BR1, -ATOM_BB}, 0)),
		entry(RULE_BB, new NoGood(new int[]{-ATOM_BB, ATOM_BR2}, 0)),
		entry(BRULE_BB, new NoGood(new int[]{-ATOM_BR2, -ATOM_AA}, 0)),
		entry(CHOICE_EN_BR1, new NoGood(new int[]{-ATOM_EN_BR1}, 0)),
		entry(CHOICE_EN_BR2, new NoGood(new int[]{-ATOM_EN_BR2}, 0)),
		entry(CHOICE_DIS_BR1, new NoGood(new int[]{-ATOM_DIS_BR1, ATOM_BB}, 0)),
		entry(CHOICE_DIS_BR2, new NoGood(new int[]{-ATOM_DIS_BR2, ATOM_AA}, 0))
	).collect(entriesToMap());

	private static final Map<Integer, Integer> CHOICE_ENABLE = Stream.of(
		entry(ATOM_EN_BR1, ATOM_BR1),
		entry(ATOM_EN_BR2, ATOM_BR2)
	).collect(entriesToMap());

	private static final Map<Integer, Integer> CHOICE_DISABLE = Stream.of(
		entry(ATOM_DIS_BR1, ATOM_BR1),
		entry(ATOM_DIS_BR2, ATOM_BR2)
	).collect(entriesToMap());

	@Override
	public AnswerSet assignmentToAnswerSet(java.util.function.Predicate<Predicate> filter, int[] trueAtoms) {
		// Note: This grounder only deals with 0-ary predicates, i.e., every atom is a predicate and there is
		// 	 only one predicate instance representing 0 terms.
		BasicAnswerSet answerSet = new BasicAnswerSet();

		ArrayList<Predicate> trueAtomPredicates = new ArrayList<>();
		for (int trueAtom : trueAtoms) {
			BasicPredicate atomPredicate = new BasicPredicate(atomIdToString.get(trueAtom), 0);
			if (filter.test(atomPredicate)) {
				trueAtomPredicates.add(atomPredicate);
			}
		}
		answerSet.setPredicateList(trueAtomPredicates);

		// Add the atom instances
		HashMap<Predicate, ArrayList<PredicateInstance>> predicateInstances = new HashMap<>();
		for (Predicate trueAtomPredicate : trueAtomPredicates) {
			PredicateInstance predicateInstance = new PredicateInstance(trueAtomPredicate, new Term[0]);
			ArrayList<PredicateInstance> instanceList = new ArrayList<>();
			instanceList.add(predicateInstance);
			predicateInstances.put(trueAtomPredicate, instanceList);
		}
		answerSet.setPredicateInstances(predicateInstances);

		return answerSet;
	}

	boolean returnedAllNogoods;
	@Override
	public Map<Integer, NoGood> getNoGoods() {
		if (!returnedAllNogoods) {
			returnedAllNogoods = true;
			return NOGOODS;
		} else {
			return new HashMap<>();
		}
	}

	@Override
	public Pair<Map<Integer, Integer>, Map<Integer, Integer>> getChoiceAtoms() {
		return new ImmutablePair<>(CHOICE_ENABLE, CHOICE_DISABLE);
	}

	@Override
	public void updateAssignment(int[] atomIds, boolean[] truthValues) {
		// This test grounder reports all NoGoods immediately, irrespective of any assignment.
		return;
	}

	@Override
	public void forgetAssignment(int[] atomIds) {
		return;
	}
}