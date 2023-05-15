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
import com.mobit.Globals;
import com.mobit.IDb;
import com.mobit.IEnum;
import com.mobit.IndexInfo;
import mobit.elec.IMuhurKod;
import mobit.elec.ISerialize;
import mobit.elec.ITesisatMuhur;
import mobit.elec.enums.IMuhurKodCins;
import mobit.elec.mbs.put.put_tesisat_muhur;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.MuhurKodCins;

public class muhur_kod implements IDb, IMuhurKod, IDeserialize, ISerialize {

	private Integer id = null;

	private IMuhurKodCins KOD_CINSI; /*
								 * CHAR(1) "N" - neden, "Y" - yer, "D" - durum,
								 * "I" - iptal
								 */
	private int MUHUR_KODU; /* NUM 99 */
	private String MUHUR_ACIKLAMA; /* CHAR(40) */
	
	private boolean checked = false;

	public muhur_kod()
	{
		
	}
	public muhur_kod(int MUHUR_KODU, IMuhurKodCins KOD_CINSI)
	{
		this.MUHUR_KODU = MUHUR_KODU;
		this.KOD_CINSI = KOD_CINSI;
	}
	
	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub
		String[] list = utility.parser(row, info);

		if (list.length != 3)
			throw new ParseException(row, list.length);

		setKOD_CINSI(MuhurKodCins.fromChar(list[0].charAt(0)));
		setMUHUR_KODU(utility.MBS_FORMAT.parse(list[1]).intValue());
		setMUHUR_ACIKLAMA(list[2]);

		return 0;
	}
	
	private static final String format = "%0"+field.s_MUHUR_KODU+"d";
	
	@Override
	public void toSerialize(StringBuilder sb) throws Exception {
		// TODO Auto-generated method stub
		sb.append(String.format(format, MUHUR_KODU));
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "muhurkod";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),

			new FieldInfo(field.KOD_CINSI, DbType.VARCHAR, field.s_KOD_CINSI, true),
			new FieldInfo(field.MUHUR_KODU, DbType.INTEGER, field.s_MUHUR_KODU, true),
			new FieldInfo(field.MUHUR_ACIKLAMA, DbType.VARCHAR, field.s_MUHUR_ACIKLAMA, true) };

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_muhur_kodu", true, field.MUHUR_KODU + "," + field.KOD_CINSI), };

	public static final String insertString = DbHelper.PrepareInsertString(muhur_kod.class);
	public static final String updateString = DbHelper.PrepareUpdateString(muhur_kod.class, field.KOD_CINSI,
			field.MUHUR_KODU);

	// -------------------------------------------------------------------------

	@Override
	public String toString() {

		if(getMUHUR_ACIKLAMA() != null) return String.format("%s: %s", getKOD_CINSI(), getMUHUR_ACIKLAMA());
		return String.format("%s: %d", getKOD_CINSI(), getMUHUR_KODU());
	}
	
	private static final int TABLOID = 5;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}

	public IEnum getKOD_CINSI() {
		return KOD_CINSI;
	}

	public int getMUHUR_KODU() {
		return MUHUR_KODU;
	}

	public String getMUHUR_ACIKLAMA() {
		if(MUHUR_ACIKLAMA == null) load();
		return MUHUR_ACIKLAMA;
	}

	public void setKOD_CINSI(IEnum KOD_CINSI) {
		this.KOD_CINSI = (IMuhurKodCins)KOD_CINSI;
	}

	public void setMUHUR_KODU(int mUHUR_KODU) {
		com.mobit.utility.check(mUHUR_KODU, field.s_MUHUR_KODU);
		MUHUR_KODU = mUHUR_KODU;
	}

	public void setMUHUR_ACIKLAMA(String mUHUR_ACIKLAMA) {
		com.mobit.utility.check(mUHUR_ACIKLAMA, field.s_MUHUR_ACIKLAMA);
		MUHUR_ACIKLAMA = mUHUR_ACIKLAMA;
	}

	public void check(IEnum KOD_CINSI) {
		if (getKOD_CINSI() != KOD_CINSI)
			throw new IllegalArgumentException(getKOD_CINSI().toString());
	}
	
	
	public void setCheck(boolean checked)
	{
		this.checked = checked;
	}
	
	public boolean getCheck()
	{
		return checked;
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

		setKOD_CINSI(MuhurKodCins.fromString(rs.getString(field.KOD_CINSI)));
		setMUHUR_KODU(rs.getInt(field.MUHUR_KODU));
		setMUHUR_ACIKLAMA(rs.getString(field.MUHUR_ACIKLAMA));

	}

	@Override
	public int put(DbHelper h) throws Exception {
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;
		if (getId() == null) {
			stmt = h.getInsStatment(this);
			stmt.setObject(1, getId());
			stmt.setString(2, String.valueOf((char) getKOD_CINSI().getValue()));
			stmt.setInt(3, getMUHUR_KODU());
			stmt.setString(4, getMUHUR_ACIKLAMA());

			cnt = stmt.executeUpdate();
		}

		if (cnt > 0) {

			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setString(1, String.valueOf((char) getKOD_CINSI().getValue()));
			stmt.setInt(2, getMUHUR_KODU());
			stmt.setString(3, getMUHUR_ACIKLAMA());

			// where
			stmt.setString(4, String.valueOf((char) getKOD_CINSI().getValue()));
			stmt.setInt(5, getMUHUR_KODU());

			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}


	private static final String query1 = String.format("select * from %s where %s=? and %s=?",
			tableName, field.KOD_CINSI, field.MUHUR_KODU);

	private void load() {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			Connection conn = Globals.app.getConnection();
			stmt = conn.prepareStatement(query1);

			stmt.setString(1, String.valueOf((char)KOD_CINSI.getValue()));
			stmt.setInt(2, MUHUR_KODU);
			rs = stmt.executeQuery();
			if (rs.next()) {
				get(rs);
			}

		}
		catch (Exception e){
			return;
		}
		finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
			catch (Exception e){

			}
		}
	}

}
