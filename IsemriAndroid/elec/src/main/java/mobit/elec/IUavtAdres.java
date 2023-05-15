package mobit.elec;

import com.mobit.IEnum;

public interface IUavtAdres {
	
	public int getTESISAT_NO();

	public void setTESISAT_NO(int tESISAT_NO);

	public IEnum getSEVIYE();
	
	public void setSEVIYE(IEnum sEVIYE);

	public String getKOD();

	public void setKOD(String kOD);

	public IAciklama getACIKLAMA();

	public void setACIKLAMA(IAciklama aCIKLAMA) throws Exception;


}
