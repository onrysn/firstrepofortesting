package mobit.elec.mbs.get;

import java.awt.geom.Arc2D;
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
import com.mobit.ICbs;
import com.mobit.IDb;
import com.mobit.IDetail;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.IMapMarker;
import com.mobit.IndexInfo;
import com.mobit.IslemGrup;
import com.mobit.IslemMaster;
import com.mobit.MobitException;
import com.mobit.RecordStatus;

import mobit.elec.AltEmirTuru;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriRapor;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriZabit;
import mobit.elec.IIslemRapor;
import mobit.elec.IKarne;
import mobit.elec.IMuhurSokme;
import mobit.elec.ISayacIslem;
import mobit.elec.ISayaclar;
import mobit.elec.ITakilanSayac;
import mobit.elec.ITesisat;
import mobit.elec.ITesisatMuhur;
import mobit.elec.INavigate;
import mobit.elec.ISayacBilgi;
import mobit.elec.MsgInfo;
import mobit.elec.Sayaclar;
import mobit.elec.enums.IIslemDurum;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemDurum;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IMbsApplication;
import mobit.elec.mbs.IsemriRapor;
import mobit.elec.mbs.IslemRapor;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.put.put_endeks;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_isemri_zabit;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.put.put_tesisat_muhur;

public class isemri_guncelle implements IDb, IIsemri2, IDeserialize, IDetail, INavigate, IMapMarker {

	protected Integer id = null;

	protected int ZAMAN_KODU; /*
	 * CHAR(8) Saniye cinsinden zaman kodu hex
	 * 32-bit
	 */

	protected char HAREKET_KODU; /* CHAR(1) A: İlave Kayıt, U: Güncelleme */
	protected int SAHA_ISEMRI_NO;/* NUM 9(8) Saha işemri Numarası */
	protected IIslemTipi ISLEM_TIPI; /*
	 * NUM 9 0. Bilgi, 1. Kesme, 2. Açma, 3.
	 * İhbar, 4. Kontrol, 5.Sayac Değiştir,
	 * 6.Sayaç Tak
	 */
	protected Date ISEMRI_TARIHI; /* NUM 9(8) YYYYAAGG Formatında tarih */
	protected IIslemDurum ISEMRI_DURUMU; /*
	 * NUM 9 0. Serbest, 1.Atanmış,
	 * 2.İptal, 3.Tamamlandı,
	 * 4.Yapılamadı, 5.Yapılmadı
	 */
	protected int ATANMIS_GOREVLI;/* NUM 9999 /* Atandığı Görevli */
	protected Date ISLEM_TARIHI; /*
	 * NUM 9(8) YYYYAAGG Formatında tamamlandığı
	 * tarih
	 */
	protected int KARNE_NO; /* NUM 9(7) Karne No */
	protected int TESISAT_NO; /* NUM 9(8) Tesisat No */
	/* ------- U modunda iken burada kayıt kesilecek ---- */
	protected int SIRA_NO; /* NUM 9999 Sıra No */
	protected int SIRA_EK; /* NUM 999 Sıra No Ek */
	protected String UNVAN; /* CHAR(70) Abone Adı veya şirket Adı */
	protected String ADRES; /* CHAR(70) Abone Adresi */
	protected String ADRES_TARIF; /* CHAR(70) Okuma elemanına adres notları */
	protected String SEMT; /* CHAR(20) Semt Adı */
	protected boolean CIFT_TERIM_DUR;/* NUM 9 çift terim abone ise 1 gelecek */
	protected boolean PUANT_DUR; /* NUM 9 Puant abone ise 1 gelecek */
	protected int GIDEN_DURUM_KODU;/* NUM 99 Giden Durum Kodu */
	protected int TARIFE_KODU; /* NUM 999 Tüketim Tarife Kodu */
	protected double CARPAN; /* NUM 9999.9999 çarpan Nokta dahil 9 hane */
	protected Date SON_OKUMA_TARIHI;/* NUM 99999999 Son Okuma Tarihi YYYYAAGG */
	protected String ANET_ISLETME_KODU;/* CHAR(11) 11 HANE işletme kodu */
	protected String ANET_ABONE_NO; /* CHAR(11) 11 HANE abone no */
	protected String KESME_DUR; /* CHAR(3) Kesik Durumu */

	protected int BORC_ADET; /* NUM 999 */
	protected double BORC_TUTARI; /* NUM 9(12).99 */
	protected double BORC_GECIKME; /* NUM 9(12).99 */

	protected int ALT_ISLEM_TIPI;/*
	 * NUM 99 Alt işlem kodu, sayaç değişiklikte
	 * 7-tümü,1=aktif,2=reaktif,4=kapasitif
	 */
	protected String TELEFON; /* NUM 9(10) VER1112 */
	protected String TELEFON2; /* NUM 9(10) VER1112 */
	protected String CEP_TELEFON; /* NUM 9(10) VER1112 */
	protected String ISEMRI_ACIKLAMA;/*
	 * CHAR(100) işemri ile ilgili açıklama
	 * VER1112
	 */
	protected int ALT_EMIR_TURU; /* Alt iş emri türü VER1113 */

	protected ISayaclar SAYACLAR = new Sayaclar();

	protected String SOZLESME_DUR;
	protected boolean KONTROL_SAYAC_DUR;
	protected ISayaclar KSAYACLAR = new Sayaclar();


	protected ICbs CBS;

	/*protected int OG_DUR;
	protected int OLCUM_KODU;
	protected int HESAP_KODU;
	protected int SOZLESME_GUCU;
	protected int KURULU_GUC;
	protected char ORTAK_TRAFO_DURUMU;*/


	protected boolean checked = false;

	protected float distance = -1;

	protected boolean mUpdate = false;

	//Onur
	//protected int BINA_KODU; /* NUM 9999999999 VER1122 */
	//protected int SOZLESME_NO; /* NUM 99999999 VER1123 */

	//H.Elif
	protected int OG_DUR;	 /* NUM 9 OG durumu VER1121 */
	protected int OLCUM_KODU; 		 /*NUM 9  OLCUM_KODU VER1121 */
	protected int HESAP_KODU; 		/* NUM 99 iki hane hesap-abone grubu VER1121*/
	protected int SOZLESME_GUCU;    /* NUM 99999999 W cinsinden Sözleşme gücü VER1121 */
	protected int KURULU_GUC;    /* NUM 99999999 W cinsinden Kurulu güç VER1121 */
	protected String ORTAK_TRAFO_DURUMU;		 /* CHAR(1) E veya boşluk VER1121 */

	@Override
	public String toString() {

		return UNVAN;
	}

	public int deserialize(ICommand cmd, String row) throws Exception {

		if (row.length() == field.s_ZAMAN_KODU) {
			int ZAMAN_KODU = Integer.parseInt(row, 16);
			cmd.setObj(ZAMAN_KODU);// row.substring(0, 8);
			return 1;
		}
		String[] list = utility.parser(row, info);
		return deserialize(cmd, list);
	}

	public int deserialize(ICommand cmd, String[] list) throws Exception {

		setZAMAN_KODU((Integer) cmd.getObj());

		setHAREKET_KODU(list[0].charAt(0));
		setSAHA_ISEMRI_NO(utility.MBS_FORMAT.parse(list[1]).intValue());
		setISLEM_TIPI(IslemTipi.fromInteger(utility.MBS_FORMAT.parse(list[2]).intValue()));
		setISEMRI_TARIHI(field.date_formatter.parse(list[3]));
		IslemDurum durum = IslemDurum.fromInteger(utility.MBS_FORMAT.parse(list[4]).intValue());
		if(durum.equals(IslemDurum.Serbest)) durum = IslemDurum.Atanmis;
		setISEMRI_DURUMU(durum);
		setATANMIS_GOREVLI(utility.MBS_FORMAT.parse(list[5]).intValue());
		setISLEM_TARIHI(field.date_formatter.parse(list[6]));
		setKARNE_NO(utility.MBS_FORMAT.parse(list[7]).intValue());
		setTESISAT_NO(utility.MBS_FORMAT.parse(list[8]).intValue());
		/* ------- U modunda iken burada kayıt kesilecek ---- */
		if (HAREKET_KODU == 'U') {
			mUpdate = true;
			return 0;
		}
		setSIRA_NO(utility.MBS_FORMAT.parse(list[9]).intValue());
		setSIRA_EK(utility.MBS_FORMAT.parse(list[10]).intValue());
		setUNVAN(list[11]);
		setADRES(list[12]);
		setADRES_TARIF(list[13]);
		setSEMT(list[14]);

		setCIFT_TERIM_DUR(utility.MBS_FORMAT.parse(list[15]).intValue() != 0);
		setPUANT_DUR(utility.MBS_FORMAT.parse(list[16]).intValue() != 0);

		setGIDEN_DURUM_KODU(utility.MBS_FORMAT.parse(list[17]).intValue());
		setTARIFE_KODU(utility.MBS_FORMAT.parse(list[18]).intValue());

		setCARPAN(utility.MBS_FORMAT.parse(list[19]).doubleValue());
		setSON_OKUMA_TARIHI(field.date_formatter.parse(list[20]));

		setANET_ISLETME_KODU(list[21]);
		setANET_ABONE_NO(list[22]);

		setKESME_DUR(list[23]);

		setBORC_ADET(utility.MBS_FORMAT.parse(list[24]).intValue());
		setBORC_TUTARI(utility.MBS_FORMAT.parse(list[25]).doubleValue());
		setBORC_GECIKME(utility.MBS_FORMAT.parse(list[26]).doubleValue());

		setALT_ISLEM_TIPI(utility.MBS_FORMAT.parse(list[27]).intValue());
		//Samet abi kapattırdı.
		setTELEFON("");//list[28]
		setTELEFON2("");//list[29]
		setCEP_TELEFON("");//list[30]
		setISEMRI_ACIKLAMA(list[31]);

		setALT_EMIR_TURU(utility.MBS_FORMAT.parse(list[32]).intValue());

		setCBS(Cbs.fromString(list[33], list[34]));

		//Onur
		//setBINA_KODU(utility.MBS_FORMAT.parse(list[35]).intValue());
		//setSOZLESME_NO(utility.MBS_FORMAT.parse(list[36]).intValue()); //liste numaralarını aşağıya doğru birer artır

		//H.Elif
		setOG_DUR(utility.MBS_FORMAT.parse(list[35]).intValue());
		setOLCUM_KODU(utility.MBS_FORMAT.parse(list[36]).intValue());
		setHESAP_KODU(utility.MBS_FORMAT.parse(list[37]).intValue());
		setSOZLESME_GUCU(utility.MBS_FORMAT.parse(list[38]).intValue());
		setKURULU_GUC(utility.MBS_FORMAT.parse(list[39]).intValue());
		setORTAK_TRAFO_DURUMU(list[40]);

		ISayaclar sayaclar = getSAYACLAR();
		for (ISayacBilgi sb : SayacBilgi.fromString(list, 41))
			sayaclar.add(sb);


		/*setOG_DUR(utility.MBS_FORMAT.parse(list[35]).intValue());
		setOLCUM_KODU(utility.MBS_FORMAT.parse(list[36]).intValue());
		setHESAP_KODU(utility.MBS_FORMAT.parse(list[37]).intValue());
		setSOZLESME_GUCU(utility.MBS_FORMAT.parse(list[38]).intValue());
		setKURULU_GUC(utility.MBS_FORMAT.parse(list[39]).intValue());
		setORTAK_TRAFO_DURUMU(list[40]);

		ISayaclar sayaclar = getSAYACLAR();
		for (ISayacBilgi sb : SayacBilgi.fromString(list, 41))
			sayaclar.add(sb);*/

		return 0;
	}

	// -------------------------------------------------------------------------

	public static final String tableName = "isemri";

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.ZAMAN_KODU, DbType.INTEGER, field.s_ZAMAN_KODU, false),
			new FieldInfo(field.HAREKET_KODU, DbType.VARCHAR, field.s_HAREKET_KODU, true),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.INTEGER, field.s_SAHA_ISEMRI_NO, true),
			new FieldInfo(field.ISLEM_TIPI, DbType.INTEGER, field.s_ISLEM_TIPI, true),
			new FieldInfo(field.ISEMRI_TARIHI, DbType.DATE, field.s_ISEMRI_TARIHI, true),
			new FieldInfo(field.ISEMRI_DURUMU, DbType.INTEGER, field.s_ISEMRI_DURUMU, true),
			new FieldInfo(field.ATANMIS_GOREVLI, DbType.INTEGER, field.s_ATANMIS_GOREVLI, true),
			new FieldInfo(field.ISLEM_TARIHI, DbType.DATE, field.s_ISLEM_TARIHI, true),
			new FieldInfo(field.KARNE_NO, DbType.INTEGER, field.s_KARNE_NO, true),
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, true),
			new FieldInfo(field.SIRA_NO, DbType.INTEGER, field.s_SIRA_NO, true),
			new FieldInfo(field.SIRA_EK, DbType.INTEGER, field.s_SIRA_EK, true),
			new FieldInfo(field.UNVAN, DbType.VARCHAR, field.s_UNVAN, true),
			new FieldInfo(field.ADRES, DbType.VARCHAR, field.s_ADRES, true),
			new FieldInfo(field.ADRES_TARIF, DbType.VARCHAR, field.s_ADRES_TARIF, true),
			new FieldInfo(field.SEMT, DbType.VARCHAR, field.s_SEMT, true),
			new FieldInfo(field.CIFT_TERIM_DUR, DbType.INTEGER, field.s_CIFT_TERIM_DUR, true),
			new FieldInfo(field.PUANT_DUR, DbType.INTEGER, field.s_PUANT_DUR, true),
			new FieldInfo(field.GIDEN_DURUM_KODU, DbType.INTEGER, field.s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.TARIFE_KODU, DbType.INTEGER, field.s_TARIFE_KODU, true),
			new FieldInfo(field.CARPAN, DbType.REAL, field.s_CARPAN, true),
			new FieldInfo(field.SON_OKUMA_TARIHI, DbType.DATE, field.s_SON_OKUMA_TARIHI, true),
			new FieldInfo(field.ANET_ISLETME_KODU, DbType.VARCHAR, field.s_ANET_ISLETME_KODU, true),
			new FieldInfo(field.ANET_ABONE_NO, DbType.VARCHAR, field.s_ANET_ABONE_NO, true),
			new FieldInfo(field.KESME_DUR, DbType.VARCHAR, field.s_KESME_DUR, true),
			new FieldInfo(field.BORC_ADET, DbType.INTEGER, field.s_BORC_ADET, true),
			new FieldInfo(field.BORC_TUTARI, DbType.REAL, field.s_BORC_TUTARI, true),
			new FieldInfo(field.BORC_GECIKME, DbType.REAL, field.s_BORC_GECIKME, true),
			new FieldInfo(field.ALT_ISLEM_TIPI, DbType.INTEGER, field.s_ALT_ISLEM_TIPI, true),
			new FieldInfo(field.TELEFON, DbType.VARCHAR, field.s_TELEFON, true),
			new FieldInfo(field.TELEFON2, DbType.VARCHAR, field.s_TELEFON2, true),
			new FieldInfo(field.CEP_TELEFON, DbType.VARCHAR, field.s_CEP_TELEFON, true),
			new FieldInfo(field.ISEMRI_ACIKLAMA, DbType.VARCHAR, field.s_ISEMRI_ACIKLAMA, true),
			new FieldInfo(field.ALT_EMIR_TURU, DbType.INTEGER, field.s_ALT_EMIR_TURU, true),
			new FieldInfo(field.CBS_ENLEM, DbType.REAL, field.s_CBS_ENLEM, true),
			new FieldInfo(field.CBS_BOYLAM, DbType.REAL, field.s_CBS_BOYLAM, true),

			//Onur
			//new FieldInfo(field.BINA_KODU, DbType.INTEGER, field.s_BINA_KODU, true),
			//new FieldInfo(field.SOZLESME_NO, DbType.INTEGER, field.s_SOZLESME_NO, true),

			//H.Elif
			new FieldInfo(field.OG_DUR, DbType.INTEGER, field.s_OG_DUR, true),
			new FieldInfo(field.OLCUM_KODU, DbType.INTEGER, field.s_OLCUM_KODU, true),
			new FieldInfo(field.HESAP_KODU, DbType.INTEGER, field.s_HESAP_KODU, true),
			new FieldInfo(field.SOZLESME_GUCU, DbType.INTEGER, field.s_SOZLESME_GUCU, true),
			new FieldInfo(field.KURULU_GUC, DbType.INTEGER, field.s_KURULU_GUC, true),
			new FieldInfo(field.ORTAK_TRAFO_DURUMU, DbType.VARCHAR, field.s_ORTAK_TRAFO_DURUMU, true),


			new FieldInfo(field.MARKA_1, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_1, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_1, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_1, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_1, DbType.INTEGER, field.s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_1, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_2, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_2, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_2, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_2, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_2, DbType.INTEGER, field.s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_2, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_3, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_3, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_3, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_3, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_3, DbType.INTEGER, field.s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_3, DbType.INTEGER, field.s_IMAL_YILI, true),

			// -----------------------------------------------------------------

			new FieldInfo(field.SOZLESME_DUR, DbType.VARCHAR, field.s_SOZLESME_DUR, false),
			new FieldInfo(field.KONTROL_SAYAC_DUR, DbType.INTEGER, field.s_KONTROL_SAYAC_DUR, false),

			new FieldInfo(field.MARKA_K1, DbType.VARCHAR, field.s_MARKA, false),
			new FieldInfo(field.SAYAC_NO_K1, DbType.INTEGER, field.s_SAYAC_NO, false),
			new FieldInfo(field.SAYAC_CINSI_K1, DbType.VARCHAR, field.s_SAYAC_CINSI, false),
			new FieldInfo(field.HANE_SAYISI_K1, DbType.INTEGER, field.s_HANE_SAYISI, false),
			new FieldInfo(field.GIDEN_DURUM_KODU_K1, DbType.INTEGER, field.s_GIDEN_DURUM_KODU_2, false),
			new FieldInfo(field.IMAL_YILI_K1, DbType.INTEGER, field.s_IMAL_YILI, false),

			new FieldInfo(field.MARKA_K2, DbType.VARCHAR, field.s_MARKA, false),
			new FieldInfo(field.SAYAC_NO_K2, DbType.INTEGER, field.s_SAYAC_NO, false),
			new FieldInfo(field.SAYAC_CINSI_K2, DbType.VARCHAR, field.s_SAYAC_CINSI, false),
			new FieldInfo(field.HANE_SAYISI_K2, DbType.INTEGER, field.s_HANE_SAYISI, false),
			new FieldInfo(field.GIDEN_DURUM_KODU_K2, DbType.INTEGER, field.s_GIDEN_DURUM_KODU_2, false),
			new FieldInfo(field.IMAL_YILI_K2, DbType.INTEGER, field.s_IMAL_YILI, false),

			new FieldInfo(field.MARKA_K3, DbType.VARCHAR, field.s_MARKA, false),
			new FieldInfo(field.SAYAC_NO_K3, DbType.INTEGER, field.s_SAYAC_NO, false),
			new FieldInfo(field.SAYAC_CINSI_K3, DbType.VARCHAR, field.s_SAYAC_CINSI, false),
			new FieldInfo(field.HANE_SAYISI_K3, DbType.INTEGER, field.s_HANE_SAYISI, false),
			new FieldInfo(field.GIDEN_DURUM_KODU_K3, DbType.INTEGER, field.s_GIDEN_DURUM_KODU_2, false),
			new FieldInfo(field.IMAL_YILI_K3, DbType.INTEGER, field.s_IMAL_YILI, false),

	};

	public static final IndexInfo[] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_karne_no", false, field.KARNE_NO, field.SIRA_NO),
			new IndexInfo(tableName+"_saha_isemri_no", true, field.SAHA_ISEMRI_NO),
			new IndexInfo(tableName+"_isemri_durumu", false, field.ISEMRI_DURUMU, field.ISLEM_TIPI),
			new IndexInfo(tableName+"_unvan", false, field.UNVAN),
			new IndexInfo(tableName+"_zaman_kodu", false, field.ZAMAN_KODU),
			new IndexInfo(tableName+"_adres", false, field.ADRES)

	};

	public static final String insertString = DbHelper.PrepareInsertString(isemri_guncelle.class);
	public static final String updateString =
			DbHelper.PrepareUpdateString(isemri_guncelle.class, field.SAHA_ISEMRI_NO);


	// -------------------------------------------------------------------------

	public static final int TABLOID = 3;

	@Override
	public int getTabloId() {
		return TABLOID;
	}

	@Override
	public int getZAMAN_KODU() {
		return ZAMAN_KODU;
	}

	@Override
	public char getHAREKET_KODU() {
		return HAREKET_KODU;
	}

	@Override
	public int getSAHA_ISEMRI_NO() {
		return SAHA_ISEMRI_NO;
	}

	@Override
	public IIslemTipi getISLEM_TIPI() {
		return ISLEM_TIPI;
	}

	@Override
	public Date getISEMRI_TARIHI() {
		return ISEMRI_TARIHI;
	}

	@Override
	public IIslemDurum getISEMRI_DURUMU() {
		return ISEMRI_DURUMU;
	}

	@Override
	public int getATANMIS_GOREVLI() {
		return ATANMIS_GOREVLI;
	}

	@Override
	public Date getISLEM_TARIHI() {
		return ISLEM_TARIHI;
	}

	@Override
	public int getKARNE_NO() {
		return KARNE_NO;
	}

	@Override
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	@Override
	public int getSIRA_NO() {
		return SIRA_NO;
	}

	@Override
	public int getSIRA_EK() {
		return SIRA_EK;
	}

	@Override
	public String getUNVAN() {
		return UNVAN;
	}

	@Override
	public String getADRES() {
		return ADRES;
	}

	@Override
	public String getADRES_TARIF() {
		return ADRES_TARIF;
	}

	@Override
	public String getSEMT() {
		return SEMT;
	}

	@Override
	public boolean getCIFT_TERIM_DUR() {
		return CIFT_TERIM_DUR;
	}

	@Override
	public boolean getPUANT_DUR() {
		return PUANT_DUR;
	}

	@Override
	public int getGIDEN_DURUM_KODU() {
		return GIDEN_DURUM_KODU;
	}

	@Override
	public int getTARIFE_KODU() {
		return TARIFE_KODU;
	}

	@Override
	public double getCARPAN() {
		return CARPAN;
	}

	@Override
	public Date getSON_OKUMA_TARIHI() {
		return SON_OKUMA_TARIHI;
	}

	@Override
	public String getANET_ISLETME_KODU() {
		return ANET_ISLETME_KODU;
	}

	@Override
	public String getANET_ABONE_NO() {
		return ANET_ABONE_NO;
	}

	@Override
	public String getKESME_DUR() {
		return KESME_DUR;
	}

	@Override
	public int getBORC_ADET() {
		return BORC_ADET;
	}

	@Override
	public double getBORC_TUTARI() {
		return BORC_TUTARI;
	}

	@Override
	public double getBORC_GECIKME() {
		return BORC_GECIKME;
	}

	@Override
	public int getALT_ISLEM_TIPI() {
		return ALT_ISLEM_TIPI;
	}

	@Override
	public String getTELEFON() {
		return TELEFON;
	}

	@Override
	public String getTELEFON2() {
		return TELEFON2;
	}

	@Override
	public String getCEP_TELEFON() {
		return CEP_TELEFON;
	}

	/*@Override
	public int getBINA_KODU() {

		return BINA_KODU;
	}*/

	//Onur
	/*@Override
	public int getSOZLESME_NO() {
	return SOZLESME_NO;
	}*/


	@Override
	public int getOG_DUR() {
		return OG_DUR;
	}

	@Override
	public int getOLCUM_KODU() {
		return OLCUM_KODU;
	}

	@Override
	public int getHESAP_KODU() {
		return HESAP_KODU;
	}

	@Override
	public int getSOZLESME_GUCU() {
		return SOZLESME_GUCU;
	}

	@Override
	public int getKURULU_GUC() {
		return KURULU_GUC;
	}

	@Override
	public String getORTAK_TRAFO_DURUMU() {
		return ORTAK_TRAFO_DURUMU;
	}

	@Override
	public String getISEMRI_ACIKLAMA() {
		return ISEMRI_ACIKLAMA;
	}

	@Override
	public int getALT_EMIR_TURU() {
		return ALT_EMIR_TURU;
	}

	@Override
	public String getSOZLESME_DUR() {
		return SOZLESME_DUR;
	}

	@Override
	public boolean getKONTROL_SAYAC_DUR() {
		return KONTROL_SAYAC_DUR;
	}

	@Override
	public ISayaclar getSAYACLAR() {
		return SAYACLAR;
	}

	@Override
	public ISayaclar getKSAYACLAR() {
		return KSAYACLAR;
	}

	@Override
	public float getDistance() {
		if(distance < 0){
			try {
				ILocation location = Globals.platform.getLocation();
				ICbs cbs = getCBS();
				if(location != null && location.isValid() && cbs != null && !cbs.isEmpty()) {
					distance = com.mobit.utility.distance(location.getLatitude(), location.getLongitude(),
							cbs.getX(), cbs.getY());
				}
			}
			catch (Exception e){

			}
		}
		return distance;
	}

	@Override
	public ICbs getCBS() {
		return CBS;
	}





	@Override
	public void setZAMAN_KODU(int zAMAN_KODU) {
		ZAMAN_KODU = zAMAN_KODU;
	}

	@Override
	public void setHAREKET_KODU(char hAREKET_KODU) {
		HAREKET_KODU = hAREKET_KODU;
	}

	@Override
	public void setSAHA_ISEMRI_NO(int sAHA_ISEMRI_NO) {
		com.mobit.utility.check(sAHA_ISEMRI_NO, field.s_SAHA_ISEMRI_NO);
		SAHA_ISEMRI_NO = sAHA_ISEMRI_NO;
	}

	@Override
	public void setISLEM_TIPI(IIslemTipi iSLEM_TIPI) {
		ISLEM_TIPI = iSLEM_TIPI;
	}

	@Override
	public void setISEMRI_TARIHI(Date iSEMRI_TARIHI) {

		ISEMRI_TARIHI = (iSEMRI_TARIHI != null && iSEMRI_TARIHI.getTime() > 0) ? iSEMRI_TARIHI : null;
	}

	@Override
	public void setISEMRI_DURUMU(IIslemDurum iSEMRI_DURUMU) {
		ISEMRI_DURUMU = iSEMRI_DURUMU;
	}

	@Override
	public void setATANMIS_GOREVLI(int aTANMIS_GOREVLI) {
		com.mobit.utility.check(aTANMIS_GOREVLI, field.s_ATANMIS_GOREVLI);
		ATANMIS_GOREVLI = aTANMIS_GOREVLI;
	}

	@Override
	public void setISLEM_TARIHI(Date iSLEM_TARIHI) {

		ISLEM_TARIHI = (iSLEM_TARIHI != null && iSLEM_TARIHI.getTime() > 0) ? iSLEM_TARIHI : null;
	}

	@Override
	public void setKARNE_NO(int kARNE_NO) {
		com.mobit.utility.check(kARNE_NO, field.s_KARNE_NO);
		KARNE_NO = kARNE_NO;
	}

	@Override
	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	@Override
	public void setSIRA_NO(int sIRA_NO) {
		com.mobit.utility.check(sIRA_NO, field.s_SIRA_NO);
		SIRA_NO = sIRA_NO;
	}

	@Override
	public void setSIRA_EK(int sIRA_EK) {
		com.mobit.utility.check(sIRA_EK, field.s_SIRA_EK);
		SIRA_EK = sIRA_EK;
	}

	@Override
	public void setUNVAN(String uNVAN) {
		com.mobit.utility.check(uNVAN, field.s_UNVAN);
		UNVAN = uNVAN;
	}

	@Override
	public void setADRES(String aDRES) {
		com.mobit.utility.check(aDRES, field.s_ADRES);
		ADRES = aDRES;
	}

	@Override
	public void setADRES_TARIF(String aDRES_TARIF) {
		com.mobit.utility.check(aDRES_TARIF, field.s_ADRES_TARIF);
		ADRES_TARIF = aDRES_TARIF;
	}

	@Override
	public void setSEMT(String sEMT) {
		com.mobit.utility.check(sEMT, field.s_SEMT);
		SEMT = sEMT;
	}

	@Override
	public void setCIFT_TERIM_DUR(boolean cIFT_TERIM_DUR) {
		CIFT_TERIM_DUR = cIFT_TERIM_DUR;
	}

	@Override
	public void setPUANT_DUR(boolean pUANT_DUR) {
		PUANT_DUR = pUANT_DUR;
	}

	@Override
	public void setGIDEN_DURUM_KODU(int gIDEN_DURUM_KODU) {
		com.mobit.utility.check(gIDEN_DURUM_KODU, field.s_GELEN_DURUM_KODU);
		GIDEN_DURUM_KODU = gIDEN_DURUM_KODU;
	}

	@Override
	public void setTARIFE_KODU(int tARIFE_KODU) {
		com.mobit.utility.check(tARIFE_KODU, field.s_TARIFE_KODU);
		TARIFE_KODU = tARIFE_KODU;
	}

	@Override
	public void setCARPAN(double cARPAN) {
		com.mobit.utility.check(cARPAN, field.s_CARPAN, field.s_CARPAN_PREC);
		CARPAN = cARPAN;
	}

	@Override
	public void setSON_OKUMA_TARIHI(Date sON_OKUMA_TARIHI) {
		SON_OKUMA_TARIHI = sON_OKUMA_TARIHI;
	}

	@Override
	public void setANET_ISLETME_KODU(String aNET_ISLETME_KODU) {
		com.mobit.utility.check(aNET_ISLETME_KODU, field.s_ANET_ISLETME_KODU);
		ANET_ISLETME_KODU = aNET_ISLETME_KODU;
	}

	@Override
	public void setANET_ABONE_NO(String aNET_ABONE_NO) {
		com.mobit.utility.check(aNET_ABONE_NO, field.s_ANET_ABONE_NO);
		ANET_ABONE_NO = aNET_ABONE_NO;
	}

	@Override
	public void setKESME_DUR(String kESME_DUR) {
		com.mobit.utility.check(kESME_DUR, field.s_KESME_DUR);
		KESME_DUR = kESME_DUR;
	}

	@Override
	public void setBORC_ADET(int bORC_ADET) {
		com.mobit.utility.check(bORC_ADET, field.s_BORC_ADET);
		BORC_ADET = bORC_ADET;
	}

	@Override
	public void setBORC_TUTARI(double bORC_TUTARI) {
		com.mobit.utility.check(bORC_TUTARI, field.s_BORC_TUTARI, field.s_TUTAR_PREC);
		BORC_TUTARI = bORC_TUTARI;
	}

	@Override
	public void setBORC_GECIKME(double bORC_GECIKME) {
		com.mobit.utility.check(bORC_GECIKME, field.s_BORC_GECIKME, field.s_TUTAR_PREC);
		BORC_GECIKME = bORC_GECIKME;
	}

	@Override
	public void setALT_ISLEM_TIPI(int aLT_ISLEM_TIPI) {
		com.mobit.utility.check(aLT_ISLEM_TIPI, field.s_ALT_ISLEM_TIPI);
		ALT_ISLEM_TIPI = aLT_ISLEM_TIPI;
	}

	@Override
	public void setTELEFON(String tELEFON) {
		com.mobit.utility.check(tELEFON, field.s_TELEFON);
		TELEFON = tELEFON;
	}

	@Override
	public void setTELEFON2(String tELEFON2) {
		com.mobit.utility.check(tELEFON2, field.s_TELEFON2);
		TELEFON2 = tELEFON2;
	}

	@Override
	public void setCEP_TELEFON(String cEP_TELEFON) {
		com.mobit.utility.check(cEP_TELEFON, field.s_CEP_TELEFON);
		CEP_TELEFON = cEP_TELEFON;
	}

	//Onur
	/*public void setBINA_KODU(int bINA_KODU) {
		com.mobit.utility.check(bINA_KODU, field.s_BINA_KODU);
		BINA_KODU = bINA_KODU;
	}


	public void setSOZLESME_NO(int sOZLESME_NO) {
		com.mobit.utility.check(sOZLESME_NO, field.s_SOZLESME_NO);
		SOZLESME_NO = sOZLESME_NO;
	}*/


	@Override
	public void setOG_DUR(int oG_DUR) {
		com.mobit.utility.check(oG_DUR, field.s_OG_DUR);
		OG_DUR = oG_DUR;
	}

	@Override
	public void setOLCUM_KODU(int oLCUM_KODU) {
		com.mobit.utility.check(oLCUM_KODU, field.s_OLCUM_KODU);
		OLCUM_KODU = oLCUM_KODU;
	}

	@Override
	public void setHESAP_KODU(int hESAP_KODU) {
		com.mobit.utility.check(hESAP_KODU, field.s_HESAP_KODU);
		HESAP_KODU = hESAP_KODU;
	}

	@Override
	public void setSOZLESME_GUCU(int sOZLESME_GUCU) {
		com.mobit.utility.check(sOZLESME_GUCU, field.s_SOZLESME_GUCU);
		SOZLESME_GUCU = sOZLESME_GUCU;
	}

	@Override
	public void setKURULU_GUC(int kURULU_GUC) {
		com.mobit.utility.check(kURULU_GUC, field.s_KURULU_GUC);
		KURULU_GUC = kURULU_GUC;
	}

	@Override
	public void setORTAK_TRAFO_DURUMU(String oRTAK_TRAFO_DURUMU) {
		com.mobit.utility.check(oRTAK_TRAFO_DURUMU, field.s_ORTAK_TRAFO_DURUMU);
		ORTAK_TRAFO_DURUMU = oRTAK_TRAFO_DURUMU;
	}

	@Override
	public void setISEMRI_ACIKLAMA(String iSEMRI_ACIKLAMA) {
		com.mobit.utility.check(iSEMRI_ACIKLAMA, field.s_ISEMRI_ACIKLAMA);
		ISEMRI_ACIKLAMA = iSEMRI_ACIKLAMA;
	}

	@Override
	public void setALT_EMIR_TURU(int aLT_EMIR_TURU) {
		com.mobit.utility.check(aLT_EMIR_TURU, field.s_ALT_EMIR_TURU);
		ALT_EMIR_TURU = aLT_EMIR_TURU;
	}

	public void setKONTROL_SAYAC_DUR(boolean kONTROL_SAYAC_DUR) {
		KONTROL_SAYAC_DUR = kONTROL_SAYAC_DUR;
	}

	public void setSOZLESME_DUR(String sOZLESME_DUR) {
		com.mobit.utility.check(sOZLESME_DUR, field.s_SOZLESME_DUR);
		SOZLESME_DUR = sOZLESME_DUR;
	}



	@Override
	public void setCBS(ICbs cBS) {
		CBS = cBS;
	}

	@Override
	public void setDistance(float distance) {
		this.distance = distance;
	}

	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean getCheck() {
		return checked;
	}

	@Override
	public void setCheck(boolean checked) {

		this.checked = checked;
	}

	@Override
	public void get(ResultSet rs) throws Exception {

		id = rs.getInt(field.ID);
		Object o=rs;
		setZAMAN_KODU(rs.getInt(field.ZAMAN_KODU));

		setHAREKET_KODU(rs.getString(field.HAREKET_KODU).charAt(0));
		setSAHA_ISEMRI_NO(rs.getInt(field.SAHA_ISEMRI_NO));
		setISLEM_TIPI(IslemTipi.fromInteger(rs.getInt(field.ISLEM_TIPI)));

		setISEMRI_TARIHI(com.mobit.utility.getDateFromObject(rs.getObject(field.ISEMRI_TARIHI)));
		setISEMRI_DURUMU(IslemDurum.fromInteger(rs.getInt(field.ISEMRI_DURUMU)));

		setATANMIS_GOREVLI(rs.getInt(field.ATANMIS_GOREVLI));
		setISLEM_TARIHI(com.mobit.utility.getDateFromObject(rs.getObject(field.ISLEM_TARIHI)));
		setKARNE_NO(rs.getInt(field.KARNE_NO));
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setSIRA_NO(rs.getInt(field.SIRA_NO));
		setSIRA_EK(rs.getInt(field.SIRA_EK));
		setUNVAN(rs.getString(field.UNVAN));
		setADRES(rs.getString(field.ADRES));
		setADRES_TARIF(rs.getString(field.ADRES_TARIF));
		setSEMT(rs.getString(field.SEMT));
		setCIFT_TERIM_DUR(rs.getBoolean(field.CIFT_TERIM_DUR));
		setPUANT_DUR(rs.getBoolean(field.PUANT_DUR));
		setGIDEN_DURUM_KODU(rs.getInt(field.GIDEN_DURUM_KODU));
		setTARIFE_KODU(rs.getInt(field.TARIFE_KODU));
		setCARPAN(rs.getInt(field.CARPAN));
		setSON_OKUMA_TARIHI(com.mobit.utility.getDateFromObject(rs.getObject(field.SON_OKUMA_TARIHI)));
		setANET_ISLETME_KODU(rs.getString(field.ANET_ISLETME_KODU));
		setANET_ABONE_NO(rs.getString(field.ANET_ABONE_NO));
		setKESME_DUR(rs.getString(field.KESME_DUR));

		setBORC_ADET(rs.getInt(field.BORC_ADET));
		setBORC_TUTARI(rs.getDouble(field.BORC_TUTARI));
		setBORC_GECIKME(rs.getDouble(field.BORC_GECIKME));

		setALT_ISLEM_TIPI(rs.getInt(field.ALT_ISLEM_TIPI));
		// samet abinin isteğiyle kapadım
		setTELEFON("");//rs.getString(field.TELEFON)
		setTELEFON2("");//rs.getString(field.TELEFON2)
		setCEP_TELEFON("");//rs.getString(field.CEP_TELEFON)
		setISEMRI_ACIKLAMA(rs.getString(field.ISEMRI_ACIKLAMA));
		setALT_EMIR_TURU(rs.getInt(field.ALT_EMIR_TURU));

		setCBS(Cbs.fromResultSet(rs));

		//Onur
		//setBINA_KODU(rs.getInt(field.BINA_KODU));
		//setSOZLESME_NO(rs.getInt(field.SOZLESME_NO));

		//H.Elif
		setOG_DUR(rs.getInt(field.OG_DUR));
		setOLCUM_KODU(rs.getInt(field.OLCUM_KODU));
		setHESAP_KODU(rs.getInt(field.HESAP_KODU));
		setSOZLESME_GUCU(rs.getInt(field.SOZLESME_GUCU));
		setKURULU_GUC(rs.getInt(field.KURULU_GUC));
		setORTAK_TRAFO_DURUMU(rs.getString(field.ORTAK_TRAFO_DURUMU));



		for (ISayacBilgi sb : SayacBilgi.fromResultSet(rs))
			getSAYACLAR().add(sb);

		setSOZLESME_DUR(rs.getString(field.SOZLESME_DUR));
		setKONTROL_SAYAC_DUR(rs.getBoolean(field.KONTROL_SAYAC_DUR));

		for (ISayacBilgi sb : SayacBilgi.fromResultSetK(rs))
			getKSAYACLAR().add(sb);


	}

	@Override
	public int put(DbHelper h) throws Exception {

		if (getId() == null) {

			int id = insert(h);
			if (id > 0)
				return id;

			String query = "select ID, ISEMRI_DURUMU from isemri where SAHA_ISEMRI_NO = ?";
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = h.getConnection().prepareStatement(query);
				stmt.setInt(1, getSAHA_ISEMRI_NO());
				rs = stmt.executeQuery();
				if (rs.next()) {

					if (IslemDurum.fromInteger(rs.getInt(2)).equals(IslemDurum.Tamamlandi))
						this.setISEMRI_DURUMU(IslemDurum.Tamamlandi);
				}
			} finally {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}

		}

		return update(h);

	}

	int insert(DbHelper h) throws Exception {

		int cnt = 0;
		PreparedStatement stmt;

		stmt = h.getInsStatment(this);

		int index = 1;
		stmt.setObject(index++, getId());

		stmt.setInt(index++, getZAMAN_KODU());

		stmt.setString(index++, String.valueOf(getHAREKET_KODU()));
		stmt.setInt(index++, getSAHA_ISEMRI_NO());
		stmt.setInt(index++, getISLEM_TIPI().getValue());

		stmt.setObject(index++, com.mobit.utility.getObjectFromDate(getISEMRI_TARIHI(), com.mobit.utility.TimeType.UnixEpochSeconds));
		stmt.setInt(index++, getISEMRI_DURUMU().getValue());

		stmt.setInt(index++, getATANMIS_GOREVLI());
		stmt.setObject(index++, com.mobit.utility.getObjectFromDate(getISLEM_TARIHI(), com.mobit.utility.TimeType.UnixEpochSeconds));
		stmt.setInt(index++, getKARNE_NO());
		stmt.setInt(index++, getTESISAT_NO());

		stmt.setInt(index++, getSIRA_NO());
		stmt.setInt(index++, getSIRA_EK());
		stmt.setString(index++, getUNVAN());
		stmt.setString(index++, getADRES());
		stmt.setString(index++, getADRES_TARIF());
		stmt.setString(index++, getSEMT());
		stmt.setBoolean(index++, getCIFT_TERIM_DUR());
		stmt.setBoolean(index++, getPUANT_DUR());
		stmt.setInt(index++, getGIDEN_DURUM_KODU());
		stmt.setInt(index++, getTARIFE_KODU());
		stmt.setDouble(index++, getCARPAN());
		stmt.setObject(index++, com.mobit.utility.getObjectFromDate(getSON_OKUMA_TARIHI(), com.mobit.utility.TimeType.UnixEpochSeconds));
		stmt.setString(index++, getANET_ISLETME_KODU());
		stmt.setString(index++, getANET_ABONE_NO());
		stmt.setString(index++, getKESME_DUR());

		stmt.setInt(index++, getBORC_ADET());
		stmt.setDouble(index++, getBORC_TUTARI());
		stmt.setDouble(index++, getBORC_GECIKME());

		stmt.setInt(index++, getALT_ISLEM_TIPI());

		stmt.setString(index++, getTELEFON());
		stmt.setString(index++, getTELEFON2());
		stmt.setString(index++, getCEP_TELEFON());
		stmt.setString(index++, getISEMRI_ACIKLAMA());

		stmt.setInt(index++, getALT_EMIR_TURU());

		stmt.setDouble(index++, getCBS().getX());
		stmt.setDouble(index++, getCBS().getY());

		//Onur
		//stmt.setInt(index++, getBINA_KODU());
		//stmt.setInt(index++, getSOZLESME_NO());

		stmt.setInt(index++, getOG_DUR());
		stmt.setInt(index++, getOLCUM_KODU());
		stmt.setInt(index++, getHESAP_KODU());
		stmt.setInt(index++, getSOZLESME_GUCU());
		stmt.setInt(index++, getKURULU_GUC());
		stmt.setString(index++, getORTAK_TRAFO_DURUMU());

		index = SayacBilgi.toStatement(index, getSAYACLAR(), stmt);

		stmt.setString(index++, getSOZLESME_DUR());
		stmt.setBoolean(index++, getKONTROL_SAYAC_DUR());
		index = SayacBilgi.toStatementTK(index, getKSAYACLAR(), stmt);

		cnt = stmt.executeUpdate();

		if (cnt > 0) {
			id = h.GetId();
			return id;
		}
		return 0;
	}

	int update(DbHelper h) throws Exception {

		int cnt = 0;
		PreparedStatement stmt = null;
		int index;
		if (mUpdate == false) {

			stmt = h.getUpdStatment(this);
			index = 1;
			stmt.setInt(index++, getZAMAN_KODU());

			stmt.setString(index++, String.valueOf(getHAREKET_KODU()));
			stmt.setInt(index++, getSAHA_ISEMRI_NO());
			stmt.setInt(index++, getISLEM_TIPI().getValue());

			stmt.setObject(index++, com.mobit.utility.getObjectFromDate(getISEMRI_TARIHI(), com.mobit.utility.TimeType.UnixEpochSeconds));
			stmt.setInt(index++, getISEMRI_DURUMU().getValue());

			stmt.setInt(index++, getATANMIS_GOREVLI());
			stmt.setObject(index++, com.mobit.utility.getObjectFromDate(getISEMRI_TARIHI(), com.mobit.utility.TimeType.UnixEpochSeconds));
			stmt.setInt(index++, getKARNE_NO());
			stmt.setInt(index++, getTESISAT_NO());
			stmt.setInt(index++, getSIRA_NO());
			stmt.setInt(index++, getSIRA_EK());
			stmt.setString(index++, getUNVAN());
			stmt.setString(index++, getADRES());
			stmt.setString(index++, getADRES_TARIF());
			stmt.setString(index++, getSEMT());
			stmt.setBoolean(index++, getCIFT_TERIM_DUR());
			stmt.setBoolean(index++, getPUANT_DUR());
			stmt.setInt(index++, getGIDEN_DURUM_KODU());
			stmt.setInt(index++, getTARIFE_KODU());
			stmt.setDouble(index++, getCARPAN());
			stmt.setObject(index++, com.mobit.utility.getObjectFromDate(getSON_OKUMA_TARIHI(), com.mobit.utility.TimeType.UnixEpochSeconds));
			stmt.setString(index++, getANET_ISLETME_KODU());
			stmt.setString(index++, getANET_ABONE_NO());
			stmt.setString(index++, getKESME_DUR());

			stmt.setInt(index++, getBORC_ADET());
			stmt.setDouble(index++, getBORC_TUTARI());
			stmt.setDouble(index++, getBORC_GECIKME());

			stmt.setInt(index++, getALT_ISLEM_TIPI());

			stmt.setString(index++, getTELEFON());
			stmt.setString(index++, getTELEFON2());
			stmt.setString(index++, getCEP_TELEFON());
			stmt.setString(index++, getISEMRI_ACIKLAMA());

			stmt.setInt(index++, getALT_EMIR_TURU());

			stmt.setDouble(index++, getCBS().getX());
			stmt.setDouble(index++, getCBS().getY());

			//Onur
			//stmt.setInt(index++, getBINA_KODU());
			//stmt.setInt(index++, getSOZLESME_NO());

			stmt.setInt(index++, getOG_DUR());
			stmt.setInt(index++, getOLCUM_KODU());
			stmt.setInt(index++, getHESAP_KODU());
			stmt.setInt(index++, getSOZLESME_GUCU());
			stmt.setInt(index++, getKURULU_GUC());
			stmt.setString(index++, getORTAK_TRAFO_DURUMU());

			index = SayacBilgi.toStatement(index, getSAYACLAR(), stmt);

			stmt.setString(index++, getSOZLESME_DUR());
			stmt.setBoolean(index++, getKONTROL_SAYAC_DUR());
			index = SayacBilgi.toStatementTK(index, getKSAYACLAR(), stmt);

			// where
			stmt.setInt(index, getSAHA_ISEMRI_NO());

			cnt = stmt.executeUpdate();

		} else {

			String update = "update isemri set ZAMAN_KODU = ?, HAREKET_KODU = ?, ISLEM_TIPI = ?, "
					+ "ISEMRI_TARIHI = ?, ISEMRI_DURUMU = ?, ATANMIS_GOREVLI = ?, ISLEM_TARIHI = ?, "
					+ "KARNE_NO = ?, TESISAT_NO = ? where SAHA_ISEMRI_NO = ?;";

			try {

				stmt = h.getConnection().prepareStatement(update);
				index = 1;
				stmt.setInt(index++, ZAMAN_KODU);
				stmt.setInt(index++, HAREKET_KODU);
				stmt.setInt(index++, ISLEM_TIPI.getValue());
				stmt.setObject(index++, com.mobit.utility.getObjectFromDate(ISEMRI_TARIHI, com.mobit.utility.TimeType.UnixEpochSeconds));
				stmt.setInt(index++, ISEMRI_DURUMU.getValue());
				stmt.setInt(index++, ATANMIS_GOREVLI);
				stmt.setObject(index++, com.mobit.utility.getObjectFromDate(ISLEM_TARIHI, com.mobit.utility.TimeType.UnixEpochSeconds));
				stmt.setInt(index++, KARNE_NO);
				stmt.setInt(index++, TESISAT_NO);

				stmt.setInt(index++, SAHA_ISEMRI_NO);

				cnt = stmt.executeUpdate();

			} finally {
				if (stmt != null)
					stmt.close();
			}

		}

		return cnt;
	}

	@Override
	public String getAciklama() {

		String s = getALT_EMIR_TURU() > 0 ? Integer.toString(getALT_EMIR_TURU()) : "";
		if (Globals.app instanceof IElecApplication) {
			IElecApplication app = (IElecApplication) Globals.app;
			for (AltEmirTuru tur : app.getAltEmirTuru(getISLEM_TIPI())) {
				if (tur.altEmirTuru == getALT_EMIR_TURU()) {
					s = tur.Tanim;
					break;
				}
			}
		}
		if (s == null || s.isEmpty()) {
			return getISLEM_TIPI().equals(IslemTipi.SayacOkuma) ? "Tesisat Bilgi" : getISLEM_TIPI().toString();
		}

		return String.format("%s - %s", getISLEM_TIPI().toString(), s);
	}

	@Override
	public List<String[]> getDetay() {

		String s;
		List<String[]> list = new ArrayList<String[]>(50);
		list.add(new String[] { "İşemri No:", Integer.toString(getSAHA_ISEMRI_NO()) });
		list.add(new String[] { "İşlem Tipi:", getISLEM_TIPI().toString() });
		list.add(new String[] { "İşemri Tarihi:",
				getISEMRI_TARIHI() != null ? Globals.dateFmt.format(getISEMRI_TARIHI()) : "" });
		list.add(new String[] { "İşemri Durum:", getISEMRI_DURUMU().toString() });
		list.add(new String[] { "Personel Kodu:", Integer.toString(getATANMIS_GOREVLI()) });
		list.add(new String[] { "İşlem Tarihi:",
				getISLEM_TARIHI() != null ? Globals.dateFmt.format(getISLEM_TARIHI()) : "" });
		list.add(new String[] { "Karne No:", Integer.toString(getKARNE_NO()) });
		list.add(new String[] { "Tesisat No:", Integer.toString(getTESISAT_NO()) });
		list.add(new String[] { "Sıra No:", Integer.toString(getSIRA_NO()) });
		list.add(new String[] { "Sıra Ek:", Integer.toString(getSIRA_EK()) });
		list.add(new String[] { "Ünvan:", getUNVAN() });
		list.add(new String[] { "Adres:", getADRES() });
		list.add(new String[] { "Adres Tarif:", getADRES_TARIF() });
		list.add(new String[] { "Semt:", getSEMT() });
		list.add(new String[] { "Çift Terim:", getCIFT_TERIM_DUR() ? "Var" : "Yok" });
		list.add(new String[] { "Puant Durumu:", getPUANT_DUR() ? "Var" : "Yok" });
		list.add(new String[] { "Durum Kodu:", Integer.toString(getGIDEN_DURUM_KODU()) });
		list.add(new String[] { "Tarife Kodu:", Integer.toString(getTARIFE_KODU()) });
		list.add(new String[] { "Çarpan:", Globals.getDecimalFormat().format(getCARPAN()) });
		// Uğur Bey'in isteği ile kapatıldı
		// list.add(new String[] { "Son Okuma Tarihi:",
		// mobit.utility.dateFmt.format(getSON_OKUMA_TARIHI()) });
		String isletmeKodu = getANET_ISLETME_KODU();
		if(isletmeKodu.length() >= 11){
			isletmeKodu = String.format("%s.%s.%s.%s.%s",
					isletmeKodu.substring(0, 3), isletmeKodu.substring(3, 5), isletmeKodu.substring(5, 7),
					isletmeKodu.substring(7, 9), isletmeKodu.substring(9, 11));
		}
		//setBORC_ADET(5);
		//tesisat sorgu muhammed gökkaya
		list.add(new String[] { "Anet İşletme Kodu:", isletmeKodu });
		list.add(new String[] { "Anet Abone No:", getANET_ABONE_NO() });
		list.add(new String[] { "Kesme Durumu:", getKESME_DUR() });
		list.add(new String[] { "Borç Adet:", Integer.toString(getBORC_ADET()) });
		list.add(new String[] { "Borç Tutar:", Globals.getCurrencyFormat().format(getBORC_TUTARI()) });
		list.add(new String[] { "Borç Gecikme:", Globals.getCurrencyFormat().format(getBORC_GECIKME()) });

		int tip = getALT_ISLEM_TIPI();
		s = "";
		switch (tip) {
			case 1:
				s = "aktif";
				break;
			case 2:
				s = "reaktif";
				break;
			case 4:
				s = "kapasitif";
				break;
			case 7:
				s = "tümü";
				break;
			default:
				s = Integer.toString(tip);
		}
		list.add(new String[] { "Alt İşlem Tipi:", s });

		list.add(new String[] { "Telefon:", getTELEFON() });
		list.add(new String[] { "Telefon 2:", getTELEFON2() });
		list.add(new String[] { "Cep Telefon:", getCEP_TELEFON() });
		list.add(new String[] { "Açıklama:", getISEMRI_ACIKLAMA() });

		//Onur
		//list.add(new String[] { "Bina Kodu:", Integer.toString(getBINA_KODU()) });
		//list.add(new String[] { "Sözleşme No:", Integer.toString(getSOZLESME_NO()) });

		//H.Elif
		list.add(new String[] { "OG Durumu:", Integer.toString(getOG_DUR()) });
		list.add(new String[] { "Ölçüm Kodu:", Integer.toString(getOLCUM_KODU()) });
		list.add(new String[] { "Hesap Kodu:", Integer.toString(getHESAP_KODU()) });
		list.add(new String[] { "Sözleşme Gücü:", Integer.toString(getSOZLESME_GUCU()) });
		list.add(new String[] { "Kurulu Güç:", Integer.toString(getKURULU_GUC()) });
		list.add(new String[] { "Ortak Trafo Durumu:", getORTAK_TRAFO_DURUMU() });

		// ---------------------------------------------------------------------

		String altEmirTuru = "";
		if (Globals.app instanceof IMbsApplication) {
			IMbsApplication app = (IMbsApplication) Globals.app;
			AltEmirTuru[] altEmirTurleri = app.getAltEmirTuru(getISLEM_TIPI());
			if (altEmirTurleri != null) {
				for (AltEmirTuru tur : altEmirTurleri) {
					if (tur.altEmirTuru == getALT_EMIR_TURU()) {
						altEmirTuru = tur.Tanim;
						break;
					}
				}
			}
		}
		if (altEmirTuru.isEmpty())
			altEmirTuru = Integer.toString(getALT_EMIR_TURU());
		list.add(new String[] { "Alt Emir Türü:", altEmirTuru });

		// ---------------------------------------------------------------------

		list.add(new String[] { "CBS:", getCBS().toString() });
		list.add(new String[] { "Mesafe:", com.mobit.utility.distance(getDistance()) });

		list.addAll(getSAYACLAR().getDetay());
		list.addAll(getSAYACLAR().getEndeksler().getDetay());
		list.addAll(getKSAYACLAR().getDetay());

		return list;
	}

	private static final String query = String.format("select min(%s) from %s where %s <> ?",
			field.ZAMAN_KODU, tableName, field.ISLEM_TIPI);

	public static int getMinZamanKodu(Connection conn) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();

			if (rs.next())
				return rs.getInt(1);

		} catch (Exception e) {

			return 0;

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

	private static final String query01 = String.format("select max(%s) from %s where %s = ? and %s <> ?",
			field.ZAMAN_KODU, tableName, field.TESISAT_NO, field.ISLEM_TIPI);

	public static int getTesisatMaxZamanKodu(Connection conn, int tesisat_no) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(query01);
			stmt.setInt(1, tesisat_no);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();
			if (rs.next()){
				int zamanKodu = rs.getInt(1);
				return (zamanKodu != 0) ? zamanKodu : 0xffffffff;
			}

		} catch (Exception e) {


		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}
		return 0xffffffff;
	}

	private static final String query02 = String.format("select min(%s) from %s where %s = ? and %s <> ?",
			field.ZAMAN_KODU, tableName, field.KARNE_NO, field.ISLEM_TIPI);

	public static int getKarneMinZamanKodu(Connection conn, int karne_no) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(query02);
			stmt.setInt(1, karne_no);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();
			if (rs.next()){
				return rs.getInt(1);
			}

		} catch (Exception e) {


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

	private static final String query1 = String.format("update %s set %s=? where %s = ?", tableName, field.ISEMRI_DURUMU,
			field.SAHA_ISEMRI_NO);

	public static long setISEMRI_DURUM(Connection conn, int SAHA_ISEMRI_NO, IIslemDurum durum) {

		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(query1);
			stmt.setInt(1, durum.getValue());
			stmt.setInt(2, SAHA_ISEMRI_NO);
			stmt.executeUpdate();

		} catch (Exception e) {

			return 0;

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}
		return 0;
	}

	/*
	

	IIsemriZabit zabit;

	@Override
	public IIsemriZabit getZabit() {
		return zabit;
	}

	@Override
	public void setZabit(IIsemriZabit zabit) {
		this.zabit = zabit;
	}
	
	ITakilanSayac newTakilanSayac();
	
	ISayaclar getTakilanSayaclar();
	
	IIsemriZabit newZabit();
	
	IIsemriZabit getZabit();
	
	void setZabit(IIsemriZabit zabit);
	
	public List<IIsemriSoru> getIsemriSorular();
	
	IIsemriCevapSil newIsemriCevapSil();

	 */

	@Override
	public ITakilanSayac newTakilanSayac() {
		return new takilan_sayac(this);
	}

	@Override
	public IIsemriZabit newZabit() {
		return new put_isemri_zabit(this);
	}

	@Override
	public IMuhurSokme newMuhurSokme() {
		return new put_muhur_sokme(this);
	}
	@Override
	public ITesisatMuhur newMuhurleme() {
		return new put_tesisat_muhur(this);
	}

	@Override
	public IIslem newIslem() throws Exception {

		IIslemGrup grup = new IslemGrup();
		if (getISLEM_TIPI().equals(IslemTipi.SayacOkuma)){
			grup.add(new put_endeks(this));
		}
		else {

			if(getISLEM_TIPI().equals(IslemTipi.Tespit))
				for(IIslem islem : getIsemriSorular()) grup.add(islem);

			if(getISLEM_TIPI().equals(IslemTipi.SayacDegistir) || getISLEM_TIPI().equals(IslemTipi.SayacTakma))
				grup.add(new takilan_sayac(this));

			grup.add(new put_isemri(this));
		}
		grup.setIslemTipi(getISLEM_TIPI().getValue());
		return grup;
	}

	ISayacIslem sayacIslem;

	public void setSayacIslem(ISayacIslem sayacIslem) {
		this.sayacIslem = sayacIslem;
	}

	// ----------------------------------------------------------------------------------------------

	private static final String query2 = String.format("select * from %s where %s=? and %s=? order by %s LIMIT 1", tableName,
			field.KARNE_NO, field.ISLEM_TIPI, field.SIRA_NO);

	public static IIsemri KarneIlkTesisat(Connection conn, int KarneNo) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn.prepareStatement(query2);
			stmt.setInt(1, KarneNo);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery(query2);
			if (rs.next()) {
				tesisat t = new tesisat();
				t.get(rs);
				return t;
			}

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

		return null;
	}

	private static final String query3 = String.format("select * from %s where %s=? and %s=? and cast(%s*1000+%s as int) > ? order by %s, %s LIMIT 1",
			tableName, field.KARNE_NO, field.ISLEM_TIPI, field.SIRA_NO, field.SIRA_EK, field.SIRA_NO, field.SIRA_EK);
	private static final String query31 = String.format("select * from %s where %s=? and %s=? and %s=? and cast(%s*1000+%s as int) > ? order by %s, %s LIMIT 1",
			tableName, field.KARNE_NO, field.ISLEM_TIPI, field.ISEMRI_DURUMU, field.SIRA_NO, field.SIRA_EK, field.SIRA_NO, field.SIRA_EK);

	public static IIsemri2 KarneSonrakiTesisat(Connection conn, int KarneNo, int SiraNo, int SiraEk, boolean unread) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			int idx = 1;
			String query = unread ? query31 : query3;
			stmt = conn.prepareStatement(query);
			stmt.setInt(idx++, KarneNo);
			stmt.setInt(idx++, IslemTipi.SayacOkuma.getValue());
			if(unread)stmt.setInt(idx++, IslemDurum.Atanmis.getValue());
			stmt.setInt(idx++, SiraNo*1000+SiraEk);
			rs = stmt.executeQuery();
			if (rs.next()) {
				tesisat t = new tesisat();
				t.get(rs);
				return t;
			}

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

		return null;
	}

	private static final String query4 = String.format(
			"select * from %s where %s=? and %s=? and cast(%s*1000+%s as int) < ? order by %s desc, %s desc LIMIT 1", tableName, field.KARNE_NO,
			field.ISLEM_TIPI, field.SIRA_NO, field.SIRA_EK, field.SIRA_NO, field.SIRA_EK);
	private static final String query41 = String.format(
			"select * from %s where %s=? and %s=? and %s=? and cast(%s*1000+%s as int) < ? order by %s desc, %s desc LIMIT 1", tableName, field.KARNE_NO,
			field.ISLEM_TIPI, field.ISEMRI_DURUMU, field.SIRA_NO, field.SIRA_EK, field.SIRA_NO, field.SIRA_EK);

	public static IIsemri2 KarneOncekiTesisat(Connection conn, int KarneNo, int SiraNo, int SiraEk, boolean unread) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn.prepareStatement(unread ? query41 : query4);
			int idx = 1;
			stmt.setInt(idx++, KarneNo);
			stmt.setInt(idx++, IslemTipi.SayacOkuma.getValue());
			if(unread) stmt.setInt(idx++, IslemDurum.Atanmis.getValue());
			stmt.setInt(idx++, SiraNo*1000+SiraEk);
			rs = stmt.executeQuery();
			if (rs.next()) {
				tesisat t = new tesisat();
				t.get(rs);
				return t;
			}

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

		return null;
	}

	private static final String query5 = String.format("select %s from %s where %s=? group by %s order by %s ",
			field.KARNE_NO, tableName, field.ISLEM_TIPI, field.KARNE_NO, field.KARNE_NO);

	public static List<Integer> KarneListe(Connection conn) throws Exception {

		PreparedStatement stmt = null;
		List<Integer> list = new ArrayList<Integer>();
		ResultSet rs = null;
		try {

			stmt = conn.prepareStatement(query5);
			stmt.setInt(1, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery(query5);
			while (rs.next()) {
				list.add(rs.getInt(1));
			}

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

		return list;
	}


	private static final String query61 = String.format("select * from %s where (%s=? or %s=? or %s=? or %s=?) and %s<>?",
			tableName, field.TESISAT_NO, field.SAYAC_NO_1, field.SAYAC_NO_2, field.SAYAC_NO_3, field.ISLEM_TIPI);

	public static List<IIsemri> IsemriTesisatSayacBul(Connection conn, int TesisatNoSayacNo) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemri> list = new ArrayList<IIsemri>();
		try {

			stmt = conn.prepareStatement(query61);
			stmt.setInt(1, TesisatNoSayacNo);
			stmt.setInt(2, TesisatNoSayacNo);
			stmt.setInt(3, TesisatNoSayacNo);
			stmt.setInt(4, TesisatNoSayacNo);
			stmt.setInt(5, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();
			while (rs.next()) {
				tesisat t = new tesisat();
				t.get(rs);
				list.add(t);
			}

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
		return list;
	}

	private static final String query62 = String.format("select * from %s where (%s=? or %s=? or %s=? or %s=?) and %s=?",
			tableName, field.TESISAT_NO, field.SAYAC_NO_1, field.SAYAC_NO_2, field.SAYAC_NO_3, field.ISLEM_TIPI);

	private static final String querydeneme = String.format("select * from %s where ( SAYAC_NO_1 IS NULL) AND  %s=?",
			tableName, field.ISLEM_TIPI);

	public static List<IIsemri> OkumaTesisatSayacBul(Connection conn, int TesisatNoSayacNo) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemri> list = new ArrayList<IIsemri>();
		try {
//Hüseyin Emre Çevik Deneme
			if (TesisatNoSayacNo==0){
				stmt = conn.prepareStatement(querydeneme);

				stmt.setInt(1, IslemTipi.SayacOkuma.getValue());
			}
			else{
				stmt = conn.prepareStatement(query62);
				stmt.setInt(1, TesisatNoSayacNo);
				stmt.setInt(2, TesisatNoSayacNo);
				stmt.setInt(3, TesisatNoSayacNo);
				stmt.setInt(4, TesisatNoSayacNo);
				stmt.setInt(5, IslemTipi.SayacOkuma.getValue());
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				tesisat t = new tesisat();
				t.get(rs);
				list.add(t);
			}

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
		return list;
	}

	//-----------------

	private static final String query7 = String.format("select * from %s where %s=?",
			tableName, field.SAHA_ISEMRI_NO);

	public static IIsemri IsemriBul(Connection conn, int SAHA_ISEMRI_NO) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn.prepareStatement(query7);
			stmt.setInt(1, SAHA_ISEMRI_NO);
			rs = stmt.executeQuery();
			while (rs.next()) {
				isemri_guncelle t = new isemri_guncelle();
				t.get(rs);
				return t;
			}

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
		return null;
	}

	private static final String query8 = String.format("select * from %s where %s=?",
			tableName, field.TESISAT_NO);

	public static List<ITesisat> TesisatBul(Connection conn, int TESISAT_NO) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ITesisat> list = new ArrayList<ITesisat>();
		try {

			stmt = conn.prepareStatement(query8);
			stmt.setInt(1, TESISAT_NO);
			rs = stmt.executeQuery();
			while (rs.next()) {
				tesisat t = new tesisat();
				t.get(rs);
				list.add(t);
			}

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
		return list;
	}


	@Override
	public IIsemri next(boolean unread) throws Exception {
		IIsemri t = tesisat.KarneSonrakiTesisat(Globals.app.getConnection(), getKARNE_NO(), getSIRA_NO(), getSIRA_EK(), unread);
		if (t == null)
			throw new MobitException(MsgInfo.SON_KAYITTA_BULUNUYORSUNUZ);
		return t;
	}

	@Override
	public IIsemri prev(boolean unread) throws Exception {
		IIsemri t = tesisat.KarneOncekiTesisat(Globals.app.getConnection(), getKARNE_NO(), getSIRA_NO(), getSIRA_EK(), unread);
		if (t == null)
			throw new MobitException(MsgInfo.ILK_KAYITTA_BULUNUYORSUNUZ);

		return t;
	}

	@Override
	public ISayacIslem getSayacIslem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIslemRapor getIslemRapor(IElecApplication app) throws Exception {
		// TODO Auto-generated method stub
		return new IslemRapor(app, getSAHA_ISEMRI_NO());
	}

	@Override
	public List<IIsemriSoru> getIsemriSorular() {
		IMbsApplication app = (IMbsApplication) Globals.app;
		List<IIsemriSoru> list = app.getIsemriSorular(this);
		for (IIsemriSoru soru : list) {
			soru.setTESISAT_NO(getTESISAT_NO());
			soru.setSAHA_ISEMRI_NO(getSAHA_ISEMRI_NO());
		}
		return list;
	}

	@Override
	public ILocation getLocation() {
		// TODO Auto-generated method stub
		if (getCBS() == null)
			return null;
		try {
			return getCBS().toLocation();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return String.format("%d - %s", getTESISAT_NO(), getUNVAN());
	}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return ((IElecApplication) Globals.app).getIslemRenk(getISLEM_TIPI());
	}

	@Override
	public String getDetail() {
		// TODO Auto-generated method stub
		return getADRES();
	}

	@Override
	public boolean isOSOS()
	{
		String s = getADRES_TARIF();
		if(s != null && s.contains("OSOS")) return true;
		if(getISLEM_TIPI() != null && getISLEM_TIPI().equals(IslemTipi.Tespit) && getALT_EMIR_TURU() == 1011)
			return true;

		return false;
	}

	private static final String query9 = String.format("select %s, %s, count(*) from %s group by %s, %s order by %s, %s",
			field.ISLEM_TIPI, field.ISEMRI_DURUMU, tableName, field.ISLEM_TIPI, field.ISEMRI_DURUMU, field.ISLEM_TIPI, field.ISEMRI_DURUMU);

	public static IIsemriRapor getIsemriRapor(Connection conn) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		IsemriRapor rapor = new IsemriRapor();
		try {

			stmt = conn.prepareStatement(query9);
			rs = stmt.executeQuery();
			while (rs.next()) {
				rapor.add(new IsemriRapor.Item(IslemTipi.fromInteger(rs.getInt(1)),
						IslemDurum.fromInteger(rs.getInt(2)), rs.getInt(3)));
			}

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
		return rapor;
	}

	private static final String query10 = String.format("delete from %s where %s = ? and %s <> ? and %s = ?",
			tableName, field.KARNE_NO, field.ISLEM_TIPI, field.ISEMRI_DURUMU);

	public static void isemriKarneSil(Connection conn, int KARNE_NO) throws Exception {

		PreparedStatement stmt = null;
		boolean tran = false;
		try {

			conn.setAutoCommit(false);
			tran = true;

			stmt = conn.prepareStatement(query10);
			stmt.setInt(1, KARNE_NO);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			stmt.setInt(3, IslemDurum.Atanmis.getValue());
			stmt.execute();


		} catch (Exception e) {
			if(tran) conn.rollback();
			throw e;

		} finally {

			if (tran)
				conn.setAutoCommit(true);

			if (stmt != null)
				stmt.close();
		}

	}

	private static final String query11 = String.format("delete from %s where %s = ? and %s <> ?",
			tableName, field.TESISAT_NO, field.ISLEM_TIPI);

	public static void isemriTesisatSil(Connection conn, int TESISAT_NO) throws Exception {

		PreparedStatement stmt = null;
		boolean tran = false;
		try {

			conn.setAutoCommit(false);
			tran = true;

			stmt = conn.prepareStatement(query11);
			stmt.setInt(1, TESISAT_NO);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			stmt.execute();


		} catch (Exception e) {
			if(tran) conn.rollback();
			throw e;

		} finally {

			if (tran)
				conn.setAutoCommit(true);

			if (stmt != null)
				stmt.close();
		}

	}

	private static final String query12 = String.format("delete from %s where %s = ? and %s = ?",
			tableName, field.KARNE_NO, field.ISLEM_TIPI);

	public static void okumaKarneSil(Connection conn, int KARNE_NO) throws Exception {

		PreparedStatement stmt = null;
		boolean tran = false;
		try {

			conn.setAutoCommit(false);
			tran = true;

			stmt = conn.prepareStatement(query12);
			stmt.setInt(1, KARNE_NO);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			stmt.execute();


		} catch (Exception e) {
			if(tran) conn.rollback();
			throw e;

		} finally {

			if (tran)
				conn.setAutoCommit(true);

			if (stmt != null)
				stmt.close();
		}

	}

	private static final String query13 = String.format("delete from %s where %s = ? and %s = ?",
			tableName, field.TESISAT_NO, field.ISLEM_TIPI);

	public static void okumaTesisatSil(Connection conn, int KARNE_NO) throws Exception {

		PreparedStatement stmt = null;
		boolean tran = false;
		try {

			conn.setAutoCommit(false);
			tran = true;

			stmt = conn.prepareStatement(query13);
			stmt.setInt(1, KARNE_NO);
			stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
			stmt.execute();


		} catch (Exception e) {
			if(tran) conn.rollback();
			throw e;

		} finally {
			if (tran)
				conn.setAutoCommit(true);

			if (stmt != null)
				stmt.close();
		}

	}

	@Override
	public void setMasterId(Integer masterId) {
		// TODO Auto-generated method stub

	}

	//-------------------------------------------------------------------------

	private static final String query14 = String.format("select a.* from %s i" +
					" inner join %s ii on i.%s = ii.%s" +
					" inner join %s a on ii.%s = a.%s" +
					" where i.%s = ? ", IslemMaster.tableName,
			put_isemri.tableName, IslemMaster.sID, field.ISLEM_ID,
			isemri_guncelle.tableName, field.SAHA_ISEMRI_NO, field.SAHA_ISEMRI_NO,
			IslemMaster.sDURUM, field.SAHA_ISEMRI_NO);



	public static List<IIsemri> bekleyenListesi(Connection conn, RecordStatus status, IslemTipi islemTipi) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemri> list = new ArrayList<IIsemri>();
		try {

			stmt = conn.prepareStatement(query14);
			stmt.setInt(1, status.getValue());
			rs = stmt.executeQuery();
			while (rs.next()) {
				isemri_guncelle isemri = new isemri_guncelle();
				isemri.get(rs);
				list.add(isemri);
			}

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
		return list;
	}

	//-------------------------------------------------------------------------

	private static final String query15 = String.format("select 0 %s, 0 %s, %s, %s, count(*) %s from %s where %s < ? group by %s, %s",
			field.ID, field.ZAMAN_KODU, field.KARNE_NO, field.ISLEM_TIPI, field.ADET,
			isemri_guncelle.tableName, field.ISLEM_TIPI,
			field.KARNE_NO, field.ISLEM_TIPI);

	public static List<IKarne> isemriKarneListesi(Connection conn) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IKarne> list = new ArrayList<IKarne>();
		try {

			stmt = conn.prepareStatement(query15);
			stmt.setInt(1, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();
			while (rs.next()) {
				karne karne = new karne();
				karne.get(rs);
				list.add(karne);
			}

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
		return list;
	}
	//muhammed gökkaya tesisat bilgi
	//-------------------------------------------------------------------------
	private static final String query16 = String.format("select * from %s where %s < ?",
			isemri_guncelle.tableName, field.ISLEM_TIPI);

	public static List<IIsemri> isemriListesi(Connection conn) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIsemri> list = new ArrayList<IIsemri>();
		try {

			stmt = conn.prepareStatement(query16);
			stmt.setInt(1, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();

			while (rs.next()) {
				isemri_guncelle isemri = new isemri_guncelle();
				isemri.get(rs);
				list.add(isemri);
			}

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
		return list;
	}

	private static final String query17 = String.format("select 0 %s, 0 %s, %s, %s, count(*) %s from %s where %s = ? group by %s, %s",
			field.ID, field.ZAMAN_KODU, field.KARNE_NO, field.ISLEM_TIPI, field.ADET,
			isemri_guncelle.tableName, field.ISLEM_TIPI,
			field.KARNE_NO, field.ISLEM_TIPI);

	public static List<IKarne> okumaKarneListesi(Connection conn) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IKarne> list = new ArrayList<IKarne>();
		try {

			stmt = conn.prepareStatement(query17);
			stmt.setInt(1, IslemTipi.SayacOkuma.getValue());
			rs = stmt.executeQuery();
			while (rs.next()) {
				karne karne = new karne();
				karne.get(rs);
				list.add(karne);
			}

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
		return list;
	}

}
