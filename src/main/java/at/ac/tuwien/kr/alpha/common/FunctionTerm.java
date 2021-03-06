package at.ac.tuwien.kr.alpha.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static at.ac.tuwien.kr.alpha.Util.appendDelimited;

/**
 * Copyright (c) 2016, the Alpha Team.
 */
public class FunctionTerm extends Term {
	private static HashMap<FunctionTerm, FunctionTerm> knownFunctionTerms = new HashMap<>();

	public final TermSymbol functionSymbol;
	public final List<Term> termList;

	private FunctionTerm(TermSymbol functionSymbol, List<Term> termList) {
		this.functionSymbol = functionSymbol;
		this.termList = termList;
	}

	public static FunctionTerm getFunctionTerm(TermSymbol functionSymbol, List<Term> termList) {
		FunctionTerm functionTerm = new FunctionTerm(functionSymbol, termList);
		knownFunctionTerms.putIfAbsent(functionTerm, functionTerm);
		return knownFunctionTerms.get(functionTerm);
	}

	public static FunctionTerm getFunctionTerm(String functionSymbol, List<Term> termList) {
		return getFunctionTerm(TermSymbol.getInstance(functionSymbol), termList);
	}

	@Override
	public boolean 	isGround() {
		for (Term term : termList) {
			if (!term.isGround()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<VariableTerm> getOccurringVariables() {
		LinkedList<VariableTerm> vars = new LinkedList<>();
		for (Term term : termList) {
			vars.addAll(term.getOccurringVariables());
		}
		return vars;
	}

	@Override
	public String toString() {
		if (termList.isEmpty()) {
			return functionSymbol.getSymbol();
		}

		final StringBuilder sb = new StringBuilder("(");
		appendDelimited(sb, termList);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FunctionTerm that = (FunctionTerm) o;

		if (!functionSymbol.equals(that.functionSymbol)) {
			return false;
		}
		return termList.equals(that.termList);
	}

	@Override
	public int hashCode() {
		return 31 * functionSymbol.hashCode() + termList.hashCode();
	}
}
