package dmarm.utilities;

public class Rule {
	private Integer id;
	private String antecedent;
	private String consequent;
	private Double confidence;
	private Double support;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAntecedent() {
		return antecedent;
	}

	public void setAntecedent(String antecedent) {
		this.antecedent = antecedent;
	}

	public String getConsequent() {
		return consequent;
	}

	public void setConsequent(String consequent) {
		this.consequent = consequent;
	}

	public Double getConfidence() {
		return confidence;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	public Double getSupport() {
		return support;
	}

	public void setSupport(Double support) {
		this.support = support;
	}
}
