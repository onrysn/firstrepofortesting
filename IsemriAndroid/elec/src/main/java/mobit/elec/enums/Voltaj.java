package mobit.elec.enums;


public class Voltaj extends com.mobit.Enum implements IVoltaj {

	protected static final int iV220 = 220;
	protected static final int iV380 = 380;
	protected static final int iV57 = 57;
	
	public static final Voltaj V220 = new Voltaj(iV220, null);
	public static final Voltaj V380 = new Voltaj(iV380, null);
	public static final Voltaj V57 = new Voltaj(iV57, null);
	
	protected Voltaj(final Object value, final Object baseValue)
	{
		super(value, baseValue);
		
	}
	public int getValue() {
        return (Integer)getDerivedValue();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(getValue()){
    	case iV220: return "220 V"; 
    	case iV380: return "380 V"; 
    	case iV57: return "57 V"; 
    	}
        return null;
    }
    public static Voltaj fromInteger(int x) {
        switch(x) {
        case iV220: return V220; 
        case iV380: return V380; 
        case iV57: return V57; 
        }
        return null;
    }
    
    private static final Voltaj [] values = new Voltaj[] {
    		V220,
    		V380,
    		V57
    };
	
	public static Voltaj [] values()
	{
		return values;
	}
    
}
