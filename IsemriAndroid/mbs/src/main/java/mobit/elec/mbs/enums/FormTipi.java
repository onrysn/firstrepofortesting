package mobit.elec.mbs.enums;

import mobit.elec.enums.IFormTipi;

public enum FormTipi implements IFormTipi {

	perakende_form(1),
	dagitim_form(2),
	okuma_bildirim_form(3),
	ytu_form(4),
	tah_kes_form(5),
	ihbar_form(6),
	acma_form(7),
	kesme_form(8),
	sayac_degistirme_form(9),
	kesme_ihbar_form(10),
	aysonu_form(11),
	olcu_devre_akim_var_form(12),
	olcu_devre_akim_yok_form(13),
	perakende_kdm_form(14),
	aysonu_kdm_form(15);


	private final int value;

	FormTipi(final int value)
	{
		this.value = value;
	}
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch(this){
			case perakende_form: return "Perakende Fatura";
			case dagitim_form: return "Dağıtım Fatura";
			case okuma_bildirim_form: return "Okuma Bildirimi";
			case ytu_form: return "YTU ";
			case tah_kes_form: return "Tahliyeden Kesme Bildirimi";
			case ihbar_form: return "Borç Bildirimi";
			case acma_form: return "Açma Bildirimi";
			case kesme_form: return "Kesme Bildirimi";
			case sayac_degistirme_form: return "Sayaç Değiştirme Bildirimi";
			case kesme_ihbar_form: return "Kesme İhbar Formu";//HÜSEYİN EMRE ÇEVİK
			case aysonu_form: return "Ay Sonu Form";
			case aysonu_kdm_form: return "Ay Sonu Kademe Form";
			case olcu_devre_akim_var_form: return "Ölçü devre akım trafosu var Form";
			case olcu_devre_akim_yok_form: return "Ölçü devre akım trafosu yok Form";
			case perakende_kdm_form: return "Perakende Kademe Fatura";
		}
		return null;
	}

	public static FormTipi fromInteger(int x) {
		switch(x){
			case 1: return perakende_form;
			case 2: return dagitim_form;
			case 3: return okuma_bildirim_form;
			case 4: return ytu_form;
			case 5: return tah_kes_form;
			case 6: return ihbar_form;
			case 7: return acma_form;
			case 8: return kesme_form;
			case 9: return sayac_degistirme_form;
			case 10: return kesme_ihbar_form;
			case 11: return aysonu_form;
			case 12: return olcu_devre_akim_var_form;
			case 13: return olcu_devre_akim_yok_form;
			case 14: return perakende_kdm_form;
			case 15: return aysonu_kdm_form;
		}
		return null;
	}

}
