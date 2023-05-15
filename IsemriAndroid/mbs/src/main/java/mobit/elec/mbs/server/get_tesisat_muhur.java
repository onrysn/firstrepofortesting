package mobit.elec.mbs.server;

import mobit.elec.mbs.get.field;

public class get_tesisat_muhur extends get_muhur_ortak {

	int TESISAT_NO;
	
	public get_tesisat_muhur(int TESISAT_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
	}
	
	static final String format = "GET TESISAT_MUHUR %0"+field.s_TESISAT_NO+"d\n";
	
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, TESISAT_NO));
		
	}

}
