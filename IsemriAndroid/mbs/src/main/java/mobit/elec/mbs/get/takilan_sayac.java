package mobit.elec.mbs.get;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IDb;
import com.mobit.IResult;
import com.mobit.IndexInfo;
import com.mobit.MobitException;
import mobit.eemr.IReadResult;
import mobit.elec.Endeksler;
import mobit.elec.IEndeks;
import mobit.elec.IEndeksler;
import mobit.elec.IIsemri;
import mobit.elec.ITakilanSayac;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayacMarka;
import mobit.elec.ISdurum;
import mobit.elec.ISeriNo;
import mobit.elec.ISerialize;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IFazSayisi;
import mobit.elec.enums.IMulkiyet;
import mobit.elec.enums.IOkumaMetodu;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IVoltaj;
import mobit.elec.mbs.enums.SayacHaneSayisi;
import mobit.elec.mbs.Endeks;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.FazSayisi;
import mobit.elec.mbs.enums.Mulkiyet;
import mobit.elec.mbs.enums.OkumaMetodu;
import mobit.elec.mbs.enums.SayacCinsi;
import mobit.elec.mbs.enums.SayacKodu;
import mobit.elec.mbs.enums.Voltaj;

public class takilan_sayac extends SayacBilgi implements IDb, ITakilanSayac, IResult {

	private Integer id;
	private Integer masterId;
	
	private int TESISAT_NO;
	private int SAHA_ISEMRI_NO;
	
	private IMulkiyet MULKIYET; /*
	 * NUM 9. 0:Gecici Kurum Sayacç, 1:Abone Yeni
	 * Sayacı, 2:Abone Kendi Sayacı
	 */
	private ISeriNo SERI_NO; /*
								 * CHAR(1) Sayaca atılan mühür Seri No NUM 9(8)
								 * Sayaca atılan Mühür Sıra No
								 */

	/* E harfi gönderilecek *ü
	//private IEndeks[] ENDEKS; /* NUM 9(7).999 Takılma Endeksleri */
	
	private IEndeksler ENDEKSLER = new Endeksler();
	
	private String RESULT_TYPE;
	private int RESULT_CODE;
	private String RESULT;
	
	private IIsemri isemri;
	
	private IOkumaMetodu OKUMA_METODU = OkumaMetodu.Manuel;
	
	private IReadResult result;
	
	private Boolean yeniSayac = null;
	
	public static final int TABLOID = 7;
	@Override
	public int getTabloId()
	{
		return TABLOID;
	}
	
	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub
		this.masterId = masterId;
	}
		
	@Override
	public int getTESISAT_NO()
	{
		return TESISAT_NO;
	}
	
	@Override
	public void setTESISAT_NO(int TESISAT_NO)
	{
		this.TESISAT_NO = TESISAT_NO;
	}
	
	@Override
	public int getSAHA_ISEMRI_NO()
	{
		return SAHA_ISEMRI_NO;
	}
	
	@Override
	public void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO)
	{
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
	}

	@Override
	public ISeriNo getSERI_NO() {
		return SERI_NO;
	}

	@Override
	public void setSERI_NO(ISeriNo SERI_NO) {
		this.SERI_NO = SERI_NO;
	}
	
	@Override
	public IMulkiyet getMULKIYET() {
		return MULKIYET;
	}

	@Override
	public void setMULKIYET(IMulkiyet MULKIYET) {
		this.MULKIYET = MULKIYET;
	}
	
	@Override
	public IOkumaMetodu getOKUMA_METODU() {
		return OKUMA_METODU;
	}
	
	@Override
	public IIsemri getIsemri()
	{
		return isemri;
	}
	
	public void setSAYAC_CINSI(ISayacCinsi sAYAC_CINSI)
	{
		this.SAYAC_CINSI = SayacCinsi.fromSayacCinsi(sAYAC_CINSI);
	}
	public void setFAZ_SAYISI(IFazSayisi fAZ_SAYISI)
	{
		this.FAZ_SAYISI = FazSayisi.fromFazSayisi(fAZ_SAYISI);
	}

	@Override
	public void setOKUMA_METODU(IOkumaMetodu OKUMA_METODU) {
		this.OKUMA_METODU = OKUMA_METODU;
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
	public IReadResult getOptikResult()
	{
		return result;
	}
	
	@Override
	public void setOptikResult(IReadResult result)
	{
		this.result = result;
		OKUMA_METODU = (result != null) ? OkumaMetodu.Optik : OkumaMetodu.Manuel;
		
	}
	
	@Override
	public Boolean getYENI_SAYAC()
	{
		return yeniSayac;
	}
	@Override
	public void setYENI_SAYAC(boolean yeniSayac)
	{
		this.yeniSayac = yeniSayac;
	}

	public static final String tableName = "sayac";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),

			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, false),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.INTEGER, field.s_SAHA_ISEMRI_NO, false),
			
			new FieldInfo(field.SAYAC_KODU, DbType.INTEGER, field.s_SAYAC_KODU, false),
			new FieldInfo(field.MARKA, DbType.VARCHAR, field.s_MARKA, false),
			new FieldInfo(field.SAYAC_NO, DbType.INTEGER, field.s_SAYAC_NO, false),
			new FieldInfo(field.SAYAC_CINSI, DbType.VARCHAR, field.s_SAYAC_CINSI, false),
			new FieldInfo(field.HANE_SAYISI, DbType.INTEGER, field.s_HANE_SAYISI, false),
			new FieldInfo(field.GIDEN_DURUM_KODU, DbType.INTEGER, field.s_GIDEN_DURUM_KODU, false),
			new FieldInfo(field.IMAL_YILI, DbType.INTEGER, field.s_IMAL_YILI, false),
			new FieldInfo(field.DAMGA_TARIHI, DbType.DATE, field.s_DAMGA_TARIHI, false),
			new FieldInfo(field.MULKIYET, DbType.INTEGER, field.s_MULKIYET, false),
			new FieldInfo(field.FAZ_SAYISI, DbType.INTEGER, field.s_FAZ_SAYISI, false),
			new FieldInfo(field.AMPERAJ, DbType.INTEGER, field.s_AMPERAJ, false),
			new FieldInfo(field.VOLTAJ, DbType.INTEGER, field.s_VOLTAJ, false),
			new FieldInfo(field.MODEL_KODU, DbType.VARCHAR, field.s_MODEL_KODU, false),
			
			new FieldInfo(field.MUHUR_SERI, DbType.VARCHAR, field.s_MUHUR_SERI, false),
			new FieldInfo(field.MUHUR_NO, DbType.INTEGER, field.s_MUHUR_NO, false),
			
			/*
			new FieldInfo(field.SAYAC_KODU_2, DbType.INTEGER, field.s_SAYAC_KODU_2, false),
			new FieldInfo(field.MARKA_2, DbType.VARCHAR, field.s_MARKA_2, false),
			new FieldInfo(field.SAYAC_NO_2, DbType.INTEGER, field.s_SAYAC_NO_2, false),
			new FieldInfo(field.SAYAC_CINSI_2, DbType.VARCHAR, field.s_SAYAC_CINSI_2, false),
			new FieldInfo(field.HANE_SAYISI_2, DbType.INTEGER, field.s_HANE_SAYISI_2, false),
			new FieldInfo(field.GIDEN_DURUM_KODU_2, DbType.INTEGER, field.s_GIDEN_DURUM_KODU_2, false),
			new FieldInfo(field.IMAL_YILI_2, DbType.INTEGER, field.s_IMAL_YILI_2, false),
			new FieldInfo(field.DAMGA_TARIHI_2, DbType.DATE, field.s_DAMGA_TARIHI_2, false),
			new FieldInfo(field.MULKIYET_2, DbType.INTEGER, field.s_MULKIYET_2, false),
			new FieldInfo(field.FAZ_SAYISI_2, DbType.INTEGER, field.s_FAZ_SAYISI_2, false),
			new FieldInfo(field.AMPERAJ_2, DbType.INTEGER, field.s_AMPERAJ_2, false),
			new FieldInfo(field.VOLTAJ_2, DbType.INTEGER, field.s_VOLTAJ_2, false),
			new FieldInfo(field.MODEL_KODU_2, DbType.VARCHAR, field.s_MODEL_KODU_2, false),
			
			new FieldInfo(field.SAYAC_KODU_3, DbType.INTEGER, field.s_SAYAC_KODU_3, false),
			new FieldInfo(field.MARKA_3, DbType.VARCHAR, field.s_MARKA_3, false),
			new FieldInfo(field.SAYAC_NO_3, DbType.INTEGER, field.s_SAYAC_NO_3, false),
			new FieldInfo(field.SAYAC_CINSI_3, DbType.VARCHAR, field.s_SAYAC_CINSI_3, false),
			new FieldInfo(field.HANE_SAYISI_3, DbType.INTEGER, field.s_HANE_SAYISI_3, false),
			new FieldInfo(field.GIDEN_DURUM_KODU_3, DbType.INTEGER, field.s_GIDEN_DURUM_KODU_3, false),
			new FieldInfo(field.IMAL_YILI_3, DbType.INTEGER, field.s_IMAL_YILI_3, false),
			new FieldInfo(field.DAMGA_TARIHI_3, DbType.DATE, field.s_DAMGA_TARIHI_3, false),
			new FieldInfo(field.MULKIYET_3, DbType.INTEGER, field.s_MULKIYET_3, false),
			new FieldInfo(field.FAZ_SAYISI_3, DbType.INTEGER, field.s_FAZ_SAYISI_3, false),
			new FieldInfo(field.AMPERAJ_3, DbType.INTEGER, field.s_AMPERAJ_3, false),
			new FieldInfo(field.VOLTAJ_3, DbType.INTEGER, field.s_VOLTAJ_3, false),
			new FieldInfo(field.MODEL_KODU_3, DbType.VARCHAR, field.s_MODEL_KODU_3, false),
			*/
			new FieldInfo(field.SON_ENDEKS_1, DbType.REAL, field.s_SON_ENDEKS, false),
			new FieldInfo(field.SON_ENDEKS_2, DbType.REAL, field.s_SON_ENDEKS, false),
			new FieldInfo(field.SON_ENDEKS_3, DbType.REAL, field.s_SON_ENDEKS, false),
			new FieldInfo(field.SON_ENDEKS_4, DbType.REAL, field.s_SON_ENDEKS, false),
			new FieldInfo(field.SON_ENDEKS_5, DbType.REAL, field.s_SON_ENDEKS, false),
			
			
			new FieldInfo(field.OKUMA_METODU, DbType.VARCHAR, field.s_OKUMA_METODU, false),
	
			new FieldInfo(field.RESULT_TYPE, DbType.VARCHAR, field.s_RESULT_TYPE, false),
			new FieldInfo(field.RESULT_CODE, DbType.INTEGER, 0, false),
			new FieldInfo(field.RESULT, DbType.VARCHAR, field.s_RESULT, false),
	
	};
	
	
	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_islem_id", false, field.ISLEM_ID), };
	
	public static final String insertString = DbHelper.PrepareInsertString(takilan_sayac.class);
	public static final String updateString = DbHelper.PrepareUpdateString(takilan_sayac.class, field.ID);

	
	public takilan_sayac() {
	
	}
	
	public takilan_sayac(IIsemri isemri) {
		this();
		this.isemri = isemri;
		setTESISAT_NO(isemri.getTESISAT_NO());
		setSAHA_ISEMRI_NO(isemri.getSAHA_ISEMRI_NO());
	}

	public takilan_sayac(ISayacKodu sAYAC_KODU, ISayacMarka mARKA, int sAYAC_NO, ISayacCinsi sAYAC_CINSI, ISayacHaneSayisi hANE_SAYISI,
			ISdurum gIDEN_DURUM_KODU, int iMAL_YILI, Date dAMGA_TARIHI, IMulkiyet mULKIYET, IFazSayisi fAZ_SAYISI, int aMPERAJ,
			IVoltaj vOLTAJ, String mODEL_KODU, ISeriNo sERI_NO, IOkumaMetodu oKUMA_METODU) throws Exception {
		super();
		setSAYAC_KODU(sAYAC_KODU);
		setMARKA(mARKA);
		setSAYAC_NO(sAYAC_NO);
		setSAYAC_CINSI(sAYAC_CINSI);
		setHANE_SAYISI(hANE_SAYISI);
		setGIDEN_DURUM_KODU(gIDEN_DURUM_KODU);
		setIMAL_YILI(iMAL_YILI);
		setDAMGA_TARIHI(dAMGA_TARIHI);
		setMULKIYET(mULKIYET);
		setFAZ_SAYISI(fAZ_SAYISI);
		setAMPERAJ(aMPERAJ);
		setVOLTAJ(vOLTAJ);
		setMODEL_KODU(mODEL_KODU);
		setSERI_NO(sERI_NO);
		setOKUMA_METODU(oKUMA_METODU);
	}

	private static final String format_s = "S%s%s%0" + field.s_SAYAC_NO + "d%s%s%s%s%s%s%0" + field.s_AMPERAJ + "d%s%"
			+ field.s_MODEL_KODU + "s%s%s";

	public int serialize_s(StringBuilder b) throws Exception {

		b.append('S');
		((ISerialize)SayacKodu.fromSayacKodu(getSAYAC_KODU())).toSerialize(b);
		((ISerialize) getMARKA()).toSerialize(b);
		b.append(String.format("%0" + field.s_SAYAC_NO + "d", getSAYAC_NO()));
		((ISerialize) SayacCinsi.fromSayacCinsi(getSAYAC_CINSI())).toSerialize(b);
		b.append(String.format("%0" + field.s_IMAL_YILI + "d", getIMAL_YILI()));
		((ISerialize) SayacHaneSayisi.fromSayacHaneSayisi(getHANE_SAYISI())).toSerialize(b);
		if(getDAMGA_TARIHI() != null)
			b.append(field.date_formatter.format(getDAMGA_TARIHI()));
		else
			b.append(com.mobit.utility.padLeft("", field.s_DAMGA_TARIHI));
		((ISerialize) MULKIYET).toSerialize(b);
		((ISerialize) FazSayisi.fromFazSayisi(getFAZ_SAYISI())).toSerialize(b);
		b.append(String.format("%0" + field.s_AMPERAJ + "d", getAMPERAJ()));
		((ISerialize)Voltaj.fromVoltaj(getVOLTAJ())).toSerialize(b);
		b.append(String.format("%" + field.s_MODEL_KODU + "s", getMODEL_KODU()));
		((ISerialize) SERI_NO).toSerialize(b);
		((ISerialize) OKUMA_METODU).toSerialize(b);

		//Globals.platform.DebugLog(b.toString());
		
		return 0;
	}

	public static IEndeks [] endeksSirala(IEndeksler endler)
	{
		
		IEndeks [] endeksler = new IEndeks[]{
				new Endeks(EndeksTipi.Gunduz), new Endeks(EndeksTipi.Puant), new Endeks(EndeksTipi.Gece), 
				new Endeks(EndeksTipi.Enduktif), new Endeks(EndeksTipi.Kapasitif)};
		
		for(IEndeks endeks : endler.getEndeksler()){
			if(endeks.getType().equals(EndeksTipi.Gunduz)) endeksler[0] = endeks;
			else if(endeks.getType().equals(EndeksTipi.Puant)) endeksler[1] = endeks;
			else if(endeks.getType().equals(EndeksTipi.Gece)) endeksler[2] = endeks;
			else if(endeks.getType().equals(EndeksTipi.Enduktif)) endeksler[3] = endeks;
			else if(endeks.getType().equals(EndeksTipi.Kapasitif)) endeksler[4] = endeks;
		}
		return endeksler;
	}
	
	public int serialize_e(StringBuilder b) throws Exception {
		b.append("E");
		for(IEndeks endeks : endeksSirala(getENDEKSLER())) utility.toSerialize(b, endeks);
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
	public void get(ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		id = rs.getInt(field.ID);

		masterId = rs.getInt(field.ISLEM_ID);
		
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSAHA_ISEMRI_NO(rs.getInt(field.SAHA_ISEMRI_NO));
		
		setSAYAC_KODU(SayacKodu.fromInteger(rs.getInt(field.SAYAC_KODU)));
		setMARKA(new sayac_marka(rs.getString(field.MARKA)));
		setSAYAC_NO(rs.getInt(field.SAYAC_NO));
		setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI)));
		ISayacHaneSayisi hs = SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI));
		setHANE_SAYISI(hs);
		setGIDEN_DURUM_KODU(new sdurum(rs.getInt(field.GIDEN_DURUM_KODU)));
		setIMAL_YILI(rs.getInt(field.IMAL_YILI));
		setDAMGA_TARIHI(rs.getDate(field.DAMGA_TARIHI));
		setMULKIYET(Mulkiyet.fromInteger(rs.getInt(field.MULKIYET)));
		setFAZ_SAYISI(FazSayisi.fromInteger(rs.getInt(field.FAZ_SAYISI)));
		setAMPERAJ(rs.getInt(field.AMPERAJ));
		setVOLTAJ(Voltaj.fromInteger(rs.getInt(field.VOLTAJ)));
		setMODEL_KODU(rs.getString(field.MODEL_KODU));
		setSERI_NO(new SeriNo(rs, field.MUHUR_SERI, field.MUHUR_NO));

		IEndeksler endeksler = getENDEKSLER();
		endeksler.add(new Endeks(EndeksTipi.Gunduz, rs, field.SON_ENDEKS_1));
		endeksler.add(new Endeks(EndeksTipi.Puant, rs, field.SON_ENDEKS_2));
		endeksler.add(new Endeks(EndeksTipi.Gece, rs, field.SON_ENDEKS_3));
		endeksler.add(new Endeks(EndeksTipi.Enduktif, rs, field.SON_ENDEKS_4));
		endeksler.add(new Endeks(EndeksTipi.Kapasitif, rs, field.SON_ENDEKS_5));
		

		setOKUMA_METODU(OkumaMetodu.fromString(rs.getString(field.OKUMA_METODU)));

		setRESULT_TYPE(rs.getString(field.RESULT_TYPE));
		setRESULT_CODE(rs.getInt(field.RESULT_CODE));
		setRESULT(rs.getString(field.RESULT));
		
	}

	@Override
	public int put(DbHelper h) throws Exception {
		// TODO Auto-generated method stub
		int cnt = 0;
		PreparedStatement stmt;
		int index;
		
		if (getId() == null) {

			stmt = h.getInsStatment(this);

			stmt.setObject(1, getId());
			stmt.setObject(2, masterId);
			
			stmt.setInt(3, getTESISAT_NO());
			stmt.setInt(4, getSAHA_ISEMRI_NO());
			
			if (getSAYAC_KODU() != null)
				stmt.setInt(5, getSAYAC_KODU().getValue());
			if (getMARKA() != null && getMARKA().getSAYAC_MARKA_KODU() != null)
				stmt.setString(6, getMARKA().getSAYAC_MARKA_KODU());
			stmt.setInt(7, getSAYAC_NO());
			if (getSAYAC_CINSI() != null)
				stmt.setString(8, String.valueOf((char) getSAYAC_CINSI().getValue()));
			if (getHANE_SAYISI() != null)
				stmt.setInt(9, getHANE_SAYISI().getValue());
			if (getGIDEN_DURUM_KODU() != null && !getGIDEN_DURUM_KODU().isEmpty())
				stmt.setInt(10, getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
			stmt.setInt(11, getIMAL_YILI());
			if(getDAMGA_TARIHI() != null)
				stmt.setString(12, com.mobit.utility.toISO8601(getDAMGA_TARIHI()));
			if (getMULKIYET() != null)
				stmt.setInt(13, getMULKIYET().getValue());
			if (getFAZ_SAYISI() != null)
				stmt.setInt(14, getFAZ_SAYISI().getValue());
			stmt.setInt(15, getAMPERAJ());
			if (getVOLTAJ() != null)
				stmt.setInt(16, getVOLTAJ().getValue());
			stmt.setString(17, getMODEL_KODU());
			if (getSERI_NO() != null)
				stmt.setString(18, getSERI_NO().getSeri());
			if (getSERI_NO() != null)
				stmt.setInt(19, getSERI_NO().getNo());

			index = 20;
			 
			for(IEndeks endeks : endeksSirala(getENDEKSLER()))
				stmt.setObject(index++, endeks.getValue());
			
			if (getOKUMA_METODU() != null)
				stmt.setString(25, String.valueOf((char) getOKUMA_METODU().getValue()));

			cnt = stmt.executeUpdate();
			if (cnt > 0) id = h.GetId();
			
		}
		 else {

			stmt = h.getUpdStatment(this);

			stmt.setObject(1, masterId);
			
			stmt.setInt(2, getTESISAT_NO());
			stmt.setInt(3, getSAHA_ISEMRI_NO());
			
			if (getSAYAC_KODU() != null)
				stmt.setInt(4, getSAYAC_KODU().getValue());
			if (getMARKA() != null && getMARKA().getSAYAC_MARKA_KODU() != null)
				stmt.setString(5, getMARKA().getSAYAC_MARKA_KODU());
			stmt.setInt(6, getSAYAC_NO());
			if (getSAYAC_CINSI() != null)
				stmt.setString(7, String.valueOf((char) getSAYAC_CINSI().getValue()));
			if (getHANE_SAYISI() != null)
				stmt.setInt(8, getHANE_SAYISI().getValue());
			if (getGIDEN_DURUM_KODU() != null && !getGIDEN_DURUM_KODU().isEmpty())
				stmt.setInt(9, getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
			stmt.setInt(10, getIMAL_YILI());
			stmt.setString(11, com.mobit.utility.toISO8601(getDAMGA_TARIHI()));
			if (getMULKIYET() != null)
				stmt.setInt(12, getMULKIYET().getValue());
			if (getFAZ_SAYISI() != null)
				stmt.setInt(13, getFAZ_SAYISI().getValue());
			stmt.setInt(14, getAMPERAJ());
			if (getVOLTAJ() != null)
				stmt.setInt(15, getVOLTAJ().getValue());
			stmt.setString(16, getMODEL_KODU());
			if (getSERI_NO() != null)
				stmt.setString(17, getSERI_NO().getSeri());
			if (getSERI_NO() != null)
				stmt.setInt(18, getSERI_NO().getNo());

			index = 19;
			for(IEndeks endeks : endeksSirala(getENDEKSLER()))
				stmt.setObject(index++, endeks.getValue());
			
			if (getOKUMA_METODU() != null)
				stmt.setString(24, String.valueOf((char) getOKUMA_METODU().getValue()));

			// where
			stmt.setObject(25, getId());

			cnt = stmt.executeUpdate();

		}

		return cnt;
	}

	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub
		List<String[]> list = new ArrayList<String[]>();

		list.add(new String[] { "Tipi:", getSAYAC_KODU().toString() });
		list.add(new String[] { "Marka:", getMARKA().toString() });
		list.add(new String[] { "No:", Integer.toString(getSAYAC_NO()) });
		list.add(new String[] { "Cinsi:", getSAYAC_CINSI().toString() });
		list.add(new String[] { "Hane Sayısı:", getHANE_SAYISI().toString() });
		list.add(new String[] { "Giden Durum:", getGIDEN_DURUM_KODU().toString() });
		list.add(new String[] { "İmal Yılı:", Integer.toString(getIMAL_YILI()) });

		return list;
	}

	@Override
	public void sayacHaneKontrol(ISayacHaneSayisi h, Callback clb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IEndeksler getENDEKSLER() {
		// TODO Auto-generated method stub
		return ENDEKSLER;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof ISayacBilgi){
			ISayacBilgi sb = (ISayacBilgi)obj;
			return getSAYAC_NO() == sb.getSAYAC_NO();
		}
		else if(obj instanceof Integer){
			Integer sayac_no = (Integer)obj;
			return getSAYAC_NO() == sayac_no;
		} 
		return super.equals(obj);
	}
	
	@Override
	public void Kontrol() throws Exception
	{
		
		super.Kontrol();
		
		if(getYENI_SAYAC() == null)
			throw new MobitException("Sayaç durum(yeni/eski) belirsiz!");
		if(getMULKIYET() == null)
				throw new MobitException("Sayaç mülkiyetini seçin!");
		/*
		if(getSERI_NO() == null || getSERI_NO().isEmpty())
				throw new MobitException("Takılan mühür seri girin!");
		*/
	}

	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<ITakilanSayac> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ITakilanSayac> list = new ArrayList<ITakilanSayac>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				takilan_sayac ts = new takilan_sayac();
				ts.get(rs);
				list.add(ts);
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
