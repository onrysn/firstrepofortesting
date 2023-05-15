package mobit.elec;

import com.mobit.IChecked;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;

public class IslemTipi2 implements IIslemTipi, IChecked {

	private IIslemTipi islemTipi;
	private boolean checked;
	
	public IslemTipi2(IIslemTipi islemTipi)
	{
		this.islemTipi = islemTipi;
	}
	
	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return islemTipi.getValue();
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

	@Override
	public int getIslemRenk() {
		// TODO Auto-generated method stub
		return islemTipi.getIslemRenk();
	}
	
	@Override
    public String toString() {
        // TODO Auto-generated method stub
		return islemTipi.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof IIslemTipi)) return false;
		else if(obj instanceof IslemTipi2) return islemTipi.equals((IslemTipi2)obj);
		return islemTipi.equals(obj);
	}
	
	public static IslemTipi2 [] values()
	{
		IslemTipi [] v = IslemTipi.values();
		IslemTipi2 [] values = new IslemTipi2[v.length];
		for(int i = 0; i < values.length; i++) values[i] = new IslemTipi2(v[i]);
		return values;
	}
}
