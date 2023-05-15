package mobit.eemr.base;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import mobit.eemr.IBluetooth;

public class CBtSocketStream extends IBluetooth implements ICommStream {
	Socket m_hSocket;
	long m_RecvTimeout;
	int m_ReadBufPos;
	Object m_hAbortEvent;
	volatile boolean m_Aborted;
	CDynamicBuffer m_ReadBuffer;
	CDynamicBuffer m_WriteBuffer;

	public CBtSocketStream() {

		m_ReadBufPos = 0;
		m_Aborted = false;
		m_hAbortEvent = new Object();
		m_RecvTimeout = utils.INFINITE;
		m_hSocket = null;
		m_ReadBuffer = new CDynamicBuffer();
		m_WriteBuffer = new CDynamicBuffer();
	}

	@Override
	protected void finalize() throws Throwable {
		Close();
		super.finalize();
	}

	protected void Connect(Socket hSocket, BTH_ADDR pBthAddr, int channel, String pWidePin) {

	}

	public void Open(BTH_ADDR pBthAddr, int channel, String pPin, int readBufSize, int writeBufSize)
			throws Exception {

		Close();

		Socket hSocket = null;

		try {
			
			hSocket = new Socket();
			Connect(hSocket, pBthAddr, channel, pPin);

			m_ReadBuffer.SetDataSize(0);
			m_ReadBuffer.SetSize(readBufSize);
			m_WriteBuffer.SetDataSize(0);
			m_WriteBuffer.SetSize(writeBufSize);

			m_hSocket = hSocket;
			m_ReadBufPos = 0;
			m_input = hSocket.getInputStream();
			m_output = hSocket.getOutputStream();
			
		} catch (Exception e) {

			Close();
			throw e;
			
		}
	}

	@Override
	public void Close() {

		if (m_hAbortEvent != null) {
			synchronized(m_hAbortEvent) {m_hAbortEvent.notify();}
		}
		if (m_hSocket != null) {
			try {
				m_hSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_hSocket = null;
		}

		super.close();
	}

	@Override
	public boolean IsAborted() {
		return (m_Aborted);
	}

	@Override
	public void SetAborted(boolean aborted) {
		m_Aborted = aborted;
		if (aborted)
			synchronized(m_hAbortEvent) {m_hAbortEvent.notify();}

	}

	@Override
	public Object GetHandle() {
		return (m_hSocket);
	}

	@Override
	public void SetReadTimeout(long timeout) {
		m_RecvTimeout = timeout;
	}

	@Override
	public void Read(byte[] pBuffer, int off, int bytesToRead, int[] pBytesRead) throws Exception {
		
		if (m_Aborted)
			throw new Exception("Durdurulmuş!");

		pBytesRead[0] = 0;
		if (m_ReadBufPos == m_ReadBuffer.GetDataSize()) {
			long received = m_input.read(m_ReadBuffer.GetMemory(), 
					0, m_ReadBuffer.GetSize());
			m_ReadBuffer.SetDataSize((int) received);
			m_ReadBufPos = 0;
		}

		if (bytesToRead == 1) {
			pBuffer[off] = m_ReadBuffer.GetMemory()[m_ReadBufPos];

		} else {
			int remSize;

			remSize = m_ReadBuffer.GetDataSize() - m_ReadBufPos;
			if (bytesToRead > remSize)
				bytesToRead = remSize;

			System.arraycopy(m_ReadBuffer.GetMemory(), m_ReadBufPos, pBuffer, off, bytesToRead);
			// memcpy(pBuffer, static_cast<const
			// char*>(m_ReadBuffer.GetMemory()) +
			// m_ReadBufPos, bytesToRead);
		}

		m_ReadBufPos += bytesToRead;
		pBytesRead[0] = bytesToRead;

	}

	public void Read(byte[] pBuffer, int bytesToRead, int[] pBytesRead) throws Exception {
		Read(pBuffer, 0, bytesToRead, pBytesRead);
	}

	@Override
	public void Write(byte[] pBuffer, int off, int bytesToWrite, int[] pBytesWritten) throws Exception {
		if (m_Aborted)
			throw new Exception("Durdurulmuş!");

		if (m_WriteBuffer.GetDataSize() == m_WriteBuffer.GetSize()) {
			FlushWrite();
		}

		if (bytesToWrite == 1) {
			m_WriteBuffer.GetMemory()[m_WriteBuffer.GetDataSize()] = pBuffer[off];

		} else {
			int remSize;

			remSize = m_WriteBuffer.GetSize() - m_WriteBuffer.GetDataSize();
			if (bytesToWrite > remSize)
				bytesToWrite = remSize;

			System.arraycopy(pBuffer, off, m_WriteBuffer.GetMemory(), m_WriteBuffer.GetDataSize(), bytesToWrite);
			// memcpy(static_cast<char*>(m_WriteBuffer.GetMemory()) +
			// m_WriteBuffer.GetDataSize(),
			// pBuffer, bytesToWrite);
		}

		m_WriteBuffer.SetDataSize(m_WriteBuffer.GetDataSize() + bytesToWrite);
		pBytesWritten[0] = bytesToWrite;

	}

	public void Write(byte[] pBuffer, int bytesToWrite, int[] pBytesWritten) throws Exception {
		Write(pBuffer, 0, bytesToWrite, pBytesWritten);
	}

	@Override
	public void FlushWrite() throws IOException {
		int totalWritten;

		if (m_Aborted)
			return;

		totalWritten = 0;
		while (totalWritten < m_WriteBuffer.GetDataSize()) {
			int sendRes;
			sendRes = m_WriteBuffer.GetDataSize() - totalWritten;
			m_output.write(m_WriteBuffer.GetMemory(), totalWritten, sendRes);
			totalWritten += sendRes;
		}

		m_WriteBuffer.SetDataSize(0);

	}

	@Override
	public void ClearIoBuffers(boolean clearInput, boolean clearOutput) throws IOException {
		if (clearInput) {
			m_ReadBufPos = 0;
			m_ReadBuffer.SetDataSize(0);
			while (m_input.available() > 0) {
				m_input.read(m_ReadBuffer.GetMemory(), 0, m_ReadBuffer.GetSize());
			}
		}
	}

	@Override
	public void Open(UUID sdpid, String BthAddr, String Pin) throws Exception {
		// TODO Auto-generated method stub
		Open(utils.ParseBthAddr(BthAddr), 0, Pin, 0, 0);
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

};
