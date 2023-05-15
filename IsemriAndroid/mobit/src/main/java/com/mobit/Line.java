package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.mobit.INode;

public class Line extends INode {

	public String Style;
	public float Bottom;
	public float Right;
	public float Top;
	public float Left;
	public float Width = 1;
	
	
	public String getId()
	{
		return Name;
	}
	
	@Override
	public void get(Element eElement)
	{
		
		Attr attr;
		attr = eElement.getAttributeNode("Name");
		if(attr != null) Name = attr.getValue();
		attr = eElement.getAttributeNode("Style");
		if(attr != null) Style = attr.getValue();
		attr = eElement.getAttributeNode("Bottom");
		if(attr != null) Bottom = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Right");
		if(attr != null) Right = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Top");
		if(attr != null) Top = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Left");
		if(attr != null) Left = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Width");
		if(attr != null) Width = Float.parseFloat(attr.getValue());
	}
	
	
	
}
