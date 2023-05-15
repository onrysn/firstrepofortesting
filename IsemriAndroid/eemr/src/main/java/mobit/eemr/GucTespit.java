package mobit.eemr;

public class GucTespit {
    public String cinsi = "";
    public int adet = -1;
    public String guc = "";

    public void set_cinsi(String Cinsi) {
        cinsi = Cinsi;
    }
    public void set_adet(int Adet) {
        adet = Adet;
    }
    public void set_guc(String Guc) {
        guc = Guc;
    }

    public String get_cinsi() {
        return cinsi;
    }
    public int get_adet() {
        return adet;
    }
    public String get_guc() {
        return guc;
    }
}
