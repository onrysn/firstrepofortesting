package mobit.elec.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import com.mobit.Globals;
import com.mobit.IChecked;
import com.mobit.IForm;
import com.mobit.MobitException;
import mobit.elec.AltEmirTuru;
import mobit.elec.ElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIslemYetki;
import mobit.elec.IKarne;
import mobit.elec.IYetki;
import mobit.elec.IsemriFilterParam;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.MbsException;
import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.ArrayAdapterEx;
import mobit.android.CheckPopupList;
import mobit.android.LoadData;
import mobit.android.PopupList;
import mobit.android.ViewHolderEx;

public class IsemriIndirActivity extends AppCompatActivity implements IForm, OnClickListener, IPopupCallback {

	ElecApplication app;

	TextView textIslemTipi;
	TextView textAltEmirTuru;
	EditText KarneTesisatNo;
	ListView listSerbest;
	Button karneListe;

	List<IKarne> arraySerbest;
	ArrayAdapter<IKarne> adapterSerbest;

	CheckPopupList islemTipiPopup;
	CheckPopupList altEmirTuruPopup;

	List<IIslemTipi> islemTipList;
	CheckPopupList.CheckList islemTipleri;
	CheckPopupList.CheckList altEmirTurleri;

	IYetki yetki = null;
	List<IIslemYetki> islemYetki = null;
	
	AltEmirTuru tumuAltEmirTuru = new AltEmirTuru(IslemTipi.Tumu, -1, "Tümü");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_isemri_indir);

		if (!(Globals.app instanceof ElecApplication)) {
			finish();
			return;
		}
		app = (ElecApplication) Globals.app;
		app.initForm(this);

		textIslemTipi = (TextView) findViewById(R.id.textIslemTipi);
		textAltEmirTuru = (TextView) findViewById(R.id.textAltEmirTuru);
		KarneTesisatNo = (EditText) findViewById(R.id.editKarneTesisatNo);
		listSerbest = (ListView) findViewById(R.id.listSerbest);
		karneListe = (Button) findViewById(R.id.karneListe);

		((Button) findViewById(R.id.buttonKarneIndir)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonTesisatIndir)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonKarneBirak)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonSerbest)).setOnClickListener(this);
		karneListe.setOnClickListener(this);




		arraySerbest = new ArrayList<IKarne>();
		adapterSerbest = new ArrayAdapterEx<IKarne>(this, R.layout.row_karneler, arraySerbest,
				ViewHolder.class);
		listSerbest.setAdapter(adapterSerbest);

		// ---------------------------------------------------------------------

		if (app.getEleman().getYetki() instanceof IYetki) {
			yetki = (IYetki) app.getEleman().getYetki();
			islemYetki = yetki.getIslemYetki();
		}

		// ---------------------------------------------------------------------

		islemTipList = new ArrayList<IIslemTipi>(Arrays.asList((IIslemTipi[]) app.getEnumValues(IDef.ENUM_ISLEM_TIPI)));

		for (Iterator<IIslemTipi> iter = islemTipList.iterator(); iter.hasNext();) {

			IIslemTipi islemTipi = iter.next();
			if (islemTipi.equals(IslemTipi.Tumu))
				continue;

			boolean exists = false;
			if (islemYetki != null) {
				for (IIslemYetki yetki : islemYetki) {
					if (islemTipi.equals(yetki.getISLEM_TIPI())) {
						exists = true;
						break;
					}
				}
			}
			try {
				if (!exists)
					iter.remove();
			} catch (Exception e) {
				return;
			}
		}
		
		// ---------------------------------------------------------------------
		
		islemTipleri = new CheckPopupList.CheckList((List<IChecked>)(List<?>)islemTipList);
		CheckPopupList.CheckList.setCheck(islemTipleri, IslemTipi.Tumu, true);
		islemTipiPopup = new CheckPopupList(textIslemTipi, 350, islemTipleri, this);
		textIslemTipi.setText(islemTipleri.toString());
		textIslemTipi.setTag(islemTipleri);
		
		// ---------------------------------------------------------------------

		List<AltEmirTuru> altEmirTuruList = new ArrayList<AltEmirTuru>();
		tumuAltEmirTuru.setCheck(true);
		altEmirTuruList.add(tumuAltEmirTuru);
		altEmirTurleri = new CheckPopupList.CheckList((List<IChecked>)(List<?>)altEmirTuruList);
		altEmirTuruPopup = new CheckPopupList(textAltEmirTuru, 450, altEmirTurleri, this);
		textAltEmirTuru.setText(altEmirTurleri.toString());
		textAltEmirTuru.setTag(altEmirTurleri);
		
		// ---------------------------------------------------------------------
		KarneTesisatNo.requestFocus();

		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(KarneTesisatNo, InputMethodManager.SHOW_IMPLICIT);

		karneListe.performClick();
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

	class LoadData2 extends LoadData {

		public int karne_no = 0;
		public int tesisat_no = 0;

		public LoadData2(Activity activity, String message) {
			super(activity, message);
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			List<IIsemri2> list;
			try {

				// İşlemTipi ve alt emir türüne göre filitrelenmesi
				//IIslemTipi islemTipleri = (IIslemTipi)textIslemTipi.getTag();
				//AltEmirTuru altEmirTurleri = (AltEmirTuru) textAltEmirTuru.getTag();
				if(islemTipleri.getCheckCount() == 0) throw new MobitException("İş emri tipini seçin");
				if(altEmirTurleri.getCheckCount() == 0) throw new MobitException("Alt emir türünü seçin");
				
				IsemriFilterParam filter = new IsemriFilterParam();
				filter.islemTipleri = (List<IChecked>)islemTipleri.getList();
				filter.altEmirTurleri = (List<IChecked>)altEmirTurleri.getList();
				filter.tesisat_no = tesisat_no;
				
				if (tesisat_no > 0)
					list = (List<IIsemri2>) (List<? extends IIsemri>) app.fetchTesisatIsemri(tesisat_no, filter);
				else
					list = (List<IIsemri2>) (List<? extends IIsemri>) app.fetchKarneIsemri(karne_no, filter);
				
				setMessage("İş emirleri kaydediliyor...");
				app.Save(list);
				app.ShowMessage(IsemriIndirActivity.this, String.format("Toplam %d iş emri alındı", list.size()), "");

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						karneListe.performClick();
					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(IsemriIndirActivity.this, e);
			}

			return null;
		}

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
				LoadData2 load = new LoadData2(this, "İş emri karne alınıyor...");
				load.karne_no = karne_no;
				load.execute();
				return;
			} else if (id == R.id.buttonTesisatIndir) {

				if (KarneTesisatNo.getText().toString().length() == 0)
					throw new MobitException("Tesisat no girin");

				int tesisat_no = Integer.parseInt(KarneTesisatNo.getText().toString());
				LoadData2 load = new LoadData2(this, "İş emri tesisat alınıyor...");
				load.tesisat_no = tesisat_no;
				load.execute();
				return;
			} 
			else if (id == R.id.buttonKarneBirak){
				
				if (KarneTesisatNo.getText().toString().length() == 0)
					throw new MobitException("Karne no girin");
				int karne_no = Integer.parseInt(KarneTesisatNo.getText().toString());
				app.ismriKarneBirak(karne_no);
				app.ShowMessage(this, "Karne bırakıldı", "");
				
			}
			else if (id == R.id.buttonSerbest) {

				arraySerbest.clear();
				adapterSerbest.notifyDataSetChanged();
				MbsException ex = null;

				for (IslemTipi tip : IslemTipi.values()) {
					List<IKarne> list = null;
					try {

						list = app.fetchSerbestIsemri(tip, 0, null);
					} catch (MbsException e) {
						// Listelenecek kayıt bulunamadı hatası gösterilmemesi
						if (!(e.getErrType().equals(mobit.elec.mbs.IDef.ERR) && e.getErrCode() == 432))
							throw e;
						ex = e;
					}
					if (list != null) {
						arraySerbest.addAll(list);
					}

				}
				if (arraySerbest.size() > 0) {
					adapterSerbest.notifyDataSetChanged();
				} else {
					if (ex != null)
						throw ex;
				}

			}
			else if(id == R.id.karneListe){
				arraySerbest.clear();
				adapterSerbest.notifyDataSetChanged();
				List<IKarne> list = app.getIsemriKarneListesi();
				arraySerbest.addAll(list);
				if (arraySerbest.size() > 0)
					adapterSerbest.notifyDataSetChanged();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
		}
	}

	public class ViewHolder<T> extends ViewHolderEx<T> {

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

		}

		@Override
		public void set(int position, T obj) {

			super.set(position, obj);

			IKarne karne = (IKarne) item;

			TextView tv;
			tv = (TextView) columnList.get(0);
			tv.setText(Integer.toString(karne.getKARNE_NO()));
			tv.setTag(karne);

			tv = (TextView) columnList.get(1);
			tv.setText(karne.getISLEM_TIPI().toString());
			tv.setTag(karne);

			tv = (TextView) columnList.get(2);
			tv.setText(Integer.toString(karne.getADET()));
			tv.setTag(karne);

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
		}

	}

	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub
		int id = anchorView.getId();
		if (id == R.id.textIslemTipi) {

			textIslemTipi.setText(obj.toString());
			textIslemTipi.setTag(obj);
			textAltEmirTuru.setText("");
			textAltEmirTuru.setTag(null);
			altEmirTurleri.getList().clear();
			
			CheckPopupList.CheckList checkList = (CheckPopupList.CheckList)obj;
			List<AltEmirTuru> list = new ArrayList<AltEmirTuru>();
			tumuAltEmirTuru.setCheck(true);
			list.add(tumuAltEmirTuru);
			
			for(IChecked islemTipi : (List<IChecked>)checkList.getList()){
				if(!islemTipi.getCheck()) continue;
				list.addAll(Arrays.asList(app.getAltEmirTuru((IIslemTipi) islemTipi)));
			}
			
			for (Iterator<AltEmirTuru> iter = list.iterator(); iter.hasNext();) {
				AltEmirTuru tur = iter.next();
				if(tur.altEmirTuru < 0) continue; // Tümü
				boolean exists = false;
				if (islemYetki != null) {
					for (IIslemYetki yetki : islemYetki) {
						if (tur.islemTipi.equals(yetki.getISLEM_TIPI())
								&& tur.altEmirTuru == yetki.getALT_EMIR_TURU()) {
							exists = true;
							break;
						}
					}
				}
				if (!exists)
					iter.remove();
			}

			List<IChecked> c = (List<IChecked>)altEmirTurleri.getList();
			c.addAll(list);
			textAltEmirTuru.setText(altEmirTurleri.toString());
			textAltEmirTuru.setTag(altEmirTurleri);
			

		} else if (id == R.id.textAltEmirTuru) {
			textAltEmirTuru.setText(obj.toString());
			textAltEmirTuru.setTag(obj);
		}

	}
}
