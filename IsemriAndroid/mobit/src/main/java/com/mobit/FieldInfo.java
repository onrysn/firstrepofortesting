package com.mobit;

import com.mobit.DbType;

public class FieldInfo {

	public String Name;
	public boolean PrimaryKey = false;
	public int Size = 0;
	public DbType Type;
	public boolean Parse;
	
	public FieldInfo(String Name, int Size)
	{
		this(Name, Size, DbType.VARCHAR, true);
	}
	public FieldInfo(String Name, int Size, DbType Type)
	{
		this(Name, Size, Type, true);
	}
	public FieldInfo(String Name, int Size, DbType Type, boolean Parse)
	{
		this(Name, Type, Size, Parse);
		
	}
	public FieldInfo(String Name, DbType Type, int Size, boolean Parse)
	{
		this(Name, false, Type, Size, Parse);
	}
	
	public FieldInfo(String Name, boolean PrimaryKey, DbType Type, int Size, boolean Parse)
	{
		this.Name = Name;
		this.PrimaryKey = PrimaryKey;
		this.Size = Size;
		this.Type = Type;
		this.Parse = Parse;
	}
}
