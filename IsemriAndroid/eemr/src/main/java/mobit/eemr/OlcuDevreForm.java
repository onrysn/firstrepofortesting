package mobit.eemr;

import java.util.ArrayList;
import java.util.List;

public class OlcuDevreForm {
    public static String kadi = "dusoft";
    public static String sifre = "Sayac!Sor123";
    public static int tesisat_no = -1;
    public static int isemri_no = -1;
    public static int alt_emir_turu = -1;
    public static int tesis_enerji_dur = -1;
    public static int pano_muhur_dur = -1;
    public static int sayac_muhur_dur = -1;
    public static int sayac_ariza_dur = -1;
    public static int akim_trafosu_dur = -1;
    public static int guc_trafosu_dur = -1;
    public static int guc_tespiti_dur = -1;
    public static String AT_1_Marka = "";
    public static String AT_1_Oran = "/";
    public static int AT_1_gucu = -1;
    public static String at_1_sinif = "";
    public static String at_1_seri = "";
    public static int at_1_imalyili = -1;
    public static String AT_2_Marka = "";
    public static String AT_2_Oran = "/";
    public static int AT_2_gucu = -1;
    public static String at_2_sinif = "";
    public static String at_2_seri = "";
    public static int at_2_imalyili = -1;
    public static String AT_3_Marka = "";
    public static String AT_3_Oran = "/";
    public static int AT_3_gucu = -1;
    public static String at_3_sinif = "";
    public static String at_3_seri = "";
    public static int at_3_imalyili = -1;
    public static String gt_1_Marka = "";
    public static String gt_1_Oran = "/";
    public static int gt_1_gucu = -1;
    public static String gt_1_sinif = "";
    public static String gt_1_seri = "";
    public static int gt_1_imalyili = -1;
    public static String gt_2_Marka = "";
    public static String gt_2_Oran = "/";
    public static int gt_2_gucu = -1;
    public static String gt_2_sinif = "";
    public static String gt_2_seri = "";
    public static int gt_2_imalyili = -1;
    public static String gt_3_Marka = "";
    public static String gt_3_Oran = "/";
    public static int gt_3_gucu = -1;
    public static String gt_3_sinif = "";
    public static String gt_3_seri = "";
    public static int gt_3_imalyili = -1;
    public static String primer_akim_1 = "";
    public static String sekonde_akim_1 = "";
    public static String sayac_akim_1 = "";
    public static String primer_akim_2 = "";
    public static String sekonde_akim_2 = "";
    public static String sayac_akim_2 = "";
    public static String primer_akim_3 = "";
    public static String sekonde_akim_3 = "";
    public static String sayac_akim_3 = "";
    public static String sayac_gerili_1 = "";
    public static String sayac_gerili_2 = "";
    public static String sayac_gerili_3 = "";
    public static String son_primer_akim_1 = "";
    public static String son_sekonde_akim_1 = "";
    public static String son_sayac_akim_1 = "";
    public static String son_primer_akim_2 = "";
    public static String son_sekonde_akim_2 = "";
    public static String son_sayac_akim_2 = "";
    public static String son_primer_akim_3 = "";
    public static String son_sekonde_akim_3 = "";
    public static String son_sayac_akim_3 = "";
    public static String son_sayac_gerili_1 = "";
    public static String son_sayac_gerili_2 = "";
    public static String son_sayac_gerili_3 = "";
    public static int gerilim_trafosu_dur = -1;
    public static double carpan = 0.0;
    public static int servisdur = -1;
    public static int teyit_dur = -1;

    public static String at_1_sinif_gucu = "";
    public static String at_2_sinif_gucu = "";
    public static String at_3_sinif_gucu = "";

    public static String gt_1_sinif_gucu = "";
    public static String gt_2_sinif_gucu = "";
    public static String gt_3_sinif_gucu = "";

    //H.Elif
    public static String polarite_1 = "";
    public static String polarite_2 = "";
    public static String polarite_3 = "";
    public static String sonpolarite_1 = "";
    public static String sonpolarite_2 = "";
    public static String sonpolarite_3 = "";

    // Abone Bilgileri H.Elif
    public static String unvan_txt = "";
    public static String adres_txt = "";
    public static String sayac_no_txt = "";
    public static String sayac_marka_spn = "";
    public static String hane_sayisi_txt = "";
    public static int imal_yili_txt = -1;

    public static String tcNo_txt = "";
    public static String telNo_txt = "";
    public static String eposta_txt = "";

    public static int faz_txt = -1;
    public static int amperaj = -1;
    public static int voltaj = -1;

    // Güç trafosu //H.Elif
    public static String trafoMarkasi_txt = "";
    public static int trafoSerino_txt = -1;
    public static int trafoGucu_txt = -1;
    public static int trafoGerilimi_txt = -1;
    public static int trafoImalYili_txt = -1;

    // Sökülen Mühür Bilgileri H.Elif
    public static List<String> sokulen_muhur_list = new ArrayList<String>();
    public static String aciklama = "";
    public static int ihbar_dur = -1;

    // Güç Tespit Bilgileri H.Elif
    public static List<GucTespit> guc_tespit_list = new ArrayList<GucTespit>();
    public static List<String> guc_tespit_cins = new ArrayList<String>();
    public static List<String> guc_tespit_adet = new ArrayList<String>();
    public static List<String> guc_tespit_guc = new ArrayList<String>();


    public static String tarifeKodu = "";
    public static String cbs1 = "";
    public static String cbs2 = "";
    public static String yeni_muhur = "";
    public static String t = "";
    public static String t1 = "";
    public static String t2 = "";
    public static String t3 = "";
    public static String kapasitif = "";
    public static String enduktif = "";
    public static String demand = "";
    public static int user_code = -1;

    public static String formType = "";
    public static String date = "";
    public static String time = "";

    // sayac değişimi takılan
    public static String takilan_marka = "";
    public static String takilan_no = "";
    public static String takilan_akim = "";
    public static String takilan_gerilim = "";
    public static String takilan_carpan = "";
    public static String takilan_haneadeti = "";
    public static String takilan_imalyili = "";
    public static String takilan_damgayili = "";
    public static String takilan_t = "";
    public static String takilan_t1 = "";
    public static String takilan_t2 = "";
    public static String takilan_t3 = "";
    public static String takilan_kapasitif = "";
    public static String takilan_enduktif = "";
    public static String takilan_demand = "";
    public static String demand_tarihi = "";

    public static int sayac_degisimi_dur = 0;
    public static int disaridan_olcu_dur = 0;





    /// Get Fonksiyonlar
    public int getteyit_dur() {
        return teyit_dur;
    }

    public int getservisdur() {
        return servisdur;
    }

    public double getcarpan() {
        return carpan;
    }

    public String getkadi() {
        return kadi;
    }

    public String getsifre() {
        return sifre;
    }

    public int gettesisat_no() {
        return tesisat_no;
    }

    public int getisemri_no() {
        return isemri_no;
    }

    public int get_alt_emir_turu() { return alt_emir_turu; }

    public int gettesis_enerji_dur() {
        return tesis_enerji_dur;
    }

    public int getpano_muhur_dur() {
        return pano_muhur_dur;
    }

    public int getsayac_muhur_dur() {
        return sayac_muhur_dur;
    }

    public int getsayac_ariza_dur() {
        return sayac_ariza_dur;
    }

    public int getakim_trafosu_dur() {
        return akim_trafosu_dur;
    }

    public int getguc_trafosu_dur() {
        return guc_trafosu_dur;
    }

    public int getguc_tespiti_dur() {
        return guc_tespiti_dur;
    }

    public String getAT_1_Marka() {
        return AT_1_Marka;
    }

    public String getAT_1_Oran() {
        return AT_1_Oran;
    }

    public int getAT_1_gucu() {
        return AT_1_gucu;
    }

    public String getat_1_sinif() {
        return at_1_sinif;
    }

    public String getat_1_seri() {
        return at_1_seri;
    }

    public int getat_1_imalyili() {
        return at_1_imalyili;
    }

    public String getAT_2_Marka() {
        return AT_2_Marka;
    }

    public String getAT_2_Oran() {
        return AT_2_Oran;
    }

    public int getAT_2_gucu() {
        return AT_2_gucu;
    }

    public String getat_2_sinif() {
        return at_2_sinif;
    }

    public String getat_2_seri() {
        return at_2_seri;
    }

    public int getat_2_imalyili() {
        return at_2_imalyili;
    }

    public String getAT_3_Marka() {
        return AT_3_Marka;
    }

    public String getAT_3_Oran() {
        return AT_3_Oran;
    }

    public int getAT_3_gucu() {
        return AT_3_gucu;
    }

    public String getat_3_sinif() {
        return at_3_sinif;
    }

    public String getat_3_seri() {
        return at_3_seri;
    }

    public int getat_3_imalyili() {
        return at_3_imalyili;
    }

    public String getgt_1_Marka() {
        return gt_1_Marka;
    }

    public String getgt_1_Oran() {
        return gt_1_Oran;
    }

    public int getgt_1_gucu() {
        return gt_1_gucu;
    }

    public String getgt_1_sinif() {
        return gt_1_sinif;
    }

    public String getgt_1_seri() {
        return gt_1_seri;
    }

    public int getgt_1_imalyili() {
        return gt_1_imalyili;
    }

    public String getgt_2_Marka() {
        return gt_2_Marka;
    }

    public String getgt_2_Oran() {
        return gt_2_Oran;
    }

    public int getgt_2_gucu() {
        return gt_2_gucu;
    }

    public String getgt_2_sinif() {
        return gt_2_sinif;
    }

    public String getgt_2_seri() {
        return gt_2_seri;
    }

    public int getgt_2_imalyili() {
        return gt_2_imalyili;
    }

    public String getgt_3_Marka() {
        return gt_3_Marka;
    }

    public String getgt_3_Oran() {
        return gt_3_Oran;
    }

    public int getgt_3_gucu() {
        return gt_3_gucu;
    }

    public String getgt_3_sinif() {
        return gt_3_sinif;
    }

    public String getgt_3_seri() {
        return gt_3_seri;
    }

    public int getgt_3_imalyili() {
        return gt_3_imalyili;
    }

    public String getprimer_akim_1() {
        return primer_akim_1;
    }

    public String getsekonde_akim_1() {
        return sekonde_akim_1;
    }

    public String getsayac_akim_1() {
        return sayac_akim_1;
    }

    public String getprimer_akim_2() {
        return primer_akim_2;
    }

    public String getsekonde_akim_2() {
        return sekonde_akim_2;
    }

    public String getsayac_akim_2() {
        return sayac_akim_2;
    }

    public String getprimer_akim_3() {
        return primer_akim_3;
    }

    public String getsekonde_akim_3() {
        return sekonde_akim_3;
    }

    public String getsayac_akim_3() {
        return sayac_akim_3;
    }

    public String getsayac_gerili_1() {
        return sayac_gerili_1;
    }

    public String getsayac_gerili_2() {
        return sayac_gerili_2;
    }

    public String getsayac_gerili_3() {
        return sayac_gerili_3;
    }

    public String getson_primer_akim_1() {
        return son_primer_akim_1;
    }

    public String getson_sekonde_akim_1() {
        return son_sekonde_akim_1;
    }

    public String getson_sayac_akim_1() {
        return son_sayac_akim_1;
    }

    public String getson_primer_akim_2() {
        return son_primer_akim_2;
    }

    public String getson_sekonde_akim_2() {
        return son_sekonde_akim_2;
    }

    public String getson_sayac_akim_2() {
        return son_sayac_akim_2;
    }

    public String getson_primer_akim_3() {
        return son_primer_akim_3;
    }

    public String getson_sekonde_akim_3() {
        return son_sekonde_akim_3;
    }

    public String getson_sayac_akim_3() {
        return son_sayac_akim_3;
    }

    public String getson_sayac_gerili_1() {
        return son_sayac_gerili_1;
    }

    public String getson_sayac_gerili_2() {
        return son_sayac_gerili_2;
    }

    public String getson_sayac_gerili_3() {
        return son_sayac_gerili_3;
    }

    public int getgerilim_trafosu_dur() {
        return gerilim_trafosu_dur;
    }

    //H.Elif
    public String get_polarite_1() {
        return polarite_1;
    }

    public String get_polarite_2() {
        return polarite_2;
    }

    public String get_polarite_3() {
        return polarite_3;
    }

    public String getson_polarite_1() {
        return sonpolarite_1;
    }

    public String getson_polarite_2() {
        return sonpolarite_2;
    }

    public String getson_polarite_3() {
        return sonpolarite_3;
    }

    // Get Fonk. Abone Bilgileri H.Elif
    public String get_unvan() {
        return unvan_txt;
    }

    public String get_adres() {
        return adres_txt;
    }

    public String get_sayac_no() {
        return sayac_no_txt;
    }

    public String get_hane_sayisi() {
        return hane_sayisi_txt;
    }

    public int get_imal_yili() {
        return imal_yili_txt;
    }

    public String get_tcNo() {
        return tcNo_txt;
    }

    public String get_telNo() {
        return telNo_txt;
    }

    public String get_eposta() {
        return eposta_txt;
    }

    public int get_faz() {
        return faz_txt;
    }

    public int get_amperaj() {
        return amperaj;
    }

    public int get_voltaj() {
        return voltaj;
    }

    //Güç trafosu H.Elif
    public String get_trafoMarkasi() {
        return trafoMarkasi_txt;
    }

    public int get_trafoSerino() {
        return trafoSerino_txt;
    }

    public int get_trafoGucu() {
        return trafoGucu_txt;
    }

    public int get_trafoGerilimi() {
        return trafoGerilimi_txt;
    }

    public int get_trafoImalYili() {
        return trafoImalYili_txt;
    }


    public String get_sayac_marka() {
        return sayac_marka_spn;
    }

    // Get Fonk. Sökülen Mühür Bilgileri H.Elif
    public List<String> get_sokulen_muhur_list() {
        return sokulen_muhur_list;
    }

    public String get_aciklama() {
        return aciklama;
    }

    public int get_ihbar_dur() {
        return ihbar_dur;
    }

    // Get Fonk. Güç Tespit Bilgileri H.Elif
    public List<GucTespit> get_guc_tespit_list() {
        return guc_tespit_list;
    }

    public List<String> get_guc_tespit_cins() {
        return guc_tespit_cins;
    }

    public List<String> get_guc_tespit_adet() {
        return guc_tespit_adet;
    }

    public List<String> get_guc_tespit_guc() {
        return guc_tespit_guc;
    }

    public String get_tarife_kodu() {
        return tarifeKodu;
    }

    public String get_cbs1() {
        return cbs1;
    }

    public String get_cbs2() {
        return cbs2;
    }

    public String get_yeni_muhur() {
        return yeni_muhur;
    }

    public String get_t() {
        return t;
    }

    public String get_t1() {
        return t1;
    }

    public String get_t2() {
        return t2;
    }

    public String get_t3() {
        return t3;
    }

    public String get_kapasitif() {
        return kapasitif;
    }

    public String get_enduktif() {
        return enduktif;
    }

    public String get_demand() {
        return demand;
    }

    public String get_demand_tarihi() {return demand_tarihi; }

    public int get_user_code() {
        return user_code;
    }

    public String get_date() {
        return date;
    }

    public String get_time() {
        return time;
    }

    public String get_formtype() {
        return formType;
    }

    public String get_takilan_marka() {
        return takilan_marka;
    }

    public String get_takilan_no() {
        return takilan_no;
    }

    public String get_takilan_akim() {
        return takilan_akim;
    }

    public String get_takilan_gerilim() {
        return takilan_gerilim;
    }

    public String get_takilan_carpan() {
        return takilan_carpan;
    }

    public String get_takilan_haneadeti() {
        return takilan_haneadeti;
    }

    public String get_takilan_imalyili() {
        return takilan_imalyili;
    }

    public String get_takilan_damgayili() {
        return takilan_damgayili;
    }
//onur sahada sayaç değişiminde takılan sayaç endeks değerleri gelmezse buradan kontrolünü yap
    public String get_takilan_t() {
        return takilan_t;
    }

    public String get_takilan_t1() {
        return takilan_t1;
    }

    public String get_takilan_t2() {
        return takilan_t2;
    }

    public String get_takilan_t3() {
        return takilan_t3;
    }

    public String get_takilan_kapasitif() {
        return takilan_kapasitif;
    }

    public String get_takilan_enduktif() {
        return takilan_enduktif;
    }

    public String get_takilan_demand() {
        return takilan_demand;
    }


    public String get_at_1_sinif_gucu() {
        return at_1_sinif_gucu;
    }

    public String get_at_2_sinif_gucu() {
        return at_2_sinif_gucu;
    }

    public String get_at_3_sinif_gucu() {
        return at_3_sinif_gucu;
    }

    public String get_gt_1_sinif_gucu() {
        return gt_1_sinif_gucu;
    }

    public String get_gt_2_sinif_gucu() {
        return gt_2_sinif_gucu;
    }

    public String get_gt_3_sinif_gucu() {
        return gt_3_sinif_gucu;
    }

    public int get_sayac_degisimi_dur() {return sayac_degisimi_dur; }

    public int get_disaridan_olcu_dur() {return disaridan_olcu_dur; }






    //set fonksiyonlarF
    public void setteyit_dur(int Teyit_dur) {
        teyit_dur = Teyit_dur;
    }

    public void setservisdur(int Servisdur) {
        servisdur = Servisdur;
    }

    public void setcarpan(double Carpan) {
        carpan = Carpan;
    }

    public void settesisat_no(int Tesisat_no) {
        tesisat_no = Tesisat_no;
    }

    public void setisemri_no(int Isnemri_no) {
        isemri_no = Isnemri_no;
    }

    public void set_alt_emir_turu(int Alt_emir_turu) { alt_emir_turu = Alt_emir_turu; }

    public void settesis_enerji_dur(int Tesis_enerji_dur) {
        tesis_enerji_dur = Tesis_enerji_dur;
    }

    public void setpano_muhur_dur(int Pano_muhur_dur) {
        pano_muhur_dur = Pano_muhur_dur;
    }

    public void setsayac_muhur_dur(int Sayac_muhur_dur) {
        sayac_muhur_dur = Sayac_muhur_dur;
    }

    public void setsayac_ariza_dur(int Sayac_ariza_dur) {
        sayac_ariza_dur = Sayac_ariza_dur;
    }

    public void setakim_trafosu_dur(int Akim_trafosu_dur) {
        akim_trafosu_dur = Akim_trafosu_dur;
    }

    public void setguc_trafosu_dur(int Guc_trafosu_dur) {
        guc_trafosu_dur = Guc_trafosu_dur;
    }

    public void setguc_tespiti_dur(int Guc_tespiti_dur) {
        guc_tespiti_dur = Guc_tespiti_dur;
    }

    public void setAT_1_Marka(String AAT_1_Marka) {
        AT_1_Marka = AAT_1_Marka;
    }

    public void setAT_1_Oran(String AAT_1_Oran) {
        AT_1_Oran = AAT_1_Oran;
    }

    public void setAT_1_gucu(int AAT_1_gucu) {
        AT_1_gucu = AAT_1_gucu;
    }

    public void setat_1_sinif(String At_1_sinif) {
        at_1_sinif = At_1_sinif;
    }

    public void setat_1_seri(String At_1_seri) {
        at_1_seri = At_1_seri;
    }

    public void setat_1_imalyili(int At_1_imalyili) {
        at_1_imalyili = At_1_imalyili;
    }

    public void setAT_2_Marka(String AAT_2_Marka) {
        AT_2_Marka = AAT_2_Marka;
    }

    public void setAT_2_Oran(String AAT_2_Oran) {
        AT_2_Oran = AAT_2_Oran;
    }

    public void setAT_2_gucu(int AAT_2_gucu) {
        AT_2_gucu = AAT_2_gucu;
    }

    public void setat_2_sinif(String At_2_sinif) {
        at_2_sinif = At_2_sinif;
    }

    public void setat_2_seri(String At_2_seri) {
        at_2_seri = At_2_seri;
    }

    public void setat_2_imalyili(int At_2_imalyili) {
        at_2_imalyili = At_2_imalyili;
    }

    public void setAT_3_Marka(String AAT_3_Marka) {
        AT_3_Marka = AAT_3_Marka;
    }

    public void setAT_3_Oran(String AAT_3_Oran) {
        AT_3_Oran = AAT_3_Oran;
    }

    public void setAT_3_gucu(int AAT_3_gucu) {
        AT_3_gucu = AAT_3_gucu;
    }

    public void setat_3_sinif(String At_3_sinif) {
        at_3_sinif = At_3_sinif;
    }

    public void setat_3_seri(String At_3_seri) {
        at_3_seri = At_3_seri;
    }

    public void setat_3_imalyili(int At_3_imalyili) {
        at_3_imalyili = At_3_imalyili;
    }

    public void setgt_1_Marka(String Gt_1_Marka) {
        gt_1_Marka = Gt_1_Marka;
    }

    public void setgt_1_Oran(String Gt_1_Oran) {
        gt_1_Oran = Gt_1_Oran;
    }

    public void setgt_1_gucu(int Gt_1_gucu) {
        gt_1_gucu = Gt_1_gucu;
    }

    public void setgt_1_sinif(String Gt_1_sinif) {
        gt_1_sinif = Gt_1_sinif;
    }

    public void setgt_1_seri(String Gt_1_seri) {
        gt_1_seri = Gt_1_seri;
    }

    public void setgt_1_imalyili(int Gt_1_imalyili) {
        gt_1_imalyili = Gt_1_imalyili;
    }

    public void setgt_2_Marka(String Gt_2_Marka) {
        gt_2_Marka = Gt_2_Marka;
    }

    public void setgt_2_Oran(String Gt_2_Oran) {
        gt_2_Oran = Gt_2_Oran;
    }

    public void setgt_2_gucu(int Gt_2_gucu) {
        gt_2_gucu = Gt_2_gucu;
    }

    public void setgt_2_sinif(String Gt_2_sinif) {
        gt_2_sinif = Gt_2_sinif;
    }

    public void setgt_2_seri(String Gt_2_seri) {
        gt_2_seri = Gt_2_seri;
    }

    public void setgt_2_imalyili(int Gt_2_imalyili) {
        gt_2_imalyili = Gt_2_imalyili;
    }

    public void setgt_3_Marka(String Gt_3_Marka) {
        gt_3_Marka = Gt_3_Marka;
    }

    public void setgt_3_Oran(String Gt_3_Oran) {
        gt_3_Oran = Gt_3_Oran;
    }

    public void setgt_3_gucu(int Gt_3_gucu) {
        gt_3_gucu = Gt_3_gucu;
    }

    public void setgt_3_sinif(String Gt_3_sinif) {
        gt_3_sinif = Gt_3_sinif;
    }

    public void setgt_3_seri(String Gt_3_seri) {
        gt_3_seri = Gt_3_seri;
    }

    public void setgt_3_imalyili(int Gt_3_imalyili) {
        gt_3_imalyili = Gt_3_imalyili;
    }

    public void setprimer_akim_1(String Primer_akim_1) {
        primer_akim_1 = Primer_akim_1;
    }

    public void setsekonde_akim_1(String Sekonde_akim_1) {
        sekonde_akim_1 = Sekonde_akim_1;
    }

    public void setsayac_akim_1(String Sayac_akim_1) {
        sayac_akim_1 = Sayac_akim_1;
    }

    public void setprimer_akim_2(String Primer_akim_2) {
        primer_akim_2 = Primer_akim_2;
    }

    public void setsekonde_akim_2(String Sekonde_akim_2) {
        sekonde_akim_2 = Sekonde_akim_2;
    }

    public void setsayac_akim_2(String Sayac_akim_2) {
        sayac_akim_2 = Sayac_akim_2;
    }

    public void setprimer_akim_3(String Primer_akim_3) {
        primer_akim_3 = Primer_akim_3;
    }

    public void setsekonde_akim_3(String Sekonde_akim_3) {
        sekonde_akim_3 = Sekonde_akim_3;
    }

    public void setsayac_akim_3(String Sayac_akim_3) {
        sayac_akim_3 = Sayac_akim_3;
    }

    public void setsayac_gerili_1(String Sayac_gerili_1) {
        sayac_gerili_1 = Sayac_gerili_1;
    }

    public void setsayac_gerili_2(String Sayac_gerili_2) {
        sayac_gerili_2 = Sayac_gerili_2;
    }

    public void setsayac_gerili_3(String Sayac_gerili_3) {
        sayac_gerili_3 = Sayac_gerili_3;
    }

    public void setson_primer_akim_1(String Son_primer_akim_1) { son_primer_akim_1 = Son_primer_akim_1; }

    public void setson_sekonde_akim_1(String Son_sekonde_akim_1) { son_sekonde_akim_1 = Son_sekonde_akim_1; }

    public void setson_sayac_akim_1(String Son_sayac_akim_1) { son_sayac_akim_1 = Son_sayac_akim_1; }

    public void setson_primer_akim_2(String Son_primer_akim_2) { son_primer_akim_2 = Son_primer_akim_2; }

    public void setson_sekonde_akim_2(String Son_sekonde_akim_2) { son_sekonde_akim_2 = Son_sekonde_akim_2; }

    public void setson_sayac_akim_2(String Son_sayac_akim_2) { son_sayac_akim_2 = Son_sayac_akim_2; }

    public void setson_primer_akim_3(String Son_primer_akim_3) { son_primer_akim_3 = Son_primer_akim_3; }

    public void setson_sekonde_akim_3(String Son_sekonde_akim_3) { son_sekonde_akim_3 = Son_sekonde_akim_3; }

    public void setson_sayac_akim_3(String Son_sayac_akim_3) { son_sayac_akim_3 = Son_sayac_akim_3; }

    public void setson_sayac_gerili_1(String Son_sayac_gerili_1) { son_sayac_gerili_1 = Son_sayac_gerili_1; }

    public void setson_sayac_gerili_2(String Son_sayac_gerili_2) { son_sayac_gerili_2 = Son_sayac_gerili_2; }

    public void setson_sayac_gerili_3(String Son_sayac_gerili_3) { son_sayac_gerili_3 = Son_sayac_gerili_3; }

    public void setgerilim_trafosu_dur(int Gerilim_trafosu_dur) { gerilim_trafosu_dur = Gerilim_trafosu_dur; }

    //H.Elif
    public void set_polarite_1(String Polarite_1) {
        polarite_1 = Polarite_1;
    }

    public void set_polarite_2(String Polarite_2) {
        polarite_2 = Polarite_2;
    }

    public void set_polarite_3(String Polarite_3) {
        polarite_3 = Polarite_3;
    }

    public void setson_polarite_1(String SonPolarite_1) {
        sonpolarite_1 = SonPolarite_1;
    }

    public void setson_polarite_2(String SonPolarite_2) {
        sonpolarite_2 = SonPolarite_2;
    }

    public void setson_polarite_3(String SonPolarite_3) {
        sonpolarite_3 = SonPolarite_3;
    }

    // Set Fonk. Abone Bilgileri H.Elif
    public void set_unvan(String Unvan_txt) {
        unvan_txt = Unvan_txt;
    }

    public void set_adres(String Adres_txt) {
        adres_txt = Adres_txt;
    }

    public void set_sayac_no(String Sayac_no_txt) {
        sayac_no_txt = Sayac_no_txt;
    }

    public void set_sayac_marka(String Sayac_marka_spn) {
        sayac_marka_spn = Sayac_marka_spn;
    }

    public void set_hane_sayisi(String Hane_sayisi_txt) {
        hane_sayisi_txt = Hane_sayisi_txt;
    }

    public void set_imal_yili(int Imal_yili_txt) {
        imal_yili_txt = Imal_yili_txt;
    }

    public void set_tcNo(String TcNo_txt) {
        tcNo_txt = TcNo_txt;
    }

    public void set_telNo(String TelNo_txt) {
        telNo_txt = TelNo_txt;
    }

    public void set_eposta(String Eposta_txt) {
        eposta_txt = Eposta_txt;
    }

    public void set_faz(int Faz_txt) {
        faz_txt = Faz_txt;
    }

    public void set_amperaj(int Amperaj) {
        amperaj = Amperaj;
    }

    public void set_voltaj(int Voltaj) {
        voltaj = Voltaj;
    }

    // güç trafosu
    public void set_trafoMarkasi(String TrafoMarkasi_txt) {
        trafoMarkasi_txt = TrafoMarkasi_txt;
    }

    public void set_trafoSerino(int TrafoSerino_txt) {
        trafoSerino_txt = TrafoSerino_txt;
    }

    public void set_trafoGucu(int TrafoGucu_txt) {
        trafoGucu_txt = TrafoGucu_txt;
    }

    public void set_trafoGerilimi(int TrafoGerilimi_txt) {
        trafoGerilimi_txt = TrafoGerilimi_txt;
    }

    public void set_trafoImalYili(int TrafoImalYili_txt) {
        trafoImalYili_txt = TrafoImalYili_txt;
    }

    // Set Fonk. Sökülen Mühür Bilgileri H.Elif
    public void set_sokulen_muhur_list(List<String> Sokulen_muhur_list) {
        sokulen_muhur_list = Sokulen_muhur_list;
    }

    public void set_aciklama(String Aciklama) {
        aciklama = Aciklama;
    }

    public void set_ihbar_dur(int Ihbar_dur) {
        ihbar_dur = Ihbar_dur;
    }

    // Set Fonk. Güç Tespit Bilgileri H.Elif
    public void set_guc_tespit_list(List<GucTespit> Guc_tespit_list) {
        guc_tespit_list = Guc_tespit_list;
    }

    public void set_guc_tespit_cins(List<String> Guc_tespit_cins) {
        guc_tespit_cins = Guc_tespit_cins;
    }

    public void set_guc_tespit_adet(List<String> Guc_tespit_adet) {
        guc_tespit_adet = Guc_tespit_adet;
    }

    public void set_guc_tespit_guc(List<String> Guc_tespit_guc) {
        guc_tespit_guc = Guc_tespit_guc;
    }

    public void set_tarife_kodu(String Tarife_kodu) {
        tarifeKodu = Tarife_kodu;
    }

    public void set_cbs1(String Cbs1) {
        cbs1 = Cbs1;
    }

    public void set_cbs2(String Cbs2) {
        cbs2 = Cbs2;
    }

    public void set_yeni_muhur(String Yeni_muhur) {
        yeni_muhur = Yeni_muhur;
    }

    public void set_t(String T) {
        t = T;
    }

    public void set_t1(String T1) {
        t1 = T1;
    }

    public void set_t2(String T2) {
        t2 = T2;
    }

    public void set_t3(String T3) {
        t3 = T3;
    }

    public void set_kapasitif(String Kapasitif) {
        kapasitif = Kapasitif;
    }

    public void set_enduktif(String Enduktif) {
        enduktif = Enduktif;
    }

    public void set_demand(String Demand) {
        demand = Demand;
    }

    public void set_demand_tarihi(String Demand_tarihi) {
        demand_tarihi = Demand_tarihi;
    }

    public void set_user_code(int User_code) {
        user_code = User_code;
    }

    public void set_date(String Date) {
        date = Date;
    }

    public void set_time(String Time) {
        time = Time;
    }

    public void set_formType(String FormType) {
        formType = FormType;
    }

    public void set_takilan_marka(String takilan_marka) {

        this.takilan_marka = takilan_marka;
    }

    public void set_takilan_no(String takilan_no) {
        this.takilan_no = takilan_no;
    }

    public void set_takilan_akim(String takilan_akim) {
        this.takilan_akim = takilan_akim;
    }

    public void set_takilan_gerilim(String takilan_gerilim) {
        this.takilan_gerilim = takilan_gerilim;
    }

    public void set_takilan_carpan(String takilan_carpan) {
        this.takilan_carpan = takilan_carpan;
    }

    public void set_takilan_haneadeti(String takilan_haneadeti) {
        this.takilan_haneadeti = takilan_haneadeti;
    }

    public void set_takilan_imalyili(String takilan_imalyili) {
        this.takilan_imalyili = takilan_imalyili;
    }

    public void set_takilan_damgayili(String takilan_damgayili) {
        this.takilan_damgayili = takilan_damgayili;
    }

    public void set_takilan_t(String takilan_t) {
        this.takilan_t = takilan_t;
    }

    public void set_takilan_t1(String takilan_t1) {
        this.takilan_t1 = takilan_t1;
    }

    public void set_takilan_t2(String takilan_t2) {
        this.takilan_t2 = takilan_t2;
    }

    public void set_takilan_t3(String takilan_t3) {
        this.takilan_t3 = takilan_t3;
    }

    public void set_takilan_kapasitif(String takilan_kapasitif) {
        this.takilan_kapasitif = takilan_kapasitif;
    }

    public void set_takilan_enduktif(String takilan_enduktif) {
        this.takilan_enduktif = takilan_enduktif;
    }

    public void set_takilan_demand(String takilan_demand) {
        this.takilan_demand = takilan_demand;
    }


    public void set_at_1_sinif_gucu(String at_1_sinif_gucu) {
        this.at_1_sinif_gucu = at_1_sinif_gucu;
    }

    public void set_at_2_sinif_gucu(String at_2_sinif_gucu) {
        this.at_2_sinif_gucu = at_2_sinif_gucu;
    }

    public void set_at_3_sinif_gucu(String at_3_sinif_gucu) {
        this.at_3_sinif_gucu = at_3_sinif_gucu;
    }

    public void set_gt_1_sinif_gucu(String gt_1_sinif_gucu) {
        this.gt_1_sinif_gucu = gt_1_sinif_gucu;
    }

    public void set_gt_2_sinif_gucu(String gt_2_sinif_gucu) {
        this.gt_2_sinif_gucu = gt_2_sinif_gucu;
    }

    public void set_gt_3_sinif_gucu(String gt_3_sinif_gucu) {
        this.gt_3_sinif_gucu = gt_3_sinif_gucu;
    }

    public void set_sayac_degisimi_dur(int sayac_degisimi_dur) { this.sayac_degisimi_dur = sayac_degisimi_dur; }

    public void set_disaridan_olcu_dur(int disaridan_olcu_dur) { this.disaridan_olcu_dur = disaridan_olcu_dur; }




    //Sıfırlama fonksiyonu
    public void AllClear() {
        kadi = "dusoft";
        sifre = "Sayac!Sor123";
        tesisat_no = -1;
        isemri_no = -1;
        alt_emir_turu = -1;
        tesis_enerji_dur = -1;
        pano_muhur_dur = -1;
        sayac_muhur_dur = -1;
        sayac_ariza_dur = -1;
        akim_trafosu_dur = -1;
        guc_trafosu_dur = -1;
        guc_tespiti_dur = -1;
        AT_1_Marka = "";
        AT_1_Oran = "/";
        AT_1_gucu = -1;
        at_1_sinif = "";
        at_1_seri = "";
        at_1_imalyili = -1;
        AT_2_Marka = "";
        AT_2_Oran = "/";
        AT_2_gucu = -1;
        at_2_sinif = "";
        at_2_seri = "";
        at_2_imalyili = -1;
        AT_3_Marka = "";
        AT_3_Oran = "/";
        AT_3_gucu = -1;
        at_3_sinif = "";
        at_3_seri = "";
        at_3_imalyili = -1;
        gt_1_Marka = "";
        gt_1_Oran = "/";
        gt_1_gucu = -1;
        gt_1_sinif = "";
        gt_1_seri = "";
        gt_1_imalyili = -1;
        gt_2_Marka = "";
        gt_2_Oran = "/";
        gt_2_gucu = -1;
        gt_2_sinif = "";
        gt_2_seri = "";
        gt_2_imalyili = -1;
        gt_3_Marka = "";
        gt_3_Oran = "/";
        gt_3_gucu = -1;
        gt_3_sinif = "";
        gt_3_seri = "";
        gt_3_imalyili = -1;
        primer_akim_1 = "";
        sekonde_akim_1 = "";
        sayac_akim_1 = "";
        primer_akim_2 = "";
        sekonde_akim_2 = "";
        sayac_akim_2 = "";
        primer_akim_3 = "";
        sekonde_akim_3 = "";
        sayac_akim_3 = "";
        sayac_gerili_1 = "";
        sayac_gerili_2 = "";
        sayac_gerili_3 = "";
        son_primer_akim_1 = "";
        son_sekonde_akim_1 = "";
        son_sayac_akim_1 = "";
        son_primer_akim_2 = "";
        son_sekonde_akim_2 = "";
        son_sayac_akim_2 = "";
        son_primer_akim_3 = "";
        son_sekonde_akim_3 = "";
        son_sayac_akim_3 = "";
        son_sayac_gerili_1 = "";
        son_sayac_gerili_2 = "";
        son_sayac_gerili_3 = "";
        gerilim_trafosu_dur = -1;
        carpan = 0.0;
        servisdur = -1;

        //H.Elif
        polarite_1 = "";
        polarite_2 = "";
        polarite_3 = "";
        sonpolarite_1 = "";
        sonpolarite_2 = "";
        sonpolarite_3 = "";

        unvan_txt = "";
        adres_txt = "";
        sayac_no_txt = "";
        sayac_marka_spn = "";
        hane_sayisi_txt = "";
        imal_yili_txt = -1;
        tcNo_txt = "";
        telNo_txt = "";
        eposta_txt = "";
        faz_txt = -1;
        amperaj = -1;
        voltaj = -1;

        trafoMarkasi_txt = "";
        trafoSerino_txt = -1;
        trafoGucu_txt = -1;
        trafoGerilimi_txt = -1;
        trafoImalYili_txt = -1;

        sokulen_muhur_list = new ArrayList<String>();
        aciklama = "";
        ihbar_dur = -1;

        guc_tespit_list = new ArrayList<GucTespit>();
        guc_tespit_cins = new ArrayList<String>();
        guc_tespit_adet = new ArrayList<String>();
        guc_tespit_guc = new ArrayList<String>();

        tarifeKodu = "";
        cbs1 = "";
        cbs2 = "";
        yeni_muhur = "";
        t = "";
        t1 = "";
        t2 = "";
        t3 = "";
        kapasitif = "";
        enduktif = "";
        demand = "";
        demand_tarihi = "";
        user_code = -1;
        date = "";
        time = "";
        formType = "";

        takilan_marka = "";
        takilan_no = "";
        takilan_akim = "";
        takilan_gerilim = "";
        takilan_carpan = "";
        takilan_haneadeti = "";
        takilan_imalyili = "";
        takilan_damgayili = "";
        takilan_t = "";
        takilan_t1 = "";
        takilan_t2 = "";
        takilan_t3 = "";
        takilan_kapasitif = "";
        takilan_enduktif = "";
        takilan_demand = "";

        at_1_sinif_gucu = "";
        at_2_sinif_gucu = "";
        at_3_sinif_gucu = "";

        gt_1_sinif_gucu = "";
        gt_2_sinif_gucu = "";
        gt_3_sinif_gucu = "";

        sayac_degisimi_dur = 0;
        disaridan_olcu_dur = 0;
    }

}


