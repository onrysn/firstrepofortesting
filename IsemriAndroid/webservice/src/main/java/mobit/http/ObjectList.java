package mobit.http;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.ksoap2.x.serialization.KvmSerializable;
import org.ksoap2.x.serialization.PropertyInfo;

public class ObjectList implements KvmSerializable {

	public List<Object> list = new ArrayList<Object>();
	
	String namespace;
	String name;
		
	public ObjectList(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}
		
	@Override
	public Object getProperty(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public void setProperty(int index, Object value) {
		// TODO Auto-generated method stub
		if(index == list.size()){
			list.add(value);
			return;
		}
		list.set(index, value);
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
		return list.size();
	}

}
