package mobit.http;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface IDef {

	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String UserAgent = "User-Agent";
	public static final String ContentType = "Content-Type";
	public static final String SOAPAction = "SOAPAction";
	public static final String Host = "Host";
	public static final String ContentLength = "Content-Length";
	public static final String Expect = "Expect";
	public static final String Connection = "Connection";

	public static final String CacheControl = "Cache-Control";
	public static final String Server = "Server";
	public static final String XAspNetVersion = "X-AspNet-Version";
	public static final String XPoweredBy = "X-Powered-By";
	public static final String Date = "Date";
	
	public static final String HTTP_1_1 = "HTTP/1.1";
	
	public static final int HTTP_OK = 200;
	
	public static final String _100continue= "100-continue";
	public static final String KeepAlive = "Keep-Alive";

	// Android'te tanımlı değil
	DateTimeFormatter RFC_1123_DATE_TIME = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.US);    

	
}
