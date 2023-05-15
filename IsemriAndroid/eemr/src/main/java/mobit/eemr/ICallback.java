package mobit.eemr;

public interface ICallback {

	void Opened();
	void Failed();
	void BeginRead(IReadResult result);
	void EndRead();
	void Read(IProbeEvent event, IReadResult result);
	void PowerEvent(IProbeEvent event);
	void ConnectionResetEvent(IProbeEvent event);
}
