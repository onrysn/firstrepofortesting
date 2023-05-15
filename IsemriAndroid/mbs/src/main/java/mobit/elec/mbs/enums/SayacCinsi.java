package mobit.elec.mbs.enums;

import com.mobit.MobitException;
import mobit.elec.ISerialize;
import mobit.elec.enums.ISayacCinsi;

public class SayacCinsi extends mobit.elec.enums.SayacCinsi implements ISerialize {

	
	public static final SayacCinsi Elektronik = new SayacCinsi(cElektronik, mobit.elec.enums.SayacCinsi.Elektronik);
	public static final SayacCinsi Kombi = new SayacCinsi(cKombi, mobit.elec.enums.SayacCinsi.Kombi); 
	public static final SayacCinsi Mekanik = new SayacCinsi(cMekanik, mobit.elec.enums.SayacCinsi.Mekanik);
	
	protected SayacCinsi(final Object value, final Object baseValue)
	{
		super(value, baseValue);
	}
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		if(equals(Elektronik)){
			b.append("1"); return;
		}
		else if(equals(Kombi)){
			b.append("2"); return;
		}
		else if(equals(Mekanik)){
			b.append("0"); return;
		}
		
		throw new MobitException("Tanımsız sayaç cinsi");
	}
	
	public static ISayacCinsi fromSayacCinsi(ISayacCinsi sc)
	{
		if(sc.equals(mobit.elec.enums.SayacCinsi.Elektronik))
			return Elektronik;
		else if(sc.equals(mobit.elec.enums.SayacCinsi.Kombi))
			return Kombi;
		else if(sc.equals(mobit.elec.enums.SayacCinsi.Mekanik))
			return Mekanik;
		return null;
		
	}
	
}
