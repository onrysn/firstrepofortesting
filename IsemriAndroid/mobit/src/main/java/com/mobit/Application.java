package com.mobit;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobit.eemr.GucTespit;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.eemr.SayacEslestir;
import mobit.eemr.YaziciHataBildirimi;


public abstract class Application implements Closeable, IObject, IApplication, ILogin, IMap {


    private boolean closed = false;
    private Object sync = new Object();
    private Thread thread = null;
    private List<Runnable> clbList = new ArrayList<Runnable>();
    protected Timer updCheckTimer = new Timer();
    protected Timer sendScanTimer = new Timer();
    protected int scanTimeout = -1;


    protected File appDataPath;
    protected File appPicturePath;
    protected IPlatform platform = null;
    protected Object obj = null;
    protected ISettings settings = null;
    protected ISettings2 settings2 = null;
    protected boolean settingsChanged = false;
    protected IServer server = null;
    protected List<IPrinterFormat> printFormatList = new ArrayList<IPrinterFormat>();
    protected List<IPrinter> printerList = new ArrayList<IPrinter>();
    protected List<PageFormatInfo> pfiList = new ArrayList<PageFormatInfo>();
    protected List<PrintForm> pfList = new ArrayList<PrintForm>();
    protected ICamera camera = null;
    Activity activity = null;

    protected Connection conn = null;

    protected String ver = "45";
    protected URL verURL = null;
    protected URL appURL = null;
    protected int verCheckPeriod = 0;
    protected String dbFile;
    protected int dbVersion = 1;

    protected String appPath = "demoApp";
    protected String dbName = "data.db";
    protected String dbUser = "";
    protected String dbPassword = "";


    protected FormInitParam param = new FormInitParam("#A4C639", "");

    /*
     * protected String host = null; protected int port = 0; protected String
     * apn = null;
     */
    protected String servVer = null;
    protected IEleman eleman = null;

    protected ExecutorService executor = null;

    private Item[] menuItems = new Item[0];

    private java.util.Map<Integer, Object> objectMap = new HashMap<Integer, Object>();

    protected LoginParam loginParam;


    @Override
    public String getVersiyon() {
        String vers = ver;
        return ver;
    }

    @Override
    public Item[] getMainMenuItems() {
        return menuItems;
    }

    @Override
    public void setMainMenuItems(Item[] menuItems) {
        this.menuItems = menuItems;
    }

    public Application() throws Exception {

        // Globals sınıfının static yapıcısınıb çağrılması için
        boolean init = Globals.init;
        Class.forName(com.mobit.MsgInfo.class.getName());
        for (FormClass fc : getFormClassList())
            Class.forName(fc.formClass.getName());

    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public void setObject(int id, Object obj) {
        objectMap.put(id, obj);
    }

    @Override
    public Object getObject(int id) {
        return objectMap.get(id);
    }

    @Override
    public void init(IPlatform platform, Object obj) throws Exception {

        closed = false;
        this.platform = platform;
        this.obj = obj;

        FormClass[] formList = getFormClassList();
        if (formList != null)
            for (FormClass fc : formList)
                fc.load();
        Class<?>[] tblList = getTableClass();
        if (tblList != null)
            for (Class<?> c : tblList)
                Class.forName(c.getName());

        copySettingFiles(getClass(), getSettingFiles());

        loadSettings();


        /*
         * platform.addMessage("Ayarlar yükleniyor..."); Collection<INode> items
         * = settings.getItems(Parameter.class, null); for (INode item : items)
         * { platform.setSettingValue(item.getId(), item.getStringValue()); }
         */
        INode node;

        node = settings.getItem(Parameter.class, IDef.VER);
        if (node != null)
            ver = node.getStringValue();

        node = settings.getItem(Parameter.class, IDef.VERURL);
        if (node != null)
            verURL = new URL(node.getStringValue());

        node = settings.getItem(Parameter.class, IDef.APPURL);
        if (node != null)
            appURL = new URL(node.getStringValue());

        node = settings.getItem(Parameter.class, IDef.VERCHECKPERIOD);
        if (node != null)
            verCheckPeriod = node.getIntValue();

        node = settings.getItem(Parameter.class, IDef.SERVVER);
        if (node != null)
            servVer = node.getStringValue();

        platform.addMessage("Sunucu modülü yükleniyor...");

        server = (IServer) ModLoader.load(settings.getItem(Parameter.class, "Server").getStringValue());
        server.init(this);
        platform.addMessage("Sunucu modülü yüklendi.");

        platform.addMessage("Yazıcı modülü yükleniyor...");

        Collection<Printer> list;
        list = settings.getItems(Printer.class);
        for (Printer n : list) {
            IPrinter printer = (IPrinter) ModLoader.load(n.getStringValue());
            // Parametreler geçici olarak verildi
            printer.init(this, n);
            addPrinter(printer);
        }

        platform.addMessage("Yazıcı modülü yüklendi.");

        platform.addMessage("Yazdırma formları yükleniyor...");

        loadPageFormatInfo();

        platform.addMessage("Yazdırma formları yüklendi");

        platform.addMessage("Veritabanı oluşturuluyor...");

        initDatabase(dbUser, dbPassword);

        createDatabase(getTableClass());

        platform.addMessage("Veritabanı oluşturuldu.");

        platform.addMessage("Kamera modülü yükleniyor...");

        if (camera == null)
            camera = platform.createCamera();

        platform.addMessage("Kamera modülü yüklendi.");

        if (thread == null) {
            thread = new Thread(rthread);
            thread.start();
        }


        if (verCheckPeriod > 0) {

            updCheckTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (ver != null && verURL != null && appURL != null) {
                        UpdateWork updWork = new UpdateWork(false);
                        queueWork(updWork);
                    }
                }
            }, 5000, verCheckPeriod);

        }

        if (executor == null)
            executor = Executors.newSingleThreadExecutor();

    }


    @Override
    public void queueWork(Runnable r) {
        clbList.add(r);
        synchronized (sync) {
            sync.notify();
        }
    }

    private Runnable rthread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                while (!closed) {

                    synchronized (sync) {
                        sync.wait();
                    }

                    if (closed)
                        break;
                    for (Runnable r : clbList) {
                        if (closed)
                            break;
                        Exception ex = null;
                        try {
                            r.run();
                        } catch (Exception e) {
                            ex = e;
                        }
                        clbList.remove(r);
                    }
                }
            } catch (Exception e) {

            }
        }
    };


    @Override
    public void checkUpdate() {
        return;
        //UpdateWork updWork = new UpdateWork(true);
        //executor.submit(updWork);
    }

    private class UpdateWork implements Runnable {
        boolean msg = false;

        public UpdateWork(boolean msg) {

            this.msg = msg;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {

                if (ver != null && ver.length() > 0) {
                    if (!ver.equalsIgnoreCase(platform.getVersion(verURL))) {
                        platform.updateApp(appURL);
                    } else {
                        if (msg) ShowMessage(null, "Program güncel", "");
                    }
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void close() {

        closed = true;

        if (executor != null) {
            ExecutorService executor = this.executor;
            this.executor = null;
            executor.shutdown();
        }

        if (conn != null) {
            Connection conn = this.conn;
            this.conn = null;
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        for (IPrinter printer : printerList) {
            printer.close();
        }
        if (camera != null) {
            camera.close();
            camera = null;
        }

        if (updCheckTimer != null) {
            updCheckTimer.cancel();
            updCheckTimer = null;
        }

        if (thread != null && thread.isAlive()) {

            synchronized (thread) {
                thread.notify();
            }
            try {
                thread.join(1000);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                thread.interrupt();
            }
            thread = null;
        }
    }

    @Override
    public IApplication clone() {
        IPlatform platform = getPlatform();
        close();
        IApplication app = null;
        try {
            app = platform.createApplication(this.getClass().getName());
            Globals.app = app;

        } catch (Exception e) {
            platform.exit();
        }
        return app;
    }

    @Override
    public Connection getConnection() throws Exception {
        return conn;
    }

    @Override
    public IPlatform getPlatform() {
        return platform;
    }

    @Override
    public ICamera getCamera() {
        return camera;
    }

    @Override
    public void createForm(Object context, int formId) {
        platform.createForm(context, getFormClass(formId));
    }

    @Override
    public void loadSettings() throws Exception {

        // Kuruluma özel ayarlar. Sunucu ip, port vs.
        if (settings == null) {
            File file = new File(getAppDataPath(), "settings.xml");
            FileInputStream is = null;
            XMLLoader loader = null;
            try {
                is = new FileInputStream(file);
                loader = new Settings();
                loader.load(is, file.getAbsolutePath());
            } finally {

                if (is != null)
                    is.close();
            }
            settings = (ISettings) loader;
        }

        // Terminale özel ayarlar. Yazıcı ve optik port bluetooth MAC adresleri
        // gibi
        if (settings2 == null) {
            File file = new File(getAppDataPath(), "settings2.xml");
            FileInputStream is = null;
            XMLLoader loader = null;
            try {
                XMLLoader.create(file, "parameters");
                is = new FileInputStream(file);
                loader = new Settings2();
                loader.load(is, file.getAbsolutePath());

            } finally {

                if (is != null)
                    is.close();
            }
            settings2 = (ISettings2) loader;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadPageFormatInfo() throws Exception {

        pfiList.clear();
        pfList.addAll((List<PrintForm>) (Object) settings.getItems(PrintForm.class));

        FilenameFilter formFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".xml") && lowercaseName.indexOf("form") > -1) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File[] files = getAppDataPath().listFiles(formFilter);
        for (File file : files) {

            FileInputStream is = null;
            try {
                is = new FileInputStream(file);
                PageFormatInfo pfi = new PageFormatInfo();
                pfi.load(is, file.getAbsolutePath());
                pfi.Prepare();
                pfiList.add(pfi);
                PrintForm pf = getPrintForm(pfi.getFormType());
                if (pf != null)
                    pf.pfi = pfi;

            } finally {
                if (is != null)
                    is.close();
            }
        }

    }

    @Override
    public IEleman Login(LoginParam param) throws Exception {
        // TODO Auto-generated method stub

        Connection conn = getConnection();
        ILogin login = (ILogin) server;
        eleman = login.Login(param);
        if (eleman != null) DbHelper.Save(conn, eleman);
        loginParam = param;
        return eleman;
    }

    @Override
    public void Logout() {
        try {
            if (server != null) {
                ((ILogin) server).Logout();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public IEleman getEleman() {
        return eleman;
    }

    @Override
    public Date getTime() {
        return platform.getTime();
    }


    @Override
    public PageFormatInfo getPageFormatInfo(String formId) throws Exception {
        for (PageFormatInfo pfi : pfiList) {
            if (pfi.getFormType().compareToIgnoreCase(formId) == 0) {
                formId = pfi.getFormType();
                return pfi;
            }
        }
        throw new MobitException("Yazdırma formu tanımlanmamış!");
    }

    @Override
    public PrintForm getPrintForm(String formId) throws Exception {
        // TODO Auto-generated method stub
        for (PrintForm pf : pfList) {
            if (pf.getId().equals(formId)) {
                formId = pf.getId();
                return pf;
            }
        }
        return null;
        // throw new MobitException("Bu yazdırma için ayarlarda PrintForm tanımı
        // yapılmamış!");
    }

    @Override
    public List<PrintForm> getPrintFormList() {
        // TODO Auto-generated method stub
        return pfList;
    }

    @Override
    public <T> void Save(T db) throws Exception {
        DbHelper.Save(getConnection(), db);
    }

    @Override
    public <T> void Save(List<T> list) throws Exception {
        DbHelper.Save(getConnection(), list);
    }


    public void DeleteKarne(int KarneNo)throws Exception{
        DbHelper.DeleteKarne(getConnection(), KarneNo);
    }

    @Override
    public <T> List<T> Select(Class<?> c, Integer id) throws Exception {

        return DbHelper.Select(getConnection(), c, id);
    }

    @Override
    public <T> List<T> SelectAll(Class<?> c) throws Exception {

        return DbHelper.SelectAll(getConnection(), c);
    }

    @Override
    public String getHost() {

        return settings.getHost();

    }

    @Override
    public void setHost(String Host) {
        settings.setHost(Host);
    }

    @Override
    public int getPort() {
        return settings.getTcpPort();

    }

    @Override
    public void setPort(int Port) {

        settings.setTcpPort(Port);
    }

    @Override
    public String getApn() {

        return settings.getApn();
    }

    @Override
    public void setApn(String Apn) {
        settings.setApn(Apn);
    }

    @Override
    public String getPrinterMac(int printerNo) {
        return settings2.getPrinterMac(printerNo);

    }

    @Override
    public void setPrinterMac(int printerNo, String PrinterMac) {
        settings2.setPrinterMac(printerNo, PrinterMac);
    }

    @Override
    public String getPrinterPIN(int printerNo) {
        return settings2.getPrinterPIN(printerNo);

    }

    @Override
    public void setPrinterPIN(int printerNo, String Pin) {
        settings2.setPrinterPIN(printerNo, Pin);
    }

    @Override
    public String getOpticMac() {
        return settings2.getOpticMac();

    }

    @Override
    public void setOpticMac(String Mac) {
        settings2.setOpticMac(Mac);
    }

    @Override
    public String getOpticPIN(int printerNo) {
        return settings2.getPrinterPIN(printerNo);

    }

    @Override
    public void setOpticPIN(String Pin) {
        settings2.setOpticPIN(Pin);
    }

    @Override
    public String findMessage(int msgCode) {

        String msg;
        MsgInfo info = MsgInfo.findMsgInfo(msgCode);
        if (info == null) {
            info = MsgInfo.findMsgInfo(MsgInfo.HATA_MESAJ_ACIKLAMASI_YOK);
            msg = String.format("%d : %s", msgCode, info.getMessage());
        } else {
            msg = info.getMessage();
        }
        return msg;
    }

    @Override
    public void ShowMessage(IForm form, int msgCode, DialogMode mode, final int msg_id, int timeout, final IDialogCallback clb) {

        platform.ShowMessage(form, findMessage(msgCode), "Uyarı", mode, msg_id, timeout, clb);
    }

    @Override
    public void ShowMessage(IForm form, String msg, String title, DialogMode mode, final int msg_id,
                            final int timeout, final IDialogCallback clb) {
        platform.ShowMessage(form, msg, title, mode, msg_id, timeout, clb);
    }

    @Override
    public void ShowMessage(IForm form, String msg, String title) {
        platform.ShowMessage(form, msg, title);
    }

    @Override
    public void ShowMessage(IForm form, int msgCode) {

        platform.ShowMessage(form, findMessage(msgCode), "Uyarı");
    }

    @Override
    public void ShowException(IForm form, Throwable e,
                              DialogMode mode, final int msg_id, final IDialogCallback clb) {
        platform.ShowException(form, e, mode, msg_id, clb);
    }

    @Override
    public void ShowException(IForm form, Throwable e) {

        platform.ShowException(form, e);
    }

    @Override
    public ISettings getSettings() throws Exception {
        return settings;
    }

    protected void initDatabase(String user, String password) throws Exception {

        dbFile = new File(getAppDataPath(), dbName).toString();

        Connection conn = platform.createDbConnection(dbFile, user, password);

        if (conn != null) {
            int dbVer = DbHelper.getVersion(conn);
            if (dbVer < dbVersion) {
                conn.close();
                Thread.sleep(100);
                platform.deleteDatabase(dbFile);
                conn = platform.createDbConnection(dbFile, user, password);
                DbHelper.setVersion(conn, dbVersion);
            }
        }
        this.conn = conn;
    }


    protected void createDatabase(Class<?>[] tables) throws Exception {

        Statement stmt = null;
        boolean tran = false;

        try {

            conn.setAutoCommit(false);
            tran = true;

            stmt = conn.createStatement();

            List<String> cmds = DbHelper.CreateTables(tables);
            for (String cmd : cmds) stmt.execute(cmd);

            updateDatabase(conn, DbHelper.getVersion(conn));


            // if(tran) conn.commit();

        } catch (Exception e) {

            if (tran)
                conn.rollback();
            throw e;

        } finally {
            if (tran)
                conn.setAutoCommit(true);
            if (stmt != null)
                stmt.close();
        }
    }


    protected void updateDatabase(Connection conn, int dbVer) throws Exception {

    }


    @Override
    public void clearTables(Class<?>[] tables) throws Exception {

        boolean tran = false;
        try {

            conn.setAutoCommit(false);
            tran = true;

            for (Class<?> c : tables) {
                DbHelper.DeleteAll(conn, c);
            }
            // if(tran) conn.commit();

        } catch (Exception e) {

            if (tran)
                conn.rollback();
            throw e;

        } finally {
            if (tran)
                conn.setAutoCommit(true);
        }
    }

    protected IPrinter getPrinter(int printerNo) throws Exception {
        for (IPrinter printer : printerList)
            if (printer.getPrinterNo() == printerNo)
                return printer;
        throw new MobitException("Yazıcı bulunamadı!");
    }

    @Override
    public void addPrinter(IPrinter printer) throws Exception {
        printerList.add(printer);

    }

    IPrinter getPrintFormat(int formatNo) throws Exception {
        for (IPrinter printer : printerList)
            if (printer.getPrinterNo() == formatNo)
                return printer;
        throw new MobitException("Yazıcı bulunamadı!");
    }

    @Override
    public void addPrintFormat(IPrinterFormat format) throws Exception {
        printFormatList.add(format);

    }


    @Override

    public String printNew(List<PageData> pages, IIslem islem, boolean suret) throws Exception {
        // print 2.adım burada

        //zpl için yazdırma texti burada oluşturulacak
        //    String donecekDeger="";
        StringBuilder donecekDeger = new StringBuilder();
        for (PageData pd : pages) {

            PageFormatInfo pfi = getPageFormatInfo(pd.getFormType());
            IPrinter printer = getPrinter(pfi.getPrinterNo());
            File mSaveBit; // Your image file

            String sss = printer.printNew(pd, pfi, islem, suret);

            //+= olunca sayac değişikliği formu 2 kez yazdırıyordu = olunca tek yazdı kontrol edelim
//HÜSEYİN EMRE ÇEVİK
            donecekDeger.append(sss);

            //	donecekDeger=sss;
        }
        return donecekDeger.toString();
    }

    public String printNewOdf(PageData page, OlcuDevreForm odf) throws Exception {
        StringBuilder donecekDeger = new StringBuilder();

        PageFormatInfo pfi = getPageFormatInfo(page.getFormType());
        IPrinter printer = getPrinter(pfi.getPrinterNo());

        String sss = printer.printNewOdf(page, pfi, odf);

        donecekDeger.append(sss);

        return donecekDeger.toString();
    }


    @Override
    public ILocation getLocation() throws Exception {
        return platform.getLocation();
    }

    @Override
    public void showSoftKeyboard(IForm form, boolean show) {
        platform.showSoftKeyboard(form, show);
    }

    @Override
    public File getAppDataPath() {


        if (appDataPath != null) return appDataPath;
        appDataPath = new File(platform.getExternalStorageDirectory(), appPath);
        if (!appDataPath.exists()) appDataPath.mkdirs();
        return appDataPath;
		/*
		if(appDataPath != null) return appDataPath;
		if(platform.getId().equals(IPlatform.PlatformId.Android)){
			appDataPath = new File(platform.getExternalStorageDirectory());
		}
		else {
			
			appDataPath = new File(platform.getExternalStorageDirectory(), getClass().getPackage().getName());
		}
		return appDataPath;
		*/
    }

    @Override
    public File getAppPicturePath() {
        if (appPicturePath != null)
            return appPicturePath;
        appPicturePath = new File(getAppDataPath(), "resim");
        if (!appPicturePath.exists())
            appPicturePath.mkdir();
        return appPicturePath;
    }

    @Override
    public void saveSettings() throws Exception {
        if (settings != null) {
            settings.save();
            setSettingsChanged(true);
        }
        if (settings2 != null) {
            settings2.save();
            setSettingsChanged(true);
        }

    }

    /*
     * URL url = Thread.currentThread().getContextClassLoader().getResource(
     * "mobit/elc/mbs/medas/a.xml"); // resources dizini altındaki dosyaları
     * erişiyor InputStream is =
     * MedasApplication.class.getClassLoader().getResourceAsStream("b.xml");
     */

	/*
	@Override
	public void copySettingFiles(Class<?> cls, String[] files) throws Exception {

		ClassLoader clsLoader = cls.getClassLoader();

		byte[] buffer = new byte[1024];

		for (String file : files) {

			InputStream is = null;
			FileOutputStream os = null;
			try {
				// resources dizini altındaki dosyalara erişiliyor
				is = clsLoader.getResourceAsStream(file);
				if (is == null)
					continue;
				os = new FileOutputStream(new File(this.getAppDataPath(), file));
				int read;
				while ((read = is.read(buffer)) > 0)
					os.write(buffer, 0, read);

			} finally {
				if (os != null)
					os.close();
				if (is != null)
					is.close();
			}
		}

	}*/

    boolean checkVersiyon(ClassLoader clsLoader, String f) throws Exception {

        //if(Globals.isDeveloping()) return false;
        ISettings settings;
        File file = null;
        InputStream is = null;
        XMLLoader loader = null;
        String ver1 = "";
        String ver2 = "";

        try {

            file = new File(getAppDataPath(), f);
            if (!file.exists()) return false;

            is = new FileInputStream(file);
            loader = new Settings();
            loader.load(is);
            settings = (ISettings) loader;
            ver1 = settings.getVerisyon();

        } finally {

            if (is != null)
                is.close();
            if (loader != null)
                loader.close();
        }
        try {
            is = clsLoader.getResourceAsStream(f);
            loader = new Settings();
            loader.load(is);
            settings = (ISettings) loader;
            ver2 = settings.getVerisyon();

        } finally {

            if (is != null)
                is.close();
            if (loader != null)
                loader.close();
        }
        return ver1.equals(ver2);
    }

    @Override
    public void copySettingFiles(Class<?> cls, String[] files) throws Exception {


        ClassLoader clsLoader = cls.getClassLoader();

        if (!Globals.isDeveloping() && checkVersiyon(clsLoader, IDef.settingsFile)) return;

        byte[] buffer = new byte[1024];
        InputStream is = null;

        for (String file : files) {

            if (false && Globals.isDeveloping()) {
                if (file.compareToIgnoreCase(IDef.settingsFile) == 0) continue;
            }

            FileOutputStream os = null;
            try {
                // resources dizini altındaki dosyalara erişiliyor
                is = clsLoader.getResourceAsStream(file);
                if (is == null)
                    throw new MobitRuntimeException(String.format("resources dizini altındaki %s dosyası bulunamadı(rebuild et)!!!", file));

                os = new FileOutputStream(new File(this.getAppDataPath(), file));
                int read;
                while ((read = is.read(buffer)) > 0)
                    os.write(buffer, 0, read);

            } catch (Exception e) {
                throw e;
            } finally {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            }
        }
    }

    @Override
    public Class<?> getFormClass(int formId) {
        // TODO Auto-generated method stub
        for (FormClass fc : getFormClassList())
            if (fc.formId == formId)
                return fc.formClass;
        return null;
    }

    @Override
    public void restart() {
        Globals.app = null;
        close();
        platform.restart();
    }

    @Override
    public void setSettingsChanged(boolean settingsChanged) {
        this.settingsChanged = settingsChanged;
    }

    @Override
    public boolean getSettingsChanged() {
        return settingsChanged;
    }

    @Override
    public String rawTextPrepar(RawText text, PageData pd, IIslem islem) throws Exception {
        return "";
    }

    @Override
    public IEnum[] getEnumValues(int enumId) {
        try {
            Class<?> cls = getEnumClass(enumId);
            Method method = cls.getMethod("values");
            return (IEnum[]) method.invoke(null);
        } catch (Exception e) {

        }
        return new IEnum[0];
    }

    @Override
    public IEnum enumFromInteger(int enumId, int i) throws Exception {

        Class<?> cls = getEnumClass(enumId);
        Method method = cls.getMethod("fromInteger", int.class);
        return (IEnum) method.invoke(null, i);

    }

    @Override
    public abstract Class<?> getEnumClass(int enumId) throws Exception;

    @Override
    public void initForm(IForm form, FormInitParam param) {
        platform.initForm(form, param);
    }

    @Override
    public void initForm(IForm form) {

        initForm(form, param);
    }

    @Override
    public FormInitParam getFormInitParam() {
        return new FormInitParam(param.captionColor, param.captionText);
    }

    @Override
    public boolean checkException(Object obj) {
        return (obj instanceof Throwable);
    }

    @Override
    public boolean checkException(IForm form, Object obj, boolean msg) {
        if (!checkException(obj)) return false;
        if (msg) ShowException(form, (Throwable) obj);
        return true;
    }

    @Override
    public boolean checkException(IForm form, Object obj) {
        return checkException(form, obj, true);
    }

    @Override
    public void runAsync(IForm form, String message, String title, Callback rPre, Callback rDo, Callback rPost) {
        platform.runAsync(form, message, title, rPre, rDo, rPost);
    }

    @Override
    public void runAsyncOdf(String message, String title, Callback rPre, Callback rDo, Callback rPost) {
        platform.runAsyncOdf(message, title, rPre, rDo, rPost);
    }

    @Override
    public List<IMapMarker> getMarkerList() {
        return (List<IMapMarker>) objectMap.get(IDef.OBJ_MARKER_LIST);
    }

    @Override
    public void setMarkerList(List<IMapMarker> markerList) {
        setObject(IDef.OBJ_MARKER_LIST, markerList);
    }

    @Override
    public List<IMapMarker> getSelectedMarkerList() {
        return (List<IMapMarker>) objectMap.get(IDef.OBJ_SELECTED_MARKER_LIST);
    }

    @Override
    public void setSelectedMarkerList(List<IMapMarker> markerList) {
        setObject(IDef.OBJ_SELECTED_MARKER_LIST, markerList);
    }

    public void IslemTamamlandi(final IForm form, final IIslem islem, final boolean close, final boolean msg) {
        //activity = (Activity)form;
        final YaziciHataBildirimi yhb = new YaziciHataBildirimi();
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    //HÜSEYİN EMRE ÇEVİK 12.04.2021 (BİLDİRİM SESİ)
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone((Activity) form, notification);
                    r.play();
                    OlcuDevreForm odf = new OlcuDevreForm(); // H.Elif
                        runAsync(form, "Yazdırma bekleniyor...", "", null, new Callback() {

                            @Override
                            public Object run(Object obj) {
                                // TODO Auto-generated method stub
                                try {
                                    //HÜSEYİN EMRE ÇEVİK 7.2021 (TEKLİ YAZDIRMA YAVAŞ KAPATIP DENE)
                                    //Yazıcı bağlantısı açıksa kapat
                                    com.mobit.Yazici yazici = new Yazici();
                                    if (yazici.bluetooth_kontrol != 0)
                                        yazici.baglanti_kapat();

                                    OlcuDevreForm odf = new OlcuDevreForm(); // H.Elif
                                    if (odf.gettesisat_no() != -1) {
                                            synchronized (sync) {
                                                try { // iki çıktı alınıyor
                                                        printNewOdf(odf);
                                                        sync.wait(5000);
                                                        printNewOdf(odf);
                                                } catch (Exception e) { // TODO Auto-generated
                                                    // catch block
                                                }
                                            }
                                        //odf.AllClear();
                                    } else {
                                        printNew(islem, false);
                                    }
                                    //Yazdırma işlemi burada yaptırılıyor.

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    yhb.setHata_aciklama(e.toString());
                                    yhb.setYazdirma_dur(0);

                                    return e;
                                }
                                return null;
                            }

                        }, new Callback() {

                            @Override
                            public Object run(final Object obj) {
                                // TODO Auto-generated method stub
                                if (!Application.this.checkException(obj)) {
                                    if (close)
                                        form.finish();
                                    return null;
                                }
                                ShowException(form, (Throwable) obj, DialogMode.Ok, 1, new IDialogCallback() {

                                    @Override
                                    public void DialogResponse(int msg_id, DialogResult result) {
                                        // TODO Auto-generated
                                        // method stub
                                        if (close)
                                            form.finish();
                                    }

                                });
                                return obj;
                            }

                        });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    yhb.setHata_aciklama(e.toString());
                    yhb.setYazdirma_dur(0);
                    ShowException(form, e);
                }
            }

        };

        if (msg) {
            //İşlem Başarılı olduysa resimleri siliyoruz...
            String path = Globals.platform.getExternalStorageDirectory();

            File f = new File(path);
            File file[] = f.listFiles();
            int foto_control = 0;

            Lun_Control Ln = new Lun_Control();
            int isno = Ln.IsEmriNo;
            for (int i = 0; i < file.length; i++) {
                if (file[i].getName().equals("medas")) {

                } else {
                    if (isno == Integer.parseInt(file[i].getName().split("_")[0])) {
                        file[i].delete();
                    }
                }
            }
            //r.run();
            ShowMessage(form, "İşlem tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                @Override
                public void DialogResponse(int msg_id, DialogResult result) {
                    // TODO Auto-generated method stub
                    r.run();
                }

            });
        } else {
            r.run();
        }
    }

    public void IslemTamamlandiToplu(final IForm form, final IIslem islem, final boolean close, final boolean msg, final int timeout) {
        //activity = (Activity)form;
        final YaziciHataBildirimi yhb = new YaziciHataBildirimi();

        final Runnable r = new Runnable() {
            String message1 = "", message2 = "";

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    //HÜSEYİN EMRE ÇEVİK
                    if (timeout == 0) {
                        message1 = "Yazdırma bekleniyor...";
                        message2 = "";
                    } else {
                        message1 = null;
                        message2 = null;
                    }
                    runAsync(form, message1, message2, null, new Callback() {

                        @Override
                        public Object run(Object obj) {
                            // TODO Auto-generated method stub
                            try {

                                printNew(islem, false);

                                //Yazdırma işlemi burada yaptırılıyor.

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                yhb.setHata_aciklama(e.toString());
                                yhb.setYazdirma_dur(0);

                                return e;
                            }
                            return null;
                        }

                    }, new Callback() {

                        @Override
                        public Object run(final Object obj) {
                            // TODO Auto-generated method stub
                            if (!Application.this.checkException(obj)) {

                                if (close)
                                    form.finish();
                                return null;


                            }
                            ShowException(form, (Throwable) obj, DialogMode.Ok, 1, new IDialogCallback() {

                                @Override
                                public void DialogResponse(int msg_id, DialogResult result) {
                                    // TODO Auto-generated
                                    // method stub

                                    if (close)
                                        form.finish();

                                }

                            });
                            return obj;
                        }

                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    yhb.setHata_aciklama(e.toString());
                    yhb.setYazdirma_dur(0);
                    ShowException(form, e);
                }

            }

        };

        if (msg) {

            //İşlem Başarılı olduysa resimleri siliyoruz...

            String path = Globals.platform.getExternalStorageDirectory();

            File f = new File(path);
            File file[] = f.listFiles();
            int foto_control = 0;

            Lun_Control Ln = new Lun_Control();
            int isno = Ln.IsEmriNo;
            for (int i = 0; i < file.length; i++) {
                if (file[i].getName().equals("medas")) {

                } else {
                    if (isno == Integer.parseInt(file[i].getName().split("_")[0])) {
                        file[i].delete();
                    }

                }
            }

            //r.run();


            ShowMessage(form, "İşlem tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                @Override
                public void DialogResponse(int msg_id, DialogResult result) {
                    // TODO Auto-generated method stub

                    r.run();
                }

            });


        } else {
            r.run();
        }


    }

    void FormYazdir(String formText) {

        String printerMac = getPrinterMac(0);
        com.mobit.Yazici yazici = new Yazici();
        yazici.Yazdir(printerMac, formText);
        yazici.baglanti_kapat();

    }

}
