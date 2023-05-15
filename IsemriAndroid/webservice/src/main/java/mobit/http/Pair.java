package mobit.http;

public class Pair {

	String key;
	String value;
	public Pair(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		
		Object _key;
		if(obj instanceof Pair)
			_key = ((Pair)obj).key;
		else if(obj instanceof String)
			return key.compareToIgnoreCase((String)obj) == 0;
		else
			_key = obj;
		return this.key.equals(_key);
	}
	
	@Override
	public String toString()
	{
		if(key.equals(IDef.POST) || key.equals(IDef.GET) || key.equals(IDef.PUT))
			return String.format("%s %s\n", key, value);
			
		return String.format("%s: %s\n", key, value);
	}
}
