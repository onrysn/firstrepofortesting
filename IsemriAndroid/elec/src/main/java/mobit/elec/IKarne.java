package mobit.elec;

import com.mobit.IEnum;

public interface IKarne {

	public int getZAMAN_KODU();

	public IEnum getISLEM_TIPI();

	public int getKARNE_NO();

	public int getADET();

	public void setZAMAN_KODU(int zAMAN_KODU);

	public void setISLEM_TIPI(IEnum iSLEM_TIPI);

	public void setKARNE_NO(int kARNE_NO);

	public void setADET(int aDET);

}
