package com.mobit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.mobit.IApplication;
import com.mobit.IPlatform;

public class Globals {

	public static final boolean init;
	public static void setDeveloping(boolean developing)
	{
		Globals.developing = developing;
	}
	public static boolean isDeveloping()
	{
		//sami
		//return developing;
		return false;
	}
	
	private static boolean developing = false;
	public static IPlatform platform = null;
	public static IApplication app = null;
	private static NumberFormat nf;
	private static DecimalFormat df;
	private static NumberFormat cf;
	private static DecimalFormatSymbols dfs;
	
	public static float textSize = 24;
	
	public static final Locale enLocale = new Locale("en", "EN");
	public static final Locale trLocale = new Locale("tr", "TR");
	public static Locale locale = trLocale;
	
	public static final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
	public static final TimeZone trTimeZone = TimeZone.getTimeZone("Asia/Baghdad");
	public static TimeZone timeZone = utcTimeZone;//trTimeZone;
	
	public static final NumberFormat enNumberFormat = DecimalFormat.getInstance(enLocale);
	public static final NumberFormat trNumberFormat = DecimalFormat.getInstance(trLocale);
	
	public static final NumberFormat enCurrencyFormat = NumberFormat.getCurrencyInstance(enLocale);
	public static final NumberFormat trCurrencyFormat = NumberFormat.getCurrencyInstance(trLocale);
	
	public static SimpleDateFormat dateFmt;
	public static SimpleDateFormat dateTimeFmt;

	private static long realDate = 0;
	private static long refDate = 0;

	static Date date;
	static {

		init = true;
		dateFmt = new SimpleDateFormat("dd/MM/yyyy");
		dateFmt.setTimeZone(timeZone);
		dateTimeFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dateTimeFmt.setTimeZone(timeZone);
		TimeZone.setDefault(timeZone);
		
	};
	
	public static void setLocale(Locale locale)
	{
		
		Locale.setDefault(locale);
		Globals.locale = locale;
		nf = null;
		df = null;
		getNumberFormat();
		getDecimalFormat();
		getDecimalFormatSymbols();
		
	}
	public static Locale getLocale()
	{
		if(locale == null) locale = Locale.getDefault();
		return locale;
	}
	
	public static NumberFormat getNumberFormat()
	{
		if(nf != null) return nf; 
		 nf = NumberFormat.getInstance(getLocale());
		 return nf;
	}
	
	public static DecimalFormat getDecimalFormat()
	{
		if(df != null) return df; 
		 df = (DecimalFormat)DecimalFormat.getInstance(getLocale());
		 return df;
	}
	
	public static NumberFormat getCurrencyFormat()
	{
		
		if(cf != null) return cf; 
		 cf = NumberFormat.getCurrencyInstance(getLocale());
		 return cf;
	}
	
	public static DecimalFormatSymbols getDecimalFormatSymbols()
	{
		if(dfs != null) return dfs; 
		 dfs = new DecimalFormatSymbols(getLocale());
		 return dfs;
	}

	public static void setTime(Date date) {

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		realDate = calendar1.getTimeInMillis();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		refDate = calendar2.getTimeInMillis();

		return;
	}

	public static Date getTime() {

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(realDate + (calendar1.getTimeInMillis() - refDate));
		Date t = calendar2.getTime();
		return t;
	}
	
}
