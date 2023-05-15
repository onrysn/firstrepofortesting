package com.mobit;

import java.util.Collection;

import com.mobit.Barcode;
import com.mobit.CPCLFont;
import com.mobit.INode;
import com.mobit.Line;
import com.mobit.Map;
import com.mobit.Property;
import com.mobit.Rectangle;
import com.mobit.Text;
import com.mobit.WinFont;
import com.mobit.XMLLoader;


public class PageFormatInfo extends XMLLoader {

	private static Map [] mapList = 
	{
		new Map("Text", Text.class),
		new Map("Line", Line.class),
		new Map("Rectangle", Rectangle.class),
		new Map("Barcode", Barcode.class),
		new Map("RawText", RawText.class),
		new Map("Property", Property.class),
		new Map("WinFont", WinFont.class),
		new Map("CPCLFont", CPCLFont.class),
		new Map("Image", Image.class),
		
	};
	
	private boolean rawText = false;
	
	@Override
	protected Map [] getMapList()
	{
		return mapList;
	}
	
	public String getFormType()
	{
		return getItem(Property.class, "FormTipi").getStringValue();
	}
	
	public int getPageSize()
	{
		return getItem(Property.class, "PAGESIZE").getIntValue();
		
	}
	public int getPageWidth()
	{
		INode node = getItem(Property.class, "PAGEWIDTH");
		if(node == null) return 576;
		return node.getIntValue();
		
	}
	//HÜSEYİN EMRE ÇEVİK XML SAYFA UZUNLUĞU 02.04.2021
	public int getPageHeight()
	{
		INode node = getItem(Property.class, "PAGEHEIGHT");

		if(node == null) return 120;
		return node.getIntValue();
	}
	public  int getFaturaSize(int fontId){
		if (fontId == 4){
			return 30;
		}
		else if (fontId == 5){
			return 33;
		}
		else if (fontId == 6) {
			return 15;
		}
		else {
			return getFontSize();
		}
	}
	public int getFontSize(){
		//return  50;

		INode node = getItem(Property.class, "FontSize");
		if(node == null) return 21;
		return node.getIntValue();
	}
	public int getxoffset()
	{
		return getItem(Property.class, "X-Referans").getIntValue();
	}
	public int getyoffset()
	{
		return getItem(Property.class, "Y-Referans").getIntValue();
	}
	public int getxdpi()
	{
		return getItem(Property.class, "XDPI").getIntValue();
	}
	public int getydpi()
	{
		return getItem(Property.class, "YDPI").getIntValue();
	}
	public int getquality()
	{
		return getItem(Property.class, "QUALITY").getIntValue();
	}
	public String getUnit()
	{
		return getItem(Property.class, "UNIT").getStringValue();
	}

	public int getquantity()
	{
		return 1;
		//return getItem(Property.class, "QUANTITY").getIntValue();
	}
	public int getPrinterNo()
	{
		return getItem(Property.class, "PrinterNo").getIntValue();
	}
	
	public boolean isRawText()
	{
		return rawText;
	}
	
	public int getBlackMark()
	{
		return getItem(Property.class, "BlackMark").getIntValue();
	}
	
	public Font getCPCLFont(String fontId)
	{
		return getItem(CPCLFont.class, fontId);
	}
	public void Prepare()
	{
		Collection<INode> list = getItemList();
		for(INode node : list){
			if(node instanceof Text || node instanceof Line || 
			   node instanceof Rectangle || node instanceof Barcode){ 
				
				node.Prepare(this);
			}
			else if(node instanceof RawText){
				rawText = true;
			}
		}
	}
	
}
