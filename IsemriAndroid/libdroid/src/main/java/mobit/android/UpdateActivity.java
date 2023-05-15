package mobit.android;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDialogCallback;
import com.mobit.IForm;

public class UpdateActivity extends AppCompatActivity implements IForm, View.OnClickListener {

	private TextView mesaj;
	private Button guncelle;
	private URL appUrl;
	private URI updUri;

	private IApplication app;
	private Thread thread = null;
	private boolean restart = true;
	private StrictMode.VmPolicy vmPolicy = null;
	private boolean downloaded;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);

		if (!(Globals.app instanceof IApplication)) {
			finish();
			return;
		}

		app = (IApplication) Globals.app;
		app.initForm(this);

		mesaj = (TextView) findViewById(R.id.mesaj);
		guncelle = (Button) findViewById(R.id.guncelle);
		guncelle.setOnClickListener(this);

		Intent intent = getIntent();

		try {
			appUrl = new URL(intent.getStringExtra("appUrl"));
			updUri = new URI(intent.getStringExtra("updUri"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
			return;
		}

		mesaj.setText("Yeni güncelleme var. Güncellemek istiyor musunuz?");

		//beginUpdate(); // geçici
		//if(true) return;

		restart = true;
		/*
		Globals.platform.ShowMessage(this, "Yeni güncelleme var. Kurmak istiyor musunuz?", "Güncelleme",
				DialogMode.YesNo, 1, new IDialogCallback() {

					@Override
					public void DialogResponse(int msg_id, DialogResult result) {
						// TODO Auto-generated method stub

						if (result.equals(DialogResult.Yes)) {
							restart = true;
							beginDownlaod();

						} else {
							restart = false;
							finish();
						}
					}
				});
		*/
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(restart) startActivityForResult(getIntent(), 1);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		restart = true;
		/*
		 * if (resultCode == Activity.RESULT_OK) { mesaj.setText(
		 * "Güncelleme başarılı bir şekilde tamamlandı."); return; } else if
		 * (resultCode == Activity.RESULT_CANCELED) { //mesaj.setText(
		 * "Güncelleme tamamlanamadı!"); return; }
		 */

	}

	@Override
	public void onDestroy() {

		if(vmPolicy != null) StrictMode.setVmPolicy(vmPolicy);
		terminateThread();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		restart = false;
		super.onBackPressed();

	}

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.guncelle){
			//beginUpdate();
			beginDownlaod();
		}
	}

	private void terminateThread() {
		if (thread != null) {
			if (thread.isAlive()) {
				thread.interrupt();
				try {
					thread.join(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				thread = null;
			}
		}
	}

	void beginDownlaod() {

		// Daha önceden indirilmiş ise doğrudan güncellemeye geçilmesi
		if(downloaded){
			beginUpdate();
			return;
		}
		terminateThread();

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Activity activity = UpdateActivity.this;

				// -------------------------------------------------------------

				if (activity != null && !activity.isDestroyed()) {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							guncelle.setEnabled(false);
							mesaj.setText("Program indirilmeye başlandı. Lütfen bekleyin...");
						}

					});
				}

				// -------------------------------------------------------------

				Exception ex = null;
				try {

					com.mobit.utility.downloadFile(appUrl, new File(updUri.getPath()));


				} catch (Exception e) {
					// TODO Auto-generated catch block
					ex = e;
				}

				// -------------------------------------------------------------

				if (activity != null && !activity.isDestroyed()) {
					final Object result = ex;
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							guncelle.setEnabled(true);
							if (Globals.app.checkException(UpdateActivity.this, (Throwable) result)) {
								mesaj.setText("Program indirme başarısız oldu.");
								return;
							}
							mesaj.setText("Program indirme tamamlandı.");
							downloaded = true;
							beginUpdate();
						}

					});
				}

				// -------------------------------------------------------------

			}

		});

		thread.start();

		/*
		 * AsyncTask<Object, Object, Object> task = new AsyncTask<Object,
		 * Object, Object>() {
		 * 
		 * @Override protected void onPreExecute() { super.onPreExecute();
		 * mesaj.setText("Program indirilmeye başlandı. Lütfen bekleyin..."); }
		 * 
		 * @Override protected Object doInBackground(Object... arg0) { // TODO
		 * Auto-generated method stub try {
		 * 
		 * mobit.utility.downloadFile(appUrl, new File(updUri.getPath()));
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block return e;
		 * } return null; }
		 * 
		 * @Override protected void onPostExecute(Object result) {
		 * super.onPostExecute(result); if
		 * (Globals.app.checkException(UpdateActivity.this, (Throwable) result))
		 * { mesaj.setText("Program indirme başarısız oldu."); return; }
		 * mesaj.setText("Program indirme tamamlandı."); beginUpdate(); }
		 * 
		 * }.execute();
		 */
	}

	private static final String AUTHORITY = "mobit.android";

	void beginUpdate() {


		Intent install = null;
		restart = false;


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 24

			/*
			try{
				Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
				m.invoke(null);
			}catch(Exception e){
				e.printStackTrace();
			}*/

			if (vmPolicy == null) {
				vmPolicy = StrictMode.getVmPolicy();
				StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
				StrictMode.setVmPolicy(builder.build());
			}
			// <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
			install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
			//File file = new File(updUri);
			//Uri uri = FileProvider.getUriForFile(this, AUTHORITY, file);
			install.setData(Uri.parse(updUri.toString()));
			install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			//install.setDataAndType(Uri.parse(updUri.toString()), "application/vnd.android.package-archive");
			//install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
			//install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
			//install.putExtra(Intent.EXTRA_ALLOW_REPLACE, true);
			//install.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, getApplicationInfo().packageName);

			startActivityForResult(install, 1);

		} else {

			install = new Intent(Intent.ACTION_VIEW);
			install.setDataAndType(Uri.parse(updUri.toString()), "application/vnd.android.package-archive");
			install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivityForResult(install, 1);

		}

	}



}