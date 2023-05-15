package mobit.elec.mbs;


import java.util.List;

import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriSoru;

public interface IMbsApplication extends IElecApplication {

	// Mbs has işlemler
	List<IIsemriSoru> getIsemriSorular(IIsemri isemri);
	
	
	
}
