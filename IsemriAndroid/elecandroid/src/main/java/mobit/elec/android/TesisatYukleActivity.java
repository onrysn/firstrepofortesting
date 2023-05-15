package mobit.elec.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.ILocation;
import com.mobit.IMap;
import com.mobit.IMapMarker;
import com.mobit.MobitException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import mobit.android.ArrayAdapterEx;
import mobit.android.LoadData;
import mobit.android.ViewHolderEx;
import mobit.elec.ElecApplication;
import mobit.elec.IKarne;
import mobit.elec.ITesisat;
import mobit.elec.medas.ws.SayacZimmetBilgi;


public class TesisatYukleActivity extends AppCompatActivity implements IForm, OnClickListener {

	private IMap map = null;
	private   List<ITesisat> listHarita;

	ElecApplication app;
	SayacZimmetBilgi medasServer;
	EditText KarneTesisatNo;
	ListView listKarneler;
	Button karneListe;
	Button haritaokuma;
	List<SayacZimmetBilgi.GidilmeyenKarneListe> list;

	List<IKarne> arrayKarneler;
	ArrayAdapter<IKarne> adapterKarneler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tesisat_yukle);

		if(!(Globals.app instanceof ElecApplication)){
			finish();
			return;
		}
		app = (ElecApplication)Globals.app;
		app.initForm(this);

		if (app instanceof IMap)
			map = (IMap) app;

		medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
		KarneTesisatNo = (EditText) findViewById(R.id.editKarneTesisatNo);
		listKarneler = (ListView) findViewById(R.id.listKarneler);
		karneListe = (Button) findViewById(R.id.karneListe);
		haritaokuma= (Button) findViewById(R.id.haritaokuma);

		((Button) findViewById(R.id.buttonKarneIndir)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonTesisatIndir)).setOnClickListener(this);
		((Button) findViewById(R.id.ikincigidilmeyen)).setOnClickListener(this);
		karneListe.setOnClickListener(this);
		haritaokuma.setOnClickListener(this);

		listHarita=new ArrayList<ITesisat>();

		arrayKarneler = new ArrayList<IKarne>();
		adapterKarneler = new ArrayAdapterEx<IKarne>(this, R.layout.row_karneler, arrayKarneler,
				ViewHolder.class);
		listKarneler.setAdapter(adapterKarneler);

		KarneTesisatNo.requestFocus();

		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(KarneTesisatNo, InputMethodManager.SHOW_IMPLICIT);

		karneListe.performClick();
	}

	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);
		super.onResume();
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		try {
			if (id == R.id.buttonKarneIndir) {
				if (KarneTesisatNo.getText().toString().length() == 0)
					throw new MobitException("Karne no girin");

				int karne_no = Integer.parseInt(KarneTesisatNo.getText().toString());
				LoadData2 load = new LoadData2(this, "Karne alınıyor...");
				load.karne_no = karne_no;
				load.execute();
				return;
			} else if (id == R.id.buttonTesisatIndir) {

				if (KarneTesisatNo.getText().toString().length() == 0)
					throw new MobitException("Tesisat no girin");

				int tesisat_no = Integer.parseInt(KarneTesisatNo.getText().toString());
				LoadData2 load = new LoadData2(this, "Tesisat alınıyor...");
				load.tesisat_no = tesisat_no;
				load.execute();
				return;
			}
			else if(id == R.id.karneListe){
				arrayKarneler.clear();
				adapterKarneler.notifyDataSetChanged();
				try {
					List<IKarne> list = app.getOkumaKarneListesi();
					arrayKarneler.addAll(list);
					if (arrayKarneler.size() > 0)
						adapterKarneler.notifyDataSetChanged();
				}
				catch (Exception e){
					app.ShowException(this, e);
				}
			}
			else if (id == R.id.ikincigidilmeyen){
				if (KarneTesisatNo.getText().toString().length() == 0)
					throw new MobitException("Karne no girin");
				int karne_no = Integer.parseInt(KarneTesisatNo.getText().toString());

				//GidilmeyenKarneler(karne_no);
				IkinciGidilmeyen ikinciGidilmeyen = new IkinciGidilmeyen(karne_no);
				ikinciGidilmeyen.execute();

				return;
			}
			else if (id == R.id.haritaokuma){
				List<ITesisat> list = new ArrayList<ITesisat>();
			//	list=app.getKarneIsemri(Integer.parseInt(KarneTesisatNo.getText().toString()));


					Harita(listHarita);





			}
		} catch (Exception e) {
			app.ShowException(this, e);
			return;
		}

	}
	public void YukleTesisat(int tesisat_no){
		LoadData3 load = new LoadData3(this, "Tesisat alınıyor...");
		load.tesisat_no = tesisat_no;
		load.execute();
	}
	class IkinciGidilmeyen extends AsyncTask<Void, Void, Void> {



		int _karne_no;

		private ProgressDialog progressDialog = new ProgressDialog(TesisatYukleActivity.this);

		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Sorgulanıyor..."); // Setting Message
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
			progressDialog.show(); // Display Progress Dialog
			progressDialog.setCancelable(false);

		}

		public IkinciGidilmeyen(int karne_no) {
			super();
			_karne_no=karne_no;
		}



		@Override
		protected Void doInBackground(Void... voids) {

			try  {

				list = medasServer.GidilmeyenKarne(_karne_no);



			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					try {
						//beklemesi gerekli çünkü alternatif hata dönüyor
						YukleTesisat(list.get(i).getTESISAT_NO());
						Thread.sleep(100);
					}catch (Exception ee){
						ee.printStackTrace();
					}
				}
				progressDialog.dismiss();
				AlertDialog alertDialog = new AlertDialog.Builder(TesisatYukleActivity.this)
						.setTitle("2.Gidilmeyen Sonuc")
						.setMessage(list.size() + " adet tesisat alındı.")
						.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//set what would happen when positive button is clicked
							}
						})
						.show();
			}
			else {
				progressDialog.dismiss();
				AlertDialog alertDialog = new AlertDialog.Builder(TesisatYukleActivity.this)
						.setTitle("2.Gidilmeyen Sonuc")
						.setMessage("0 adet tesisat alındı.")
						.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//set what would happen when positive button is clicked
							}
						})
						.show();
			}
		}
	}

	class LoadData2 extends LoadData {

		public int karne_no = 0;
		public int tesisat_no = 0;
		
		public LoadData2(Activity activity, String message) {
			super(activity, message);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			List<ITesisat> list;
			try {
				
				if(tesisat_no > 0)
					list = app.fetchTesisat(tesisat_no);
				else
					list = app.fetchKarneTesisat(karne_no, null);

				listHarita=list;
				setMessage("Tesisatlar kaydediliyor...");
				
				app.Save(list);
				app.ShowMessage(TesisatYukleActivity.this, String.format("%d adet tesisat alındı", list.size()), "");

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						karneListe.performClick();
					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(TesisatYukleActivity.this, e);
			}
			
			return null;
		}
		
	}
	class LoadData3 extends LoadData {

		public int karne_no = 0;
		public int tesisat_no = 0;

		public LoadData3(Activity activity, String message) {
			super(activity, message);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			List<ITesisat> list;
			try {

				if(tesisat_no > 0)
					list = app.fetchTesisat(tesisat_no);
				else
					list = app.fetchKarneTesisat(karne_no, null);

				//setMessage("Tesisatlar kaydediliyor...");
				if (list.get(0)!=null){

					listHarita.add(list.get(0));
				}

				app.Save(list);
				//app.ShowMessage(TesisatYukleActivity.this, String.format("%d adet tesisat alındı", list.size()), "");

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						karneListe.performClick();
					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(TesisatYukleActivity.this, e);
			}

			return null;
		}

	}
	//Hüseyin Emre Çevik 27.04.2021 Harita Eklendi

	private void Harita(List<ITesisat> arrayList) {
		if (map == null)
			return;

		List<IMapMarker> list = new ArrayList<IMapMarker>();
		synchronized (arrayList) {
			for (ITesisat isemri : arrayList) {
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


	public class ViewHolder<T> extends ViewHolderEx<T> {

		private ImageView img;

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);

			View v;

			v = view.findViewById(R.id.karneNo);
			v.setOnClickListener(this);
			columnList.add(v);

			v = view.findViewById(R.id.islemTipi);
			v.setOnClickListener(this);
			columnList.add(v);

			v = view.findViewById(R.id.adet);
			v.setOnClickListener(this);
			columnList.add(v);

			//Onur Karne silmek için eklendi
			img = view.findViewById(R.id.sil);
			img.setOnClickListener(this);
			columnList.add(img);

		}

		@Override
		public void set(int position, T obj) {

			super.set(position, obj);
			img.setImageResource(R.drawable.carpi);

			IKarne karne = (IKarne) item;

			TextView tv;
			ImageView im;
			tv = (TextView) columnList.get(0);
			tv.setText(Integer.toString(karne.getKARNE_NO()));
			tv.setTag(karne);

			tv = (TextView) columnList.get(1);
			tv.setText(karne.getISLEM_TIPI().toString());
			tv.setTag(karne);

			tv = (TextView) columnList.get(2);
			tv.setText(Integer.toString(karne.getADET()));
			tv.setTag(karne);

			//Onur Karne silmek için eklendi
			im = (ImageView) columnList.get(3);
			im.setTag(karne);

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			int id = arg0.getId();
			/*
			 * if (id == columnList.get(1).getId()) { IKarne karne; for (T i :
			 * adapter.getListe()) { karne = (IKarne) i; if (karne.getCheck()) {
			 * karne.setCheck(false); break; } } karne = (IKarne) arg0.getTag();
			 * karne.setCheck(true); adapter.notifyDataSetChanged(); }
			 */
			if (id == columnList.get(0).getId()){

				IKarne karne = (IKarne) arg0.getTag();
				try {
					List<ITesisat> listHaritaSecim=app.fetchKarneTesisat(karne.getKARNE_NO(),null);
					Harita(listHaritaSecim);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (id == columnList.get(3).getId()){
				//Onur Karne silmek için eklendi 4. sütunun click event'i
				IKarne karne = (IKarne) arg0.getTag();
				int karneNo = karne.getKARNE_NO();
				try {
					app.DeleteKarne(karneNo);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							karneListe.performClick();
						}
					});


				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}

	}
}
