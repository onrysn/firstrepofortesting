package mobit.elec.mbs.medas.android;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.MobitException;
import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.PopupList;
import mobit.elec.Globals;
import mobit.elec.mbs.IIslemMaster;
import mobit.elec.mbs.IslemMaster2;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.mbs.medas.kacak;
import mobit.elec.mbs.server.IslemGrup2;

import mobit.elec.medas.ws.SayacZimmetBilgi.KacakBilgisi;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;

public class KacakGirisActivity extends AppCompatActivity implements IForm, OnClickListener, IPopupCallback {

	IMedasApplication app;
	SayacZimmetBilgi szb;
	
	EditText editAdresTarif;
	EditText editKacakciUnvan;
	EditText editKacakciTelefon;
	EditText editKacakciEmail;
	TextView kacakTipi;
	EditText editIhbarEdenUnvan;
	EditText editIhbarEdenTelefon;
	EditText editIhbarEdenEmail;
	EditText editTesisatNo;
	EditText editReferansTesisatNo;
	EditText editDirekNo;
	EditText editBoxNo;
	EditText editSayacNo;
	EditText editAciklama;

	Button buttonKaydet;
	
	PopupList<String> popupKacakTipi;
	
	kacak k;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kacak_giris);

		if (!(Globals.app instanceof IMedasApplication)) {
			finish();
			return;
		}
		app = (IMedasApplication) Globals.app;

		app.initForm(this);

		editAdresTarif = (EditText) findViewById(R.id.editAdresTarif);
		editKacakciUnvan = (EditText) findViewById(R.id.editKacakciUnvan);
		editKacakciTelefon = (EditText) findViewById(R.id.editKacakciTelefon);
		editKacakciEmail = (EditText) findViewById(R.id.editKacakciEmail);
		kacakTipi = (TextView) findViewById(R.id.kacakTipi);
		editIhbarEdenUnvan = (EditText) findViewById(R.id.editIhbarEdenUnvan);
		editIhbarEdenTelefon = (EditText) findViewById(R.id.editIhbarEdenTelefon);
		editIhbarEdenEmail = (EditText) findViewById(R.id.editIhbarEdenEmail);
		editTesisatNo = (EditText) findViewById(R.id.editTesisatNo);
		editReferansTesisatNo = (EditText) findViewById(R.id.editReferansTesisatNo);
		editDirekNo = (EditText) findViewById(R.id.editDirekNo);
		editBoxNo = (EditText) findViewById(R.id.editBoxNo);
		editSayacNo = (EditText) findViewById(R.id.editSayacNo);
		editAciklama = (EditText) findViewById(R.id.editAciklama);
		
		buttonKaydet = (Button) findViewById(R.id.buttonKaydet);
		
		buttonKaydet.setOnClickListener(this);
		
		
		popupKacakTipi = new PopupList<String>(kacakTipi, 200, IDef.kacakTipleri, this);

		szb = new SayacZimmetBilgi();
		
		if(Globals.isDeveloping()){
			doldur();
		}
		
		k = new kacak();
	}
	
	@Override
	protected void onDestroy()
	{
		if(popupKacakTipi != null) popupKacakTipi.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);
		super.onResume();

	}
	
	void doldur()
	{
		
		editAdresTarif.setText("ATARIF_DENEME");
		editKacakciUnvan.setText("UNV DENEME");
		editKacakciTelefon.setText("TEL DENEME");
		editKacakciEmail.setText("DENEME@DENEME");
		kacakTipi.setText("KACAKCI_TIPI DENEME");
		editIhbarEdenUnvan.setText("IHBAR EDEN DENEME");
		editIhbarEdenTelefon.setText("TEL DENEME");
		editIhbarEdenEmail.setText("DENEME@DENEME");
		editTesisatNo.setText("1");
		editReferansTesisatNo.setText("2");
		
		editDirekNo.setText("10");
		editBoxNo.setText("10");
        editSayacNo.setText("123");
        editAciklama.setText("ACIKLAMA DENEME");
        
        
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean Kontrol()  {
		
		if (editAdresTarif.getText().toString().isEmpty()) {
			editAdresTarif.setError("Boş olamaz!");
			editAdresTarif.requestFocus();
			return false;
		}
		if (editKacakciUnvan.getText().toString().isEmpty()) {
			editKacakciUnvan.setText("");

		}
		if (editKacakciTelefon.getText().toString().isEmpty()) {
			editKacakciTelefon.setText("");
		}
		if (editKacakciEmail.getText().toString().isEmpty()) {
			editKacakciEmail.setText("");
		}
		if (kacakTipi.getText().toString().isEmpty()) {
			kacakTipi.setError("Boş olamaz!");
			kacakTipi.requestFocus();
			return false;
		}
		if (editIhbarEdenUnvan.getText().toString().isEmpty()) {
			editIhbarEdenTelefon.setText("");
		}
		if (editIhbarEdenTelefon.getText().toString().isEmpty()) {
			editIhbarEdenTelefon.setText("");
		}
		if (editIhbarEdenEmail.getText().toString().isEmpty()) {
			editIhbarEdenEmail.setText("");
		}
		if (editTesisatNo.getText().toString().isEmpty()) {
			editTesisatNo.setText("");
		}
		if (editReferansTesisatNo.getText().toString().isEmpty()) {
			editReferansTesisatNo.setError("Boş olamaz!");
			editReferansTesisatNo.requestFocus();
			return false;
		}
		if (editDirekNo.getText().toString().isEmpty()) {
			editDirekNo.setError("Boş olamaz!");
			editDirekNo.requestFocus();
			return false;
		}
		if (editBoxNo.getText().toString().isEmpty()) {
			editBoxNo.setText("");
		}
		if (editSayacNo.getText().toString().isEmpty()) {
			editSayacNo.setText("");
		}
		if (editAciklama.getText().toString().isEmpty()) {
			editAciklama.setError("Açıklama Boş olamaz!");
			editAciklama.requestFocus();
			return false;
		}
		return true;
	}

	void Kaydet() {
		if (!Kontrol())
			return;
		try {

			k.setId(null);
			k.setADRES_TARIF(editAdresTarif.getText().toString());
			k.setKACAKCI_UNVAN(editKacakciUnvan.getText().toString());
			k.setKACAKCI_TELEFON(editKacakciTelefon.getText().toString());
			k.setKACAKCI_EMAIL(editKacakciEmail.getText().toString());
			k.setKACAK_TIPI(kacakTipi.getText().toString());
			k.setIHBAR_EDEN_UNVAN(editIhbarEdenUnvan.getText().toString());
			k.setIHBAR_EDEN_TELEFON(editIhbarEdenTelefon.getText().toString());
			k.setIHBAR_EDEN_EMAIL(editIhbarEdenEmail.getText().toString());
			if (!editTesisatNo.getText().toString().equals(""))
				k.setTESISAT_NO(Integer.parseInt(editTesisatNo.getText().toString()));
			else
				k.setTESISAT_NO(0);
			if (!editReferansTesisatNo.getText().toString().equals(""))
				k.setREFERANS_TESISAT_NO(Integer.parseInt(editReferansTesisatNo.getText().toString()));
			else
				k.setREFERANS_TESISAT_NO(0);
			k.setDIREK_NO(editDirekNo.getText().toString());
			k.setBOX_NO(editBoxNo.getText().toString());
			if (!editSayacNo.getText().toString().equals(""))
				k.setSAYAC_NO(Integer.parseInt(editSayacNo.getText().toString()));
			else
				k.setSAYAC_NO(0);
			k.setACIKLAMA(editAciklama.getText().toString());

			IIslemGrup grup = new IslemGrup2();
			grup.add(k);
			grup.setIslemTipi(IDef.KACAK_BILDIRIM);
			IIslemMaster master = new IslemMaster2(app, k);
			IIslem islem = app.saveIslem(grup);
			app.sendIslem(this, islem, new Callback(){

				@Override
				public Object run(Object obj) {
					// TODO Auto-generated method stub
					if(app.checkException(KacakGirisActivity.this, obj))return null;
					
					KacakGirisActivity.this.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							app.ShowMessage(KacakGirisActivity.this, "Tamamlandı!", "", DialogMode.Ok, 1, 0, new IDialogCallback(){
								@Override
								public void DialogResponse(int msg_id, DialogResult result) {
									// TODO Auto-generated method stub
									setResult(RESULT_OK);
									finish();
								}
								
							});
							
						}
						
					});
					
					return null;
				}
				
			}, 10000);
			
		} catch (Exception e) {
			app.ShowException(this, e);
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId() == R.id.buttonKaydet){
			Kaydet();
		}
	}

	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub
		if(anchorView.getId() == R.id.kacakTipi){
			kacakTipi.setTag(obj);
			kacakTipi.setText(kacakTipi.getTag().toString());
		}
		
	}
}
