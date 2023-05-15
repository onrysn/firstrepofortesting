package mobit.eemr;

import java.io.IOException;
import java.util.concurrent.ExecutorService;


public interface IMeterReader extends AutoCloseable {

	@Override
	public void close();
	
	void Connect(String BthAddr, String Pin, String logFile) throws Exception;
	void ConnectAsync(String BthAddr, String Pin, String logFile);
	public boolean IsConnected();
	void Reconnect() throws Exception;
	void ReconnectAsync();
	
	MbtMeterInformation GetInformation()throws Exception;
	void SetProbeEventHandler(IProbeEvent handler);
	MbtProbePowerStatus GetPowerStatus() throws IOException, Exception;
	void Disconnect();
	void read(IReadResult result) throws IOException, Exception;
	
	void SetLed(boolean enable) throws IOException, Exception;
	void SetAutoPowerOffTime(short duration) throws IOException, Exception;
	void AbortRead() throws Exception;
	int GetVersion() throws Exception;
	
	void setCallback(ICallback clb);
	
	Object getContext();
	Object getHandler();
	ExecutorService getExecutor();
	IProbeEvent getProbeEvent();
	boolean getSilent();
	void setSilent(boolean silent);
	void Trigger();
	void Retry(IReadResult result);
	IReadResult getReadResult();
	
	IBluetooth getBluetooth();

	boolean isTriggerEnabled();
	void setTriggerEnabled(boolean enabled);
	
}
