package com.mobit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IslemGrup implements IIslemGrup {

	private long grupId = new Date().getTime();
	private List<IIslem> islemler = new ArrayList<IIslem>();
	private Integer islemTipi;
	
	@Override
	public long getGrupId()
	{
		return grupId;
	}
	@Override
	public void setGrupId(long grupId)
	{
		this.grupId = grupId;
	}
	
	@Override
	public void add(IIslem islem) {
		// TODO Auto-generated method stub
		islemler.add(islem);
	}

	@Override
	public List<IIslem> getIslem() {
		// TODO Auto-generated method stub
		return new ArrayList<IIslem>(islemler);
	}
	
	@Override
	public List<IIslem> getIslem(Class<?> cls)
	{
		List<IIslem> list = new ArrayList<IIslem>();
		for(IIslem islem : islemler) if(cls.isInstance(islem)) list.add(islem);
		return list;
	}
	
	
	@Override
	public Integer getIslemTipi()
	{
		return islemTipi;
	}
	@Override
	public void setIslemTipi(Integer islemTipi)
	{
		this.islemTipi = islemTipi;
	}
	
	@Override
	public void remove(IIslem islem)
	{
		islemler.remove(islem);
	}


}
