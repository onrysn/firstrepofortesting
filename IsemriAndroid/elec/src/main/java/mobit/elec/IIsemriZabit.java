package mobit.elec;

import com.mobit.IIslem;

public interface IIsemriZabit extends IIslem {
	
	void setTESISAT_NO(int tESISAT_NO);

	int getSAHA_ISEMRI_NO();

	void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO);

	String getZABIT_SERI();

	void setZABIT_SERI(String zABIT_SERI);

	int getZABIT_NO();

	void setZABIT_NO(int zABIT_NO);

	int getOKUYUCU2_KODU();

	void setOKUYUCU2_KODU(int oKUYUCU2_KODU);
	
	int getZABIT_TIPI();
	
	void setZABIT_TIPI(int ZABIT_TIPI);
	
	int getOKUYUCU_KODU();

	void setOKUYUCU_KODU(int oKUYUCU_KODU);
	
	String getRESULT_TYPE();
	
	void setRESULT_TYPE(String RESULT_TYPE);
	
	int getRESULT_CODE();
	
	void setRESULT_CODE(int RESULT_CODE);
	
	String getRESULT();
	
	void setRESULT(String RESULT);
	
}
