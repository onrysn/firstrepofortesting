package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class Printer extends INode {

	public int printerNo;
	public String Emulation;
	
	@Override
	public String toString()
	{
		return getId();
	}

	@Override
	public void get(Element eElement)
	{
		Attr attr;
		attr = eElement.getAttributeNode("Value");
		if(attr != null) Value = attr.getValue();
		attr = eElement.getAttributeNode("PrinterNo");
		if(attr != null) printerNo = Integer.parseInt(attr.getValue());
		attr = eElement.getAttributeNode("Emulation");
		if(attr != null) Emulation = attr.getValue();
		
	}
}
