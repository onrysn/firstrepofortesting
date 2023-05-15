package mobit.elec.mbs.get;

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
import com.mobit.IChecked;
import com.mobit.IDb;
import com.mobit.IndexInfo;

import mobit.eemr.Lun_Control;
import mobit.eemr.SeletIsemriCevap;
import mobit.elec.IIsemriSoru;
import mobit.elec.enums.ICevapTipi;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.CevapTipi;
import mobit.elec.mbs.server.get_isemri_soru;

public class isemri_soru extends ICommand implements IDb, IIsemriSoru, IDeserialize, IChecked {
	private Integer id;
	private Integer masterId;

	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	private int SORU_NO; /* NUM 99. */
	private String SORU_ACIKLAMA; /* CHAR(256). */
	private ICevapTipi CEVAP_TIPI; /* NUM 9. */
	private String CEVAP_FORMAT; /* CHAR(140). */

	private String CEVAP = "";/* CHAR(70). */
	//private String CEVAP;

	private String RESULT_TYPE;
	private int RESULT_CODE;
	private String RESULT;

	private boolean checked = false;
	private boolean active = false;

	public isemri_soru() {

	}

	/*
	public isemri_soru(int TESISAT_NO, int SAHA_ISEMRI_NO, int SORU_NO, String SORU_ACIKLAMA, ICevapTipi CEVAP_TIPI,
			String CEVAP_FORMAT, String CEVAP) {
		setTESISAT_NO(TESISAT_NO);
		setSAHA_ISEMRI_NO(SAHA_ISEMRI_NO);
		setSORU_NO(SORU_NO);
		setSORU_ACIKLAMA(SORU_ACIKLAMA);
		setCEVAP_TIPI(CEVAP_TIPI);
		setCEVAP_FORMAT(CEVAP_FORMAT);
		setCEVAP(CEVAP);
	}*/
	
	public isemri_soru(IIsemriSoru soru) {
		//setTESISAT_NO(soru.getTESISAT_NO);
		//setSAHA_ISEMRI_NO(soru.getSAHA_ISEMRI_NO());
		setSORU_NO(soru.getSORU_NO());
		setSORU_ACIKLAMA(soru.getSORU_ACIKLAMA());
		setCEVAP_TIPI(soru.getCEVAP_TIPI());
		setCEVAP_FORMAT(soru.getCEVAP_FORMAT());
		setCEVAP(soru.getCEVAP());
	}

	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub

		String[] list = utility.parser(row, info);

		if (list.length > 4)
			throw new ParseException(row, list.length);

		get_isemri_soru gis = (get_isemri_soru) cmd;
		setTESISAT_NO(gis.getTESISAT_NO());
		setSAHA_ISEMRI_NO(gis.getSAHA_ISEMRI_NO());

		setSORU_NO(utility.MBS_FORMAT.parse(list[0]).intValue());
		setSORU_ACIKLAMA(list[1]);
		setCEVAP_TIPI(CevapTipi.fromInteger(utility.MBS_FORMAT.parse(list[2]).intValue()));
		setCEVAP_FORMAT(list[3]);

		return 0;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "soru";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, false),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.INTEGER, field.s_SAHA_ISEMRI_NO, false),
			
			new FieldInfo(field.SORU_NO, DbType.INTEGER, field.s_SORU_NO, true),
			new FieldInfo(field.SORU_ACIKLAMA, DbType.VARCHAR, field.s_SORU_ACIKLAMA, true),
			new FieldInfo(field.CEVAP_TIPI, DbType.INTEGER, field.s_CEVAP_TIPI, true),
			new FieldInfo(field.CEVAP_FORMAT, DbType.VARCHAR, field.s_CEVAP_FORMAT, true),
			
			new FieldInfo(field.CEVAP, DbType.VARCHAR, field.s_CEVAP, false),
			new FieldInfo(field.YAPILDI, DbType.BOOLEAN, field.s_YAPILDI, false),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),
	
			new FieldInfo(field.RESULT_TYPE, DbType.VARCHAR, field.s_RESULT_TYPE, false),
			new FieldInfo(field.RESULT_CODE, DbType.INTEGER, 0, false),
			new FieldInfo(field.RESULT, DbType.VARCHAR, field.s_RESULT, false),
	};

	public static final IndexInfo[] indices = new IndexInfo[] { new IndexInfo(tableName+"_islem_id", false, field.ISLEM_ID)};

	public static final String insertString = DbHelper.PrepareInsertString(isemri_soru.class);
	public static final String updateString = DbHelper.PrepareUpdateString(isemri_soru.class, field.ID);

	// -------------------------------------------------------------------------

	@Override
	public String toString() {
		if(getCEVAP() == null || getCEVAP().isEmpty()) return getSORU_ACIKLAMA();
		String cevap = getCEVAP();
		if(getCEVAP_TIPI().equals(CevapTipi.Boolean)){
			cevap = (cevap.equals("1")) ? "Evet" : "Hayır";
		}
		return String.format("%s", getSORU_ACIKLAMA());
	}
	
	public static final int TABLOID = 4;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}

	@Override
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	@Override
	public int getSAHA_ISEMRI_NO() {
		return SAHA_ISEMRI_NO;
	}

	@Override
	public int getSORU_NO() {
		return SORU_NO;
	}

	@Override
	public String getSORU_ACIKLAMA() {
		return SORU_ACIKLAMA;
	}

	@Override
	public ICevapTipi getCEVAP_TIPI() {
		return CEVAP_TIPI;
	}

	@Override
	public String getCEVAP_FORMAT() {
		return CEVAP_FORMAT;
	}

	@Override
	public String getCEVAP() {
		return CEVAP;
	}
	
	public static String[] parseCEVAP_FORMAT(String CEVAP_FORMAT){
		String fmt = CEVAP_FORMAT;
		if(fmt.indexOf(',') > -1) return fmt.split(",");
		else if(fmt.indexOf(';') > -1) return fmt.split(";");
		return new String[] {fmt};
	}

	public String[] getCEVAP_FORMAT_LIST() {
		
		return parseCEVAP_FORMAT(getCEVAP_FORMAT());
	}

	@Override
	public boolean getCheck() {
		// TODO Auto-generated method stub
		return checked;
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}

	@Override
	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	@Override
	public void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO) {
		com.mobit.utility.check(sAHA_ISEMRI_NO, field.s_SAHA_ISEMRI_NO);
		SAHA_ISEMRI_NO = sAHA_ISEMRI_NO;
	}

	@Override
	public void setSORU_NO(int sORU_NO) {
		com.mobit.utility.check(sORU_NO, field.s_SORU_NO);
		SORU_NO = sORU_NO;
	}

	@Override
	public void setSORU_ACIKLAMA(String sORU_ACIKLAMA) {
		//mobit.utility.check(sORU_ACIKLAMA, field.s_SORU_ACIKLAMA);
		SORU_ACIKLAMA = sORU_ACIKLAMA;
	}

	@Override
	public void setCEVAP_TIPI(ICevapTipi cEVAP_TIPI) {
		CEVAP_TIPI = cEVAP_TIPI;
	}

	@Override
	public void setCEVAP_FORMAT(String cEVAP_FORMAT) {
		//mobit.utility.check(cEVAP_FORMAT, field.s_CEVAP_FORMAT);
		CEVAP_FORMAT = cEVAP_FORMAT;
	}

	@Override
	public void setCEVAP(String cEVAP) {
		com.mobit.utility.check(cEVAP, field.s_CEVAP);
		CEVAP = cEVAP;
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


	@Override
	public void setCheck(boolean checked) {
		// TODO Auto-generated method stub
		this.checked = checked;
	}
	
	@Override
	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
	}

	@Override
	public void get(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		id = rs.getInt(field.ID);

		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSAHA_ISEMRI_NO(rs.getInt(field.SAHA_ISEMRI_NO));
		setSORU_NO(rs.getInt(field.SORU_NO));
		setSORU_ACIKLAMA(rs.getString(field.SORU_ACIKLAMA));
		setCEVAP_TIPI(CevapTipi.fromInteger(rs.getInt(field.CEVAP_TIPI)));
		setCEVAP_FORMAT(rs.getString(field.CEVAP_FORMAT));
		//yeni ekldim 21072020 kuyruk mevzusu tmm
		setCEVAP(rs.getString(field.CEVAP));

		setCheck(rs.getBoolean(field.YAPILDI));
		setMasterId(rs.getInt(field.ISLEM_ID));
		
		setRESULT_TYPE(rs.getString(field.RESULT_TYPE));
		setRESULT_CODE(rs.getInt(field.RESULT_CODE));
		setRESULT(rs.getString(field.RESULT));

	}
	
	@Override
	public int put(DbHelper h) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {

			stmt = h.getInsStatment(this);

			stmt.setObject(1, getId());
			stmt.setInt(2, getTESISAT_NO());
			stmt.setInt(3, getSAHA_ISEMRI_NO());
			stmt.setInt(4, getSORU_NO());
			stmt.setString(5, getSORU_ACIKLAMA());
			stmt.setInt(6, getCEVAP_TIPI().getValue());
			stmt.setString(7, getCEVAP_FORMAT());
			stmt.setString(8, getCEVAP());
			stmt.setBoolean(9, getCheck());
			stmt.setObject(10, masterId);
			
			cnt = stmt.executeUpdate();
			if (cnt > 0) id = h.GetId();

		}
		else {

			stmt = h.getUpdStatment(this);

			stmt.setObject(1, getId());
			stmt.setInt(2, getTESISAT_NO());
			stmt.setInt(3, getSAHA_ISEMRI_NO());
			stmt.setInt(4, getSORU_NO());
			stmt.setString(5, getSORU_ACIKLAMA());
			stmt.setInt(6, getCEVAP_TIPI().getValue());
			stmt.setString(7, getCEVAP_FORMAT());
			stmt.setString(8, getCEVAP());
			stmt.setBoolean(9, getCheck());
			stmt.setObject(10, masterId);
			// where
			stmt.setObject(11, getId());
						
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT ISEMRI_CEVAP " + "%0" + field.s_TESISAT_NO + "d" + "%0"
			+ field.s_SAHA_ISEMRI_NO + "d" + "%0" + field.s_SORU_NO + "d" + "%-" + field.s_CEVAP + "s\n";

	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		//SeletIsemriCevap isemriCevap = new SeletIsemriCevap();
		//Select(isemriCevap.getdbConn(),1386981,19225825);
		String cEVAP = getCEVAP();
		if(CEVAP_TIPI.equals(CevapTipi.Float) && cEVAP.contains(","))
			cEVAP = cEVAP.replace(",", ".");
		b.append(String.format(format, getTESISAT_NO(), getSAHA_ISEMRI_NO(), getSORU_NO(), cEVAP));
	}

	public static List<IIsemriSoru> Select(Connection conn, int TESISAT_NO, int SAHA_ISEMRI_NO) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriSoru> list = null;

		try {
			stmt = conn.prepareStatement("select * from " + tableName + " where TESISAT_NO=? and SAHA_ISEMRI_NO=?");
			stmt.setInt(1, TESISAT_NO);
			stmt.setInt(2, SAHA_ISEMRI_NO);
			rs = stmt.executeQuery();
			list = DbHelper.Select(rs, isemri_soru.class);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	public static void Delete(Connection conn, int TESISAT_NO, int SAHA_ISEMRI_NO) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("delete from " + tableName + " where TESISAT_NO=? and SAHA_ISEMRI_NO=?");
			stmt.setInt(1, TESISAT_NO);
			stmt.setInt(2, SAHA_ISEMRI_NO);
			stmt.execute();
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}

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
	
	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<IIsemriSoru> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriSoru> list = new ArrayList<IIsemriSoru>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				isemri_soru is = new isemri_soru();
				is.get(rs);
				list.add(is);
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
