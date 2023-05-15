package mobit.elec;

import com.mobit.ICbs;

public interface ICbsPos {

	public int getTESISAT_NO();

	public void setTESISAT_NO(int tESISAT_NO);

	public ICbs getCBS();

	public void setCBS(ICbs cBS);

	public IAciklama getCBS_EK1();

	public void setCBS_EK1(IAciklama cBS_EK1) throws Exception;

	public IAciklama getCBS_EK2();

	public void setCBS_EK2(IAciklama cBS_EK2) throws Exception;

	public IAciklama getCBS_EK3();

	public void setCBS_EK3(IAciklama cBS_EK3) throws Exception;

}
