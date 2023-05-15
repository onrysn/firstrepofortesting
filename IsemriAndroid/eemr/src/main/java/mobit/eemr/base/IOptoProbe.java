package mobit.eemr.base;

import mobit.eemr.MbtProbePowerStatus;

public interface IOptoProbe {
	
	void Open(String pDevice, String pParameter, String pLogFileName) throws Exception;
	void Close();

	int GetType();
	ICommStream GetStream();
	void IrEnable(boolean enable) throws Exception;
	void IrSetConfig(int baudRateId, int byteSize, int parity) throws Exception;

	void SetLed(boolean enable) throws Exception;
	int GetVersion() throws Exception;
	void SetAutoPowerOffTime(int duration) throws Exception;
	MbtProbePowerStatus GetPowerStatus() throws Exception;
	void SetProbeEventHandler(IProbeEvent pProbeEventHandler)throws Exception;
	
}
