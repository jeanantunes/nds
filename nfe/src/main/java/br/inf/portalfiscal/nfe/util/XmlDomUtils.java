package br.inf.portalfiscal.nfe.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDomUtils {
	/**
     * Get direct node childs by tag name. If childTagName = null,
     * returns all direct childs. Never returns null.
     * @param parent - parent node.
     * @param childTagName - child tag name to search.
     * @return ArrayList of found tags (nodes). 
     */
	public static ArrayList<Node> getDirectChildsByTagName(Node parent, String childTagName) {
		ArrayList<Node> childs = new ArrayList<Node>();
		if (parent == null) return childs;
		NodeList nList = parent.getChildNodes();
		if (nList == null) return childs;
		for (int i=0; i<nList.getLength(); i++) {
			if (childTagName == null)
				childs.add(nList.item(i));
			else { 
				if (nList.item(i).getNodeName().equalsIgnoreCase(childTagName))
					childs.add(nList.item(i)); 
			}
		}
		
		return childs;
	}
	
	/**
	 * Create new XML DOM document.
	 * @return new XML DOM document
	 * @throws ParserConfigurationException if error occurs
	 */
	public static Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}
	
	/**
	 * Create XML DOM document by parsing XML provided by InputStream.
	 * @param is - XML contents input stream
	 * @return XML DOM document formed from InputStream 
	 * @throws Exception if error occurs creating or parsing document
	 */
	public static Document createNewDocument(InputStream is) throws Exception {
		if (is == null) return createNewDocument();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(is);
	}
	
	/**
	 * Get XML DOM document contents in string form.
	 * @param doc - XML DOM document
	 * @return XML DOM document contents in string form
	 * @throws Exception if error occurs
	 */
	public static String getXMLDocString(Document doc) throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    DOMSource source = new DOMSource(doc);
	    ByteArrayOutputStream output = new ByteArrayOutputStream(); 
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
		transformer.transform(source, new StreamResult(writer));
		return new String(output.toByteArray(), "UTF-8").trim();
	}
	
	/**
	 * Recursively remove unused or duplicate namespaces (xmlns:<ns> attributes).
	 * @param node - parent node from where to start unused/duplicate namespace removal  
	 */
	public static ArrayList<String> removeUnusedNamespaces(Node node) {
		ArrayList<String> usedNsPrefixes = new ArrayList<String>();
		if (node == null) return usedNsPrefixes;
		
		/*
		 *  Get xmlnss defined in current node.
		 */
		ArrayList<String> currNodeNsPrefixes = new ArrayList<String>();
		NamedNodeMap attrs = node.getAttributes();
		if (attrs != null) {
			for (int j=0; j<attrs.getLength(); j++) {
				Node attr = attrs.item(j);
				if (attr != null) {
					String attrName = attr.getNodeName();
					String attrPrefix = parseQNamePrefix(attrName);
					String attrLocalName = parseQNameLocalName(attrName);
					
					if (attrPrefix != null && attrPrefix.equalsIgnoreCase("xmlns")) {
						if (attrLocalName != null) {
							currNodeNsPrefixes.add(attrLocalName);
						}
					}
				}
			}
		}

		/*
		 * Get current node prefix.
		 */
		String currNodePrefix = parseQNamePrefix(node.getNodeName());
		if (currNodePrefix != null) {
			if (!usedNsPrefixes.contains(currNodePrefix)) {
				usedNsPrefixes.add(currNodePrefix);
			}
		}
		
		/*
		 * Get current node attributes prefixes, except xmlns.
		 */
		if (attrs != null) {
			for (int j=0; j<attrs.getLength(); j++) {
				Node attr = attrs.item(j);
				if (attr != null) {
					String attrPrefix = parseQNamePrefix(attr.getNodeName());
					if (attrPrefix != null && !attrPrefix.equalsIgnoreCase("xmlns")) {
						if (!usedNsPrefixes.contains(attrPrefix)) {
							usedNsPrefixes.add(attrPrefix);	
						}
					}
				}
			}
		}
		
		/*
		 * Iterate through child nodes and get used prefixes.
		 */
		NodeList childs = node.getChildNodes();
		if (childs != null) {
			for (int j=0; j<childs.getLength(); j++) {
				Node child = childs.item(j);
				if (child != null) {
					ArrayList<String> usedNsPrefixes2 = removeUnusedNamespaces(child);
					for (int k=0; k<usedNsPrefixes2.size(); k++) {
						String tmpPrefix = usedNsPrefixes2.get(k);
						if (!usedNsPrefixes.contains(tmpPrefix)) {
							usedNsPrefixes.add(tmpPrefix);
						}
					}
				}
			}
		}
		
		/*
		 * Remove unused namespaces in current node.
		 */
		ArrayList<String> prefixesToRemove = new ArrayList<String>(); 
		for (int i=0; i<currNodeNsPrefixes.size(); i++) {
			String currNodeNsAttrPrefix = currNodeNsPrefixes.get(i);
			if (usedNsPrefixes.contains(currNodeNsAttrPrefix)) {
				prefixesToRemove.add(currNodeNsAttrPrefix);
			} else {
				((Element)node).removeAttribute("xmlns:" + currNodeNsAttrPrefix);
			}
		}
		
		/*
		 * Remove locally defined used prefixes. 
		 */
		for (String prefix : prefixesToRemove) {
			usedNsPrefixes.remove(prefix);
		}
		
		return usedNsPrefixes; // only prefixes which are used and not defined locally
	}
	
	
	/**
	 * Try to resolve node namespace URI
	 * @param node - node which namespace URI should be resolved
	 * @return resolved namespace URI or null if it cannot be resolved
	 */
	public static String resolveNsURI(Node node) {
		if (node == null) return null;
		
		String nodeNsURI = node.getNamespaceURI();
		if (nodeNsURI == null || nodeNsURI.length() <= 0) {
			String nodeQName = node.getNodeName();
			if (nodeQName == null || nodeQName.length() <= 0) return null;

			String prefix = XmlDomUtils.parseQNamePrefix(nodeQName);
			
			while (node != null) {
				NamedNodeMap attrs = node.getAttributes();
				if (attrs != null) {
					Node attr = null;
					if (prefix != null && prefix.length() > 0) {
						attr = attrs.getNamedItem("xmlns:" + prefix);
					} else {
						attr = attrs.getNamedItem("xmlns");
					}
 
					if (attr != null) {
						String attrVal = attr.getNodeValue();
						if(attrVal != null && attrVal.length() > 0) {
							return attrVal;
						}
					}
				}
				
				node = node.getParentNode();
			}
		}

		return nodeNsURI;
	}
	
	/**
	 * Find direct parent node childs by tag name
	 * @param parentNode - parent node
	 * @param namespaceURI - childs namespace URI
	 * @param qualifiedName - childs qualified (with prefix) tag name
	 * @return a list of found
	 */
	public static List<Node> findDirectChildsByName(Node parentNode, String namespaceURI, String qualifiedName) {
		ArrayList<Node> childsByName = new ArrayList<Node>();
		
		if (parentNode == null || qualifiedName == null || qualifiedName.length() <=0) return childsByName; 
		
		String prefix = XmlDomUtils.parseQNamePrefix(qualifiedName);
		NodeList childs = parentNode.getChildNodes();
		if (childs != null) {
			for (int i=0; i<childs.getLength(); i++) {
				boolean found = false;
				
				Node child = childs.item(i);
				if (child != null) {
					String childQName = child.getNodeName();
					if (childQName != null && childQName.length() > 0) {
						if (namespaceURI != null) {
							if (prefix != null) { // if prefix specified, must consider it
								if (childQName.equalsIgnoreCase(qualifiedName)) {
									String childNsURI = XmlDomUtils.resolveNsURI(child);
									if (childNsURI != null) {
										if (childNsURI.equalsIgnoreCase(namespaceURI)) {
											found = true;
										}
									}
								}
							} else {  // if prefix not specified, must consider both: with and without prefix
								String childPrefix = XmlDomUtils.parseQNamePrefix(childQName);
								String childLocalName = null;
								if (childPrefix != null) {
									childLocalName = XmlDomUtils.parseQNameLocalName(childQName); 
								} else {
									childLocalName = childQName;
								}
								
								if (childLocalName != null && childLocalName.equalsIgnoreCase(qualifiedName)) {
									String childNsURI = XmlDomUtils.resolveNsURI(child);
									if (childNsURI != null) {
										if (childNsURI.equalsIgnoreCase(namespaceURI)) {
											found = true;
										}
									}
								}
							}
						} else {
							if (childQName.equalsIgnoreCase(qualifiedName)) {
								found = true;
							}
						}
					}
				}
				
				if (found) childsByName.add(child);
			}
		}
		
		return childsByName;
	}

	
	/**
	 * Get qualified name prefix (example: for 'dc:title' will return 'dc')
	 * @param qName - qualified name
	 * @return parsed qualified name prefix
	 */
	public static String parseQNamePrefix(String qName) {
		String prefix = null;
		if (qName != null) {
			int index = qName.indexOf(":");
			if (index > -1)
				prefix = qName.substring(0, index);
		}
		
		return prefix;
	}

	/**
	 * Get qualified name local name (example: for 'dc:title' will return 'title')
	 * @param qName - qualified name
	 * @return parsed qualified name local name
	 */
	public static String parseQNameLocalName(String qName) {
		String suffix = null;
		if (qName != null) {
			int index = qName.indexOf(":");
			if (index > -1)
				suffix = qName.substring(index + 1);
		}
		
		return suffix;
	}

	/**
	 * Split predicate into two parts (example: for http://ns/els#loc will return {http://ns/els#, loc})
	 * @param predicate - predicate to split
	 * @return Split predicate. Can return null or {null, predicate} if namespace part cannot be parsed. 
	 */
	public static String [] splitPredicate(String predicate) {
		if (predicate == null) return null;
		
		// first look for last # and only if not found look for /
		int index = predicate.lastIndexOf("#");
		if (index < 0) {
			index = predicate.lastIndexOf("/");	
		}
		
		if (index >= 0) {
			String ns = predicate.substring(0, index+1);
			String localName = predicate.substring(index+1);
			
			if (ns.trim().length() <= 1) ns = null;
			if (localName.trim().length() <= 0) localName = null;
			
			if (ns == null && localName == null) return null;
			
			return new String[] {ns, localName}; 
		}
		
		return new String[] {null, predicate};
	}
	
	/**
	 * Check if node has specified attribute
	 * @param node - node to check
	 * @param attribute - attribute to search
	 * @return true if node has specified attribute
	 */
	public static boolean hasAttribute(Node node, String attribute) {
		if (node == null || attribute == null) return true;
		NamedNodeMap nodes = ((Element)node).getAttributes();
		if (nodes != null) {
			Node attr = nodes.getNamedItem(attribute);
			if (attr != null) return false;
		} 
		
		return true;
	}
}
