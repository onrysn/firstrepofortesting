package com.mobit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class utility {

	private static String nlcr = "\r\n";

	public static String FormatException(Throwable e, boolean innerExcpt, boolean onlyMsg) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		int dept = 0;

		do {

			if (dept == 0) {
				ps.print("--------------------------------------------------");
				ps.print(nlcr);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(Globals.getTime());
				String zaman = String.format("%04d/%02d/%02d %02d:%02d:%02d%s", calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
						calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), nlcr);
				ps.print(zaman);
			}
			ps.print(e.getMessage());
			ps.print(nlcr);
			if (!onlyMsg && Globals.isDeveloping()) {
				e.printStackTrace(ps);
				ps.print(nlcr);
			}
			e = e.getCause();
			ps.print(nlcr);
			ps.print(nlcr);
			ps.print(nlcr);
			dept++;

		} while (e != null && innerExcpt);
		return os.toString();
	}

	public static class ExceptionInfo {
		public Throwable e;
		public String caption;
		public String msg;
	};

	public static ExceptionInfo FilterException(Throwable e) {
		ExceptionInfo info = new ExceptionInfo();
		info.msg = "";
		info.caption = MobitException.s_caption;
		if (e instanceof MobitException) {
			info.caption = ((MobitException) e).getCaption();
			info.msg = e.toString();
		} else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
			info.caption = "Zaman Aşımı";
			info.msg = e.getMessage();
		} else if (e instanceof ConnectException && e.getMessage().contains("ENETUNREACH")) {
			info.caption = "Bağlantı Hatası";
			info.msg = e.getMessage();
		} else if (e instanceof RuntimeException) {
			info.caption = e.getClass().getName();
			info.msg = String.format("%s\n%s", info.msg, utility.FormatException(e, true, false));
		}
		else if (e instanceof InterruptedException) {
			if(Globals.isDeveloping()){
				info.msg = utility.FormatException(e, Globals.isDeveloping(), !Globals.isDeveloping());
			}
			else {
				info.msg = "İşlem iptal edildi!";
			}
		}
		else {
			info.msg = utility.FormatException(e, Globals.isDeveloping(), !Globals.isDeveloping());
		}
		return info;

	}

	public static void Log(File file, Throwable e) {

		PrintStream ps = null;
		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(file, true);
			ps = new PrintStream(fos);
			fos.write(FormatException(e, true, false).getBytes());
			fos.write(new byte[] { 13, 10, 13, 10 });

		} catch (Exception ex) {
			// TODO Auto-generated catch block

		} finally {

			if (ps != null)
				ps.close();
			if (fos != null)
				try {
					fos.close();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

		}

	}

	public static void Log(File file, byte[] b) {

		PrintStream ps = null;
		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(file, true);
			ps = new PrintStream(fos);
			fos.write(b);

		} catch (Exception ex) {
			// TODO Auto-generated catch block

		} finally {

			if (ps != null)
				ps.close();
			if (fos != null)
				try {
					fos.close();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
		}
	}

	public static String pathJoin(final String... pathElements) {
		final String path;

		if (pathElements == null || pathElements.length == 0) {
			path = File.separator;
		} else {
			final StringBuilder builder;

			builder = new StringBuilder();

			int i = 0;
			for (final String pathElement : pathElements) {
				final String sanitizedPathElement;

				// the "\\" is for Windows... you will need to come up with the
				// appropriate regex for this to be portable
				sanitizedPathElement = pathElement.replace("\\", File.separator);

				if (sanitizedPathElement.length() > 0) {
					builder.append(sanitizedPathElement);
					if (i < pathElements.length - 1)
						builder.append(File.separator);
				}
				i++;
			}

			path = builder.toString();
		}

		return (path);
	}

	public static Object newInstance(String fileName, String className) throws MalformedURLException, IOException {
		return newInstance(new File(fileName).toURI().toURL(), className);
	}

	public static Object newInstance(URL u, String className) throws IOException {

		Object obj = null;
		URLClassLoader loader = null;
		try {

			loader = new URLClassLoader(new URL[] { u });
			Class<?> c = loader.loadClass(className);
			obj = c.newInstance();

		} catch (Throwable t) {

			throw new IOException("Error, could not add URL to system classloader", t);
		} finally {

			if (loader != null)
				loader.close();
		}
		return obj;
	}

	public static String getVersion(URL verUrl) throws IOException {

		URLConnection urlConnection = null;
		HttpURLConnection httpUrlConnection = null;

		InputStream in = null;
		String s = "";
		try {

			urlConnection = verUrl.openConnection();
			if (urlConnection instanceof HttpURLConnection) {
				httpUrlConnection = (HttpURLConnection) urlConnection;
				httpUrlConnection.setRequestMethod("GET");
			}
			// urlConnection.setDoOutput(true);
			in = urlConnection.getInputStream();
			byte[] buffer = new byte[50];
			int n;
			StringBuilder sb = new StringBuilder();
			while ((n = in.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, n, "UTF-8"));
			}
			String ss = sb.toString();
			sb.setLength(0);
			for (int i = 0; i < ss.length(); i++)
				if (ss.charAt(i) > ' ' && ss.charAt(i) <= 'z')
					sb.append(ss.charAt(i));
			s = sb.toString();
		} finally {
			if (in != null)
				in.close();
			if (httpUrlConnection != null)
				httpUrlConnection.disconnect();
		}
		return s;
	}

	public static void downloadFile(URL appUrl, File file) throws Exception {

		URLConnection urlConnection = null;
		HttpURLConnection httpUrlConnection = null;

		InputStream in = null;
		OutputStream out = null;

		long cnt = 0;
		try {

			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
			urlConnection = appUrl.openConnection();
			if (urlConnection instanceof HttpURLConnection) {
				httpUrlConnection = (HttpURLConnection) urlConnection;
				httpUrlConnection.setRequestMethod("GET");
			}
			in = new BufferedInputStream(urlConnection.getInputStream());
			out = new FileOutputStream(file);
			byte[] buffer = new byte[8 * 1024];
			int n;
			cnt = 0;
			while (true) {
				n = in.read(buffer, 0, buffer.length);
				if (n < 0)
					break;
				if (n > 0) {
					cnt += n;
					out.write(buffer, 0, n);
				}
			}
			out.flush();

		} finally {
			if (out != null)
				out.close();
			if (in != null)
				in.close();

			cnt = file.length();

			if (httpUrlConnection != null)
				httpUrlConnection.disconnect();

		}

	}

	public static String[] toStringArray(Object[] array) {
		String[] strs = new String[array.length];
		for (int i = 0; i < array.length; i++)
			strs[i] = array[i].toString();
		return strs;
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	public static double getFractionalPart(double n) {
		if (n > 0) {
			return n - Math.floor(n);
		} else {
			return ((n - Math.ceil(n)) * -1);
		}
	}

	public static void check(Integer value, int digit) {
		if (value != null && value >= Math.pow(10, (double) digit))
			throw new IllegalArgumentException(String.format("%d : %d haneli olmalı", value, digit));
	}

	public static void check(double value, int digit) {
		if (value >= Math.pow(10, (double) digit))
			throw new IllegalArgumentException(String.format("%f : %d haneli olmalı", value, digit));
	}

	public static void check(Double value, int digit, int prec) {
		if (prec > 0)
			digit -= (prec + 1);
		if (value != null && value >= Math.pow(10, (double) digit))
			throw new IllegalArgumentException(String.format("%f : %d haneli olmalı", value, digit));
	}

	public static void check(String value, int size) {
		if (value != null && value.length() > size)
			throw new IllegalArgumentException(String.format("%s : %d haneli olmalı", value, size));
	}

	// -------------------------------------------------------------------------

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String toISO8601(Date d) {
		return (d != null) ? df.format(d) : null;
	}

	public static Date fromISO8601(String s) throws ParseException {
		return (s != null) ? df.parse(s) : null;
	}

	public static double getJulianFromUnixMilliSecs(long unixMilliSecs) {
		return (unixMilliSecs / 86400000.0) + 2440587.5;
	}

	public static long getUnixMilliSecsFromJulian(double julian) {
		return (long) ((julian - 2440587.5) * 86400000.0);

	}

	public static Date getDateFromObject(Object obj) throws ParseException {

		Calendar calendar = Calendar.getInstance();
		if (obj instanceof Long)
			calendar.setTimeInMillis((Long)obj);
		if (obj instanceof Integer)
			calendar.setTimeInMillis(((int) obj) * 1000L);
		else if (obj instanceof Double)
			calendar.setTimeInMillis(utility.getUnixMilliSecsFromJulian((double) obj));
		else if (obj instanceof String)
			return fromISO8601((String) obj);
		else 
			return null;
		return calendar.getTime();
	}

	public enum TimeType {
		UnixEpochSeconds, // integer
		UnixEpochMilliSeconds, //long
		Julian, // double
		ISO8601 // String
	};

	public static Object getObjectFromDate(Date date, TimeType type) throws ParseException {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		TimeZone timeZone = calendar.getTimeZone();
		long time = calendar.getTimeInMillis();// + timeZone.getOffset(Calendar.ZONE_OFFSET);

		switch (type) {
		case UnixEpochMilliSeconds:
			return time;
		case UnixEpochSeconds:
			return (Integer)(int)(time / 1000);
		case ISO8601:
			return toISO8601(date);
		case Julian:
			return getJulianFromUnixMilliSecs(time);
		default:
			return (Integer)(int)(time / 1000);
		}

	}

	// -------------------------------------------------------------------------

	static final double earthRadius = 6371000; // meters

	public static float distance(double lat1, double lng1, double lat2, double lng2) {

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i >= 0)
			return fileName.substring(i + 1);
		return "";
	}

	public static long minutesBetween(Date date1, Date date2) {

		return (date2.getTime() - date1.getTime()) / (60 * 1000);

	}

	public static Date classBuildTime(Class<?> cls)
			throws URISyntaxException, IllegalStateException, IllegalArgumentException {

		Date d = null;

		Class<?> currentClass = new Object() {
		}.getClass().getEnclosingClass();
		String name = currentClass.getSimpleName() + ".class";
		URL resource = currentClass.getResource(name);
		resource = currentClass.getClassLoader().getResource(name);
		if (resource != null) {
			if (resource.getProtocol().equals("file")) {
				try {
					d = new Date(new File(resource.toURI()).lastModified());
				} catch (URISyntaxException ignored) {
				}
			} else if (resource.getProtocol().equals("jar")) {
				String path = resource.getPath();
				d = new Date(new File(path.substring(5, path.indexOf("!"))).lastModified());
			} else if (resource.getProtocol().equals("zip")) {
				String path = resource.getPath();
				File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
				// long jfodLastModifiedLong = jarFileOnDisk.lastModified ();
				// Date jfodLasModifiedDate = new Date(jfodLastModifiedLong);
				try (JarFile jf = new JarFile(jarFileOnDisk)) {
					ZipEntry ze = jf.getEntry(path.substring(path.indexOf("!") + 2));// Skip
																						// the
																						// !
																						// and
																						// the
																						// /
					long zeTimeLong = ze.getTime();
					Date zeTimeDate = new Date(zeTimeLong);
					d = zeTimeDate;
				} catch (IOException | RuntimeException ignored) {
				}
			}
		}
		return d;
	}

	public static byte[] combine(byte[] a, byte[] b) {
		int length = a.length + b.length;
		final byte[] result = new byte[length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] combine(Class<T> cls, T[] a, T[] b) {
		
		final T [] result = (T [])Array.newInstance(cls, a.length + b.length);
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static <T> T[] removeArrayItem(Class<T> cls, T [] array, Object item)
	{
		int idx = -1;
		for (int i = 0; i < array.length; i++){
			if(array[i].equals(item)){
				idx = i;
				break;
			}
		}
		if(idx < 0) return array;
		T [] _array = (T [])Array.newInstance(cls, array.length - 1);
		for(int i = 0, j = 0; i < array.length; i++)
			if(i != idx) _array[j++] = array[i];

		return _array;

	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		Field field = null;
		Exception ex = null;
		while (clazz != null && field == null) {
			try {
				field = clazz.getDeclaredField(name);
				
			} catch (Exception e) {
				ex = e;
			}
			clazz = clazz.getSuperclass();
		}
		if (field == null)
			throw ex;
		
		if (!field.isAccessible())
			field.setAccessible(true);
		
		return field;
	}

	public static String distance(float d) {
		if (d < 0)
			return "Hesaplanmamış";
		if (d < 1000)
			return String.format("%.0f m", d);
		return String.format("%.1f km", d / 1000);
	}

	public static int search(String s1, String s2, Pattern[] patternList) {
		int matched = 0;
		for (int i = 0; i < patternList.length; i++) {
			Pattern pattern = patternList[i];
			if (s1 != null && !s1.isEmpty()) {
				if (pattern.matcher(s1).find()) {
					matched |= 1 << i;
				}
			} else if (s2 != null && !s2.isEmpty()) {
				if (pattern.matcher(s2).find()) {
					matched |= 1 << i;
				}
			}
		}
		return matched;
	}

	public static String toHexColor(int color) {
		return String.format("#%06X", 0xFFFFFF & color);
	}


	
}
