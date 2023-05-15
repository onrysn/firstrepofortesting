package mobit.elec;

public interface IAboneDurum {

	IAdurum [] getGELEN_DURUM();
	void setGELEN_DURUM(IAdurum MASK_GELEN_DURUM, IAdurum GELEN_DURUM) throws Exception;
	IAciklama getOKUMA_ACIKLAMA();	
	void setOKUMA_ACIKLAMA(IAciklama OKUMA_ACIKLAMA) throws Exception;
	
	void AboneDurumKontrol() throws Exception;
}
