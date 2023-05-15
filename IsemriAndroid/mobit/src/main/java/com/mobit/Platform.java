package com.mobit;

import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.lang.Thread.sleep;

public abstract class Platform implements Closeable, IPlatform {

	//public static String LOG_FILE_NAME = IDef.LOG_FILE_NAME;
	
	private static TimeZone zone = TimeZone.getTimeZone("Turkey");
	
	private long realDate = 0;
	private long refDate = 0;

	Object resid = null;
	protected ICallback callback = null;
	protected ISettings settings = null;
	protected IApplication app = null;
	
	public Platform()
	{
		// Globals sınıfının static yapıcısının çağrılması için
		boolean init = Globals.init;
		realDate = refDate = Calendar.getInstance(zone).getTime().getTime();
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	@Override
	public void init(ICallback callback, Object resid) throws Exception {

		this.callback = callback;
		addMessage("Genel ayarlar yükleniyor...");
		//muhammed gökkaya açılırken kapanma sorunu için deneme
        for (;;) {
            try {

                    settings = (ISettings) getXmlResource(Settings.class, resid);
                    addMessage(settings.toString());

                    if (settings != null) {
                        break;
                    }

            }catch (Exception e){

            }
            sleep(1000);
        }
		addMessage("Genel ayarlar yüklendi.");
        sleep(1000);
	}
	@Override
	public IApplication createApplication(String className) throws Exception
	{
		addMessage("Uygulama modülü yükleniyor...");
		//className = settings.getItem(Parameter.class, "firma").getStringValue();

		IApplication app = loadApplication(className);
		app.init(this, callback);
		addMessage("Uygulama modülü yüklendi.");
		return app;
	}

	@Override
	public void close() {

		Globals.platform = null;

		if (app != null){
			app.close();
			app = null;
		}
	}
	@Override
	public void setTime(Date date) {

		Globals.setTime(date);
	}

	@Override
	public Date getTime() {

		return Globals.getTime();
	}

	@Override
	public String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						//boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						boolean isIPv4 = sAddr.indexOf(':') < 0;

						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
								return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
							}
						}
					}
				}
			}
		} catch (Exception ignored) { } // for now eat exceptions
		return "";
	}
	
	
	public ISettings getSettings() {
		return settings;
	}
	public IApplication getApp() {
		return app;
	}
	
	
	public void addMessage(final String msg) {
		callback.addMessage(msg);
	}
	//muhammed gökkaya log
	@Override
	public void Log(Throwable e) {

		//utility.Log(new File(getExternalStorageDirectory(), LOG_FILE_NAME), e);
	}
	
	@Override
	public void Log(byte [] b) {
		//utility.Log(new File(getExternalStorageDirectory(), LOG_FILE_NAME), b);
	}
	
	@Override
	public void Log(String s) {
		s = s + "\r\n";
		//Log(s.getBytes());
	}
	
	@Override
	public void DebugLog(String s) {

		if(!Globals.isDeveloping()) return;
		//Log(s);
	}
	
	public IApplication loadApplication(String appName) throws Exception {
		return (IApplication) ModLoader.load(appName);
	}
	
	public void restart()
	{
		Globals.platform = null;
	}
	
	
	@Override
	public void ShowException(IForm form, Throwable e, 
			DialogMode mode, final int msg_id, final IDialogCallback clb){

		utility.ExceptionInfo info = utility.FilterException(e);
		ShowMessage(form, info.msg, info.caption, mode, msg_id, 0, clb);

	}
	@Override
	public void ShowException(IForm form, Throwable e) {

		ShowException(form, e, DialogMode.Ok, 1, null);

	}
}
