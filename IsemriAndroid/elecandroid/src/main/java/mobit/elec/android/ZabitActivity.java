package mobit.elec.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriZabit;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IMbsApplication;

public class ZabitActivity extends AppCompatActivity implements IForm, OnClickListener {

	IMbsApplication app = null;
	IIsemriIslem islem;
	IIsemriZabit zabit;
	EditText editZabitSeri;
	EditText editZabitNo;
	EditText editKullanici2Kodu;
	
	int mode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zabit);

		if(!(Globals.app instanceof IMbsApplication)){
			finish();
			return;
		}
		app = (IMbsApplication)Globals.app;
		if(!(app.getActiveIslem() instanceof IIslemGrup)){ 
			finish();
			return;
		}
		
		islem = (IIsemriIslem)app.getActiveIslem();
		if(!islem.getIsemri().getISLEM_TIPI().equals(IslemTipi.Tespit)){
			finish();
			return;
		}
		
		app.initForm(this);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) mode = bundle.getInt("MODE");
		
		editZabitSeri = (EditText) findViewById(R.id.editZabitSeri);
		editZabitNo = (EditText) findViewById(R.id.editZabitNo);
		editKullanici2Kodu = (EditText) findViewById(R.id.editKullanici2Kodu);
		
		
		((Button)findViewById(R.id.buttonKaydet)).setOnClickListener(this);
	
		//zabit = islem.getZabit();
		if(zabit != null){
			editZabitSeri.setText(zabit.getZABIT_SERI());
			editZabitNo.setText(Integer.toString(zabit.getZABIT_NO()));
			editKullanici2Kodu.setText(Integer.toString(zabit.getOKUYUCU2_KODU()));
		}
	
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
			try {
				
				if(editZabitSeri.getText().toString().isEmpty()) throw new MobitException("Zabıt seri girin!");
				if(editZabitNo.getText().toString().isEmpty()) throw new MobitException("Zabıt no girin!");
				if(editKullanici2Kodu.getText().toString().isEmpty()) throw new MobitException("İkinci kullanıcı kodu girin!");
				
				//if(zabit == null) zabit = islem.newZabit();
				zabit.setZABIT_SERI(editZabitSeri.getText().toString());
				zabit.setZABIT_NO(Integer.parseInt(editZabitNo.getText().toString()));
				zabit.setOKUYUCU2_KODU(Integer.parseInt(editKullanici2Kodu.getText().toString()));
				zabit.setOKUYUCU_KODU(app.getEleman().getELEMAN_KODU());
				//islem.setZabit(zabit);
				
				/*
				IIsemri2 isemri = (IIsemri2)app.getActiveIsemri();
				put_isemri_zabit z = new put_isemri_zabit(isemri.getTESISAT_NO(), isemri.getSAHA_ISEMRI_NO(),
						editZabitSeri.getText().toString(), Integer.parseInt(editZabitNo.getText().toString()),
						Integer.parseInt(editKullanici2Kodu.getText().toString()));
				app.sendIsemriZabit(z);
				app.ShowMessage(this, MsgInfo.ZABIT_ISLENDI);
				*/
				
				finish();
				
			} catch (Exception e) {
				app.ShowException(this, e);
				return;
			}
		}

	}
}
