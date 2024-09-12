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

import dmarm.utilities.Rule;

public class ReportGenerator {

	public static void generateReportFile(ResultsObject resObj) {

		String filePath = null;
		Rule[] rules = null;
		try {
			rules = resObj.getRules();
			filePath = resObj.getOutputFolder() + "\\GeneratedReport.xml";
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element rootElement = doc.createElement("report");
			rootElement.setAttribute("name", "DMARM Report");
			rootElement.setAttribute("rules", Integer.toString(rules.length));
			rootElement.setAttribute("minSupport", Double.toString(resObj.getMinimumSupport()));
			rootElement.setAttribute("minConfidence", Double.toString(resObj.getMinimumConfidence()));
			doc.appendChild(rootElement);

			for (int i = 0; i < rules.length; i++) {
				Element rule = doc.createElement("rule");
				rule.setAttribute("id", Integer.toString(i + 1));
				rootElement.appendChild(rule);
				Element antecedent = doc.createElement("antecedent");
				antecedent.appendChild(doc.createTextNode(rules[i].getAntecedent().toString()));
				rule.appendChild(antecedent);
				Element consequent = doc.createElement("consequent");
				consequent.appendChild(doc.createTextNode(rules[i].getConsequent().toString()));
				rule.appendChild(consequent);
				Element confidence = doc.createElement("confidence");
				confidence.appendChild(doc.createTextNode(rules[i].getConfidence().toString()));
				rule.appendChild(confidence);
				Element support = doc.createElement("support");
				support.appendChild(doc.createTextNode(rules[i].getSupport().toString()));
				rule.appendChild(support);
			}
			doc.normalize();
			doc.setXmlStandalone(true);
			/* write the content into xml file */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
