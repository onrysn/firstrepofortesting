package com.mobit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mobit.IApplication;
import com.mobit.IBluetooth;
import com.mobit.IPrinter;
import com.mobit.PageData;
import com.mobit.PageFormatInfo;

import mobit.eemr.OlcuDevreForm;


public class BluetoothPrinter extends IPrinter implements AutoCloseable {


	IBluetooth bth = null;
	Thread thread;
	File file;

	public BluetoothPrinter()
	{
		file = new File(Globals.platform.getExternalStorageDirectory(), "printout.txt");

	}



	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}
	
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(thread != null){
			thread.interrupt();
			thread = null;
		}
		if(bth != null){
			bth.close();
			bth = null;
		}

		super.close();

	}
	@Override
	public void init(IApplication app, Printer printer) throws Exception
	{
		super.init(app, printer);
		bth = app.getPlatform().createBluetooth();
		if(thread != null){
			thread.interrupt();
			thread = null;
		}
		thread = new Thread(ThreadProc);
		thread.start();
	}
	private Runnable ThreadProc = new Runnable() {
		@Override
		public void run() {
			try {
				while (true) {

					if(bth != null && bth.isConnected()){
						InputStream is = bth.get_InputStream();
						is.read();
					}
					else {
						Thread.sleep(1000);
					}
				}
			}
			catch (Exception e){
				return;
			}
		}
	};
	//sami
	public String printNew(PageData pd, PageFormatInfo pfi, IIslem islem, boolean suret) throws Exception
	{
		//print 3.adım
		  //format.prepare(pd, pfi, islem, suret);//bixolon için hazırlıyor

		final String str=format.prepareZPLFormat(pd, pfi, islem, suret);
	//	return  str;


		final byte [] b = format.getPrintStream();
		//if(b == null || b.length == 0) return"";

		if(Globals.isDeveloping()) utility.Log(file, b);

		Runnable r = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					OutputStream os = bth.get_OutputStream();
					os.write(b); // Bağlantı koptuktan bir müddet sonra IOException üretiyor!!!

				}
				catch(IOException e){
					if(e.getMessage().equals("Broken pipe")){
						app.ShowMessage(app.getPlatform().getActiveForm(), "Bluetooth bağlantısı kesildi!", "");
						bth.close();
					}
					else {
						app.ShowException(app.getPlatform().getActiveForm(), e);
					}
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					app.ShowException(app.getPlatform().getActiveForm(), e);
				}
			}

		};
		//Hüseyin emre çevik ( 01.06.2021 ) Açarsan Toplu gönderme hızı artıyor
//		bth.Open(IBluetooth.SPP, app.getPrinterMac(printer.printerNo), app.getPrinterPIN(printer.printerNo), r);




		/*
		int packet_size = 100;
		int packets = b.length / packet_size;
		int p;
		for(p = 0; p < packets; p++){
			os.write(b, packet_size*p, packet_size);
			//Thread.sleep(100);
		}
		if((b.length % packet_size) > 0) 
			os.write(b, packet_size*p, b.length-packet_size*p);		
		*/
		return  str;
	}

	@Override
	public String printNewOdf(PageData pd, PageFormatInfo pfi, OlcuDevreForm odf) throws Exception {
		final String str=format.prepareZPLFormatOdf(pd, pfi, odf);

		final byte [] b = format.getPrintStream();
		if(Globals.isDeveloping()) utility.Log(file, b);

		Runnable r = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					OutputStream os = bth.get_OutputStream();
					os.write(b); // Bağlantı koptuktan bir müddet sonra IOException üretiyor!!!
				}
				catch(IOException e){
					if(e.getMessage().equals("Broken pipe")){
						app.ShowMessage(app.getPlatform().getActiveForm(), "Bluetooth bağlantısı kesildi!", "");
						bth.close();
					}
					else {
						app.ShowException(app.getPlatform().getActiveForm(), e);
					}
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					app.ShowException(app.getPlatform().getActiveForm(), e);
				}
			}

		};
		return  str;
	}


	public void print(String s) throws Exception
	{
		
		//bth.Open(IBluetooth.SPP, app.getPrinterMac(printer.printerNo), app.getPrinterPIN(printer.printerNo));
		OutputStream os = bth.get_OutputStream();
		if(os == null) throw new MobitException("Yazıcıya bağlanılamadı!");
		
		String sb = "This line is printed as raw text.\n\n\n\n\n\n\n\n\n\n\n";
		
		byte [] b = sb.toString().getBytes();
		os.write(b);
		
		/*
		int packet_size = 100;
		int packets = b.length / packet_size;
		int p;
		for(p = 0; p < packets; p++){
			os.write(b, packet_size*p, packet_size);
			//Thread.sleep(100);
		}
		if((b.length % packet_size) > 0) 
			os.write(b, packet_size*p, b.length-packet_size*p);		
			*/
		
		
	}
	
	
	
}
