package com.mobit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class Image extends INode {

	
	public float Left;
	public float Right;
	public float Top;
	public float Bottom;
	public File file;
	public int Suret = 0;
	private String Style;
	private boolean left = true;
	public boolean isLeft()
	{
		return left;
	}

	private boolean right = false;
	public boolean isRight()
	{
		return right;
	}
	private boolean center = false;
	public boolean isCenter()
	{
		return center;
	}
	private boolean top = false;
	public boolean isTop()
	{
		return top;
	}
	private boolean bottom = true;
	public boolean isBottom()
	{
		return bottom;
	}
	
	public Image()
	{
		return;
	}
	
	@Override
	public String getId()
	{
		return Name;
		
		
	}
	
	@Override
	public void get(Element eElement) throws MobitException
	{
		Attr attr;

		attr = eElement.getAttributeNode("Style");
		if(attr != null){
			Style = attr.getValue();
			checkStyle(Style);
		}

		attr = eElement.getAttributeNode("Name");
		if(attr != null) Name = attr.getValue();
		attr = eElement.getAttributeNode("Value");
		if(attr != null) Value = attr.getValue();
		attr = eElement.getAttributeNode("Left");
		if(attr != null) Left = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Right");
		if(attr != null) Right = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Top");
		if(attr != null) Top = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Bottom");
		if(attr != null) Bottom = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("File");
		if(attr != null){
			file = new File(Globals.app.getAppDataPath(), attr.getValue().toString());
		}
		attr = eElement.getAttributeNode("Suret");
		if(attr != null) Suret = Integer.parseInt(attr.getValue());


	}

	private void checkStyle(String sytle) throws MobitException
	{

		String _Style = Style.toLowerCase();

		int cnt = 0;
		if(_Style.contains("c")){
			left = false;
			center = true;
			cnt++;
		}
		if(_Style.contains("r")){
			left = false;
			right = true;
			cnt++;
		}

		if(cnt > 1) throw new MobitException(String.format("%s text kaydının stilinde hata var", getId()));

		cnt = 0;
		if(_Style.contains("t")){
			bottom = false;
			cnt++;
		}

		if(cnt > 1) throw new MobitException(String.format("%s text kaydının stilinde hata var", getId()));



	}

	public String getImageAttribute(String attrName){
		if (attrName.equals("Left")){
			return String.valueOf(Left);
		}
		if(attrName.equals("Top"))
		{
			return String.valueOf(Top);
		}
		return "";
	}
	
}
