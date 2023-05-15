package mobit.elec.enums;

import com.mobit.Enum;

public class SayacCinsi extends Enum implements ISayacCinsi  {

	protected static final char cElektronik = 'E'; // 1
	protected static final char cKombi = 'K'; // 2
	protected static final char cMekanik = 'M'; // 0
	
	public static final SayacCinsi Elektronik = new SayacCinsi(cElektronik, cElektronik);
	public static final SayacCinsi Kombi = new SayacCinsi(cKombi, cKombi); 
	public static final SayacCinsi Mekanik = new SayacCinsi(cMekanik, cMekanik);
	
	protected SayacCinsi(final Object value, final Object baseValue) {
		super(value, baseValue);
	}

	public int getValue() {
		return (Character)getDerivedValue();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (getValue()) {
		case cMekanik:
			return "Mekanik";
		case cElektronik:
			return "Elektronik";
		case cKombi:
			return "Kombi";
		}
		return null;
	}
	
	public static SayacCinsi fromString(String x) {
		return fromChar(x.charAt(0));
	}

	public static SayacCinsi fromChar(char x) {
		switch (x) {
		case cMekanik:
		case '0':
			return Mekanik;
		case cElektronik:
		case '1':
			return Elektronik;
		case cKombi:
		case '2':
			return Kombi;
		}
		return null;
	}
	

	@Override
	public boolean is(ISayacCinsi sayacCinsi) {
		// TODO Auto-generated method stub
		if(equals(sayacCinsi)) return true; 
		if(sayacCinsi.equals(SayacCinsi.Elektronik) && (equals(SayacCinsi.Elektronik) || equals(SayacCinsi.Kombi))) return true;
		return false; 
	}
	
	private static final SayacCinsi []values = new SayacCinsi[] {Elektronik, Kombi, Mekanik};
	public static SayacCinsi [] values()
	{
		return values; 
	}
	

}
