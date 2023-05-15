package com.mobit;

import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mobit.IEleman;
import com.mobit.IPlatform;
import com.mobit.IPrinter;
import com.mobit.IPrinterFormat;
import com.mobit.ISettings;
import com.mobit.PageData;
import com.mobit.PageFormatInfo;

import mobit.eemr.OlcuDevreForm;


public interface IApplication {


	boolean isTest();

	String getVersiyon();
	
	Item[] getMainMenuItems();
	
	void setMainMenuItems(Item[] menuItems);
	

	void init(IPlatform platform, Object obj) throws Exception;

	void queueWork(Runnable r);

	void checkUpdate();
	
	void close();

	IApplication clone();
	
	Connection getConnection() throws Exception;

	IPlatform getPlatform();
	
	ICamera getCamera();
	
	void createForm(Object context, int formId);
	
	void loadSettings() throws Exception;

	void loadPageFormatInfo() throws Exception;
	
	
	IEleman getEleman();

	
	Date getTime();
	
	PageFormatInfo getPageFormatInfo(String formId) throws Exception;
	
	PrintForm getPrintForm(String formId) throws Exception;
	
	List<PrintForm> getPrintFormList();
	
	<T> void Save(T db) throws Exception;
	
	<T> void Save(List<T> list) throws Exception;
	
	<T> List<T> Select(Class<?> clazz, Integer id) throws Exception;
	
	<T> List<T> SelectAll(Class<?> c) throws Exception;
	
	String getHost();

	void setHost(String Host);

	int getPort();

	void setPort(int Port);

	String getApn();

	void setApn(String Apn);

	String getPrinterMac(int printerNo);

	void setPrinterMac(int printerNo, String PrinterMac);

	String getPrinterPIN(int printerNo);

	void setPrinterPIN(int printerNo, String Pin);

	String getOpticMac();

	void setOpticMac(String Mac);

	String getOpticPIN(int printerNo);

	void setOpticPIN(String Pin);

	String findMessage(int msgCode);


	void ShowMessage(IForm form, int msgCode, DialogMode mode, final int msg_id, final int timeout, final IDialogCallback clb);

	void ShowMessage(IForm form, String msg, String title, DialogMode mode, final int msg_id, int timeout, final IDialogCallback clb);

	void ShowMessage(IForm form, String msg, String title);

	void ShowMessage(IForm form, int msgCode);
	
	void ShowException(IForm form, Throwable e, DialogMode mode, final int msg_id, final IDialogCallback clb);

	void ShowException(IForm obj, Throwable e);

	ISettings getSettings() throws Exception;

	void clearTables(Class<?>[] tables) throws Exception;
	
	void addPrinter(IPrinter printer) throws Exception;

	void addPrintFormat(IPrinterFormat format) throws Exception;

	String printNew(List<PageData> pages, IIslem islem, boolean suret) throws Exception;

	String printNew(IIslem islem, boolean suret) throws Exception;

	String printNewOdf(PageData pages, OlcuDevreForm odf) throws Exception;

	String printNewOdf(OlcuDevreForm odf) throws Exception;

	Object getIslemData(String key, PageData pd, IIslem islem) throws Exception;

	ILocation getLocation() throws Exception;


	void showSoftKeyboard(IForm form, boolean show);

	File getAppDataPath();

	File getAppPicturePath();

	void saveSettings() throws Exception;


	void copySettingFiles(Class<?> cls, String[] files) throws Exception;

	// Sunucu entegrasyonu
	List<PageData> getPrintPageList(Object obj, IIslem islem, String formType) throws Exception;

	// Sunucu entegrasyonu
	Object getPrintTestData(String formId) throws Exception;

	// H. Elif
	PageData getOdfPrintPageList(Object obj, OlcuDevreForm odf) throws Exception;


	// Firma düzeyinde kullanılacak ayar dosyaları tanımlamak için.
	// Dizindeki dosyalar alınabilirse buna da gerek kalmayacaktır
	String[] getSettingFiles();

	Class<?>[] getTableClass();
	
	Class<?>[] getClearTableClass();

	Class<?> getFormClass(int formId);

	// Platform entegrasyonu
	FormClass[] getFormClassList();

	void restart();

	void setSettingsChanged(boolean settingsChanged);

	boolean getSettingsChanged();

	String rawTextPrepar(RawText text, PageData pd, IIslem islem) throws Exception;

	IEnum[] getEnumValues(int enumId);

	IEnum enumFromInteger(int enumId, int i) throws Exception;

	Class<?> getEnumClass(int enumId) throws Exception;

	void initForm(IForm form, FormInitParam param);
	
	void initForm(IForm form);
	
	void fetchParameter()throws Exception;

	IIslemMaster newIslemMaster()throws MobitException;

	boolean checkException(Object obj);

	boolean checkException(IForm form, Object obj, boolean msg);

	boolean checkException(IForm form, Object obj);
	
	FormInitParam getFormInitParam();
	
	void runAsync(IForm form, String message, String title, Callback rPre, Callback rDo, Callback rPost);

	void runAsyncOdf(String message, String title, Callback rPre, Callback rDo, Callback rPost);

	void setObject(int id, Object obj);
	
	Object getObject(int id);
	
	void requestPermission(IForm form);
	
	void permissionGrant(int requestCode, String permissions[], int[] grantResults);
	
	IServer getServer(int serverId);
	
	IIslem saveIslem(IIslem islem) throws Exception;
	
	List<IIslem> getIslem(IRecordStatus durum) throws Exception;
	
	List<IIslem> getIslem(int isemriNo) throws Exception;
	
	void sendIslem(IForm form, IIslem islem, Callback clb, int timeout) throws Exception;

	void IslemTamamlandi(final IForm form, final IIslem islem,
			final boolean close, final boolean msg);

	void IslemTamamlandiToplu(final IForm form, final IIslem islem,
							  final boolean close, final boolean msg, final int timeout);

	IBarcode newBarcodeObject();

	Object getPrintZPLFormatData(String s) throws Exception;
}
