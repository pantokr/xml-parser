package finalterm_pc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class XMLFunctions {

	int lines = 0;
	final short node_st[] = new short[] { Node.DOCUMENT_NODE, Node.ELEMENT_NODE, Node.COMMENT_NODE, Node.TEXT_NODE,
			Node.ATTRIBUTE_NODE };
	final String node_st_str[] = { "DOCUMENT", "ELEMENT", "COMMENT", "TEXT" };

	public Document isValid(String xml, String xsd) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			SchemaFactory schemaFactory = null;
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

			factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(xsd) }));

			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			Document doc = builder.parse(xml);

			return doc;
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document make(String uri) {
		Document doc = new DocumentImpl();
		doc.setXmlStandalone(true);
		
		Element root = doc.createElement("root");
		Element sub = doc.createElement("sub");
		
		doc.appendChild(root);
		root.appendChild(sub);
		
		sub.appendChild(doc.createTextNode("subtext"));
		
		return doc;
	}

	public String find(Document doc, int index) {

		Node node = find_findNode(doc.getDocumentElement(), index);
		String str = "";

		// type
		int type = node.getNodeType();
		int type_index = 0;
		for (int i = 0; i < node_st.length; i++) {
			if (node_st[i] == type) {
				type_index = i;
				break;
			}
		}
		str += "type : " + node_st_str[type_index] + "\n";

		// name
		str += "name : " + node.getNodeName() + "\n";

		// text
		str += "text : " + node.getNodeValue() + "\n";

		// parent
		Node par = node.getParentNode();
		if (par == null) {
			str += "parent : No parent\n";
		} else {
			str += "parent : " + par.getNodeName() + "\n";
		}

		// children
		NodeList children = node.getChildNodes();
		str += "children count : " + Integer.toString(children.getLength());

		return str;
	}

	public String[] find_getNodeNameList(Document doc) {

		String content[] = new String[4096];
		lines = 0;
		find_traverse(doc.getDocumentElement(), "戌", content);
		String[] new_content = Arrays.copyOfRange(content, 0, lines);

		lines = 0;
		return new_content;
	}

	public void find_traverse(Node node, String indent, String[] content) {
		if (node == null) {
			return;
		}

		int type = node.getNodeType();
		switch (type) {
		case Node.ELEMENT_NODE:
			content[lines++] = "[ELEMENT] " + node.getNodeName();
			break;

		case Node.COMMENT_NODE:
			content[lines++] = "[COMMENT] " + node.getNodeName();
			break;

		case Node.TEXT_NODE:
			content[lines++] = "[TEXT] " + node.getParentNode().getNodeName();
			break;

		case Node.ATTRIBUTE_NODE:
			content[lines++] = "[ATTRIBUTE] " + node.getParentNode().getNodeName();
			break;

		default:
			break;
		}
		// System.out.println(node.getNodeName());

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				find_traverse(children.item(i), indent + "式", content);
			}
		}
	}

	public Node find_findNode(Node node, int number) {
		if (node == null) {
			return null;
		}

		int type = node.getNodeType();
		switch (type) {
		case Node.ELEMENT_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;
		case Node.COMMENT_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;

		case Node.TEXT_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;

		case Node.ATTRIBUTE_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;

		default:
			break;
		}
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node t = find_findNode(children.item(i), number);

				if (t != null) {
					return t;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void Save(Document doc, String fileName) {
		try {
			OutputFormat format = new OutputFormat(doc);
			format.setEncoding("UTF-8");
			StringWriter stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.asDOMSerializer();

			serial.serialize(doc.getDocumentElement());

			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(stringOut.toString());
			bw.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String print(Document doc) {

		String content[] = new String[4096];
		print_traverse(doc.getDocumentElement(), "戌", content);
		int cnt = 0;
		StringBuilder sb = new StringBuilder(content.length);
		for (int k = 0; k < content.length; k++) {// displays the ______ in the text area
			if (content[cnt] == null) {
				break;
			}
			sb.append(content[cnt++]);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	private void print_traverse(Node node, String indent, String[] content) {
		if (node == null) {
			return;
		}

		int type = node.getNodeType();
		switch (type) {
		case Node.DOCUMENT_NODE:
			content[lines++] = indent + "[Document] " + node.getNodeName() + "\n";
			break;

		case Node.ENTITY_NODE:
			content[lines++] = indent + "[ENTITY] " + node.getNodeName() + "\n";
			break;

		case Node.ELEMENT_NODE:
			content[lines++] = (indent + "[Element] " + node.getNodeName()) + "\n";
			break;

		case Node.ENTITY_REFERENCE_NODE:
			content[lines++] = (indent + "[ENTITY_REFERENCE] " + node.getNodeName());
			break;

		case Node.CDATA_SECTION_NODE:
			content[lines++] = (indent + "[CDATA_SECTION] " + node.getNodeValue()) + "\n";
			break;

		case Node.COMMENT_NODE:
			content[lines++] = (indent + "[COMMENT] " + node.getNodeName()) + "\n";
			break;

		case Node.TEXT_NODE:
			if (node.getNodeValue().trim() == "") {
				break;
			}
			content[lines++] = (indent + "[TEXT] " + node.getNodeName()) + "\n";
			break;

		default:
			break;
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++)
				print_traverse(children.item(i), indent + "式", content);
		}

		if (type == node.DOCUMENT_NODE) {
			lines = 0;
		}
	} // print(Node)

	public void insert_element(Document doc, int element, String name) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = insert_findNode(doc.getDocumentElement(), element);
		Node par = node.getParentNode();

		Element item = doc.createElement(name);
		par.insertBefore(item, node);
	}

	public void insert_text(Document doc, int element, String name) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = insert_findNode(doc.getDocumentElement(), element);
		Node par = node.getParentNode();

		((Element) node).insertBefore(doc.createTextNode(name), node.getFirstChild());
	}

	public void insert_attribute(Document doc, int element, String name1, String name2) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = insert_findNode(doc.getDocumentElement(), element);

		Attr attrItem = doc.createAttribute(name1);
		attrItem.setValue(name2);
		((Element) node).setAttributeNode(attrItem);
	}

	public void insert_comment(Document doc, int element, String name) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = insert_findNode(doc.getDocumentElement(), element);
		Node par = node.getParentNode();

		((Element) node).insertBefore(doc.createComment(name), node.getFirstChild());
	}

	public String[] insert_getNodeNameList(Document doc) {
		String content[] = new String[4096];
		lines = 0;
		insert_traverse(doc.getDocumentElement(), "戌", content);
		String[] new_content = Arrays.copyOfRange(content, 0, lines);

		lines = 0;
		return new_content;
	}

	public void insert_traverse(Node node, String indent, String[] content) {
		if (node == null) {
			return;
		}

		int type = node.getNodeType();
		switch (type) {

		case Node.ELEMENT_NODE:
			content[lines++] = indent + node.getNodeName() + "\n";
			break;
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++)
				insert_traverse(children.item(i), indent + "式", content);
		}
	}

	public Node insert_findNode(Node node, int number) {
		if (node == null) {
			return null;
		}

		int type = node.getNodeType();
		switch (type) {

		case Node.ELEMENT_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node t = insert_findNode(children.item(i), number);

				if (t != null) {
					return t;
				}
			}
		}
		return null;
	}

	public void update_element(Document doc, int element, String name) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = update_findNode(doc.getDocumentElement(), element);

		doc.renameNode(node, null, name);
	}

	public void update_text(Document doc, int node_number, int content_number, String str) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = update_findNode(doc.getDocumentElement(), node_number);

		System.out.println(node.getNodeName());
		NodeList children = node.getChildNodes();
		int count = children.getLength();

		Node toUpdate = null;
		int cnt = 0;
		for (int i = 0; i < count; i++) {
			System.out.println(children.item(i).getNodeValue());
			if (children.item(i).getNodeType() == Node.TEXT_NODE) {
				if (cnt == content_number) {
					toUpdate = children.item(i);
					break;
				}
				cnt++;
			}
		}

		toUpdate.setNodeValue(str);
	}

	public void update_attribute(Document doc, int node_number, int content_number, String str1, String str2) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = delete_findNode(doc.getDocumentElement(), node_number);

		NodeList children = node.getChildNodes();
		int count = children.getLength();

		Node toUpdate = null;
		if (node.hasAttributes()) {
			NamedNodeMap attr = node.getAttributes();
			toUpdate = attr.item(content_number);
		} else {
			return;
		}

		// ((Element)toUpdate).setAttribute(str1, str2);
		((Element) node).removeAttributeNode((Attr) toUpdate);

		Attr attrItem = doc.createAttribute(str1);
		attrItem.setValue(str2);
		((Element) node).setAttributeNode(attrItem);

	}

	public void update_comment(Document doc, int node_number, int content_number, String str) {
		// type 1=element 2=text 3=attribute 4=comment

		Node node = update_findNode(doc.getDocumentElement(), node_number);

		System.out.println(node.getNodeName());
		NodeList children = node.getChildNodes();
		int count = children.getLength();

		Node toUpdate = null;
		int cnt = 0;
		for (int i = 0; i < count; i++) {
			System.out.println(children.item(i).getNodeValue());
			if (children.item(i).getNodeType() == Node.COMMENT_NODE) {
				if (cnt == content_number) {
					toUpdate = children.item(i);
					break;
				}
				cnt++;
			}
		}

		toUpdate.setNodeValue(str);
	}

	public String[] update_getNodeNameList(Document doc) {
		String content[] = new String[4096];
		lines = 0;
		insert_traverse(doc.getDocumentElement(), "戌", content);
		String[] new_content = Arrays.copyOfRange(content, 0, lines);

		lines = 0;
		return new_content;
	}

	public void update_traverse(Node node, String indent, String[] content) {
		if (node == null) {
			return;
		}

		int type = node.getNodeType();
		switch (type) {

		case Node.ELEMENT_NODE:
			content[lines++] = indent + node.getNodeName() + "\n";
			break;
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++)
				insert_traverse(children.item(i), indent + "式", content);
		}
	}

	public Node update_findNode(Node node, int number) {
		if (node == null) {
			return null;
		}

		int type = node.getNodeType();
		switch (type) {

		case Node.ELEMENT_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node t = update_findNode(children.item(i), number);

				if (t != null) {
					return t;
				}
			}
		}
		return null;
	}

	public String[] update_getContentNameList(Document doc, int type, int element) {
		String content[] = new String[4096];
		lines = 0;
		int cnt = 0;
		Node node = update_findNode(doc.getDocumentElement(), element);
		lines = 0;
		System.out.println(node.getNodeName());

		if (type == 1) {
			// text
			NodeList children = node.getChildNodes();
			int count = children.getLength();

			for (int i = 0; i < count; i++) {
				if (children.item(i).getNodeType() == Node.TEXT_NODE) {
					content[cnt++] = children.item(i).getNodeValue();
				}
			}
			String[] new_content = Arrays.copyOfRange(content, 0, cnt);

			return new_content;
		} else if (type == 2) {
			// text
			NodeList children = node.getChildNodes();
			int count = children.getLength();

			if (node.hasAttributes()) {
				NamedNodeMap attr = node.getAttributes();
				for (int i = 0; i < attr.getLength(); i++) {
					content[cnt++] = attr.item(i).getNodeName() + " : " + attr.item(i).getNodeValue();
				}
			} else {
				return null;
			}

			String[] new_content = Arrays.copyOfRange(content, 0, cnt);

			return new_content;
		} else if (type == 3) {
			// text
			NodeList children = node.getChildNodes();
			int count = children.getLength();

			for (int i = 0; i < count; i++) {
				if (children.item(i).getNodeType() == Node.COMMENT_NODE) {
					content[cnt++] = children.item(i).getNodeValue();
				}
			}

			String[] new_content = Arrays.copyOfRange(content, 0, cnt);

			return new_content;
		}
		return null;
	}

	public void delete_element(Document doc, int element) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = delete_findNode(doc.getDocumentElement(), element);
		Node par = node.getParentNode();

		System.out.println("Deleted Element : " + node.getNodeName());
		par.removeChild(node);
	}

	public void delete_text(Document doc, int node_number, int content_number) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = delete_findNode(doc.getDocumentElement(), node_number);

		NodeList children = node.getChildNodes();
		int count = children.getLength();

		Node toDel = null;
		int cnt = 0;
		for (int i = 0; i < count; i++) {
			if (children.item(i).getNodeType() == Node.TEXT_NODE) {
				if (cnt == content_number) {
					toDel = children.item(i);
					break;
				}
				cnt++;
			}
		}

		node.removeChild(toDel);
	}

	public void delete_attr(Document doc, int node_number, int content_number) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = delete_findNode(doc.getDocumentElement(), node_number);

		NodeList children = node.getChildNodes();
		int count = children.getLength();

		Node toDel = null;
		if (node.hasAttributes()) {
			NamedNodeMap attr = node.getAttributes();
			toDel = attr.item(content_number);
		} else {
			return;
		}

		((Element) node).removeAttributeNode((Attr) toDel);
	}

	public void delete_comment(Document doc, int node_number, int content_number) {
		// type 1=element 2=text 3=attribute 4=comment
		// element

		Node node = delete_findNode(doc.getDocumentElement(), node_number);

		NodeList children = node.getChildNodes();
		int count = children.getLength();

		Node toDel = null;
		int cnt = 0;
		for (int i = 0; i < count; i++) {
			if (children.item(i).getNodeType() == Node.COMMENT_NODE) {
				if (cnt == content_number) {
					toDel = children.item(i);
					break;
				}
				cnt++;
			}
		}

		node.removeChild(toDel);
	}

	public String[] delete_getNodeNameList(Document doc) {
		String content[] = new String[4096];
		lines = 0;
		delete_traverse(doc.getDocumentElement(), "戌", content);
		String[] new_content = Arrays.copyOfRange(content, 0, lines);

		lines = 0;
		return new_content;
	}

	public String[] delete_getContentNameList(Document doc, int type, int element) {
		String content[] = new String[4096];
		lines = 0;
		int cnt = 0;
		Node node = delete_findNode(doc.getDocumentElement(), element);
		lines = 0;
		System.out.println(node.getNodeName());

		if (type == 1) {
			// text
			NodeList children = node.getChildNodes();
			int count = children.getLength();

			for (int i = 0; i < count; i++) {
				if (children.item(i).getNodeType() == Node.TEXT_NODE) {
					content[cnt++] = children.item(i).getNodeValue();
				}
			}
			String[] new_content = Arrays.copyOfRange(content, 0, cnt);

			return new_content;
		} else if (type == 2) {
			// text
			NodeList children = node.getChildNodes();
			int count = children.getLength();

			if (node.hasAttributes()) {
				NamedNodeMap attr = node.getAttributes();
				for (int i = 0; i < attr.getLength(); i++) {
					content[cnt++] = attr.item(i).getNodeName() + " : " + attr.item(i).getNodeValue();
				}
			} else {
				return null;
			}

			String[] new_content = Arrays.copyOfRange(content, 0, cnt);

			return new_content;
		} else if (type == 3) {
			// text
			NodeList children = node.getChildNodes();
			int count = children.getLength();

			for (int i = 0; i < count; i++) {
				if (children.item(i).getNodeType() == Node.COMMENT_NODE) {
					content[cnt++] = children.item(i).getNodeValue();
				}
			}

			String[] new_content = Arrays.copyOfRange(content, 0, cnt);

			return new_content;
		}
		return null;
	}

	public void delete_traverse(Node node, String indent, String[] content) {
		if (node == null) {
			return;
		}

		int type = node.getNodeType();
		switch (type) {

		case Node.ELEMENT_NODE:
			content[lines++] = indent + node.getNodeName() + "\n";
		}
		// System.out.println(node.getNodeName());

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				delete_traverse(children.item(i), indent + "式", content);
			}
		}
	}

	public Node delete_findNode(Node node, int number) {

		if (node == null) {
			return null;
		}

		int type = node.getNodeType();
		switch (type) {

		case Node.ELEMENT_NODE:
			if (lines == number) {
				return node;
			}
			lines++;
			break;
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node t = delete_findNode(children.item(i), number);

				if (t != null) {
					return t;
				}
			}
		}
		return null;

	}
}