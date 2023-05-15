package com.mobit;

import java.util.Date;
import com.mobit.IApplication;


public interface IServer {

	boolean isTest();
	void init(IApplication app)throws Exception;
	void close();
	void connect() throws Exception;
	Date getUTCTime();
	
}
