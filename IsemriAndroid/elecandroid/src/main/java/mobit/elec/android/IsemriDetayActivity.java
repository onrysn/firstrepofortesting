package mobit.elec.android;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.ICbs;
import com.mobit.IDetail;
import com.mobit.IForm;
import com.mobit.IIslemMaster;

import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.elec.ElecApplication;
import mobit.elec.IIsemri;

public class IsemriDetayActivity extends AppCompatActivity implements IForm, View.OnClickListener {
	ElecApplication app;
	IDetail detail;
	ListView list;
	Button HaritaGonder;
	public String CBS_X;
	public String CBS_Y;
	List<String[]> arrayList;
	ArrayAdapterEx<String[]> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_isemri_detay);
		HaritaGonder = (Button) findViewById(R.id.HaritaGonder);
		HaritaGonder.setOnClickListener(this);


		if(!(Globals.app instanceof ElecApplication)){
			finish();
			return;
		}
		app = (ElecApplication) Globals.app;
		IIsemri isemri = app.getActiveIsemri();
		if(!(isemri instanceof IDetail)){
			finish();
			return;
		}
		detail = (IDetail)isemri;
		
		FormInitParam param = app.getFormInitParam();
		if(param != null){
			param.captionText = detail.getAciklama();
		}
		app.initForm(this, param);
		
		list = (ListView) findViewById(R.id.list);
		
		arrayList = detail.getDetay();
		arrayAdapter = new ArrayAdapterEx<String[]>(this, R.layout.row_isemri_detay, arrayList, ViewHolder.class);
		list.setAdapter(arrayAdapter);
		
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
	public void onClick(View view) {
		//Onur tesisat sorgu ekranında sorgulanan tesisata yol tarifi almak için eklendi.
		int id = view.getId();
		if (id == R.id.HaritaGonder) {
				try {
					CBS_X = app.getActiveIsemri().getCBS().getX().toString();
					CBS_Y = app.getActiveIsemri().getCBS().getY().toString();
				} catch (Exception e) {

				}
			Uri gmmIntentUri = Uri.parse("google.navigation:q="+CBS_X+","+CBS_Y);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
		}
	}


	public class ViewHolder<T> extends ViewHolderEx<T> {

		
		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);
			
			View v;
			
			v = view.findViewById(R.id.textAlanAdi);
			v.setOnClickListener(this);
			columnList.add(v);
			
			v = view.findViewById(R.id.textAlanDeger);
			v.setOnClickListener(this);
			columnList.add(v);

		}

		@Override
		public void set(int position, T obj) {
			
			super.set(position, obj);
			
			String [] strs = (String [])item;
			TextView tv;
			tv = (TextView)columnList.get(0);
			//tv.setTextSize(mobit.elec.android.utility.textSize);
			tv.setText(strs.length > 0 && strs[0] != null ? strs[0] : "");
			tv.setTag(strs);
			
			tv = (TextView)columnList.get(1);
			//tv.setTextSize(mobit.elec.android.utility.textSize);
			tv.setText(strs.length > 1 && strs[1] != null ? strs[1] : "");
			tv.setTag(strs);

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		}

	}
}
