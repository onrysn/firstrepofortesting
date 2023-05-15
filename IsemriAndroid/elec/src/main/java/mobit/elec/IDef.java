package mobit.elec;

import java.util.regex.Pattern;

import com.mobit.Item;
import com.mobit.Operation;

public interface IDef extends com.mobit.IDef {

	public static final int FORM_ABONE_DURUM_GIRIS = 101;
	public static final int FORM_ISEMRI_ENDEKS = 102;
	public static final int FORM_ISEMRI_DETAY = 103;
	public static final int FORM_ISEMRI_INDIR = 104;
	public static final int FORM_ISEMRI_LISTE = 105;
	public static final int FORM_ISEMRI_MENU = 106;
	public static final int FORM_TESISAT_SORGU = 108;
	public static final int FORM_SAYAC_DURUM_GIRIS = 109;
	public static final int FORM_MUHUR_SOKME = 110;
	public static final int FORM_MUHUR_TAKMA = 111;
	public static final int FORM_TESPIT = 112;
	public static final int FORM_ZABIT = 113;
	public static final int FORM_VERITABANI_SIFIRLA = 114;
	public static final int FORM_ENDEKS = 115;
	public static final int FORM_ISLEM_TAMAMLA = 116;
	public static final int FORM_MUHUR_ISLEM_MENU = 117;
	public static final int FORM_SAYAC_ISLEM_TAMAMLA = 118;
	public static final int FORM_KACAK_GIRIS = 119;
	public static final int FORM_ISLEM_LISTE = 120;
	public static final int FORM_ISLEM_RAPOR = 121;
	public static final int FORM_ADRES_TARIF = 122;
	public static final int FORM_ISLEMLER = 123;
	public static final int FORM_KUYRUK_LISTE = 124;
	public static final int FORM_MUHUR_SORGU = 125;
	public static final int FORM_OKUMA_RAPOR = 126;
	public static final int FORM_YIKIK_GIRIS = 127;
	public static final int FORM_OLCU_DEVRE = 128;
	public static final int FORM_ENDEKSOR_KAPATMA = 129;


	public static final int FORM_OKUMA_MENU = 201;
	public static final int FORM_TESISAT_YUKLE = 202;
	public static final int FORM_OKUMA_ENDEKS = 203;
	public static final int FORM_SAYAC_TAKMA = 204;
	public static final int FORM_SAYAC_TAKMA_ACIKLAMA = 205;

	public static final int ENUM_CEVAP_TIPI = 1;
	public static final int ENUM_ENDEKS_TIPI = 2;
	public static final int ENUM_FAZ_SAYISI = 3;
	public static final int ENUM_ISLEM_DURUM = 4;
	public static final int ENUM_ISLEM_TIPI = 5;
	public static final int ENUM_MUHUR_KOD_CINS = 6;
	public static final int ENUM_MULKIYET = 7;
	public static final int ENUM_OKUMA_METODU = 8;
	public static final int ENUM_SAYAC_CINSI = 9;
	public static final int ENUM_SAYAC_HANE_SAYISI = 10;
	public static final int ENUM_SAYAC_KODU = 11;
	public static final int ENUM_SEVIYE = 12;
	public static final int ENUM_VOLTAJ = 13;
	public static final int ENUM_FORM_TIPI = 14;
	
	public static final int OBJ_AKTIF_ISEMRI = 100;
	public static final int OBJ_AKTIF_ISLEM = 101;
	public static final int OBJ_AKTIF_ODF = 102;

	public static final int MUHUR_SOKME_ISLEMI = 1001;
	public static final int MUHURLEME_ISLEMI = 1002;
	public static final int KACAK_BILDIRIM = 1003;
	public static final int ZABIT_EKLEME_ISLEMI = 1004;

	
	public static final Item[] isemriMenuItems = new Item[] {

			new Item(FORM_ISEMRI_LISTE, "İŞEMRİ LİSTE", null),
			new Item(FORM_ISEMRI_INDIR, "İŞEMRİ İNDİR", null),
			new Item(FORM_TESISAT_SORGU, "TESİSAT SORGU", null),
			new Item(FORM_MUHUR_ISLEM_MENU, "MÜHÜR İŞLEMLERİ", null),
			new Item(FORM_VERITABANI_SIFIRLA, "VERİTABANI SIFIRLA", null),
			new Item(FORM_GPS, "GPS", null),
			
	};
	
	public static final Item[] okumaMenuItems = new Item[] {

			new Item(FORM_TESISAT_YUKLE, "TESİSAT YÜKLE", null),
			new Item(FORM_OKUMA_ENDEKS, "OKUMA YAP", null),
			new Item(FORM_TESISAT_SORGU, "TESİSAT SORGU", null),
			new Item(FORM_VERITABANI_SIFIRLA, "VERİTABANI SIFIRLA", null),
			new Item(FORM_GPS, "GPS", null),
			new Item(FORM_KACAK_GIRIS, "KAÇAK BİLDİRİMİ", null),
			new Item(FORM_YIKIK_GIRIS, "YIKIK BİLDİRİMİ", null),
	};
	
	public static final Item[] karisikMenuItems = new Item[] {

			new Item(FORM_ISEMRI_LISTE, "İŞEMRİ LİSTE", null),
			new Item(FORM_ISEMRI_INDIR, "İŞEMRİ İNDİR", null),
			new Item(FORM_TESISAT_SORGU, "TESİSAT SORGU", null),
			new Item(FORM_MUHUR_ISLEM_MENU, "MÜHÜR İŞLEMLERi", null),
			new Item(FORM_TESISAT_YUKLE, "TESİSAT YÜKLE", null),
			new Item(FORM_OKUMA_ENDEKS, "OKUMA YAP", null),
			new Item(FORM_VERITABANI_SIFIRLA, "VERİTABANI SIFIRLA", null),
			new Item(FORM_GPS, "GPS", null),
			new Item(FORM_KACAK_GIRIS, "KAÇAK BİLDİRİMİ", null),
			new Item(FORM_YIKIK_GIRIS, "YIKIK BİLDİRİMİ", null),

	};
	
	public static final Operation[] genel = new Operation[] { 
			new Operation(1, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 2, -1),
			new Operation(2, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 1),
	};
	
	
	public static final Operation[] tespit = new Operation[] { // H.Elif düzenlendi.
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 6, null),
			new Operation(2, "Takılı mühür var mı?", 0, 3, 4),
			new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Anket sorularının cevaplanması", FORM_TESPIT, 5, 1),
			new Operation(5, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 7, 4),
			new Operation(6, "Not Atma", FORM_ABONE_DURUM_GIRIS, 7, 1),
			new Operation(7, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 5),
	};
	
	public static final Operation[] sayacDegistir = new Operation[] { // H.Elif aciklama eklendi
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 7, null),
			new Operation(2, "Taklı mühür var mı?", 0, 3, 4),
			new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Takılacak Sayaç", FORM_SAYAC_TAKMA, 6, 1),
			new Operation(5, "Açıklama", FORM_SAYAC_TAKMA_ACIKLAMA, 6, 1),
			new Operation(6, "Sökülen Endeks Girişi", FORM_ISEMRI_ENDEKS, 8, 5),
			new Operation(7, "Not Atma", FORM_ABONE_DURUM_GIRIS, 8, 1),
			new Operation(8, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 4),
	};
	
	public static final Operation[] sayacTak = new Operation[] { 
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 2, 3, null),
			new Operation(2, "Takılacak Sayaç", FORM_SAYAC_TAKMA, 4, 1),
			new Operation(3, "Not Atma", FORM_ABONE_DURUM_GIRIS, 4, 1),
			new Operation(4, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 1),
	};
	
	public static final Operation[] kesmeIsl = new Operation[] { 
			//new Operation(1, "İş emri tamamlanabilecek mi?", 0, 2, 5, null),
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 6, null),
			new Operation(2, "Takılı mühür var mı?", 0, 3, 4),
			new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 6, 2),
			new Operation(5, "Not Atma", FORM_ABONE_DURUM_GIRIS, 6, 1),
			new Operation(6, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, 7, 4),
			new Operation(7, "Endeksor Kapatma", FORM_ENDEKSOR_KAPATMA, -1, 4),
	};
	
	public static final Operation[] acmaIsl = new Operation[] { 
			//new Operation(1, "İş emri tamamlanabilecek mi?", 0, 2, 5, null),
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 6, null),
			new Operation(2, "Takılı mühür var mı?", 0, 3, 4),
			new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 6, 2),
			new Operation(5, "Not Atma", FORM_ABONE_DURUM_GIRIS, 6, 1),
			new Operation(6, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 4),
	};
	
	public static final Operation[] okuma = new Operation[] { 
			new Operation(1, "Sayac Okuma", FORM_OKUMA_ENDEKS, 2, -1),
			new Operation(2, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 1),
	};
	
	public static final Pattern[] kacak = new Pattern[] {
			Pattern.compile(Pattern.quote("kacak"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("kaçak"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("kaçağı"), Pattern.CASE_INSENSITIVE),
	};

	public static final Pattern[] muhur = new Pattern[] {
			Pattern.compile(Pattern.quote("muhur"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("mühür"), Pattern.CASE_INSENSITIVE),
	};
	
	public static final Pattern[] kirma = new Pattern[] {
			Pattern.compile(Pattern.quote("korma"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("kirma"), Pattern.CASE_INSENSITIVE),
	};
}
