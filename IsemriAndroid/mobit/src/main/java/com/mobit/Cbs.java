package com.mobit;



import java.sql.ResultSet;
import java.sql.SQLException;

import mobit.eemr.Lun_Control;




public class Cbs  implements ICbs  {
    private Double x = null, y = null;
	
	private boolean empty = true;
	
	public Cbs()
	{
		
	}
	
	public Cbs(Double x, Double y)
	{
		setXY(x, y);
	}
	public Cbs(String x, String y)
	{
		setXY(Double.parseDouble(x), Double.parseDouble(y));
	}
	
	public Cbs(ResultSet rs, String columnX, String columnY) throws SQLException
	{

		double x = rs.getDouble(columnX);
		if(rs.wasNull()) return;
		double y = rs.getDouble(columnY);
		if(rs.wasNull()) return;
		setXY(x, y);



	}
	
	public Cbs(ILocation location)
	{
		//muhammed gökkaya konum açıp kapama
		//KONUM ALMA
		/*
		Context applicationContext=getApplicationContext();
		SharedPreferences prefs = applicationContext.getSharedPreferences();
		*/
		Lun_Control Gps=new Lun_Control();
		String asd=Gps.Time;

		if(location != null){
			if (location.getAccuracy()>40.0)
			{
                if (Gps.Accuracy>0.0)
				{
					setXY(Gps.Latitude, Gps.Longitude);
				}
                else {
					setXY(location.getLatitude(), location.getLongitude());
				}


			}else{
				setXY(location.getLatitude(), location.getLongitude());
			}
		}

		
	}
	
	public boolean isEmpty()
	{
		return empty;
	}
	
	public Double getX() throws Exception
	{
		return this.x;
	}
	public Double getY() throws Exception
	{
		return this.y;
	}
	
	
	public void setXY(Double x, Double y)
	{
		
		this.x = x;
		this.y = y;
		if((x != null && x != 0) && (y != null && y != 0)) empty = false;
	}
	
	public void setXY(String x, String y)
	{
		setXY(Double.parseDouble(x), Double.parseDouble(y));	
	}
	
	protected static final String formatString = "%.7f; %.7f";
	
	public String toString()
	{
		try {
			
			return String.format(Globals.getLocale(), formatString, getX(), getY());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static Cbs fromString(String x, String y)
	{
		return new Cbs(x, y);
	}
	
	
	@Override
	public ILocation toLocation() throws Exception
	{
		ILocation location = Globals.platform.newLocationObject();
		location.setLatitude(getX());
		location.setLongitude(getY());
		
		return location;
	}
	
	
}
