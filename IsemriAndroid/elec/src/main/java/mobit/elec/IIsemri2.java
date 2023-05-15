package mobit.elec;

import java.util.List;


public interface IIsemri2 extends IIsemri {

	int getZAMAN_KODU();

	char getHAREKET_KODU();

	int getALT_ISLEM_TIPI();
	
	String getISEMRI_ACIKLAMA();
	
	int getBORC_ADET();

	double getBORC_TUTARI();

	double getBORC_GECIKME();

	String getTELEFON();

	String getTELEFON2();
	
	String getCEP_TELEFON();

	//Onur
	//int getBINA_KODU();
	//int getSOZLESME_NO();

	//H.Elif
	int getOG_DUR();
	int getOLCUM_KODU();
	int getHESAP_KODU();
	int getSOZLESME_GUCU();
	int getKURULU_GUC();
	String getORTAK_TRAFO_DURUMU();
	
	
	//---------------------------------	
	
	void setZAMAN_KODU(int zAMAN_KODU);

	void setHAREKET_KODU(char hAREKET_KODU);
	
	void setALT_ISLEM_TIPI(int aLT_ISLEM_TIPI);
	
	void setISEMRI_ACIKLAMA(String iSEMRI_ACIKLAMA);

	void setBORC_ADET(int bORC_ADET);
	
	void setBORC_TUTARI(double bORC_TUTARI);
	
	void setBORC_GECIKME(double bORC_GECIKME);
	//Muhammed Gökkaya Samet abinin isteğiyle gizlendi.
	void setTELEFON(String tELEFON);
	
	void setTELEFON2(String tELEFON2);
	
	void setCEP_TELEFON(String cEP_TELEFON);

	//Onur
	//void setBINA_KODU(int bINA_KODU);
	//void setSOZLESME_NO(int sOZLESME_NO);

	//H.Elif
	void setOG_DUR(int oG_DUR);
	void setOLCUM_KODU(int oLCUM_KODU);
	void setHESAP_KODU(int hESAP_KODU);
	void setSOZLESME_GUCU(int sOZLESME_GUCU);
	void setKURULU_GUC(int kURULU_GUC);
	void setORTAK_TRAFO_DURUMU(String oRTAK_TRAFO_DURUMU);

	//------------------------------------
	
	void setSayacIslem(ISayacIslem islem);
	
	ISayacIslem getSayacIslem();
	
	List<IIsemriSoru> getIsemriSorular();
	
	ITakilanSayac newTakilanSayac();
	
	IIsemriZabit newZabit();
	
	IMuhurSokme newMuhurSokme();
	
	ITesisatMuhur newMuhurleme();
	
	
}
