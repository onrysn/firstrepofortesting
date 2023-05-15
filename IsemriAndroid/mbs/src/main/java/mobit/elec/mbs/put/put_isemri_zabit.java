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
import com.mobit.IDb;
import com.mobit.IndexInfo;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriZabit;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_isemri_zabit extends ICommand implements IDb, IIsemriZabit {

	private Integer masterId;
	private Integer id;
	
	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	private String ZABIT_SERI;
	private int ZABIT_NO;
	private int OKUYUCU2_KODU;
	private int ZABIT_TIPI = 0;
	private int OKUYUCU_KODU;
	private String RESULT_TYPE;
	private int RESULT_CODE;
	private String RESULT;
	
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public int getSAHA_ISEMRI_NO() {
		return SAHA_ISEMRI_NO;
	}

	public void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO) {
		com.mobit.utility.check(sAHA_ISEMRI_NO, field.s_SAHA_ISEMRI_NO);
		SAHA_ISEMRI_NO = sAHA_ISEMRI_NO;
	}

	public String getZABIT_SERI() {
		return ZABIT_SERI;
	}

	public void setZABIT_SERI(String zABIT_SERI) {
		com.mobit.utility.check(zABIT_SERI, field.s_ZABIT_SERI);
		ZABIT_SERI = zABIT_SERI;
	}

	public int getZABIT_NO() {
		return ZABIT_NO;
	}

	public void setZABIT_NO(int zABIT_NO) {
		com.mobit.utility.check(zABIT_NO, field.s_ZABIT_NO);
		ZABIT_NO = zABIT_NO;
	}

	public int getOKUYUCU2_KODU() {
		return OKUYUCU2_KODU;
	}

	public void setOKUYUCU2_KODU(int oKUYUCU2_KODU) {
		com.mobit.utility.check(oKUYUCU2_KODU, field.s_OKUYUCU_KODU);
		OKUYUCU2_KODU = oKUYUCU2_KODU;
	}
	
	public int getOKUYUCU_KODU() {
		return OKUYUCU_KODU;
	}

	public void setOKUYUCU_KODU(int oKUYUCU_KODU) {
		com.mobit.utility.check(oKUYUCU_KODU, field.s_OKUYUCU_KODU);
		OKUYUCU_KODU = oKUYUCU_KODU;
	}
	

	public int getZABIT_TIPI() {
		return ZABIT_TIPI;
	}

	@Override
	public void setZABIT_TIPI(int zABIT_TIPI) {
		ZABIT_TIPI = zABIT_TIPI;
	}
	
	@Override
	public String getRESULT_TYPE() {
		return RESULT_TYPE;
	}

	@Override
	public void setRESULT_TYPE(String RESULT_TYPE) {
		this.RESULT_TYPE = RESULT_TYPE;
	}

	@Override
	public int getRESULT_CODE() {
		return RESULT_CODE;
	}

	@Override
	public void setRESULT_CODE(int RESULT_CODE) {
		this.RESULT_CODE = RESULT_CODE;
	}

	@Override
	public String getRESULT() {
		return RESULT;
	}

	@Override
	public void setRESULT(String RESULT) {
		this.RESULT = RESULT;
	}

	
	public put_isemri_zabit()
	{
		
	}

	public put_isemri_zabit(int tESISAT_NO, int sAHA_ISEMRI_NO, String zABIT_SERI, int zABIT_NO, int oKUYUCU2_KODU, int zABIT_TIPI, int oKUYUCU_KODU) {
		this();
		setTESISAT_NO(tESISAT_NO);
		setSAHA_ISEMRI_NO(sAHA_ISEMRI_NO);
		setZABIT_SERI(zABIT_SERI);
		setZABIT_NO(zABIT_NO);
		setOKUYUCU2_KODU(oKUYUCU2_KODU);
		setZABIT_TIPI(zABIT_TIPI);
		setOKUYUCU_KODU(oKUYUCU_KODU);
	}

	public put_isemri_zabit(IIsemri isemri) {
		// TODO Auto-generated constructor stub
		this();
		setTESISAT_NO(isemri.getTESISAT_NO());
		setSAHA_ISEMRI_NO(isemri.getSAHA_ISEMRI_NO());
	}
	public put_isemri_zabit(IIsemriIslem islem) {
		// TODO Auto-generated constructor stub
		this();
		setTESISAT_NO(islem.getTESISAT_NO());
		setSAHA_ISEMRI_NO(islem.getSAHA_ISEMRI_NO());
	}

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT ISEMRI_ZABIT %0"+
			field.s_TESISAT_NO+"d%0"+field.s_SAHA_ISEMRI_NO+"d%s%0"+
			field.s_ZABIT_NO+"d%0"+field.s_OKUYUCU_KODU+"d\n";
	
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, SAHA_ISEMRI_NO, ZABIT_SERI, ZABIT_NO, OKUYUCU2_KODU));
		
	}
	
	//-------------------------------------------------------------------------
	
	public static final String tableName = "zabit";
	public static final FieldInfo [] info = new FieldInfo[] {
			
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),	
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.TESISAT_NO, DbType.VARCHAR, field.s_TESISAT_NO, true),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.VARCHAR, field.s_SAHA_ISEMRI_NO, true),
			new FieldInfo(field.ZABIT_SERI, DbType.VARCHAR, field.s_ZABIT_SERI, true),
			new FieldInfo(field.ZABIT_NO, DbType.VARCHAR, field.s_ZABIT_NO, true),
			new FieldInfo(field.OKUYUCU2_KODU, DbType.VARCHAR, field.s_OKUYUCU_KODU, true),
			new FieldInfo(field.ZABIT_TIPI, DbType.INTEGER, 0, false),
			new FieldInfo(field.OKUYUCU_KODU, DbType.VARCHAR, field.s_OKUYUCU_KODU, true),
			
			new FieldInfo(field.RESULT_TYPE, DbType.VARCHAR, field.s_RESULT_TYPE, false),
			new FieldInfo(field.RESULT_CODE, DbType.INTEGER, 0, false),
			new FieldInfo(field.RESULT, DbType.VARCHAR, field.s_RESULT, false),
	};
	
	public static final IndexInfo[] indices = new IndexInfo[] { new IndexInfo(tableName+"_islem_id", true, field.ISLEM_ID) };

	public static final String insertString = DbHelper.PrepareInsertString(put_isemri_zabit.class);
	public static final String updateString = DbHelper.PrepareUpdateString(put_isemri_zabit.class, field.ID);
	
	public static final int TABLOID = 11;
	

	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		this.masterId = masterId;
	}

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
	public void get(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		
		id = rs.getInt(field.ID);
		setMasterId(rs.getInt(field.ISLEM_ID));
		
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSAHA_ISEMRI_NO(rs.getInt(field.SAHA_ISEMRI_NO));
		setZABIT_SERI(rs.getString(field.ZABIT_SERI));
		setZABIT_NO(rs.getInt(field.ZABIT_NO));
		setOKUYUCU2_KODU(rs.getInt(field.OKUYUCU2_KODU));
		setZABIT_TIPI(rs.getInt(field.ZABIT_TIPI));
		setOKUYUCU_KODU(rs.getInt(field.OKUYUCU_KODU));
		
		
	}
	
	@Override
	public int put(DbHelper h) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {

			stmt = h.getInsStatment(this);

			stmt.setObject(1, getId());
			stmt.setObject(2, masterId);
			
			stmt.setInt(3, getTESISAT_NO());
			stmt.setInt(4, getSAHA_ISEMRI_NO());
			stmt.setString(5, getZABIT_SERI());
			stmt.setInt(6, getZABIT_NO());
			stmt.setInt(7, getOKUYUCU2_KODU());
			stmt.setInt(8, getZABIT_TIPI());
			stmt.setInt(9, getOKUYUCU_KODU());
			
			cnt = stmt.executeUpdate();
			if(cnt > 0) id = h.GetId();
		}
		else {

			stmt = h.getUpdStatment(this);

			stmt.setObject(1, getId());
			stmt.setObject(2, masterId);
			
			stmt.setInt(3, getTESISAT_NO());
			stmt.setInt(4, getSAHA_ISEMRI_NO());
			stmt.setString(5, getZABIT_SERI());
			stmt.setInt(6, getZABIT_NO());
			stmt.setInt(7, getOKUYUCU2_KODU());
			stmt.setInt(8, getZABIT_TIPI());
			stmt.setInt(9, getOKUYUCU_KODU());
			
			//where
			stmt.setObject(10, getId());
			
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}
	
	//-------------------------------------------------------------------------

	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<IIsemriZabit> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriZabit> list = new ArrayList<IIsemriZabit>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_isemri_zabit ts = new put_isemri_zabit();
				ts.get(rs);
				list.add(ts);
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
