package mobit.elec.mbs;


import com.mobit.IIslem;
import com.mobit.IServer;

import java.util.List;

import mobit.elec.IElecServer;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriSorular;
import mobit.elec.IKarne;
import mobit.elec.IMuhur;
import mobit.elec.ISeriNo;
import mobit.elec.enums.IIslemTipi;


public interface IMbsServer extends IElecServer, IServer {

	boolean isTest();
	// Mbs'ye has işlemler
	List<IIsemriSorular> fetchIsemriSorular() throws Exception;
	List<IIsemriSoru> fetchIsemriSoru(Integer TESISAT_NO, Integer ISEMRI_NO)throws Exception;
	
	void ismriKarneBirak(Integer KARNE_NO) throws Exception;
	void isemriBirak(Integer TESISAT_NO, Integer SAHA_ISEMRI_NO) throws Exception;
	
	List<IIsemri> fetchIsemri(int ZAMAN_KODU, boolean update) throws Exception;
	List<IIsemri> fetchKarneIsemri(int ZAMAN_KODU, Integer KARNE_NO) throws Exception;
	List<IIsemri> fetchTesisatIsemri(int ZAMAN_KODU, Integer TESISAT_NO) throws Exception;
	List<IKarne> fetchSerbestIsemri(int ZAMAN_KODU, IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU) throws Exception;

	void sendIslem(IIslem islem) throws Exception;
}
