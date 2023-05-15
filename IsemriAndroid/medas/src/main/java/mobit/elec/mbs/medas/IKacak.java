package mobit.elec.mbs.medas;

import com.mobit.IIslem;

public interface IKacak extends IIslem {

	String getADRES_TARIF();
	void setADRES_TARIF(String ADRES_TARIF);
	String getKACAKCI_UNVAN();
	void setKACAKCI_UNVAN(String KACAKCI_UNVAN);
	String getKACAKCI_TELEFON();
	void setKACAKCI_TELEFON(String KACAKCI_TELEFON);
	String getKACAKCI_EMAIL();
	void setKACAKCI_EMAIL(String KACAKCI_EMAIL);
	String getKACAK_TIPI();
	void setKACAK_TIPI(String KACAK_TIPI);
	String getIHBAR_EDEN_UNVAN();
	void setIHBAR_EDEN_UNVAN(String IHBAR_EDEN_UNVAN);
	String getIHBAR_EDEN_TELEFON();
	void setIHBAR_EDEN_TELEFON(String IHBAR_EDEN_TELEFON);
	String getIHBAR_EDEN_EMAIL();
	void setIHBAR_EDEN_EMAIL(String IHBAR_EDEN_EMAIL);
	Integer getTESISAT_NO();
	void setTESISAT_NO(Integer TESISAT_NO);
	Integer getREFERANS_TESISAT_NO();
	void setREFERANS_TESISAT_NO(Integer REFERANS_TESISAT_NO);
	String getDIREK_NO();
	void setDIREK_NO(String DIREK_NO);
	String getBOX_NO();
	void setBOX_NO(String BOX_NO);
	Integer getSAYAC_NO();
	void setSAYAC_NO(Integer SAYAC_NO);
	String getACIKLAMA();
	void setACIKLAMA(String ACIKLAMA);
	
}
