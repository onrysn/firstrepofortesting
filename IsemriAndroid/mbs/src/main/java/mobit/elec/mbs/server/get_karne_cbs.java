package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.koordinat;

public class get_karne_cbs extends ICommand {

	int KARNE_NO;
	
	public get_karne_cbs(int KARNE_NO)
	{
		this.KARNE_NO = KARNE_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return koordinat.class;
	}

	static final String format = "GET KARNE_CBS %0"+field.s_KARNE_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, KARNE_NO));
	}

}
