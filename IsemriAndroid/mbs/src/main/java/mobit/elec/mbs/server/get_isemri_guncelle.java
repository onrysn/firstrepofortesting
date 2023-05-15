package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_guncelle;

public class get_isemri_guncelle extends ICommand {

	int ZAMAN_KODU;
	
	public get_isemri_guncelle(int ZAMAN_KODU)
	{
		this.ZAMAN_KODU = ZAMAN_KODU;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return isemri_guncelle.class;
	}

	static final String format = "GET ISEMRI_GUNCELLE %0"+field.s_ZAMAN_KODU+"X\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, ZAMAN_KODU));
		
	}

}
