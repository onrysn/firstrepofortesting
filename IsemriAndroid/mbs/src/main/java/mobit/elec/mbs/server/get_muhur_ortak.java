package mobit.elec.mbs.server;


import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.muhur;


public abstract class get_muhur_ortak extends ICommand {

	
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return muhur.class;
	}
	
	public int deserialize(String b) throws Exception
	{
	
		int index = 0;
		int err = 1;
		int TESISAT_NO = Integer.parseInt(b.substring(index, index+=field.s_TESISAT_NO)); 
	
		if(classInfo != null){
			while(true){
				b = b.substring(index);
				index = b.indexOf('*');
				if(index < 0) break;
				muhur obj = (muhur)classInfo.newInstance();
				obj.setTESISAT_NO(TESISAT_NO);
				err = obj.deserialize(this, b.substring(0, index));
				if(err == 0) list.add(obj);
				index++;
			}
		}
		return err;	
	}
}
