package mobit.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.sqldroid.SQLDroidDriver;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.android.internal.telephony.PhoneConstants;
import com.android.mms.transaction.TransactionSettings;
import com.google.android.mms.util.SqliteWrapper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.provider.Telephony.Carriers;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import dalvik.system.DexFile;
import com.mobit.Callback;
import com.mobit.DeviceInfo;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.IBluetooth;
import com.mobit.ICallback;
import com.mobit.ICamera;
import com.mobit.IDeviceInfo;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.ILocation;
import com.mobit.MobitException;
import com.mobit.Platform;
import com.mobit.XMLLoader;
import mobit.eemr.IMeterReader;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkUtils;

public class AndroidPlatform extends Platform {

	private Application app = null;
	private Context context = null;
	private AssetManager assetManager = null;
	private Activity activity = null;

	@SuppressWarnings("unused")
	private Resources resources = null;
	private SharedPreferences pref;
	private InputMethodManager imm = null;
	Uri updUri = null;
	DownloadManager manager = null;
	private PowerManager pm = null;
	PowerManager.WakeLock wl = null;
	ConnectivityManager connectivityManager = null;

	Criteria criteria = null;
	String bestProvider = "gps";

	public static final ColorDrawable AndroidGreen = new ColorDrawable(Color.parseColor("#A4C639"));

	public AndroidPlatform() throws Exception {

		// Bu kısmı değiştirme
		//sami zurunlu değiştirdi
		Globals.setDeveloping(false);
		// Globals.setDeveloping(false);

		Globals.platform = this;

		app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null,
				(Object[]) null);

		/*
		 * return (Application) Class.forName("android.app.ActivityThread")
		 * .getMethod("currentApplication").invoke(null, (Object[]) null);
		 */
		context = app.getApplicationContext();
		assetManager = context.getAssets();
		resources = app.getResources();
		pref = PreferenceManager.getDefaultSharedPreferences(context);

		updUri = Uri.fromFile(new File(getExternalStorageDirectory(), "tmp.apk")); // context.getCacheDir()
		manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		/*
		 * SharedPreferences sharedPref; sharedPref =
		 * context.getSharedPreferences("A", Context.MODE_PRIVATE);
		 * 
		 * sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		 * //SharedPreferences.Editor editor = sharedPref.edit();
		 * //editor.putInt("A", 1); //editor.commit(); int x =
		 * sharedPref.getInt("A", 0); x++;
		 */
		pm = (PowerManager) app.getSystemService(Context.POWER_SERVICE);

		connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		// wl.acquire(); İşlemcinin çalışır halde kalmasını sağlıyor
		/*
		 * 
		 * this.getApplication().registerReceiver(new BroadcastReceiver(){
		 * 
		 * @Override public void onReceive(Context context, Intent intent) {
		 * IsemriMenuActivity.this.app.ShowMessage(IsemriMenuActivity.this,
		 * "Uyandı", ""); } }, new IntentFilter(Intent.ACTION_SCREEN_ON));
		 */
		setLocale(new Locale("TR-tr"));

		if (ActivityCompat.checkSelfPermission(context,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(context,
						Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

		}

		Screen();

	}

	@Override
	public void init(ICallback callback, Object resid) throws Exception {
		//Muhammed Gökkaya
		super.init(callback, resid);
		if (callback instanceof Activity)

			activity = (Activity) callback;

		if (activity != null) {
			activity.startService(new Intent(activity, LocationService.class));
		}
	}

	@Override
	public void close() {

		super.close();
		if (wl != null)
			wl.release();
		//HÜSEYİN EMRE ÇEVİK (Giriş ekranın da atma hatası)
		//activity.stopService(new Intent(activity, LocationService.class));
	}

	@Override
	public void exit() {
		close();
		if (assetManager != null) {
			assetManager.close();
			assetManager = null;
		}
		//HÜSEYİN EMRE ÇEVİK (Giriş ekranın da atma hatası)
	//	activity.stopService(new Intent(activity, LocationService.class));
		System.exit(0);

	}

	@Override
	public PlatformId getId() {
		return PlatformId.Android;
	}

	@Override
	public Object getApplication() {

		return app;
	}

	@Override
	public Object getContext() {

		return context;
	}

	@Override
	public IForm getActiveForm() {
		try {
			return (IForm) getActivity();
		} catch (Exception e) {
			throw new RuntimeException("Aktif aktivite alınamadı!");
		}
	}

	public String getExternalStorageDirectory() {
		
		//String path = Environment.getExternalStorageDirectory().getPath();
		//if(path.length() > 0) return path;
		// Programın kullandığı dizinleri döndürüyor
		File[] files = ContextCompat.getExternalFilesDirs(context, null);
		if (files.length == 0)
			return Environment.getExternalStorageDirectory().getPath();
		if (files.length == 1)
			return files[0].getPath();
		return files[0].getPath();
		
	}


	private static boolean registered = false;

	public Connection createDbConnection(String dbFile, String user, String password) throws Exception {

		// PooledConnection pool = new PooledConnection();
		if (registered == false) {

			// DriverManager.registerDriver((Driver)Class.forName("org.sqldroid.SQLDroidDriver").newInstance());
			Class.forName("org.sqldroid.SQLDroidDriver"); // "SQLite.JDBCDriver"

			registered = true;
		}

		String driver = SQLDroidDriver.sqldroidPrefix; // "jdbc:sqlite:"
		final String jdbcURL = String.format("%s%s", driver, dbFile);

		/*
		 * // Dikkatli değiştirilmesi gerekiyor if (Globals.isDeveloping()) { //
		 * context.deleteDatabase(dbFile);
		 * 
		 * }
		 */

		return DriverManager.getConnection(jdbcURL, user, password);

	}
	
	@Override
	public void deleteDatabase(String dbFile) throws Exception
	{
		context.deleteDatabase(dbFile);
	}

	public Object loadClassFromPackage(String packageName, String className) throws Exception {

		Object plugin = null;
		DexFile df = null;
		try {
			PackageManager packageManager = app.getPackageManager();
			ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
			df = new DexFile(appInfo.sourceDir); //
			ClassLoader cl = app.getClassLoader();
			Class<?> classToInvestigate = df.loadClass(className, cl);
			plugin = classToInvestigate.newInstance();
		} finally {

			if (df != null)
				df.close();
		}
		return plugin;
	}

	@Override
	public XMLLoader getXmlResource(Class<?> cls, Object id) throws Exception {

		InputStream is = null;
		XMLLoader loader = null;
		try {
			loader = (XMLLoader) cls.getConstructor().newInstance();
			is = app.getResources().openRawResource((Integer) id);
			loader.load(is);
		} finally {

			if (is != null)
				is.close();
		}
		return loader;
	}

	@Override
	public String getTextResource(Object id) throws Exception {

		InputStream is = null;
		ByteArrayOutputStream out = null;
		String s = null;

		try {

			out = new ByteArrayOutputStream();
			is = app.getResources().openRawResource((Integer) id);
			byte[] buffer = new byte[4 * 1024];
			while (is.available() > 0) {
				int len = is.read(buffer);
				out.write(buffer, 0, len);
			}
		} finally {

			if (is != null)
				is.close();
			if (out != null) {
				s = out.toString("UTF-8");
				out.close();
			}
		}
		return s;
	}

	@Override
	public String getSettingValue(String key) {

		return pref.getString(key, "");

	}

	@Override
	public void setSettingValue(String key, String value) {
		Editor edit = pref.edit();
		edit.putString(key, value);
		edit.commit();

	}

	public static Activity getActivity(Activity activity)
	{
		if(activity != null && !(activity.isDestroyed() || activity.isFinishing()))
			return activity;
		
		activity = null;
		
		try {
			activity = (Activity) AndroidPlatform.getActivity();
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		if (activity == null) return null;
		if(activity.isDestroyed() || activity.isFinishing())
			return null;
		
		return activity;
	}

	public static void ShowMessage(Activity activity, String msg, String title, DialogMode mode, final int msg_id, final int timeout,
			final IDialogCallback clb) {

		activity = getActivity(activity);
		if(activity == null) return;

		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		if (title == null)
			title = "";
		if (msg == null)
			msg = "";
		SpannableString _title = new SpannableString(title);
		SpannableString _msg = new SpannableString(msg);
		if (msg.length() > 0) {
			_msg.setSpan(new RelativeSizeSpan(1.3f), 0, msg.length(), 0);
			_msg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, msg.length(), 0);
			if (msg.equals("Onaylanacaktır!\nEmin misiniz?")) _msg.setSpan(new ForegroundColorSpan(Color.GREEN), 0, msg.length(), 0);
			if (msg.equals("Reddedilecektir!\nEmin misiniz?")) _msg.setSpan(new ForegroundColorSpan(Color.RED), 0, msg.length(), 0);
		}

		if (title.length() > 0) {
			_title.setSpan(new RelativeSizeSpan(1.3f), 0, title.length(), 0);
			_title.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
			if (!title.equals("TESİSAT KABUL SONUCU"))
				_title.setSpan(new BackgroundColorSpan(AndroidPlatform.AndroidGreen.getColor()), 0, title.length(), 0);
		}
		builder.setTitle(_title);
		builder.setMessage(_msg);

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
				if (clb != null) {
					DialogResult result = null;
					switch (which) {
					case DialogInterface.BUTTON_NEUTRAL:
						result = DialogResult.Ok;
						break;
					case DialogInterface.BUTTON_POSITIVE:
						result = DialogResult.Yes;
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						result = DialogResult.No;
						break;
					default:
						result = DialogResult.None;

					}
					clb.DialogResponse(msg_id, result);
				}
			}
		};

		if (mode == DialogMode.Ok) {

			builder.setNeutralButton(R.string.ok, listener);

		} else if (mode == DialogMode.YesNo) {
			builder.setPositiveButton(R.string.yes, listener);
			builder.setNegativeButton(R.string.no, listener);
		}

		activity.runOnUiThread(new Runnable() {
			public void run() {

				final AlertDialog dlg = builder.create();
				final int MSG_DISMISS_DIALOG = 0;
				Handler handler = null;
				if(timeout > 0) {
					handler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
								case MSG_DISMISS_DIALOG:
									if (dlg != null && dlg.isShowing()) {
										dlg.dismiss();
									}
									break;

								default:
									break;
							}
						}
					};
				}

				dlg.show();

				if(handler != null)
					handler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, timeout);
			}
		});

	}
	
	@Override
	public void ShowMessage(IForm form, String msg, String title, DialogMode mode, final int msg_id,
			final int timeout, final IDialogCallback clb) {

		Activity activity = null;

		if (form instanceof Activity)
			activity = (Activity) form;
		else if (form instanceof Fragment)
			activity = ((Fragment) form).getActivity();
		
		if(activity != null && (activity.isDestroyed() || activity.isFinishing()))
			activity = null;
		
		if (activity == null)
			activity = (Activity) getActiveForm();
		if (activity == null)
			return;
		
		ShowMessage(activity, msg, title, mode, msg_id, timeout, clb);

	}

	@Override
	public void ShowMessage(IForm form, String msg, String title) {

		ShowMessage(form, msg, title, DialogMode.Ok, 0, 0, null);
	}

	@Override
	public IBluetooth createBluetooth() {
		return new AndroidBluetooth();
	}

	@Override
	public void createForm(Object context, Class<?> cls) {
		Context ctx = (Context) context;
		Intent intent = new Intent(ctx, cls);
		ctx.startActivity(intent);
	}

	public static void FullScreen(Activity activity) {

		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower
																		// api
			View v = activity.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			// for new api versions.
			View decorView = activity.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE // |
																// View.SYSTEM_UI_FLAG_IMMERSIVE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | 8
			// | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			;
			// android.permission.EXPAND_STATUS_BAR
			decorView.setSystemUiVisibility(uiOptions);
		}

		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public String getVersion(URL verUrl) throws IOException {

		return com.mobit.utility.getVersion(verUrl);
	}

	@Override
	public void updateApp(final URL appUrl) throws Exception {
		//yazılım güncelleme Muhammed Gökkaya

		//Activity activity = getActivity();
		//Intent install = new Intent(context, UpdateActivity.class);
		//install.putExtra("appUrl", appUrl.toString());
		//install.putExtra("updUri", updUri.toString());
		//install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//context.startActivity(install);

		/*
		 * DownloadManager.Request request = new
		 * DownloadManager.Request(Uri.parse(appUrl.toString())); //
		 * request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
		 * ); request.setTitle("Güncelleme"); request.setDescription(
		 * "APK İndirme"); request.setDestinationUri(updUri);
		 * 
		 * @SuppressWarnings("unused") final long downloadId =
		 * manager.enqueue(request);
		 * 
		 * final BroadcastReceiver onComplete = new BroadcastReceiver() { public
		 * void onReceive(Context ctxt, Intent intent) {
		 * 
		 * Intent install = new Intent(context, UpdateActivity.class);
		 * install.setData(updUri);
		 * install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * context.startActivity(install);
		 * 
		 * } }; context.registerReceiver(onComplete, new
		 * IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)); //
		 * context.unregisterReceiver(onComplete);
		 * 
		 */
	}

	/*
	 * @Override public PageFormatInfo getPageFormatInfo(Object id) throws
	 * Exception {
	 * 
	 * 
	 * InputStream is = null; PageFormatInfo pfi = null; try {
	 * 
	 * pfi = new PageFormatInfo(); is =
	 * app.getResources().openRawResource((Integer)id); pfi.load(is); } finally
	 * {
	 * 
	 * if(is != null) is.close(); } return pfi; }
	 * 
	 * @Override public ISettings getSettings(Object id) throws Exception {
	 * 
	 * InputStream is = null; Settings settings = null; try {
	 * 
	 * settings = new Settings(); is =
	 * app.getResources().openRawResource((Integer)id); settings.load(is); }
	 * finally {
	 * 
	 * if(is != null) is.close(); } return settings;
	 * 
	 * /* res/xml compiled resources XmlPullParser xpp =
	 * app.getResources().getXml((Integer)id); Settings stg = new Settings();
	 * while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) { if
	 * (xpp.getEventType() == XmlPullParser.START_TAG) { if
	 * (xpp.getName().equals("Parameter")) { Parameter p = new
	 * Parameter(xpp.getAttributeValue(0), xpp.getAttributeValue(1));
	 * stg.addItem(p); } } xpp.next(); }
	 */

	/*
	 * InputStream stream = null; try {
	 * 
	 * stream = assetManager.open(file); Settings stg = new Settings();
	 * stg.load(stream); return stg;
	 * 
	 * } finally {
	 * 
	 * if(stream != null) stream.close(); }
	 *
	 * 
	 * }
	 */

	
	private static Class<?> activityThreadClass = null;
	private static Method currentActivityThread = null;
	private static Field activitiesField = null;
	
	
	public static Activity getActivity() throws Exception {
		
		if(activityThreadClass == null){
			activityThreadClass = Class.forName("android.app.ActivityThread");
			activitiesField = activityThreadClass.getDeclaredField("mActivities");
			activitiesField.setAccessible(true);
			currentActivityThread = activityThreadClass.getMethod("currentActivityThread");
		}
		
		Object activityThread = currentActivityThread.invoke(null);

		@SuppressWarnings("unchecked")
		Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
		if (activities == null)
			return null;

		Activity activity = null;
		Object [] activityRecords = activities.values().toArray();
		for (int i = 0; i < activityRecords.length; i++) {
		
			Object activityRecord = activityRecords[i];
			Class<?> activityRecordClass = activityRecord.getClass();
			Field pausedField = activityRecordClass.getDeclaredField("paused");
			pausedField.setAccessible(true);
			Field activityField = activityRecordClass.getDeclaredField("activity");
			activityField.setAccessible(true);
			
			activity = (Activity) activityField.get(activityRecord);
			if(!(activity.isDestroyed() || activity.isFinishing())){
				//if(!pausedField.getBoolean(activityRecord)) 
					return activity;
			}
			
		}
		return activity;
	}

	@Override
	public IMeterReader createMeterReader() throws Exception {
		// TODO Auto-generated method stub
		try {
			String clazz = "mobit.eemr.MeterReaderCpp";
			// String clazz ="mobit.eemr.MeterReaderAndroid";
			return mobit.eemr.utility.create(clazz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MobitException("Optik port kütüphanesi yüklenemedi!", e);

		}
	}

	@SuppressLint("ServiceCast")
	@Override
	public ILocation getLocation() throws Exception {

		Location location = null;
		LocationService locationService = LocationService.sLocationService;
		if (locationService != null) {
			if (!locationService.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER))
				throw new MobitException("Konum alma(GPS) kapalı. Konum almayı açın!");
			location = LocationService.sLocationService.getLocation();
		}

		if (location == null)
			location = new Location("");
		return new LocationEx(location);

	}

	public ILocation newLocationObject() {
		return new LocationEx();
	}

	@Override
	public ICamera createCamera() {
		// TODO Auto-generated method stub
		return new AndroidCamera();
	}

	@Override
	public void showSoftKeyboard(IForm form, boolean show) {
		Activity activity = null;
		if (form instanceof Activity)
			activity = (Activity) form;
		else if (form instanceof Fragment)
			activity = ((Fragment) form).getActivity();

		if(activity == null || activity.isDestroyed() || activity.isFinishing())
			return;
		
		if (imm == null)
			imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (show) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

		} else {
			View view = activity.getCurrentFocus();
			if(view != null && view.getWindowToken() != null) 
				imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	public void initForm(IForm form, FormInitParam param) {
		if (!(form instanceof Activity))
			return;
		if (form instanceof AppCompatActivity) {
			initForm((AppCompatActivity) form, param);
			return;
		}
		initForm((Activity) form);
	}

	public static void initForm(Activity activity, FormInitParam param) {

		if (activity instanceof AppCompatActivity) {
			ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();

			if (actionBar != null) {
				boolean homeButton = (param != null) ? param.homeButton : true;
				actionBar.setDisplayHomeAsUpEnabled(homeButton);

				ColorDrawable dc = (param != null) ? new ColorDrawable(Color.parseColor(param.captionColor))
						: AndroidPlatform.AndroidGreen;
				actionBar.setBackgroundDrawable(dc);

				if (param != null && param.captionText != null && param.captionText.length() > 0) {
					actionBar.setTitle(param.captionText);
				}

				TextView textview = new TextView(activity);

				RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);

				textview.setLayoutParams(layoutparams);
				textview.setText(actionBar.getTitle());
				textview.setTextColor(Color.WHITE);
				textview.setGravity(Gravity.START);
				//MUHAMMED GÖKKAYA
				textview.setTextSize(15);
				int options = ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE;
				if(homeButton) options |= ActionBar.DISPLAY_HOME_AS_UP;
				actionBar.setDisplayOptions(options);
				actionBar.setCustomView(textview);

			}

		} else {
			android.app.ActionBar actionBar = activity.getActionBar();
			ColorDrawable dc = (param != null) ? new ColorDrawable(Color.parseColor(param.captionColor))
					: AndroidPlatform.AndroidGreen;
			actionBar.setBackgroundDrawable(dc);
			if (param != null && param.captionText != null && param.captionText.length() > 0)
				actionBar.setTitle(param.captionText);
		}
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Window w = activity.getWindow();
		w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		w.setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	}

	public static void initForm(Activity activity) {

		initForm(activity, null);
		/*
		 * android.app.ActionBar actionBar = activity.getActionBar(); if
		 * (actionBar != null) {
		 * actionBar.setBackgroundDrawable(AndroidPlatform.AndroidGreen);
		 * actionBar.setDisplayHomeAsUpEnabled(true);
		 * actionBar.setBackgroundDrawable(AndroidGreen);
		 * 
		 * } activity.setRequestedOrientation(ActivityInfo.
		 * SCREEN_ORIENTATION_PORTRAIT);
		 */
	}

	@Override
	public void onFormResult(int requestCode, int resultCode, Object data) {

	}

	private static final String[] APN_PROJECTION = {

			Telephony.Carriers.APN, Telephony.Carriers.MMSPROXY, Telephony.Carriers.MMSPORT, Telephony.Carriers.TYPE// ,
																													// Telephony.Carriers._ID
			// Telephony.Carriers._ID,
			// Telephony.Carriers.NAME,
			// Telephony.Carriers.NUMERIC,
			// Telephony.Carriers.MCC,
			// Telephony.Carriers.MNC,

			// Telephony.Carriers.MMSC,
			// Telephony.Carriers.CURRENT
			// Telephony.Carriers.USER,
			// Telephony.Carriers.SERVER,
			// Telephony.Carriers.PASSWORD,
			// Telephony.Carriers.PROXY,
			// Telephony.Carriers.PORT,

	};

	private static final String[] sProjection = new String[] { Telephony.Carriers._ID, // 0
			Telephony.Carriers.NAME, // 1
			Telephony.Carriers.APN, // 2
			Telephony.Carriers.PROXY, // 3
			Telephony.Carriers.PORT, // 4
			Telephony.Carriers.USER, // 5
			Telephony.Carriers.SERVER, // 6
			Telephony.Carriers.PASSWORD, // 7
			Telephony.Carriers.MMSC, // 8
			Telephony.Carriers.MCC, // 9
			Telephony.Carriers.MNC, // 10
			Telephony.Carriers.NUMERIC, // 11
			Telephony.Carriers.MMSPROXY, // 12
			Telephony.Carriers.MMSPORT, // 13
			Telephony.Carriers.AUTH_TYPE, // 14
			Telephony.Carriers.TYPE, // 15
			Telephony.Carriers.PROTOCOL, // 16
			Telephony.Carriers.CARRIER_ENABLED, // 17
			Telephony.Carriers.BEARER, // 18
			Telephony.Carriers.ROAMING_PROTOCOL, // 19
			Telephony.Carriers.MVNO_TYPE, // 20
			Telephony.Carriers.MVNO_MATCH_DATA // 21
	};

	/*
	 * private static final int ID_INDEX = 0; private static final int
	 * NAME_INDEX = 1; private static final int APN_INDEX = 2; private static
	 * final int PROXY_INDEX = 3; private static final int PORT_INDEX = 4;
	 * private static final int USER_INDEX = 5; private static final int
	 * SERVER_INDEX = 6; private static final int PASSWORD_INDEX = 7; private
	 * static final int MMSC_INDEX = 8; private static final int MCC_INDEX = 9;
	 * private static final int MNC_INDEX = 10; private static final int
	 * MMSPROXY_INDEX = 12; private static final int MMSPORT_INDEX = 13; private
	 * static final int AUTH_TYPE_INDEX = 14; private static final int
	 * TYPE_INDEX = 15; private static final int PROTOCOL_INDEX = 16; private
	 * static final int CARRIER_ENABLED_INDEX = 17; private static final int
	 * BEARER_INDEX = 18; private static final int ROAMING_PROTOCOL_INDEX = 19;
	 * private static final int MVNO_TYPE_INDEX = 20; private static final int
	 * MVNO_MATCH_DATA_INDEX = 21;
	 */

	public void checkAPN(String apn) throws Exception {

		Cursor c = null;
		boolean ok = false;
		/*
		try {

			// apn'nin tanımlı olup olmadığının tespiti
			c = context.getContentResolver().query(Uri.withAppendedPath(Carriers.CONTENT_URI, "current"),
					APN_PROJECTION, null, null, null);
			for (boolean b = c.moveToFirst(); b; b = c.moveToNext()) {
				String s = c.getString(0);
				if (c.getString(0).compareToIgnoreCase(apn) == 0) {
					ok = true;
					break;
				}
			}
			c.close();
			c = null;
			if (!ok)
				throw new MobitException(100001, String.format("%s APN'i tanımlanmamış", apn));

			// apn'nin varsayılan olarak seçilip seçilmediğinin tespiti
			ok = false;
			c = context.getContentResolver().query(Uri.withAppendedPath(Carriers.CONTENT_URI, "preferapn"),
					APN_PROJECTION, null, null, null);
			for (boolean b = c.moveToFirst(); b; b = c.moveToNext()) {
				if (c.getString(0).compareToIgnoreCase(apn) == 0) {
					ok = true;
					break;
				}
			}
			if (!ok)
				throw new MobitException(100002, String.format("%s APN'i varsayılan olarak seçilmemiş", apn));

		} finally {

			if (c != null)
				c.close();
		}
		 */

	}

	private void addNewApn() throws Exception {
		/*
		 * Activity activity = getActivity(); int subId =
		 * activity.getIntent().getIntExtra("sub_id", 0); SubscriptionInfo
		 * mSubscriptionInfo = null;// Utils.findRecordBySubId(activity, subId);
		 * Intent intent = new Intent(Intent.ACTION_INSERT,
		 * Telephony.Carriers.CONTENT_URI); subId = mSubscriptionInfo != null ?
		 * mSubscriptionInfo.getSubscriptionId() : 0; intent.putExtra("sub_id",
		 * subId); activity.startActivity(intent);
		 */
	}

	public void setAPN(IForm form, String apn) {

		// if(true) return;

		try {
			Activity activity = (Activity) form;
			/*
			 * int permissionCheck = ContextCompat.checkSelfPermission(activity,
			 * Manifest.permission.WRITE_APN_SETTINGS); if (permissionCheck !=
			 * PackageManager.PERMISSION_GRANTED) { //requesting permission
			 * ActivityCompat.requestPermissions(activity, new
			 * String[]{Manifest.permission.WRITE_APN_SETTINGS}, 1); } else {
			 * //permission is granted and you can change APN settings }
			 * 
			 * InsertAPN(apn);
			 */

			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_APN_SETTINGS);
			activity.startActivityForResult(intent, 1);

		} catch (Exception e) {
			return;
		}
	}

	@SuppressWarnings("unused")
	private void apn() throws Exception {

		// boolean b = checkAPN("inTERnet1");
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setClassName("com.android.settings", "com.android.settings.ApnSettings");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

		/*
		 * Intent intent = new Intent("apndroid.intent.action.CHANGE_STATUS");
		 * intent.putExtra("apndroid.intent.extra.STATUS", true);
		 * intent.putExtra("apndroid.intent.extra.KEEP_MMS_ON", true);
		 * context.startService(intent);
		 * 
		 * context.registerReceiver(new BroadcastReceiver() {
		 * 
		 * @Override public void onReceive(Context context, Intent intent) { if
		 * (intent.getAction().equals("apndroid.intent.action.STATUS")) {
		 * boolean dataEnabled =
		 * intent.getBooleanExtra("apndroid.intent.extra.STATUS", true); } } },
		 * new IntentFilter("apndroid.intent.action.STATUS"));
		 * 
		 * 
		 * context.startService(new
		 * Intent("apndroid.intent.action.GET_STATUS"));
		 * 
		 * context.registerReceiver(new BroadcastReceiver() {
		 * 
		 * @Override public void onReceive(Context context, Intent intent) { if
		 * (intent.getAction().equals("apndroid.intent.action.STATUS")) {
		 * boolean dataEnabled =
		 * intent.getBooleanExtra("apndroid.intent.extra.STATUS", true); } } },
		 * new IntentFilter("apndroid.intent.action.STATUS"));
		 * 
		 */

	}

	public int InsertAPN(String name) {

		// Set the URIs and variables
		int id = -1;
		boolean existing = false;
		final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
		final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

		final Cursor apnCursor = SqliteWrapper.query(context, this.context.getContentResolver(),
				Uri.withAppendedPath(Carriers.CONTENT_URI, "current"), APN_PROJECTION, null, null, null);

		@SuppressWarnings("unused")
		String[] cc;
		String s;
		for (boolean b = apnCursor.moveToFirst(); b; b = apnCursor.moveToNext()) {
			cc = apnCursor.getColumnNames();
			for (int i = 0; i < 3; i++) {
				s = apnCursor.getString(i);
				s = s + "";
			}

		}
		apnCursor.close();

		// Check if the specified APN is already in the APN table, if so skip
		// the insertion
		Cursor parser = context.getContentResolver().query(APN_TABLE_URI, null, null, null, null);
		parser.moveToLast();
		while (parser.isBeforeFirst() == false) {
			int index = parser.getColumnIndex("name");
			String n = parser.getString(index);
			if (n.equals(name)) {
				existing = true;
				Toast.makeText(context, "APN already configured.", Toast.LENGTH_SHORT).show();
				break;
			}
			parser.moveToPrevious();
		}
		parser.close();
		// if the entry doesn't already exist, insert it into the APN table
		if (!existing) {

			// Initialize the Content Resolver and Content Provider
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();

			// Capture all the existing field values excluding name
			Cursor apu = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
			apu.moveToFirst();
			int index;

			index = apu.getColumnIndex("apn");
			String apn = apu.getString(index);
			index = apu.getColumnIndex("type");
			String type = apu.getString(index);
			index = apu.getColumnIndex("proxy");
			String proxy = apu.getString(index);
			index = apu.getColumnIndex("port");
			String port = apu.getString(index);
			index = apu.getColumnIndex("user");
			String user = apu.getString(index);
			index = apu.getColumnIndex("password");
			String password = apu.getString(index);
			index = apu.getColumnIndex("server");
			String server = apu.getString(index);
			index = apu.getColumnIndex("mmsc");
			String mmsc = apu.getString(index);
			index = apu.getColumnIndex("mmsproxy");
			String mmsproxy = apu.getString(index);
			index = apu.getColumnIndex("mmsport");
			String mmsport = apu.getString(index);
			index = apu.getColumnIndex("mcc");
			String mcc = apu.getString(index);
			index = apu.getColumnIndex("mnc");
			String mnc = apu.getString(index);
			index = apu.getColumnIndex("numeric");
			String numeric = apu.getString(index);

			// Assign them to the ContentValue object
			values.put("name", name); // the method parameter
			values.put("apn", apn);
			values.put("type", type);
			values.put("proxy", proxy);
			values.put("port", port);
			values.put("user", user);
			values.put("password", password);
			values.put("server", server);
			values.put("mmsc", mmsc);
			values.put("mmsproxy", mmsproxy);
			values.put("mmsport", mmsport);
			values.put("mcc", mcc);
			values.put("mnc", mnc);
			values.put("numeric", numeric);

			// Actual insertion into table
			Cursor c = null;
			Uri newRow = resolver.insert(APN_TABLE_URI, values);

			if (newRow != null) {
				c = resolver.query(newRow, null, null, null, null);
				int idindex = c.getColumnIndex("_id");
				c.moveToFirst();
				id = c.getShort(idindex);
			}

			if (c != null)
				c.close();
		}

		return id;
	}

	// Takes the ID of the new record generated in InsertAPN and sets that
	// particular record the default preferred APN configuration
	public boolean SetPreferredAPN(int id) {

		// If the id is -1, that means the record was found in the APN table
		// before insertion, thus, no action required
		if (id == -1) {
			return false;
		}

		Uri.parse("content://telephony/carriers");
		final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

		boolean res = false;
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();

		values.put("apn_id", id);

		resolver.update(PREFERRED_APN_URI, values, null, null);
		Cursor c = resolver.query(PREFERRED_APN_URI, new String[] { "name", "apn" }, "_id=" + id, null, null);
		if (c != null) {
			res = true;
			c.close();
		}

		return res;
	}

	String mServiceCenter;
	String mProxyAddress;
	int mProxyPort;

	public boolean isProxySet() {
		return (mProxyAddress != null) && (mProxyAddress.trim().length() != 0);
	}

	@SuppressLint("NewApi")
	private boolean getSettingsFromApnsFile(Context context, String apnName) {
		FileReader reader = null;
		boolean sawValidApn = false;

		try {
			/*
			 * final ContentResolver cr = context.getContentResolver();
			 * ContentValues values = new ContentValues();
			 * values.put(Telephony.Carriers.APN, "apn");
			 * values.put(Telephony.Carriers.NAME, "name");
			 * values.put(Telephony.Carriers.TYPE, "type");
			 * values.put(Telephony.Carriers.PROXY, "proxy");
			 * values.put(Telephony.Carriers.MNC, "mnc");
			 * values.put(Telephony.Carriers.MCC, "mcc");
			 * values.put(Telephony.Carriers.USER, "user");
			 * values.put(Telephony.Carriers.PASSWORD, "password");
			 * values.put(Telephony.Carriers.SERVER, "server");
			 * cr.insert(Telephony.Carriers.CONTENT_URI, values);
			 */
			reader = new FileReader("/etc/apns-conf.xml");

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(reader);

			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String simOperator = telephonyManager.getSimOperator();
			if (TextUtils.isEmpty(simOperator)) {
				// logger.warn("unable to get sim operator - so unable to get
				// mms config");
				return false;
			}

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("apn")) {
					HashMap<String, String> attributes = new HashMap<String, String>();
					for (int i = 0; i < xpp.getAttributeCount(); i++) {
						attributes.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
					}
					if (attributes.containsKey("mcc") && attributes.containsKey("mnc")
							&& simOperator.equals(attributes.get("mcc") + attributes.get("mnc"))) {
						if (!TextUtils.isEmpty(apnName) && !apnName.trim().equals(attributes.get("apn"))) {
							eventType = xpp.next();
							continue;
						}

						if (TransactionSettings.isValidApnType(attributes.get("type"), PhoneConstants.APN_TYPE_ALL)) {
							sawValidApn = true;

							String mmsc = attributes.get("mmsc");
							if (mmsc == null) {
								eventType = xpp.next();
								continue;
							}

							mServiceCenter = NetworkUtils.trimV4AddrZeros(mmsc.trim());
							mProxyAddress = NetworkUtils.trimV4AddrZeros(attributes.get("mmsproxy"));
							if (isProxySet()) {
								String portString = attributes.get("mmsport");
								try {
									mProxyPort = Integer.parseInt(portString);
								} catch (NumberFormatException e) {
									if (TextUtils.isEmpty(portString)) {
										// logger.warn("mms port not set!");
									} else {
										// logger.error("Bad port number format:
										// " + portString, e);
									}
								}
							}
						}

					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {

			return sawValidApn;

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
		return sawValidApn;
	}

	public interface IPopupCallback {

		void OnItemSelect(View anchorView, Object obj);
	};

	public static void initPopupList(final ArrayAdapterEx<?> adapter, final View anchorView, int width,
			final IPopupCallback clb) {

		final ListPopupWindow popup = new ListPopupWindow(adapter.getContext());

		popup.setAdapter(adapter);
		if (anchorView != null)
			popup.setAnchorView(anchorView);
		popup.setDropDownGravity(Gravity.CENTER);
		popup.setWidth(width);
		popup.setModal(true);

		if (anchorView != null) {
			anchorView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					popup.show();
					ListView list = popup.getListView();
					list.setItemsCanFocus(true);
					list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
					/*
					 * list.setDivider(null);
					 * 
					 * list.setFastScrollEnabled(false);
					 * list.setSmoothScrollbarEnabled(true);
					 * list.setItemsCanFocus(true);
					 * list.setTranscriptMode(ListView.
					 * TRANSCRIPT_MODE_ALWAYS_SCROLL);
					 */
				}
			});
		} else {
			popup.show();
		}

		popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				clb.OnItemSelect(anchorView, adapter.getItem(arg2));
				popup.dismiss();

			}

		});
		popup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

			}

		});

	}

	private static final String LAST_APP_VERSION = "last_app_version";

	@Override
	public AppStart checkAppStart() {

		PackageInfo pInfo;
		Context ctx = (Context) getContext();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		AppStart appStart = AppStart.NORMAL;
		try {
			pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
			int lastVersionCode = sharedPreferences.getInt(LAST_APP_VERSION, -1);
			int currentVersionCode = pInfo.versionCode;
			appStart = checkAppStart(currentVersionCode, lastVersionCode);
			// Update version in preferences
			sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).commit();
		} catch (NameNotFoundException e) {
			appStart = AppStart.FIRST_TIME;
		}
		return appStart;
	}

	private AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {

		if (lastVersionCode == -1) {
			return AppStart.FIRST_TIME;
		} else if (lastVersionCode != currentVersionCode) {
			return AppStart.FIRST_TIME_VERSION;
		} else {
			return AppStart.NORMAL;
		}
	}

	@Override
	public void restart() {
		close();
		super.restart();
		callback.restart();
	}

	@Override
	public void setLocale(Locale locale) {

		Configuration overrideConfiguration = resources.getConfiguration();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			overrideConfiguration.setLocale(locale);
		} else {
			overrideConfiguration.locale = locale;
		}

		if (Build.VERSION.SDK_INT >= 24) {
			context.getApplicationContext().createConfigurationContext(overrideConfiguration);
		} else {
			resources.updateConfiguration(overrideConfiguration, null);
		}
	}

	@Override
	public IDeviceInfo getDeviceInfo() {

		DeviceInfo device = new DeviceInfo();
		device.BRAND = Build.BRAND;
		device.MODEL = Build.MODEL;
		device.PRODUCT = Build.PRODUCT;

		return device;
	}

	@Override
	public void runAsync(IForm form, String message, String title, Callback rPre, Callback rDo, Callback rPost) {
		new LoadData2((Activity) form, message, title, rPre, rDo, rPost).execute();
	}

	@Override
	public void runAsyncOdf(String message, String title, Callback rPre, Callback rDo, Callback rPost) {
		new LoadData2(null, message, title, rPre, rDo, rPost).execute();
	}

	private void Screen() {

		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();

		manager.getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;

		Configuration config = context.getResources().getConfiguration();
		float font = config.fontScale;
		int screenSize = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

		String s = "";
		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			s = "Large screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			s = "Normal screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			s = "Small screen";
			break;
		default:
			s = "Screen size is neither large, normal or small";

		}

		int density = context.getResources().getDisplayMetrics().densityDpi;
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			s = "LDPI";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			s = "MDPI";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			s = "HDPI";
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			s = "XHDPI";
			break;
		}

	}

	@Override
	public boolean checkInternetConnection() {

		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null) {
			if(activeNetwork.isConnected()) return true;
			if(activeNetwork.isConnectedOrConnecting()){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				activeNetwork = connectivityManager.getActiveNetworkInfo();
				return activeNetwork.isConnected();
			}
		}
		
		NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}


}
