package mobit.elec;

import java.util.Date;

import com.mobit.ICbs;
import com.mobit.ILocation;

public interface ITesisatBilgi {

	int getKARNE_NO();

	int getSIRA_NO();

	int getSIRA_EK();

	int getTESISAT_NO();

	String getUNVAN();

	String getADRES();

	String getADRES_TARIF();

	String getSEMT();

	String getKESME_DUR();

	boolean getCIFT_TERIM_DUR();

	boolean getPUANT_DUR();

	int getGIDEN_DURUM_KODU();

	int getTARIFE_KODU();

	double getCARPAN();

	Date getSON_OKUMA_TARIHI();

	String getANET_ISLETME_KODU();

	String getANET_ABONE_NO();

	ICbs getCBS();

	ISayaclar getSAYACLAR();
	
	ISayaclar getKSAYACLAR();
	
	boolean getKONTROL_SAYAC_DUR();
	
	String getSOZLESME_DUR();

	//Onur
	//int getBINA_KODU();
	//int getSOZLESME_NO();

	//H.Elif
	int getOG_DUR();
	int getOLCUM_KODU();

	//-------------------------------------------------------------------------
	
	
	void setKARNE_NO(int kARNE_NO);

	void setSIRA_NO(int sIRA_NO);

	void setSIRA_EK(int sIRA_EK);

	void setTESISAT_NO(int tESISAT_NO);

	void setUNVAN(String uNVAN);

	void setADRES(String aDRES);

	void setADRES_TARIF(String aDRES_TARIF);

	void setSEMT(String sEMT);

	void setKESME_DUR(String kESME_DUR);

	void setCIFT_TERIM_DUR(boolean cIFT_TERIM_DUR);

	void setPUANT_DUR(boolean pUANT_DUR);
	
	void setGIDEN_DURUM_KODU(int gIDEN_DURUM_KODU);

	void setTARIFE_KODU(int tARIFE_KODU);

	void setCARPAN(double cARPAN);

	void setSON_OKUMA_TARIHI(Date sON_OKUMA_TARIHI);

	void setANET_ISLETME_KODU(String aNET_ISLETME_KODU);

	void setANET_ABONE_NO(String aNET_ABONE_NO);

	void setCBS(ICbs cBS);
	
	void setSOZLESME_DUR(String SOZLESME_DUR);

	//Onur
	//void setBINA_KODU(int bINA_KODU);
	//void setSOZLESME_NO(int sOZLESME_NO);


	//H.Elif
	void setOG_DUR(int oG_DUR);
	void setOLCUM_KODU(int oLCUM_KODU);

	//-------------------------------------------------------------------------
	
	boolean getCheck();
	
	void setCheck(boolean checked);
	
	float getDistance();
	
	void setDistance(float distance);
	
	boolean isOSOS();
	

}
