package dmarm.utilities;

import java.util.ArrayList;

public class AugmentationFunctions {

	public static void updatePathsFromFile(AugmentationObject augmObj, String filePath) {

		XMLDto xmlDto = null;
		xmlDto = Functions.readXMLConfigurationFile(filePath);
		String[] names = xmlDto.getPropertyNames();
		String[] values = xmlDto.getPropertyValues();

		for (int i = 0; i < xmlDto.getPropertyNumber(); i++) {
			switch (names[i]) {
			case "input_folder":
				augmObj.setInputFolder(values[i].toString());
				break;
			case "hierarchies_folder":
				augmObj.setHierarchyFilesFolder(values[i].toString());
				break;
			case "output_folder":
				augmObj.setOutputFolder(values[i].toString());
				break;
			case "mapping_file":
				augmObj.setMappingFile(values[i].toString());
				break;
			}
		}
	}

	/* read hierarchies to attributes file and return the mappings */
	public static Mapping[] hierarchiesMapping(String filePath) {
		String[] lines = null;
		String[] lineArgs = null;
		Mapping mapping = null;
		ArrayList<Mapping> hierarchiesMap = new ArrayList<Mapping>();
		String data = Functions.readTXTFile(filePath);
		lines = data.split("\n");
		for (int i = 0; i < lines.length; i++) {
			mapping = new Mapping();
			lineArgs = lines[i].split(",");
			if (lineArgs.length == 3) {
				mapping.setFileName(lineArgs[0]);
				mapping.setHierarchyName(lineArgs[1]);
				mapping.setFileColumn(Integer.parseInt(lineArgs[2]));
				hierarchiesMap.add(mapping);
			}
		}
		return hierarchiesMap.toArray(new Mapping[hierarchiesMap.size()]);
	}

	public static void setupFunction(AugmentationObject augmObj) {

		augmObj.setHierarchyFileNumber(Functions.countFiles(augmObj.getHierarchyFilesFolder()));
		/* read hierarchy files to augmentation object */
		int hierarchyNum = augmObj.getHierarchyFileNumber();
		String[] hierarchyNames = Functions.getFilesInDirectory(augmObj.getHierarchyFilesFolder());
		Hierarchy[] hierarchies = new Hierarchy[hierarchyNum];
		/* read all hierarchies */
		for (int i = 0; i < hierarchyNum; i++) {
			hierarchies[i] = Functions
					.readXMLHierarchyFile(augmObj.getHierarchyFilesFolder() + "\\" + hierarchyNames[i]);
		}
		/* update the augmentation object with file and mapping counts */
		augmObj.setHierarchies(hierarchies);
		augmObj.setInputFileNumber(Functions.countFiles(augmObj.getInputFolder()));
		augmObj.setMappings(hierarchiesMapping(augmObj.getMappingFile()));
	}

	public static boolean augmentationFunction(AugmentationObject augmObj) {

		int inputFileNum = augmObj.getInputFileNumber();
		String[] dataFileNames = Functions.getFilesInDirectory(augmObj.getInputFolder());
		String[] inputLines = null;
		String[] lineArgs = null;
		Hierarchy[] fileHierarchies = null;
		String dataFile = null;
		String fileName = null;
		String augmentedLine = null;
		StringBuffer augmentedFileBuffer = null;
		Mapping[] fileMapping = null;
		HierarchyNode[] ancestors = new HierarchyNode[] {};
		HierarchyNode leaf = new HierarchyNode();

		/* for each input data file */
		for (int fileIterator = 0; fileIterator < inputFileNum; fileIterator++) {
			dataFile = new String();
			fileName = augmObj.getInputFolder() + "\\" + dataFileNames[fileIterator];
			dataFile = Functions.readTXTFile(fileName);
			inputLines = new String[] {};
			inputLines = Functions.splitLine(dataFile, "\n");

			/* check for hierarchies for this file */
			fileHierarchies = retrieveHierarchiesForFile(dataFileNames[fileIterator], augmObj);

			/* check for mappings for this file */
			if (fileHierarchies == null)
				continue;

			fileMapping = retrieveMappingForFile(dataFileNames[fileIterator], augmObj);

			augmentedFileBuffer = new StringBuffer();

			for (int lineIterator = 0; lineIterator < inputLines.length; lineIterator++) {
				lineArgs = inputLines[lineIterator].split(",");
				augmentedLine = inputLines[lineIterator];
				for (Mapping mapIterator : fileMapping) {

					for (int argIterator = 0; argIterator < lineArgs.length; argIterator++) {
						if (argIterator == mapIterator.getFileColumn() - 1) {
							leaf.setName(lineArgs[argIterator]);
							if (leaf.getName() == null)
								continue;
							augmentedLine = augmentLineForArgument(fileHierarchies, augmentedLine, ancestors, leaf,
									mapIterator, argIterator);
						}
					}
				}
				augmentedFileBuffer.append(augmentedLine + "\n");
			}
			Functions.appendTXTFile(augmObj.getOutputFolder() + "\\" + "AugmentedDataFile.txt",
					augmentedFileBuffer.toString());
		} /* end loop for each input data file */
		return Functions.fileExists(augmObj.getOutputFolder() + "\\" + "AugmentedDataFile.txt");
	}

	public static String augmentLineForArgument(Hierarchy[] fileHierarchies, String augmentedLine,
			HierarchyNode[] ancestors, HierarchyNode leaf, Mapping mapping, int column) {

		Hierarchy hierarchy = new Hierarchy();
		for (int hierarchyIterator = 0; hierarchyIterator < fileHierarchies.length; hierarchyIterator++) {
			hierarchy = fileHierarchies[hierarchyIterator];
			if (mapping.getHierarchyName().equals(hierarchy.getHierarchyName())) {
				ancestors = getAncestors(leaf.getName(), hierarchy);
				break;
			}
		}
		augmentedLine = augmentLine(augmentedLine.split(","), ancestors, hierarchy.getHierarchyName(), ",", column);
		return augmentedLine;
	}

	public static String augmentLine(String[] lineFields, HierarchyNode[] ancestors, String hierarchyName,
			String delimiter, int column) {
		StringBuffer augmentedLine = new StringBuffer();
		String ancestorName = null;
		/* for each ancestor append and entry at the augmented line */
		for (int i = 0; i < lineFields.length; i++) {

			if (column == i) {
				augmentedLine.append(lineFields[i] + "-" + hierarchyName + "-" + ancestors[0].getLevel());
			} else {
				augmentedLine.append(lineFields[i]);
			}
			if (i < lineFields.length) {
				augmentedLine.append(delimiter);
			}
		}
		/* append all ancestors with hierarchy info */
		for (int j = 1; j < ancestors.length; j++) {
			ancestorName = ancestors[j].getName().replaceAll(" ", "_") + "-" + hierarchyName + "-"
					+ ancestors[j].getLevel();
			augmentedLine.append(ancestorName);
			if (j != (ancestors.length - 1)) {
				augmentedLine.append(delimiter);
			}
		}
		return augmentedLine.toString();
	}

	/* retrieve all ancestor nodes for a given leaf node in a given hierarchy */
	public static HierarchyNode[] getAncestors(String leafName, Hierarchy hierarchy) {

		/*
		 * for all nodes in the hierarchy if node type == leaf and values == leafName
		 * node found, create new node (level,id,parent)
		 */
		int parentNode = 0;
		ArrayList<HierarchyNode> ancestors = new ArrayList<HierarchyNode>();
		HierarchyNode node = new HierarchyNode();
		for (int i = 0; i < hierarchy.getNodeNumber(); i++) {
			node = hierarchy.getNodes().get(i);
			if (node.getNodeType().equals("LEAF") && node.getName().equals(leafName)) {
				parentNode = node.getParentId();
				ancestors.add(node);
				break;
			}
		}

		/* root will be included */
		/*
		 * While parent node is not the hierarchy root, search for the parent node
		 */
		while (parentNode > 0) {
			for (int i = 0; i < hierarchy.getNodeNumber(); i++) {
				if (hierarchy.getNodes().get(i).getId() == parentNode) {
					/* found an ancestor */
					parentNode = hierarchy.getNodes().get(i).getParentId();
					ancestors.add(hierarchy.getNodes().get(i));
					break;
				}
			}
		}
		return ancestors.toArray(new HierarchyNode[ancestors.size()]);
	}

	/* retrieve hierarchies associated with the given file */
	protected static Hierarchy[] retrieveHierarchiesForFile(String fileName, AugmentationObject augmObj) {
		ArrayList<Hierarchy> hierarchies = new ArrayList<Hierarchy>();
		ArrayList<String> hierarchyNames = new ArrayList<String>();

		/* retrieve hierarchy names associated with this file */
		for (int i = 0; i < augmObj.getMappings().length; i++) {

			if (!augmObj.getMappings()[i].getFileName().equals("")
					&& augmObj.getMappings()[i].getFileName().equals(fileName))
				hierarchyNames.add(augmObj.getMappings()[i].getHierarchyName());
		}

		/* retrieve hierarchies from mainObj */
		for (int k = 0; k < hierarchyNames.size(); k++) {
			for (int j = 0; j < augmObj.getHierarchies().length; j++) {
				if (augmObj.getHierarchies()[j].getHierarchyName().equals(hierarchyNames.toArray()[k].toString())) {
					hierarchies.add(augmObj.getHierarchies()[j]);
				}
			}
		}

		return hierarchies.toArray(new Hierarchy[hierarchies.size()]);
	}

	/* retrieve mappings associated with the given file */
	protected static Mapping[] retrieveMappingForFile(String fileName, AugmentationObject augmObj) {
		ArrayList<Mapping> fileMapping = new ArrayList<Mapping>();

		/* retrieve mappings associated with this file */
		for (int i = 0; i < augmObj.getMappings().length; i++) {

			if (!augmObj.getMappings()[i].getFileName().equals("")
					&& augmObj.getMappings()[i].getFileName().equals(fileName))
				fileMapping.add(augmObj.getMappings()[i]);
		}

		return fileMapping.toArray(new Mapping[fileMapping.size()]);
	}
}