package mobit.android;

import java.io.File;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
/*
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
*/
import android.os.StrictMode;
import android.util.Log;

import com.mobit.Globals;

import dalvik.system.DexClassLoader;

public class xMyApplication extends Application/*MultiDexApplication*/  {

	private Activity mainActivity = null;
	
	public void setMainActivity(Activity activity)
	{
		this.mainActivity = activity;
	}
	
	public xMyApplication()
	{
		super();
		// Globals sınıfının static yapıcısının çağrılması için
		boolean init = Globals.init;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}
		});

	}

	public void handleUncaughtException(Thread thread, Throwable e) {

		Throwable ex = null;
		//muhammed gökkaya log
		try {
			/*
			File file = new File(Environment.getExternalStorageDirectory().getPath(), com.mobit.IDef.LOG_FILE_NAME);
			String stackTrace = Log.getStackTraceString(e);
			Log.e("APP", "Exception : " + stackTrace);
			com.mobit.utility.Log(file, stackTrace.getBytes());
			com.mobit.utility.Log(file, e);
			
			if (com.mobit.Globals.app != null)
				com.mobit.Globals.app.close();
			if(mainActivity != null)
				mainActivity.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			*/
		} catch (Throwable ee) {
		
		}
		finally {
			System.exit(0);
		}
		/*
		 * if (Globals.platform != null) { Globals.platform.ShowException(null,
		 * e, DialogMode.Ok, 1, new IDialogCallback() {
		 * 
		 * @Override public void DialogResponse(int msg_id, DialogResult result)
		 * { // TODO Auto-generated method stub
		 * 
		 * try { if (Globals.app != null) Globals.app.close();
		 * 
		 * } catch (Exception e) {
		 * 
		 * } finally {
		 * android.os.Process.killProcess(android.os.Process.myPid());
		 * System.exit(0); } }
		 * 
		 * }); }
		 */

	}


	/*
	@Override
	protected void attachBaseContext(Context base) {
	    // TODO Auto-generated method stub
	    super.attachBaseContext(base);
	    MultiDex.install(this);
	   
	}
	*/
}
