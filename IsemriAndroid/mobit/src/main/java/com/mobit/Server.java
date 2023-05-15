package com.mobit;

import java.io.Closeable;
import java.util.Date;
import java.util.List;

import com.mobit.IApplication;
import com.mobit.PageData;


public abstract class Server implements IServer, Closeable {

	protected IApplication app;
	
	public void init(IApplication app) throws Exception
	{
		this.app = app;
	}
	
	public abstract void close();
	
	public abstract List<PageData> parse(Object obj);
	
	public abstract void connect() throws Exception;
	
	public abstract Date getUTCTime();
	
}
