package mobit.elec.mbs.get;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IDb;
import com.mobit.IYetki;
import mobit.elec.mbs.server.get_eleman;
import mobit.elec.Yetki;
import mobit.elec.mbs.utility;

public class eleman implements IDb, IEleman, IDeserialize {

	private Integer id = null;

	private int ELEMAN_KODU;
	private String ELEMAN_ADI;
	private Date TARIH;
	private String FILLER;
	private String SAAT;
	private String RESET_DUR;
	private int ELEMAN_SIFRE;
	
	private String YETKI = "";

	public eleman(){
		
	}
	public eleman(int ELEMAN_KODU, String ELEMAN_ADI, Date TARIH, 
			String FILLER, String SAAT, String RESET_DUR, int ELEMAN_SIFRE)
	{
		setELEMAN_KODU(ELEMAN_KODU);
		setELEMAN_ADI(ELEMAN_ADI);
		setTARIH(TARIH);
		setFILLER(FILLER);
		setSAAT(SAAT);
		setRESET_DUR(RESET_DUR);
		setELEMAN_SIFRE(ELEMAN_SIFRE);
		
	}
	
	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {

		String[] list = utility.parser(row, info);

		if (list.length < 2)
			throw new ParseException(row, list.length);

		String tarih = null, saat = null;
		
		ELEMAN_KODU = utility.MBS_FORMAT.parse(list[0]).intValue();
		ELEMAN_ADI = list[1];
		if(list.length > 2){
			TARIH = field.date_formatter2.parse(list[2]);
			tarih = list[2];
		}
		if(list.length > 3) FILLER = list[3];
		if(list.length > 4) saat = list[4];
		if(list.length > 5) RESET_DUR = list[5];
		
		SAAT = saat;
		if(tarih != null && saat != null)
			TARIH = field.datetime_formatter.parse(tarih+saat);
		
		if (cmd instanceof get_eleman) {
			get_eleman ge = (get_eleman) cmd;
			ELEMAN_SIFRE = ge.ELEMAN_SIFRE;
		}

		return 0;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "eleman";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),

			new FieldInfo(field.ELEMAN_KODU, DbType.INTEGER, field.s_ELEMAN_KODU, true),
			new FieldInfo(field.ELEMAN_ADI, DbType.VARCHAR, field.s_ELEMAN_ADI, true),
			new FieldInfo(field.ELEMAN_TARIH, DbType.DATETIME, field.s_ELEMAN_TARIH, true),
			new FieldInfo(field.FILLER, DbType.VARCHAR, field.s_FILLER, true),
			new FieldInfo(field.SAAT, DbType.VARCHAR, field.s_SAAT, true),
			new FieldInfo(field.RESET_DUR, DbType.VARCHAR, field.s_RESET_DUR, true),
			new FieldInfo(field.ELEMAN_SIFRE, DbType.VARCHAR, field.s_ELEMAN_SIFRE, false) };
	/*
	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo("eleman_eleman_kodu", true, field.ELEMAN_KODU), };
	*/
	public static final String insertString = DbHelper.PrepareInsertString(eleman.class);
	public static final String updateString = DbHelper.PrepareUpdateString(eleman.class, field.ID);

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
		return getELEMAN_KODU() == 1111;
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
	public Date getTARIH() {
		return TARIH;
	}
	@Override
	public String getFILLER() {
		return FILLER;
	}

	@Override
	public String getSAAT() {
		return SAAT;
	}

	@Override
	public String getRESET_DUR() {
		return RESET_DUR;
	}

	@Override
	public int getELEMAN_SIFRE() {
		return ELEMAN_SIFRE;
	}
	
	@Override
	public void setELEMAN_KODU(int ELEMAN_KODU) {
		com.mobit.utility.check(ELEMAN_KODU, field.s_ELEMAN_KODU);
		this.ELEMAN_KODU = ELEMAN_KODU;
	}

	@Override
	public void setELEMAN_ADI(String ELEMAN_ADI) {
		com.mobit.utility.check(ELEMAN_ADI, field.s_ELEMAN_ADI);
		this.ELEMAN_ADI = ELEMAN_ADI;
	}

	@Override
	public void setTARIH(Date TARIH) {
		this.TARIH = TARIH;
	}

	@Override
	public void setFILLER(String FILLER) {
		com.mobit.utility.check(FILLER, field.s_FILLER);
		this.FILLER = FILLER;
	}

	@Override
	public void setSAAT(String SAAT) {
		com.mobit.utility.check(SAAT, field.s_SAAT);
		this.SAAT = SAAT;
	}

	@Override
	public void setRESET_DUR(String RESET_DUR) {
		com.mobit.utility.check(RESET_DUR, field.s_RESET_DUR);
		this.RESET_DUR = RESET_DUR;
	}
	@Override
	public void setELEMAN_SIFRE(int ELEMAN_SIFRE) {
		com.mobit.utility.check(ELEMAN_SIFRE, field.s_ELEMAN_SIFRE);
		this.ELEMAN_SIFRE = ELEMAN_SIFRE;
	}

	@Override
	public String getYETKI()
	{
		return YETKI;
	}
	@Override
	public void setYETKI(String YETKI)
	{
		this.YETKI = YETKI;
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

		id = rs.getInt(field.ID);

		setELEMAN_KODU(rs.getInt(field.ELEMAN_KODU));
		setELEMAN_ADI(rs.getString(field.ELEMAN_ADI));
		setTARIH(com.mobit.utility.getDateFromObject(rs.getObject(field.ELEMAN_TARIH)));
		setFILLER(rs.getString(field.FILLER));
		setSAAT(rs.getString(field.SAAT));
		setRESET_DUR(rs.getString(field.RESET_DUR));
		setELEMAN_SIFRE(rs.getInt(field.ELEMAN_SIFRE));

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
			stmt.setObject(4, com.mobit.utility.getObjectFromDate(getTARIH(), com.mobit.utility.TimeType.UnixEpochSeconds));
			stmt.setString(5, getFILLER());
			stmt.setString(6, getSAAT());
			stmt.setString(7, getRESET_DUR());
			stmt.setInt(8, getELEMAN_SIFRE());

			cnt = stmt.executeUpdate();
			
		}
		
		if (cnt > 0) {
			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setInt(1, getELEMAN_KODU());
			stmt.setString(2, getELEMAN_ADI());
			stmt.setObject(3, com.mobit.utility.getObjectFromDate(getTARIH(), com.mobit.utility.TimeType.UnixEpochSeconds));
			stmt.setString(4, getFILLER());
			stmt.setString(5, getSAAT());
			stmt.setString(6, getRESET_DUR());
			stmt.setInt(7, getELEMAN_SIFRE());

			stmt.setInt(8, getId());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}
	
	Yetki yetki = new Yetki();
	@Override
	public IYetki getYetki() {
		// TODO Auto-generated method stub
		return yetki;
	}
	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}

}
