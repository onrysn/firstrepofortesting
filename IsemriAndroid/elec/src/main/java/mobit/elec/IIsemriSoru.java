package mobit.elec;

import com.mobit.IIslem;
import mobit.elec.enums.ICevapTipi;

public interface IIsemriSoru extends IIslem {

	
	int getTESISAT_NO();
	
	int getSAHA_ISEMRI_NO();
	
	int getSORU_NO();

	String getSORU_ACIKLAMA();

	ICevapTipi getCEVAP_TIPI();

	String getCEVAP_FORMAT();
	
	String [] getCEVAP_FORMAT_LIST();
	
	String getCEVAP();
	
	boolean getCheck();
	
	boolean isActive();
	
	void setTESISAT_NO(int tESISAT_NO);
	
	void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO);

	void setSORU_NO(int sORU_NO);

	void setSORU_ACIKLAMA(String sORU_ACIKLAMA);

	void setCEVAP_TIPI(ICevapTipi cEVAP_TIPI);

	void setCEVAP_FORMAT(String cEVAP_FORMAT);
	
	void setCEVAP(String cEVAP);
	
	void setCheck(boolean checked);
	
	void setActive(boolean active);
	
	String getRESULT_TYPE();
	
	void setRESULT_TYPE(String RESULT_TYPE);
	
	int getRESULT_CODE();
	
	void setRESULT_CODE(int RESULT_CODE);
	
	String getRESULT();
	
	void setRESULT(String RESULT);
	

}
