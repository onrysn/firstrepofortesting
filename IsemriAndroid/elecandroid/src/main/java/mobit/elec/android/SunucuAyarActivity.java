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
import com.mobit.IApplication;
import com.mobit.IForm;

import mobit.elec.IElecApplication;

public class SunucuAyarActivity extends AppCompatActivity implements IForm, OnClickListener {

	EditText editSunucuIp;
	EditText editSunucuPort;
	EditText editApn;
	Button buttonKaydet;

	IElecApplication app = (IElecApplication) Globals.app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sunucu_ayar);

		app.initForm(this);

		editSunucuIp = (EditText) findViewById(R.id.editSunucuIp);
		editSunucuPort = (EditText) findViewById(R.id.editSunucuPort);
		editApn = (EditText) findViewById(R.id.editApn);
		buttonKaydet = (Button) findViewById(R.id.kaydet);

		editSunucuIp.setText(app.getHost());
		editSunucuPort.setText(String.format("%d", app.getPort()));
		editApn.setText(app.getApn());

		buttonKaydet.setOnClickListener(this);
		editSunucuIp.setEnabled(false);
		editSunucuPort.setEnabled(false);
		editApn.setEnabled(false);
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
		if (id == R.id.kaydet) {
			try {
				app.setHost(editSunucuIp.getText().toString());
				app.setPort(Integer.parseInt(editSunucuPort.getText().toString()));
				app.setApn(editApn.getText().toString());
				app.saveSettings();
				app.restart();
				
				
			} catch (Exception e) {

				app.ShowException(this, e);
			}
		}
	}
}
