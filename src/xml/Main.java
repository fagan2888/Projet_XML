package xml;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import controller.GUIController;
import ui.GUI;
import util.Rechercher;

public class Main {

	public static void main(String[] args) {
		Rechercher model;
		try {
			model = new Rechercher();
			GUIController controller = new GUIController(model);
			GUI gui = new GUI(controller);
			gui.setVisible(true);
		} catch (SQLException e) {
			System.out.println("Impossible de se connecter à la BDD");
		}
		// TODO Auto-generated method stub

		/*
		 Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(src));
		 DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());
		 XMLSignature signature = fac.newXMLSignature(si, ki);
		 signature.sign(dsc);
		 TransformerFactory tf = TransformerFactory.newInstance();
		 Transformer trans = tf.newTransformer();
		*/
		/* trans.setOutputProperty(OutputKeys.INDENT, "yes");
		 trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");*/
		 //trans.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(dest)));
		

		// webapp example xsd: 
		// URL schemaFile = new URL("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd");
		// local file example:
		// File schemaFile = new File("/location/to/localfile.xsd"); // etc.
		
		// "C:\\Users\\Thierry\\eclipse-workspace\\XML\\envelopeSelectSigned.xml"
		
	}

}
