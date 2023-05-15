package org.ksoap2.x.serialization;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ksoap2.x.SoapEnvelope;
import org.ksoap2.x.SoapFault;
import org.ksoap2.x.serialization.KvmSerializable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import mobit.http.Result;
import mobit.http.ReturnValue;

public class SoapSerializationEnvelope extends SoapEnvelope {

	static final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	static final String envelope_begin = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">";
	static final String envelope_end = "</soap:Envelope>";
	static final String body_begin = "<soap:Body>";
	static final String body_end = "</soap:Body>";


	List<Map> mapping = new ArrayList<Map>();
	
	public SoapSerializationEnvelope(int version) {
		// org.ksoap2.serialization.SoapSerializationEnvelope en;
		super(version);
		mapping.add(new Map("", "ReturnValue", ReturnValue.class));
	}



	StringBuilder sb = new StringBuilder(4096);
	public Object bodyOut = null;
	public Object bodyIn = null;
	public boolean dotNet = true;

	public void setOutputSoapObject(Object soapObject) {

		if(soapObject instanceof SoapObject){
			SoapObject obj = (SoapObject)soapObject;
			obj.dotNet = dotNet;
		}
		bodyOut = soapObject;

	}

	@Override
	public String toString() {
		return serialize();
	}

	class Map {
		String namespace;
		String name;
		Class<?> clazz;

		public Map(String namespace, String name, Class<?> clazz) {
			this.namespace = namespace;
			this.name = name;
			this.clazz = clazz;
		}
	}

	
	public void addMapping(String namespace, String name, Class<?> clazz) {

		mapping.add(new Map(namespace, name, clazz));

	}

	public String serialize() {
		sb.setLength(0);
		sb.append(xml);
		sb.append(envelope_begin);

		sb.append(body_begin);

		if(bodyOut instanceof SoapObject){
			SoapObject obj = (SoapObject)bodyOut;
			obj.dotNet = dotNet;
		}
		sb.append(bodyOut.toString());

		sb.append(body_end);
		sb.append(envelope_end);

		return sb.toString();

	}

	private Map findMap(String name) {
		for (Map map : mapping)
			if (map != null && map.name != null && name != null && map.name.equalsIgnoreCase(name))
				return map;
		
		return null;
	}

	private void recursive(Node node, int level, KvmSerializable parent, int index) throws Exception {

		KvmSerializable obj = null;
		NodeList nodes = node.getChildNodes();
		Map map = findMap(node.getNodeName());
		if (map != null) {
			Constructor<?> cons = map.clazz.getConstructor(String.class, String.class);
			obj = (KvmSerializable) cons.newInstance(map.namespace, map.name);
			if (parent != null) parent.setProperty(index, obj);
			if(bodyIn == null) bodyIn = obj;
			
		}
		
		if (nodes.getLength() == 0) return;

		for (int i = 0; i < nodes.getLength(); i++) {

			Node n = nodes.item(i);
			if (obj == null) {
				recursive(n, level++, parent, i);
				continue;
			}

			int idx = obj.getPropertyIndex(n.getNodeName());
			if(idx < 0) continue;
			
			PropertyInfo info = new PropertyInfo();
			obj.getPropertyInfo(idx, null, info);
			
			if (info.type.equals(PropertyInfo.STRING_CLASS))
				obj.setProperty(idx, n.getTextContent());
			else if (info.type.equals(PropertyInfo.OBJECT_CLASS)) {
				recursive(n, level++, obj, idx);
			}

		}
		
		if (obj instanceof Result) {
			Result result = (Result) obj;
			if (result.ReturnCode.equalsIgnoreCase("Error")) {
				SoapFault fault = new SoapFault(version);
				fault.faultcode = result.ReturnCode;
				fault.faultstring = result.ReturnMessage;
				bodyIn = fault;
			}
		}
		
	}

	public void parse(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		InputSource is;
		try {

			builder = factory.newDocumentBuilder();
			is = new InputSource(new StringReader(xml));
			Document doc = builder.parse(is);

			
			doc.getDocumentElement().normalize();
			NodeList nodes;
			nodes = doc.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				recursive(n, 0, null, i);
			}

			/*
			for (Map map : mapping) {

				NodeList list = doc.getElementsByTagName(map.name);
				for (int i = 0; i < list.getLength(); i++) {

					Constructor<?> cons = map.clazz.getConstructor(String.class, String.class);
					KvmSerializable obj = (KvmSerializable) cons.newInstance("", map.name);
					NodeList list2 = list.item(i).getChildNodes();
					for (int j = 0; j < list2.getLength(); j++) {
						Node n = list2.item(j);
						obj.setProperty(j, n.getTextContent());
					}
					if (obj instanceof Result) {
						Result result = (Result) obj;
						if (result.ReturnCode.equalsIgnoreCase("Error")) {
							SoapFault fault = new SoapFault(version);
							fault.faultcode = result.ReturnCode;
							fault.faultstring = result.ReturnMessage;
							bodyIn = fault;
							return;
						}

					}

					if (bodyIn != null) {
						Result r = (Result) bodyIn;
						if (r.ReturnValue == null)
							r.ReturnValue = obj;
					}
					if (bodyIn == null)
						bodyIn = obj;

				}
			}*/
			return;

		} catch (Exception e) {

			throw e;
		}

	}
}
