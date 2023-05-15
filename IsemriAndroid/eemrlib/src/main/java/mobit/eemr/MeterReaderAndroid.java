package mobit.eemr;


import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.mobit.MobitException;
import mobit.eemr.IReadResult;
import mobit.eemr.MbtMeterInformation;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.MeterType;
import mobit.eemr.ReadMode;
import mobit.eemr.base.CBluSkyBtProbe;
import mobit.eemr.base.CElecMeter;
import mobit.eemr.base.CMeterReader;
import mobit.eemr.base.CMobitBtProbe1;
import mobit.eemr.base.CStdOptoProbe;
import mobit.eemr.base.IOptoProbe;

public class MeterReaderAndroid extends MeterReader {

	
	/*
	private IProbeEvent m_ProbeEvent = null;
	private IReadResult m_ReadResult = null;

	private String pDevice;
	private String pPin;
	private String pLogFileName;

	private ICallback clb = null;

	private boolean silent = false;
	*/
	
	protected static Application app;
	
	static {

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
	
	private long m_hBtProbe = 0;
	private long m_hElecMeter = 0;
	private long m_hMeterReader = 0;
	
	private AndroidBluetooth m_bth = null;
	
	private Hashtable<Long, Object> m_hashtable =  new Hashtable<Long, Object>();
	private long add(Object obj)
	{
		Long key = Long.valueOf(obj.hashCode());
		m_hashtable.put(key, obj);
		return key;
	}
	private void remove(long key)
	{
		m_hashtable.remove(key);
	}
	private Object get(long key)
	{
		return m_hashtable.get(key);
	}
	
	@Override
	public IBluetooth getBluetooth()
	{
		return m_bth;
	}
	

	protected MeterReaderAndroid () {
		
		super();
		
		m_bth = new AndroidBluetooth();
		m_ProbeEvent = new ProbeEventEx(this);
		
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
		
		if (m_bth != null)
			m_bth.close();
	}

	@Override
	protected final void CheckHandle(long handle) throws Exception {
		if (handle == 0)
			throw new MobitException("Geçersiz nesne değeri");
	}

	
	// ---------------------------------------------------------------------------
	
	private  long nGetLibraryVersion()
	{
		return 0;
	}

	private  long nCreateMbtStdProbe(String pDevice, String pLogFileName) throws Exception
	{
		
		IOptoProbe pOptoProbe = null;
		try {
			pOptoProbe = new CStdOptoProbe();
			pOptoProbe.Open(pDevice, null, pLogFileName);
			return add(pOptoProbe);
		}
		catch(Exception e){
			if(pOptoProbe != null) pOptoProbe.Close();
			throw e;
		}
		
	}
	
	
	private long nCreateMbtBtProbe(String pDevice, String pParameter, String pLogFileName) throws Exception
	{
		
		IOptoProbe pOptoProbe = null;
		try {
			pOptoProbe = new CMobitBtProbe1(this);
			pOptoProbe.Open(pDevice, pParameter, pLogFileName);
			return add(pOptoProbe);
		}
		catch(Exception e){
			if(pOptoProbe != null) pOptoProbe.Close();
			throw e;
		}
		
	}


	private  int nSetLed(long hBtProbe, int enable) throws Exception{
		IOptoProbe pOptoProbe = (IOptoProbe)get(hBtProbe);
		pOptoProbe.SetLed(enable != 0);
		return 0;
	}

	private  int nSetAutoPowerOffTime(long hBtProbe, short duration) throws Exception{
		
		IOptoProbe pOptoProbe = (IOptoProbe)get(hBtProbe);
		pOptoProbe.SetAutoPowerOffTime(duration);
		return 0;
	}

	private  void nAbortRead(long hProbe)
	{
		IOptoProbe pOptoProbe = (IOptoProbe)get(hProbe);
		
	}

	private  void nDeleteMbtProbe(long hProbe){
		
		IOptoProbe pOptoProbe = (IOptoProbe)get(hProbe);
		remove(pOptoProbe.hashCode());
		pOptoProbe.Close();
	}

	private  int nGetVersion(long hBtProbe) throws Exception{
		IOptoProbe pOptoProbe = (IOptoProbe)get(hBtProbe);
		Version = pOptoProbe.GetVersion();
		return 0;
	}

	private  int nGetPowerStatus(long hBtProbe) throws Exception{
		IOptoProbe pOptoProbe = (IOptoProbe)get(hBtProbe);
		PowerStatus = pOptoProbe.GetPowerStatus();
		return 0;
	}

	private  long nOpenMbtElecMeter(long hProbe, int meterType) throws Exception{
		
		CElecMeter pElecMeter = null;
		try {
			pElecMeter = new CElecMeter((IOptoProbe)get(m_hBtProbe));
			pElecMeter.Open(meterType);
			return add(pElecMeter);
		}
		catch(Exception e) {
			if(pElecMeter != null) pElecMeter.Close();
			throw e;
		}
		
	}

	private  void nCloseMbtElecMeter(long hElecMeter){
		CElecMeter pElecMeter = (CElecMeter)get(hElecMeter);
		remove(pElecMeter.hashCode());
		pElecMeter.Close();
	}

	private  void nGetInformation(long hElecMeter){
		CElecMeter pElecMeter = (CElecMeter)get(hElecMeter);
		Information = pElecMeter.GetInformation();
	}

	private  long nOpenMbtMeterReader(long hElecMeter, long mode) throws Exception{
		
		CMeterReader pMeterReader = null;
		try {
			CElecMeter pElecMeter = (CElecMeter)get(hElecMeter);
			pMeterReader = pElecMeter.CreateReader((int)mode);
			return add(pMeterReader);
		}
		catch(Exception e) {
			if(pMeterReader != null) pMeterReader.Close();
			throw e;
		}
	}

	private  void nCloseMbtMeterReader(long hMeterReader){
		CMeterReader pMeterReader = (CMeterReader)get(hMeterReader);
		remove(pMeterReader.hashCode());
		pMeterReader.Close();
	}

	private  int nRead(long hMeterReader) throws Exception{
		
		CMeterReader pMeterReader = (CMeterReader)get(hMeterReader);
		return pMeterReader.Read() ? 0 : 1;
		
	}
	
	// ---------------------------------------------------------------------------

	protected final void CreateMbtStdProbe(String pDevice, String pLogFileName) throws Exception {
		
		nCreateMbtStdProbe(pDevice, pLogFileName);
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

		if(m_hMeterReader == 0) throw new MobitException("Sayaç açılmamış!");
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

	@Override
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

	@Override
	protected void TriggerEventHandler() {
		if (m_ProbeEvent == null)
			return;

		getExecutor().submit(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				m_ProbeEvent.TriggerEvent(null);
			}

		});

	}

	@Override
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

	
	public final long GetLibraryVersion() {
		return nGetLibraryVersion();
	}

	@Override
	public final boolean IsConnected() {
		return (m_hBtProbe != 0);
	}

	@Override
	public final void Connect(String BthAddr, String Pin, String logFile) throws Exception {

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
	public final void SetLed(boolean enable) throws Exception {
		CheckHandle(m_hBtProbe);
		if (nSetLed(m_hBtProbe, enable ? 1 : 0) != 0)
			throw new MobitException("Led ışık açılamadı!");
	}

	@Override
	public final void SetAutoPowerOffTime(short duration) throws Exception {
		CheckHandle(m_hBtProbe);
		if (nSetAutoPowerOffTime(m_hBtProbe, duration) != 0)
			throw new MobitException("Otomatik kapanma zaman aşım süresi değiştirilemedi!");
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

	@Override
	public final MbtMeterInformation GetInformation() throws Exception {
		CheckHandle(m_hElecMeter);
		nGetInformation(m_hElecMeter);
		return Information.clone();
	}
	

}
