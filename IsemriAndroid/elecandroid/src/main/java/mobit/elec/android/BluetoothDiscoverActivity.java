package mobit.elec.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDialogCallback;
import com.mobit.IForm;

public class BluetoothDiscoverActivity extends AppCompatActivity implements IForm, OnClickListener {

	private BluetoothAdapter adapter = null;

	private TextView textDurum;
	private ListView listBluetooth;
	private Button buttonArama;
	private Button buttonSec;

	private List<BluetoothDeviceEx> deviceList;
	private ArrayAdapter<BluetoothDeviceEx> arrayAdapter;
	private BluetoothDeviceEx device = null;
	
	public static String CAPTION = "CAPTION";
	public static String DEVICE = "DEVICE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_discover);

		IApplication app = com.mobit.Globals.app;
		app.initForm(this);
		
		ActionBar actionBar = getSupportActionBar();
		
		textDurum = (TextView) findViewById(R.id.textDurum);
		listBluetooth = (ListView) findViewById(R.id.listBluetooth);
		buttonArama = (Button) findViewById(R.id.buttonArama);
		buttonArama.setOnClickListener(this);
		buttonSec = (Button) findViewById(R.id.buttonSec);
		buttonSec.setOnClickListener(this);

		Intent intent = getIntent();
		String caption = intent.getStringExtra(CAPTION);
		if (caption != null)
			actionBar.setTitle(caption);

		deviceList = new ArrayList<BluetoothDeviceEx>();
		arrayAdapter = new DeviceArrayAdapter(this, deviceList);
		listBluetooth.setAdapter(arrayAdapter);

		adapter = BluetoothAdapter.getDefaultAdapter();

		if (adapter.isDiscovering())
			adapter.cancelDiscovery();

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		registerReceiver(mReceiver, filter);

		buttonArama.performClick();
	}

	@Override
	protected void onDestroy() {

		if (adapter.isDiscovering())
			adapter.cancelDiscovery();
		unregisterReceiver(mReceiver);
		super.onDestroy();
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
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		if (adapter.isDiscovering()) {
			Globals.platform.ShowMessage(this, "Arama devam ediyor. İşlemi iptal etmek istiyor musunuz", "Uyarı",
					DialogMode.YesNo, 1, 0, new IDialogCallback() {
						public void DialogResponse(int msg_id, DialogResult result) {
							if (result == DialogResult.Yes) {
								setResult(Activity.RESULT_CANCELED, new Intent());
								finish();
							}
						}
					});
		} else {
			setResult(Activity.RESULT_CANCELED, new Intent());
			super.onBackPressed();
		}
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

				deviceList.clear();
				arrayAdapter.notifyDataSetChanged();
				textDurum.setText("Arama başladı...");

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

				textDurum.setText("Arama tamamlandı");

			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				textDurum.setText("Arama devam ediyor. Lütfen bekleyin...");

				BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				BluetoothClass cls = device.getBluetoothClass();

				switch (cls.getMajorDeviceClass()) {
				case BluetoothClass.Device.Major.UNCATEGORIZED:
				case 1536:

					// device.getBluetoothClass().getMajorDeviceClass()
					deviceList.add(new BluetoothDeviceEx(device));
					arrayAdapter.notifyDataSetChanged();
					break;
				}

			}
		}

	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		if (id == R.id.buttonArama) {
			if (!adapter.isDiscovering())
				adapter.startDiscovery();
		} else if (id == R.id.buttonSec) {

			
			for (int i = 0; i < deviceList.size(); i++) {
				if (deviceList.get(i).getCheck()) {
					device = deviceList.get(i);
					break;
				}
			}
			if (device == null) {
				Globals.platform.ShowMessage(this, "Listeden seçim yapın!", "Uyarı");
				return;
			}
			
			if(checkPair()){
				Intent intent = new Intent();
				intent.putExtra(DEVICE, device.Device.getAddress());
				setResult(Activity.RESULT_OK, intent);
				finish();
				return;
			}
				
			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			startActivityForResult(intent, 1);
			
		}
	}
	
	boolean checkPair()
	{
		boolean paired = false;
		Set<BluetoothDevice> mPairedDevices = adapter.getBondedDevices();
		if (mPairedDevices.size() > 0) {
			for (BluetoothDevice mDevice : mPairedDevices) {
				if (mDevice.getAddress().equalsIgnoreCase(device.Device.getAddress())) {
					paired = true;
					break;
				}
			}
		}
		return paired;
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
       
		if(requestCode == 1){
			
			
			if(!checkPair()){
				Globals.app.ShowMessage(this, "Cihaz eşleştirilemedi!", "Uyarı");
				return;
			}
			
			Intent intent = new Intent();
			intent.putExtra(DEVICE, device.Device.getAddress());
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
    }
	

	// -------------------------------------------------------------------------

	private class BluetoothDeviceEx {

		public BluetoothDevice Device;
		boolean checked = false;

		public BluetoothDeviceEx(BluetoothDevice Device) {
			this.Device = Device;
		}

		@Override
		public String toString() {
			return String.format("%s(%s)", Device.getAddress(), (Device.getName() != null) ? Device.getName() : "");
		}

		public void setCheck(boolean checked) {
			this.checked = checked;
		}

		public boolean getCheck() {
			return checked;
		}
	}

	private class DeviceViewHolder {

		private RadioButton radioButton;
		private TextView textView;

		public DeviceViewHolder(TextView textView, RadioButton radioButton) {
			this.radioButton = radioButton;
			this.textView = textView;
		}

		public RadioButton getRadioButton() {
			return radioButton;
		}

		public TextView getTextView() {
			return textView;
		}

	}

	private class DeviceArrayAdapter extends ArrayAdapter<BluetoothDeviceEx> {

		private LayoutInflater inflater;

		public DeviceArrayAdapter(Context context, List<BluetoothDeviceEx> list) {
			super(context, R.layout.row_with_radio, R.id.rowTextView, list);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			BluetoothDeviceEx device = (BluetoothDeviceEx) getItem(position);

			RadioButton radioButton;
			TextView textView;

			if (convertView == null) {

				convertView = inflater.inflate(R.layout.row_with_radio, null);

				textView = (TextView) convertView.findViewById(R.id.rowTextView);
				radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);

				convertView.setTag(new DeviceViewHolder(textView, radioButton));

				final DeviceArrayAdapter _this = this;

				radioButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						for (int position = 0; position < _this.getCount(); position++) {
							if (_this.getItem(position).getCheck()) {
								_this.getItem(position).setCheck(false);
								_this.notifyDataSetChanged();
								break;
							}
						}

						RadioButton rb = (RadioButton) v;
						BluetoothDeviceEx device = (BluetoothDeviceEx) rb.getTag();
						device.setCheck(true);

					}
				});

			} else {

				DeviceViewHolder viewHolder = (DeviceViewHolder) convertView.getTag();
				radioButton = viewHolder.getRadioButton();
				textView = viewHolder.getTextView();
			}

			radioButton.setTag(device);

			radioButton.setChecked(device.getCheck());
			textView.setText(device.toString());

			return convertView;
		}

	}

}