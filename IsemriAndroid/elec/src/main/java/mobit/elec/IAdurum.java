package mobit.elec;

public interface IAdurum {

	boolean isEmpty();
	
	int getABONE_DURUM_KODU() throws Exception;
	
	void setABONE_DURUM_KODU(int ABONE_DURUM_KODU);

	String getDURUM_KODU_ACIKLAMA();
	
	void setDURUM_KODU_ACIKLAMA(String DURUM_KODU_ACIKLAMA);

	int getISLEM_KODU();
	
	void setISLEM_KODU(int ISLEM_KODU);

	int getACIKLAMA_DUR();
	
	void setACIKLAMA_DUR(int ACIKLAMA_DUR);
	
	int getKOD_TIPI();
	
	void setKOD_TIPI(int KOD_TIPI);
	
	boolean getCheck();
	
	void setCheck(boolean checked);
	
}
