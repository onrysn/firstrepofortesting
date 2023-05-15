package mobit.elec.android;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.MobitException;
import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.LoadData;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacMarka;
import mobit.elec.ITakilanSayac;
import mobit.elec.enums.IFazSayisi;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IMulkiyet;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IVoltaj;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacCinsi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.enums.Mulkiyet;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.get.sayac_marka;
import mobit.android.PopupList;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.ReadMode2;

public class SayacTakmaActivity extends AppCompatActivity
		implements IForm, IPopupCallback, OnClickListener, ICallback, OnTabChangeListener {

	IElecApplication app;
	IIsemri isemri;
	IIsemriIslem isemriIslem;
	ITakilanSayac islem;
	
	TabHost tabHost;

	TextView textHaneSayisi;
	TextView textSayacKodu;
	TextView textMarka;
	TextView textSayacCinsi;
	TextView textFazSayisi;
	TextView textGerilim;
	TextView textMulkiyet;

	EditText editSayacNo;
	EditText editImalYili;
	EditText editAkim;
	EditText editMuhurSeri;
	EditText editMuhurNo;

	Button optikOku;
	Button sayacBilgi;
	Button buttonKaydet;

	PopupList<ISayacHaneSayisi> popupHaneSayisi;
	PopupList<ISayacKodu> popupSayacKodu;
	PopupList<ISayacMarka> popupSayacMarka;
	PopupList<ISayacCinsi> popupSayacCinsi;
	PopupList<IFazSayisi> popupFazSayisi;
	PopupList<IVoltaj> popupVoltaj;
	PopupList<IMulkiyet> popupMulkiyet;

	List<ISayacMarka> markalar;
	
	//IndexFragment endeks;

	//private static final String tab1 = "tab1";
	//private static final String tab2 = "tab2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sayac_takma);

		if (!(Globals.app instanceof IElecApplication)) {
			// Desteklenmiyor
			finish();
			return;
		}
		
		Globals.app.initForm(this);
		
		app = (IElecApplication) Globals.app;
		
		IIslem iislem = app.getActiveIslem();
		if(!(iislem instanceof IIsemriIslem)){
			// Bu işlem desteklenmiyor
			finish();
			return;
		}
		
		isemriIslem = (IIsemriIslem)iislem;
		isemri = (IIsemri)isemriIslem.getIsemri();
		IIslemTipi islemTipi = isemri.getISLEM_TIPI();
		if(!islemTipi.equals(IslemTipi.SayacTakma) && !islemTipi.equals(IslemTipi.SayacDegistir)){
			// Bu iş emri desteklenmiyor
			//finish();
			//return;
		}
		
		//islem = isemriIslem.newTakilanSayac();
		
		/*
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabSpec spec = tabHost.newTabSpec(tab1);
		spec.setContent(R.id.tab1);
		spec.setIndicator("Sayaç Bilgi");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec(tab2);
		spec.setContent(R.id.tab2);
		spec.setIndicator("Sayaç Endeks");
		tabHost.addTab(spec);

		tabHost.setOnTabChangedListener(this);
		
		endeks = (IndexFragment)getSupportFragmentManager().findFragmentById(R.id.indexFragment);
		*/
		
	
		textHaneSayisi = (TextView) findViewById(R.id.textHaneSayisi);
		textSayacKodu = (TextView) findViewById(R.id.textSayacKodu);
		textMarka = (TextView) findViewById(R.id.textMarka);
		textSayacCinsi = (TextView) findViewById(R.id.textSayacCinsi);
		textFazSayisi = (TextView) findViewById(R.id.textFazSayisi);
		textGerilim = (TextView) findViewById(R.id.textGerilim);
		textMulkiyet = (TextView) findViewById(R.id.textMulkiyet);
		editSayacNo = (EditText) findViewById(R.id.editSayacNo);
		editImalYili = (EditText) findViewById(R.id.editImalYili);
		editAkim = (EditText) findViewById(R.id.editAkim);
		editMuhurSeri = (EditText) findViewById(R.id.editMuhurSeri);
		editMuhurNo = (EditText) findViewById(R.id.editMuhurNo);

		optikOku = (Button) findViewById(R.id.optikOku);
		sayacBilgi = (Button) findViewById(R.id.sayacBilgi);
		buttonKaydet = (Button) findViewById(R.id.buttonKaydet);

		popupHaneSayisi = new PopupList<ISayacHaneSayisi>(textHaneSayisi, 350,
				(ISayacHaneSayisi[]) app.getEnumValues(IDef.ENUM_SAYAC_HANE_SAYISI), this);
		popupSayacKodu = new PopupList<ISayacKodu>(textSayacKodu, 350,
				(ISayacKodu[]) app.getEnumValues(IDef.ENUM_SAYAC_KODU), this);

		try {
			markalar = DbHelper.SelectAll(app.getConnection(), sayac_marka.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
			finish();
			return;
		}
		popupSayacMarka = new PopupList<ISayacMarka>(textMarka, 350, markalar, this);
		popupSayacCinsi = new PopupList<ISayacCinsi>(textSayacCinsi, 350, new ISayacCinsi[0], this);
		popupFazSayisi = new PopupList<IFazSayisi>(textFazSayisi, 350,
				(IFazSayisi[]) app.getEnumValues(IDef.ENUM_FAZ_SAYISI), this);
		popupVoltaj = new PopupList<IVoltaj>(textGerilim, 350,
				(IVoltaj[]) app.getEnumValues(IDef.ENUM_VOLTAJ), this);

		popupMulkiyet = new PopupList<IMulkiyet>(textMulkiyet, 500,
				(IMulkiyet[]) app.getEnumValues(IDef.ENUM_MULKIYET), this);

		optikOku.setOnClickListener(this);
		sayacBilgi.setOnClickListener(this);
		buttonKaydet.setOnClickListener(this);
		
		textMulkiyet.setTag(Mulkiyet.KurumSayaci);
		textMulkiyet.setText(textMulkiyet.getTag().toString());

		app.getMeterReader().setTriggerEnabled(true);
		app.setPortCallback(this);

	}

	@Override
	protected void onResume() {
		app.setPortCallback(this);
		super.onResume();

	}
	
	@Override
	public void onDestroy() {
		if (popupHaneSayisi != null)
			popupHaneSayisi.close();
		if (popupSayacKodu != null)
			popupSayacKodu.close();
		if (popupSayacMarka != null)
			popupSayacMarka.close();
		if (popupSayacCinsi != null)
			popupSayacCinsi.close();
		if (popupFazSayisi != null)
			popupFazSayisi.close();
		if (popupVoltaj != null)
			popupVoltaj.close();
		if (popupMulkiyet != null)
			popupMulkiyet.close();

		super.onDestroy();
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub
		
		if (anchorView.equals(textSayacKodu)) {
			textSayacKodu.setTag(obj);
			textSayacKodu.setText(obj.toString());
			SayacKodu sk = (SayacKodu) obj;
			ISayacCinsi[] sc = SayacCinsi.values();
			ISayacCinsi aktif;
			if (sk.equals(SayacKodu.Kombi)) {
				aktif = SayacCinsi.Kombi;
				sc = new ISayacCinsi[] { aktif };
			} else {
				aktif = SayacCinsi.Elektronik;
				sc = new ISayacCinsi[] { aktif, SayacCinsi.Mekanik };

			}
			if (popupSayacCinsi != null)
				popupSayacCinsi.close();
			popupSayacCinsi = new PopupList<ISayacCinsi>(textSayacCinsi, 350, sc, this);
			textSayacCinsi.setTag(aktif);
			textSayacCinsi.setText(aktif.toString());

		} else if (anchorView.equals(textHaneSayisi)) {
			textHaneSayisi.setTag(obj);
			textHaneSayisi.setText(obj.toString());
		} else if (anchorView.equals(textMarka)) {
			textMarka.setTag(obj);
			textMarka.setText(obj.toString());
		} else if (anchorView.equals(textSayacCinsi)) {
			textSayacCinsi.setTag(obj);
			textSayacCinsi.setText(obj.toString());
		} else if (anchorView.equals(textFazSayisi)) {
			textFazSayisi.setTag(obj);
			textFazSayisi.setText(obj.toString());
		} else if (anchorView.equals(textGerilim)) {
			textGerilim.setTag(obj);
			textGerilim.setText(obj.toString());
		} else if (anchorView.equals(textMulkiyet)) {
			textMulkiyet.setTag(obj);
			textMulkiyet.setText(obj.toString());
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		if (id == R.id.buttonKaydet) {
			Kaydet();
		}
		else if(id == R.id.sayacBilgi){
			
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
					if (app.checkException(SayacTakmaActivity.this, obj))
						return null;
					return null;
				}

			});
		}
	}

	private void Kaydet() {
		new LoadData(this, "Sayaç bilgisi gönderiliyor...", kaydet).execute();
	}

	Runnable kaydet = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				Kontrol();
				
				//sayaclar.Kontrol();
				//sayaclar.EndeksKontrol();
				
				//app.sendIslem(islem, null);
				
				SayacTakmaActivity.this.setResult(RESULT_OK);
				SayacTakmaActivity.this.finish();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(SayacTakmaActivity.this, e);
				return;
			}

		}

	};

	// -------------------------------------------------------------------------

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

		result.setReadMode(ReadMode2.PROGRAM_MOD);
		result.SetObisCodeMap(IReadResult.s_sayac_bilgi);
	}

	@Override
	public void EndRead() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Read(IProbeEvent event, IReadResult result) {
		// TODO Auto-generated method stub

		result.set_okuma_zamani(app.getTime());
		editSayacNo.setText(result.get_sayac_no());
		
	
		try {

			if (result.getHaneSayisi() != null) {
				textHaneSayisi
						.setTag(app.enumFromInteger(IDef.ENUM_SAYAC_HANE_SAYISI, result.getHaneSayisi()));
				textHaneSayisi.setText(textHaneSayisi.getTag().toString());
			}

			String flag = result.get_FlagCode();
			for (ISayacMarka sm : markalar) {
				if (sm.getSAYAC_MARKA_KODU().equals(flag)) {
					textMarka.setTag(sm);
					textMarka.setText(textMarka.getTag().toString());
					break;
				}
			}

			if (result.getKombi() != null) {
				textSayacCinsi.setTag(result.getKombi() == true ? SayacCinsi.Kombi : SayacCinsi.Elektronik);
				textSayacCinsi.setText(textSayacCinsi.getTag().toString());
			}

			if (result.getFazSayisi() != null) {
				textFazSayisi.setTag(app.enumFromInteger(IDef.ENUM_FAZ_SAYISI, result.getFazSayisi()));
				textFazSayisi.setText(textFazSayisi.getTag().toString());
			}

			if (result.getVoltaj() != null) {
				textGerilim.setTag(app.enumFromInteger(IDef.ENUM_VOLTAJ, result.getVoltaj()));
				textGerilim.setText(textGerilim.getTag().toString());
			}

			textMulkiyet.setTag(Mulkiyet.KurumSayaci);
			textMulkiyet.setText(textMulkiyet.getTag().toString());

			if (result.getUretimYili() != null) {
				editImalYili.setText(result.getUretimYili().toString());
			}
			
			islem.setOptikResult(result);
			
		} catch (Exception e) {
			app.ShowException(this, e);
		}
	}

	@Override
	public void PowerEvent(IProbeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ConnectionResetEvent(IProbeEvent event) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub


		/*
		if (arg0.equals(tab2)) {

			try {
				
				Kontrol();	
				sayaclar.add(islem);
				endeks.show(isemri, sayaclar, null);

			} catch (Exception e) {
				tabHost.setCurrentTab(0);
				app.ShowException(this, e);
				return;
			}
		} else if (arg0.equals(tab1)) {
			
		}*/
	}
	
	void Kontrol() throws Exception
	{
		
		if (editSayacNo.getText().toString().isEmpty())
			throw new MobitException("Sayaç numarasını girin!");
		
		int sayac_no = Integer.parseInt(editSayacNo.getText().toString());
		
		islem.setSAYAC_KODU((ISayacKodu) textSayacKodu.getTag());
		islem.setMARKA((ISayacMarka) textMarka.getTag());
		islem.setSAYAC_NO(sayac_no);
		islem.setSAYAC_CINSI((ISayacCinsi)textSayacCinsi.getTag());
		islem.setHANE_SAYISI((ISayacHaneSayisi) textHaneSayisi.getTag());
		if(!editImalYili.getText().toString().isEmpty()) islem.setIMAL_YILI(Integer.parseInt(editImalYili.getText().toString()));
		islem.setMULKIYET((IMulkiyet) textMulkiyet.getTag());
		islem.setFAZ_SAYISI((IFazSayisi) textFazSayisi.getTag());
		if(!editAkim.getText().toString().isEmpty()) islem.setAMPERAJ(Integer.parseInt(editAkim.getText().toString()));
		islem.setVOLTAJ((IVoltaj) textGerilim.getTag());
		islem.setSERI_NO(new SeriNo(editMuhurSeri.getText().toString(), editMuhurNo.getText().toString()));
		
		islem.Kontrol();
		
		//isemriIslem.getTakilanSayaclar().add(islem);
		
		
	}

	// -------------------------------------------------------------------------
}
