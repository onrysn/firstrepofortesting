package com.mobit;

public interface ILogin {

	IEleman Login(LoginParam param) throws Exception;
	void Logout() throws Exception;
	void ChangePassword(int kullaniciKodu, int sifre, int yeniSifre) throws Exception;
}
