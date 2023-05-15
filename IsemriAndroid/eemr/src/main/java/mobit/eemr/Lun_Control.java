package mobit.eemr;

public class Lun_Control {
    public static int Luna_Control=0;
    public static int IsEmriNo;
    public static int TesisatNo;
    public static int DurumControl=0;
    //Okunan Obis codelarını tutmak için
    public static String ObisCode="";
    //26 mart zabit için eklendi
    public static int ZabitDur=0;
    public static int ZabitMod=0;
    public static int KacakVarmi=0;
    public static int ZabitTesisat=0;
    public static int ZabitIsemriNo=0;
    //GPS İyileştirme için eklendi
    public static double Latitude=0.0;
    public static double Longitude=0.0;
    public static double Accuracy=0.0;
    public static String Time="";
    //Endeksleri tutacak
    public static String[] Endeksler=new String[8];


    //Luna
    public static void  setLuna_Control(int kontrol){
        Luna_Control=kontrol;
    }
    public int getLuna_Control(){
        return Luna_Control;
    }

    public static int getIsEmriNo() {
        return IsEmriNo;
    }

    public static void setIsEmriNo(int isEmriNo) {
        IsEmriNo = isEmriNo;
    }

    public static int getTesisatNo(){
        return TesisatNo;
    }
    public  static void setTesisatNo(int tesisatNo){
        TesisatNo=tesisatNo;
    }

    public static   int getDurumControl(){return DurumControl;}
    public static  void setDurumControl(int durumControl){DurumControl=durumControl;}

    //ZABİT
    public static  void setZabitDur(int zabitDur){ZabitDur=zabitDur;}
    public static  void setZabitMod(int zabitMod){ZabitMod=zabitMod;}
    public static void setKacakVarmi(int kacakVarmi){KacakVarmi=kacakVarmi;}
    public static  void setZabitTesisat(int zabitTesisat){ZabitTesisat=zabitTesisat;}
    public static void setZabitIsemriNo(int zabitIsemriNo){ZabitIsemriNo=zabitIsemriNo;}

    //obiscode

    public static void setObisCode(String obisCode){ObisCode=obisCode;}

    //GPS

    public static void setLatitude(double latitude){Latitude=latitude;}
    public static void setLongitude(double longitude){Longitude=longitude;}
    public static void setAccuracy(double accuracy){Accuracy=accuracy;}
    public static void setTime(String time){Time=time;}

    //Endeksler
    public static void setEndeksler(String[] endeksler){Endeksler=endeksler;}
}
