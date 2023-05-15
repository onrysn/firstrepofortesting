package mobit.eemr.base;

public interface IProbeEvent {

	void TriggerEventHandler(Object pContext);
	void ConnectionResetEventHandler(Object pContext);
	void PowerEventHandler(Object pContext);
	
}
