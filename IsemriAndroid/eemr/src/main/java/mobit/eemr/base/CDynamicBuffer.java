package mobit.eemr.base;

import java.util.Arrays;

public class CDynamicBuffer {

	byte[] m_pMemory;
	int m_GrowBy;
	int m_DataSize;
	
	public int GetSize() {
		return m_pMemory.length;
	}

	public int GetDataSize() {
		return (m_DataSize);
	}

	public void SetDataSize(int dataSize) {
		m_DataSize = dataSize;
	}

	public void Grow() {
		
		SetSize(GetSize() + m_GrowBy);
	}

	public void SetGrowBy(int growBy) {
		m_GrowBy = growBy;
	}

	public byte[] GetMemory() {
		if(GetSize() == 0) SetSize(m_GrowBy);
		return (m_pMemory);
	}
	
	public CDynamicBuffer()
	{
		m_GrowBy = 16;
		m_DataSize = 0;
		m_pMemory = new byte[m_GrowBy];
	}

	@Override
	protected void finalize() throws Throwable
	{
		if (m_pMemory != null)
			m_pMemory = null;
		
		super.finalize();
	}

	public void SetSize(int memorySize)
	{
		if (memorySize == 0)
		{
			m_pMemory = null;
		}
		else
		{		
			m_pMemory = Arrays.copyOf(m_pMemory, memorySize);
		}
		
		if (m_DataSize > memorySize)
			m_DataSize = memorySize;
	}

	public void Append(byte [] pData, int dataSize) throws Exception
	{
		if (GetSize() < GetDataSize() + dataSize)
		{
			int growSize;

			growSize = (GetDataSize() + dataSize) - GetSize();
			if (growSize < m_GrowBy)
				growSize = m_GrowBy;

			SetSize(GetSize() + growSize);
		}
		for(int i = 0; i < pData.length; i++)
			m_pMemory[m_DataSize++] = pData[i];
			
		
	}
	
	public void Append(byte [] pData) throws Exception
	{
		Append(pData, pData.length);
	}
	
	public void AppendTChar(char data) throws Exception
	{
		byte [] b = new byte[]{(byte)(data & 0xff), (byte)(data >> 8)};
		Append(b, b.length);
	}

	public void Append(char [] pData) throws Exception
	{
		Append(utils.charsToBytes(pData));
	}
	
}
