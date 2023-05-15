package mobit.elec;

import java.util.Date;


public interface IMuhur {

	int getTESISAT_NO();

	ISeriNo getSERI_NO();

	int getMUHUR_YERI();

	Date getMUHUR_TARIHI();

	int getMUHUR_NEDENI();

	String getMUHUR_DUR();

	void setTESISAT_NO(int tESISAT_NO);

	void setSERI_NO(ISeriNo sERI_NO);

	void setMUHUR_YERI(int mUHUR_YERI);

	void setMUHUR_TARIHI(Date mUHUR_TARIHI);

	void setMUHUR_NEDENI(int mUHUR_NEDENI);

	void setMUHUR_DUR(String mUHUR_DUR);
	
	String getRESULT_TYPE();
	
	void setRESULT_TYPE(String RESULT_TYPE);
	
	int getRESULT_CODE();
	
	void setRESULT_CODE(int RESULT_CODE);
	
	String getRESULT();
	
	void setRESULT(String RESULT);

	IMuhurKod getMUHUR_YERI2();
	IMuhurKod getMUHUR_NEDENI2();
	IMuhurKod getMUHUR_DUR2();

	boolean getCheck(); //H.Elif

	void setCheck(boolean checked); //H.Elif
}
