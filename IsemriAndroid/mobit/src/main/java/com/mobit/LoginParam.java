package com.mobit;

public class LoginParam {

	public int kullanici_kodu;
	public int kullanici_sifre;
	public int mode = 0;
	
	public LoginParam(int kullanici_kodu, int kullanici_sifre, int mode){
		this.kullanici_kodu = kullanici_kodu;
		this.kullanici_sifre = kullanici_sifre;
		this.mode = mode;
	}
}
