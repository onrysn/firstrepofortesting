package mobit.elec.android;
 
 
 
import android.os.Bundle;
import com.mobit.Globals;
import com.mobit.IDetail;

import mobit.android.DetayActivity;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
 
public class IslemRaporActivity extends DetayActivity {
 
    private IElecApplication app;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         
        app = (IElecApplication)Globals.app;

        if(!(Globals.app instanceof IElecApplication)){
            finish();
            return;
        }

        try {
            app.setObject(IDef.OBJ_DETAY, app.getObject(IDef.OBJ_ISLEM_RAPOR));
             
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        super.onCreate(savedInstanceState);
         
         
    }

    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(false);
        app.setPortCallback(null);
        super.onResume();

    }
     
}