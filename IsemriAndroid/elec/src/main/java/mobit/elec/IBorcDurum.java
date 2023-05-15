package mobit.elec;


public interface IBorcDurum {

	public int getTESISAT_NO();

	public String getUNVAN();

	public int getBORC_ADET();

	public double getBORC_TUTARI();

	public double getBORC_GECIKME();

	public double getACMA_KAPAMA_BEDELI();

	public String getKESME_DUR();

	public void setTESISAT_NO(int TESISAT_NO);

	public void setUNVAN(String UNVAN);

	public void setBORC_ADET(int BORC_ADET);

	public void setBORC_TUTARI(double BORC_TUTARI);

	public void setBORC_GECIKME(double BORC_GECIKME);

	public void setACMA_KAPAMA_BEDELI(double ACMA_KAPAMA_BEDELI);

	public void setKESME_DUR(String KESME_DUR);

}
