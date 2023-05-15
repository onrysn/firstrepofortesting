package mobit.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import dalvik.system.DexClassLoader;
import com.mobit.IDef;
import mobit.android.xMyApplication;

public class utility {

	public static void selectSpinnerItemByValue(Spinner spinner, Object value) {
		if(spinner == null) return;
	    ArrayAdapter<?> adapter = (ArrayAdapter<?>) spinner.getAdapter();
	    if(adapter == null) return;
	    for (int position = 0; position < adapter.getCount(); position++) {
	        if(adapter.getItem(position).equals(value)) {
	        	spinner.setSelection(position);
	            return;
	        }
	    }
	}
	
	public static boolean isValidDialog(Dialog dlg)
	{
		if(dlg == null) return false;
		Activity activity = scanForActivity(dlg.getContext());
		if(activity == null) return false;
		return !(activity.isFinishing() || activity.isDestroyed()); 
	
	}
	
	public static Activity scanForActivity(Context context) {
	    if (context == null)
	        return null;
	    else if (context instanceof Activity)
	        return (Activity) context;
	    else if (context instanceof ContextWrapper)
	        return scanForActivity(((ContextWrapper)context).getBaseContext());
	    return null;
	}
	
	public static void setClassLoader(android.app.Application application, ClassLoader classLoader) throws Exception
	{
		
		Field mBase = com.mobit.utility.getField(android.content.ContextWrapper.class, "mBase");
		Object base = mBase.get(application);
		Field mMainThread = com.mobit.utility.getField(base.getClass(), "mMainThread");
		Object mainThread = mMainThread.get(base);
		Class<?> threadClass = mainThread.getClass();
		Field mPackages = com.mobit.utility.getField(threadClass, "mPackages");
		ArrayMap<String,?> map = (ArrayMap<String,?>)mPackages.get(mainThread);
		WeakReference<?> ref = (WeakReference<?>) map.get(application.getPackageName());
		Object apk = ref.get();
		Class<?> apkClass = apk.getClass();
		Field mClassLoader = com.mobit.utility.getField(apkClass, "mClassLoader");

		mClassLoader.set(apk, classLoader);
			
	}
	
	public static File copyAsset(android.app.Application app, String assetName, String destPath) throws IOException {
		
		InputStream is = null;
		FileOutputStream os = null;
		 try {

			 File fos = new File(app.getCacheDir(), assetName);
		    is = app.getAssets().open(assetName);
		    os = new FileOutputStream(fos);
		    byte[] buffer = new byte[16*1024];
		    int read;
		    while((read = is.read(buffer)) > 0)
		    	os.write(buffer, 0, read);
		    
		    return fos;
		    
		  } 
		 finally{
			 
			 if(is != null) is.close();
			 if(os != null) os.close();
		 }
	}

	static DexClassLoader dexClassLoader;
	
	void loadMapApk(android.app.Application app)
	{	
		
		try {
			
			File file = mobit.android.utility.copyAsset(app, "googleMapAndroid.apk", app.getCacheDir().getAbsolutePath());
			File fo = app.getDir("outdex", Context.MODE_PRIVATE);
			fo.mkdir();
			
			dexClassLoader = new DexClassLoader(file.getAbsolutePath(), fo.getAbsolutePath(), 
		    		null, app.getClassLoader());
		    
			mobit.android.utility.setClassLoader(app, dexClassLoader);
		   
		    
		} catch (Throwable e) {
		    throw new RuntimeException(e);
		}
	}
	
	public static void Harita(Activity activity)
	{
		String s;
		try {
			Class<?> activityClass = utility.dexClassLoader.loadClass("mobit.android.GoogleMap.MapsActivity");
			Intent intent = new Intent(activity, activityClass);
			activity.startActivityForResult(intent, IDef.FORM_MAP);
		}
		catch(Exception e){
			s = e.getMessage();
		}
	}

	public static float sp2px(Context context, float sp)
	{
		return sp * context.getResources().getDisplayMetrics().scaledDensity;
	}
	public static float px2sp(Context context, float px)
	{
		return px / context.getResources().getDisplayMetrics().scaledDensity;
	}
	public static float dp2px(Context context, float dp)
	{
		return dp * context.getResources().getDisplayMetrics().density;
	}
	public static float px2dp(Context context, float px)
	{
		return px / context.getResources().getDisplayMetrics().density;
	}

	public static void setNewTab(TabHost tabHost, String tag, String titleString, int icon, int contentID)
	{
		TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(titleString, tabHost.getContext().getResources().getDrawable(android.R.drawable.star_on));
		tabSpec.setContent(contentID);
		tabHost.addTab(tabSpec);

	}
	public static void setTabTextSize(TabWidget tw, float textSize)
	{
		for (int i = 0; i < tw.getChildCount(); i++)
		{
			View tabView = tw.getChildTabViewAt(i);
			TextView tv = (TextView)tabView.findViewById(android.R.id.title);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		}
	}


}
