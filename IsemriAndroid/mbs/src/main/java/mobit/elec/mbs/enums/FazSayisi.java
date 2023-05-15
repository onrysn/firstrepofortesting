package mobit.elec.mbs.enums;

import mobit.elec.ISerialize;
import mobit.elec.enums.IFazSayisi;

public class FazSayisi extends mobit.elec.enums.FazSayisi implements ISerialize {

	public static final FazSayisi Monofaze = new FazSayisi(iMonofaze, mobit.elec.enums.FazSayisi.Monofaze);
	public static final FazSayisi Trifaze = new FazSayisi(iTrifaze, mobit.elec.enums.FazSayisi.Trifaze);
	public static final FazSayisi X5 = new FazSayisi(iX5, mobit.elec.enums.FazSayisi.X5);
	
	protected FazSayisi(final Object value, final Object baseValue)
	{
		super(value, baseValue);
	}
	@Override
    public void toSerialize(StringBuilder sb) throws Exception {
        // TODO Auto-generated method stub
    	switch(getValue()){
    	case iMonofaze: sb.append("1");return;
    	case iTrifaze: sb.append("3"); return;
    	case iX5: sb.append("5"); return;
    	}
    	throw new Exception("Tanımsız faz sayısı");
    }
	
	public static IFazSayisi fromFazSayisi(IFazSayisi fs)
	{
		if(fs.equals(mobit.elec.enums.FazSayisi.Monofaze))
			return Monofaze;
		else if(fs.equals(mobit.elec.enums.FazSayisi.Trifaze))
			return Trifaze;
		else if(fs.equals(mobit.elec.enums.FazSayisi.X5))
			return X5;
		return null;
		
	}
}
