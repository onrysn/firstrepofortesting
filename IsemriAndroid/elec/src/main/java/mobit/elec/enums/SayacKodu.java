package mobit.elec.enums;


public class SayacKodu extends com.mobit.Enum implements ISayacKodu  {

	protected static final int sAktif = 1;
	protected static final int sKombi = 0;
	protected static final int sReaktif = 4;
	protected static final int sKapasitif = 5;
	
	public static final SayacKodu Aktif = new SayacKodu(sAktif, null);
	public static final SayacKodu Kombi = new SayacKodu(sKombi, null);
	public static final SayacKodu Reaktif = new SayacKodu(sReaktif, null);
	public static final SayacKodu Kapasitif = new SayacKodu(sKapasitif, null);

	protected SayacKodu(Object value, Object baseValue)
	{
		super(value, baseValue);
		
	}

	public int getValue() {
		return (Integer)getDerivedValue();
	}
	
	@Override
	public boolean is(ISayacKodu sk){
		
		if(equals(Kombi)) return true;
		if(sk.equals(Kombi)) return true;
		else if(equals(sk)) return true;
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (getValue()) {
		case 0:
			return "Kombi";
		case 1:
			return "Aktif";
		case 4:
			return "Reaktif";
		case 5:
			return "Kapasitif";
		}
		return null;
	}

	public static SayacKodu fromInteger(int x) {
		switch (x) {
		case 0:
			return Kombi;
		case 1:
			return Aktif;
		case 4:
			return Reaktif;
		case 5:
			return Kapasitif;
		}
		return null;
	}
	

}
