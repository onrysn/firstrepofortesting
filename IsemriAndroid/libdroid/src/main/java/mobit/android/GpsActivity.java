package mobit.android;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDetail;
import com.mobit.IForm;
import com.mobit.ILocation;

import org.sqldroid.Log;


public class GpsActivity extends DetayActivity implements IForm, LocationListener, Listener, NmeaListener {

	private Timer timer;
	private LocationManager locationManager = null;
	Location l;
	String bestProvider;
	int coarse_izin=1001;
	int access_fine_izin=1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (!(Globals.app instanceof IApplication)) {
			finish();
			return;
		}

		app = (IApplication) Globals.app;

		detail = new LocationEx();

		super.onCreate(savedInstanceState);


		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			finish();
			return;
		}

		Criteria criteria = null;
		bestProvider = LocationManager.GPS_PROVIDER;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = locationManager.getBestProvider(criteria, false);
		if (!locationManager.isProviderEnabled(bestProvider)) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 1);
		} else {
			initManager();
		}

	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}

		locationManager.removeGpsStatusListener(this);
		locationManager.removeNmeaListener(this);
		locationManager.removeUpdates(this);

		super.onDestroy();
	}

	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {

		initManager();
	}

	private void initManager() {

		try {

			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(
						this,
						new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
						coarse_izin
				);
				return;
			}
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(
						this,
						new String [] { Manifest.permission.ACCESS_FINE_LOCATION },
						access_fine_izin
				);
				return;
			}
			locationManager.addGpsStatusListener(this);
			locationManager.addNmeaListener(this);
			locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
			l = locationManager.getLastKnownLocation(bestProvider);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		//---------------------------------------------------------------------
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ILocation location;
				try {
					location = Globals.platform.getLocation();
					updateList(location);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		}, 0, 1000);
		
		//---------------------------------------------------------------------
	}
	
	private void updateList(ILocation location)
	{
		if (location instanceof IDetail) {
			final IDetail detail = (IDetail) location;


			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					setTitle(detail.getAciklama());
					arrayList.clear();
					arrayList.addAll(detail.getDetay());
					arrayAdapter.notifyDataSetChanged();

				}
			});
		}
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		//updateList(new LocationEx(arg0));
		return;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void onNmeaReceived(long arg0, String arg1) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void onGpsStatusChanged(int arg0) {
		// TODO Auto-generated method stub
		return;
	}
	


}
