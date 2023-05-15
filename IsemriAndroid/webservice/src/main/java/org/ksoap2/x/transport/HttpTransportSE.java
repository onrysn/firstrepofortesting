package org.ksoap2.x.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.ksoap2.x.SoapFault;
import org.ksoap2.x.serialization.SoapSerializationEnvelope;

import mobit.http.IDef;
import mobit.http.IHttpRequestHeader;
import mobit.http.IISHttpRequestHeader;
import mobit.http.IISHttpResponseHeader;
import mobit.http.utility;

import static java.net.SocketOptions.SO_LINGER;


public class HttpTransportSE {

	public boolean debug = false;
	//HttpURLConnection connection = null;
	public String requestDump;
	public String responseDump;
	private Socket sock = null;
	private DataOutputStream wr = null;
	private InputStream is = null;
	private IISHttpRequestHeader requestHeader;
	private URL u;
	private String host;
	private int port;
	private int timeout = 30000;

	private static final String charsetName = "UTF-8";

	public HttpTransportSE(String url, int timeout) throws MalformedURLException, IOException {

		this.timeout = timeout;
		u = new URL(url);
		host = u.getHost();
		port = u.getPort() > 0 ? u.getPort() : u.getProtocol().equals("http") ? 80 : u.getProtocol().equals("https") ? 443 : -1;
		requestHeader = new IISHttpRequestHeader(new URL(url), IDef.HTTP_1_1);
	}

	public HttpTransportSE(String url) throws MalformedURLException, IOException {

		this(url, 30 * 1000);
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	void close() {
		if (is != null) {
			try {
				is.close();
			} catch (Exception e) {

			}
			is = null;
		}
		if (wr != null) {
			try {
				wr.close();
			} catch (Exception e) {

			}
			wr = null;
		}
		if (sock != null) {
			try {
				sock.close();
			} catch (Exception e) {

			}
			sock = null;
		}
	}

	public void call(String arg0, SoapSerializationEnvelope arg1) throws Exception {

		requestDump = arg1.serialize();

		byte[] bytes = requestDump.getBytes(charsetName);
		byte[] buf = new byte[8 * 1024];

		String line;

		int trycount = 2;
		for (int i = 0; i < trycount; i++) {
			try {

				if (sock == null) {
					sock = new Socket();
					sock.setTcpNoDelay(true);
					sock.setKeepAlive(true);
					sock.setSoTimeout(timeout);
					sock.connect(new InetSocketAddress(host, port), 10000);
					is = sock.getInputStream();
					wr = new DataOutputStream(sock.getOutputStream());
				} else {
					is.reset();
				}

				requestHeader.setRequestProperty(IHttpRequestHeader.SOAPAction, String.format("\"%s\"", arg0));
				requestHeader.setRequestProperty(IHttpRequestHeader.Host, u.getHost());
				requestHeader.setRequestProperty(IHttpRequestHeader.ContentLength, Integer.toString(bytes.length));
				requestHeader.setRequestProperty(IHttpRequestHeader.Expect, IDef._100continue);
				requestHeader.setRequestProperty(IHttpRequestHeader.Connection, IDef.KeepAlive);

				wr.write(requestHeader.toString().getBytes(charsetName));
				wr.flush();

				line = utility.readLine(is, buf, charsetName);
				if (line.isEmpty() || !line.equals("HTTP/1.1 100 Continue")) {
					SoapFault fault = new SoapFault(arg1.version);
					fault.faultcode = "";
					fault.faultstring = line;
					throw fault;
				}

				wr.write(bytes);
				wr.flush();

				IISHttpResponseHeader headerResponse = new IISHttpResponseHeader(is, buf, charsetName);

				if (headerResponse.getResultCode() != IDef.HTTP_OK) {
					SoapFault fault = new SoapFault(arg1.version);
					fault.faultcode = Integer.toString(headerResponse.getResultCode());
					fault.faultstring = headerResponse.getResultMessage();
					throw fault;
				}

				int contentLength = headerResponse.getContentLength();
				byte[] response = new byte[contentLength];

				int count = 0;
				while (count < contentLength) {
					int r = is.read(response, count, contentLength - count);
					if (r < 0) break;
					count += r;
				}

				responseDump = new String(response, charsetName);

				break; // Başarılı ise döngüden çıkılması

			} catch (ConnectException e) {

				if (i == (trycount - 1)) throw e;

				String msg = e.getMessage();
				if (msg.contains("ENETUNREACH") || msg.contains("Network is unreachable")) {
					// Cihaz yeni açıldığında hemen GPRS bağlantı sağlanmıyor
					// Biraz bekleyip tekrar denenmesi
					Thread.sleep(5000);
				}
				close();
			} catch (IOException e) {

				if (i == (trycount - 1)) throw e;
				close();

			} finally {

				close();
			}
		}

		arg1.parse(responseDump);

	}


}
