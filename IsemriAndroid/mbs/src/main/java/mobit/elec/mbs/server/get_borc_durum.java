package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.borc_durum;
import mobit.elec.mbs.get.field;

public class get_borc_durum extends ICommand {

	
	int TESISAT_NO;
	String CEP_TEL_NO;
	
	public get_borc_durum(int TESISAT_NO, String CEP_TEL_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
		this.CEP_TEL_NO = CEP_TEL_NO;
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return borc_durum.class;
	}

	static final String format = "GET GUNCEL_BORC %0"+field.s_TESISAT_NO+"d %s\n";
	
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(format, TESISAT_NO, CEP_TEL_NO));
	
	}

}
