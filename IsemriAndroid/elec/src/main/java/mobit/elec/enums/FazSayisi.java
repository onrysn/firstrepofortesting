package mobit.elec.enums;

import com.mobit.Enum;

public class FazSayisi extends Enum implements IFazSayisi  {

	
	protected static final int iMonofaze = 1;
	protected static final int iTrifaze = 3;
	protected static final int iX5 = 5;
	
	public static final FazSayisi Monofaze = new FazSayisi(iMonofaze, iMonofaze);
	public static final FazSayisi Trifaze = new FazSayisi(iTrifaze, iTrifaze);
	public static final FazSayisi X5 = new FazSayisi(iX5, iX5);
	
	
	protected FazSayisi(final Object value, final Object baseValue)
	{
		super(value, baseValue);
	}
	public int getValue() {
        return (Integer)super.getDerivedValue();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(getValue()){
    	case iMonofaze: return "Monofaze";
    	case iTrifaze: return "Trifaze";
    	case iX5: return "X5";
    	}
    	return null;
    }
    
    public static FazSayisi fromInteger(int x) {
        switch(x) {
        case iMonofaze: return Monofaze; 
        case iTrifaze: return Trifaze; 
        case iX5: return X5; 
        }
        return null;
    }
    
    private static final FazSayisi [] values = new FazSayisi[] {Monofaze, Trifaze, X5};
    public static FazSayisi [] values()
    {
    	return values;
    }
    

}
