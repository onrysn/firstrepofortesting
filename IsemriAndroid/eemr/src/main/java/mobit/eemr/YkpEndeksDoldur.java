package mobit.eemr;

public class YkpEndeksDoldur {
    public static String kadi = "dusoft";
    public static String sifre = "Sayac!Sor123";
    public static int tesisat_no;
    public static int isemri_no;
    public static String endeks_tarihi=null;
    public static String endeks_saati=null;
    public static String Aktif=null;
    public static String Enduktif=null;
    public static String Kapasitif=null;
    public static String AkimL1=null;
    public static String AkimL2=null;
    public static String AkimL3=null;
    public static String GerilimL1=null;
    public static String GerilimL2=null;
    public static String GerilimL3=null;

    public  void setTesisat_no(int tesisat_no) {
        YkpEndeksDoldur.tesisat_no = tesisat_no;
    }
    public void setIsemri_no(int isemri_no){
        YkpEndeksDoldur.isemri_no=isemri_no;
    }
    public void setEndeks_tarihi( String endeks_tarihi){
        YkpEndeksDoldur.endeks_tarihi=endeks_tarihi;
    }
    public void setEndeks_saati(  String endeks_saati){
        YkpEndeksDoldur.endeks_saati=endeks_saati;
    }
    public void setAktif(String Aktif){
        YkpEndeksDoldur.Aktif=Aktif;
    }
    public void setEnduktif(String Enduktif){
        YkpEndeksDoldur.Enduktif=Enduktif;
    }
    public void setKapasitif(String Kapasitif){
        YkpEndeksDoldur.Kapasitif=Kapasitif;
    }
    public void setAkimL1(String AkimL1){
        YkpEndeksDoldur.AkimL1=AkimL1;
    }
    public void setAkimL2(String AkimL2){
        YkpEndeksDoldur.AkimL2=AkimL2;
    }
    public void setAkimL3(String AkimL3){
        YkpEndeksDoldur.AkimL3=AkimL3;
    }
    public void setGerilimL1(String GerilimL1){
        YkpEndeksDoldur.GerilimL1=GerilimL1;
    }
    public void setGerilimL2(String GerilimL2){
        YkpEndeksDoldur.GerilimL2=GerilimL2;
    }
    public void setGerilimL3(String GerilimL3){
        YkpEndeksDoldur.GerilimL3=GerilimL3;
    }

    public  String getKadi() {
        return kadi;
    }

    public  String getSifre() {
        return sifre;
    }

    public static int getIsemri_no() {
        return isemri_no;
    }

    public static int getTesisat_no() {
        return tesisat_no;
    }

    public    String getEndeks_saati() {
        return endeks_saati;
    }

    public    String getEndeks_tarihi() {
        return endeks_tarihi;
    }

    public  String getAktif() {
        return Aktif;
    }

    public  String getEnduktif() {
        return Enduktif;
    }

    public  String getKapasitif() {
        return Kapasitif;
    }

    public  String getAkimL1() { return AkimL1; }

    public  String getAkimL2() {
        return AkimL2;
    }

    public  String getAkimL3() {
        return AkimL3;
    }

    public  String getGerilimL1() {
        return GerilimL1;
    }

    public  String getGerilimL2() {
        return GerilimL2;
    }

    public  String getGerilimL3() {
        return GerilimL3;
    }



}
