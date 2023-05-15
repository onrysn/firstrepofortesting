package mobit.elec;


import java.util.Date;
import java.util.List;

import com.mobit.Callback;
import com.mobit.IDetail;
import mobit.eemr.IReadResult;
import mobit.elec.enums.IFazSayisi;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IVoltaj;

public interface ISayacBilgi extends IDetail {

	
	Object clone();
	
	ISayacKodu getSAYAC_KODU();
	
	ISayacMarka getMARKA();

	int getSAYAC_NO();

	ISayacCinsi getSAYAC_CINSI();

	ISayacHaneSayisi getHANE_SAYISI();
	
	int getIMAL_YILI();
	
	Date getDAMGA_TARIHI();
	
	IFazSayisi getFAZ_SAYISI();

	int getAMPERAJ();

	IVoltaj getVOLTAJ();
		
	String getMODEL_KODU();
	
	IEndeksler getENDEKSLER();
	
	ISdurum getGIDEN_DURUM_KODU();
	
	List<String []> getDetay();
	

	void setSAYAC_KODU(ISayacKodu SAYAC_KODU);
		
	void setMARKA(ISayacMarka mARKA);

	void setSAYAC_NO(int sAYAC_NO);

	void setSAYAC_CINSI(ISayacCinsi sAYAC_CINSI);

	void setHANE_SAYISI(ISayacHaneSayisi hANE_SAYISI);
	
	void setIMAL_YILI(int iMAL_YILI);
	
	void setDAMGA_TARIHI(Date DAMGA_TARIHI);
	
	void setFAZ_SAYISI(IFazSayisi FAZ_SAYISI);
	
	void setAMPERAJ(int AMPERAJ);
	
	void setVOLTAJ(IVoltaj VOLTAJ);
	
	void setMODEL_KODU(String MODEL_KODU);
	
	void setGIDEN_DURUM_KODU(ISdurum GIDEN_DURUM_KODU) throws Exception;
	
	void sayacHaneKontrol(ISayacHaneSayisi h, Callback clb);
	
	void Kontrol() throws Exception;
	
	void EndeksKontrol() throws Exception;
	
	IReadResult getOptikResult();
	
	void setOptikResult(IReadResult result) throws Exception;

}
