package mobit.eemr;

public class YaziciHataBildirimi {
    public static int tesisat_no=0;
    public static int isemri_no=0;
    public static int gonderme_dur=1;
    public static int yazdirma_dur=0;
    public static String hata_aciklama="";

    public int getTesisat_no() {
        return tesisat_no;
    }
    public int getIsemri_no(){
        return isemri_no;
    }
    public int getGonderme_dur(){
        return gonderme_dur;
    }
    public int getYazdirma_dur(){
        return yazdirma_dur;
    }
    public String getHata_aciklama(){
        return hata_aciklama;
    }

    public void setTesisat_no(int Tesisat_no){
        tesisat_no=Tesisat_no;
    }
    public void setIsemri_no(int Isemri_no){
        isemri_no=Isemri_no;
    }
    public void setGonderme_dur(int Gonderme_dur){
        gonderme_dur=Gonderme_dur;
    }
    public void setYazdirma_dur(int Yazdirma_dur){
        yazdirma_dur=Yazdirma_dur;
    }
    public void setHata_aciklama(String Hata_aciklama){
        hata_aciklama=Hata_aciklama;
    }
}
