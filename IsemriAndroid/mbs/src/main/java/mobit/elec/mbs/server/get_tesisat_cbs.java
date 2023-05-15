package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.koordinat;

public class get_tesisat_cbs extends ICommand {

	
	int TESISAT_NO;
	

	public get_tesisat_cbs(int TESISAT_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return koordinat.class;
	}

	static final String format = "GET TESISAT_CBS %0"+field.s_TESISAT_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, TESISAT_NO));
		
	}

}
