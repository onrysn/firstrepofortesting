package mobit.eemr.base;

import java.util.Date;

import mobit.eemr.IReadEvent;

public abstract class CMeterReader implements IReadEvent {	
	
	protected CIEC62056 m_IEC62056;
	protected CElecMeter m_pElecMeter;
	protected CDynamicBuffer m_ValueBuffer;
	protected CDynamicBuffer m_SpareBuffer;
	
	protected IReadEvent m_pReadEventHandler;
	
	public CMeterReader(CElecMeter pElecMeter)
	{
		m_IEC62056 = new CIEC62056(pElecMeter.GetOptoProbe().GetStream());
		m_ValueBuffer = new CDynamicBuffer();
		m_SpareBuffer = new CDynamicBuffer();
		
		m_pElecMeter = pElecMeter;
		m_ValueBuffer.SetGrowBy(64);
		m_SpareBuffer.SetGrowBy(64);
		
		m_pReadEventHandler = this;
	
	}
	
	public abstract void Open() throws Exception;
	public abstract boolean Read() throws Exception;
	
	public void Close()
	{
	}
	
	public CElecMeter GetElecMeter()
	{
		return(m_pElecMeter);
	}

	public void SetReadEventHandler(IReadEvent pReadEventHandler)
	{
		if (pReadEventHandler != null)
			m_pReadEventHandler = pReadEventHandler;
		else
			m_pReadEventHandler = this;
	}
	
	
	boolean ReadProfiles(int flags, Date pStartTime, Date pEndTime)  throws Exception
	{
		return(false);
	}

	boolean ReadAddressList(char cti, AddressMapEntry [] pAddressMap) throws Exception
	{
		int [] pPosition = new int[1];
		String [] address = new String[1];

		pPosition = null;
		while(NextAddress(pPosition, address))
		{
			int result;
			String pAddress;

			if (pAddressMap != null)
			{
				pAddress = utils.FindAddress(pAddressMap, address[0]);
				if (pAddress == null)
					continue;
			}
			else
			{
				pAddress = address[0];
			}

			result = QueryDataBlockEx(cti, pAddress, m_ValueBuffer);
			if (result > 0)
			{
				if (!TranslateDataBlock(address[0], pAddress, m_ValueBuffer))
					return(false);

				result = DataBlockHandler(address[0], m_ValueBuffer);
				if (result != 0)
					return(result > 0);
			}
			else
			{
				if (GetElecMeter().GetOptoProbe().GetStream().IsAborted())
					return(false);

				if (result < 0)
					return(false);
			}
		}
		return(true);
	}

	boolean QueryDataBlock(char cti, String pAddress, CDynamicBuffer pDataBlock) throws Exception
	{
		m_IEC62056.WriteProgrammingCommandMessage('R', cti, pAddress);
		return(m_IEC62056.ReadDataMessage(pDataBlock, true));
	}

	protected int QueryDataBlockEx(char cti, String pAddress, CDynamicBuffer pDataBlock) throws Exception
	{
		int tryIndex;

		tryIndex = 0;
		while(!QueryDataBlock(cti, pAddress, m_ValueBuffer))
		{
			int result;

			if (GetElecMeter().GetOptoProbe().GetStream().IsAborted())
				return(-1);

			result = QueryError(pAddress, tryIndex++);
			if (result != 0)
				return(result < 0 ? -1 : 0);

			Thread.sleep(500);
			GetElecMeter().GetOptoProbe().GetStream().ClearIoBuffers(true, true);
		}
		return(1);
	}

	int DataBlockHandler(String pAddress, CDynamicBuffer pDataBlock)
	{
	// Data blocks may contain many data-lines, this will be supported in the future
		return(DataLine(pAddress, new String(pDataBlock.GetMemory())));
	}

	boolean TranslateDataBlock(String pAddress, String pMappedAddress, 
		CDynamicBuffer pDataBlock)  throws Exception
	{
		return(true);
	}

	protected static int InternalDataLineHandler(Object pContext, String pDataLine)
	{
		return((CMeterReader)pContext).DataLine(null, pDataLine);
	}

	@Override
	public int DataLine(String pAddress, String pDataLine)
	{
		return(0);
	}

	@Override
	public boolean NextAddress(int[] ppPosition, String[] pAddress)
	{
		return(false);
	}

	@Override
	public int QueryError(String pAddress, int tryIndex)
	{
		return(tryIndex < 5 ? 0 : 1);
	}

	@Override
	public int EnergyProfile(String pAct, String pEnd, String pCap, String pTime)
	{
		return(0);
	}

	@Override
	public int CurrentProfile(String pIL1, String pIL2, String pIL3, String pTime)
	{
		return(0);
	}

	@Override
	public int VoltageProfile(String pVL1, String pVL2, String pVL3, String pTime)
	{
		return(0);
	}

}
