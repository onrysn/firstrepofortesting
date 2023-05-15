package com.mobit;

import java.io.File;

public interface IFotograf {

	File getFotoDosyaAdi();
	
	void FotoKontrol() throws Exception;
	
	void addFoto(File foto);
	
	void removeFoto(File foto);
}
