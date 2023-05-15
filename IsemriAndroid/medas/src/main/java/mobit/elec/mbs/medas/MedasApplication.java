package mobit.elec.mbs.medas;


import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IEleman;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.ILogin;
import com.mobit.IPlatform;
import com.mobit.IServer;
import com.mobit.Item;
import com.mobit.LoginParam;
import com.mobit.MobitException;
import com.mobit.Operation;
import com.mobit.PageData;
import com.mobit.PrintItem;
import com.mobit.Soti;


import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.AltEmirTuru;
import mobit.elec.IAdurum;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIslemYetki;
import mobit.elec.IMuhurKod;
import mobit.elec.ISayacMarka;
import mobit.elec.ISdurum;
import mobit.elec.ITesisat;
import mobit.elec.IYetki;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IIslemMaster;
import mobit.elec.mbs.MbsApplication;
import mobit.elec.mbs.MbsException;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.muhur_kod;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.get.sdurum;
import mobit.elec.medas.ws.OsosServis;
import mobit.elec.medas.ws.SayacZimmetBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.DurumKoduBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.muhurBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.zabitBilgi;
import mobit.elec.osos.viko.IOperationService;
import mobit.elec.osos.viko.OperationService;

/*
	Testlerde kullanılan tesisat : 1386981

 */
public abstract class MedasApplication extends MbsApplication implements IMedasApplication {

    IApplication app = Globals.app;
    IIsemri2 isemri2;

    protected IForm form = null;
    private List<DurumKoduBilgi> aboneDurumAciklama = new ArrayList<DurumKoduBilgi>();
    private List<muhurBilgi> muhurSeriler = new ArrayList<muhurBilgi>();
    private List<zabitBilgi> zabitSeriler = new ArrayList<zabitBilgi>();
    public static String Senaryo;
    public static String durum_Kodu="";

    protected IServer medasServer;
    protected IOperationService ososServer;
    protected IServer medasOsosServer;

    public MedasApplication() throws Exception {
        super();
        // TODO Auto-generated constructor stub
        ososServer = new OperationService();
        medasOsosServer = new OsosServis();
        param.captionColor = "#1566E0";
        dbVersion = 2;
        appPath = "medas";
        dbUser = "";
        dbPassword = "";

    }

    @Override
    public void init(IPlatform platform, Object obj) throws Exception {

        super.init(platform, obj);

        // if (Globals.isDeveloping())
        setMainMenuItems(IDef.karisikMenuItems);


    }

    @Override
    public void initForm(IForm form) {
        super.initForm(form, param);

    }

    @Override
    public List<muhurBilgi> getMuhurSeriler() {
        if (muhurSeriler.isEmpty()) {
            muhurBilgi b = new muhurBilgi("dd", "MUHUR_SERI");
            b.setProperty(0
                    , "N");
            muhurSeriler.add(b);
        }
        return muhurSeriler;
    }

    @Override
    public List<zabitBilgi> getZabitSeriler() {
        return zabitSeriler;
    }

    @Override
    public List<ITesisat> getTesisat2(Integer SAYAC_NO) throws Exception {
        // TODO Auto-generated method stub
        throw new MobitException("Hatalı");

    }

    @Override
    public String[] getSettingFiles() {
        return new String[]{"settings.xml", "acma_form.xml", "ihbar_form.xml", "kesme_form2.xml", "kesme_form_luyalt.xml", "ack_form.xml",
                "kesme_form3.xml", "perakende_form.xml", "perakende_kdm_form.xml", "sayac_degistirme_form.xml", "sayac_takma_form.xml", "dagitim_form.xml",
                "okuma_bildirim_form.xml", "perakende_imza2.bmp", "imza_dagitim.bmp", "kesme_ihbar_form.xml", "aysonu_form.xml", "aysonu_kdm_form.xml", "mepas_logo.bmp", "medas_logo.bmp", "eArsiv_logo.bmp",
                "olcu_devre_akim_var_form.xml", "olcu_devre_akim_yok_form.xml"};   //LOGO GÖNDERME GÜNCELLEME 04.11.2021

        //"imza_perakende.bmp", "imza_perakende_sur.bmp",
    }

    @Override
    public List<String> getAboneDurumAciklama(IIsemriIslem islem, IAdurum durum) throws Exception {

        String durumKodu = durum.toString().split("-")[0];
        durum_Kodu =durumKodu;
        IIslemTipi islemTipi = islem.getIsemri().getISLEM_TIPI();
        List<String> list = new ArrayList<String>();
        for (DurumKoduBilgi dkb : aboneDurumAciklama) {
//			if (islemTipi.equals(dkb.ISLEM_TIPI) && durumKodu.equals(dkb.DURUM_KODU.toString()))
//				list.add(dkb.SECENEK);
            if (durumKodu.equals(dkb.DURUM_KODU.toString()))
                list.add(dkb.SECENEK);
        }
        return list;
    }

    @Override
    public AltEmirTuru[] getAltEmirTuru(IIslemTipi islemTipi) {
        if (islemTipi.equals(IslemTipi.Kesme))
            return IDef.kesmeAltEmirTuruList;
        else if (islemTipi.equals(IslemTipi.Acma))
            return IDef.acmaAltEmirTuruList;
        else if (islemTipi.equals(IslemTipi.Ihbar))
            return IDef.ihbarAltEmirTuruList;
        else if (islemTipi.equals(IslemTipi.Kontrol))
            return IDef.kontrolAltEmirTuruList;
        else if (islemTipi.equals(IslemTipi.SayacDegistir))
            return IDef.sayacDegistirAltEmirTuruList;
        else if (islemTipi.equals(IslemTipi.SayacTakma))
            return IDef.sayacTakAltEmirTuruList;
        else if (islemTipi.equals(IslemTipi.Tespit))
            return IDef.tespitAltEmirTuruList;

        return IDef.bosAltEmirTuruList;
    }

    LoginParam loginParam;

    @Override
    public IEleman Login(LoginParam param) throws Exception {

        Connection conn = getConnection();
        Exception ex = null;
        IEleman eleman = null;
        String YETKI = "";

        DbHelper.DeleteAll(conn, adurum.class);
        if (!Globals.isDeveloping()) {
            DbHelper.DeleteAll(conn, sdurum.class);
        }


        ILogin login = (ILogin) mbsServer;

        for (int i = 0; i < 2; i++) {

            try {
                param.mode = 0;
                eleman = login.Login(param);
                ex = null;
                if (i == 0) {
                    List<IAdurum> list1 = fetchAboneDurum(0, param.mode);
                    DbHelper.Save(conn, list1);
                    List<ISdurum> list2 = fetchSayacDurum(0, param.mode);
                    DbHelper.Save(conn, list2);
                    login.Logout();
                    YETKI = YETKI + "I";
                }
            } catch (MbsException e) {

                ex = e;
            }

            if (i == 1)
                break;

            try {
                param.mode = 1;
                eleman = login.Login(param);
                ex = null;
                List<IAdurum> list1 = fetchAboneDurum(0, param.mode);
                DbHelper.Save(conn, list1);
                List<ISdurum> list2 = fetchSayacDurum(0, param.mode);
                DbHelper.Save(conn, list2);
                YETKI = YETKI + "O";

            } catch (MbsException e) {
                ex = e;
                continue;
            }

            break;
        }

        if (ex != null)
            throw ex;

        if (eleman == null)
            throw new MobitException("Kullanıcı girişi yapılamadı!");

        Save(eleman);

        eleman.setYETKI(YETKI);
        this.eleman = eleman;

        Item[] items;
        if (eleman.getYETKI().contains("I") && eleman.getYETKI().contains("O"))
            items = IDef.karisikMenuItems;
        else if (eleman.getYETKI().contains("I"))
            items = IDef.isemriMenuItems;
        else if (eleman.getYETKI().contains("O"))
            items = IDef.okumaMenuItems;
        else
            items = new Item[0];

        setMainMenuItems(items);

        // ---------------------------------------------------------------------

        webservice(eleman);

        // ---------------------------------------------------------------------
        // Soti'ye kullanıcı bilgisi aktarma
        File file = new File(getAppDataPath(), "test.xml");
        Soti.setUserInfo(file, Integer.toString(eleman.getELEMAN_KODU()), eleman.getELEMAN_ADI(), "");

        // ---------------------------------------------------------------------
        return eleman;
    }


    SoruSenaryo soruSenaryo = new SoruSenaryo();

    private void webservice(IEleman eleman) throws Exception {
        final IYetki yetki = (IYetki) eleman.getYetki();

        SayacZimmetBilgi szb = newSayacZimmetBilgi();

        Senaryo = szb.SoruSenaryoService();
        SoruSenaryo soruSenaryo = new SoruSenaryo();
        soruSenaryo.SetSoruSenaryosu(Senaryo);
        szb.endeksor_kullanici(getEleman().getELEMAN_KODU(), new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (checkException(form, obj))
                    return null;
                List<SayacZimmetBilgi.OkuyucuBilgi> list = (List<SayacZimmetBilgi.OkuyucuBilgi>) obj;
                List<IIslemYetki> l = yetki.getIslemYetki();
                l.clear();
                l.addAll(list);
                return null;
            }

        });

        szb.DurumKoduDataService(new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (checkException(form, obj))
                    return null;
                aboneDurumAciklama = (List<DurumKoduBilgi>) obj;
                return null;
            }

        });

        szb.endeksor_muhur(getEleman().getELEMAN_KODU(), new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (checkException(form, obj))
                    return null;
                muhurSeriler.clear();
                muhurSeriler.addAll((List<muhurBilgi>) obj);
                return null;
            }

        });

        szb.endeksor_zabit(getEleman().getELEMAN_KODU(), new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (checkException(form, obj))
                    return null;
                zabitSeriler.clear();
                zabitSeriler.addAll((List<zabitBilgi>) obj);
                return null;
            }

        });


    }

    @Override
    public void fetchParameter() throws Exception {

        /*
         * if (Globals.isDeveloping()) { return; }
         */

        Connection conn = getConnection();

        List<IMuhurKod> list3 = null;
        List<ISayacMarka> list4 = null;

        try {

            if (eleman.getYETKI().contains("I")) {
                list3 = fetchMuhurKod(null); // TEST sunucuda zaman aşımına
                // düşüyor.
                list4 = fetchSayacMarka(null);
            }
        } catch (Exception e) {

            throw new MobitException("Parametreleri indirmede sorun oldu!", e);
        }

        boolean tran = false;
        try {

            if (conn.getAutoCommit() == true) {
                conn.setAutoCommit(false);
                tran = true;
            }

            sorular = mbsServer.fetchIsemriSorular();

            if (!Globals.isDeveloping()) {
                DbHelper.DeleteAll(getConnection(), sdurum.class);
            }
            DbHelper.DeleteAll(getConnection(), muhur_kod.class);
            DbHelper.DeleteAll(getConnection(), sayac_marka.class);

            Save(list3);
            Save(list4);

            // if (tran) conn.commit();

        } catch (Exception e) {
            if (tran)
                conn.rollback();
            throw e;
        } finally {
            if (tran)
                conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean Kontrol(int mode, IIsemriIslem islem) throws Exception {
        boolean kontrol = true;
        IIsemri isemri = islem.getIsemri();
        IIslemTipi islemTipi = isemri.getISLEM_TIPI();
        int altEmirTuru = isemri.getALT_EMIR_TURU();


        if (!(islemTipi.equals(IslemTipi.Acma) || islemTipi.equals(IslemTipi.Kesme) || islemTipi.equals(IslemTipi.SayacOkuma)))
            return false;

        // 1 - endeks kontrolü
        // 2 - mühür seri/no kontrolü
        if (mode == 1) {
            if (islemTipi.equals(IslemTipi.SayacDegistir)) {
                if (altEmirTuru == 1)
                    return false;
            }
        }
        if (mode == 2) {

            if (true) return false; // Mühür kontrolü kapatıldı

            if (islemTipi.equals(IslemTipi.Kesme)) {
                if (altEmirTuru == 2)
                    return false;
            }
        }

        if (mode == 1 || mode == 2) {

            for (IAdurum durum : islem.getGELEN_DURUM()) {
                if (!durum.isEmpty()) {
                    // Sayaç okumada bir durum seçilmiş ise endeks zorunlu olmasın
                    if (islemTipi.equals(IslemTipi.SayacOkuma)) {
                        kontrol &= false;
                        break;
                    }
                    // 56 - Bağlantıdan Kesildi
                    // 58 - Kaçaktan Sayaç Sökülmüştür
                    if ((durum.getABONE_DURUM_KODU() == 56 || durum.getABONE_DURUM_KODU() == 58)) {
                        kontrol &= false;
                        break;
                    }
                }
            }
        }
        return kontrol;
    }

    // -------------------------------------------------------------------------

    @Override
    protected void updateDatabase(Connection conn, int dbVer) throws Exception {

        if (dbVer < 1)
            update_v1(conn);

    }

    void update_v1(Connection conn) throws Exception {

        adurum.update_v1(conn);
        sdurum.update_v1(conn);
        DbHelper.setVersion(conn, 1);

    }

    // -------------------------------------------------------------------------

    @Override
    public Operation[] getOperationList(IIslemTipi islemTipi) {
        if (islemTipi.equals(IslemTipi.Acma))
            return IDef.acmaIsl;
        else if (islemTipi.equals(IslemTipi.Bilgi))
            return IDef.genel;
        else if (islemTipi.equals(IslemTipi.Ihbar))
            return IDef.genel;
        else if (islemTipi.equals(IslemTipi.Kesme))
            return IDef.kesmeIsl;
        else if (islemTipi.equals(IslemTipi.Kontrol))
            return IDef.genel;
        else if (islemTipi.equals(IslemTipi.SayacDegistir))
            return IDef.sayacDegistir;
        else if (islemTipi.equals(IslemTipi.SayacOkuma))
            return IDef.okuma;
        else if (islemTipi.equals(IslemTipi.SayacTakma))
            return IDef.sayacTak;
        else if (islemTipi.equals(IslemTipi.Tespit))
            return IDef.tespit;

        return IDef.genel;

    }

    @Override
    public int getIslemRenk(IIslemTipi tip) {

        if (tip.equals(IslemTipi.Bilgi))
            return IDef.BilgiRenk;
        else if (tip.equals(IslemTipi.Kesme))
            return IDef.KesmeRenk;
        else if (tip.equals(IslemTipi.Acma))
            return IDef.AcmaRenk;
        else if (tip.equals(IslemTipi.Ihbar))
            return IDef.IhbarRenk;
        else if (tip.equals(IslemTipi.Kontrol))
            return IDef.KontrolRenk;
        else if (tip.equals(IslemTipi.SayacDegistir))
            return IDef.SayacDegistirRenk;
        else if (tip.equals(IslemTipi.SayacTakma))
            return IDef.SayacTakRenk;
        else if (tip.equals(IslemTipi.Tespit))
            return IDef.TespitRenk;
        else if (tip.equals(IslemTipi.SayacOkuma))
            return IDef.SayacOkumaRenk;
        return IDef.BeyazRenk;

    }

    @Override
    public List<PageData> getPrintPageList(Object obj, IIslem islem, String formType) throws Exception {

        //pagedeata oluşturuyor

        List<PageData> list = super.getPrintPageList(obj, islem, formType);


        //obj büyükse 0 dan fatura gelmiş demektir.
        //muhammed gokkaya
        IIslemTipi islemTipi = null;
        int altEmirTuru = 0;
        int end_ftr_control = 0;

        if (islem != null) {
            IIsemriIslem iislem = (IIsemriIslem) islem;
            IIsemri isemri = getIsemri(iislem.getSAHA_ISEMRI_NO());
            islemTipi = isemri.getISLEM_TIPI();
            try {
                IIsemriIslem isemriIslem = (IIsemriIslem) islem;
                IAdurum[] adurum = isemriIslem.getGELEN_DURUM();
                if (adurum.length > 0) {
                    if (islemTipi.getValue() == 100 && (adurum[0].getABONE_DURUM_KODU() == 5 ||
                            adurum[0].getABONE_DURUM_KODU() == 8 ||
                            adurum[0].getABONE_DURUM_KODU() == 10 ||
                            adurum[0].getABONE_DURUM_KODU() == 12 ||
                            adurum[0].getABONE_DURUM_KODU() == 13 ||
                            adurum[0].getABONE_DURUM_KODU() == 14 ||
                            adurum[0].getABONE_DURUM_KODU() == 1 ||
                            adurum[0].getABONE_DURUM_KODU() == 18 ||
                            adurum[0].getABONE_DURUM_KODU() == 15))
                        end_ftr_control = 1;
                    //Onur Bu abone durum kodlarından sonra boş fatura çıkmaması için eklendi.
                }
            } catch (Exception ex) {
                //pass
            }
            altEmirTuru = isemri.getALT_EMIR_TURU();
            if (altEmirTuru == 5 || altEmirTuru == 6006 || altEmirTuru == 25 || altEmirTuru == 19 || altEmirTuru == 26) {
                Lun_Control zbt = new Lun_Control();
                zbt.setKacakVarmi(1);
            }
        } else {
            if (formType.contains(IDef.kesme_form))
                islemTipi = IslemTipi.Kesme;
        }

        if (islemTipi != null) {
            String so = islemTipi.toString();
            int i = islemTipi.getValue();

            if (i == 100 && end_ftr_control == 0) {
                for (PageData pd : list) {
                    if(pd.getFormType()==IDef.kesme_ihbar_form)
                    {
                        pd.setFormType(IDef.kesme_ihbar_form);
                    }else{
                        String frmType = pd.getFormType(); // H.Elif
                        if (frmType.equals("perakende_form")){
                            pd.setFormType(IDef.perakende_form);
                        }else {
                            pd.setFormType(IDef.perakende_kdm_form);
                        }
                    }
                }
            }
            if (islemTipi.equals(IslemTipi.Kesme)) {
                IIsemriIslem iislem = (IIsemriIslem) islem;
                IIsemri2 isemri2 = (IIsemri2) getIsemri(iislem.getSAHA_ISEMRI_NO());


                for (PageData pd : list) {
                    pd.setFormType("kesme_form");
                    String formTipi = pd.getFormType();

                    boolean faturalimi = false;
                    if (formTipi.equalsIgnoreCase(IDef.perakende_form) || formTipi.equalsIgnoreCase(IDef.perakende_kdm_form)
                            || formTipi.equalsIgnoreCase(IDef.dagitim_form)
                            || formTipi.equalsIgnoreCase(IDef.aysonu_form)
                            || formTipi.equalsIgnoreCase(IDef.aysonu_kdm_form)
                            || formTipi.equalsIgnoreCase(IDef.okuma_bildirim_form)
                            || formTipi.equalsIgnoreCase(IDef.fatura))
                        faturalimi = true;

                    Number borc_tutar = 0;
                    PrintItem item = pd.getPrintItem("borc_tutar");
                    if (item != null) {
                        Object value = item.getValue();
                        if (value != null) {
                            String s = value.toString().trim();
                            if (!s.isEmpty())
                                borc_tutar = Globals.trNumberFormat.parse(s);
                        }
                    }
                    boolean borcvarmi = borc_tutar.doubleValue() > 0;

                    // pd.setFormType(faturalimi ? IDef.kesme_form3 : borcvarmi
                    // ? IDef.kesme_form : IDef.kesme_form2);
                    //Onur LUYALT tesisatlarda yapılan kesmelerde yönetmelik maddesi eklemek için yeni fatura oluşturuldu.
                    String KesmeLuyalt = "kesme_form_luyalt";
                    String aciklama = "";
                    String acklm = "LİSANSSIZ ÜRETİCİ SKB BORCU";

                    if (isemri2 != null){
                        aciklama = isemri2.getISEMRI_ACIKLAMA();
                    }
                    else if (formType.equals(KesmeLuyalt) && isemri2 == null){
                        aciklama = acklm;
                    }



                    if (aciklama.equals(acklm)){
                        pd.setFormType(IDef.kesme_form_luyalt);
                    }else {
                        pd.setFormType(faturalimi ? IDef.kesme_form3 : IDef.kesme_form2);
                    }

                    //DENEME
                    //ACK
                    if (altEmirTuru == 5) {

                    }
                    if (altEmirTuru == 4) {
                        pd.setFormType(IDef.ack_form);
                    }


                }
            } else if (islemTipi.equals(IslemTipi.SayacDegistir) || islemTipi.equals(IslemTipi.SayacTakma)) {
                // Sayaç takma ve değiştirmede fatura formu gelirse
                // yazdırılmasının engellenmesi
                for (Iterator<PageData> iterator = list.iterator(); iterator.hasNext(); ) {
                    PageData pd = iterator.next();
                    if (!pd.getFormType().equals(IDef.sayac_degistirme_form)
                            && !pd.getFormType().equals(IDef.sayac_takma_form))
                        iterator.remove();
                }
            } else if (i == 100 && end_ftr_control == 1) {
                for (Iterator<PageData> iterator = list.iterator(); iterator.hasNext(); ) {
                    PageData pd = iterator.next();
                    if (!pd.getFormType().equals(IDef.sayac_degistirme_form)
                            && !pd.getFormType().equals(IDef.sayac_takma_form))
                        iterator.remove();
                }
            } else if (islemTipi.equals(IslemTipi.Ihbar)) {
                for (PageData pd : list) pd.setFormType(IDef.ihbar_form);
            } else if (islemTipi.equals(IslemTipi.Acma)) {
                for (PageData pd : list) pd.setFormType(IDef.acma_form);
            }
            //muhammed gökkaya
            else if (islemTipi.equals(IslemTipi.Tespit) && altEmirTuru == 6097) {
                for (Iterator<PageData> iterator = list.iterator(); iterator.hasNext(); ) {
                    PageData pd = iterator.next();
                    if (!pd.getFormType().equals(IDef.sayac_degistirme_form)
                            && !pd.getFormType().equals(IDef.sayac_takma_form))
                        iterator.remove();
                }
            }
            //muhammed gökkaya 25 mart 2020
            else if (islemTipi.equals(IslemTipi.Tespit) && altEmirTuru == 34) {
                for (Iterator<PageData> iterator = list.iterator(); iterator.hasNext(); ) {
                    PageData pd = iterator.next();
                    if (!pd.getFormType().equals(IDef.sayac_degistirme_form)
                            && !pd.getFormType().equals(IDef.sayac_takma_form))
                        iterator.remove();
                }
            } else if (islemTipi.equals(IslemTipi.Tespit) && altEmirTuru == 23) {
                for (PageData pd : list){
                    String frmType = pd.getFormType(); // Onur Kademeli kademesiz ayrım bozulduğu için yeniden yazıldı.
                    if (obj.toString().contains("perakende_form")){
                        pd.setFormType(IDef.aysonu_form);
                    }else {
                        pd.setFormType(IDef.aysonu_kdm_form);
                    }
                }
            }
            // H. Elif ölçü devre yazdırmak için eklendi
            else if (islemTipi.equals(IslemTipi.Tespit) && altEmirTuru == 6) {
                for (PageData pd : list) {
                    pd.setFormType(IDef.olcu_devre_akim_var_form);
                }
            }

        }

        // Dikkat! deneme amaçlı eklenmiştir
        if (Globals.isDeveloping()) {
			/*
			 	IDef.perakende_form
			 	IDef.okuma_bildirim_form
				IDef.ihbar
				IDef.acma
				IDef.kesme_form2
				IDef.kesme_form3
				IDef.sayac_degistirme_form
				IDef.sayac_takma_form
			*/

            //for (PageData pd : list) pd.setFormType(IDef.perakende_form);
        }


        return list;
    }

    @Override
    public PageData getOdfPrintPageList(Object obj, OlcuDevreForm odf) throws Exception {
        PageData data = super.getOdfPrintPageList(obj, odf);
        data.setFormType(odf.get_formtype());
        return data;
    }


    @Override
    public Object getIslemData(String key, PageData pd, IIslem islem) throws Exception {
        // İleride başka alanlara ihtiyaç olabilir
        return super.getIslemData(key, pd, islem);

    }

    @Override
    public void sendIslem(final IForm form, final IIslem islem, final Callback clb, int timeout) throws Exception {

        Callback rDo = new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                try {

                    IslemSendTask2 task = new IslemSendTask2(MedasApplication.this, islem);
                    return task.call();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    return e;
                }
            }
        };

        Callback rPost = new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (clb != null)
                    return clb.run(obj != null ? obj : 0);

                return null;
            }

        };

        //HÜSEYİN EMRE ÇEVİK (Toplu Gönderide Çok Popup açılma sorunu çözüldü)
        if (timeout == 15000) {
            runAsync(form, "İşlem sunucuya gönderiliyor...", "", null, rDo, rPost);
        } else {
            runAsync(form, null, null, null, rDo, rPost);
        }


    }

    @Override
    public IServer getServer(int serverId) {
        switch (serverId) {
            case IDef.MBS_SERVER:
                return (IServer) mbsServer;
            case IDef.MEDAS_SERVER:
                return (IServer) medasServer;
            case IDef.OSOS_SERVER:
                return (IServer) ososServer;
            case IDef.MEDAS_OSOS_SERVER:
                return (IServer) medasOsosServer;
        }
        return null;
    }

    @Override
    public Class<?>[] getTableClass() {
        return com.mobit.utility.combine(Class.class, mobit.elec.mbs.IDef.tables, mobit.elec.mbs.medas.IDef.tables);
    }

    @Override
    public Class<?>[] getClearTableClass() {
        return com.mobit.utility.combine(Class.class, mobit.elec.mbs.IDef.clearTables, mobit.elec.mbs.medas.IDef.clearTables);
    }

    @Override
    public IIslemMaster newIslemMaster() throws MobitException {
        return new IslemMaster3(this);
    }

    @Override
    protected IIslemMaster newIslemMaster(IIslem iislem) throws Exception {
        return new IslemMaster3(this, iislem);
    }

    @Override
    protected Integer getIsemriNo(IIslem islem) {

        Integer isemriNo = super.getIsemriNo(islem);
        if (isemriNo != null) return isemriNo;
        if (islem instanceof kacak) {
            kacak iislem = (kacak) islem;
            return iislem.getTESISAT_NO();
        }
        return null;
    }
	/*
	@Override
	public List<IIslem> getIslem(IRecordStatus durum) throws Exception
	{
		return (List<IIslem>)(List<?>)IslemMaster2.selectFromDurum(this, durum);
	}

	@Override
	public List<IIslem> getIslem(int isemriNo) throws Exception
	{
		return (List<IIslem>)(List<?>)IslemMaster3.selectFromIsemriNo(this, isemriNo);
	}*/

}
