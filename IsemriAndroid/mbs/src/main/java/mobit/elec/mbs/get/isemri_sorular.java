package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IResult;
import com.mobit.IndexInfo;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriSorular;
import mobit.elec.enums.ICevapTipi;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.CevapTipi;

public class isemri_sorular implements IIsemriSorular, IDeserialize, IResult {

	private int id = 0;

	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;

	private int EMIR_TURU; /* NUM 99. */
	private int ALT_EMIR_TURU; /* NUM 9999. */
	private int SORU_NO; /* NUM 99. */
	private String SORU_ACIKLAMA; /* CHAR(70). 256 */
	private ICevapTipi CEVAP_TIPI; /* NUM 9. */
	private String CEVAP_FORMAT; /* CHAR(70). 140 */

	private String CEVAP = "";/* CHAR(70). */

	private boolean checked = false;
	
	private boolean active = false;
	
	public isemri_sorular()
	{
		
	}
	public isemri_sorular(IIsemriSoru soru){
		SORU_NO = soru.getSORU_NO();
		SORU_ACIKLAMA = soru.getSORU_ACIKLAMA();
		CEVAP_TIPI = soru.getCEVAP_TIPI();
		CEVAP_FORMAT = soru.getCEVAP_FORMAT();
		
	}
	public isemri_sorular(IIsemriSorular soru){
		this((IIsemriSoru)soru);
		EMIR_TURU = soru.getALT_EMIR_TURU();
		ALT_EMIR_TURU = soru.getALT_EMIR_TURU();
		
	}
	
	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub

		
		String[] list = utility.parser(row, info);

		if (list.length != 6)
			throw new ParseException(row, list.length);

		setEMIR_TURU(utility.MBS_FORMAT.parse(list[0]).intValue());
		setALT_EMIR_TURU(utility.MBS_FORMAT.parse(list[1]).intValue());
		setSORU_NO(utility.MBS_FORMAT.parse(list[2]).intValue());
		setSORU_ACIKLAMA(list[3]);
		setCEVAP_TIPI(CevapTipi.fromInteger(utility.MBS_FORMAT.parse(list[4]).intValue()));
		setCEVAP_FORMAT(list[5]);
		

		return 0;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "sorular";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),

			new FieldInfo(field.EMIR_TURU, DbType.INTEGER, field.s_EMIR_TURU, true),
			new FieldInfo(field.ALT_EMIR_TURU, DbType.INTEGER, field.s_ALT_EMIR_TURU, true),
			new FieldInfo(field.SORU_NO, DbType.INTEGER, field.s_SORU_NO, true),
			new FieldInfo(field.SORU_ACIKLAMA, DbType.VARCHAR, field.s_SORU_ACIKLAMA_SORULAR, true),
			new FieldInfo(field.CEVAP_TIPI, DbType.INTEGER, field.s_CEVAP_TIPI_SORULAR, true),
			new FieldInfo(field.CEVAP_FORMAT, DbType.VARCHAR, field.s_CEVAP_FORMAT_SORULAR, true) };

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_soru_no", true, field.SORU_NO + "," + field.EMIR_TURU + "," + field.ALT_EMIR_TURU), };

	public static final String insertString = DbHelper.PrepareInsertString(isemri_sorular.class);
	public static final String updateString = DbHelper.PrepareUpdateString(isemri_sorular.class);

	// -------------------------------------------------------------------------

	@Override
	public String toString() {
		return SORU_ACIKLAMA;
	}

	@Override
	public int getTESISAT_NO() {
		// TODO Auto-generated method stub
		return TESISAT_NO;
	}
	@Override
	public int getSAHA_ISEMRI_NO() {
		// TODO Auto-generated method stub
		return SAHA_ISEMRI_NO;
	}
	
	@Override
	public int getEMIR_TURU() {
		return EMIR_TURU;
	}

	@Override
	public int getALT_EMIR_TURU() {
		return ALT_EMIR_TURU;
	}

	@Override
	public int getSORU_NO() {
		return SORU_NO;
	}

	@Override
	public String getSORU_ACIKLAMA() {
		return SORU_ACIKLAMA;
	}

	@Override
	public ICevapTipi getCEVAP_TIPI() {
		return CEVAP_TIPI;
	}

	@Override
	public String getCEVAP_FORMAT() {
		return CEVAP_FORMAT;
	}

	public String getCEVAP() {
		return CEVAP;
	}

	@Override
	public boolean getCheck() {
		// TODO Auto-generated method stub
		return checked;
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}

	@Override
	public String[] getCEVAP_FORMAT_LIST() {
		// TODO Auto-generated method stub
		return isemri_soru.parseCEVAP_FORMAT(getCEVAP_FORMAT());

	}

	
	@Override
	public void setTESISAT_NO(int tESISAT_NO) {
		// TODO Auto-generated method stub
		TESISAT_NO = tESISAT_NO;
	}
	@Override
	public void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO) {
		// TODO Auto-generated method stub
		SAHA_ISEMRI_NO = sAHA_ISEMRI_NO;
	}

	@Override
	public void setEMIR_TURU(int eMIR_TURU) {
		com.mobit.utility.check(eMIR_TURU, field.s_EMIR_TURU);
		EMIR_TURU = eMIR_TURU;
	}

	@Override
	public void setALT_EMIR_TURU(int aLT_EMIR_TURU) {
		com.mobit.utility.check(aLT_EMIR_TURU, field.s_ALT_EMIR_TURU);
		ALT_EMIR_TURU = aLT_EMIR_TURU;
	}

	@Override
	public void setSORU_NO(int sORU_NO) {
		com.mobit.utility.check(sORU_NO, field.s_SORU_NO);
		SORU_NO = sORU_NO;
	}

	@Override
	public void setSORU_ACIKLAMA(String sORU_ACIKLAMA) {
		com.mobit.utility.check(sORU_ACIKLAMA, field.s_SORU_ACIKLAMA_SORULAR);
		SORU_ACIKLAMA = sORU_ACIKLAMA;
	}

	@Override
	public void setCEVAP_TIPI(ICevapTipi cEVAP_TIPI) {
		CEVAP_TIPI = cEVAP_TIPI;
	}

	@Override
	public void setCEVAP_FORMAT(String cEVAP_FORMAT) {
		com.mobit.utility.check(cEVAP_FORMAT, field.s_CEVAP_FORMAT_SORULAR);
		CEVAP_FORMAT = cEVAP_FORMAT;
	}

	@Override
	public void setCEVAP(String cEVAP) {
		com.mobit.utility.check(cEVAP, field.s_CEVAP);
		CEVAP = cEVAP;
	}

	@Override
	public void setCheck(boolean checked) {
		// TODO Auto-generated method stub
		this.checked = checked;
	}
	
	@Override
	public void setActive(boolean active) {
		// TODO Auto-generated method stub
		this.active = active;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void get(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		id = rs.getInt(field.ID);

		setEMIR_TURU(rs.getInt(field.EMIR_TURU));
		setALT_EMIR_TURU(rs.getInt(field.ALT_EMIR_TURU));
		setSORU_NO(rs.getInt(field.SORU_NO));
		setSORU_ACIKLAMA(rs.getString(field.SORU_ACIKLAMA));
		setCEVAP_TIPI(CevapTipi.fromInteger(rs.getInt(field.CEVAP_TIPI)));
		setCEVAP_FORMAT(rs.getString(field.CEVAP_FORMAT));

	}
	@Override
	public String getRESULT_TYPE() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setRESULT_TYPE(String RESULT_TYPE) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getRESULT_CODE() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setRESULT_CODE(int RESULT_CODE) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getRESULT() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setRESULT(String RESULT) {
		// TODO Auto-generated method stub
		
	}
	
	
}
