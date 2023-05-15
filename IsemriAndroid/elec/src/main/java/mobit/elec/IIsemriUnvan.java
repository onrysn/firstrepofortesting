package mobit.elec;

import com.mobit.IIslem;

public interface IIsemriUnvan extends IIslem {

	public int getTESISAT_NO();
	
	public void setTESISAT_NO(int TESISAT_NO);
	
	public int getSAHA_ISEMRI_NO();
	
	public void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO);
	
	public IAciklama getUNVAN();
	
	public void setUNVAN(IAciklama UNVAN) throws Exception;
	
}
