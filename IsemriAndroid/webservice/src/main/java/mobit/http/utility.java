package mobit.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class utility {

	
	public static String readLine(InputStream is, byte [] buf, String charsetName) throws UnsupportedEncodingException, IOException, Exception{
		int ch;
		int i = 0;
		String line = "";
		while((ch = is.read()) > -1 && i < buf.length){
			if(ch == '\r'){
				if((ch = is.read()) < 0) throw new Exception("Satır sonu hatası!");
				line = new String(buf, 0, i, charsetName);
				break;
			}
			buf[i++] = (byte)ch;
		}
		
		if(i == buf.length) throw new Exception("Satır uzunluğu bufferdan fazla!");
		
		return line;
		
	}
}
