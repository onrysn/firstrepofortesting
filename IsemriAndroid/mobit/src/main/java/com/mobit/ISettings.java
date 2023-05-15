package com.mobit;

import java.io.InputStream;
import java.util.Collection;

import com.mobit.INode;

public interface ISettings {

	public void load(String docName) throws Exception;
	
	public void load(InputStream sream) throws Exception;
	
	public void save() throws Exception;
	
	public <T extends INode> T getItem(Class<?> c, String Id);
	
	public <T extends INode> Collection<T> getItems(Class<?> c, String Id);
	
	public <T extends INode> Collection<T> getItems(Class<?> c);
	
	public void setItem(INode node);
	
	public String getHost();
	
	public int getTcpPort();
	
	public String getApn();
	
	public void setHost(String host);
	
	public void setTcpPort(int port);
	
	public void setApn(String apn);
	
	public String getVerisyon();
	
}
