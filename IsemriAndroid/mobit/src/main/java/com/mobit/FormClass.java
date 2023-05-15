package com.mobit;

public class FormClass {
	public int formId;
	public Class<?> formClass;
	
	public FormClass(int formId, Class<?> formClass)
	{
		this.formId = formId;
		this.formClass = formClass;
	}
	
	public void load() throws ClassNotFoundException
	{
		if(formClass != null)
				Class.forName(formClass.getName());
			
	}
}
