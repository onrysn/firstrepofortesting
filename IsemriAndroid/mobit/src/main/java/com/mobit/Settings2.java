package com.mobit;

public class Settings2 extends XMLLoader implements ISettings2 {

	private static Map [] mapList = 
	{
		new Map("Parameter", Parameter.class)
	};
		
	@Override
	protected Map [] getMapList()
	{
		return mapList;
	}
		
	@Override
	public String getPrinterMac(int printerNo) {
		// TODO Auto-generated method stub
		
		INode node = getItem(String.format("Printer%dMac", printerNo));
		return (node != null) ? node.Value.toString() : "";
	}

	@Override
	public void setPrinterMac(int printerNo, String PrinterMac) {
		// TODO Auto-generated method stub
		String key = String.format("Printer%dMac", printerNo);
		setItem(new Parameter(key, PrinterMac)); 
		
	}

	@Override
	public String getPrinterPIN(int printerNo) {
		// TODO Auto-generated method stub
		
		INode node = getItem(String.format("Printer%dPin", printerNo));
		return (node != null) ? node.Value.toString() : "";
	}

	@Override
	public void setPrinterPIN(int printerNo, String Pin) {
		// TODO Auto-generated method stub
		String key = String.format("Printer%dPin", printerNo);
		setItem(new Parameter(key, Pin)); 
		
	}
	
	public String getOpticMac()
	{
	
		INode node = getItem("OpticMac");
		return node != null ? node.Value.toString() : "";
	}
	
	public void setOpticMac(String Mac)
	{
		INode node = getItem("OpticMac");
		if(node != null)  node.setValue(Mac);
	}
	
	public String getOpticPIN()
	{
		INode node = getItem("OpticPin");
		return node != null ? node.Value.toString() : "";
	}
	
	public void setOpticPIN(String Pin)
	{
		INode node = getItem("OpticPin");
		if(node != null) node.setValue(Pin);
	}

}
