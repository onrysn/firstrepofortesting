package mobit.elec.mbs.server;

import mobit.elec.ISeriNo;
import mobit.elec.ISerialize;
import mobit.elec.mbs.get.SeriNo;

public class get_muhur extends get_muhur_ortak {

	ISeriNo SERI_NO;
	
	public get_muhur(ISeriNo SERI_NO)
	{
		this.SERI_NO = SERI_NO;
	}
	
	static final String format = "GET MUHUR ";
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		b.append(format);
		((ISerialize)SERI_NO).toSerialize(b);
		b.append('\n');
	}
	
}
