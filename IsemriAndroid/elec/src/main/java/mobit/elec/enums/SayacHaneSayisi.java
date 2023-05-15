package mobit.elec.enums;

import com.mobit.IChecked;
import com.mobit.Enum;

public class SayacHaneSayisi extends Enum implements ISayacHaneSayisi, IChecked  {

	protected static final int iH4 = 4;
	protected static final int iH5 = 5;
	protected static final int iH6 = 6;
	
	public static final SayacHaneSayisi H4 = new SayacHaneSayisi(iH4, null);
	public static final SayacHaneSayisi H5 = new SayacHaneSayisi(iH5, null);
	public static final SayacHaneSayisi H6 = new SayacHaneSayisi(iH6, null);
	
	private boolean check = false;
	
	
	protected SayacHaneSayisi(final Object value, final Object baseValue) {
		super(value, baseValue);
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return (Integer)super.getDerivedValue();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (getValue()) {
		case iH4:
			return "4 haneli";
		case iH5:
			return "5 haneli";
		case iH6:
			return "6 haneli";
		}
		return null;
	}

	public static SayacHaneSayisi fromInteger(int x) {
		switch (x) {
		case iH4:
			return H4;
		case iH5:
			return H5;
		case iH6:
			return H6;
		}
		return null;
	}

	
	
	public boolean getCheck()
	{
		return check;
	}
	public void setCheck(boolean check)
	{
		this.check = check;
	}
	
	private static final SayacHaneSayisi [] values = new SayacHaneSayisi[] {
			H4, 
			H5, 
			H6, 
			};
	
	public static SayacHaneSayisi [] values()
	{
		return values;
	}

	
}
