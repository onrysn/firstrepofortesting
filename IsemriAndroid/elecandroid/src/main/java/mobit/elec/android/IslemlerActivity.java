package mobit.elec.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.mobit.Globals;
import com.mobit.IForm;
import mobit.elec.IElecApplication;

public class IslemlerActivity extends AppCompatActivity implements IForm {

	private IElecApplication app;
	
	ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_islemler);
		
		if (!(Globals.app instanceof IElecApplication)) {
			finish();
			return;
		}
		app = (IElecApplication) Globals.app;

		app.initForm(this);

		list = (ListView) findViewById(R.id.list);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
	}

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
		if (id == android.R.id.home)
			onBackPressed();
		
		return super.onOptionsItemSelected(item);
	}
}
