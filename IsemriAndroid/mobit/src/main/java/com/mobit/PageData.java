package com.mobit;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.mobit.PrintItem;

public class PageData implements AutoCloseable, Cloneable {

	private String formType = null;
	private java.util.Map<String, PrintItem> map = new HashMap<String, PrintItem>();;
	
	public PageData()
	{
		
	}
	
	public PageData(PageData pd)
	{
		formType = pd.formType;
		map.putAll(pd.map);


	}
	
	void finalizer()
	{
		close();
	}
	
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		map.clear();
	}
	
	public void setFormType(String formType)
	{
		this.formType = formType;
	}
	
	public String getFormType()
	{
		return formType;
	}
	
	
	public void addItem(PrintItem printItem)
	{
		map.put(printItem.getName(), printItem);
	}
	
	public void removeItem(PrintItem printItem)
	{
		map.remove(printItem);
	}
	
	
	public Collection<PrintItem> getlist()
	{
		return map.values();
	}
	
	public PrintItem getPrintItem(String Name)
	{
		
		return map.get(Name);
	}

	
}
