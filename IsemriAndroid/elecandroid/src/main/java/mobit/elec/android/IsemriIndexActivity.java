package mobit.elec.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.IMap;
import com.mobit.IMapMarker;

import mobit.elec.MsgInfo;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.ISeriNo;
import mobit.elec.mbs.get.SeriNo;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.MbtMeterInformation;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.ReadMode2;

public class IsemriIndexActivity extends AppCompatActivity implements IForm, OnKeyListener, ICallback, OnClickListener {

	IElecApplication app;
	boolean readOnly = false;

	EditText editMuhurSeri;
	EditText editMuhurNo;

	Button optikOku;
	Button elleOku;
	Button aboneDurum;
	Button fotoCek;
	Button buttonTamam;

	IIsemri isemri;
	IIsemriIslem isemriIslem;

	//IReadResult result = null;

	TesisatBilgiFragment bilgi;
	IndexFragment2 index;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_isemri_index);

		if (!(Globals.app instanceof IElecApplication)) {
			finish();
			Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
			return;
		}

		app = (IElecApplication) Globals.app;


		if (!(app.getActiveIslem() instanceof IIslemGrup)) {
			finish();
			Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
			return;
		}
		
		IIslemGrup islemGrup = (IIslemGrup)app.getActiveIslem();
		isemriIslem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
		isemri = isemriIslem.getIsemri();
		if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) {
			finish();
			Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
			return;
		}
		
		Globals.app.initForm(this);

		bilgi = (TesisatBilgiFragment) getSupportFragmentManager().findFragmentById(R.id.tesisatBilgiFragment);
		bilgi.show(isemri);

		index = (IndexFragment2) getSupportFragmentManager().findFragmentById(R.id.indexFragment);
		
		editMuhurSeri = (EditText) findViewById(R.id.editMuhurSeri);
		editMuhurNo = (EditText) findViewById(R.id.editMuhurNo);

		optikOku = (Button) findViewById(R.id.optikOku);
		elleOku = (Button) findViewById(R.id.elleOku);
		aboneDurum = (Button) findViewById(R.id.aboneDurum);
		fotoCek = (Button) findViewById(R.id.fotoCek);
		buttonTamam = (Button) findViewById(R.id.buttonTamam);

		optikOku.setOnClickListener(this);
		elleOku.setOnClickListener(this);
		aboneDurum.setOnClickListener(this);
		fotoCek.setOnClickListener(this);
		buttonTamam.setOnClickListener(this);


		hide();
		show();
		
		editMuhurSeri.requestFocus();
		app.showSoftKeyboard(this, true);
		
		

	}

	void clear() {

		this.runOnUiThread(new Runnable() {

			public void run() {
				editMuhurSeri.getText().clear();
				editMuhurNo.getText().clear();
				index.clear();
			}

		});

	}

	void hide() {

		index.hide();

	}

	void show() {

		hide();
		bilgi.show(isemri);
		ISeriNo seriNo = isemriIslem.getSERINO();
		editMuhurSeri.setText(seriNo.getSeri());
		if(seriNo.getNo() != 0)editMuhurNo.setText(Integer.toString(seriNo.getNo()));
		ISayacBilgi sayac = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);
		IReadResult result = (sayac != null) ? sayac.getOptikResult() : null; 
		index.show(isemriIslem, isemriIslem.getSAYACLAR(), result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.isemri_index, menu);
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
		} else if (id == R.id.durumKodGiris) {
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
			intent.putExtra("islemTipi", 1);
			startActivity(intent);
		} else if (id == R.id.isemriDetay) {
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
			startActivity(intent);
		}
		

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(true);
		app.setPortCallback(this);
		super.onResume();

	}
	@Override
	protected void onPause(){
		app.showSoftKeyboard(this, false);
	    super.onPause();
	}
	

	OnEditorActionListener editorListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			boolean handled = false;
			if (actionId == EditorInfo.IME_ACTION_DONE) {

				buttonTamam.performClick();
				handled = true;
			}
			return handled;
		}
	};

	@Override
	public void Opened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Failed() {
		// TODO Auto-generated method stub
		app.ShowMessage(this, MsgInfo.OPTIK_PORT_BAGLANTI_KESILDI);
	}

	@Override
	public void BeginRead(IReadResult result) {
		// TODO Auto-generated method stub
		
		index.hide();

		result.setReadMode(ReadMode2.READOUT_MOD);

		ISayacBilgi sb = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);

		if (result.getReadMode().equals(ReadMode2.PROGRAM_MOD))
			// Sadece program modda anlamlı!!!
			if (sb.getSAYAC_KODU().equals(SayacKodu.Kombi)) {
			result.SetObisCodeMap(IReadResult.s_kombi_endeks);
			} else {
			result.SetObisCodeMap(IReadResult.s_aktif_endeks);
			}

	}

	@Override
	public void EndRead() {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void Read(IProbeEvent event, final IReadResult result) {
		// TODO Auto-generated method stub

		MbtMeterInformation emi = result.get_Information();

		result.set_okuma_zamani(app.getTime());
		/*
		 * // Sayaç ve hane sayısı kontrolü kapatıldı if (false &&
		 * Globals.isDeveloping()) { optikEndeksDoldur(result); return; }
		 */
		sayacKontrol(result);
	}

	void sayacKontrol(final IReadResult result) {

		ISayacBilgi syc = null;
		int sayac_no = Integer.parseInt(result.get_sayac_no());
		for (ISayacBilgi s : isemriIslem.getSAYACLAR().getSayaclar()) {
			if (sayac_no == s.getSAYAC_NO()) {
				syc = s;
				break;
			}
		}
		if (syc != null) {
			app.setOptikResult(result);
			sayacHaneKontrol(syc, result);
			return;
		}

		app.ShowMessage(this, MsgInfo.FARKLI_SAYAC_OKUTTUNUZ, DialogMode.YesNo, 1, 0, new IDialogCallback() {

			@Override
			public void DialogResponse(int msg_id, DialogResult rs) {
				// TODO Auto-generated method stub
				if (msg_id == 1) {
					if (rs.equals(DialogResult.Yes)) {
						final ISayacBilgi _syc = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);
						sayacHaneKontrol(_syc, result);
					}
				}
			}

		});

	}

	void sayacHaneKontrol(final ISayacBilgi syc, final IReadResult result) {

		final ISayaclar sayaclar = isemriIslem.getSAYACLAR();
		if (sayaclar.getSayac(SayacKodu.Aktif).getHANE_SAYISI().getValue() == result.getHaneSayisi()) {
			index.show(isemriIslem, sayaclar, result);
			return;
		}


		app.ShowMessage(this, MsgInfo.SAYAC_HANE_SAYISI_FARKLI, DialogMode.YesNo, 2, 0, new IDialogCallback() {

			@Override
			public void DialogResponse(int msg_id, DialogResult rs) {
				// TODO Auto-generated method stub
				if (rs.equals(DialogResult.Yes)) {

					index.show(isemriIslem, sayaclar, result);

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (status == null)
			return;
		//app.ShowMessage(this, String.format("Optik port enerji seviyesi:%%%d", status.batteryLifePercent), "Uyarı");
	}

	@Override
	public void ConnectionResetEvent(IProbeEvent event) {
		// TODO Auto-generated method stub
		app.ShowMessage(this, MsgInfo.OPTIK_PORT_BAGLANTI_KESILDI);
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		try {
			if (id == R.id.buttonTamam) {
//button tamama tıklandı "Endex Girişi Sayfası"
				Tamam();

			} else if (id == R.id.optikOku) {
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
						if (app.checkException(IsemriIndexActivity.this, obj))
							return null;
						return null;
					}

				});
			}
			else if(id == R.id.elleOku){

				index.show(isemriIslem, isemriIslem.getSAYACLAR(), null);
			}
			 else if (id == R.id.aboneDurum) {
				Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
				startActivity(intent);
			}
			 else if(id == R.id.fotoCek){
				app.getMeterReader().setTriggerEnabled(false);
				app.setPortCallback(null);
				FotografCekme fc = new FotografCekme(this, null);
				fc.run();
			 }
			/*
			 else if (id == R.id.sayacDurum) {
					Intent intent = new Intent(this, app.getFormClass(IDef.FORM_SAYAC_DURUM_GIRIS));
					startActivity(intent);
				}*/
			
		} catch (Exception e) {

			app.ShowException(this, e);
		}
	}

	void Tamam() {

		try {

			index.save();
			isemriIslem.setSERINO(new SeriNo(editMuhurSeri.getText().toString(), editMuhurNo.getText().toString()));
			isemriIslem.IslemKontrol(app);
			isemriIslem.setIsemriTamamlanacak(true);
			List<IReadResult> results = isemriIslem.getSAYACLAR().getOptikResult();
			isemriIslem.setOPTIK_DATA(app.getOptikData(results));
			isemriIslem.setZAMAN(app.getTime());

			setResult(RESULT_OK);
			finish();


		} catch (Exception e) {
			app.ShowException(this, e);
		}

	}

	/*
	Runnable tamam = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				
				app.sendIslem(isemriIslem, new Callback() {

					public Object run(final Object obj) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (obj instanceof Throwable) {
									app.ShowException(IsemriIndexActivity.this, (Throwable) obj);
									return;
								}
								setResult(RESULT_OK, new Intent());
								IsemriIndexActivity.this.finish();
							}
						});

						return null;
					}
				});

				result = null;

			} catch (Exception e) {
				e.printStackTrace();
				app.ShowException(IsemriIndexActivity.this, e);

			}
		}

	};*/

}
