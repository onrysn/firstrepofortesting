package mobit.elec.mbs.medas.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobit.BarcodeResult;
import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;

import mobit.eemr.Lun_Control;
import mobit.elec.Aciklama;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriZabit;
import mobit.elec.IMuhurSokme;
import mobit.elec.android.MuhurSokmeActivity;
import mobit.elec.android.SendingListActivity;
import mobit.elec.android.ZabitActivity;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.mbs.put.put_isemri_unvan;
import mobit.elec.mbs.put.put_isemri_zabit;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.server.IslemGrup2;
import mobit.elec.medas.ws.SayacZimmetBilgi.zabitBilgi;

public class ZabitActivity2 extends AppCompatActivity implements IForm, OnClickListener {

	IMedasApplication app = null;
	IIslemGrup islemGrup;
	IIsemriIslem islem;
	IIsemriZabit zabit;
	Spinner zabitSeri;
	EditText editZabitNo;
	EditText editKullanici2Kodu;

	IElecApplication app2=null;


	int mode = IDef.MBS_ZABIT;

	IBarcode barcode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zabit2);
		if (!(Globals.app instanceof IElecApplication)) {
			// Desteklenmiyor
			finish();
			return;
		}
		if (!(Globals.app instanceof IMedasApplication)) {
			finish();
			return;
		}
		app = (IMedasApplication) Globals.app;
		if (!(app.getActiveIslem() instanceof IIslemGrup)) {
			finish();
			return;
		}

		islemGrup = (IIslemGrup)app.getActiveIslem();
		islem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);

		if (!islem.getIsemri().getISLEM_TIPI().equals(IslemTipi.Tespit)) {
			finish();
			return;
		}
		
		List<IIslem> list = islemGrup.getIslem(IIsemriZabit.class);
		if(list.size() == 0){
			zabit = ((IIsemri2)islem.getIsemri()).newZabit();
			islemGrup.add(zabit);
		}
		else {
			zabit = (IIsemriZabit)list.get(0);
		}
		
		
		
		app.initForm(this);
		app2 = (IElecApplication) Globals.app;

		app2.initForm(this);

		Bundle bundle = null;
		if (bundle != null) {
			mode = bundle.getInt("MODE");
		}
		else{
			Lun_Control ln=new Lun_Control();
			mode=ln.ZabitMod;
		}

		zabitSeri = (Spinner) findViewById(R.id.zabitSeri);
		editZabitNo = (EditText) findViewById(R.id.editZabitNo);
		editKullanici2Kodu = (EditText) findViewById(R.id.editKullanici2Kodu);

		((Button) findViewById(R.id.barkod)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonKaydet)).setOnClickListener(this);
		
		ArrayAdapter<zabitBilgi> spinnerAdapter = 
				new ArrayAdapter<zabitBilgi>(this, android.R.layout.simple_spinner_item, app.getZabitSeriler());
		zabitSeri.setAdapter(spinnerAdapter);

		
		if (zabit != null) {
			
			zabit.setZABIT_TIPI(mode);
			
			mobit.android.utility.selectSpinnerItemByValue(zabitSeri, zabit.getZABIT_SERI());
			if(zabit.getZABIT_NO() > 0) editZabitNo.setText(Integer.toString(zabit.getZABIT_NO()));
			if(zabit.getOKUYUCU2_KODU() > 0) editKullanici2Kodu.setText(Integer.toString(zabit.getOKUYUCU2_KODU()));
		}

		barcode = app.newBarcodeObject();

	}

	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);
		super.onResume();

	}
	@Override
	public void onBackPressed() {
		//Onur zabıt ekranından geri çıkılmasını engelleme kapatılmıştı sahada istisnai durumlar meydana geldiği için tekrar açıldı sonra toparlanacak.
		super.onBackPressed();

		if(zabit != null) islemGrup.remove(zabit);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.zabit, menu);
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.buttonKaydet) {
			Zabit(mode);
		}
		else if(arg0.getId() == R.id.barkod){
			barcode.startScanner(this);
		}

	}

	void Kontrol() throws MobitException {
		if (zabitSeri.getSelectedItem() == null)
			throw new MobitException("Zabıt seri girin!");
		if (editZabitNo.getText().toString().isEmpty())
			throw new MobitException("Zabıt no girin!");
		if (editKullanici2Kodu.getText().toString().isEmpty())
			throw new MobitException("İkinci kullanıcı kodu girin!");
	}
	
	void Zabit(int mode) {
        final int[] hata = {0};
		try {
			Lun_Control Tesisat=new Lun_Control();
			if (Tesisat.KacakVarmi==0) {
				Kontrol();


				zabit.setZABIT_SERI(zabitSeri.getSelectedItem().toString().toUpperCase());
				zabit.setZABIT_NO(Integer.parseInt(editZabitNo.getText().toString()));
				zabit.setOKUYUCU2_KODU(Integer.parseInt(editKullanici2Kodu.getText().toString()));
				zabit.setZABIT_TIPI(mode);
				zabit.setOKUYUCU_KODU(app.getEleman().getELEMAN_KODU());

				setResult(RESULT_OK);
				finish();
			}
			else {

				Kontrol();


				zabit.setZABIT_SERI(zabitSeri.getSelectedItem().toString().toUpperCase());
				zabit.setZABIT_NO(Integer.parseInt(editZabitNo.getText().toString()));
				zabit.setOKUYUCU2_KODU(Integer.parseInt(editKullanici2Kodu.getText().toString()));
				zabit.setZABIT_TIPI(mode);
				zabit.setOKUYUCU_KODU(app.getEleman().getELEMAN_KODU());
				IIsemri2 isemri = (IIsemri2) app.getActiveIsemri();

				//send(islemGrup,true);
				//muhammed gökkaya zabit
				try {

					put_isemri_zabit zz = new put_isemri_zabit(isemri.getTESISAT_NO(), isemri.getSAHA_ISEMRI_NO(), zabit.getZABIT_SERI(), zabit.getZABIT_NO(), zabit.getOKUYUCU2_KODU(), zabit.getZABIT_TIPI(), zabit.getOKUYUCU_KODU());

					islemGrup = new IslemGrup2();
					islemGrup.add(zz);

					islemGrup.setIslemTipi(mobit.elec.android.IDef.ZABIT_EKLEME_ISLEMI);

					IIslem islem = app2.saveIslem(islemGrup);

					app2.sendIslem(this, islem, new Callback() {
						@Override
						public Object run(Object obj) {
							if (app2.checkException(ZabitActivity2.this, obj)) {
								hata[0] = 1;
								return obj;
							}

							app2.ShowMessage(ZabitActivity2.this, "Zabit  Eklendi", "Başarılı");
							setResult(RESULT_OK);
							Lun_Control Tesisat2 = new Lun_Control();
							Tesisat2.setKacakVarmi(0);
							Tesisat2.setZabitMod(0);
							Tesisat2.setZabitDur(0);
							Tesisat2.setZabitIsemriNo(0);
							Tesisat2.setZabitTesisat(0);
							finish();
							return null;
						}
					}, 10000);


				} catch (Exception e) {
					app2.ShowException(ZabitActivity2.this, e);
				}
			}


		} catch (Exception e) {
			app.ShowException(this, e);
			return;
		}

        setResult(RESULT_OK);
		//finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		BarcodeResult result = barcode.getScanResult(requestCode, resultCode, data);
		if(result.resultStatus == BarcodeResult.SUCCESS){
			View view = getCurrentFocus();
			if(view instanceof EditText){
				EditText edit = (EditText)view;
				edit.setText(result.resultData);
			}
		}

	}


	/*
	void MbsZabit() {

		try {

			Kontrol();

			if (zabit == null)
				zabit = islem.newZabit();
			zabit.setZABIT_SERI(zabitSeri.getSelectedItem().toString().toUpperCase());
			zabit.setZABIT_NO(Integer.parseInt(editZabitNo.getText().toString()));
			zabit.setOKUYUCU2_KODU(Integer.parseInt(editKullanici2Kodu.getText().toString()));
			islem.setZabit(zabit);
			
			setResult(RESULT_OK);
			finish();

		} catch (Exception e) {
			app.ShowException(this, e);
			return;
		}

	}

	void MedasZabit() {

		try {

			szb.kacak_kayit(islem.getSAHA_ISEMRI_NO(), app.getUTCTime(), zabitSeri.getSelectedItem().toString().toUpperCase(),
					Integer.parseInt(editZabitNo.getText().toString()), app.getEleman().getELEMAN_KODU(),
					Integer.parseInt(editKullanici2Kodu.getText().toString()), new Callback() {

						@Override
						public Object run(Object obj) {
							// TODO Auto-generated method stub
							if (app.checkException(ZabitActivity2.this, obj))
								return null;
							
							setResult(RESULT_OK);
							finish();
							return null;
						}
					});

		} catch (Exception e) {
			app.ShowException(this, e);
			return;
		}
	}*/
}
