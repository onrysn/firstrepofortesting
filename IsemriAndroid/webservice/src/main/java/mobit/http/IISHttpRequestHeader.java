package mobit.http;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IISHttpRequestHeader implements IHttpRequestHeader {
	
	private List<Pair> properties = new ArrayList<Pair>();
 	
	public IISHttpRequestHeader(URL url, String httpVersion)
	{
		setRequestProperty(POST, String.format("%s %s", url.getFile(), httpVersion));
		setRequestProperty(UserAgent, "Mozilla/4.0 (compatible; MSIE 6.0; MS Web Services Client Protocol 2.0.50727.5420)");
		setRequestProperty(ContentType, "text/xml; charset=utf-8");
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(Pair property : properties)
			sb.append(property);
		sb.append("\n");
		return sb.toString();
	}
	
	public void setRequestProperty(String key, String value)
	{
		boolean add = true;
		for(Pair property : properties){
			if(property.equals(key)){
				property.value = value;
				add = false;
				break;
			}
		}
		if(add) properties.add(new Pair(key, value));
	}
}
