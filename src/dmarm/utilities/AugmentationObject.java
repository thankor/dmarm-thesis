package dmarm.utilities;

public class AugmentationObject {

	private String inputFolder;
	private String outputFolder;
	private String hierarchyFilesFolder;
	private String mappingFile;
	private Hierarchy[] hierarchies;
	private Mapping[] mappings;
	private int inputFileNumber;
	private int hierarchyFileNumber;
	private int mappingsNumber;

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getHierarchyFilesFolder() {
		return hierarchyFilesFolder;
	}

	public void setHierarchyFilesFolder(String hierarchyFilesFolder) {
		this.hierarchyFilesFolder = hierarchyFilesFolder;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public Hierarchy[] getHierarchies() {
		return hierarchies;
	}

	public void setHierarchies(Hierarchy[] hierarchies) {
		this.hierarchies = hierarchies;
	}

	public Mapping[] getMappings() {
		return mappings;
	}

	public void setMappings(Mapping[] mappings) {
		this.mappings = mappings;
	}

	public int getInputFileNumber() {
		return inputFileNumber;
	}

	public void setInputFileNumber(int inputFileNumber) {
		this.inputFileNumber = inputFileNumber;
	}

	public int getHierarchyFileNumber() {
		return hierarchyFileNumber;
	}

	public void setHierarchyFileNumber(int hierarchyFileNumber) {
		this.hierarchyFileNumber = hierarchyFileNumber;
	}

	public int getMappingsNumber() {
		return mappingsNumber;
	}

	public void setMappingsNumber(int mappingsNumber) {
		this.mappingsNumber = mappingsNumber;
	}

}
