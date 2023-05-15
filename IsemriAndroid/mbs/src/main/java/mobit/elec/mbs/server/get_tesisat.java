package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.tesisat;

public class get_tesisat extends ICommand {

	int TESISAT_NO;
	
	public get_tesisat(int TESISAT_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
	}
	
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return tesisat.class;
	}

	static final String format = "GET TESISAT %0"+field.s_TESISAT_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, TESISAT_NO));
	
	}

}
