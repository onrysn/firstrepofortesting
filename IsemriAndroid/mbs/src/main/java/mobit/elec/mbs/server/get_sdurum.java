package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.sdurum;

public class get_sdurum extends ICommand {

	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return sdurum.class;
	}

	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append("GET SDURUM\n");
	}

}
