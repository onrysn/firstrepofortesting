package mobit.elec.enums;

import com.mobit.IChecked;

import mobit.elec.IslemTipi2;

/**
 * Created by Genel on 11.10.2018.
 */

public class IslemDurum2 implements IIslemDurum, IChecked {

    private IIslemDurum islemDurum;
    private boolean checked;

    public IslemDurum2(IIslemDurum islemDurum)
    {
        this.islemDurum = islemDurum;
    }
    @Override
    public boolean YapilabilirMi()
    {
        return (islemDurum == IslemDurum.Atanmis || islemDurum == IslemDurum.Serbest);
    }
    @Override
    public int getValue() {
        // TODO Auto-generated method stub
        return islemDurum.getValue();
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
    public int getColor() {
        // TODO Auto-generated method stub
        return islemDurum.getColor();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return islemDurum.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof IIslemDurum)) return false;
        else if(obj instanceof IslemDurum2) return islemDurum.equals((IslemDurum2)obj);
        return islemDurum.equals(obj);
    }

    public static IslemDurum2 [] values()
    {
        IslemDurum [] v = IslemDurum.values();
        IslemDurum2 [] values = new IslemDurum2[v.length];
        for(int i = 0; i < values.length; i++) values[i] = new IslemDurum2(v[i]);
        return values;
    }

}
