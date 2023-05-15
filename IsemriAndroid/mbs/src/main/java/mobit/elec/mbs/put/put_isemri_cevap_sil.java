package mobit.elec.mbs.put;

import mobit.elec.IIsemriCevapSil;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;

public class put_isemri_cevap_sil extends ICommand implements IIsemriCevapSil {

	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	
	public put_isemri_cevap_sil(int TESISAT_NO, int SAHA_ISEMRI_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, SAHA_ISEMRI_NO));
	}

	private static final String format = "PUT ISEMRI_CEVAP_SIL %0"+field.s_TESISAT_NO+"d%0"+field.s_SAHA_ISEMRI_NO+"d\n";

	
}
