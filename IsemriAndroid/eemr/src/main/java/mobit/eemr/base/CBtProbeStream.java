package mobit.eemr.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class CBtProbeStream implements ICommStream {

	long m_InputTimeout;
	Object m_hInputEvent;
	volatile boolean m_Aborted;
	ICommStream m_pOutputStream;
	CCircularBuffer m_InputBuffer;

	public CBtProbeStream()
	{
		m_Aborted = false;
		m_pOutputStream = null;
		m_hInputEvent = new Object();
		m_InputBuffer = new CCircularBuffer();
		
	}

	@Override
	protected void finalize() throws Throwable
	{
		Close();
		super.finalize();
	}

	void Open(ICommStream pOutputStream) throws Exception
	{
		if (!m_InputBuffer.SetSize(1024))
			throw new Exception("Gelen veri bellek uzunluğu değiştirilemedi");
		m_pOutputStream = pOutputStream;
		
	}

	public void Close()
	{
		m_hInputEvent = null;
		m_pOutputStream = null;
	}

	public boolean IsAborted()
	{
		return(m_Aborted);
	}

	public void SetAborted(boolean aborted)
	{
		m_Aborted = aborted;
		if (aborted)
			synchronized(m_hInputEvent){m_hInputEvent.notify();}
			
	}

	public Object GetHandle()
	{
		return(null);
	}

	public void SetReadTimeout(long timeout)
	{
		m_InputTimeout = timeout;
		
	}
	
	public void Read(byte [] pBuffer, int off, int bytesToRead, int [] pBytesRead) throws Exception
	{
		
		long startTicks = utils.GetTickCount();
		while(m_InputBuffer.IsEmpty())
		{
			long timeSpent;

			if (m_Aborted) throw new Exception("Durdurulmuş!");
			
			timeSpent = utils.GetTickCount() - startTicks;
			if (timeSpent >= m_InputTimeout)
				throw new TimeoutException();
			
			synchronized(m_hInputEvent){ m_hInputEvent.wait(m_InputTimeout - timeSpent);}
				
		}

		if (m_Aborted) throw new Exception("Durdurulmuş!");

		pBytesRead[0] = m_InputBuffer.Read(pBuffer, off, bytesToRead);
		
	}
	@Override
	public void Read(byte[] pBuffer, int bytesToRead, int[] pBytesRead) throws Exception {
		// TODO Auto-generated method stub
		Read(pBuffer, 0, bytesToRead, pBytesRead);
	}

	@Override
	public void Write(byte[] pBuffer, int off, int bytesToWrite, int[] pBytesWritten) throws Exception {
		// TODO Auto-generated method stub
		if (m_Aborted) throw new Exception("Durdurulmuş!");
		m_pOutputStream.Write(pBuffer, off, bytesToWrite, pBytesWritten);
	}

	public void Write(byte [] pBuffer, int bytesToWrite, int [] pBytesWritten) throws Exception
	{
		Write(pBuffer, 0, bytesToWrite, pBytesWritten);
	}

	public void FlushWrite() throws Exception
	{
		if (m_Aborted)  throw new Exception("Durdurulmuş!");
		m_pOutputStream.FlushWrite();
	}

	public void ClearIoBuffers(boolean clearInput, boolean clearOutput) throws InterruptedException, IOException
	{
		if (clearInput)
		{
			while(!m_InputBuffer.IsEmpty())
			{
				m_InputBuffer.SetEmpty();
				Thread.sleep(500);
			}
		}
		m_pOutputStream.ClearIoBuffers(false, clearOutput);
	}

	void QueueInput(byte [] pBuffer, int dataLen) throws Exception
	{
		
		int bufPos = 0;
		while( bufPos == dataLen)
		{
			bufPos += m_InputBuffer.Write(pBuffer, bufPos, 1);
			if(m_Aborted) throw new Exception("İptal edildi!");
			Thread.sleep(10);
		}	
		if (bufPos == dataLen)
			synchronized(m_hInputEvent){ m_hInputEvent.notify();}	
		
	}

	

}
