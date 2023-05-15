package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IndexInfo;
import mobit.elec.IMuhur;
import mobit.elec.IMuhurKod;
import mobit.elec.ISeriNo;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.utility;

public class muhur implements IMuhur, IDeserialize {

	private Integer id = null;
	
	private int TESISAT_NO;
	private ISeriNo SERI_NO;
	private int MUHUR_YERI;
	private Date MUHUR_TARIHI;
	private int MUHUR_NEDENI;
	private String MUHUR_DUR;
	
	private String RESULT_TYPE;
	private int RESULT_CODE;
	private String RESULT;

	private boolean checked = false; //H.Elif

	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub

		String[] list = utility.parser(row, info);

		if (list.length != 6) throw new ParseException(row, list.length);

		setSERI_NO(new SeriNo(list[0], list[1]));
		setMUHUR_YERI(utility.MBS_FORMAT.parse(list[2]).intValue());
		setMUHUR_TARIHI(field.date_formatter.parse(list[3]));
		setMUHUR_NEDENI(utility.MBS_FORMAT.parse(list[4]).intValue());
		setMUHUR_DUR(list[5]);

		return 0;
	}
	
	//-------------------------------------------------------------------------

	public static final String tableName = "muhur";
	
	public static final FieldInfo [] info = new FieldInfo[]{
			
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),	
			
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, false),
			new FieldInfo(field.MUHUR_SERI, DbType.VARCHAR, field.s_MUHUR_SERI, true),
			new FieldInfo(field.MUHUR_NO, DbType.INTEGER, field.s_MUHUR_NO, true),
			new FieldInfo(field.MUHUR_YERI, DbType.VARCHAR, field.s_MUHUR_YERI, true),
			new FieldInfo(field.MUHUR_TARIHI, DbType.DATE, field.s_MUHUR_TARIHI, true),
			new FieldInfo(field.MUHUR_NEDENI, DbType.INTEGER, field.s_MUHUR_NEDENI, true),
			new FieldInfo(field.MUHUR_DUR, DbType.VARCHAR, field.s_MUHUR_DUR, true),
			
			new FieldInfo(field.RESULT_TYPE, DbType.VARCHAR, field.s_RESULT_TYPE, false),
			new FieldInfo(field.RESULT_CODE, DbType.INTEGER, 0, false),
			new FieldInfo(field.RESULT, DbType.VARCHAR, field.s_RESULT, false)
	};
	
	public static final IndexInfo [] indices = new IndexInfo[] {
			new IndexInfo("ix_tesisat_no", true, field.TESISAT_NO+","+field.MUHUR_SERI+","+field.MUHUR_NO),
	};
	
	public static final String insertString = DbHelper.PrepareInsertString(muhur.class);
	public static final String updateString = DbHelper.PrepareUpdateString(muhur.class);
	
	//-------------------------------------------------------------------------
	
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public ISeriNo getSERI_NO() {
		return SERI_NO;
	}

	public int getMUHUR_YERI() {
		return MUHUR_YERI;
	}

	public Date getMUHUR_TARIHI() {
		return MUHUR_TARIHI;
	}

	public int getMUHUR_NEDENI() {
		return MUHUR_NEDENI;
	}

	public String getMUHUR_DUR() {
		return MUHUR_DUR;
	}

	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public void setSERI_NO(ISeriNo sERI_NO) {
		SERI_NO = sERI_NO;
	}

	public void setMUHUR_YERI(int mUHUR_YERI) {
		com.mobit.utility.check(mUHUR_YERI, field.s_MUHUR_YERI);
		MUHUR_YERI = mUHUR_YERI;
	}

	public void setMUHUR_TARIHI(Date mUHUR_TARIHI) {
		MUHUR_TARIHI = mUHUR_TARIHI;
	}

	public void setMUHUR_NEDENI(int mUHUR_NEDENI) {
		com.mobit.utility.check(mUHUR_NEDENI, field.s_MUHUR_NEDENI);
		MUHUR_NEDENI = mUHUR_NEDENI;
	}

	public void setMUHUR_DUR(String mUHUR_DUR) {
		com.mobit.utility.check(mUHUR_DUR, field.s_MUHUR_DUR);
		MUHUR_DUR = mUHUR_DUR;
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
	
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	
	public void get(ResultSet rs) throws SQLException, ParseException {
		// TODO Auto-generated method stub
		
		id = rs.getInt(field.ID);
		
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSERI_NO(SeriNo.fromString(rs.getString(field.MUHUR_SERI), rs.getString(field.MUHUR_NO)));
		setMUHUR_YERI(rs.getInt(field.MUHUR_YERI));
		setMUHUR_TARIHI(com.mobit.utility.fromISO8601(rs.getString(field.MUHUR_TARIHI)));
		setMUHUR_NEDENI(rs.getInt(field.MUHUR_NEDENI));
		setMUHUR_DUR(rs.getString(field.MUHUR_DUR));
		
	}

	IMuhurKod muhurYeri = null;
	@Override
	public IMuhurKod getMUHUR_YERI2() {

		if(muhurYeri == null) muhurYeri = new muhur_kod(getMUHUR_YERI(), MuhurKodCins.Yer);
		return muhurYeri;
	}
	IMuhurKod muhurNedeni = null;
	@Override
	public IMuhurKod getMUHUR_NEDENI2() {
		if(muhurNedeni == null) muhurNedeni = new muhur_kod(getMUHUR_NEDENI(), MuhurKodCins.Neden);
		return muhurNedeni;
	}
	IMuhurKod muhurDur = null;
	@Override
	public IMuhurKod getMUHUR_DUR2() {
		if(muhurDur == null) muhurDur = new muhur_kod(Integer.parseInt(getMUHUR_DUR()), MuhurKodCins.Durum);
		return muhurDur;
	}

	@Override
	public boolean getCheck()
	{
		return checked;
	} //H.Elif
	@Override
	public void setCheck(boolean checked)
	{
		this.checked = checked;
	} //H.Elif

}
