package com.mobit;

public enum RecordStatus implements IRecordStatus {
	
	None(0),
	Saved(1),
	Sending(2),
	Sent(3),
	Recieved(4),
	Printed(5);


	private final int value;
	
	RecordStatus(int value)
	{
		this.value = value;
	}
	@Override
	public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	
    	switch(getValue()){
    	case 0:return "Tanımsız";
        case 1:return "Kaydedildi";
        case 2:return "Gönderiliyor...";
        case 3:return "Gönderildi";
        case 4:return "Alındı";
        case 5:return "Yazdırıldı";
        
        }
    	return "";
    }

    public int getStatusColor()
    {
        switch(this){
            case None: return 0xFFFFC8C8;
            case Saved: return 0xFF00FF00;
            case Sending: return 0xFF6400FF;
            case Sent: return 0xffffff00;
            case Recieved: return 0xFFFFFF00;
            case Printed: return 0xFFFF00FF;
        }
        return 0xFFFFFFFF;
    }

    // H.Elif renklerde düzenlemeler yapıldı.
    public static final int BilgiRenk = 0xbfFFC8C8;// new Color(255, 200, 200, 0.75);
    public static final int KesmeRenk = 0xbffc4d4d;// new Color(0, 255, 0, 0.75);
    public static final int AcmaRenk = 0xbf00FF00;// new Color(255, 255, 255, 0.75);
    public static final int IhbarRenk = 0xbf6400FF;// new Color(100, 0, 255, 0.75);
    public static final int KontrolRenk = 0xbfFFFF00;// new Color(255, 255, 0, 0.75);
    public static final int SayacDegistirRenk = 0xbfffff2b;// new Color(255, 0, 255, 0.75);
    public static final int SayacTakRenk = 0xbfFF5e00;// new Color(255, 0, 255, 0.75);
    public static final int TespitRenk = 0xbf00FFFF;// new Color(0, 255, 255, 0.75);
    public static final int SayacOkumaRenk = 0xbf80A080;

    
    public static RecordStatus fromInteger(int x) {
        switch(x){
        case 0:return None;
        case 1:return Saved;
        case 2:return Sending;
        case 3:return Sent;
        case 4:return Recieved;
        case 5:return Printed;
        
        }
        return null;
    }
}
