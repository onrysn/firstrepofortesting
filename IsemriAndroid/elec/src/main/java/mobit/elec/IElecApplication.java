package mobit.elec;

import java.util.List;

import com.mobit.IApplication;
import com.mobit.IIslem;
import com.mobit.ILocation;
import com.mobit.Operation;
import mobit.eemr.ICallback;
import mobit.eemr.IMeterReader;
import mobit.eemr.IReadResult;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.enums.IIslemTipi;


public interface IElecApplication extends IApplication, IElec, IElecServer {

	IIsemri getActiveIsemri();
	void setActiveIsemri(IIsemri isemri);
	IIslem getActiveIslem();
	void setActiveIslem(IIslem islem);
	String getPortMac();
	void setPortMac(String PortMac);
	String getPortPIN();
	void setPortPIN(String Pin);
	int getIslemRenk(IIslemTipi tip);
	IMeterReader getMeterReader();
	void setPortCallback(ICallback clb);
	void setOptikResult(IReadResult result);
	IReadResult getOptikResult();
	
	
	IIsemri getIsemri(Integer ISEMRI_NO) throws Exception;
	List<IIsemriIslem> getIsemriIslem(Integer ISEMRI_NO) throws Exception;
	
	void ismriKarneBirak(Integer KARNE_NO) throws Exception;
	void isemriBirak(Integer TESISAT_NO, Integer SAHA_ISEMRI_NO) throws Exception; 
	
	IIsemri position(int SAHA_ISEMRI_NO) throws Exception;
	IIsemri positionFirst(int KARNE_NO) throws Exception;

	void printIsemriIslem(int SAHA_ISEMRI_NO) throws Exception;
	List<String> getAboneDurumAciklama(IIsemriIslem islem, IAdurum durum) throws Exception;
	Operation [] getOperationList(IIslemTipi islemTipi);
	Object getOptikData(List<IReadResult> rr) throws Exception;
	void Sirala(List<IIsemri> list, ILocation location) throws InterruptedException;
	
	AltEmirTuru [] getAltEmirTuru(IIslemTipi islemTipi);
	
	boolean Kontrol(int mode, IIsemriIslem islem) throws Exception;
	
	
	boolean checkFilter(IsemriFilterParam param);
	void Filter(List<IIsemri> list, IsemriFilterParam param);
	
	IIsemriRapor getIsemriRapor() throws Exception;

	//void Gonder(final IForm form, IIslemMaster master, final Callback clb) throws Exception;
	//void Gonder(final IForm form, IIsemri isemri, final Callback clb) throws Exception;
	//void Gonder(IForm form, List<IIsemri> arrayList, final Callback clb);

	List<IKarne> getIsemriKarneListesi()throws Exception;
	List<IKarne> getOkumaKarneListesi()throws Exception;

	IOkumaRapor getOkumaRaporu(int karne_no)throws Exception;

	String printNewOdf(OlcuDevreForm odf) throws Exception;
}
