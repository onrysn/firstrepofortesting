package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class PrintForm extends INode {

	public int printerNo;
	public String File;
	
	public PageFormatInfo pfi;
	
	@Override
	public String toString()
	{
		return getId();
	}
	
	@Override
	public void get(Element eElement)
	{
		Attr attr;
		attr = eElement.getAttributeNode("Name");
		if(attr != null) Name = attr.getValue();
		attr = eElement.getAttributeNode("Value");
		if(attr != null) Value = attr.getValue();
		attr = eElement.getAttributeNode("printerNo");
		if(attr != null) printerNo = Integer.parseInt(attr.getValue());
		attr = eElement.getAttributeNode("File");
		if(attr != null) File = attr.getValue();
		
	}
}
