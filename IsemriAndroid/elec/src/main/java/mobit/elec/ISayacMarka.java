package mobit.elec;

import com.mobit.IEnum;

public interface ISayacMarka {

	public String getSAYAC_MARKA_KODU();

	public String getSAYAC_MARKA_ADI();
	
	public IEnum getSAYAC_CINS_KODU();

	public void setSAYAC_MARKA_KODU(String sAYAC_MARKA_KODU);

	public void setSAYAC_MARKA_ADI(String sAYAC_MARKA_ADI);

	public void setSAYAC_CINS_KODU(IEnum sAYAC_CINS_KODU);

}
