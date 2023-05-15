package mobit.elec;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Aciklama implements IAciklama {

	private String s;
	
	@Override
	public boolean isEmpty()
	{
		return s == null || s.isEmpty();
	}
	@Override
	public String getSTR() {
		return s;
	}
	@Override
	public void setSTR(String s) {
		this.s = s;
	}

	public Aciklama()
	{
		
	}
	
	public Aciklama(String s) {
		
		setSTR(s);
	}
	public Aciklama(CharSequence s) {
		
		String ss = (s != null) ? s.toString() : null;
		setSTR(ss);
	}
	
	public Aciklama(ResultSet rs, String columnLabel) throws SQLException {
		
		String s = rs.getString(columnLabel);
		if(!rs.wasNull()) setSTR(s);
	}
	
}
