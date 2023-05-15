package com.mobit;

import java.util.Date;
import java.util.List;

public interface IIslemMaster extends IDb, IIslem, IResult {

	Integer getId();
	
	Date getZaman();
	
	void setZaman(Date zaman);
	
	int getTabloId();
	
	IRecordStatus getDurum();
	
	void setDurum(IRecordStatus durum);
	
	IIslem getIslem() throws Exception;
	
	void Save() throws Exception;
	
	void updateDurum(IRecordStatus durum) throws Exception;
	
	Integer getIsemriNo();
	
	void setIsemriNo(Integer isemriNo);
	
	void ResetId();
	
	void add(IIslem islem);
	
	ICbs getCBS();
	
	void setCBS(ICbs cbs);
	
	Integer getIslemTipi();
	
	void setIslemTipi(Integer islemTipi);
	
	IEleman getEleman();
	
	void setEleman(IEleman eleman);
	
	Long getGrupId();
	
	void setGrupId(Long id);
	
	
}
