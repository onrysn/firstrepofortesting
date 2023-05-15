package mobit.elec.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import mobit.eemr.IReadResult;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayaclar;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;

public class EndeksActivity extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app;
	boolean readOnly = false;

	TesisatBilgiFragment bilgi;
	IndexFragment index;
	Button buttonTamam;
	IIsemriIslem isemriIslem;
	ISayaclar sayaclar;
	IReadResult result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_endeks);

		if (!(Globals.app instanceof IElecApplication)) {
			// Desteklenmiyor
			finish();
			return;
		}
		app = (IElecApplication) Globals.app;
		IIslem islem = app.getActiveIslem();
		if (!(islem instanceof IIsemriIslem)) {
			// Desteklenmiyor
			finish();
			return;
		}

		app.initForm(this);

		isemriIslem = (IIsemriIslem) islem;
		IIslemTipi islemTipi = isemriIslem.getIsemri().getISLEM_TIPI();
		if (islemTipi.equals(IslemTipi.SayacDegistir) || islemTipi.equals(IslemTipi.SayacTakma)) {
			setTitle("Yeni Takılan Sayaç Endeks Girişi");
			//sayaclar = isemriIslem.getTakilanSayaclar();
			result = sayaclar.getSayac(SayacKodu.Aktif).getOptikResult();
		} else {
			setTitle("Endeks Girişi");
			sayaclar = isemriIslem.getIsemri().getSAYACLAR();
			result = sayaclar.getSayac(SayacKodu.Aktif).getOptikResult();
		}

		FragmentManager fm = getSupportFragmentManager();

		bilgi = (TesisatBilgiFragment) fm.findFragmentById(R.id.tesisatBilgiFragment);
		bilgi.show(app.getActiveIsemri());
		bilgi.getView().setVisibility(View.GONE);

		index = (IndexFragment) fm.findFragmentById(R.id.indexFragment);
		index.show(isemriIslem, sayaclar, result);
		
		buttonTamam = (Button)findViewById(R.id.buttonTamam);
		buttonTamam.setOnClickListener(this);

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
		if(arg0.getId() == R.id.buttonTamam){
			try {
				index.save();
				setResult(RESULT_OK);
				finish();
			}
			catch(Exception e){
				app.ShowException(this, e);
			}
		}
	}
}
