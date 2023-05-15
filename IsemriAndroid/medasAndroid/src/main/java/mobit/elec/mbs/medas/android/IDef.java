package mobit.elec.mbs.medas.android;

import com.mobit.FormClass;
import mobit.android.DetayActivity;
import mobit.android.GpsActivity;
import mobit.android.WebBrowserActivity;
import mobit.android.GoogleMap.MapsActivity;
import mobit.elec.android.AboneDurumGirisActivity;
import mobit.elec.android.AdresTarifActivity;
import mobit.elec.android.AyarlarActivity;
import mobit.elec.android.BluetoothDiscoverActivity;
import mobit.elec.android.EndeksorKapatma;
import mobit.elec.android.FotografCekme;
import mobit.elec.android.IsemriDetayActivity;
import mobit.elec.android.IsemriIndirActivity;
import mobit.elec.android.IsemriListeActivity;
import mobit.elec.android.IsemriRaporActivity;
import mobit.elec.android.IslemListeActivity;
import mobit.elec.android.IslemRaporActivity;
import mobit.elec.android.IslemTamamla;
import mobit.elec.android.IslemlerActivity;
import mobit.elec.android.LoginActivity;
import mobit.elec.android.MuhurIslemMenuActivity;
import mobit.elec.android.MuhurSokmeActivity;
import mobit.elec.android.MuhurSorguActivity;
import mobit.elec.android.MuhurTakmaActivity;
import mobit.elec.android.OkumaMenuActivity;
import mobit.elec.android.OkumaRaporActivity;
import mobit.elec.android.OptikPortAyarActivity;
import mobit.elec.android.SahaIsemriActivity;
import mobit.elec.android.SayacDurumGirisActivity;
import mobit.elec.android.SayacIslemTamamla;
import mobit.elec.android.SendFileDemo;
import mobit.elec.android.SendingListActivity;
import mobit.elec.android.SifreDegistirActivity;
import mobit.elec.android.SunucuAyarActivity;
import mobit.elec.android.TesisatSorguActivity;
import mobit.elec.android.TesisatYukleActivity;
import mobit.elec.android.VeritabaniSifirla;
import mobit.elec.android.YaziciAyarActivity;
import mobit.elec.android.SahaIsemriActivity;

public interface IDef extends mobit.elec.mbs.medas.IDef, mobit.elec.android.IDef {

	public static final FormClass[] formClassList = new FormClass[] {

			new FormClass(FORM_ABONE_DURUM_GIRIS, AboneDurumGirisActivity.class),
			new FormClass(FORM_AYARLAR, AyarlarActivity.class),
			new FormClass(FORM_BLUETOOTH_DISCOVER, BluetoothDiscoverActivity.class),
			new FormClass(FORM_ISEMRI_ENDEKS, IsemriIndexActivity2.class),
			new FormClass(FORM_ISEMRI_DETAY, IsemriDetayActivity.class),
			new FormClass(FORM_ISEMRI_INDIR, IsemriIndirActivity.class),
			new FormClass(FORM_ISEMRI_LISTE, IsemriListeActivity.class), 
			new FormClass(FORM_LOGIN, LoginActivity.class),
			new FormClass(FORM_ISEMRI_MENU, IsemriMenuActivity2.class),
			new FormClass(FORM_OPTIK_PORT_AYAR, OptikPortAyarActivity.class),
			new FormClass(FORM_SUNUCU_AYAR, SunucuAyarActivity.class),
			new FormClass(FORM_TESISAT_SORGU, TesisatSorguActivity.class),
			new FormClass(FORM_YAZICI_AYAR, YaziciAyarActivity.class),
			new FormClass(FORM_SEND_FILE_DEMO, SendFileDemo.class),
			new FormClass(FORM_SAYAC_DURUM_GIRIS, SayacDurumGirisActivity.class),
			new FormClass(FORM_MUHUR_SOKME, MuhurSokmeActivity.class),
			new FormClass(FORM_MUHUR_TAKMA, MuhurTakmaActivity.class),
			new FormClass(FORM_OKUMA_MENU, OkumaMenuActivity.class),
			new FormClass(FORM_TESISAT_YUKLE, TesisatYukleActivity.class),
			new FormClass(FORM_OKUMA_ENDEKS, OkumaYapActivity2.class),
			new FormClass(FORM_SAYAC_TAKMA, SayacTakmaActivity2.class),
			new FormClass(FORM_SAYAC_TAKMA_ACIKLAMA, SayacTakmaAciklamaActivity.class),
			new FormClass(FORM_TESPIT, TespitActivity2.class), // TespitActivity2.class
			new FormClass(FORM_OLCU_DEVRE, OlcuDevreActivity.class), // OlcuDevreActivity.class
			new FormClass(FORM_FOTOGRAF, FotografCekme.class),
			new FormClass(FORM_ZABIT, ZabitActivity2.class),
			new FormClass(FORM_VERITABANI_SIFIRLA, VeritabaniSifirla.class),
			new FormClass(FORM_ENDEKS, EndeksActivity2.class),
			new FormClass(FORM_ISLEM_TAMAMLA, IslemTamamla.class),
			new FormClass(FORM_ENDEKSOR_KAPATMA, EndeksorKapatma.class),
			new FormClass(FORM_MUHUR_ISLEM_MENU, MuhurIslemMenuActivity.class),
			new FormClass(FORM_SAYAC_ISLEM_TAMAMLA, SayacIslemTamamla.class),
			new FormClass(FORM_GPS, GpsActivity.class),
			new FormClass(FORM_KACAK_GIRIS, KacakGirisActivity.class),
			new FormClass(FORM_YIKIK_GIRIS, YikikGirisActivity.class),
			new FormClass(FORM_ISLEM_LISTE, IslemListeActivity.class),
			new FormClass(FORM_ISLEM_RAPOR, IslemRaporActivity.class),
			new FormClass(FORM_WEB_BROWSER, WebBrowserActivity.class),
			new FormClass(FORM_MAP, MapsActivity.class),
			new FormClass(FORM_SIFRE_DEGISTIR, SifreDegistirActivity.class),
			new FormClass(FORM_DETAY, DetayActivity.class),
			new FormClass(FORM_RAPOR, IsemriRaporActivity.class),
			new FormClass(FORM_SAHA_ISEMRI, SahaIsemriActivity.class),
			new FormClass(FORM_ADRES_TARIF, AdresTarifActivity.class),
			new FormClass(FORM_ISLEMLER, IslemlerActivity.class),
			new FormClass(FORM_KUYRUK_LISTE, SendingListActivity.class),
			new FormClass(FORM_CAMERA_BARCODE_SCAN, CameraBarcodeScanActivity.class),
			new FormClass(FORM_MUHUR_SORGU, MuhurSorguActivity.class),
			new FormClass(FORM_OKUMA_RAPOR, OkumaRaporActivity.class),
			

	};
	
	
}
