package mobit.elec.mbs.medas.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.ILogin;
import com.mobit.Item;
import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.elec.IElecApplication;
import mobit.elec.android.IsemriMenuActivity;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Intent;

public class IsemriMenuActivity2 extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app = null;

	ListView list;
	Button cikis;
	ArrayAdapterEx<Item> arrayAdapter;

	SayacZimmetBilgi szb = new SayacZimmetBilgi();

	Date date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_isemri_menu2);

		if(!(Globals.app instanceof IElecApplication)){
			finish();
			return;
		}

		app = (IElecApplication) Globals.app;
		
		FormInitParam param = app.getFormInitParam();
		param.captionText = app.getEleman().getELEMAN_ADI();
		param.homeButton = false;
		app.initForm(this, param);
		
		list = (ListView) findViewById(R.id.list);
		cikis = (Button) findViewById(R.id.cikis);
		cikis.setOnClickListener(this);

		List<Item> arrayList = Arrays.asList(app.getMainMenuItems());

		date = app.getTime();
		
		arrayAdapter = new ArrayAdapterEx<Item>(this, R.layout.row_with_button, arrayList, ViewHolder.class);
		list.setAdapter(arrayAdapter);


	}


	@Override
	protected void onResume() {
		app.getMeterReader().setTriggerEnabled(false);
		app.setPortCallback(null);
		super.onResume();

	}
	@Override
	protected void onPause(){
		app.setPortCallback(null);
		super.onPause();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu2, menu);
		return true;
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
		else if(id == R.id.sifreDegistir){
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_SIFRE_DEGISTIR));
			startActivityForResult(intent, IDef.FORM_SIFRE_DEGISTIR);
		}
		//muhammed gökkaya
		//menu2.xml içinden kaldırıldılar !
		/*
		else if(id == R.id.zabitZimmet){

			try {
				szb.ZabitZimmetService(app, this, app.getEleman().getELEMAN_KODU(), new Callback() {

					public Object run(Object obj) {

						if(app.checkException(IsemriMenuActivity2.this, obj)) return null;
						if(obj instanceof List<?>){
							List<SayacZimmetBilgi.zabitBilgi> list = (List<SayacZimmetBilgi.zabitBilgi>)obj;
							StringBuilder sb = new StringBuilder();
							for(SayacZimmetBilgi.zabitBilgi zb : list) sb.append(String.format("%s\n", zb.toString()));
							app.ShowMessage(IsemriMenuActivity2.this, sb.toString(), "Zabıt Zimmet Listesi", DialogMode.Ok, 1, 0,null);
							return null;
						}
						app.ShowMessage(IsemriMenuActivity2.this, "Zabıt zimmeti alınamadı!", "", DialogMode.Ok, 1, 0, null);
						return null;
					}

				});
			}
			catch(Exception e){
				app.ShowException(this, e);
			}
		}/*
		/*
		else if(id == R.id.muhurZimmet){
			try {
				szb.MuhurZimmetService(app, this, app.getEleman().getELEMAN_KODU(), new Callback() {

					public Object run(Object obj) {

						if(app.checkException(IsemriMenuActivity2.this, obj)) return null;
						if(obj instanceof List<?>){
							List<SayacZimmetBilgi.muhurBilgi> list = (List<SayacZimmetBilgi.muhurBilgi>)obj;
							StringBuilder sb = new StringBuilder();
							for(SayacZimmetBilgi.muhurBilgi mb : list) sb.append(String.format("%s\n", mb.toString()));
							app.ShowMessage(IsemriMenuActivity2.this, sb.toString(), "Mühür Zimmet Listesi", DialogMode.Ok, 1, 0,null);
							return null;
						}
						app.ShowMessage(IsemriMenuActivity2.this, "Mühür zimmeti alınamadı!", "", DialogMode.Ok, 1, 0,null);
						return null;
					}

				});
			}
			catch(Exception e){
				app.ShowException(this, e);
			}
		}

		 */
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {



		AlertDialog.Builder dialog = new AlertDialog.Builder(IsemriMenuActivity2.this);
		dialog.setCancelable(false);
		dialog.setTitle("Emin Misiniz?");
		dialog.setMessage("Hesabınızdan Çıkıyorsunuz. Emin Misiniz?");
		dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				if(app instanceof ILogin){
					try {
						((ILogin)app).Logout();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						app.ShowException(IsemriMenuActivity2.this, e);
					}
				}

				Intent intent = new Intent(IsemriMenuActivity2.this, app.getFormClass(mobit.elec.android.IDef.FORM_LOGIN));
				startActivity(intent);
				finish();


			}
		});
		dialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		dialog.show();


	}
	
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		app.setActiveIsemri(null);
		app.setActiveIslem(null);
		app.setOptikResult(null);
		app.setPortCallback(null);
		app.getMeterReader().setTriggerEnabled(false);
	}

	public void itemSelected(Item item) {
		try {

			Class<?> cls = app.getFormClass(item.id);
			if (cls != null) {
				if (IForm.class.isAssignableFrom(cls)) {
					app.setPortCallback(null);
					app.getMeterReader().setTriggerEnabled(false);
					Intent intent = new Intent(this, cls);
					startActivityForResult(intent, item.id);
				} else if (Runnable.class.isAssignableFrom(cls)) {
					Constructor<?> c = cls.getConstructor(IForm.class);
					Runnable r = (Runnable) c.newInstance(this);
					r.run();
				}
			}
		} catch (Exception e) {
			app.ShowException(this, e);
		}
	}

	public class ViewHolder<T> extends ViewHolderEx<T> {

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);

			View v;

			v = view.findViewById(R.id.rowButton);
			v.setOnClickListener(this);
			columnList.add(v);

		}

		@Override
		public void set(int position, T obj) {

			super.set(position, obj);

			Item item = (Item) obj;
			Button btn;
			btn = (Button) columnList.get(0);
			// btn.setTextSize(utility.textSize);
			btn.setText(item.desc);
			btn.setTag(item);
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			Item item = (Item) arg0.getTag();
			IsemriMenuActivity2 activity = (IsemriMenuActivity2) adapter.getContext();
			activity.itemSelected(item);
			/*
			if (item.id==127){
				Yikik();
			}
			 */

		}

	}
	public void Yikik(){
		Intent intent = new Intent(this,YikikGirisActivity.class);
		startActivity(intent);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId() == R.id.cikis){
			onBackPressed();



		}
	}

}
