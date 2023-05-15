package mobit.elec.mbs.medas.android;

import java.util.List;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.mobit.FormClass;
import com.mobit.IBarcode;
import com.mobit.IForm;
import com.mobit.IPlatform;

import mobit.elec.IIsemri;
import mobit.elec.mbs.medas.MedasApplication;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;
import mobit.elec.osos.viko.OperationService;


public class AndroidModule extends MedasApplication {

	Application xapp;
	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-XXXXX-Y";
	
	
	public AndroidModule() throws Exception {
		super();
		// TODO Auto-generated constructor stub
		ososServer = new OperationService();

	
	}
	@Override
	public void init(IPlatform platform, Object obj) throws Exception {


		xapp = (Application)platform.getApplication();

		Intent intent;
		form = (IForm) obj;
		
		super.init(platform, obj);
		
		medasServer = new SayacZimmetBilgi();

		/*
		try {
			((mobit.elec.medas.ws.SayacZimmetBilgi) medasServer).kacak_kayit(1, getTime(), "Z", 5, 9613, 9613);
		}
		catch (Exception e){

			return;
		}*/
		final Activity activity = (Activity)form;

		/*
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				OsosServis os = new OsosServis();
				// "65000538", "MSY",
				os.OsosAktivasyon(AndroidModule.this, (IForm) activity, "80115487", "MSY", "", new Callback() {
					@Override
					public Object run(Object obj) {
						return null;
					}
				});
			}
		});
		*/

		/*
		intent = new Intent(activity, SendingListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
		if(true)return;
		*/




		/*
		intent = new Intent(activity, CameraBarcodeScanActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
		Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
		*/


		//createForm(this.getPlatform().getContext(), );
		intent = new Intent(activity, getFormClass(IDef.FORM_LOGIN));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
			Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivityForResult(intent, IDef.FORM_LOGIN);
		activity.finish();


		
	}

	@Override
	public FormClass[] getFormClassList() {
		
		return IDef.formClassList;
	}
	
	@Override
	public SayacZimmetBilgi newSayacZimmetBilgi()
	{
		return new SayacZimmetBilgi();
	}
	
	
	@Override
	public void requestPermission(IForm form)
	{
		
		Activity activity = (Activity)form;
		
		if(activity.isFinishing() || activity.isDestroyed()) return;
		
		ActivityCompat.requestPermissions(activity, 
				new String[]{
						Manifest.permission.INTERNET, 
						Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, 
						Manifest.permission.WRITE_APN_SETTINGS, 
						Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, 
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.EXPAND_STATUS_BAR, Manifest.permission.WAKE_LOCK,
						Manifest.permission.CAMERA},
				1);
	}
	
	@Override
	public void permissionGrant(int requestCode, String permissions[], int [] grantResults)
	{
		switch (requestCode) {
	    default:
	    	if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            	return;
            } else {	
                platform.exit();
            }
	    }
	
	}

	@Override
	public List<IIsemri> getKarneIsemri(Integer KARNE_NO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IIsemri> getTesisatIsemri(Integer TESISAT_NO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBarcode newBarcodeObject()
	{
		return new ZxingCameraBarcodeScanner();
	}
	

}
