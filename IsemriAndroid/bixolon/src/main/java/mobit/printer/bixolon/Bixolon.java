package mobit.printer.bixolon;

import java.io.IOException;


import com.bixolon.constant.BXLConst;
import com.bixolon.printer.BXLPrinter;

import com.mobit.Barcode;
import com.mobit.IApplication;
import com.mobit.IIslem;
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
import com.mobit.utility;

public class Bixolon extends IPrinterFormat {

	protected BXLPrinter printer = null;

	PageFormatInfo pfi = null;

	boolean blackMarker = false;
	int printMode = 0;
	boolean pageModeStarted = false;
	byte [] nvImage = null;

	@Override
	public void init(IApplication app, Printer printerInfo) throws Exception {
		super.init(app, printerInfo);
		printer = new BXLPrinter(BXLConst.SPP_R300);
		
		
	}

	@Override
	public void close()throws Exception
	{

	}

	@Override
	public String getModeleId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected static final byte[] formFeed = new byte[] { '\f' };

	@Override
	public byte[] getPrintStream() throws IOException {
		// TODO Auto-generated method stub

		byte[] buf;
		if (printMode == 1)
			buf = printer.getNormalBuffer();
		else if (printMode == 2)
			buf = printer.getPagemodeBuffer();
		else
			buf = new byte[0];
		if (blackMarker && buf.length > 0)
			buf = utility.combine(buf, formFeed);
		printMode = 0;

		if(nvImage != null) buf = utility.combine(nvImage, buf);
		//buf = printNVGraphic("01", 1, 1);

		return buf;

	}

	@Override
	protected void begin_print(PageData page, PageFormatInfo pfi) throws Exception {
		// TODO Auto-generated method stub
		this.pfi = pfi;
		blackMarker = pfi.getBlackMark() != 0;

		nvImage = null;
		pageModeStarted = false;

		printer.initPrinter();

		if (!pfi.isRawText()) {
			printMode = 2;

			
		} else {
			printMode = 1;
			if(blackMarker)
				printer.changeLabelMode();
			else
				printer.changeReceiptMode();
		}
		printer.setCharacterSet(1254); // 857

	}

	@Override
	protected void end_print()throws Exception{
		// TODO Auto-generated method stub
		switch(printMode){
			case 1: 
				//if(blackMarker) printer.printText("\f"); 
				break;
			case 2: 
				if(pageModeStarted){
					printer.endPageMode(false);
					pageModeStarted = false;
				}
				break;
		}
		
	}

	void startPageMode() throws Exception
	{
		if(printMode != 2 || pageModeStarted) return;
		pageModeStarted = true;
		printer.beginPageMode(xoffset, yoffset, pageWidth, pageSize, true, blackMarker);
		printer.changeLabelMode();
	}

	@Override
	protected void TextOut(Text text, PrintItem item) throws Exception {

		startPageMode();

		if (!item.hasSubItems()) {
			TextOutInner(text, item);
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (PrintItem subItem : item.getSubItems())
			sb.append(subItem.getValue().toString());

		TextOutInner(new Text(text), new PrintItem("", sb.toString()));

		/*
		 * text = new Text(text); PrintItem [] subItems = item.getSubItems();
		 * float step = (text.Bottom - text.Top)/subItems.length; text.Bottom =
		 * text.Top + step;
		 * 
		 * for(PrintItem subItem : subItems){ TextOutInner(text, subItem);
		 * text.Top += step; text.Bottom += step; }
		 */
	}

	protected void TextOutInner(Text text, PrintItem item) throws Exception {
		// TODO Auto-generated method stub

		
		int alignment = 48;
		int x, y;
		if (text.isLeft()) {
			alignment = 48;
			x = converter.toPixel(text.Left, xdpi);
			y = converter.toPixel(text.Top, ydpi);
		} else if (text.isRight()) {
			alignment = 50;
			x = converter.toPixel(text.Left, xdpi);
			y = converter.toPixel(text.Top, ydpi);
		} else if (text.isCenter()) {
			alignment = 49;
			x = converter.toPixel(text.Left, xdpi);
			y = converter.toPixel(text.Top, ydpi);
		} else {
			alignment = 48;
			x = converter.toPixel(text.Left, xdpi);
			y = converter.toPixel(text.Top, ydpi);
		}

		if(text.font != null){
			String id = text.font.getId();
			if(id.equals("1")) printer.setRecFont(BXLConst.FontB);
			else if(id.equals("3")) printer.setRecFont(BXLConst.FontA);
			else printer.setRecFont(BXLConst.FontC);
		}

		printer.setBold(text.Bold);
		printer.setAlignment(alignment);
		
		printer.printTextInPageMode(BXLConst.PTR_PD_LEFT_TO_RIGHT, 
				x, y, item.getValue().toString());

	}


	@Override
	protected void DrawLine(Line line) throws Exception{
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawRectangle(Rectangle rect) throws Exception{
		// TODO Auto-generated method stub

	}

	@Override
	protected void DrawBarcode(Barcode bc, PrintItem item) throws Exception {
		// TODO Auto-generated method stub

		startPageMode();

		if(printMode == 1){
			printer.print1DBarcode(item.getValue().toString(), getBarcodeType(bc.Type), converter.toPixel(bc.Left, xdpi),
					converter.toPixel(bc.Top, ydpi), BXLConst.PTR_BC_CENTER, BXLConst.PTR_BC_TEXT_BELOW);
		}
		else if(printMode == 2){

			int height = converter.toPixel(bc.Height, ydpi);
			printer.print1DBarcodeInPageMode(BXLConst.PTR_PD_LEFT_TO_RIGHT,
					converter.toPixel(bc.Left, xdpi), converter.toPixel(bc.Top, ydpi),
					item.getValue().toString(), getBarcodeType(bc.Type), height, bc.Ratio,
					BXLConst.PTR_BC_CENTER, BXLConst.PTR_BC_TEXT_BELOW);
		}

	}

	protected int getBarcodeType(String type) throws Exception{
		if (type.equalsIgnoreCase("CODE128"))
			return BXLConst.PTR_BCS_Code128;

		return BXLConst.PTR_BCS_Code128;
	}


	@Override
	protected void DrawImage(Image image) throws Exception {
		// TODO Auto-generated method stub

		int res;
		startPageMode();

		if(printMode == 1) {
			nvImage = printNVGraphic(image.getValue().toString(), 1, 1);
		}
		else if(printMode == 2){
			int x = converter.toPixel(image.Left, xdpi);
			int y = converter.toPixel(image.Top, ydpi);

			if(image.file != null) {
				// android ortamında ImageIO sınıfı olmadığından çalışmıyor
				//printer.printBitmapInPageMode(BXLConst.PTR_PD_LEFT_TO_RIGHT,
				//		x, y, image.file.getAbsolutePath(), pageWidth, 0);
			}
			else {
				byte [] cmd = new byte[] {0x1b, '|', 0, 'B'};
				cmd[2] = (byte)image.getValue().toString().charAt(0);
				//cmd[3] = (byte)image.getValue().toString().charAt(1);
				//printer.printTextInPageMode(BXLConst.PTR_PD_LEFT_TO_RIGHT,
				//		x, y, new String(cmd));
			}

		}

	}



	@Override
	protected void RawTextOut(RawText text, PageData pd, IIslem islem) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * String [] rows = text.Prepare(app, pd).split("\n|\r\n"); for(String
		 * row : rows){ printer.printText(row + "\n"); }
		 */
		String s = text.Prepare(app, pd, islem);
		printer.printText(s);
	}

	//-------------------------------------------------------------------------

	static final byte [] graphicCommand = new byte[]{0x1d, 0x28, 0x4c, 0x0, 0x0, 0x30, 0x0};
	static byte [] buildPrintNVGraphicCommand(int function, byte [] parameter){
		byte [] buf = new byte[graphicCommand.length + parameter.length];
		System.arraycopy(graphicCommand, 0, buf, 0, graphicCommand.length);
		if(parameter != null) System.arraycopy(parameter, 0, buf, graphicCommand.length, parameter.length);
		int len = parameter.length + 2;
		buf[3] = (byte)(len & 0xff);
		buf[4] = (byte)((len >> 8) & 0xff);
		buf[6] = (byte)(function & 0xff);
		return buf;
	}

	static byte [] printNVGraphic(String ImageId, int xScale, int yScale)
	{
		byte [] parameter = new byte[]{(byte)ImageId.charAt(0), (byte)ImageId.charAt(1),
				(byte)(xScale & 0xff), (byte)(xScale & 0xff)};
		return buildPrintNVGraphicCommand(0x45, parameter);
	}

	@Override
	public void finish() {

	}
}
