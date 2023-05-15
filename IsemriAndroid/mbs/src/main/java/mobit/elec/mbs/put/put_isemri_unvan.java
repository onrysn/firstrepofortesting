package mobit.elec.mbs.put;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IDb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import mobit.elec.Aciklama;
import mobit.elec.IAciklama;
import mobit.elec.IIsemriUnvan;
import mobit.elec.ISerialize;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.muhur_kod;
import mobit.elec.mbs.utility;

public class put_isemri_unvan extends ICommand implements IDb,IIsemriUnvan {

	private Integer masterId;
	private Integer id;
	
	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	private IAciklama TC_KIMLIK_NO;
	private IAciklama VERGI_NO;
	private IAciklama UNVAN;

	public int getTESISAT_NO() { return TESISAT_NO;}
	public void setTESISAT_NO(int TESISAT_NO){
		com.mobit.utility.check(TESISAT_NO, field.s_TESISAT_NO);
		this.TESISAT_NO = TESISAT_NO;
	}
	public int getSAHA_ISEMRI_NO() { return SAHA_ISEMRI_NO;}
	public void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO) {
		com.mobit.utility.check(SAHA_ISEMRI_NO, field.s_SAHA_ISEMRI_NO);
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
	}
	public IAciklama getTC_KIMLIK_NO() { return TC_KIMLIK_NO;}
	public void setTC_KIMLIK_NO(IAciklama TC_KIMLIK_NO) {
		com.mobit.utility.check(TC_KIMLIK_NO.getSTR(), field.s_TC_KIMLIK_NO);
		this.TC_KIMLIK_NO = TC_KIMLIK_NO;
	}
	public IAciklama getVERGI_NO() { return VERGI_NO;}
	public void setVERGI_NO(IAciklama VERGI_NO) {
		com.mobit.utility.check(VERGI_NO.getSTR(), field.s_VERGI_NO);
		this.VERGI_NO = VERGI_NO;
	}
	public IAciklama getUNVAN() { return UNVAN;}
	public void setUNVAN(IAciklama UNVAN) throws Exception {
		com.mobit.utility.check(UNVAN.getSTR(), field.s_UNVAN);
		this.UNVAN = UNVAN;
	}
	
	public put_isemri_unvan(int tESISAT_NO, int sAHA_ISEMRI_NO,IAciklama tC_KIMLIK_NO,IAciklama vERGI_NO, Aciklama uNVAN) throws Exception {
		super();
		setTESISAT_NO(tESISAT_NO);
		setSAHA_ISEMRI_NO(sAHA_ISEMRI_NO);
		setTC_KIMLIK_NO(tC_KIMLIK_NO);
		setVERGI_NO(vERGI_NO);
		setUNVAN(uNVAN);
	}
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT ISEMRI_UNVAN %0"+field.s_TESISAT_NO+"d%0"+field.s_SAHA_ISEMRI_NO+"d";



	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, SAHA_ISEMRI_NO));

	/*	if (TC_KIMLIK_NO != null) {
			ISerialize ser1 = (TC_KIMLIK_NO instanceof ISerialize) ? (ISerialize) TC_KIMLIK_NO
					: new mobit.elec.mbs.get.Aciklama(TC_KIMLIK_NO.getSTR());
			ser1.toSerialize(b);
		}

		if (VERGI_NO != null) {
			ISerialize ser2 = (VERGI_NO instanceof ISerialize) ? (ISerialize) VERGI_NO
					: new mobit.elec.mbs.get.Aciklama(VERGI_NO.getSTR());
			ser2.toSerialize(b);
		}*/

		/*((ISerialize)TESISAT_NO).toSerialize(b);
		((ISerialize)SAHA_ISEMRI_NO).toSerialize(b);*/
		b.append(TC_KIMLIK_NO.getSTR());
		b.append(VERGI_NO.getSTR());
		if (UNVAN != null) {
			ISerialize ser3 = (UNVAN instanceof ISerialize) ? (ISerialize) UNVAN
					: new mobit.elec.mbs.get.Aciklama(UNVAN.getSTR());
			ser3.toSerialize(b);
		}
		//((ISerialize)UNVAN).toSerialize(b);
		b.append('\n');
	}
	//-------------------------------------------------------------------------

	public static final String tableName = "unvan";
	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),

			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, false),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.INTEGER, field.s_SAHA_ISEMRI_NO, false),
			new FieldInfo(field.TC_KIMLIK_NO, DbType.VARCHAR, field.s_TC_KIMLIK_NO, false),
			new FieldInfo(field.VERGI_NO, DbType.VARCHAR, field.s_VERGI_NO, false),
			new FieldInfo(field.UNVAN,  DbType.VARCHAR, field.s_UNVAN, false),
	};

	public static final String insertString = DbHelper.PrepareInsertString(put_isemri_unvan.class);
	public static final String updateString = DbHelper.PrepareUpdateString(put_isemri_unvan.class, field.ID);

	public static final int TABLOID = 19;
	//-------------------------------------------------------------------------
	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		this.masterId = masterId;
	}

	@Override
	public int getTabloId() {
		// TODO Auto-generated method stub
		return 0;
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
	public void get(ResultSet rs) throws SQLException, ParseException, Exception {
		id = rs.getInt(field.ID);
		setMasterId(rs.getInt(field.ISLEM_ID));

		Aciklama unvan =new Aciklama(rs.getString(field.UNVAN));
	Aciklama tc =new Aciklama(rs.getString(field.TC_KIMLIK_NO));
		Aciklama vergi_no =new Aciklama(rs.getString(field.VERGI_NO));

		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSAHA_ISEMRI_NO(rs.getInt(field.SAHA_ISEMRI_NO));
		setTC_KIMLIK_NO(tc);
		setVERGI_NO(vergi_no);
		setUNVAN(unvan);

	}

	@Override
	public int put(DbHelper h) throws SQLException, ParseException, Exception {
		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {

			stmt = h.getInsStatment(this);

			stmt.setObject(1, null);
			stmt.setObject(2, masterId);
			stmt.setInt(3, getTESISAT_NO());
			stmt.setInt(4, getSAHA_ISEMRI_NO());
			stmt.setString(5, getTC_KIMLIK_NO().getSTR());
			stmt.setString(6, getVERGI_NO().getSTR());
			stmt.setString(7, getUNVAN().getSTR());

			cnt = stmt.executeUpdate();

			if (cnt > 0) {
				id = h.GetId();
			}

		} else {

			stmt = h.getUpdStatment(this);


			stmt.setObject(1, null);
			stmt.setObject(2, masterId);
			stmt.setInt(3, getTESISAT_NO());
			stmt.setInt(4, getSAHA_ISEMRI_NO());
			stmt.setString(5,getTC_KIMLIK_NO().getSTR());
			stmt.setString(6, getVERGI_NO().getSTR());
			stmt.setString(7, getUNVAN().getSTR());
			//where
			stmt.setInt(8, getId());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

}
