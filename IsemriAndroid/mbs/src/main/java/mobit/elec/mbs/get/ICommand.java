package mobit.elec.mbs.get;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.mobit.MobitException;
import mobit.elec.ISerialize;

public abstract class ICommand implements ISerialize {

	protected Class<?> classInfo = getClassInfo();
	protected List<IDeserialize> list = new ArrayList<IDeserialize>();
	protected result rs = new result();
	protected Object obj = null;

	public List<IDeserialize> getList() {
		return list;
	}

	public result getResult() {
		return rs;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public int getTESISAT_NO() {
		return 0;
	}
	
	public int getSAHA_ISEMRI_NO() {
		return 0;
	}

	protected abstract Class<?> getClassInfo();

	public abstract void toSerialize(StringBuilder b) throws Exception;

	public int deserialize(String b) throws Exception { // işemri liste indirirken string ham verinin geldiği yer
		int err = 1;
		if (classInfo != null) {
			IDeserialize obj = (IDeserialize) classInfo.newInstance();
			err = obj.deserialize(this, b);
			if (err == 0)
				list.add(obj);
			return 0;
		}
		
		return rs.deserialize(this, b);
	}

	public int getResult(String b) throws UnsupportedEncodingException, MobitException, ParseException {
		return rs.deserialize(this, b);
	}

}
