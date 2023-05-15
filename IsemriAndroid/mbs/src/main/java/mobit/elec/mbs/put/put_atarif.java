package mobit.elec.mbs.put;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.ICbs;
import com.mobit.IDb;
import mobit.elec.IAciklama;
import mobit.elec.IAtarif;
import mobit.elec.mbs.get.Aciklama;
import mobit.elec.mbs.get.Cbs;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_atarif extends ICommand implements IAtarif, IDb {

	private Integer id;
	private Integer masterId;
	
	private int TESISAT_NO;
	private IAciklama ADRES_TARIF;
	private ICbs CBS;
	
	public put_atarif()
	{
		
	}
	
	public put_atarif(int TESISAT_NO, Aciklama ADRES_TARIF, ICbs CBS) throws Exception
	{
		setTESISAT_NO(TESISAT_NO);
		setADRES_TARIF(ADRES_TARIF);
		setCBS(CBS);
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//-------------------------------------------------------------------------
	
	public static final String tableName = "atarif";
	public static final FieldInfo [] info = new FieldInfo[] {
			
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),	
			
			new FieldInfo(field.TESISAT_NO, DbType.VARCHAR, field.s_TESISAT_NO, true),
			new FieldInfo(field.ADRES_TARIF, DbType.VARCHAR, field.s_ADRES_TARIF, true),
			new FieldInfo(field.CBS_X, DbType.VARCHAR, field.s_CBS_X, true),
			new FieldInfo(field.CBS_Y, DbType.VARCHAR, field.s_CBS_Y, true),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false)
	};
	
	//-------------------------------------------------------------------------
	
	public static final String insertString = DbHelper.PrepareInsertString(put_atarif.class);
	public static final String updateString = DbHelper.PrepareUpdateString(put_atarif.class, field.ID);
	
	private static final String format = "PUT ATARIF %0"+field.s_TESISAT_NO+"d";

	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public IAciklama getADRES_TARIF() {
		return ADRES_TARIF;
	}

	public void setADRES_TARIF(IAciklama aDRES_TARIF) throws Exception {
		if(aDRES_TARIF != null) com.mobit.utility.check(aDRES_TARIF.getSTR(), field.s_ADRES_TARIF);
		ADRES_TARIF = aDRES_TARIF;
	}

	public ICbs getCBS() {
		return CBS;
	}

	public void setCBS(ICbs cBS) {
		CBS = cBS;
	}

	@Override
	public void toSerialize(StringBuilder sb) throws Exception {
		// TODO Auto-generated method stub
		
		if(CBS != null) new Cbs(CBS).toSerialize(sb); 
			
		sb.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO));
		new Aciklama(ADRES_TARIF).toSerialize(sb);
		sb.append('\n');
	}

	public static final int TABLOID = 12;
	
	@Override
	public int getTabloId() {
		// TODO Auto-generated method stub
		return TABLOID;
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
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		this.masterId = masterId;
	}

	@Override
	public void get(ResultSet rs) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		
		id = rs.getInt(field.ID);

		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setADRES_TARIF(new Aciklama(rs.getString(field.ADRES_TARIF)));
		setCBS(new Cbs(rs, field.CBS_X, field.CBS_Y));
		setMasterId(rs.getInt(field.ISLEM_ID));
		
	}

	@Override
	public int put(DbHelper h) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		
		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {
			
			stmt = h.getInsStatment(this);

			stmt.setObject(1, null);
			stmt.setInt(2, getTESISAT_NO());
			if(getADRES_TARIF() != null) stmt.setString(3, getADRES_TARIF().getSTR());
			ICbs cbs = getCBS();
			if(cbs != null){
				stmt.setDouble(4, cbs.getX());
				stmt.setDouble(5, cbs.getY());
			}
			stmt.setInt(6, masterId);
		

			cnt = stmt.executeUpdate();
			
		}
		
		if (cnt > 0) {
			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setInt(1, getTESISAT_NO());
			if(getADRES_TARIF() != null) stmt.setString(2, getADRES_TARIF().getSTR());
			ICbs cbs = getCBS();
			if(cbs != null){
				stmt.setDouble(3, cbs.getX());
				stmt.setDouble(4, cbs.getY());
			}
			stmt.setInt(5, masterId);
			// where
			stmt.setInt(6, getId());
			
			cnt = stmt.executeUpdate();
		}

		
		return cnt;
	}
	
	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<IAtarif> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IAtarif> list = new ArrayList<IAtarif>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_atarif at = new put_atarif();
				at.get(rs);
				list.add(at);
			}

		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}
	
}
