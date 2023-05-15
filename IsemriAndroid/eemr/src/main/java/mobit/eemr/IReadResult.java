package mobit.eemr;

import java.util.Date;
import java.util.logging.Logger;

public interface IReadResult extends IReadEvent {

	//Gövde ve Manyetik: bilgileri eklenecek!
	//Muhammed Gökkaya

	public static final String s_sayac_no = "0.0.0";
	public static final String s_sayac_saati = "0.9.1";
	public static final String s_sayac_tarihi = "0.9.2";
	public static final String s_demand_end = "1.6.0";
	public static final String s_top_end = "1.8.0";
	public static final String s_gunduz_end = "1.8.1";
	public static final String s_puant_end = "1.8.2";
	public static final String s_gece_end = "1.8.3";
	public static final String s_enduktif_end = "5.8.0";
	public static final String s_kapasitif_end = "8.8.0";
	public static final String s_uretim_tarih = "96.1.3";
	public static final String s_kalibrasyon_tarih = "96.2.5";
	public static final String s_tarife_bilgi = "96.2.2";
	public static final String s_pil_durum_kodu = "96.6.1";
	public static final String s_birinci_faz_kesilme_sayisi = "96.7.1";
	public static final String s_ikinci_faz_kesilme_sayisi = "96.7.2";
	public static final String s_ucuncu_faz_kesilme_sayisi = "96.7.3";
	public static final String s_klemens_kapagi_acilma = "96.71";
	public static final String s_demand_reset = "0.1.0";
	public static final String s_demand_reset_tarih = "0.1.2*1";
	public static final String s_lun_manyetik = "96.80*1";
	//public static final String s_lun_manyetik = "96.80.0";
	public static final String s_manyetik = "96.77.6*1";
	public static final String s_govde = "96.70";
	public static final String s_toplam_notr_enerji = "C.18.0";
	public static final String s_toplam_notr_faz_enerji_farki = "C.18.1";
	public static final String s_cos_l1 = "33.7.0";
	public static final String s_cos_l2 = "53.7.0";
	public static final String s_cos_l3 = "73.7.0";

	public static final String[] s_kesintiler = utility.obisCode("96.77.0", 1, 10);

	public static final String[] s_faz1_kesintiler = utility.obisCode("96.77.1", 1, 10); 
	public static final String[] s_faz2_kesintiler = utility.obisCode("96.77.2", 1, 10); 
	public static final String[] s_faz3_kesintiler = utility.obisCode("96.77.3", 1, 10); 
	
	
	public static final String [] s_demand_ends = utility.obisCode("1.6.0", 1, 10);
	public static final String [] s_top_ends = utility.obisCode("1.8.0", 1, 10);
	public static final String [] s_gunduz_ends = utility.obisCode("1.8.1", 1, 10);
	public static final String [] s_puant_ends = utility.obisCode("1.8.2", 1, 10);
	public static final String [] s_gece_ends = utility.obisCode("1.8.3", 1, 10);
	public static final String [] s_enduktif_ends = utility.obisCode("5.8.0", 1, 10);
	public static final String [] s_kapasitif_ends = utility.obisCode("8.8.0", 1, 10);
	public static final String [] s_toplam_notr_enerjis = utility.obisCode("C.18.0", 1, 10);
	public static final String [] s_toplam_notr_faz_enerji_farkis = utility.obisCode("C.18.1", 1, 10);

	public static final String s_faz1_akim = "31.7.0";
	public static final String s_faz2_akim = "51.7.0";
	public static final String s_faz3_akim = "71.7.0";
	
	public static final String s_faz1_gerilim = "32.7.0";
	public static final String s_faz2_gerilim = "52.7.0";
	public static final String s_faz3_gerilim = "72.7.0";
	
	
	void reset();
	int getRetry();
	int incRetry();
	
	public void setReadMode(ReadMode2 mod);
	public ReadMode2 getReadMode();
	
	public void setMeterType(MeterType meterType);
	public MeterType getMeterType();
	
	public void set_okuma_zamani(Date date);
	public Date get_okuma_zamani();
	
	public void set_Information(MbtMeterInformation emi);
	
	public MbtMeterInformation get_Information();
	public String get_toplam_notr_enerji();
	public String get_toplam_notr_faz_enerji_farki();
	public String get_cos_l1();
	public String get_cos_l2();
	public String get_cos_l3();
	public String get_sayac_no();
	public String get_demand_end();
	public String get_top_end();
	public String get_gunduz_end();
	public String get_puant_end();
	public String get_gece_end();
	public String get_enduktif_end();
	public String get_kapasitif_end();
	public String get_uretim_tarih();
	public String get_kalibrasyon_tarih();
	public String get_birinci_faz_kesilme_sayisi();
	public String get_ikinci_faz_kesilme_sayisi();
	public String get_ucuncu_faz_kesilme_sayisi();
	public String get_FlagCode();
	public String get_kimlik();

	public String get_sayac_saati();
	public String get_sayac_tarihi();
	public Date get_sayac_zamani();
	
	public String get_pil_durum_kodu();
	public String get_tarife_bilgi();
	public String get_klemens_kapagi_acilma();
	public String get_govde();
	public String get_lun_manyetik();
	public String get_manyetik();
	public String get_demand_reset();
	public String get_demand_reset_tarih();
	
	public String[] get_kesintiler();
	public String[] get_faz1_kesintiler();
	public String[] get_faz2_kesintiler();
	public String[] get_faz3_kesintiler();
	
	public Boolean getX5();
	public Boolean getKombi();
	public Integer getFazSayisi();
	public Integer getHaneSayisi();
	public Integer getVoltaj();
	public Integer getUretimYili();
	
	public void tespit();
	
	String get_demand_end(int i);
	String get_top_end(int i);
	String get_gunduz_end(int i);
	String get_puant_end(int i);
	String get_gece_end(int i);
	String get_enduktif_end(int i);
	String get_kapasitif_end(int i);
	
	String get_faz1_akim();
	String get_faz2_akim();
	String get_faz3_akim();
	
	String get_faz1_gerilim();
	String get_faz2_gerilim();
	String get_faz3_gerilim();
	
	
	public ObisMapItem[] GetObisCodeMap();

	public void SetObisCodeMap(ObisMapItem[] pObisCodeMap);

	public static final ObisMapItem[] s_aktif_endeks = new ObisMapItem[] {

			new ObisMapItem(s_sayac_no, "Seri numarası", true, 1),
			new ObisMapItem(s_top_end, "Kümülatif aktif enerji", true, 1),
			//muhammed gökkaya
			new ObisMapItem(s_gunduz_end, "Toplam enerji T1", true, 1),
			new ObisMapItem(s_puant_end, "Toplam enerji T2", true, 1),
			new ObisMapItem(s_gece_end, "Toplam enerji T3", true, 1),
			new ObisMapItem(s_demand_end, "Maximum Aktif Güç", false, 1)
					 
	};
	
	public static final ObisMapItem[] s_aktif_detay = new ObisMapItem[] {
			
			new ObisMapItem(s_sayac_no, "Seri numarası", true, 1),
			new ObisMapItem(s_top_end, "Kümülatif aktif enerji", true, 1),
			new ObisMapItem(s_gunduz_end, "Toplam enerji T1", true, 1),
			new ObisMapItem(s_puant_end, "Toplam enerji T2", true, 1),
			new ObisMapItem(s_gece_end, "Toplam enerji T3", true, 1),
			new ObisMapItem(s_demand_end, "Maximum Aktif Güç", false, 1),
			
			new ObisMapItem(s_uretim_tarih, "Üretim tarihi", false, 1),
			new ObisMapItem(s_kalibrasyon_tarih, "Kalibrasyon tarihi", false, 1),
			new ObisMapItem(s_tarife_bilgi, "Tarife bilgi değişikliği tarihi", false, 1),

			//muhammed gökkaya
			new ObisMapItem(s_klemens_kapagi_acilma, "Klemens kapağının son açılma tarihi ve sayısı", false, 1),
			new ObisMapItem(s_demand_reset, "Demand reset tarihi", false, 1),
			new ObisMapItem(s_pil_durum_kodu, "Pil durum kodu", false, 1),
			new ObisMapItem(s_sayac_saati, "Sayaç saati", false, 1),
			new ObisMapItem(s_sayac_tarihi, "Sayaç tarihi", false, 1),

			// -------------------------------------------------------------------------
			// Dikkat! Bu blok en son olması gerekiyor.
			new ObisMapItem(s_kesintiler[0], "", false, 0), new ObisMapItem(s_kesintiler[1], "", false, 0),
			new ObisMapItem(s_kesintiler[2], "", false, 0), new ObisMapItem(s_kesintiler[3], "", false, 0),
			new ObisMapItem(s_kesintiler[4], "", false, 0), new ObisMapItem(s_kesintiler[5], "", false, 0),
			new ObisMapItem(s_kesintiler[6], "", false, 0), new ObisMapItem(s_kesintiler[7], "", false, 0),
			new ObisMapItem(s_kesintiler[8], "", false, 0), new ObisMapItem(s_kesintiler[9], "", false, 0)

			// -------------------------------------------------------------------------
			 
	};
	
	public static final ObisMapItem[] s_kombi_endeks = new ObisMapItem[] {
			
			new ObisMapItem(s_sayac_no, "Seri numarası", true, 1),
			new ObisMapItem(s_top_end, "Kümülatif aktif enerji", true, 1),
			new ObisMapItem(s_gunduz_end, "Toplam enerji T1", true, 1),
			new ObisMapItem(s_puant_end, "Toplam enerji T2", true, 1),
			new ObisMapItem(s_gece_end, "Toplam enerji T3", true, 1),
			new ObisMapItem(s_demand_end, "Maximum Aktif Güç", false, 1),
			new ObisMapItem(s_enduktif_end, "Reaktif endüktif", false, 1),
			new ObisMapItem(s_kapasitif_end, "Reaktif kapasitif", false, 1),
			 
	};
	
	public static final ObisMapItem[] s_kombi_detay = new ObisMapItem[] {
			
			new ObisMapItem(s_sayac_no, "Seri numarası", true, 1),
			new ObisMapItem(s_top_end, "Kümülatif aktif enerji", true, 1),
			new ObisMapItem(s_gunduz_end, "Toplam enerji T1", true, 1),
			new ObisMapItem(s_puant_end, "Toplam enerji T2", true, 1),
			new ObisMapItem(s_gece_end, "Toplam enerji T3", true, 1),
			new ObisMapItem(s_demand_end, "Maximum Aktif Güç", false, 1),
			new ObisMapItem(s_enduktif_end, "Reaktif endüktif", false, 1),
			new ObisMapItem(s_kapasitif_end, "Reaktif kapasitif", false, 1),
			
			new ObisMapItem(s_uretim_tarih, "Üretim tarihi", false, 1),
			new ObisMapItem(s_kalibrasyon_tarih, "Kalibrasyon tarihi", false, 1),
			new ObisMapItem(s_tarife_bilgi, "Tarife bilgi değişikliği tarihi", false, 1),
			new ObisMapItem(s_klemens_kapagi_acilma, "Klemens kapağının son açılma tarihi ve sayısı", false, 1),
			new ObisMapItem(s_demand_reset, "Demand reset tarihi", false, 1),
			new ObisMapItem(s_pil_durum_kodu, "Pil durum kodu", false, 1),
			new ObisMapItem(s_sayac_saati, "Sayaç saati", false, 1),
			new ObisMapItem(s_sayac_tarihi, "Sayaç tarihi", false, 1),

			// -------------------------------------------------------------------------
			// Dikkat! Bu blok en son olması gerekiyor.
			new ObisMapItem(s_kesintiler[0], "", false, 0), new ObisMapItem(s_kesintiler[1], "", false, 0),
			new ObisMapItem(s_kesintiler[2], "", false, 0), new ObisMapItem(s_kesintiler[3], "", false, 0),
			new ObisMapItem(s_kesintiler[4], "", false, 0), new ObisMapItem(s_kesintiler[5], "", false, 0),
			new ObisMapItem(s_kesintiler[6], "", false, 0), new ObisMapItem(s_kesintiler[7], "", false, 0),
			new ObisMapItem(s_kesintiler[8], "", false, 0), new ObisMapItem(s_kesintiler[9], "", false, 0)

			// -------------------------------------------------------------------------
			 
	};
	
	public static final ObisMapItem[] s_sayac_bilgi = new ObisMapItem[] {
			
			new ObisMapItem(s_sayac_no, "Seri numarası", true, 1),
			new ObisMapItem(s_uretim_tarih, "Üretim tarihi", false, 1),
			new ObisMapItem(s_kalibrasyon_tarih, "Kalibrasyon tarihi", false, 1),
			
			new ObisMapItem(s_top_end, "Kümülatif aktif enerji", true, 1),
			new ObisMapItem(s_gunduz_end, "Toplam enerji T1", true, 1),
			new ObisMapItem(s_puant_end, "Toplam enerji T2", true, 1),
			new ObisMapItem(s_gece_end, "Toplam enerji T3", true, 1),
			new ObisMapItem(s_demand_end, "Maximum Aktif Güç", false, 1),
			new ObisMapItem(s_enduktif_end, "Reaktif endüktif", false, 1),
			new ObisMapItem(s_kapasitif_end, "Reaktif kapasitif", false, 1),
			new ObisMapItem(s_toplam_notr_enerji, "Toplam Nötr Enerji", false, 1),
			new ObisMapItem(s_toplam_notr_faz_enerji_farki, "Toplam Nötr Faz Enerji Farkı", false, 1),
			new ObisMapItem(s_cos_l1, "Cos L1", false, 1),
			new ObisMapItem(s_cos_l2, "Cos L2", false, 1),
			new ObisMapItem(s_cos_l3, "Cos L3", false, 1),

			new ObisMapItem(s_birinci_faz_kesilme_sayisi, "Birinci faz kesilme sayısı", false, 0),
			new ObisMapItem(s_ikinci_faz_kesilme_sayisi, "İkinci faz kesilme sayısı", false, 0),
			new ObisMapItem(s_ucuncu_faz_kesilme_sayisi, "Üçüncü faz kesilme sayısı", false, 0)

	};


}
