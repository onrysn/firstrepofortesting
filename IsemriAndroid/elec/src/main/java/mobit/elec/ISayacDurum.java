package mobit.elec;

public interface ISayacDurum {

	ISdurum [] getGELEN_DURUM_KODU();
	void setGELEN_DURUM_KODU(ISdurum MASK_GELEN_DURUM_KODU, ISdurum GELEN_DURUM_KODU)  throws Exception;
	void SayacDurumKontrol() throws Exception;
}
