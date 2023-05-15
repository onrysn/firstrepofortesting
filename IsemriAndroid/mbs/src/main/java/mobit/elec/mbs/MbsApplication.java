package mobit.elec.mbs;


import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.Globals;
import com.mobit.ICbs;
import com.mobit.IDb;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.ILogin;
import com.mobit.INode;
import com.mobit.IPlatform;
import com.mobit.IRecordStatus;
import com.mobit.IServer;
import com.mobit.IslemGrup;
import com.mobit.MobitException;
import com.mobit.PageData;
import com.mobit.PrintForm;
import com.mobit.PrintItem;
import com.mobit.RawText;
import com.mobit.RecordStatus;
import com.mobit.Text;
import com.mobit.Yazici;

import mobit.eemr.IReadResult;
import mobit.eemr.OlcuDevreForm;
import mobit.eemr.YaziciHataBildirimi;
import mobit.elec.IIsemriUnvan;
import mobit.elec.IMuhur;
import mobit.elec.IOkumaRapor;
import mobit.elec.ISeriNo;
import mobit.elec.MsgInfo;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.FazSayisi;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemDurum2;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacCinsi;
import mobit.elec.enums.SayacHaneSayisi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.enums.Voltaj;
import mobit.elec.AltEmirTuru;
import mobit.elec.ElecApplication;
import mobit.elec.IAdurum;
import mobit.elec.IAtarif;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriRapor;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriSorular;
import mobit.elec.IIsemriZabit;
import mobit.elec.IKarne;
import mobit.elec.IMuhurKod;
import mobit.elec.ITakilanSayac;
import mobit.elec.ISayacMarka;
import mobit.elec.ISdurum;
import mobit.elec.ITesisat;
import mobit.elec.IsemriFilterParam;
import mobit.elec.IslemTipi2;
import mobit.elec.mbs.enums.CevapTipi;
import mobit.elec.mbs.enums.FormTipi;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.enums.Mulkiyet;
import mobit.elec.mbs.enums.OkumaMetodu;
import mobit.elec.mbs.enums.Seviye;
import mobit.elec.mbs.get.Cbs;
import mobit.elec.mbs.get.adurum;
import mobit.elec.mbs.get.eleman;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.get.muhur_kod;
import mobit.elec.mbs.get.result;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.get.sdurum;
import mobit.elec.mbs.get.tesisat;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.put.put_tesisat_muhur;
import mobit.elec.mbs.server.IslemGrup2;

public abstract class MbsApplication extends ElecApplication implements IMbsApplication {

    protected IMbsServer mbsServer = null;
    public int IsemriNO;
    //IIsemriIslem isemriIslem;
    IIsemri isemriIslem;
    String adres_3;
    OptikIsEmri optikis;
    Date updateDate = null;
    List<IIsemri> list = null;
    protected List<IIsemriSorular> sorular = new ArrayList<IIsemriSorular>();

    public static final long HOUR = 3600 * 1000; // in milli-seconds.


    public MbsApplication() throws Exception {

        Globals.app = this;

        eleman = new eleman();
	/*	eleman.setELEMAN_KODU(1111);
		eleman.setELEMAN_ADI("TEST");
		eleman.setELEMAN_SIFRE(1111);

*/
        eleman.setELEMAN_KODU(8888);
//		eleman.setELEMAN_ADI("sami");
        eleman.setELEMAN_SIFRE(123456);

		/*
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		TimeZone timeZone = calendar.getTimeZone();
		int saat = timeZone.getOffset(Calendar.ZONE_OFFSET);
		saat /= 3600;
		*/

    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public void close() {
        if (mbsServer != null) mbsServer.close();
        super.close();
    }

    @Override
    public void init(IPlatform platform, Object obj) throws Exception {

        super.init(platform, obj);
        mbsServer = (IMbsServer) super.server;

        if (scanTimeout > 0) {
            sendScanTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ExecutorService executor = MbsApplication.this.executor;
                    //if (executor != null)
                    //	executor.submit(scanTask);
                }

            }, 0, scanTimeout);
        }

    }

    @Override
    public boolean isTest() {
        //if(eleman != null && eleman.isTest()) return true;
        if (mbsServer != null && mbsServer.isTest()) return true;
        return false;
    }

    @Override
    public List<IAdurum> getAboneDurum(Integer ABONE_DURUM_KODU, Integer KOD_TIPI) throws Exception {
        // TODO Auto-generated method stub

        List<IAdurum> list = DbHelper.SelectAll(getConnection(), adurum.class);
        for (Iterator<IAdurum> iter = list.listIterator(); iter.hasNext(); ) {
            IAdurum element = iter.next();
            if (element.getKOD_TIPI() != KOD_TIPI)
                //Eleman koduna göre kod tipini belirleyip endeks ve mobil kodlarını ayırıyor. Onur
                iter.remove();
        }
        return list;
    }

    @Override
    public List<ISdurum> getSayacDurum(Integer SAYAC_DURUM_KODU, Integer KOD_TIPI) throws Exception {
        // TODO Auto-generated method stub
        List<ISdurum> list = DbHelper.SelectAll(getConnection(), sdurum.class);
        for (Iterator<ISdurum> iter = list.listIterator(); iter.hasNext(); ) {
            ISdurum element = iter.next();
            if (element.getKOD_TIPI() != KOD_TIPI)
                iter.remove();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IIsemri> getIsemri(boolean update) throws Exception {
        // TODO Auto-generated method stub

        if (update == true /*
         * || updateDate == null ||
         * mobit.utility.minutesBetween(updateDate, new
         * Date()) > 30
         */) {
            int zaman_kodu = isemri_guncelle.getMinZamanKodu(getConnection());
            if (zaman_kodu != 0) {
                try {
                    updateDate = new Date();
                    List<IIsemri> list2 = mbsServer.fetchIsemri(zaman_kodu, update);
                    if (!list2.isEmpty()) {
                        IsemriFilterParam param = new IsemriFilterParam();
                        for (Iterator<IIsemri> it = list2.iterator(); it.hasNext(); ) {
                            IIsemri isemri = it.next();
                            param.isemri = isemri;
                            if (!checkFilter(param))
                                it.remove();
                        }
                        DbHelper.Save(getConnection(), list2);
                    }
                } catch (Exception e) {

                }
            }
        }

        //list = DbHelper.SelectAll(getConnection(), isemri_guncelle.class);
        list = isemri_guncelle.isemriListesi(getConnection());

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IIsemri> getFullIsemri(boolean update) throws Exception {
        // TODO Auto-generated method stub

        if (update == true /*
         * || updateDate == null ||
         * mobit.utility.minutesBetween(updateDate, new
         * Date()) > 30
         */) {
            Date now = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
            Long longTime = new Long(now.getTime() / 1000);
            int zaman_kodu = longTime.intValue();
            if (zaman_kodu != 0) {
                try {
                    updateDate = new Date();

                    List<IIsemri> list2 = mbsServer.fetchIsemri(zaman_kodu, update);
                    if (!list2.isEmpty()) {

                        IsemriFilterParam param = new IsemriFilterParam();
                        for (Iterator<IIsemri> it = list2.iterator(); it.hasNext(); ) {
                            IIsemri isemri = it.next();
                            param.isemri = isemri;
                            if (!param.isemri.getISEMRI_DURUMU().YapilabilirMi() || !checkFilter(param)) {
                                it.remove();
                            }
							/*
							if (!checkFilter(param))
								it.remove();
							 */
                        }
                        DbHelper.DeleteAtanmis(getConnection(), isemri_guncelle.class, 1);
                        DbHelper.DeleteAtanmis(getConnection(), isemri_guncelle.class, 0);
                        DbHelper.Save(getConnection(), list2);
                        //DbHelper.DeleteAll(getConnection(),isemri_guncelle.class);
                    }
                } catch (Exception e) {
                    if (e.getMessage().equals("WRN: 419 \n" +
                            "Güncellenecek kayıt bulunamadı")) {
                        DbHelper.DeleteAtanmis(getConnection(), isemri_guncelle.class, 1);
                        DbHelper.DeleteAtanmis(getConnection(), isemri_guncelle.class, 0);
                    }
                    e.printStackTrace();
                }
            }
        }

        //list = DbHelper.SelectAll(getConnection(), isemri_guncelle.class);
        list = isemri_guncelle.isemriListesi(getConnection());

        return list;
    }

    @Override
    public IIsemri getIsemri(Integer ISEMRI_NO) throws Exception {
        IsemriNO = ISEMRI_NO;
        return isemri_guncelle.IsemriBul(conn, ISEMRI_NO);
    }

    @Override
    public List<ITesisat> getTesisat(Integer TESISAT_NO) throws Exception {
        // TODO Auto-generated method stub
        return isemri_guncelle.TesisatBul(conn, TESISAT_NO);
    }

    @Override
    public List<IMuhurKod> getMuhurKod(Integer MUHUR_KOD) throws Exception {
        // TODO Auto-generated method stub
        return DbHelper.SelectAll(getConnection(), muhur_kod.class);
    }

    @Override
    public List<ITakilanSayac> getSayac(Integer SAYAC_NO) throws Exception {
        // TODO Auto-generated method stub
        return DbHelper.SelectAll(getConnection(), takilan_sayac.class);
    }

    @Override
    public List<ISayacMarka> getSayacMarka(String SAYAC_MARKA) throws Exception {
        // TODO Auto-generated method stub
        return DbHelper.SelectAll(getConnection(), sayac_marka.class);
    }


    @Override
    public List<ITesisat> getKarneTesisat(Integer KARNE_NO) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IIsemri> getTesisatIsemri(Integer TESISAT_NO) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    //Sema
    @Override
    public List<PageData> getPrintPageList(Object obj, IIslem islem, String formType) throws Exception {

        IIsemriIslem iislem = (IIsemriIslem) islem;
        if (iislem != null)
            formType = iislem.getFormType();
        String s = "";

        List<PageData> pageList = new ArrayList<PageData>();

        if (obj instanceof result) {
            result r = (result) obj;
            if (!r.isPrintable())
                throw new MobitException(MsgInfo.YAZDIRMA_VERISI_DEGIL);
            s = r.getRACIKLAMA();

        } else {

            s = obj.toString();
        }

        if (s == null) return pageList;

        // Sayfalara bölme
        //sayaç değiştirmede 2 sayfa geliyor bölünce 2 veri yazdırıyor
        //String[] pages = s.split("\f" + IDef.FORM_BEGIN);
        //	System.out.println("Emre Çevik pages (s)  ="+s);
        int asci = 12;

        String str = null;
        str = Character.toString((char) asci);
        String[] pages = s.split(str);

        for (String page : pages) {

            // page.replaceAll("^\n+", "").replaceAll("\n+$", "");

            // Boş sayfaların atlanması
            if (page.length() == 0 || (page.length() == 1 && page.charAt(0) == '\n'))
                continue;

            PageData pd = new PageData();
            pd.setFormType(formType);

            if (page.contains("ft")) {
                String[] items = page.split("\n|\r\n");

                for (String item : items) {
                    String[] key_value = item.split("\t");
                    if (key_value.length > 1)
                        pd.addItem(new PrintItem(key_value[0], key_value.length > 1 ? key_value[1] != null ? key_value[1].trim() : null : null));
                }
                pageList.add(pd);
                continue;
            }

            // Satırlara bölme0
            String[] items = page.split("\n|\r\n");

            String tip = null;
            String ft = null;
            int kontrol_bl = 0;
            for (String item : items) {

                // Boş satırların atlanması
                if (item.length() == 0)
                    continue;

                // alan adı vdeğerinie  bölme
                String[] key_value = item.split("\t");
                // Boş alan adlarının atlanması

                //Onur adres_1 ile adres_2'yi birleştirmek için eklendi.
                if (key_value.length == 1 && key_value[0].equals("adres_2")) {
                    key_value = new String[] {"adres_1", adres_3};
                } else if (key_value[0].equals("adres_1")){
                    adres_3 = key_value[1];
                    key_value[1] = "";
                    key_value[0] = "adres1";
                } else if (key_value[0].equals("adres_2")){
                    adres_3 = adres_3 +" "+ key_value[1];
                    key_value[1] = adres_3;
                    key_value[0] = "adres_1";
                }

                if (key_value.length < 1 || key_value[0].isEmpty())
                    continue;
                try {
                    if (key_value[1].equalsIgnoreCase("bl")) {
                        kontrol_bl++;
                    }
                    if (key_value.length > 1) {
                        if (key_value[1].equals("KESME İHBAR BİLDİRİMİ") && kontrol_bl != 0) {
                            kontrol_bl++;//HÜSEYİN EMRE ÇEVİK
                        }
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                }


            }
            for (String item : items) {

                // Boş satırların atlanması
                if (item.length() == 0)
                    continue;

                // alan adı vdeğerinie  bölme
                String[] key_value = item.split("\t");
                // Boş alan adlarının atlanması
                if (key_value.length < 1 || key_value[0].isEmpty())
                    continue;

                //Onur adres_1 ile adres_2'yi birleştirmek için eklendi.
                if (key_value.length == 1 && key_value[0].equals("adres_2")) {
                    key_value = new String[] {"adres_1", adres_3};
                } else if (key_value[0].equals("adres_1")){
                    adres_3 = key_value[1];
                    key_value[1] = "";
                    key_value[0] = "adres1";
                } else if (key_value[0].equals("adres_2")){
                    adres_3 = adres_3 +" "+ key_value[1];
                    key_value[1] = adres_3;
                    key_value[0] = "adres_1";
                }

                if (tip == null) {
                    if (key_value[0].equalsIgnoreCase(IDef.perakende_form)
                            || key_value[0].equalsIgnoreCase(IDef.perakende_kdm_form)
                            || key_value[0].equalsIgnoreCase(IDef.aysonu_form)
                            || key_value[0].equalsIgnoreCase(IDef.aysonu_kdm_form)
                            || key_value[0].equalsIgnoreCase(IDef.perakende_kdm_form)
                            || key_value[0].equalsIgnoreCase(IDef.dagitim_form)
                            || key_value[0].equalsIgnoreCase(IDef.okuma_bildirim_form)
                            || key_value[0].equalsIgnoreCase(IDef.ihbar_form)
                            || key_value[0].equalsIgnoreCase(IDef.ytu_form)
                            || key_value[0].equalsIgnoreCase(IDef.tah_kes_form)
                            || key_value[0].equalsIgnoreCase(IDef.olcu_devre_akim_var_form)
                            || key_value[0].equalsIgnoreCase(IDef.olcu_devre_akim_yok_form)
                    ) {
                        tip = key_value[0];

                    } else if (key_value[0].equalsIgnoreCase(IDef.form_tipi)) {
                        if (key_value[1].equalsIgnoreCase(IDef.fatura)) {
                            // yukarıdaki seçeneklerle belirlenecektir
                            ft = key_value[1];

                        } else if (key_value[1].equalsIgnoreCase(IDef.acma) && kontrol_bl == 1)
                            tip = IDef.acma_form;
                        else if (key_value[1].equalsIgnoreCase(IDef.kesme))
                            tip = IDef.kesme_form;
                        else if (key_value[1].equalsIgnoreCase("bl") && kontrol_bl == 2) {
                            tip = IDef.kesme_ihbar_form;//HÜSEYİN EMRE ÇEVİK
                        } else if (key_value[1].equalsIgnoreCase(IDef.sayac_degistirme)) {
                            tip = IDef.sayac_degistirme_form;
                        } else if (key_value[1].equalsIgnoreCase(IDef.ihbar) && kontrol_bl == 1)
                            tip = IDef.ihbar_form;
                    }
                    // Eski formattaki formların belirlenmesi
                    else if (key_value[0].equalsIgnoreCase(IDef.FORM_BEGIN)) {
                        tip = formType;
                    }

                    if (tip != null) {
                        pd.setFormType(tip);
                        continue;
                    }
                }

                if (tip == null && ft != null)
                    pd.setFormType(ft);


                if (key_value.length > 1)
                    pd.addItem(new PrintItem(key_value[0], key_value.length > 1 ? key_value[1] != null ? key_value[1].trim() : null : null));
            }

            pageList.add(pd);
        }
        return pageList;
    }

    // H. Elif
    @Override
    public PageData getOdfPrintPageList(Object obj, OlcuDevreForm odf) throws Exception {
        String s = "";
        PageData pageData = new PageData();

        if (obj instanceof mobit.elec.mbs.get.result) {
            result r = (result) obj;
            if (!r.isPrintable())
                throw new MobitException(MsgInfo.YAZDIRMA_VERISI_DEGIL);
            s = r.getRACIKLAMA();
        } else {
            s = obj.toString();
        }

        if (s == null) return pageData;

        int asci = 12;

        String str = null;
        str = Character.toString((char) asci);
        String[] pages = s.split(str);

        for (String page : pages) {

            // Boş sayfaların atlanması
            if (page.length() == 0 || (page.length() == 1 && page.charAt(0) == '\n'))
                continue;

            PageData pd = new PageData();
            pd.setFormType(odf.get_formtype());

            if (page.contains("ft")) {
                String[] items = page.split("\n|\r\n");

                for (String item : items) {
                    String[] key_value = item.split("\t");
                    if (key_value.length > 1)
                        pd.addItem(new PrintItem(key_value[0], key_value.length > 1 ? key_value[1] != null ? key_value[1].trim() : null : null));
                }
                pageData = pd;
                continue;
            }

            // Satırlara bölme0
            String[] items = page.split("\n|\r\n");

            String tip = null;
            String ft = null;
            int kontrol_bl = 0;
            for (String item : items) {

                // Boş satırların atlanması
                if (item.length() == 0)
                    continue;

                // alan adı vdeğerinie  bölme
                String[] key_value = item.split("\t");
                // Boş alan adlarının atlanması

                if (key_value.length < 1 || key_value[0].isEmpty())
                    continue;
                try {
                    if (key_value[1].equalsIgnoreCase("bl")) {
                        kontrol_bl++;
                    }
                    if (key_value.length > 1) {
                        if (key_value[1].equals("KESME İHBAR BİLDİRİMİ") && kontrol_bl != 0) {
                            kontrol_bl++;//HÜSEYİN EMRE ÇEVİK
                        }
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                }


            }
            for (String item : items) {

                // Boş satırların atlanması
                if (item.length() == 0)
                    continue;

                // alan adı vdeğerinie  bölme
                String[] key_value = item.split("\t");
                // Boş alan adlarının atlanması
                if (key_value.length < 1 || key_value[0].isEmpty())
                    continue;

                if (tip == null) {
                    if (key_value[0].equalsIgnoreCase(mobit.elec.mbs.IDef.olcu_devre_akim_var_form)
                            || key_value[0].equalsIgnoreCase(mobit.elec.mbs.IDef.olcu_devre_akim_yok_form)
                    ) {

                        tip = key_value[0];

                    }

                    if (tip != null) {
                        pd.setFormType(tip);
                        continue;
                    }
                }

                if (tip == null && ft != null)
                    pd.setFormType(ft);

                if (key_value.length > 1)
                    pd.addItem(new PrintItem(key_value[0], key_value.length > 1 ? key_value[1] != null ? key_value[1].trim() : null : null));
            }

            pageData = pd;
        }
        return pageData;
    }

    @Override
    public String rawTextPrepar(RawText text, PageData pd, IIslem islem) throws Exception {
        StringBuilder sb = new StringBuilder(text.getText());
        for (String key : text.getKeyList()) {
            PrintItem pi = pd.getPrintItem(key);
            String value = null;
            if (pi != null)
                value = pi.getValue().toString();
            if (value == null) {
                Object obj = getIslemData(key, pd, islem);
                if (obj != null)
                    value = obj.toString();
            }
            if (value == null)
                value = "";
            String _s = String.format("{%s}", key);
            replaceAll(sb, _s, value);

        }
        return sb.toString();
    }

    protected static void replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }

    // List<IIsemriIslem> islem = new ArrayList<IIsemriIslem>(1);

    @Override
    public List<IIsemriIslem> getIsemriIslem(Integer ISEMRI_NO) throws Exception {
        // TODO Auto-generated method stub
        IsemriNO = ISEMRI_NO;
        return put_isemri.select(getConnection(), ISEMRI_NO);
    }

    @Override
    public IIsemri position(int SAHA_ISEMRI_NO) throws Exception {
        IsemriNO = SAHA_ISEMRI_NO;
        List<IIsemri> list = tesisat.OkumaTesisatSayacBul(getConnection(), SAHA_ISEMRI_NO);
        if (list.size() == 0)
            throw new MobitException(MsgInfo.TESISAT_VEYA_SAYAC_BULUNAMADI);
        IIsemri t = list.get(0);
        setActiveIsemri(t);
        setActiveIslem(t.newIslem());
        return t;
    }

    @Override
    public IIsemri positionFirst(int KARNE_NO) throws Exception {
        IIsemri t = tesisat.KarneIlkTesisat(getConnection(), KARNE_NO);
        if (t == null)
            throw new MobitException(MsgInfo.KARNE_BULUNAMADI);
        setActiveIsemri(t);
        return t;
    }

    /*
     * @Override public IIsemri positionNextTesisat() throws Exception { IIsemri
     * t = getActiveIsemri(); if (t == null) throw new
     * MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN); t =
     * tesisat.KarneSonrakiTesisat(getConnection(), t.getKARNE_NO(),
     * t.getSIRA_NO()); if (t == null) throw new
     * MobitException(MsgInfo.SON_KAYITTA_BULUNUYORSUNUZ); setActiveIsemri(t);
     * return t; }
     *
     * @Override public IIsemri positionPrevTesisat() throws Exception { IIsemri
     * t = getActiveIsemri(); if (t == null) throw new
     * MobitException(MsgInfo.ONCE_BIR_TESISATA_KONUMLAN); t =
     * tesisat.KarneOncekiTesisat(getConnection(), t.getKARNE_NO(),
     * t.getSIRA_NO()); if (t == null) throw new
     * MobitException(MsgInfo.ILK_KAYITTA_BULUNUYORSUNUZ); setActiveIsemri(t);
     * return t; }
     */

    // -------------------------------------------------------------------------
    @Override
    public void fetchParameter() throws Exception {

        /*
         * if (Globals.isDeveloping()) { return; }
         */

        Connection conn = getConnection();

        List<IAdurum> list1 = null;
        List<ISdurum> list2 = null;
        List<IMuhurKod> list3 = null;
        List<ISayacMarka> list4 = null;

        try {

            list1 = fetchAboneDurum(null, 0);
            list2 = fetchSayacDurum(null, 0);

            sorular = mbsServer.fetchIsemriSorular();

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

            DbHelper.DeleteAll(getConnection(), adurum.class);
            if (!Globals.isDeveloping()) {
                DbHelper.DeleteAll(getConnection(), sdurum.class);
            }
            DbHelper.DeleteAll(getConnection(), muhur_kod.class);
            DbHelper.DeleteAll(getConnection(), sayac_marka.class);

            Save(list1);
            Save(list2);
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
    public List<IAdurum> fetchAboneDurum(Integer ABONE_DURUM_KODU, Integer KOD_TIPI) throws Exception {
        // TODO Auto-generated method stub
        List<IAdurum> list = mbsServer.fetchAboneDurum(ABONE_DURUM_KODU, KOD_TIPI);
        //Onur Abone durum kodlarında endeks tarafının kodlarının sıralaması değiştirildi.
        //Yeni eklenen abone durum kodu olursa listenin başına gelecek list 2 add ve list.remove döngülerinin index değerleri artırılarak sona alınabilir.
        List<IAdurum> list2 = new ArrayList<>();
        if (KOD_TIPI == 1){
            list2.add(0, list.get(9));
            list2.add(1, list.get(10));
            list2.add(2, list.get(4));
            list2.add(3, list.get(7));
            list2.add(4, list.get(16));
            list2.add(5, list.get(0));
            list2.add(6, list.get(5));
            list2.add(7, list.get(17));
            list2.add(8, list.get(18));
            list2.add(9, list.get(11));
            list2.add(10, list.get(12));
            list2.add(11, list.get(13));
            list2.add(12, list.get(6));
            list2.add(13, list.get(15));
            list2.add(14, list.get(1));
            list2.add(15, list.get(2));
            list2.add(16, list.get(3));
            list2.add(17, list.get(8));
            list2.add(18, list.get(14));
            list2.add(19, list.get(19));
            list2.add(20, list.get(20));
            list2.add(21, list.get(21));
            list2.add(22, list.get(22));

            for(int i = 0; i < 23; i++ ) list.remove(0);

            for (IAdurum item : list2){
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public List<ISdurum> fetchSayacDurum(Integer SAYAC_DURUM_KODU, Integer KOD_TIPI) throws Exception {
        // TODO Auto-generated method stub
        List<ISdurum> list = null;
        try {
            list = mbsServer.fetchSayacDurum(SAYAC_DURUM_KODU, KOD_TIPI);
        } catch (MbsException e) {

            list = new ArrayList<ISdurum>(0);
        }

        return list;
    }

    @Override
    public List<IMuhurKod> fetchMuhurKod(Integer MUHUR_KOD) throws Exception {
        // TODO Auto-generated method stub
        List<IMuhurKod> list = mbsServer.fetchMuhurKod(MUHUR_KOD);
        return list;
    }

    @Override
    public List<ISayacMarka> fetchSayacMarka(String SAYAC_MARKA) throws Exception {
        // TODO Auto-generated method stub
        List<ISayacMarka> list = mbsServer.fetchSayacMarka(SAYAC_MARKA);

        // H.Elif luna ve makel ilk sırada çıksın diye eklendi. 10 adet alındı bilgi eklenirse patlamasın diye
        //Onur Satera 7. Sıraya Eklendi luna ve makelin döngüsü 7 ye çekildi ilk 10 içerisinde satera da bulunsun diye
        List<ISayacMarka> list2 = new ArrayList<>();
        List<ISayacMarka> list3 = new ArrayList<>();
        for(int i = 0; i < 7; i++ )
            list3.add(i,list.get(i + 50));

        for(int i = 0; i < 1; i++ )
            list2.add(i,list.get(i + 70));

        for(int i = 0; i < 1; i++ )
            list.remove(70);

        for(int i = 0; i < 7; i++ )
            list.remove(50);

        for (ISayacMarka item : list3){
            list2.add(item);
        }
        for (ISayacMarka item : list){
            list2.add(item);
        }
        return list2;
    }

    @Override
    public List<IIsemri> fetchIsemri(Integer ISEMRI_NO, IsemriFilterParam filter) throws Exception {
        // TODO Auto-generated method stub
        IsemriNO = ISEMRI_NO;
        List<IIsemri> list = mbsServer.fetchIsemri(ISEMRI_NO, filter);
        return list;
    }

    @Override
    public List<ITesisat> fetchKarneTesisat(Integer KARNE_NO, IsemriFilterParam filter) throws Exception {
        // TODO Auto-generated method stub
        List<ITesisat> list = mbsServer.fetchKarneTesisat(KARNE_NO, filter);
        return list;
    }

    @Override
    public List<IIsemri> fetchKarneIsemri(Integer KARNE_NO, IsemriFilterParam filter) throws Exception {
        // TODO Auto-generated method stub
        int ZAMAN_KODU = 0;
        if (!isTest()) ZAMAN_KODU = isemri_guncelle.getKarneMinZamanKodu(conn, KARNE_NO);
        List<IIsemri> list = mbsServer.fetchKarneIsemri(ZAMAN_KODU, KARNE_NO);
        Filter(list, filter);
        return list;
    }

    @Override
    public List<IIsemri> fetchTesisatIsemri(Integer TESISAT_NO, IsemriFilterParam filter) throws Exception {
        // TODO Auto-generated method stub
        int ZAMAN_KODU = 0;
        if (!isTest()) ZAMAN_KODU = isemri_guncelle.getTesisatMaxZamanKodu(conn, TESISAT_NO);
        if (ZAMAN_KODU == 0) ZAMAN_KODU = 0xffffffff;
        List<IIsemri> list = mbsServer.fetchTesisatIsemri(ZAMAN_KODU, TESISAT_NO);
        if (filter == null) filter = new IsemriFilterParam();
        filter.tesisat_no = TESISAT_NO;
        Filter(list, filter);
        return list;
    }

    @Override
    public List<IKarne> fetchSerbestIsemri(IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU, IsemriFilterParam filter) throws Exception {
        // TODO Auto-generated method stub
        int zamanKodu = isemri_guncelle.getMinZamanKodu(getConnection());
        return mbsServer.fetchSerbestIsemri(zamanKodu, ISLEM_TIPI, ALT_EMIR_TURU);
    }

    @Override
    public List<IKarne> getSerbestIsemri(IIslemTipi ISLEM_TIPI, int ALT_EMIR_TURU) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void ismriKarneBirak(Integer KARNE_NO) throws Exception {
        // TODO Auto-generated method stub
        mbsServer.ismriKarneBirak(KARNE_NO);
        isemri_guncelle.isemriKarneSil(getConnection(), KARNE_NO);

    }

    @Override
    public void isemriBirak(Integer TESISAT_NO, Integer SAHA_ISEMRI_NO) throws Exception {
        // TODO Auto-generated method stub
        IsemriNO = SAHA_ISEMRI_NO;
        mbsServer.isemriBirak(TESISAT_NO, SAHA_ISEMRI_NO);
        isemri_guncelle.isemriTesisatSil(getConnection(), TESISAT_NO);
    }


    @Override
    public List<IIsemriSoru> getIsemriSoru(Integer TESISAT_NO, Integer ISEMRI_NO) throws Exception {
        // TODO Auto-generated method stub
        IsemriNO = ISEMRI_NO;
        List<IIsemriSoru> list = mbsServer.fetchIsemriSoru(TESISAT_NO, ISEMRI_NO);
        return list;
    }

    @Override
    public List<IIsemriSoru> getIsemriSorular(IIsemri isemri) {

        List<IIsemriSoru> list = new ArrayList<IIsemriSoru>();
        int tur = isemri.getISLEM_TIPI().getValue();
        int altTur = isemri.getALT_EMIR_TURU();
        for (IIsemriSorular soru : sorular) {
            if (soru.getEMIR_TURU() == tur && soru.getALT_EMIR_TURU() == altTur) {
                list.add(new isemri_soru(soru));
            }
        }
        return list;
    }

    @Override
    public List<ITesisat> fetchTesisat(Integer TESISAT_NO) throws Exception {
        // TODO Auto-generated method stub
        List<ITesisat> list = mbsServer.fetchTesisat(TESISAT_NO);
        return list;
    }

    @Override
    public List<ITesisat> fetchTesisat2(Integer SAYAC_NO) throws Exception {
        // TODO Auto-generated method stub
        List<ITesisat> list = mbsServer.fetchTesisat2(SAYAC_NO);
        return list;
    }

    @Override
    public List<ITakilanSayac> fetchSayac(Integer SAYAC_NO) throws Exception {
        // TODO Auto-generated method stub
        List<ITakilanSayac> list = mbsServer.fetchSayac(SAYAC_NO);
        return list;
    }

    // -------------------------------------------------------------------------

    protected Integer getIsemriNo(IIslem islem) {

        if (islem instanceof IIsemriIslem) {
            IIsemriIslem iislem = (IIsemriIslem) islem;
            return iislem.getSAHA_ISEMRI_NO();
        } else if (islem instanceof ITakilanSayac) {
            ITakilanSayac iislem = (ITakilanSayac) islem;
            return iislem.getSAHA_ISEMRI_NO();
        } else if (islem instanceof IIsemriSoru) {
            IIsemriSoru iislem = (IIsemriSoru) islem;
            IsemriNO = iislem.getSAHA_ISEMRI_NO();
            return iislem.getSAHA_ISEMRI_NO();
        } else if (islem instanceof IIsemriZabit) {
            IIsemriZabit iislem = (IIsemriZabit) islem;
            return iislem.getSAHA_ISEMRI_NO();
        } else if (islem instanceof IAtarif) {
            IAtarif iislem = (IAtarif) islem;
            return iislem.getTESISAT_NO();
        } else if (islem instanceof put_muhur_sokme) {
            put_muhur_sokme iislem = (put_muhur_sokme) islem;
            return iislem.getTESISAT_NO();
        } else if (islem instanceof put_tesisat_muhur) {
            put_tesisat_muhur iislem = (put_tesisat_muhur) islem;
            return iislem.getTESISAT_NO();
        } else if (islem instanceof IIsemriUnvan) {
            IIsemriUnvan iislem = (IIsemriUnvan) islem;
            return iislem.getTESISAT_NO();
        }

        return null;
    }

    private static void islemSirala(IIslemGrup islemGrup) {

        IIslem iislem = null;
        for (IIslem islem : islemGrup.getIslem()) {
            if (islem instanceof IIsemriZabit) {
                iislem = islem;
                break;
            }
        }
        if (iislem != null) {
            islemGrup.getIslem().remove(iislem);
            islemGrup.getIslem().add(iislem);
        }
    }

    protected IIslemMaster newIslemMaster(IIslem iislem) throws Exception {
        return new IslemMaster2(this, iislem);
    }

    @Override
    public IIslem saveIslem(IIslem islem) throws Exception {

        ILocation location = getLocation();

        ICbs cbs = null;
        if (location != null && location.getLatitude() != 0)
            cbs = new Cbs(location);

        if (islem instanceof IIslemGrup) {

            IIslemGrup islemGrup = (IIslemGrup) islem;
            IIslemGrup masterGrup = new IslemGrup();
            islemSirala(islemGrup);
            for (IIslem iislem : islemGrup.getIslem()) {
                IIslemMaster master;

                // Soru numarası sıfır olan soruların atlanması
                if (iislem instanceof IIsemriSoru && ((IIsemriSoru) iislem).getSORU_NO() == 0)
                    continue;

                if (iislem instanceof IDb) ((IDb) iislem).setId(null);
                master = newIslemMaster(iislem);
                master.setIslemTipi(islemGrup.getIslemTipi());
                master.setGrupId(islemGrup.getGrupId());
                master.setDurum(RecordStatus.Saved);
                master.setZaman(getTime());
                master.setEleman(this.getEleman());
                master.setIsemriNo(getIsemriNo(iislem));
                master.setCBS(cbs);
                master.Save();
                masterGrup.add(master);
            }
            return masterGrup;

        } else if (islem instanceof IIslemMaster) {
            IIslemMaster master = (IIslemMaster) islem;
            master.setDurum(RecordStatus.Saved);
            master.setZaman(getTime());
            master.setEleman(this.getEleman());
            master.setCBS(cbs);
            master.Save();
        } else {
            if (islem instanceof IDb) {
                IDb db = (IDb) islem;
                Save(db);
                db.setMasterId(null);
            }
        }
        return islem;
    }

    @Override
    public void sendIslem(final IForm form, final IIslem islem, final Callback clb, int timeout) throws Exception {

        Callback rDo = new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                try {
                    if (islem instanceof IIslemGrup) {
                        IIslemGrup islemGrup = (IIslemGrup) islem;
                        for (IIslem iislem : islemGrup.getIslem()) {
                            IslemSendTask task = new IslemSendTask(MbsApplication.this, (IIslemMaster) iislem);
                            // Exception döndürmüyor. Düzeltilmesi gerekiyor
                            task.call();
                        }
                    }
                } catch (Exception e) {
                    //		System.out.println("Emre Çevik sendIslem ex ="+e.toString());
                    // TODO Auto-generated catch block
                    return e;
                }
                return null;
            }
        };

        Callback rPost = new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (clb != null)
                    return clb.run(obj);

                return null;
            }

        };

        runAsync(form, "İşlem sunucuya gönderiliyor...", "", null, rDo, rPost);

    }

	/*
	@Override
	public void sendIslemAndPrint(final IForm form, final IIslemGrup grup, final Callback clb) throws Exception {

		sendIslem(form, grup, new Callback() {
			@Override
			public Object run(Object obj) {
				// TODO Auto-generated method stub
				if(!(obj instanceof Throwable)) {
					try {
						for (IIslem ii : grup.getIslem()) {
							if (!(ii instanceof IIslemMaster)) continue;
							IIslemMaster master = (IIslemMaster) ii;
							IIslem islem = master.getIslem();
							print(islem, false);
						}
					} catch (Exception e) {
						obj = e;
					}
				}
				clb.run(obj);
				return null;
			}

		}, 15000);

	}
	*/

    // -------------------------------------------------------------------------

    @Override
    public Class<?>[] getTableClass() {
        return IDef.tables;
    }

    @Override
    public Class<?>[] getClearTableClass() {
        return IDef.clearTables;
    }

    @Override
    public void clearTables(Class<?>[] tables) throws Exception {
        List<IKarne> list = getIsemriKarneListesi();
        try {
            for (IKarne karne : list)
                ismriKarneBirak(karne.getKARNE_NO());
        } catch (Exception e) {

        }
        super.clearTables(tables);
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    static boolean isEmpty(String s) {
        return (s == null || s.length() == 0);
    }

    // 20180416172854 SayacZamani;20180416172928 Pil;1 Demant;000.224
    // Imal;20160929 Kalibrasyon;20160929 KapakAcma;18-04-0100:00
    // Tarife;16-09-2913:53 Reset;17;18-04-0100:00 SayacMarka;MSY
    // SayacModel;<1>T510.2251 SayacNo;40107036
    // Endeks;000474.849;000286.756;000381.913;001143.518
    // Kesintiler;20180416092700-00000000000000;20180409091200-20180409091200
    // 18-04-16,09:27,00-00-00,00:00

    private static String zaman(String zaman) throws Exception {

        if (zaman == null) return "";
        String[] zamanbilgi = zaman.split(",");
        String s = String.format("%s%s", zamanbilgi.length >= 1 ? tarih(zamanbilgi[0]) : "",
                zamanbilgi.length == 2 ? saat(zamanbilgi[1]) : "");
        return s;
    }

    private static String tarih(String tarih) throws Exception {
        if (tarih == null) return "";
        int yil = 0, ay = 0, gun = 0;
        String[] tarihbilgi = tarih.split("-");
        if (tarihbilgi.length >= 1)
            yil = Integer.parseInt(tarihbilgi[0]);
        if (tarihbilgi.length >= 2)
            ay = Integer.parseInt(tarihbilgi[1]);
        if (tarihbilgi.length >= 3)
            gun = Integer.parseInt(tarihbilgi[2]);
        if (gun > 0) {
            yil += (yil < 30) ? 2000 : 1900;
        } else {
            yil = ay = gun = 0;
        }
        return String.format("%04d%02d%02d", yil, ay, gun);

    }

    private static String saat(String saat) {
        if (saat == null) return "";
        int _saat = 0, dakika = 0, saniye = 0;
        String[] saatbilgi = saat.split(":");
        if (saatbilgi.length >= 1)
            _saat = Integer.parseInt(saatbilgi[0]);
        if (saatbilgi.length >= 2)
            dakika = Integer.parseInt(saatbilgi[1]);
        if (saatbilgi.length >= 3)
            saniye = Integer.parseInt(saatbilgi[2]);

        return String.format("%02d%02d%02d", _saat, dakika, saniye);

    }

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public String getOptikData(List<IReadResult> results) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (IReadResult result : results)
            sb.append(getOptikData(result));
        return sb.toString();
    }

    private String getOptikData(IReadResult result) throws Exception {

        StringBuilder sb = new StringBuilder();
        Calendar calendar;
        calendar = Calendar.getInstance();

        //System.out.println("Emre Çevik : (getOptikData)");
        Date dt = result.get_okuma_zamani();
        calendar.clear();
        calendar.setTime(dt);
        //calendar.add(Calendar.HOUR_OF_DAY, -2);
        dt = calendar.getTime();

        sb.append(dateFormat.format(dt));

        Date zaman = result.get_sayac_zamani();
        calendar.clear();
        calendar.setTime(zaman);
        sb.append(String.format("\tSayacZamani;%s", dateFormat.format(result.get_sayac_zamani())));

        if (!isEmpty(result.get_pil_durum_kodu()))
            if (result.get_FlagCode().equals("LGZ")) {
                if (Integer.parseInt(result.get_pil_durum_kodu()) > 0) {
                    sb.append(String.format("\tPil;%s", "1"));
                } else {
                    sb.append(String.format("\tPil;%s", "0"));
                }

            } else {
                sb.append(String.format("\tPil;%s", result.get_pil_durum_kodu()));
            }
        //sb.append(String.format("\tPil;%s", result.get_pil_durum_kodu()));
		/*
		Muhammed Gökkaya IsemriNo Alanı mevcut değilmiş
		*/


        sb.append(String.format("\tIsemriNo;%s", optikis.IsemriNo));

        if (!isEmpty(result.get_demand_end())) {
            sb.append(String.format("\tDemant;%s", result.get_demand_end()));
            if (!isEmpty(result.get_demand_end(1)))
                sb.append(String.format("\tDemant-1;%s", result.get_demand_end(1)));
            if (!isEmpty(result.get_demand_end(2)))
                sb.append(String.format("\tDemant-2;%s", result.get_demand_end(2)));
        }

        if (!isEmpty(result.get_uretim_tarih()))
            sb.append(String.format("\tImal;%s", tarih(result.get_uretim_tarih())));

        if (!isEmpty(result.get_kalibrasyon_tarih()))
            sb.append(String.format("\tKalibrasyon;%s", tarih(result.get_kalibrasyon_tarih())));

        if (!isEmpty(result.get_klemens_kapagi_acilma())) {
            String[] klemens = result.get_klemens_kapagi_acilma().split(":adet:");
            if (klemens.length > 1)
                sb.append(String.format("\tKapakAcilma;%s;%s-%s", zaman(result.get_govde()), zaman(klemens[0]), klemens[1]));
            else
                sb.append(String.format("\tKapakAcilma;%s;%s", zaman(result.get_govde()), zaman(klemens[0])));
        }
        //muhammed gökkaya
        if (!isEmpty(result.get_manyetik()))
            sb.append(String.format("\tManyetik;%s", zaman(result.get_manyetik())));

        if (isEmpty(result.get_manyetik())) {
            if (!isEmpty(result.get_lun_manyetik()))
                sb.append(String.format("\tManyetik;%s", zaman(result.get_lun_manyetik())));
        }

        if (!isEmpty(result.get_tarife_bilgi()))
            sb.append(String.format("\tTarife;%s", zaman(result.get_tarife_bilgi())));

        if (!isEmpty(result.get_demand_reset()))
            sb.append(
                    String.format("\tReset;%s;%s", result.get_demand_reset(), zaman(result.get_demand_reset_tarih())));

        if (!isEmpty(result.get_FlagCode()))
            sb.append(String.format("\tSayacMarka;%s", result.get_FlagCode()));

        if (!isEmpty(result.get_kimlik()))
            sb.append(String.format("\tSayacModel;%s", result.get_kimlik()));

        if (!isEmpty(result.get_sayac_no())) {
            sb.append(String.format("\tSayacNo;%s", result.get_sayac_no()));

            sb.append(String.format("\tSayacDijit;%d", result.getHaneSayisi()));

            if (!isEmpty(result.get_top_end())) {

                String t1 = !isEmpty(result.get_gunduz_end()) ? result.get_gunduz_end() : "";
                String t2 = !isEmpty(result.get_puant_end()) ? result.get_puant_end() : "";
                String t3 = !isEmpty(result.get_gece_end()) ? result.get_gece_end() : "";
                String t = !isEmpty(result.get_top_end()) ? result.get_top_end() : "";
                String enduktif = !isEmpty(result.get_enduktif_end()) ? result.get_enduktif_end() : "";
                String kapasitif = !isEmpty(result.get_kapasitif_end()) ? result.get_kapasitif_end() : "";
                sb.append(String.format("\tEndeksler;%s;%s;%s;%s", t1, t2, t3, t));
                if (!isEmpty(enduktif))
                    sb.append(String.format(";%s", enduktif));
                if (!isEmpty(kapasitif))
                    sb.append(String.format(";%s", kapasitif));

            }

            if (!isEmpty(result.get_gunduz_end(1))) {

                String t1 = !isEmpty(result.get_gunduz_end(1)) ? result.get_gunduz_end(1) : "";
                String t2 = !isEmpty(result.get_puant_end(1)) ? result.get_puant_end(1) : "";
                String t3 = !isEmpty(result.get_gece_end(1)) ? result.get_gece_end(1) : "";
                String t = !isEmpty(result.get_top_end(1)) ? result.get_top_end(1) : "";
                String enduktif = !isEmpty(result.get_enduktif_end(1)) ? result.get_enduktif_end(1) : "";
                String kapasitif = !isEmpty(result.get_kapasitif_end(1)) ? result.get_kapasitif_end(1) : "";
                //Onur yıldızlı endekslerde satera sayaçlarda 999999.999 hatalı değeri gelmesine karşılık eklendi.
                if (t1.equals("999999.999"))
                    t1 = "0";
                if (t2.equals("999999.999"))
                    t2 = "0";
                if (t3.equals("999999.999"))
                    t3 = "0";
                if (t.equals("999999.999"))
                    t = "0";
                if (enduktif.equals("999999.999"))
                    enduktif = "0";
                if (kapasitif.equals("999999.999"))
                    kapasitif = "0";
                sb.append(String.format("\tEndeksler-1;%s;%s;%s;%s", t1, t2, t3, t));
                if (!isEmpty(enduktif))
                    sb.append(String.format(";%s", enduktif));
                if (!isEmpty(kapasitif))
                    sb.append(String.format(";%s", kapasitif));

            }

            if (!isEmpty(result.get_gunduz_end(2))) {

                String t1 = !isEmpty(result.get_gunduz_end(2)) ? result.get_gunduz_end(2) : "";
                String t2 = !isEmpty(result.get_puant_end(2)) ? result.get_puant_end(2) : "";
                String t3 = !isEmpty(result.get_gece_end(2)) ? result.get_gece_end(2) : "";
                String t = !isEmpty(result.get_top_end(2)) ? result.get_top_end(2) : "";
                ;
                String enduktif = !isEmpty(result.get_enduktif_end(2)) ? result.get_enduktif_end(2) : "";
                String kapasitif = !isEmpty(result.get_kapasitif_end(2)) ? result.get_kapasitif_end(2) : "";
                //Onur yıldızlı endekslerde satera sayaçlarda 999999.999 hatalı değeri gelmesine karşılık eklendi.
                if (t1.equals("999999.999"))
                    t1 = "0";
                if (t2.equals("999999.999"))
                    t2 = "0";
                if (t3.equals("999999.999"))
                    t3 = "0";
                if (t.equals("999999.999"))
                    t = "0";
                if (enduktif.equals("999999.999"))
                    enduktif = "0";
                if (kapasitif.equals("999999.999"))
                    kapasitif = "0";
                sb.append(String.format("\tEndeksler-2;%s;%s;%s;%s", t1, t2, t3, t));
                if (!isEmpty(enduktif))
                    sb.append(String.format(";%s", enduktif));
                if (!isEmpty(kapasitif))
                    sb.append(String.format(";%s", kapasitif));

            }

//			int maxKesintiSayisi = 2; // ikiden fazla kesinti sayısında Mbs hata
            // veriyor
            int maxKesintiSayisi = 3;
            boolean baslik;

            baslik = false;

            String[] kesintiler = result.get_kesintiler();
            for (int i = 0, kesinti = 0; i < kesintiler.length && kesinti < maxKesintiSayisi; i++) {

                if (isEmpty(kesintiler[i]))
                    continue;

                if (baslik == false) {
                    sb.append(String.format("\tKesintiler"));
                    baslik = true;
                }
                String[] aralik = kesintiler[i].split(",|\\|");
                if (aralik.length == 4 && !aralik[0].contains("00-00-00")) {
                    sb.append(String.format(";%s%s-%s%s", tarih(aralik[0]), saat(aralik[1]), tarih(aralik[2]),
                            saat(aralik[3])));
                    kesinti++;
                }
            }

            String[] faz1_kesintiler = result.get_faz1_kesintiler();
            for (int i = 0, kesinti = 0; i < faz1_kesintiler.length && kesinti < maxKesintiSayisi; i++) {

                if (isEmpty(faz1_kesintiler[i]))
                    continue;

                if (baslik == false) {
                    sb.append(String.format("\tKesintiler"));
                    baslik = true;
                }
                String[] aralik = faz1_kesintiler[i].split(",|\\|");
                if (aralik.length == 4 && !aralik[0].contains("00-00-00")) {
                    sb.append(String.format(";%s%s-%s%s R", tarih(aralik[0]), saat(aralik[1]), tarih(aralik[2]),
                            saat(aralik[3])));
                    kesinti++;
                }
            }

            String[] faz2_kesintiler = result.get_faz2_kesintiler();
            for (int i = 0, kesinti = 0; i < faz2_kesintiler.length && kesinti < maxKesintiSayisi; i++) {

                if (isEmpty(faz2_kesintiler[i]))
                    continue;

                if (baslik == false) {
                    sb.append(String.format("\tKesintiler"));
                    baslik = true;
                }
                String[] aralik = faz2_kesintiler[i].split(",|\\|");
                if (aralik.length == 4 && !aralik[0].contains("00-00-00")) {
                    sb.append(String.format(";%s%s-%s%s S", tarih(aralik[0]), saat(aralik[1]), tarih(aralik[2]),
                            saat(aralik[3])));
                    kesinti++;
                }
            }
            String[] faz3_kesintiler = result.get_faz3_kesintiler();
            for (int i = 0, kesinti = 0; i < faz3_kesintiler.length && kesinti < maxKesintiSayisi; i++) {

                if (isEmpty(faz3_kesintiler[i]))
                    continue;

                if (baslik == false) {
                    sb.append(String.format("\tKesintiler"));
                    baslik = true;
                }
                String[] aralik = faz3_kesintiler[i].split(",|\\|");
                if (aralik.length == 4 && !aralik[0].contains("00-00-00")) {
                    sb.append(String.format(";%s%s-%s%s T", tarih(aralik[0]), saat(aralik[1]), tarih(aralik[2]),
                            saat(aralik[3])));
                    kesinti++;
                }
            }

            if (!isEmpty(result.get_faz1_akim())) {
                sb.append(String.format("\tAkim;%s", result.get_faz1_akim()));
                if (result.get_faz2_akim() != null)
                    sb.append(String.format("-%s-%s", result.get_faz2_akim(), result.get_faz3_akim()));


            }
            if (!isEmpty(result.get_faz1_gerilim())) {
                sb.append(String.format("\tGerilim;%s", result.get_faz1_gerilim()));
                if (result.get_faz2_gerilim() != null)
                    sb.append(String.format("-%s-%s", result.get_faz2_gerilim(), result.get_faz3_gerilim()));
            }

            if (!isEmpty(result.get_toplam_notr_enerji())){
                sb.append(String.format("\tToplamNotrEnerji;%s", result.get_toplam_notr_enerji()));
            }
            if (!isEmpty(result.get_toplam_notr_faz_enerji_farki())){
                sb.append(String.format("\tToplamNotrFazEnerjiFarki;%s", result.get_toplam_notr_faz_enerji_farki()));
            }
            if (!isEmpty(result.get_cos_l1())){
                sb.append(String.format("\tCosL1;%s", result.get_cos_l1()));
            }
            if (!isEmpty(result.get_cos_l2())){
                sb.append(String.format("\tCosL2;%s", result.get_cos_l2()));
            }
            if (!isEmpty(result.get_cos_l3())){
                sb.append(String.format("\tCosL3;%s", result.get_cos_l3()));
            }

        }

        return sb.toString();
    }


    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    //test de kullanılıyor .
    public Object getPrintTestData(String formId) throws Exception {
        PrintForm pf = getPrintForm(formId);
        if (pf == null || pf.pfi == null)
            throw new MobitException(MsgInfo.FORM_TANIMLANMAMIS);

        StringBuilder sb = new StringBuilder(4096);

        Collection<RawText> list = pf.pfi.getItems(RawText.class);
        if (list.size() > 0) {
            sb.append(IDef.FORM_BEGIN + "\n");
            sb.append(String.format("%s\t%s\n", IDef.form_tipi, pf.getValue().toString()));
            for (RawText rt : list) {
                for (String key : rt.getKeyList())
                    sb.append(String.format("%s\t%s\n", key, key));
            }
            sb.append(IDef.FORM_END + "\n");

        } else {
            List<Text> texts;
            texts = (List<Text>) (Object) pf.pfi.getItems(Text.class);
            sb.append(String.format("%s\n", formId));

            for (INode t : texts)
                if (!t.getId().isEmpty())
                    sb.append(String.format("%s\t%s\n", t.getId(), t.getId()));
        }
        return sb.toString();

    }

    public Object getPrintZPLFormatData(String formId) throws Exception {
        PrintForm pf = getPrintForm(formId);
        if (pf == null || pf.pfi == null)

            throw new MobitException(MsgInfo.FORM_TANIMLANMAMIS);

        StringBuilder sb = new StringBuilder(4096);

        Collection<RawText> list = pf.pfi.getItems(RawText.class);
        int PageWidth = pf.pfi.getPageWidth();
        int PageSize = pf.pfi.getPageSize();
        String pw = Integer.toString(PageWidth);
        String ps = Integer.toString(PageSize);
        List<Text> texts;
        texts = (List<Text>) (Object) pf.pfi.getItems(Text.class);
        String str = "^XA^CI28^MMT^POI^MNM^LL" + ps + "^PW" + pw + "\n" +
                "^FX Second section with recipient address and permit information.\n" +
                "^CFP,20\n";
        str += "^A2N,50,50,B:CYRI_UB.FNT\n";
        for (INode t : texts) {
            String Left = t.getAttribute("Left");
            String Top = t.getAttribute("Top");

            if (t.getAttribute("Name") == null || t.getAttribute("Name").length() == 0) {
                String text = t.getText();
                str += "^FO" + Left + "," + Top + "^FH^FD" + text + "^FS\n";
            } else {
                String name = t.getAttribute("Name");
                str += "^FO" + Left + "," + Top + "^FD" + t.getAttribute("Name") + "^FS\n";
            }

        }
        str += "^XZ";


        sb.append(str);
        return sb.toString();
    }

    @Override
    public Object getIslemData(String key, PageData pd, IIslem islem) throws Exception {

        if (key == null || islem == null)
            return null;

        IIsemriIslem iislem = null;
        if (islem instanceof IIsemriIslem) {
            iislem = (IIsemriIslem) islem;
        } else if (islem instanceof IIslemMaster) {
            IIslemMaster master = (IIslemMaster) islem;
            IIslem i = master.getIslem();
            if (i instanceof IIsemriIslem) iislem = (IIsemriIslem) i;
        }
        if (iislem == null) return null;

        //muhammed gökkaya tck kesme saat alanı

        if ((iislem.getIsemri().getISLEM_TIPI().toString() == "Kesme" && (iislem.getIsemri().getALT_EMIR_TURU() == 5 || iislem.getIsemri().getALT_EMIR_TURU() == 10 || iislem.getIsemri().getALT_EMIR_TURU() == 7 || iislem.getIsemri().getALT_EMIR_TURU() == 4 || iislem.getIsemri().getALT_EMIR_TURU() == 2 || iislem.getIsemri().getALT_EMIR_TURU() == 9 || iislem.getIsemri().getALT_EMIR_TURU() == 6) && key.equalsIgnoreCase("okuma_saati")) || (iislem.getIsemri().getISLEM_TIPI().toString() == "Açma" && key.equalsIgnoreCase("okuma_saati"))) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            Date newDate = new Date(date.getTime() + 3 * HOUR);

            return dateFormat.format(newDate);
        }
        if (key.equalsIgnoreCase("isemri_no"))
            return iislem.getSAHA_ISEMRI_NO();

        else if (key.equalsIgnoreCase("muhur_seri")) {
            String SeriNo = iislem.getSERINO().toString();
            if (SeriNo.contains("000000")) {
                ITakilanSayac sayacIslem;
                if (iislem.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)) {
                    IIslemGrup islemGrup = (IIslemGrup) islem;
                    sayacIslem = (ITakilanSayac) islemGrup.getIslem(ITakilanSayac.class).get(0);
                    return sayacIslem.getSERI_NO().toString();
                }
                return "";

            } else {
                return iislem.getSERINO();
            }

        } else if (key.equalsIgnoreCase("s10_br")) {
            if (!String.valueOf(pd.getPrintItem("s10_birim_fiyat").getValue()).equals("")) {
                return String.valueOf(Float.parseFloat(String.valueOf(Double.parseDouble(String.valueOf(pd.getPrintItem("s10_birim_fiyat").getValue()).replace(',', '.')) / Double.parseDouble(String.valueOf(pd.getPrintItem("kacak_carpan").getValue()).replace(',', '.'))))).replace('.', ',');
            }
        } else if (key.equalsIgnoreCase("s11_br")) {
            if (!String.valueOf(pd.getPrintItem("s11_birim_fiyat").getValue()).equals("")) {
                return String.valueOf(Float.parseFloat(String.valueOf(Double.parseDouble(String.valueOf(pd.getPrintItem("s11_birim_fiyat").getValue()).replace(',', '.')) / Double.parseDouble(String.valueOf(pd.getPrintItem("kacak_carpan").getValue()).replace(',', '.'))))).replace('.', ',');
            }
        } else if (key.equalsIgnoreCase("s12_br")) {
            if (!String.valueOf(pd.getPrintItem("s12_birim_fiyat").getValue()).equals("")) {
                return String.valueOf(Float.parseFloat(String.valueOf(Double.parseDouble(String.valueOf(pd.getPrintItem("s12_birim_fiyat").getValue()).replace(',', '.')) / Double.parseDouble(String.valueOf(pd.getPrintItem("kacak_carpan").getValue()).replace(',', '.'))))).replace('.', ',');
            }
        } else if (key.equalsIgnoreCase("s13_br")) {
            if (!String.valueOf(pd.getPrintItem("s13_birim_fiyat").getValue()).equals("")) {
                return String.valueOf(Float.parseFloat(String.valueOf(Double.parseDouble(String.valueOf(pd.getPrintItem("s13_birim_fiyat").getValue()).replace(',', '.')) / Double.parseDouble(String.valueOf(pd.getPrintItem("kacak_carpan").getValue()).replace(',', '.'))))).replace('.', ',');
            }
        } else if (key.equalsIgnoreCase("s2_br")) {
            if (!String.valueOf(pd.getPrintItem("s2_birim_fiyat").getValue()).equals("")) {
                return String.valueOf(Float.parseFloat(String.valueOf(Double.parseDouble(String.valueOf(pd.getPrintItem("s2_birim_fiyat").getValue()).replace(',', '.')) / Double.parseDouble(String.valueOf(pd.getPrintItem("kacak_carpan").getValue()).replace(',', '.'))))).replace('.', ',');
            }
        } else if (key.equalsIgnoreCase("s3_br")) {
            if (!String.valueOf(pd.getPrintItem("s3_birim_fiyat").getValue()).equals("")) {
                return String.valueOf(Float.parseFloat(String.valueOf(Double.parseDouble(String.valueOf(pd.getPrintItem("s3_birim_fiyat").getValue()).replace(',', '.')) / Double.parseDouble(String.valueOf(pd.getPrintItem("kacak_carpan").getValue()).replace(',', '.'))))).replace('.', ',');
            }
        } else if (key.equalsIgnoreCase("BELGE_TARIH")) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            Date newDate = new Date(date.getTime());

            return dateFormat.format(newDate);
        } else if (key.equalsIgnoreCase("BELGE_TARIH_SAAT")) {
            SimpleDateFormat utc = (SimpleDateFormat) Globals.dateTimeFmt.clone();
            utc.setTimeZone(Globals.utcTimeZone);

            return utc.format(Globals.getTime());
        } else if (key.equalsIgnoreCase("okuyucu_kodu"))
            return iislem.getELEMAN_KODU();
        else if (key.equalsIgnoreCase("fatura_tutar")) {
            try {

                PrintItem pi;
                pi = pd.getPrintItem("borc_tutar");
                if (pi == null)
                    return "";
                double borc_tutar = Double.parseDouble(pi.getValue().toString());
                pi = pd.getPrintItem("borc_gecikme");
                if (pi == null)
                    return "";
                double borc_gecikme = Double.parseDouble(pi.getValue().toString());
                return new Double(borc_tutar + borc_gecikme).toString();

            } catch (Exception e) {

            }
            return "";
        }

        IIsemri isemri = iislem.getIsemri();
        if (isemri == null)
            return null;

        if (key.equalsIgnoreCase("unvan"))
            return isemri.getUNVAN();
        if (key.equalsIgnoreCase("adres_1"))
            return isemri.getADRES();
        if (key.equalsIgnoreCase("mytesisat_cbs")) {
            return String.valueOf(isemri.getCBS());
        }

        return null;
    }


    @Override
    public String printNew(IIslem islem, boolean suret) throws Exception {
//buraya giriyor print adım 1
        int kontrol = 0;
        List<IIslemMaster> masterList = new ArrayList<>();
        if (islem instanceof IIslemMaster) {
            IIslemMaster master = (IIslemMaster) islem;
            if (master.getTabloId() == put_isemri.TABLOID && result.isPrintable(master.getRESULT_TYPE())) {
                masterList.add(master);
            }
        } else if (islem instanceof IIslemGrup) {
            IIslemGrup islemGrup = (IIslemGrup) islem;
            List<IIslemMaster> _masterList = (List<IIslemMaster>) (List<?>) islemGrup.getIslem();
            for (IIslemMaster m : _masterList) {
                if (m.getTabloId() == put_isemri.TABLOID && result.isPrintable(m.getRESULT_TYPE())) {
                    masterList.add(m);
                }
            }

        }


        if (masterList.size() == 0) return "";
        String DonecekDeger = "";
        for (IIslemMaster master : masterList)
            DonecekDeger += printNew(getPrintPageList(master.getRESULT(), master.getIslem(), ""), master.getIslem(), suret);//BURAYA BAK
        try {
            IIsemriIslem iislem = (IIsemriIslem) masterList.get(0).getIslem();
            IIsemri isemri = getIsemri(iislem.getSAHA_ISEMRI_NO());
            IIslemTipi islemTipi = null;
            islemTipi = isemri.getISLEM_TIPI();
            IIsemriIslem isemriIslem = (IIsemriIslem) masterList.get(0).getIslem();
            IAdurum[] adurum = isemriIslem.getGELEN_DURUM();
            if (adurum.length > 0) {
                if (islemTipi.getValue() == 100 && (adurum[0].getABONE_DURUM_KODU() == 5 ||
                        adurum[0].getABONE_DURUM_KODU() == 8 ||
                        adurum[0].getABONE_DURUM_KODU() == 10 ||
                        adurum[0].getABONE_DURUM_KODU() == 12 ||
                        adurum[0].getABONE_DURUM_KODU() == 13 ||
                        adurum[0].getABONE_DURUM_KODU() == 14 ||
                        adurum[0].getABONE_DURUM_KODU() == 15))
                    kontrol = 1;
            }
        } catch (Exception ex) {
            //pass
        }
		/*
		IIsemriIslem isemriIslem = (IIsemriIslem) islem;
		IAdurum[] adurum = isemriIslem.getGELEN_DURUM();

		if (isemriIslem.getIsemri().getISLEM_TIPI().equals(IslemTipi.SayacOkuma) &&
				(adurum[0].getABONE_DURUM_KODU()==5
				|| adurum[0].getABONE_DURUM_KODU()==8
				|| adurum[0].getABONE_DURUM_KODU()==10
				|| adurum[0].getABONE_DURUM_KODU()==12
				|| adurum[0].getABONE_DURUM_KODU()==13
				|| adurum[0].getABONE_DURUM_KODU()==14
				|| adurum[0].getABONE_DURUM_KODU()==15))
		{
			return "";
		}
		 */
        if (kontrol == 0 || masterList.size() > 1) {
            String printerMac = getPrinterMac(0);
            com.mobit.Yazici yazici = new Yazici();
            yazici.Yazdir(printerMac.toString(), DonecekDeger);

        }
        return DonecekDeger;

    }


    @Override
    public String printNewOdf(OlcuDevreForm odf) throws Exception {
        //buraya giriyor print adım 1
        int kontrol = 0;

        String DonecekDeger = "";
        DonecekDeger += printNewOdf(getOdfPrintPageList(obj, odf), odf);//BURAYA BAK

        String printerMac = getPrinterMac(0);
        com.mobit.Yazici yazici = new Yazici();
        yazici.Yazdir(printerMac.toString(), DonecekDeger);

        return DonecekDeger;
    }


    @Override
    public void printIsemriIslem(int SAHA_ISEMRI_NO) throws Exception {

        List<IIslemMaster> list = IslemMaster2.selectFromIsemriNo(this, SAHA_ISEMRI_NO);
        if (list.size() == 0)
            throw new MobitException("Yazdırma bilgisi yok!");
        YaziciHataBildirimi yhb = new YaziciHataBildirimi();
        yhb.setHata_aciklama("Yazdirildi");
        yhb.setGonderme_dur(1);
        yhb.setYazdirma_dur(1);
        String DonecekDeger = "";
        //Yazıcı bağlantısı açıksa kapat
        com.mobit.Yazici yazici = new Yazici();
        if (yazici.bluetooth_kontrol != 0)
            yazici.baglanti_kapat();
        for (IIslemMaster master : list) {
            DonecekDeger += printNew(master, true);
        }

    }

    @Override
    public IServer getServer(int serverId) {
        return (IServer) mbsServer;
    }


    @Override
    public Class<?> getEnumClass(int enumId) throws Exception {
        switch (enumId) {
            case IDef.ENUM_CEVAP_TIPI:
                return CevapTipi.class;
            case IDef.ENUM_ENDEKS_TIPI:
                return EndeksTipi.class;
            case IDef.ENUM_FAZ_SAYISI:
                return FazSayisi.class;
            case IDef.ENUM_ISLEM_DURUM:
                return IslemDurum2.class;
            case IDef.ENUM_ISLEM_TIPI:
                return IslemTipi2.class;
            case IDef.ENUM_MUHUR_KOD_CINS:
                return MuhurKodCins.class;
            case IDef.ENUM_MULKIYET:
                return Mulkiyet.class;
            case IDef.ENUM_OKUMA_METODU:
                return OkumaMetodu.class;
            case IDef.ENUM_SAYAC_CINSI:
                return SayacCinsi.class;
            case IDef.ENUM_SAYAC_HANE_SAYISI:
                return SayacHaneSayisi.class;
            case IDef.ENUM_SAYAC_KODU:
                return SayacKodu.class;
            case IDef.ENUM_SEVIYE:
                return Seviye.class;
            case IDef.ENUM_VOLTAJ:
                return Voltaj.class;
            case IDef.ENUM_FORM_TIPI:
                return FormTipi.class;
        }

        throw new MobitException(String.format("%d enum tanımlanmamış", enumId));
    }

    @Override
    public void ChangePassword(int kullaniciKodu, int sifre, int yeniSifre) throws Exception {
        ((ILogin) mbsServer).ChangePassword(kullaniciKodu, sifre, yeniSifre);
    }

    @Override
    public IIsemriRapor getIsemriRapor() throws Exception {
        return isemri_guncelle.getIsemriRapor(getConnection());
    }

    @Override
    public IIslemMaster newIslemMaster() throws MobitException {
        return new IslemMaster2(this);
    }

    @Override
    public List<IIslem> getIslem(IRecordStatus durum) throws Exception {
        return (List<IIslem>) (List<?>) IslemGrup2.select(this, durum);
    }

    @Override
    public List<IIslem> getIslem(int isemriNo) throws Exception {
        IsemriNO = isemriNo;
        return (List<IIslem>) (List<?>) IslemGrup2.select(this, RecordStatus.Saved, isemriNo);
    }

    @Override
    public AltEmirTuru[] getAltEmirTuru(IIslemTipi islemTipi) {
        return IDef.bosAltEmirTuruList;
    }

    //-------------------------------------------------------------------------
    public static class Param {
        public Exception ex;
        public Object obj;

        public Param(Exception ex, Object obj) {
            this.ex = ex;
            this.obj = obj;
        }
    }


    @Override
    public List<IKarne> getIsemriKarneListesi() throws Exception {
        return isemri_guncelle.isemriKarneListesi(getConnection());
    }

    @Override
    public List<IKarne> getOkumaKarneListesi() throws Exception {
        return isemri_guncelle.okumaKarneListesi(getConnection());
    }

    @Override
    public List<IMuhur> fetchKarneMuhur(int KARNE_NO) throws Exception {
        return mbsServer.fetchKarneMuhur(KARNE_NO);
    }

    @Override
    public List<IMuhur> fetchTesisatMuhur(int TESISAT_NO) throws Exception {
        return mbsServer.fetchTesisatMuhur(TESISAT_NO);
    }

    @Override
    public List<IMuhur> fetchMuhur(ISeriNo muhur) throws Exception {
        return mbsServer.fetchMuhur(muhur);
    }

    @Override
    public List<IMuhur> fetchMuhurDurum(ISeriNo muhur) throws Exception {
        return mbsServer.fetchMuhurDurum(muhur);
    }

    @Override
    public IOkumaRapor getOkumaRaporu(int karne_no) throws Exception {
        return new OkumaRaporu(getConnection(), karne_no);
    }
}
