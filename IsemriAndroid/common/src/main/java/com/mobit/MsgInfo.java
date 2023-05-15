package com.mobit;

import java.util.ArrayList;
import java.util.List;

public class MsgInfo {

	private int msgCode;
	private String message;
	
	public int getMsgCode()
	{
		return msgCode;
	}
	public String getMessage()
	{
		return message;
	}
	
	public MsgInfo(int msgCode, String message){
		this.msgCode = msgCode;
		this.message = message;
	}
	
	private static List<MsgInfo> errorInfoList = new ArrayList<MsgInfo>();
	
	protected static void addMsgInfo(MsgInfo info){
		
		if(findMsgInfo(info.getMsgCode()) != null) return;
		errorInfoList.add(info);
	}
	
	public static MsgInfo findMsgInfo(int msgCode){
		
		for(MsgInfo info : errorInfoList){
			if(info.getMsgCode() == msgCode){
				return info;
			}
		}
		return null;
	}
	
	public static final int ISLEM_BASARILI = 0;
	public static final int ISLEM_BASARISIZ = 1;
	public static final int HATA_MESAJ_ACIKLAMASI_YOK = 2;
	public static final int APN_AYAR_YAPILMASI_GEREKIYOR = 3;
	public static final int DESTEKLENMIYOR = 4;
	
	
	
	static {
		addMsgInfo(new MsgInfo(ISLEM_BASARILI, "İşlem başarılı"));
		addMsgInfo(new MsgInfo(ISLEM_BASARISIZ, "İşlem başarısız!"));
		addMsgInfo(new MsgInfo(HATA_MESAJ_ACIKLAMASI_YOK, "Hata/mesaj açıklaması tanımlanmamış!"));
		addMsgInfo(new MsgInfo(APN_AYAR_YAPILMASI_GEREKIYOR, "APN ayarı yapılması gerekiyor"));
		addMsgInfo(new MsgInfo(DESTEKLENMIYOR, "Desteklenmiyor!"));
	}
	
}
