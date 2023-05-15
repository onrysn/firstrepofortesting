package com.mobit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbHelper;

public interface IDb {

	int getTabloId();
	Integer getId();
	void setId(Integer id);
	void setMasterId(Integer masterId);
	void get(ResultSet rs) throws SQLException, ParseException, Exception;
	int put(DbHelper h) throws SQLException, ParseException, Exception;
	
}
