package mobit.eemr;

import java.io.IOException;
import java.util.UUID;

import mobit.eemr.IBluetooth;

public class BluecoveBluetooth extends IBluetooth {

	//private StreamConnection m_streamConnection = null;

	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();	
	}
	
	@Override
	public void Open(UUID sdpid, String BthAddr, String Pin) throws IOException {
		// TODO Auto-generated method stub
		//String connectionURL = String.format("btspp://%s:1;authenticate=false;encrypt=false;master=false", BthAddr);
		/*
		m_streamConnection = (StreamConnection)Connector.open(connectionURL); 
		m_output = m_streamConnection.openOutputStream(); 
		m_input = m_streamConnection.openInputStream(); 
		*/
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

}
