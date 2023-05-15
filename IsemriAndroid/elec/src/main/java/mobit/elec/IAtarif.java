package mobit.elec;

import com.mobit.ICbs;
import com.mobit.IIslem;

public interface IAtarif extends IIslem{
	
	public int getTESISAT_NO();
	
	public void setTESISAT_NO(int tESISAT_NO);

	public IAciklama getADRES_TARIF();
	
	public void setADRES_TARIF(IAciklama aDRES_TARIF) throws Exception;

	public ICbs getCBS();

	public void setCBS(ICbs cBS);


}
