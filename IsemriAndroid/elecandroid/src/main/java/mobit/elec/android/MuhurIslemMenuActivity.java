package mobit.elec.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;

public class MuhurIslemMenuActivity extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app = null;
	
	Button muhurSokme;
	Button muhurTakma;
	Button muhurSorgu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_muhur_islem_menu);
	
		if(!(Globals.app instanceof IElecApplication)){
			finish();
			return;
		}
		
		app = (IElecApplication)Globals.app;
		
		IIslem islem = app.getActiveIslem();
		if(islem != null && !(islem instanceof IIsemriIslem)){
			finish();
			return;
		}
		
		app.initForm(this);
	
		muhurSokme = (Button)findViewById(R.id.muhurSokme);
		muhurTakma = (Button)findViewById(R.id.muhurTakma);
		muhurSorgu = (Button)findViewById(R.id.muhurSorgu);
		muhurSokme.setOnClickListener(this);
		muhurTakma.setOnClickListener(this);
		muhurSorgu.setOnClickListener(this);
		
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
		if(id == R.id.muhurSokme){
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_MUHUR_SOKME)); 
			startActivityForResult(intent, IDef.FORM_MUHUR_SOKME);
		}
		else if(id == R.id.muhurTakma){
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_MUHUR_TAKMA)); 
			startActivityForResult(intent, IDef.FORM_MUHUR_TAKMA);
		}
		else if(id == R.id.muhurSorgu){
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_MUHUR_SORGU));
			startActivityForResult(intent, IDef.FORM_MUHUR_SORGU);
		}
	}
}
