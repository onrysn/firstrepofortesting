package mobit.eemr.base;

public class CReadOutReader extends CMeterReader {

	boolean m_ChkBcc;
	int m_DelayForOptSel;
	short m_Mode;

	public CReadOutReader(CElecMeter pElecMeter, int delayForOptSel, boolean chkBcc) {
		super(pElecMeter);
		//muhammed gökkaya
 		m_Mode = 0;
		m_ChkBcc = chkBcc;
		m_DelayForOptSel = delayForOptSel;
	}
	
	public CReadOutReader(CElecMeter pElecMeter, int delayForOptSel) {
		this(pElecMeter, delayForOptSel, true);
	}
	//muhammed
	public CReadOutReader(CElecMeter pElecMeter) {
		this(pElecMeter, 0);
	}

	
	@Override
	public void Open() throws Exception {
		if (m_DelayForOptSel != 0)
			Thread.sleep(m_DelayForOptSel);
		m_IEC62056.WriteOptionSelectMessage(0, GetElecMeter().GetInformation().baudRateId, m_Mode);
		GetElecMeter().GetOptoProbe().IrSetConfig(GetElecMeter().GetInformation().baudRateId, 7, IDef.EVENPARITY);
	}

	@Override
	public boolean Read() throws Exception {
		return m_IEC62056.ReadDataMessage(m_ValueBuffer, this, this, (char)CIEC62056.ETX, m_ChkBcc);
	}

}
