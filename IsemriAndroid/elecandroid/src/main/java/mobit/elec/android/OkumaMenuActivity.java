package mobit.elec.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.mobit.Globals;
import com.mobit.IForm;
import mobit.elec.mbs.IMbsApplication;

public class OkumaMenuActivity extends AppCompatActivity implements IForm, OnClickListener {

	IMbsApplication app = (IMbsApplication)Globals.app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_okuma_menu);

		app.initForm(this);
		
		/*
		((Button)findViewById(R.id.IsemriListe)).setOnClickListener(this);
		((Button)findViewById(R.id.IsemriIndir)).setOnClickListener(this);
		((Button)findViewById(R.id.tesisatBilgi)).setOnClickListener(this);
		((Button)findViewById(R.id.muhurSokme)).setOnClickListener(this);
		((Button)findViewById(R.id.muhurTakma)).setOnClickListener(this);
		*/
	
		setTitle(String.format("Okuma Menü - %s",app.getEleman().getELEMAN_ADI()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.okuma_menu, menu);
		return true;
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
	public void onBackPressed()
	{ 
		Intent intent = new Intent(this, app.getFormClass(IDef.FORM_LOGIN));
		startActivity(intent);
		finish();   
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
