package mobit.elec.android;

import android.os.Bundle;
import com.mobit.Globals;
import com.mobit.IDef;
import mobit.android.DetayActivity;
import mobit.elec.IElecApplication;


public class IsemriRaporActivity extends DetayActivity {

	IElecApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if(!(Globals.app instanceof IElecApplication)){
			finish();
			return;
		}
		
		app = (IElecApplication)Globals.app;
		
		try {
			app.setObject(IDef.OBJ_DETAY, app.getIsemriRapor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.ShowException(this, e);
			finish();
		}
		
		super.onCreate(savedInstanceState);
		
	}
}
