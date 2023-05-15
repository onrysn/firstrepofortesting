package mobit.eemr;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobit.eemr.ICallback;
import mobit.eemr.IMeterReader;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.MbtMeterInformation;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.MeterType;
import mobit.eemr.ReadMode;
import mobit.eemr.ReadMode2;

public abstract class MeterReader implements IMeterReader {

	private static int s_refCount = 0;
	
	protected static Object context;
	protected static Object handler;
	
	protected int Version;
	protected MbtProbePowerStatus PowerStatus = new MbtProbePowerStatus();
	protected MbtMeterInformation Information = new MbtMeterInformation();

	protected int cnt = 0;

	protected IProbeEvent m_ProbeEvent = null;
	protected IReadResult m_ReadResult = null;

	protected String pDevice;
	protected String pPin;
	protected String pLogFileName;

	protected ICallback clb = null;

	private boolean silent = false;
	private boolean triggerEnabled = false;

	static protected final int NEXT_ADDRESS = 1;
	static protected final int QUERY_ERROR = 2;
	static protected final int DATA_LINE = 3;
	static protected final int ENERGY_PROFILE = 4;
	static protected final int CURRENT_PROFILE = 5;
	static protected final int VOLTAGE_PROFILE = 6;
	static protected final int POWER_EVENT = 10;
	static protected final int TRIGGER_EVENT = 11;
	static protected final int CONNECTION_RESET_EVENT = 12;

	protected ExecutorService executer = null;

	protected MeterReader() {
		/*
		 * if(s_refCount > 0) throw new Exception(
		 * "Sadece bir tane nesne oluşturabilirsiniz!");
		 */
		s_refCount++;
		executer = Executors.newFixedThreadPool(2);
	}

	@Override
	protected void finalize() throws Throwable {
		
		try {
		
			close();
		}
		catch(Exception e){
			
		}
		s_refCount--;
		super.finalize();

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

		try {
			
			Disconnect();
		}
		catch(Exception e){
			
		}	

		if (executer != null) {
			executer.shutdown();
			executer = null;
		}
	}

	@Override
	public Object getContext() {
		return context;
	}

	@Override
	public Object getHandler() {
		return handler;
	}

	@Override
	public ExecutorService getExecutor() {
		return executer;
	}

	@Override
	public IProbeEvent getProbeEvent() {
		return m_ProbeEvent;
	}
	
	@Override
	public IReadResult getReadResult()
	{
		return m_ReadResult;
	}

	@Override
	public boolean getSilent() {
		return silent;
	}

	@Override
	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public boolean isTriggerEnabled()
	{
		return triggerEnabled;
	}
	public void setTriggerEnabled(boolean enabled)
	{
		this.triggerEnabled = enabled;
	}


	// ---------------------------------------------------------------------------
	
	// ---------------------------------------------------------------------------

	protected abstract void CreateMbtStdProbe(String pDevice, String pLogFileName) throws Exception;

	protected abstract void CreateMbtBtProbe(String pDevice, String pPin, String pLogFileName) throws Exception;
	protected abstract void OpenMbtElecMeter(MeterType meterType) throws Exception;
	protected abstract void DeleteMbtProbe();

	protected abstract void CloseMbtElecMeter();

	protected abstract void OpenMbtMeterReader(ReadMode mode) throws Exception;
	protected abstract void CloseMbtMeterReader();

	protected abstract void Read() throws Exception;
	
	protected abstract void CheckHandle(long handle) throws Exception;
	
	// ---------------------------------------------------------------------------

	protected void PowerEventHandler() {

		if (m_ProbeEvent == null)
			return;
		getExecutor().submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				m_ProbeEvent.PowerEvent();
			}

		});

	}

	protected void TriggerEventHandler() {
		if (m_ProbeEvent == null)
			return;

		if(!isTriggerEnabled()) return;

		getExecutor().submit(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				m_ProbeEvent.TriggerEvent(null);
			}

		});

	}

	protected void ConnectionResetEventHandler() {
		if (m_ProbeEvent == null)
			return;

		getExecutor().submit(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				m_ProbeEvent.ConnectionResetEvent();
			}

		});

	}

	// ---------------------------------------------------------------------------

	protected int NextAddressHandler(int[] ppPosition, String[] pAddress) {

		if (m_ReadResult == null)
			return 0;
		return m_ReadResult.NextAddress(ppPosition, pAddress) ? 1 : 0;
	}

	protected int QueryErrorHandler(String pAddress, int tryIndex) {
		if (m_ReadResult == null)
			return 0;
		return m_ReadResult.QueryError(pAddress, tryIndex);
	}

	protected int DataLineHandler(String pAddress, String pDataLine) {
		if (m_ReadResult == null)
			return 0;
		return m_ReadResult.DataLine(pAddress, pDataLine);
	}

	protected int EnergyProfileHandler(String pAct, String pEnd, String pCap, String pTime) {
		if (m_ReadResult == null)
			return 0;
		return m_ReadResult.EnergyProfile(pAct, pEnd, pCap, pTime);
	}

	protected int CurrentProfileHandler(String pIL1, String pIL2, String pIL3, String pTime) {
		if (m_ReadResult == null)
			return 0;
		return m_ReadResult.CurrentProfile(pIL1, pIL2, pIL3, pTime);
	}

	protected int VoltageProfileHandler(String pVL1, String pVL2, String pVL3, String pTime) {
		if (m_ReadResult == null)
			return 0;
		return m_ReadResult.VoltageProfile(pVL1, pVL2, pVL3, pTime);
	}

	// ---------------------------------------------------------------------------	
	
	@Override
	public void Connect(String BthAddr, String Pin, String logFile) throws Exception {

		CreateMbtBtProbe(BthAddr, Pin, logFile);
	}
	
	@Override
	public void ConnectAsync(final String BthAddr, final String Pin, final String logFile) {

		getExecutor().submit(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					Connect(BthAddr, Pin, logFile);
					if (clb != null)
						clb.Opened();

				} catch (Exception e) {

					if (clb != null)
						clb.Failed();
				}
			}

		});

	}

	@Override
	public void Reconnect() throws Exception {
		if (IsConnected())
			return;

		Connect(pDevice, pPin, pLogFileName);

	}

	@Override
	public void ReconnectAsync() {
		if (IsConnected())
			return;
		ConnectAsync(pDevice, pPin, pLogFileName);
	}

	@Override
	public void Trigger() {

		m_ProbeEvent.TriggerEvent(null);
	}
	@Override
	public void Retry(IReadResult result) {

		m_ProbeEvent.TriggerEvent(result);
	}

	@Override
	public void Disconnect() {
		CloseMbtMeterReader();
		CloseMbtElecMeter();
		DeleteMbtProbe();
	}

	@Override
	public void setCallback(ICallback clb) {
		this.clb = clb;
		m_ProbeEvent.setCallback(clb);
	}

	@Override
	public synchronized final void SetProbeEventHandler(IProbeEvent handler) {
		m_ProbeEvent = handler;
		m_ProbeEvent.setMeterReader(this);
	}

	//-------------------------------------------------------------------------
	
	
	@Override
	public void read(IReadResult result) throws IOException, Exception {

		
		try {

			
			Exception ex = null;
			
			
			for (int i = 0; i < 3; i++) {

				CloseMbtElecMeter();
				
				try {
					
					OpenMbtElecMeter(result.getMeterType());
					ex = null;
					
				} catch (Exception e) {
					ex = e;
					continue;
				}
				
				if(result.incRetry() > 0) result.reset();
				
				MbtMeterInformation emi = GetInformation();
				result.set_Information(emi);
				
				result.set_okuma_zamani(new Date());
				if (clb != null) clb.BeginRead(result);
				ReadMode2 mod = result.getReadMode();
				
				ReadMode mode = ReadMode.READOUT;
				if (mod == ReadMode2.OTOMATIK_MOD) {
					if ((emi.supportedModes & ReadMode.READOUT.getValue()) != 0)
						mode = ReadMode.READOUT;
					else if ((emi.supportedModes & ReadMode.PROGRAMMODE.getValue()) != 0)
						mode = ReadMode.PROGRAMMODE;
					else if ((emi.supportedModes & ReadMode.LONGREADOUT.getValue()) != 0)
						mode = ReadMode.LONGREADOUT;

				} else if (mod == ReadMode2.READOUT_MOD) {
					mode = ReadMode.READOUT;
				} else if (mod == ReadMode2.PROGRAM_MOD) {
					mode = ReadMode.PROGRAMMODE;
				} else if (mod == ReadMode2.LONG_READOUT_MOD) {
					mode = ReadMode.LONGREADOUT;
				}

				m_ReadResult = result;
				CloseMbtMeterReader();

				OpenMbtMeterReader(mode);

				Read();

				ex = null;

				if (clb != null)
					clb.EndRead();

				CloseMbtMeterReader();
				CloseMbtElecMeter();
				
				break;

			}
			
			if(ex != null) throw ex;
			

		} catch (Exception e) {

			e.printStackTrace();
			throw e;

		} finally {
			CloseMbtMeterReader();
			CloseMbtElecMeter();
		}

	}
	
	

}
