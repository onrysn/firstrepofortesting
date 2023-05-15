package mobit.elec.mbs.medas;

import java.util.List;

import mobit.elec.mbs.IMbsApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.muhurBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.zabitBilgi;
import mobit.elec.osos.viko.IOperationService;

public interface IMedasApplication extends IMbsApplication {

	SayacZimmetBilgi newSayacZimmetBilgi();
	List<muhurBilgi> getMuhurSeriler();
	List<zabitBilgi> getZabitSeriler();
	
	
}
