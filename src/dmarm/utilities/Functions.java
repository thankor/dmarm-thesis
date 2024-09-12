package dmarm.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Functions {

	private static final String ROOT_NODE = "ROOT";
	private static final String NODE = "NODE";
	private static final String LEAF_NODE = "LEAF";

	/* read a configuration file */
	public static XMLDto readXMLConfigurationFile(String filename) {
		String[] propertyValues = null;
		String[] propertyNames = null;
		XMLDto xmlDto = null;

		try {
			File fileToRead = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileToRead);
			doc.getDocumentElement().normalize();
			xmlDto = new XMLDto();
			xmlDto.setRootName(doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("property");
			propertyNames = new String[nList.getLength()];
			propertyValues = new String[nList.getLength()];
			xmlDto.setPropertyNumber(nList.getLength());

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					propertyNames[temp] = eElement.getAttribute("name");
					propertyValues[temp] = eElement.getElementsByTagName("value").item(0).getTextContent();
				}
			}
			xmlDto.setPropertyNames(propertyNames);
			xmlDto.setPropertyValues(propertyValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlDto;
	}

	/* read a hierarchy file */
	public static Hierarchy readXMLHierarchyFile(String filename) {

		Hierarchy hierarchy = new Hierarchy();
		HierarchyNode node = new HierarchyNode();

		try {
			File fileToRead = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileToRead);
			doc.getDocumentElement().normalize();

			hierarchy.setHierarchyName(doc.getDocumentElement().getAttribute("name"));
			// set levels
			hierarchy.setLevels(Integer.parseInt(doc.getDocumentElement().getAttribute("levels")));
			// set node number
			hierarchy.setNodeNumber(Integer.parseInt(doc.getDocumentElement().getAttribute("nodes")));

			NodeList nList = doc.getElementsByTagName("node");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				node = new HierarchyNode();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					node.setLevel(Integer.parseInt(eElement.getAttribute("level")));

					switch (eElement.getAttribute("type").toString().toUpperCase()) {
					case ROOT_NODE:
						node.setNodeType(ROOT_NODE);
						break;
					case NODE:
						node.setNodeType(NODE);
						break;
					case LEAF_NODE:
						node.setNodeType(LEAF_NODE);
						break;
					default:
						node.setNodeType(null);
						break;
					}
					node.setId(Integer.parseInt(eElement.getAttribute("id")));
					node.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
					node.setParentId(
							Integer.parseInt(eElement.getElementsByTagName("parent_node").item(0).getTextContent()));
					hierarchy.addNode(node);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hierarchy;
	}

	public static boolean fileExists(String filename) {
		File f = new File(filename);
		if (f.exists() && f.isFile())
			return true;
		else
			return false;
	}

	public static boolean deleteFile(String filename) {
		File f = new File(filename);
		if (f.delete())
			return true;
		else
			return false;
	}

	public static boolean directoryExists(String dirname) {
		File f = new File(dirname);
		if (f.exists() && f.isDirectory())
			return true;
		else
			return false;
	}

	public static boolean fileIsReadable(String filename) {
		File f = new File(filename);
		if (f.canRead() && f.isFile())
			return true;
		else
			return false;
	}

	public static boolean createDirectory(String filename) {
		File f = new File(filename);
		return f.mkdir();
	}

	public static String[] getFilesInDirectory(String dirname) {
		File f = new File(dirname);
		String[] files = f.list();
		return files;
	}

	public static File getDirectoryPath() {
		File f = new File("");
		return f.getAbsoluteFile();
	}

	public static String[] splitLine(String input_line, String delimiter) {
		String[] arguments = input_line.split(delimiter);
		return arguments;
	}

	public static String readTXTFile(String filename) {
		BufferedReader inp = null;
		StringBuffer content = null;
		try {
			File f = new File(filename);
			inp = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			content = new StringBuffer();
			String line;
			while ((line = inp.readLine()) != null)
				content.append(line).append("\n");
		} catch (IOException ex) {
			System.out.println("Error reading file" + filename + "!");
		} finally {
			try {
				inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content.toString();
	}

	public static void writeTXTFile(String filename, String context) {
		BufferedWriter out = null;

		try {
			File f = new File(filename);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			out.write(context);
		} catch (IOException ex) {
			System.out.println("Error writing file" + filename + "!");
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void appendTXTFile(String filename, String context) {
		File f = new File(filename);
		BufferedWriter bw = null;
		FileWriter fileWriter = null;
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileWriter = new FileWriter(filename, true);
			bw = new BufferedWriter(fileWriter);
			bw.write(context);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isEmptyOrNullString(String input) {
		if (input.equals(null) || input.trim().equals(""))
			return true;
		else
			return false;
	}

	/*
	 * return the number of files in a given directory or "-1" if the directory
	 * doesn't exist
	 */
	protected static int countFiles(String filePath) {
		int count = 0;
		String[] files = null;
		if (Functions.directoryExists(filePath)) {
			files = Functions.getFilesInDirectory(filePath);
			count = files.length;
			return count;
		} else
			return -1;// Directory not found
	}

}
