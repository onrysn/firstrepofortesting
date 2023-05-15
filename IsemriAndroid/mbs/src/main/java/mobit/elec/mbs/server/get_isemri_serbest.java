package mobit.elec.mbs.server;

import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.karne;

public class get_isemri_serbest extends ICommand {

	int ZAMAN_KODU;
	IIslemTipi ISLEM_TIPI;
	int ALT_EMIR_TURU;
	
	public get_isemri_serbest(int ZAMAN_KODU, IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU)
	{
		this.ZAMAN_KODU = ZAMAN_KODU;
		this.ISLEM_TIPI = ISLEM_TIPI;
		this.ALT_EMIR_TURU = ALT_EMIR_TURU;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return karne.class;
	}

	static final String format = "GET ISEMRI_SERBEST %0"+field.s_ZAMAN_KODU+"X %0"+field.s_ISLEM_TIPI+"d %s\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		String tur = (ALT_EMIR_TURU > 0) ? Integer.toString(ALT_EMIR_TURU) : "";
		b.append(String.format(format, ZAMAN_KODU, ISLEM_TIPI.getValue(), tur));
		
	}

}
