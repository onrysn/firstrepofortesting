package mobit.eemr;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.mobit.MobitException;
import mobit.eemr.IReadResult;
import mobit.eemr.MbtMeterInformation;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.MeterType;
import mobit.eemr.ReadMode;
import mobit.eemr.ReadMode2;


public class MeterReaderCpp extends MeterReader {

	// Android'e Özel kesim
	protected static Application app;
	private OpticMeterReadActivity activity = null;
	Lun_Control Tesisat=new Lun_Control();


	static {

		// android.os.Debug.waitForDebugger();
		System.loadLibrary("eemr");

		// Android'e Özel kesim
		try {
			app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null,
					(Object[]) null);
			context = app.getApplicationContext();
			handler = new Handler(((Context) context).getMainLooper());

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private int Version;
	protected MbtProbePowerStatus PowerStatus = new MbtProbePowerStatus();
	protected MbtMeterInformation Information = new MbtMeterInformation();

	private int cnt = 0;
	
	long m_hBtProbe = 0;
	long m_hElecMeter = 0;
	long m_hMeterReader = 0;

	protected AndroidBluetooth m_bth = null;

	final int NEXT_ADDRESS = 1;
	final int QUERY_ERROR = 2;
	final int DATA_LINE = 3;
	final int ENERGY_PROFILE = 4;
	final int CURRENT_PROFILE = 5;
	final int VOLTAGE_PROFILE = 6;
	final int POWER_EVENT = 10;
	final int TRIGGER_EVENT = 11;
	final int CONNECTION_RESET_EVENT = 12;

	private ExecutorService executer = null;

	protected MeterReaderCpp() {
		/*
		 * if(s_refCount > 0) throw new MobitException(
		 * "Sadece bir tane nesne oluşturabilirsiniz!");
		 */
		m_bth = new AndroidBluetooth();
		m_ProbeEvent = new ProbeEventEx(this);
		executer = Executors.newFixedThreadPool(2);
	}

	@Override
	protected void finalize() throws Throwable {

		close();		
		super.finalize();

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

		Disconnect();

		if (activity != null) {
			activity.finish();
			activity = null;
		}
		if (m_bth != null)
			m_bth.close();

		if (executer != null) {
			executer.shutdown();
			executer = null;
		}
	}

	protected final void CheckHandle(long handle) throws Exception {
		if (handle == 0)
			throw new MobitException("Geçersiz nesne değeri");
	}

	
	// ---------------------------------------------------------------------------
	private native long nGetLibraryVersion();

	private native long nCreateMbtStdProbe(String pDevice, String pLogFileName);

	private native long nCreateMbtBtProbe(String pDevice, String pPin, String pLogFileName);

	private native int nSetLed(long hBtProbe, int enable);

	private native int nSetAutoPowerOffTime(long hBtProbe, short duration);

	private native void nAbortRead(long hProbe);

	private native void nDeleteMbtProbe(long hProbe);

	private native int nGetVersion(long hBtProbe);

	private native int nGetPowerStatus(long hBtProbe);

	private native long nOpenMbtElecMeter(long hProbe, int meterType);

	private native void nCloseMbtElecMeter(long hElecMeter);

	private native void nGetInformation(long hElecMeter);

	private native long nOpenMbtMeterReader(long hElecMeter, long mode);

	private native void nCloseMbtMeterReader(long hMeterReader);

	private native int nRead(long hMeterReader);

	// ---------------------------------------------------------------------------

	protected final void CreateMbtStdProbe(String pDevice, String pLogFileName) throws Exception {
		throw new MobitException("Tanımlanmadı!");
	}

	protected final void CreateMbtBtProbe(String pDevice, String pPin, String pLogFileName) throws Exception {

		DeleteMbtProbe();

		this.pDevice = pDevice;
		this.pPin = pPin;
		this.pLogFileName = pLogFileName;

		if (pDevice == null || pDevice.length() == 0)
			throw new MobitException("Optik port MAC adresi boş olamaz!");

		m_hBtProbe = nCreateMbtBtProbe(pDevice, pPin, pLogFileName);

		if (m_hBtProbe == 0)
			throw new MobitException("Optik port açılamadı");

		nSetAutoPowerOffTime(m_hBtProbe, (short)(30*60));
		
	}

	protected final void OpenMbtElecMeter(MeterType meterType) throws Exception {
		CheckHandle(m_hBtProbe);
		CloseMbtElecMeter();
		m_hElecMeter = nOpenMbtElecMeter(m_hBtProbe, meterType.getValue());
		//HÜSEYİN EMRE ÇEVİK SAYAÇ AÇILAMADI HATASI 19/03/2021( ÇÖZÜLMEDİ )
		/*	int i =0;
		if (m_hElecMeter == 0){

			while(i<5 && m_hElecMeter==0){
				m_hElecMeter = nOpenMbtElecMeter(m_hBtProbe, meterType.getValue());
				i++;
			}
			if (m_hElecMeter==0)
				throw new MobitException("Sayaç açılamadı!");

		}*/

		if (m_hElecMeter == 0)
			throw new MobitException("Sayaç açılamadı!");
	}

	protected final void DeleteMbtProbe() {
		if (m_hBtProbe != 0) {
			long handle = m_hBtProbe;
			m_hBtProbe = 0;
			nDeleteMbtProbe(handle);
		}
	}

	protected final void CloseMbtElecMeter() {
		if (m_hElecMeter != 0) {
			long handle = m_hElecMeter;
			m_hElecMeter = 0;
			nCloseMbtElecMeter(handle);
		}
	}

	protected final void OpenMbtMeterReader(ReadMode mode) throws Exception {
		CheckHandle(m_hElecMeter);
		CloseMbtMeterReader();
		m_hMeterReader = nOpenMbtMeterReader(m_hElecMeter, mode.getValue());

		if (m_hMeterReader == 0)
			throw new MobitException("Sayaç okumaya açılamadı!");

	}

	protected final void CloseMbtMeterReader() {
		if (m_hMeterReader != 0) {
			long handle = m_hMeterReader;
			m_hMeterReader = 0;
			nCloseMbtMeterReader(handle);
		}
	}

	protected final void Read() throws Exception {

		if(m_hMeterReader == 0) throw new MobitException("Sayaç açılmamadı!");
		if(nRead(m_hMeterReader) != 0) {
			YkpOkuma ykpOkuma=new YkpOkuma();
			if (ykpOkuma.YkpOkumaDurum==1 && YkpOkuma.YkpResult!=null)
			{
				throw new MobitException("YKP Okuma Bitti");
			}
			if (m_ReadResult.get_sayac_no()==null) {
				throw new MobitException("Sayaç okunamadı!");
			}
		}
	}


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

	
	public final long GetLibraryVersion() {
		return nGetLibraryVersion();
	}

	@Override
	public final boolean IsConnected() {
		return (m_hBtProbe != 0);
	}

		@Override
	public final void SetLed(boolean enable) throws Exception {
		CheckHandle(m_hBtProbe);
		if (nSetLed(m_hBtProbe, enable ? 1 : 0) != 0)
			throw new MobitException("Led ışık açılamadı!");
	}

	@Override
	public final void SetAutoPowerOffTime(short duration) throws Exception {
		CheckHandle(m_hBtProbe);
		if (nSetAutoPowerOffTime(m_hBtProbe, duration) != 0)
			throw new MobitException("Otomatik kapanma zaman aşım süresi deşiştirilemedi!");
	}

	@Override
	public final void AbortRead() throws Exception {
		CheckHandle(m_hBtProbe);
		nAbortRead(m_hBtProbe);
	}

	@Override
	public final int GetVersion() throws Exception {
		CheckHandle(m_hBtProbe);
		if (nGetVersion(m_hBtProbe) != 0)
			throw new MobitException("Versiyon bilgisi alınamadı!");
		return Version;
	}

	@Override
	public final MbtProbePowerStatus GetPowerStatus() throws Exception {
		CheckHandle(m_hBtProbe);
		if (nGetPowerStatus(m_hBtProbe) != 0)
			throw new MobitException("Prob pil durum bilgisi alınamadı!");
		return PowerStatus.clone();
	}

	public final MbtMeterInformation GetInformation() throws Exception {
		CheckHandle(m_hElecMeter);
		nGetInformation(m_hElecMeter);
		try {
			if (Information.flag.equals("LUN")){
				Tesisat.setLuna_Control(1);
			}
			else if (Information.flag.equals("MSY") &&
					(Information.identification.indexOf("560.2251")>-1
					|| Information.identification.indexOf("500.2251")>-1
					|| Information.identification.indexOf("550.2251")>-1
					|| Information.identification.indexOf("600.2251")>-1))
			{
				Tesisat.setLuna_Control(1);
			}
			else {
				Tesisat.setLuna_Control(0);
			}
		}catch (Exception e){
			Tesisat.setLuna_Control(0);
		}


		return Information.clone();
	}

	@Override
	public void read(IReadResult result) throws IOException, Exception {

		
		try {

			
			Exception ex = null;
			//muhammed gökkaya orj:3
			Tesisat.setLuna_Control(0);
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
					//mode=ReadMode.PROFILEMODE;
				} else if (mod == ReadMode2.PROGRAM_MOD) {
					mode = ReadMode.PROGRAMMODE;
				} else if (mod == ReadMode2.LONG_READOUT_MOD) {
					mode = ReadMode.LONGREADOUT;
				}
				else  if (mod == ReadMode2.pPROFIL_MOD){
					mode=ReadMode.PROFILEMODE;
				}
				else  if (mod == ReadMode2.DLMS_COSEM){
					mode=ReadMode.DLMS_COSEM;
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
			Log.d("eemr", e.getMessage(), e);
			throw e;

		} finally {
			CloseMbtMeterReader();
			CloseMbtElecMeter();
		}

	}
	
	@Override
	public IBluetooth getBluetooth()
	{
		return m_bth;
	}
	
	

}
