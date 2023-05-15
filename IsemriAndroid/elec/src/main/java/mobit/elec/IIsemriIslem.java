package mobit.elec;

import java.sql.Connection;
import java.util.Date;

import com.mobit.ICbs;
import com.mobit.IIslem;
import com.mobit.IRecordStatus;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.IIslemTipi;

public interface IIsemriIslem extends IIslem, IAboneDurum, ISayacDurum {
	
	int getTESISAT_NO();
	
	void setTESISAT_NO(int TESISAT_NO);
	
	int getSAHA_ISEMRI_NO();
	
	void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO);
	
	ISeriNo getSERINO();
	
	void setSERINO(ISeriNo MUHUR_SERINO);
	
	ISayaclar getSAYACLAR();
	
	void setSON_ENDEKS(IEndeksTipi endeksTipi, String endeks) throws Exception;
	
	//IEndeks getCEKILEN_GUC();
	
	//void setCEKILEN_GUC(IEndeks CEKILEN_GUC);
	
	ICbs getCBS();
	
	void setCBS(ICbs CBS);
	
	int getELEMAN_KODU();
	
	void setELEMAN_KODU(int ELEMAN_KODU);
	
	String getOPTIK_DATA();
	
	void setOPTIK_DATA(Object OPTIK_DATA);
	
	String getFormType();
	
	void setDURUM(IRecordStatus durum);
	
	IRecordStatus getDURUM();
	
	void updateDurum(Connection conn) throws Exception;
	
	IIsemri getIsemri();
	
	void IslemKontrol(IElecApplication app) throws Exception;
	
	void setSINYAL_SEVIYESI(ISinyalSeviye SINYAL_SEVIYESI);

	ISinyalSeviye getSINYAL_SEVIYESI();
		
	boolean getIsemriTamamlanacak();
	
	void setIsemriTamamlanacak(boolean tamamlanacak);
	
	IIslemTipi getISLEM_TIPI();
	
	void setISLEM_TIPI(IIslemTipi ISLEM_TIPI);
	
	Date getZAMAN();
	
	void setZAMAN(Date ZAMAN);

}
