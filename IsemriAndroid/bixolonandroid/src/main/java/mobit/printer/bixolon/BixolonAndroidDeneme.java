package mobit.printer.bixolon;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.bxl.printer.POSPrinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import com.mobit.Barcode;
import com.mobit.IApplication;
import com.mobit.Image;
import com.mobit.Line;
import com.mobit.PageData;
import com.mobit.PageFormatInfo;
import com.mobit.PrintItem;
import com.mobit.Printer;
import com.mobit.RawText;
import com.mobit.Rectangle;
import com.mobit.Text;

public class BixolonAndroidDeneme extends Bixolon {

	Context mContext;
	
	protected POSPrinter posPrinter;

	// private BixolonPrinter mBixolonPrinter = null;
	private Handler mHandler = null;

	@Override
	public void init(IApplication app, Printer printerInfo) throws Exception {
		super.init(app, printerInfo);
		mContext = (Context) app.getPlatform().getContext();
		
		/*
		 * initHandler(); mBixolonPrinter = new BixolonPrinter(mContext,
		 * mHandler, Looper.getMainLooper());
		 * mBixolonPrinter.findBluetoothPrinters();
		 * mBixolonPrinter.connect("74:F0:7D:E7:89:41");
		 */
	}

	@Override
	protected void finalize() throws Throwable {
		// if(mBixolonPrinter != null) mBixolonPrinter.disconnect();
		super.finalize();
	}

	@Override
	public String getModeleId() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public byte[] getPrintStream() throws IOException {
		// TODO Auto-generated method stub
		
		return bitmapGSv0();
		//return image;
	}

	@Override
	protected void begin_print(PageData page, PageFormatInfo pfi) {
		// TODO Auto-generated method stub
		DrawImage(null);
	}

	@Override
	protected void end_print() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void TextOut(Text text, PrintItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawLine(Line line) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawRectangle(Rectangle rect) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawBarcode(Barcode bc, PrintItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawImage(Image image) {
		// TODO Auto-generated method stub

	}

	
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
		
		Bitmap image1 = Bitmap.createBitmap(576, 40, Bitmap.Config.ARGB_8888);
		
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
		} */
		
		
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
		
		
	}
}

