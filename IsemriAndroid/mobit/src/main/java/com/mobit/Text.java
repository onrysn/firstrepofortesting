package com.mobit;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.mobit.CPCLFont;
import com.mobit.Font;
import com.mobit.INode;
import com.mobit.PageFormatInfo;

public class Text extends INode {

	private String Style;
	public String FontId;
	public float Left;
	public float Top;
	public float Right;
	public float Bottom;
	public boolean Bold = false;
	
	public Font font;
		
	@Override
	public String getId()
	{
		return Name;
		
	}
	
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
	public Text()
	{
		
	}
	public Text(Text text)
	{
		Style = text.Style;
		FontId = text.FontId;
		Left = text.Left;
		Top = text.Top;
		Right = text.Right;
		Bottom = text.Bottom;
	}
	
	@Override
	public void get(Element eElement) throws Exception
	{
		
		super.get(eElement);
		Attr attr;
		attr = eElement.getAttributeNode("Style");
		if(attr != null){
			Style = attr.getValue();
			checkStyle(Style);
		}
		attr = eElement.getAttributeNode("FontId");
		if(attr != null) FontId = attr.getValue();
		attr = eElement.getAttributeNode("Bottom");
		if(attr != null) Bottom = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Right");
		if(attr != null) Right = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Top");
		if(attr != null) Top = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Left");
		if(attr != null) Left = Float.parseFloat(attr.getValue());
		attr = eElement.getAttributeNode("Bold");
		if(attr != null) Bold = Boolean.parseBoolean(attr.getValue());
		
	}
	
	@Override
	public void Prepare(Object obj)
	{
		PageFormatInfo pfi = (PageFormatInfo)obj;
		font = (Font)pfi.getItem(CPCLFont.class, FontId);
		
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
}
