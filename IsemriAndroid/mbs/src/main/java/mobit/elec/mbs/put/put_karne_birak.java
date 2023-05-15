package mobit.elec.mbs.put;

import mobit.elec.IKarneBirak;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_karne_birak extends ICommand implements IKarneBirak {

	private int KARNE_NO;
	
	public int getKARNE_NO() {
		return KARNE_NO;
	}
	public void setKARNE_NO(int kARNE_NO) {
		com.mobit.utility.check(kARNE_NO, field.s_KARNE_NO);
		KARNE_NO = kARNE_NO;
	}
	
	public put_karne_birak(int kARNE_NO) {
		super();
		setKARNE_NO(kARNE_NO);
	}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	static final String format = "PUT KARNE_BIRAK %0"+field.s_KARNE_NO+"d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, KARNE_NO));
		
	}

}
