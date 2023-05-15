package mobit.printer.bixolon;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bixolon.PrinterControl.BixolonPrinter;
import com.bixolon.PrinterControl.EscapeSequence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;

import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.config.JposEntry;
import jpos.events.DataEvent;
import jpos.events.DataListener;
import jpos.events.DirectIOEvent;
import jpos.events.DirectIOListener;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;

import com.bxl.config.editor.BXLConfigLoader;
import com.mobit.Barcode;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.INode;
import com.mobit.Image;
import com.mobit.Line;
import com.mobit.MobitException;
import com.mobit.PageData;
import com.mobit.PageFormatInfo;
import com.mobit.PrintItem;
import com.mobit.Printer;
import com.mobit.Rectangle;
import com.mobit.Text;

import static jpos.POSPrinterConst.PTR_BC_TEXT_BELOW;

public class BixolonAndroid extends Bixolon implements ErrorListener, OutputCompleteListener,
		StatusUpdateListener, DirectIOListener, DataListener {


	// ------------------- alignment ------------abi {------- //
	public static int ALIGNMENT_LEFT = 1;
	public static int ALIGNMENT_CENTER = 2;
	public static int ALIGNMENT_RIGHT = 4;

	// ------------------- Text attribute ------------------- //
	public static int ATTRIBUTE_NORMAL = 0;
	public static int ATTRIBUTE_FONT_A = 1;
	public static int ATTRIBUTE_FONT_B = 2;
	public static int ATTRIBUTE_FONT_C = 4;
	public static int ATTRIBUTE_BOLD = 8;
	public static int ATTRIBUTE_UNDERLINE = 16;
	public static int ATTRIBUTE_REVERSE = 32;

	// ------------------- Barcode Symbology ------------------- //
	public static int BARCODE_TYPE_UPCA = POSPrinterConst.PTR_BCS_UPCA;
	public static int BARCODE_TYPE_UPCE = POSPrinterConst.PTR_BCS_UPCE;
	public static int BARCODE_TYPE_EAN8 = POSPrinterConst.PTR_BCS_EAN8;
	public static int BARCODE_TYPE_EAN13 = POSPrinterConst.PTR_BCS_EAN13;
	public static int BARCODE_TYPE_ITF = POSPrinterConst.PTR_BCS_ITF;
	public static int BARCODE_TYPE_Codabar = POSPrinterConst.PTR_BCS_Codabar;
	public static int BARCODE_TYPE_Code39 = POSPrinterConst.PTR_BCS_Code39;
	public static int BARCODE_TYPE_Code93 = POSPrinterConst.PTR_BCS_Code93;
	public static int BARCODE_TYPE_Code128 = POSPrinterConst.PTR_BCS_Code128;
	public static int BARCODE_TYPE_PDF417 = POSPrinterConst.PTR_BCS_PDF417;
	public static int BARCODE_TYPE_MAXICODE = POSPrinterConst.PTR_BCS_MAXICODE;
	public static int BARCODE_TYPE_DATAMATRIX = POSPrinterConst.PTR_BCS_DATAMATRIX;
	public static int BARCODE_TYPE_QRCODE = POSPrinterConst.PTR_BCS_QRCODE;

	// ------------------- Barcode HRI ------------------- //
	public static int BARCODE_HRI_NONE = POSPrinterConst.PTR_BC_TEXT_NONE;
	public static int BARCODE_HRI_ABOVE = POSPrinterConst.PTR_BC_TEXT_ABOVE;
	public static int BARCODE_HRI_BELOW = POSPrinterConst.PTR_BC_TEXT_BELOW;

	// ------------------- Farsi Option ------------------- //
	public static int OPT_REORDER_FARSI_RTL = 0;
	public static int OPT_REORDER_FARSI_MIXED = 1;

	// ------------------- CharacterSet ------------------- //
	public static int CS_437_USA_STANDARD_EUROPE = 437;
	public static int CS_737_GREEK = 737;
	public static int CS_775_BALTIC = 775;
	public static int CS_850_MULTILINGUAL = 850;
	public static int CS_852_LATIN2 = 852;
	public static int CS_855_CYRILLIC = 855;
	public static int CS_857_TURKISH = 857;
	public static int CS_858_EURO = 858;
	public static int CS_860_PORTUGUESE = 860;
	public static int CS_862_HEBREW_DOS_CODE = 862;
	public static int CS_863_CANADIAN_FRENCH = 863;
	public static int CS_864_ARABIC = 864;
	public static int CS_865_NORDIC = 865;
	public static int CS_866_CYRILLIC2 = 866;
	public static int CS_928_GREEK = 928;
	public static int CS_1250_CZECH = 1250;
	public static int CS_1251_CYRILLIC = 1251;
	public static int CS_1252_LATIN1 = 1252;
	public static int CS_1253_GREEK = 1253;
	public static int CS_1254_TURKISH = 1254;
	public static int CS_1255_HEBREW_NEW_CODE = 1255;
	public static int CS_1256_ARABIC = 1256;
	public static int CS_1257_BALTIC = 1257;
	public static int CS_1258_VIETNAM = 1258;
	public static int CS_FARSI = 7065;
	public static int CS_KATAKANA = 7565;
	public static int CS_KHMER_CAMBODIA = 7572;
	public static int CS_THAI11 = 8411;
	public static int CS_THAI14 = 8414;
	public static int CS_THAI16 = 8416;
	public static int CS_THAI18 = 8418;
	public static int CS_THAI42 = 8442;
	public static int CS_KS5601 = 5601;
	public static int CS_BIG5 = 6605;
	public static int CS_GB2312 = 2312;
	public static int CS_SHIFT_JIS = 8374;
	public static int CS_TCVN_3_1 = 3031;
	public static int CS_TCVN_3_2 = 3032;

	Context mContext;

	//protected BixolonPrinter bxlPrinter;
	private BXLConfigLoader bxlConfigLoader = null;
	protected POSPrinter posPrinter;

	// private BixolonPrinter mBixolonPrinter = null;
	private Handler mHandler = null;
	private boolean pageModeStarted = false;
	private Object syncOpen = new Object();
	private  int mAlignment = 0;
	private int mAttribute = 0;
	private  int mTextSize = 1;

	@Override
	public void init(IApplication app, Printer printerInfo) throws Exception {
		super.init(app, printerInfo);
		mContext = (Context) app.getPlatform().getContext();

		try {
			bxlConfigLoader = new BXLConfigLoader(mContext);

		} catch (Throwable e) {

			if (bxlConfigLoader == null)
				bxlConfigLoader = null;
		}

		try {
			bxlConfigLoader.openFile();
		} catch (Exception e) {
			bxlConfigLoader.newFile();
		}

		posPrinter = new POSPrinter(mContext);
		posPrinter.addStatusUpdateListener(this);
		posPrinter.addErrorListener(this);
		posPrinter.addOutputCompleteListener(this);
		posPrinter.addDirectIOListener(this);


	}


	@Override
	public void close() throws Exception {
		closeInt();
		if (posPrinter != null) {
			posPrinter.addStatusUpdateListener(null);
			posPrinter.addErrorListener(null);
			posPrinter.addOutputCompleteListener(null);
			posPrinter.addDirectIOListener(null);
		}
		super.close();
	}

	private void closeInt() {
		synchronized (syncOpen) {
			logicalName = null;
			if (posPrinter != null) {
				try {
					if (posPrinter.getDeviceEnabled()) posPrinter.setDeviceEnabled(false);
				} catch (Exception e) {
				}
				try {
					if (posPrinter.getClaimed()) posPrinter.release();
				} catch (Exception e) {
				}
				try {
					posPrinter.close();
				} catch (Exception e) {
				}

			}
		}

	}

	@Override
	protected void finalize() throws Throwable {
		// if(mBixolonPrinter != null) mBixolonPrinter.disconnect();
		close();
		super.finalize();
	}

	@Override
	public String getModeleId() {
		// TODO Auto-generated method stub
		return logicalName;
	}

	@Override
	public byte[] getPrintStream() throws IOException {
		// TODO Auto-generated method stub

		return null;

	}

	String logicalName = null;

	@Override
	protected void begin_print(PageData page, PageFormatInfo pfi) throws Exception {
		// TODO Auto-generated method stub

		this.pfi = pfi;
		blackMarker = pfi.getBlackMark() != 0;
		pageModeStarted = false;
		if (!pfi.isRawText()) {
			printMode = 2;
		} else {
			printMode = 1;
		}

		String macAddr = app.getPrinterMac(printerInfo.printerNo);
		int width = converter.toPixel(pfi.getPageWidth(), xdpi);
		String _logicalName = (width > 580) ? "SPP-R410" : "SPP-R310";

		synchronized (syncOpen) {

			if (_logicalName != logicalName) {

				for (int i = 0; i < 2; i++) {
					try {
						setTargetDevice(0, _logicalName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, macAddr);
						posPrinter.open(_logicalName);
						break;
					} catch (Exception e) {
						closeInt();
						if (i == 1)throw new MobitException("Yazıcı açılamadı!", e);
					}
					Thread.sleep(1000);
				}



				try {




					posPrinter.claim(10000);
					posPrinter.setDeviceEnabled(true);
					posPrinter.setAsyncMode(true);
					logicalName = _logicalName;


					// buraya geliyor yeni yazıcıda


				} catch (Exception e) {
					closeInt();
					throw new MobitException("Yazıcıya nbağlanılamadı ömer burada !!"+ _logicalName, e);
				}
			}
		}
		try {
		checkDeviceEnabled();
		mAlignment = 0;
		mAttribute = 0;
		mTextSize = 1;

		posPrinter.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);



		Collection<INode> list = pfi.getItemList();
		for (INode item : list) {
			if (item instanceof Image) {
				uploadBitmap((Image) item);
			}
		}


		posPrinter.setCharacterSet(CS_1254_TURKISH);
		if (printMode == 2) startPageMode();
		} catch (Exception e) {
			closeInt();
			throw new MobitException("Test Deneme Hata OmerOurc burada !!"+ POSPrinterConst.PTR_S_RECEIPT, e);
		}

	}

	private void checkDeviceEnabled() throws JposException, MobitException
	{
		if(!posPrinter.getDeviceEnabled())
			throw new MobitException("Yazıcı aktive edilemedi!");
	}

	void startPageMode() throws Exception {

		if (printMode != 2 || pageModeStarted) return;
		int width = Math.min(converter.toPixel(pfi.getPageWidth(), xdpi), posPrinter.getRecLineWidth());
		int height = converter.toPixel(pfi.getPageSize(), ydpi);
		int x = converter.toPixel(pfi.getxoffset(), xdpi);
		int y = converter.toPixel(pfi.getyoffset(), ydpi);
		posPrinter.setPageModePrintArea(String.format("%d,%d,%d,%d", x, y, width, height));
		posPrinter.setPageModePrintDirection(POSPrinterConst.PTR_PD_LEFT_TO_RIGHT);
		posPrinter.pageModePrint(POSPrinterConst.PTR_PM_PAGE_MODE);
		pageModeStarted = true;

	}

	@Override
	protected void end_print() throws Exception {
		// TODO Auto-generated method stub
		posPrinter.pageModePrint(POSPrinterConst.PTR_PM_NORMAL);
		posPrinter.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
		if (printMode == 2) {
			if (blackMarker) posPrinter.markFeed(0);
		}
	}


	@Override
	protected void TextOut(Text text, PrintItem item) throws Exception {
		// TODO Auto-generated method stub

		int alignment = ALIGNMENT_LEFT;
		int attribute = BixolonPrinter.ATTRIBUTE_NORMAL;
		int textSize = 1;
		int x, y;
		if (text.isLeft()) {
			alignment = ALIGNMENT_LEFT;
			x = converter.toPixel(text.Left, pfi.getxdpi());
			y = converter.toPixel(text.Top, pfi.getydpi());
		} else if (text.isRight()) {
			alignment = BixolonPrinter.ALIGNMENT_RIGHT;
			x = converter.toPixel(text.Left, pfi.getxdpi());
			y = converter.toPixel(text.Top, pfi.getydpi());
		} else if (text.isCenter()) {
			alignment = BixolonPrinter.ALIGNMENT_CENTER;
			x = converter.toPixel(text.Left, pfi.getxdpi());
			y = converter.toPixel(text.Top, pfi.getydpi());
		} else {
			alignment = ALIGNMENT_LEFT;
			x = converter.toPixel(text.Left, pfi.getxdpi());
			y = converter.toPixel(text.Top, pfi.getydpi());
		}

		if (text.font != null) {
			String name = text.font.getName();
			if (name.equals("1")) attribute |= BixolonPrinter.ATTRIBUTE_FONT_B;
			else if (name.equals("3")) attribute |= BixolonPrinter.ATTRIBUTE_FONT_A;
			else attribute |= BixolonPrinter.ATTRIBUTE_FONT_C;

			int size = Integer.parseInt(text.font.Size);
			if (size > 0) textSize = size;
			if (textSize == 2) {
				textSize = 2;
			}

		}

		if (text.Bold) attribute |= BixolonPrinter.ATTRIBUTE_BOLD;

		posPrinter.setPageModeHorizontalPosition(x);
		posPrinter.setPageModeVerticalPosition(y);

		printText(item.getValue().toString(), alignment, attribute, textSize);

	}

	@Override
	protected void DrawLine(Line line) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawRectangle(Rectangle rect) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawBarcode(Barcode bc, PrintItem item) throws Exception {
		// TODO Auto-generated method stub
		//if(true)return;

		int x = converter.toPixel(bc.Left, pfi.getxdpi());
		int y = converter.toPixel(bc.Top, pfi.getydpi());
		int height = converter.toPixel(bc.Height, pfi.getydpi());
		posPrinter.setPageModeHorizontalPosition(x);
		posPrinter.setPageModeVerticalPosition(y);

		int alignment = POSPrinterConst.PTR_BC_LEFT;
		/*
		if (bc.isLeft()) {
			alignment = POSPrinterConst.PTR_BC_LEFT;
		} else if (bc.isRight()) {
			alignment = POSPrinterConst.PTR_BC_RIGHT;
		} else if (bc.isCenter()) {
			alignment = POSPrinterConst.PTR_BC_CENTER;

		} else {
			alignment = POSPrinterConst.PTR_BC_LEFT;
		}*/

		posPrinter.printBarCode(POSPrinterConst.PTR_S_RECEIPT, item.getValue().toString(),
				BARCODE_TYPE_Code128, height, 2, alignment, PTR_BC_TEXT_BELOW);

	}

	protected static class Pair {
		public String file;
		public Bitmap bitmap;

		public Pair(String file, Bitmap bitmap) {
			this.file = file;
			this.bitmap = bitmap;
		}
	}

	private List<Pair> bitmapList = new ArrayList<Pair>();

	protected Bitmap getBitmap(String file) {
		for (Pair pair : bitmapList) {
			if (pair.file.equalsIgnoreCase(file)) {
				return pair.bitmap;
			}
		}
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmOptions);
		bitmapList.add(new Pair(file, bitmap));
		return bitmap;
	}


	@Override
	protected void DrawImage(Image image) throws Exception {
		// TODO Auto-generated method stub
		if (printMode == 2) {
			startPageMode();
			int x = converter.toPixel(image.Left, pfi.getxdpi());
			int y = converter.toPixel(image.Bottom, pfi.getydpi());
			posPrinter.setPageModeHorizontalPosition(x);
			posPrinter.setPageModeVerticalPosition(y);
		}

		// Logo daha önce manuel yüklenmiş veya program tarafından yüklenmiş ise yazıcıdan bastırılması
		if (isUploaded(image) || image.getValue() != null) {
			String cmd = EscapeSequence.ESCAPE_CHARACTERS + String.format("%dB", image.getIntValue());
			posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, cmd);
		}
		else if(image.file != null){
			// Logonun doğrudan programdan gönderilmesi
			printBitmap(image);
		}

	}

	private void printBitmap(Image image) throws JposException {


		int alignment = POSPrinterConst.PTR_BM_LEFT;
		if (image.isLeft()) {
			alignment = POSPrinterConst.PTR_BM_LEFT;
		} else if (image.isRight()) {
			alignment = POSPrinterConst.PTR_BM_RIGHT;
		} else if (image.isCenter()) {
			alignment = POSPrinterConst.PTR_BM_CENTER;

		} else {
			alignment = POSPrinterConst.PTR_BM_LEFT;
		}

		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
		buffer.put((byte) 80); // brightness
		buffer.put((byte) 0x01); // compress
		buffer.put((byte) 0x00);

		Bitmap bitmap = getBitmap(image.file.getAbsolutePath());
		posPrinter.printBitmap(buffer.getInt(0), bitmap,
				bitmap.getWidth(), alignment);

	}

	private List<Image> uploadedBitmap = new ArrayList<Image>();

	static byte [] cmdReset = new byte[]{0x1b, '@'};

	private boolean isUploaded(Image image)
	{
		// deneme için eklendi
		//if(true) return true;
		//if(Globals.isDeveloping()) return true;

		for (Image _image : uploadedBitmap) if (_image == image) return true;
		return false;
	}

	private boolean uploadBitmap(Image image) throws JposException, InterruptedException {

		if(isUploaded(image)) return true;

		Bitmap bitmap;
		bitmap = getBitmap(image.file.getAbsolutePath());
		byte [] buf = func67b(bitmap, image.getIntValue());
		posPrinter.directIO(1, null, buf);

		uploadedBitmap.add(image);

		if(true) return true;


		if (image.file == null) return false;

		int alignment = POSPrinterConst.PTR_BM_LEFT;
		if (image.isLeft()) {
			alignment = POSPrinterConst.PTR_BM_LEFT;
		} else if (image.isRight()) {
			alignment = POSPrinterConst.PTR_BM_RIGHT;
		} else if (image.isCenter()) {
			alignment = POSPrinterConst.PTR_BM_CENTER;

		} else {
			alignment = POSPrinterConst.PTR_BM_LEFT;
		}

		bitmap = getBitmap(image.file.getAbsolutePath());
		String path = image.file.getAbsolutePath();
		posPrinter.setBitmap(image.getIntValue(), POSPrinterConst.PTR_S_RECEIPT, path, bitmap.getWidth(), alignment);
		uploadedBitmap.add(image);

		return true;
	}

	static final int pL = 3;
	static final int pH = 4;
	static final int m = 5;
	static final int fn = 6;
	static final int a = 7;
	static final int kc1 = 8;
	static final int kc2 = 9;
	static final int b = 10;
	static final int xL = 11;
	static final int xH = 12;
	static final int yL = 13;
	static final int yH = 14;

	private static byte[] cmdFunc67 = new byte[]{0x1d, 0x28, 0x4c, 0, 0, 48, 67, 48, 0, 0, 1, 0, 0, 0, 0};

	private static byte [] func67(Bitmap bitmap, int nvImageNumber) {

		int width = (bitmap.getWidth() + 7) / 8;
		int height = bitmap.getHeight();
		int bitmapSize = width * height;
		int size = cmdFunc67.length + 1 + bitmapSize + 1;
		byte [] buffer = new byte[size];

		int idx = 0;
		System.arraycopy(cmdFunc67, 0, buffer, idx, cmdFunc67.length); idx += cmdFunc67.length;

		byte[] bytes;
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);

		bb.clear();
		bytes = bb.putInt(size).array();
		buffer[pL] = bytes[0];
		buffer[pH] = bytes[1];

		bb.clear();
		bytes = String.format("%02d", nvImageNumber).getBytes();
		buffer[kc1] = bytes[0];
		buffer[kc2] = bytes[1];

		bb.clear();
		bytes = bb.putInt(bitmap.getWidth()).array();
		buffer[xL] = bytes[0];
		buffer[xH] = bytes[1];

		bb.clear();
		bytes = bb.putInt(bitmap.getHeight()).array();
		buffer[yL] = bytes[0];
		buffer[yH] = bytes[1];

		buffer[idx++] = 49;
		idx += getNegativeMonochromeBitmap(bitmap, buffer, idx);
		buffer[idx++] = 1;

		return buffer;
	}

	static final int p1 = 3;
	static final int p2 = 4;
	static final int p3 = 5;
	static final int p4 = 6;
	static final int mb = 7;
	static final int fnb = 8;
	static final int ab = 9;
	static final int kc1b = 10;
	static final int kc2b = 11;
	static final int bb = 12;
	static final int xLb = 13;
	static final int xHb = 14;
	static final int yLb = 15;
	static final int yHb = 16;

	private static byte[] cmdFunc67b = new byte[]{0x1d, 0x38, 0x4c, 0, 0, 0, 0, 48, 67, 48, 0, 0, 1, 0, 0, 0, 0};

	private static byte [] func67b(Bitmap bitmap, int nvImageNumber) {

		int width = (bitmap.getWidth() + 7) / 8;
		int height = bitmap.getHeight();
		int bitmapSize = width * height;
		int size = cmdFunc67b.length + 1 + bitmapSize + 1;
		byte [] buffer = new byte[size];

		int idx = 0;
		System.arraycopy(cmdFunc67b, 0, buffer, idx, cmdFunc67b.length); idx += cmdFunc67b.length;

		byte[] bytes;
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);

		bb.clear();
		bytes = bb.putInt(size).array();
		buffer[p1] = bytes[0];
		buffer[p2] = bytes[1];
		buffer[p3] = bytes[2];
		buffer[p4] = bytes[3];

		bb.clear();
		bytes = String.format("%02d", nvImageNumber).getBytes();
		buffer[kc1b] = bytes[0];
		buffer[kc2b] = bytes[1];

		bb.clear();
		bytes = bb.putInt(bitmap.getWidth()).array();
		buffer[xLb] = bytes[0];
		buffer[xHb] = bytes[1];

		bb.clear();
		bytes = bb.putInt(bitmap.getHeight()).array();
		buffer[yLb] = bytes[0];
		buffer[yHb] = bytes[1];


		buffer[idx++] = 49;
		idx += getNegativeMonochromeBitmap(bitmap, buffer, idx);
		buffer[idx++] = 1;

		return buffer;
	}

	private static int getNegativeMonochromeBitmap(Bitmap bitmap, byte [] buffer, int offset)
	{
		int width = (bitmap.getWidth() + 7)/8;
		int height = bitmap.getHeight();
		float[] hsv = new float[3];
		int idx = offset;
		for( int row = 0; row < bitmap.getHeight(); row++) {
			for( int col = 0; col < bitmap.getWidth(); col++) {
				Color.colorToHSV(bitmap.getPixel(col, row), hsv);
				if(hsv[2] <= 0.5f) buffer[idx + col/8] |= (1 << (7 - (col % 8)));
			}
			idx += width;
		}
		return idx - offset;
	}

	@Override
	public void directIOOccurred(DirectIOEvent directIOEvent) {

		//app.ShowMessage(null, "directIOOccurred", "");
        /*
        Fragment fm = MainActivity.getVisibleFragment();
        if(fm != null) {
            if(fm instanceof DirectIOFragment)
            {
                ((DirectIOFragment) fm).setDeviceLog("DirectIO: " + directIOEvent + "(" + getBatterStatusString(directIOEvent.getData()) + ")");
                if (directIOEvent.getObject() != null) {
                    ((DirectIOFragment) fm).setDeviceLog(new String((byte[]) directIOEvent.getObject()) + "\n");
                }
            }
        }*/
	}

	@Override
	public void errorOccurred(ErrorEvent errorEvent) {

		app.ShowMessage(null, "Yazıcı Hata : " + errorEvent.getErrorCode(), "");
    	/*
        Fragment fm = MainActivity.getVisibleFragment();
        if(fm != null) {
            if(fm instanceof TextFragment)
            {
                ((TextFragment) fm).setDeviceLog("Error : " + errorEvent);
            }

            if(fm instanceof ImageFragment)
            {
                ((ImageFragment) fm).setDeviceLog("Error : " + errorEvent);
            }

            if(fm instanceof BarcodeFragment)
            {
                ((BarcodeFragment) fm).setDeviceLog("Error : " + errorEvent);
            }

            if(fm instanceof PageModeFragment)
            {
                ((PageModeFragment) fm).setDeviceLog("Error : " + errorEvent);
            }

            if(fm instanceof DirectIOFragment)
            {
                ((DirectIOFragment) fm).setDeviceLog("Error : " + errorEvent);
            }

            if(fm instanceof MsrFragment)
            {
                ((MsrFragment) fm).setDeviceLog("Error : " + errorEvent);
            }

            if(fm instanceof CashDrawerFragment)
            {
                ((CashDrawerFragment) fm).setDeviceLog("Error : " + errorEvent);
            }
        }*/
	}

	@Override
	public void outputCompleteOccurred(OutputCompleteEvent outputCompleteEvent) {

		return;
		//app.ShowMessage(null, "outputCompleteOccurred", "");
    	/*
        Fragment fm = MainActivity.getVisibleFragment();
        if(fm != null) {
            if(fm instanceof TextFragment)
            {
                ((TextFragment) fm).setDeviceLog("outputComplete : " + outputCompleteEvent.getOutputID());
            }

            if(fm instanceof ImageFragment)
            {
                ((ImageFragment) fm).setDeviceLog("outputComplete : " + outputCompleteEvent.getOutputID());
            }

            if(fm instanceof BarcodeFragment)
            {
                ((BarcodeFragment) fm).setDeviceLog("outputComplete : " + outputCompleteEvent.getOutputID());
            }

            if(fm instanceof PageModeFragment)
            {
                ((PageModeFragment) fm).setDeviceLog("outputComplete : " + outputCompleteEvent.getOutputID());
            }

            if(fm instanceof DirectIOFragment)
            {
                ((DirectIOFragment) fm).setDeviceLog("outputComplete : " + outputCompleteEvent.getOutputID());
            }

            if(fm instanceof MsrFragment)
            {
                ((MsrFragment) fm).setDeviceLog("outputComplete : " + outputCompleteEvent.getOutputID());
            }
        }*/
	}

	@Override
	public void statusUpdateOccurred(StatusUpdateEvent statusUpdateEvent) {

		try {

			if (statusUpdateEvent.getStatus() == JposConst.JPOS_SUE_POWER_OFF_OFFLINE) {

				closeInt();
			}
			//app.ShowMessage(null, getSUEMessage(statusUpdateEvent.getStatus()), "");
		} catch (Exception e) {

		}
		return;
    	/*
        Fragment fm = MainActivity.getVisibleFragment();
        if(fm != null) {
           if(fm instanceof TextFragment)
           {
               ((TextFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
           }

            if(fm instanceof ImageFragment)
            {
                ((ImageFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
            }

            if(fm instanceof BarcodeFragment)
            {
                ((BarcodeFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
            }

            if(fm instanceof PageModeFragment)
            {
                ((PageModeFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
            }

            if(fm instanceof DirectIOFragment)
            {
                ((DirectIOFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
            }

            if(fm instanceof MsrFragment)
            {
                ((MsrFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
            }

            if(fm instanceof CashDrawerFragment)
            {
                ((CashDrawerFragment) fm).setDeviceLog(getSUEMessage(statusUpdateEvent.getStatus()));
            }
        }*/
	}

	@Override
	public void dataOccurred(DataEvent dataEvent) {

		return;
		//app.ShowMessage(null, "dataOccurred", "");
    	/*
        Fragment fm = MainActivity.getVisibleFragment();
        if(fm != null) {
            if(fm instanceof MsrFragment)
            {
                String track1Len = Integer.toString(dataEvent.getStatus() & 0xff);
                String track2Len = Integer.toString((dataEvent.getStatus() & 0xff00) >> 8);
                String track3Len = Integer.toString((dataEvent.getStatus() & 0xff0000) >> 16);

                String track1 = "Track1(" + track1Len + ") : " + getTrackData(1) + "\n";
                String track2 = "Track2(" + track2Len + ") : " + getTrackData(2) + "\n";
                String track3 = "Track3(" + track3Len + ") : " + getTrackData(3) + "\n";

                ((MsrFragment) fm).setDeviceLog(track1 + track2 + track3);
            }
        }*/
	}


	private void setTargetDevice(int portType, String logicalName, int deviceCategory, String address) throws Exception {
		for (Object entry : bxlConfigLoader.getEntries()) {
			JposEntry jposEntry = (JposEntry) entry;
			if (jposEntry.getLogicalName().equals(logicalName)) {
				bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
			}
		}
		bxlConfigLoader.addEntry(logicalName, deviceCategory, getProductName(logicalName), portType, address);
		bxlConfigLoader.saveFile();

	}


	private String getProductName(String name) {
		String productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200II;

		if ((name.indexOf("SPP-R200II") >= 0)) {
			if (name.length() > 10) {
				if (name.substring(10, 11).equals("I")) {
					productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200III;
				}
			}
		} else if ((name.indexOf("SPP-R210") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R210;
		} else if ((name.indexOf("SPP-R215") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R215;
		} else if ((name.indexOf("SPP-R220") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R220;
		} else if ((name.indexOf("SPP-R300") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R300;
		} else if ((name.indexOf("SPP-R310") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R310;
		} else if ((name.indexOf("SPP-R318") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R318;
		} else if ((name.indexOf("SPP-R400") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R400;
		} else if ((name.indexOf("SPP-R410") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R410;
		} else if ((name.indexOf("SPP-R418") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SPP_R418;
		} else if ((name.indexOf("SRP-350III") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_350III;
		} else if ((name.indexOf("SRP-352III") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_352III;
		} else if ((name.indexOf("SRP-350plusIII") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_350PLUSIII;
		} else if ((name.indexOf("SRP-352plusIII") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_352PLUSIII;
		} else if ((name.indexOf("SRP-380") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_380;
		} else if ((name.indexOf("SRP-382") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_382;
		} else if ((name.indexOf("SRP-383") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_383;
		} else if ((name.indexOf("SRP-340II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_340II;
		} else if ((name.indexOf("SRP-342II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_342II;
		} else if ((name.indexOf("SRP-Q300") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q300;
		} else if ((name.indexOf("SRP-Q302") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q302;
		} else if ((name.indexOf("SRP-QE300") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_QE300;
		} else if ((name.indexOf("SRP-QE302") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_QE302;
		} else if ((name.indexOf("SRP-E300") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_E300;
		} else if ((name.indexOf("SRP-E302") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_E302;
		} else if ((name.indexOf("SRP-330II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_330II;
		} else if ((name.indexOf("SRP-332II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_332II;
		} else if ((name.indexOf("SRP-S300") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_S300;
		} else if ((name.indexOf("SRP-F310II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_F310II;
		} else if ((name.indexOf("SRP-F312II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_F312II;
		} else if ((name.indexOf("SRP-F313II") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_F313II;
		} else if ((name.indexOf("SRP-275III") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SRP_275III;
		} else if ((name.indexOf("MSR") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_MSR;
		} else if ((name.indexOf("SmartCardRW") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_SMART_CARD_RW;
		} else if ((name.indexOf("CashDrawer") >= 0)) {
			productName = BXLConfigLoader.PRODUCT_NAME_CASH_DRAWER;
		}

		return productName;
	}

	public void printText(String data, int alignment, int attribute, int textSize) throws JposException {


		String strOption = "";//EscapeSequence.getString(0);

		if ((alignment & ALIGNMENT_LEFT) == ALIGNMENT_LEFT) {
			if ((mAlignment & ALIGNMENT_LEFT) != ALIGNMENT_LEFT) {
				mAlignment &= ~7;
				mAlignment |= ALIGNMENT_LEFT;
				strOption += EscapeSequence.getString(4);
			}
		} else if ((alignment & ALIGNMENT_CENTER) == ALIGNMENT_CENTER) {
			if ((mAlignment & ALIGNMENT_CENTER) != ALIGNMENT_CENTER) {
				mAlignment &= ~7;
				mAlignment |= ALIGNMENT_CENTER;
				strOption += EscapeSequence.getString(5);
			}
		} else if ((alignment & ALIGNMENT_RIGHT) == ALIGNMENT_RIGHT) {
			if ((mAlignment & ALIGNMENT_RIGHT) != ALIGNMENT_RIGHT) {
				mAlignment &= ~7;
				mAlignment |= ALIGNMENT_RIGHT;
				strOption += EscapeSequence.getString(6);
			}
		}

		if ((attribute & ATTRIBUTE_FONT_A) == ATTRIBUTE_FONT_A) {
			if ((mAttribute & ATTRIBUTE_FONT_A) != ATTRIBUTE_FONT_A) {
				mAttribute &= ~7;
				mAttribute |= ATTRIBUTE_FONT_A;
				strOption += EscapeSequence.getString(1);
			}
		} else if ((attribute & ATTRIBUTE_FONT_B) == ATTRIBUTE_FONT_B) {
			if ((mAttribute & ATTRIBUTE_FONT_B) != ATTRIBUTE_FONT_B) {
				mAttribute &= ~7;
				mAttribute |= ATTRIBUTE_FONT_B;
				strOption += EscapeSequence.getString(2);
			}
		} else if ((attribute & ATTRIBUTE_FONT_C) == ATTRIBUTE_FONT_C) {
			if ((mAttribute & ATTRIBUTE_FONT_C) != ATTRIBUTE_FONT_C) {
				mAttribute &= ~7;
				mAttribute |= ATTRIBUTE_FONT_C;
				strOption += EscapeSequence.getString(3);
			}
		}

		if ((attribute & ATTRIBUTE_BOLD) == ATTRIBUTE_BOLD) {
			mAttribute |= ATTRIBUTE_BOLD;
			strOption += EscapeSequence.getString(7);
		} else if ((mAttribute & ATTRIBUTE_BOLD) == ATTRIBUTE_BOLD) {
			mAttribute &= ~ATTRIBUTE_BOLD;
			strOption += EscapeSequence.getString(8);
		}

		if ((attribute & ATTRIBUTE_UNDERLINE) == ATTRIBUTE_UNDERLINE) {
			mAttribute |= ATTRIBUTE_UNDERLINE;
			strOption += EscapeSequence.getString(9);
		} else if ((mAttribute & ATTRIBUTE_UNDERLINE) == ATTRIBUTE_UNDERLINE) {
			mAttribute &= ~ATTRIBUTE_UNDERLINE;
			strOption += EscapeSequence.getString(10);
		}


		if ((attribute & ATTRIBUTE_REVERSE) == ATTRIBUTE_REVERSE) {
			mAttribute |= ATTRIBUTE_REVERSE;
			strOption += EscapeSequence.getString(11);
		} else if ((mAttribute & ATTRIBUTE_REVERSE) == ATTRIBUTE_REVERSE) {
			mAttribute &= ~ATTRIBUTE_REVERSE;
			strOption += EscapeSequence.getString(12);
		}

		if (textSize != mTextSize) {
			if (textSize == 1) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(17);
				strOption += EscapeSequence.getString(25);

			} else if (textSize == 2) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(18);
				strOption += EscapeSequence.getString(26);

			} else if (textSize == 3) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(19);
				strOption += EscapeSequence.getString(27);

			} else if (textSize == 4) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(20);
				strOption += EscapeSequence.getString(28);

			} else if (textSize == 5) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(21);
				strOption += EscapeSequence.getString(29);

			} else if (textSize == 6) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(22);
				strOption += EscapeSequence.getString(30);

			} else if (textSize == 7) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(23);
				strOption += EscapeSequence.getString(31);

			} else if (textSize == 8) {
				mTextSize = textSize;
				strOption += EscapeSequence.getString(24);
				strOption += EscapeSequence.getString(32);
			} else {
				mTextSize = 1;
				strOption += EscapeSequence.getString(17);
				strOption += EscapeSequence.getString(25);
			}
		}

		posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, strOption + data);

	}

	private String getERMessage(int status) {
		switch (status) {
			case POSPrinterConst.JPOS_EPTR_COVER_OPEN:
				return "Cover open";

			case POSPrinterConst.JPOS_EPTR_REC_EMPTY:
				return "Paper empty";

			case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
				return "Power off";

			default:
				return "Unknown";
		}
	}

	private String getSUEMessage(int status) throws Exception {
		switch (status) {
			case JposConst.JPOS_SUE_POWER_ONLINE:
				return "StatusUpdate : Power on";

			case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
				return "StatusUpdate : Power off";

			case POSPrinterConst.PTR_SUE_COVER_OPEN:
				return "StatusUpdate : Cover Open";

			case POSPrinterConst.PTR_SUE_COVER_OK:
				return "StatusUpdate : Cover OK";

			case POSPrinterConst.PTR_SUE_BAT_LOW:
				return "StatusUpdate : Battery-Low";

			case POSPrinterConst.PTR_SUE_BAT_OK:
				return "StatusUpdate : Battery-OK";

			case POSPrinterConst.PTR_SUE_REC_EMPTY:
				return "StatusUpdate : Receipt Paper Empty";

			case POSPrinterConst.PTR_SUE_REC_NEAREMPTY:
				return "StatusUpdate : Receipt Paper Near Empty";

			case POSPrinterConst.PTR_SUE_REC_PAPEROK:
				return "StatusUpdate : Receipt Paper OK";

			case POSPrinterConst.PTR_SUE_IDLE:
				return "StatusUpdate : Printer Idle";

			case POSPrinterConst.PTR_SUE_OFF_LINE:
				return "StatusUpdate : Printer off line";

			case POSPrinterConst.PTR_SUE_ON_LINE:
				return "StatusUpdate : Printer on line";

			default:
				return "StatusUpdate : Unknown";
		}
	}

	private String getBatterStatusString(int status) {
		switch (status) {
			case 0x30:
				return "BatterStatus : Full";

			case 0x31:
				return "BatterStatus : High";

			case 0x32:
				return "BatterStatus : Middle";

			case 0x33:
				return "BatterStatus : Low";

			default:
				return "BatterStatus : Unknwon";
		}
	}


	/*
	public static byte[] decodeBitmap(byte[] bitmapBytes) {

		Bitmap bmp = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

		int zeroCount = bmp.getWidth() % 8;
		String zeroStr = "";
		if (zeroCount > 0) {
			for (int i = 0; i < (8 - zeroCount); i++) {
				zeroStr = zeroStr + "0";
			}
		}

		List<String> list = new ArrayList<>();
		for (int i = 0; i < bmp.getHeight(); i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < bmp.getWidth(); j++) {
				int color = bmp.getPixel(j, i);

				int r = (color >> 16) & 0xff;
				int g = (color >> 8) & 0xff;
				int b = color & 0xff;

				// if color close to white，bit='0', else bit='1'
				if (r > 160 && g > 160 && b > 160)
					sb.append("0");
				else
					sb.append("1");
			}
			if (zeroCount > 0) {
				sb.append(zeroStr);
			}

			list.add(sb.toString());
		}

		List<String> bmpHexList = binaryListToHexStringList(list);
		List<String> commandList = new ArrayList<>();
		commandList.addAll(bmpHexList);

		return hexListToBytes(commandList);
	}
	*/

	/*
	static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;
	static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;
	static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
	static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;
	
	static final byte[] data = new byte[] { 0x1b, 0x2a, 0x01, (byte)150, 0x00, 
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			(byte)0xaa, (byte)0xaa, 0x02, 0x01, 0x01, 0x01, 0x01, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
			
			0x1b, 'J', 0
			};

	static final byte[] image = new byte[] { 0x1b, 0x40, 0x1b, 0x53, 0x1d, 0x76, 0x30, 0x00, 0x0f, 0x00, 0x07, 0x00,
			(byte)0xff, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01, 
			0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01, 
			0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01, 
			0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01, 
			0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01, 
			0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01, 
			0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01

	};

	byte [] bitmapGSv0() {
		
		Bitmap image1 = Bitmap.createBitmap(576, 30, Bitmap.Config.ARGB_8888);
		
		image1.eraseColor(Color.WHITE);
		
		Canvas c = new Canvas(image1);
		c.drawColor(Color.WHITE);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		//paint.setAntiAlias(true);
		//paint.setFilterBitmap(true);
		//paint.setDither(true);
		//c.drawRect(20, 5, 200, 25, paint);
		
		//c.drawLine(0, 1, 576, 24, paint);
		//c.drawLine(0, 24, 576, 1, paint);
		
		paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(20);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
		String str = "ABCDEFGH";
		Rect rectText = new Rect();
	    paint.getTextBounds(str, 0, str.length()-1, rectText);
	    
	    c.drawText(str, 0, rectText.height(), paint);
	    /*
		c.save();
		OutputStream out;
		try {
			File file = new File(app.getAppDataPath(), "printout.png");
			file.createNewFile();
			out = new FileOutputStream(file);
			image1.compress(CompressFormat.PNG, 100, out);
			out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} *
		
		
		int byte_per_pixel = image1.getByteCount();
		int height = image1.getHeight();
		int width = image1.getWidth();
		int row_bytes = image1.getRowBytes();
		byte_per_pixel = image1.getRowBytes() / width;
		ByteBuffer b = ByteBuffer.allocate(image1.getByteCount());
		b.clear();
		image1.copyPixelsToBuffer(b);
		
		int mono_row_bytes = (width + 7) / 8;
		byte[] array = new byte[4 + 8 + mono_row_bytes * height];
		
		b.position(0);
		byte[] bb = null;
		if(b.hasArray()){
			bb = (byte[]) b.array();
		}
		else {
			bb = new byte[b.remaining()];
			b.get(bb);
		}
		

		// 2
		array[0] = 0x1b;
		array[1] = 0x40;
		// 2
		array[2] = 0x1b;
		array[3] = 0x53;
		
		// 8
		array[4] = 0x1d; 
		array[5] = 0x76;
		array[6] = 0x30;
		array[7] = 0x00;
		array[8] = (byte)(mono_row_bytes & 0xff);
		array[9] = (byte)((mono_row_bytes >> 8) & 0xff);
		array[10] = (byte)(height & 0xff);
		array[11] = (byte)((height >> 8) & 0xff);
		
		int i = 12;
		
		for (int row = 0; row < height; row++) {
			byte pix = 0;
			boolean add = false;
			for (int col = 0; col < width; col++) {
				int index = row_bytes*row + col*byte_per_pixel;
				byte p = (byte) ((bb[index+1] != 0 || bb[index+2] != 0 && bb[index+3] != 0) ? 0 : 1);
				int mod = col % 8;
				pix |= p << (7 - mod);
				add = true;
				if (mod == 7) {
					array[i++] = (byte)pix;
					pix = 0;
					add = false;
				}
			}
			if(add) array[i++] = pix;
		}
		return array;
	}
	
	byte [] bitmapGSL() {
		
		Bitmap image1 = Bitmap.createBitmap(576, 30, Bitmap.Config.ARGB_8888);
		
		image1.eraseColor(Color.WHITE);
		
		Canvas c = new Canvas(image1);
		c.drawColor(Color.WHITE);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		//paint.setAntiAlias(true);
		//paint.setFilterBitmap(true);
		//paint.setDither(true);
		//c.drawRect(20, 5, 200, 25, paint);
		
		c.drawLine(0, 0, 576, 24, paint);
		//c.drawLine(550, 5, 576, 25, paint);
		
		paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Style.STROKE); 
		paint.setTextSize(20); 
		String str = "************************";
		//c.drawText(str, 0, str.length()-1, 0, 0, paint);
		
		
		int byte_per_pixel = image1.getByteCount();
		int height = image1.getHeight();
		int width = image1.getWidth();
		int row_bytes = image1.getRowBytes();
		byte_per_pixel = image1.getRowBytes() / width;
		ByteBuffer b = ByteBuffer.allocate(image1.getByteCount());
		b.clear();
		image1.copyPixelsToBuffer(b);
		
		int mono_row_bytes = (width + 7) / 8;
		int datasize = mono_row_bytes * height;
		datasize += 10;
		byte[] array = new byte[2 + 2 + 7 + datasize + 7];
		
		b.position(0);
		byte[] bb = null;
		if(b.hasArray()){
			bb = (byte[]) b.array();
		}
		else {
			bb = new byte[b.remaining()];
			b.get(bb);
		}
		

		// 2  reset
		array[0] = 0x1b;
		array[1] = 0x40;
		// 2
		array[2] = 0x1b;
		array[3] = 0x53;

		// 7
		array[4] = 0x1d; 
		array[5] = 0x38;
		array[6] = 0x4C;		
		array[7] = (byte)(datasize & 0xff); //P1
		array[8] = (byte)((datasize >> 8) & 0xff); //P2
		array[9] = (byte)((datasize >> 16) & 0xff); //P3
		array[10] = (byte)((datasize >> 24) & 0xff); //P4
		// 10
		array[11] = 0x30; // m
		array[12] = 0x70; // fn
		array[13] = 0x30; // a
		array[14] = 0x01; // bx
		array[15] = 0x01; // by
		array[16] = 0x31; // c
		array[17] = (byte)(mono_row_bytes & 0xff); // xL
		array[18] = (byte)((mono_row_bytes >> 8) & 0xff); // xH
		array[19] = (byte)(height & 0xff); // yL
		array[20] = (byte)((height >> 8) & 0xff); // yH
		
		int i = 21;
		
		for (int row = 0; row < height; row++) {
			byte pix = 0;
			boolean add = false;
			for (int col = 0; col < width; col++) {
				int index = row_bytes*row + col*byte_per_pixel;
				byte p = (byte) ((bb[index+1] != 0 || bb[index+2] != 0 && bb[index+3] != 0) ? 0 : 1);
				int mod = col % 8;
				pix |= p << (7 - mod);
				add = true;
				if (mod == 7) {
					array[i++] = (byte)pix;
					pix = 0;
					add = false;
				}
			}
			if(add) 
				array[i++] = pix;
		}
		// 7
		array[i++] = 0x1d; 
		array[i++] = 0x28;
		array[i++] = 0x4C;		
		array[i++] = 0x02;
		array[i++] = 0x00;
		array[i++] = 0x30;
		array[i++] = 0x32;
		
		return array;
		
		
	}

	byte [] bitImageMode() {
		
		Bitmap image1 = Bitmap.createBitmap(576, 240, Bitmap.Config.ARGB_8888);
		
		image1.eraseColor(Color.WHITE);
		
		Canvas c = new Canvas(image1);
		c.drawColor(Color.WHITE);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		//paint.setAntiAlias(true);
		//paint.setFilterBitmap(true);
		//paint.setDither(true);
		//c.drawRect(20, 5, 200, 25, paint);
		
		c.drawLine(0, 0, 576, 24, paint);
		//c.drawLine(550, 5, 576, 25, paint);
		
		paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Style.STROKE); 
		paint.setTextSize(20); 
		String str = "************************";
		//c.drawText(str, 0, str.length()-1, 0, 0, paint);
		
		
		int byte_per_pixel = image1.getByteCount();
		int height = image1.getHeight();
		int width = image1.getWidth();
		int row_bytes = image1.getRowBytes();
		byte_per_pixel = image1.getRowBytes() / width;
		ByteBuffer b = ByteBuffer.allocate(image1.getByteCount());
		b.clear();
		image1.copyPixelsToBuffer(b);
		
		//---------------------------------------------------------------------
		
		int m = 0;
		int vdots;
		switch(m){
		case 32:
		case 33:
			vdots = 24;
			break;
		case 0:
		case 1:
		default:
			vdots = 8;
		}
		
		int packets = (height + vdots - 1) / vdots;
		
		int packet_bytes = width * (vdots/8);
		byte[] array = new byte[2 + 2 + (5 + packet_bytes + 3)*packets];
		
		b.position(0);
		byte[] bb = null;
		if(b.hasArray()){
			bb = (byte[]) b.array();
		}
		else {
			bb = new byte[b.remaining()];
			b.get(bb);
		}

		// 2  reset
		array[0] = 0x1b;
		array[1] = 0x40;
		// 2
		array[2] = 0x1b;
		array[3] = 0x53;
		
		int i = 4;
		
		for (int packet = 0; packet < packets; packet++) {
			// 5
			array[i++] = 0x1b; 
			array[i++] = 0x2a;
			array[i++] = (byte)(m & 0xff); // m
			array[i++] = (byte)(packet_bytes & 0xff); // nL
			array[i++] = (byte)((packet_bytes >> 8) & 0xff); // nH
			
			int row = packet * 8;
			for (int col = 0; col < width; col++) {
				byte pix = 0;
				boolean add = false;
				for(int r = 0; r < 8; r++){
				int index = row_bytes*(row+r) + col*byte_per_pixel;
				byte p = (byte) ((bb[index+1] != 0 || bb[index+2] != 0 && bb[index+3] != 0) ? 0 : 1);
				pix |= p << (7 - r);
				add = true;
				if (r == 7) {
					array[i++] = (byte)pix;
					pix = 0;
					add = false;
				}
				}
			}
			
			// 3
			array[i++] = 0x1b; 
			array[i++] = 'J';
			array[i++] = 0x00;
		}
		
		return array;
		
		
	}*/
}

