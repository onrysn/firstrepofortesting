package mobit.elec.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.Globals;
import com.mobit.ICallbackEx;
import com.mobit.IEnum;
import com.mobit.IForm;
import mobit.android.ArrayAdapterEx;
import mobit.android.LoadData;
import mobit.android.PopupList;
import mobit.android.ViewHolderEx;
import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IMbsApplication;
import mobit.elec.mbs.IslemMaster2;
import mobit.elec.mbs.enums.CevapTipi;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.put.put_isemri_cevap_sil;

public class TespitActivity extends AppCompatActivity implements IForm, IPopupCallback, OnClickListener, ICallbackEx {

	IMbsApplication app;
	IIsemri2 isemri;
	IIsemriIslem islem;

	ListView listSorular;
	TextView textCoktanSecmeli;
	RadioGroup radioEvetHayir;
	EditText editVeri;
	Button buttonCevapla;
	Button buttonSil;
	Button buttonZabit;
	
	IIsemriSoru soru;
	IEnum cevapTipi;

	InputFilter[] fInteger = new InputFilter[] { new InputFilter.LengthFilter(10) };
	InputFilter[] fFloat = new InputFilter[] { new InputFilter.LengthFilter(20) };
	InputFilter[] fString = new InputFilter[] { new InputFilter.LengthFilter(field.s_CEVAP) };

	List<IIsemriSoru> arraySorular = null;
	ArrayAdapterEx<IIsemriSoru> adapterSorular;

	PopupList<String> popuplist = null;
	String[] listCoktanSecmeli = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tespit);


		if (!(Globals.app instanceof IMbsApplication)) {
			finish();
			return;
		}
		app = (IMbsApplication) Globals.app;
		if(!(app.getActiveIslem() instanceof IIsemriIslem)){ 
			finish();
			return;
		}
		
		islem = (IIsemriIslem)app.getActiveIslem();
		if(!islem.getIsemri().getISLEM_TIPI().equals(IslemTipi.Tespit)){
			finish();
			return;
		}
		
		if(!(app.getActiveIsemri() instanceof IIsemri2)){
			finish();
			return;
		}
		isemri = (IIsemri2) app.getActiveIsemri();
		
		app.initForm(this);

		listSorular = (ListView) findViewById(R.id.listSorular);
		textCoktanSecmeli = (TextView) findViewById(R.id.textCoktanSecmeli);
		radioEvetHayir = (RadioGroup) findViewById(R.id.radioEvetHayir);
		editVeri = (EditText) findViewById(R.id.editVeri);
		buttonCevapla = (Button) findViewById(R.id.buttonCevapla);
		buttonSil = (Button) findViewById(R.id.buttonSil);
		buttonZabit = (Button) findViewById(R.id.buttonZabit);
		
		buttonCevapla.setOnClickListener(this);
		buttonSil.setOnClickListener(this);
		buttonZabit.setOnClickListener(this);
		

		textCoktanSecmeli.setVisibility(View.GONE);
		radioEvetHayir.setVisibility(View.GONE);
		editVeri.setVisibility(View.GONE);
		
		
		try {
			arraySorular = isemri_soru.Select(app.getConnection(), islem.getTESISAT_NO(), islem.getSAHA_ISEMRI_NO());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
			return;
		}
		adapterSorular = new ArrayAdapterEx<IIsemriSoru>(this, R.layout.row_with_check, arraySorular, ViewHolder.class);
		listSorular.setAdapter(adapterSorular);

		soru_iste();
	}

	@Override
	protected void onDestroy() {
		if (popuplist != null)
			popuplist.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);
		super.onResume();
	}

	@Override
	public void onBackPressed() {

		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	void reset() {
		textCoktanSecmeli.setVisibility(View.GONE);
		radioEvetHayir.setVisibility(View.GONE);
		editVeri.setVisibility(View.GONE);

	}

	void secim(isemri_soru soru) {

		this.soru = soru;
		reset();

		cevapTipi = soru.getCEVAP_TIPI();

		if (cevapTipi.equals(CevapTipi.CoktanSecmeli)) {
			textCoktanSecmeli.setText("");
			textCoktanSecmeli.setVisibility(View.VISIBLE);
			if (popuplist != null) {
				popuplist.close();
				popuplist = null;
			}
			listCoktanSecmeli = soru.getCEVAP_FORMAT_LIST();
			popuplist = new PopupList<String>(textCoktanSecmeli, 350, listCoktanSecmeli, this);

		} else if (cevapTipi.equals(CevapTipi.Boolean)) {
			radioEvetHayir.clearCheck();
			radioEvetHayir.setVisibility(View.VISIBLE);
		} else {
			editVeri.setText("");
			editVeri.setVisibility(View.VISIBLE);
			if (cevapTipi.equals(CevapTipi.Float)) {
				editVeri.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				editVeri.setFilters(fFloat);
				editVeri.requestFocus();
			} else if (cevapTipi.equals(CevapTipi.Integer)) {
				editVeri.setInputType(InputType.TYPE_CLASS_NUMBER);
				editVeri.setFilters(fInteger);
				editVeri.requestFocus();
			} else if (cevapTipi.equals(CevapTipi.Strings)) {
				editVeri.setInputType(InputType.TYPE_CLASS_TEXT);
				editVeri.setFilters(fString);

			}
		}
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
		if (id == R.id.buttonCevapla) {
			if (soru.getCheck()) {
				app.ShowMessage(this, "Bu soru cevaplanmış!", "");
				return;
			}
			String cevap = "";
			if (cevapTipi.equals(CevapTipi.CoktanSecmeli)) {
				String s = textCoktanSecmeli.getText().toString();
				int i = 0;
				for (i = 0; i < listCoktanSecmeli.length; i++) {
					if (listCoktanSecmeli[i].compareTo(s) == 0) {
						break;
					}
				}
				if (i == listCoktanSecmeli.length) {
					app.ShowMessage(this, "Cevap seçin", "");
					return;
				}
				cevap = Integer.toString(i);

			} else if (cevapTipi.equals(CevapTipi.Boolean)) {
				cevap = (radioEvetHayir.getCheckedRadioButtonId() == R.id.radioEvet) ? "1" : "0";
				if (cevap.length() == 0) {
					app.ShowMessage(this, "Evet/Hayır seçin", "");
					return;
				}
			} else if (cevapTipi.equals(CevapTipi.Float)
					|| cevapTipi.equals(CevapTipi.Integer) | cevapTipi.equals(CevapTipi.Strings)) {
				cevap = editVeri.getText().toString();
				if (cevap.length() == 0) {
					app.ShowMessage(this, "Cevap girin", "");
					return;
				}
			}

			soru.setCEVAP(cevap);

			cevapla();

		} else if (id == R.id.buttonSil) {

			sil();
			
		} else if (id == R.id.buttonZabit) {
				
			zabit(0);
		}
		

	}

	void zabit(int mode)
	{
		Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
		if(clazz != null){
			Intent intent = new Intent(this, clazz);
			intent.putExtra("MODE1", mode);
			startActivityForResult(intent, 1);
		}
	}
	
	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub
		textCoktanSecmeli.setText(obj.toString());
		
	}

	void soru_iste() {

		new LoadData(this, "Soru isteniyor...", soru_iste).execute();

	}

	void cevapla() {
		new LoadData(this, "Cevap gönderiliyor...", cevapla).execute();
	}

	void sil() {

		new LoadData(this, "Cevaplar siliniyor...", sil).execute();
	}

	Runnable soru_iste = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				final List<IIsemriSoru> list = app.getIsemriSoru(islem.getTESISAT_NO(), islem.getSAHA_ISEMRI_NO());

				TespitActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (list.size() == 0) {

							/*
							 * app.ShowMessage(TespitActivity.this,
							 * "Tespit tamamlandı", ""); Intent intent = new
							 * Intent(TespitActivity.this,
							 * app.getFormClass(IDef.FORM_ISEMRI_ENDEKS));
							 * startActivity(intent);
							 */
							setResult(RESULT_OK);
							finish();
							return;
						}
						arraySorular.addAll(list);
						adapterSorular.notifyDataSetChanged();
						int cnt = adapterSorular.getCount();
						if (cnt > 0)
							listSorular.setSelection(cnt - 1);
					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(TespitActivity.this, e);
			}
		}
	};

	Runnable cevapla = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				IslemMaster2 master = new IslemMaster2(soru);
				app.sendIslem(TespitActivity.this, master, new Callback() {
					@Override
					public Object run(Object obj) {
						if(app.checkException(TespitActivity.this, obj)) return obj;
						adapterSorular.notifyDataSetChanged();
						soru.setCheck(true);
						reset();
						soru_iste();
						return null;
					}
				}, 10000);


			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(TespitActivity.this, e);
				return;
			}

		}

	};

	Runnable sil = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				put_isemri_cevap_sil cs = new put_isemri_cevap_sil(islem.getTESISAT_NO(), islem.getSAHA_ISEMRI_NO());
				IslemMaster2 master = new IslemMaster2(cs);
				app.sendIslem(TespitActivity.this, master, new Callback() {
					@Override
					public Object run(Object obj) {
						if(app.checkException(TespitActivity.this, obj)) return obj;
						try {
							isemri_soru.Delete(app.getConnection(), islem.getTESISAT_NO(), islem.getSAHA_ISEMRI_NO());
						}
						catch (Exception e){
							app.ShowException(TespitActivity.this, e);
							return e;
						}
						arraySorular.clear();
						adapterSorular.notifyDataSetChanged();
						soru_iste();
						return null;
					}
				}, 10000);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(TespitActivity.this, e);
				return;
			}
		}

	};

	public class ViewHolder<T> extends ViewHolderEx<T> {

		private ICallbackEx clb = null;
		private CheckBox checkBox;
		private TextView textView;

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
			super(adapter, view);
			clb = (ICallbackEx) adapter.getContext();
			checkBox = (CheckBox) view.findViewById(R.id.rowCheckBox);
			textView = (TextView) view.findViewById(R.id.rowTextView);
			checkBox.setOnClickListener(this);
			textView.setOnClickListener(this);
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.rowTextView || arg0.getId() == R.id.rowCheckBox) {

				if (arg0.getId() == R.id.rowCheckBox)
					checkBox.setChecked(!checkBox.isChecked());

				isemri_soru s = (isemri_soru) arg0.getTag();
				if (s != null) {
					if (s.getCheck()) {
						Globals.app.ShowMessage((IForm) adapter.getContext(), "Bu soru cevaplanmış", "");
						return;
					}
					clb.call(s);
				}
			}

		}

		@Override
		public void set(int position, T obj) {

			super.set(position, obj);
			if (obj != null) {
				textView.setText(obj.toString());
				textView.setTag(obj);
				checkBox.setTag(obj);
				isemri_soru s = (isemri_soru) obj;
				checkBox.setChecked(s.getCheck());
			}

		}

	}

	@Override
	public Object call(Object arg, Object... args) {
		// TODO Auto-generated method stub

		secim((isemri_soru) arg);
		return null;
	}
}
