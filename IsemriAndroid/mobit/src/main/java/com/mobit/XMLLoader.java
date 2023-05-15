package com.mobit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mobit.INode;
import com.mobit.Map;

public abstract class XMLLoader implements AutoCloseable {

	static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	// private java.util.Map<String, INode> map = new HashMap<String, INode>();
	private List<INode> items = new ArrayList<INode>(250);

	protected abstract Map[] getMapList();

	void finalizer() {
		close();

	}

	@Override
	public void close() {
		items.clear();
	}

	private INode getObject(String nodeName) throws InstantiationException, IllegalAccessException {
		for (Map map : getMapList()) {
			if (map.nodeName.equals(nodeName)) {
				return (INode) map.c.newInstance();
			}
		}

		return null;
	}

	public void setItem(INode node) {

		
		INode nn = null;
		
		String tagName = node.getClass().getSimpleName();
		for(INode n : items){
			if(node.getClass().getSimpleName().equals(tagName)){
				if(node.getId().equals(n.getId())){
					nn = n;
					break;
				}
			}
		}
		/*
		Element e = doc.getDocumentElement();
		NodeList list = e.getElementsByTagName();
		for(int i= 0; i < list.getLength(); i++){
			NamedNodeMap map = list.item(i).getAttributes();
			for(int j = 0; j < map.getLength(); j++){
				Node n = map.item(j);
				if(node.getId().equalsIgnoreCase(n.getNodeValue())){
					ee = (Element)list.item(i);
					break;
				}
			}
			if(ee != null)break;
		}*/
		
		Element ee;
		if(nn == null){
			nn = node;
			Element e = doc.getDocumentElement();
			ee = doc.createElement(tagName);
			e.appendChild(ee);
			addItem(nn);
			
		}
		else {
			nn.copy(node);
			ee = nn.getElement();
		}
		nn.set(ee);

	}

	private void addItem(INode node) {
		items.add(node);

	}

	public INode getItem(String Id) {
		for (INode item : items) {
			try{
				if (item.getId().equalsIgnoreCase(Id)) {
					return item;
				}
			}catch (Exception e){}

		}
		return null;

	}

	public void setItemValue(String Id, Object value) {
		for (INode item : items) {
			try{
				if (item.getId().equalsIgnoreCase(Id)) {
					item.setValue(value);
				}
			}catch (Exception e){}

		}
	}

	@SuppressWarnings("unchecked")
	public <T extends INode> T getItem(Class<?> c, String Id) {
		for (INode item : items) {
			try{
				if (item.getId().equalsIgnoreCase(Id) && c.isInstance(item)) {
					return (T) item;
				}
			}catch (Exception e){}

		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public <T extends INode> Collection<T> getItems(Class<?> c) {
		Collection<T> list = new ArrayList<T>();
		for (INode item : items) {
			if (c.isInstance(item)) {
				list.add((T) item);
			}
		}
		return list;

	}

	public Collection<INode> getItemList() {
		return items;
	}

	@SuppressWarnings("unchecked")
	public <T extends INode> Collection<T> getItems(Class<?> c, String Id) {
		List<T> list = new ArrayList<T>();
		for (INode item : getItemList()) {
			try{
				if (Id == null || item.getId().equalsIgnoreCase(Id)) {
					list.add((T) item);
				}
			}catch (Exception e){}

		}
		return list;
	}

	private void load(Element eElement) throws Exception {

		INode item = getObject(eElement.getNodeName());
		if (item != null) {
			item.get(eElement);
			addItem(item);
		}
		NodeList nList = eElement.getChildNodes();
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			short nType = nNode.getNodeType();
			switch (nType) {
			case Node.ELEMENT_NODE:
				load((Element) nNode);
				break;
			case Node.ENTITY_NODE:
				break;
			}

		}
	}

	String docName;
	Document doc;

	public void load(String docName) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(docName);
		doc.getDocumentElement().normalize();
		Element e = doc.getDocumentElement();

		load(e);

		this.docName = docName;

		return;
	}

	public void load(InputStream stream) throws Exception {

		load(stream, null);
		return;
	}

	public void load(InputStream stream, String docName) throws Exception {

		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(stream);
		doc.getDocumentElement().normalize();
		Element e = doc.getDocumentElement();

		load(e);

		this.docName = docName;

		return;
	}

	public void save(String docName) throws Exception {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		File file = new File(docName);
		StreamResult streamResult = new StreamResult(file);
		transformer.transform(source, streamResult);

	}

	public void save() throws Exception {
		save(docName);
	}

	public static void create(File file, String tagName) throws Exception {
		if (file.exists())
			return;

		FileOutputStream os = null;

		try {

			file.createNewFile();
			os = new FileOutputStream(file);
			String s = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?><%s/>", tagName);
			os.write(s.getBytes());
			os.close();

		} finally {
			if (os != null)
				os.close();
		}
	}

}
