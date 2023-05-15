package mobit.elec.android;

import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IForm;
import mobit.android.AndroidCamera;

public class FotografCekme implements Runnable {

	IForm form;
	public FotografCekme(IForm form, Callback clb){
		this.form = form;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AndroidCamera camera = new AndroidCamera(form);
		//Muhammed Gokkaya
		//Fotograf kisiti
		camera.setMaxPhotoNumber(3);
		camera.setMinPhotoNumber(0);

		try {
			camera.show(null, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Globals.app.ShowException(form, e);
		}
		

	}

}
