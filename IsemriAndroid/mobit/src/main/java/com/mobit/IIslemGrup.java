package com.mobit;

import java.util.List;

public interface IIslemGrup extends IIslem {

	long getGrupId();
	void setGrupId(long grupId);
	void add(IIslem islem);
	List<IIslem> getIslem();
	List<IIslem> getIslem(Class<?> cls);
	Integer getIslemTipi();
	void setIslemTipi(Integer islemTipi);
	void remove(IIslem islem);
}
