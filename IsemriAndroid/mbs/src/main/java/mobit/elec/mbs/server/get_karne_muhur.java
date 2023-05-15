package mobit.elec.mbs.server;

import mobit.elec.mbs.get.field;

public class get_karne_muhur extends get_muhur_ortak {

	
	private int KARNE_NO;
	
	public get_karne_muhur(int KARNE_NO)
	{
		this.KARNE_NO = KARNE_NO;
	}
	
	static final String format = "GET KARNE_MUHUR %0"+field.s_KARNE_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, KARNE_NO));
		
	}
	
}
