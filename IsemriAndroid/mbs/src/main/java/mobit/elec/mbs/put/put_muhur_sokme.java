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
import mobit.elec.IMuhurSokme;
import mobit.elec.ISeriNo;
import mobit.elec.ISerialize;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.muhur_kod;

public class put_muhur_sokme extends ICommand implements IMuhurSokme, IDb {

	private Integer id;
	private Integer masterId;
	
	private int TESISAT_NO;
	private ISeriNo SERI_NO;
	private IMuhurKod MUHUR_DURUMU;
	private IMuhurKod IPTAL_DUR;
	
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
	public IMuhurKod getMUHUR_DURUMU() {
		return MUHUR_DURUMU;
	}
	public void setMUHUR_DURUMU(IMuhurKod mUHUR_DURUMU) throws Exception {
		
		if(mUHUR_DURUMU == null) throw new MobitException("Mühür durumu seçin!");
		mUHUR_DURUMU.check(MuhurKodCins.Durum);
		MUHUR_DURUMU = mUHUR_DURUMU;
	}
	public IMuhurKod getIPTAL_DUR() {
		return IPTAL_DUR;
	}
	public void setIPTAL_DUR(IMuhurKod iPTAL_DUR) throws Exception {
		if(iPTAL_DUR == null) throw new MobitException("İptal durumu seçin!");
		iPTAL_DUR.check(MuhurKodCins.Iptal);
		IPTAL_DUR = iPTAL_DUR;
	}
	
	public put_muhur_sokme()
	{
		super();
	}
	
	public put_muhur_sokme(IIsemri isemri) {
		setTESISAT_NO(isemri.getTESISAT_NO());
	}
	
	public put_muhur_sokme(int tESISAT_NO, SeriNo sERI_NO, IMuhurKod mUHUR_DURUMU, IMuhurKod iPTAL_DUR) throws Exception {
		super();
		setTESISAT_NO(tESISAT_NO);
		setSERI_NO(sERI_NO);
		setMUHUR_DURUMU(mUHUR_DURUMU);
		setIPTAL_DUR(iPTAL_DUR);
	}

	@Override
	public String toString()
	{
		return String.format("Mühür Sökme\nTesisat No:%d Seri No:%s %s %s",
				TESISAT_NO, SERI_NO, MUHUR_DURUMU, IPTAL_DUR);
	}
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	static final String format = "PUT MUHUR_SOKME %0"+field.s_TESISAT_NO+"d";
	@Override
	public void toSerialize(StringBuilder b)  throws Exception {
		// TODO Auto-generated method stub
		
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO));
		((ISerialize)SERI_NO).toSerialize(b);
		((ISerialize)MUHUR_DURUMU).toSerialize(b);
		((ISerialize)IPTAL_DUR).toSerialize(b);
		
		b.append('\n');
		
	}
	
	//-------------------------------------------------------------------------
	
	public static final int TABLOID = 13;
	
	public static final String tableName = "muhursokme";
	
	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, false),
			new FieldInfo(field.MUHUR_SERI, DbType.VARCHAR, field.s_MUHUR_SERI, false),
			new FieldInfo(field.MUHUR_NO, DbType.INTEGER, field.s_MUHUR_NO, false),
			new FieldInfo(field.MUHUR_DURUMU, DbType.INTEGER, field.s_MUHUR_DURUMU, false),
			new FieldInfo(field.IPTAL_DUR, DbType.INTEGER, field.s_IPTAL_DUR, false),
	};
	
	public static final String insertString = DbHelper.PrepareInsertString(put_muhur_sokme.class);
	public static final String updateString = DbHelper.PrepareUpdateString(put_muhur_sokme.class, field.ID);
	
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
		setMUHUR_DURUMU(new muhur_kod(rs.getInt(field.MUHUR_DURUMU), MuhurKodCins.Durum));
		setIPTAL_DUR(new muhur_kod(rs.getInt(field.IPTAL_DUR), MuhurKodCins.Iptal));
		
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
			
			stmt.setInt(6, getMUHUR_DURUMU().getMUHUR_KODU());
			stmt.setInt(7, getIPTAL_DUR().getMUHUR_KODU());
			
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
			
			stmt.setInt(5, getMUHUR_DURUMU().getMUHUR_KODU());
			stmt.setInt(6, getIPTAL_DUR().getMUHUR_KODU());
			//where
			stmt.setInt(7, getId());
			cnt = stmt.executeUpdate();
		}

		return cnt;
	}
	
	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<IMuhurSokme> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IMuhurSokme> list = new ArrayList<IMuhurSokme>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_muhur_sokme pms = new put_muhur_sokme();
				pms.get(rs);
				list.add(pms);
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
