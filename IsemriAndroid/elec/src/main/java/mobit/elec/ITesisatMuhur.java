package mobit.elec;

import com.mobit.IIslem;

public interface ITesisatMuhur extends IIslem {
	
	public int getTESISAT_NO();
	
	public void setTESISAT_NO(int tESISAT_NO);

	public ISeriNo getSERI_NO();

	public void setSERI_NO(ISeriNo sERI_NO);

	public IMuhurKod getMUHUR_YERI();

	public void setMUHUR_YERI(IMuhurKod mUHUR_YERI) throws Exception;

	public IMuhurKod getMUHUR_NEDENI();

	public void setMUHUR_NEDENI(IMuhurKod mUHUR_NEDENI) throws Exception;


}
