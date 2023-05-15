package mobit.elec.mbs.put;

import com.mobit.ICbs;
import mobit.elec.Aciklama;
import mobit.elec.IAciklama;
import mobit.elec.ICbsPos;
import mobit.elec.mbs.get.Cbs;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_cbs_pos extends ICommand implements ICbsPos {

	private int TESISAT_NO;
	private ICbs CBS;
	private IAciklama CBS_EK1;
	private IAciklama CBS_EK2;
	private IAciklama CBS_EK3;
	
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public ICbs getCBS() {
		return CBS;
	}

	public void setCBS(ICbs cBS) {
		CBS = cBS;
	}

	public IAciklama getCBS_EK1() {
		return CBS_EK1;
	}

	public void setCBS_EK1(IAciklama cBS_EK1) throws Exception {
		com.mobit.utility.check(cBS_EK1.getSTR(), field.s_CBS_EK1);
		CBS_EK1 = cBS_EK1;
	}

	public IAciklama getCBS_EK2() {
		return CBS_EK2;
	}

	public void setCBS_EK2(IAciklama cBS_EK2) throws Exception {
		com.mobit.utility.check(cBS_EK2.getSTR(), field.s_CBS_EK);
		CBS_EK2 = cBS_EK2;
	}

	public IAciklama getCBS_EK3() {
		return CBS_EK3;
	}

	public void setCBS_EK3(IAciklama cBS_EK3) throws Exception {
		com.mobit.utility.check(cBS_EK3.getSTR(), field.s_CBS_EK);
		CBS_EK3 = cBS_EK3;
	}

	public put_cbs_pos(int TESISAT_NO, Cbs CBS, Aciklama CBS_EK1, Aciklama CBS_EK2, Aciklama CBS_EK3) throws Exception
	{
		setTESISAT_NO(TESISAT_NO);
		setCBS(CBS);
		setCBS_EK1(CBS_EK1);
		setCBS_EK2(CBS_EK2);
		setCBS_EK3(CBS_EK3);
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	static final String format = "PUT CBS_POS "+
			"%0"+field.s_TESISAT_NO+"d %s %s";
	
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, CBS, CBS_EK1));
		/*
		if(CBS_EK2 != null && CBS_EK2.length() > 0){
			String s;
			if(CBS_EK3 != null && CBS_EK3.length() > 0){
				s = String.format(utility.locale, "%s;%s", CBS_EK2, CBS_EK3);
			}
			else {
				s = CBS_EK2;
			}
			b.append(Base64.encodeToString(s.getBytes(), 0));
		}*/
		b.append("\n");
		
	}

}
