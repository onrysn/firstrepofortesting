package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;

import mobit.elec.ISerialize;
import mobit.elec.ISinyalSeviye;

public class SinyalSeviye implements ISinyalSeviye, ISerialize {

	private int SEVIYE;
	private boolean empty = true;
	
	
	public SinyalSeviye()
	{
	}
	public SinyalSeviye(int SEVIYE)
	{
		setSEVIYE(SEVIYE);
	}
	
	public SinyalSeviye(ResultSet rs, String columnLabel) throws SQLException
	{
		int s = rs.getInt(columnLabel);
		if(!rs.wasNull()) setSEVIYE(s);
	}
	
	
	public boolean isEmpty()
	{
		return empty;
	}
	
	public int getSEVIYE() throws Exception { 
		//if(isEmpty()) throw new Exception("Sinyal seviyesi tanımlı değil!");
		return SEVIYE;
	}
	public void setSEVIYE(int SEVIYE) { 
		this.SEVIYE = SEVIYE;
		empty = false;
	}
	
	private static final String format = "%c";
	
	@Override
	public void toSerialize(StringBuilder b)
	{
		if(!isEmpty())
			b.append(String.format(format, SEVIYE + 'A' - 6));
		else
			b.append(" ");
	}
	
	public static ISinyalSeviye fromInteger(int x)
	{
		return new SinyalSeviye(x);
	}
	
}
