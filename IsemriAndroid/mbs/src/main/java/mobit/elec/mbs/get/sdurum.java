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
import mobit.elec.ISdurum;
import mobit.elec.ISerialize;
import mobit.elec.mbs.utility;

public class sdurum implements IDb, ISdurum, IDeserialize, ISerialize {

	private Integer id = null;

	private int SAYAC_DURUM_KODU; /* NUM 99 */
	private String SAYAC_KODU_ACIKLAMA; /* CHAR(40) */
	private int ISLEM_KODU; /* NUM 9 */
	private int ACIKLAMA_DUR; /* NUM 9 */
	
	private int KOD_TIPI = 0; /* NUM 9 */

	private boolean empty = true;

	public sdurum() {
		// TODO Auto-generated constructor stub
		this.SAYAC_DURUM_KODU = 0;
	}

	public sdurum(int SAYAC_DURUM_KODU) {
		// TODO Auto-generated constructor stub
		setSAYAC_DURUM_KODU(SAYAC_DURUM_KODU);
	}

	public sdurum(ResultSet rs, String columnLabel) throws SQLException {
		// TODO Auto-generated constructor stub
		int durum = rs.getInt(columnLabel);
		if (!rs.wasNull() && durum != 0)
			setSAYAC_DURUM_KODU(durum);
	}

	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub
		String[] list = utility.parser(row, info);

		if (list.length != 4)
			throw new ParseException(row, list.length);

		setSAYAC_DURUM_KODU(utility.MBS_FORMAT.parse(list[0]).intValue());
		setSAYAC_KODU_ACIKLAMA(list[1]);
		setISLEM_KODU(utility.MBS_FORMAT.parse(list[2]).intValue());
		setACIKLAMA_DUR(utility.MBS_FORMAT.parse(list[3]).intValue());

		return 0;
	}

	public int getValue() {
		return SAYAC_DURUM_KODU;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof sdurum)) return false;
		return (SAYAC_DURUM_KODU == ((sdurum)obj).SAYAC_DURUM_KODU);
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "sdurum";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),

			new FieldInfo(field.SAYAC_DURUM_KODU, DbType.INTEGER, field.s_SAYAC_DURUM_KODU, true),
			new FieldInfo(field.SAYAC_KODU_ACIKLAMA, DbType.VARCHAR, field.s_SAYAC_KODU_ACIKLAMA, true),
			new FieldInfo(field.ISLEM_KODU, DbType.INTEGER, field.s_ISLEM_KODU, true),
			new FieldInfo(field.ACIKLAMA_DUR, DbType.INTEGER, field.s_ACIKLAMA_DUR, true),
			new FieldInfo(field.KOD_TIPI, DbType.INTEGER, 0, false)};

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo("sdurum_sayac_durum_kodu", true, field.SAYAC_DURUM_KODU, field.KOD_TIPI) 
	};
	public static final String insertString = DbHelper.PrepareInsertString(sdurum.class);
	public static final String updateString = DbHelper.PrepareUpdateString(sdurum.class, field.SAYAC_DURUM_KODU, field.KOD_TIPI);

	public static void update_v1(Connection conn) throws Exception
	{
		Statement stmt = null;
		List<String> list;
		try {
			
			stmt = conn.createStatement();
			
			if(!DbHelper.isColumnExists(conn, tableName, field.KOD_TIPI))
				stmt.execute(String.format("ALTER TABLE %s ADD COLUMN %s INTEGER", tableName, field.KOD_TIPI));
			list = DbHelper.DropIndices(sdurum.class);
			for(String cmd : list) stmt.execute(cmd);
			list = DbHelper.CreateIndices(sdurum.class);
			for(String cmd : list) stmt.execute(cmd);
			

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	// -------------------------------------------------------------------------

	@Override
	public String toString() {
		
		if(SAYAC_KODU_ACIKLAMA != null) return String.format("%d-%s", SAYAC_DURUM_KODU, SAYAC_KODU_ACIKLAMA);
		else if(SAYAC_DURUM_KODU > 0) return String.format("%d", SAYAC_DURUM_KODU);
		return "";
		
	}

	private static final String format = "%0" + field.s_SAYAC_DURUM_KODU + "d";
	private static final String formats = "%" + field.s_SAYAC_DURUM_KODU + "s";

	@Override
	public void toSerialize(StringBuilder sb) {
		if (!isEmpty())
			sb.append(String.format(format, getValue()));
		else
			sb.append(String.format(formats, ""));
	}

	@Override
	public boolean isEmpty() {
		return empty;
	}
	
	private static final int TABLOID = 8;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}

	@Override
	public int getSAYAC_DURUM_KODU() throws Exception {
		if (isEmpty())
			throw new Exception("Durum tanımlı değil");
		return SAYAC_DURUM_KODU;
	}

	@Override
	public String getSAYAC_KODU_ACIKLAMA() {
		return SAYAC_KODU_ACIKLAMA;
	}

	@Override
	public int getISLEM_KODU() {
		return ISLEM_KODU;
	}

	@Override
	public int getACIKLAMA_DUR() {
		return ACIKLAMA_DUR;
	}
	
	@Override
	public int getKOD_TIPI() {
		return KOD_TIPI;
	}

	@Override
	public void setSAYAC_DURUM_KODU(int sAYAC_DURUM_KODU) {
		com.mobit.utility.check(sAYAC_DURUM_KODU, field.s_SAYAC_DURUM_KODU);
		SAYAC_DURUM_KODU = sAYAC_DURUM_KODU;
		empty = false;
	}

	@Override
	public void setSAYAC_KODU_ACIKLAMA(String sAYAC_KODU_ACIKLAMA) {
		com.mobit.utility.check(sAYAC_KODU_ACIKLAMA, field.s_SAYAC_KODU_ACIKLAMA);
		SAYAC_KODU_ACIKLAMA = sAYAC_KODU_ACIKLAMA;
	}

	@Override
	public void setISLEM_KODU(int iSLEM_KODU) {
		com.mobit.utility.check(iSLEM_KODU, field.s_ISLEM_KODU);
		ISLEM_KODU = iSLEM_KODU;
	}
	
	@Override
	public void setKOD_TIPI(int KOD_TIPI) {
		
		this.KOD_TIPI = KOD_TIPI;
	}

	boolean checked = false;
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
	public void setACIKLAMA_DUR(int aCIKLAMA_DUR) {
		com.mobit.utility.check(aCIKLAMA_DUR, field.s_ACIKLAMA_DUR);
		ACIKLAMA_DUR = aCIKLAMA_DUR;
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

		setSAYAC_DURUM_KODU(rs.getInt(field.SAYAC_DURUM_KODU));
		setSAYAC_KODU_ACIKLAMA(rs.getString(field.SAYAC_KODU_ACIKLAMA));
		setISLEM_KODU(rs.getInt(field.ISLEM_KODU));
		setACIKLAMA_DUR(rs.getInt(field.ACIKLAMA_DUR));
		setKOD_TIPI(rs.getInt(field.KOD_TIPI));

	}

	@Override
	public int put(DbHelper h) throws Exception {
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {
			
			stmt = h.getInsStatment(this);

			stmt.setInt(1, getId());
			stmt.setInt(2, getSAYAC_DURUM_KODU());
			stmt.setString(3, getSAYAC_KODU_ACIKLAMA());
			stmt.setInt(4, getISLEM_KODU());
			stmt.setInt(5, getACIKLAMA_DUR());
			stmt.setInt(6, getKOD_TIPI());

			cnt = stmt.executeUpdate();
		}
		if (cnt > 0) {
			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setInt(1, getSAYAC_DURUM_KODU());
			stmt.setString(2, getSAYAC_KODU_ACIKLAMA());
			stmt.setInt(3, getISLEM_KODU());
			stmt.setInt(4, getACIKLAMA_DUR());
			stmt.setInt(5, getKOD_TIPI());
			// where
			stmt.setInt(6, getSAYAC_DURUM_KODU());
			stmt.setInt(7, getKOD_TIPI());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}
	
	public static ISdurum fromInteger(int GIDEN_DURUM_KODU)
	{
		return new sdurum(GIDEN_DURUM_KODU);
	}

	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}

}
