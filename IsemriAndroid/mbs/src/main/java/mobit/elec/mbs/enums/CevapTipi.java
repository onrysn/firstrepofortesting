package mobit.elec.mbs.enums;

import mobit.elec.enums.ICevapTipi;

public enum CevapTipi implements ICevapTipi {
	
	Boolean(0),
	Integer(1),
	Float(2),
	Strings(3),
	CoktanSecmeli(4);
	
	private final int value;
	
	CevapTipi(final int value)
	{
		this.value = value;
	}
	public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(getValue()) {
        case 0: return Boolean.name();
        case 1: return Integer.name();
        case 2: return Float.name();
        case 3: return Strings.name();
        case 4: return "Çoktan Seçmeli";
        }
    	return null;
    }
    
    public static CevapTipi fromInteger(int x) {
        switch(x) {
        case 0: return Boolean;
        case 1: return Integer;
        case 2: return Float;
        case 3: return Strings;
        case 4: return CoktanSecmeli;
        }
        return null;
    }

}
