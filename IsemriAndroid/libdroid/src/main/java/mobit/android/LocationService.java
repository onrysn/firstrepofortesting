package mobit.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.mobit.Globals;
import com.mobit.MobitException;
import static android.os.SystemClock.sleep;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import mobit.eemr.Lun_Control;

public class LocationService extends Service {

	private static final String TAG = "LocationService";
	private LocationManager mLocationManager = null;
	private static final int LOCATION_INTERVAL = 1000;
	private static final float LOCATION_DISTANCE = 0;//10f;

	private volatile Location mLastLocation = null;
	public static LocationService sLocationService;

	private Object sync = new Object();
	public LocationService() throws Exception {
		super();
		if (sLocationService != null) {//23.07.2020 Muhammed Gökkaya
			//sLocationService = this;
			//sLocationService = null;
			throw new MobitException(String.format("Sadece bir tane %s oluşturulabilir!", TAG));
		}
		sLocationService = this;
	}
	/*
	public LocationService() throws Exception {
		super();
		//muhammed gökkaya location ex kapatıldı!

		if (sLocationService != null)

			sLocationService=null;
			//throw new MobitException(String.format("Sadece bir tane %s oluşturulabilir!", TAG));
		sLocationService = this;
	}
	*/
	protected void finalize() throws Throwable {
		sLocationService = null;
		super.finalize();
	}

	private class LocationListener implements android.location.LocationListener, Listener, NmeaListener {

		private volatile Location mLastLocation;
		public final String provider;

		public LocationListener(String provider) {
			this.provider = provider;
			mLastLocation = new Location(provider);
		}

		@Override
		public void onLocationChanged(Location location) {
			mLastLocation.set(location);
			LocationService.this.setLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onNmeaReceived(long arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGpsStatusChanged(int arg0) {
			// TODO Auto-generated method stub

		}

	}

	public Location getLocation() {
		
		synchronized (sync) {
			return mLastLocation;
		}
	}

	private void setLocation(Location location) {
		synchronized (sync) {
			//Location burada çalışıyor sürekli //
			mLastLocation = location;
			if (location.getAccuracy()<=40.0 && location.getAccuracy()>0.0) {
			    /*
				SharedPreferences prefs = getSharedPreferences("GPS", MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("Latitude", String.format(Globals.locale, "%.7f", location.getLatitude()));
				editor.putString("Longitude", String.format(Globals.locale, "%.7f", location.getLongitude()));
				editor.putString("Accuracy", String.format(Globals.locale, "%.1f", location.getAccuracy()));
				String pattern = "MM/dd/yyyy HH:mm:ss";
				DateFormat df = new SimpleDateFormat(pattern);
				Date today = Calendar.getInstance().getTime();
				String todayAsString = df.format(today);
				//System.out.println("Today is: " + todayAsString);
				editor.putString("Time", todayAsString);
				editor.commit();
			     */
                Lun_Control Gps =new Lun_Control();

                Gps.setLatitude((location.getLatitude()));
                Gps.setLongitude((location.getLongitude()));
                Gps.setAccuracy((location.getAccuracy()));
                String pattern = "MM/dd/yyyy HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
                Gps.setTime(todayAsString);
			}
		}
	}

	public LocationManager getLocationManager() {

		return mLocationManager;
	}

	private LocationListener[] mLocationListeners = new LocationListener[] {
			new LocationListener(LocationManager.GPS_PROVIDER), new LocationListener(LocationManager.PASSIVE_PROVIDER),
			new LocationListener(LocationManager.NETWORK_PROVIDER) };

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onCreate() {

		initializeLocationManager();

		for (int i = 0; i < mLocationListeners.length; i++) {

			try {
				
				mLocationManager.requestLocationUpdates(mLocationListeners[i].provider, 
						LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[i]);

			} catch (java.lang.SecurityException ex) {
				Log.i(TAG, "fail to request location update, ignore", ex);
			} catch (IllegalArgumentException ex) {
				Log.d(TAG, "network provider does not exist, " + ex.getMessage());
			}
		}

		/*
		 * try { mLocationManager.requestLocationUpdates(
		 * LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
		 * mLocationListeners[1] ); } catch (java.lang.SecurityException ex) {
		 * Log.i(TAG, "fail to request location update, ignore", ex); } catch
		 * (IllegalArgumentException ex) { Log.d(TAG,
		 * "gps provider does not exist " + ex.getMessage()); }
		 */
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
		if (mLocationManager != null) {
			for (int i = 0; i < mLocationListeners.length; i++) {
				try {
					if (ActivityCompat.checkSelfPermission(this,
							android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
							&& ActivityCompat.checkSelfPermission(this,
									android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						return;
					}
					mLocationManager.removeUpdates(mLocationListeners[i]);
				} catch (Exception ex) {
					Log.i(TAG, "fail to remove location listener, ignore", ex);
				}
			}
		}
	}

	private void initializeLocationManager() {
		
		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		}
		
	}
	
}
