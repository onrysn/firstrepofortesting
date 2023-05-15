package com.mobit;

public class PrintItem {

	/*
	Date date = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	 */


	String Name;
	Object Value;
	PrintItem [] subItems = null;
	
	@Override
	public String toString()
	{
		return (Value != null) ? Value.toString() : "";
	}
	
	public String getName()
	{
		return Name;
	}
	public Object getValue()
	{
		return Value;
	}
	
	public boolean hasSubItems()
	{
		return subItems != null;
	}
	
	public PrintItem [] getSubItems()
	{
		return subItems;
	}

	public PrintItem(String name, Object value) {
		super();
		//HÜSEYİN EMRE ÇEVİK İŞEMRİ MESAJ
		Name = name;
		Value = value;
		if(Value instanceof String){
			//Value = ((String)Value).trim();
			String s = (String)Value;
			if(s.contains("#")){
				String [] items = s.split("#");
				int i = 0;
				subItems = new PrintItem[items.length];
				for(String item : items){
					subItems[i] = new PrintItem(Integer.toString(i), item);
					i++;
				}

			}
		}
	}
	
	
	
}
