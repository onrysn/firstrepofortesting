package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_guncelle;

public class get_isemri_karne extends ICommand {
	
	Integer ZAMAN_KODU;
	int KARNE_NO;

	public get_isemri_karne(Integer ZAMAN_KODU, int KARNE_NO)
	{
		this.ZAMAN_KODU = ZAMAN_KODU;
		this.KARNE_NO = KARNE_NO;
	}
	
	@Override
	protected  Class<?> getClassInfo()
	{
		return (Class<?>)isemri_guncelle.class;
	}
	
	static final String format = "GET ISEMRI_KARNE %0"+field.s_ZAMAN_KODU+"x %0"+field.s_KARNE_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, ZAMAN_KODU != null ? ZAMAN_KODU : 0xffffffff, KARNE_NO));
		
	}

}
