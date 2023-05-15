package mobit.eemr;

import java.util.Hashtable;
import java.util.Map;

public class ProbeEvent implements IProbeEvent {

	
	private static final Map<Integer, IProbeEvent> eventList = new Hashtable<Integer, IProbeEvent>();
	
	public static void addEvent(int sessionId, IProbeEvent event) {
		eventList.put(sessionId, event);
	}

	public static void removeEvent(Integer sessionId) {
		eventList.remove(sessionId);
	}

	public static IProbeEvent getEvent(Integer sessionId) {
		return eventList.get(sessionId);
	}
	
	protected final int sessionId = System.identityHashCode(this);
	
	protected IMeterReader mr = null;
	protected Object context = null;

	protected ICallback clb = null;

	public ProbeEvent(IMeterReader mr) {

		addEvent(sessionId, this);
		
		this.mr = mr;
		context = mr.getContext();
	
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}

	@Override
	public IMeterReader getMeterReader() {
		return mr;
	}

	@Override
	public void setCallback(ICallback clb) {
		this.clb = clb;
	}

	@Override
	public synchronized void setMeterReader(IMeterReader mr) {
		this.mr = mr;
	}

	@Override
	public void PowerEvent() {

	}

	@Override
	public void TriggerEvent(IReadResult result) {

	}

	@Override
	public void ConnectionResetEvent() {

		try {
			mr.Reconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
