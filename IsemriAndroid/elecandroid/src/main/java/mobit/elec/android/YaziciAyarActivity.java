package mobit.elec.android;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.mobit.Yazici;




import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IForm;
import com.mobit.PageData;
import com.mobit.PrintForm;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.io.InputStream;

import mobit.eemr.OlcuDevreForm;


public class YaziciAyarActivity extends AppCompatActivity implements IForm, OnClickListener {
	private UIHelper helper = new UIHelper(this);
	IApplication app = (IApplication) Globals.app;

	EditText editMac1;
	Button buttonArama1;
	EditText editMac2;
	Button buttonArama2;
	Button buttonKaydet;
	Spinner formlar;
	Button buttonTestYazdir;
	Button buttonSendGraphic;
	Button yaziciayaryukle;
    RadioButton radio1;
	RadioButton radio2;
	RadioButton radio3;
	SpinnerAdapter spinnerAdapter;
	java.sql.Connection connection;
	int GraphicControl =0;
	String Mesaj;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yazici_ayar);

		app.initForm(this);
		editMac1 = (EditText) findViewById(R.id.editMac1);
		buttonArama1 = (Button) findViewById(R.id.buttonArama1);
		editMac2 = (EditText) findViewById(R.id.editMac2);
		buttonArama2 = (Button) findViewById(R.id.buttonArama2);
		buttonKaydet = (Button) findViewById(R.id.buttonKaydet);
		formlar = (Spinner) findViewById(R.id.formlar);
		buttonTestYazdir = (Button) findViewById(R.id.buttonTestYazdir);
		buttonSendGraphic = (Button) findViewById(R.id.buttonSendGraphic);
		yaziciayaryukle =(Button) findViewById(R.id.yaziciayaryukle);

		editMac1.setText(app.getPrinterMac(0));
		editMac2.setText(app.getPrinterMac(1));

		buttonArama1.setOnClickListener(this);
		buttonArama2.setOnClickListener(this);
		buttonKaydet.setOnClickListener(this);
		buttonTestYazdir.setOnClickListener(this);
		buttonSendGraphic.setOnClickListener(this);
		yaziciayaryukle.setOnClickListener(this);

		spinnerAdapter =  new ArrayAdapter(this, android.R.layout.simple_spinner_item, app.getPrintFormList());
		formlar.setAdapter(spinnerAdapter);




	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		if (id == R.id.buttonArama1) {
			Intent intent = new Intent(this, BluetoothDiscoverActivity.class);
			intent.putExtra(BluetoothDiscoverActivity.CAPTION, "Yazıcı Arama");
			startActivityForResult(intent, 0);
		}
		if (id == R.id.buttonArama2) {
			Intent intent = new Intent(this, BluetoothDiscoverActivity.class);
			intent.putExtra(BluetoothDiscoverActivity.CAPTION, "Yazıcı Arama");
			startActivityForResult(intent, 1);
		} else if (id == R.id.buttonKaydet) {

			app.setPrinterMac(0, editMac1.getText().toString());
			app.setPrinterMac(1, editMac2.getText().toString());

			try {

				app.saveSettings();
				app.restart();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				app.ShowException(this, e);
				return;
			}
		} else if (id == R.id.buttonTestYazdir) {
			PrintForm form =  (PrintForm)formlar.getSelectedItem();
			String FormName=form.toString();
			if(form == null){
				app.ShowMessage(this, "Listeden yazdırılacak formu seçin!", "");
				return;
			}

			android.app.AlertDialog.Builder adb=new android.app.AlertDialog.Builder(YaziciAyarActivity.this);
			adb.setTitle("Endeksör Fotoğraf Yazdırma Durumu");
			adb.setMessage("Endeksör(Okuyucu) kullanıcısı iseniz 'İMZA KAYDET' butonu ile grafikleri yazıcıya göndermelisiniz."+"\nİşlemi gerçekleştirmek ister misiniz?");
			adb.setNegativeButton("Evet",new android.app.AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					com.mobit.Yazici yazici = new Yazici();
					yazici.PerakendeZplGraphicSend(editMac1.getText().toString());
					return;
				}});
			adb.setPositiveButton("Hayır", new android.app.AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//pass
				}});
			adb.show();
			try {

				String s = form.toString();
				Object obj = app.getPrintZPLFormatData(form.toString());


				final List<PageData> list = app.getPrintPageList(obj, null, form.toString());

				//app.getConnection();
				//String arr= app.getAppDataPath(appDataPath,"pre");
		//	app.runAsync();
//				Connection connection = null;
//				connection = new BluetoothConnection(editMac1.getText().toString());
//				connection.open();
//				ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
//				testSendFile(printer,obj.toString());  //,obj.toString()
//				connection.close();
				String strr=app.printNew(list, null, false);

				printTest(strr,FormName);

			} catch (Exception e) {
				app.ShowException(this, e);
			}
		}else  if (id == R.id.buttonSendGraphic){
			com.mobit.Yazici yazici = new Yazici();
			yazici.PerakendeZplGraphicSend(editMac1.getText().toString());
		}
		else if(id == R.id.yaziciayaryukle)
		{
			//HÜSEYİN EMRE ÇEVİK 24.03.2021 ( YAZICILARDA BLUETOOTH KAPANMA SORUNU )
			com.mobit.Yazici yazici = new Yazici();
			yazici.denemeZebra(editMac1.getText().toString());

		}

	}




	public static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";
	void printTest(String obj, String FormName) {

			com.mobit.Yazici yazici=new Yazici();
//HÜSEYİN EMRE ÇEVİK DÜZELT
		//	for(int i=0;i<5;i++){
				yazici.Yazdir(editMac1.getText().toString(), obj);

	//	}
		yazici.baglanti_kapat();


		/*String macAddress = Settings.Secure.getString(getContentResolver(), SECURE_SETTINGS_BLUETOOTH_ADDRESS);
		BluetoothConnection connection = new BluetoothConnection(editMac1.getText().toString());

		try {
			((BluetoothConnection) connection).open();
			if (((BluetoothConnection) connection).isConnected()) {
				printer = ZebraPrinterFactory.getInstance(((BluetoothConnection) connection));

				// Open the connection - physical connection is established here.


               String zpl=obj;

				printer.sendCommand("! U1 setvar \"device.languages\" \"zpl\"\r\n");


				Thread.sleep(500);

				// Send the data to printer as a byte array.
//				printer.sendCommand("^XA^FO20,20^A0N,25,25^FD"+obj+"^FS^XZ");
				printer.sendCommand(zpl);
				connection.close();

//                setPrinterLanguageToZPL();
				//              storeFormat();
				// if (getStatusBeforePrint()) {
				// printFormat();
				// getStatusAfterPrint();
				// }
			}
			else {


			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
			//  myLabel.setText("yazdırma tamamlandı");
		}
*/
	}

	/*void storeFormat() throws ConnectionException {
		((ZebraPrinter) printer).sendFileContents("/sdcard/documents/NAME.ZPL");


	}
	/*
	/*void printFormat() throws ConnectionException {

		String format = "E:NAME.ZPL";
		Map<Integer, String> vars = new HashMap<Integer, String>();
		vars.put(1, "deneme");
		printer.printStoredFormat(format, vars);
	}
	boolean getStatusBeforePrint(){
		try {
			PrinterStatus printerStatus= printer.getCurrentStatus();
			if (printerStatus.isReadyToPrint) {
				return true;
			} else if (printerStatus.isHeadOpen) {

				Mesaj="Printer Head Open" ;
			} else if (printerStatus.isPaused) {
				Mesaj="Printer is Paused" ;


			} else if (printerStatus.isPaperOut) {

				Mesaj="Printer Media Out" ;

			}
			return false;
		} catch (ConnectionException e) {

			Mesaj="Error during print" ;

			disconnect();
			return false;
		}
	}
	boolean getStatusAfterPrint(){
		try{
			PrinterStatus printerStatus= printer.getCurrentStatus();
			while ((printerStatus.numberOfFormatsInReceiveBuffer> 0) && (printerStatus.isReadyToPrint)) {
				printerStatus= printer.getCurrentStatus();
			}
			if (printerStatus.isReadyToPrint) {
				Mesaj="Test Print Sent" ;


				return true;
			} else if (printerStatus.isHeadOpen) {

				Mesaj="Printer Head Open" ;

			} else if (printerStatus.isPaused) {
				Mesaj="Printer is Paused" ;


			} else if (printerStatus.isPaperOut) {
				Mesaj="Printer Media Out" ;

			}
			return false;
		} catch (ConnectionException e) {
			Mesaj="Error during print" ;


			disconnect();
			return false;
		}
	}
	void disconnect() {
		try {
			Mesaj="Disconnecting" ;


			if ((((BluetoothConnection) connection) != null) && (((BluetoothConnection) connection).isConnected())){
				((BluetoothConnection) connection).close();
			}
			Mesaj="Yazdırmaya hazır";
		} catch (ConnectionException e) {
			Mesaj="COMM Error! Disconnected" ;


		} finally {
// enable any UI elements to allow printing again
		}
	}


	void cnXmlPrintingExample() {
		// The possible inputs to the one-line XML printing function(s)

		String destinationDevice = editMac1.getText().toString();

		String templateFilename = "C:\\CnXmlPrinterExampleTemplate.zpl";
		String defaultQuantityString = "1";
		boolean verbose = true;

		// The outputDataStream argument may be null, in which case the data generated by the XmlPrinter class will
		// not be logged but will be sent to the destination device.

		try {
			XmlPrinter.print(destinationDevice, getSampleCnXmlData(), templateFilename, defaultQuantityString, null, verbose);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	 private static ByteArrayInputStream getSampleCnXmlData() {

		String sampleXmlData =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<file _FORMAT=\"XmlExamp.zpl\">"
						+ " <label>\n"
						+ "     <variable name=\"Customer Name\">Name</variable>"
						+ "     <variable name=\"Component Name\">Component Name</variable>"
						+ "     <variable name=\"Vendor Name\">Vendor Name</variable>"
						+ "     <variable name=\"Vendor ID\">Vendor Id</variable>"
						+ "     <variable name=\"Invoice Number\">Invoice Number</variable>"
						+ "  </label>\n"
						+ "</file>";


		return new ByteArrayInputStream(sampleXmlData.getBytes());
	}





/*	private void testSendFile(ZebraPrinter printer,String deger) {
		try {
			//File filepath = getFileStreamPath("acma_form.xml");

		//	FileOutputStream os = this.openFileOutput("acma_form.xml", Context.MODE_PRIVATE);
		//	FileOutputStream os = null;

		//	File appPath = this.getApplication().getApplicationContext().getFilesDir().getAbsoluteFile();
		// 	String path = this.getFilesDir().getAbsolutePath();
		//	Context ctx = getApplicationContext();
			//String yourFilePath = this.getFilesDir() + "/medas/" + "acma_form.xml";
		//	File yourFile = new File( yourFilePath );
	//		File sdcard = Environment.getExternalStorageDirectory();
		//	File file = new File(sdcard,"file.txt");
		//	String appPath = App.getApp().getApplicationContext().getFilesDir().getAbsolutePath();
			//File tt = new File(this.getAppDataPath(), "acma_form.xml");
			//File file = new File(a);
		//	FileInputStream fis = new FileInputStream(new File("/data/mobit.elec.mbs.medas.android/files/medas/acma_form.xml"));
			//	os = new FileOutputStream(new File(this.getAppDataPath(), "acma_form.xml"));


 		//	 File temp = File.createTempFile("test",".xml");
 	//		 String apath=temp.getAbsolutePath();

		//	File file = new File(getAppDataPath(), "acma_form.xml");

			File file = new File(app.getAppDataPath(), "acma_form.xml");

			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(new FileOutputStream(new File(getExternalFilesDir(null), "acma_form.xml")));


			createDemoFile(printer,file.getPath());
			printer.sendFileContents(deger);
		//	createDemoFile(printer, "sass.xml" );
		//	printer.sendFileContents(os.);
			SettingsHelper.saveBluetoothAddress(this, editMac1.getText().toString());
			//SettingsHelper.saveIp(this, getTcpAddress());
		//	SettingsHelper.savePort(this, getTcpPortNumber());

		} catch (ConnectionException e1) {
			helper.showErrorDialogOnGuiThread("Error sending file to printer");
		} catch (IOException e) {
			helper.showErrorDialogOnGuiThread("Error creating file");
		}
	}*/
/*
	private void testSendFile(ZebraPrinter printer,String FormName) {

        try {
            //File filepath = getFileStreamPath("TEST.LBL");
            //createDemoFile(printer, "TEST.LBL");
            InputStream i=getAssets().open(FormName);
            File filepath=new File(FormName);
            URL fileURL = getClass().getClassLoader().getResource(FormName);
            String fileName = fileURL.getFile();
            String filePath = fileURL.getPath();
            String ap=filepath.getAbsolutePath();
            if(filepath.exists())
                printer.sendFileContents(filepath.getAbsolutePath());

        } catch (Exception e) {

        }


/*



//		try {
////			File filepath = getFileStreamPath("TEST.LBL");
////			createDemoFile(printer, "TEST.LBL",degerim);
////		//	printer.sendFileContents(filepath.getAbsolutePath());
////			SettingsHelper.saveBluetoothAddress(this, editMac1.getText().toString());
////		//	SettingsHelper.saveIp(this, getTcpAddress());
////		//	SettingsHelper.savePort(this, getTcpPortNumber());
////
////		//} catch (ConnectionException e1) {
////		//	helper.showErrorDialogOnGuiThread("Error sending file to printer");
////		} catch (IOException e) {
////			helper.showErrorDialogOnGuiThread("Error creating file");
////		}
	}

	private void createDemoFile(ZebraPrinter printer, String fileName) throws IOException {

		FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);
		String impresion= "! 0 200 200 639 1\r\n"+
				"PW 830\r\n"+
				"TONE 0\r\n"+
				"SPEED 0\r\n"+
				"ON-FEED IGNORE\r\n"+
				"NO-PACE\r\n"+
				"BAR-SENSE\r\n"+
				"T 7 0 315 144 Var_bodega\r\n"+
				"T 7 0 542 241 Var_fechall\r\n"+
				"T 7 0 529 137 Var_medidas\r\n"+
				"T 7 0 313 243 Var_contenedor\r\n"+
				"T 7 0 104 240 Var_huacal\r\n"+
				"T 0 3 544 214 F.LLEGADA:\r\n"+
				"T 7 0 104 144 Var_lotesap\r\n"+
				"T 0 3 531 110 MEDIDAS:\r\n"+
				"T 0 3 315 215 CONTENEDOR:\r\n"+
				"T 0 3 315 112 BODEGA:\r\n"+
				"T 0 3 105 212 HUACAL:\r\n"+
				"T 7 0 104 59 30021832 - GRIS 4MM 1.829*3.302 PK\r\n"+
				"T 0 3 104 114 LOTE SAP:\r\n"+
				"T 0 3 102 26 DESCRIPCION DEL MATERIAL\r\n"+
				"BT 7 0 3\r\n"+
				"B 128 2 30 30 206 323 ABCDEF12345\r\n"+
				"BT 7 0 3\r\n"+
				"B 128 2 30 30 220 417 ABCDEF123456\r\n"+
				"PRINT\r\n";
		byte[] configLabel = null;
		String cpclConfigLabel = impresion;
		configLabel = cpclConfigLabel.getBytes();
		os.write(configLabel);
		os.flush();
		os.close();

//		FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);
//		byte[] configLabel = null;
//		PrinterLanguage pl = printer.getPrinterControlLanguage();
//		if (pl == PrinterLanguage.ZPL) {
//			configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes() ;
//		} else if (pl == PrinterLanguage.CPCL) {
//			String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
//			configLabel = cpclConfigLabel.getBytes();
//		}
//		os.write(configLabel);
//		os.write(mesajim.getBytes());
//		os.flush();
//		os.close();
	}


/*

	private void createDemoFile(ZebraPrinter printer, String fileName) throws IOException {

		FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);

		byte[] configLabel = null;

		PrinterLanguage pl = printer.getPrinterControlLanguage();
		if (pl == PrinterLanguage.ZPL) {
			configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
		} else if (pl == PrinterLanguage.CPCL) {
			String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
			configLabel = cpclConfigLabel.getBytes();
		}
		os.write(configLabel);
		os.flush();
		os.close();
	}
*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 0:
				editMac1.setText(intent.getStringExtra(BluetoothDiscoverActivity.DEVICE));
				break;
			case 1:
				editMac2.setText(intent.getStringExtra(BluetoothDiscoverActivity.DEVICE));
				break;
			}
		}

	}
}





//Projenin orjinal halinden alınan kod satırları
//package mobit.elec.android;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//
//import com.mobit.Callback;
//import com.mobit.Globals;
//import com.mobit.IApplication;
//import com.mobit.IForm;
//import com.mobit.PageData;
//import com.mobit.PrintForm;
//
//import java.util.List;
//
//public class YaziciAyarActivity extends AppCompatActivity implements IForm, OnClickListener {
//
//	IApplication app = (IApplication) Globals.app;
//
//	EditText editMac1;
//	Button buttonArama1;
//	EditText editMac2;
//	Button buttonArama2;
//	Button buttonKaydet;
//	Spinner formlar;
//	Button buttonTestYazdir;
//
//	SpinnerAdapter spinnerAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_yazici_ayar);
//
//		app.initForm(this);
//
//		editMac1 = (EditText) findViewById(R.id.editMac1);
//		buttonArama1 = (Button) findViewById(R.id.buttonArama1);
//		editMac2 = (EditText) findViewById(R.id.editMac2);
//		buttonArama2 = (Button) findViewById(R.id.buttonArama2);
//		buttonKaydet = (Button) findViewById(R.id.buttonKaydet);
//		formlar = (Spinner) findViewById(R.id.formlar);
//		buttonTestYazdir = (Button) findViewById(R.id.buttonTestYazdir);
//
//		editMac1.setText(app.getPrinterMac(0));
//		editMac2.setText(app.getPrinterMac(1));
//
//		buttonArama1.setOnClickListener(this);
//		buttonArama2.setOnClickListener(this);
//		buttonKaydet.setOnClickListener(this);
//		buttonTestYazdir.setOnClickListener(this);
//
//		spinnerAdapter =  new ArrayAdapter(this, android.R.layout.simple_spinner_item, app.getPrintFormList());
//		formlar.setAdapter(spinnerAdapter);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		return false;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == android.R.id.home) {
//			onBackPressed();
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	public void onBackPressed() {
//
//		super.onBackPressed();
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		int id = arg0.getId();
//		if (id == R.id.buttonArama1) {
//			Intent intent = new Intent(this, BluetoothDiscoverActivity.class);
//			intent.putExtra(BluetoothDiscoverActivity.CAPTION, "Yazıcı Arama");
//			startActivityForResult(intent, 0);
//		}
//		if (id == R.id.buttonArama2) {
//			Intent intent = new Intent(this, BluetoothDiscoverActivity.class);
//			intent.putExtra(BluetoothDiscoverActivity.CAPTION, "Yazıcı Arama");
//			startActivityForResult(intent, 1);
//		} else if (id == R.id.buttonKaydet) {
//
//			app.setPrinterMac(0, editMac1.getText().toString());
//			app.setPrinterMac(1, editMac2.getText().toString());
//
//			try {
//
//				app.saveSettings();
//				app.restart();
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				app.ShowException(this, e);
//				return;
//			}
//		} else if (id == R.id.buttonTestYazdir) {
//
//			PrintForm form =  (PrintForm)formlar.getSelectedItem();
//			if(form == null){
//				app.ShowMessage(this, "Listeden yazdırılacak formu seçin!", "");
//				return;
//			}
//			try {
//
//				String s = form.toString();
//				Object obj = app.getPrintTestData(form.toString());
//				final List<PageData> list = app.getPrintPageList(obj, null, form.toString());
//				app.runAsync(this, "Yazıcıya gönderiliyor...", "", null, new Callback() {
//					@Override
//					public Object run(Object obj) {
//						try {
//
//							app.print(list, null, false);
//
//						} catch (Exception e) {
//							return e;
//						}
//						return null;
//					}
//				}, new Callback() {
//					@Override
//					public Object run(Object obj) {
//						if(app.checkException(YaziciAyarActivity.this, obj)) return null;
//						return null;
//					}
//				});
//
//
//
//			} catch (Exception e) {
//				app.ShowException(this, e);
//			}
//
//		}
//
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		super.onActivityResult(requestCode, resultCode, intent);
//
//		if (resultCode == Activity.RESULT_OK) {
//			switch (requestCode) {
//				case 0:
//					editMac1.setText(intent.getStringExtra(BluetoothDiscoverActivity.DEVICE));
//					break;
//				case 1:
//					editMac2.setText(intent.getStringExtra(BluetoothDiscoverActivity.DEVICE));
//					break;
//			}
//		}
//
//	}
//}