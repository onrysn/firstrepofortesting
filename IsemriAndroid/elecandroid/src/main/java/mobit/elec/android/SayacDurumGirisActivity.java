package mobit.elec.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISdurum;
import mobit.elec.enums.IslemTipi;

public class SayacDurumGirisActivity extends AppCompatActivity implements IForm {

	IElecApplication app = null;

	ListView listDurumlar;
	List<ISdurum> durumListe;
	ISdurum[] durumlar;
	
	IIsemriIslem islem = null;
	int islemTipi = 0;
	

	ArrayAdapterEx<ISdurum> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sayac_durum_giris);

		if(!(Globals.app instanceof IElecApplication)){
			finish();
			return;
		}
		
		app = (IElecApplication)Globals.app;
		
		IIslem islem = app.getActiveIslem();
		if (!(islem instanceof IIslemGrup)) {
			finish();
			return;
		}

		IIslemGrup grup = (IIslemGrup)islem;
		List<IIslem> list = grup.getIslem(IIsemriIslem.class);
		if(list.size() == 0){
			finish();
			return;
		}
		this.islem = (IIsemriIslem)list.get(0);
		
		app.initForm(this);

		listDurumlar = (ListView) findViewById(R.id.listDurumlar);

		islemTipi = getIntent().getIntExtra("islemTipi", 0);

		
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				load();
			}
		});
		
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
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		try {
				
			islem.SayacDurumKontrol();
			
		} catch (Exception e) {
			app.ShowException(this, e);
			return;
		}
		
		setResult(RESULT_OK, new Intent());
		super.onBackPressed();

	}

	private void load() {
		try {

			int KOD_TIPI = islem.getIsemri().getISLEM_TIPI().equals(IslemTipi.SayacOkuma) ? 1 : 0;
			durumListe = app.getSayacDurum(0, KOD_TIPI);
			loadIslem();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.ShowException(this, e);
			return;
		}
		
		arrayAdapter = new ArrayAdapterEx<ISdurum>(this, R.layout.row_with_check, durumListe, this, ViewHolder.class);
		listDurumlar.setAdapter(arrayAdapter);

		
	}

	// -------------------------------------------------------------------------

	public class ViewHolder<T> extends ViewHolderEx<T> {

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
			super(adapter, view);

			View v;

			v = view.findViewById(R.id.rowTextView);
			v.setOnClickListener(this);
			columnList.add(v);

			v = view.findViewById(R.id.rowCheckBox);
			v.setOnClickListener(this);
			columnList.add(v);
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.rowCheckBox) {
				// AboneDurumGirisActivity.this.

				CheckBox checkBox = (CheckBox) arg0;
				ISdurum durum = (ISdurum) checkBox.getTag();
				if (checkBox.isChecked()) {

					int cnt = 0;
					for (int i = 0; i < durumListe.size(); i++)
						if (durumListe.get(i).getCheck())
							cnt++;
					durum.setCheck(cnt < durumlar.length);
				} else {
					durum.setCheck(false);
				}
				checkBox.setChecked(durum.getCheck());
				
			}

		}

		@Override
		public void set(int position, T obj) {

			item = obj;

			ISdurum durum = (ISdurum) item;

			TextView tv = (TextView) columnList.get(0);
			tv.setText(durum.toString());
			tv.setTag(obj);

			CheckBox cb = (CheckBox) columnList.get(1);
			cb.setChecked(durum.getCheck());
			cb.setTag(obj);
		}
	}
	
	/*
	private class DurumViewHolder {

		private CheckBox checkBox;
		private TextView textView;

		public DurumViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public TextView getTextView() {
			return textView;
		}

	}

	private class DurumArrayAdapter extends ArrayAdapter<ISdurum> {

		private LayoutInflater inflater;
		private SayacDurumGirisActivity activity;

		public DurumArrayAdapter(Context context, List<ISdurum> list) {
			super(context, R.layout.row_with_check, R.id.rowTextView, list);
			inflater = LayoutInflater.from(context);
			activity = (SayacDurumGirisActivity)context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ISdurum durum = (ISdurum) getItem(position);

			CheckBox checkBox;
			TextView textView;

			if (convertView == null) {

				convertView = inflater.inflate(R.layout.row_with_check, null);

				textView = (TextView) convertView.findViewById(R.id.rowTextView);
				checkBox = (CheckBox) convertView.findViewById(R.id.rowCheckBox);

				convertView.setTag(new DurumViewHolder(textView, checkBox));

				final DurumArrayAdapter _this = this;

				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						
						CheckBox cb = (CheckBox) v;
						ISdurum durum = (ISdurum) cb.getTag();
						boolean checked = cb.isChecked();
						
						try {
						
							islem.setGELEN_DURUM_KODU(durum, checked ? durum : null);
							durum.setCheck(checked);
							
						}
						catch(Exception e){
							cb.setChecked(!checked);
							app.ShowException(SayacDurumGirisActivity.this, e);
							return;
							
						}
					}
				});

			} else {

				DurumViewHolder viewHolder = (DurumViewHolder) convertView.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			checkBox.setTag(durum);

			checkBox.setChecked(durum.getCheck());
			textView.setText(durum.toString());

			return convertView;
		}

	}*/
	
	void loadIslem() throws Exception {

		if (islem == null)
			return;

		durumlar = islem.getGELEN_DURUM_KODU();
		if (durumlar != null) {
			for (int i = 0; i < durumlar.length; i++) {
				if (durumlar[i].isEmpty())
					continue;
				for (int j = 0; j < durumListe.size(); j++) {
					if (durumlar[i].getSAYAC_DURUM_KODU() == durumListe.get(j).getSAYAC_DURUM_KODU()) {
						durumListe.get(j).setCheck(true);
						break;
					}
				}
			}
		}
		
	}

}
