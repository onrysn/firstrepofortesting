package mobit.eemr;


public class utility {


	public static boolean ParseDataSet(String pDataSet, String[] ppObisCode, String[] ppObisValue) {
		int i, j;
		i = pDataSet.indexOf('(');
		if (i < 0)
			return false;
		ppObisCode[0] = pDataSet.substring(0, i);
		j = pDataSet.indexOf(')');
		if (j < 0)
			return false;
		ppObisValue[0] = pDataSet.substring(i + 1, j);
		if (ppObisCode[0].equals("96.71"))
		{
			if (pDataSet.length()-1>j)
				ppObisValue[0]=ppObisValue[0]+":adet:"+pDataSet.substring(j+2,pDataSet.length()-1);
		}
		return true;
	}
	
	public static IMeterReader create(String _class) throws Exception
	{
		ClassLoader classLoader = utility.class.getClassLoader();
		Object obj = null;
		Class<?> c = null;
		
		c = classLoader.loadClass(_class); // "mobit.eemr.MeterReaderCpp"
		obj = c.newInstance();
		
		return (IMeterReader)obj;
		
	}

	public static String [] obisCode(String code, int begin, int end)
	{
		
		int count = 1;
		if(begin >= 1) count += ((end - begin) + 1);
		String [] items = new String[count];
		items[0] = code;
		
		if(count > 1) for(int i = begin; i <= end; i++) items[i] = String.format("%s*%d", code, i);
		
		return items;
		
	}
	
	public static String [] obisCode(String code)
	{
		return obisCode(code, 0, 0);
	}
}
