package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IndexInfo;
import mobit.elec.IBorcDurum;
import mobit.elec.mbs.utility;

public class borc_durum implements IBorcDurum, IDeserialize {

	private Integer id = 0;
	
	private int TESISAT_NO;
	private String UNVAN; /* Abone Adı */
	private int BORC_ADET; /* NUM 999 */
	private double BORC_TUTARI; /* NUM 9(12).99 */
	private double BORC_GECIKME; /* NUM 9(12).99 */
	private double ACMA_KAPAMA_BEDELI; /* NUM 9(12).99 Açma Kapama */
	private String KESME_DUR; /* CHAR(3) Kesik Durumu */

	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub
		String[] list = utility.parser(row, info);
		
		if (list.length > 6) throw new ParseException(row, list.length);
		try {
		TESISAT_NO = utility.MBS_FORMAT.parse(list[0]).intValue();
		UNVAN = list[1];
		BORC_ADET = utility.MBS_FORMAT.parse(list[2]).intValue();
		BORC_TUTARI = utility.MBS_FORMAT.parse(list[3].trim()).doubleValue();
		BORC_GECIKME = utility.MBS_FORMAT.parse(list[4].trim()).doubleValue();
		ACMA_KAPAMA_BEDELI = utility.MBS_FORMAT.parse(list[5].trim()).doubleValue();
		// KESME_DUR = row.substring(i, i += field.s_KESME_DUR);
		}
		catch(Exception e){
			return 1;
		}
		
		return 0;
	}

	//-------------------------------------------------------------------------
	
	public static final String tableName = "borcdurum";
	
	public static final FieldInfo [] info = new FieldInfo[] {
			
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, true),
			new FieldInfo(field.UNVAN, DbType.VARCHAR, field.s_UNVAN, true),
			new FieldInfo(field.BORC_ADET, DbType.INTEGER, field.s_BORC_ADET, true),
			new FieldInfo(field.BORC_TUTARI, DbType.REAL, field.s_BORC_TUTARI, true),
			new FieldInfo(field.BORC_GECIKME, DbType.REAL, field.s_BORC_GECIKME, true),
			new FieldInfo(field.ACMA_KAPAMA_BEDELI, DbType.REAL, field.s_ACMA_KAPAMA_BEDELI, true),
			new FieldInfo(field.KESME_DUR, DbType.VARCHAR, field.s_KESME_DUR, true)
	};
	
	public static final IndexInfo [] indices = new IndexInfo[] {
			new IndexInfo("ix_tesisat_no", true, field.TESISAT_NO),
	};
	
	public static final String insertString = DbHelper.PrepareInsertString(borc_durum.class);
	public static final String updateString = DbHelper.PrepareUpdateString(borc_durum.class);
	
	//-------------------------------------------------------------------------
	
	
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public String getUNVAN() {
		return UNVAN;
	}

	public int getBORC_ADET() {
		return BORC_ADET;
	}

	public double getBORC_TUTARI() {
		return BORC_TUTARI;
	}

	public double getBORC_GECIKME() {
		return BORC_GECIKME;
	}

	public double getACMA_KAPAMA_BEDELI() {
		return ACMA_KAPAMA_BEDELI;
	}

	public String getKESME_DUR() {
		return KESME_DUR;
	}

	public void setTESISAT_NO(int TESISAT_NO) {
		com.mobit.utility.check(TESISAT_NO, field.s_TESISAT_NO);
		this.TESISAT_NO = TESISAT_NO;
	}

	public void setUNVAN(String UNVAN) {
		com.mobit.utility.check(UNVAN, field.s_UNVAN);
		this.UNVAN = UNVAN;
	}

	public void setBORC_ADET(int BORC_ADET) {
		com.mobit.utility.check(BORC_ADET, field.s_BORC_ADET);
		this.BORC_ADET = BORC_ADET;
	}

	public void setBORC_TUTARI(double BORC_TUTARI) {
		com.mobit.utility.check(BORC_TUTARI, field.s_BORC_TUTARI, field.s_TUTAR_PREC);
		this.BORC_TUTARI = BORC_TUTARI;
	}

	public void setBORC_GECIKME(double BORC_GECIKME) {
		com.mobit.utility.check(BORC_GECIKME, field.s_BORC_GECIKME, field.s_TUTAR_PREC);
		this.BORC_GECIKME = BORC_GECIKME;
	}

	public void setACMA_KAPAMA_BEDELI(double ACMA_KAPAMA_BEDELI) {
		com.mobit.utility.check(ACMA_KAPAMA_BEDELI, field.s_ACMA_KAPAMA_BEDELI, field.s_TUTAR_PREC);
		this.ACMA_KAPAMA_BEDELI = ACMA_KAPAMA_BEDELI;
	}

	public void setKESME_DUR(String KESME_DUR) {
		com.mobit.utility.check(KESME_DUR, field.s_KESME_DUR);
		this.KESME_DUR = KESME_DUR;
	}

	
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	
	public void get(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		id = rs.getInt(field.ID);
		
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setUNVAN(rs.getString(field.UNVAN));
		setBORC_TUTARI(rs.getDouble(field.BORC_TUTARI));
		setBORC_GECIKME(rs.getDouble(field.BORC_GECIKME));
		setACMA_KAPAMA_BEDELI(rs.getDouble(field.ACMA_KAPAMA_BEDELI));
		setKESME_DUR(rs.getString(field.KESME_DUR));
	}

	

}
