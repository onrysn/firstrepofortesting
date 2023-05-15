package mobit.elec.mbs.put;

import com.mobit.IIslem;
import mobit.elec.Globals;
import mobit.elec.IOptikData;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_optikdata extends ICommand implements IOptikData, IIslem  {

	
	private int TESISAT_NO;
	private String OPTIK_DATA;
	private int IsemriNo;

	//muhammed gökkaya
	public int getIsemriNo() {
		return IsemriNo;
	}

	public void setIsemriNo(int tIsemriNo) {
		IsemriNo = tIsemriNo;
	}
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public String getOPTIK_DATA() {
		return OPTIK_DATA;
	}

	public void setOPTIK_DATA(String oPTIK_DATA) {
		com.mobit.utility.check(oPTIK_DATA, field.s_OPTIK_DATA);
		OPTIK_DATA = oPTIK_DATA;
	}

	public put_optikdata()
	{
		
	}
	public put_optikdata(int tESISAT_NO, String oPTIK_DATA,int iSEMRINO)
	{
		setTESISAT_NO(tESISAT_NO);
		setOPTIK_DATA(oPTIK_DATA);
		setIsemriNo(iSEMRINO);
	}
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	static final String format = "PUT OPTIKDATA %0"+field.s_TESISAT_NO+"d\t%s\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
	
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, OPTIK_DATA,IsemriNo));

		// Test için değiştirilmesi gerekiyor
		// Sürekli loglama yapmaması için true kaldır!
		if(false && Globals.isDeveloping()) {
			Globals.platform.Log(OPTIK_DATA);
		}
	}
	
}
