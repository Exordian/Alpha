package at.ac.tuwien.kr.alpha.common;

import java.util.*;

import static java.util.Collections.*;

/**
 * Copyright (c) 2016, the Alpha Team.
 */
public class BasicAnswerSet implements AnswerSet {
	public static final BasicAnswerSet EMPTY = new BasicAnswerSet(emptySet(), emptyMap());

	private final Set<Predicate> predicates;
	private final Map<Predicate, Set<BasicAtom>> predicateInstances;

	public BasicAnswerSet(Set<Predicate> predicates, Map<Predicate, Set<BasicAtom>> predicateInstances) {
		this.predicates = predicates;
		this.predicateInstances = predicateInstances;
	}

	@Override
	public Set<Predicate> getPredicates() {
		return predicates;
	}

	@Override
	public Set<BasicAtom> getPredicateInstances(Predicate predicate) {
		return predicateInstances.get(predicate);
	}

	@Override
	public boolean isEmpty() {
		return predicates.isEmpty();
	}

	public String toString() {
		if (predicates.isEmpty()) {
			return "{}";
		}

		final StringBuilder sb = new StringBuilder("{ ");
		for (Iterator<Predicate> iterator = predicates.iterator(); iterator.hasNext();) {
			Predicate predicate = iterator.next();
			Set<BasicAtom> instances = getPredicateInstances(predicate);

			if (instances == null || instances.isEmpty()) {
				sb.append(predicate.getPredicateName());
				continue;
			}

			for (Iterator<BasicAtom> instanceIterator = instances.iterator(); instanceIterator.hasNext();) {
				sb.append(instanceIterator.next());
				if (instanceIterator.hasNext()) {
					sb.append(", ");
				}
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(" }");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BasicAnswerSet)) {
			return false;
		}

		BasicAnswerSet that = (BasicAnswerSet) o;

		if (!predicates.equals(that.predicates)) {
			return false;
		}
		return predicateInstances.equals(that.predicateInstances);
	}

	@Override
	public int hashCode() {
		return  31 * predicates.hashCode() + predicateInstances.hashCode();
	}

	public static class Builder {
		private boolean firstInstance = true;
		private String predicateSymbol;
		private Predicate predicate;
		private Set<Predicate> predicates = new HashSet<>();
		private Set<BasicAtom> instances = new HashSet<>();
		private Map<Predicate, Set<BasicAtom>> predicateInstances = new HashMap<>();

		public Builder() {
		}

		public Builder(Builder copy) {
			this.firstInstance = copy.firstInstance;
			this.predicateSymbol = copy.predicateSymbol;
			this.predicate = copy.predicate;
			this.predicates = new HashSet<>(copy.predicates);
			this.instances = new HashSet<>(copy.instances);
			this.predicateInstances = new HashMap<>(copy.predicateInstances);
		}

		private void flush() {
			if (firstInstance) {
				predicate = new BasicPredicate(predicateSymbol, 0);
				predicates.add(predicate);
				predicateInstances.put(predicate, new HashSet<>(singletonList(new BasicAtom(predicate))));
			} else {
				predicateInstances.put(predicate, new HashSet<>(instances));
			}
			firstInstance = true;
			instances.clear();
			predicate = null;
		}

		public Builder predicate(String predicateSymbol) {
			if (this.predicateSymbol != null) {
				flush();
			}
			this.predicateSymbol = predicateSymbol;
			return this;
		}

		public Builder instance(String... constantSymbols) {
			if (firstInstance) {
				firstInstance = false;
				predicate = new BasicPredicate(predicateSymbol, constantSymbols.length);
				predicates.add(predicate);
			}

			final Term[] terms = new Term[constantSymbols.length];
			for (int i = 0; i < constantSymbols.length; i++) {
				terms[i] = ConstantTerm.getInstance(constantSymbols[i]);
			}
			instances.add(new BasicAtom(predicate, terms));
			return this;
		}

		public BasicAnswerSet build() {
			flush();
			return new BasicAnswerSet(predicates, predicateInstances);
		}
	}
}
