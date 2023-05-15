package mobit.eemr.base;

import java.util.logging.Logger;

import mobit.eemr.IBluetooth;
import mobit.eemr.IMeterReader;
import mobit.eemr.MbtProbePowerStatus;

public class CBluSkyBtProbe implements IOptoProbe, IProbeEvent {

	private ICommStream m_pStream;
	private Thread m_hReceiveThread;
	private volatile boolean m_ExitThread;
	private CBtProbeStream m_BtProbeStream;
	private IMeterReader m_pMeterReader;

	private IProbeEvent m_pProbeEventHandler;

	private static final int PROBE_TIMEOUT = 10 * 1000;

	public CBluSkyBtProbe() {

		m_pStream = null;
		m_hReceiveThread = null;
		m_pProbeEventHandler = this;
		m_BtProbeStream = new CBtProbeStream();
		
	}
	
	public CBluSkyBtProbe(IMeterReader pMeterReader) {
		this();
		m_pMeterReader = pMeterReader;
	}

	@Override
	protected void finalize() throws Throwable {
		Close();
		super.finalize();
	}

	@Override
	public void Open(String pDevice, String pParameter, String pLogFileName) throws Exception {

		ICommStream pCommStream = null;

		try {

			IBluetooth pBluetooth = m_pMeterReader.getBluetooth();
			pBluetooth.Open(IBluetooth.SPP, pDevice, pParameter);
			pCommStream = (ICommStream) pBluetooth;

			Thread.sleep(1000);
			pCommStream.ClearIoBuffers(true, true);

			pCommStream.SetReadTimeout(PROBE_TIMEOUT);

			m_BtProbeStream.Open(pCommStream);

			m_pStream = pCommStream;
			StartReceiveThread();

		} catch (Exception e) {
			m_pStream = null;
			throw e;
		}
	}

	@Override
	public void Close() {

		StopReceiveThread();
		if (m_BtProbeStream != null) {
			m_BtProbeStream.Close();
			m_BtProbeStream = null;
		}

		if (m_pStream != null) {
			m_pStream.Close();
			m_pStream = null;
		}
	}

	@Override
	public int GetType() {
		return (IDef.MBTPROBE_BLUSKYBLUETOOTH);
	}

	@Override
	public ICommStream GetStream() {
		return m_BtProbeStream;
	}

	@Override
	public void IrEnable(boolean enable) {

	}

	@Override
	public void IrSetConfig(int baudRateId, int byteSize, int parity) throws Exception {
		String command;
		baudRateId=0;
		command = String.format("+++serial,%d,7e1\r\n", baudRateId);

		WriteOutputBuffer(command.getBytes(), command.getBytes().length);

		m_pStream.FlushWrite();
	}

	@Override
	public void SetLed(boolean enable) throws Exception {
		throw new Exception("SetLed desteklenmiyor!");

	}

	@Override
	public int GetVersion() throws Exception {
		throw new Exception("GetVersion desteklenmiyor!");

	}

	@Override
	public void SetAutoPowerOffTime(int duration) throws Exception {
		throw new Exception("SetAutoPowerOffTime desteklenmiyor!");
	}

	@Override
	public MbtProbePowerStatus GetPowerStatus() throws Exception {
		byte[] respLine = new byte[IDef.MAX_PATH + 1];

		m_BtProbeStream.SetAborted(false);
		String s = "+++battery\r\n";
		WriteOutputBuffer(s.getBytes(), 12);
		m_pStream.FlushWrite();
		ReadResponse(respLine, IDef.MAX_PATH);

		MbtProbePowerStatus pStatus = new MbtProbePowerStatus();
		pStatus.charging = 0;
		pStatus.batteryVoltage = 0;
		pStatus.batteryLifePercent = Integer.parseInt(new String(respLine));
		return pStatus;
	}

	public synchronized void SetProbeEventHandler(IProbeEvent pProbeEventHandler) {

		if (pProbeEventHandler == null) {
			m_pProbeEventHandler = this;
		} else {
			m_pProbeEventHandler = pProbeEventHandler;
		}
	}

	private void StartReceiveThread() {

		StopReceiveThread();

		Thread hThread;

		m_ExitThread = false;
		hThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ReceiveThreadProc();
			}

		});

		hThread.setPriority(Thread.MAX_PRIORITY);
		hThread.start();

		m_hReceiveThread = hThread;

	}

	private boolean StopReceiveThread() {

		m_ExitThread = true;

		if (m_hReceiveThread != null && m_hReceiveThread.isAlive()) {
			try {
				m_hReceiveThread.join(1000);

			} catch (InterruptedException e) {

			}
			if (m_hReceiveThread.isAlive())
				m_hReceiveThread.interrupt();
			m_hReceiveThread = null;
		}

		return (true);
	}

	private void TriggerEvent() {

		m_pProbeEventHandler.TriggerEventHandler(null);
	}

	private void ConnectionResetEvent() {
		m_pProbeEventHandler.ConnectionResetEventHandler(null);

	}

	private boolean ReadResponse(byte[] pLine, int maxLen) throws Exception {
		byte[] chr1 = new byte[1];
		byte[] chr2 = new byte[1];
		int index;
		int[] bytesRead = new int[1];

		index = 0;
		while (true) {
			byte[] chr = new byte[1];

			m_BtProbeStream.Read(chr, chr.length, bytesRead);

			if (bytesRead[0] == 0)
				return (false);

			if (chr[0] == '\r' || chr[0] == '\n') {
				chr1[0] = chr[0];
				break;
			}

			if (index >= maxLen)
				return (false);

			pLine[index++] = chr[0];
		}

		m_BtProbeStream.Read(chr2, chr2.length, bytesRead);

		if (bytesRead[0] == 0)
			return (false);

		if ((chr1[0] != '\r' || chr2[0] != '\n') && (chr1[0] != '\n' || chr2[0] != '\r')) {
			return (false);
		}


		pLine[index] = '\0';
		return (true);
	}

	private void WriteOutputBuffer(byte[] pBuffer, int bytesToWrite) throws Exception {
		int totalWritten;

		totalWritten = 0;
		while (totalWritten < bytesToWrite) {
			int[] bytesWritten = new int[1];

			if (m_ExitThread)
				throw new Exception("Thread durdurulmuş!");

			m_pStream.Write(pBuffer, bytesToWrite - totalWritten, bytesWritten);

			if (bytesWritten[0] == 0)
				throw new Exception("Veri buffera yazılamadı!");

			totalWritten += bytesWritten[0];
		}
	}

	private Exception ex = null;

	private long ReceiveThreadProc() {

		int eventDataLength;
		byte[] eventBuffer = new byte[7];
		byte[] data = new byte[1];
		int[] bytesRead = new int[1];

		try {

			eventDataLength = 0;
			while (!m_ExitThread) {

				data[0] = 0;
				bytesRead[0] = 0;

				m_pStream.Read(data, data.length, bytesRead);

				if (bytesRead[0] == 0) {
					if (eventDataLength != 0) {
						m_BtProbeStream.QueueInput(eventBuffer, eventDataLength);
						eventDataLength = 0;
					}
					Thread.sleep(50);
					continue;
				}

				if (eventDataLength == 0) {
					if (data[0] == 'R') {
						eventBuffer[eventDataLength++] = data[0];
					} else
						m_BtProbeStream.QueueInput(data, data.length);
				} else {
					eventBuffer[eventDataLength++] = data[0];
					// if (memcmp(eventBuffer, "Ready\r\n",
					// min(eventDataLength,
					// 7)) == 0)

					if (new String(eventBuffer).substring(0, Math.min(eventDataLength, 7))
							.compareTo("Ready\r\n") == 0) {
						if (eventDataLength == 7) {
							TriggerEvent();
							eventDataLength = 0;
						}
					} else {
						m_BtProbeStream.QueueInput(eventBuffer, eventDataLength);
						eventDataLength = 0;
					}

				}
			}

		} catch (Exception e) {
			/*
			 * MessageBox(NULL, TEXT(
			 * "'CBluSkyBtProbe::ReceiveThreadProc' thread'inde kritik bir hata oluştu!."
			 * ) TEXT(" Thread'i sonlandırmak için lütfen onaylayın"), TEXT(
			 * "Kritik Hata"), MB_OK|MB_ICONERROR|MB_TOPMOST);
			 */

			ex = e;

		}

		if (!m_ExitThread) {
			m_ExitThread = true;
			m_BtProbeStream.SetAborted(true);
			ConnectionResetEvent();
		}

		return (0);
	}

	@Override
	public void TriggerEventHandler(Object pContext) {
		return;
	}

	@Override
	public void ConnectionResetEventHandler(Object pContext) {
		return;
	}
	@Override
	public void PowerEventHandler(Object pContext) {
		return;
	}

}
