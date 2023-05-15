package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mobit.Globals;
import com.mobit.ICbs;
import com.mobit.ILocation;
import mobit.elec.ISerialize;
import mobit.elec.mbs.utility;

public class Cbs extends com.mobit.Cbs implements ISerialize {
	
	
	public Cbs()
	{
		super();
	}
	
	public Cbs(ICbs cbs)
	{
		try {
			setXY(cbs.getX(), cbs.getY());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Cbs(Double x, Double y)
	{
		super(x, y);
	}
	public Cbs(String x, String y)
	{
		super(x, y);
	}
	
	public Cbs(ResultSet rs, String columnX, String columnY) throws SQLException
	{
		super(rs, columnX, columnY);
	}
	
	public Cbs(ILocation location)
	{
		super(location);
	}
		
	private static final String format = "CBS:%0"+field.s_CBS_X+"."+field.s_CBS_PREC+"f;%0"+field.s_CBS_Y+"."+field.s_CBS_PREC+"f";

	@Override
	public void toSerialize(StringBuilder sb)
	{
		if(!isEmpty()){
			try {
				sb.append(String.format(utility.MBS_LOCALE, format, getX(), getY()));
				sb.append("\t");
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public ILocation toLocation() throws Exception
	{
		ILocation location = Globals.platform.newLocationObject();
		location.setLatitude(getX());
		location.setLongitude(getY());
		
		return location;
	}
	
	
	public static Cbs fromResultSet(ResultSet rs) throws SQLException
	{
		return new Cbs(rs.getDouble(field.CBS_ENLEM), rs.getDouble(field.CBS_BOYLAM));
	}
	public static Cbs fromResultSet2(ResultSet rs) throws SQLException
	{
		return new Cbs(rs.getDouble(field.CBS_X), rs.getDouble(field.CBS_Y));
	}
	
	
}
