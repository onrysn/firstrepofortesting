package com.mobit;

public interface IDef {

	public static final String HOST = "Host";
	public static final String PORT = "Port";
	public static final String APN = "Apn";
	public static final String VER = "ver";
	public static final String VERURL = "verURL";
	public static final String APPURL = "appURL";
	public static final String VERCHECKPERIOD = "verCheckPeriod";
	public static final String SERVVER = "servVer";
	public static final String PRINTER_MAC = "PrinterMac_";
	public static final String PRINTER_PIN = "PrinterPin_";
	
	public static final int FORM_LOGIN = 10;
	public static final int FORM_AYARLAR = 11;
	public static final int FORM_SUNUCU_AYAR = 12;
	public static final int FORM_YAZICI_AYAR = 13;
	public static final int FORM_OPTIK_PORT_AYAR = 14;
	public static final int FORM_BLUETOOTH_DISCOVER = 15;
	public static final int FORM_FOTOGRAF = 16;
	public static final int FORM_GPS = 17;
	public static final int FORM_WEB_BROWSER = 18;
	public static final int FORM_MAP = 19;
	public static final int FORM_SIFRE_DEGISTIR = 20;
	public static final int FORM_DETAY = 21;
	public static final int FORM_RAPOR = 22;
	public static final int FORM_SAHA_ISEMRI = 25;
	public static final int FORM_CAMERA_BARCODE_SCAN = 23;
	public static final int FORM_SEND_FILE_DEMO = 24;
	public static final int FORM_OKUMA_ENDEKS = 203;

	
	public static final int OBJ_CAMERA = 1;
	public static final int OBJ_GPS = 2;
	public static final int OBJ_DETAY = 3;
	public static final int OBJ_MARKER_LIST = 4;
	public static final int OBJ_SELECTED_MARKER_LIST = 5;
	public static final int OBJ_ISLEM_RAPOR = 6;
	
	public static final String settingsFile = "settings.xml";
	
	//public static final String LOG_FILE_NAME = "log.txt";
	
	public static final int WRN_KUYRUGA_EKLENDI = 1000;
}
