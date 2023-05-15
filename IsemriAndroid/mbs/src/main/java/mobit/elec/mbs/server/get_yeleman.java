package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.eleman;
import mobit.elec.mbs.get.field;

public class get_yeleman extends ICommand {

	
	int ELEMAN_KODU;
	int ELEMAN_SIFRE;
	
	public get_yeleman(int ELEMAN_KODU, int ELEMAN_SIFRE)
	{
		
		this.ELEMAN_KODU = ELEMAN_KODU;
		this.ELEMAN_SIFRE = ELEMAN_SIFRE;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return eleman.class;
	}

	static final String format = "GET YELEMAN %0"+field.s_ELEMAN_KODU+"d %0"+field.s_ELEMAN_SIFRE+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, ELEMAN_KODU, ELEMAN_SIFRE));
	}

}
