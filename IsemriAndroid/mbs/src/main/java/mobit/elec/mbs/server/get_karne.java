package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.tesisat;

public class get_karne extends ICommand {

	
	int KARNE_NO;
	String TEDARIK_DUR;
	
	public get_karne(int KARNE_NO, String TEDARIK_DUR)
	{
		this.KARNE_NO = KARNE_NO;
		this.TEDARIK_DUR = TEDARIK_DUR;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return tesisat.class;
	}

	static final String format = "GET KARNE %0"+field.s_KARNE_NO+"d %-"+field.s_TEDARIK_DUR+"s\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, KARNE_NO, TEDARIK_DUR));
	}

}
