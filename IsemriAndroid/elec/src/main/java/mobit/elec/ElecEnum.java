package mobit.elec;

import com.mobit.IEnum;

public enum ElecEnum implements IEnum {
	
	CevapTipi(1),
	FazSayisi(2),
	IsemriDurum(3),
	IslemTipi(4),
	MuhurKodCins(5),
	Mulkiyet(6),
	OkumaMetodu(7),
	SayacCinsi(8),
	SayacHaneSayisi(9),
	SayacIndex(10),
	SayacKodu(11),
	Seviye(12),
	Voltaj(13);
	
	
	
	private final int value;
	
	ElecEnum(final int value)
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
        case 1:return "Cevap Tipi";
        case 2:return "Faz Sayisi";
        case 3:return "Isemri Durum";
        case 4:return "Islem Tipi";
        case 5:return "Muhur Kod Cinsi";
        case 6:return "Mulkiyet";
        case 7:return "Okuma Metodu";
        case 8:return "Sayac Cinsi";
        case 9:return "Sayac Hane Sayisi";
        case 10:return "Sayac Endeks";
        case 11:return "Sayac Kodu";
        case 12:return "Seviye";
        case 13:return "Voltaj";
        }
    	return "";
    }
    
    public static IEnum fromInit(int x) {
        switch(x){
        case 1:return CevapTipi;
        case 2:return FazSayisi;
        case 3:return IsemriDurum;
        case 4:return IslemTipi;
        case 5:return MuhurKodCins;
        case 6:return Mulkiyet;
        case 7:return OkumaMetodu;
        case 8:return SayacCinsi;
        case 9:return SayacHaneSayisi;
        case 10:return SayacIndex;
        case 11:return SayacKodu;
        case 12:return Seviye;
        case 13:return Voltaj;
        }
        return null;
    }

}
