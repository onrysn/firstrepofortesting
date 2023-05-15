package com.mobit;

public interface ISettings2 {

	public String getPrinterMac(int printerNo);
	
	public void setPrinterMac(int printerNo, String PrinterMac);
	
	public String getPrinterPIN(int printerNo);
	
	public void setPrinterPIN(int printerNo, String Pin);
	
	public String getOpticMac();
	
	public void setOpticMac(String Mac);
	
	public String getOpticPIN();
	
	public void setOpticPIN(String Pin);
	
	public void save() throws Exception;
}
