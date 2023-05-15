package com.mobit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetWrapper {

	private ResultSet rs = null;
	
	public ResultSetWrapper(ResultSet rs)
	{
		this.rs = rs;
	}
	
	public Integer getInt(String columnLabel) throws SQLException
	{
		Integer i = rs.getInt(columnLabel);
		if(rs.wasNull()) 
			return (Integer)null;
		return i;
	}
	public Double getDouble(String columnLabel) throws SQLException
	{
		Double d = rs.getDouble(columnLabel);
		if(rs.wasNull()) return null;
		return d;
	}
	public String getString(String columnLabel) throws SQLException
	{
		String s = rs.getString(columnLabel);
		if(rs.wasNull()) return null;
		return s;
	}
}
