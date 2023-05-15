package com.mobit;

import com.mobit.ISettings;
import com.mobit.Map;
import com.mobit.Parameter;
import com.mobit.XMLLoader;

public class Settings extends XMLLoader implements ISettings {

	
	private static Map [] mapList = 
	{
		new Map("Parameter", Parameter.class),
		new Map("PrintForm", PrintForm.class),
		new Map("Printer", Printer.class)
		
	};
	
	@Override
	protected Map [] getMapList()
	{
		return mapList;
	}
	@Override
	public String getVerisyon()
	{
		INode node = getItem(Parameter.class, IDef.VER);
		return (node != null) ? node.getStringValue() : "";
	}
	
	@Override
	public String getHost()
	{
		return getItem("Host").Value.toString();
		
	}
	@Override
	public int getTcpPort(){
		return Integer.parseInt(getItem("Port").Value.toString());
	}

	@Override
	public String getApn() {
		// TODO Auto-generated method stub
		return getItem("Apn").Value.toString();
	}

	
	
	@Override
	public void setHost(String host) {
		// TODO Auto-generated method stub
		setItem(new Parameter("Host", host));
	}

	@Override
	public void setTcpPort(int port) {
		// TODO Auto-generated method stub
		setItem(new Parameter("Port", Integer.toString(port)));
	}

	@Override
	public void setApn(String apn) {
		// TODO Auto-generated method stub
		setItem(new Parameter("Apn", apn));
	}
			
}
