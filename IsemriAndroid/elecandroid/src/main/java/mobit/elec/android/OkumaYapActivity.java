package mobit.elec.android;

import java.util.ArrayList;
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
import com.mobit.MobitException;
import com.mobit.RecordStatus;
import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.PopupList;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.MbtProbePowerStatus;
import mobit.elec.IKarne;
import mobit.elec.MsgInfo;
import mobit.elec.enums.IslemTipi;
import mobit.elec.ElecApplication;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.INavigate;
import mobit.elec.ISayacBilgi;
import mobit.elec.ITesisat;
import mobit.elec.mbs.get.Cbs;

public class OkumaYapActivity extends AppCompatActivity
		implements IForm, OnKeyListener, ICallback, OnClickListener, IPopupCallback {

	ElecApplication app;
	boolean readOnly = false;

	EditText editTesisatSayacNo;
	EditText editKarneNo;
	TextView textKarneler;
	
	Button optikOku;
	Button elleOku;
	Button aboneDurum;
	
	Button buttonTamam;
	
	Button buttonOnceki;
	Button buttonSonraki;

	IIsemriIslem islem;
	IIsemri isemri;
	
	PopupList<IKarne> popup = null;
	
	TesisatBilgiFragment bilgi;
	IndexFragment index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_okuma_yap);

		if(!(Globals.app instanceof ElecApplication)){
			finish();
			return;
		}
		app = (ElecApplication) Globals.app;
		app.initForm(this);
		
		editTesisatSayacNo = (EditText) findViewById(R.id.editTesisatSayacNo);
		editKarneNo = (EditText) findViewById(R.id.editKarneNo);
		textKarneler = (TextView) findViewById(R.id.textKarneler);

		optikOku = (Button) findViewById(R.id.optikOku);
		elleOku = (Button) findViewById(R.id.elleOku);
		aboneDurum = (Button) findViewById(R.id.aboneDurum);
		buttonTamam = (Button) findViewById(R.id.buttonTamam);

		buttonOnceki = (Button) findViewById(R.id.buttonOnceki);
		buttonSonraki = (Button) findViewById(R.id.buttonSonraki);

		editTesisatSayacNo.setImeOptions(EditorInfo.IME_ACTION_GO);
		editKarneNo.setImeOptions(EditorInfo.IME_ACTION_GO);

		editTesisatSayacNo.setOnEditorActionListener(editorListener);
		editKarneNo.setOnEditorActionListener(editorListener);

		optikOku.setOnClickListener(this);
		elleOku.setOnClickListener(this);
		aboneDurum.setOnClickListener(this);
		buttonTamam.setOnClickListener(this);

		buttonOnceki.setOnClickListener(this);
		buttonSonraki.setOnClickListener(this);
		
		editTesisatSayacNo.setSelectAllOnFocus(true);
		editKarneNo.setSelectAllOnFocus(true);
		
		bilgi = (TesisatBilgiFragment)getSupportFragmentManager().findFragmentById(R.id.tesisatBilgiFragment);
		index = (IndexFragment)getSupportFragmentManager().findFragmentById(R.id.indexFragment);

		app.getMeterReader().setTriggerEnabled(true);
		app.setPortCallback(this);
		
		try {
			popup = new PopupList<IKarne>(textKarneler, 350, app.getOkumaKarneListesi(), this);
			show(app.getActiveIsemri(), app.getOptikResult());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
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
		getMenuInflater().inflate(R.menu.okuma_yap, menu);
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
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
			intent.putExtra("islemTipi", 2);
			startActivity(intent);
			return true;

		}/* else if (id == R.id.sayacDurumKodGiris) {
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_SAYAC_DURUM_GIRIS));
			intent.putExtra("islemTipi", 2);
			startActivity(intent);
			return true;

		}*/ else if (id == R.id.tesisatDetay) {
			IIsemri t = app.getActiveIsemri();
			if(t == null){
				app.ShowMessage(OkumaYapActivity.this, "Önce bir tesisata konumlanın!", "");
				return false;
			}
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
			startActivity(intent);
			return true;
		} else if (id == R.id.tekrarYazdirma) {
			IIsemri t = app.getActiveIsemri();
			if(t != null){
				try {
					app.printIsemriIslem(t.getTESISAT_NO());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					app.ShowException(this, e);
					e.printStackTrace();
				}
			}
			return true;
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
						app.ShowException(OkumaYapActivity.this, e);
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
				Tamam();
			}
			else if(id == R.id.buttonSonraki){
				if(isemri == null) throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				show(((INavigate)isemri).next(false), null);
				
			}
			else if(id == R.id.buttonOnceki){
				if(isemri == null) throw new MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN);
				show(((INavigate)isemri).prev(false), null);
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
						if (app.checkException(OkumaYapActivity.this, obj))
							return null;
						return null;
					}

				});
				
			}
			else if(id == R.id.elleOku){
				
				show(isemri, null);
			}
			else if(id == R.id.aboneDurum){
				
				Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
				startActivityForResult(intent, IDef.FORM_ABONE_DURUM_GIRIS);
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
				editTesisatSayacNo.setText(result.get_sayac_no());
				tesisatBul(false, result);
				
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

			tesisat_no = Integer.parseInt(editTesisatSayacNo.getText().toString());
			show(app.position(tesisat_no), result);

		} catch (MobitException ex) {
			if(ex.getCode() == MsgInfo.TESISAT_VEYA_SAYAC_BULUNAMADI && !recursive){
				final int tno = tesisat_no;
				app.ShowMessage(OkumaYapActivity.this, MsgInfo.TESISAT_BULUNAMADI_SUNUCUDA_SORGULA, 
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
										app.ShowException(OkumaYapActivity.this, e);
										return;
									}
									tesisatBul(true, result);
								}
								else {
									if(ex != null) app.ShowException(OkumaYapActivity.this, ex);
								}
					
							}	
				});
			}
		}
		catch (Exception e) {	
				
			app.ShowException(OkumaYapActivity.this, e);
			
		}
		
	}

	void show(IIsemri t, IReadResult result) throws Exception {

		if(t == null) return;
		
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
		
		
	}

	
	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub
		if (anchorView.equals(textKarneler)) {
			String s;
			if(obj instanceof IKarne){
				IKarne karne = (IKarne)obj;
				s = Integer.toString(karne.getKARNE_NO());
			}
			else {
				s = obj.toString();
			}

			editKarneNo.setText(s);
			editorListener.onEditorAction(editKarneNo, EditorInfo.IME_ACTION_GO,
					new KeyEvent(0, 0, KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_ENTER, 0));
		}
	}

	void Tamam() {

		IIsemri t = app.getActiveIsemri();
		if (t == null)
			return;

		try {
			
			index.save();
			islem.IslemKontrol(app);

			//new LoadData(this, "Gönderiliyor...", tamam).execute();
			tamam.run();

		} catch (Exception e) {
			app.ShowException(this, e);
		}

	}

	Runnable tamam = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {

				IIslemGrup islemGrup = (IIslemGrup)app.getActiveIslem();
				islem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
				
				islem.setELEMAN_KODU(app.getEleman().getELEMAN_KODU());

				islem.setCBS(new Cbs(app.getLocation()));
					
				List<IReadResult> results = islem.getSAYACLAR().getOptikResult();
				islem.setOPTIK_DATA(app.getOptikData(results));
				
				islem.setDURUM(RecordStatus.Saved);
				
				IIslemGrup masterGrup = (IIslemGrup)app.saveIslem(islemGrup);
				app.sendIslem(OkumaYapActivity.this, masterGrup, new Callback() {

					@Override
					public Object run(final Object obj) {
						// TODO Auto-generated method stub
						OkumaYapActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(obj instanceof Throwable){
									app.ShowException(OkumaYapActivity.this, (Throwable)obj);
									return;
								}
								buttonSonraki.performClick();
							}
						});
						return null;
					}

				}, 15000);
	

			} catch (Exception e) {
				e.printStackTrace();
				app.ShowException(OkumaYapActivity.this, e);

			}
		}

	};

}
