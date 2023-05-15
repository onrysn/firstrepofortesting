package mobit.eemr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity implements OnClickListener {

	IMeterReader mr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		((Button) findViewById(R.id.button1)).setOnClickListener(this);

		try {
			mr = utility.create("mobit.eemr.MeterReaderCpp");
			mr.Connect("00:12:6F:26:08:81", "1234", null);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		if (mr != null)
			mr.close();
		System.exit(0);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.button1) {

			ReadResult result = new ReadResultEx(this);
			try {
				result.setReadMode(ReadMode2.PROGRAM_MOD);
				mr.read(result);
			} catch (Exception e) {

				return;
			}

		}
	}
}
