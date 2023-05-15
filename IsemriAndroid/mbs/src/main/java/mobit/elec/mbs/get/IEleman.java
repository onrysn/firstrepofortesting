package mobit.elec.mbs.get;

import java.util.Date;

public interface IEleman extends com.mobit.IEleman {

	
	Date getTARIH();

	String getFILLER();

	String getSAAT();
	
	String getRESET_DUR();
	
	void setTARIH(Date TARIH);
	
	void setFILLER(String FILLER);

	void setSAAT(String SAAT);

	void setRESET_DUR(String RESET_DUR);

	String getYETKI();
	
	void setYETKI(String YETKI);
	
}
