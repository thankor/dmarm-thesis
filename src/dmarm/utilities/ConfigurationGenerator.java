package dmarm.utilities;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConfigurationGenerator {

	public static boolean generateConfigurationFile(AugmentationObject augmObj) {

		boolean fileGenerationResult;
		String filePath = null;
		try {

			filePath = augmObj.getOutputFolder() + "\\ConfigurationFile.xml";
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element rootElement = doc.createElement("configuration");
			doc.appendChild(rootElement);

			Element input_folder = doc.createElement("property");
			input_folder.setAttribute("name", "input_folder");
			Element value2 = doc.createElement("value");
			value2.appendChild(doc.createTextNode(augmObj.getInputFolder()));
			input_folder.appendChild(value2);
			rootElement.appendChild(input_folder);

			Element output_folder = doc.createElement("property");
			output_folder.setAttribute("name", "output_folder");
			Element value4 = doc.createElement("value");
			value4.appendChild(doc.createTextNode(augmObj.getOutputFolder()));
			output_folder.appendChild(value4);
			rootElement.appendChild(output_folder);

			Element hierarchies_folder = doc.createElement("property");
			hierarchies_folder.setAttribute("name", "hierarchies_folder");
			Element value3 = doc.createElement("value");
			value3.appendChild(doc.createTextNode(augmObj.getHierarchyFilesFolder()));
			hierarchies_folder.appendChild(value3);
			rootElement.appendChild(hierarchies_folder);

			Element mapping_file = doc.createElement("property");
			mapping_file.setAttribute("name", "mapping_file");
			Element value8 = doc.createElement("value");
			value8.appendChild(doc.createTextNode(augmObj.getMappingFile()));
			mapping_file.appendChild(value8);
			rootElement.appendChild(mapping_file);

			doc.normalize();
			doc.setXmlStandalone(true);

			/* write the content into xml file */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
			fileGenerationResult = true;
		} catch (Exception e) {
			e.printStackTrace();
			fileGenerationResult = false;
		}

		return fileGenerationResult;
	}
}
