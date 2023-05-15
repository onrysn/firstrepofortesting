package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.mobit.Font;

public class WinFont extends Font {
	
	
	public String FaceName;
	public String PitchAndFamily;
	public String Quality;
	public String ClipPrecision; 
	public String OutPrecision;
	public String CharSet;
	public String StrikeOut;
	public String Underline;
	public String Italic;
	public String Weight;
	public String Orientation;
	public String Escapement;
	public String Width;
	public String Height;
	
	@Override
	public void get(Element eElement)
	{
		
		Attr attr;
		attr = eElement.getAttributeNode("Id");
		if(attr != null) Id = attr.getValue();
		attr = eElement.getAttributeNode("Name");
		if(attr != null) Name = attr.getValue();
		attr = eElement.getAttributeNode("FaceName");
		if(attr != null) FaceName = attr.getValue();
		attr = eElement.getAttributeNode("PitchAndFamily");
		if(attr != null) PitchAndFamily = attr.getValue();
		attr = eElement.getAttributeNode("Quality");
		if(attr != null) Quality = attr.getValue();
		attr = eElement.getAttributeNode("ClipPrecision");
		if(attr != null) ClipPrecision = attr.getValue();
		attr = eElement.getAttributeNode("OutPrecision");
		if(attr != null) OutPrecision = attr.getValue();
		attr = eElement.getAttributeNode("CharSet");
		if(attr != null) CharSet = attr.getValue();
		attr = eElement.getAttributeNode("StrikeOut");
		if(attr != null) StrikeOut = attr.getValue();
		attr = eElement.getAttributeNode("Underline");
		if(attr != null) Underline = attr.getValue();
		attr = eElement.getAttributeNode("Italic");
		if(attr != null) Italic = attr.getValue();
		attr = eElement.getAttributeNode("Weight");
		if(attr != null) Weight = attr.getValue();
		attr = eElement.getAttributeNode("Orientation");
		if(attr != null) Orientation = attr.getValue();
		attr = eElement.getAttributeNode("Escapement");
		if(attr != null) Escapement = attr.getValue();
		attr = eElement.getAttributeNode("Width");
		if(attr != null) Width = attr.getValue();
		attr = eElement.getAttributeNode("Height");
		if(attr != null) Height = attr.getValue();
		
	}

}
