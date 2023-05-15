package mobit.elec;

import com.mobit.IIslem;

public interface IMuhurSokme extends IIslem {
	
	public int getTESISAT_NO();
	
	public void setTESISAT_NO(int tESISAT_NO);
	
	public ISeriNo getSERI_NO();
	
	public void setSERI_NO(ISeriNo sERI_NO);
	
	public IMuhurKod getMUHUR_DURUMU();
	
	public void setMUHUR_DURUMU(IMuhurKod mUHUR_DURUMU)throws Exception;
	
	public IMuhurKod getIPTAL_DUR();
	
	public void setIPTAL_DUR(IMuhurKod iPTAL_DUR)throws Exception;


}
