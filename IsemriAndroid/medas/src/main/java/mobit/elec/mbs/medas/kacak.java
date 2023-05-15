package mobit.elec.mbs.medas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IDb;
import com.mobit.IndexInfo;

import mobit.elec.IMuhurSokme;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.put.put_muhur_sokme;

public class kacak implements IDb, IKacak {

	private Integer id = null;

	private Integer masterId = null;

	private String ADRES_TARIF;
	private String KACAKCI_UNVAN;
	private String KACAKCI_TELEFON;
	private String KACAKCI_EMAIL;
	private String KACAK_TIPI;
	private String IHBAR_EDEN_UNVAN;
	private String IHBAR_EDEN_TELEFON;
	private String IHBAR_EDEN_EMAIL;
	private Integer TESISAT_NO;
	private Integer REFERANS_TESISAT_NO;
	private String DIREK_NO;
	private String BOX_NO;
	private Integer SAYAC_NO;
	private String ACIKLAMA;
	
	
	public String getADRES_TARIF()
	{
		return ADRES_TARIF;
	}
	public void setADRES_TARIF(String ADRES_TARIF)
	{
		this.ADRES_TARIF = ADRES_TARIF;
	}
	public String getKACAKCI_UNVAN()
	{
		return KACAKCI_UNVAN;
	}
	public void setKACAKCI_UNVAN(String KACAKCI_UNVAN)
	{
		this.KACAKCI_UNVAN = KACAKCI_UNVAN;
	}
	public String getKACAKCI_TELEFON()
	{
		return KACAKCI_TELEFON;
	}
	public void setKACAKCI_TELEFON(String KACAKCI_TELEFON)
	{
		this.KACAKCI_TELEFON = KACAKCI_TELEFON;
	}
	public String getKACAKCI_EMAIL()
	{
		return KACAKCI_EMAIL;
	}
	public void setKACAKCI_EMAIL(String KACAKCI_EMAIL)
	{
		this.KACAKCI_EMAIL = KACAKCI_EMAIL;
	}
	public String getKACAK_TIPI()
	{
		return KACAK_TIPI;
	}
	public void setKACAK_TIPI(String KACAK_TIPI)
	{
		this.KACAK_TIPI = KACAK_TIPI;
	}
	public String getIHBAR_EDEN_UNVAN()
	{
		return IHBAR_EDEN_UNVAN;
	}
	public void setIHBAR_EDEN_UNVAN(String IHBAR_EDEN_UNVAN)
	{
		this.IHBAR_EDEN_UNVAN = IHBAR_EDEN_UNVAN;
	}
	public String getIHBAR_EDEN_TELEFON()
	{
		return IHBAR_EDEN_TELEFON;
	}
	public void setIHBAR_EDEN_TELEFON(String IHBAR_EDEN_TELEFON)
	{
		this.IHBAR_EDEN_TELEFON = IHBAR_EDEN_TELEFON;
	}
	public String getIHBAR_EDEN_EMAIL()
	{
		return IHBAR_EDEN_EMAIL;
	}
	public void setIHBAR_EDEN_EMAIL(String IHBAR_EDEN_EMAIL)
	{
		this.IHBAR_EDEN_EMAIL = IHBAR_EDEN_EMAIL;
	}
	public Integer getTESISAT_NO()
	{
		return TESISAT_NO;
	}
	public void setTESISAT_NO(Integer TESISAT_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
	}
	public Integer getREFERANS_TESISAT_NO()
	{
		return REFERANS_TESISAT_NO;
	}
	public void setREFERANS_TESISAT_NO(Integer REFERANS_TESISAT_NO)
	{
		this.REFERANS_TESISAT_NO = REFERANS_TESISAT_NO;
	}
	public String getDIREK_NO()
	{
		return DIREK_NO;
	}
	public void setDIREK_NO(String DIREK_NO)
	{
		this.DIREK_NO = DIREK_NO;
	}
	public String getBOX_NO()
	{
		return BOX_NO;
	}
	public void setBOX_NO(String BOX_NO)
	{
		this.BOX_NO = BOX_NO;
	}
	public Integer getSAYAC_NO()
	{
		return SAYAC_NO;
	}
	public void setSAYAC_NO(Integer SAYAC_NO)
	{
		this.SAYAC_NO = SAYAC_NO;
	}
	public String getACIKLAMA()
	{
		return ACIKLAMA;
	}
	public void setACIKLAMA(String ACIKLAMA)
	{
		this.ACIKLAMA = ACIKLAMA;
	}

	public static final int TABLOID = 15;

	@Override
	public int getTabloId() {
		return TABLOID;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "kacak";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),

			new FieldInfo(IDef.ADRES_TARIF, DbType.VARCHAR, IDef.s_ADRES_TARIF, false),
			new FieldInfo(IDef.KACAKCI_UNVAN, DbType.VARCHAR, IDef.s_KACAKCI_UNVAN, false),
			new FieldInfo(IDef.KACAKCI_TELEFON, DbType.VARCHAR, IDef.s_KACAKCI_TELEFON, false),
			new FieldInfo(IDef.KACAKCI_EMAIL, DbType.VARCHAR, IDef.s_KACAKCI_EMAIL, false),
			new FieldInfo(IDef.KACAK_TIPI, DbType.VARCHAR, IDef.s_KACAK_TIPI, false),
			new FieldInfo(IDef.IHBAR_EDEN_UNVAN, DbType.VARCHAR, IDef.s_IHBAR_EDEN_UNVAN, false),
			new FieldInfo(IDef.IHBAR_EDEN_TELEFON, DbType.VARCHAR, IDef.s_IHBAR_EDEN_TELEFON, false),
			new FieldInfo(IDef.IHBAR_EDEN_EMAIL, DbType.VARCHAR, IDef.s_IHBAR_EDEN_EMAIL, false),
			new FieldInfo(IDef.TESISAT_NO, DbType.INTEGER, 0, false),
			new FieldInfo(IDef.REFERANS_TESISAT_NO, DbType.INTEGER, IDef.s_REFERANS_TESISAT_NO, false),
			new FieldInfo(IDef.DIREK_NO, DbType.VARCHAR, IDef.s_DIREK_NO, false),
			new FieldInfo(IDef.BOX_NO, DbType.VARCHAR, IDef.s_BOX_NO, false),
			new FieldInfo(IDef.SAYAC_NO, DbType.INTEGER, 0, false),
			new FieldInfo(IDef.ACIKLAMA, DbType.VARCHAR, IDef.s_ACIKLAMA, false),

	};

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName + "_islem_id", true, field.ISLEM_ID) };

	public static final String insertString = DbHelper.PrepareInsertString(kacak.class);
	public static final String updateString = DbHelper.PrepareUpdateString(kacak.class, field.ID);

	// -------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return String.format("Kaçak\nTesisat No: %s", TESISAT_NO);
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
	public void get(ResultSet rs) throws SQLException {

		id = rs.getInt(field.ID);
		masterId = rs.getInt(field.ISLEM_ID);
		
		setADRES_TARIF(rs.getString(IDef.ADRES_TARIF));
		setKACAKCI_UNVAN(rs.getString(IDef.KACAKCI_UNVAN));
		setKACAKCI_TELEFON(rs.getString(IDef.KACAKCI_TELEFON));
		setKACAKCI_EMAIL(rs.getString(IDef.KACAKCI_EMAIL));
		setKACAK_TIPI(rs.getString(IDef.KACAK_TIPI));
		setIHBAR_EDEN_UNVAN(rs.getString(IDef.IHBAR_EDEN_UNVAN));
		setIHBAR_EDEN_TELEFON(rs.getString(IDef.IHBAR_EDEN_TELEFON));
		setIHBAR_EDEN_EMAIL(rs.getString(IDef.IHBAR_EDEN_EMAIL));
		setTESISAT_NO((Integer)rs.getObject(IDef.TESISAT_NO));
		setREFERANS_TESISAT_NO((Integer)rs.getObject(IDef.REFERANS_TESISAT_NO));
		setDIREK_NO(rs.getString(IDef.DIREK_NO));
		setBOX_NO(rs.getString(IDef.BOX_NO));
		setSAYAC_NO((Integer)rs.getObject(IDef.SAYAC_NO));
		setACIKLAMA(rs.getString(IDef.ACIKLAMA));

	}

	@Override
	public int put(DbHelper h) throws Exception { // PreparedStatement getid,
													// PreparedStatement upd
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {

			stmt = h.getInsStatment(this);

			stmt.setObject(1, getId());
			stmt.setObject(2, masterId);
			stmt.setString(3, getADRES_TARIF());
			stmt.setString(4, getKACAKCI_UNVAN());
			stmt.setString(5, getKACAKCI_TELEFON());
			stmt.setString(6, getKACAKCI_EMAIL());
			stmt.setString(7, getKACAK_TIPI());
			stmt.setString(8, getIHBAR_EDEN_UNVAN());
			stmt.setString(9, getIHBAR_EDEN_TELEFON());
			stmt.setString(10, getIHBAR_EDEN_EMAIL());
			stmt.setObject(11, getTESISAT_NO());
			stmt.setObject(12, getREFERANS_TESISAT_NO());
			stmt.setString(13, getDIREK_NO());
			stmt.setString(14, getBOX_NO());
			stmt.setObject(15, getSAYAC_NO());
			stmt.setString(16, getACIKLAMA());

			cnt = stmt.executeUpdate();
		}

		if (cnt > 0) {

			id = h.GetId();

		} else {
			stmt = h.getUpdStatment(this);

			
			stmt.setObject(1, masterId);
			stmt.setString(2, getADRES_TARIF());
			stmt.setString(3, getKACAKCI_UNVAN());
			stmt.setString(4, getKACAKCI_TELEFON());
			stmt.setString(5, getKACAKCI_EMAIL());
			stmt.setString(6, getKACAK_TIPI());
			stmt.setString(7, getIHBAR_EDEN_UNVAN());
			stmt.setString(8, getIHBAR_EDEN_TELEFON());
			stmt.setString(9, getIHBAR_EDEN_EMAIL());
			stmt.setObject(10, getTESISAT_NO());
			stmt.setObject(11, getREFERANS_TESISAT_NO());
			stmt.setString(12, getDIREK_NO());
			stmt.setString(13, getBOX_NO());
			stmt.setObject(14, getSAYAC_NO());
			stmt.setString(15, getACIKLAMA());
			//where
			stmt.setObject(16, getId());
			
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<IKacak> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IKacak> list = new ArrayList<IKacak>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				kacak k = new kacak();
				k.get(rs);
				list.add(k);
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
