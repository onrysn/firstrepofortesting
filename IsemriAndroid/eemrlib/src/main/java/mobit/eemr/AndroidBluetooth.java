
package mobit.eemr;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mobit.MobitException;
import mobit.eemr.base.BTH_ADDR;
import mobit.eemr.base.CBtSocketStream;

public class AndroidBluetooth extends CBtSocketStream {

	private Object sync = new Object();

	@SuppressWarnings("unused")
	private UUID sdpid;
	@SuppressWarnings("unused")
	private String BthAddr;
	private String Pin;

	private BluetoothAdapter m_adapter;
	private BluetoothDevice m_device;
	@SuppressWarnings("unused")
	private BluetoothDevice m_rdevice;
	private BluetoothSocket m_sock;
	private boolean m_enabled;

	static Application app = null;

	static {
		try {
			app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null,
					(Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {

		close();
		synchronized (sync) {
			sync.notifyAll();
		}
		super.finalize();
	}

	@Override
	public void close() {

		super.close();

		if (m_sock != null) {
			try {
				m_sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_sock = null;
		}

		if (m_enabled == false)
			m_adapter.disable();

	}

	public AndroidBluetooth() {

		m_adapter = BluetoothAdapter.getDefaultAdapter();
		m_enabled = m_adapter.isEnabled();
		if (m_enabled == false)
			m_adapter.enable();
	}

	/*
	 * public void Open(String BthAddr, String Pin) throws Exception {
	 * Open(IBluetooth.SPP, BthAddr, Pin); }
	 */
	public void Open(UUID sdpid, String BthAddr, String Pin) throws Exception {

		if (m_sock != null && m_sock.isConnected())
			return;

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

		if (paired == false) {

			throw new MobitException("Optik port eşleştirilmemiş!");
			/*
			 * try {
			 * 
			 * m_device = m_adapter.getRemoteDevice(BthAddr); IntentFilter
			 * filter = new
			 * IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
			 * app.registerReceiver(pairingRequest, filter); Method mm =
			 * m_device.getClass().getMethod("createBond", (Class[]) null);
			 * mm.invoke(m_device, (Object[]) null);
			 * 
			 * synchronized (sync) { sync.wait(10000); }
			 * 
			 * } catch (Exception e) { throw new MobitException(
			 * "Bluetooth cihaz eşleştirilemedi!"); }
			 */

		}

		try {
			m_sock = m_device.createRfcommSocketToServiceRecord(sdpid);
			int state = m_device.getBondState();

			if (state == BluetoothDevice.BOND_NONE)
				throw new MobitException("Bluetooth cihaza bağlanılamadı!");

			m_sock.connect();

			m_rdevice = m_sock.getRemoteDevice();
			m_input = m_sock.getInputStream();
			m_output = m_sock.getOutputStream();
			
		} catch (Exception e) {
			throw new MobitException("Optik portla bağlantı sağlanamadı!", e);
		}
	}

	public boolean isConnected() {
		return (m_sock != null && m_sock.isConnected());
	}

	@SuppressWarnings("unused")
	private final BroadcastReceiver pairingRequest = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
				try {

					if(Pin != null){
						byte[] pin = (byte[]) BluetoothDevice.class.getMethod("convertPinToBytes", String.class)
							.invoke(BluetoothDevice.class, Pin);
						m_device.getClass().getMethod("setPin", byte[].class).invoke(m_device, pin);
					}
					m_device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(m_device, true);

					synchronized (sync) {
						sync.notify();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void Connect(Socket hSocket, BTH_ADDR pBthAddr, int channel, String pWidePin) {
		// TODO Auto-generated method stub
		
	}

}
