package mobit.eemr.base;

import mobit.eemr.MbtProbePowerStatus;

public class CStdOptoProbe implements IOptoProbe {

	private ICommStream m_pStream;
	
	@Override
	public void Open(String pDevice, String pParameter, String pLogFileName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void Close() {
		// TODO Auto-generated method stub

	}

	@Override
	public int GetType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ICommStream GetStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void IrEnable(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void IrSetConfig(int baudRateId, int byteSize, int parity) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetLed(boolean enable) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public int GetVersion() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void SetAutoPowerOffTime(int duration) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public MbtProbePowerStatus GetPowerStatus() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized void SetProbeEventHandler(IProbeEvent pProbeEventHandler) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
