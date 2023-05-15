package mobit.elec.medas.ws;

import com.mobit.IApplication;
import com.mobit.ICallback;
import com.mobit.IForm;
import com.mobit.IServer;
import com.mobit.MobitException;

import org.ksoap2.x.SoapEnvelope;
import org.ksoap2.x.SoapFault;
import org.ksoap2.x.serialization.PropertyInfo;
import org.ksoap2.x.serialization.SoapObject;
import org.ksoap2.x.serialization.SoapSerializationEnvelope;
import org.ksoap2.x.transport.HttpTransportSE;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Hashtable;

import mobit.eemr.IReadResult;
import mobit.eemr.ReadResult;
import mobit.elec.Globals;
import mobit.elec.osos.viko.OperationService;
import mobit.http.Result;

/**
 * Created by Genel on 17.09.2018.
 */

public class OsosServis implements IServer {


    public class OsosWsException extends MobitException {

        /**
         *
         */
        public Object result;
        private static final String s_caption = "Medaş OSOS Web Servis Hata";

        public OsosWsException(String message) {
            super(message, s_caption);
        }

        public OsosWsException(Object result) {
            super(result.toString(), s_caption);
            this.result = result;
        }

    }

    @Override
    public boolean isTest()
    {
        return false;
    }

    @Override
    public void init(IApplication app) throws Exception {

    }

    @Override
    public void close() {

    }

    @Override
    public void connect() throws Exception {

    }

    @Override
    public Date getUTCTime() {
        return null;
    }

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://meramedas.net/ososservis.asmx";
    private int SOAP_VER = SoapEnvelope.VER12;
    private static final boolean dotNet = false;
    private static final boolean debug = Globals.isDeveloping();

    protected String usercode = "Os0sUs3r";
    protected String pass = "OsosP4sw0rd";

    //-------------------------------------------------------------------------

    private static final String methodping = "ping";
    private static final String actionping = NAMESPACE + methodping;

    public static class pingResult extends SoapObject {


        public String sonuc;
        public String durum;

        public boolean isSuccessfull() {
            return durum != null && durum.equals("OK");
        }

        private static final NumberFormat nf = NumberFormat.getInstance(Globals.enLocale);

        private static final String[] propertyList = new String[]{"sonuc", "durum"};

        public pingResult(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return sonuc;
                case 1:
                    return durum;
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
                    sonuc = (String) value;
                    return;
                case 1:
                    durum = (String) value;
                    return;
            }
        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

    }

    public pingResult ping(String sayacno, String flag, String ip) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodping);
        request.addProperty("usercode", usercode);
        request.addProperty("pass", pass);
        request.addProperty("sayacno", sayacno);
        request.addProperty("flag", flag);
        request.addProperty("ip", ip);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "pingResult", pingResult.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionping, envelope);

        if (envelope.bodyIn instanceof SoapObject) {
            pingResult r = (pingResult) envelope.bodyIn;
            if (!r.isSuccessfull()) throw new OsosWsException(r.sonuc);
            return r;

        }

        throw new MobitException("ping sonucu alınamadı!");

    }

    //-------------------------------------------------------------------------

    private static final String methodModemDurumSor = "ModemDurumSor";
    private static final String actionModemDurumSor = NAMESPACE + methodModemDurumSor;

    public static class ModemDurumSorResult extends SoapObject {


        public String sonuc;
        public String durum;

        public boolean isSuccessfull() {
            return durum != null && durum.equals("OK");
        }

        private static final NumberFormat nf = NumberFormat.getInstance(Globals.enLocale);

        private static final String[] propertyList = new String[]{"sonuc", "durum"};

        public ModemDurumSorResult(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return sonuc;
                case 1:
                    return durum;
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
                    sonuc = (String) value;
                    return;
                case 1:
                    durum = (String) value;
                    return;
            }
        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

    }

    public ModemDurumSorResult ModemDurumSor(String sayacno, String flag, String ip) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodModemDurumSor);
        request.addProperty("usercode", usercode);
        request.addProperty("pass", pass);
        request.addProperty("sayacno", sayacno);
        request.addProperty("flag", flag);
        request.addProperty("ip", ip);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "ModemDurumSorResult", ModemDurumSorResult.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionModemDurumSor, envelope);

        if (envelope.bodyIn instanceof SoapObject) {
            ModemDurumSorResult r = (ModemDurumSorResult) envelope.bodyIn;
            if (!r.isSuccessfull()) throw new OsosWsException(r.sonuc);
            return r;
        }

        throw new MobitException("Modem durumu sorgulanamadı!");

    }

    //-------------------------------------------------------------------------

    private static final String methodKimlikSorgulama = "KimlikSorgulama";
    private static final String actionKimlikSorgulama = NAMESPACE + methodKimlikSorgulama;

    public static class KimlikSorgulamaResult extends SoapObject {


        public String sonuc;
        public String durum;

        public boolean isSuccessfull() {
            return durum != null && durum.equals("OK");
        }

        private static final NumberFormat nf = NumberFormat.getInstance(Globals.enLocale);

        private static final String[] propertyList = new String[]{"sonuc", "durum"};

        public KimlikSorgulamaResult(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return sonuc;
                case 1:
                    return durum;
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
                    sonuc = (String) value;
                    return;
                case 1:
                    durum = (String) value;
                    return;
            }
        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

    }


    public KimlikSorgulamaResult KimlikSorgulama(String sayacno, String flag, String ip) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodKimlikSorgulama);
        request.addProperty("usercode", usercode);
        request.addProperty("pass", pass);
        request.addProperty("sayacno", sayacno);
        request.addProperty("flag", flag);
        request.addProperty("ip", ip);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "KimlikSorgulamaResult", KimlikSorgulamaResult.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        httpTransport.call(actionKimlikSorgulama, envelope);

        if (envelope.bodyIn instanceof SoapFault) {
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            throw new OsosWsException(soapFault.getMessage());
        }

        if (envelope.bodyIn instanceof SoapObject) {
            KimlikSorgulamaResult r = (KimlikSorgulamaResult) envelope.bodyIn;
            if (!r.isSuccessfull()) throw new OsosWsException(r.sonuc);
            return r;
        }

        throw new MobitException("Sayaç kimlik sorgulanamadı!");

    }

    //-------------------------------------------------------------------------

    private static final String methodReadOutSorgula = "ReadOutSorgula";
    private static final String actionReadOutSorgula = NAMESPACE + methodReadOutSorgula;


    public static class ReadOutSorgulaResult extends SoapObject {


        public String sonuc;
        public String durum;

        public boolean isSuccessfull() {
            return durum != null && durum.equals("OK");
        }

        public String getObisResult() {
            return sonuc.substring(sonuc.indexOf("<"));
        }

        private static final NumberFormat nf = NumberFormat.getInstance(Globals.enLocale);

        private static final String[] propertyList = new String[]{"sonuc", "durum"};

        public ReadOutSorgulaResult(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return sonuc;
                case 1:
                    return durum;
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
                    sonuc = (String) value;
                    return;
                case 1:
                    durum = (String) value;
                    return;
            }
        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

    }

    public ReadOutSorgulaResult ReadOutSorgula(String sayacno, String flag, String ip) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, methodReadOutSorgula);
        request.addProperty("usercode", usercode);
        request.addProperty("pass", pass);
        request.addProperty("sayacno", sayacno);
        request.addProperty("flag", flag);
        request.addProperty("ip", ip);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = dotNet;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "ReadOutSorgulaResult", ReadOutSorgulaResult.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL, 5 * 60 * 1000);

        httpTransport.call(actionReadOutSorgula, envelope);

        if (envelope.bodyIn instanceof SoapObject) {
            ReadOutSorgulaResult r = (ReadOutSorgulaResult) envelope.bodyIn;
            if (!r.isSuccessfull() || r.sonuc == null) throw new OsosWsException(r.sonuc);
            return r;
        }

        throw new MobitException("Sayaç endeks alınamadı!");

    }


    //-------------------------------------------------------------------------

    private static final String methodOkumaTestYap = "OkumaTestYap";
    private static final String actionOkumaTestYap = NAMESPACE + methodOkumaTestYap;

    public static class IstekRequest {
        String sayacno;
        String flag;
        String ip;
        String tesisatno;
        String ceptelno;
        String ceptelid;
        String modemno;

    }

    public static class OkumaTestYapResult extends SoapObject {


        public String sonuc;
        public String durum;
        public String tesisatno;
        public String modemserino;
        public String modemmarka;
        public String sim_ip;
        public String sim_no;

        public String toString() {
            return String.format("Tesisat No: %s\nModem serino: %s\nModem marka: %s\nSIM IP: %s\n SIM No: %s",
                    tesisatno, modemserino, modemmarka, sim_ip, sim_no);

        }

        public boolean isSuccessfull() {
            return durum != null && durum.equals("OK");
        }

        private static final String[] propertyList = new String[]{"sonuc", "durum",
                "tesisatno", "modemserino", "modemmarka", "sim_ip", "sim_no"};


        public OkumaTestYapResult(String namespace, String name) {
            super(namespace, name);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getProperty(int arg0) {

            switch (arg0) {
                case 0:
                    return sonuc;
                case 1:
                    return durum;
                case 2:
                    return tesisatno;
                case 3:
                    return modemserino;
                case 4:
                    return modemmarka;
                case 5:
                    return sim_ip;
                case 6:
                    return sim_no;
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
                    sonuc = (String) value;
                    return;
                case 1:
                    durum = (String) value;
                    return;
                case 2:
                    tesisatno = (String)value;
                    return;
                case 3:
                    modemserino = (String)value;
                    return;
                case 4:
                    modemmarka = (String)value;
                    return;
                case 5:
                    sim_ip = (String)value;
                    return;
                case 6:
                    sim_no = (String)value;
                    return;

            }
        }

        @Override
        public int getPropertyIndex(String propertyName) {
            return getPropertyIndex(propertyList, propertyName);
        }

    }

    public Object OkumaTestYap(String sayacno, String flag, String ip, String tesisatno, String ceptelno, String ceptelid, String modemno) throws Exception {

        SoapObject method = new SoapObject(NAMESPACE, methodOkumaTestYap);
        SoapObject request = new SoapObject("", "iq");
        request.addProperty("usercode", usercode);
        request.addProperty("pass", pass);
        request.addProperty("sayacno", sayacno);
        request.addProperty("flag", flag);
        request.addProperty("ip", ip);
        request.addProperty("tesisatno", tesisatno);
        request.addProperty("ceptelno", ceptelno);
        request.addProperty("ceptelid", ceptelid);
        request.addProperty("modemno", modemno);
        method.addProperty("iq", request);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VER);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(method);
        envelope.addMapping(NAMESPACE, "OkumaTestYapResult", OkumaTestYapResult.class);

        HttpTransportSE httpTransport = new HttpTransportSE(URL, 3 * 60 * 1000);

        httpTransport.call(actionOkumaTestYap, envelope);

        if (envelope.bodyIn instanceof SoapObject) {
            OkumaTestYapResult r = (OkumaTestYapResult) envelope.bodyIn;
            if (/*true*/!r.isSuccessfull()) return r;
            IReadResult rr = ReadResult.fromString(r.sonuc);
            return rr;
        }

        throw new MobitException("Sayaç endeks alınamadı!");

    }

    //-------------------------------------------------------------------------

    public void OsosAktivasyon(final IApplication app, IForm form, final String sayac_no, final String flag, final String ceptelno, final String ceptelid, com.mobit.Callback clb) {
        app.runAsync(form, "OSOS okuma yapılıyor. 3 dk kadar sürebilir. Lütfen bekleyin...", "", null, new com.mobit.Callback() {

            @Override
            public Object run(Object obj) {
                try {

                    return OkumaTestYap(sayac_no, flag, "", "", ceptelno, ceptelid, "");


                } catch (Exception e) {
                    return e;
                }
            }
        }, clb);
    }
}

/*
1000020
1000024
1000081
1000088
1000103
1000106
1000121
1000134
1000207
1000213
1000215
*/