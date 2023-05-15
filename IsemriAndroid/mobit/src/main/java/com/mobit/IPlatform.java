package com.mobit;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.Locale;

import com.mobit.DialogMode;
import com.mobit.IApplication;
import com.mobit.IBluetooth;
import com.mobit.ICallback;
import com.mobit.IDialogCallback;
import com.mobit.ILocation;
import com.mobit.ISettings;
import com.mobit.XMLLoader;
import mobit.eemr.IMeterReader;

public interface IPlatform {

	public enum AppStart {
	    FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
	}
	
	public enum PlatformId {
		JRE, Android
	}
	
	PlatformId getId(); 
		
	void init(ICallback callback, Object resid) throws Exception;
	IApplication createApplication(String className) throws Exception;
	void close();
	void setTime(Date date);
	Date getTime();
	void exit();
	
	Object getApplication();
	Object getContext();
	IForm getActiveForm();
	
	ISettings getSettings();
	IApplication getApp();
	
	XMLLoader getXmlResource(Class<?> cls, Object id) throws Exception;
	String getTextResource(Object id) throws Exception;
	
	void ShowMessage(IForm form, String msg, String title, 
			DialogMode mode, final int msg_id, final int timeout, final IDialogCallback clb);
	void ShowMessage(IForm form, String msg, String title);
	
	void ShowException(IForm form, Throwable e, 
			DialogMode mode, final int msg_id, final IDialogCallback clb);
	void ShowException(IForm form, Throwable e);
	
	void addMessage(final String msg);
	
	IBluetooth createBluetooth();
	
	Connection createDbConnection(String dbFile, String user, String password) throws Exception;
	void deleteDatabase(String dbFile) throws Exception;
	String getExternalStorageDirectory();
	
	void Log(byte [] b);
	void Log(Throwable e);
	void Log(String s);
	void DebugLog(String s);
	
	IApplication loadApplication(String appName) throws Exception;
	
	ILocation getLocation() throws Exception;
	ILocation newLocationObject();
	
	ICamera createCamera();
	
	String getSettingValue(String key);
	void setSettingValue(String key, String value);
	
	void showSoftKeyboard(IForm obj, boolean show);

	String getVersion(URL verUrl) throws IOException;
	void updateApp(final URL appUrl) throws Exception;
	
	void checkAPN(String apn) throws Exception;
	void setAPN(IForm form, String apn);
	
	void onFormResult(int requestCode, int resultCode, Object data);
	
	void createForm(Object context, Class<?> cls);
	
	AppStart checkAppStart();
	
	void restart();
	
	void initForm(IForm form, FormInitParam param);
	
	IMeterReader createMeterReader() throws Exception;
	
	void setLocale(Locale locale);
	
	IDeviceInfo getDeviceInfo();
	
	void runAsync(IForm form, String message, String title, Callback rPre, Callback rDo, Callback rPost);

	void runAsyncOdf(String message, String title, Callback rPre, Callback rDo, Callback rPost);

	boolean checkInternetConnection();

	String getIPAddress(boolean useIPv4);
	
}
