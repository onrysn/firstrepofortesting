package com.mobit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class IslemMaster implements IIslemMaster, IDb, IResult {

	protected Integer id;
	protected Date zaman;
	protected int tabloId;
	protected IRecordStatus durum = RecordStatus.None;
	protected Integer islemTipi;
	protected IEleman eleman;
	protected ICbs cbs;
	protected int isemriNo;
	protected Long grupId;
	protected String RESULT_TYPE;
	protected int RESULT_CODE;
	protected String RESULT;
	
	
	protected IIslem islem = null;
	protected IApplication app;
	
	
	public IslemMaster(IApplication app)
	{
		this.app = app;
	}
	
	public IslemMaster(IApplication app, IIslem islem) throws MobitException
	{
		this(app);
		add(islem);
	}
	
	public void add(IIslem islem)
	{
		this.islem = islem;
		
	}
	
	public IslemMaster(IIslem islem)
	{
		this.islem = islem;	
	}
	
	@Override
	public Date getZaman()
	{
		return zaman;
	}
	@Override
	public void setZaman(Date zaman)
	{
		this.zaman = zaman;
	}
	@Override
	public  IRecordStatus getDurum()
	{
		return durum;
	}
	@Override
	public void setDurum(IRecordStatus durum)
	{
		this.durum = durum;
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
	public int getTabloId() {
		// TODO Auto-generated method stub
		return tabloId;
	}
	@Override
	public IIslem getIslem() throws Exception {
		// TODO Auto-generated method stub
		if(islem != null) return islem;
		return loadIslem();
	}
	
	@Override
	public Integer getIslemTipi()
	{
		return islemTipi;
	}
	@Override
	public void setIslemTipi(Integer islemTipi)
	{
		this.islemTipi = islemTipi;
	}
	
	@Override
	public IEleman getEleman()
	{
		return eleman;
	}
	@Override
	public void setEleman(IEleman eleman)
	{
		this.eleman = eleman;
	}
	@Override
	public ICbs getCBS()
	{
		return cbs;
	}
	@Override
	public void setCBS(ICbs cbs)
	{
		//MUHAMMED GÖKKAYA
		this.cbs = cbs;
	}
	@Override
	public Integer getIsemriNo()
	{
		return isemriNo;
	}
	@Override
	public void setIsemriNo(Integer isemriNo)
	{
		this.isemriNo = isemriNo;
	}
	
	@Override
	public Long getGrupId() {
		// TODO Auto-generated method stub
		return grupId;
	}
	
	@Override
	public void setGrupId(Long grupId) {
		// TODO Auto-generated method stub
		this.grupId = grupId; 
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
	
	
	@Override
	public void Save() throws Exception
	{
		boolean tran = false;
		DbHelper h = null;
		Connection conn = null;
		try {
			conn = app.getConnection();
			h = new DbHelper(conn);
			conn.setAutoCommit(false);
			tran = true;
			
			tabloId = 0;
			if(islem instanceof IDb) tabloId = ((IDb)islem).getTabloId(); 
			
			if(put(h) == 0) throw new MobitException("Master işlem kaydı oluşturulamadı!");
			
			if(islem instanceof IDb) ((IDb)islem).setMasterId(getId());
			DbHelper.Save(conn, islem);
			
			
		}
		catch(Exception e){
			if(tran) conn.rollback();
			throw e;
		}
		finally {
			conn.setAutoCommit(true);
			if(h != null) h.close();
			
		}
	}
	
	@Override
	public void get(ResultSet rs) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		id = rs.getInt(sID);
		setZaman(utility.getDateFromObject(rs.getObject(sZAMAN)));
		tabloId = rs.getInt(sTABLOID);
		durum = RecordStatus.fromInteger(rs.getInt(sDURUM));
		islemTipi = (Integer)rs.getObject(sISLEM_TIPI);
		eleman = new eleman(rs, sELEMAN_NO);
		cbs = new Cbs(rs, sENLEM, sBOYLAM);
		isemriNo = rs.getInt(sSAHA_ISEMRI_NO);
		
		RESULT_TYPE = rs.getString(sRESULT_TYPE);
		RESULT_CODE = rs.getInt(sRESULT_CODE);
		RESULT = rs.getString(sRESULT);
				
		grupId = rs.getLong(sGRUPID);
	}
	
	
	@Override
	public int put(DbHelper h) throws SQLException, ParseException, Exception {
		// TODO Auto-generated method stub
		int cnt;
		int index;
		PreparedStatement stmt = null;
		
		if (getId() == null) {
			
			stmt = h.getInsStatment(this);
			
			index = 1;
			
			stmt.setObject(index++, getId());
			stmt.setObject(index++, utility.getObjectFromDate(getZaman(), utility.TimeType.UnixEpochSeconds)); // utility.TimeType.UnixEpochSeconds
			
			stmt.setInt(index++, tabloId);
			stmt.setInt(index++, getDurum().getValue());
			stmt.setObject(index++, islemTipi);
			
			stmt.setObject(index++, (eleman != null) ? eleman.getELEMAN_KODU() : null);
			stmt.setObject(index++, cbs != null ? cbs.getX() : null);
			stmt.setObject(index++, cbs != null ? cbs.getY() : null);
			stmt.setObject(index++, isemriNo);
			
			stmt.setString(index++, RESULT_TYPE);
			stmt.setInt(index++, RESULT_CODE);
			stmt.setString(index++, RESULT);
			
			stmt.setObject(index++, grupId);
			
			cnt = stmt.executeUpdate();
			
			if (cnt > 0) {
				id = h.GetId();
				return id;
			}
			
		}
		else {
			
			stmt = h.getUpdStatment(this);

			index = 1;
			
			stmt.setObject(index++, utility.getObjectFromDate(getZaman(), utility.TimeType.UnixEpochSeconds));
			stmt.setInt(index++, tabloId);
			stmt.setInt(index++, getDurum().getValue());
			stmt.setObject(index++, islemTipi);
			
			stmt.setObject(index++, (eleman != null) ? eleman.getELEMAN_KODU() : null);
			stmt.setObject(index++, cbs != null ? cbs.getX() : null);
			stmt.setObject(index++, cbs != null ? cbs.getY() : null);
			stmt.setObject(index++, isemriNo);
			
			stmt.setString(index++, RESULT_TYPE);
			stmt.setInt(index++, RESULT_CODE);
			stmt.setString(index++, RESULT);
			
			stmt.setObject(index++, grupId);
			// where
			stmt.setObject(index++, getId());
			cnt = stmt.executeUpdate();
		}
		
		
		return cnt;
	}
	
	public static final String tableName = "islem";
	
	public static final String sID = "ID";
	public static final String sZAMAN = "ZAMAN";
	public static final String sTABLOID = "TABLOID";
	public static final String sDURUM = "DURUM";
	public static final String sISLEM_TIPI = "ISLEM_TIPI";
	
	public static final String sELEMAN_NO = "ELEMAN_NO";
	public static final String sENLEM = "ENLEM";
	public static final String sBOYLAM = "BOYLAM";
	public static final String sSAHA_ISEMRI_NO = "SAHA_ISEMRI_NO";
	public static final String sGRUPID = "GRUPID";
	
	public static final String sRESULT_TYPE = "RESULT_TYPE";
	public static final String sRESULT_CODE = "RESULT_CODE";
	public static final String sRESULT = "RESULT";
	
	public static final int s_RESULT_TYPE = 10;
	public static final int s_RESULT = 8*1024;
	

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(sID, true, DbType.INTEGER, 0, false),
			new FieldInfo(sZAMAN, DbType.DATETIME, 0, false),
			new FieldInfo(sTABLOID, DbType.INTEGER, 0, false),
			new FieldInfo(sDURUM, DbType.INTEGER, 0, false),
			new FieldInfo(sISLEM_TIPI, DbType.INTEGER, 0, false),
			
			new FieldInfo(sELEMAN_NO, DbType.INTEGER, 0, false),
			new FieldInfo(sENLEM, DbType.REAL, 0, false),
			new FieldInfo(sBOYLAM, DbType.REAL, 0, false),
			new FieldInfo(sSAHA_ISEMRI_NO, DbType.INTEGER, 0, false),
			
			new FieldInfo(sRESULT_TYPE, DbType.VARCHAR, s_RESULT_TYPE, false),
			new FieldInfo(sRESULT_CODE, DbType.INTEGER, 0, false),
			new FieldInfo(sRESULT, DbType.VARCHAR, s_RESULT, false),
			
			new FieldInfo(sGRUPID, DbType.INTEGER, 0, false),
			
	};
	
	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_zaman", false, sZAMAN),
			new IndexInfo(tableName+"_durum", false, sDURUM),
	};

	public static final String insertString = DbHelper.PrepareInsertString(IslemMaster.class);
	public static final String updateString = DbHelper.PrepareUpdateString(IslemMaster.class, "ID");
		
	private static final String updateDurum = String.format("update %s set %s=? where %s=?", tableName, sDURUM, sID);
	public void updateDurum(IRecordStatus durum) throws Exception {
		
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			
			conn = app.getConnection();
			stmt = conn.prepareStatement(updateDurum);
			stmt.setInt(1, durum.getValue());
			stmt.setObject(2, getId());
			stmt.execute();
			setDurum(durum);
			
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	protected abstract IIslem loadIslem() throws Exception;
	
	protected static final String query1 = String.format("select * from %s where %s = ? order by %s asc", tableName, sDURUM, sID);

	
	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void ResetId()
	{
		setId(null);
		if(islem instanceof IDb) ((IDb)islem).setMasterId(null);
	}

	static final String query2 = String.format("select count(*) adet from %s where %s = ? ", tableName, sDURUM);
	
	public static int select(Connection conn, IRecordStatus status) throws Exception
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn.prepareStatement(query2);
			stmt.setInt(1, status.getValue());
			rs = stmt.executeQuery();
			if (rs.next()) return rs.getInt("adet");

		} catch (Exception e) {

			throw e;

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}
		return 0;
	}
	
}
