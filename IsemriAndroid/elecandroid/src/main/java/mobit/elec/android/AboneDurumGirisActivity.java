package mobit.elec.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.mobit.Globals;

import mobit.eemr.Lun_Control;
import mobit.elec.Aciklama;
import mobit.elec.IAciklama;
import mobit.elec.IAdurum;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.enums.IslemTipi;
import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.ArrayAdapterEx;
import mobit.android.PopupList;
import mobit.android.ViewHolderEx;
import android.widget.Button;
import com.mobit.ICamera;
import com.mobit.IForm;
import com.mobit.IFotograf;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;

public class AboneDurumGirisActivity extends AppCompatActivity
		implements IForm, OnClickListener, ICamera.ICallback, IPopupCallback {

	IElecApplication app = null;

	ListView listDurumlar;
	List<IAdurum> durumListe;
	List<IAdurum> durumListe2;
	List<IAdurum> durumListe3;
	IAdurum[] durumlar;
	IIsemri isemri;
	TextView listAciklamalar;
	EditText editAciklama;
	Button buttonFoto;
	Button buttonTamam;
	IIsemriIslem islem = null;
	List<String> aciklamaListe = new ArrayList<String>();
	ArrayAdapterEx<IAdurum> arrayAdapter;
	PopupList<String> aciklamaPopup;
	int islemTipi = 0;
	public  static  int isno;
	Boolean check;

	public String endeks_data="0";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abone_durum_giris);

		if (!(Globals.app instanceof IElecApplication)) {
			finish();
			return;
		}

		app = (IElecApplication) Globals.app;

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
		try {
			Intent intent = getIntent();
			endeks_data = intent.getStringExtra("endeks");
			if (endeks_data==null){
				endeks_data="0";
			}
		}
		catch (Exception e){
			endeks_data="0";
		}
		listDurumlar = (ListView) findViewById(R.id.listDurumlar);
		listAciklamalar = (TextView) findViewById(R.id.listAciklamalar);
		editAciklama = (EditText) findViewById(R.id.editAciklama);
		buttonFoto = (Button) findViewById(R.id.buttonResimCekme);
		buttonTamam = (Button) findViewById(R.id.buttonTamam);

		buttonFoto.setOnClickListener(this);
		buttonTamam.setOnClickListener(this);

		islemTipi = getIntent().getIntExtra("islemTipi", 0);

		if (!(islem instanceof IFotograf)) {
			buttonFoto.setEnabled(false);
		}

		aciklamaPopup = new PopupList<String>(listAciklamalar, 350, aciklamaListe, this);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				load();
			}
		});

	}

	protected void onDestroy() {
		if (aciklamaPopup != null)
			aciklamaPopup.close();
		super.onDestroy();
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
	public void onBackPressed() {

		super.onBackPressed();

	}

	private void load() {
		try {

			int KOD_TIPI = islem.getIsemri().getISLEM_TIPI().equals(IslemTipi.SayacOkuma) ? 1 : 0;
			durumListe = app.getAboneDurum(0, KOD_TIPI);
			durumListe2 = app.getAboneDurum(0, KOD_TIPI);
			durumListe3 = app.getAboneDurum(0, KOD_TIPI);
			loadIslem();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.ShowException(this, e);
			return;
		}
		//muhammed gökkaya
		durumListe2.clear();
		durumListe3.clear();
		for (int i=0;i<durumListe.size();i++){
			if(durumListe.get(i).getISLEM_KODU()==1){
					durumListe2.add(durumListe.get(i));
			}
			else {
				durumListe3.add(durumListe.get(i));
			}
		}
		durumListe3.add(durumListe.get(1));
		durumListe3.add(durumListe.get(2));
		durumListe3.add(durumListe.get(14));
		durumListe3.remove(0);
		durumListe3.remove(3);
		durumListe2.remove(7);
		durumListe2.remove(6);
		/*
		for (int rem=0;rem<durumListe.size();rem++)
		{
			durumListe2.remove(rem);
			durumListe3.remove(rem);
		}

		 */
		Lun_Control Ln=new Lun_Control();
		if (endeks_data.equals("1")){
			durumListe=durumListe;
			buttonFoto.setEnabled(true);
		}
		else if (Ln.DurumControl==1 )
		{
			durumListe=durumListe3;
		}
		else {
			buttonFoto.setEnabled(true);
			durumListe=durumListe2;
			durumListe.remove(0);//endesk doğru
			durumListe.remove(0);//sayacoptik
		}

		Ln.setDurumControl(0);

		arrayAdapter = new ArrayAdapterEx<IAdurum>(this, R.layout.row_with_check, durumListe, this, ViewHolder.class);

		listDurumlar.setAdapter(arrayAdapter);


	}

	void aciklamaListeDoldur() throws Exception {
		aciklamaListe.clear();
		listAciklamalar.setText("");
		for (IAdurum durum : durumListe) {
			if (!durum.getCheck())
				continue;
			aciklamaListe.addAll(app.getAboneDurumAciklama(islem, durum));
		}

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
				IAdurum durum = (IAdurum) checkBox.getTag();
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
				try {
					
					aciklamaListeDoldur();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					app.ShowException(AboneDurumGirisActivity.this, e);
				}
			}

		}

		@Override
		public void set(int position, T obj) {

			item = obj;

			IAdurum durum = (IAdurum) item;

			TextView tv = (TextView) columnList.get(0);
			tv.setText(durum.toString());
			tv.setTag(obj);

			CheckBox cb = (CheckBox) columnList.get(1);
			cb.setChecked(durum.getCheck());
			cb.setTag(obj);
		}
	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.buttonResimCekme) {
			try {

				//app.getCamera().show(this, (IFotograf) islem);
				//İlla fotoları görsün derse bunu aktf et
				//Intent fac=new Intent(this,FotoActivity.class);
				//startActivity(fac);
				//muhammed gökkaya

				Lun_Control isNo=new Lun_Control();
				isno=app.getActiveIsemri().getSAHA_ISEMRI_NO();
				isNo.setIsEmriNo(isno);
				isNo.setTesisatNo(app.getActiveIsemri().getTESISAT_NO());
				//foto yolu
				String path =Globals.platform.getExternalStorageDirectory();
				File f = new File(path);
				File file[] = f.listFiles();
				int counter=0;
				//foto kontrolu
				for (int i=0; i < file.length; i++)
				{
					if (file[i].getName().equals("medas")){

					}
					else {
						if (isno==Integer.parseInt(file[i].getName().split("_")[0]) ){
							counter++;
						}
					}
				}
				//eğer öncesinde çekilmiş ise
				if (counter>0){
					AlertDialog.Builder dialog = new AlertDialog.Builder(AboneDurumGirisActivity.this);
					dialog.setCancelable(false);
					dialog.setTitle("Fotoğraf Çekme");
					dialog.setMessage("Bu İşemri için "+ counter +" adet Fotoğraf Çekilmiş. Yeniden çekmek ister misin?" );
					dialog.setPositiveButton("Sil/Yeniden Çek", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							//Action for "Delete".
							String path =Globals.platform.getExternalStorageDirectory();

							File f = new File(path);
							File file[] = f.listFiles();
							int foto_control=0;
							for (int i=0; i < file.length; i++)
							{
								if (file[i].getName().equals("medas")){

								}
								else {
									if (isno==Integer.parseInt(file[i].getName().split("_")[0]) ){
										file[i].delete();
										foto_control=1;
									}

								}
							}
							if (foto_control==1){
								FotografCek();
							}
						}
					}).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Action for "Cancel".
						}
					});
					final AlertDialog alert = dialog.create();
					alert.show();
				}
				else {
					FotografCek();
				}
			} catch (Exception e) {
				app.ShowException(this, e);
			}


		} else if (arg0.getId() == R.id.buttonTamam ) {

			String path =Globals.platform.getExternalStorageDirectory();
			File f = new File(path);
			File file[] = f.listFiles();
			int optik_kontrol=0;
			int counter=0;
			check = false;
			isno=app.getActiveIsemri().getSAHA_ISEMRI_NO();

			//foto kontrolu
			for (int i=0; i < file.length; i++)
			{
				if (file[i].getName().equals("medas")){

				}
				else {
					if (isno==Integer.parseInt(file[i].getName().split("_")[0]) ){
						counter++;
					}
				}
			}
			//HÜSEYİN EMRE ÇEVİK OPTİKLE OKUDUKTAN SONRA FOTO İSTEMEME

			for(int k = 0; k < durumListe.size(); k++) {
				try {
					if (durumListe.get(k).getCheck() && durumListe.get(k).getABONE_DURUM_KODU() == 29)
					{	optik_kontrol++;
					break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//Onur Abone Durum kodunda fotoğraf kısıtını esnetmek için eklendi
			for(IAdurum durum : durumlar) {
				try {
					islem.setGELEN_DURUM(durum, null);
					for(int i = 0; i < durumListe.size(); i++)
						if(durumListe.get(i).getCheck()) islem.setGELEN_DURUM(durumListe.get(i), durumListe.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			durumlar = islem.getGELEN_DURUM();
			if (durumlar != null) {
				for (int i = 0; i < durumlar.length; i++) {
					try {
						if (durumlar[i].getABONE_DURUM_KODU()== 6) {
							check = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (app.getActiveIsemri().getCARPAN() == 1 && check == false && app.getEleman().getYETKI().contains("O")){
				counter++;
			}
			if (counter>0 || buttonFoto.isEnabled()==false  || (optik_kontrol>0 && islem.getOPTIK_DATA()!=null) )
			{
				try {
					for(IAdurum durum : durumlar) islem.setGELEN_DURUM(durum, null);
					for(int i = 0; i < durumListe.size(); i++)
						if(durumListe.get(i).getCheck()) islem.setGELEN_DURUM(durumListe.get(i), durumListe.get(i));
					islem.setOKUMA_ACIKLAMA(new Aciklama(editAciklama.getText().toString()));
					setResult(RESULT_OK);
					finish();

				} catch (Exception e) {
					app.ShowException(this, e);
				}
			}
			else {
				AlertDialog.Builder dialog2 = new AlertDialog.Builder(AboneDurumGirisActivity.this);
				dialog2.setCancelable(false);
				dialog2.setTitle("Dikkat!");
				dialog2.setMessage("Bu İşemri için "+ counter +" adet Fotoğraf Çekilmiş. Lütfen önce fotoğraf çekiniz..." );
				dialog2.setPositiveButton("", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

					}
				}).setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Action for "Cancel".
					}
				});
				final AlertDialog alert2 = dialog2.create();
				alert2.show();
			}

		}
	}
	void  FotografCek(){
		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);
		FotografCekme fc = new FotografCekme(this, null);
		fc.run();
	}

	@Override
	public File getFileName() {
		// TODO Auto-generated method stub

		String file = String.format(Globals.getLocale(), "%010d_%s.jpg", app.getActiveIsemri().getTESISAT_NO(),
				ICamera.fmt.format(app.getTime()));
		return new File(app.getAppPicturePath(), file);
	}

	void loadIslem() throws Exception {

		if (islem == null)
			return;

		durumlar = islem.getGELEN_DURUM();
		if (durumlar != null) {
			for (int i = 0; i < durumlar.length; i++) {
				if (durumlar[i].isEmpty())
					continue;
				for (int j = 0; j < durumListe.size(); j++) {
					if (durumlar[i].getABONE_DURUM_KODU() == durumListe.get(j).getABONE_DURUM_KODU()) {
						durumListe.get(j).setCheck(true);
						break;
					}
				}
			}
		}
		IAciklama aciklama = islem.getOKUMA_ACIKLAMA();
		if (aciklama != null && !aciklama.isEmpty())
			editAciklama.setText(aciklama.getSTR());

	}

	@Override
	public void OnItemSelect(View anchorView, Object obj) {
		// TODO Auto-generated method stub

		String s = editAciklama.getText().toString();
		listAciklamalar.setText(obj.toString());
		if (s.length() == 0) {
			editAciklama.setText(obj.toString());
			return;
		}
		editAciklama.setText(String.format("%s\n%s", s, obj));
	}
}
