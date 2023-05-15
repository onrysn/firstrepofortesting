package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.mobit.INode;

public class Property extends INode {


	public String getId()
	{
		return Name;
	}
	public void get(Element eElement)
	{
		Attr attr;
		attr = eElement.getAttributeNode("Name");
		Name = attr.getValue();
		attr = eElement.getAttributeNode("Value");
		Value = attr.getValue();
		
	}
}
