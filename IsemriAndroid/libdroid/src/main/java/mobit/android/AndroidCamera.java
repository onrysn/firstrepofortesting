package mobit.android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import com.mobit.Globals;
import com.mobit.ICamera;
import com.mobit.IForm;
import com.mobit.IFotograf;

public class AndroidCamera extends ICamera {

	
	Activity activity = null;
	
	private int sessionId = System.identityHashCode(this);
	private static SparseArray<AndroidCamera> cameraList = new SparseArray<AndroidCamera>();
	
	public static void addCamera(int sessionId, AndroidCamera camera) {
		cameraList.put(sessionId, camera);
	}

	public static void removeCamera(Integer sessionId) {
		cameraList.remove(sessionId);
	}

	public static AndroidCamera getCamera(Integer sessionId) {
		return cameraList.get(sessionId);
	}
	
	public AndroidCamera()
	{
		addCamera(sessionId, this);
		setPath(new File(Globals.platform.getExternalStorageDirectory()));
	}
	
	public AndroidCamera(IForm form)
	{
		this();
		activity = (Activity)form;
	}
	
	
	
	@Override
	protected void finalize() throws Throwable
	{
		removeCamera(sessionId);
		
		super.finalize();
	}
	
	@Override
	public void show(ICallback clb, IFotograf foto) throws Exception
	{
		this.clb = clb;
		if(activity == null) activity = AndroidPlatform.getActivity();
		Intent intent = new Intent(activity, CameraActivity.class);
		intent.putExtra("sessionId", sessionId);	
		activity.startActivityForResult(intent, 1);
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}
	public Activity getActivity()
	{
		return activity;
	}
}
