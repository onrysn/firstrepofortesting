package mobit.elec;


public interface ISdurum {

	
	boolean isEmpty();
	
	int getSAYAC_DURUM_KODU() throws Exception;

	String getSAYAC_KODU_ACIKLAMA();

	int getISLEM_KODU();

	int getACIKLAMA_DUR();
	
	int getKOD_TIPI();

	void setSAYAC_DURUM_KODU(int sAYAC_DURUM_KODU);

	void setSAYAC_KODU_ACIKLAMA(String sAYAC_KODU_ACIKLAMA);

	void setISLEM_KODU(int iSLEM_KODU);

	void setACIKLAMA_DUR(int aCIKLAMA_DUR);
	
	void setKOD_TIPI(int KOD_TIPI);
	
	boolean getCheck();
	
	void setCheck(boolean checked);

}
