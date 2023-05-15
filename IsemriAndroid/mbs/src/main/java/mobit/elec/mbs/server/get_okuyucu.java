package mobit.elec.mbs.server;

import com.mobit.MobitException;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.okuyucu;

public class get_okuyucu extends ICommand {

	static final String cmd = "GET OKUYUCU";
	
	int ELEMAN_KODU;
	public int ELEMAN_SIFRE;
	
	public get_okuyucu(int ELEMAN_KODU, int ELEMAN_SIFRE) throws Exception
	{
		if(ELEMAN_KODU >= Math.pow(10, field.s_OKUYUCU_KODU) || ELEMAN_SIFRE >= Math.pow(10, field.s_OKUYUCU_SIFRE)) 
			throw new MobitException("Hatalı kod veya şifre");
		
		this.ELEMAN_KODU = ELEMAN_KODU;
		this.ELEMAN_SIFRE = ELEMAN_SIFRE;
	}
	
	@Override
	protected Class<?> getClassInfo()
	{
		return okuyucu.class;
	}
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format("%s %d %d\n", cmd, ELEMAN_KODU, ELEMAN_SIFRE));
		
	}

}
