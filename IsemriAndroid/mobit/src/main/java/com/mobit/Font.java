package com.mobit;

import com.mobit.INode;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class Font extends INode {

	protected String Id;
	public String Size = "0";
	
	public String getId()
	{
		return Id;
	}
	public String getName() { return Name;}

	@Override
	public void get(Element eElement) throws Exception
	{
		super.get(eElement);
		Attr attr;
		attr = eElement.getAttributeNode("Id");
		if(attr != null) Id = attr.getValue();
		attr = eElement.getAttributeNode("Size");
		if(attr != null) Size = attr.getValue();

	}
}
