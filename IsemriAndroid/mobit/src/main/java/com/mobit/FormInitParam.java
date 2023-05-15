package com.mobit;


public class FormInitParam  implements Cloneable {

	public String captionColor;
	public String captionText;
	public boolean homeButton = true;
	
	public FormInitParam(String captionColor, String captionText)
	{
		this.captionColor = captionColor;
		this.captionText = captionText;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
