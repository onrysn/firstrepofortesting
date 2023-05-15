package mobit.elec;

import com.mobit.IChecked;
import mobit.elec.enums.IIslemTipi;

public class AltEmirTuru implements IChecked {
	public IIslemTipi islemTipi;
	public int altEmirTuru;
	public String Tanim;
	
	private boolean checked;
	
	
	public AltEmirTuru(IIslemTipi islemTipi, int altEmirTuru, String Tanim)
	{
		this.islemTipi = islemTipi;
		this.altEmirTuru = altEmirTuru;
		this.Tanim = Tanim;
	}
	
	public String toString()
	{
		return Tanim != null ? Tanim : "";
	}

	@Override
	public boolean getCheck() {
		// TODO Auto-generated method stub
		return checked;
	}

	@Override
	public void setCheck(boolean checked) {
		// TODO Auto-generated method stub
		this.checked = checked;
	}
};
