package mobit.elec.enums;

import mobit.elec.Globals;

public class IslemDurum implements IIslemDurum  {
	
	protected static final int iTumu = -1;
	protected static final int iSerbest = 0; 
	protected static final int iAtanmis = 1; 
	protected static final int iIptal = 2; 
	protected static final int iTamamlandi = 3;
	protected static final int iYapilamadi = 4;
	protected static final int iYapilmadi = 5;
	protected static final int iKuyrukta = 6;
	
	public static final IslemDurum Tumu = new IslemDurum(iTumu);
	public static final IslemDurum Serbest = new IslemDurum(iSerbest); 
	public static final IslemDurum Atanmis = new IslemDurum(iAtanmis); 
	public static final IslemDurum Iptal = new IslemDurum(iIptal); 
	public static final IslemDurum Tamamlandi = new IslemDurum(iTamamlandi);
	public static final IslemDurum Yapilamadi = new IslemDurum(iYapilamadi);
	public static final IslemDurum Yapilmadi = new IslemDurum(iYapilmadi);
	public static final IslemDurum Kuyrukta = new IslemDurum(iKuyrukta);

	
	private final int value;
	
	private IslemDurum(final int value)
	{
		this.value = value;
	}
	public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	switch(getValue()){
    	case iTumu: return "Tümü";
    	case iSerbest: return "Serbest"; 
    	case iAtanmis: return "Atanmış";
    	case iIptal: return "İptal";
    	case iTamamlandi: return "Tamamlandı";
    	case iYapilamadi: return "Yapılamadı";
    	case iYapilmadi: return "Yapılmadı";
    	case iKuyrukta: return "Kuyrukta";
    	
    	}
        return "";
    }

	@Override
	public boolean YapilabilirMi()
	{
		return (this == Atanmis || this == Serbest);
	}

	@Override
	public int getColor()
	{
		if(this == Tumu) return 0xFFFFFFFF;
		if(this == Serbest) return 0xFF6400FF;
		if(this == Atanmis) return 0xFF00FF00;
		if(this == Iptal) return 0xFFFFFF00;
		if(this == Tamamlandi) return 0xFFFF0000;
		if(this == Yapilamadi) return 0xFFFF00c8;
		if(this == Yapilmadi) return 0xFFFF0064;
		if(this == Kuyrukta) return 0xFFFF00FF;
		return 0xFFFFFFFF;
	}
    
    public static IslemDurum fromInteger(int x) {
        switch(x) {
        case iTumu: return Tumu;
        case iSerbest: return Serbest; 
        case iAtanmis: return Atanmis; 
        case iIptal: return Iptal; 
        case iTamamlandi: return Tamamlandi;
        case iYapilamadi: return Yapilamadi;
        case iYapilmadi: return Yapilmadi;
        case iKuyrukta: return Kuyrukta;
        }
        return null;
    }

    private static final IslemDurum [] values = new IslemDurum[] {
    		Tumu,
    		Serbest, 
    		Atanmis, 
    		Iptal, 
    		Tamamlandi,
    		Yapilamadi,
    		Yapilmadi,
    		Kuyrukta
    };
	
	public static IslemDurum [] values()
	{
		return values;
	}

}
