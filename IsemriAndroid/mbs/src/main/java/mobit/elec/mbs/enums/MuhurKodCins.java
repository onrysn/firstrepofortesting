package mobit.elec.mbs.enums;

import mobit.elec.enums.IMuhurKodCins;

public enum MuhurKodCins implements IMuhurKodCins {
	
	Neden('N'),
	Yer('Y'),
	Durum('D'),
	Iptal('I');
	
	private final char value;
	
	MuhurKodCins(final char value)
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
        case 'N': return "Neden"; 
        case 'Y': return "Yer"; 
        case 'D': return "Durum"; 
        case 'I': return "İptal";
        }
    	return null;
    }
    
    public static MuhurKodCins fromString(String x) {
    	
    	return fromChar(x.charAt(0));
    }
    
    public static MuhurKodCins fromChar(char x) {
        switch(x) {
        case 'N': return Neden; 
        case 'Y': return Yer; 
        case 'D': return Durum; 
        case 'I': return Iptal; 
        }
        return null;
    }

}
