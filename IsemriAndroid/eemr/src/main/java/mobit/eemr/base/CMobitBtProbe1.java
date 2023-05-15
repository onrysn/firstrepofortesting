package mobit.eemr.base;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.concurrent.TimeoutException;

import mobit.eemr.IBluetooth;
import mobit.eemr.IMeterReader;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.PowerStatus;

public class CMobitBtProbe1 implements IOptoProbe, IProbeEvent {

	private ICommStream m_pStream;
	private Thread m_hReceiveThread;
	private Object m_hCmdResultEvent;
	private volatile boolean m_ExitThread;
	private CBtProbeStream m_BtProbeStream;
	private CCircularBuffer m_CmdResultBuffer;
	private Object m_CrCallbacks;

	private IMeterReader m_pMeterReader;
	private IProbeEvent m_pProbeEventHandler;

	private static final int IRBR_300 = 0;
	private static final int IRBR_600 = 1;
	private static final int IRBR_1200 = 2;
	private static final int IRBR_2400 = 3;
	private static final int IRBR_4800 = 4;
	private static final int IRBR_9600 = 5;
	private static final int IRBR_19200 = 6;
	private static final int PROBE_TIMEOUT = 10 * 1000;

	private static final int PACKETCLASS_EVENT = 0;
	private static final int PACKETCLASS_COMMAND = 1;
	private static final int PACKETCLASS_RESULT = 2;
	
	private static final int EVENT_POWER = 0;
	private static final int EVENT_TRIGGER = 1;
		
	private static final int COMMAND_GETVERSION = 0;
	private static final int COMMAND_GETBTVERSION = 1;
	private static final int COMMAND_POWEROFF = 2;
	private static final int COMMAND_SETLED = 3;
	private static final int COMMAND_GETPOWERSTATUS = 4;
	private static final int COMMAND_IRENABLE = 5;
	private static final int COMMAND_IrSetConfig = 6;
	private static final int COMMAND_SETAUTOPOWEROFFTIME = 7;
	
	private static final int RESULT_SUCCESS = 0;
	private static final int RESULT_FAILED = 1;
	private static final int RESULT_NOTIMPLEMENTED = 2;
	

	private class PacketType
	{
		
		public byte [] type = new byte[]{0};
		
		public PacketType(byte class_, byte code, byte reserved)
		{
			setClass_(class_);
			setCode(code);
			setReserved(reserved);
		}
		public PacketType(byte type)
		{
			this.type[0] = type;
		}
		
		public int getSize()
		{
			return type.length;
		}
		
		public byte getClass_()
		{
			return (byte)((type[0] >> 0) & 0x3);
		}
		public void setClass_(byte class_)
		{
			type[0] |= (class_ & 0x3);
		}
		public byte getCode()
		{
			return (byte)((type[0] >> 2) & 0x1f);
		}
		public void setCode(byte code)
		{
			type[0] |= ((code & 0x1f) << 2);
		}
		public byte getReserved()
		{
			return (byte)(type[0] >> 7);
		}
		public void setReserved(byte reserved)
		{
			type[0] |= (reserved << 7);
		}
	
	};

	private class PacketHeader
	{
		byte [] header = new byte[2];
		
		public int getSize()
		{
			return header.length;
		}
		
		public PacketType getPacketType()
		{
			return new PacketType(header[0]);
		}
		public void setPacketType(PacketType type)
		{
			header[0] = type.type[0];
		}
		
		public int getLength()
		{
			return utils.intFromBytes(header, 1, 1);
		}
		public void setLength(int length)
		{
			header[1] = (byte)length;
		}
		
	};

	private class ProbePacket
	{
		PacketHeader header = new PacketHeader();
		byte [] data;
	};
	
	private class PowerStatus
	{
		
		public byte [] status = new byte[4];
		
		public int getCharging()
		{
			return utils.intFromBytes(status, 0, 1);
		}
		public int getBatteryVoltage()
		{
			return utils.intFromBytes(status, 1, 2);
			
		}
		public int getbatteryLifePercent()
		{
			return utils.intFromBytes(status, 3, 1);
		}
	};

	public CMobitBtProbe1() {
		super();
		m_pStream = null;
		m_hCmdResultEvent = new Object();
		m_hReceiveThread = null;
		m_BtProbeStream = new CBtProbeStream();
		m_CrCallbacks = new Object();
		m_CmdResultBuffer = new CCircularBuffer();
	}
	
	public CMobitBtProbe1(IMeterReader pMeterReader) {
		this();
		m_pMeterReader = pMeterReader;
	}

	@Override
	protected void finalize() throws Throwable {
		Close();
		super.finalize();

	}

	@Override
	public void Open(String pDevice, String pParameter, String pLogFileName) throws Exception {
		
		int version;
		ICommStream pCommStream = null;

		try{
			
			/*
			pCommStream = utils.OpenBtCommStream(pDevice, 1, pParameter, pLogFileName);
			*/
			IBluetooth pBluetooth = m_pMeterReader.getBluetooth();
			pBluetooth.Open(IBluetooth.SPP, pDevice, pParameter);
			pCommStream = (ICommStream) pBluetooth;
			//muhammed gökkaya
			Thread.sleep(2000);
			pCommStream.ClearIoBuffers(true,true);

			pCommStream.SetReadTimeout(utils.INFINITE);

			m_CmdResultBuffer.SetSize(64);

			m_BtProbeStream.Open(pCommStream);

			m_pStream = pCommStream;StartReceiveThread();

			version = GetVersion();
			if(version < 259){
				StopReceiveThread();
			}
		}
		catch(Exception e){
			m_pStream = null;
			if(pCommStream != null) pCommStream.Close();
			if(m_BtProbeStream != null) m_BtProbeStream.Close();
			throw e;
		}

	}

	@Override
	public void Close()
	{
	
		StopReceiveThread();
		if(m_BtProbeStream != null){
			m_BtProbeStream.Close();
			m_BtProbeStream = null;
		}
		if(m_pStream != null){
			m_pStream.Close();
			m_pStream = null;
		}
	}

	@Override
	public int GetType()
	{
		return(IDef.MBTPROBE_MOBITBLUETOOTH1);
	}

	@Override
	public ICommStream GetStream()
	{
		return(m_BtProbeStream);
	}

	@Override
	public void IrEnable(boolean enable) throws Exception
	{
		byte [] data = new byte[1];
		int [] resLength = new int[1];

		data[0] = enable ? (byte)1 : (byte)0;
		WriteCommand(COMMAND_IRENABLE, data, data.length);
		
		ReadResult(null, 0, resLength);
	}

	@Override
	public void IrSetConfig(int baudRateId, int byteSize, int parity) throws Exception
	{
		byte [] data = new byte[1];
		int [] resLength = new int[1];

		switch(baudRateId)
		{
		case CIEC62056.IEC62056_BR300: data[0] = IRBR_300; break;
		case CIEC62056.IEC62056_BR600: data[0] = IRBR_600; break;
		case CIEC62056.IEC62056_BR1200: data[0] = IRBR_1200; break;
		case CIEC62056.IEC62056_BR2400: data[0] = IRBR_2400; break;
		case CIEC62056.IEC62056_BR4800: data[0] = IRBR_4800; break;
		case CIEC62056.IEC62056_BR9600: data[0] = IRBR_9600; break;
		case CIEC62056.IEC62056_BR19200: data[0] = IRBR_19200; break;
		default: throw new Exception("Desteklenmeyen baud hızı!");
		}

		WriteCommand(COMMAND_IrSetConfig, data, data.length);
		
		ReadResult(null, 0, resLength);
	}

	@Override
	public void SetLed(boolean enable) throws Exception
	{
		byte [] data = new byte[1];
		int [] resLength = new int[1];

		data[0] = enable ? (byte)1 : (byte)0;
		WriteCommand(COMMAND_SETLED, data, data.length);
		ReadResult(null, 0, resLength);
	}

	@Override
	public int GetVersion() throws Exception
	{
		byte [] data = new byte[2];
		int [] resLength = new int[1];

		WriteCommand(COMMAND_GETVERSION, null, 0);
		
		ReadResult(data, data.length, resLength);
		
		if (resLength[0] != data.length)
			throw new Exception("Port versiyon verisinde hata var!");
			
		return new BigInteger(data).intValue();
		
	}
	@Override
	public void SetAutoPowerOffTime(int duration) throws Exception
	{
		int [] resLength = new int[1];
		
		byte [] data = utils.shortToBytes((short)duration);
		WriteCommand(COMMAND_SETAUTOPOWEROFFTIME, data, data.length);
		
		ReadResult(null, 0, resLength);
	}

	@Override
	public MbtProbePowerStatus GetPowerStatus() throws Exception
	{
		int [] resLength = new int[1];
		PowerStatus status = new PowerStatus();
		MbtProbePowerStatus pStatus = new MbtProbePowerStatus();

		WriteCommand(COMMAND_GETPOWERSTATUS, null, 0);

		ReadResult(status.status, status.status.length, resLength);
	
		if (resLength[0] != status.status.length)
			throw new Exception("Prob güç bilgisi hatalı!");
	
		pStatus.charging = status.getCharging();
		pStatus.batteryVoltage = status.getBatteryVoltage();
		pStatus.batteryLifePercent = status.getbatteryLifePercent();
	
		return pStatus;
	}

	
	@Override
	public void SetProbeEventHandler(IProbeEvent pProbeEventHandler)
	{
		synchronized(m_CrCallbacks){
			m_pProbeEventHandler = (pProbeEventHandler != null) ? pProbeEventHandler : this;
		}
	}

	
	private void StartReceiveThread()
	{
		Thread hThread;

		m_ExitThread = false;
		hThread = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ReceiveThreadProc();
			}
		});
		hThread.setPriority(Thread.MAX_PRIORITY);
		m_hReceiveThread = hThread;
		
	}

	
	private void  StopReceiveThread()
	{
		m_ExitThread = true;
		if(m_pStream != null) m_pStream.Close();

		if(m_hReceiveThread != null && m_hReceiveThread.isAlive()){
			try {
			m_hReceiveThread.join(1000);
			}
			catch(InterruptedException e){
				
			}
			if(m_hReceiveThread.isAlive()) m_hReceiveThread.interrupt();
		}
		
	}

	
	private void WriteCommand(int cmd, byte [] pData, int dataLen) throws Exception
	{
		PacketHeader header = new PacketHeader();
		
		header.setPacketType(new PacketType((byte)PACKETCLASS_COMMAND, (byte)cmd, (byte)1));
		header.setLength(header.header.length + dataLen);
		
		WriteOutputBuffer(header.header, header.header.length);
		
		WriteOutputBuffer(pData, dataLen);
		
		m_pStream.FlushWrite();
	}

	private void ReadResult(byte [] pResData, int maxLength, int [] pResLength) throws Exception
	{
		int resLength;
		PacketHeader header = new PacketHeader();

		ReadCmdResultBuffer(header.header, header.header.length);
		
		if (header.getPacketType().getClass_() != PACKETCLASS_RESULT)
			throw new Exception("Hatalı paket sınıfı!");

		if (header.getPacketType().getCode() != RESULT_SUCCESS)
			throw new Exception(String.format("Probe hata kodu : %s", header.getPacketType().getCode()));
		
		if (header.getLength() < header.header.length)
			throw new Exception("Probdan gelen veri olması gerekenden az!");
			

		resLength = header.getLength() - header.header.length;
		if (resLength > maxLength)
			throw new Exception("Probdan gelen veri olması gerekenden fazla!");

		ReadCmdResultBuffer(pResData, resLength);
		
		pResLength[0] = resLength;
		
	}

	private void PowerEvent()
	{
		synchronized(m_CrCallbacks){ m_pProbeEventHandler.PowerEventHandler(null);}
		
	}

	private void TriggerEvent()
	{
		
		synchronized(m_CrCallbacks){ m_pProbeEventHandler.TriggerEventHandler(null);}
		
	}

	
	private void ConnectionResetEvent()
	{
		synchronized(m_CrCallbacks){ m_pProbeEventHandler.TriggerEventHandler(null);}
	}

	

	private void HandlePacket(PacketHeader pHeader) throws Exception
	{
		int dataLen, totalRead;

		if (pHeader.getLength() < pHeader.getSize())
			throw new Exception("Gelen veri bağlık bilgisinden az!");
			

		dataLen = pHeader.getLength() - pHeader.getSize();
		if (pHeader.getPacketType().getClass_() == PACKETCLASS_EVENT)
		{
			if (dataLen != 0)
				throw new Exception("Farklı bir veri geldi");

			if (pHeader.getPacketType().getCode() == EVENT_POWER)
				PowerEvent();
			else if (pHeader.getPacketType().getCode() == EVENT_TRIGGER)
				TriggerEvent();

			return;
		}
		else if (pHeader.getPacketType().getClass_() == PACKETCLASS_RESULT)
		{
			WriteCmdResultBuffer(pHeader.header, pHeader.getSize());
			
			totalRead = 0;
			while(totalRead < dataLen)
			{
				byte [] buffer = new byte[64];
				int bytesToRead; 
				int [] bytesRead = new int[1];

				bytesToRead = dataLen - totalRead;
				if (bytesToRead > buffer.length)
					bytesToRead = buffer.length;

				m_pStream.Read(buffer, bytesToRead, bytesRead);
				
				WriteCmdResultBuffer(buffer, bytesRead[0]);
				
				totalRead += bytesRead[0];
			}

			synchronized(m_hCmdResultEvent){m_hCmdResultEvent.notify();}
			return;
			
		}
		throw new Exception("Veri hatası!");
	}

	public void ReadCmdResultBuffer(byte [] pBuffer, int bytesToRead) throws Exception
	{
		long startTicks;
		int totalRead;

		totalRead = 0;
		startTicks = utils.GetTickCount();
		while(totalRead < bytesToRead)
		{
			if (m_ExitThread) throw new Exception("Thread kapanmış!");
			
			if (!m_CmdResultBuffer.IsEmpty())
			{
				totalRead += m_CmdResultBuffer.Read(pBuffer, totalRead, 
					bytesToRead - totalRead);
			}
			else
			{
				long timeSpent;

				timeSpent = utils.GetTickCount() - startTicks;
				if (timeSpent >= PROBE_TIMEOUT) return;
					
				synchronized(m_hCmdResultEvent){m_hCmdResultEvent.wait(PROBE_TIMEOUT - timeSpent);}
				
			}
		}
	}

	public void  WriteCmdResultBuffer(byte [] pBuffer, int bytesToWrite) throws Exception
	{
		int totalWritten;

		totalWritten = 0;
		while(totalWritten < bytesToWrite)
		{
			int [] bytesWritten = new int[1];

			if (m_ExitThread) throw new Exception("Thread kapanmış!");
			
			bytesWritten[0] = m_CmdResultBuffer.Write(pBuffer, totalWritten, bytesToWrite - totalWritten);

			if (bytesWritten[0] == 0)
				Thread.sleep(10);

			totalWritten += bytesWritten[0];
		}
	}

	private void WriteOutputBuffer(byte [] pBuffer, int bytesToWrite) throws Exception
	{
		int totalWritten;

		totalWritten = 0;
		while(totalWritten < bytesToWrite)
		{
			int [] bytesWritten = new int[1];

			if (m_ExitThread) throw new Exception("");
			m_pStream.Write(pBuffer, totalWritten, bytesToWrite - totalWritten, bytesWritten);
			
			if (bytesWritten[0] == 0)
				throw new Exception("Veri buffera yazılamadı!");
			
			totalWritten += bytesWritten[0];
		}
	}

	private void ReceiveThreadProc()
	{
		
		try
		{
			boolean recvPacket = false;
			int packetLength = 0;
			PacketHeader packetBuffer = new PacketHeader();
			
			recvPacket = false;
			while(!m_ExitThread)
			{
				byte [] data = new byte[1];
				int [] bytesRead = new int[1];

				m_pStream.Read(data, data.length, bytesRead);
				
				if (bytesRead[0] == 0)
				{
					recvPacket = false;
					continue;
				}

				if (!recvPacket)
				{
					if ((data[0] & 0x80) != 0)
					{
						packetLength = 0;
						packetBuffer.header[packetLength++] = data[0];
						recvPacket = true;
					}
					else
					{
						m_BtProbeStream.QueueInput(data, data.length);
					}
				}
				else
				{
					packetBuffer.header[packetLength++] = data[0];
					if (packetLength == packetBuffer.getSize())
					{
						HandlePacket(packetBuffer);
							
						recvPacket = false;
					}
				}
			}

			if (!m_ExitThread)
			{
				m_ExitThread = true;
				synchronized(m_hCmdResultEvent){m_hCmdResultEvent.notify();}
				m_BtProbeStream.SetAborted(true);
				ConnectionResetEvent();
			}
		}
		catch(Exception e)
		{
		
		}
		
	}

	@Override
	public  void PowerEventHandler(Object pContext)
	{
		
	}

	@Override
	public void TriggerEventHandler(Object pContext)
	{
	}
	
	@Override
	public void ConnectionResetEventHandler(Object pContext)
	{
		
	}

	
}
