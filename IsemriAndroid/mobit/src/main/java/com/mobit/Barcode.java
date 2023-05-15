package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.mobit.INode;

public class Barcode extends INode {

	public String Type;
	public int Ratio = 1;
	public float Bottom;
	public float Right;
	public float Top;
	public float Left;
	public float Height;
	
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
		attr = eElement.getAttributeNode("Type");
		if(attr != null) Type = attr.getValue();
		attr = eElement.getAttributeNode("Ratio");
		if(attr != null) Ratio = Integer.parseInt(attr.getValue());
		attr = eElement.getAttributeNode("Bottom");
		if(attr != null) Bottom = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Right");
		if(attr != null) Right = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Top");
		if(attr != null) Top = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Left");
		if(attr != null) Left = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Height");
		if(attr != null) Height = Float.parseFloat(attr.getValue());
	}
	
}
