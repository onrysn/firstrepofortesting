package mobit.elec.mbs.get;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.Globals;
import com.mobit.IDb;
import com.mobit.IEnum;
import com.mobit.IndexInfo;
import mobit.elec.ISayacMarka;
import mobit.elec.ISerialize;
import mobit.elec.enums.SayacCinsi;
import mobit.elec.mbs.utility;

public class sayac_marka implements IDb, ISayacMarka, IDeserialize, ISerialize {

	private Integer id = null;

	private String SAYAC_MARKA_KODU; // CHAR(3)
	private String SAYAC_MARKA_ADI; // CHAR(20)
	private IEnum SAYAC_CINS_KODU; // NUM 9

	public sayac_marka() {
		this("");
	}

	protected sayac_marka(String SAYAC_MARKA_KODU) {
		this.SAYAC_MARKA_KODU = SAYAC_MARKA_KODU;
	}

	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub

		String[] list = utility.parser(row, info);


		if (SAYAC_MARKA_KODU == "STE"){
			SAYAC_MARKA_KODU = "STR";
		}

		if (list.length != 3)
			throw new ParseException(row, list.length);

		setSAYAC_MARKA_KODU(list[0]);
		setSAYAC_MARKA_ADI(list[1]);
		setSAYAC_CINS_KODU(SayacCinsi.fromChar(list[2].charAt(0)));

		return 0;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "sayacmarka";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),

			new FieldInfo(field.SAYAC_MARKA_KODU, DbType.VARCHAR, field.s_SAYAC_MARKA_KODU, true),
			new FieldInfo(field.SAYAC_MARKA_ADI, DbType.VARCHAR, field.s_SAYAC_MARKA_ADI, true),
			new FieldInfo(field.SAYAC_CINS_KODU, DbType.VARCHAR, field.s_SAYAC_CINS_KODU, true) };

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_sayac_marka_kodu", true, field.SAYAC_MARKA_KODU), };

	public static final String insertString = DbHelper.PrepareInsertString(sayac_marka.class);
	public static final String updateString = DbHelper.PrepareUpdateString(sayac_marka.class, field.SAYAC_MARKA_KODU);

	// -------------------------------------------------------------------------

	@Override
	public String toString() {

		String markaAdi = getSAYAC_MARKA_ADI();
		return (markaAdi != null && !markaAdi.isEmpty()) ? markaAdi : SAYAC_MARKA_KODU;
	}

	private static final String format = "%-" + field.s_SAYAC_MARKA_KODU + "s";

	public void toSerialize(StringBuilder b) {
		b.append(String.format(format, SAYAC_MARKA_KODU));
	}
	
	private static final int TABLOID = 6;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}

	public String getSAYAC_MARKA_KODU() {
		return SAYAC_MARKA_KODU;
	}

	public String getSAYAC_MARKA_ADI() {

		if(SAYAC_MARKA_ADI == null){
			ISayacMarka sm = select(SAYAC_MARKA_KODU);
			if(sm != null) setSAYAC_MARKA_ADI(sm.getSAYAC_MARKA_ADI());
		}
		return SAYAC_MARKA_ADI;
	}

	public IEnum getSAYAC_CINS_KODU() {
		return SAYAC_CINS_KODU;
	}

	public void setSAYAC_MARKA_KODU(String sAYAC_MARKA_KODU) {
		com.mobit.utility.check(sAYAC_MARKA_KODU, field.s_SAYAC_MARKA_KODU);
		SAYAC_MARKA_KODU = sAYAC_MARKA_KODU;
	}

	public void setSAYAC_MARKA_ADI(String sAYAC_MARKA_ADI) {
		com.mobit.utility.check(sAYAC_MARKA_ADI, field.s_SAYAC_MARKA_ADI);
		SAYAC_MARKA_ADI = sAYAC_MARKA_ADI;
	}

	public void setSAYAC_CINS_KODU(IEnum sAYAC_CINS_KODU) {
		SAYAC_CINS_KODU = sAYAC_CINS_KODU;
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

		setSAYAC_MARKA_KODU(rs.getString(field.SAYAC_MARKA_KODU));
		setSAYAC_MARKA_ADI(rs.getString(field.SAYAC_MARKA_ADI));
		setSAYAC_CINS_KODU(SayacCinsi.fromString(rs.getString(field.SAYAC_CINS_KODU)));

	}

	@Override
	public int put(DbHelper h) throws Exception {
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;
		if (getId() == null) {
			stmt = h.getInsStatment(this);
			stmt.setObject(1, getId());
			stmt.setString(2, getSAYAC_MARKA_KODU());
			stmt.setString(3, getSAYAC_MARKA_ADI());
			stmt.setString(4, String.valueOf((char) getSAYAC_CINS_KODU().getValue()));

			cnt = stmt.executeUpdate();
		}
		if (cnt > 0) {
			id = h.GetId();
		} else {
			stmt = h.getUpdStatment(this);
			
			stmt.setString(1, getSAYAC_MARKA_KODU());
			stmt.setString(2, getSAYAC_MARKA_ADI());
			stmt.setString(3, String.valueOf((char) getSAYAC_CINS_KODU().getValue()));
			// where
			stmt.setString(4, getSAYAC_MARKA_KODU());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

	public static ISayacMarka fromString(String SAYAC_MARKA_KODU)
	{
		return new sayac_marka(SAYAC_MARKA_KODU);
	}

	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}

	private static final String query1 = String.format("select * from %s where %s=?",
			tableName, field.SAYAC_MARKA_KODU);

	public static ISayacMarka select(String SAYAC_MARKA_KODU) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			Connection conn = Globals.app.getConnection();
			stmt = conn.prepareStatement(query1);

			stmt.setString(1, SAYAC_MARKA_KODU);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sayac_marka sm = new sayac_marka();
				sm.get(rs);
				return sm;
			}

		}
		catch (Exception e){
			return null;
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
		return null;
	}
}
