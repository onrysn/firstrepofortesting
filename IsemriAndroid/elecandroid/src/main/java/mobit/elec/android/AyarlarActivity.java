package mobit.elec.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.mobit.IApplication;
import com.mobit.IForm;
import mobit.android.AndroidPlatform;

public class AyarlarActivity extends AppCompatActivity implements IForm {

	SharedPreferences preferances;
	public static Context contextOfApplication;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayarlar);
		contextOfApplication = getApplicationContext();
		IApplication app = com.mobit.Globals.app;
		app.initForm(this);
		
		getFragmentManager().beginTransaction()
        	.replace(android.R.id.content, new SettingsFragment())
        	.commit();
        
		preferances = PreferenceManager.getDefaultSharedPreferences(this);
		@SuppressWarnings("unused")
		String s = null;
		s = preferances.getString("host", "");
		s = preferances.getString("port", "");
		s = preferances.getString("apn", "");
		s = preferances.getString("yazici_model", "");
		s = preferances.getString("yazici_mac", "");
		s = preferances.getString("port_tip", "");
		s = preferances.getString("optik_mac", "");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayarlar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
	//muhammed gökkaya Gps için
	public static Context getContextOfApplication(){
		return contextOfApplication;
	}
}

class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.pref_settings);
 
        // show the current value in the settings screen
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            pickPreferenceObject(getPreferenceScreen().getPreference(i));
        }
        
        
    }
 
    private void pickPreferenceObject(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory cat = (PreferenceCategory) p;
            for (int i = 0; i < cat.getPreferenceCount(); i++) {
                pickPreferenceObject(cat.getPreference(i));
            }
        } else {
            initSummary(p);
        }
    }
 
    private void initSummary(Preference p) {
 
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
 
        // More logic for ListPreference, etc...
    }
}
