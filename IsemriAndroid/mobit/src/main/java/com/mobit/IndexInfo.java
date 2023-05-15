package com.mobit;

public class IndexInfo {

	public String Name;
	public boolean Unique = false;
	public String [] fieldNames;
	
	public IndexInfo(String Name, boolean Unique, String ... fieldNames)
	{
		this.Unique = Unique;
		this.Name = Name;
		this.fieldNames = fieldNames;
	}
	
}
