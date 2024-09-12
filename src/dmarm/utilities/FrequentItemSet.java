package dmarm.utilities;

import java.util.HashSet;

public class FrequentItemSet {

	HashSet<String> frequentItemSet; /* contains the distinct items of the itemSet */
	private Integer itemSetCount; /* contains the count for the itemSet */
	private Double itemSetSupport; /* contains the support for the itemSet */
	private String delimiter; /* contains the delimiter for splitting the itemSet fields */

	
	public FrequentItemSet() {
		this.frequentItemSet = new HashSet<String>();
		this.itemSetCount = new Integer(0);
		this.itemSetSupport = new Double(0);
		this.setDelimiter("#");
	}
	/*
	 * gets a string with the items, delimited with the '#' character along with
	 * the count and support (initialized as 0.0) for the itemSet
	 */
	public FrequentItemSet(String itemSet, Integer count, Double support, String delimiter) {
		String[] set = itemSet.split("#");
		this.frequentItemSet = new HashSet<String>();
		for (int i = 0; i < set.length; i++) {
			this.frequentItemSet.add(set[i].toString());
		}
		this.itemSetCount = count;
		this.itemSetSupport = new Double(0);
		if (delimiter == null || delimiter.trim().isEmpty())
			this.setDelimiter("#");
		else
			this.setDelimiter(delimiter);
	}

	public HashSet<String> getFrequentItemSet() {
		return frequentItemSet;
	}

	public void setFrequentItemSet(HashSet<String> frequentItemSet) {
		this.frequentItemSet = frequentItemSet;
	}

	public Integer getItemSetCount() {
		return itemSetCount;
	}

	public void setItemSetCount(Integer itemSetCount) {
		this.itemSetCount = itemSetCount;
	}

	public Double getItemSetSupport() {
		return itemSetSupport;
	}

	public void setItemSetSupport(Double itemSetSupport) {
		this.itemSetSupport = itemSetSupport;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}