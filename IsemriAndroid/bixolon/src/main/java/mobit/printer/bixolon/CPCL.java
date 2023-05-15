package mobit.printer.bixolon;

import com.mobit.CPCLFont;
import com.mobit.Image;
import com.mobit.PrintItem;
import com.mobit.Text;

public class CPCL extends mobit.printer.CPCL {

	
	@Override
	protected void TextOut(Text text, PrintItem item) {

		int left = converter.toPixel(text.Left, xdpi);
		int top = converter.toPixel(text.Top, ydpi) + yoffset;
		int right = converter.toPixel(text.Right, xdpi);
		int bottom = converter.toPixel(text.Bottom, ydpi) + yoffset;

		if(text.isRight() && alignment != RIGHT){
			if(right != end){
				alignment = RIGHT;
				end = right;
				sb.append(String.format("%s %d\r\n", alignment, end));
			}
		}
		else if(text.isCenter() && alignment != CENTER){
			int center = (left + right)/2;
			if(center != end){
				alignment = CENTER;
				end = center;
				sb.append(String.format("%s %d\r\n", alignment, end));
			}
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
	protected void DrawImage(Image image) {
		
	/*	String value = (String)image.getValue();
		sb.append(String.format("BXL-NVIMAGE %s %s 1 1 0 %d %d\r\n",
				value.substring(0, 1),
				value.substring(1, 2),
				converter.toPixel(image.Left, xdpi),
				converter.toPixel(image.Top, ydpi) + yoffset
				));*/
		String value = (String)image.getValue();
		sb.append(String.format("BXL-NVIMAGE %s  1 1 0 %d %d\r\n",
				value.substring(0, 1),

				converter.toPixel(image.Left, xdpi),
				converter.toPixel(image.Top, ydpi) + yoffset
		));
	}
}
