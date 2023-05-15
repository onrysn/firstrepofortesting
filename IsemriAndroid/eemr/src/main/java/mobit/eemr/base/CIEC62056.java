package mobit.eemr.base;

import mobit.eemr.IReadEvent;
import mobit.eemr.MbtMeterInformation;
import java.util.logging.Logger;
class CIEC62056 {

    //LOG MESAJI DÜŞMÜYOR VE DEBUGDA BURAYA GELMİYOR?
	public static final int IEC62056_BR300					= 0;
	public static final int IEC62056_BR600					= 1;
	public static final int IEC62056_BR1200					= 2;
	public static final int IEC62056_BR2400					= 3;
	public static final int IEC62056_BR4800					= 4;
	public static final int IEC62056_BR9600					= 5;
	public static final int IEC62056_BR19200				= 6;
	public static final int IEC62056_TIMEOUT				= 2000;
	public static final int IEC62056_IDENTIFICATIONTIMEOUT	= 3000;	// Elektromed 2 saniyede cevap veremiyor

	public static final byte SOH = (byte)0x01;
	public static final byte STX = (byte)0x02;
	public static final byte ETX = (byte)0x03;
	public static final byte EOT = (byte)0x04;
	public static final byte ACK = (byte)0x06;
	public static final byte NAK = (byte)0x15;



	ICommStream m_pStream;
	
	public CIEC62056(ICommStream pStream)
	{
		m_pStream = pStream;
	}

	void WriteRequestMessage(String pDevAddr) throws Exception
	{

		//muhammed gökkaya
		Logger LOGGER = Logger.getLogger("AK3");
		LOGGER.info("/?");
		String s = "/?";
		WriteString(s, s.length());



		if (pDevAddr != null && pDevAddr.length() > 0)
		{
			WriteString(pDevAddr, pDevAddr.length());
		}

		s = "!\r\n";
		WriteString(s, s.length());
	
		m_pStream.FlushWrite();
	}

	void ReadIdentificationMessage(boolean ignoreSlash, MbtMeterInformation information) throws Exception
	{
		char [] chr1 = new char[1];
		char [] chr2 = new char[1];
		CDynamicBuffer buffer = new CDynamicBuffer();
		char [] pResponse;
        //muhammed gökkaya
        Logger LOGGER = Logger.getLogger("AK4");
        LOGGER.info("read identification");
		ReadString(buffer, '\r', '\n', (char)-1, chr1);
		
		ReadChar(chr2);
		
		if ((chr1[0] != '\r' || chr2[0] != '\n') && 
			(chr1[0] != '\n' || chr2[0] != '\r'))
			throw new Exception("Sayaç kimlik bilgisi alınamadı!");

		if ((buffer.GetDataSize() / 2) < (1 + 3 + 1) || (buffer.GetDataSize() / 
			2) > (1 + 3 + 1 + 1 + IDef.MBTMETERREADER_MAXIDENTIFICATIONLENGTH))
			throw new Exception("Sayaç kimlik bilgisi alınamadı!");

		buffer.AppendTChar('\0');
		
		int i = 0;
		pResponse = utils.bytesToChars(buffer.GetMemory());
		if (pResponse[i] == '/')
			i++;
		else if (!ignoreSlash)
			throw new Exception("Sayaç kimlik bilgisi alınamadı!");

		
		information.flag = new String(pResponse, 0 , 3);
		information.identification = new String(pResponse, 4, pResponse.length - 4);
		information.baudRateId = pResponse[3] - '0';
	}

	void WriteOptionSelectMessage(int protocol, int baudRateId, int mode) throws Exception
	{
		char [] msg = new char[6];
        Logger LOGGER = Logger.getLogger("AK5");
        LOGGER.info("writeoptionselect messsage");
		msg[0] = ACK;

		msg[1] = (char)('0' + protocol);
		msg[2] = (char)('0' + baudRateId);
		msg[3] = (char)('0' + mode);
		msg[4] = '\r';
		msg[5] = '\n';
		String s = new String(msg);
		WriteString(s, s.length());

		m_pStream.FlushWrite();
	}

	boolean ReadAcknowledgementMessage() throws Exception
	{
		char [] chr = new char[1];
        Logger LOGGER = Logger.getLogger("AK6");
        LOGGER.info("read ack");
		ReadChar(chr);
		return chr[0] != ACK;
	}

	void WriteProgrammingCommandMessage(char cmi, char cti, String pDataSet) throws Exception
	{
		WriteProgrammingCommandMessage(cmi, cti, (char)STX, pDataSet);
	}

	void WriteProgrammingCommandMessage(char cmi, char cti, char stx, String pDataSet) throws Exception
	{
		byte bcc;
		char [] msg = new char[4];
        Logger LOGGER = Logger.getLogger("AK7");
        LOGGER.info("read ack");
		msg[0] = SOH;
		msg[1] = cmi;
		msg[2] = cti;
		msg[3] = stx;
		String s = new String(msg);
		WriteString(s, s.length());
		
		bcc = CalculateBCC((byte)0, s, 1, 3);
		WriteString(pDataSet, pDataSet.length());
		
		bcc = CalculateBCC(bcc, pDataSet, 0, pDataSet.length());
		WriteChar((char)ETX);
		
		bcc = CalculateBCC(bcc, (char)ETX);
		WriteChar((char)bcc);
		
		m_pStream.FlushWrite();
	}

	boolean ReadProgrammingCommandMessage(char [] pCmi, char [] pCti, 
		CDynamicBuffer pDataSet, boolean chkSoh, boolean chkBcc) throws Exception
	{
        Logger LOGGER = Logger.getLogger("AK8");
        LOGGER.info("read ack");
		byte bcc;
		char [] chr = new char[1];

		pDataSet.SetDataSize(0);
		ReadChar(chr);
		
		if (chr[0] == SOH)
		{
			ReadChar(pCmi);
		}
		else
		{
			if (chkSoh)
				return(false);

			if (chr[0] == STX)
			{
				ReadChar(pCmi);
			}
			else
			{
				pCmi[0] = chr[0];
			}
		}

		bcc = CalculateBCC((byte)0, pCmi[0]);
		ReadChar(pCti);
		
		bcc = CalculateBCC(bcc, pCti[0]);
		ReadChar(chr); 
		if(chr[0] != STX) return(false);

		bcc = CalculateBCC(bcc, chr[0]);
		ReadString(pDataSet, (char)ETX, (char)-1, (char)-1, chr);

		String s = new String(pDataSet.GetMemory());
		bcc = CalculateBCC(bcc, s, 0, s.length());
		bcc = CalculateBCC(bcc, chr[0]);

		ReadChar(chr);
		
		if (chkBcc && bcc != chr[0])
			return(false);

		pDataSet.AppendTChar('\0');
		
		return true;
	}

	boolean ReadDataMessage(CDynamicBuffer pDataBlock, boolean chkBcc) throws Exception
	{
		byte bcc;
		char [] chr = new char[1]; 

		pDataBlock.SetDataSize(0);
		ReadChar(chr);
		if(chr[0] != STX) return(false);

		ReadString(pDataBlock, (char)ETX, (char)-1, (char)-1, chr);
		
		bcc = CalculateBCC((byte)0, new String(pDataBlock.GetMemory()), 
			0, pDataBlock.GetDataSize() / 2);
		bcc = CalculateBCC(bcc, chr[0]);

		ReadChar(chr);
		
		if (chkBcc && bcc != chr[0])
			return(false);

		pDataBlock.AppendTChar('\0');
		return true;
	}

	boolean ReadDataMessage(CDynamicBuffer pDataLine, IReadEvent pDataLineProc, 
		Object pContext, char etx, boolean chkBcc) throws Exception
	{
		byte bcc;
		char [] chr1 = new char[1];
		char [] chr2 = new char[1];

		ReadChar(chr1);
		if(chr1[0] != STX) return(false);

		bcc = 0;
		while(true)
		{
			int result;

			pDataLine.SetDataSize(0);
			ReadString(pDataLine, '\r', '\n', etx, chr1);
			
			bcc = CalculateBCC(bcc, new String(pDataLine.GetMemory()), 
				0, pDataLine.GetDataSize() / 2);
			bcc = CalculateBCC(bcc, chr1[0]);

			if (chr1[0] == etx)
				break;

			ReadChar(chr2);
			
			bcc = CalculateBCC(bcc, chr2[0]);
			if ((chr1[0] != '\r' || chr2[0] != '\n') && 
				(chr1[0] != '\n' || chr2[0] != '\r')) {
//				Logger LOGGER = Logger.getLogger("baslik");
//				LOGGER.info("xx2");
				return(false);
			}


			pDataLine.AppendTChar('\0');
			
			result = pDataLineProc.DataLine(pContext.toString(), new String(pDataLine.GetMemory()));
			if (result != 0)
				return(result > 0);
		}

		ReadChar(chr1);
		
		return(!chkBcc || bcc == chr1[0]);
	}

	void WriteBreakMessage() throws Exception
	{
		byte [] msg = new byte[4];

		msg[0] = SOH;
		msg[1] = (byte)'B';
		msg[2] = (byte)'0';
		msg[3] = ETX;
		String s = new String(msg);
		WriteString(s, s.length());
		
		WriteChar((char)CalculateBCC((byte)0, s, 1, 3));
		m_pStream.FlushWrite();
	}

	void WriteChar(char chr) throws Exception
	{
		byte [] data = new byte [1];
		int [] bytesWritten = new int[1];
		data[0] = (byte)chr;
		m_pStream.Write(data, 1, bytesWritten);
	}

	void ReadChar(char [] pChr) throws Exception
	{
		byte [] data = new byte[1];
		int [] bytesRead = new int[1];

		m_pStream.Read(data, data.length, bytesRead);
		pChr[0] = (char)data[0];
	}

	void ReadString(CDynamicBuffer pBuffer, char chr1, char chr2, 
		char chr3, char [] pChr) throws Exception
	{
		char [] chr = new char[1];
		byte [] b = new byte[1];
		
		while(true)
		{
			ReadChar(chr);
			if (chr[0] == chr1 || chr[0] == chr2 || chr[0] == chr3)
			{
				pChr[0] = chr[0];
				return;
			}
			b[0] = (byte)chr[0];
			pBuffer.Append(b, 1);
			
		}
	}

	void WriteString(String pData, int dataLength) throws Exception
	{
		int index;

		if (dataLength == -1)
			dataLength = pData.length();

		for(index = 0; index < dataLength; index++)
			WriteChar(pData.charAt(index));
		
	}

	byte CalculateBCC(byte bcc, char data)
	{
		return(bcc ^= data);
	}

	byte CalculateBCC(byte bcc, String pData, int off, int dataLength)
	{
		int index;

		if (dataLength == -1)
			dataLength = pData.length();

		for(index = 0; index < dataLength; index++)
			bcc = CalculateBCC(bcc, pData.charAt(off+index));

		return(bcc);
	}

	
}
