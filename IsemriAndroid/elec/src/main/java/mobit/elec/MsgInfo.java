package mobit.elec;


public class MsgInfo extends com.mobit.MsgInfo {
	
	
	public MsgInfo(int errorCode, String message)
	{
		super(errorCode, message);
	}
	
	public static MsgInfo info = new MsgInfo(0, "");
	
	public static final int TESISAT_VEYA_SAYAC_BULUNAMADI = 100;
	public static final int KARNE_BULUNAMADI = 101;
	public static final int YAZDIRMA_VERISI_DEGIL = 102;
	public static final int ONCE_BIR_TESISATA_KONUMLAN = 103;
	public static final int SON_KAYITTA_BULUNUYORSUNUZ = 104;
	public static final int ILK_KAYITTA_BULUNUYORSUNUZ = 105;
	public static final int FORM_TANIMLANMAMIS = 106;
	public static final int TESISAT_BULUNAMADI_SUNUCUDA_SORGULA = 107;
	public static final int OPTIK_PORT_BAGLANTI_KESILDI = 108;
	
	public static final int T_ENDEKSI_GIRIN = 201;
	public static final int T1_ENDEKSI_GIRIN = 202;
	public static final int T2_ENDEKSI_GIRIN = 203;
	public static final int T3_ENDEKSI_GIRIN = 204;
	
	public static final int ENDUKTIF_ENDEKSI_GIRIN = 112;
	public static final int KAPASITIF_ENDEKSI_GIRIN = 113;
	public static final int DEMAND_GIRIN = 114;
	public static final int OPTIK_PORT_ENERJI_SEVIYESI = 115;
	public static final int ZABIT_ISLENDI = 116;
	public static final int FARKLI_SAYAC_OKUTTUNUZ = 117;
	public static final int SAYAC_HANE_SAYISI_FARKLI = 118;
	public static final int BIRDEN_FAZLA_SAYAC_VAR = 119;
	public static final int SAYAC_BULUNAMADI = 120;
	public static final int TESISAT_SECIN = 121;
	public static final int MUHUR_SERINO_GIRIN = 122;
	public static final int MAKSIMUM_ABONE_DURUM_SAYISINA_ULASILDI = 123;
	public static final int MAKSIMUM_SAYAC_DURUM_SAYISINA_ULASILDI = 124;
	
	static {
		addMsgInfo(new MsgInfo(TESISAT_VEYA_SAYAC_BULUNAMADI, "Tesisat veya sayaç bulunamadı!"));
		addMsgInfo(new MsgInfo(KARNE_BULUNAMADI, "Karne bulunamadı!"));
		addMsgInfo(new MsgInfo(YAZDIRMA_VERISI_DEGIL, "Yazdırma verisi değil!"));
		addMsgInfo(new MsgInfo(ONCE_BIR_TESISATA_KONUMLAN, "Önce bir tesisata konumlanın!"));
		addMsgInfo(new MsgInfo(SON_KAYITTA_BULUNUYORSUNUZ, "Son kayıtta bulunuyorsunuz!"));
		addMsgInfo(new MsgInfo(ILK_KAYITTA_BULUNUYORSUNUZ, "Ilk kayıtta bulunuyorsunuz!"));
		addMsgInfo(new MsgInfo(FORM_TANIMLANMAMIS, "Form tanımlanmamış!"));
		addMsgInfo(new MsgInfo(TESISAT_BULUNAMADI_SUNUCUDA_SORGULA, "Tesisat bulunamadı. Sunucudan sorgulatmak ister misiniz?"));
		addMsgInfo(new MsgInfo(OPTIK_PORT_BAGLANTI_KESILDI, "Optik port bağlantısı kesildi!"));
		addMsgInfo(new MsgInfo(T_ENDEKSI_GIRIN, "T endeksi girin!"));
		addMsgInfo(new MsgInfo(T1_ENDEKSI_GIRIN, "T1 endeksi girin!"));
		addMsgInfo(new MsgInfo(T2_ENDEKSI_GIRIN, "T2 endeksi girin!"));
		addMsgInfo(new MsgInfo(T3_ENDEKSI_GIRIN, "T3 endeksi girin!"));
		addMsgInfo(new MsgInfo(ENDUKTIF_ENDEKSI_GIRIN, "Endüktif endeksi girin!"));
		addMsgInfo(new MsgInfo(KAPASITIF_ENDEKSI_GIRIN, "Kapasitif endeksi girin!"));
		addMsgInfo(new MsgInfo(DEMAND_GIRIN, "Demand girin!"));
		addMsgInfo(new MsgInfo(OPTIK_PORT_ENERJI_SEVIYESI, "Optik port enerji seviyesi!"));
		addMsgInfo(new MsgInfo(ZABIT_ISLENDI, "Zabıt işlendi"));
		addMsgInfo(new MsgInfo(FARKLI_SAYAC_OKUTTUNUZ, "Farklı bir sayacı okuttunuz. Devam etmek isityor musunuz?"));
		addMsgInfo(new MsgInfo(SAYAC_HANE_SAYISI_FARKLI, "Sayaç hane sayısı farklı. Devam etmek isityor musunuz?"));
		addMsgInfo(new MsgInfo(BIRDEN_FAZLA_SAYAC_VAR, "Anyı sayaç numarasından birden fazla sayaç var."));
		addMsgInfo(new MsgInfo(SAYAC_BULUNAMADI, "Sayaç bulunamadı."));
		addMsgInfo(new MsgInfo(TESISAT_SECIN, "Tesisat seçin"));
		addMsgInfo(new MsgInfo(MUHUR_SERINO_GIRIN, "Mühür seri/no girin"));
		addMsgInfo(new MsgInfo(MAKSIMUM_ABONE_DURUM_SAYISINA_ULASILDI, "Maksimum seçilebilecek abone durum sayısına ulaşıldı"));
		addMsgInfo(new MsgInfo(MAKSIMUM_SAYAC_DURUM_SAYISINA_ULASILDI, "Maksimum seçilebilecek sayaç durum sayısına ulaşıldı"));
		
		
	}

}
