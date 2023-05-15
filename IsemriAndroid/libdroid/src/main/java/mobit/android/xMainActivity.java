package mobit.android;

import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import com.mobit.Globals;
import com.mobit.ICallback;
import com.mobit.IPlatform;
import com.mobit.MobitException;
import com.mobit.ModLoader;
import com.mobit.utility;
import mobit.android.AndroidPlatform;
import mobit.android.R;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import com.mobit.IForm;

import static android.os.SystemClock.sleep;


public class xMainActivity extends AppCompatActivity implements IForm, ICallback, LoaderCallbacks<Object> {

	IPlatform platform = null;
	ListView lv;
	List<String> msgList;
	ArrayAdapter<String> arrayAdapter;
	List<String> moduleList;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		xMyApplication application = (xMyApplication)getApplication();

		application.setMainActivity(this);

		super.onCreate(savedInstanceState);

		setTheme(R.style.AppTheme_NoActionBar);

		LinearLayout ll = new LinearLayout(this, null, R.style.AppTheme_NoActionBar);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setOrientation(LinearLayout.VERTICAL);

		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.medas5);
		lv = new ListView(this);
		//Onur UI'ı iyileştirmek için ListView kapatıldı modüller yine aynı şekil arka planda çalışmaya devam ediyor.
		//ll.addView(lv);
		ll.addView(imageView);
		setContentView(ll);


		AndroidPlatform.initForm((AppCompatActivity)this);
		
		moduleList = findModuleClasses();

		msgList = new ArrayList<String>();
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgList);
		lv.setAdapter(arrayAdapter);

		getLoaderManager().initLoader(1, null, this);

	}
	
	public void ShowException(Exception e) {

		AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setTitle("Hata");
		dlg.setMessage(utility.FormatException(e, false, true));
		dlg.show();
	}

	@Override
	public void onBackPressed() {
		/*
		 * moveTaskToBack(true); finishAffinity();
		 */
		if (platform != null)
			platform.close();
		
		super.onBackPressed();
	}

	@Override
	public void addMessage(String msg) {

		runOnUiThread(new Runnable() {

			String msg;

			public Runnable init(String msg) {
				this.msg = msg;
				return (this);
			}

			public void run() {
				// msgList.add(msg);
				// arrayAdapter.notifyDataSetChanged();
				arrayAdapter.add(msg);

			}
		}.init(msg));

	}

	public void onClick(View v) {

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
	 protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if(platform != null) platform.onFormResult(requestCode, resultCode, data);
     }

	private static class AppLoader extends AsyncTaskLoader<Object> {

		private xMainActivity activity;

		public AppLoader(xMainActivity activity) {
			super(activity);
			// TODO Auto-generated constructor stub
			this.activity = activity;
		}

		@Override
		protected void onStartLoading() {
			activity.msgList.clear();
			activity.arrayAdapter.notifyDataSetChanged();
			forceLoad();
		}

		@Override
		public Object loadInBackground() {
			// TODO Auto-generated method stub
			//muhammed gökkaya
			//açılırken kapanma sorunu için kapatıldı!!


			try {
				for (;;) {
					activity.platform = ModLoader.loadPlatform();
                    //activity.addMessage("1");
					if (activity.moduleList.size()>=1){
					    //activity.addMessage("2");
					    try{
					        //activity.addMessage("3");
                            activity.platform.init(activity, R.raw.xsettings);
                            //activity.addMessage("4");
                            break;
                        }catch(Exception e){
                            activity.addMessage("Hata oluştu bekle");
                        }
					}
					sleep(2000);
				}



				activity.addMessage("Platform yüklendi");
                Thread.sleep(1000);
				if(activity.moduleList.size() == 0)
					throw new MobitException("Uygulama modülü bulunamadı!");
				if(activity.moduleList.size() != 1)
					throw new MobitException("Birden fazla uygulama modülü bulundu!");
				Globals.app = activity.platform.createApplication(activity.moduleList.get(0));

			} catch (final Exception e) {

				// TODO Auto-generated catch block
				if (activity.platform != null)
					activity.platform.Log(e);
				Log.e("APP", "Exception : " + Log.getStackTraceString(e));

				activity.runOnUiThread(new Runnable() {
					public void run() {
						// ShowException(e);

						if (activity.platform != null)
							activity.platform.ShowException(activity, e);
						else
							activity.ShowException(e);
					}
				});

			}

			return null;
		}

		@Override
		public void onStopLoading() {
			
		}

	};
	
	@Override
	public void restart() {
		// TODO Auto-generated method stub

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), xMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// |Intent.FLAG_ACTIVITY_NEW_TASK
																// Intent.FLAG_ACTIVITY_CLEAR_TASK
				startActivity(intent);

			}
		});

	}

	@Override
	public Loader<java.lang.Object> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return new AppLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<java.lang.Object> arg0, java.lang.Object arg1) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void onLoaderReset(Loader<java.lang.Object> arg0) {
		// TODO Auto-generated method stub
		return;
	}

	private List<String> getClassesOfPackage(String packageName) {
		List<String> classes = new ArrayList<String>();
		DexFile df = null;
		try {
			String packageCodePath = getPackageCodePath();
			df = new DexFile(packageCodePath);
			for (Enumeration<String> iter = df.entries(); iter.hasMoreElements();) {
				String className = iter.nextElement();
				if (className.contains(packageName)) {
					classes.add(className);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(df != null){
				try {
					df.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return classes;
	}
	private List<String> findModuleClasses() {

		List<String> classes = new ArrayList<String>();
		//classes.add("mobit.elec.mbs.medas.android.AndroidModule");
		/*
		File dexOutputDir = getApplicationContext().getDir("dex", Context.MODE_PRIVATE);
// Get the current class loader and pass it as parent when creating DexClassLoader
//
		DexClassLoader dexLoader = new DexClassLoader(getPackageCodePath(),
				dexOutputDir.getAbsolutePath(),
				null,
				getClassLoader());
// Use dex loader to load the class
//
		Class<?> loadedClass;
		String s;
		try {
			loadedClass = Class.forName("mobit.elec.mbs.medas.android.AndroidModule", true, dexLoader);
			classes.add(loadedClass);


		}
		catch(Exception e){

		}
		*/


		String packageCodePath = getPackageCodePath();
		File directory = new File(packageCodePath).getParentFile();
		File[] files = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".apk");
			}
		});
		for (File file : files) {
			DexFile df = null;
			try {
				df = new DexFile(file);
				for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
					String className = iter.nextElement();
					if ((className.contains("Module") && !className.contains("$"))) {
						classes.add(className);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (df != null) {
					try {
						df.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return classes;
	}

}
