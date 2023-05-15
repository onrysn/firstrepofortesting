package com.mobit;

import com.mobit.IApplication;
import com.mobit.IPrinterFormat;
import com.mobit.ModLoader;
import com.mobit.PageData;
import com.mobit.PageFormatInfo;

import mobit.eemr.OlcuDevreForm;

public abstract class IPrinter implements AutoCloseable {

	protected IApplication app;
	protected IPrinterFormat format;
	protected Printer printer;
	
	public void init(IApplication app, Printer printer) throws Exception
	{
		this.app = app;
		this.printer = printer;
		
		format = (IPrinterFormat)ModLoader.load(printer.Emulation);
		format.init(app, printer);
	}
	
	public int getPrinterNo()
	{
		return printer.printerNo;
	}
	
	@Override
	public void close()
	{
		if(format != null){
			try {
				format.close();
			}
			catch (Exception e){

			}
		}
	}
	
	
	public abstract String printNew(PageData pd, PageFormatInfo pfi, IIslem islem, boolean suret) throws Exception;
	public abstract String printNewOdf(PageData pd, PageFormatInfo pfi, OlcuDevreForm odf) throws Exception;
	public abstract void print(String s) throws Exception;
}
