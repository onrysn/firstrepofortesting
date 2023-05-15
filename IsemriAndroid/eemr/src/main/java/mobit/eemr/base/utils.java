package mobit.eemr.base;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utils {

	public static final long INFINITE = 0;

	public static boolean ReadByte(ICommStream pStream, byte[] pData, int off) throws Exception {
		int[] bytesRead = new int[1];

		pStream.Read(pData, off, 1, bytesRead);
		return bytesRead[0] == 1;
	}

	public static boolean ReadByte(ICommStream pStream, byte[] pData) throws Exception {
		return ReadByte(pStream, pData, 0);
	}
	

	public static final Pattern validate_mac_address = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
	public static BTH_ADDR ParseBthAddr(String pString) throws Exception {
		
		Matcher matcher = validate_mac_address.matcher(pString);
        if(!matcher.find()) throw new Exception(String.format("Geçersiz '%s' bluetooth MAC adresi!", pString));
        pString = matcher.group();
        pString = pString.replaceAll("[^a-fA-F0-9]", ":");
        String [] fields = pString.split(":");
        if(fields.length != 6) throw new Exception(String.format("Geçersiz '%s' bluetooth MAC adresi!", pString));
        BTH_ADDR bthAddr = new BTH_ADDR();
        for(int i = 0; i < fields.length; i++){
        	long l = Long.parseLong(fields[i], 16);
        	bthAddr.addr |= (l << (i * 8));
        }
        
		return bthAddr;
	}

	public static ICommStream OpenBtCommStream(String pDevice, int channel, 
		String pPin, String pLogFileName) throws Exception
	{
		BTH_ADDR bthAddr = null;
		ICommStream pCommStream;

		try {
			
			bthAddr = ParseBthAddr(pDevice);
		}
		catch(Exception e){
		}
		
		if (bthAddr != null)
		{
			CBtSocketStream pSocketStream = null;
			try {
				pSocketStream = new CBtSocketStream();
				pSocketStream.Open(bthAddr, channel, pPin, 1024, 1024);
			}
			catch(Exception e)
			{
				if(pSocketStream != null) pSocketStream.Close();
				throw e;
			}
			pCommStream = pSocketStream;
		}
		else
		{
			CSerialStream pSerialStream;

			pSerialStream = new CSerialStream();
			if (!pSerialStream.Open(pDevice, 1024, 1024))
			{
				pSerialStream.Close();
				return(null);
			}

			pCommStream = pSerialStream;
		}

		if (pLogFileName != null)
		{
			CLoggingFilterStream pFilterStream;

			pFilterStream = new CLoggingFilterStream(pCommStream);
			
			if (!pFilterStream.Open(pLogFileName))
			{
				pFilterStream.Close();
				return(null);
			}
			pCommStream = pFilterStream;
		}
		return(pCommStream);
	}

	public static String FindAddress(AddressMapEntry[] pAddressMap, String pAddress) {
		for (int i = 0; i < pAddressMap.length; i++) {
			AddressMapEntry pEntry = pAddressMap[i];
			if (pEntry.pAddress.compareToIgnoreCase(pAddress) == 0)
				return (pEntry.pOriginalAddress);
		}
		return (null);
	}

	public static final Pattern parantez_ici = Pattern.compile("\\(\\w+\\)");
	public static void SplitDataLine(String pDataLine, String[] pValueArray) throws Exception {
		
		int i = 0;
        Matcher matcher = parantez_ici.matcher(pDataLine);
        while(matcher.find()) pValueArray[i++] = matcher.group();
        if(i != pValueArray.length) throw new Exception("SplitDataLine"); 	 
	}

	public static long DecodeAsciiValue(String pValue, int off, int length, int radix) {
		int index;
		long value;

		value = 0;
		off += 2;
		for (index = length; index >= off; index -= 2) {
			String ascii;

			ascii = pValue.substring(index - 2, index - 1);

			value = value * radix + Long.parseLong(ascii, radix);

			ascii = pValue.substring(index - 1, index);
			value = value * radix + Long.parseLong(ascii, radix);
		}
		return (value);
	}

	public static void FormatBuffer(CDynamicBuffer pBuffer, String pFormat, Object... argList) throws Exception {
		pBuffer.SetDataSize(0);
		AppendBufferFormatV(pBuffer, pFormat, argList);
	}

	public static void AppendBufferFormat(CDynamicBuffer pBuffer, String pFormat, Object... argList) throws Exception {
		AppendBufferFormatV(pBuffer, pFormat, argList);

	}

	public static void AppendBufferFormatV(CDynamicBuffer pBuffer, String pFormat, Object[] argList) throws Exception {

		if (pBuffer.GetSize() == pBuffer.GetDataSize())
			pBuffer.Grow();

		String s = String.format(pFormat, argList);
		if (s.length() > ((pBuffer.GetSize() - pBuffer.GetDataSize()) / 2)) {
			pBuffer.Grow();
		}

		byte[] b = s.getBytes();
		pBuffer.Append(b, b.length);

	}

	static final Charset iso8859_1 = Charset.forName("ISO-8859-1");

	public static byte[] charsToBytes(char[] chars){
	    
	    ByteBuffer byteBuffer = iso8859_1.encode(CharBuffer.wrap(chars));
	    return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
	}

	static  final Charset utf16 = Charset.forName("UTF-16");

	public static  char[] bytesToChars(byte[] bytes){
	    
	    CharBuffer charBuffer = utf16.decode(ByteBuffer.wrap(bytes));
	    return Arrays.copyOf(charBuffer.array(), charBuffer.limit());    
	}

	public static  String bytesToString(byte[] bytes){
		return new String(bytesToChars(bytes));
	}
	
	public static byte[] shortToBytes(short s){
	    
	    return new byte[]{(byte)(s & 0xff), (byte)((s >> 8) & 0xff)};
	}
	
	public static byte[] intToBytes(short i){
	    
	    return new byte[]{(byte)(i & 0xff), (byte)((i >> 8) & 0xff),
	    		(byte)((i >> 16) & 0xff), (byte)((i >> 24) & 0xff)};
	}
	
	public static byte[] longToBytes(short l){
	    
	    return new byte[]{
	    		(byte)(l & 0xff), (byte)((l >> 8) & 0xff),
	    		(byte)((l >> 16) & 0xff), (byte)((l >> 24) & 0xff),
	    		
	    		(byte)((l >> 32) & 0xff), (byte)((l >> 40) & 0xff),
	    		(byte)((l >> 48) & 0xff), (byte)((l >> 56) & 0xff)
	   };
	}

	public static short shortFromBytes(byte [] b, int off, int len){
	    
		short s = 0;
		for(int i = 0; i < len && off < b.length; i++)
			s |= (b[off++] << 8);
		return s;
	}
	public static int intFromBytes(byte [] b, int off, int len){
	    
		int j = 0;
		for(int i = 0; i < len && off < b.length; i++)
			j |= (b[off++] << 8);
		return j;
	}
	public static long longFromBytes(byte [] b, int off, int len){
	    
		int j = 0;
		for(int i = 0; i < len && off < b.length; i++)
			j |= (b[off++] << 8);
		return j;
	}
	
	public static boolean _istalnum(char ch)
	{
		return Character.isLetterOrDigit(ch);
	}
	
	public static long GetTickCount()
	{
		return System.currentTimeMillis();
	}
}
