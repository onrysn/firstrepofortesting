package mobit.http;

import java.util.Hashtable;

import org.ksoap2.x.serialization.KvmSerializable;
import org.ksoap2.x.serialization.PropertyInfo;

public class ReturnValue implements KvmSerializable {

	String namespace;
	String name;
	
	public Object value;
	
	public ReturnValue(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}
	
	@Override
	public Object getProperty(int index) {
		// TODO Auto-generated method stub
		return (index == 0) ? value : null;
		
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void setProperty(int index, Object value) {
		// TODO Auto-generated method stub
		if(index == 0) this.value = value;
	}

	@Override
	public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
		// TODO Auto-generated method stub
		info.type = PropertyInfo.OBJECT_CLASS;
		info.name = "";
	}

	@Override
	public int getPropertyIndex(String propertyName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
