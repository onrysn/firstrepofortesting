package mobit.elec.mbs.medas.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobit.BarcodeResult;
import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.IMap;
import com.mobit.IMapMarker;
import com.mobit.MobitException;
import com.mobit.RecordStatus;
import com.mobit.Yazici;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.PopupList;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.Lun_Control;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.YkpOkuma;
import mobit.elec.IAboneDurum;
import mobit.elec.IAdurum;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IKarne;
import mobit.elec.INavigate;
import mobit.elec.ISayacBilgi;
import mobit.elec.ITesisat;
import mobit.elec.MsgInfo;
import mobit.elec.android.AboneDurumGirisActivity;
import mobit.elec.android.IDef;
import mobit.elec.android.IndexFragment2;
import mobit.elec.android.OkumaRaporActivity;
import mobit.elec.android.TesisatBilgiFragment;
import mobit.elec.enums.IslemDurum;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.IIslemMaster;
import mobit.elec.mbs.get.Cbs;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.mbs.medas.MedasApplication;

import static mobit.elec.IDef.FORM_ABONE_DURUM_GIRIS;
import static mobit.elec.mbs.IDef.PRN;
import static mobit.elec.mbs.IDef.fatura;

public class OkumaYapActivity2 extends AppCompatActivity
		implements IForm, OnKeyListener, ICallback, OnClickListener, IPopupCallback,IDialogListener {

	IMedasApplication app;
	boolean readOnly = false;
	private IMap map = null;
	EditText editTesisatSayacNo;
	EditText editKarneNo;
	EditText editUnvan;
	TextView textKarneler;

	Button dialog_islemler;
	Button optikOku;
	Button elleOku;
	Button aboneDurum;
	CheckBox topluOku,optikAD;
	Button barkod;

	Button buttonTamam;
	Button haritaokuma;
	Button tekrarYazdirma2;
	Button gps;
	Button Ara;
	Button adressTarif;

	Button buttonOncekiOkunmayan;
	Button buttonOnceki;
	Button buttonSonraki;
	Button buttonSonrakiOkunmayan;

	IIsemriIslem islem;
	IIsemri isemri;
	IElecApplication islem3;
	IAboneDurum islem4;
	IAdurum islem5;
	com.mobit.IIslemMaster master;
	IIslem islem2;

	int HataDurum=0;
	IReadResult n_res2;
	PopupList<IKarne> popup = null;

	TesisatBilgiFragment bilgi;
	IndexFragment2 index;

	IBarcode barcode;
	YkpOkuma ykpOkuma=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_okuma_yap2);

		if(!(Globals.app instanceof IMedasApplication)){
			finish();
			return;
		}

		app = (IMedasApplication) Globals.app;

		if (app instanceof IMap)
			map = (IMap) app;

		IIslem iislem = app.getActiveIslem();
		if(iislem != null){

			if(iislem instanceof IIslemGrup){
				IIslemGrup islemGrup = (IIslemGrup)iislem;
				try {
					islem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
				}catch (Exception e){
					islem= (IIsemriIslem) islemGrup.getIslem();
				}


			}
			else {
				islem = (IIsemriIslem)iislem;
			}

			isemri = islem.getIsemri();
			if (isemri!=null)
				app.setActiveIsemri(isemri);
			if (!isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) {
				finish();
				Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
				return;
			}
			n_res2= app.getOptikResult();
		}

		app.initForm(this);

		editTesisatSayacNo = (EditText) findViewById(R.id.editTesisatSayacNo);
		editUnvan = (EditText) findViewById(R.id.editUnvan);
		editKarneNo = (EditText) findViewById(R.id.editKarneNo);
		textKarneler = (TextView) findViewById(R.id.textKarneler);

		dialog_islemler= (Button) findViewById(R.id.dialog_islemler);
		optikOku = (Button) findViewById(R.id.optikOku);
		elleOku = (Button) findViewById(R.id.elleOku);
		aboneDurum = (Button) findViewById(R.id.aboneDurum);
		buttonTamam = (Button) findViewById(R.id.buttonTamam);
		topluOku = (CheckBox)findViewById(R.id.topluOku);
		optikAD= (CheckBox)findViewById(R.id.optikAD);
		barkod = (Button)findViewById(R.id.barkod);

		haritaokuma=(Button)findViewById(R.id.haritaokuma);
		tekrarYazdirma2=(Button)findViewById(R.id.tekrarYazdirma2);
		gps=(Button)findViewById(R.id.gps);
		Ara=(Button)findViewById(R.id.Ara);
		adressTarif=(Button)findViewById(R.id.adressTarif);

		buttonOncekiOkunmayan = (Button) findViewById(R.id.buttonOncekiOkunmayan);
		buttonOnceki = (Button) findViewById(R.id.buttonOnceki);
		buttonSonraki = (Button) findViewById(R.id.buttonSonraki);
		buttonSonrakiOkunmayan = (Button) findViewById(R.id.buttonSonrakiOkunmayan);

		editTesisatSayacNo.setImeOptions(EditorInfo.IME_ACTION_GO);
		editKarneNo.setImeOptions(EditorInfo.IME_ACTION_GO);

		editTesisatSayacNo.setOnEditorActionListener(editorListener);
		editKarneNo.setOnEditorActionListener(editorListener);

		dialog_islemler.setOnClickListener(this);
		optikOku.setOnClickListener(this);
		elleOku.setOnClickListener(this);
		aboneDurum.setOnClickListener(this);
		buttonTamam.setOnClickListener(this);
		barkod.setOnClickListener(this);
		haritaokuma.setOnClickListener(this);
		tekrarYazdirma2.setOnClickListener(this);
		gps.setOnClickListener(this);
		Ara.setOnClickListener(this);
		adressTarif.setOnClickListener(this);

		buttonOncekiOkunmayan.setOnClickListener(this);
		buttonOnceki.setOnClickListener(this);
		buttonSonraki.setOnClickListener(this);
		buttonSonrakiOkunmayan.setOnClickListener(this);

		editTesisatSayacNo.setSelectAllOnFocus(true);
		editKarneNo.setSelectAllOnFocus(true);

		bilgi = (TesisatBilgiFragment)getSupportFragmentManager().findFragmentById(R.id.tesisatBilgiFragment);
		index = (IndexFragment2)getSupportFragmentManager().findFragmentById(R.id.indexFragment);



		try {

			popup = new PopupList<IKarne>(textKarneler, 300, app.getOkumaKarneListesi(), this);
			show();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
		}

		barcode = app.newBarcodeObject();
		Intent iin= getIntent();
		String tesisat = String.valueOf(iin.getIntExtra("SAHA_ISEMRI_NO", 0));
		Bundle b = iin.getExtras();
		boolean handled = false;
		HataDurum =0;

		if(b!=null && tesisat.length() <= 2){
			editTesisatSayacNo.setText((String) b.get("TesisatNo"));
			tesisatBul(false, null);
			handled = true;
			//HÜSEYİN EMRE ÇEVİK ENDEKSLERİ GETİRME ( DAHA ÖNCE KAPALIYDI )
			islem.getSAYACLAR().getEndeksler();
			tesisatBul(false,null);
			show();

		}else if (tesisat.length() > 2){
			// Onur haritalardan gelen tesisat no bu koşuldan alınıyor.
			editTesisatSayacNo.setText(tesisat);
			tesisatBul(false, null);
			handled = true;
		}
		SendGraphic();

		ykpOkuma=new YkpOkuma();
		try {
			if (b!=null){
				String knt=(String)b.get("message");
				if (knt.equals("ykp1")){
					ykpOkuma.setTesisatNo(app.getActiveIsemri().getTESISAT_NO());
					ykpOkuma.setIsemriNo(app.getActiveIsemri().getSAHA_ISEMRI_NO());
					YkpOku();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {


		AlertDialog.Builder dialog = new AlertDialog.Builder(OkumaYapActivity2.this);
		dialog.setCancelable(false);
		dialog.setTitle("Emin Misiniz?");
		dialog.setMessage("Okuma Yap Ekranından Çıkıyorsunuz. Emin Misiniz?");
		dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				OkumaYapActivity2.super.onBackPressed();

			}
		});
		dialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	public void  SendGraphic(){
		ProgressDialog progressDialog = new ProgressDialog(OkumaYapActivity2.this);
		progressDialog.setMessage("Lütfen Bekleyin..."); // Setting Message
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
		progressDialog.show(); // Display Progress Dialog
		progressDialog.setCancelable(false);
		final String printerMac=app.getPrinterMac(0);

		final com.mobit.Yazici yazici=new Yazici();
		//HÜSEYİN EMRE ÇEVİK - OKUMAYAP A GİRERKEN İMZA ATMA SORUNU ÇÖZÜM 09.03.2021
		if (app.getEleman().getYETKI().contains("O")&& !app.getEleman().getYETKI().contains("I")){

			if (yazici.bluetoothBagliMi(printerMac)==1){
				//Onur- Okuma yap sayfası açılırken yazıcıya gönderilen imza asenkron yapıldı.
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							yazici.PerakendeZplGraphicSend(printerMac);
						}catch (Exception e){
							app.ShowMessage(OkumaYapActivity2.this, "Yazıcı Ayar Hatası! Lütfen Tekrar Giriş Yapınız","Hata!");
						}
					}
				};
				progressDialog.dismiss();
				new Thread(runnable).start();
			}
			else{
				progressDialog.dismiss();
				AlertDialog.Builder dialog = new AlertDialog.Builder(OkumaYapActivity2.this);
				dialog.setCancelable(false);
				dialog.setTitle("HATA");
				dialog.setMessage("Bluetooth Bağlantısı Koptu Tekrar Bağla ( İmza Faturaya aktarılmadı )");

				dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						OkumaYapActivity2.this.finish();
					}
				});

				dialog.show();

			}
		}else{
			//Onur- Okuma yap sayfası açılırken yazıcıya gönderilen imza asenkron yapıldı.
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						yazici.PerakendeZplGraphicSend(printerMac);
					}catch (Exception e){
						app.ShowMessage(OkumaYapActivity2.this, "Yazıcı Ayar Hatası! Lütfen Tekrar Giriş Yapınız","Hata!");
					}
				}
			};
			progressDialog.dismiss();
			new Thread(runnable).start();
		}

	}
	@Override
	protected void onDestroy() {
		popup.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(true);
		app.setPortCallback(this);
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.okuma_yap2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (id == R.id.aboneDurumKodGiris) {
			Intent intent = new Intent(this, app.getFormClass(FORM_ABONE_DURUM_GIRIS));
			intent.putExtra("islemTipi", 2);
			startActivity(intent);
			return true;

		} else if (id == R.id.tesisatDetay) {
			IIsemri t = app.getActiveIsemri();
			if(t == null){
				app.ShowMessage(OkumaYapActivity2.this, "Önce bir tesisata konumlanın!", "");
				return false;
			}
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
			startActivity(intent);
			return true;
		} else if (id == R.id.tekrarYazdirma ) {
			final IIsemri t = app.getActiveIsemri();
			if(t != null){

				app.runAsync(this, "Tekrar yazdırılıyor...", "", null, new Callback(){

					@Override
					public Object run(Object obj) {
						// TODO Auto-generated method stub
						try {
							app.printIsemriIslem(t.getTESISAT_NO());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							return e;
						}
						return null;
					}

				}, new Callback(){

					@Override
					public Object run(Object obj) {
						// TODO Auto-generated method stub
						if(app.checkException(OkumaYapActivity2.this, obj));
						return null;
					}

				});

			}
			return true;
		}
		else if(id == R.id.islemDetay){
			try {
				app.setObject(IDef.OBJ_ISLEM_RAPOR, isemri.getIslemRapor(app));
			}
			catch (Exception e){
				app.ShowException(this, e);
				return true;
			}
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISLEM_RAPOR));
			startActivityForResult(intent, IDef.FORM_ISLEM_RAPOR);
		}
		else if(id == R.id.islemListe){

			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_KUYRUK_LISTE));
			startActivityForResult(intent, IDef.FORM_KUYRUK_LISTE);

		}
		else if(id == R.id.okumaRapor){
			if(isemri != null) {
				Intent intent = new Intent(this, app.getFormClass(IDef.FORM_OKUMA_RAPOR));
				intent.putExtra(OkumaRaporActivity.KEY_KARNE_NO, isemri.getKARNE_NO());
				startActivityForResult(intent, IDef.FORM_OKUMA_RAPOR);
			}
			else {
				app.ShowMessage(this, "Bir karne seçin", "");
			}
		}


		return super.onOptionsItemSelected(item);
	}

	OnEditorActionListener editorListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			boolean handled = false;
			if (actionId == EditorInfo.IME_ACTION_DONE) {

				buttonTamam.performClick();
				handled = true;
			} else if (actionId == EditorInfo.IME_ACTION_GO) {

				if (v.getId() == R.id.editTesisatSayacNo) {

					tesisatBul(false, null);
					handled = true;

				} else if (v.getId() == R.id.editKarneNo) {
					try {

						show(app.positionFirst(Integer.parseInt(editKarneNo.getText().toString())), null);

					} catch (Exception e) {
						app.ShowException(OkumaYapActivity2.this, e);
					}
					handled = true;
				}

			}
			return handled;
		}
	};




	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		try {
			if (id == R.id.buttonTamam) {
				/*
				//Onur Şantiye abonelerine durum kodu atılması sağlandı. 12.10.2022 Tarihli güncelleme ile artık ihtiyaç kalmadığı için geri çekildi.
				if (isemri.getTARIFE_KODU()==(807)){

					String AboneDurumKodu = "75";

					if (MedasApplication.durum_Kodu.equals(AboneDurumKodu)){
						Tamam();
						MedasApplication.durum_Kodu = "";
						return;
					}else{
						app.ShowMessage(OkumaYapActivity2.this, "Şantiye Abonesidir! 6075 Durum Kodu Atınız! Kullanım Amacını Belirtiniz!", "");
						return;
					}
				}
				if (isemri.getTARIFE_KODU()==(808)){

					String AboneDurumKodu = "75";

					if (MedasApplication.durum_Kodu.equals(AboneDurumKodu)){
						Tamam();
						MedasApplication.durum_Kodu = "";
						return;
					}else{
						app.ShowMessage(OkumaYapActivity2.this, "Şantiye Abonesidir! 6075 Durum Kodu Atınız! Kullanım Amacını Belirtiniz!", "");
						return;
					}
				}else{
						Tamam();
					}*/
				Tamam();
			}
			else if(id == R.id.dialog_islemler){
				okumaYapDialog dialog = new okumaYapDialog(this,this);
				dialog.showDialog();
			}
			else if(id == R.id.buttonOncekiOkunmayan){
				if(isemri == null) throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				show(((INavigate)isemri).prev(true), null);
			}
			else if(id == R.id.buttonOnceki){
				if(isemri == null) throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				show(((INavigate)isemri).prev(false), null);
			}
			else if(id == R.id.buttonSonraki){
				if(isemri == null) throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				show(((INavigate)isemri).next(false), null);
			}
			else if(id == R.id.buttonSonrakiOkunmayan){
				if(isemri == null) throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				show(((INavigate)isemri).next(true), null);


			}else if (id == R.id.Ara && !editUnvan.getText().toString().isEmpty()){
				// Onur karnede ünvanla arama için eklendi basit şekilde çalışıyor detaylandırılabilir.
				final String ara = editUnvan.getText().toString().toUpperCase();
				if(isemri == null) {
					throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				}else {
					if (isemri.getUNVAN().contains(ara)){
						final AlertDialog.Builder alert = new AlertDialog.Builder(OkumaYapActivity2.this);
						alert.setMessage("Girmiş Olduğunuz Ünvanla Eşleşen Bir Tesisatta Bulunuyorsunuz, Sonraki Eşleşmeye Geçmek İstiyormusunuz?");
						alert.setTitle("...");
						alert.setCancelable(false);
						alert.setIcon(R.drawable.ic_emoji);

						alert.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								int count = 0;
								for (int i = 0; i < 1500 ; i++){
									if (isemri.getUNVAN().contains(ara)) count++;
									if (isemri.getUNVAN().contains(ara) && count == 2){
										show();
										break;
									}else {
										try {
											show(((INavigate)isemri).next(false), null);
										} catch (Exception e) {
											final AlertDialog.Builder alert = new AlertDialog.Builder(OkumaYapActivity2.this);
											alert.setMessage("Başka Eşleşme Bulunamadı!");
											alert.setTitle("...");
											alert.setIcon(R.drawable.ic_emoji3);
											alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													editUnvan.setText("");
												}
											});
											alert.show();
											editorListener.onEditorAction(editKarneNo, EditorInfo.IME_ACTION_GO,
													new KeyEvent(0, 0, KeyEvent.ACTION_UP,
															KeyEvent.KEYCODE_ENTER, 0));
											break;
										}
									}
								}
							}
						});
						alert.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								editUnvan.setText("");
							}
						});
						alert.show();
					}else {
						int counter = 0;
						editorListener.onEditorAction(editKarneNo, EditorInfo.IME_ACTION_GO,
								new KeyEvent(0, 0, KeyEvent.ACTION_UP,
										KeyEvent.KEYCODE_ENTER, 0));
						for (int i = 0; i < 1500 ; i++){
							if (isemri.getUNVAN().contains(ara)) counter++;
							if (isemri.getUNVAN().contains(ara) && counter == 1){
								show();
								break;
							}else {
								try {
									show(((INavigate)isemri).next(false), null);
								} catch (Exception e) {
									final AlertDialog.Builder alert = new AlertDialog.Builder(OkumaYapActivity2.this);
									alert.setMessage("Maalesef Eşleşme Bulunamadı!");
									alert.setTitle("...");
									alert.setIcon(R.drawable.ic_emoji3);
									alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											editUnvan.setText("");
										}
									});
									alert.show();
									editorListener.onEditorAction(editKarneNo, EditorInfo.IME_ACTION_GO,
											new KeyEvent(0, 0, KeyEvent.ACTION_UP,
													KeyEvent.KEYCODE_ENTER, 0));
									break;
								}
							}
						}
					}

				}
			}
			else if(id == R.id.optikOku){

				app.runAsync(this, "Optik porta bağlanıyor...", "", null, new Callback() {

					@Override
					public Object run(Object obj) {
						// TODO Auto-generated method stub
						try {
							app.getMeterReader().Reconnect();
							app.getMeterReader().Trigger();
						} catch (Exception e) {
							return e;
						}
						return null;
					}

				}, new Callback() {

					@Override
					public Object run(Object obj) {
						// TODO Auto-generated method stub
						if (app.checkException(OkumaYapActivity2.this, obj))
							return null;
						return null;
					}

				});

			}
			else if(id == R.id.elleOku){
				Lun_Control ln = new Lun_Control();
				String[] Endler=ln.Endeksler;
				if (Endler!=null && index.AddOptikData(Endler[7],isemri.getTESISAT_NO(),Endler[6])){
					Endler[7]=null;
					ln.setEndeksler(Endler);
				}
				islem.getSAYACLAR().setOptikResult(null);
				index.setReadOnly(false);
				show(isemri, null);
			}
			else if(id == R.id.aboneDurum){

				Intent intent = new Intent(this, app.getFormClass(FORM_ABONE_DURUM_GIRIS));
				intent.putExtra("endeks","1");
				startActivityForResult(intent, FORM_ABONE_DURUM_GIRIS);
			}
			else if(id == R.id.barkod){
				barcode.startScanner(this);
			}
			else if(id== R.id.haritaokuma){
				List<IIsemri> list = new ArrayList<IIsemri>();
				list.add(isemri);
				Harita(list);
			}
			else if(id== R.id.gps)
			{
				Intent intent = new Intent(this, app.getFormClass(17));//17 GpsActivity
				startActivity(intent);
			}
			else if (id== R.id.tekrarYazdirma2)
			{
				final IIsemri t = app.getActiveIsemri();
				if(t != null){

					app.runAsync(this, "Tekrar yazdırılıyor...", "", null, new Callback(){

						@Override
						public Object run(Object obj) {
							// TODO Auto-generated method stub
							try {
								app.printIsemriIslem(t.getTESISAT_NO());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								return e;
							}
							return null;
						}

					}, new Callback(){

						@Override
						public Object run(Object obj) {
							// TODO Auto-generated method stub
							if(app.checkException(OkumaYapActivity2.this, obj));
							return null;
						}

					});

				}

			}
			else if(id == R.id.adressTarif)
			{
				//Adres Tarif ekranına yöndelendirme 13.04.2021
				Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ADRES_TARIF));
				startActivityForResult(intent, IDef.FORM_ADRES_TARIF);
			}
		} catch (Exception e) {
			app.ShowException(this, e);
		}

	}

	@Override
	public void Opened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Failed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void BeginRead(IReadResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void EndRead() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Read(IProbeEvent event, final IReadResult result) {
		// TODO Auto-generated method stub
		// Önceki okumadan kalan okuma bilgisinin sıfırlanması
		result.set_okuma_zamani(app.getTime());

		runOnUiThread(new Runnable() {
			public void run() {
			/*	editTesisatSayacNo.setText(result.get_sayac_no());
				tesisatBul(false, result);*/
				//HÜSEYİN EMRE ÇEVİK KOHLER SAYAÇLARIN OPTİKLE ENDEKSLERİNİ ALABİLMEMİZ İÇİN EKLENDİ 17.03.2021
				if (result.get_sayac_no().equals("0")){

					final AlertDialog.Builder alert = new AlertDialog.Builder(OkumaYapActivity2.this);
					final EditText edittext = new EditText(OkumaYapActivity2.this);
					edittext.setInputType(InputType.TYPE_CLASS_NUMBER );
					alert.setMessage("Tesisat numarasını elle giriş yapmak için TAMAM butonunu kullanabilirsiniz. "+isemri.getTESISAT_NO()+"Tesisat numaralı, "+isemri.getUNVAN()+ " ünvanına sahip ve "+isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getSAYAC_NO()+" seri nolu sayaç ile işlem yapmak için AKTİF TESİSAT butonunu kullanınız!");
					alert.setTitle("OPTİKTEN 'TESİSAT NO' ALINAMADI");
					alert.setCancelable(false);
					alert.setView(edittext);


					alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {


							String girilensayacno = edittext.getText().toString();
							editTesisatSayacNo.setText(girilensayacno);
							tesisatBul(false, result);

						}
					});
					alert.setNegativeButton("AKTİF TESİSAT", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							String girilensayacno = String.valueOf(isemri.getTESISAT_NO());
							editTesisatSayacNo.setText(girilensayacno);
							tesisatBul(false, result);

						}
					});



					alert.show();

				}
				else{
					editTesisatSayacNo.setText(result.get_sayac_no());
					tesisatBul(false, result);
				}


			}
		});

	}


	@Override
	public void PowerEvent(IProbeEvent event) {
		// TODO Auto-generated method stub
		MbtProbePowerStatus status = null;
		try {
			status = event.getMeterReader().GetPowerStatus();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status == null) return;
		//app.ShowMessage(this, String.format("Optik port enerji seviyesi:%%%d",status.batteryLifePercent), "Uyarı");
	}

	@Override
	public void ConnectionResetEvent(IProbeEvent event) {
		// TODO Auto-generated method stub
		app.ShowMessage(this, MsgInfo.OPTIK_PORT_BAGLANTI_KESILDI);
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}


	void tesisatBul(final boolean recursive, final IReadResult result)
	{

		int tesisat_no = 0;
		try {

			String s = editTesisatSayacNo.getText().toString();
			if(s.isEmpty()){
				app.ShowMessage(OkumaYapActivity2.this, "Tesisat numarası girin!", "");
				return;
			}
			tesisat_no = Integer.parseInt(s);
			IIsemri t;
			// Test için değiştirilmesi gerekiyor
			if(false && Globals.isDeveloping()){
				t = isemri;
			}
			else {
				t = app.position(tesisat_no);
			}

			if(t != null) show(t, result);


		} catch (MobitException ex) {
			if(ex.getCode() == MsgInfo.TESISAT_VEYA_SAYAC_BULUNAMADI && !recursive){
				final int tno = tesisat_no;
				app.ShowMessage(OkumaYapActivity2.this, MsgInfo.TESISAT_BULUNAMADI_SUNUCUDA_SORGULA,
						DialogMode.YesNo, 1, 0, new IDialogCallback(){

							@Override
							public void DialogResponse(int msg_id, DialogResult res) {
								// TODO Auto-generated method stub
								if(res != DialogResult.Yes) return;
								List<ITesisat> list = new ArrayList<ITesisat>();
								Exception ex = null;
								try {

									list.addAll(app.fetchTesisat(tno));
								}
								catch(Exception e){
									ex = e;
								}
								try {
									list.addAll(app.fetchTesisat2(tno));

								} catch (Exception e) {
									// TODO Auto-generated catch block
									ex = e;
								}

								if(list.size() > 0){

									try {
										app.Save(list);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										app.ShowException(OkumaYapActivity2.this, e);
										return;
									}
									tesisatBul(true, result);
								}
								else {
									if(ex != null) app.ShowException(OkumaYapActivity2.this, ex);
								}
							}

						});
			}
		}
		catch (Exception e) {
			app.ShowException(OkumaYapActivity2.this, e);


		}

	}

	void show() {

		if(isemri != null) bilgi.show(isemri);
		if(islem != null){
			ISayacBilgi sayac = islem.getSAYACLAR().getSayac(SayacKodu.Aktif);
			IReadResult result = (sayac != null) ? sayac.getOptikResult() : null;
			//IReadResult result1=sayac.getOptikResult();
			if (result==null)
				result= n_res2;
			index.show(islem, islem.getSAYACLAR(), result);
		}
	}

	void show(IIsemri t, IReadResult result) throws Exception {

		if(t == null) return;
		//t=islem2.getIsemri();
		boolean enabled = t instanceof INavigate;
		buttonSonraki.setEnabled(enabled);
		buttonOnceki.setEnabled(enabled);

		IIsemriIslem islem = null;
		IIslemGrup islemGrup = null;
		try {
			islemGrup = (IIslemGrup)t.newIslem();
			islem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bilgi.show(t);
		if(result != null){
			ISayacBilgi sayac = islem.getSAYACLAR().getSayac(Integer.parseInt(result.get_sayac_no()));
			if(sayac != null) sayac.setOptikResult(result);
		}
		index.show(islem, islem.getSAYACLAR(), result);

		this.isemri = t;
		app.setActiveIsemri(isemri);
		this.islem = islem;
		app.setActiveIslem(islemGrup);


//Hüseyin Emre Çevik optik okumasından sonra a.d girilebilmesi için 7.21
		if (!optikAD.isChecked() &&result != null) {
			buttonTamam.performClick();
		}

		/*if(result != null)
			buttonTamam.performClick();*/

	}


	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub
		if (anchorView.equals(textKarneler)) {
			String s = "";
			if(obj instanceof IKarne){
				IKarne karne = (IKarne)obj;
				s = Integer.toString(karne.getKARNE_NO());
			}
			else {
				s = obj.toString();
			}
			editKarneNo.setText(s);
			editTesisatSayacNo.setText("");
			//editKarneNo.requestFocus();
			//app.showSoftKeyboard(this, true);
			editorListener.onEditorAction(editKarneNo, EditorInfo.IME_ACTION_GO,
					new KeyEvent(0, 0, KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_ENTER, 0));

		}
	}


	void Tamam() {
		IIsemri t = app.getActiveIsemri();
		if (t == null)
			return;

		final Runnable r = new Runnable() {
			@Override
			public void run() {
				try {

					index.save();
					islem.IslemKontrol(app);
					tamam.run();

				} catch (Exception e) {
					app.ShowException(OkumaYapActivity2.this, e);
				}
			}
		};

		if(!t.getISEMRI_DURUMU().equals(IslemDurum.Atanmis)){
			app.ShowMessage(this, "Bu okuma daha önceden yapılımış. Tekrar okumak istiyor musunuz?", "",
					DialogMode.YesNo, 1, 0, new IDialogCallback() {
						@Override
						public void DialogResponse(int msg_id, DialogResult result) {
							if(result == DialogResult.Yes)
								r.run();
						}
					});
		}
		else {
			r.run();
		}
	}

	Runnable tamam = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {

				IIslemGrup islemGrup=null;
				if (HataDurum==0) {
					islemGrup = (IIslemGrup) app.getActiveIslem();
					islem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
				}
				else {
					islem=(IIsemriIslem)app.getActiveIslem();
					islem=(IIsemriIslem)islem;
				}
				islem.setELEMAN_KODU(app.getEleman().getELEMAN_KODU());
				islem.setZAMAN(Globals.getTime());
				if (islem.getCBS()==null)
					islem.setCBS(new Cbs(app.getLocation()));

				List<IReadResult> results = islem.getSAYACLAR().getOptikResult();
				if (islem.getOPTIK_DATA()==null)
					islem.setOPTIK_DATA(app.getOptikData(results));

				islem.setDURUM(RecordStatus.Saved);
				ILocation location = null;
				try {
					location = app.getLocation();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				try {
					if (HataDurum==0) {
						List<IIslemGrup> arrayList2;
						arrayList2 = new ArrayList<IIslemGrup>();
						try {
							arrayList2 = (List<IIslemGrup>)(List<?>)app.getIslem(RecordStatus.Sent);
							for (IIslemGrup arr : arrayList2) {
								IIslemMaster master = (IIslemMaster) arr.getIslem().get(0);
								if (master.getRESULT_CODE() != 0 && !master.getRESULT_TYPE().equals(PRN)) {
									if(master.getIsemriNo()==islem.getIsemri().getTESISAT_NO()){
										master.updateDurum(RecordStatus.None);
									}
								}
							}
						} catch (Exception e) {
						}
						final IIslemGrup masterGrup = (IIslemGrup) app.saveIslem(islemGrup);


						if (topluOku.isChecked()) {
							index.clear();
							isemri.setISEMRI_DURUMU(IslemDurum.Kuyrukta);
							app.Save(isemri);
							app.ShowMessage(OkumaYapActivity2.this, "Okuma kaydedildi", "",
									DialogMode.Ok, 0, 1000, null);
							return;
						}

						if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
							AlertDialog.Builder dialog = new AlertDialog.Builder(OkumaYapActivity2.this);
							dialog.setCancelable(false);
							dialog.setTitle("Konum Bilgis");
							dialog.setMessage("Konum bilgisi alınmadı! Yine de devam etmek ister misin?");
							dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									try {
										app.sendIslem(OkumaYapActivity2.this, masterGrup, new Callback() {

											@Override
											public Object run(final Object obj) {
												// TODO Auto-generated method stub

												if (app.checkException(OkumaYapActivity2.this, obj)) return null;
												index.clear();
												app.IslemTamamlandi(OkumaYapActivity2.this, masterGrup, false, false);
												buttonSonrakiOkunmayan.performClick();
												return null;
											}
										}, 15000);
									}
									catch (Exception ex){
									}
								}
							}).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//Action for "Cancel".
								}
							});
							final AlertDialog alert = dialog.create();
							alert.show();
						} else {
							app.sendIslem(OkumaYapActivity2.this, masterGrup, new Callback() {

								@Override
								public Object run(final Object obj) {
									// TODO Auto-generated method stub

									if (app.checkException(OkumaYapActivity2.this, obj)) return null;
									index.clear();
									app.IslemTamamlandi(OkumaYapActivity2.this, masterGrup, false, false);
									buttonSonrakiOkunmayan.performClick();
									return null;
								}
							}, 15000);

						}

					}
					else {
						final IIsemriIslem masterGrup = (IIsemriIslem) app.saveIslem(islem);



						if(topluOku.isChecked()){
							index.clear();
							isemri.setISEMRI_DURUMU(IslemDurum.Kuyrukta);
							app.Save(isemri);
							app.ShowMessage(OkumaYapActivity2.this, "Okuma kaydedildi", "",
									DialogMode.Ok, 0, 1000, null);
							return;
						}

						if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
							AlertDialog.Builder dialog = new AlertDialog.Builder(OkumaYapActivity2.this);
							dialog.setCancelable(false);
							dialog.setTitle("Konum Bilgis");
							dialog.setMessage("Konum bilgisi alınmadı! Yine de devam etmek ister misin?");
							dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									try {
										app.sendIslem(OkumaYapActivity2.this, masterGrup, new Callback() {

											@Override
											public Object run(final Object obj) {
												// TODO Auto-generated method stub

												if (app.checkException(OkumaYapActivity2.this, obj)) return null;
												index.clear();
												app.IslemTamamlandi(OkumaYapActivity2.this, masterGrup, false, false);
												buttonSonrakiOkunmayan.performClick();
												return null;
											}
										}, 15000);
									}
									catch (Exception ex){
									}
								}
							}).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//Action for "Cancel".
								}
							});
							final AlertDialog alert = dialog.create();
							alert.show();
						} else {
							app.sendIslem(OkumaYapActivity2.this, masterGrup, new Callback() {

								@Override
								public Object run(final Object obj) {
									// TODO Auto-generated method stub

									if (app.checkException(OkumaYapActivity2.this, obj)) return null;
									index.clear();
									app.IslemTamamlandi(OkumaYapActivity2.this, masterGrup, false, false);
									buttonSonrakiOkunmayan.performClick();
									return null;
								}
							}, 15000);

						}
					}
				}catch (Exception e){
					final IIsemriIslem masterGrup = (IIsemriIslem)app.saveIslem(islem);



					if(topluOku.isChecked()){
						index.clear();
						isemri.setISEMRI_DURUMU(IslemDurum.Kuyrukta);
						app.Save(isemri);
						app.ShowMessage(OkumaYapActivity2.this, "Okuma kaydedildi", "",
								DialogMode.Ok, 0, 1000, null);
						return;
					}

					if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(OkumaYapActivity2.this);
						dialog.setCancelable(false);
						dialog.setTitle("Konum Bilgis");
						dialog.setMessage("Konum bilgisi alınmadı! Yine de devam etmek ister misin?");
						dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								try {
									app.sendIslem(OkumaYapActivity2.this, masterGrup, new Callback() {

										@Override
										public Object run(final Object obj) {
											// TODO Auto-generated method stub

											if (app.checkException(OkumaYapActivity2.this, obj)) return null;
											index.clear();
											app.IslemTamamlandi(OkumaYapActivity2.this, masterGrup, false, false);
											buttonSonrakiOkunmayan.performClick();
											return null;
										}
									}, 15000);
								}
								catch (Exception ex){
								}
							}
						}).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Action for "Cancel".
							}
						});
						final AlertDialog alert = dialog.create();
						alert.show();
					} else {
						app.sendIslem(OkumaYapActivity2.this, masterGrup, new Callback() {

							@Override
							public Object run(final Object obj) {
								// TODO Auto-generated method stub

								if (app.checkException(OkumaYapActivity2.this, obj)) return null;
								index.clear();
								app.IslemTamamlandi(OkumaYapActivity2.this, masterGrup, false, false);
								buttonSonrakiOkunmayan.performClick();
								return null;
							}
						}, 15000);

					}
				}



			} catch (Exception e) {
				e.printStackTrace();
				app.ShowException(OkumaYapActivity2.this, e);

			}
		}

	};








	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == IDef.FORM_OKUMA_RAPOR){
			if(resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				editTesisatSayacNo.setText(Integer.toString(bundle.getInt(OkumaRaporActivity.KEY_TESISAT_NO)));
				tesisatBul(false, null);
				editTesisatSayacNo.setText("");
			}
		}
		else {
			BarcodeResult result = barcode.getScanResult(requestCode, resultCode, data);
			if (result.resultStatus == BarcodeResult.SUCCESS) {
				View view = getCurrentFocus();
				if (view instanceof EditText) {
					EditText edit = (EditText) view;
					edit.setText(result.resultData);
				}
			}
		}
	}


	//Hüseyin Emre Çevik 12.04.2021 Harita Eklendi

	private void Harita(List<IIsemri> arrayList) {
		if (map == null)
			return;

		List<IMapMarker> list = new ArrayList<IMapMarker>();
		synchronized (arrayList) {
			for (IIsemri isemri : arrayList) {
				if (isemri instanceof IMapMarker) {
					IMapMarker marker = (IMapMarker) isemri;
					ILocation location = marker.getLocation();
					if (location != null && location.isValid())
						list.add(marker);
				}
			}
		}
		if (list.isEmpty()) {
			app.ShowMessage(this, "Konum bilgisi yok!", "");
			return;
		}

		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);

		map.setMarkerList(list);

		Class<?> cls = app.getFormClass(IDef.FORM_MAP);
		Intent intent = new Intent(this, cls);
		startActivityForResult(intent, IDef.FORM_MAP);


	}

	@Override
	public void dialogClickListener(int Id) {
		try{


			if(Id == 1){//islemListe

				Intent intent = new Intent(this, app.getFormClass(IDef.FORM_KUYRUK_LISTE));
				startActivityForResult(intent, IDef.FORM_KUYRUK_LISTE);

			}
			else if(Id == 2){//elleOku
				Lun_Control ln = new Lun_Control();
				String[] Endler=ln.Endeksler;
				if (Endler!=null && index.AddOptikData(Endler[7],isemri.getTESISAT_NO(),Endler[6])){
					Endler[7]=null;
					ln.setEndeksler(Endler);
				}
				islem.getSAYACLAR().setOptikResult(null);
				index.setReadOnly(false);
				show(isemri, null);
			}
			else if(Id == 3){//aboneDurum

				Intent intent = new Intent(this, app.getFormClass(FORM_ABONE_DURUM_GIRIS));
				intent.putExtra("endeks","1");
				startActivityForResult(intent, FORM_ABONE_DURUM_GIRIS);
			}
			else if (Id == 4){//tekrarYazdirma2
				final IIsemri t = app.getActiveIsemri();
				if(t != null){

					app.runAsync(this, "Tekrar yazdırılıyor...", "", null, new Callback(){

						@Override
						public Object run(Object obj) {
							// TODO Auto-generated method stub
							try {
								app.printIsemriIslem(t.getTESISAT_NO());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								return e;
							}
							return null;
						}

					}, new Callback(){

						@Override
						public Object run(Object obj) {
							// TODO Auto-generated method stub
							if(app.checkException(OkumaYapActivity2.this, obj));
							return null;
						}

					});

				}

			}
			else if(Id == 5){//gps
				Intent intent = new Intent(this, app.getFormClass(17));//17 GpsActivity
				startActivity(intent);
			}
			else if(Id == 6){//adressTarif
				//Adres Tarif ekranına yöndelendirme 13.04.2021
				Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ADRES_TARIF));
				startActivityForResult(intent, IDef.FORM_ADRES_TARIF);
			}
			else if(Id == 7){//haritaokuma
				List<IIsemri> list = new ArrayList<IIsemri>();
				list.add(isemri);
				Harita(list);
			}
			else if(Id == 8){//ykpBirGunluk
				ISayacBilgi sb = islem.getSAYACLAR().getSayac(SayacKodu.Aktif);

				String marka=sb.getMARKA().getSAYAC_MARKA_KODU();
				Intent intent=new Intent(this,YkpActivity.class);
				intent.putExtra("sayac",marka);
				intent.putExtra("tekgun_mu",1);
				intent.putExtra("activity","OkumaYapActivity2");
				startActivity(intent);
			}
		} catch (Exception e) {
			app.ShowException(this, e);
		}
	}

	public void YkpOku()
	{
		//	yuk_ptofil=1;
		app.runAsync(this, "Optik porta bağlanıyor...", "", null, new Callback() {

			@Override
			public Object run(Object obj) {
				// TODO Auto-generated method stub
				try {
					app.getMeterReader().Reconnect();
					app.getMeterReader().Trigger();

				} catch (Exception e) {
					return e;
				}
				return null;
			}

		}, new Callback() {

			@Override
			public Object run(Object obj) {
				// TODO Auto-generated method stub
				if (app.checkException(OkumaYapActivity2.this, obj))
					return null;
				return null;
			}

		});
	}
}
