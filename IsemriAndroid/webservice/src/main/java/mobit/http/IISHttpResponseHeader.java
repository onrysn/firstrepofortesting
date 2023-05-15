package mobit.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IISHttpResponseHeader implements IDef {

	private String httpVersion;
	private int resultCode;
	private String resultMessage;
	private List<Pair> properties = new ArrayList<Pair>();

	public String getHttpVersion() {
		return httpVersion;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public int getContentLength() {
		return Integer.parseInt(getResponseProperty(IDef.ContentLength));
	}

	public IISHttpResponseHeader(BufferedReader rd) throws IOException {
		rd.readLine(); //
		String line = rd.readLine();
		String[] result = line.split(" ");
		httpVersion = result[0];
		resultCode = Integer.parseInt(result[1].trim());
		resultMessage = result[2];

		while ((line = rd.readLine()) != null && !line.isEmpty()) {
			int index = line.indexOf(':');

			if (index < 0)
				continue;

			String key;
			String value;
			if (index == 0) {
				key = "";
				value = line.substring(index + 1).trim();
			} else {
				key = line.substring(0, index);
				value = line.substring(index + 1).trim();
			}

			setResponseProperty(key, value);
			/*
			 * if(key.compareToIgnoreCase(IDef.ContentLength) == 0) break;
			 */
		}
	}
	
	public IISHttpResponseHeader(InputStream is, byte [] buf, String charsetName) throws Exception {

		int i = 0;
		String [] lines = new String[1024];
		String line;
		while(i < lines.length){ 
			line = utility.readLine(is, buf, charsetName);
			if(line.isEmpty() && i > 0) break; // Sona gelinmiş demek!
			lines[i++] = line;
		}
		
		if(i == lines.length)  throw new Exception("HTTP cevap başlık satır sayısı cok fazla!");
		
		for (i = 0; i < lines.length && lines[i] != null; i++) {

			if (i == 1) {
				String[] result = lines[i].split(" ");
				httpVersion = result[0];
				resultCode = Integer.parseInt(result[1].trim());
				resultMessage = result[2];
				continue;
			}

			int index = lines[i].indexOf(':');

			if (index < 0) {
				continue;
			}

			String key;
			String value;
			if (index == 0) {
				key = "";
				value = lines[i].substring(index + 1).trim();
			} else {
				key = lines[i].substring(0, index);
				value = lines[i].substring(index + 1).trim();
			}

			setResponseProperty(key, value);
			
		}
	}


	public void setResponseProperty(String key, String value) {
		boolean add = true;
		for (Pair property : properties) {
			if (property.equals(key)) {
				property.value = value;
				add = false;
				break;
			}
		}
		if (add)
			properties.add(new Pair(key, value));
	}

	public String getResponseProperty(String key) {
		for (Pair property : properties) {
			if (property.equals(key)) {
				return property.value;
			}
		}
		return "";
	}
}
