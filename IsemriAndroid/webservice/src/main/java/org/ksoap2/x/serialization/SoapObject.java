package org.ksoap2.x.serialization;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.ksoap2.x.serialization.PropertyInfo;

public class SoapObject implements org.ksoap2.x.serialization.KvmSerializable {

	static final String request_begin = "<request>";
	static final String request_end = "</request>";
	
	String namespace;
	String name;
	
	public boolean dotNet = false;
	
	class Pair {
		public String name;
		public Object value;
		
		public Pair(String name, Object value)
		{
			this.name = name;
			this.value = value;
		}
	}
	
	StringBuilder sb = new StringBuilder(1024);
	
	List<Pair> properties = new ArrayList<Pair>(64);
	
	public SoapObject(String namespace, String name)
	{
		this.namespace = namespace;
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return serialize();
	}
	
	public SoapObject addProperty(String name, Object value)
	{
		properties.add(new Pair(name, value));
		return this;
	}
	
	public SoapObject addSoapObject(SoapObject soapObject)
	{
		properties.add(new Pair(null, soapObject));
		return this;
	}
	
	public String serialize()
	{
		sb.setLength(0);

		if(namespace != null && !namespace.isEmpty())
			sb.append(String.format("<%s xmlns=\"%s\">", name, namespace));
		else
			sb.append(String.format("<%s>", name));

		if(dotNet) sb.append(request_begin);

		for(int i = 0; i < properties.size(); i++){
			Pair pair = properties.get(i);
			if(pair.value instanceof SoapObject){
				sb.append(pair.value.toString());
			}
			else {
				sb.append(String.format("<%s>", pair.name));
				sb.append(pair.value.toString());
				sb.append(String.format("</%s>", pair.name));
			}
			
		}
		
		if(dotNet) sb.append(request_end);
		sb.append(String.format("</%s>", name));
		return sb.toString();
	}
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return properties.get(arg0);
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return properties.size();
	}
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		if(arg0 < properties.size()){
			Pair pair = properties.get(arg0);
			arg2.name = pair.name;
			arg2.type = pair.getClass();
		}
		else {
			arg2.name = "";
			arg2.type = PropertyInfo.OBJECT_CLASS;
		}
	}
	
	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		//properties.set(arg0, arg1);
	}
	
	protected static int getPropertyIndex(String [] propertyList, String propertyName)
	{
		for(int i = 0; i < propertyList.length; i++)
			if(propertyList[i].equals(propertyName)) return i;
		return -1;
	}

	@Override
	public int getPropertyIndex(String propertyName) {
		// TODO Auto-generated method stub
		return -1;
	}
	
}
