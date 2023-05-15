package mobit.elec;

import java.util.List;

import com.mobit.IDetail;
import mobit.eemr.IReadResult;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacKodu;

public interface ISayaclar extends IDetail {

	void add(ISayacBilgi sayac) throws Exception;
	
	void addEndeks(IEndeksTipi endeksTipi, String endeks) throws Exception;
	
	void addEndeks(IEndeksTipi endeksTipi, String tam, String kusurat) throws Exception;
	
	void addEndeks(IEndeks endeks) throws Exception;
	
	void remove(ISayacKodu sayacKodu);
	
	void remove(ISayacBilgi sayac);
	
	void remove(int sayac_no);
	
	List<ISayacBilgi> getSayaclar();
	
	IEndeksler getEndeksler();
	
	ISayacBilgi getSayac(ISayacKodu sayacKodu);
	
	ISayacBilgi getSayac(int sayac_no);
	
	ISayacBilgi getSayac(IEndeksTipi endeksTipi);
	
	void Kontrol() throws Exception;
	
	void EndeksKontrol() throws Exception;
	
	List<IReadResult> getOptikResult();
	
	void setOptikResult(IReadResult result) throws Exception;
	
	
}
