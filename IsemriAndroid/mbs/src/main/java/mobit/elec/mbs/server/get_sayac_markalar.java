package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.sayac_marka;

public class get_sayac_markalar extends ICommand {

	
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return sayac_marka.class;
	}

	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		
		b.append("GET SAYAC_MARKALAR\n");
	}

}
