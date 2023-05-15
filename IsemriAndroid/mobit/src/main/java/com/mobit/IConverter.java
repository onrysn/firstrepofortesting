package com.mobit;

public interface IConverter {

	int toPixel(float m, int dpi);
	float fromPixel(int pixel, int dpi);
}
