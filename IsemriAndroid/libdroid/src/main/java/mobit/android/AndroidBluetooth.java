
package mobit.android;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import com.mobit.Callback;
import com.mobit.IBluetooth;
import com.mobit.MobitException;

public class AndroidBluetooth extends IBluetooth {

	private Object sync = new Object();

	private UUID sdpid;
	private String BthAddr;
	@SuppressWarnings("unused")
	private String Pin;

	Application app;
	private BluetoothAdapter m_adapter;
	private BluetoothDevice m_device;
	@SuppressWarnings("unused")
	private BluetoothDevice m_rdevice;
	private BluetoothSocket m_sock;
	private boolean m_enabled;

	protected void finalize() {

		close();
		synchronized (sync) {
			sync.notifyAll();
		}
	}

	@Override
	public void close() {

		super.close();

		if (m_sock != null) {
			try {
				m_sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ShowException(e);
			}
			m_sock = null;
		}

		if (m_enabled == false)
			m_adapter.disable();

	}

	public AndroidBluetooth() {

		app = getApplication();
		m_adapter = BluetoothAdapter.getDefaultAdapter();
		m_enabled = m_adapter.isEnabled();
		if (m_enabled == false)
			m_adapter.enable();

		IntentFilter filter = new IntentFilter();
		//filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		//filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		app.registerReceiver(receiver, filter);

	}

	void finalizer() {

		app.unregisterReceiver(receiver);
	}

	Callback rr = null;

	public void Open(UUID sdpid, String BthAddr, String Pin, final Runnable r) throws Exception {

		if (m_sock != null && m_sock.isConnected()) {
			r.run();
			return;
		}

		this.sdpid = sdpid;
		this.BthAddr = BthAddr;
		this.Pin = Pin;

		close();

		if (m_adapter.isEnabled() == false)
			m_adapter.enable();
		if (m_adapter.isDiscovering())
			m_adapter.cancelDiscovery();

		boolean paired = false;
		Set<BluetoothDevice> mPairedDevices = m_adapter.getBondedDevices();
		if (mPairedDevices.size() > 0) {
			for (BluetoothDevice mDevice : mPairedDevices) {
				if (mDevice.getAddress().equalsIgnoreCase(BthAddr)) {
					m_device = mDevice;
					paired = true;
					break;
				}
			}
		}

		rr = new Callback() {
			@Override
			public Object run(Object obj) {
				// TODO Auto-generated method stub
				try {

					m_sock = m_device.createRfcommSocketToServiceRecord(AndroidBluetooth.this.sdpid);
					int state = m_device.getBondState();

					if (state == BluetoothDevice.BOND_NONE)
						throw new Exception("Bluetooth cihaza bağlantı sağlanamadı!");

					m_sock.connect();
					m_rdevice = m_sock.getRemoteDevice();
					m_input = m_sock.getInputStream();
					m_output = m_sock.getOutputStream();

					r.run();

				} 
				catch(IOException e){
					return new MobitException("Bluetooth bağlantısı sağlanamadı!", e);
				}
				catch (Exception e) {
					return new MobitException(e.getMessage(), e);
				}
				return null;

			}
		};

		// paired = true;
		if (paired == false) {

			throw new MobitException("Cihaz eşleştirilmemiş!");
			
			/*
			AndroidBluetooth.this.m_device = m_adapter.getRemoteDevice(BthAddr);
			
			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			getActivity().startActivity(intent);
			
			/*
			IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
			app.registerReceiver(receiver, filter);
			Intent intent = new Intent();
			intent.setAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
			intent.putExtra(BluetoothDevice.EXTRA_DEVICE, m_device);
			intent.putExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.PAIRING_VARIANT_PIN);
			intent.putExtra(BluetoothDevice.EXTRA_PAIRING_KEY, "0000");
			getActivity().startActivity(intent);
			*/
			
			

		} else {
			Object obj = rr.run(null);
			if(obj instanceof Exception) throw (Exception)obj;
		}

	}

	@Override
	public boolean isConnected() {
		return (m_sock != null && m_sock.isConnected());
	}
	
	String act;

	private final BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			act = action;
			
			if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
				try {

					byte[] pin = (byte[]) BluetoothDevice.class.getMethod("convertPinToBytes", String.class)
							.invoke(BluetoothDevice.class, Pin);
					m_device.getClass().getMethod("setPin", byte[].class).invoke(m_device, pin);
					m_device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(m_device, true);

					// rr.run();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				switch (state) {
				case BluetoothAdapter.STATE_CONNECTED: {
					// Do something you need here
					System.out.println("Connected");
					break;
				}
				case BluetoothAdapter.STATE_DISCONNECTED:

					System.out.println("Disconnected");

					break;
				default:
					System.out.println("Default");
					break;
				}
			}
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Device found
				return;
			} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				// Device is now connected
				return;
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// Done searching
				return;
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
				// Device is about to disconnect
				return;
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				// Device has disconnected
				
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getAddress().equalsIgnoreCase(BthAddr)) 
					close();
				
				/*
				try {
					ShowMessage(device.toString(), device.getAddress().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				return;
			}
		}
	};

	/*
	private static class Worker extends HandlerThread {

		private Object sync = new Object();
		public volatile Handler handler;
		private AndroidBluetooth bth;
		private String BthAddr;
		private String Pin;

		public Worker(AndroidBluetooth bth, String BthAddr, String Pin) {
			super(BthAddr);
			this.bth = bth;
			this.BthAddr = BthAddr;
			this.Pin = Pin;
		}

		public synchronized boolean waitUntilReady() {

			try {
				synchronized (sync) {
					sync.wait(10000);
				}
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}

		public void run() {
			Looper.prepare();
			handler = new Handler() { // the Handler hooks up to the current

				@Override
				public void handleMessage(Message msg) {

					if (msg.arg1 == 1)
						bth.pair(BthAddr, Pin);
				}
			};
			notifyAll(); // <- ADDED
			Looper.loop();
		}

	}

	public void pair(String BthAddr, String Pin) {
		try {

			AndroidBluetooth.this.m_device = m_adapter.getRemoteDevice(BthAddr);
			IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
			app.registerReceiver(receiver, filter);
			Method mm = m_device.getClass().getMethod("createBond", (Class[]) null);
			mm.invoke(m_device, (Object[]) null);

			// Sorun oluşturuyor. thread bloke olduğu için
			// pairingRequest çağrılmıyor

			/*
			 * synchronized (sync) { sync.wait(10000); }
			 *

		} catch (Exception e) {

		}
	}
	*/
	//-------------------------------------------------------------------------
	
	private static Application getApplication()
	{
		try {
			return (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null,
					(Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	private static Class<?> activityThreadClass = null;
	private static Object activityThread = null;
	private static Field activitiesField = null;
	private static Method currentActivityThread = null;
	private static Class<?> activityRecordClass = null;
	private static Field pausedField = null;
	private static Field activityField = null;
	
	private static Activity getActivity() {

		try {
		if (activityThreadClass == null) {
			activityThreadClass = Class.forName("android.app.ActivityThread");
			activitiesField = activityThreadClass.getDeclaredField("mActivities");
			activitiesField.setAccessible(true);
			currentActivityThread = activityThreadClass.getMethod("currentActivityThread");
		}
		activityThread = currentActivityThread.invoke(null);

		@SuppressWarnings("unchecked")
		Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
		if (activities == null)
			return null;

		for (Object activityRecord : activities.values()) {
			if (activityRecordClass == null) {
				activityRecordClass = activityRecord.getClass();
				pausedField = activityRecordClass.getDeclaredField("paused");
				pausedField.setAccessible(true);
				activityField = activityRecordClass.getDeclaredField("activity");
				activityField.setAccessible(true);
			}

			if (!pausedField.getBoolean(activityRecord)) {

				Activity activity = (Activity) activityField.get(activityRecord);
				return activity;
			}
		}
		}
		catch(Exception e){
			
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	private static void ShowMessage(String msg, String title) {

		Activity activity = getActivity();
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(msg);

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO
				dialog.dismiss();
				
			}
		};
		
		builder.setNeutralButton(R.string.ok, listener);

		activity.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog dlg = builder.create();
				dlg.show();
			}
		});

	}
	
	
	private static void ShowException(Throwable e) {
		
		ShowMessage(e.getMessage(), "Hata");

	}
}
