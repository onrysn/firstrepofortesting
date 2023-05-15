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
import com.mobit.MobitException;
import mobit.elec.IIsemri;
import mobit.elec.IMuhurKod;
import mobit.elec.ISeriNo;
import mobit.elec.ISerialize;
import mobit.elec.ITesisatMuhur;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.muhur_kod;

public class put_tesisat_muhur extends ICommand implements ITesisatMuhur, IDb {

	private Integer id;
	private Integer masterId; 
	
	private int TESISAT_NO;
	private ISeriNo SERI_NO;
	private IMuhurKod MUHUR_YERI;
	private IMuhurKod MUHUR_NEDENI;
	
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public ISeriNo getSERI_NO() {
		return SERI_NO;
	}

	public void setSERI_NO(ISeriNo sERI_NO) {
		SERI_NO = sERI_NO;
	}

	public IMuhurKod getMUHUR_YERI() {
		return MUHUR_YERI;
	}

	public void setMUHUR_YERI(IMuhurKod mUHUR_YERI) throws Exception {
		if(mUHUR_YERI == null) throw new MobitException("Mühürleme yeri seçin!");
		mUHUR_YERI.check(MuhurKodCins.Yer);
		MUHUR_YERI = mUHUR_YERI;
	}

	public IMuhurKod getMUHUR_NEDENI() {
		return MUHUR_NEDENI;
	}

	public void setMUHUR_NEDENI(IMuhurKod mUHUR_NEDENI) throws Exception {
		if(mUHUR_NEDENI == null) throw new MobitException("Mühürleme nedeni seçin!");
		mUHUR_NEDENI.check(MuhurKodCins.Neden);
		MUHUR_NEDENI = mUHUR_NEDENI;
	}
	
	public put_tesisat_muhur()
	{
		
	}
	
	public put_tesisat_muhur(IIsemri isemri)
	{
		setTESISAT_NO(isemri.getTESISAT_NO());
	}
	
	public put_tesisat_muhur(int tESISAT_NO, SeriNo sERI_NO, IMuhurKod mUHUR_YERI, IMuhurKod mUHUR_NEDENI) throws Exception {
		super();
		setTESISAT_NO(tESISAT_NO);
		setSERI_NO(sERI_NO);
		setMUHUR_YERI(mUHUR_YERI);
		setMUHUR_NEDENI(mUHUR_NEDENI);
	}

	@Override
	public String toString()
	{

		return String.format("Mühürleme\nTesisat No:%d Seri No:%s %s %s",
				TESISAT_NO, SERI_NO, MUHUR_YERI, MUHUR_NEDENI);
	}

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT TESISAT_MUHUR %0"+field.s_TESISAT_NO+"d";
	
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO));
		((ISerialize)SERI_NO).toSerialize(b);
		((ISerialize)MUHUR_YERI).toSerialize(b); 
		((ISerialize)MUHUR_NEDENI).toSerialize(b);
		
		b.append('\n');
		
	}
	
	//-------------------------------------------------------------------------
	
	public static final int TABLOID = 14;
	
	public static final String tableName = "muhurleme";
	
	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, false),
			new FieldInfo(field.MUHUR_SERI, DbType.VARCHAR, field.s_MUHUR_SERI, false),
			new FieldInfo(field.MUHUR_NO, DbType.INTEGER, field.s_MUHUR_NO, false),
			new FieldInfo(field.MUHUR_YERI, DbType.INTEGER, field.s_MUHUR_YERI, false),
			new FieldInfo(field.MUHUR_NEDENI, DbType.INTEGER, field.s_MUHUR_NEDENI, false),
	};
	
	public static final String insertString = DbHelper.PrepareInsertString(put_tesisat_muhur.class);
	public static final String updateString = DbHelper.PrepareUpdateString(put_tesisat_muhur.class, field.ID);
	
	//-------------------------------------------------------------------------

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
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		this.masterId = masterId;
	}

	@Override
	public void get(ResultSet rs) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		id = rs.getInt(field.ID);
		setMasterId(rs.getInt(field.ISLEM_ID));
		
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSERI_NO(new SeriNo(rs, field.MUHUR_SERI, field.MUHUR_NO));
		setMUHUR_YERI(new muhur_kod(rs.getInt(field.MUHUR_YERI), MuhurKodCins.Yer));
		setMUHUR_NEDENI(new muhur_kod(rs.getInt(field.MUHUR_NEDENI), MuhurKodCins.Neden));
	}

	@Override
	public int put(DbHelper h) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		int cnt = 0;
		PreparedStatement stmt;

		if (getId() == null) {
			
			stmt = h.getInsStatment(this);

			stmt.setObject(1, null);
			stmt.setObject(2, masterId);
			stmt.setInt(3, getTESISAT_NO());
			if(getSERI_NO() != null){
				stmt.setString(4, getSERI_NO().getSeri());
				stmt.setInt(5, getSERI_NO().getNo());
			}
			
			stmt.setInt(6, getMUHUR_YERI().getMUHUR_KODU());
			stmt.setInt(7, getMUHUR_NEDENI().getMUHUR_KODU());
			
			cnt = stmt.executeUpdate();
			
		}
		
		if (cnt > 0) {
			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setObject(1, masterId);
			stmt.setInt(2, getTESISAT_NO());
			if(getSERI_NO() != null){
				stmt.setString(3, getSERI_NO().getSeri());
				stmt.setInt(4, getSERI_NO().getNo());
			}
			
			stmt.setInt(5, getMUHUR_YERI().getMUHUR_KODU());
			stmt.setInt(6, getMUHUR_NEDENI().getMUHUR_KODU());
			//where
			stmt.setInt(7, getId());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}
	
	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<ITesisatMuhur> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ITesisatMuhur> list = new ArrayList<ITesisatMuhur>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_tesisat_muhur ptm = new put_tesisat_muhur();
				ptm.get(rs);
				list.add(ptm);
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
