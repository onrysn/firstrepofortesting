package mobit.elec;

import com.mobit.IIslem;
import mobit.elec.ITakilanSayac;

public interface ISayacIslem extends IIslem {
	
	int getTESISAT_NO();
	
	void setTESISAT_NO(int TESISAT_NO);
	
	int getSAHA_ISEMRI_NO();
	
	void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO);
	
	ISayaclar getSAYACLAR();
	
	ITakilanSayac newTakilanSayac();
	
	void Kontrol() throws Exception;

}
