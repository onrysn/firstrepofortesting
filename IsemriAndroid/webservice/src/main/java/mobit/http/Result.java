package mobit.http;

import java.util.Hashtable;

import org.ksoap2.x.serialization.PropertyInfo;
import org.ksoap2.x.serialization.SoapObject;

public class Result extends SoapObject {
	
	public String ReturnCode;
	public String ReturnMessage;
	public Object ReturnValue;
	
	boolean success = false;
	
	public Result(String namespace, String name) {
		super(namespace, name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object getProperty(int arg0) {

		switch (arg0) {
		case 0:
			return ReturnCode;
		case 1:
			if(success)
				return ReturnMessage;
			else
				return ReturnValue;
		case 2:
			return ReturnValue;
		}

		return null;
	}

	@Override
	public int getPropertyCount() {
		return propertyList.length;
	}
	
	private static final String [] propertyList = new String[]{
			"ReturnCode",
			"ReturnMessage",
			"ReturnValue"
	};
	
	public int getPropertyIndex(String propertyName)
	{
		if(propertyName.equals(propertyList[0])) return 0;
		if(propertyName.equals(propertyList[1])) return 1;
		if(propertyName.equals(propertyList[2])) return 2;
		return -1;
	}

	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = propertyList[index];
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = propertyList[index];
			break;
		case 2:
			info.type = PropertyInfo.OBJECT_CLASS;
			info.name = propertyList[index];
			break;
		
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			ReturnCode = value.toString();
			success = ReturnCode.equals("Success");
			break;
		case 1:
			ReturnMessage = value.toString();
			break;
		case 2:
			ReturnValue = value;
			break;
		
		}
	}
}
