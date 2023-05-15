package mobit.eemr;


public interface IProbeEvent {

		
	public IMeterReader getMeterReader();
	public void setCallback(ICallback clb);
	public void setMeterReader(IMeterReader mr);
	
	public void PowerEvent();
	public void TriggerEvent(IReadResult result);
	public void ConnectionResetEvent();	
}
