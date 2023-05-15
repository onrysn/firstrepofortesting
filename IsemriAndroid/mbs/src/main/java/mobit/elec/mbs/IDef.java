package mobit.elec.mbs;

import mobit.elec.AltEmirTuru;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.eleman;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.get.muhur_kod;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.get.sdurum;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.put.put_atarif;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_isemri_zabit;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.put.put_tesisat_muhur;
import mobit.elec.mbs.put.put_isemri_unvan;

public interface IDef extends mobit.elec.IDef {

	public static final int MBS_SERVER = 0;
	
	public static final String perakende_form = "perakende_form";
	public static final String dagitim_form = "dagitim_form";
	public static final String okuma_bildirim_form = "okuma_bildirim_form";
	public static final String ytu_form = "ytu_form";
	public static final String tah_kes_form = "tah_kes_form";
	public static final String ihbar_form = "ihbar_form";
	public static final String acma_form = "acma_form";
	public static final String kesme_ihbar_form = "kesme_ihbar_form";//HÜSEYİN EMRE ÇEVİK
	public static final String aysonu_form= "aysonu_form";//HÜSEYİN EMRE ÇEVİK
	public static final String aysonu_kdm_form= "aysonu_kdm_form";//Onur
	public static final String olcu_devre_akim_var_form = "ölçü devre akım trafosu var";//H.Elif
	public static final String olcu_devre_akim_yok_form = "ölçü devre akım trafosu yok";//H.Elif
	public static final String perakende_kdm_form = "perakende_kdm_form";//H.Elif


	public static final String kesme_form = "kesme_form";
	public static final String sayac_degistirme_form = "sayac_degistirme_form";
	public static final String sayac_takma_form = "sayac_takma_form";

	public static final String form_tipi = "form_tipi";
	public static final String fatura = "ft";
	public static final String aysonu = "ay";
	public static final String acma = "bl";
	public static final String kesme = "ks";
	public static final String sayac_degistirme = "sd";
	public static final String ihbar = "bl";

	public static final String FORM_BEGIN = "FORM_BEGIN";
	public static final String FORM_END = "FORM_END";

	public static final String OK = "OK";
	public static final String FTL = "FTL";
	public static final String ERR = "ERR";
	public static final String WRN = "WRN";
	public static final String PRN = "PRN";


	public static Class<?>[] tables = new Class<?>[] {

			IslemMaster2.class, adurum.class, eleman.class, isemri_guncelle.class, muhur_kod.class, sayac_marka.class,
			sdurum.class, put_isemri.class, takilan_sayac.class, isemri_soru.class, put_isemri_zabit.class,
			put_atarif.class, put_muhur_sokme.class, put_tesisat_muhur.class,put_isemri_unvan.class//HÜSEYİN EMRE ÇEVİK unvan

			/*
			 * put_isemri_zabit.class, borc_durum.class, isemri_soru.class,
			 * isemri_sorular.class, karne.class, koordinat.class, muhur.class,
			 * put_atarif.class
			 */

	};

	public static Class<?>[] clearTables = new Class<?>[] {

			IslemMaster2.class, eleman.class, isemri_guncelle.class, put_isemri.class, takilan_sayac.class,
			isemri_soru.class, put_isemri_zabit.class, put_atarif.class,put_isemri_unvan.class//HÜSEYİN EMRE ÇEVİK unvan

	};
	
	public static final AltEmirTuru[] bosAltEmirTuruList = new AltEmirTuru[] {
			new AltEmirTuru(IslemTipi.Tumu, -1, "Tümü") };

	
	
}
