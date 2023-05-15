package mobit.elec;

import com.mobit.IIslem;
import mobit.elec.enums.IMulkiyet;
import mobit.elec.enums.IOkumaMetodu;

public interface ITakilanSayac extends IIslem, ISayacBilgi {


	int getTESISAT_NO();
	
	void setTESISAT_NO(int TESISAT_NO);
	
	int getSAHA_ISEMRI_NO();
	
	void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO);
	
	ISeriNo getSERI_NO();

	void setSERI_NO(ISeriNo SERI_NO);
	
	IMulkiyet getMULKIYET();

	void setMULKIYET(IMulkiyet MULKIYET);
	
	IOkumaMetodu getOKUMA_METODU();

	void setOKUMA_METODU(IOkumaMetodu OKUMA_METODU);
	
	IIsemri getIsemri();
	
	Boolean getYENI_SAYAC();
	
	void setYENI_SAYAC(boolean yeniSayac);
	
	
	
}
