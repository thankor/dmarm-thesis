package dmarm.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtractionFunctions {

	public static void ruleFunction(ResultsObject resObj) {

		ArrayList<Rule> extractedRules = new ArrayList<Rule>();
		Rule[] rules;

		/* for all frequent itemSets */
		for (int i = 0; i < resObj.getFrequentItemsets().length; i++) {
			/* extract rules */
			rules = dmarm.utilities.ExtractionFunctions.generateAllRules(resObj.getFrequentItemsets()[i]);
			for (int j = 0; j < rules.length; j++) {
				/* calculate rule support */
				rules[j].setSupport(resObj.getFrequentItemsets()[i].getItemSetSupport());
				extractedRules.add(rules[j]);
			}
		}
		resObj.setRules(extractedRules.toArray(new Rule[extractedRules.size()]));
		/* remove brackets from rules */
		removeBrackets(resObj);
		/* calculate rule confidence */
		calculateRuleConfidence(resObj);
		/* Filter result rules based on minimum support and minimum confidence */
		filterRules(resObj);
	}

	public static void calculateRuleConfidence(ResultsObject resObj) {
		String[] dataLines = Functions.readTXTFile(resObj.getInitialDataFile()).split("\n");
		Rule[] rules = resObj.getRules();
		int andecedent_count;
		int consequent_count;
		String[] andecedent;
		String[] consequent;
		for (Rule ruleIterator : rules) {
			andecedent_count = 0;
			consequent_count = 0;
			andecedent = ruleIterator.getAntecedent().split(",");
			consequent = ruleIterator.getConsequent().split(",");
			for (String line : dataLines) {
				if (itemsContainedInLine(andecedent, line)) {
					andecedent_count++;
					if (itemsContainedInLine(consequent, line))
						consequent_count++;
				}
			}
			ruleIterator
					.setConfidence(Math
							.round((Double.valueOf(consequent_count) / Double.valueOf(dataLines.length))
									/ (Double.valueOf(andecedent_count) / Double.valueOf(dataLines.length)) * 100.0)
							/ 100.0);
		}
	}

	public static boolean itemsContainedInLine(String[] items, String line) {
		for (String item : items) {
			if (!line.contains(item.trim()))
				return false;
		}
		return true;
	}

	/* generate all rules based on a given frequent itemSet */
	public static Rule[] generateAllRules(FrequentItemSet frequentSet) {

		List<Rule> rules = new ArrayList<Rule>();
		Rule rule = null;
		HashSet<Set<String>> subsets = generateSubsets(frequentSet.getFrequentItemSet());
		for (int i = 0; i < subsets.size(); i++) {
			rule = new Rule();
			rule = generateRule((Set<String>) subsets.toArray()[i], frequentSet.getFrequentItemSet());
			rule.setId(i + 1);
			rules.add(rule);
		}
		return rules.toArray(new Rule[rules.size()]);
	}

	/* generate all subsets of a given set */
	public static HashSet<Set<String>> generateSubsets(HashSet<String> frequentSet) {
		HashSet<Set<String>> subsets = new HashSet<Set<String>>();
		Set<String> set = new HashSet<String>();
		int n = frequentSet.size();
		for (int i = 0; i < (1 << n); i++) {
			set = new HashSet<String>();
			for (int j = 0; j < n; j++) {
				if ((i & (1 << j)) > 0) {
					set.add(frequentSet.toArray()[j].toString());
				}
				if (!set.isEmpty() && set.size() != frequentSet.size())
					subsets.add(set);
			}
		}
		return subsets;
	}

	/* generate a rule based on a antecedent set and a consequent set */
	public static Rule generateRule(Set<String> subsets, HashSet<String> frequentSet) {

		Rule rule = new Rule();
		rule.setAntecedent(subsets.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		Set<String> consequent = new HashSet<String>();
		consequent.addAll(frequentSet);
		for (String s : subsets) {
			consequent.remove(s);
		}
		rule.setConsequent(consequent.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		return rule;
	}

	/*
	 * filter rules who don't meet the minimum support and minimum confidence
	 * thresholds
	 */
	public static void filterRules(ResultsObject resObj) {
		ArrayList<Rule> filteredRules = new ArrayList<Rule>();

		for (int i = 0; i < resObj.getRules().length; i++) {
			if (resObj.getRules()[i].getConfidence() >= resObj.getMinimumConfidence()
					&& resObj.getRules()[i].getSupport() >= resObj.getMinimumSupport())
				filteredRules.add(resObj.getRules()[i]);
		}
		resObj.setRules(filteredRules.toArray(new Rule[filteredRules.size()]));
	}

	/* removes brackets from the itemSets and rules */
	public static void removeBrackets(ResultsObject resObj) {

		int length = resObj.getRules().length;
		for (int i = 0; i < length; i++) {
			resObj.getRules()[i]
					.setAntecedent(resObj.getRules()[i].getAntecedent().replaceAll("\\[", "").replaceAll("\\]", ""));
			resObj.getRules()[i]
					.setConsequent(resObj.getRules()[i].getConsequent().replaceAll("\\[", "").replaceAll("\\]", ""));
		}
	}

	public static void itemSetFunction(ResultsObject resObj) {

		String fileName = resObj.getHadoopResultsFile();
		String fileInput;
		HashSet<String> setItems;
		FrequentItemSet currentFrequentSet;
		ArrayList<FrequentItemSet> frequentItemSets = new ArrayList<FrequentItemSet>();
		String[] inputLines;
		String itemSet;
		String[] lineItems;
		int setCount;
		fileInput = Functions.readTXTFile(fileName);
		inputLines = fileInput.split("\n");

		/* read all itemSets from hadoopResultsFile */
		for (int i = 0; i < inputLines.length; i++) {
			setItems = new HashSet<String>();
			currentFrequentSet = new FrequentItemSet();
			itemSet = new String();
			lineItems = new String[] {};
			setCount = 0;
			itemSet = inputLines[i].split("\\t")[0];
			setCount = Integer.parseInt(inputLines[i].split("\\t")[1]);
			lineItems = itemSet.split("#");
			for (int j = 0; j < lineItems.length; j++) {
				setItems.add(lineItems[j]);
			}
			currentFrequentSet.setFrequentItemSet(setItems);
			currentFrequentSet.setItemSetCount(setCount);
			frequentItemSets.add(currentFrequentSet);
		}

		resObj.setFrequentItemsets(frequentItemSets.toArray(new FrequentItemSet[frequentItemSets.size()]));
		/* calculate itemSet support */
		calculateItemSetSupport(resObj.getFrequentItemsets(), resObj.getInitialCount());
		/* prune itemSets under the minimum support threshold */
		pruneItemSets(resObj);
	}

	public static void calculateItemSetSupport(FrequentItemSet[] itemSets, int initialCount) {

		for (int i = 0; i < itemSets.length; i++)
			itemSets[i].setItemSetSupport(
					Math.round(Double.valueOf(itemSets[i].getItemSetCount()) / initialCount * 100.0) / 100.0);
	}

	public static void pruneItemSets(ResultsObject resObj) {

		ArrayList<FrequentItemSet> fSets = new ArrayList<FrequentItemSet>();
		for (int i = 0; i < resObj.getFrequentItemsets().length; i++)
			if (resObj.getFrequentItemsets()[i].getItemSetSupport() >= resObj.getMinimumSupport())
				fSets.add(resObj.getFrequentItemsets()[i]);
		resObj.setFrequentItemsets(fSets.toArray(new FrequentItemSet[fSets.size()]));
	}

}