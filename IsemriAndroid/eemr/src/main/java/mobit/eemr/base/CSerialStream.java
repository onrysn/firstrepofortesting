package mobit.eemr.base;

import java.io.IOException;

public class CSerialStream implements ICommStream {

	Object m_hDevice;
	long m_ReadTimeout;
	int m_ReadBufPos;
	volatile boolean m_Aborted;
	CDynamicBuffer m_ReadBuffer;
	CDynamicBuffer m_WriteBuffer;
	
	
	public CSerialStream()
	{
		m_ReadBufPos = 0;
		m_Aborted = false;
		m_ReadTimeout = 0;
		m_hDevice = null;
	}

	protected void finilize() throws Throwable
	{
		Close();
		super.finalize();
	}

	private void CloseHandle(Object hDevice)
	{
		
	}
	
	public boolean Open(String pDevice, int readBufSize, int writeBufSize)
	{
		Object hDevice;

		hDevice = CreateFile(pDevice, GENERIC_READ|GENERIC_WRITE, 0, 
			null, OPEN_EXISTING, 0, null);
		if (hDevice == null)
			return(false);

		m_ReadBuffer.SetDataSize(0);
		m_ReadBuffer.SetSize(readBufSize);
		
		m_WriteBuffer.SetDataSize(0);
		m_WriteBuffer.SetSize(writeBufSize);

		m_hDevice = hDevice;
		m_ReadBufPos = 0;
		return(true);
	}

	public void Close()
	{
		if (m_hDevice == null)
			return;

		CloseHandle(m_hDevice);
		m_hDevice = null;
	}

	public boolean IsAborted()
	{
		return(m_Aborted);
	}

	public void SetAborted(boolean aborted) throws IOException
	{
		m_Aborted = aborted;
		if (aborted)
			PurgeComm(m_hDevice, PURGE_RXABORT);
	}

	public Object GetHandle()
	{
		return(m_hDevice);
	}

			
		
	public void ClearIoBuffers(boolean clearInput, boolean clearOutput) throws IOException
	{
		long flags;

		flags = 0;
		if (clearInput)
		{
			m_ReadBufPos = 0;
			m_ReadBuffer.SetDataSize(0);
			flags |= PURGE_RXCLEAR;
		}

		if (clearOutput)
		{
			m_WriteBuffer.SetDataSize(0);
			flags |= PURGE_TXCLEAR;
		}

		PurgeComm(m_hDevice, flags);
	}

	public void Read(byte [] pBuffer, int off, int bytesToRead, int [] pBytesRead) throws Exception
	{
		if (m_Aborted) throw new Exception("Durdurulmuş!");

		if (m_ReadBufPos == m_ReadBuffer.GetDataSize())
		{
			long tickCount;

			int [] readLength = new int[]{0};
			tickCount = System.currentTimeMillis();
			while(System.currentTimeMillis() - tickCount < m_ReadTimeout)
			{
				if (m_Aborted) throw new Exception("Durdurulmuş");

				ReadFile(m_hDevice, m_ReadBuffer.GetMemory(), 0, 
						m_ReadBuffer.GetSize(), readLength, null);
				
				if (readLength[0] != 0)
					break;

				Thread.sleep(10);
			}

			if (readLength[0] == 0)
			{
				pBytesRead[0] = 0;
				return;
			}

			m_ReadBuffer.SetDataSize(readLength[0]);
			m_ReadBufPos = 0;
		}

		if (bytesToRead == 1)
		{
			pBuffer[off] = m_ReadBuffer.GetMemory()[m_ReadBufPos];
		}
		else
		{
			int remSize;

			remSize = m_ReadBuffer.GetDataSize() - m_ReadBufPos;
			if (bytesToRead > remSize)
				bytesToRead = remSize;
		
			//memcpy(pBuffer, static_cast<const BYTE*>(m_ReadBuffer.GetMemory()) + m_ReadBufPos, bytesToRead);
			System.arraycopy(m_ReadBuffer.GetMemory(), m_ReadBufPos, pBuffer, off, bytesToRead);
		}

		m_ReadBufPos += bytesToRead;
		pBytesRead[0] = bytesToRead;
		
	}
	
	public void Read(byte [] pBuffer, int bytesToRead, int [] pBytesRead) throws Exception
	{
		Read(pBuffer, 0, bytesToRead, pBytesRead);
	}

	public void Write(byte [] pBuffer, int off, int bytesToWrite, 
		int [] pBytesWritten) throws Exception
	{
		if (m_Aborted) throw new Exception("Durdurulmuş!");
			

		if (m_WriteBuffer.GetDataSize() == m_WriteBuffer.GetSize())
		{
			FlushWrite(); 
		}

		if (bytesToWrite == 1)
		{
			//*(static_cast<BYTE*>(m_WriteBuffer.GetMemory()) + m_WriteBuffer.GetDataSize()) = *static_cast<const BYTE*>(pBuffer);
			m_ReadBuffer.GetMemory()[m_ReadBufPos] = pBuffer[off];
		}
		else
		{
			int remSize;

			remSize = m_WriteBuffer.GetSize() - m_WriteBuffer.GetDataSize();
			if (bytesToWrite > remSize)
				bytesToWrite = remSize;

			//memcpy(static_cast<BYTE*>(m_WriteBuffer.GetMemory()) + m_WriteBuffer.GetDataSize(), pBuffer, bytesToWrite);
			System.arraycopy(pBuffer, off, m_ReadBuffer.GetMemory(), m_WriteBuffer.GetDataSize(), bytesToWrite);
		}

		m_WriteBuffer.SetDataSize(m_WriteBuffer.GetDataSize() + bytesToWrite);
		pBytesWritten[0] = bytesToWrite;
		
	}

	public void FlushWrite() throws Exception
	{
		int totalWritten;

		if (m_Aborted) throw new Exception("Durdurulmuş!");
			

		totalWritten = 0;
		while(totalWritten < m_WriteBuffer.GetDataSize())
		{
			int [] bytesWritten = new int[]{0};

			WriteFile(m_hDevice, m_WriteBuffer.GetMemory(), 
				totalWritten, m_WriteBuffer.GetDataSize() - totalWritten, bytesWritten, null);

			if (bytesWritten[0] == 0)
				throw new Exception("Porta yazılamadı!");
			
			totalWritten += bytesWritten[0];
		}

		m_WriteBuffer.SetDataSize(0);
		
	}

	public void Write(byte [] pBuffer, int bytesToWrite, int [] pBytesWritten) throws Exception
	{
		Write(pBuffer, 0, bytesToWrite, pBytesWritten);
	}
	
	public void SetReadTimeout(long timeout) throws IOException
	{
		COMMTIMEOUTS cto = new COMMTIMEOUTS();

		GetCommTimeouts(m_hDevice, cto);
		
		if (timeout == INFINITE)
		{
			/* Special case */
			cto.ReadIntervalTimeout = 1;
			cto.ReadTotalTimeoutMultiplier = 0;
			cto.ReadTotalTimeoutConstant = MAXDWORD / 2;
		}
		else
		{
			cto.ReadTotalTimeoutConstant = 0;
			cto.ReadTotalTimeoutMultiplier = 0;
			cto.ReadIntervalTimeout = MAXDWORD;
		}

		SetCommTimeouts(m_hDevice, cto);
		
		m_ReadTimeout = timeout;
		
	}

	//----------------------------------------------------------
	
	private static final int INFINITE = 0;
	private static final long MAXDWORD = 0xffffffff;
	private static final int PURGE_RXABORT = 0;
	private static final int PURGE_RXCLEAR = 0;
	private static final int PURGE_TXCLEAR = 0;
	
	private static final int GENERIC_READ = 0;
	private static final int GENERIC_WRITE = 0;
	private static final int OPEN_EXISTING = 0;
	
	

	class COMMTIMEOUTS {
		public long ReadIntervalTimeout;
		public long ReadTotalTimeoutMultiplier;
		public long ReadTotalTimeoutConstant;
	};
	
	Object CreateFile(String pDevice, int flag, int x1, Object x2, int flag2, int flag3, Object x3){
		
		return null;
	}
	
	void PurgeComm(Object hDevice, long flags) throws IOException
	{
		
	}
	void ReadFile(Object hDevice, byte [] buffer, int start, int read, int [] readLength, Object x) throws IOException
	{
		
	}
	void WriteFile(Object hDevice, byte [] buffer, int start, int write, int [] bytesWritten, Object x) throws IOException
	{
		
	}
	void GetCommTimeouts(Object hDevice, COMMTIMEOUTS cto) throws IOException
	{
	}
	void SetCommTimeouts(Object hDevice, COMMTIMEOUTS cto)
	{
		
	}
		
		

}

