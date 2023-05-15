package com.mobit;

public class X10mmToPixel implements IConverter {

	@Override
	public int toPixel(float m, int dpi) {
		// TODO Auto-generated method stub
		int pixel = (int) ((dpi * m) / 254);
		return (pixel > 0) ? pixel : 1;
	}
	@Override
	public float fromPixel(int pixel, int dpi) {
		// TODO Auto-generated method stub
		return (pixel * 254) / dpi;
	}
	

}
