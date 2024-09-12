package dmarm.utilities;

public class ResultsObject {

	private Rule[] rules;
	private FrequentItemSet[] frequentItemsets;
	private Double minimumSupport;
	private Double minimumConfidence;
	private String hadoopResultsFile;
	private String initialDataFile;
	private String outputFolder;
	private int initialCount;

	public Rule[] getRules() {
		return rules;
	}

	public void setRules(Rule[] rules) {
		this.rules = rules;
	}

	public FrequentItemSet[] getFrequentItemsets() {
		return frequentItemsets;
	}

	public void setFrequentItemsets(FrequentItemSet[] frequentItemsets) {
		this.frequentItemsets = frequentItemsets;
	}

	public Double getMinimumSupport() {
		return minimumSupport;
	}

	public void setMinimumSupport(Double minimumSupport) {
		this.minimumSupport = minimumSupport;
	}

	public Double getMinimumConfidence() {
		return minimumConfidence;
	}

	public void setMinimumConfidence(Double minimumConfidence) {
		this.minimumConfidence = minimumConfidence;
	}

	public String getHadoopResultsFile() {
		return hadoopResultsFile;
	}

	public void setHadoopResultsFile(String hadoopResultsFile) {
		this.hadoopResultsFile = hadoopResultsFile;
	}

	public String getInitialDataFile() {
		return initialDataFile;
	}

	public void setInitialDataFile(String initialDataFile) {
		this.initialDataFile = initialDataFile;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public int getInitialCount() {
		return initialCount;
	}

	public void setInitialCount(int initialCount) {
		this.initialCount = initialCount;
	}

}
