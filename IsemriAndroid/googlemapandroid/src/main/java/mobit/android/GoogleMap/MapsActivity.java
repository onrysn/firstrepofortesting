package mobit.android.GoogleMap;

import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDef;
import com.mobit.IDetail;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.IIslemMaster;
import com.mobit.ILocation;
import com.mobit.IMap;
import com.mobit.IMapMarker;
import com.mobit.Item;
import com.mobit.MobitException;
import com.mobit.eleman;
import com.mobit.utility;
import mobit.android.LocationEx;

/*
 	OnMarkerClickListener,
    OnInfoWindowClickListener,
    OnMarkerDragListener,
    OnSeekBarChangeListener,
    OnInfoWindowLongClickListener,
    OnInfoWindowCloseListener,
    OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener
 */

public class MapsActivity extends AppCompatActivity implements IForm, OnMapReadyCallback, OnMapLoadedCallback,
		OnMapClickListener, OnMarkerClickListener, InfoWindowAdapter, OnInfoWindowClickListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

	private GoogleMap mMap = null;
	private MapsFragment mapFragment = null;
	private FragmentManager manager = null;
	private FragmentTransaction ft = null;

	private List<Polyline> routePolyline = new ArrayList<Polyline>();

	private Map<Marker, IMapMarker> markerList = new HashMap<Marker, IMapMarker>();

	private IApplication app;
	private IMap map = null;

	int retry = 0;

	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private Location mLastLocation;
	private Marker mCurrLocationMarker;

	private float density = 1.0f;

	private static final TimeZone timeZone = TimeZone.getTimeZone("UTC");
	private long UpToDateTime = 5 * 60 * 1000;

	private View mWindow;
	private View mContents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		if (!(Globals.app instanceof IMap)) {
			finish();
			return;
		}

		map = (IMap) Globals.app;

		if (Globals.app instanceof IApplication)
			app = (IApplication) Globals.app;

		if (app != null)
			app.initForm(this);
		
		density = getResources().getDisplayMetrics().density;


		//setContentView(R.layout.activity_maps);
		
		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setId(R.id.map1);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		setContentView(relativeLayout);
		
		if (manager == null)
			manager = getFragmentManager();
		
		mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
		mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);		

		Handler handler = new Handler();
		handler.postDelayed(loadFragment, 500);



	}

	Runnable loadFragment = new Runnable() {

		@Override
		public void run() {
			mapFragment = (MapsFragment) manager.findFragmentByTag("mapFragment"); // manager.findFragmentById(R.id.map);
			if (mapFragment == null) {

				/*
				 * GoogleMapOptions options = new
				 * GoogleMapOptions().liteMode(true)
				 * .mapType(GoogleMap.MAP_TYPE_SATELLITE);
				 */
				mapFragment = MapsFragment.newInstance(new GoogleMapOptions().zOrderOnTop(true));
				ft = manager.beginTransaction();
				ft.add(R.id.map1, mapFragment, "mapFragment");
				ft.commit();
			}
			// Handler handler = new Handler();
			// handler.postDelayed(loadMap, 500);
			mapFragment.getMapAsync(MapsActivity.this);
		}
	};

	Runnable loadMap = new Runnable() {

		@Override
		public void run() {

			mMap = mapFragment.getMap();

			if (mMap != null) {

				initMap();

			} else {
				if (retry < 3) {
					retry++;
					Handler handler = new Handler();
					handler.postDelayed(this, 500);
				}
			}
		}
	};

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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		mMap = googleMap;
		initMap();
	}

	private void initMap() {
		// mMap.addMarker(new
		// MarkerOptions().position(latLng)).setVisible(true);

		// mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f )
		// );
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
		// 17));
		// handler.removeCallbacksAndMessages(null);

		mMap.setOnMapLoadedCallback(MapsActivity.this);
		mMap.setOnMapClickListener(MapsActivity.this);
		mMap.setOnMarkerClickListener(MapsActivity.this);
		mMap.setInfoWindowAdapter(MapsActivity.this);
		mMap.setOnInfoWindowClickListener(MapsActivity.this);


		if (checkLocationPermission())
			buildGoogleApiClient();
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		fillMarkers();
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		MarkerIslemSecimi(arg0);
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		// app.ShowMessage(this, "getInfoContents", "");
		render(arg0, mContents);
		return mContents;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		// app.ShowMessage(this, "getInfoWindow", "");
		render(arg0, mWindow);
		return mWindow;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		// app.ShowMessage(this, "onMarkerClick", "");
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		// app.ShowMessage(this, "onMapClick", "");
		IslemSecimi();
		return;
	}

	private float getHue(int color) {
		return getHue(Color.red(color), Color.green(color), Color.blue(color));
	}

	private float getHue(int red, int green, int blue) {

		float hue = 0f;
		// Y?ntem 1
		/*
		float min = Math.min(Math.min(red, green), blue);
		float max = Math.max(Math.max(red, green), blue);

		if (min == max) {
			return 0;
		}
		
		if (max == red) {
			hue = (green - blue) / (max - min);

		} else if (max == green) {
			hue = 2f + (blue - red) / (max - min);

		} else {
			hue = 4f + (red - green) / (max - min);
		}

		hue = hue * 60;
		*/
		// Y?ntem 2
		float[] hsv = new float[3];
		Color.RGBToHSV(red, green, blue, hsv);
		hue = hsv[0];
		
		if (hue < 0) hue += 360;
		return hue;

		// return Math.round(hue);
		/*
		 * public static final float HUE_RED = 0.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_ORANGE =
		 * 30.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_YELLOW =
		 * 60.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_GREEN =
		 * 120.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_CYAN =
		 * 180.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_AZURE =
		 * 210.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_BLUE =
		 * 240.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_VIOLET =
		 * 270.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_MAGENTA =
		 * 300.0f;
		 * 
		 * // Field descriptor #67 F public static final float HUE_ROSE =
		 * 330.0f;
		 */
		// return BitmapDescriptorFactory.HUE_CYAN + 15.145f;
	}

	private BitmapDescriptor getMarkerIcon(int color) {
		// float[] hsv = new float[3];
		// Color.colorToHSV(color, hsv);
		float hue = getHue(color);
		// hue = BitmapDescriptorFactory.HUE_YELLOW;
		return BitmapDescriptorFactory.defaultMarker(hue);
	}

	private void fillMarkers() {

		Marker marker;

		List<IMapMarker> markers = map.getMarkerList();
		try {
			for (IMapMarker m : markers) {
				ILocation location = m.getLocation();
				if (location == null || !location.isValid())
					continue;
				MarkerOptions markerOption = new MarkerOptions();
				markerOption.position(new LatLng(location.getLatitude(), location.getLongitude()));
				markerOption.icon(getMarkerIcon(m.getColor()));
				marker = mMap.addMarker(markerOption);
				marker.setVisible(true);
				marker.setTitle(m.getTitle());
				marker.setSnippet(m.getDetail());
				marker.showInfoWindow();
				markerList.put(marker, m);
			}

			fitMarkerToScreen(markerList.keySet());
		}
		catch(Exception e){
			app.ShowException(this, e);
		}

	}

	String s;
	/*private void fillMarkers() {

		Marker marker;

		List<IMapMarker> markers = map.getMarkerList();
		try {
			for (IMapMarker m : markers) {
				ILocation location = m.getLocation();
				if (location == null || !location.isValid())
					continue;
				MarkerOptions markerOption = new MarkerOptions();
				markerOption.position(new LatLng(location.getLatitude(), location.getLongitude()));
				markerOption.icon(getMarkerIcon(m.getColor()));
				marker = mMap.addMarker(markerOption);
				marker.setVisible(true);
				marker.setTitle(m.getTitle());
				marker.setSnippet(m.getDetail());
				marker.showInfoWindow();
				markerList.put(marker, m);
			}

			fitMarkerToScreen(markerList.keySet());
		}
		catch(Exception e){
			app.ShowException(this, e);
		}

	}*/

	private void fitToScreen(List<LatLng> itemList) {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();

		try {

			Location location = getLocation();
			if (location != null && (location.getLatitude() != 0 || location.getLongitude() != 0))
				builder.include(new LatLng(location.getLatitude(), location.getLongitude()));

		} catch (MobitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.ShowException(this, e);
		}

		for (LatLng item : itemList)
			builder.include(item);

		LatLngBounds bounds = builder.build();

		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		int padding = (int) (Math.max(width, height) * 0.10);

		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

		mMap.animateCamera(cu);
	}

	private void fitMarkerToScreen(Set<Marker> markerList) {
		List<LatLng> list = new ArrayList<LatLng>();
		for (Marker m : markerList)
			list.add(m.getPosition());
		fitToScreen(list);
	}

	private static LatLng toLatLng(Location location) {
		return new LatLng(location.getLatitude(), location.getLongitude());
	}

	// -------------------------------------------------------------------------

	private static final String BulundugumKonumaGit = "Bulundugum Konuma Git";
	private static final String TumKonumlariGoster = "Tum Konumlari Goster";

	private void IslemSecimi() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Islem");
		final List<String> slist = new ArrayList<String>();
		slist.add(BulundugumKonumaGit);
		slist.add(TumKonumlariGoster);

		builder.setSingleChoiceItems((String[]) slist.toArray(new String[0]), -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						dialog.dismiss();
						String s = slist.get(item);
						if (s.equals(BulundugumKonumaGit)) {

							try {

								mMap.moveCamera(CameraUpdateFactory.newLatLng(toLatLng(getLocation())));
								mMap.animateCamera(CameraUpdateFactory.zoomTo(15));




							} catch (MobitException e) {
								// TODO Auto-generated catch block
								app.ShowException(MapsActivity.this, e);
							}

						} else if (s.equals(TumKonumlariGoster)) {

							fitMarkerToScreen(markerList.keySet());
						}
					}
				});
		AlertDialog dlg = builder.create();
		dlg.show();

	}

	// -------------------------------------------------------------------------

	private static final String Sec = "Git";
	private static final String Detay = "Detay";
	private static final String YolTarif = "Yol Tarifi";
	private static final String OkumaYap = "Okuma Yap Ekranına Git";

	private IMapMarker get(Marker marker) {
		return markerList.get(marker);
	}

	private void MarkerIslemSecimi(final Marker marker) {

		final IMapMarker m = get(marker);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Islem Sec");
		final List<String> slist = new ArrayList<String>();
		if (m != null) {
			slist.add(Sec);
			if (m instanceof IDetail)
				slist.add(Detay);
				slist.add(YolTarif);
				slist.add(OkumaYap);
		} else {
			slist.add(Detay);
		}

		builder.setSingleChoiceItems((String[]) slist.toArray(new String[0]), -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						dialog.dismiss();
						String s = slist.get(item);
						if (s.equals(Sec)) {
							List<IMapMarker> list = new ArrayList<IMapMarker>();
							list.add(m);
							map.setSelectedMarkerList(list);
							setResult(RESULT_OK);
							finish();
						} else if (s.equals(Detay)) {
							Detay(m);
						} else if (s.equals(YolTarif)) {
							//YolTarifi(marker);
							// Create a Uri from an intent string. Use the result to create an Intent.
							Uri gmmIntentUri = Uri.parse("google.navigation:q="+marker.getPosition().latitude+","+marker.getPosition().longitude);

							//Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+marker.getPosition());

							// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
							Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
							// Make the Intent explicit by setting the Google Maps package
							mapIntent.setPackage("com.google.android.apps.maps");

							// Attempt to start an activity that can handle the Intent
							startActivity(mapIntent);
						}
						else if(s.equals(OkumaYap))
						{
							Intent intent = new Intent(MapsActivity.this ,app.getFormClass(IDef.FORM_OKUMA_ENDEKS));
							//Onur haritalardan okuma yap ekranına tesisat no almak için eklendi
							intent.putExtra("SAHA_ISEMRI_NO", m.getSAHA_ISEMRI_NO());
							startActivityForResult(intent, IDef.FORM_OKUMA_ENDEKS);
						}
					}
				});
		AlertDialog dlg = builder.create();
		dlg.show();

	}

	private void Detay(IMapMarker m) {
		if (app == null)
			return;
		if (m != null)
			app.setObject(IDef.OBJ_DETAY, m);
		else
			app.setObject(IDef.OBJ_DETAY, new LocationEx(mLastLocation));
		Intent intent = new Intent(this, app.getFormClass(IDef.FORM_DETAY));
		startActivityForResult(intent, IDef.FORM_DETAY);

	}

	// -------------------------------------------------------------------------

	private Location getLocation() throws MobitException {
		Calendar calendar = Calendar.getInstance(timeZone);
		if (mLastLocation != null) {
			long millis = calendar.getTimeInMillis() - mLastLocation.getTime();
			if (millis < UpToDateTime)
				return mLastLocation;

		}

		if (!Globals.isDeveloping())
			throw new MobitException("Guncel koordinat alinamadi!");

		return mLastLocation;

	}

	private void YolTarifi(Marker m) {
		try {

			Location location = getLocation();

			LatLng origin, dest;
			if (!Globals.isDeveloping()) {
				origin = new LatLng(location.getLatitude(), location.getLongitude());
				dest = m.getPosition();
			} else {
				origin = new LatLng(37.9798920, 32.5211360);
				dest = new LatLng(37.8052910, 32.1461250);
			}

			YolTarifi(origin, dest);

		} catch (Exception e) {
			app.ShowException(MapsActivity.this, e);
		}
	}

	private void YolTarifi(LatLng origin, LatLng dest) {

		String url = getUrl(origin, dest);
		FetchUrl fetchUrl = new FetchUrl();
		fetchUrl.execute(url);
	}

	private String getUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		return url;
	}

	/**
	 * A method to download json data from url
	 */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		BufferedReader br = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

		} finally {
			if (br != null)
				br.close();
			if (iStream != null)
				iStream.close();
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class FetchUrl extends AsyncTask<String, Void, Object> {

		private ProgressDialog progressDialog = null;

		@Override
		protected void finalize() throws Throwable {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			super.finalize();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MapsActivity.this);
			if (progressDialog != null) {
				progressDialog.setTitle("");
				progressDialog.setMessage("Yol tarifi aliniyor. Lutfen bekleyin...");
				progressDialog.show();
			}
		}

		@Override
		protected Object doInBackground(String... url) {

			String data = "";

			try {

				data = downloadUrl(url[0]);

			} catch (Exception e) {
				return e;
			}
			return data;
		}

		@Override
		protected void onPostExecute(Object _result) {
			super.onPostExecute(_result);

			try {

				if (_result instanceof Exception)
					throw (Exception) _result;

				String result = (String) _result;

				if (false || (result != null && !result.isEmpty())) {
					ParserTask parserTask = new ParserTask(routePolyline);
					parserTask.execute(result);
				} else {
					throw new MobitException("Yol tarifi al?namad?!");
				}
			} catch (Exception e) {
				app.ShowException(MapsActivity.this, e);
			} finally {
				if (progressDialog != null)
					progressDialog.dismiss();
			}
		}
	}

	/**
	 * A class to parse the Google Places in JSON format
	 */
	private class ParserTask extends AsyncTask<String, Integer, Object> {

		private ProgressDialog progressDialog = null;
		List<Polyline> polyline;

		public ParserTask(List<Polyline> polyline)
		{
			super();
			this.polyline = polyline;
		}
		
		@Override
		protected void finalize() throws Throwable {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			super.finalize();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(MapsActivity.this);
			if (progressDialog != null) {
				progressDialog.setTitle("");
				progressDialog.setMessage("Yol tarifi ciziliyor. Lutfen bekleyin...");
				progressDialog.show();
			}
		}

		// Parsing the data in non-ui thread
		@Override
		protected Object doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {

				// jsonData[0] = "{ \"error_message\" : \"You have exceeded your
				// daily request quota for this API. We recommend registering
				// for a key at the Google Developers Console:
				// https://console.developers.google.com/apis/credentials?project=_\",
				// \"routes\" : [], \"status\" : \"OVER_QUERY_LIMIT\"}";

				jObject = new JSONObject(jsonData[0]);

				// Check for error
				if (jObject.has("error_message"))
					jObject.getJSONObject("error_message");

				DataParser parser = new DataParser();
				routes = parser.parse(jObject);

			} catch (Exception e) {
				e.printStackTrace();
				app.ShowException(MapsActivity.this, e);
				return e;
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(Object _result) {

			try {

				if (_result instanceof Exception)
					throw (Exception) _result;

				@SuppressWarnings("unchecked")
				List<List<HashMap<String, String>>> result = (List<List<HashMap<String, String>>>) _result;

				
				// Traversing through all the routes
				if (result != null) {

					if(polyline != null){
						for(Polyline p : polyline) p.remove();
						polyline.clear();
					}
					
					ArrayList<PolylineOptions> optionList = new ArrayList<PolylineOptions>();
					ArrayList<LatLng> points = new ArrayList<LatLng>();

					for (int i = 0; i < result.size(); i++) {

						ArrayList<LatLng> pts = new ArrayList<LatLng>();

						// Fetching i-th route
						List<HashMap<String, String>> path = result.get(i);
						if (path == null)
							continue;

						// Fetching all the points in i-th route
						for (int j = 0; j < path.size(); j++) {
							HashMap<String, String> point = path.get(j);
							if (point == null)
								continue;
							double lat = Double.parseDouble(point.get("lat"));
							double lng = Double.parseDouble(point.get("lng"));
							LatLng position = new LatLng(lat, lng);

							pts.add(position);
							
						}
						
						points.addAll(pts);

						// Adding all the points in the route to LineOptions
						PolylineOptions lineOptions = new PolylineOptions();
						lineOptions.addAll(pts);
						lineOptions.width(density * 2);
						lineOptions.color(Color.RED);
						optionList.add(lineOptions);

					}

					// Drawing polyline in the Google Map for the i-th route
					for (PolylineOptions lineOptions : optionList) {
						Polyline p = mMap.addPolyline(lineOptions);
						if(polyline != null) polyline.add(p);
					}
					fitToScreen(points);
				
				}

			} catch (Exception e) {

				app.ShowException(MapsActivity.this, e);

			}
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}

	protected synchronized void buildGoogleApiClient() {

		if (mGoogleApiClient != null)
			return;

		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(Bundle bundle) {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			mMap.setMyLocationEnabled(true);
		}

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onLocationChanged(Location location) {

		mLastLocation = location;
		if (mCurrLocationMarker != null) {
			mCurrLocationMarker.remove();
			mCurrLocationMarker = null;
		}

		// Place current location marker

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(toLatLng(location));
		markerOptions.title("Bulundugunuz Konum");
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		mCurrLocationMarker = mMap.addMarker(markerOptions);

		// move map camera
		// mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		// mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

		// stop location updates
		if (mGoogleApiClient != null) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

	private boolean checkLocationPermission() {

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			// Asking user if explanation is needed
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

				// Show an explanation to the user *asynchronously* -- don't
				// block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

				// Prompt the user once explanation has been shown
				ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
						MY_PERMISSIONS_REQUEST_LOCATION);

			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
						MY_PERMISSIONS_REQUEST_LOCATION);
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
		case MY_PERMISSIONS_REQUEST_LOCATION: {
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				// permission was granted. Do the
				// contacts-related task you need to do.
				if (ContextCompat.checkSelfPermission(this,
						Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

					buildGoogleApiClient();

				}

			} else {

				// Permission denied, Disable the functionality that depends on
				// this permission.
				Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
			}
			return;
		}

		// other 'case' lines to check for other permissions this app might
		// request.
		// You can add here other case statements according to your requirement.
		}
	}

	private void render(Marker marker, View view) {
		int badge;
		// Use the equals() method on a Marker to check for equals. Do not use
		// ==.

		((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.arrow);

		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			// Spannable string allows us to edit the formatting of the text.
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
			titleUi.setText(titleText);
		} else {
			titleUi.setText("");
		}

		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null && snippet.length() > 12) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
			snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText(snippet);
		}
	}

}
