package mobit.elec;

import java.util.Date;

import com.mobit.IIslem;
import mobit.elec.enums.IIslemDurum;
import mobit.elec.enums.IIslemTipi;

public interface IIsemri extends ITesisatBilgi {

	
	int getSAHA_ISEMRI_NO();

	IIslemTipi getISLEM_TIPI();
	
	IIslemDurum getISEMRI_DURUMU();
	
	int getATANMIS_GOREVLI();
	
	Date getISLEM_TARIHI();
	
	Date getISEMRI_TARIHI();
	
	int getALT_EMIR_TURU();
	
	//--------------
	
	void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO);

	void setISLEM_TIPI(IIslemTipi iSLEM_TIPI);
	
	void setISEMRI_DURUMU(IIslemDurum iSEMRI_DURUMU);

	void setATANMIS_GOREVLI(int aTANMIS_GOREVLI);
	
	void setISLEM_TARIHI(Date iSLEM_TARIHI);
	
	void setISEMRI_TARIHI(Date iSEMRI_TARIHI);
	
	void setALT_EMIR_TURU(int aLT_EMIR_TURU);
	
	//--------------
	
	
	IIslem newIslem() throws Exception;
	
	IIslemRapor getIslemRapor(IElecApplication app) throws Exception;
}

