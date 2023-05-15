package mobit.elec.mbs.medas.android;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.MobitException;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.android.IndexFragment;
import mobit.elec.android.TesisatBilgiFragment;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;

public class EndeksActivity2 extends AppCompatActivity implements IForm, OnClickListener {

	IElecApplication app;
	boolean readOnly = false;

	TesisatBilgiFragment bilgi;
	IndexFragment index;
	Button endeksGetir;
	Button buttonTamam;
	IIsemriIslem isemriIslem;
	ISayaclar sayaclar;
	
	SayacZimmetBilgi szb = new SayacZimmetBilgi();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_endeks2);

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

		isemriIslem = (IIsemriIslem) islem;
		IIslemTipi islemTipi = isemriIslem.getIsemri().getISLEM_TIPI();
		if (!islemTipi.equals(IslemTipi.SayacDegistir) && !islemTipi.equals(IslemTipi.SayacTakma)) {
			finish();
			return;
		}
			
		setTitle("Yeni Tak?lan Saya? Endeks Giri?i");
		//sayaclar = isemriIslem.getTakilanSayaclar();

		app.initForm(this);
		FragmentManager fm = getSupportFragmentManager();

		bilgi = (TesisatBilgiFragment) fm.findFragmentById(R.id.tesisatBilgiFragment);
		bilgi.show(app.getActiveIsemri());
		bilgi.getView().setVisibility(View.GONE);

		index = (IndexFragment) fm.findFragmentById(R.id.indexFragment);
		index.show(isemriIslem, sayaclar, null);

		endeksGetir = (Button) findViewById(R.id.endeksGetir);
		endeksGetir.setOnClickListener(this);

		buttonTamam = (Button) findViewById(R.id.buttonTamam);
		buttonTamam.setOnClickListener(this);
		
		EndeksGetir();

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
			if (arg0.getId() == R.id.buttonTamam) {

				index.save();
				setResult(RESULT_OK);
				finish();

			} else if (arg0.getId() == R.id.endeksGetir) {

				EndeksGetir();

			}
		} catch (Exception e) {
			app.ShowException(this, e);
		}
	}

	private void EndeksGetir() {
		
		try {
			final ISayacBilgi sayac = null;// = isemriIslem.getTakilanSayaclar().getSayac(SayacKodu.Aktif);
			szb.SayacSenkService(app.getEleman().getELEMAN_KODU(), sayac.getSAYAC_NO(),
					sayac.getMARKA().getSAYAC_MARKA_KODU(), new Callback() {

						/*
						 * szb.SayacEndeksServisi(Integer.toString(isemriIslem.
						 * getTESISAT_NO()),
						 * Integer.toString(isemriIslem.getTakilanSayaclar().
						 * getSayac(SayacKodu.Aktif).getSAYAC_NO()), new
						 * Callback(){
						 */

						@Override
						public Object run(Object obj) {
							// TODO Auto-generated method stub
							if (app.checkException(EndeksActivity2.this, obj))
								return null;
							try {
								if (obj == null)
									throw new MobitException("Sunucudan endeks bilgisi al?namad?");
								
								

							} catch (Exception e) {
								app.ShowException(EndeksActivity2.this, e);
							}
							return null;
						}

					});
		} catch (Exception e) {
			app.ShowException(this, e);
		}
	}
}
