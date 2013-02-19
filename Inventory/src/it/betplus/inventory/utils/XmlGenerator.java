package it.betplus.inventory.utils;

import it.betplus.inventory.db.dao.MapDAO;
import it.betplus.inventory.db.dao.OfficeDAO;
import it.betplus.inventory.primitive.Map;
import it.betplus.inventory.primitive.Office;

import java.io.File;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlGenerator {

	protected static final Log log = LogFactory.getLog(XmlGenerator.class);
	private static XmlGenerator instance;

	private XmlGenerator() {
	}

	public static synchronized XmlGenerator getInstance() {
		if (instance == null) {
			synchronized (XmlGenerator.class) {
				instance = new XmlGenerator();
			}

		}
		return instance;
	}

	public static void doXmlMaps() {
		try {
			
			List<Map> allmap = MapDAO.getInstance().getMaps();
			
			for(Map m:allmap){
			

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			List<Office> officeList = OfficeDAO.getInstance().getOffices(m.getId());
			Element rootElement = doc.createElement("rooms");
			doc.appendChild(rootElement);

			for (Office off : officeList) {
				Element officeNode = doc.createElement("room");
				rootElement.appendChild(officeNode);

				Attr attr = doc.createAttribute("id");
				attr.setValue(off.getId() + "");
				officeNode.setAttributeNode(attr);

				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(off.getName()));
				officeNode.appendChild(name);

				Element id_map = doc.createElement("floor");
				id_map.appendChild(doc.createTextNode(m.getFloor() + ""));
				officeNode.appendChild(id_map);

				Element pos = doc.createElement("palace");
				pos.appendChild(doc.createTextNode(m.getBuild() + ""));
				officeNode.appendChild(pos);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			ServletContext ctx = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String deploymentDirectoryPath = ctx.getRealPath("/");

			new File(deploymentDirectoryPath+"maps").mkdir();
			
			StreamResult result = new StreamResult(new File(
				deploymentDirectoryPath + "maps\\"+m.getNameFile()));

			//Output to console for testing
			//StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			log.info("File saved in " + deploymentDirectoryPath
					+ "maps\\"+m.getNameFile());
			
			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
