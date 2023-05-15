package mobit.elec.mbs.medas;

//import android.graphics.Region;

import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IslemMaster2;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.eleman;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.get.muhur_kod;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.get.sdurum;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.put.put_atarif;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_isemri_zabit;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.put.put_tesisat_muhur;

import java.util.regex.Pattern;

import com.mobit.Item;
import com.mobit.Operation;
import mobit.elec.AltEmirTuru;

public interface IDef extends mobit.elec.mbs.IDef {

	public static final int MEDAS_SERVER = 1;
	public static final int OSOS_SERVER = 2;
	public static final int MEDAS_OSOS_SERVER = 3;


	public static final Item[] isemriMenuItems = new Item[] {

			new Item(FORM_ISEMRI_LISTE, "İŞEMRİ LİSTE", null),
			new Item(FORM_ISEMRI_INDIR, "İŞEMRİ İNDİR", null),
			new Item(FORM_TESISAT_SORGU, "TESİSAT SORGU", null),
			new Item(FORM_MUHUR_ISLEM_MENU, "MÜHÜR İŞLEMLERİ", null),
			new Item(FORM_VERITABANI_SIFIRLA, "VERİTABANI SIFIRLA", null),
			new Item(FORM_GPS, "GPS", null),
			new Item(FORM_RAPOR, "RAPOR", null),
			new Item(FORM_SAHA_ISEMRI, "SAHA ISEMRI", null),

	};

	public static final Item[] okumaMenuItems = new Item[] {

			new Item(FORM_TESISAT_YUKLE, "TESİSAT YÜKLE", null),
			new Item(FORM_OKUMA_ENDEKS, "OKUMA YAP", null),
			new Item(FORM_TESISAT_SORGU, "TESİSAT SORGU", null),
			new Item(FORM_VERITABANI_SIFIRLA, "VERİTABANI SIFIRLA", null),
			new Item(FORM_GPS, "GPS", null),
			new Item(FORM_KACAK_GIRIS, "KAÇAK BİLDİRİMİ", null),
			new Item(FORM_YIKIK_GIRIS, "YIKIK BİLDİRİMİ", null),
			new Item(FORM_RAPOR, "RAPOR", null),
			new Item(FORM_SAHA_ISEMRI, "SAHA ISEMRI", null),
	};

	public static final Item[] karisikMenuItems = new Item[] {

			new Item(FORM_ISEMRI_LISTE, "İŞEMRİ LİSTE", null),
			new Item(FORM_ISEMRI_INDIR, "İŞEMRİ İNDİR", null),
			new Item(FORM_TESISAT_SORGU, "TESİSAT SORGU", null),
			new Item(FORM_MUHUR_ISLEM_MENU, "MÜHÜR İŞLEMLERİ", null),
			new Item(FORM_TESISAT_YUKLE, "TESİSAT YÜKLE", null),
			new Item(FORM_OKUMA_ENDEKS, "OKUMA YAP", null),
			new Item(FORM_VERITABANI_SIFIRLA, "VERİTABANI SIFIRLA", null),
			new Item(FORM_GPS, "GPS", null),
			new Item(FORM_KACAK_GIRIS, "KAÇAK BİLDİRİMİ", null),
			new Item(FORM_YIKIK_GIRIS, "YIKIK BİLDİRİMİ", null),
			new Item(FORM_RAPOR, "RAPOR", null),
			new Item(FORM_SAHA_ISEMRI, "SAHA ISEMRI", null),
			//new Item(FORM_CAMERA_BARCODE_SCAN, "BARKOD OKUTMA", null),
	};

	public static final Operation[] genel = new Operation[] {
			new Operation(1, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 2, -1),
			new Operation(2, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 1), };

	public static final Operation[] tespit = new Operation[] {
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 6, null),
			new Operation(2, "Takılı mühür var mı?", 0, 3, 4), new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			//new Operation(4, "Anket sorularının cevaplanması", FORM_OLCU_DEVRE, 5, 1),
			new Operation(4, "Anket sorularının cevaplanması", FORM_TESPIT, 5, 6),
//			new Operation(4, "Anket sorularının cevaplanması", FORM_TESPIT, 5, 2),//Unvan değiştirme için değiştirildi
			new Operation(5, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 7, 4),
			new Operation(6, "Not Atma", FORM_ABONE_DURUM_GIRIS, 7, 1),
			new Operation(7, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 5),
	};



	public static final Operation[] sayacDegistir = new Operation[] {
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 7, null),
			new Operation(2, "Taklı mühür var mı?", 0, 3, 4),
			new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Takılacak Sayaç", FORM_SAYAC_TAKMA, 5, 1),
			new Operation(5, "Açıklama", FORM_SAYAC_TAKMA_ACIKLAMA, 6, 1),
			new Operation(6, "Sökülen Endeks Girişi", FORM_ISEMRI_ENDEKS, 8, 5),
			new Operation(7, "Not Atma", FORM_ABONE_DURUM_GIRIS, 8, 1),
			new Operation(8, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 4),

	};

	public static final Operation[] sayacTak = new Operation[] {
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 2, 3, null),
			new Operation(2, "Takılacak Sayaı", FORM_SAYAC_TAKMA, 4, 1),
			new Operation(3, "Not Atma", FORM_ABONE_DURUM_GIRIS, 4, 1),
			new Operation(4, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 1), };

	public static final Operation[] kesmeIsl = new Operation[] {
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 5, null),
			new Operation(2, "Takılı mühür var mı?", 0, 3, 4), new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 6, 2),
			new Operation(5, "Not Atma", FORM_ABONE_DURUM_GIRIS, 6, 1),
			new Operation(6, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, 7, 4),
			new Operation(7, "Endeksor Kapatma", FORM_ENDEKSOR_KAPATMA, -1, 4),
	};

	public static final Operation[] acmaIsl = new Operation[] {
			new Operation(1, "İş emri tamamlanabilecek mi?", 0, 4, 5, null),
			new Operation(2, "Takılı mühür var mı?", 0, 3, 4), new Operation(3, "Mühür Sökme", FORM_MUHUR_SOKME, 4, 2),
			new Operation(4, "Endeks Girişi", FORM_ISEMRI_ENDEKS, 6, 2),
			new Operation(5, "Not Atma", FORM_ABONE_DURUM_GIRIS, 6, 1),
			new Operation(6, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 4), };

	public static final Operation[] okuma = new Operation[] { new Operation(1, "Sayac Okuma", FORM_OKUMA_ENDEKS, 2, -1),
			new Operation(2, "Sisteme Gönderme", FORM_ISLEM_TAMAMLA, -1, 1), };

	public static final Pattern[] kacak = new Pattern[] {
			Pattern.compile(Pattern.quote("kacak"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("kaçak"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("kaçağı"), Pattern.CASE_INSENSITIVE), };

	public static final Pattern[] muhur = new Pattern[] {
			Pattern.compile(Pattern.quote("muhur"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("mühür"), Pattern.CASE_INSENSITIVE), };

	public static final Pattern[] kirma = new Pattern[] {
			Pattern.compile(Pattern.quote("kırma"), Pattern.CASE_INSENSITIVE),
			Pattern.compile(Pattern.quote("kirma"), Pattern.CASE_INSENSITIVE), };

	public static final AltEmirTuru[] bosAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.Tumu, -1, "Tümü")
	};

	public static final AltEmirTuru[] kesmeAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.Kesme, -1, "Tümü"), new AltEmirTuru(IslemTipi.Kesme, 1, "TTK Kesme"),
			new AltEmirTuru(IslemTipi.Kesme, 2, "SK Kesme"), new AltEmirTuru(IslemTipi.Kesme, 3, "BTK Kesme"),
			new AltEmirTuru(IslemTipi.Kesme, 4, "ACK Kesme"), new AltEmirTuru(IslemTipi.Kesme, 5, "Borçtan Kesme"),
			new AltEmirTuru(IslemTipi.Kesme, 6, "ASY Kesme"), new AltEmirTuru(IslemTipi.Kesme, 7, "TNK Kesme"),
			new AltEmirTuru(IslemTipi.Kesme, 8, "MYK Kesme"), new AltEmirTuru(IslemTipi.Kesme, 9, "SBK Kesme"),
			new AltEmirTuru(IslemTipi.Kesme, 10, "KCK Kesme"), new AltEmirTuru(IslemTipi.Kesme, 11, "FBK Kesme"),

	};

	public static final AltEmirTuru[] acmaAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.Acma, -1, "Tümü"),
			new AltEmirTuru(IslemTipi.Acma, 0, "Kesmeden Açma"),
			new AltEmirTuru(IslemTipi.Acma, 1, "Yeni Bağlama"), new AltEmirTuru(IslemTipi.Acma, 2, "ACK Açma"),
			new AltEmirTuru(IslemTipi.Acma, 3, "ŞBK Açma"), new AltEmirTuru(IslemTipi.Acma, 4, "TTK Açma"),
			new AltEmirTuru(IslemTipi.Acma, 5, "FBK Açma"), new AltEmirTuru(IslemTipi.Acma, 6, "ASY Açma"),
			new AltEmirTuru(IslemTipi.Acma, 7, "KCK Açma"),

	};

	public static final AltEmirTuru[] ihbarAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.Ihbar, -1, "Tümü"),
			new AltEmirTuru(IslemTipi.Ihbar, 0, "TCK Borç"),
			new AltEmirTuru(IslemTipi.Ihbar, 1, "Periyodik SD"),

	};

	public static final AltEmirTuru[] kontrolAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.Kontrol, -1, "Tümü"), new AltEmirTuru(IslemTipi.Kontrol, 0, "Kesme Kontrol"),

	};

	public static final AltEmirTuru[] sayacDegistirAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.SayacDegistir, -1, "Tümü"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 0, "SD Hata Düz"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 1, "SD Arıza"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 2, "SD Periyodik"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 3, "SD Güç Artırımı"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 4, "SD Trf Değişimi"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 5, "SD Kaçak"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 6, "SD Devren Açma"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 7, "SD Diğer"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 8, "SD Geçici Sayaç"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 9, "SD Güç Tenzili"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 10, "SD İlk Takma"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 11, "SD Kalibre Takma"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 12, "SD Abone Talep"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 13, "SD Kayıp"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 14, "SD OSOS Kapsamında"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 15, "SD Sayaç Kaldırma"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 16, "SD ST Geçiş"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 17, "SD ölçü Şikayetin.Söküm"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 18, "SD Kombi"),
			new AltEmirTuru(IslemTipi.SayacDegistir, 19, "SD Optik Port Arızası"),

	};

	public static final AltEmirTuru[] sayacTakAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.SayacTakma, -1, "Tümü"),
			new AltEmirTuru(IslemTipi.SayacTakma, 10, "İlk Açma"),
			new AltEmirTuru(IslemTipi.SayacTakma, 15, "Sayac Kaldırma"),
			new AltEmirTuru(IslemTipi.SayacTakma, 0, "Hata Düzeltme"),

	};

	public static final AltEmirTuru[] tespitAltEmirTuruList = new AltEmirTuru[] {
			//new AltEmirTuru(IslemTipi.Tespit, -1, "Tümü"),
			new AltEmirTuru(IslemTipi.Tespit, 1, "Tarımsal Sulama Kontrol Tespiti (Eski)"),
			new AltEmirTuru(IslemTipi.Tespit, 2, "Tarımsal Sulama Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 3, "Yeni Abone Tesisat Kontrol (BİNA)"),
			new AltEmirTuru(IslemTipi.Tespit, 4, "Yeni Abone Tesisat Kontrol (tesisat ba"),
			new AltEmirTuru(IslemTipi.Tespit, 5, "Kaçak Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6, "İşletme Talebi ile ölçü Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 7, "Serbest Tüketici Satış"),
			new AltEmirTuru(IslemTipi.Tespit, 8, "Jeneratör Başlama"),
			new AltEmirTuru(IslemTipi.Tespit, 9, "Jeneratör Sökme"),
			new AltEmirTuru(IslemTipi.Tespit, 10, "OSOS Aktivasyon Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 11, "OSOS Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 12, "Saha Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 13, "Tarımsal Sulama Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 14, "Güvenlik Ekibi Tesisat Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 15, "Arıza Ekibi Tesisat Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 16, "Abone Talebi ile ölçü Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 17, "Yıkık Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6001, "6001 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6002, "6002 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6003, "6003 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6004, "6004 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6006, "6006 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6010, "6010 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 6016, "6016 Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 18, "GM-ölçü Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 19, "GM-Kaçak Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 20, "GM-Saha Kontrol"),
			new AltEmirTuru(IslemTipi.Tespit, 21, "Yıkık Canlandırma Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 22, "Sayaç Yok Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 23, "Ay Sonu Okuması"),
			new AltEmirTuru(IslemTipi.Tespit, 24, "Yeni Abone Tesisat Kont"),
			new AltEmirTuru(IslemTipi.Tespit, 25, "Tarımsal Güvenlik Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 26, "GM Tesisat Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 34, "Okuma Takip"),
			new AltEmirTuru(IslemTipi.Tespit, 6097, "8x Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 36, "Otomasyon OSOS Kontrol Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 37, "Otomosyon OSOS Aktivasyon Tespiti"),
			new AltEmirTuru(IslemTipi.Tespit, 38, "OSOS Aktivasyon Luyalt"),
			new AltEmirTuru(IslemTipi.Tespit, 39, "OSOS Aktivasyon Yenileme"),
			new AltEmirTuru(IslemTipi.Tespit, 40, "OSOS Aktivasyon Limit Üstü"),

	};

	public static final String[] kacakTipleri = new String[] { "Diğer", "Harici Hat", "Kayıtsız Sayaç",
			"Sayaca Müdahale",  "Ölçü Hücresine Müdahale" };
	/*
	//muhammed gökkaya
	public static final int BeyazRenk = 0xFFFFFFFF;// new Color(255, 255, 255);
	public static final int BilgiRenk = 0xFFFFC8C8;// new Color(255, 200, 200);
	public static final int KesmeRenk = 0xffff0000;// new Color(0, 255, 0); //kırmızı
	public static final int AcmaRenk = 0xff00ff00;// new Color(255, 255, 255); //yesil
	public static final int IhbarRenk = 0xFF6400FF;// new Color(100, 0, 255);
	public static final int KontrolRenk = 0xFFFFFF00;// new Color(255, 255, 0);
	public static final int SayacDegistirRenk = 0xff0000ff;// new Color(255, 0, //mavi
															// 255);
	public static final int SayacTakRenk = 0xFF8040FF;// new Color(255, 0, 255);
	public static final int TespitRenk = 0xffffff00;// new Color(0, 255, 255); //sari
	public static final int SayacOkumaRenk = 0xFF80A080;

	 */
	// H.Elif renklerde düzenlemeler yapıldı.
	public static final int BeyazRenk = 0xFFFFFFFF;// new Color(255, 255, 255);
	public static final int BilgiRenk = 0xbfFFC8C8;// new Color(255, 200, 200, 0.75);
	public static final int KesmeRenk = 0xbffc4d4d;// new Color(0, 255, 0, 0.75);
	public static final int AcmaRenk = 0xbf00FF00;// new Color(255, 255, 255, 0.75);
	public static final int IhbarRenk = 0xbf6400FF;// new Color(100, 0, 255, 0.75);
	public static final int KontrolRenk = 0xbfFFFF00;// new Color(255, 255, 0, 0.75);
	public static final int SayacDegistirRenk = 0xbfffff2b;// new Color(255, 0, 255, 0.75);
	public static final int SayacTakRenk = 0xbfFF5e00;// new Color(255, 0, 255, 0.75);
	public static final int TespitRenk = 0xbf00FFFF;// new Color(0, 255, 255, 0.75);
	public static final int TespitRenk2 = 0xbf862d2d;// new Color(0, 255, 255, 0.75);
	public static final int SayacOkumaRenk = 0xbf80A080;

	public static final String kesme_form2 = "kesme_form2";
	public static final String kesme_form3 = "kesme_form3";
	public static final String kesme_form_luyalt = "kesme_form_luyalt";
	public static final String olcu_devre_akim_var_form = "olcu_devre_akim_var_form";
	public static final String ack_form = "ack_form";

	public static final int MBS_ZABIT = 0;
	public static final int MEDAS_ZABIT = 1;

	public static Class<?>[] tables = new Class<?>[] {
		kacak.class
	};

	public static Class<?>[] clearTables = new Class<?>[] {
		kacak.class 
	};
	
	
	static final int s_ADRES_TARIF = 30;
	static final int s_KACAKCI_UNVAN = 30;
	static final int s_KACAKCI_TELEFON = 15;
	static final int s_KACAKCI_EMAIL = 20;
	static final int s_KACAK_TIPI = 10;
	static final int s_IHBAR_EDEN_UNVAN = 30;
	static final int s_IHBAR_EDEN_TELEFON = 15;
	static final int s_IHBAR_EDEN_EMAIL = 20;
	static final int s_TESISAT_NO = field.s_TESISAT_NO;
	static final int s_REFERANS_TESISAT_NO = field.s_TESISAT_NO;
	static final int s_DIREK_NO = 10;
	static final int s_BOX_NO = 10;
	static final int s_SAYAC_NO = field.s_SAYAC_NO;
	static final int s_ACIKLAMA = 30;
	
	
	static final String ADRES_TARIF = "ADRES_TARIF";
	static final String KACAKCI_UNVAN = "KACAK_UNVAN";
	static final String KACAKCI_TELEFON = "KACAKCI_TELEFON";
	static final String KACAKCI_EMAIL = "KACAKCI_EMAIL";
	static final String KACAK_TIPI = "KACAK_TIPI";
	static final String IHBAR_EDEN_UNVAN = "IHBAR_EDEN_UNVAN";
	static final String IHBAR_EDEN_TELEFON = "IHBAR_EDEN_TELEFON";
	static final String IHBAR_EDEN_EMAIL = "IHBAR_EDEN_EMAIL";
	static final String TESISAT_NO = "TESISAT_NO";
	static final String REFERANS_TESISAT_NO = "REFERANS_TESISAT_NO";
	static final String DIREK_NO = "DIREK_NO";
	static final String BOX_NO = "BOX_NO";
	static final String SAYAC_NO = "SAYAC_NO";
	static final String ACIKLAMA = "ACIKLAMA";
}
