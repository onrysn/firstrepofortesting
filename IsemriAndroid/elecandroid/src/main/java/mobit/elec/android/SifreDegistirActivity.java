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
import com.mobit.ILogin;
import com.mobit.MobitException;

import mobit.elec.IElecApplication;

public class SifreDegistirActivity extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app;
	ILogin login;

	EditText editKullaniciKodu;
	EditText editSifre;
	EditText editYeniSifre;
	Button buttonKaydet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (!(Globals.app instanceof ILogin)) {
			finish();
			return;
		}
		app = (IElecApplication) Globals.app;
		login = (ILogin) Globals.app;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sifre_degistir);

		app.initForm(this);
		
		editKullaniciKodu = (EditText) findViewById(R.id.editKullaniciKodu);
		editSifre = (EditText) findViewById(R.id.editSifre);
		editYeniSifre = (EditText) findViewById(R.id.editYeniSifre);
		buttonKaydet = (Button) findViewById(R.id.buttonKaydet);

		buttonKaydet.setOnClickListener(this);
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		try {
			if (id == R.id.buttonKaydet) {
				if(editKullaniciKodu.getText().toString().isEmpty()) throw new MobitException("Kullanıcı kodunu girin!");
				if(editSifre.getText().toString().isEmpty()) throw new MobitException("Şifreyi girin!");
				if(editYeniSifre.getText().toString().isEmpty()) throw new MobitException("Yeni Şifreyi girin!");
				int kullaniciKodu = Integer.parseInt(editKullaniciKodu.getText().toString());
				int sifre = Integer.parseInt(editSifre.getText().toString());
				int yeniSifre = Integer.parseInt(editYeniSifre.getText().toString());
				login.ChangePassword(kullaniciKodu, sifre, yeniSifre);
				app.ShowMessage(this, "Şifre değiştirildi", "");
			}
		} catch (Exception e) {
			app.ShowException(this, e);
		}

	}
}
