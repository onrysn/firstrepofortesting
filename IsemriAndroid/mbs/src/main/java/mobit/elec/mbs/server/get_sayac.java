package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.tesisat;

public class get_sayac extends ICommand {

	int SAYAC_NO;
	
	public get_sayac(int SAYAC_NO)
	{
		this.SAYAC_NO = SAYAC_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return tesisat.class;
	}

	private static final String format = "GET SAYAC %0"+field.s_SAYAC_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, SAYAC_NO));
		
	}

}
