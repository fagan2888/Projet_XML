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
import java.util.HashSet;
import java.util.Map;
import java.util.StringJoiner;

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


	public final static String QUERY_SELECT = "SELECT";
	public final static String QUERY_INSERT = "INSERT";
	public final static String QUERY_DELETE = "DELETE";
	public final static String QUERY_UPDATE = "UPDATE";
	
	private final static String ATTR_TABLE = "table";
	private final static String ATTR_ALIAS = "alias";

	public final static String INFORMATIONS_INDEX = "informations";
	
	public final static SimpleDateFormat DEFAULT_FILE_NAME = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
	
	private HashMap<String,ArrayList<String>> bddTables;

	private File xmlFile;
	
	public Rechercher() throws SQLException {
		gatherTablesColumns();
	}
	
	private void gatherTablesColumns() throws SQLException {
		bddTables = new HashMap<String, ArrayList<String>>();
		Connection cnx = ConnexionBDD.getInstance().getCnx();

		DatabaseMetaData dbmd = cnx.getMetaData();

	    ResultSet resultSet = dbmd.getColumns(null, null, "%", "%");
	    
	    while (resultSet.next()) {
		    String tableName = resultSet.getString("TABLE_NAME");
	    	ArrayList<String> columns = bddTables.get(tableName);
	    	
	    	if(columns == null) {
	    		columns = new ArrayList<String>();
	    		bddTables.put(tableName, columns);
	    	}
	    	columns.add(resultSet.getString("COLUMN_NAME"));
	    }
	}

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
	
	public File querySelectToFile(PreparedStatement ps) throws ParserConfigurationException {
		System.out.println(ps.toString());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        File result = null;
		try {
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			documentBuilder = dbf.newDocumentBuilder();
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

//        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xml = builder.parse(file);

		Element root = (Element) xml.getDocumentElement();

		return xmlQueryAction(((Node) root).getNodeName(), root);
	}

	private void validateXmlFile(File file) throws Exception {
		Source xmlFile = new StreamSource(file);
		File schemaFile = new File(Main.class.getClassLoader().getResource("validate.xsd").getFile());
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

//		schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//		schemaFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//		System.setProperty("javax.xml.accessExternalSchema", "all");
		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new Exception("Invalid xml");
		} catch (IOException e) {
			throw new Exception("Error during xml validation");
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
	
	public void handleCondition(TablesManager tm, StringBuilder sb, ArrayList<String> parameters, Node node) throws DOMException, Exception {
		NodeList nList = node.getChildNodes();
		
		int conditionsCount = 0;
		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			String nodeName = node.getNodeName();
			if(nodeName.equals("OR") || nodeName.equals("AND")) {
				NodeList nodeList2 = node.getChildNodes();
				Node node2;
				sb.append("(");
				for(int j = 0, operateurCount = 0; j < nodeList2.getLength(); j++) {
					node2 = nodeList2.item(j);
					if (node2.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if(operateurCount > 0) {
						sb.append(" ");
						sb.append(nodeName);
						sb.append(" ");
					}
					if(!(node2.getNodeName().equals("AND") || node2.getNodeName().equals("OR")))
						handleOperator(tm, sb, parameters, node2);
					else
						handleCondition(tm, sb, parameters, node2);
					operateurCount ++;
				}
				sb.append(")");
			}else {
				handleOperator(tm, sb, parameters, node);
			}
		}
	}

	public void handleOperator(TablesManager tm, StringBuilder sb, ArrayList<String> parameters, Node node) throws DOMException, Exception {
		NodeList nodeList2 = node.getChildNodes();
		Node node2;
		String operator = null;
		if(node.getNodeName().equals("EQUALS"))
			operator = "=";
		if(node.getNodeName().equals("GREATER"))
			operator = ">";
		if(node.getNodeName().equals("LESS"))
			operator = "<";
		for(int j = 0, operateurCount = 0; j < nodeList2.getLength() && operateurCount <2; j++) {
			node2 = nodeList2.item(j);
			if (node2.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if(node2.getNodeName().equals("VALUE")) {
				sb.append("?");
				parameters.add(node2.getTextContent());
			}else {
				Column c = tm.addColumn(node2);
				sb.append(c.toQuery());
			}
			if(operateurCount == 0) {
				sb.append(operator);
			}
			operateurCount ++;
		}
		sb.append(" ");
		
	}

	private PreparedStatement getQuerySelect(Element root) throws Exception {
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

		final String queryType = QUERY_SELECT;
		StringBuilder query = new StringBuilder("SELECT ");
		StringBuilder queryFrom = new StringBuilder();
		String expression;
		NodeList nList;
		Node node;
		Connection cnx = null;
		PreparedStatement ps = null;

		ArrayList<Column> selectParameters = new ArrayList<Column>();
		ArrayList<Table> fromParameters = new ArrayList<Table>();
		ArrayList<String> conditionParameters = new ArrayList<String>();

		TablesManager tm = new TablesManager();
		
		cnx = ConnexionBDD.getInstance().getCnx();

		

		/* GESTION DES TABLES */
		expression = String.format("/%s/TABLES/TABLE", queryType);
		nList = (NodeList) path.evaluate(expression, root, XPathConstants.NODESET);
		queryFrom.append("FROM ");
		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			Table f = tm.addTable(node);
			fromParameters.add(f);
			queryFrom.append(f.toQuery());
			if (i < nList.getLength() - 1)
				queryFrom.append(",");
			queryFrom.append(" ");
		}
		/* /GESTION DES TABLES */
		
		/* GESTION DES CHAMPS */
		expression = String.format("/%s/CHAMPS/CHAMP", queryType);
		nList = (NodeList) path.evaluate(expression, root, XPathConstants.NODESET);

		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			Column s = tm.addColumn(node);
			selectParameters.add(s);
			query.append(s.toQuery());
			if (i < nList.getLength() - 1)
				query.append(",");
			query.append(" ");
		}
		/* /GESTION DES CHAMPS */
		
		query.append(queryFrom);

		/* GESTION DE LA CONDITION */
		expression = String.format("/%s/CONDITION", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		if (node != null) {
			query.append("WHERE ");
			handleCondition(tm,query,conditionParameters,node);
		}
		/* /GESTION DE LA CONDITION */

		tm.checkAllExist();
		ps = cnx.prepareStatement(query.toString());
		
		int p = 1;
		for(String s: conditionParameters)
			ps.setString(p++, s);

		return ps;
	}

	private PreparedStatement getQueryInsert(Element root) throws Exception {
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
		
		int columnsCount = 0;
		
		ArrayList<String> valuesParameters = new ArrayList<String>();
		
		TablesManager tm = new TablesManager();
	
		

		/* GESTION DE LA TABLE */
		expression = String.format("/%s/TABLE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		Table table = tm.addTable(node);
		
		query.append(table.toQuery());
		
		tm.checkAllExist(); // obligé de l'utiliser ici pour vérifier que la table existe afin de récupérer son nombre de colonnes
		columnsCount = bddTables.get(table.getName()).size();
		/* /GESTION DE LA TABLE */

		
		
		
		/* GESTION DES VALEURS */
		query.append(" VALUES");
		expression = String.format("/%s/VALUES", queryType);
		nList = (NodeList) path.evaluate(expression, root, XPathConstants.NODESET);
		
		int valuesCount = 0;
		// Pour chaque balise VALUES
		for (int i = 0; i < nList.getLength(); i++) {
			node = nList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			NodeList nListChild = node.getChildNodes();
			Node childNode;
			
			if(valuesCount++ !=0)
				query.append(",");
			query.append("(");
			
			
			int childsCount = 0;
			// Pour chaque balise VALUE
			for (int j = 0; j < nListChild.getLength(); j++) {
				childNode = nListChild.item(j);
				if (childNode.getNodeType() != Node.ELEMENT_NODE)
					continue;
				valuesParameters.add(childNode.getTextContent());
				if(childsCount++ != 0)
					query.append(",");
				query.append("?");
			}
			query.append(")");
			if(childsCount != columnsCount)
				throw new Error(String.format("Not enough values, needed %d (found: %d).",columnsCount, childsCount));
			
		}

		tm.checkAllExist();
		
		/* /GESTION DES VALEURS */
		ps = cnx.prepareStatement(query.toString());
		int p = 1;
		for(String value: valuesParameters)
			ps.setString(p++, value);
		
		return ps;
	}

	private PreparedStatement getQueryUpdate(Element root) throws Exception {
		StringBuilder query = new StringBuilder("UPDATE ");
		XPathFactory xpf = XPathFactory.newInstance();
		XPath path = xpf.newXPath();

		final String queryType = QUERY_UPDATE;
		String expression;
		Node node;
		Connection cnx = null;
		PreparedStatement ps = null;
		
		cnx = ConnexionBDD.getInstance().getCnx();
		
		TablesManager tm = new TablesManager();

		ArrayList<String> conditionParameters = new ArrayList<String>();
		
		/* GESTION DE LA TABLE */
		expression = String.format("/%s/TABLE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);
		
		Table f = tm.addTable(node);
		query.append(f.toQuery());
		/* /GESTION DE LA TABLE */
		
		
		/* GESTION DU SET */
		expression = String.format("/%s/CHAMP", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);
		Column column = tm.addColumn(node);
		query.append("SET ");
		query.append(column.toQuery());
		
		query.append(" = ? ");
		/* /GESTION DU SET*/
		
		/* GESTION DE LA CONDITION */
		expression = String.format("/%s/CONDITION", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		if (node != null) {
			query.append("WHERE ");
			handleCondition(tm,query,conditionParameters,node);
		}
		/* /GESTION DE LA CONDITION */
		tm.checkAllExist();
		ps = cnx.prepareStatement(query.toString());
		

		// Ajout de la valeur du set
		expression = String.format("/%s/VALUE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);
		int p = 1;
		ps.setString(p++,node.getTextContent());
		
		
		for(String s: conditionParameters)
			ps.setString(p++, s);
		
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
		
		TablesManager tm = new TablesManager();
		
		ArrayList<String> conditionParameters = new ArrayList<String>();


		/* GESTION DE LA TABLE */
		expression = String.format("/%s/TABLE", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		
		Table f = tm.addTable(node);
		query.append(f.toQuery());
		
		/* /GESTION DE LA TABLE */
		
		

		/* GESTION DE LA CONDITION */
		expression = String.format("/%s/CONDITION", queryType);
		node = (Node) path.evaluate(expression, root, XPathConstants.NODE);

		if (node != null) {
			query.append(" WHERE ");
			handleCondition(tm,query,conditionParameters,node);
		}
		/* /GESTION DE LA CONDITION */
		tm.checkAllExist();
		ps = cnx.prepareStatement(query.toString());
		
		int p = 1;
		for(String s: conditionParameters)
			ps.setString(p++, s);
		
		return ps;
	}
	
	public class TablesManager{
		HashSet<Table> tables;
		
		public TablesManager() {
			tables = new HashSet<Table>();
		}
		
		public Table addTable(Node node) {
			Table table = null;
			String tableName = node.getTextContent().trim();
			for(Table t: tables)
				if(t.getName().equals(tableName))
					table = t;
			if(table == null) {
				String alias = null;
				if(node.getAttributes().getNamedItem(ATTR_ALIAS) != null)
					alias = node.getAttributes().getNamedItem(ATTR_ALIAS).getNodeValue();
				table = new Table(tableName, alias);
				tables.add(table);
			}
			return table;
		}
		
		public Column addColumn(Node node) throws DOMException, Exception {
			return addColumn(node.getAttributes().getNamedItem(ATTR_TABLE).getNodeValue(),
					node.getTextContent().trim());
		}
		public Column addColumn(String tableName, String columnName) throws Exception {
			Column column = null;
			for(Table t: tables) {
				if(t.getName().equals(tableName) || t.getAlias().equals(tableName)) {
					column = t.addColumn(columnName);
				}
			}
			if(column == null)
				throw new Exception(String.format("La table %s n'a pas été déclarée.", tableName));
			return column;
		}
		
		public void checkAllExist() throws Exception {
			StringBuilder sb = new StringBuilder();
			for(Table t: tables) {
				ArrayList<String> bddTable = bddTables.get(t.getName());
				if(bddTable == null) {
					sb.append(t.getName());
					sb.append(" ");
				}else {
					for(Column c: t.getColumns()) {
						if(!bddTable.contains(c.getName())) {
							sb.append(t.getName());
							sb.append(".");
							sb.append(c.getName());
							sb.append(" ");
						}
					}
				}
			}
			if(sb.length() > 0)
				throw new Exception(String.format("Erreur: tables ou colonnes inexistantes: %s", sb.toString()));
		}
	}
	
	public class Column{
		private Table table;
		private String name;
		
		public Column(Table table,String name) {
			this.table = table;
			this.name = name;
		}
		
		public Table getTable() {
			return table;
		}
		public String getName() {
			return name;
		}
		public String toQuery() {
			StringBuilder sb = new StringBuilder();
			sb.append("`");
			sb.append(table.getAlias());
			sb.append("`");
			sb.append(".");
			sb.append("`");
			sb.append(name);
			sb.append("`");
			return sb.toString();
		}
	}
	
	public class Table{
		private String name;
		private String alias;
		private boolean hasAlias;
		private HashSet<Column> columns;
		
		public Table(String name,String alias) {
			this.name = name;
			this.alias = alias;
			this.hasAlias = alias != null;
			this.columns = new HashSet<Column>();
		}
		
		public String getName() {
			return name;
		}
		public String getAlias() {
			if(!hasAlias)
				return name;
			return alias;
		}
		public HashSet<Column> getColumns(){
			return columns;
		}
		public Column addColumn(String columnName) {
			Column column = null;
			for(Column c: columns) {
				if(c.getName().equals(columnName))
					column = c;
			}
			if(column == null){
				column = new Column(this, columnName);
			}
			return column;
		}
		public String toQuery() {
			StringBuilder sb = new StringBuilder();
			sb.append("`");
			sb.append(name);
			sb.append("`");
			if(hasAlias) {
				sb.append(" ");
				sb.append("`");
				sb.append(alias);
				sb.append("`");
			}
			return sb.toString();
		}
	}

}
