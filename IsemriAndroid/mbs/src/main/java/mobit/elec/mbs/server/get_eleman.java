package mobit.elec.mbs.server;

import com.mobit.MobitException;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.eleman;
import mobit.elec.mbs.get.field;

public class get_eleman extends ICommand {

	
	static final String cmd = "GET ELEMAN";
	
	int ELEMAN_KODU;
	public int ELEMAN_SIFRE;
	
	public get_eleman(int ELEMAN_KODU, int ELEMAN_SIFRE) throws Exception
	{
		if(ELEMAN_KODU >= Math.pow(10, field.s_ELEMAN_KODU) || ELEMAN_SIFRE >= Math.pow(10, field.s_ELEMAN_SIFRE)) 
			throw new MobitException("Hatalı kod veya şifre");
		this.ELEMAN_KODU = ELEMAN_KODU;
		this.ELEMAN_SIFRE = ELEMAN_SIFRE;
	}
	
	@Override
	protected Class<?> getClassInfo()
	{
		return eleman.class;
	}
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format("%s %d %d\n", cmd, ELEMAN_KODU, ELEMAN_SIFRE));
		
	}

	
}
