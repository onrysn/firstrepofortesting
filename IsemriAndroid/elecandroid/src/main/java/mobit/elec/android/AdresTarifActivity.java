package mobit.elec.android;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.mobit.Cbs;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;
import mobit.elec.Aciklama;
import mobit.elec.IAtarif;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;
import mobit.elec.mbs.put.put_atarif;

public class AdresTarifActivity extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app;
	
	EditText editTesisatNo;
	EditText editAdresTarif;
	Button buttonTamam;
	
	IIsemriIslem isemriIslem;
	IAtarif islem;
	
	IIslemGrup grup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adres_tarif);
		
		if (!(Globals.app instanceof IElecApplication)) {
			finish();
			return;
		}

		app = (IElecApplication) Globals.app;

		IIslem iislem = app.getActiveIslem();
		if (!(iislem instanceof IIslemGrup)) {
			finish();
			return;
		}

		grup = (IIslemGrup)iislem;
		List<IIslem> list;
		
		list = grup.getIslem(IIsemriIslem.class);
		if(list.size() == 1) isemriIslem = (IIsemriIslem)list.get(0);
		
		
		list = grup.getIslem(IAtarif.class);
		if(list.size() == 0){
			islem = new put_atarif();
			grup.add(islem);
			if(isemriIslem != null){
				islem.setTESISAT_NO(isemriIslem.getTESISAT_NO());
			}
		}
		else {
			islem = (IAtarif)list.get(0);
		}
		
		app.initForm(this);
		
		editTesisatNo = (EditText)findViewById(R.id.editTesisatNo);
		editAdresTarif = (EditText)findViewById(R.id.editAdresTarif);
		buttonTamam = (Button)findViewById(R.id.buttonTamam);
		
		buttonTamam.setOnClickListener(this);
		
		editTesisatNo.setText(Integer.toString(islem.getTESISAT_NO()));
		if(islem.getADRES_TARIF() != null && !islem.getADRES_TARIF().isEmpty()){
			try {
				editAdresTarif.setText(islem.getADRES_TARIF().getSTR());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(isemriIslem != null){
			editTesisatNo.setEnabled(false);
			editAdresTarif.requestFocus();
		}
		else {
			editTesisatNo.requestFocus();
		}
		
	}
	
	@Override
	public void onBackPressed() {

		super.onBackPressed();
		
		 if(islem != null) grup.remove(islem);

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
		
		try {
			
			islem.setTESISAT_NO(Integer.parseInt(editTesisatNo.getText().toString()));
			islem.setADRES_TARIF(new Aciklama(editAdresTarif.getText().toString()));
			islem.setCBS(new Cbs(app.getLocation()));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(islem.getTESISAT_NO() == 0) throw new MobitException("Tesisat numarasını girin!");
			if(islem.getADRES_TARIF() == null || islem.getADRES_TARIF().isEmpty()) 
				throw new MobitException("Adres tarif bilgisini girin!");
			
		
		}
		catch(Exception e){
			app.ShowException(this, e);
			return;
		}
		
		setResult(RESULT_OK);
		finish();
	}
}
