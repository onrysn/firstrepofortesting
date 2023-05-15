package mobit.elec.mbs.put;

import mobit.elec.IIsemriAl;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_isemri_al extends ICommand implements IIsemriAl {

	
	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}
	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}
	public int getSAHA_ISEMRI_NO() {
		return SAHA_ISEMRI_NO;
	}
	public void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO) {
		com.mobit.utility.check(sAHA_ISEMRI_NO, field.s_SAHA_ISEMRI_NO);
		SAHA_ISEMRI_NO = sAHA_ISEMRI_NO;
	}
	
	public put_isemri_al(int tESISAT_NO, int sAHA_ISEMRI_NO) {
		super();
		setTESISAT_NO(tESISAT_NO);
		setSAHA_ISEMRI_NO(sAHA_ISEMRI_NO);
	}
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT ISEMRI_AL %0"+field.s_TESISAT_NO+"d%0"+field.s_SAHA_ISEMRI_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, SAHA_ISEMRI_NO));
	}

}
