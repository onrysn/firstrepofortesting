package mobit.eemr.base;

import java.util.Arrays;

public class CCircularBuffer {

	byte[] m_pMemory;
	int m_EndPos;
	int m_StartPos;
	int m_MemorySize;

	CCircularBuffer() {
		m_EndPos = 0;
		m_StartPos = 0;
		m_MemorySize = 0;
		m_pMemory = new byte[m_MemorySize];
	}

	protected void finalize() throws Throwable {
		if (m_pMemory != null)
			m_pMemory = null;
		
		super.finalize();
	}

	boolean SetSize(int memorySize) {
		m_pMemory = Arrays.copyOf(m_pMemory, memorySize);
		m_MemorySize = memorySize;
		m_StartPos = m_EndPos;
		return (true);
	}

	int Read(byte[] pBuffer, int off, int bytesToRead) {
		if (bytesToRead == 1) {
			if (IsEmpty())
				return (0);

			pBuffer[off] = m_pMemory[m_StartPos];
			// *static_cast<BYTE*>(pBuffer) = *(static_cast<const
			// BYTE*>(m_pMemory) + m_StartPos);

			m_StartPos = (m_StartPos + 1) & (m_MemorySize - 1);
		} else {
			int endPos, dataSize;

			endPos = m_EndPos;
			if (m_StartPos <= endPos)
				dataSize = endPos - m_StartPos;
			else
				dataSize = (m_MemorySize - m_StartPos) + endPos;

			if (bytesToRead > dataSize)
				bytesToRead = dataSize;

			if (m_StartPos + bytesToRead >= m_MemorySize) {
				int size;

				size = m_MemorySize - m_StartPos;
				System.arraycopy(m_pMemory, m_StartPos, pBuffer, off, size);
				// memcpy(pBuffer, static_cast<const BYTE*>(m_pMemory) +
				// m_StartPos, size);
				System.arraycopy(m_pMemory, 0, pBuffer, size, bytesToRead - size);
				// memcpy(static_cast<BYTE*>(pBuffer) + size, static_cast<const
				// BYTE*>(m_pMemory), bytesToRead - size);
				m_StartPos = bytesToRead - size;
			} else {
				System.arraycopy(m_pMemory, m_StartPos, pBuffer, off, bytesToRead);
				// memcpy(pBuffer, static_cast<const BYTE*>(m_pMemory) +
				// m_StartPos, bytesToRead);
				m_StartPos += bytesToRead;
			}
		}
		return (bytesToRead);
	}

	public int Read(byte[] pBuffer, int bytesToRead) {
		return Read(pBuffer, 0, bytesToRead);
	}

	public int Write(byte[] pBuffer, int off, int bytesToWrite) {
		if (bytesToWrite == 1) {
			if (IsFull())
				return (0);

			m_pMemory[m_EndPos] = pBuffer[off];
			// *((BYTE*)m_pMemory + m_EndPos) = *(BYTE*)pBuffer;

			m_EndPos = (m_EndPos + 1) & (m_MemorySize - 1);
		} else {
			int startPos, dataSize, remSize;

			startPos = m_StartPos;
			if (startPos <= m_EndPos)
				dataSize = m_EndPos - startPos;
			else
				dataSize = (m_MemorySize - startPos) + m_EndPos;

			remSize = (m_MemorySize - 1) - dataSize;
			if (bytesToWrite > remSize)
				bytesToWrite = remSize;

			if (m_EndPos + bytesToWrite >= m_MemorySize) {
				int size;

				size = m_MemorySize - m_EndPos;
				System.arraycopy(pBuffer, off, m_pMemory, m_EndPos, size);
				// memcpy(static_cast<BYTE*>(m_pMemory) + m_EndPos, pBuffer,
				// size);
				System.arraycopy(pBuffer, size, m_pMemory, 0, bytesToWrite - size);
				// memcpy(m_pMemory, static_cast<const BYTE*>(pBuffer) + size,
				// bytesToWrite - size);
				m_EndPos = bytesToWrite - size;
			} else {
				System.arraycopy(pBuffer, off, m_pMemory, m_EndPos, bytesToWrite);
				// memcpy(static_cast<BYTE*>(m_pMemory) + m_EndPos, pBuffer,
				// bytesToWrite);
				m_EndPos += bytesToWrite;
			}
		}
		return (bytesToWrite);
	}

	public int Write(byte[] pBuffer, int bytesToWrite) {
		return Write(pBuffer, 0, bytesToWrite);
	}

	public boolean IsFull() {
		return (((m_EndPos + 1) & (m_MemorySize - 1)) == m_StartPos);
	}

	public boolean IsEmpty() {
		return (m_StartPos == m_EndPos);
	}

	public void SetEmpty() {
		m_StartPos = m_EndPos;
	}
}
