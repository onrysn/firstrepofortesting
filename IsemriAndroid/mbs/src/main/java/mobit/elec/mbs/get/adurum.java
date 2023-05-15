package mobit.elec.mbs.get;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IDb;
import com.mobit.IndexInfo;
import mobit.elec.IAdurum;
import mobit.elec.ISerialize;
import mobit.elec.mbs.get.IDeserialize;
import mobit.elec.mbs.utility;

public class adurum implements IDb, IAdurum, IDeserialize, ISerialize {

	public static String abone_durum;
	private Integer id = null;

	public int ABONE_DURUM_KODU; /* NUM 99 */
	public String DURUM_KODU_ACIKLAMA; /* CHAR(40) */
	private int ISLEM_KODU; /* NUM 9. */
	private int ACIKLAMA_DUR; /* NUM 9 */

	private int KOD_TIPI; // İş emri
	
	private boolean empty = true;
	
	private boolean checked = false;

	public adurum() {
		
	}
	
	public adurum(int ABONE_DURUM_KODU) {
		setABONE_DURUM_KODU(ABONE_DURUM_KODU);
	}

	public adurum(ResultSet rs, String columnLabel) throws SQLException {
		int durum = rs.getInt(columnLabel);
		if (!rs.wasNull() && durum != 0)
			setABONE_DURUM_KODU(durum);
	}

	@Override
	public String toString() {
		
		if(DURUM_KODU_ACIKLAMA != null) return String.format("%d-%s", ABONE_DURUM_KODU, DURUM_KODU_ACIKLAMA);
		else if(ABONE_DURUM_KODU > 0) return String.format("%d", ABONE_DURUM_KODU);
		return "";
	}

	public int getValue() throws Exception {
		return getABONE_DURUM_KODU();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof adurum)) return false;
		return (ABONE_DURUM_KODU == ((adurum)obj).ABONE_DURUM_KODU);
	}
	
	private static final int TABLOID = 1;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}

	@Override
	public int deserialize(ICommand cmd, String row)  throws Exception {
		// TODO Auto-generated method stub

		String[] list = utility.parser(row, info);

		if (list.length > 5)
			throw new ParseException(row, list.length);

		setABONE_DURUM_KODU(utility.MBS_FORMAT.parse(list[0]).intValue());
		setDURUM_KODU_ACIKLAMA(list[1]);
		setISLEM_KODU((list[2].trim().length() > 0) ? utility.MBS_FORMAT.parse(list[2]).intValue() : 0);
		setACIKLAMA_DUR((list[3].trim().length() > 0) ? utility.MBS_FORMAT.parse(list[3]).intValue() : 0);

		return 0;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "adurum";

	public static final FieldInfo[] info = new FieldInfo[] { 
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.ABONE_DURUM_KODU, DbType.VARCHAR, field.s_ABONE_DURUM_KODU, true),
			new FieldInfo(field.DURUM_KODU_ACIKLAMA, DbType.VARCHAR, field.s_DURUM_KODU_ACIKLAMA, true),
			new FieldInfo(field.ISLEM_KODU, DbType.VARCHAR, field.s_ISLEM_KODU, true),
			new FieldInfo(field.ACIKLAMA_DUR, DbType.VARCHAR, field.s_ACIKLAMA_DUR, true),
			new FieldInfo(field.KOD_TIPI, DbType.INTEGER, 0, false)
	};
			
	
	 public static final IndexInfo [] indices = new IndexInfo[] { 
			 new IndexInfo(tableName+"_abone_durum_kodu", true, field.ABONE_DURUM_KODU, field.KOD_TIPI)
	};

	public final int i_ID = DbHelper.getFieldIndex(info, field.ID);
	public final int i_ABONE_DURUM_KODU = DbHelper.getFieldIndex(info, field.ABONE_DURUM_KODU);
	public final int i_DURUM_KODU_ACIKLAMA = DbHelper.getFieldIndex(info, field.DURUM_KODU_ACIKLAMA);
	public final int i_ISLEM_KODU = DbHelper.getFieldIndex(info, field.ISLEM_KODU);
	public final int i_ACIKLAMA_DUR = DbHelper.getFieldIndex(info, field.ACIKLAMA_DUR);
	public final int i_KOD_TIPI = DbHelper.getFieldIndex(info, field.KOD_TIPI);
	
	public static final String insertString = DbHelper.PrepareInsertString(adurum.class);
	public static final String updateString = DbHelper.PrepareUpdateString(adurum.class, field.ABONE_DURUM_KODU, field.KOD_TIPI);

	
	public static void update_v1(Connection conn) throws Exception
	{
		Statement stmt = null;
		List<String> list;
		try {
			
			stmt = conn.createStatement();
			
			if(!DbHelper.isColumnExists(conn, tableName, field.KOD_TIPI))
				stmt.execute(String.format("ALTER TABLE %s ADD COLUMN %s INTEGER", tableName, field.KOD_TIPI));
			
			list = DbHelper.DropIndices(adurum.class);
			for(String cmd : list) stmt.execute(cmd);
			list = DbHelper.CreateIndices(adurum.class);
			for(String cmd : list) stmt.execute(cmd);
			

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	
	// -------------------------------------------------------------------------

	private static final String format = "%0" + field.s_ABONE_DURUM_KODU + "d";
	private static final String formats = "%" + field.s_ABONE_DURUM_KODU + "s";

	@Override
	public void toSerialize(StringBuilder sb) throws Exception {
		if (!isEmpty())
			sb.append(String.format(format, getValue()));
		else
			sb.append(String.format(formats, ""));
	}

	@Override
	public boolean isEmpty() {
		return empty;
	}
	@Override
	public int getABONE_DURUM_KODU() throws Exception {
		if (isEmpty())
			throw new Exception("Durum tanımlı değil");
		return ABONE_DURUM_KODU;
	}
	@Override
	public void setABONE_DURUM_KODU(int ABONE_DURUM_KODU) {
		com.mobit.utility.check(ABONE_DURUM_KODU, field.s_ABONE_DURUM_KODU);
		this.ABONE_DURUM_KODU = ABONE_DURUM_KODU;
		empty = false;
	}
	@Override
	public String getDURUM_KODU_ACIKLAMA() {
		return DURUM_KODU_ACIKLAMA;
	}
	@Override
	public void setDURUM_KODU_ACIKLAMA(String DURUM_KODU_ACIKLAMA) {

		com.mobit.utility.check(DURUM_KODU_ACIKLAMA, field.s_DURUM_KODU_ACIKLAMA);
		this.DURUM_KODU_ACIKLAMA = DURUM_KODU_ACIKLAMA;
	}
	@Override
	public int getISLEM_KODU() {
		return ISLEM_KODU;
	}

	@Override
	public void setISLEM_KODU(int ISLEM_KODU) {
		com.mobit.utility.check(ISLEM_KODU, field.s_ISLEM_KODU);
		this.ISLEM_KODU = ISLEM_KODU;
	}

	@Override
	public int getACIKLAMA_DUR() {
		return ACIKLAMA_DUR;
	}

	@Override
	public void setACIKLAMA_DUR(int ACIKLAMA_DUR) {
		com.mobit.utility.check(ACIKLAMA_DUR, field.s_ACIKLAMA_DUR);
		this.ACIKLAMA_DUR = ACIKLAMA_DUR;
	}
	@Override
	public int getKOD_TIPI() {
		return KOD_TIPI;
	}
	@Override
	public void setKOD_TIPI(int KOD_TIPI) {
		this.KOD_TIPI = KOD_TIPI;
	}
	
	@Override
	public boolean getCheck()
	{
		return checked;
	}
	@Override
	public void setCheck(boolean checked)
	{
		this.checked = checked;
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
		
		id = rs.getInt(i_ID);
		setABONE_DURUM_KODU(rs.getInt(i_ABONE_DURUM_KODU));
		setDURUM_KODU_ACIKLAMA(rs.getString(i_DURUM_KODU_ACIKLAMA));
		setISLEM_KODU(rs.getInt(i_ISLEM_KODU));
		setACIKLAMA_DUR(rs.getInt(i_ACIKLAMA_DUR));
		setKOD_TIPI(rs.getInt(i_KOD_TIPI));

	}

	@Override
	public int put(DbHelper h) throws Exception { // PreparedStatement getid,
													// PreparedStatement upd
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {
			
			stmt = h.getInsStatment(this);

			stmt.setObject(i_ID, getId());
			stmt.setInt(i_ABONE_DURUM_KODU, getABONE_DURUM_KODU());
			stmt.setString(i_DURUM_KODU_ACIKLAMA, getDURUM_KODU_ACIKLAMA());
			stmt.setInt(i_ISLEM_KODU, getISLEM_KODU());
			stmt.setInt(i_ACIKLAMA_DUR, getACIKLAMA_DUR());
			stmt.setInt(i_KOD_TIPI, getKOD_TIPI());

			cnt = stmt.executeUpdate();
		}

		if (cnt > 0) {

			id = h.GetId();

		} else {
			stmt = h.getUpdStatment(this);
			
			stmt.setInt(1, getABONE_DURUM_KODU());
			stmt.setString(2, getDURUM_KODU_ACIKLAMA());
			stmt.setInt(3, getISLEM_KODU());
			stmt.setInt(4, getACIKLAMA_DUR());
			stmt.setInt(5, getKOD_TIPI());
			// where
			stmt.setInt(6, getABONE_DURUM_KODU());
			stmt.setInt(7, getKOD_TIPI());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}

	

}
