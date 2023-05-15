package mobit.elec.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.mobit.IForm;
import mobit.elec.ElecApplication;

public class OptikPortAyarActivity extends AppCompatActivity implements IForm, OnClickListener {

	EditText editMac;
	Button buttonArama;
	Button buttonKaydet;
	
	ElecApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_optik_port_ayar);
		
		if(!(com.mobit.Globals.app instanceof ElecApplication)){
			finish();
			return;
		}
		
		app = (ElecApplication)com.mobit.Globals.app;
		app.initForm(this);
		
		editMac = (EditText)findViewById(R.id.editMac);
		buttonArama = (Button)findViewById(R.id.buttonArama);
		buttonKaydet = (Button)findViewById(R.id.buttonKaydet);
		
		buttonArama.setOnClickListener(this);
		buttonKaydet.setOnClickListener(this);
		
		editMac.setText(app.getPortMac());
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
		if(id == R.id.buttonArama){
			Intent intent = new Intent(this, BluetoothDiscoverActivity.class);
			intent.putExtra(BluetoothDiscoverActivity.CAPTION, "Optik Port Arama");
			startActivityForResult(intent, 0);
		}
		else if(id == R.id.buttonKaydet){
			
			try {
				app.setPortMac(editMac.getText().toString());
				app.saveSettings();
				app.restart();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				app.ShowException(this, e);
			}
			
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    
	    if(resultCode == Activity.RESULT_OK){
	    	editMac.setText(intent.getStringExtra(BluetoothDiscoverActivity.DEVICE));
	    }
	   
	}
}
