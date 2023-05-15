package mobit.eemr.base;


import mobit.eemr.MbtMeterInformation;

public class CElecMeter {

	private boolean m_IsOpen;
	private IOptoProbe m_pOptoProbe;
	private MbtMeterInformation m_Information;

	public CElecMeter(IOptoProbe pOptoProbe) {
		m_IsOpen = false;
		m_pOptoProbe = pOptoProbe;
		m_Information = new MbtMeterInformation();
	}

	@Override
	protected void finalize() throws Throwable {
		Close();
		super.finalize();
	}
	
	public IOptoProbe GetOptoProbe() {
		return (m_pOptoProbe);
	}

	public MbtMeterInformation GetInformation() {
		return (m_Information);
	}

	public void Open(int meterType) throws Exception {
		
		String pDevAddr;
		
		CMeterReader pMeterReader;
		CDynamicBuffer devAddr = new CDynamicBuffer();
		CIEC62056 iec62056 = new CIEC62056(m_pOptoProbe.GetStream());

		m_pOptoProbe.GetStream().SetAborted(false);
		m_pOptoProbe.GetStream().ClearIoBuffers(true, true);

		m_pOptoProbe.GetStream().SetReadTimeout(CIEC62056.IEC62056_TIMEOUT);

		m_pOptoProbe.IrEnable(true);

		try {
			m_pOptoProbe.IrSetConfig(CIEC62056.IEC62056_BR300, 7, IDef.EVENPARITY);

			if (meterType == IDef.MBTMETERREADER_GENERICMETER || meterType == IDef.MBTMETERREADER_FEDERALFTS3DMETER) {
				pDevAddr = null;
				pMeterReader = null;
			} else if (meterType == IDef.MBTMETERREADER_OLDKOHLERMETER) {

				pDevAddr = CKhlProgReader.QuerySerial(iec62056, devAddr);

			} else {
				m_pOptoProbe.IrEnable(false);
				throw new Exception("***");
			}

			iec62056.WriteRequestMessage(pDevAddr);
			m_pOptoProbe.GetStream().SetReadTimeout(CIEC62056.IEC62056_IDENTIFICATIONTIMEOUT);

			iec62056.ReadIdentificationMessage((meterType == IDef.MBTMETERREADER_FEDERALFTS3DMETER), m_Information);

			m_pOptoProbe.GetStream().SetReadTimeout(CIEC62056.IEC62056_TIMEOUT);
			
			
		} catch (Exception e) {
			m_pOptoProbe.IrEnable(false);
			throw e;
		}

		m_Information.supportedModes = GetSupportedModes(m_Information.flag, m_Information.identification);
		m_IsOpen = true;

	}

	public void Close() {
		if (!m_IsOpen)
			return;

		try {
			
			m_pOptoProbe.IrEnable(false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_IsOpen = false;
	}

	public CMeterReader CreateReader(int mode) throws Exception {
		if ((m_Information.supportedModes & mode) != 0)
			throw new Exception("Sayaç okuma modunu desteklemiyor!");

		if (m_Information.flag.startsWith("VIK"))
			return (CreateVikReader(mode));
		else if (m_Information.flag.startsWith("MSY"))
			return (CreateMsyReader(mode));
		//muhammed gökkaya
		else if (m_Information.flag.startsWith("LUN") || m_Information.flag.startsWith("<1>LUN"))
			return (CreateLunReader(mode));
		else if (m_Information.flag.startsWith("ELM"))
			return (CreateElmReader(mode));
		else if (m_Information.flag.startsWith("AMP"))
			return (new CAmpProgReader(this));
		else if (m_Information.flag.startsWith("KHL"))
			return (new CKhlProgReader(this));
		else if (m_Information.flag.startsWith("AEL"))
			return (new CAelProgReader(this));
		else if (m_Information.flag.startsWith("BSE"))
			return (new CBseReadOutReader(this));
		else if (m_Information.flag.startsWith("EGM"))
			return (new CReadOutReader(this, 200));
		else if (m_Information.flag.startsWith("PRO"))
			return (new CReadOutReader(this, 200));
		else if (m_Information.flag.startsWith("FED"))
			return (new CReadOutReader(this, 0, false));
		else if (m_Information.flag.startsWith("ASR"))
			return (new CAsrReadOutReader(this));
		else if (m_Information.flag.startsWith("HLY"))
			return (new CDlmsReader(this));
		return (new CReadOutReader(this));
	}

	CMeterReader CreateVikReader(int mode) throws Exception
	{
		if (IsNewViko(m_Information.identification))
		{
			if (mode == IDef.MBTMETERREADER_READOUT)
				return(new CReadOutReader(this));
			else if (mode == IDef.MBTMETERREADER_LONGREADOUT)
				return(new CReadOutReader(this));
			else if (mode == IDef.MBTMETERREADER_PROGRAMMODE)
				return(new CVikNewProgReader(this));
			else if (mode == IDef.MBTMETERREADER_PROFILEMODE)
				return(new CVikNewProgReader(this));
		}
		else
		{
			if (mode == IDef.MBTMETERREADER_PROGRAMMODE)
				return(new CVikProgReader(this));
			else if (mode == IDef.MBTMETERREADER_PROFILEMODE)
				return(new CVikProgReader(this));
		}
		throw new Exception("Viko sayaç okuma modunu desteklemiyor!");
	}

	CMeterReader CreateMsyReader(int mode) throws Exception {
		if (mode == IDef.MBTMETERREADER_READOUT)
			return (new CMsyReadOutReader(this, false));
		else if (mode == IDef.MBTMETERREADER_LONGREADOUT)
			return (new CMsyReadOutReader(this, true));
		else if (mode == IDef.MBTMETERREADER_PROGRAMMODE)
			return (new CMsyProgReader(this));
		else if (mode == IDef.MBTMETERREADER_PROFILEMODE)
			return (new CMsyProgReader(this));
		throw new Exception("Msy sayaç okuma modunu desteklemiyor!");
	}

	CMeterReader CreateLunReader(int mode) throws Exception {
		if (mode == IDef.MBTMETERREADER_READOUT)
			return (new CReadOutReader(this));
		else if (mode == IDef.MBTMETERREADER_LONGREADOUT)
			return (new CReadOutReader(this));
		else if (mode == IDef.MBTMETERREADER_PROGRAMMODE)
			return (new CLunProgReader(this));
		else if (mode == IDef.MBTMETERREADER_PROFILEMODE)
			//muhammed gökkaya lun
			return (new CElmProgReader(this, 6));
		throw new Exception("Luna sayaç okuma modunu desteklemiyor!");
	}

	CMeterReader CreateElmReader(int mode) throws Exception {
		if (mode == IDef.MBTMETERREADER_READOUT)
			return (new CReadOutReader(this, 200));
		else if (mode == IDef.MBTMETERREADER_LONGREADOUT)
			return (new CReadOutReader(this, 200));
		else if (mode == IDef.MBTMETERREADER_PROGRAMMODE)
			return (new CElmProgReader(this, 1));
		else if (mode == IDef.MBTMETERREADER_PROFILEMODE)
			return (new CElmProgReader(this, 6));
		throw new Exception("Elm sayaç okuma modunu desteklemiyor!");
	}

	int GetSupportedModes(String pFlag, String pIdentification) {
		if (pFlag.equalsIgnoreCase("VIK")) {
			int modes;

			modes = IDef.MBTMETERREADER_PROGRAMMODE | IDef.MBTMETERREADER_PROFILEMODE;
			if (IsNewViko(pIdentification))
				modes |= IDef.MBTMETERREADER_READOUT;
			return (modes);
		} else if (pFlag.equalsIgnoreCase("MSY")) {
			int modes;

			modes = IDef.MBTMETERREADER_READOUT | IDef.MBTMETERREADER_PROGRAMMODE | IDef.MBTMETERREADER_PROFILEMODE;
			if (pIdentification.startsWith("<1>M"))
				modes |= IDef.MBTMETERREADER_LONGREADOUT;
			return (modes);
		} else if (pFlag.equalsIgnoreCase("ELM")) {
			int modes;

			modes = IDef.MBTMETERREADER_READOUT;
			if (pIdentification.startsWith("<1>EC25TC"))
				modes |= IDef.MBTMETERREADER_PROGRAMMODE | IDef.MBTMETERREADER_PROFILEMODE;
			return (modes);
		} else if (pFlag.equalsIgnoreCase("LUN")) {
			return (IDef.MBTMETERREADER_READOUT | IDef.MBTMETERREADER_PROGRAMMODE | IDef.MBTMETERREADER_PROFILEMODE | IDef.MBTMETERREADER_LONGREADOUT);
		} else if (pFlag.equalsIgnoreCase("AMP")) {
			return (IDef.MBTMETERREADER_PROGRAMMODE);
		} else if (pFlag.equalsIgnoreCase("KHL")) {
			return (IDef.MBTMETERREADER_PROGRAMMODE | IDef.MBTMETERREADER_PROFILEMODE);
		} else if (pFlag.equals("AEL")) {
			return (IDef.MBTMETERREADER_PROGRAMMODE | IDef.MBTMETERREADER_PROFILEMODE);
		} else if (pFlag.equalsIgnoreCase("HLY")) {
			return (IDef.MBTMETERREADER_DLMS_COSEM);
		}
		return (IDef.MBTMETERREADER_READOUT);
	}

	boolean IsNewViko(String pIdentification) {
		return (pIdentification.contains("VEM") || pIdentification.startsWith("<1>D1E2"));
	}

}