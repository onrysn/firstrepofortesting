package com.mobit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IDb;
import com.mobit.IEleman;

public class eleman implements IDb, IEleman {

	private Integer id = null;

	protected int ELEMAN_KODU;
	protected String ELEMAN_ADI;
	protected int ELEMAN_SIFRE;
	protected IYetki YETKI;

	
	public eleman(){
		
	}
	public eleman(int ELEMAN_KODU, String ELEMAN_ADI, int ELEMAN_SIFRE)
	{
		setELEMAN_KODU(ELEMAN_KODU);
		setELEMAN_ADI(ELEMAN_ADI);
		setELEMAN_SIFRE(ELEMAN_SIFRE);
		
	}
	
	public eleman(ResultSet rs, String sELEMAN_KODU) throws SQLException
	{
		setELEMAN_KODU(rs.getInt(sELEMAN_KODU));
	}
	
	
	// -------------------------------------------------------------------------

	public static final String tableName = "eleman";
	
	protected static final String sID = "ID";
	protected static final String sELEMAN_KODU = "ELEMAN_KODU";
	protected static final String sELEMAN_ADI = "ELEMAN_ADI";
	protected static final String sELEMAN_SIFRE = "ELEMAN_SIFRE";
	
	protected static final int s_ELEMAN_KODU = 0;
	protected static final int s_ELEMAN_ADI = 30;
	protected static final int s_ELEMAN_SIFRE = 8;
	
	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(sID, true, DbType.INTEGER, 0, false),

			new FieldInfo(sELEMAN_KODU, DbType.INTEGER, s_ELEMAN_KODU, false),
			new FieldInfo(sELEMAN_ADI, DbType.VARCHAR, s_ELEMAN_ADI, false),
			new FieldInfo(sELEMAN_SIFRE, DbType.VARCHAR, s_ELEMAN_SIFRE, false) };
	
	public static final String insertString = DbHelper.PrepareInsertString(eleman.class);
	public static final String updateString = DbHelper.PrepareUpdateString(eleman.class, sID);

	// -------------------------------------------------------------------------

	@Override
	public String toString() {
		return ELEMAN_ADI;
	}
	
	private static final int TABLOID = 2;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}
	@Override
	public boolean isTest()
	{
		return  false;
		//return getELEMAN_KODU() == 1111;
	}
	@Override
	public int getELEMAN_KODU() {
		return ELEMAN_KODU;
	}
	@Override
	public String getELEMAN_ADI() {
		return ELEMAN_ADI;
	}
	
	@Override
	public int getELEMAN_SIFRE() {
		return ELEMAN_SIFRE;
	}


	@Override
	public void setELEMAN_KODU(int ELEMAN_KODU) {
		this.ELEMAN_KODU = ELEMAN_KODU;
	}

	@Override
	public void setELEMAN_ADI(String ELEMAN_ADI) {
		this.ELEMAN_ADI = ELEMAN_ADI;
	}

	@Override
	public void setELEMAN_SIFRE(int ELEMAN_SIFRE) {
		this.ELEMAN_SIFRE = ELEMAN_SIFRE;
	}


	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	@Override
	public void get(ResultSet rs) throws SQLException, ParseException {
		// TODO Auto-generated method stub

		id = rs.getInt(sID);

		setELEMAN_KODU(rs.getInt(sELEMAN_KODU));
		setELEMAN_ADI(rs.getString(sELEMAN_ADI));
		setELEMAN_SIFRE(rs.getInt(sELEMAN_SIFRE));

	}

	@Override
	public int put(DbHelper h) throws Exception {
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {
			
			stmt = h.getInsStatment(this);

			stmt.setObject(1, null);
			stmt.setInt(2, getELEMAN_KODU());
			stmt.setString(3, getELEMAN_ADI());
			stmt.setInt(4, getELEMAN_SIFRE());

			cnt = stmt.executeUpdate();
			
		}
		
		if (cnt > 0) {
			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setInt(1, getELEMAN_KODU());
			stmt.setString(2, getELEMAN_ADI());
			stmt.setInt(3, getELEMAN_SIFRE());

			stmt.setInt(4, getId());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}
	@Override
	public IYetki getYetki() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getYETKI() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setYETKI(String YETKI) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}

}
