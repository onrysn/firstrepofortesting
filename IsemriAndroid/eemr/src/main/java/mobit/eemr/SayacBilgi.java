package mobit.eemr;


public class SayacBilgi {

	public String kimlik;
	public int fazSayisi;
	public boolean kombi;
	public boolean x5;

	public SayacBilgi(String kimlik, int fazSayisi, boolean kombi, boolean x5) {
		this.kimlik = kimlik;
		this.fazSayisi = fazSayisi;
		this.kombi = kombi;
		this.x5 = x5;

	}

	public static SayacBilgi[] sayacListe = new SayacBilgi[] { 
			new SayacBilgi("LUN1", 1, false, false),
			new SayacBilgi("LUN2", 3, false, false),
			new SayacBilgi("LUN4", 3, true, false),
			new SayacBilgi("LUN3", 3, true, true),
			new SayacBilgi("LUN5", 3, true, true),
			new SayacBilgi("LUN6", 3, true, true),
			new SayacBilgi("LUN22", 3, true, false),
			new SayacBilgi("LSM35-TF", 3, true, true),
			//muhammed gökkaya luna
			new SayacBilgi("M500", 1, false, false),
			new SayacBilgi("T410.2216", 3, false, false),
			new SayacBilgi("C410.K0Y.2211", 3, false, false),
			new SayacBilgi("C500.K0Y.2211", 3, false, false),
			new SayacBilgi("T500.2251", 3, true, false),
			new SayacBilgi("T500.2510", 3, true, true),
			new SayacBilgi("C410.K0Y.2256", 3, true, true),
			new SayacBilgi("C500.K0Y.2256", 3, true, true),
			new SayacBilgi("AEL.MF.07", 1, false, false),
			new SayacBilgi("AEL.TF.16", 3, false, false),
			new SayacBilgi("AEL.TF.10", 3, true, false),
			new SayacBilgi("AEL.TF.09", 3, true, true),
			new SayacBilgi("AEL.TF.19", 3, true, true),
			new SayacBilgi("AEL.TF.20", 3, true, false),
			new SayacBilgi("VEM-M580DB0", 1, false, false),
			new SayacBilgi("VEM-M580DB2", 1, false, false),
			new SayacBilgi("VEM-T5100DB2", 3, false, false),
			new SayacBilgi("VEM-T5100DB0", 3, false, false),
			new SayacBilgi("VEM-C5100DB2", 3, true, false),
			new SayacBilgi("VEM-CX5CB2", 3, true, true),
			new SayacBilgi("VEM-CX5VB2", 3, true, true),
			new SayacBilgi("EC058", 1, false, false),
			new SayacBilgi("A5", 1, false, false),
			new SayacBilgi("EC15A", 3, false, false),
			new SayacBilgi("A6", 3, false, false),
			new SayacBilgi("EC25", 3, true, false),
			new SayacBilgi("EC25TP", 3, true, false),
			new SayacBilgi("EC15X", 3, true, true),
			new SayacBilgi("EC25TC", 3, true, true),
			new SayacBilgi("EC25TV", 3, true, true),
			//luna icin denemeler
	};
}
