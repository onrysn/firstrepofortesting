package mobit.elec.mbs.enums;

import java.util.Locale;

import mobit.elec.ISerialize;
import mobit.elec.enums.ISayacKodu;

public class SayacKodu extends mobit.elec.enums.SayacKodu implements ISerialize {

	public static final SayacKodu Aktif = new SayacKodu(sAktif, mobit.elec.enums.SayacKodu.Aktif);
	public static final SayacKodu Kombi = new SayacKodu(sKombi, mobit.elec.enums.SayacKodu.Kombi);
	public static final SayacKodu Reaktif = new SayacKodu(sReaktif, mobit.elec.enums.SayacKodu.Reaktif);
	public static final SayacKodu Kapasitif = new SayacKodu(sKapasitif, mobit.elec.enums.SayacKodu.Kapasitif);
	
	private SayacKodu(final Object value, final Object baseValue)
	{
		super(value, baseValue);
		
	}
	
	private static String format = "%01d";
	
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		int v = getValue();
		if(v == sKombi) v = sAktif; // Kombi ise aktif değer atama
		b.append(String.format(Locale.ENGLISH, format, v));
	}
	
	public static ISayacKodu fromSayacKodu(ISayacKodu sayacKodu)
	{
		if(Aktif.equals(sayacKodu)) return Aktif;
		if(Kombi.equals(sayacKodu)) return Kombi;
		if(Reaktif.equals(sayacKodu)) return Reaktif;
		if(Kapasitif.equals(sayacKodu)) return Kapasitif;
		return null;
	}
}
