package mobit.elec.enums;

import com.mobit.Enum;

public class EndeksTipi extends Enum implements IEndeksTipi {
	
	public static final EndeksTipi Tanimsiz = new EndeksTipi(-1, -1);
	public static final EndeksTipi Toplam = new EndeksTipi(0, 0);
	public static final EndeksTipi Gunduz = new EndeksTipi(1, 1);
	public static final EndeksTipi Puant = new EndeksTipi(2, 2);
	public static final EndeksTipi Gece = new EndeksTipi(3, 3);
	public static final EndeksTipi Enduktif = new EndeksTipi(4, 4);
	public static final EndeksTipi Kapasitif = new EndeksTipi(5, 5);
	public static final EndeksTipi Demand = new EndeksTipi(6, 6);
		
	protected EndeksTipi(final Object value, final Object baseValue)
	{
		super(value, baseValue);
	}
	
	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return (Integer)getDerivedValue();
	}
    
	@Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(getValue()) {
    	case 0: return "Toplam";
        case 1: return "Gündüz";
        case 2: return "Puant";
        case 3: return "Gece";
        case 4: return "Endüktif";
        case 5: return "Kapasitif";
        case 6: return "Demand";
        }
    	return null;
    }
    
    public static EndeksTipi fromInteger(int x) {
        switch(x) {
        case 0: return Toplam;
        case 1: return Gunduz;
        case 2: return Puant;
        case 3: return Gece;
        case 4: return Enduktif;
        case 5: return Kapasitif;
        case 6: return Demand;
        }
        return null;
    }


}
