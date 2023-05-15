package mobit.elec.medas.ws;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;


import org.ksoap2.x.SoapEnvelope;
import org.ksoap2.x.SoapFault;
import org.ksoap2.x.serialization.PropertyInfo;

import org.ksoap2.x.serialization.SoapObject;
import org.ksoap2.x.serialization.SoapSerializationEnvelope;
import org.ksoap2.x.transport.HttpTransportSE;

import com.mobit.Callback;
import com.mobit.IApplication;
import com.mobit.IForm;
import com.mobit.IIslemMaster;
import com.mobit.IServer;
import com.mobit.MobitException;

import mobit.eemr.GucTespit;
import mobit.eemr.OlcuDevreForm;
import mobit.eemr.YkpEndeksDoldur;
import mobit.elec.AltEmirTuru;
import mobit.elec.Endeksler;
import mobit.elec.Globals;
import mobit.elec.IAdurum;
import mobit.elec.IEndeksler;
import mobit.elec.IIslemYetki;
import mobit.elec.ISayacMarka;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.FazSayisi;
import mobit.elec.enums.IFazSayisi;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IVoltaj;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacHaneSayisi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.enums.Voltaj;
import mobit.elec.mbs.Endeks;
import mobit.elec.mbs.enums.SayacCinsi;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.medas.IKacak;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.http.ObjectList;
import mobit.http.Result;
import mobit.http.ReturnValue;


public abstract class SayacZimmetBilgi implements IServer {

    public class MedasWsException extends MobitException {

        /**
         *
         */
        Result result;
        private static final long serialVersionUID = -6359575296363557498L;
        private static final String s_caption = "Medaş Web Servis Hata";

        public MedasWsException(String message) {
            super(message, s_caption);
        }

        public MedasWsException(Result result) {
            super(result.ReturnMessage, s_caption);
            this.result = result;
        }
    }

    static Type type;
    // static Object res;

    private static final String NAMESPACE = "http://tempuri.org/"; //
    private static final String URL = "http://MaximoSayac/SayacZimmetBilgi.asmx";
    private int SOAP_VER = SoapEnvelope.VER12;
    private static final boolean dotNet = true;
    private static final boolean debug = Globals.isDeveloping();

    protected String kadi = "dusoft";
    protected String sifre = "Sayac!Sor123";

    // -------------------------------------------------------------------------
    // Tesisatsız Kaçakların İhbarı için
    private static final String methodKacakBilgisiGondermeServisi = "KacakBilgisiGondermeServisi";
    private static final String actionKacakBilgisiGondermeServisi = NAMESPACE + methodKacakBilgisiGondermeServisi;

    public static class KacakBilgisi {
        public String ADRES_TARIFI;
        public String KACAKCI_UNVAN;
        public String KACAKCI_TELEFON;
        public String KACAKCI_EMAIL;
        public String KACAK_TIPI;
        public String CBS_X;
        public String CBS_Y;
        public String IHBAR_EDEN_UNVAN;
        public String IHBAR_EDEN_TELEFON;
        public String IHBAR_EDEN_EMAIL;
        public int TESISAT_NO;
        public int REFERANS_TESISAT_NO;
        public int OKUYUCU_KODU;
        public String ACIKLAMA;
        public String DIREK_NO;
        public String BOX_NO;
        public String SAYAC_NO;

        public KacakBilgisi() {

        }

        public KacakBilgisi(IIslemMaster master, IKacak k) {

            ADRES_TARIFI = k.getADRES_TARIF();
            KACAKCI_UNVAN = k.getKACAKCI_UNVAN();
            KACAKCI_TELEFON = k.getKACAKCI_TELEFON();
            KACAKCI_EMAIL = k.getKACAKCI_EMAIL();
            KACAK_TIPI = k.getKACAK_TIPI();
            if (master.getCBS() != null) {
                try {
                    CBS_X = master.getCBS().getX().toString();
                    CBS_Y = master.getCBS().getY().toString();
                } catch (Exception e) {

                }
            }
            IHBAR_EDEN_UNVAN = k.getIHBAR_EDEN_UNVAN();
            IHBAR_EDEN_TELEFON = k.getIHBAR_EDEN_TELEFON();
            IHBAR_EDEN_EMAIL = k.getIHBAR_EDEN_EMAIL();
            TESISAT_NO = k.getTESISAT_NO();
            REFERANS_TESISAT_NO = k.getREFERANS_TESISAT_NO();
            OKUYUCU_KODU = master.getEleman().getELEMAN_KODU();
            ACIKLAMA = k.getACIKLAMA();
            DIREK_NO = k.getDIREK_NO();
            BOX_NO = k.getBOX_NO();
            SAYAC_NO = k.getSAYAC_NO().toString();
        }
    }

    ;

    public void KacakBilgisiGondermeServisi(KacakBilgisi bilgi) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodKacakBilgisiGondermeServisi);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("ADRES_TARIFI", bilgi.ADRES_TARIFI);
        request.addProperty("KACAKCI_UNVAN", bilgi.KACAKCI_UNVAN);
        request.addProperty("KACAKCI_TELEFON", bilgi.KACAKCI_TELEFON);
        request.addProperty("KACAKCI_EMAIL", bilgi.KACAKCI_EMAIL);
        request.addProperty("KACAK_TIPI", bilgi.KACAK_TIPI);
        request.addProperty("CBS_X", bilgi.CBS_X);
        request.addProperty("CBS_Y", bilgi.CBS_Y);
        request.addProperty("IHBAR_EDEN_UNVAN", bilgi.IHBAR_EDEN_UNVAN);
        request.addProperty("IHBAR_EDEN_TELEFON", bilgi.IHBAR_EDEN_TELEFON);
        request.addProperty("IHBAR_EDEN_EMAIL", bilgi.IHBAR_EDEN_EMAIL);
        request.addProperty("TESISAT_NO", bilgi.TESISAT_NO);
        request.addProperty("REFERANS_TESISAT_NO", bilgi.REFERANS_TESISAT_NO);
        request.addProperty("OKUYUCU_KODU", bilgi.OKUYUCU_KODU);
        request.addProperty("ACIKLAMA", bilgi.ACIKLAMA);
        request.addProperty("DIREK_NO", bilgi.DIREK_NO);
        request.addProperty("BOX_NO", bilgi.BOX_NO);
        request.addProperty("SAYAC_NO", bilgi.SAYAC_NO);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "KacakBilgisiGondermeServisiResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionKacakBilgisiGondermeServisi, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof SoapObject) {
            SoapObject response = null;
            response = (SoapObject) envelope.bodyIn;
            response.getProperty(0);

        }

    }

    public void KacakBilgisiGondermeServisi(IForm form, KacakBilgisi bilgi, Callback clb) throws Exception {

        KacakBilgisiGondermeServisi(bilgi);
        if (clb != null)
            clb.run(clb);
    }

    private static final String methodYikikIhbarServisi = "YikikIhbarServisi";
    private static final String actionYikikIhbarServisi = NAMESPACE + methodYikikIhbarServisi;


    // yıkık ihbar servisi
    public String YikikIhbarServisi(int TESISAT_NO, String SAYAC_NO, String DIREK_NO, String BOX_NO, int REFERANS_TESISAT_NO, int OKUYUCU_KODU, String CBS_X, String CBS_Y, String KAYIT_TARIHI, String ACIKLAMA, String ADRES_TARIFI, String T1, String T2, String T3, String RI, String RC) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodYikikIhbarServisi);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("TESISAT_NO", TESISAT_NO);
        request.addProperty("SAYAC_NO", SAYAC_NO);
        request.addProperty("DIREK_NO", DIREK_NO);
        request.addProperty("BOX_NO", BOX_NO);
        request.addProperty("REFERANS_TESISAT_NO", REFERANS_TESISAT_NO);
        request.addProperty("CBS_X", CBS_X);
        request.addProperty("CBS_Y", CBS_Y);
        request.addProperty("KAYIT_TARIHI", KAYIT_TARIHI);
        request.addProperty("ACIKLAMA", ACIKLAMA);
        request.addProperty("ADRES_TARIFI", ADRES_TARIFI);
        request.addProperty("T1", T1);
        request.addProperty("T2", T2);
        request.addProperty("OKUYUCU_KODU", OKUYUCU_KODU);
        request.addProperty("T3", T3);
        request.addProperty("RI", RI);
        request.addProperty("RC", RC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "YikikIhbarServisiResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionYikikIhbarServisi, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof SoapObject) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return result.ReturnCode;

        }
        return null;

    }

    public abstract void YikikIhbarServisi(int TESISAT_NO, String SAYAC_NO, String DIREK_NO, String BOX_NO, int REFERANS_TESISAT_NO, int OKUYUCU_KODU, String CBS_X, String CBS_Y, String KAYIT_TARIHI, String ACIKLAMA, String ADRES_TARIFI, String T1, String T2, String T3, String RI, String RC, Callback clb) throws Exception;

    // -------------------------------------------------------------------------
    // Sayaç değişiminde sayaç bilgileri ve zimmet kontrolü için
    private static final String methodSayacBilgiService = "SayacBilgiService";
    private static final String actionSayacBilgiService = NAMESPACE + methodSayacBilgiService;

    public static class SayacBilgi extends SoapObject {

        public String QRCODEID;// >4935439</QRCODEID>
        public String SAYAC_KODU;// >18869400</SAYAC_KODU>
        public String MARKA;// >MAKEL</MARKA>
        public String STOK_KODU;// >28.11.101.0001</STOK_KODU>
        public String ASSET;// >1090556</ASSET>
        public String DURUM;// >CIKILDI</DURUM>
        public String EKLEYEN;// >RUYGUN</EKLEYEN>
        public String EKLENME_TARIHI;
        public String AKTARIM_TARIHI;// >20170222</AKTARIM_TARIHI>
        public String MARKA_KODU;// >MSY</MARKA_KODU>
        public String MODEL_KODU;// >Aktif</MODEL_KODU>
        public String TIP_KODU;// >Elektronik</TIP_KODU>
        public String AKTIF_ENERJI_TIPI;// >TrippleInterval</AKTIF_ENERJI_TIPI>
        public String IMAL_YILI_KODU;// >2016</IMAL_YILI_KODU>
        public String BAGLANTI_TIPI;// >Normal</BAGLANTI_TIPI>
        public String FAZ_SAYISI;// >1</FAZ_SAYISI>
        public String VOLTAJ;// >220</VOLTAJ>
        public String AMPERAJ;// >10</AMPERAJ>
        public String CARPAN;// >1</CARPAN>
        public String ENDEKS;// >6</ENDEKS>
        public String SAYAC_MODEL;// >M560.2251S</SAYAC_MODEL>
        public String KOLI_NO;// >U2590804977</KOLI_NO>
        public String PALET;// >P2590805772</PALET>
        public String QRKOLI_NO;// >U2590804977</QRKOLI_NO>
        public String QRSTOK_KODU;// >28.11.101.0001</QRSTOK_KODU>
        public String CIKIS_TARIHI;// >20170222</CIKIS_TARIHI>
        public String CIKIS_DEPO;// >MEDAS-KNY-S</CIKIS_DEPO>
        public String CIKILAN_ISLETME;// >MERAM-1</CIKILAN_ISLETME>
        public String CIKILAN_CARI_KOD;// >MERAM-1</CIKILAN_CARI_KOD>
        public String CIKILAN_FIRMA;// >TUKETICI HIZMETLERI
        // MUDURLUGU</CIKILAN_FIRMA>
        public String ANALIZ_KODU;// >01.01.023.0001</ANALIZ_KODU>
        public String KOD_ACIKLAMASI;// >15EM603-YENI ABONELIKLER ICIN SAYAC
        // DEGI</KOD_ACIKLAMASI>
        public String EXCEL_NO;// >2610</EXCEL_NO>
        public String GRUP_KODU;// >5</GRUP_KODU>
        public String CIKISID;// >1073848</CIKISID>
        public String YARATILMA_TARIHI;// >2017-02-22T11:01:57.157443+03:00</YARATILMA_TARIHI>
        public String TESISAT_NO;// >1620232</TESISAT_NO>
        public String SOKMETAKMA;// >Tarih:04.08.2017 Takildigi Tesisat:
        // 1620232,Tarih:04.08.2017 Sokuldugu
        // Tesisat: 1620232,Tarih:04.08.2017
        // Takildigi Tesisat: 1620232,</SOKMETAKMA>
        public String TALEPEDEN;// >HSABUNCULAR</TALEPEDEN>
        public String ONAYLAYAN;// >VUNAL</ONAYLAYAN>
        public String TESLIMALAN;// >murat oz</TESLIMALAN>
        // </sayacBilgi>
        // </data_sayac>

        public ISayacKodu getSayacKodu() throws Exception {
            if (MODEL_KODU.equalsIgnoreCase("Aktif"))
                return SayacKodu.Aktif;
            else if (MODEL_KODU.equalsIgnoreCase("Reaktif"))
                return SayacKodu.Reaktif;
            else if (MODEL_KODU.equalsIgnoreCase("Kapasitif"))
                return SayacKodu.Kapasitif;
            else if (MODEL_KODU.equalsIgnoreCase("Kombi"))
                return SayacKodu.Aktif;
            throw new MobitException("Tanımsız sayac model kodu!");
        }

        public ISayacHaneSayisi getHaneSayisi() {
            return SayacHaneSayisi.fromInteger(Integer.parseInt(ENDEKS));
        }

        public ISayacMarka getSayacMarka() {

            ISayacMarka marka = new sayac_marka();
            marka.setSAYAC_MARKA_KODU(MARKA_KODU);
            marka.setSAYAC_MARKA_ADI(MARKA);
            return marka;

        }

        public ISayacCinsi getSayacCinsi() throws Exception {
            if (TIP_KODU.equalsIgnoreCase("Elektronik"))
                return (MODEL_KODU.equals("Kombi")) ? SayacCinsi.Kombi : SayacCinsi.Elektronik;
            else if (TIP_KODU.equalsIgnoreCase("Kombi"))
                return SayacCinsi.Kombi;
            else if (TIP_KODU.equalsIgnoreCase("Mekanik"))
                return SayacCinsi.Mekanik;
            throw new MobitException("Tanımsız sayaç tip kodu!");
        }

        public IFazSayisi getFazSayisi() {
            if (BAGLANTI_TIPI != null && BAGLANTI_TIPI.equalsIgnoreCase("X5"))
                return FazSayisi.fromInteger(5);
            return FazSayisi.fromInteger(Integer.parseInt(FAZ_SAYISI));// >1</FAZ_SAYISI>
        }

        public IVoltaj getVoltaj() {
            return Voltaj.fromInteger(Integer.parseInt(VOLTAJ));// >220</VOLTAJ>
        }

        public int getImalYili() {
            return Integer.parseInt(IMAL_YILI_KODU);
        }

        public int getAmperaj() {
            return Integer.parseInt(AMPERAJ);
        }

        public SayacBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return QRCODEID;
                case 1:
                    return SAYAC_KODU;
                case 2:
                    return MARKA;
                case 3:
                    return STOK_KODU;
                case 4:
                    return ASSET;
                case 5:
                    return DURUM;
                case 6:
                    return EKLEYEN;
                case 7:
                    return EKLENME_TARIHI;
                case 8:
                    return AKTARIM_TARIHI;
                case 9:
                    return MARKA_KODU;
                case 10:
                    return MODEL_KODU;
                case 11:
                    return TIP_KODU;
                case 12:
                    return AKTIF_ENERJI_TIPI;
                case 13:
                    return IMAL_YILI_KODU;
                case 14:
                    return BAGLANTI_TIPI;
                case 15:
                    return FAZ_SAYISI;
                case 16:
                    return VOLTAJ;
                case 17:
                    return AMPERAJ;
                case 18:
                    return CARPAN;
                case 19:
                    return ENDEKS;
                case 20:
                    return SAYAC_MODEL;
                case 21:
                    return KOLI_NO;
                case 22:
                    return PALET;
                case 23:
                    return QRKOLI_NO;
                case 24:
                    return QRSTOK_KODU;
                case 25:
                    return CIKIS_TARIHI;
                case 26:
                    return CIKIS_DEPO;
                case 27:
                    return CIKILAN_ISLETME;
                case 28:
                    return CIKILAN_CARI_KOD;
                case 29:
                    return CIKILAN_FIRMA;
                case 30:
                    return ANALIZ_KODU;
                case 31:
                    return KOD_ACIKLAMASI;
                case 32:
                    return EXCEL_NO;
                case 33:
                    return GRUP_KODU;
                case 34:
                    return CIKISID;
                case 35:
                    return YARATILMA_TARIHI;
                case 36:
                    return TESISAT_NO;
                case 37:
                    return SOKMETAKMA;
                case 38:
                    return TALEPEDEN;
                case 39:
                    return ONAYLAYAN;
                case 40:
                    return TESLIMALAN;
            }
            return null;
        }

        @Override
        public int getPropertyCount() {

            return propertyList.length;
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        private static final String[] propertyList = new String[]{

                "QRCODEID", "SAYAC_KODU", "MARKA", "STOK_KODU", "ASSET", "DURUM", "EKLEYEN", "EKLENME_TARIHI",
                "AKTARIM_TARIHI", "MARKA_KODU", "MODEL_KODU", "TIP_KODU", "AKTIF_ENERJI_TIPI", "IMAL_YILI_KODU",
                "BAGLANTI_TIPI", "FAZ_SAYISI", "VOLTAJ", "AMPERAJ", "CARPAN", "ENDEKS", "SAYAC_MODEL", "KOLI_NO",
                "PALET", "QRKOLI_NO", "QRSTOK_KODU", "CIKIS_TARIHI", "CIKIS_DEPO", "CIKILAN_ISLETME",
                "CIKILAN_CARI_KOD", "CIKILAN_FIRMA", "ANALIZ_KODU", "KOD_ACIKLAMASI", "EXCEL_NO", "GRUP_KODU",
                "CIKISID", "YARATILMA_TARIHI", "TESISAT_NO", "SOKMETAKMA", "TALEPEDEN", "ONAYLAYAN", "TESLIMALAN"};

        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);

        }

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    QRCODEID = (String) value;
                    return;
                case 1:
                    SAYAC_KODU = (String) value;
                    return;
                case 2:
                    MARKA = (String) value;
                    return;
                case 3:
                    STOK_KODU = (String) value;
                    return;
                case 4:
                    ASSET = (String) value;
                    return;
                case 5:
                    DURUM = (String) value;
                    return;
                case 6:
                    EKLEYEN = (String) value;
                    return;
                case 7:
                    EKLENME_TARIHI = (String) value;
                    return;
                case 8:
                    AKTARIM_TARIHI = (String) value;
                    return;
                case 9:
                    MARKA_KODU = (String) value;
                    return;
                case 10:
                    MODEL_KODU = (String) value;
                    return;
                case 11:
                    TIP_KODU = (String) value;
                    return;
                case 12:
                    AKTIF_ENERJI_TIPI = (String) value;
                    return;
                case 13:
                    IMAL_YILI_KODU = (String) value;
                    return;
                case 14:
                    BAGLANTI_TIPI = (String) value;
                    return;
                case 15:
                    FAZ_SAYISI = (String) value;
                    return;
                case 16:
                    VOLTAJ = (String) value;
                    return;
                case 17:
                    AMPERAJ = (String) value;
                    return;
                case 18:
                    CARPAN = (String) value;
                    return;
                case 19:
                    ENDEKS = (String) value;
                    return;
                case 20:
                    SAYAC_MODEL = (String) value;
                    return;
                case 21:
                    KOLI_NO = (String) value;
                    return;
                case 22:
                    PALET = (String) value;
                    return;
                case 23:
                    QRKOLI_NO = (String) value;
                    return;
                case 24:
                    QRSTOK_KODU = (String) value;
                    return;
                case 25:
                    CIKIS_TARIHI = (String) value;
                    return;
                case 26:
                    CIKIS_DEPO = (String) value;
                    return;
                case 27:
                    CIKILAN_ISLETME = (String) value;
                    return;
                case 28:
                    CIKILAN_CARI_KOD = (String) value;
                    return;
                case 29:
                    CIKILAN_FIRMA = (String) value;
                    return;
                case 30:
                    ANALIZ_KODU = (String) value;
                    return;
                case 31:
                    KOD_ACIKLAMASI = (String) value;
                    return;
                case 32:
                    EXCEL_NO = (String) value;
                    return;
                case 33:
                    GRUP_KODU = (String) value;
                    return;
                case 34:
                    CIKISID = (String) value;
                    return;
                case 35:
                    YARATILMA_TARIHI = (String) value;
                    return;
                case 36:
                    TESISAT_NO = (String) value;
                    return;
                case 37:
                    SOKMETAKMA = (String) value;
                    return;
                case 38:
                    TALEPEDEN = (String) value;
                    return;
                case 39:
                    ONAYLAYAN = (String) value;
                    return;
                case 40:
                    TESLIMALAN = (String) value;
                    return;
            }

        }

    }

    protected SayacBilgi SayacBilgiService(int okuyucuKodu, int seriNo, String sayacMarka, int isEmriNo) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodSayacBilgiService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucuKodu", okuyucuKodu);
        request.addProperty("seriNo", seriNo);
        request.addProperty("sayacMarka", sayacMarka);
        request.addProperty("isEmriNo", isEmriNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "SayacBilgiServiceResult", Result.class);
        envelope.addMapping(NAMESPACE, "sayacBilgi", SayacBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionSayacBilgiService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof SoapObject) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return (SayacBilgi) value.value;
        }

        return null;

    }

    public void SayacBilgiService(IForm form, int okuyucuKodu, int seriNo, String sayacMarka, int isEmriNo, Callback clb)
            throws Exception {

        SayacBilgi sb = SayacBilgiService(okuyucuKodu, seriNo, sayacMarka, isEmriNo);
        if (clb != null)
            clb.run(sb);
    }

    // -------------------------------------------------------------------------
    // Sayaç takma sırasında eski sayaç takılacak ise eski sayacın bilgileri ve
    // endeks bilgilerini almak için
    private static final String methodSayacEndeksServisi = "SayacEndeksServisi";
    private static final String actionSayacEndeksServisi = NAMESPACE + methodSayacEndeksServisi;

    public static class SayacEndeksBilgi extends SoapObject {

        public String TESISAT_NO;// >1016605</TESISAT_NO>
        public String SAYAC_NO;// >18397831</SAYAC_NO>
        public String ISLEM_TARIHI;// >20171024</ISLEM_TARIHI>
        public String SAYAC_KODU;// >1</SAYAC_KODU>
        public String MARKA;// >MSY</MARKA>
        public String ENDEKST1;// >6787</ENDEKST1>
        public String ENDEKST2;// >8114</ENDEKST2>
        public String ENDEKST3;// >12902</ENDEKST3>
        public String ENDEKST4;// >533.797</ENDEKST4>
        public String ENDEKST5;// >288.109</ENDEKST5>
        public String FAZ_SAYISI;// >1</FAZ_SAYISI>
        public String DIJIT;// >5</DIJIT>
        public String MODEL_KODU;// >0</MODEL_KODU>
        public String IMAL_YILI;// >2015</IMAL_YILI>
        public String SAYAC_CINSI;// >1</SAYAC_CINSI>
        public String AMPERAJ;// >10</AMPERAJ>
        public String VOLTAJ;// >220</VOLTAJ>

        public ISayacKodu getSayacKodu() throws Exception {
            if (SAYAC_KODU.equalsIgnoreCase("1"))
                return SayacKodu.Aktif;
            else if (SAYAC_KODU.equalsIgnoreCase("4"))
                return SayacKodu.Reaktif;
            else if (SAYAC_KODU.equalsIgnoreCase("5"))
                return SayacKodu.Kapasitif;
            throw new MobitException("Tanımsız sayac kodu!");
        }

        public ISayacHaneSayisi getHaneSayisi() {
            return SayacHaneSayisi.fromInteger(Integer.parseInt(DIJIT));
        }

        public ISayacMarka getSayacMarka() {

            ISayacMarka marka = new sayac_marka();
            marka.setSAYAC_MARKA_KODU(MARKA);
            return marka;

        }

        public ISayacCinsi getSayacCinsi() throws Exception {
            if (SAYAC_CINSI.equalsIgnoreCase("1"))
                return SayacCinsi.Elektronik;
            else if (SAYAC_CINSI.equalsIgnoreCase("2"))
                return SayacCinsi.Kombi;
            else if (SAYAC_CINSI.equalsIgnoreCase("0"))
                return SayacCinsi.Mekanik;
            throw new MobitException("Tanımsız sayaç tip kodu!");
        }

        public IFazSayisi getFazSayisi() {
            return FazSayisi.fromInteger(Integer.parseInt(FAZ_SAYISI));// >1</FAZ_SAYISI>
        }

        public IVoltaj getVoltaj() {
            return Voltaj.fromInteger(Integer.parseInt(VOLTAJ));// >220</VOLTAJ>
        }

        public int getImalYili() {
            return Integer.parseInt(IMAL_YILI);
        }

        public int getAmperaj() {
            return Integer.parseInt(AMPERAJ);
        }

        private static final NumberFormat nf = NumberFormat.getInstance(Globals.enLocale);

        public IEndeksler getEndeksler() throws Exception {

            ISayacHaneSayisi haneSayisi = getHaneSayisi();
            IEndeksler endeksler = new Endeksler();

            if (ENDEKST1 != null && !ENDEKST1.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Gunduz, haneSayisi, nf.parse(ENDEKST1).doubleValue()));

            if (ENDEKST2 != null && !ENDEKST2.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Puant, haneSayisi, nf.parse(ENDEKST2).doubleValue()));

            if (ENDEKST3 != null && !ENDEKST3.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Gece, haneSayisi, nf.parse(ENDEKST3).doubleValue()));

            if (ENDEKST4 != null && !ENDEKST4.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Enduktif, haneSayisi, nf.parse(ENDEKST4).doubleValue()));

            if (ENDEKST5 != null && !ENDEKST5.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Kapasitif, haneSayisi, nf.parse(ENDEKST5).doubleValue()));

            return endeksler;
        }

        public SayacEndeksBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        private static final String[] propertyList = new String[]{"TESISAT_NO", "SAYAC_NO", "ISLEM_TARIHI",
                "SAYAC_KODU", "MARKA", "ENDEKST1", "ENDEKST2", "ENDEKST3", "ENDEKST4", "ENDEKST5", "FAZ_SAYISI",
                "DIJIT", "MODEL_KODU", "IMAL_YILI", "SAYAC_CINSI", "AMPERAJ", "VOLTAJ",};

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return TESISAT_NO;
                case 1:
                    return SAYAC_NO;
                case 2:
                    return ISLEM_TARIHI;
                case 3:
                    return SAYAC_KODU;
                case 4:
                    return MARKA;
                case 5:
                    return ENDEKST1;
                case 6:
                    return ENDEKST2;
                case 7:
                    return ENDEKST3;
                case 8:
                    return ENDEKST4;
                case 9:
                    return ENDEKST5;
                case 10:
                    return FAZ_SAYISI;
                case 11:
                    return DIJIT;
                case 12:
                    return MODEL_KODU;
                case 13:
                    return IMAL_YILI;
                case 14:
                    return SAYAC_CINSI;
                case 15:
                    return AMPERAJ;
                case 16:
                    return VOLTAJ;
            }

            return null;
        }

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        @Override
        public int getPropertyIndex(String propertName) {
            return getPropertyIndex(propertyList, propertName);
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];
        }

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    TESISAT_NO = (String) value;
                    return;
                case 1:
                    SAYAC_NO = (String) value;
                    return;
                case 2:
                    ISLEM_TARIHI = (String) value;
                    return;
                case 3:
                    SAYAC_KODU = (String) value;
                    return;
                case 4:
                    MARKA = (String) value;
                    return;
                case 5:
                    ENDEKST1 = (String) value;
                    return;
                case 6:
                    ENDEKST2 = (String) value;
                    return;
                case 7:
                    ENDEKST3 = (String) value;
                    return;
                case 8:
                    ENDEKST4 = (String) value;
                    return;
                case 9:
                    ENDEKST5 = (String) value;
                    return;
                case 10:
                    FAZ_SAYISI = (String) value;
                    return;
                case 11:
                    DIJIT = (String) value;
                    return;
                case 12:
                    MODEL_KODU = (String) value;
                    return;
                case 13:
                    IMAL_YILI = (String) value;
                    return;
                case 14:
                    SAYAC_CINSI = (String) value;
                    return;
                case 15:
                    AMPERAJ = (String) value;
                    return;
                case 16:
                    VOLTAJ = (String) value;
                    return;
            }
        }

    }

    protected SayacEndeksBilgi SayacEndeksServisi(String tesisatNo, String sayacNo) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodSayacEndeksServisi);

        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("tesisatNo", tesisatNo);
        request.addProperty("sayacNo", sayacNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "SayacEndeksServisiResult", Result.class);
        envelope.addMapping(NAMESPACE, "endeksBilgi", SayacEndeksBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionSayacEndeksServisi, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return (SayacEndeksBilgi) value.value;
        }

        return null;

    }

    public void SayacEndeksServisi(String tesisatNo, String sayacNo, Callback clb) throws Exception {

        Object obj = SayacEndeksServisi(tesisatNo, sayacNo);
        if (clb != null)
            clb.run(obj);
    }

    public abstract void SayacEndeksServisi(IForm form, String tesisatNo, String sayacNo, Callback clb);

    // -------------------------------------------------------------------------
    // Sayaç değişiminde eski bir sayaç takılıyorsa endekslerini almak için.
    private static final String methodSayacSenkService = "SayacSenkService";
    private static final String actionSayacSenkService = NAMESPACE + methodSayacSenkService;

    public static class SayacSenkBilgi extends SoapObject {

        public String OKUNAN_T1;// >3405</OKUNAN_T1>
        public String OKUNAN_T2;// >1827</OKUNAN_T2>
        public String OKUNAN_T3;// >1588</OKUNAN_T3>
        public String OKUNAN_T4;// >1588</OKUNAN_T4>
        public String OKUNAN_T5;// >1588</OKUNAN_T5>
        public String ES_SAYAC;// >12435906</ES_SAYAC>
        public String ESKI_MARKA;// >MSY</ESKI_MARKA>
        public String ISLEM_TARIHI;// >20160415</ISLEM_TARIHI>

        private static final NumberFormat nf = NumberFormat.getInstance(Globals.enLocale);

        public IEndeksler getEndeksler() throws Exception {
            IEndeksler endeksler = new Endeksler();

            if (OKUNAN_T1 != null && !OKUNAN_T1.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Gunduz, SayacHaneSayisi.H6, nf.parse(OKUNAN_T1).doubleValue()));
            if (OKUNAN_T2 != null && !OKUNAN_T2.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Puant, SayacHaneSayisi.H6, nf.parse(OKUNAN_T2).doubleValue()));
            if (OKUNAN_T3 != null && !OKUNAN_T3.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Gece, SayacHaneSayisi.H6, nf.parse(OKUNAN_T3).doubleValue()));
            if (OKUNAN_T4 != null && !OKUNAN_T4.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Enduktif, SayacHaneSayisi.H6, nf.parse(OKUNAN_T4).doubleValue()));
            if (OKUNAN_T5 != null && !OKUNAN_T5.isEmpty())
                endeksler.add(new Endeks(EndeksTipi.Kapasitif, SayacHaneSayisi.H6, nf.parse(OKUNAN_T5).doubleValue()));

            return endeksler;
        }

        private static final String[] propertyList = new String[]{"OKUNAN_T1", "OKUNAN_T2", "OKUNAN_T3", "OKUNAN_T4",
                "OKUNAN_T5", "ES_SAYAC", "ESKI_MARKA", "ISLEM_TARIHI"};

        public SayacSenkBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return OKUNAN_T1;
                case 1:
                    return OKUNAN_T2;
                case 2:
                    return OKUNAN_T3;
                case 3:
                    return OKUNAN_T4;
                case 4:
                    return OKUNAN_T5;
                case 5:
                    return ES_SAYAC;
                case 6:
                    return ESKI_MARKA;
                case 7:
                    return ISLEM_TARIHI;
            }

            return null;
        }

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    OKUNAN_T1 = (String) value;
                    return;
                case 1:
                    OKUNAN_T2 = (String) value;
                    return;
                case 2:
                    OKUNAN_T3 = (String) value;
                    return;
                case 3:
                    OKUNAN_T4 = (String) value;
                    return;
                case 4:
                    OKUNAN_T5 = (String) value;
                    return;
                case 5:
                    ES_SAYAC = (String) value;
                    return;
                case 6:
                    ESKI_MARKA = (String) value;
                    return;
                case 7:
                    ISLEM_TARIHI = (String) value;
                    return;
            }
        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

    }

    protected SayacSenkBilgi SayacSenkService(int okuyucuKodu, int seriNo, String sayacMarka) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodSayacSenkService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucuKodu", okuyucuKodu);
        request.addProperty("seriNo", seriNo);
        request.addProperty("sayacMarka", sayacMarka);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "SayacSenkServiceResult", Result.class);
        envelope.addMapping(NAMESPACE, "sayacBilgi", SayacSenkBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionSayacSenkService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return (SayacSenkBilgi) value.value;
        }

        return null;
    }

    public void SayacSenkService(int okuyucuKodu, int seriNo, String sayacMarka, Callback clb) throws Exception {

        Object obj = SayacSenkService(okuyucuKodu, seriNo, sayacMarka);
        if (clb != null)
            clb.run(obj);
    }

    public abstract void SayacSenkService(IForm form, final int okuyucuKodu, final int seriNo, final String sayacMarka,
                                          final Callback clb);

    // -------------------------------------------------------------------------
    // Abone durum koduna karşılık gelen açıklama listesi
    private static final String methodDurumKoduDataService = "DurumKoduDataService";
    private static final String actionDurumKoduDataService = NAMESPACE + methodDurumKoduDataService;

    public static class data_durumkodu extends SoapObject {

        public List<DurumKoduBilgi> list = new ArrayList<DurumKoduBilgi>();

        public data_durumkodu(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            return list.get(arg0);
        }

        @Override
        public int getPropertyCount() {
            return -1;
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.OBJECT_CLASS;
            info.name = "";
            return;

        }

        @Override
        public void setProperty(int index, Object value) {

            if (index < list.size())
                list.set(index, (DurumKoduBilgi) value);
            else if (index == list.size())
                list.add((DurumKoduBilgi) value);
        }
    }

    ;

    public static class DurumKoduBilgi extends SoapObject {

        public IIslemTipi ISLEM_TIPI;
        public IAdurum DURUM_KODU;
        public String SECENEK;

        public DurumKoduBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return ISLEM_TIPI;
                case 1:
                    return DURUM_KODU;
                case 2:
                    return SECENEK;
            }

            return null;
        }

        private static final String[] propertyList = new String[]{"ISLEM_TIPI", "DURUM_KODU", "SECENEK"};

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    ISLEM_TIPI = IslemTipi.fromInteger(Integer.parseInt((String) value));
                    return;
                case 1:
                    DURUM_KODU = new adurum(Integer.parseInt((String) value));
                    return;
                case 2:
                    SECENEK = (String) value;
                    return;
            }
        }

    }

    protected List<DurumKoduBilgi> DurumKoduDataService() throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodDurumKoduDataService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "DurumKoduDataServiceResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_durumkodu", ObjectList.class);
        envelope.addMapping(NAMESPACE, "DurumKoduBilgi", DurumKoduBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionDurumKoduDataService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue rv = (ReturnValue) result.ReturnValue;
            //data_durumkodu dd = (data_durumkodu) rv.value;
            ObjectList list = (ObjectList) rv.value;
//			return dd.list;
            return (List<DurumKoduBilgi>) (List<?>) list.list;
        }

        return null;
    }

    public abstract void DurumKoduDataService(Callback clb) throws Exception;

    // -------------------------------------------------------------------------
    // Sayaç değişiminde eski bir sayaç takılıyorsa endekslerini almak için.
    private static final String methodkacak_kayit = "kacak_kayit";
    private static final String actionkacak_kayit = NAMESPACE + methodkacak_kayit;

    // ><isemri_no>1</isemri_no><kayit_tarihi>120</kayit_tarihi><zabit_seri>A</zabit_seri><zabit_no>1</zabit_no><memurbir_sicil_no>1111</memurbir_sicil_no><memuriki_sicil_no>1111</memuriki_sicil_no></request></kacak_kayit>

    public String kacak_kayit(int isemri_no, Date kayit_tarihi, String zabit_seri, int zabit_no, int memurbir_sicil_no,
                              int memuriki_sicil_no) throws Exception {

        // Tarih dönüştürülmesi
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(kayit_tarihi);
        int ktarih = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100
                + calendar.get(Calendar.DAY_OF_MONTH);

        SoapObject request = new SoapObject(NAMESPACE, methodkacak_kayit);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("isemri_no", isemri_no);
        request.addProperty("kayit_tarihi", ktarih);
        request.addProperty("zabit_seri", zabit_seri);
        request.addProperty("zabit_no", zabit_no);
        request.addProperty("memurbir_sicil_no", memurbir_sicil_no);
        request.addProperty("memuriki_sicil_no", memuriki_sicil_no);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "kacak_kayitResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionkacak_kayit, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return (String) value.value;
        }

        return null;
    }

    public abstract void kacak_kayit(int isemri_no, Date kayit_tarihi, String zabit_seri, int zabit_no,
                                     int memurbir_sicil_no, int memuriki_sicil_no, Callback clb) throws Exception;

    // -------------------------------------------------------------------------



	/*
	MUHAMMED 28-11-2019
	RESİM EKLE SERVİSİ
	 */


//--------------------------------------------------------------------------------------------------------------------------------------------------------------


    public static class OkuyucuBilgi extends SoapObject implements IIslemYetki {

        private IIslemTipi ISLEM_TIPI;
        private int ALT_EMIR_TURU;

        public OkuyucuBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return ISLEM_TIPI;
                case 1:
                    return ALT_EMIR_TURU;
            }

            return null;
        }

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        private static final String[] propertyList = new String[]{"ISLEM_TIPI", "ALT_EMIR_TURU"};

        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    ISLEM_TIPI = IslemTipi.fromInteger(Integer.parseInt((String) value));
                    return;
                case 1:
                    ALT_EMIR_TURU = Integer.parseInt((String) value);
                    return;
            }
        }

        @Override
        public IIslemTipi getISLEM_TIPI() {
            // TODO Auto-generated method stub
            return ISLEM_TIPI;
        }

        @Override
        public int getALT_EMIR_TURU() {
            // TODO Auto-generated method stub
            return ALT_EMIR_TURU;
        }

        @Override
        public String toString() {
            if (ISLEM_TIPI == null)
                return "";
            if (!(Globals.app instanceof IMedasApplication))
                return "";
            IMedasApplication app = (IMedasApplication) Globals.app;
            AltEmirTuru[] list = app.getAltEmirTuru(ISLEM_TIPI);
            String turAdi = "";
            if (list != null) {
                for (AltEmirTuru tur : list) {
                    if (tur.altEmirTuru == ALT_EMIR_TURU) {
                        turAdi = tur.Tanim;
                        break;
                    }
                }
            }
            return String.format("%s - %s", ISLEM_TIPI.toString(), turAdi);
        }

    }

    private static final String methodendeksor_kullanici = "endeksor_kullanici";
    private static final String actionendeksor_kullanici = NAMESPACE + methodendeksor_kullanici;

    protected List<OkuyucuBilgi> endeksor_kullanici(int okuyucu_kodu) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodendeksor_kullanici);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucu_kodu", okuyucu_kodu);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "endeksor_kullaniciResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_endeksor", ObjectList.class);
        envelope.addMapping(NAMESPACE, "okuyucuBilgi", OkuyucuBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionendeksor_kullanici, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            ObjectList list = (ObjectList) value.value;
            return (List<OkuyucuBilgi>) (List<?>) list.list;
        }

        return null;
    }

    public abstract void endeksor_kullanici(int okuyucu_kodu, Callback clb) throws Exception;

    // -------------------------------------------------------------------------

    public static class muhurBilgi extends SoapObject {

        private String MUHUR_SERI;
        private Integer MUHUR_NO;
        private Date ZIMMET_TARIHI;

        public muhurBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public String toString() {

            if (MUHUR_SERI == null) return "";
            StringBuilder sb = new StringBuilder();
            sb.append(MUHUR_SERI.toUpperCase());
            if (MUHUR_NO != null) {
                sb.append(" / ");
                sb.append(MUHUR_NO.toString());
            }
            if (ZIMMET_TARIHI != null) {
                sb.append("  ");
                sb.append(df.format(ZIMMET_TARIHI));
            }
            return sb.toString();
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return MUHUR_SERI;
                case 1:
                    return MUHUR_NO;
                case 2:
                    return ZIMMET_TARIHI;

            }

            return null;
        }

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        private static final String[] propertyList = new String[]{"MUHUR_SERI", "MUHUR_NO", "ZIMMET_TARIHI"};

        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    MUHUR_SERI = value.toString();
                    return;
                case 1:
                    try {
                        MUHUR_NO = Integer.parseInt(value.toString());
                    } catch (Exception e) {

                    }
                    return;
                case 2:
                    try {
                        ZIMMET_TARIHI = dateTime.parse(value.toString());
                    } catch (Exception e) {
                    }
                    return;

            }
        }

    }

    //-------------------------------------------------------------------------

    private static final String methodendeksor_muhur = "endeksor_muhur";
    private static final String actionendeksor_muhur = NAMESPACE + methodendeksor_muhur;

    protected List<muhurBilgi> endeksor_muhur(int okuyucu_kodu) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodendeksor_muhur);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucu_kodu", okuyucu_kodu);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "endeksor_muhurResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_endeksor", ObjectList.class);
        envelope.addMapping(NAMESPACE, "muhurBilgi", muhurBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionendeksor_muhur, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;

            if (value.value instanceof ObjectList) {
                ObjectList list = (ObjectList) value.value;
                return (List<muhurBilgi>) (List<?>) list.list;
            } else {
                List<muhurBilgi> list = new ArrayList<muhurBilgi>();
                if (value.value instanceof zabitBilgi) list.add((muhurBilgi) value.value);
                return list;
            }
        }

        return null;
    }

    public abstract void endeksor_muhur(int okuyucu_kodu, Callback clb) throws Exception;

    // -------------------------------------------------------------------------

    private static final String methodMuhurZimmetService = "MuhurZimmetService";
    private static final String actionMuhurZimmetService = NAMESPACE + methodMuhurZimmetService;

    protected List<muhurBilgi> MuhurZimmetService(int okuyucu_kodu) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodMuhurZimmetService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucuKodu", okuyucu_kodu);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "MuhurZimmetServiceResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_muhur", ObjectList.class);
        envelope.addMapping(NAMESPACE, "muhurBilgi", muhurBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionMuhurZimmetService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;

            if (value.value instanceof ObjectList) {
                ObjectList list = (ObjectList) value.value;
                return (List<muhurBilgi>) (List<?>) list.list;
            } else {
                List<muhurBilgi> list = new ArrayList<muhurBilgi>();
                if (value.value instanceof zabitBilgi) list.add((muhurBilgi) value.value);
                return list;
            }
        }

        return null;
    }

    public void MuhurZimmetService(IApplication app, IForm form, final int okuyucu_kodu, Callback clb) throws Exception {
        app.runAsync(form, "Alınıyor...", "", null, new Callback() {
            public Object run(Object obj) {
                try {
                    return MuhurZimmetService(okuyucu_kodu);
                } catch (Exception e) {
                    return e;
                }
            }
        }, clb);
    }
    // -------------------------------------------------------------------------

    public static class zabitBilgi extends SoapObject {

        private String ZABIT_SERI;
        private Integer ZABIT_NO;
        private Date ZIMMET_TARIHI;

        public zabitBilgi(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public String toString() {

            if (ZABIT_SERI == null) return "";
            StringBuilder sb = new StringBuilder();
            sb.append(ZABIT_SERI.toUpperCase());
            if (ZABIT_NO != null) {
                sb.append(" / ");
                sb.append(ZABIT_NO.toString());
            }
            if (ZIMMET_TARIHI != null) {
                sb.append("  ");
                sb.append(df.format(ZIMMET_TARIHI));
            }
            return sb.toString();
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return ZABIT_SERI;
                case 1:
                    return ZABIT_NO;
                case 2:
                    return ZIMMET_TARIHI;
            }

            return null;
        }

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        private static final String[] propertyList = new String[]{"ZABIT_SERI", "ZABIT_NO", "ZIMMET_TARIHI"};

        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    ZABIT_SERI = value.toString();
                    return;
                case 1:
                    try {
                        ZABIT_NO = Integer.parseInt(value.toString());
                    } catch (Exception e) {

                    }
                    return;
                case 2:
                    try {
                        ZIMMET_TARIHI = dateTime.parse(value.toString());
                    } catch (Exception e) {
                    }
                    return;

            }
        }

    }

    //-------------------------------------------------------------------------

    private static final String methodendeksor_zabit = "endeksor_zabit";
    private static final String actionendeksor_zabit = NAMESPACE + methodendeksor_zabit;

    protected List<zabitBilgi> endeksor_zabit(int okuyucu_kodu) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodendeksor_zabit);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucu_kodu", okuyucu_kodu);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "endeksor_zabitResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_zabit", ObjectList.class);
        envelope.addMapping(NAMESPACE, "zabitBilgi", zabitBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionendeksor_zabit, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            if (value.value instanceof ObjectList) {
                ObjectList list = (ObjectList) value.value;
                return (List<zabitBilgi>) (List<?>) list.list;
            } else {
                List<zabitBilgi> list = new ArrayList<zabitBilgi>();
                if (value.value instanceof zabitBilgi) list.add((zabitBilgi) value.value);
                return list;
            }
        }

        return null;
    }


    public abstract void endeksor_zabit(int okuyucu_kodu, Callback clb) throws Exception;


    //-------------------------------------------------------------------------

    private static final String methodZabitZimmetService = "ZabitZimmetService";
    private static final String actionZabitZimmetService = NAMESPACE + methodZabitZimmetService;

    protected List<zabitBilgi> ZabitZimmetService(int okuyucu_kodu) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodZabitZimmetService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("okuyucuKodu", okuyucu_kodu);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "ZabitZimmetServiceResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_zabit", ObjectList.class);
        envelope.addMapping(NAMESPACE, "zabitBilgi", zabitBilgi.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionZabitZimmetService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;

            if (value.value instanceof ObjectList) {
                ObjectList list = (ObjectList) value.value;
                return (List<zabitBilgi>) (List<?>) list.list;
            } else {
                List<zabitBilgi> list = new ArrayList<zabitBilgi>();
                if (value.value instanceof zabitBilgi) list.add((zabitBilgi) value.value);
                return list;
            }
        }

        return null;
    }

    public void ZabitZimmetService(IApplication app, IForm form, final int okuyucu_kodu, Callback clb) throws Exception {
        app.runAsync(form, "Alınıyor...", "", null, new Callback() {
            public Object run(Object obj) {
                try {
                    return ZabitZimmetService(okuyucu_kodu);
                } catch (Exception e) {
                    return e;
                }
            }
        }, clb);
    }

    //-------------------------------------------------------------------------


    private static final String NAMESPACE2 = "http://tempuri.org/";
    private static final String URL2 = "http://MobilResim/SayacZimmetBilgi.asmx";
    private int SOAP_VER2 = SoapEnvelope.VER11;
    private static final boolean dotNet2 = true;

    protected String kadi2 = "dusoft";
    protected String sifre2 = "Sayac!Sor123";

    private static final String methodResimekle = "Resimekle";
    private static final String actionResimekle = NAMESPACE2 + methodResimekle;


    public String Resimekle(int isemri_no, String DosyaListesi, String DosyaAdi) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE2, methodResimekle);
        request.addProperty("kadi", kadi2);
        request.addProperty("sifre", sifre2);
        request.addProperty("isemrino", isemri_no);
        request.addProperty("DosyaListesi", DosyaListesi);
        request.addProperty("DosyaAdi", DosyaAdi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER2);
        envelope.dotNet = dotNet2;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE2, "ResimekleResult", Result.class);


        HttpTransportSE httpTransport = new HttpTransportSE(URL2);
        httpTransport.debug = true;
        httpTransport.call(actionResimekle, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }
        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            if (result.ReturnCode.equals("Success")) {
                return result.ReturnCode;
            }
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return (String) value.value;
        }


        return null;
    }


    public abstract void Resimekle(int isemri_no, String DosyaListesi, String DosyaAdi, Callback clb) throws Exception;

    // Muhammed Gökkaya
    // Soru Senaryo Servisi

    private static final String methodSoruSenaryoService = "SoruSenaryoService";
    private static final String actionSoruSenaryoService = NAMESPACE + methodSoruSenaryoService;


    public String SoruSenaryoService() throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodSoruSenaryoService);
        request.addProperty("kadi", kadi2);
        request.addProperty("sifre", sifre2);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER2);
        envelope.dotNet = dotNet2;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "SoruSenaryoServiceResult", Result.class);


        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        httpTransport.call(actionSoruSenaryoService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }
        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            if (result.ReturnCode.equals("Error")) {
                return null;
            } else {
                return result.ReturnCode;
            }
            //String value = result.ReturnValue.toString();
            //return value;
            //return (String) value.value;
        }


        return null;
    }

    public abstract void SoruSenaryoService(Callback clb) throws Exception;


    // H.Elif
    //Olcu Kontrol Servisi Güncel
    private static final String methodOlcuKontrolService = "OlcuKontrolService";
    private static final String actionOlcuKontrolService = NAMESPACE + methodOlcuKontrolService;


    public String OlcuKontrolService() throws Exception {
        OlcuDevreForm odf = new OlcuDevreForm();
        SoapObject request = new SoapObject(NAMESPACE, methodOlcuKontrolService);
        request.addProperty("kadi", odf.getkadi());
        request.addProperty("sifre", odf.getsifre());
        request.addProperty("tesisat_no", odf.gettesisat_no());
        request.addProperty("isemri_no", odf.getisemri_no());
        request.addProperty("tesis_enerji_dur", odf.gettesis_enerji_dur());
        request.addProperty("pano_muhur_dur", odf.getpano_muhur_dur());
        request.addProperty("sayac_muhur_dur", odf.getsayac_muhur_dur());
        request.addProperty("sayac_ariza_dur", odf.getsayac_ariza_dur());
        request.addProperty("AT_1_Marka", odf.getAT_1_Marka());
        request.addProperty("AT_1_Oran", odf.getAT_1_Oran());
        request.addProperty("AT_1_gucu", odf.getAT_1_gucu());
        request.addProperty("at_1_sinif", odf.getat_1_sinif());
        request.addProperty("at_1_seri", odf.getat_1_seri());
        request.addProperty("at_1_imalyili", odf.getat_1_imalyili());
        request.addProperty("AT_2_Marka", odf.getAT_2_Marka());
        request.addProperty("AT_2_Oran", odf.getAT_2_Oran());
        request.addProperty("AT_2_gucu", odf.getAT_2_gucu());
        request.addProperty("at_2_sinif", odf.getat_2_sinif());
        request.addProperty("at_2_seri", odf.getat_2_seri());
        request.addProperty("at_2_imalyili", odf.getat_2_imalyili());
        request.addProperty("AT_3_Marka", odf.getAT_3_Marka());
        request.addProperty("AT_3_Oran", odf.getAT_3_Oran());
        request.addProperty("AT_3_gucu", odf.getAT_3_gucu());
        request.addProperty("at_3_sinif", odf.getat_3_sinif());
        request.addProperty("at_3_seri", odf.getat_3_seri());
        request.addProperty("at_3_imalyili", odf.getat_3_imalyili());
        request.addProperty("gt_1_Marka", odf.getgt_1_Marka());
        request.addProperty("gt_1_Oran", odf.getgt_1_Oran());
        request.addProperty("gt_1_gucu", odf.getgt_1_gucu());
        request.addProperty("gt_1_sinif", odf.getgt_1_sinif());
        request.addProperty("gt_1_seri", odf.getgt_1_seri());
        request.addProperty("gt_1_imalyili", odf.getgt_1_imalyili());
        request.addProperty("gt_2_Marka", odf.getgt_2_Marka());
        request.addProperty("gt_2_Oran", odf.getgt_2_Oran());
        request.addProperty("gt_2_gucu", odf.getgt_2_gucu());
        request.addProperty("gt_2_sinif", odf.getgt_2_sinif());
        request.addProperty("gt_2_seri", odf.getgt_2_seri());
        request.addProperty("gt_2_imalyili", odf.getgt_2_imalyili());
        request.addProperty("gt_3_Marka", odf.getgt_3_Marka());
        request.addProperty("gt_3_Oran", odf.getgt_3_Oran());
        request.addProperty("gt_3_gucu", odf.getgt_3_gucu());
        request.addProperty("gt_3_sinif", odf.getgt_3_sinif());
        request.addProperty("gt_3_seri", odf.getgt_3_seri());
        request.addProperty("gt_3_imalyili", odf.getgt_3_imalyili());
        request.addProperty("primer_akim_1", odf.getprimer_akim_1());
        request.addProperty("sekonde_akim_1", odf.getsekonde_akim_1());
        request.addProperty("sayac_akim_1", odf.getsayac_akim_1());
        request.addProperty("primer_akim_2", odf.getprimer_akim_2());
        request.addProperty("sekonde_akim_2", odf.getsekonde_akim_2());
        request.addProperty("sayac_akim_2", odf.getsayac_akim_2());
        request.addProperty("primer_akim_3", odf.getprimer_akim_3());
        request.addProperty("sekonde_akim_3", odf.getsekonde_akim_3());
        request.addProperty("sayac_akim_3", odf.getsayac_akim_3());
        request.addProperty("sayac_gerili_1", odf.getsayac_gerili_1());
        request.addProperty("sayac_gerili_2", odf.getsayac_gerili_2());
        request.addProperty("sayac_gerili_3", odf.getsayac_gerili_3());
        request.addProperty("son_primer_akim_1", odf.getson_primer_akim_1());
        request.addProperty("son_sekonde_akim_1", odf.getson_sekonde_akim_1());
        request.addProperty("son_sayac_akim_1", odf.getson_sayac_akim_1());
        request.addProperty("son_primer_akim_2", odf.getson_primer_akim_2());
        request.addProperty("son_sekonde_akim_2", odf.getson_sekonde_akim_2());
        request.addProperty("son_sayac_akim_2", odf.getson_sayac_akim_2());
        request.addProperty("son_primer_akim_3", odf.getson_primer_akim_3());
        request.addProperty("son_sekonde_akim_3", odf.getson_sekonde_akim_3());
        request.addProperty("son_sayac_akim_3", odf.getson_sayac_akim_3());
        request.addProperty("son_sayac_gerili_1", odf.getson_sayac_gerili_1());
        request.addProperty("son_sayac_gerili_2", odf.getson_sayac_gerili_2());
        request.addProperty("son_sayac_gerili_3", odf.getson_sayac_gerili_3());
        request.addProperty("teyit_dur", odf.getteyit_dur());
        request.addProperty("carpan", odf.getcarpan());

        request.addProperty("unvan", odf.get_unvan());
        request.addProperty("adres", odf.get_adres());
        request.addProperty("sayac_no", odf.get_sayac_no());
        request.addProperty("sayac_marka", odf.get_sayac_marka());
        request.addProperty("faz_sayisi", odf.get_faz());
        request.addProperty("amperaj", odf.get_amperaj());
        request.addProperty("voltaj", odf.get_voltaj());
        request.addProperty("dijit", odf.get_hane_sayisi());
        request.addProperty("imal_yili", odf.get_imal_yili());
        request.addProperty("aciklama", odf.get_aciklama());
        request.addProperty("ihbar_dur", odf.get_ihbar_dur());
        request.addProperty("tc_kimlik_no", odf.get_tcNo());
        request.addProperty("telefon_no", odf.get_telNo());
        request.addProperty("eposta", odf.get_eposta());
        request.addProperty("polarite_1", odf.get_polarite_1());
        request.addProperty("polarite_2", odf.get_polarite_2());
        request.addProperty("polarite_3", odf.get_polarite_3());
        request.addProperty("son_polarite_1", odf.getson_polarite_1());
        request.addProperty("son_polarite_2", odf.getson_polarite_2());
        request.addProperty("son_polarite_3", odf.getson_polarite_3());
        request.addProperty("guc_trafosu_marka", odf.get_trafoMarkasi());
        request.addProperty("guc_trafosu_seri_no", odf.get_trafoSerino());
        request.addProperty("guc_trafosu_gucu", odf.get_trafoGucu());
        request.addProperty("guc_trafosu_gerilim", odf.get_trafoGerilimi());
        request.addProperty("guc_trafosu_imal_yili", odf.get_trafoImalYili());


        List<GucTespit> odfGucTespit = odf.get_guc_tespit_list();
        SoapObject guc_tespit_list_header = new SoapObject(null, "guc_tespit_list");
        for (int i = 0; i < odfGucTespit.size(); i++) {
            SoapObject olcu_tespit_header = new SoapObject(null, "OlcuTespit");
            olcu_tespit_header.addProperty("cinsi", odfGucTespit.get(i).get_cinsi());
            olcu_tespit_header.addProperty("adet", odfGucTespit.get(i).get_adet());
            olcu_tespit_header.addProperty("guc", odfGucTespit.get(i).get_guc());
            guc_tespit_list_header.addSoapObject(olcu_tespit_header);
        }

        request.addSoapObject(guc_tespit_list_header);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "OlcuKontrolServiceResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionOlcuKontrolService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return result.ReturnCode;
        }

        return null;
    }

    public abstract void OlcuKontrolService(Callback clb) throws Exception;


// H.Elif
    //Olcu işemri Kontrol Servisi iş emri no db de var mı kontrolü
    private static final String methodOlcuIsemriKontrolService = "OlcuIsemriKontrolService";
    private static final String actionOlcuIsemriKontrolService = NAMESPACE + methodOlcuIsemriKontrolService;


    public String OlcuIsemriKontrolService() throws Exception {
        OlcuDevreForm odf = new OlcuDevreForm();
        SoapObject request = new SoapObject(NAMESPACE, methodOlcuIsemriKontrolService);
        request.addProperty("kadi", odf.getkadi());
        request.addProperty("sifre", odf.getsifre());
        request.addProperty("tesisat_no", odf.gettesisat_no());
        request.addProperty("isemri_no", odf.getisemri_no());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "OlcuIsemriKontrolServiceResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionOlcuIsemriKontrolService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return result.ReturnCode;
        }

        return null;
    }
    public abstract void OlcuIsemriKontrolService(Callback clb) throws Exception;




    private static final String methodEndeksorKapatmaService = "EndeksorKapatmaService";
    private static final String actionEndeksorKapatmaService = NAMESPACE + methodEndeksorKapatmaService;

    public String EndeksorKapatmaService(int tesisat_no, int isemri_no) throws Exception {
        SoapObject request = new SoapObject(NAMESPACE, methodEndeksorKapatmaService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("tesisat_no", tesisat_no);
        request.addProperty("isemri_no", isemri_no);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "EndeksorKapatmaServiceResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionEndeksorKapatmaService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return result.ReturnCode;
        }

        return null;
    }

    public abstract void EndeksorKapatmaService(int tesisat_no, int isemri_no, Callback clb) throws Exception;


    //Muhammed Gökkaya
    //yazici kontrol service
    private static final String methodYaziciKontrolService = "YaziciKontrolService";
    private static final String actionYaziciKontrolService = NAMESPACE + methodYaziciKontrolService;


    public String YaziciKontrolService(int tesisat_no, int isemri_no, int gonderme_dur, int yazdirma_dur, String hata_aciklama) throws Exception {
        SoapObject request = new SoapObject(NAMESPACE, methodYaziciKontrolService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("tesisat_no", tesisat_no);
        request.addProperty("isemri_no", isemri_no);
        request.addProperty("gonderme_dur", gonderme_dur);
        request.addProperty("yazdirma_dur", yazdirma_dur);
        request.addProperty("hata_aciklama", hata_aciklama);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "YaziciKontrolServiceResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionYaziciKontrolService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return result.ReturnCode;
        }

        return null;
    }

    public abstract void YaziciKontrolService(int tesisat_no, int isemri_no, int gonderme_dur, int yazdirma_dur, String hata_aciklama, Callback clb) throws Exception;

    //Yük profili servisi tamamlandı.
    private static final String methodYkpEndeksService = "YkpEndeksService";
    private static final String actionYkpEndeksService = NAMESPACE + methodYkpEndeksService;


    public String YkpEndeksService() throws Exception {
        YkpEndeksDoldur ykp = new YkpEndeksDoldur();
        SoapObject request = new SoapObject(NAMESPACE, methodYkpEndeksService);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("tesisat_no", ykp.getTesisat_no());
        request.addProperty("isemri_no", ykp.getIsemri_no());
        request.addProperty("endeks_tarihi", ykp.getEndeks_tarihi());
        request.addProperty("endeks_saati", ykp.getEndeks_saati());
        request.addProperty("Aktif", ykp.getAktif());
        if (ykp.getEnduktif() != null)
            request.addProperty("Enduktif", ykp.getEnduktif());
        if (ykp.getKapasitif() != null)
            request.addProperty("Kapasitif", ykp.getKapasitif());
        if (ykp.getAkimL1() != null)
            request.addProperty("AkimL1", ykp.getAkimL1());
        if (ykp.getAkimL2() != null)
            request.addProperty("AkimL2", ykp.getAkimL2());
        if (ykp.getAkimL3() != null)
            request.addProperty("AkimL3", ykp.getAkimL3());
        if (ykp.getGerilimL1() != null)
            request.addProperty("GerilimL1", ykp.getGerilimL1());
        if (ykp.getGerilimL2() != null)
            request.addProperty("GerilimL2", ykp.getGerilimL2());
        if (ykp.getGerilimL3() != null)
            request.addProperty("GerilimL3", ykp.getGerilimL3());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "YkpEndeksServiceResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionYkpEndeksService, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            return result.ReturnCode;
        }

        return null;
    }

    public abstract void YkpEndeksService(Callback clb) throws Exception;


    public static class GidilmeyenKarneListe extends SoapObject {

        private int TESISAT_NO;

        public GidilmeyenKarneListe(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return TESISAT_NO;
            }

            return null;
        }

        @Override
        public int getPropertyCount() {
            return propertyList.length;
        }

        private static final String[] propertyList = new String[]{"TESISAT_NO"};

        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

        @Override
        public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {

            info.type = PropertyInfo.STRING_CLASS;
            info.name = propertyList[index];

        }

        @Override
        public void setProperty(int index, Object value) {

            switch (index) {
                case 0:
                    TESISAT_NO = Integer.parseInt((String) value);
                    return;
            }
        }

        public int getTESISAT_NO() {
            // TODO Auto-generated method stub
            return TESISAT_NO;
        }
    }

    private static final String methodGidilmeyenKarne = "GidilmeyenKarne";
    private static final String actionGidilmeyenKarne = NAMESPACE + methodGidilmeyenKarne;


    public List<GidilmeyenKarneListe> GidilmeyenKarne(int karne_no) throws Exception {
        SoapObject request = new SoapObject(NAMESPACE, methodGidilmeyenKarne);
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("karne_no", karne_no);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "GidilmeyenKarneResult", Result.class);
        envelope.addMapping(NAMESPACE, "data_Karne2", ObjectList.class);
        envelope.addMapping(NAMESPACE, "data_Karne", GidilmeyenKarneListe.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionGidilmeyenKarne, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof Result) {
            Result result = (Result) envelope.bodyIn;
            //ResultSet value= (ResultSet) result.ReturnValue;
            ReturnValue value = (ReturnValue) result.ReturnValue;
            ObjectList list = (ObjectList) value.value;
            if (list != null)
                return (List<GidilmeyenKarneListe>) (List<?>) list.list;
        }

        return null;
    }

    public abstract void GidilmeyenKarne(int karne_no, Callback clb) throws Exception;

    private static final String NAMESPACEEndeksorFoto = "http://tempuri.org/";
    private static final String URLEndeksorFoto = "http://AndSvis/AndridSer.asmx?wsdl";
    private static final String methodAddPhoto = "AddPhoto";
    private static final String actionAddPhoto = NAMESPACEEndeksorFoto + methodAddPhoto;


    public String AddPhoto(int tesisat_no, String aciklama, String dosyalistesi, String dosyaadi, int okumadurumu) throws Exception {
        SoapObject request = new SoapObject(NAMESPACEEndeksorFoto, methodAddPhoto);
        request.addProperty("KullaniciAdi", String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_KODU()));
        request.addProperty("Parola", String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_SIFRE()));
        request.addProperty("Tesisatno", String.valueOf(tesisat_no));
        request.addProperty("Aciklama", aciklama);
        request.addProperty("DosyaListesi", dosyalistesi);
        request.addProperty("DosyaAdi", dosyaadi);
        request.addProperty("OkumaDurumu", okumadurumu);
        request.addProperty("OkumaTarihi", "");
        request.addProperty("ApiVersiyon", "1.2");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = true;
        //envelope.setOutputSoapObject(request);
        envelope.setOutputSoapObject(request);
        envelope.bodyOut = envelope.bodyOut.toString().split("<request>")[0] + envelope.bodyOut.toString().split("<request>")[1].split("</request>")[0] + envelope.bodyOut.toString().split("<request>")[1].split("</request>")[1];
        //envelope.bodyOut = istek;
        //envelope.addMapping(NAMESPACEEndeksorFoto, "AddPhotoResult",SoapObject.class);
        //envelope =(SoapSerializationEnvelope)(Object) istek;

        HttpTransportSE httpTransport = new HttpTransportSE(URLEndeksorFoto);

        httpTransport.call(actionAddPhoto, envelope);
        String response = httpTransport.responseDump;
        if (response.split("<AddPhotoResult>")[1].split("</AddPhotoResult>")[0].equals("201")) {
            return "success";
        }

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }
        SoapObject result = (SoapObject) envelope.bodyIn;

		/*
		if (envelope.bodyIn instanceof Result) {
			Result result = (Result) envelope.bodyIn;
			//ResultSet value= (ResultSet) result.ReturnValue;
			ReturnValue value = (ReturnValue) result.ReturnValue;
			return "ok";
		}
		*/
        return "hata";
    }

    public abstract void AddPhoto(int tesisat_no, String aciklama, String dosyalistesi, String dosyaadi, int okumadurumu, Callback clb) throws Exception;

    private static final String methodOrtakTrafoKontrol = "OrtakTrafoKontrol";
    private static final String actionOrtakTrafoKontrol = NAMESPACEEndeksorFoto + methodOrtakTrafoKontrol;

    public String OrtakTrafoKontrol(int tesisat_no, int isemri_no) throws Exception {
        SoapObject request = new SoapObject(NAMESPACEEndeksorFoto, methodOrtakTrafoKontrol);
        request.addProperty("KullaniciAdi", String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_KODU()));
        request.addProperty("Parola", String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_SIFRE()));
        request.addProperty("Tesisatno", String.valueOf(tesisat_no));
        request.addProperty("Isemrino", String.valueOf(isemri_no));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = true;
        //envelope.setOutputSoapObject(request);
        envelope.setOutputSoapObject(request);
        envelope.bodyOut = envelope.bodyOut.toString().split("<request>")[0] + envelope.bodyOut.toString().split("<request>")[1].split("</request>")[0] + envelope.bodyOut.toString().split("<request>")[1].split("</request>")[1];
        //envelope.bodyOut = istek;
        //envelope.addMapping(NAMESPACEEndeksorFoto, "OrtakTrafoKontrolResult",SoapObject.class);
        //envelope =(SoapSerializationEnvelope)(Object) istek;

        HttpTransportSE httpTransport = new HttpTransportSE(URLEndeksorFoto);

        httpTransport.call(actionOrtakTrafoKontrol, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }
        SoapObject result = (SoapObject) envelope.bodyIn;
        String response = httpTransport.responseDump;
        return response.split("<OrtakTrafoKontrolResult>")[1].split("</OrtakTrafoKontrolResult>")[0];
    }

    public abstract void OrtakTrafoKontrol(int tesisat_no, int isemri_no, Callback clb) throws Exception;

    private static final String methodAddOrtakTrafo = "AddOrtakTrafo";
    private static final String actionAddOrtakTrafo = NAMESPACEEndeksorFoto + methodAddOrtakTrafo;

    public String AddOrtakTrafo(int tesisat_no, int isemri_no, String yenitesisat_no, int teyit_dur) throws Exception {
        SoapObject request = new SoapObject(NAMESPACEEndeksorFoto, methodAddOrtakTrafo);
        request.addProperty("KullaniciAdi", String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_KODU()));
        request.addProperty("Parola", String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_SIFRE()));
        request.addProperty("Tesisatno", String.valueOf(tesisat_no));
        request.addProperty("Isemrino", String.valueOf(isemri_no));
        request.addProperty("YeniTesisatNo", yenitesisat_no);
        request.addProperty("TeyitDur", teyit_dur);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = true;
        //envelope.setOutputSoapObject(request);
        envelope.setOutputSoapObject(request);
        envelope.bodyOut = envelope.bodyOut.toString().split("<request>")[0] + envelope.bodyOut.toString().split("<request>")[1].split("</request>")[0] + envelope.bodyOut.toString().split("<request>")[1].split("</request>")[1];
        //envelope.bodyOut = istek;
        //envelope.addMapping(NAMESPACEEndeksorFoto, "AddOrtakTrafoResult",SoapObject.class);
        //envelope =(SoapSerializationEnvelope)(Object) istek;

        HttpTransportSE httpTransport = new HttpTransportSE(URLEndeksorFoto);

        httpTransport.call(actionAddOrtakTrafo, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }
        SoapObject result = (SoapObject) envelope.bodyIn;
        String response = httpTransport.responseDump;
        return response.split("<AddOrtakTrafoResult>")[1].split("</AddOrtakTrafoResult>")[0];
    }

    public abstract void AddOrtakTrafo(int tesisat_no, int isemri_no, String yenitesisat_no, int teyit_dur, Callback clb) throws Exception;

    // -------------------------------------------------------------------------

//	EndoksorUnvansizKacak

    public boolean EndoksorUnvansizKacak(int isemri_no, int tesisat_no, int kayit_tarihi) throws Exception {
        SoapObject request = new SoapObject("http://tempuri.org/", "EndoksorUnvansizKacak");
        request.addProperty("kadi", kadi);
        request.addProperty("sifre", sifre);
        request.addProperty("ISEMRI_NO", isemri_no);
        request.addProperty("TESISAT_NO", tesisat_no);
        request.addProperty("KAYIT_TARIHI", kayit_tarihi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "EndoksorUnvansizKacakResult", Result.class);

        HttpTransportSE httpTransport = new HttpTransportSE("http://MaximoSayac/SayacZimmetBilgi.asmx");

        httpTransport.call("http://tempuri.org/" + "EndoksorUnvansizKacak", envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new MedasWsException(soapFault.getMessage());
        }
        Result result = (Result) envelope.bodyIn;
        if (result.ReturnCode.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public Result PushOtomasyonEmir(String AboneNo, int EskiAnten, int EskiModem, String EskiModemSeri, int EskiSim
            , String Ihbar_Tarihi, String KapanisAciklama, String Kapanma_Tarihi, String KapatanUnvan, int KapatanCode
            , int Order_Number, String SorunKaynak, String YapilanIslemOto
            , int YeniAnten, int YeniModem, String YeniModemSeri, int YeniSim) throws Exception {
        SoapObject request = new SoapObject("http://tempuri.org/", "PushOtomasyonEmir");
        request.addProperty("AppPass", "O82SX");
        request.addProperty("Appid", "OTS");
        request.addProperty("AboneNo", AboneNo);
        request.addProperty("EskiAnten", EskiAnten);
        request.addProperty("EskiModem", EskiModem);
        request.addProperty("EskiModemSeri", EskiModemSeri);
        request.addProperty("EskiSim", EskiSim);
        request.addProperty("Ihbar_Tarihi", Ihbar_Tarihi);
        request.addProperty("KapanisAciklama", KapanisAciklama);
        request.addProperty("Kapanma_Tarihi", Kapanma_Tarihi);
        request.addProperty("Order_Number", Order_Number);
        request.addProperty("SorunKaynak", SorunKaynak);
        request.addProperty("YapilanIslemOto", YapilanIslemOto);
        request.addProperty("YeniAnten", YeniAnten);
        request.addProperty("YeniModem", YeniModem);
        request.addProperty("YeniModemSeri", YeniModemSeri);
        request.addProperty("YeniSim", YeniSim);
        request.addProperty("KapatanCode", KapatanCode);
        request.addProperty("KapatanUnvan", KapatanUnvan);
//		request.addProperty("AboneNo",11400197);
//		request.addProperty("EskiAnten",1);
//		request.addProperty("EskiModem",1);
//		request.addProperty("EskiModemSeri",1);
//		request.addProperty("EskiSim",1);
//		request.addProperty("Ihbar_Tarihi","2021-07-16T15:00:00");
//		request.addProperty("KapanisAciklama","Test açıklama.");
//		request.addProperty("Kapanma_Tarihi","2021-07-16T15:00:00");
//		request.addProperty("Order_Number",11111);
//		request.addProperty("SorunKaynak",1);
//		request.addProperty("YapilanIslemOto",1);
//		request.addProperty("YeniAnten",1);
//		request.addProperty("YeniModem",1);
//		request.addProperty("YeniModemSeri",1);
//		request.addProperty("YeniSim",1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "PushOtomasyonEmirResult", Result.class);

        //Onur Osos Test İçin Eklendi
        HttpTransportSE httpTransport = new HttpTransportSE("http://TSKS/QBIOperation.asmx");

        httpTransport.call("http://tempuri.org/" + "PushOtomasyonEmir", envelope);
        Result result;
        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            result = new Result("copy", "copy");
            result.ReturnCode = "FALSE";
            result.ReturnMessage = soapFault.getMessage();
            return result;
        }
        result = (Result) envelope.bodyIn;
        return result;
    }


}

