package com.mobit;


public interface IEleman {

	
	int getELEMAN_KODU();

	String getELEMAN_ADI();
		
	int getELEMAN_SIFRE();


	void setELEMAN_KODU(int ELEMAN_KODU);

	void setELEMAN_ADI(String ELEMAN_ADI);

	void setELEMAN_SIFRE(int ELEMAN_SIFRE);


	boolean isTest();
	
	IYetki getYetki();
	
	String getYETKI();
	
	void setYETKI(String YETKI);
	

}
