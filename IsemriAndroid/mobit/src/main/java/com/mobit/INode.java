package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public abstract class INode {

	
	protected String Name;
	protected String Text;
	protected Object Value;
	
	protected Element eElement;
	
	public Element getElement()
	{
		return eElement;
	}
	
	public boolean isBounded()
	{
		return eElement != null;
	}
	
	public String getId()
	{
		return Name;
	}
	
	public void copy(INode n){
		Name = n.Name;
		Text = n.Text;
		Value = n.Value;
	}
	
	public void get(Element eElement) throws Exception
	{
		this.eElement = eElement;
		Attr attr;
		attr = eElement.getAttributeNode("Name");
		if(attr != null) Name = attr.getValue();
		attr = eElement.getAttributeNode("Value");
		if(attr != null) Value = attr.getValue();
		Text = eElement.getTextContent();
		
	}
	
	public void set(Element eElement)
	{
		this.eElement = eElement;
		eElement.setAttribute("Name", Name);
		eElement.setAttribute("Value", Value.toString());
		eElement.setTextContent(Text);
		
	}
	
	public void set()
	{
		set(eElement);
	}
	
	public String getAttribute(String attrName)
	{
		Attr attr;
		attr = eElement.getAttributeNode(attrName);
		if(attr != null) return attr.getValue();
		return "";
	}
	
	public void setAttribute(String attrName, String value)
	{
		Attr attr;
		attr = eElement.getAttributeNode(attrName);
		if(attr != null) attr.setValue(value);
		
	}
	
	public String getText()
	{
		return Text;
	}
	
	public void setText(String text)
	{
		this.Text = text;
		eElement.setTextContent(Text);
	}
	
	public Object getValue()
	{
		return Value;
	}
	public int getIntValue()
	{
		if(Value instanceof Integer) return (Integer)Value;
		return Integer.parseInt((String)Value);
	}
	public String getStringValue()
	{
		return (String)Value;
	}
	
	public void setValue(Object value)
	{
		this.Value = value;
		Attr attr;
		attr = eElement.getAttributeNode("Value");
		if(attr != null) attr.setValue(value.toString());
	}
	
	public void Prepare(Object obj)
	{
		
	}
	
}
