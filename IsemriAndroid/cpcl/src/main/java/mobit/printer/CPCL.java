package mobit.printer;

import java.io.IOException;

import com.mobit.Barcode;
import com.mobit.CPCLFont;
import com.mobit.Font;
import com.mobit.IApplication;
import com.mobit.IBluetooth;
import com.mobit.IConverter;
import com.mobit.IIslem;
import com.mobit.IPlatform;
import com.mobit.IPrinterFormat;
import com.mobit.Image;
import com.mobit.Line;
import com.mobit.PageData;
import com.mobit.PageFormatInfo;
import com.mobit.PrintItem;
import com.mobit.Printer;
import com.mobit.RawText;
import com.mobit.Rectangle;
import com.mobit.Text;

public class CPCL extends IPrinterFormat {

	public static final String LEFT = "LEFT";
	public static final String CENTER = "CENTER";
	public static final String RIGHT = "RIGHT";
	
	
	protected StringBuilder sb = new StringBuilder(5*1024);
	protected IBluetooth bth = null;
	protected String alignment = LEFT;
	protected int end = 0;


	public CPCL()
	{
		super();
		
	}

	@Override
	public void init(IApplication app, Printer printerInfo) throws Exception
	{
		super.init(app, printerInfo);
		IPlatform platform = app.getPlatform();
		bth = platform.createBluetooth();
	}
	@Override
	public void close()
	{
		if(bth != null) bth.close();
	}

	@Override
	public String getModeleId()
	{
		return "CPCL";
	}
	
	@Override
	protected void begin_print(PageData page, PageFormatInfo pfi) {

		sb.setLength(0);
		alignment = LEFT;
		
		if(pfi.isRawText()) return;
		
		header();
	}
	
	protected void header()
	{
		
		String s = String.format("! %d %d %d %d %d\r\n", 
				xoffset, xdpi, ydpi, pageSize, quantity);
		sb.append(s);
	}

	@Override
	protected void end_print() {
		
		if(pfi.isRawText()) return;
		footer();
	}

	protected void footer()
	{
		if(blackMarker) sb.append("FORM\r\n");
		sb.append("PRINT\r\n");
	}
	
	@Override
	protected void TextOut(Text text, PrintItem item) {
		
		int left = converter.toPixel(text.Left, xdpi);
		int top = converter.toPixel(text.Top, ydpi) + yoffset;
		int right = converter.toPixel(text.Right, xdpi);
		int bottom = converter.toPixel(text.Bottom, ydpi) + yoffset;

		if(text.isRight()){
			alignment = RIGHT;
			end = right;
				sb.append(String.format("%s %d\r\n", alignment, end));

		}
		else if(text.isCenter()){
			int center = (left + right)/2;
			alignment = CENTER;
			end = center;
			sb.append(String.format("%s %d\r\n", alignment, end));

		}
		else if(text.isLeft() && alignment != LEFT) {
			alignment = LEFT;
			end = 0;
			sb.append(String.format("%s\r\n", alignment));
		}

		
		String font = "0";
		String size = "0";
		if(text.font != null){
			CPCLFont cpclFont = (CPCLFont)text.font;
			font = cpclFont.getName();
			size = cpclFont.Size;
		}
		
		sb.append(String.format("TEXT %s %s %d %d %s\r\n", 
				font,
				size,
				left,
				top,
				item.getValue()));

	}
	
	@Override
	protected void DrawLine(Line line) {
		
		sb.append(String.format("LINE %d %d %d %d %d\r\n",
				converter.toPixel(line.Left, xdpi),
				converter.toPixel(line.Top, ydpi) + yoffset,
				converter.toPixel(line.Right, xdpi),
				converter.toPixel(line.Bottom, ydpi) + yoffset,
				converter.toPixel(line.Width, xdpi)));

	}
	
	@Override
	protected void DrawRectangle(Rectangle rect) {
		
		sb.append(String.format("BOX %d %d %d %d %d\r\n",
				converter.toPixel(rect.Left, xdpi),
				converter.toPixel(rect.Top, ydpi) + yoffset,
				converter.toPixel(rect.Right, xdpi),
				converter.toPixel(rect.Bottom, ydpi) + yoffset,
				converter.toPixel(rect.Width, xdpi)));

	}

	protected String convertBarcodeType(String type)
	{
		String codeType;
		if(type.equals("CODE128")) codeType = "128";
		else codeType = "128"; 	
		return codeType;
	}
	
	@Override
	protected void DrawBarcode(Barcode bc, PrintItem item) {
		
		sb.append(String.format("BARCODE %s %d %d %d %d %d %s\r\n", 
				convertBarcodeType(bc.Type),
				converter.toPixel(1, xdpi),
				10,
				converter.toPixel(Math.abs(bc.Bottom - bc.Top), ydpi),
				converter.toPixel(bc.Left, xdpi),
				converter.toPixel(bc.Top, ydpi) + yoffset,
				item.getValue())); 
	}
	
	@Override
	public byte [] getPrintStream() throws IOException
	{

		// Geçici bir çözüm Bixolon yazıcılar farklı
		// Karakter sayfasına sahip.
		byte [] buf;
		String s = sb.toString();
		//buf = s.getBytes("857");
		buf = s.getBytes("cp1254");  //cp1254  857

		return buf;
	}
	
	@Override
	protected void DrawImage(Image image) {
		
		sb.append(String.format("PCX %d %d !<%s\r\n",
				converter.toPixel(image.Left, xdpi),
				converter.toPixel(image.Top, ydpi) + yoffset,
				image.getValue()));
	}

	protected void RawTextOut(RawText text, PageData pd, IIslem islem) throws Exception
	{
		
		sb.append(text.Prepare(app, pd, islem));
//		//ToCPCL(pd);
	}
	
	@SuppressWarnings("unused")
	private void ToCPCL(PageData pd)
	{
		
		String [] rows = sb.toString().split("\n");
		sb.setLength(0);
		
		int left = 0, top = 0, right = 760; // x10 mm
		
		Font font = (Font)pfi.getItem(CPCLFont.class, "0");
		
		header();
		
		for(String row : rows){
			Text text = new Text();
			text.Left = left;
			text.Top = top;
			text.Right = right;
			text.Bottom = top + 20; // 2mm
			text.font = font;
			TextOut(text, new PrintItem("", row));
			top += 25;
		}
		
		footer();
	
	}

	@Override
	public void finish() {

	}
}
