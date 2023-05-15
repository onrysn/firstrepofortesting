package mobit.elec.mbs.server;

import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.isemri_sorular;

public class get_isemri_sorular extends ICommand {

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return isemri_sorular.class;
	}

	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append("GET ISEMRI_SORULAR\n");
	}

}
