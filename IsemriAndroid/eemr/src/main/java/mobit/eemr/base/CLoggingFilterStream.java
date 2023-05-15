package mobit.eemr.base;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CLoggingFilterStream implements ICommStream {
	
	class CommLogChannel
	{
		long tickCount;
		String pName;
		long startTickCount;
		Date startTime;
		CDynamicBuffer buffer;
		
	};

	
	BufferedWriter m_hLogFile;
	Object m_Cr;
	Thread m_hWorkerThread;
	ICommStream m_pStream;
	CDynamicBuffer m_Buffer;
	CommLogChannel m_InputChannel;
	CommLogChannel m_OutputChannel;
	Object m_timer;
	boolean m_autoflush;
	
	public CLoggingFilterStream(ICommStream pStream)
	{
		m_pStream = pStream;
		m_hWorkerThread = null;
		m_InputChannel.pName = "<=";
		m_OutputChannel.pName = "=>";
		m_Cr = new Object();
		m_hLogFile = null;
		m_timer = new Object();
		m_autoflush = false;
	}

	protected void finilize()
	{
		Close();
		m_pStream.Close();
		m_Cr = null;
	}

	public boolean Open(String pFileName) throws Exception
	{
		try {
		
			m_hLogFile = new BufferedWriter(new FileWriter(pFileName, true));
			m_hWorkerThread = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					WorkerThread();
				}
				
			});
		
		}
		catch(Exception e) {
			if(m_hLogFile != null){
				m_hLogFile.close();
				m_hLogFile = null;
			}
			throw e;
		}
		return(true);
	}

	public void Close()
	{
		
		m_autoflush = false;
		
		if (m_hLogFile == null)
			return;

		if(m_pStream != null) m_pStream.Close();
		
		if(m_timer != null) synchronized(m_timer){ m_timer.notify();}
		
		if(m_hWorkerThread != null && m_hWorkerThread.isAlive()){
			try {
				
				m_hWorkerThread.join(1500);
				if(m_hWorkerThread.isAlive()) m_hWorkerThread.interrupt();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		try {
			
			m_hLogFile.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_hLogFile = null;
	}

	public boolean IsAborted()
	{
		return(m_pStream.IsAborted());
	}

	public void SetAborted(boolean aborted) throws IOException
	{
		m_pStream.SetAborted(aborted);
	}

	public Object GetHandle()
	{
		return(m_pStream.GetHandle());
	}

	public void SetReadTimeout(long timeout) throws IOException
	{
		m_pStream.SetReadTimeout(timeout);
	}

	@Override
	public void Read(byte[] pBuffer, int off, int bytesToRead, int[] pBytesRead) throws Exception {
		// TODO Auto-generated method stub
		m_pStream.Read(pBuffer, bytesToRead, pBytesRead);
		LogData(m_InputChannel, pBuffer, pBytesRead[0]);
	}
	
	public void Read(byte [] pBuffer, int bytesToRead, int [] pBytesRead) throws Exception
	{
		Read(pBuffer, 0, bytesToRead, pBytesRead);
	}

	@Override
	public void Write(byte[] pBuffer, int off, int bytesToWrite, int[] pBytesWritten) throws Exception {
		// TODO Auto-generated method stub
		m_pStream.Write(pBuffer, bytesToWrite, pBytesWritten);
		LogData(m_OutputChannel, pBuffer, pBytesWritten[0]);
	}
	public void Write(byte [] pBuffer, int bytesToWrite, int [] pBytesWritten) throws Exception
	{
		Write(pBuffer, 0, bytesToWrite, pBytesWritten);
	}

	public void FlushWrite() throws Exception
	{
		FlushChannel(m_InputChannel, true);
		FlushChannel(m_OutputChannel, true);
		
		m_pStream.FlushWrite();
	}

	public void ClearIoBuffers(boolean clearInput, boolean clearOutput) throws InterruptedException, IOException
	{
		m_pStream.ClearIoBuffers(clearInput, clearOutput);
	}

	public void FlushChannel(CommLogChannel pLogChannel, boolean forge) throws Exception
	{
		
		synchronized(m_Cr)
		{
			long tickCount;
			
			if (pLogChannel.buffer.GetDataSize() == 0)
				return;
			

			tickCount = System.currentTimeMillis();
			if (!forge && (tickCount - pLogChannel.tickCount) < 1000)
			{
				return;
				
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pLogChannel.startTime);
			
			m_Buffer.SetDataSize(0);
			String value = String.format("[%04d-%02d-%02d %02d:%02d:%02d (%08x) %s]: ", 
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
					calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 
					calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), 
				pLogChannel.startTickCount, pLogChannel.pName);
			byte [] b = value.getBytes();
			
			m_Buffer.Append(b, b.length);
			
			AppendLogData(m_Buffer, pLogChannel.buffer.GetMemory(), 
				pLogChannel.buffer.GetDataSize());
			

			b = "\r\n".getBytes();
			m_Buffer.Append(b, b.length);

			m_hLogFile.write(new String(m_Buffer.GetMemory()));
			
			pLogChannel.buffer.SetDataSize(0);
			
		}
		
		
	}

	public void LogData(CommLogChannel pLogChannel, byte [] pData, int dataSize) throws Exception
	{
		synchronized(m_Cr){
		
			if (pLogChannel.buffer.GetDataSize() == 0)
			{
				pLogChannel.startTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
				pLogChannel.startTickCount = System.currentTimeMillis();
			}

			pLogChannel.tickCount = System.currentTimeMillis();
			pLogChannel.buffer.Append(pData, dataSize);
		}
	}

	public void AppendLogData(CDynamicBuffer pBuffer, byte [] pData, int  dataSize) throws Exception
	{
		int index;

		for(index = 0; index < dataSize; ++index)
		{
			byte chr;
			byte [] value;

			chr = pData[index];
			value = String.format("%02x ", chr).getBytes();
			pBuffer.Append(value, value.length);
	/*		if (isprint(chr))
			{
				result = pBuffer->Append(&chr, sizeof(chr));
			}
			else
			{
				char value[MAX_PATH + 1];

				switch(chr)
				{
				case '\r': result = pBuffer->Append("{cr}", 4); break;
				case '\n': result = pBuffer->Append("{lf}", 4); break;
				case '\x01': result = pBuffer->Append("{soh}", 5); break;
				case '\x02': result = pBuffer->Append("{stx}", 5); break;
				case '\x03': result = pBuffer->Append("{etx}", 5); break;
				case '\x04': result = pBuffer->Append("{eot}", 5); break;
				case '\x06': result = pBuffer->Append("{ack}", 5); break;
				case '\x15': result = pBuffer->Append("{nak}", 5); break;
				default: result = pBuffer->Append(value, sprintf(value, 
					"{0x%02x}", chr));
				}
			}
	*/
			
		}
	}

	
	private long WorkerThread()
	{
		m_autoflush = true;
		try
		{
			while(m_autoflush && m_timer != null)
			{
				synchronized(m_timer){ m_timer.wait(1000);}
				if(!m_autoflush) break;
				FlushChannel(m_InputChannel, false);
				FlushChannel(m_OutputChannel, false);
			}
		}
		catch(Exception e)
		{
			m_autoflush = false;
			return 1;
		}
		return(0);
	}

	



}
