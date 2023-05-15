package com.mobit;

public interface ICbs {

	
	public boolean isEmpty();
	
	public Double getX() throws Exception;
	
	public Double getY() throws Exception;
	
	public void setXY(Double x, Double y);
	
	public void setXY(String x, String y);
	
	ILocation toLocation() throws Exception;
	

}
