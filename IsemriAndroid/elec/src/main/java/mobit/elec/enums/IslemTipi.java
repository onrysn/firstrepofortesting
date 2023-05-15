package mobit.elec.enums;

import com.mobit.Enum;

public class IslemTipi extends Enum implements IIslemTipi  {
	
	
	protected static final int iTumu = -1;
	protected static final int iBilgi = 0;
	protected static final int iKesme = 1; 
	protected static final int iAcma = 2;
	protected static final int iIhbar = 3; 
	protected static final int iKontrol = 4; 
	protected static final int iSayacDegistir = 5; 
	protected static final int iSayacTakma = 6;
	protected static final int iTespit = 7;
	protected static final int iSayacOkuma = 100;
	
	public static final IslemTipi Tumu = new IslemTipi(iTumu, iTumu);
	public static final IslemTipi Bilgi = new IslemTipi(iBilgi, iBilgi);
	public static final IslemTipi Kesme = new IslemTipi(iKesme, iKesme); 
	public static final IslemTipi Acma = new IslemTipi(iAcma, iAcma);
	public static final IslemTipi Ihbar = new IslemTipi(iIhbar, iIhbar); 
	public static final IslemTipi Kontrol = new IslemTipi(iKontrol, iKontrol); 
	public static final IslemTipi SayacDegistir = new IslemTipi(iSayacDegistir, iSayacDegistir); 
	public static final IslemTipi SayacTakma = new IslemTipi(iSayacTakma, iSayacTakma);
	public static final IslemTipi Tespit = new IslemTipi(iTespit, iTespit);
	public static final IslemTipi SayacOkuma = new IslemTipi(iSayacOkuma, iSayacOkuma);
	
	
    IslemTipi(final Object value, final Object baseValue) {
        super(value, baseValue);
    }

    public int getValue() {
        return (Integer)super.getDerivedValue();
    }
    
    
	
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        switch(getValue()){
        case iTumu: return "Tümü";
        case iBilgi: return "Bilgi";
        case iKesme: return "Kesme"; 
        case iAcma: return "Açma";
        case iIhbar: return "İhbar";
        case iKontrol: return "Kontrol"; 
        case iSayacDegistir: return  "Sayaç Değişimi";
        case iSayacTakma: return "Sayaç Takma";
        case iTespit: return "Tespit";
        case iSayacOkuma: return "Sayaç Okuma";
        }
    	return "";
    	
    }

    public static IslemTipi fromInteger(int x) {
        switch(x) {
        case iTumu: return Tumu;
        case iBilgi: return Bilgi;
        case iKesme: return Kesme; 
        case iAcma: return Acma;
        case iIhbar: return Ihbar; 
        case iKontrol: return Kontrol; 
        case iSayacDegistir: return SayacDegistir; 
        case iSayacTakma: return SayacTakma; 
        case iTespit: return Tespit;
        case iSayacOkuma: return SayacOkuma;
        }
        return null;
    }

	// H.Elif renklerde düzenlemeler yapıldı.
	public static final int BeyazRenk = 0xFFFFFFFF;// new Color(255, 255, 255);
	public static final int BilgiRenk = 0xbfFFC8C8;// new Color(255, 200, 200, 0.75);
	public static final int KesmeRenk = 0xbffc4d4d;// new Color(0, 255, 0, 0.75);
	public static final int AcmaRenk = 0xbf00FF00;// new Color(255, 255, 255, 0.75);
	public static final int IhbarRenk = 0xbf6400FF;// new Color(100, 0, 255, 0.75);
	public static final int KontrolRenk = 0xbfFFFF00;// new Color(255, 255, 0, 0.75);
	public static final int SayacDegistirRenk = 0xbfffff2b;// new Color(255, 0, 255, 0.75);
	public static final int SayacTakRenk = 0xbfFF5e00;// new Color(255, 0, 255, 0.75);
	public static final int TespitRenk = 0xbf00FFFF;// new Color(0, 255, 255, 0.75);
	public static final int SayacOkumaRenk = 0xbf80A080;

	/*
	//Muhammed Gökkaya
    private static final int BeyazRenk = 0xFFFFFFFF;// new Color(255, 255, 255);
    private static final int BilgiRenk = 0xFFFFC8C8;// new Color(255, 200, 200);
    private static final int KesmeRenk = 0xFF00FF00;// new Color(0, 255, 0);
    private static final int AcmaRenk = 0xFFFF0000;// new Color(255, 255, 255);
    private static final int IhbarRenk = 0xFF6400FF;// new Color(100, 0, 255);
    private static final int KontrolRenk = 0xFFFFFF00;// new Color(255, 255, 0);
    private static final int SayacDegistirRenk = 0xFFFF00FF;// new Color(255, 0, 255);
    private static final int SayacTakRenk = 0xFF8040FF;// new Color(255, 0, 255);
    private static final int TespitRenk = 0xFF00FFFF;// new Color(0, 255, 255);
    private static final int SayacOkumaRenk = 0xFF80A080;
	*/
	@Override
	public int getIslemRenk()
	{
		
		if(equals(Bilgi)) return BilgiRenk;
		else if(equals(Kesme)) return KesmeRenk;
		else if(equals(Acma)) return AcmaRenk;
		else if(equals(Ihbar)) return IhbarRenk; 
		else if(equals(Kontrol)) return KontrolRenk; 
		else if(equals(SayacDegistir)) return SayacDegistirRenk; 
		else if(equals(SayacTakma)) return SayacTakRenk; 
		else if(equals(Tespit)) return TespitRenk;
		else if(equals(SayacOkuma)) return SayacOkumaRenk;
		return BeyazRenk;
	}
	
	private static final IslemTipi [] values = new IslemTipi[] {
			Tumu, 
			Bilgi, 
			Kesme, 
			Acma,
			Ihbar, 
			Kontrol, 
			SayacDegistir, 
			SayacTakma, 
			Tespit,
			SayacOkuma};
	
	public static IslemTipi [] values()
	{
		return values;
	}
}
