package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_soru;

public class get_isemri_soru extends ICommand {

	
	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	
	public get_isemri_soru(int TESISAT_NO, int SAHA_ISEMRI_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return isemri_soru.class;
	}

	static final String format = "GET ISEMRI_SORU %0"+field.s_TESISAT_NO+"d%0"+field.s_SAHA_ISEMRI_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, TESISAT_NO, SAHA_ISEMRI_NO));
		
	}
	
	public int getTESISAT_NO()
	{
		return TESISAT_NO;
	}
	public int getSAHA_ISEMRI_NO()
	{
		return SAHA_ISEMRI_NO;
	}
	

}
