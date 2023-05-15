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
import android.widget.TextView;
import com.mobit.Globals;
import com.mobit.IForm;
import mobit.android.ArrayAdapterEx;
import mobit.android.LoadData;
import mobit.android.ViewHolderEx;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.elec.ElecApplication;
import mobit.elec.ITesisat;

public class TesisatSorguActivity extends AppCompatActivity implements IForm, OnClickListener, ICallback {

	ElecApplication app; 
	
	EditText editNo;
	Button tesisatSorgu;
	Button sayacSorgu;
	Button optikOku;
	ListView listTesisatlar;
	
	List<ITesisat> arrayList;
	ArrayAdapter<ITesisat> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tesisat_sorgu);
		
		if(!(Globals.app instanceof ElecApplication)){
			finish();
			return;
		}
		app = (ElecApplication)Globals.app; 
		
		app.initForm(this);
	
		editNo = (EditText)findViewById(R.id.editNo);
		tesisatSorgu = (Button)findViewById(R.id.tesisatSorgu);
		sayacSorgu = (Button)findViewById(R.id.sayacSorgu);
		listTesisatlar = (ListView)findViewById(R.id.listTesisatlar);
		optikOku = (Button)findViewById(R.id.optikOku);
		
		tesisatSorgu.setOnClickListener(this);
		sayacSorgu.setOnClickListener(this);
		optikOku.setOnClickListener(this);
		
		app.getPlatform().showSoftKeyboard(this, true);
	
		arrayList = new ArrayList<ITesisat>();
		arrayAdapter = new ArrayAdapterEx<ITesisat>(this, R.layout.row_tesisat_adres, arrayList, ViewHolder.class); 
		listTesisatlar.setAdapter(arrayAdapter);
		
		
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
		if(id == R.id.tesisatSorgu){
			if(editNo.getText().toString().isEmpty()){
				app.ShowMessage(this, "Tesisat numarasını girin!", "Uyarı");
				return;
			}
			int tno = Integer.parseInt(editNo.getText().toString());
			tesisatSorgu(tno);
		}
		else if(id == R.id.sayacSorgu){
			if(editNo.getText().toString().isEmpty()){
				app.ShowMessage(this, "Sayaç numarasını girin!", "Uyarı");
				return;
			}
			int sno = Integer.parseInt(editNo.getText().toString());
			sayacSorgu(sno);
		}
		else if(id == R.id.optikOku){
			try {
				app.getMeterReader().Reconnect();
				app.getMeterReader().Trigger();
			} catch (Exception e) {
				app.ShowException(this, e);
			}
		}
	}
	
	void goster(ITesisat tesisat)
	{
		app.setActiveIsemri(tesisat);
		Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
		startActivity(intent); 
		
	}
	
	void tesisatSorgu(final int tno)
	{
		
		new LoadData(this, "Tesisat sorgulanıyor...", new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<ITesisat> list;
				try {
					list = app.fetchTesisat(tno);
					list.get(0).getANET_ISLETME_KODU();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					app.ShowException(TesisatSorguActivity.this, e);
					return;
				}
				
				if(list == null || list.size() == 0){
					app.ShowMessage(TesisatSorguActivity.this, "Tesisat bulunamadı!", "Uyarı");
					return;
				}
				
				goster(list.get(0));
				
			}
			
		}).execute();
		
		
	}
	
	void sayacSorgu(final int sno)
	{
	
		arrayList.clear();
		arrayAdapter.notifyDataSetChanged();
		
		new LoadData(this, "Sayaç sorgulanıyor...", ""){

			@Override
			protected Void doInBackground(Void... arg0) {

				try {
					
					arrayList.addAll(app.fetchTesisat2(sno));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					app.ShowException(TesisatSorguActivity.this, e);
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				
				try {
					
					arrayAdapter.notifyDataSetChanged();
					
					if(arrayList.size() == 0){
						app.ShowMessage(TesisatSorguActivity.this, "Sayaç bulunamadı!", "Uyarı");
						return;
					}
					
					if(arrayList.size() > 1){
						app.ShowMessage(TesisatSorguActivity.this, 
								"Aynı sayaç no kullanılan birden fazla tesisat var. Listeden seçim yapın", "Uyarı");
					}
					else {
						tesisatSorgu(arrayList.get(0).getTESISAT_NO());
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					app.ShowException(TesisatSorguActivity.this, e);
				}
				
			};
			
		}.execute();
		
		
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
	public void Read(IProbeEvent event, IReadResult result) {
		// TODO Auto-generated method stub
		int _sayac_no = Integer.parseInt(result.get_sayac_no());
		final int sayac_no = _sayac_no;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				editNo.setText(Integer.toString(sayac_no));
				sayacSorgu(sayac_no);
			}

		});
	}

	@Override
	public void PowerEvent(IProbeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ConnectionResetEvent(IProbeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public class ViewHolder<T> extends ViewHolderEx<T> {

		ElecApplication app = (ElecApplication)TesisatSorguActivity.this.app;
		
		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);
			
			View v;
			
			v = view.findViewById(R.id.tesisatNo);
			v.setOnClickListener(this);
			columnList.add(v);
			
			v = view.findViewById(R.id.tesisatAdres);
			v.setOnClickListener(this);
			columnList.add(v);

		}

		@Override
		public void set(int position, T obj) {
			
			super.set(position, obj);
			
			ITesisat tesisat = (ITesisat) item;
			
			TextView tv;
			tv = (TextView)columnList.get(0);
			tv.setText(Integer.toString(tesisat.getTESISAT_NO()));
			tv.setTag(tesisat);
			tv = (TextView)columnList.get(1);
			tv.setText(tesisat.getADRES());
			tv.setTag(tesisat);

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			ITesisat tesisat = (ITesisat)item;
				
			app.setActiveIsemri(tesisat);
			Intent intent = new Intent(adapter.getContext(), app.getFormClass(IDef.FORM_ISEMRI_DETAY));
			adapter.getContext().startActivity(intent);	
			
		}

	}
	
}
