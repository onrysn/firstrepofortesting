package mobit.elec.mbs.get;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.mobit.Globals;

public interface field {

	
	public static final int OK_LEN = 2;
	public static final int ERR_LEN = 3;
	public static final int FTL_LEN = 3;
	public static final int WRN_LEN = 3;
	public static final int PRN_LEN = 3;
	public static final int SYS_LEN = 3;

	public static final int s_VER = 4;				/* NUM 9(4). */

	public static final int s_ENDEKS_PREC = 3;
	public static final int s_ENDEKS = 11;			/* NUM 9(7).999 Demant Endeksi */
	public static final int s_TARIH = 8;			/* NUM 9(8) YYYYAAGG Formatında tamamlandığı tarih */
	public static final int s_YIL = 4;				/* NUM 9(4). */
	public static final int s_DEMAND_ENDEKS = 8;	/* NUM 9(3).9999  Demant Endeksi */
	public static final int s_DEMAND_PREC = 4;	/* NUM 9(3).9999  Demant Endeksi */
	public static final int s_TUTAR_PREC = 2;
	public static final int s_CARPAN_PREC = 3;
	
	public static final int s_OKUYUCU_KODU = 4;	/* 9999 */
	public static final int s_OKUYUCU_SIFRE = 8;	/* CHAR(8) */
	public static final int s_OKUYUCU_ADI = 30;	/* CHAR(30) */
	public static final int s_ELEMAN_KODU = 4;		/* 9999 */
	public static final int s_ELEMAN_ADI = 30;		/* CHAR(30) */
	public static final int s_ELEMAN_TARIH = 6;		/* NUM 9(8) YYAAGG Formatında tamamlandığı tarih */
	public static final int s_ELEMAN_SIFRE = 8;	/* CHAR(8) */
	public static final int s_KULLANICI_DUR = 1;	/* CHAR(1) */

	public static final int s_ZAMAN_KODU = 8;		/* CHAR(8)	Saniye cinsinden zaman kodu hex 32-bit */
	public static final int s_HAREKET_KODU = 1;	/* CHAR(1)	A: İlave Kayıt, U: Güncelleme */
	public static final int s_SAHA_ISEMRI_NO = 8;	/* NUM 9(8) Saha işemri Numarası */
	public static final int s_ISLEM_TIPI = 1;		/* NUM 9 0. Bilgi, 1. Kesme, 2. Açma, 3. İhbar, 4. Kontrol, 5.Sayac Değiştir, 6.Sayaç Tak */
	public static final int s_ISEMRI_TARIHI = s_TARIH;	/* NUM 9(8) YYYYAAGG Formatında tarih */
	public static final int s_ISEMRI_DURUMU = 1;	/* NUM 9 0. Serbest,1.Atanmış, 2.İptal, 3.Tamamlandı, 4.Yapılamadı, 5.Yapılmadı  */
	public static final int s_ATANMIS_GOREVLI = 4;	/* NUM 9999 Atandığı Görevli */
	public static final int s_ISLEM_TARIHI = s_TARIH;	/* NUM 9(8) YYYYAAGG Formatında tamamlandığı tarih */
	public static final int s_KARNE_NO = 7;		/* NUM 9(7) Karne No */
	public static final int s_TEDARIK_DUR = 1;		/* 0: Tüm aboneler indirilir 1: ST aboneler hariç indirilir 2: ST aboneler indirilir*/
	public static final int s_TESISAT_NO = 8;		/* NUM 9(8) Tesisat No */
	public static final int s_SIRA_NO = 4;			/* NUM 9999 Sıra No */
	public static final int s_SIRA_EK = 3;			/* NUM 999  Sıra No Ek */
	public static final int s_UNVAN = 70;			/* */
	public static final int s_ADRES = 70;			/* CHAR(70) Abone Adresi */
	public static final int s_ADRES_TARIF = 70;	/* CHAR(70) Okuma elemanına adres notları */
	public static final int s_SEMT = 20;			/* CHAR(20) Semt Adı */
	public static final int s_CIFT_TERIM_DUR = 1; 	/* NUM 9 Çift terim abone ise 1 gelecek */
	public static final int s_PUANT_DUR = 1; 		/* NUM 9 Puant abone ise 1 gelecek */
	public static final int s_SAYAC_DURUM_KODU = 2;/* NUM 99 */
	public static final int s_GIDEN_DURUM_KODU = s_SAYAC_DURUM_KODU; /* NUM 99 Giden Durum Kodu */
	public static final int s_TARIFE_KODU = 3;		/* NUM 999 Tüketim Tarife Kodu */
	public static final int s_CARPAN = 9;			/* NUM 9999.9999  çarpan Nokta dahil 9 hane */
	public static final int s_SON_OKUMA_TARIHI = s_TARIH; /* NUM 99999999 Son Okuma Tarihi YYYYAAGG */
	public static final int s_ANET_ISLETME_KODU = 11;/* CHAR(11) 11 HANE işletme kodu */
	public static final int s_ANET_ABONE_NO = 11;	/* CHAR(11) 11 HANE abone no */
	public static final int s_KESME_DUR = 3;		/* CHAR(3) Kesik Durumu */
	public static final int s_KESME_DUR_T = 1;		/* CHAR(1) K:Kesik */
	public static final int s_BORC_ADET = 3;		/* NUM 999 */
	public static final int s_BORC_TUTARI = 15;	/* NUM 9(12).99 */
	public static final int s_BORC_GECIKME = 15;	/* NUM 9(12).99 */
	public static final int s_ALT_ISLEM_TIPI = 2;	/* NUM 99 Alt işlem kodu, sayaç değişiklikte 7-tümü,1=aktif,2=reaktif,4=kapasitif */
	public static final int s_TELEFON = 10;		/* NUM 9(10) VER1112 */
	public static final int s_TELEFON2 = 10;      /* NUM 9(10) VER1112 */
	public static final int s_CEP_TELEFON = 10;	/* NUM 9(10) VER1112 */
	public static final int s_ISEMRI_ACIKLAMA = 100;/* CHAR(100) işemri ile ilgili açıklama VER1112 */
	public static final int s_ALT_EMIR_TURU = 4;
	public static final int s_EMIR_TURU = 2;

	public static final int s_ADET = 8;			/* NUM 9(8) */
	public static final int s_SON_ENDEKS = s_ENDEKS;/* NUM 9(7).999	Son Endeks */
	public static final int s_GELEN_DURUM_KODU = 2;/* NUM 99	Gelen Sayaç Durum Kodu */
	public static final int s_CEKILEN_GUC = 8;		/* NUM 9(8) Demant çekilen Güç */
	public static final int s_OKUMA_ACIKLAMA = 94;	/*CHAR(94)	Okuma Açıklama, BASE64 kodlanacak */
	public static final int s_SEVIYE = 1;			/* NUM 9 */
	public static final int s_KOD = 10;			/* NUM 9(10). */
	public static final int s_ACIKLAMA = 20;		/* CHAR(20). */
	public static final int s_CBS_X = 11;			/* NUM 999.9999999 */
	public static final int s_CBS_Y = 11;			/* NUM 999.9999999 */
	public static final int s_CBS_EK1 = 3;			/* NUM 999. */
	public static final int s_CBS_EK = 30;			/* CHAR(30). */
	public static final int s_CBS_PREC = 7;

	public static final int s_MUHUR_SERI = 1;		/* CHAR(1) */
	public static final int s_MUHUR_NO = 8;		/* NUM 9(8) */
	public static final int s_MUHUR_YERI = 2;		/* NUM 99 */
	public static final int s_MUHUR_TARIHI = s_TARIH;	/* NUM 99999999	/* Mühür Tarihi YYYYAAGG */
	public static final int s_MUHUR_NEDENI = 2;	/* NUM 99 */
	public static final int s_MUHUR_DUR = 2;		/* NUM 99 Mühür Durumu */
	public static final int s_MUHUR_DURUMU = s_MUHUR_DUR;
	public static final int s_KOD_CINSI = 1;		/* CHAR(1) "N" - neden, "Y" - yer, "D" - durum, "I" - iptal */
	public static final int s_MUHUR_KODU = 2;		/* NUM 99 */
	public static final int s_MUHUR_ACIKLAMA = 40;	/* CHAR(40) */


	public static final int s_IPTAL_DUR = 2;		/* NUM 99 */
	
	public static final int s_SAYAC_KODU = 1;		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final int s_MARKA = 3;			/* CHAR(3). */
	public static final int s_SAYAC_NO = 9;		/* NUM 9(9). */
	public static final int s_SAYAC_CINSI = 1;     /* NUM 9. 0:Mekanik,1:Elektronik,2:Kombi M: mekanik, E: Elektronik, K:Kombi */
	public static final int s_IMAL_YILI = s_YIL;	/* NUM 9(4). */
	public static final int s_HANE_SAYISI = 1;		/* NUM 9 Sayaç hane sayısı */
	public static final int s_DIJIT = s_HANE_SAYISI;/* NUM 9. Hane Sayısı */
	public static final int s_DAMGA_TARIHI = s_TARIH;/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final int s_MULKIYET = 1;		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */
	
	public static final int s_SAYAC_KODU_1 = s_SAYAC_KODU;		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final int s_MARKA_1 = s_MARKA;			/* CHAR(3). */
	public static final int s_SAYAC_NO_1 = s_SAYAC_NO;		/* NUM 9(9). */
	public static final int s_SAYAC_CINSI_1 = s_SAYAC_CINSI;     /* NUM 9. 0:Mekanik,1:Elektronik,2:Kombi M: mekanik, E: Elektronik, K:Kombi */
	public static final int s_IMAL_YILI_1 = s_IMAL_YILI;	/* NUM 9(4). */
	public static final int s_HANE_SAYISI_1 = s_HANE_SAYISI;		/* NUM 9 Sayaç hane sayısı */
	public static final int s_DAMGA_TARIHI_1 = s_DAMGA_TARIHI;/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final int s_MULKIYET_1 = s_MULKIYET;		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */
	
	public static final int s_SAYAC_KODU_2 = s_SAYAC_KODU;		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final int s_MARKA_2 = s_MARKA;			/* CHAR(3). */
	public static final int s_SAYAC_NO_2 = s_SAYAC_NO;		/* NUM 9(9). */
	public static final int s_SAYAC_CINSI_2 = s_SAYAC_CINSI;     /* NUM 9. 0:Mekanik,1:Elektronik,2:Kombi M: mekanik, E: Elektronik, K:Kombi */
	public static final int s_IMAL_YILI_2 = s_YIL;	/* NUM 9(4). */
	public static final int s_HANE_SAYISI_2 = s_HANE_SAYISI;		/* NUM 9 Sayaç hane sayısı */
	public static final int s_DIJIT_2 = s_HANE_SAYISI;/* NUM 9. Hane Sayısı */
	public static final int s_DAMGA_TARIHI_2 = s_TARIH;/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final int s_MULKIYET_2 = s_MULKIYET;		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */
	
	public static final int s_SAYAC_KODU_3 = s_SAYAC_KODU;		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final int s_MARKA_3 = s_MARKA;			/* CHAR(3). */
	public static final int s_SAYAC_NO_3 = s_SAYAC_NO;		/* NUM 9(9). */
	public static final int s_SAYAC_CINSI_3 = s_SAYAC_CINSI;     /* NUM 9. 0:Mekanik,1:Elektronik,2:Kombi M: mekanik, E: Elektronik, K:Kombi */
	public static final int s_IMAL_YILI_3 = s_YIL;	/* NUM 9(4). */
	public static final int s_HANE_SAYISI_3 = s_HANE_SAYISI;		/* NUM 9 Sayaç hane sayısı */
	public static final int s_DIJIT_3 = s_HANE_SAYISI;/* NUM 9. Hane Sayısı */
	public static final int s_DAMGA_TARIHI_3 = s_TARIH;/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final int s_MULKIYET_3 = s_MULKIYET;		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */

	public static final int s_MODEL_KODU = 5;		/* NUM 99999 İleri aşamada gerekebilecek model kodu */
	
	// Sadece elektrik uygulamasında
	public static final int s_FAZ_SAYISI = 1;		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final int s_AMPERAJ = 3;			/* NUM 999 Amperaj */
	public static final int s_VOLTAJ = 3;			/* NUM 999 220, 380 */
	
	public static final int s_FAZ_SAYISI_1 = s_FAZ_SAYISI;		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final int s_AMPERAJ_1 = s_AMPERAJ;			/* NUM 999 Amperaj */
	public static final int s_VOLTAJ_1 = s_VOLTAJ;			/* NUM 999 220, 380 */
	public static final int s_MODEL_KODU_1 = s_MODEL_KODU;
	public static final int s_GIDEN_DURUM_KODU_1 = s_GIDEN_DURUM_KODU;
	
	
	public static final int s_FAZ_SAYISI_2 = s_FAZ_SAYISI;		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final int s_AMPERAJ_2 = s_AMPERAJ;			/* NUM 999 Amperaj */
	public static final int s_VOLTAJ_2 = s_VOLTAJ;			/* NUM 999 220, 380 */
	public static final int s_MODEL_KODU_2 = s_MODEL_KODU;
	public static final int s_GIDEN_DURUM_KODU_2 = s_GIDEN_DURUM_KODU;
	
	public static final int s_FAZ_SAYISI_3 = s_FAZ_SAYISI;		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final int s_AMPERAJ_3 = s_AMPERAJ;			/* NUM 999 Amperaj */
	public static final int s_VOLTAJ_3 = s_VOLTAJ;			/* NUM 999 220, 380 */
	public static final int s_MODEL_KODU_3 = s_MODEL_KODU;
	public static final int s_GIDEN_DURUM_KODU_3 = s_GIDEN_DURUM_KODU;

	// Sadece su uygulamasında
	public static final int s_SAYAC_CAPI = 2;		/* NUM 99 */
	public static final int s_REKORLU_DUR  = 1;		/* NUM 9  */
	public static final int s_SAYAC_SINIFI = 1;	/* NUM 9  */

	
	public static final int s_SAYAC_KODU_ACIKLAMA = 40; /* CHAR(40) */
	public static final int s_ISLEM_KODU = 1;		/* NUM 9 */
	public static final int s_SAYAC_MARKA_KODU = 3; /* CHAR(3) */
	public static final int s_SAYAC_MARKA_ADI = 20; /* CHAR(20) */
	public static final int s_SAYAC_CINS_KODU = 1;	 /* NUM 9 */
	public static final int s_ABONE_DURUM_KODU = 2;/* NUM 99 */
	public static final int s_DURUM_KODU_ACIKLAMA = 40;/* CHAR(40) */
	public static final int s_ACIKLAMA_DUR = 1;	/* NUM 9. */
	public static final int s_FILLER = 1;			/* CHAR(1)	<SPACE> */
	public static final int s_SAAT = 6;			/* NUM 9(6)	HHMMSS */
	public static final int s_RESET_DUR = 1;		/* CHAR(1) "R" ise Cihaz Reset */

	public static final int s_VERGI_NO = 10;		/* NUM 9(10) */
	public static final int s_TC_KIMLIK_NO = 11;	/* NUM 9(11) */

	public static final int s_SINYAL_SEVIYESI = 1;	/* CHAR(1)		Harf kodlanmış sinyal seviyesi */
	public static final int s_GELEN_DURUM_1 = 2;	/* NUM 99		Gelen Abone Durum Kodu */
	public static final int s_GELEN_DURUM_2 = 2;	/* NUM 99		Gelen Abone Durum Kodu */ 
	public static final int s_GELEN_DURUM_3 = 2;	/* NUM 99		Gelen Abone Durum Kodu */ 

	public static final int s_FATURA_SERI = 1;		/* CHAR(1) */
	public static final int s_FATURA_NO = 8;		/* NUM 9(8)*/

	public static final int s_T = 1;				/* A veya boşluk olursa ASCII mod, B ise binary */
	public static final int s_V = 1;				/* S veya boşluk ise sabit genişlikli, D ise TAB'la ayrılmış */
	public static final int s_MMM = 3;				/* MOBIT (IT3000) için 001 olacak */
	public static final int s_BUILD_DATE = s_TARIH;
	public static final int s_TYPE = 3;
	public static final int s_MVERSION = 10;		/* CHAR(10) Terminal yazılımı versiyonu*/

	public static final int s_SOZLESME_DUR = 1;	/* CHAR(1) I: Sözleşmesi iptal edilmiş */
	public static final int s_KONTROL_SAYAC_DUR = 1;/* NUM 9 Kontrol sayaç var */

	public static final int s_DEMANT_ENDEKS = s_ENDEKS;
	public static final int s_ESKI_SIFRE = s_OKUYUCU_SIFRE;
	public static final int s_YENI_SIFRE = s_OKUYUCU_SIFRE;

	public static final int s_SORU_NO = 2;
	public static final int s_SORU_ACIKLAMA = 70;
	public static final int s_SORU_ACIKLAMA_SORULAR = 256;
	public static final int s_CEVAP_TIPI = 1;
	public static final int s_CEVAP_TIPI_SORULAR = 2;
	public static final int s_CEVAP_FORMAT = 70;
	public static final int s_CEVAP_FORMAT_SORULAR = 140;
	public static final int s_CEVAP = 70;
	
	public static final int s_RTIP = 3;
	public static final int s_RKOD = 3;
	public static final int s_RACIKLAMA = 40;

	public static final int s_PKOD = 10;
	public static final int s_UAVT_ACIKLAMA = 30;

	public static final int s_GONDERILDI = 1;
	public static final int s_ISLEM = 1;
	public static final int s_RECNO = 10;
	public static final int s_ID = 8;
	public static final int s_ZAMAN = 14;
	public static final int s_YAPILDI = 1;

	public static final int s_ACMA_KAPAMA_BEDELI = 15;

	public static final int s_ZABIT_SERI = 1;
	public static final int s_ZABIT_NO = 8;
	public static final int s_ZABIT_TIPI = 1;

	public static final int s_MESAJ = 255;

	public static final int s_OPTIK_DATA = (3*1024);

	public static final int s_OKUMA_METODU = 1;

	public static final int s_CBS_ENLEM = 10;
	public static final int s_CBS_BOYLAM = 10;


	//Onur güncelleme bilgisi gelince eklendi
	//public static final int s_BINA_KODU = 10;
	//public static final int s_SOZLESME_NO = 8;

	//H.Elif OG-AG bilgisi gelince eklendi
	public static final int s_OG_DUR = 1;
	public static final int s_OLCUM_KODU = 1;
	public static final int s_HESAP_KODU = 2;
	public static final int s_SOZLESME_GUCU = 8;
	public static final int s_KURULU_GUC = 8;
	public static final int s_ORTAK_TRAFO_DURUMU = 1;






	//---------------------------------------------------------
	
		/*
	public static char IT_Bilgi = "0";
	public static char IT_Kesme = "1"; 
	public static char IT_Acma = "2";
	public static char IT_Ihbar = "3"; 
	public static String IT_Kontrol = "4"; 
	public static String IT_SayacDegistir = "5"; 
	public static String IT_SayacTak = "6";
	public static String IT_Tespit = "7";
	*/

	public static final DateFormat date_formatter =  new SimpleDateFormat("yyyyMMdd", Globals.trLocale);
	public static final DateFormat year_formatter =  new SimpleDateFormat("yyyy", Globals.trLocale);
	public static final DateFormat date_formatter2 =  new SimpleDateFormat("yyMMdd", Globals.trLocale);
	public static final DateFormat datetime_formatter =  new SimpleDateFormat("yyMMddHHmmss", Globals.trLocale);

	//public static String cbs_format = "CBS:%0"+field.s_CBS_X+"."+field.s_CBS_PREC+"f;%0"+field.s_CBS_Y+"."+field.s_CBS_PREC+"f\t";
	

	public static final String ENDEKS = "ENDEKS";			/* NUM 9(7).999 Demant Endeksi */
	public static final String TARIH = "TARIH";			/* NUM 9(8) YYYYAAGG Formatında tamamlandığı tarih */
	public static final String YIL = "YIL";				/* NUM 9(4). */
	public static final String DEMAND_ENDEKS = "DEMAND_ENDEKS";	/* NUM 9(3).9999  Demant Endeksi */
	
	public static final String OKUYUCU_KODU = "OKUYUCU_KODU";	/* 9999 */
	public static final String OKUYUCU2_KODU = "OKUYUCU2_KODU";	/* 9999 */
	public static final String OKUYUCU_SIFRE = "OKUYUCU_SIFRE";	/* CHAR(6) */
	public static final String OKUYUCU_ADI = "OKUYUCU_ADI";	/* CHAR(30) */
	public static final String ELEMAN_KODU = "ELEMAN_KODU";		/* 9999 */
	public static final String ELEMAN_ADI = "ELEMAN_ADI";		/* CHAR(30) */
	public static final String ELEMAN_TARIH = "ELEMAN_TARIH";		/* NUM 9(8) YYAAGG Formatında tamamlandığı tarih */
	public static final String ELEMAN_SIFRE = "ELEMAN_SIFRE";	/* CHAR(6) */
	public static final String KULLANICI_DUR = "KULLANICI_DUR";	/* CHAR(1) */

	public static final String ZAMAN_KODU = "ZAMAN_KODU";		/* CHAR(8)	Saniye cinsinden zaman kodu hex 32-bit */
	public static final String HAREKET_KODU = "HAREKET_KODU";	/* CHAR(1)	A: İlave Kayıt, U: Güncelleme */
	public static final String SAHA_ISEMRI_NO = "SAHA_ISEMRI_NO";	/* NUM 9(8) Saha işemri Numarası */
	public static final String ISLEM_TIPI = "ISLEM_TIPI";		/* NUM 9 0. Bilgi, 1. Kesme, 2. Açma, 3. İhbar, 4. Kontrol, 5.Sayac Değiştir, 6.Sayaç Tak */
	public static final String ISEMRI_TARIHI = "ISEMRI_TARIHI";	/* NUM 9(8) YYYYAAGG Formatında tarih */
	public static final String ISEMRI_DURUMU = "ISEMRI_DURUMU";	/* NUM 9 0. Serbest,1.Atanmış, 2.İptal, 3.Tamamlandı, 4.Yapılamadı, 5.Yapılmadı  */
	public static final String ATANMIS_GOREVLI = "ATANMIS_GOREVLI";	/* NUM 9999 Atandığı Görevli */
	public static final String ISLEM_TARIHI = "ISLEM_TARIHI";	/* NUM 9(8) YYYYAAGG Formatında tamamlandığı tarih */
	public static final String KARNE_NO = "KARNE_NO";		/* NUM 9(7) Karne No */
	public static final String TEDARIK_DUR = "TEDARIK_DUR";		/* 0: Tüm aboneler indirilir 1: ST aboneler hariç indirilir 2: ST aboneler indirilir*/
	public static final String TESISAT_NO = "TESISAT_NO";		/* NUM 9(8) Tesisat No */
	public static final String SIRA_NO = "SIRA_NO";			/* NUM 9999 Sıra No */
	public static final String SIRA_EK = "SIRA_EK";			/* NUM 999  Sıra No Ek */
	public static final String UNVAN = "UNVAN";			/* CHAR(70) Abone Adı veya şirket Adı */
	public static final String ADRES = "ADRES";			/* CHAR(70) Abone Adresi */
	public static final String ADRES_TARIF = "ADRES_TARIF";	/* CHAR(70) Okuma elemanına adres notları */
	public static final String SEMT = "SEMT";			/* CHAR(20) Semt Adı */
	public static final String CIFT_TERIM_DUR = "CIFT_TERIM_DUR"; 	/* NUM 9 çift terim abone ise 1 gelecek */
	public static final String PUANT_DUR = "PUANT_DUR"; 		/* NUM 9 Puant abone ise 1 gelecek */
	public static final String SAYAC_DURUM_KODU = "SAYAC_DURUM_KODU";/* NUM 99 */
	public static final String GIDEN_DURUM_KODU = "GIDEN_DURUM_KODU"; /* NUM 99 Giden Durum Kodu */
	public static final String TARIFE_KODU = "TARIFE_KODU";		/* NUM 999 Tıketim Tarife Kodu */
	public static final String CARPAN = "CARPAN";			/* NUM 9999.9999  Çarpan Nokta dahil 9 hane */
	public static final String SON_OKUMA_TARIHI = "SON_OKUMA_TARIHI"; /* NUM 99999999 Son Okuma Tarihi YYYYAAGG */
	public static final String ANET_ISLETME_KODU = "ANET_ISLETME_KODU";/* CHAR(11) 11 HANE işletme kodu */
	public static final String ANET_ABONE_NO = "ANET_ABONE_NO";	/* CHAR(11) 11 HANE abone no */
	public static final String KESME_DUR = "KESME_DUR";		/* CHAR(3) Kesik Durumu */
	public static final String KESME_DUR_T = "KESME_DUR_T";		/* CHAR(1) K:Kesik */
	public static final String BORC_ADET = "BORC_ADET";		/* NUM 999 */
	public static final String BORC_TUTARI = "BORC_TUTARI";	/* NUM 9(12).99 */
	public static final String BORC_GECIKME = "BORC_GECIKME";	/* NUM 9(12).99 */
	public static final String ALT_ISLEM_TIPI = "ALT_ISLEM_TIPI";	/* NUM 99 Alt işlem kodu, sayaç değişiklikte 7-tümü,1=aktif,2=reaktif,4=kapasitif */
	public static final String TELEFON = "TELEFON";		/* NUM 9(10) VER1112 */
	public static final String TELEFON2 = "TELEFON2";      /* NUM 9(10) VER1112 */
	public static final String CEP_TELEFON = "CEP_TELEFON";	/* NUM 9(10) VER1112 */
	public static final String ISEMRI_ACIKLAMA = "ISEMRI_ACIKLAMA";/* CHAR(100) işemri ile ilgili açıklama VER1112 */
	public static final String ALT_EMIR_TURU = "ALT_EMIR_TURU";
	public static final String EMIR_TURU = "EMIR_TURU";

	public static final String ADET = "ADET";			/* NUM 9(8) */
	public static final String SON_ENDEKS = "SON_ENDEKS";/* NUM 9(7).999	Son Endeks */
	public static final String SON_ENDEKS_1 = "SON_ENDEKS_1";/* NUM 9(7).999	Son Endeks */
	public static final String SON_ENDEKS_2 = "SON_ENDEKS_2";/* NUM 9(7).999	Son Endeks */
	public static final String SON_ENDEKS_3 = "SON_ENDEKS_3";/* NUM 9(7).999	Son Endeks */
	public static final String SON_ENDEKS_4 = "SON_ENDEKS_4";/* NUM 9(7).999	Son Endeks */
	public static final String SON_ENDEKS_5 = "SON_ENDEKS_5";/* NUM 9(7).999	Son Endeks */
	public static final String GELEN_DURUM_KODU = "GELEN_DURUM_KODU";/* NUM 99	Gelen Sayaç Durum Kodu */
	public static final String GELEN_DURUM_KODU_1 = "GELEN_DURUM_KODU_1";/* NUM 99	Gelen Sayaç Durum Kodu */
	public static final String GELEN_DURUM_KODU_2 = "GELEN_DURUM_KODU_2";/* NUM 99	Gelen Sayaç Durum Kodu */
	public static final String GELEN_DURUM_KODU_3 = "GELEN_DURUM_KODU_3";/* NUM 99	Gelen Sayaç Durum Kodu */
	public static final String GELEN_DURUM_KODU_4 = "GELEN_DURUM_KODU_4";/* NUM 99	Gelen Sayaç Durum Kodu */
	public static final String GELEN_DURUM_KODU_5 = "GELEN_DURUM_KODU_5";/* NUM 99	Gelen Sayaç Durum Kodu */
	
	public static final String CEKILEN_GUC = "CEKILEN_GUC";		/* NUM 9(8) Demant çekilen Güç */
	public static final String OKUMA_ACIKLAMA ="OKUMA_ACIKLAMA";	/*CHAR(94)	Okuma Açıklama, BASE64 kodlanacak */
	public static final String SEVIYE = "SEVIYE";			/* NUM 9 */
	public static final String KOD = "KOD";			/* NUM 9(10). */
	public static final String ACIKLAMA = "ACIKLAMA";		/* CHAR(20). */
	public static final String CBS_X = "CBS_X";			/* NUM 999.9999999 */
	public static final String CBS_Y = "CBS_Y";			/* NUM 999.9999999 */
	public static final String CBS_EK1 = "CBS_EK1";			/* NUM 999. */
	public static final String CBS_EK = "CBS_EK";			/* CHAR(30). */
	public static final String CBS_PREC = "CBS_PREC";

	public static final String MUHUR_SERI = "MUHUR_SERI";		/* CHAR(1) */
	public static final String MUHUR_NO = "MUHUR_NO";		/* NUM 9(8) */
	public static final String MUHUR_YERI = "MUHUR_YERI";		/* NUM 99 */
	public static final String MUHUR_TARIHI = "MUHUR_TARIHI";	/* NUM 99999999	/* Mühür Tarihi YYYYAAGG */
	public static final String MUHUR_NEDENI = "MUHUR_NEDENI";	/* NUM 99 */
	public static final String MUHUR_DUR = "MUHUR_DUR";		/* NUM 99 Mühür Durumu */
	public static final String MUHUR_DURUMU = "MUHUR_DURUMU";
	public static final String KOD_CINSI = "KOD_CINSI";		/* CHAR(1) "N" - neden, "Y" - yer, "D" - durum, "I" - iptal */
	public static final String MUHUR_KODU = "MUHUR_KODU";		/* NUM 99 */
	public static final String MUHUR_ACIKLAMA = "MUHUR_ACIKLAMA";	/* CHAR(40) */


	public static final String IPTAL_DUR = "IPTAL_DUR";		/* NUM 99 */
	
	public static final String SAYAC_KODU = "SAYAC_KODU";		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final String MARKA = "MARKA";			/* CHAR(3). */
	public static final String SAYAC_NO = "SAYAC_NO";		/* NUM 9(9). */
	public static final String SAYAC_CINSI = "SAYAC_CINSI";     /* NUM 9. 0:Mekanik,1:Elektronik,2:Kombi M: mekanik, E: Elektronik, K:Kombi */
	public static final String IMAL_YILI = YIL;	/* NUM 9(4). */
	public static final String HANE_SAYISI = "HANE_SAYISI";		/* NUM 9 Sayaç hane sayısı */
	public static final String DIJIT = "DIJIT";/* NUM 9. Hane Sayısı */
	public static final String DAMGA_TARIHI = "DAMGA_TARIHI";/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final String MULKIYET = "MULKIYET";		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */

	public static final String SAYAC_KODU_1 = "SAYAC_KODU_1";		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final String MARKA_1 = "MARKA_1"; 
	public static final String SAYAC_NO_1 = "SAYAC_NO_1"; 
	public static final String SAYAC_CINSI_1 = "SAYAC_CINSI_1"; 
	public static final String HANE_SAYISI_1 = "HANE_SAYISI_1"; 
	public static final String GIDEN_DURUM_KODU_1 = "GIDEN_DURUM_KODU_1";
	public static final String IMAL_YILI_1 = "IMAL_YILI_1";
	public static final String DAMGA_TARIHI_1 = "DAMGA_TARIHI_1";/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final String MULKIYET_1 = "MULKIYET_1";		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */
	
	public static final String SAYAC_KODU_2 = "SAYAC_KODU_2";		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final String MARKA_2 = "MARKA_2"; 
	public static final String SAYAC_NO_2 = "SAYAC_NO_2"; 
	public static final String SAYAC_CINSI_2 = "SAYAC_CINSI_2"; 
	public static final String HANE_SAYISI_2 = "HANE_SAYISI_2"; 
	//public static final String GIDEN_DURUM_KODU_2 = "GIDEN_DURUM_KODU_2";
	public static final String IMAL_YILI_2 = "IMAL_YILI_2";
	public static final String DAMGA_TARIHI_2 = "DAMGA_TARIHI_2";/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final String MULKIYET_2 = "MULKIYET_2";		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */
	
	public static final String SAYAC_KODU_3 = "SAYAC_KODU_3";		/* NUM 9. 1.Aktif,4:Reaktif,5:Kapasitif */
	public static final String MARKA_3 = "MARKA_3"; 
	public static final String SAYAC_NO_3 = "SAYAC_NO_3"; 
	public static final String SAYAC_CINSI_3 = "SAYAC_CINSI_3"; 
	public static final String HANE_SAYISI_3 = "HANE_SAYISI_3"; 
	public static final String GIDEN_DURUM_KODU_3 = "GIDEN_DURUM_KODU_3";
	public static final String IMAL_YILI_3 = "IMAL_YILI_3";
	public static final String DAMGA_TARIHI_3 = "DAMGA_TARIHI_3";/* NUM 9(8). YYYYAAGG formatında damga tarihi */
	public static final String MULKIYET_3 = "MULKIYET_3";		/* NUM 9. 0:Gecici Kurum Sayacı, 1:Abone Yeni Sayacı, 2:Abone Kendi Sayacı */
	
	public static final String MARKA_K1 = "MARKA_K1"; 
	public static final String SAYAC_NO_K1 = "SAYAC_NO_K1"; 
	public static final String SAYAC_CINSI_K1 = "SAYAC_CINSI_K1"; 
	public static final String HANE_SAYISI_K1 = "HANE_SAYISI_K1"; 
	public static final String GIDEN_DURUM_KODU_K1 = "GIDEN_DURUM_KODU_K1";
	public static final String IMAL_YILI_K1 = "IMAL_YILI_K1";
	
	public static final String MARKA_K2 = "MARKA_K2"; 
	public static final String SAYAC_NO_K2 = "SAYAC_NO_K2"; 
	public static final String SAYAC_CINSI_K2 = "SAYAC_CINSI_K2"; 
	public static final String HANE_SAYISI_K2 = "HANE_SAYISI_K2"; 
	public static final String GIDEN_DURUM_KODU_K2 = "GIDEN_DURUM_KODU_K2";
	public static final String IMAL_YILI_K2 = "IMAL_YILI_K2";
	
	public static final String MARKA_K3 = "MARKA_K3"; 
	public static final String SAYAC_NO_K3 = "SAYAC_NO_K3"; 
	public static final String SAYAC_CINSI_K3 = "SAYAC_CINSI_K3"; 
	public static final String HANE_SAYISI_K3 = "HANE_SAYISI_K3"; 
	public static final String GIDEN_DURUM_KODU_K3 = "GIDEN_DURUM_KODU_K3";
	public static final String IMAL_YILI_K3 = "IMAL_YILI_K3";
	
	// Sadece elektrik uygulamasında
	public static final String FAZ_SAYISI = "FAZ_SAYISI";		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final String AMPERAJ = "AMPERAJ";			/* NUM 999 Amperaj */
	public static final String VOLTAJ = "VOLTAJ";			/* NUM 999 220, 380 */
	public static final String MODEL_KODU = "MODEL_KODU";		/* NUM 99999 İleri aşamada gerekebilecek model kodu */
	
	public static final String FAZ_SAYISI_1 = "FAZ_SAYISI_1";		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final String AMPERAJ_1 = "AMPERAJ_1";			/* NUM 999 Amperaj */
	public static final String VOLTAJ_1 = "VOLTAJ_1";			/* NUM 999 220, 380 */
	public static final String MODEL_KODU_1 = "MODEL_KODU_1";		/* NUM 99999 İleri aşamada gerekebilecek model kodu */
	
	public static final String FAZ_SAYISI_2 = "FAZ_SAYISI_2";		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final String AMPERAJ_2 = "AMPERAJ_2";			/* NUM 999 Amperaj */
	public static final String VOLTAJ_2 = "VOLTAJ_2";			/* NUM 999 220, 380 */
	public static final String MODEL_KODU_2 = "MODEL_KODU_2";		/* NUM 99999 İleri aşamada gerekebilecek model kodu */
	
	public static final String FAZ_SAYISI_3 = "FAZ_SAYISI_3";		/* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	public static final String AMPERAJ_3 = "AMPERAJ_3";			/* NUM 999 Amperaj */
	public static final String VOLTAJ_3 = "VOLTAJ_3";			/* NUM 999 220, 380 */
	public static final String MODEL_KODU_3 = "MODEL_KODU_3";		/* NUM 99999 İleri aşamada gerekebilecek model kodu */

	// Sadece su uygulamasında
	public static final String SAYAC_CAPI = "SAYAC_CAPI";		/* NUM 99 */
	public static final String REKORLU_DUR  = "REKORLU_DUR";		/* NUM 9  */
	public static final String SAYAC_SINIFI = "SAYAC_SINIFI";	/* NUM 9  */

	
	public static final String SAYAC_KODU_ACIKLAMA = "SAYAC_KODU_ACIKLAMA"; /* CHAR(40) */
	public static final String ISLEM_KODU = "ISLEM_KODU";		/* NUM 9 */
	public static final String SAYAC_MARKA_KODU = "SAYAC_MARKA_KODU"; /* CHAR(3) */
	public static final String SAYAC_MARKA_ADI = "SAYAC_MARKA_ADI"; /* CHAR(20) */
	public static final String SAYAC_CINS_KODU = "SAYAC_CINS_KODU";	 /* NUM 9 */
	public static final String ABONE_DURUM_KODU = "ABONE_DURUM_KODU";/* NUM 99 */
	public static final String DURUM_KODU_ACIKLAMA = "DURUM_KODU_ACIKLAMA";/* CHAR(40) */
	public static final String ACIKLAMA_DUR = "ACIKLAMA_DUR";	/* NUM 9. */
	public static final String FILLER = "FILLER";			/* CHAR(1)	<SPACE> */
	public static final String SAAT = "SAAT";			/* NUM 9(6)	HHMMSS */
	public static final String RESET_DUR = "RESET_DUR";		/* CHAR(1) "R" ise Cihaz Reset */
	public static final String KOD_TIPI = "KOD_TIPI";	

	public static final String VERGI_NO = "VERGI_NO";		/* NUM 9(10) */
	public static final String TC_KIMLIK_NO = "TC_KIMLIK_NO";	/* NUM 9(11) */

	public static final String SINYAL_SEVIYESI = "SINYAL_SEVIYESI";	/* CHAR(1)		Harf kodlanmış sinyal seviyesi */
	public static final String GELEN_DURUM_1 = "GELEN_DURUM_1";	/* NUM 99		Gelen Abone Durum Kodu */
	public static final String GELEN_DURUM_2 = "GELEN_DURUM_2";	/* NUM 99		Gelen Abone Durum Kodu */ 
	public static final String GELEN_DURUM_3 = "GELEN_DURUM_3";	/* NUM 99		Gelen Abone Durum Kodu */ 

	public static final String FATURA_SERI = "FATURA_SERI";		/* CHAR(1) */
	public static final String FATURA_NO = "FATURA_NO";		/* NUM 9(8)*/

	public static final String T = "T";				/* A veya boşluk olursa ASCII mod, B ise binary */
	public static final String V = "V";				/* S veya boşluk ise sabit genişlikli, D ise TAB'la ayrılmış */
	public static final String MMM = "MMM";				/* MOBIT (IT3000) için 001 olacak */
	public static final String BUILD_DATE = "BUILD_DATE";
	public static final String TYPE = "TYPE";
	public static final String MVERSION = "MVERSION";		/* CHAR(10) Terminal yazılımı versiyonu*/

	public static final String SOZLESME_DUR = "SOZLESME_DUR";	/* CHAR(1) I: Sözleşmesi iptal edilmiş */
	public static final String KONTROL_SAYAC_DUR = "KONTROL_SAYAC_DUR";/* NUM 9 Kontrol sayaç var */

	public static final String DEMANT_ENDEKS = "DEMANT_ENDEKS";
	public static final String ESKI_SIFRE = "ESKI_SIFRE";
	public static final String YENI_SIFRE = "YENI_SIFRE";

	public static final String SORU_NO = "SORU_NO";
	public static final String SORU_ACIKLAMA = "SORU_ACIKLAMA";
	public static final String CEVAP_TIPI = "CEVAP_TIPI";
	public static final String CEVAP_FORMAT = "CEVAP_FORMAT";
	public static final String CEVAP = "CEVAP";
	
	public static final String RTIP = "RTIP";
	public static final String RKOD = "RKOD";
	public static final String RACIKLAMA = "RACIKLAMA";

	public static final String GIDEN_DURUM_KODU_2 = "GIDEN_DURUM_KODU_2";

	public static final String PKOD = "PKOD";
	public static final String UAVT_ACIKLAMA = "UAVT_ACIKLAMA";

	public static final String GONDERILDI = "GONDERILDI";
	public static final String ISLEM = "ISLEM";
	public static final String RECNO = "RECNO";
	public static final String ID = "ID";
	public static final String ZAMAN = "ZAMAN";
	public static final String YAPILDI = "YAPILDI";

	public static final String ACMA_KAPAMA_BEDELI = "ACMA_KAPAMA_BEDELI";

	public static final String ZABIT_SERI = "ZABIT_SERI";
	public static final String ZABIT_NO = "ZABIT_NO";
	public static final String ZABIT_TIPI = "ZABIT_TIPI";

	public static final String MESAJ = "MESAJ";

	public static final String OPTIK_DATA = "OPTIK_DATA";

	public static final String OKUMA_METODU = "OKUMA_METODU";

	public static final String CBS_ENLEM = "CBS_ENLEM";
	public static final String CBS_BOYLAM = "CBS_BOYLAM";

	public static final String ISLEM_ID = "ISLEM_ID";
	
	public static final String RESULT_TYPE = "RESULT_TYPE";
	public static final int s_RESULT_TYPE = 3;
	public static final String RESULT_CODE = "RESULT_CODE";
	public static final String RESULT = "RESULT";
	public static final int s_RESULT = 8*1024;
	
	public static final String DURUM = "DURUM";

	public static final String guc_tespit_cins = "Cins";
	public static final String guc_tespit_adet = "";
	public static final String guc_tespit_guc = "";

	public static final int s_guc_tespit_cins = 500;
	public static final int s_guc_tespit_adet = 8;
	public static final int s_guc_tespit_guc = 8;

	//Onur
	//public static final String BINA_KODU = "BINA_KODU";
	//public static final String SOZLESME_NO = "SOZLESME_NO";

	public static final String OG_DUR = "OG_DUR";
	public static final String OLCUM_KODU = "OLCUM_KODU";
	public static final String HESAP_KODU = "HESAP_KODU";
	public static final String SOZLESME_GUCU = "SOZLESME_GUCU";
	public static final String KURULU_GUC = "KURULU_GUC";
	public static final String ORTAK_TRAFO_DURUMU = "ORTAK_TRAFO_DURUMU";
}



