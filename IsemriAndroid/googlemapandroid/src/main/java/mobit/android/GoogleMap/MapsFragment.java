package mobit.android.GoogleMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapsFragment extends MapFragment {

	private int retry = 0;
	private GoogleMap mMap = null;
	private OnMapReadyCallback callback = null;
	private GoogleMapOptions options = new GoogleMapOptions();

	public void getMapAsync(OnMapReadyCallback callback) {
		this.callback = callback;
	}

	public static MapsFragment newInstance(GoogleMapOptions options) {
		
		MapsFragment mapFragment = new  MapsFragment();
		mapFragment.options = options;
		return mapFragment;
	}

	public static MapsFragment newInstance() {
		return new MapsFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Handler handler = new Handler();
		handler.postDelayed(loadMap, 500);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
	}

	private static final CharSequence[] MAP_TYPE_ITEMS =
			{"Klasik Mod", "Arazi Mod"};

	Runnable loadMap = new Runnable() {

		@Override
		public void run() {

			mMap = getMap();

			if (mMap == null) {
				if(retry < 3){
					retry++;
					Handler handler = new Handler();
					handler.postDelayed(this, 500);
				}
			}
			else {
				showMapTypeSelectorDialog();
			}
		}
	};

	private void showMapTypeSelectorDialog() {
		// Prepare the dialog by setting up a Builder.
		final String fDialogTitle = "Harita Tipi Seçiniz";
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(fDialogTitle);

		// Find the current map type to pre-check the item representing the current state.
		int checkItem = mMap.getMapType() - 1;

		// Add an OnClickListener to the dialog, so that the selection will be handled.
		builder.setSingleChoiceItems(
				MAP_TYPE_ITEMS,
				checkItem,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int item) {
						// Locally create a finalised object.

						// Perform an action depending on which item was selected.
						switch (item) {
							case 1:
								mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
								break;
							default:
								mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

						}
						dialog.dismiss();
						retry = 0;
						if(callback != null) callback.onMapReady(mMap);
					}
				}
		);

		// Build the dialog and show it.
		AlertDialog fMapTypeDialog = builder.create();
		fMapTypeDialog.setCanceledOnTouchOutside(true);
		fMapTypeDialog.show();
	}

}
