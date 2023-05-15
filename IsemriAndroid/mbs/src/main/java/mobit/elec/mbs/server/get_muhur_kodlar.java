package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.muhur_kod;

public class get_muhur_kodlar extends ICommand {

	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return muhur_kod.class;
	}

	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format("GET MUHUR_KODLAR\n"));
		
	}

}
