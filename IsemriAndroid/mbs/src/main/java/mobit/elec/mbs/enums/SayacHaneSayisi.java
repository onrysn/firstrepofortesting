package mobit.elec.mbs.enums;

import com.mobit.MobitException;
import mobit.elec.ISerialize;
import mobit.elec.enums.ISayacHaneSayisi;

public class SayacHaneSayisi extends mobit.elec.enums.SayacHaneSayisi implements ISerialize {

	public static final SayacHaneSayisi H4 = new SayacHaneSayisi(iH4, mobit.elec.enums.SayacHaneSayisi.H4);
	public static final SayacHaneSayisi H5 = new SayacHaneSayisi(iH5, mobit.elec.enums.SayacHaneSayisi.H5);
	public static final SayacHaneSayisi H6 = new SayacHaneSayisi(iH6, mobit.elec.enums.SayacHaneSayisi.H6);
	
	SayacHaneSayisi(final Object value, final Object baseValue) {
		super(value, baseValue);
	}
	
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		switch (getValue()) {
		case iH4:
			b.append("4"); return;
		case iH5:
			b.append("5"); return;
		case iH6:
			b.append("6"); return;
		}
		throw new MobitException("Tanımsız sayaç hane sayısı");
	}
	
	public static ISayacHaneSayisi fromSayacHaneSayisi(ISayacHaneSayisi haneSayisi){
		
		if(H4.equals(haneSayisi)) return H4;
		if(H5.equals(haneSayisi)) return H5;
		if(H6.equals(haneSayisi)) return H6;
		return null;
	}
}
