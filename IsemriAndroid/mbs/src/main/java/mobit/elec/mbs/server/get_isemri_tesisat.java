package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_guncelle;

public class get_isemri_tesisat extends ICommand {

	
	Integer ZAMAN_KODU;
	int TESISAT_NO;
	
	public get_isemri_tesisat(Integer ZAMAN_KODU, int TESISAT_NO)
	{
		this.ZAMAN_KODU = ZAMAN_KODU;
		this.TESISAT_NO = TESISAT_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return isemri_guncelle.class;
	}

	static final String format = "GET ISEMRI_TESISAT %0"+field.s_ZAMAN_KODU+"X %0"+field.s_TESISAT_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		
		b.append(String.format(format, ZAMAN_KODU != null ? ZAMAN_KODU : 0xffffffff, TESISAT_NO));
		
	}

}
