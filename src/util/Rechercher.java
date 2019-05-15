package util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xml.Main;

public class Rechercher {

	/* 
	 * SQL:
	 * PrepareStatement, contre les injections SQL
	 * Vérification des noms de tables
	 * 
	 * XML:
	 * Expansion d'entités
	 * 	limiter la taille totale du document XML à canoniser
	 * 	identifier et enlever les déclarations de DTD
	 * Injection de transformations
	 * 	Restreindre le nombre total des transformation
	 * 	Rejeter (par schema) tout élément référence ou retrievalMethod précisant multiples C14N transformations
	 * Injection de XPath
	 * 	Ne pas traiter KeyInfo
	 * 	Restreindre le nombre total des transformations
	 *  Refuser (par schema) toute Reference RetrievalMethod précisant des expressions XPath
	 * Injection de XSLT
	 * 	désactiver XSLT par schema
	 * 
	 * 
	 * 
	 */
	public final static String QUERY_SELECT = "SELECT";
	public final static String QUERY_INSERT = "INSERT";
	public final static String QUERY_DELETE = "DELETE";
	public final static String QUERY_UPDATE = "UPDATE";

	public final static String INFORMATIONS_INDEX = "informations";
	
	public final static SimpleDateFormat DEFAULT_FILE_NAME = new SimpleDateFormat("ddMMyy-hhmmss.SSS");

	private File xmlFile;

	public File getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(File file) {
		this.xmlFile = file;
	}
	
	public File execute() throws Exception{
		if (xmlFile == null)
			throw new Exception("You didn't set a file");
		return xmlQuery(xmlFile);
	}
	
	public File querySelectToFile(PreparedStatement ps) {
		System.out.println(ps.toString());
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        File result = null;
		try {
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
	        // root element
	        Element root = document.createElement("result");
	        document.appendChild(root);
	        
	        while(rs.next()) {
	        	Element row = document.createElement("row");
	        	for(int i = 1; i <= columnCount; i++) {
	        		Element value = document.createElement(rsmd.getColumnName(i));
	        		value.setTextContent(rs.getString(i));
	        		row.appendChild(value);
	        	}
	        	root.appendChild(row);
	        }
	        
	        XMLSign sign = new XMLSign();
	        
	        String fileName = String.format("%s.%s", DEFAULT_FILE_NAME.format( new Date() ), ".xml");
	        if(sign.generate(fileName, document))
	        	return new File(fileName);
	        
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

		return result;
	}
	public File queryUpdateToFile(PreparedStatement ps) {
		System.out.println(ps.toString());
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        File result = null;
		try {
			int rowCount = ps.executeUpdate();
			documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
	        // root element
	        Element root = document.createElement("result");
	        document.appendChild(root);
	        
	        Element count = document.createElement("rowUpdatedCount");
	        count.setTextContent(String.valueOf(rowCount));
	        root.appendChild(count);
	        
	        XMLSign sign = new XMLSign();

	        String fileName = String.format("%s.%s", DEFAULT_FILE_NAME.format( new Date() ), ".xml");
	        if(sign.generate(fileName, document))
	        	return new File(fileName);
	        
	        
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

		return result;
	}

	public File xmlQuery(File file) throws Exception {
		// Validation du fichier
		validateXmlFile(file);
		// Check signature
		XMLSign sign = new XMLSign();
		if(!sign.check(file.getAbsolutePath()))
			throw new Exception("Signature failed");

		// Traitement des informations du fichier
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xml = builder.parse(file);

		Element root = (Element) xml.getDocumentElement();

		return xmlQueryAction(((Node) root).getNodeName(), root);
	}

	private void validateXmlFile(File file) {
		Source xmlFile = new StreamSource(file);
		File schemaFile = new File(Main.class.getClassLoader().getResource("validate.xsd").getFile());
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			System.out.println(xmlFile.getSystemId() + " is valid");
		} catch (SAXException e) {
			System.out.println(xmlFile.getSystemId() + " is NOT valid reason:" + e);
			e.printStackTrace();
		} catch (IOException e) {
		}
	}

	private File xmlQueryAction(String action, Element root) throws Exception {
		File result = null;
		if (QUERY_INSERT.equals(action)) {
			result = queryUpdateToFile(getQueryInsert(root));
		} else if (QUERY_UPDATE.equals(action)) {
			result = queryUpdateToFile(getQueryUpdate(root));
		} else if (QUERY_DELETE.equals(action)) {
			result = queryUpdateToFile(getQueryDelete(root));
		} else if (QUERY_SELECT.equals(action)) {
			result = querySelectToFile(getQuerySelect(root));
		} else {
			System.out.println("Invalid query action : " + action);
		}
		return result;
	}

	private ArrayList<String> tablesExists(Connection cnx, ArrayList<String> tables) throws SQLException {
		ArrayList<String> remainingTables = new ArrayList<String>(tables);
		DatabaseMetaData meta = cnx.getMetaData();
		ResultSet rs = meta.getTables(null, null, "%", null);
		while (rs.next()) {
			for (String t : remainingTables) {
				if (rs.getString("TABLE_NAME").equals(t))
					remainingTables.remove(t);
				break;
			}
		}
		return remainingTables;
	}

	private PreparedStatement getQuerySelect(Element root) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

		final String queryType = QUERY_SELECT;
		StringBuilder query = new StringBuilder("SELECT ");
		String expression;
		NodeList nList;
		Node node;
		Connection cnx = null;
		PreparedStatement ps = null;
		ArrayList<String> undefinedIdentifiers;

		ArrayList<String> selectParameters = new ArrayList<String>();
		ArrayList<String> fromParameters = new ArrayList<String>();
		String conditionParameter = null;

		cnx = ConnexionBDD.getInstance().getCnx();

		/* GESTION DES CHAMPS */
		expression = String.format("/%s/CHAMPS/CHAMP", queryType);
		nList = (NodeList) path.evaluate(expression, root, XPathConstants.NODESET);

		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			selectParameters.add(node.getTextContent().trim());
			query.append("?");
			if (i < nList.getLength() - 1)
				query.append(",");
			query.append(" ");
		}
		/* /GESTION DES CHAMPS */

		/* GESTION DES TABLES */
		expression = String.format("/%s/TABLES/TABLE", queryType);
		nList = (NodeList) path.evaluate(expression, root, XPathConstants.NODESET);
		query = query.append("FROM ");
		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			fromParameters.add(node.getTextContent().trim());
			query.append(node.getTextContent().trim());
			if (i < nList.getLength() - 1)
				query.append(",");
			query.append(" ");
		}

		// Vérification des tables
		undefinedIdentifiers = tablesExists(cnx, fromParameters);
		if (!undefinedIdentifiers.isEmpty()) {
			throw new Exception(String.format("Les tables %s n'existent pas.", String.join(",", undefinedIdentifiers)));
		}
		/* /GESTION DES TABLES */

		/* GESTION DE LA CONDITION */
		expression = String.format("/%s/CONDITION", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		if (node != null) {
			query.append("WHERE " + node.getTextContent().trim());
//			conditionParameter = node.getTextContent().trim();
		}
		/* /GESTION DE LA CONDITION */

		ps = cnx.prepareStatement(query.toString());
		int p = 1;
		for (String s : selectParameters)
			ps.setString(p++, s);
//		for (String s : fromParameters)
//			ps.setString(p++, s);
//		if (conditionParameter != null)
//			ps.setString(p++, conditionParameter);
		return ps;
	}

	private PreparedStatement getQueryInsert(Element root) throws XPathExpressionException, SQLException {
		StringBuilder query = new StringBuilder("INSERT INTO ");
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

		final String queryType = QUERY_INSERT;
		String expression;
		NodeList nList;
		Node node;
		Connection cnx = null;
		PreparedStatement ps = null;
		
		
		cnx = ConnexionBDD.getInstance().getCnx();
		ResultSet rs = null;
		DatabaseMetaData meta;
		int columnsCount = 0;
		
		ArrayList<String> valuesParameters = new ArrayList<String>();
		
	
		

		/* GESTION DE LA TABLE */
		expression = String.format("/%s/TABLE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);


		query.append(node.getTextContent().trim());
		
		meta = cnx.getMetaData();
		rs = meta.getColumns(cnx.getCatalog(), null, node.getTextContent().trim().toUpperCase(), "%");
		while (rs.next()) {
			columnsCount ++;
		}
		/* /GESTION DE LA TABLE */

		
		
		
		/* GESTION DES VALEURS */
		query.append(" VALUES");
		expression = String.format("/%s/VALUES", queryType);
		nList = (NodeList) path.evaluate(expression, root, XPathConstants.NODESET);
		
		// Pour chaque balise VALUES
		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			NodeList nListChild = node.getChildNodes();
			Node childNode;
			
			if(i!=0)
				query.append(",");
			query.append("(");
			
			int nbChilds = nListChild.getLength();
			if(nbChilds != columnsCount)
				throw new Error("Not enough values");
			// Pour chaque balise VALUE
			for (int j = 0; j < nbChilds; j++) {
				childNode = nListChild.item(j);
				if (childNode.getNodeType() != Node.ELEMENT_NODE)
					continue;
				valuesParameters.add(childNode.getTextContent().trim());
				if(j != 0)
					query.append(",");
				query.append("?");
			}
			query.append(")");
		}
		/* /GESTION DES VALEURS */
		ps = cnx.prepareStatement(query.toString());
		int p = 1;
		for(String value: valuesParameters)
			ps.setString(p++, value);
		
		return ps;
	}

	private PreparedStatement getQueryUpdate(Element root) throws Exception {
		StringBuilder query = new StringBuilder("UPDATE ? SET ? = ?");
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

		final String queryType = QUERY_UPDATE;
		String expression;
		Node node;
		Connection cnx = null;
		PreparedStatement ps = null;
		
		cnx = ConnexionBDD.getInstance().getCnx();
		
		ArrayList<String> undefinedIdentifiers = new ArrayList<String>();

		ArrayList<String> fromParameters = new ArrayList<String>();
		String conditionParameter = null;
		
		/* GESTION DE LA TABLE */
		expression = String.format("/%s/TABLE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);
		
		query.append(node.getTextContent().trim());
		
		// Vérification des tables
		undefinedIdentifiers = tablesExists(cnx, fromParameters);
		if (!undefinedIdentifiers.isEmpty()) {
			throw new Exception(String.format("Les tables %s n'existent pas.", String.join(",", undefinedIdentifiers)));
		}
		/* /GESTION DE LA TABLE */
		
		
		/* GESTION DE LA CONDITION */
		expression = String.format("/%s/CONDITION", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		if (node != null) {
			query.append("WHERE "+node.getTextContent().trim());
//			conditionParameter = node.getTextContent().trim();
		}
		/* /GESTION DE LA CONDITION */
		ps = cnx.prepareStatement(query.toString());
//		ps.setString(4, conditionParameter);
		
		
		

		/* GESTION DU SET */
		expression = String.format("/%s/CHAMP", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		ps.setString(1,node.getTextContent().trim());

		
		expression = String.format("/%s/VALUE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		ps.setString(2,node.getTextContent().trim());
		/* /GESTION DU SET */

		
		return ps;
	}

	private PreparedStatement getQueryDelete(Element root) throws Exception {
		StringBuilder query = new StringBuilder("DELETE FROM ");
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

		final String queryType = QUERY_DELETE;
		String expression;
		Node node;
		Connection cnx = null;
		PreparedStatement ps = null;
		
		cnx = ConnexionBDD.getInstance().getCnx();

		ArrayList<String> undefinedIdentifiers;
		
		ArrayList<String> fromParameters = new ArrayList<String>();
		String conditionParameter = null;


		/* GESTION DE LA TABLE */
		expression = String.format("/%s/TABLE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		query.append(node.getTextContent().trim());
		
		// Vérification des tables
		undefinedIdentifiers = tablesExists(cnx, fromParameters);
		if (!undefinedIdentifiers.isEmpty()) {
			throw new Exception(String.format("Les tables %s n'existent pas.", String.join(",", undefinedIdentifiers)));
		}
		/* /GESTION DE LA TABLE */
		
		

		/* GESTION DE LA CONDITION */
		expression = String.format("/%s/CONDITION", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		if (node != null) {
			query.append("WHERE "+node.getTextContent().trim());
//			conditionParameter = node.getTextContent().trim();
		}
		/* /GESTION DE LA CONDITION */
		ps = cnx.prepareStatement(query.toString());
//		ps.setString(2, conditionParameter);
		
		
		
		return ps;
	}

}
