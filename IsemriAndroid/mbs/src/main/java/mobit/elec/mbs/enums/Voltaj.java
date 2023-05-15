package mobit.elec.mbs.enums;

import com.mobit.MobitException;
import mobit.elec.ISerialize;
import mobit.elec.enums.IVoltaj;

public class Voltaj extends mobit.elec.enums.Voltaj implements ISerialize {

	public static final Voltaj V220 = new Voltaj(iV220, mobit.elec.enums.Voltaj.V220);
	public static final Voltaj V380 = new Voltaj(iV380, mobit.elec.enums.Voltaj.V380);
	public static final Voltaj V57 = new Voltaj(iV57, mobit.elec.enums.Voltaj.V57);
	
	private Voltaj(final Object value, final Object baseValue)
	{
		super(value, baseValue);
		
	}
	
	@Override
    public void toSerialize(StringBuilder b) throws Exception {
        // TODO Auto-generated method stub
    	switch(getValue()) {
			case iV220:
				b.append("220");
				return;
			case iV380:
				b.append("380");
				return;
			case iV57:
				b.append("057");
				return;
		}
        throw new MobitException("Tanımsız voltaj değeri");
    }
	
	public static IVoltaj fromVoltaj(IVoltaj voltaj)
	{
		
		if(V220.equals(voltaj)) return V220;
		else if(V380.equals(voltaj)) return V380;
		else if(V57.equals(voltaj)) return V57;
		return null;
	}
	
}
