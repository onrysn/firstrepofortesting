package mobit.elec.mbs.get;


import java.sql.ResultSet;
import java.sql.SQLException;

import mobit.elec.IAciklama;
import mobit.elec.ISerialize;
import mobit.elec.mbs.server.Base64;

public class Aciklama extends mobit.elec.Aciklama implements ISerialize {

	
	public Aciklama()
	{
		super();
	}
	
	public Aciklama(String s) {
		
		super(s);
	}
	public Aciklama(IAciklama a) {
		
		super(a.getSTR());
	}
	public Aciklama(CharSequence s) {
		
		String ss = (s != null) ? s.toString() : null;
		setSTR(ss);
	}
	
	public Aciklama(ResultSet rs, String columnLabel) throws SQLException
	{
		super(rs, columnLabel);
	}
	
	@Override
	public void toSerialize(StringBuilder  b) {
		
		if(getSTR() != null) b.append(Base64.encodeToString(getSTR().getBytes(), 0));
	}

}
