package mobit.elec.mbs;

import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.borc_durum;
import mobit.elec.mbs.get.eleman;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.get.isemri_sorular;
import mobit.elec.mbs.get.karne;
import mobit.elec.mbs.get.koordinat;
import mobit.elec.mbs.get.muhur;
import mobit.elec.mbs.get.muhur_kod;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.get.sdurum;
import mobit.elec.mbs.get.tesisat;
import mobit.elec.mbs.put.put_atarif;
import mobit.elec.mbs.put.put_endeks;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_isemri_zabit;

public class TableList {
	
	public static Class<?> [] tables = new Class<?>[]{
		
		adurum.class,
		eleman.class,
		isemri_guncelle.class,
		muhur_kod.class,
		sayac_marka.class,
		sdurum.class,
		tesisat.class,
		put_endeks.class,
		put_isemri.class,
		takilan_sayac.class,
		put_isemri_zabit.class,
		
		borc_durum.class,
		isemri_soru.class,
		isemri_sorular.class,
		karne.class,
		koordinat.class,
		
		muhur.class,
		put_atarif.class
		
	};

}
