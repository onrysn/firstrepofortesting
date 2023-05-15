package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;

import mobit.elec.ISeriNo;
import mobit.elec.ISerialize;

public class SeriNo implements ISeriNo, ISerialize {

	
	private String seri;
	private int no;
	
	private boolean empty = true;
	
	public SeriNo()
	{
		
	}
	public SeriNo(String seri, String no)
	{
		setSeri(seri);
		if(no != null && no.length() > 0) setNo(Integer.parseInt(no));
	}
	
	public SeriNo(ResultSet rs, String columnSeri, String columnNo) throws SQLException
	{
		String seri = rs.getString(columnSeri);
		if(!rs.wasNull()) setSeri(seri);
		int no = rs.getInt(columnNo);
		if(!rs.wasNull()) setNo(no);	
	}
	
	public boolean isEmpty()
	{
		return empty;
	}
	public String getSeri()
	{
		return this.seri;
	}
	public void setSeri(String seri)
	{
		com.mobit.utility.check(seri, field.s_MUHUR_SERI);
		this.seri = seri.toUpperCase();
	}
	public int getNo()
	{
		return this.no;
	}
	public void setNo(int no)
	{
		com.mobit.utility.check(no, field.s_MUHUR_NO);
		this.no = no;
		empty = false;
	}
	
	private static final String format0 = "%-"+field.s_MUHUR_SERI+"s/"+"%0"+field.s_MUHUR_NO+"d";
	
	@Override
	public String toString()
	{
		String seri = getSeri();
		if(seri == null) seri = "";
		return String.format(format0, seri, getNo());
	}
	
	public static SeriNo fromString(String seri, String no)
	{
		return new SeriNo(seri, no);
	}
	
	public static SeriNo fromString(String seri_no)
	{
		return new SeriNo(seri_no.substring(0,2), seri_no.substring(2));
	}
	
	private static final String format = "%-"+field.s_MUHUR_SERI+"s"+"%0"+field.s_MUHUR_NO+"d";
	private static final String format2 = "%-"+field.s_MUHUR_SERI+"s"+"%"+field.s_MUHUR_NO+"s";
	
	private String toSerialize()
	{
		if(getNo() > 0) return String.format(format, getSeri(), getNo());
		String seri = getSeri();
		if(seri == null) seri = "";
		return String.format(format2, seri, "");
	}
	
	public void toSerialize(StringBuilder b)
	{
		b.append(toSerialize());
	}
}
