package mobit.elec.mbs.enums;

import mobit.elec.ISerialize;
import mobit.elec.enums.IMulkiyet;

public enum Mulkiyet implements IMulkiyet, ISerialize  {
	
	KurumSayaci(1),
	GeciciKurumSayaci(0), 
	AboneKendiSayaci(2);
	
	private final int value;
	
	Mulkiyet(final int value)
	{
		this.value = value;
		
	}
	public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(this){
    	case GeciciKurumSayaci: return "Geçici Kurum Sayacı";
    	case KurumSayaci: return "Kurum Sayacı";
    	case AboneKendiSayaci: return "Abone Kendi Sayacı";
    	}
        return "";
    }
    
    public static Mulkiyet fromInteger(int x) {
        switch(x) {
        case 0: return GeciciKurumSayaci; 
        case 1: return KurumSayaci; 
        case 2: return AboneKendiSayaci; 
        }
        return null;
    }
    
    @Override
    public void toSerialize(StringBuilder b) throws Exception {
        // TODO Auto-generated method stub
    	switch(this){
    	case GeciciKurumSayaci: b.append("0"); return; 
    	case KurumSayaci: b.append("1"); return;
    	case AboneKendiSayaci: b.append("2"); return;
    	}
        throw new Exception("Tanımsız mülkiyet kodu");
    }
	

}
