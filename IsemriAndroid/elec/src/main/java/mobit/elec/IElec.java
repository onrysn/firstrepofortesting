package mobit.elec;

import java.util.List;
import mobit.elec.enums.IIslemTipi;

public interface IElec {
	
	List<IAdurum> getAboneDurum(Integer ABONE_DURUM_KODU, Integer KOD_TIPI) throws Exception;
	List<ISdurum> getSayacDurum(Integer SAYAC_DURUM_KODU, Integer KOD_TIPI) throws Exception;
	List<IIsemri> getIsemri(boolean update) throws Exception;
	List<IIsemri> getFullIsemri(boolean update) throws Exception;
	List<ITesisat> getTesisat(Integer TESISAT_NO) throws Exception;
	List<ITesisat> getTesisat2(Integer SAYAC_NO) throws Exception;
	List<IMuhurKod> getMuhurKod(Integer MUHUR_KOD) throws Exception;
	List<ITakilanSayac> getSayac(Integer SAYAC_NO) throws Exception;
	
	List<ISayacMarka> getSayacMarka(String SAYAC_MARKA) throws Exception;
	List<IIsemri> getKarneIsemri(Integer KARNE_NO) throws Exception;
	List<IIsemri> getTesisatIsemri(Integer TESISAT_NO) throws Exception;
	List<IKarne> getSerbestIsemri(IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU) throws Exception;
	List<ITesisat> getKarneTesisat(Integer KARNE_NO) throws Exception;
	
	List<IIsemriSoru> getIsemriSoru(Integer TESISAT_NO, Integer ISEMRI_NO) throws Exception;
	 
		
}
