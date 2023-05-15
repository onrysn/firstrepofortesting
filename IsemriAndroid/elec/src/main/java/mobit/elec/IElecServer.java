package mobit.elec;

import java.util.List;

import com.mobit.Callback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IServer;

import mobit.elec.enums.IIslemTipi;

public interface IElecServer {

	List<IAdurum> fetchAboneDurum(Integer ABONE_DURUM_KODU, Integer KOD_TIPI) throws Exception;
	List<ISdurum> fetchSayacDurum(Integer SAYAC_DURUM_KODU, Integer KOD_TIPI) throws Exception;
	List<ITesisat> fetchTesisat(Integer TESISAT_NO) throws Exception;
	List<ITesisat> fetchTesisat2(Integer SAYAC_NO) throws Exception;
	List<IMuhurKod> fetchMuhurKod(Integer MUHUR_KOD) throws Exception;
	List<ITakilanSayac> fetchSayac(Integer SAYAC_NO) throws Exception;
	List<ISayacMarka> fetchSayacMarka(String SAYAC_MARKA) throws Exception;
	
	List<IIsemri> fetchIsemri(Integer ISEMRI_NO, IsemriFilterParam filter) throws Exception;
	List<IKarne> fetchSerbestIsemri(IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU, IsemriFilterParam filter)  throws Exception;
	List<IIsemri> fetchKarneIsemri(Integer KARNE_NO, IsemriFilterParam filter) throws Exception;
	List<IIsemri> fetchTesisatIsemri(Integer TESISAT_NO, IsemriFilterParam filter) throws Exception;
	List<ITesisat> fetchKarneTesisat(Integer KARNE_NO, IsemriFilterParam filter) throws Exception;
	List<IMuhur> fetchKarneMuhur(int KARNE_NO) throws Exception;
	List<IMuhur> fetchTesisatMuhur(int TESISAT_NO) throws Exception;
	List<IMuhur> fetchMuhur(ISeriNo muhur) throws Exception;
	List<IMuhur> fetchMuhurDurum(ISeriNo muhur) throws Exception;
	
	void sendIslem(IForm form, IIslem islem, Callback clb, int timeout) throws Exception;

}
