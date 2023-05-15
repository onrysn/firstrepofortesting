package mobit.elec.mbs.put;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.Globals;
import com.mobit.ICamera;
import com.mobit.ICbs;
import com.mobit.IDb;
import com.mobit.IDetail;
import com.mobit.IEnum;
import com.mobit.IFotograf;
import com.mobit.IRecordStatus;
import com.mobit.IResult;
import com.mobit.IndexInfo;
import com.mobit.MobitException;
import com.mobit.RecordStatus;
import mobit.elec.IAciklama;
import mobit.elec.IAdurum;
import mobit.elec.IElecApplication;
import mobit.elec.IEndeks;
import mobit.elec.IEndeksler;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriCevapSil;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriZabit;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayacMarka;
import mobit.elec.ISayaclar;
import mobit.elec.ISdurum;
import mobit.elec.ISeriNo;
import mobit.elec.ISerialize;
import mobit.elec.ISinyalSeviye;
import mobit.elec.ITakilanSayac;
import mobit.elec.MsgInfo;
import mobit.elec.Sayaclar;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacHaneSayisi;
import mobit.elec.mbs.get.Aciklama;
import mobit.elec.mbs.get.Cbs;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.SayacBilgi;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.get.SinyalSeviye;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.get.sdurum;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.Endeks;
import mobit.elec.mbs.IDef;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.enums.SayacCinsi;
import mobit.elec.mbs.enums.SayacKodu;

public class put_isemri extends ICommand implements IDb, IIsemriIslem, IResult, IFotograf, IDetail, IIsemrı {

	protected Integer id = null;
	protected Integer masterId = null;
	IElecApplication app;

	protected int TESISAT_NO;
	protected int SAHA_ISEMRI_NO;
	protected IAdurum[] GELEN_DURUM = new adurum[] { new adurum(), new adurum(), new adurum() };
	protected ISeriNo MUHUR_SERINO = new SeriNo();
	protected ISdurum[] GELEN_DURUM_KODU = new sdurum[] { new sdurum(), new sdurum(), new sdurum(), new sdurum(),
			new sdurum() };

		protected IAciklama OKUMA_ACIKLAMA = new Aciklama();
	protected IIslemTipi ISLEM_TIPI = IslemTipi.Bilgi;

	protected ISinyalSeviye SINYAL_SEVIYESI = new SinyalSeviye();

	protected int ELEMAN_KODU;
	protected String ELEMAN_ADI;
	protected String OPTIK_DATA;

	protected ICbs CBS;
	protected String RESULT_TYPE;
	protected int RESULT_CODE;
	protected String RESULT;

	protected IRecordStatus DURUM = RecordStatus.None;

	protected Date ZAMAN;
	
	boolean tamamlanacak = false;

	// protected IEndeksler ENDEKSLER = new Endeksler();

	protected ISayaclar sayaclar = new Sayaclar();

	public static final int TABLOID = 10;

	@Override
	public int getTabloId() {
		return TABLOID;
	}

	@Override
	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
	}

	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public void setTESISAT_NO(int TESISAT_NO) {
		com.mobit.utility.check(TESISAT_NO, field.s_TESISAT_NO);
		this.TESISAT_NO = TESISAT_NO;
	}

	public int getSAHA_ISEMRI_NO() {
		return SAHA_ISEMRI_NO;
	}

	public void setSAHA_ISEMRI_NO(int SAHA_ISEMRI_NO) {
		com.mobit.utility.check(SAHA_ISEMRI_NO, field.s_SAHA_ISEMRI_NO);
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
	}

	public IAdurum[] getGELEN_DURUM() {
		return GELEN_DURUM;
	}

	@Override
	public void setGELEN_DURUM(IAdurum MASK_GELEN_DURUM, IAdurum GELEN_DURUM) throws Exception {

		boolean set = false;
		int len = this.GELEN_DURUM.length;

		for (int i = 0; i < len; i++) {
			if (this.GELEN_DURUM[i].equals(MASK_GELEN_DURUM)) {
				this.GELEN_DURUM[i] = GELEN_DURUM != null ? GELEN_DURUM : new adurum();
				set = true;
				break;
			}
		}
		if (set == false) {
			for (int i = 0; i < len; i++) {
				if (this.GELEN_DURUM[i].isEmpty()) {
					this.GELEN_DURUM[i] = GELEN_DURUM != null ? GELEN_DURUM : new adurum();
					set = true;
					break;
				}
			}
		}
		if (set == false) {
			throw new MobitException(MsgInfo.MAKSIMUM_ABONE_DURUM_SAYISINA_ULASILDI);
		}

	}

	public ISeriNo getSERINO() {
		return MUHUR_SERINO;
	}

	public void setSERINO(ISeriNo MUHUR_SERINO) {
		this.MUHUR_SERINO = MUHUR_SERINO;
	}

	@Override
	public ISayaclar getSAYACLAR() {
		return sayaclar;
	}

	@Override
	public IIsemri getIsemri() {
		if(isemri == null){
			try {
				isemri = (IIsemri2)isemri_guncelle.IsemriBul(Globals.app.getConnection(), getSAHA_ISEMRI_NO());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isemri;
	}

	

	@Override
	public void setSON_ENDEKS(IEndeksTipi endeksTipi, String endeks) throws Exception {
		ISayacBilgi sb = getSAYACLAR().getSayac(endeksTipi);
		ISayacHaneSayisi haneSayisi = (sb != null) ? sb.getHANE_SAYISI() : SayacHaneSayisi.H6;
		sb.getENDEKSLER().add(new Endeks(endeksTipi, haneSayisi, endeks));


	/*	ISayacBilgi sayac; ISayacHaneSayisi haneSayisi; if
		 (endeksTipi.equals(EndeksTipi.Gunduz)){ sayac =
	 isemri.getSAYACLAR().getSayac(SayacKodu.Aktif); haneSayisi = (sayac != null) ?
		 sayac.getHANE_SAYISI() : SayacHaneSayisi.H6; SON_ENDEKS[0] = new
		 Endeks(endeksTipi, haneSayisi, endeks); } else if
		 (endeksTipi.equals(EndeksTipi.Puant)){ sayac =
				isemri.getSAYACLAR().getSayac(SayacKodu.Aktif); haneSayisi = (sayac != null) ?
		 sayac.getHANE_SAYISI() : SayacHaneSayisi.H6; SON_ENDEKS[1] = new
		 Endeks(endeksTipi, SayacHaneSayisi.H6, endeks); } else if
		 (endeksTipi.equals(EndeksTipi.Gece)){ sayac =
				isemri.getSAYACLAR().getSayac(SayacKodu.Aktif); haneSayisi = (sayac != null) ?
		 sayac.getHANE_SAYISI() : SayacHaneSayisi.H6; SON_ENDEKS[2] = new
		  Endeks(endeksTipi, SayacHaneSayisi.H6, endeks); } else if
		 (endeksTipi.equals(EndeksTipi.Enduktif)){ sayac =
				isemri.getSAYACLAR().getSayac(SayacKodu.Reaktif); haneSayisi = (sayac != null) ?
		 sayac.getHANE_SAYISI() : SayacHaneSayisi.H6; SON_ENDEKS[3] = new
		  Endeks(endeksTipi, SayacHaneSayisi.H6, endeks); } else if
		 (endeksTipi.equals(EndeksTipi.Kapasitif)){ sayac =
				isemri.getSAYACLAR().getSayac(SayacKodu.Kapasitif); haneSayisi = (sayac != null) ?
		 sayac.getHANE_SAYISI() : SayacHaneSayisi.H6; SON_ENDEKS[4] = new
		 Endeks(endeksTipi, SayacHaneSayisi.H6, endeks); } else if
		 (endeksTipi.equals(EndeksTipi.Demand)){ sayac =
				isemri.getSAYACLAR().getSayac(SayacKodu.Aktif); haneSayisi = (sayac != null) ?
		 sayac.getHANE_SAYISI() : SayacHaneSayisi.H6; SON_ENDEKS[5] = new
		 Endeks(endeksTipi, SayacHaneSayisi.H6, endeks); }
		 */
	}

	@Override
	public ISdurum[] getGELEN_DURUM_KODU() {
		return GELEN_DURUM_KODU;
	}

	@Override
	public void setGELEN_DURUM_KODU(ISdurum MASK_GELEN_DURUM_KODU, ISdurum GELEN_DURUM_KODU) throws Exception {
		boolean set = false;
		int len = this.GELEN_DURUM_KODU.length;

		for (int i = 0; i < len; i++) {
			if (this.GELEN_DURUM_KODU[i].equals(MASK_GELEN_DURUM_KODU)) {
				this.GELEN_DURUM_KODU[i] = GELEN_DURUM_KODU != null ? GELEN_DURUM_KODU : new sdurum();
				set = true;
				break;
			}
		}
		if (set == false) {
			for (int i = 0; i < len; i++) {
				if (this.GELEN_DURUM_KODU[i].isEmpty()) {
					this.GELEN_DURUM_KODU[i] = GELEN_DURUM_KODU != null ? GELEN_DURUM_KODU : new sdurum();
					set = true;
					break;
				}
			}
		}
		if (set == false) {
			throw new MobitException(MsgInfo.MAKSIMUM_SAYAC_DURUM_SAYISINA_ULASILDI);
		}
	}

	@Override
	public void SayacDurumKontrol() {

	}

	/*
	 * public IEndeks getCEKILEN_GUC() { return CEKILEN_GUC; }
	 * 
	 * public void setCEKILEN_GUC(IEndeks CEKILEN_GUC) { this.CEKILEN_GUC =
	 * CEKILEN_GUC; }
	 */

	public IAciklama getOKUMA_ACIKLAMA() {
		return OKUMA_ACIKLAMA;
	}

	public void setOKUMA_ACIKLAMA(IAciklama OKUMA_ACIKLAMA) throws Exception {
		if (!OKUMA_ACIKLAMA.isEmpty())
			com.mobit.utility.check(OKUMA_ACIKLAMA.getSTR(), field.s_OKUMA_ACIKLAMA);
		this.OKUMA_ACIKLAMA = OKUMA_ACIKLAMA;
	}

	@Override
	public void AboneDurumKontrol() throws Exception {
		if (!getIsemriTamamlanacak()) {
			boolean b = false;
			for (IAdurum adurum : GELEN_DURUM) {
				if (!adurum.isEmpty()) {
					b = true;
					break;
				}
			}
			// if(!b) throw new MobitException("En az bir durum seçmeniz
			// gerekiyor!");
		}
	}

	public ICbs getCBS() {
		return CBS;
	}

	public void setCBS(ICbs CBS) {
		this.CBS = CBS;
	}

	public void setISLEM_TIPI(IIslemTipi ISLEM_TIPI) {

		this.ISLEM_TIPI = ISLEM_TIPI;
	}

	public void setELEMAN_KODU(int ELEMAN_KODU) {
		this.ELEMAN_KODU = ELEMAN_KODU;
	}

	public void setELEMAN_ADI(String ELEMAN_ADI) {
		this.ELEMAN_ADI = ELEMAN_ADI;
	}

	public void setOPTIK_DATA(Object OPTIK_DATA) {
		this.OPTIK_DATA = (String) OPTIK_DATA;
	}

	public IIslemTipi getISLEM_TIPI() {

		return ISLEM_TIPI;
	}

	public int getELEMAN_KODU() {
		return ELEMAN_KODU;
	}

	public String getELEMAN_ADI() {
		return ELEMAN_ADI;
	}

	@Override
	public String getOPTIK_DATA() {
		return OPTIK_DATA;
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
	public IRecordStatus getDURUM() {
		return DURUM;
	}

	@Override
	public void setDURUM(IRecordStatus DURUM) {
		this.DURUM = DURUM;
	}

	@Override
	public boolean getIsemriTamamlanacak() {
		return tamamlanacak;
	}

	@Override
	public void setIsemriTamamlanacak(boolean tamamlanacak) {
		this.tamamlanacak = tamamlanacak;
	}

	@Override
	public String getFormType() {
		IEnum tip = getISLEM_TIPI();
		if (tip.equals(IslemTipi.Acma))
			return IDef.acma_form;
		else if (tip.equals(IslemTipi.Kesme))
			return IDef.kesme_form;
		else if (tip.equals(IslemTipi.SayacDegistir))
			return IDef.sayac_degistirme_form;
		else if (tip.equals(IslemTipi.Ihbar))
			return IDef.ihbar_form;
		else if (tip.equals(IslemTipi.SayacOkuma))
			return IDef.fatura;
		else if (tip.equals(IslemTipi.Tespit)) {//HÜSEYİN EMRE ÇEVİK
				return IDef.dagitim_form;
		}
		return "";
	}
	
	@Override
	public Date getZAMAN()
	{
		return ZAMAN;
	}
	
	@Override
	public void setZAMAN(Date ZAMAN)
	{
		this.ZAMAN = ZAMAN;
	}

	public put_isemri() {
		setZAMAN(new Date());
	}

	private IIsemri2 isemri;
	private List<IIsemriSoru> sorular;


	public put_isemri(IIsemri2 isemri) throws Exception {

		this.isemri = isemri;
		setISLEM_TIPI(isemri.getISLEM_TIPI());
		setTESISAT_NO(isemri.getTESISAT_NO());
		setSAHA_ISEMRI_NO(isemri.getSAHA_ISEMRI_NO());

		for (ISayacBilgi sb : isemri.getSAYACLAR().getSayaclar())
			getSAYACLAR().add((ISayacBilgi) sb.clone());

		if (isemri.getISLEM_TIPI().equals(IslemTipi.Tespit)) {
			sorular = isemri.getIsemriSorular();
		}

}

	@Override
	public String toString()
	{
		return String.format("İşlem : %s\nTesisat No: %d", getISLEM_TIPI(), getTESISAT_NO());
	}

	/*
	 * public put_isemri(Cbs cBS, int tESISAT_NO, int sAHA_ISEMRI_NO, adurum[]
	 * gELEN_DURUM, SeriNo mUHUR_SERINO, Endeks[] sON_ENDEKS, sdurum[]
	 * gELEN_DURUM_KODU, Aciklama oKUMA_ACIKLAMA, IEnum ISLEM_TIPI, int
	 * ELEMAN_KODU, String ELEMAN_ADI, String OPTIK_DATA) throws Exception {
	 * super(); setCBS(cBS); setTESISAT_NO(tESISAT_NO);
	 * setSAHA_ISEMRI_NO(sAHA_ISEMRI_NO);
	 * 
	 * setSERINO(mUHUR_SERINO); setSON_ENDEKS(sON_ENDEKS);
	 * setGELEN_DURUM_KODU(gELEN_DURUM_KODU); setOKUMA_ACIKLAMA(oKUMA_ACIKLAMA);
	 * setISLEM_TIPI(ISLEM_TIPI); setELEMAN_KODU(ELEMAN_KODU);
	 * setELEMAN_ADI(ELEMAN_ADI); setOPTIK_DATA(OPTIK_DATA); }
	 */

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT ISEMRI " + "%0" + field.s_TESISAT_NO + "d" + "%0" + field.s_SAHA_ISEMRI_NO
			+ "d";

	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		if (CBS != null){
			Cbs cbs = new Cbs(CBS);
			if(!cbs.isEmpty()) cbs.toSerialize(b);
		}
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO, SAHA_ISEMRI_NO));

		for (int i = 0; i < GELEN_DURUM.length; i++)
			((ISerialize) GELEN_DURUM[i]).toSerialize(b);
		((ISerialize) MUHUR_SERINO).toSerialize(b);

		IEndeks[] SON_ENDEKS = endeksSirala(sayaclar.getEndeksler());

		for (int i = 0; i < SON_ENDEKS.length; i++) {
			((ISerialize) SON_ENDEKS[i]).toSerialize(b);
			if (i < GELEN_DURUM_KODU.length)
				((ISerialize) GELEN_DURUM_KODU[i]).toSerialize(b);
		}

		if (OKUMA_ACIKLAMA != null) {
			ISerialize ser = (OKUMA_ACIKLAMA instanceof ISerialize) ? (ISerialize) OKUMA_ACIKLAMA
					: new Aciklama(OKUMA_ACIKLAMA.getSTR());
			ser.toSerialize(b);
		}
		b.append('\n');
	
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "isemri_islem";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.ISLEM_ID, DbType.INTEGER, 0, false),

			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, true),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.INTEGER, field.s_SAHA_ISEMRI_NO, true),
			new FieldInfo(field.GELEN_DURUM_1, DbType.INTEGER, field.s_GELEN_DURUM_1, true),
			new FieldInfo(field.GELEN_DURUM_2, DbType.INTEGER, field.s_GELEN_DURUM_2, true),
			new FieldInfo(field.GELEN_DURUM_3, DbType.INTEGER, field.s_GELEN_DURUM_3, true),
			new FieldInfo(field.MUHUR_SERI, DbType.VARCHAR, field.s_MUHUR_SERI, true),
			new FieldInfo(field.MUHUR_NO, DbType.INTEGER, field.s_MUHUR_NO, true),
			new FieldInfo(field.SON_ENDEKS_1, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_2, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_3, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_4, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_5, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.GELEN_DURUM_KODU_1, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_2, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_3, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_4, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_5, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.CEKILEN_GUC, DbType.REAL, field.s_CEKILEN_GUC, true),
			new FieldInfo(field.OKUMA_ACIKLAMA, DbType.VARCHAR, field.s_OKUMA_ACIKLAMA, true),
			new FieldInfo(field.ISLEM_TIPI, DbType.INTEGER, field.s_ISLEM_TIPI, true),
			new FieldInfo(field.CBS_X, DbType.REAL, field.s_CBS_X, true),
			new FieldInfo(field.CBS_Y, DbType.REAL, field.s_CBS_Y, true),
			new FieldInfo(field.ELEMAN_KODU, DbType.INTEGER, field.s_ELEMAN_KODU, true),
			new FieldInfo(field.ELEMAN_ADI, DbType.VARCHAR, field.s_ELEMAN_ADI, true),
			new FieldInfo(field.OPTIK_DATA, DbType.VARCHAR, field.s_OPTIK_DATA, true),

			new FieldInfo(field.RESULT_TYPE, DbType.VARCHAR, field.s_RESULT_TYPE, false),
			new FieldInfo(field.RESULT_CODE, DbType.INTEGER, 0, false),
			new FieldInfo(field.RESULT, DbType.VARCHAR, field.s_RESULT, false),
			new FieldInfo(field.DURUM, DbType.INTEGER, 0, false),

			new FieldInfo(field.SINYAL_SEVIYESI, DbType.VARCHAR, field.s_SINYAL_SEVIYESI, false),
			
			new FieldInfo(field.ZAMAN, DbType.DATETIME, 0, false),

	};

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_isemri_no", false, field.SAHA_ISEMRI_NO),
			new IndexInfo(tableName+"_tesisat_no", false, field.TESISAT_NO),
			new IndexInfo(tableName+"_islem_id", false, field.ISLEM_ID), };

	public static final String insertString = DbHelper.PrepareInsertString(put_isemri.class);
	public static final String updateString = DbHelper.PrepareUpdateString(put_isemri.class, field.ID);

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

		setMasterId(rs.getInt(field.ISLEM_ID));

		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSAHA_ISEMRI_NO(rs.getInt(field.SAHA_ISEMRI_NO));

		IAdurum durum;
		durum = new adurum(rs, field.GELEN_DURUM_1);
		setGELEN_DURUM(durum, durum);
		durum = new adurum(rs, field.GELEN_DURUM_2);
		setGELEN_DURUM(durum, durum);
		durum = new adurum(rs, field.GELEN_DURUM_3);
		setGELEN_DURUM(durum, durum);

		setSERINO(new SeriNo(rs, field.MUHUR_SERI, field.MUHUR_NO));

		ISayaclar sayaclar = getSAYACLAR();
		
		SayacBilgi sayac = new SayacBilgi();
		sayac.setSAYAC_KODU(SayacKodu.Kombi);
		sayac.setSAYAC_CINSI(SayacCinsi.Elektronik);
		sayac.setHANE_SAYISI(SayacHaneSayisi.H6);
		sayaclar.add(sayac);
		
		sayaclar.addEndeks(new Endeks(EndeksTipi.Gunduz, rs, field.SON_ENDEKS_1));
		sayaclar.addEndeks(new Endeks(EndeksTipi.Puant, rs, field.SON_ENDEKS_2));
		sayaclar.addEndeks(new Endeks(EndeksTipi.Gece, rs, field.SON_ENDEKS_3));
		sayaclar.addEndeks(new Endeks(EndeksTipi.Enduktif, rs, field.SON_ENDEKS_4));
		sayaclar.addEndeks(new Endeks(EndeksTipi.Kapasitif, rs, field.SON_ENDEKS_5));

		ISdurum sdurum;
		sdurum = new sdurum(rs, field.GELEN_DURUM_KODU_1);
		setGELEN_DURUM_KODU(sdurum, sdurum);
		sdurum = new sdurum(rs, field.GELEN_DURUM_KODU_2);
		setGELEN_DURUM_KODU(sdurum, sdurum);
		sdurum = new sdurum(rs, field.GELEN_DURUM_KODU_3);
		setGELEN_DURUM_KODU(sdurum, sdurum);
		sdurum = new sdurum(rs, field.GELEN_DURUM_KODU_4);
		setGELEN_DURUM_KODU(sdurum, sdurum);
		sdurum = new sdurum(rs, field.GELEN_DURUM_KODU_5);
		setGELEN_DURUM_KODU(sdurum, sdurum);

		sayaclar.addEndeks(new Endeks(EndeksTipi.Demand, rs, field.CEKILEN_GUC));

		setOKUMA_ACIKLAMA(new Aciklama(rs, field.OKUMA_ACIKLAMA));

		setISLEM_TIPI(IslemTipi.fromInteger(rs.getInt(field.ISLEM_TIPI)));
		setCBS(new Cbs(rs, field.CBS_X, field.CBS_Y));

		setELEMAN_KODU(rs.getInt(field.ELEMAN_KODU));
		setELEMAN_ADI(rs.getString(field.ELEMAN_ADI));
		setOPTIK_DATA(rs.getString(field.OPTIK_DATA));

		setRESULT_TYPE(rs.getString(field.RESULT_TYPE));
		setRESULT_CODE(rs.getInt(field.RESULT_CODE));
		setRESULT(rs.getString(field.RESULT));

		setDURUM(RecordStatus.fromInteger(rs.getInt(field.DURUM)));

		setSINYAL_SEVIYESI(SinyalSeviye.fromInteger(rs.getInt(field.SINYAL_SEVIYESI)));
		
		setZAMAN(rs.getDate(field.ZAMAN));

	}

	@Override
	public void setSINYAL_SEVIYESI(ISinyalSeviye SINYAL_SEVIYESI) {
		this.SINYAL_SEVIYESI = SINYAL_SEVIYESI;
	}

	@Override
	public ISinyalSeviye getSINYAL_SEVIYESI() {
		return SINYAL_SEVIYESI;
	}

	@Override
	public int put(DbHelper h) throws Exception {
		// TODO Auto-generated method stub

		int cnt = 0;
		PreparedStatement stmt;
		IAdurum[] durum;
		ISeriNo serino;
		ISdurum[] sd;

		if (getId() == null) {

			stmt = h.getInsStatment(this);

			stmt.setObject(1, getId());
			stmt.setInt(2, masterId);
			stmt.setInt(3, getTESISAT_NO());
			stmt.setInt(4, getSAHA_ISEMRI_NO());

			durum = getGELEN_DURUM();
			if (durum != null) {
				if (durum.length > 0 && durum[0] != null && !durum[0].isEmpty())
					stmt.setInt(5, durum[0].getABONE_DURUM_KODU());
				if (durum.length > 1 && durum[1] != null && !durum[1].isEmpty())
					stmt.setInt(6, durum[1].getABONE_DURUM_KODU());
				if (durum.length > 2 && durum[2] != null && !durum[2].isEmpty())
					stmt.setInt(7, durum[2].getABONE_DURUM_KODU());
			}
			serino = getSERINO();
			if (serino != null) {
				stmt.setObject(8, serino.getSeri());
				stmt.setInt(9, serino.getNo());
			}

			IEndeksler endeksler = getSAYACLAR().getEndeksler();
			stmt.setObject(10, endeksler.getEndeks(EndeksTipi.Gunduz).getValue());
			stmt.setObject(11, endeksler.getEndeks(EndeksTipi.Puant).getValue());
			stmt.setObject(12, endeksler.getEndeks(EndeksTipi.Gece).getValue());
			stmt.setObject(13, endeksler.getEndeks(EndeksTipi.Enduktif).getValue());
			stmt.setObject(14, endeksler.getEndeks(EndeksTipi.Kapasitif).getValue());


			sd = getGELEN_DURUM_KODU();
			if (sd != null) {
				if (sd.length > 0 && sd[0] != null && !sd[0].isEmpty())
					stmt.setInt(15, sd[0].getSAYAC_DURUM_KODU());
				if (sd.length > 1 && sd[1] != null && !sd[1].isEmpty())
					stmt.setInt(16, sd[1].getSAYAC_DURUM_KODU());
				if (sd.length > 2 && sd[2] != null && !sd[2].isEmpty())
					stmt.setInt(17, sd[2].getSAYAC_DURUM_KODU());
				if (sd.length > 3 && sd[3] != null && !sd[3].isEmpty())
					stmt.setInt(18, sd[3].getSAYAC_DURUM_KODU());
				if (sd.length > 4 && sd[4] != null && !sd[4].isEmpty())
					stmt.setInt(19, sd[4].getSAYAC_DURUM_KODU());
			}

			stmt.setObject(20, endeksler.getEndeks(EndeksTipi.Demand).getValue());

			stmt.setObject(21, getOKUMA_ACIKLAMA().getSTR());

			stmt.setObject(22, getISLEM_TIPI().getValue());

			if (getCBS() != null) {
				stmt.setObject(23, getCBS().getX());
				stmt.setObject(24, getCBS().getY());
			}

			stmt.setInt(25, getELEMAN_KODU());
			stmt.setObject(26, getELEMAN_ADI());
			stmt.setObject(27, getOPTIK_DATA());

			stmt.setObject(28, getRESULT_TYPE());
			stmt.setInt(29, getRESULT_CODE());
			stmt.setObject(30, getRESULT());

			stmt.setInt(31, getDURUM().getValue());

			stmt.setInt(32, getSINYAL_SEVIYESI().getSEVIYE());
			
			stmt.setString(33, com.mobit.utility.toISO8601(getZAMAN()));

			cnt = stmt.executeUpdate();
		}

		if (cnt > 0) {
			id = h.GetId();
		} else {

			stmt = h.getUpdStatment(this);

			stmt.setInt(1, masterId);
			stmt.setInt(2, getTESISAT_NO());
			stmt.setInt(3, getSAHA_ISEMRI_NO());

			durum = getGELEN_DURUM();
			if (durum != null) {
				if (durum.length > 0 && durum[0] != null && !durum[0].isEmpty())
					stmt.setInt(4, durum[0].getABONE_DURUM_KODU());
				if (durum.length > 1 && durum[1] != null && !durum[1].isEmpty())
					stmt.setInt(5, durum[1].getABONE_DURUM_KODU());
				if (durum.length > 2 && durum[2] != null && !durum[2].isEmpty())
					stmt.setInt(6, durum[2].getABONE_DURUM_KODU());
			}
			serino = getSERINO();
			if (serino != null && !serino.isEmpty()) {
				stmt.setString(7, serino.getSeri());
				stmt.setInt(8, serino.getNo());
			}

			IEndeksler endeksler = getSAYACLAR().getEndeksler();
			stmt.setObject(9, endeksler.getEndeks(EndeksTipi.Gunduz).getValue());
			stmt.setObject(10, endeksler.getEndeks(EndeksTipi.Puant).getValue());
			stmt.setObject(11, endeksler.getEndeks(EndeksTipi.Gece).getValue());
			stmt.setObject(12, endeksler.getEndeks(EndeksTipi.Enduktif).getValue());
			stmt.setObject(13, endeksler.getEndeks(EndeksTipi.Kapasitif).getValue());

			sd = getGELEN_DURUM_KODU();
			if (sd != null) {
				if (sd.length > 0 && sd[0] != null && !sd[0].isEmpty())
					stmt.setInt(14, sd[0].getSAYAC_DURUM_KODU());
				if (sd.length > 1 && sd[1] != null && !sd[1].isEmpty())
					stmt.setInt(15, sd[1].getSAYAC_DURUM_KODU());
				if (sd.length > 2 && sd[2] != null && !sd[2].isEmpty())
					stmt.setInt(16, sd[2].getSAYAC_DURUM_KODU());
				if (sd.length > 3 && sd[3] != null && !sd[3].isEmpty())
					stmt.setInt(17, sd[3].getSAYAC_DURUM_KODU());
				if (sd.length > 4 && sd[4] != null && !sd[4].isEmpty())
					stmt.setInt(18, sd[4].getSAYAC_DURUM_KODU());
			}

			stmt.setObject(19, endeksler.getEndeks(EndeksTipi.Demand).getValue());

			stmt.setObject(20, getOKUMA_ACIKLAMA().getSTR());

			if (getISLEM_TIPI() != null)
				stmt.setInt(21, getISLEM_TIPI().getValue());

			if (getCBS() != null) {
				stmt.setObject(22, getCBS().getX());
				stmt.setObject(23, getCBS().getY());
			}

			stmt.setInt(24, getELEMAN_KODU());
			stmt.setObject(25, getELEMAN_ADI());
			stmt.setObject(26, getOPTIK_DATA());

			stmt.setObject(27, getRESULT_TYPE());
			stmt.setInt(28, getRESULT_CODE());
			stmt.setObject(29, getRESULT());

			stmt.setInt(30, getDURUM().getValue());

			stmt.setInt(31, getSINYAL_SEVIYESI().getSEVIYE());
			
			stmt.setString(32, com.mobit.utility.toISO8601(getZAMAN()));

			// where
			stmt.setObject(33, getId());

			cnt = stmt.executeUpdate();
		}

		return cnt;
	}

	// -------------------------------------------------------------------------

	public static List<IIsemriIslem> select(Connection conn, int SAHA_ISEMRI_NO) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriIslem> list = new ArrayList<IIsemriIslem>();
		try {
			stmt = conn.prepareStatement(String.format("select * from %s where SAHA_ISEMRI_NO=?", tableName));
			stmt.setInt(1, SAHA_ISEMRI_NO);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_isemri pi = new put_isemri();
				pi.get(rs);
				list.add(pi);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	public static List<IIsemriIslem> select(Connection conn, RecordStatus durum) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriIslem> list = new ArrayList<IIsemriIslem>();
		try {
			stmt = conn.prepareStatement(
					String.format("select * from %s where DURUM<=? order by ID desc LIMIT 1", tableName));
			stmt.setInt(1, durum.getValue());
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_isemri pi = new put_isemri();
				pi.get(rs);
				list.add(pi);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	public static List<IIsemriIslem> selectPrint(Connection conn, int SAHA_ISEMRI_NO) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriIslem> list = new ArrayList<IIsemriIslem>();
		try {

			stmt = conn.prepareStatement(String.format(
					"select * from %s " + "where SAHA_ISEMRI_NO=? and RESULT_TYPE='%s' order by ID desc LIMIT 1",
					tableName, IDef.PRN));

			stmt.setInt(1, SAHA_ISEMRI_NO);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_isemri pi = new put_isemri();
				pi.get(rs);
				list.add(pi);
			}

		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}
	
	private static final String query1 = String.format("select * from %s where %s=? order by %s", tableName, field.ISLEM_ID, field.ID);
	public static List<IIsemriIslem> selectIslem(Connection conn, int masterId) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemriIslem> list = new ArrayList<IIsemriIslem>();
		try {

			stmt = conn.prepareStatement(query1);

			stmt.setObject(1, masterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				put_isemri pi;
				IIslemTipi islemTipi = IslemTipi.fromInteger(rs.getInt(field.ISLEM_TIPI));
				if(islemTipi.equals(IslemTipi.SayacOkuma)) {
					pi = new put_endeks();
				}
				else {
					pi = new put_isemri();
				}
				pi.get(rs);
				list.add(pi);
			}

		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}
	

	public void updateDurum(Connection conn) throws Exception {

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("update %s set %s=? where ID=?", tableName, field.DURUM));
			stmt.setInt(1, getDURUM().getValue());
			stmt.setInt(2, getId());
			stmt.execute();

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	@Override
	public void IslemKontrol(IElecApplication app) throws Exception {
		
		AboneDurumKontrol();
		SayacDurumKontrol();

		// Endeks kontrolü
		if (app.Kontrol(1, this))
			getSAYACLAR().EndeksKontrol();

		// Mühür seri/no kontrolü
		if (app.Kontrol(2, this)) {
			if (!Globals.isDeveloping() && getSERINO().isEmpty())
				throw new MobitException(MsgInfo.MUHUR_SERINO_GIRIN);
		}

	}

	protected void endeksKontrol() throws Exception {

		/*
		 * ISayacBilgi sayac; sayac =
		 * isemri.getSAYACLAR().getSayac(SayacKodu.Aktif); if(sayac != null){
		 * if(sayac.getSAYAC_CINSI().is(SayacCinsi.Elektronik)){
		 * if(getSON_ENDEKS(EndeksTipi.Gunduz).isEmpty()) throw new
		 * MobitException(MsgInfo.T1_ENDEKSI_GIRIN);
		 * if(getSON_ENDEKS(EndeksTipi.Puant).isEmpty()) throw new
		 * MobitException(MsgInfo.T2_ENDEKSI_GIRIN);
		 * if(getSON_ENDEKS(EndeksTipi.Gece).isEmpty()) throw new
		 * MobitException(MsgInfo.T3_ENDEKSI_GIRIN); } else {
		 * if(getSON_ENDEKS(EndeksTipi.Gunduz).isEmpty()) throw new
		 * MobitException(MsgInfo.T_ENDEKSI_GIRIN); } } sayac =
		 * isemri.getSAYACLAR().getSayac(SayacKodu.Reaktif); if(sayac != null &&
		 * getSON_ENDEKS(EndeksTipi.Enduktif).isEmpty()) throw new
		 * MobitException(MsgInfo.ENDUKTIF_ENDEKSI_GIRIN); sayac =
		 * isemri.getSAYACLAR().getSayac(SayacKodu.Kapasitif); if(sayac != null
		 * && getSON_ENDEKS(EndeksTipi.Kapasitif).isEmpty()) throw new
		 * MobitException(MsgInfo.KAPASITIF_ENDEKSI_GIRIN);
		 * 
		 * if (isemri.getCIFT_TERIM_DUR() &&
		 * getSON_ENDEKS(EndeksTipi.Demand).isEmpty()) throw new
		 * MobitException(MsgInfo.DEMAND_GIRIN);
		 */
	}

	@Override
	public File getFotoDosyaAdi() {
		// TODO Auto-generated method stub
		String file = String.format(Globals.getLocale(), "%010d_%s.jpg", getTESISAT_NO(),
				ICamera.fmt.format(Globals.app.getTime()));
		return new File(Globals.app.getAppPicturePath(), file);
	}

	@Override
	public void FotoKontrol() throws Exception {

	}

	@Override
	public void addFoto(File foto) {

	}

	@Override
	public void removeFoto(File foto) {

	}

	
	protected static IEndeks[] endeksSirala(IEndeksler endler) throws Exception {

		IEndeks[] endeksler = new IEndeks[6];

		for (int i = 0; i < endeksler.length; i++) {
			IEndeks endeks = null;
			String typeControl = null; //H.Elif A.D. durum kodu açıklama geliştirmesi
			switch (i) {
				case 0:
					endeks = endler.getEndeks(EndeksTipi.Gunduz);
					typeControl = endeks.getType().toString(); //H.Elif A.D. durum kodu açıklama geliştirmesi
					endeks = (endeks != null && typeControl != null) ? new Endeks(endeks) : new Endeks(EndeksTipi.Gunduz);
					break;
				case 1:
					endeks = endler.getEndeks(EndeksTipi.Puant);
					typeControl = endeks.getType().toString();
					endeks = (endeks != null && typeControl != null) ? new Endeks(endeks) : new Endeks(EndeksTipi.Puant);
					break;
				case 2:
					endeks = endler.getEndeks(EndeksTipi.Gece);
					typeControl = endeks.getType().toString();
					endeks = (endeks != null && typeControl != null) ? new Endeks(endeks) : new Endeks(EndeksTipi.Gece);
					break;
				case 3:
					endeks = endler.getEndeks(EndeksTipi.Enduktif);
					typeControl = endeks.getType().toString();
					endeks = (endeks != null && typeControl != null) ? new Endeks(endeks) : new Endeks(EndeksTipi.Enduktif);
					break;
				case 4:
					endeks = endler.getEndeks(EndeksTipi.Kapasitif);
					typeControl = endeks.getType().toString();
					endeks = (endeks != null && typeControl != null) ? new Endeks(endeks) : new Endeks(EndeksTipi.Kapasitif);
					break;
				case 5:
					endeks = endler.getEndeks(EndeksTipi.Demand);
					typeControl = endeks.getType().toString();
					endeks = (endeks != null && typeControl != null) ? new Endeks(endeks) : new Endeks(EndeksTipi.Demand);
					break;
			}
			endeksler[i] = endeks;
		}

		return endeksler;
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
		/*
		 * protected String RESULT_TYPE; protected int RESULT_CODE; protected
		 * String RESULT;
		 */
		list.add(new String[] { "", "" });

		return list;
	}
	
	
}
