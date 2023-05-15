package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.adurum;

public class get_adurum extends ICommand {

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return adurum.class;
	}

	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		
		b.append("GET ADURUM\n");
	}

}
