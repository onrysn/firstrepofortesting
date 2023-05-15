package mobit.elec.android;

import com.mobit.FormClass;
import mobit.android.GpsActivity;
import mobit.android.WebBrowserActivity;

public interface IDef extends mobit.elec.IDef {

	public static float textSize = 24;
	
	
	public static final FormClass[] formClassList = new FormClass[] {

			new FormClass(FORM_ABONE_DURUM_GIRIS, AboneDurumGirisActivity.class),
			new FormClass(FORM_AYARLAR, AyarlarActivity.class),
			new FormClass(FORM_BLUETOOTH_DISCOVER, BluetoothDiscoverActivity.class),
			new FormClass(FORM_ISEMRI_ENDEKS, IsemriIndexActivity.class),
			new FormClass(FORM_ISEMRI_DETAY, IsemriDetayActivity.class),
			new FormClass(FORM_ISEMRI_INDIR, IsemriIndirActivity.class),
			new FormClass(FORM_ISEMRI_LISTE, IsemriListeActivity.class), 
			new FormClass(FORM_LOGIN, LoginActivity.class),
			new FormClass(FORM_ISEMRI_MENU, IsemriMenuActivity.class),
			new FormClass(FORM_OPTIK_PORT_AYAR, OptikPortAyarActivity.class),
			new FormClass(FORM_SUNUCU_AYAR, SunucuAyarActivity.class),
			new FormClass(FORM_TESISAT_SORGU, TesisatSorguActivity.class),
			new FormClass(FORM_YAZICI_AYAR, YaziciAyarActivity.class),
			new FormClass(FORM_SAYAC_DURUM_GIRIS, SayacDurumGirisActivity.class),
			new FormClass(FORM_MUHUR_SOKME, MuhurSokmeActivity.class),
			new FormClass(FORM_MUHUR_TAKMA, MuhurTakmaActivity.class),
			new FormClass(FORM_OKUMA_MENU, OkumaMenuActivity.class),
			new FormClass(FORM_TESISAT_YUKLE, TesisatYukleActivity.class),
			new FormClass(FORM_OKUMA_ENDEKS, OkumaYapActivity.class), 
			new FormClass(FORM_SAYAC_TAKMA, SayacTakmaActivity.class),
			new FormClass(FORM_TESPIT, TespitActivity.class),
			new FormClass(FORM_FOTOGRAF, FotografCekme.class),
			new FormClass(FORM_ZABIT, ZabitActivity.class),
			new FormClass(FORM_VERITABANI_SIFIRLA, VeritabaniSifirla.class),
			new FormClass(FORM_MUHUR_ISLEM_MENU, MuhurIslemMenuActivity.class),
			new FormClass(FORM_SAYAC_ISLEM_TAMAMLA, SayacIslemTamamla.class),
			new FormClass(FORM_GPS, GpsActivity.class),
			new FormClass(FORM_ISLEM_LISTE, IslemListeActivity.class),
			new FormClass(FORM_ISLEM_RAPOR, IslemRaporActivity.class),
			new FormClass(FORM_WEB_BROWSER, WebBrowserActivity.class),
			//new FormClass(FORM_MAP, MapActivity.class),
			new FormClass(FORM_SIFRE_DEGISTIR, SifreDegistirActivity.class),
			new FormClass(FORM_ADRES_TARIF, AdresTarifActivity.class),
			new FormClass(FORM_ISLEMLER, IslemlerActivity.class),
			new FormClass(FORM_MUHUR_SORGU, MuhurSorguActivity.class),
			new FormClass(FORM_ISLEM_TAMAMLA, IslemTamamla.class),
			new FormClass(FORM_ENDEKSOR_KAPATMA, EndeksorKapatma.class),
			new FormClass(FORM_SAYAC_ISLEM_TAMAMLA, SayacIslemTamamla.class),
			new FormClass(FORM_OKUMA_RAPOR, OkumaRaporActivity.class),


	};
	
	
	
}
