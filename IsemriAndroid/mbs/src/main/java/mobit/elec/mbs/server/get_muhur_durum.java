package mobit.elec.mbs.server;

import mobit.elec.ISeriNo;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.SeriNo;

public class get_muhur_durum extends ICommand {

	ISeriNo SERI_NO;
	
	public get_muhur_durum(ISeriNo SERI_NO)
	{
		this.SERI_NO = SERI_NO;
		
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	static final String format = "GET MUHUR_DURUM %s\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, SERI_NO));
	}

}
