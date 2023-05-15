package mobit.elec.android;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mobit.BarcodeResult;
import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;
import mobit.android.ArrayAdapterEx;
import mobit.android.LoadData;
import mobit.android.ViewHolderEx;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.IMuhurKod;
import mobit.elec.IMuhurSokme;
import mobit.elec.mbs.IslemMaster2;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.server.IslemGrup2;

public class MuhurSokmeActivity extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app = null;

	EditText editTesisatNo;
	EditText editMuhurSeri;
	EditText editMuhurNo;
	ListView listMuhurDurum;
	ListView listIptalDurum;
	Button buttonTamam;
	Button barkod;

	List<IMuhurKod> arrayMuhurDurum;
	ArrayAdapter<IMuhurKod> adapterMuhurDurum;

	List<IMuhurKod> arrayIptalDurum;
	ArrayAdapter<IMuhurKod> adapterIptalDurum;
	
	IMuhurSokme muhurSokme;
	IIsemri2 isemri;
	IBarcode barcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_muhur_sokme);

		if(!(Globals.app instanceof IElecApplication)){
			finish();
			return;
		}
		
		app = (IElecApplication)Globals.app;
		
		isemri = (IIsemri2)app.getActiveIsemri();
		
		IIslem islem = app.getActiveIslem();
		if(islem != null){
			if(!(islem instanceof IIslemGrup)){
				finish();
				return;
			}
			IIslemGrup grup = (IIslemGrup)islem;
			List<IIslem> islemler = grup.getIslem(IMuhurSokme.class);
			if(islemler.size() == 0){
				
				islemler.add(isemri.newMuhurSokme());
			}
			else {
				muhurSokme = (IMuhurSokme)islemler.get(0);
			}
		}
		
		app.initForm(this);

		editTesisatNo = (EditText) findViewById(R.id.editTesisatNo);
		editMuhurSeri = (EditText) findViewById(R.id.editMuhurSeri);
		editMuhurNo = (EditText) findViewById(R.id.editMuhurNo);
		listMuhurDurum = (ListView) findViewById(R.id.listMuhurDurum);
		listIptalDurum = (ListView) findViewById(R.id.listIptalDurum);
		buttonTamam = (Button) findViewById(R.id.buttonTamam);
		barkod = (Button) findViewById(R.id.barkod);

		buttonTamam.setOnClickListener(this);
		barkod.setOnClickListener(this);

		List<IMuhurKod> list = null;
		try {
			list = app.getMuhurKod(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
			finish();
			return;
		}
		if (list != null) {
			arrayMuhurDurum = new ArrayList<IMuhurKod>();
			for (IMuhurKod mk : list)
				if (mk.getKOD_CINSI() == MuhurKodCins.Durum)
					arrayMuhurDurum.add(mk);
			adapterMuhurDurum = new ArrayAdapterEx<IMuhurKod>(this, R.layout.row_with_radio, arrayMuhurDurum,
					ViewHolder.class);
			listMuhurDurum.setAdapter(adapterMuhurDurum);

			arrayIptalDurum = new ArrayList<IMuhurKod>();
			for (IMuhurKod mk : list)
				if (mk.getKOD_CINSI() == MuhurKodCins.Iptal)
					arrayIptalDurum.add(mk);
			adapterIptalDurum = new ArrayAdapterEx<IMuhurKod>(this, R.layout.row_with_radio, arrayIptalDurum,
					ViewHolder.class);
			listIptalDurum.setAdapter(adapterIptalDurum);
		}
		
		if(islem instanceof IIslemGrup){
			IIslemGrup grup = (IIslemGrup)islem;
			List<IIslem> islemler = grup.getIslem(IIsemriIslem.class);
			if(islemler.size() > 0){
				IIsemriIslem iislem = (IIsemriIslem)islemler.get(0);
				editTesisatNo.setText(Integer.toString(iislem.getTESISAT_NO()));
				editTesisatNo.setFocusableInTouchMode(false);
				editTesisatNo.setFocusable(false);
			}
		}

		barcode = app.newBarcodeObject();
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

	public class ViewHolder<T> extends ViewHolderEx<T> {

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);

			View v;

			v = view.findViewById(R.id.rowTextView);
			v.setOnClickListener(this);
			columnList.add(v);

			v = view.findViewById(R.id.radioButton);
			v.setOnClickListener(this);
			columnList.add(v);

		}

		@Override
		public void set(int position, T obj) {

			super.set(position, obj);

			IMuhurKod mkod = (IMuhurKod) item;

			TextView tv;
			tv = (TextView) columnList.get(0);
			tv.setText(mkod.getMUHUR_ACIKLAMA());
			tv.setTag(mkod);
			RadioButton rb;
			rb = (RadioButton) columnList.get(1);
			rb.setTag(mkod);
			rb.setChecked(mkod.getCheck());

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			int id = arg0.getId();
			if (id == columnList.get(1).getId()) {
				IMuhurKod mk;
				for (T i : adapter.getListe()) {
					mk = (IMuhurKod) i;
					if (mk.getCheck()) {
						mk.setCheck(false);
						break;
					}
				}
				mk = (IMuhurKod) arg0.getTag();
				mk.setCheck(true);
				adapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.buttonTamam) {
					
			try {

				if(editTesisatNo.getText().length() == 0) 
					throw new MobitException("Tesisat numarasını giriniz!");
				
				IMuhurKod muhurDurum = null;
				IMuhurKod iptalDurum = null;
				for (IMuhurKod mk : arrayMuhurDurum)
					if (mk.getCheck()) {
						muhurDurum = mk;
						break;
					}

				for (IMuhurKod mk : arrayIptalDurum)
					if (mk.getCheck()) {
						iptalDurum = mk;
						break;
					}

				try {
					IMuhurSokme ms = new put_muhur_sokme(Integer.parseInt(editTesisatNo.getText().toString()),
							new SeriNo(editMuhurSeri.getText().toString(), editMuhurNo.getText().toString()), muhurDurum,
							iptalDurum);
					IIslemGrup islemGrup = new IslemGrup2();
					islemGrup.add(ms);
					islemGrup.setIslemTipi(IDef.MUHUR_SOKME_ISLEMI);
					IIslem islem = app.saveIslem(islemGrup);

					app.sendIslem(this, islem, new Callback() {
						@Override
						public Object run(Object obj) {
							if(app.checkException(MuhurSokmeActivity.this, obj)) return obj;
							app.ShowMessage(MuhurSokmeActivity.this, "Mühür söküldü", "");
							setResult(RESULT_OK);
							return null;
						}
					}, 10000);

				}
				catch (Exception e) {
					app.ShowException(MuhurSokmeActivity.this, e);
				}
				
			} catch (Exception e) {
				app.ShowException(this, e);
			}
		}
		else if(arg0.getId() == R.id.barkod){

			barcode.startScanner(this);

		}
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
}
