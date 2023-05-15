package mobit.elec;

import java.util.List;

import com.mobit.IDetail;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacHaneSayisi;

public interface IEndeksler extends IDetail {

	void add(IEndeks endeks);
	
	void add(IEndeksTipi endeksTipi, ISayacHaneSayisi haneSayisi, String endeks) throws Exception;
	
	void add(IEndeksTipi endeksTipi, ISayacHaneSayisi haneSayisi, String tam, String kusurat) throws Exception;
	
	void remove(IEndeksTipi endeksTipi);
	
	void remove(IEndeks endeks);
	
	List<IEndeks> getEndeksler();
	
	IEndeks getEndeks(IEndeksTipi endeksTipi);
}
