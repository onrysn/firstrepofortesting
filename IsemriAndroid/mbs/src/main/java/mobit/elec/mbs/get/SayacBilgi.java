package mobit.elec.mbs.get;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IDialogCallback;
import com.mobit.MobitException;
import mobit.eemr.IReadResult;
import mobit.elec.Endeksler;
import mobit.elec.IEndeks;
import mobit.elec.IEndeksler;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayacMarka;
import mobit.elec.ISayaclar;
import mobit.elec.ISdurum;
import mobit.elec.MsgInfo;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IFazSayisi;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IVoltaj;
import mobit.elec.enums.SayacCinsi;
import mobit.elec.enums.SayacHaneSayisi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.utility;

public class SayacBilgi implements ISayacBilgi {

	protected ISayacKodu kod;
	protected ISayacMarka MARKA; /* CHAR(3) Marka */
	protected int SAYAC_NO; /* NUM 9(9) Sayac seri-no */
	protected ISayacCinsi SAYAC_CINSI; /*
								 * CHAR(1) M: mekanik, E: Elektronik, K:Kombi
								 */
	protected ISayacHaneSayisi HANE_SAYISI; /* NUM 9 Sayaç hane sayısı */
	protected ISdurum GIDEN_DURUM_KODU; /* NUM 99 Giden durum kodu */
	protected int IMAL_YILI; /* NUM 9999 İmal yılı verisi VER1112 */
	
	protected Date DAMGA_TARIHI; /* NUM 9(8). YYYYAAGG formatında damga tarihi */
	
	// Sadece elektrik uygulamasında kullanılıyor
	protected IFazSayisi FAZ_SAYISI; /* NUM 9. 1:MONOFAZE,3:TRIFAZE,5:X5 */
	protected int AMPERAJ; /* NUM 999 Amperaj */
	protected IVoltaj VOLTAJ; /* NUM 999 220, 380 */

	// Sadece su uygulamasında kullanılıyor
	// private String SAYAC_CAPI; /* NUM 99 */
	// private String REKORLU_DUR; /* NUM 9 */
	// private String SAYAC_SINIFI; /* NUM 9 */

	protected String MODEL_KODU = "     ";/*
										 * NUM 99999 İleri aşamada gerekebilecek
										 * model kodu
										 */
	/* E harfi gönderilecek */
	
	protected IEndeksler ENDEKSLER = new Endeksler(); /* NUM 9(7).999 Takılma Endeksleri */

	private IReadResult result = null;
	 
	
	@Override
	public Object clone() 
	{
		ISayacBilgi sb = new SayacBilgi();
		
		sb.setSAYAC_KODU(getSAYAC_KODU());
		sb.setMARKA(getMARKA());
		sb.setSAYAC_NO(getSAYAC_NO());
		sb.setSAYAC_CINSI(getSAYAC_CINSI());
		sb.setHANE_SAYISI(getHANE_SAYISI());
		try {
			sb.setGIDEN_DURUM_KODU(getGIDEN_DURUM_KODU());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.setIMAL_YILI(getIMAL_YILI()); 
		sb.setDAMGA_TARIHI(getDAMGA_TARIHI());
		sb.setFAZ_SAYISI(getFAZ_SAYISI());
		sb.setAMPERAJ(getAMPERAJ());
		sb.setVOLTAJ(getVOLTAJ());
		sb.setMODEL_KODU(getMODEL_KODU());

		for(IEndeks endeks : getENDEKSLER().getEndeksler()){
			sb.getENDEKSLER().add((IEndeks)endeks.clone());
		}
		try {
			sb.setOptikResult(getOptikResult());
		}
		catch(Exception e){
			return null;
		}
		
		return sb;
	
	}
	
	public List<String[]> getDetay() {
		List<String[]> list = new ArrayList<String[]>();

		list.add(new String[] { "Tipi:", getSAYAC_KODU().toString() });
		list.add(new String[] { "Marka:", getMARKA().toString() });
		list.add(new String[] { "No:", Integer.toString(getSAYAC_NO()) });
		list.add(new String[] { "Cinsi:", getSAYAC_CINSI().toString() });
		list.add(new String[] { "Hane Sayısı:", getHANE_SAYISI().toString() });
		list.add(new String[] { "Giden Durum:", getGIDEN_DURUM_KODU().toString() });
		list.add(new String[] { "İmal Yılı:", Integer.toString(getIMAL_YILI()) });

		return list;
	}

	@Override
	public ISayacKodu getSAYAC_KODU() {
		return kod;
	}

	@Override
	public ISayacMarka getMARKA() {
		return MARKA;
	}

	@Override
	public int getSAYAC_NO() {
		return SAYAC_NO;
	}

	@Override
	public ISayacCinsi getSAYAC_CINSI() {
		return SAYAC_CINSI;
	}

	@Override
	public ISayacHaneSayisi getHANE_SAYISI() {
		return HANE_SAYISI;
	}

	@Override
	public ISdurum getGIDEN_DURUM_KODU() {
		return GIDEN_DURUM_KODU;
	}

	@Override
	public int getIMAL_YILI() {
		return IMAL_YILI;
	}
	
	@Override
	public IEndeksler getENDEKSLER() {
		// TODO Auto-generated method stub
		return ENDEKSLER;
	}

	@Override
	public void setSAYAC_KODU(ISayacKodu kod) {
		this.kod = kod;
	}

	@Override
	public void setMARKA(ISayacMarka mARKA) {
		if(mARKA != null) com.mobit.utility.check(mARKA.getSAYAC_MARKA_KODU(), field.s_MARKA);
		MARKA = mARKA;
	}

	@Override
	public void setSAYAC_NO(int sAYAC_NO) {
		com.mobit.utility.check(sAYAC_NO, field.s_SAYAC_NO);
		SAYAC_NO = sAYAC_NO;
	}

	@Override
	public void setSAYAC_CINSI(ISayacCinsi sAYAC_CINSI) {
		SAYAC_CINSI = sAYAC_CINSI;
	}

	@Override
	public void setHANE_SAYISI(ISayacHaneSayisi hANE_SAYISI) {
		HANE_SAYISI = hANE_SAYISI;
	}

	@Override
	public void setGIDEN_DURUM_KODU(ISdurum gIDEN_DURUM_KODU) throws Exception {
		if(gIDEN_DURUM_KODU != null) com.mobit.utility.check(gIDEN_DURUM_KODU.getSAYAC_DURUM_KODU(), field.s_GIDEN_DURUM_KODU);
		GIDEN_DURUM_KODU = gIDEN_DURUM_KODU;
	}

	@Override
	public void setIMAL_YILI(int iMAL_YILI) {
		IMAL_YILI = iMAL_YILI;
	}
	
	@Override
	public IReadResult getOptikResult()
	{
		return result;
	}
	@Override
	public void setOptikResult(IReadResult result) throws Exception
	{
		if(result == null){
			this.result = result;
			return;
		}
		/*
		if(getSAYAC_NO() != Integer.parseInt(result.get_sayac_no()) && !Globals.isDeveloping())
			throw new MobitException("Farklı sayaca ait optik port verisi");
			*/
		this.result = result;
	}

	@Override
	public void sayacHaneKontrol(final ISayacHaneSayisi h, final Callback clb) {

		if (getHANE_SAYISI().equals(h)) {
			// optikEndeksDoldur(result);
			if (clb != null)
				clb.run(true);
			return;
		}

		Globals.app.ShowMessage(null, "Sayaç hane sayısı farklı. Devam etmek isityor musunuz?", "", DialogMode.YesNo, 2,
				0, new IDialogCallback() {

					@Override
					public void DialogResponse(int msg_id, DialogResult rs) {
						// TODO Auto-generated method stub
						if (rs.equals(DialogResult.Yes)) {

							if (clb != null)
								clb.run(false);
						} else {

						}
					}
				});

	}

	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDAMGA_TARIHI() {
		// TODO Auto-generated method stub
		return DAMGA_TARIHI;
	}

	@Override
	public void setDAMGA_TARIHI(Date DAMGA_TARIHI) {
		// TODO Auto-generated method stub
		this.DAMGA_TARIHI = DAMGA_TARIHI;
	}

	@Override
	public IFazSayisi getFAZ_SAYISI() {
		// TODO Auto-generated method stub
		return FAZ_SAYISI;
	}

	@Override
	public void setFAZ_SAYISI(IFazSayisi FAZ_SAYISI) {
		// TODO Auto-generated method stub
		this.FAZ_SAYISI = FAZ_SAYISI;
	}

	@Override
	public int getAMPERAJ() {
		// TODO Auto-generated method stub
		return AMPERAJ;
	}

	@Override
	public void setAMPERAJ(int AMPERAJ) {
		// TODO Auto-generated method stub
		this.AMPERAJ = AMPERAJ;
	}

	@Override
	public IVoltaj getVOLTAJ() {
		// TODO Auto-generated method stub
		return VOLTAJ;
	}

	@Override
	public void setVOLTAJ(IVoltaj VOLTAJ) {
		// TODO Auto-generated method stub
		this.VOLTAJ = VOLTAJ;
	}

	@Override
	public String getMODEL_KODU() {
		// TODO Auto-generated method stub


		return MODEL_KODU;
	}

	@Override
	public void setMODEL_KODU(String MODEL_KODU) {
		// TODO Auto-generated method stub


		this.MODEL_KODU = MODEL_KODU;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		
		if(obj instanceof ISayacBilgi){
			ISayacBilgi sb = (ISayacBilgi)obj;
			return getSAYAC_NO() == sb.getSAYAC_NO();
		}
		else if(obj instanceof Integer){
			Integer sayac_no = (Integer)obj;
			return getSAYAC_NO() == sayac_no;
		} 
		return super.equals(obj);
	}
	
	
	@Override
	public void Kontrol() throws Exception
	{
		
		if(getSAYAC_NO() == 0)
				throw new MobitException("Sayaç numarasını girin!");
		if(getSAYAC_KODU() == null)	
				throw new MobitException("Sayaç kodunu seçin!");
		if(getMARKA() == null)
				throw new MobitException("Sayaç marka seçin!");
		if(getSAYAC_CINSI() == null)
				throw new MobitException("Sayaç cinsi seçin!");
		if(getHANE_SAYISI() == null)
				throw new MobitException("Sayaç hane sayısı seçin!");
		if(getIMAL_YILI() == 0)
				throw new MobitException("Sayaç imal yılını girin!");
		if(getFAZ_SAYISI() == null)
				throw new MobitException("Sayaç faz sayısını seçin!");
		if(getAMPERAJ() == 0)
				throw new MobitException("Sayaç akımını giriniz!");
		if(getVOLTAJ() == null)	
				throw new MobitException("Sayaç gerilimini seçin!");
				
		
	}
	
	@Override
	public void EndeksKontrol() throws Exception
	{
		IEndeks endeks;
		ISayacKodu sk = getSAYAC_KODU();
		if(sk == null) throw new MobitException("Sayaç kodu yok");
		if(sk.is(SayacKodu.Aktif)){	
			ISayacCinsi sc = getSAYAC_CINSI();
			if(sc == null) throw new MobitException("Sayaç cinsi yok");
			
			if(sc.is(SayacCinsi.Mekanik)){
				endeks = getENDEKSLER().getEndeks(EndeksTipi.Gunduz);
				if(endeks == null || endeks.isEmpty()) throw new MobitException(MsgInfo.T_ENDEKSI_GIRIN);
			}
			else {
				// Elektronik veya kombi
				endeks = getENDEKSLER().getEndeks(EndeksTipi.Gunduz);
				if(endeks == null || endeks.isEmpty()) throw new MobitException(MsgInfo.T1_ENDEKSI_GIRIN);
				endeks = getENDEKSLER().getEndeks(EndeksTipi.Puant);
				if(endeks == null || endeks.isEmpty()) throw new MobitException(MsgInfo.T2_ENDEKSI_GIRIN);
				endeks = getENDEKSLER().getEndeks(EndeksTipi.Gece);
				if(endeks == null || endeks.isEmpty()) throw new MobitException(MsgInfo.T3_ENDEKSI_GIRIN);
			}
			
		}
		//HÜSEYİN EMRE ÇEVİK ENDÜKTİF ENDEKSİ GİRİLMESİ ZORUNLULUĞU KALDIRILDI
	/*	if(sk.is(SayacKodu.Reaktif)){
			endeks = getENDEKSLER().getEndeks(EndeksTipi.Enduktif);
			if(endeks == null || endeks.isEmpty()) throw new MobitException(MsgInfo.ENDUKTIF_ENDEKSI_GIRIN);
		}*/
		if(!sk.is(SayacKodu.Aktif) && sk.is(SayacKodu.Kapasitif)) {
			endeks = getENDEKSLER().getEndeks(EndeksTipi.Kapasitif);
			if (endeks == null || endeks.isEmpty())
				throw new MobitException(MsgInfo.KAPASITIF_ENDEKSI_GIRIN);
		}
	}
	
	
	public static SayacBilgi fromString(SayacKodu kod, String MARKA, String SAYAC_NO, String SAYAC_CINSI,
			String HANE_SAYISI, String GIDEN_DURUM_KODU, String IMAL_YILI) throws Exception {

		if (SAYAC_NO.trim().length() == 0)
			return null;
		//HÜSEYİN EMRE ÇEVİK SAYAC HANE SAYISI SIFIR OLAN KARNELERİN ÇEKİLMEME SORUNU 09.03.2021
		if (HANE_SAYISI.equals("0")){
			HANE_SAYISI="4";
		}
		SayacBilgi sb = new SayacBilgi();
		sb.setSAYAC_KODU(kod);
		sb.setMARKA(new sayac_marka(MARKA));
		sb.setSAYAC_NO(utility.MBS_FORMAT.parse(SAYAC_NO).intValue());
		sb.setSAYAC_CINSI(SayacCinsi.fromChar(SAYAC_CINSI.charAt(0)));
		sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(utility.MBS_FORMAT.parse(HANE_SAYISI).intValue()));
		String s = GIDEN_DURUM_KODU.trim();
		sb.setGIDEN_DURUM_KODU(sdurum.fromInteger((s.length() > 0) ? utility.MBS_FORMAT.parse(s).intValue() : 0));
		sb.setIMAL_YILI(utility.MBS_FORMAT.parse(IMAL_YILI).intValue());

		return sb;

	}

	public static void kombiTespit(List<ISayacBilgi> sayaclar) {

		if (sayaclar.size() <= 1)
			return;

		int i = 0;
		if (sayaclar.get(0).getSAYAC_NO() == sayaclar.get(1).getSAYAC_NO())
			i++;
		if (sayaclar.size() >= 3)
			if (sayaclar.get(0).getSAYAC_NO() == sayaclar.get(2).getSAYAC_NO())
				i++;

		if (i == 2) {
			sayaclar.get(0).setSAYAC_KODU(SayacKodu.Kombi);
			sayaclar.remove(2);
			sayaclar.remove(1);
		}
	}

	public static List<ISayacBilgi> fromString(String[] list, int index) throws Exception {

		List<ISayacBilgi> sayaclar = new ArrayList<ISayacBilgi>();
		SayacBilgi sb;

		sb = SayacBilgi.fromString(SayacKodu.Aktif, list[index], list[index + 1], list[index + 2], list[index + 3],
				list[index + 4], list[index + 5]);

		if (sb == null)
			return sayaclar;

		sayaclar.add(sb);

		sb = SayacBilgi.fromString(SayacKodu.Reaktif, list[index + 6], list[index + 7], list[index + 8],
				list[index + 9], list[index + 10], list[index + 11]);

		if (sb == null)
			return sayaclar;

		sayaclar.add(sb);

		sb = SayacBilgi.fromString(SayacKodu.Kapasitif, list[index + 12], list[index + 13], list[index + 14],
				list[index + 15], list[index + 16], list[index + 17]);

		if (sb == null)
			return sayaclar;

		sayaclar.add(sb);

		kombiTespit(sayaclar);

		return sayaclar;

	}

	public static List<ISayacBilgi> fromResultSet(ResultSet rs) throws Exception {

		SayacBilgi sb;
		List<ISayacBilgi> sayaclar = new ArrayList<ISayacBilgi>();

		if (rs.getInt(field.SAYAC_NO_1) > 0) {
			sb = new SayacBilgi();
			sb.setSAYAC_KODU(SayacKodu.Aktif);
			sb.setMARKA(sayac_marka.fromString(rs.getString(field.MARKA_1)));
			sb.setSAYAC_NO(rs.getInt(field.SAYAC_NO_1));
			sb.setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI_1)));
			sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI_1)));
			sb.setGIDEN_DURUM_KODU(sdurum.fromInteger(rs.getInt(field.GIDEN_DURUM_KODU_1)));
			sb.setIMAL_YILI(rs.getInt(field.IMAL_YILI_1));
			sayaclar.add(sb);
		}

		if (rs.getInt(field.SAYAC_NO_2) > 0) {
			sb = new SayacBilgi();
			sb.setSAYAC_KODU(SayacKodu.Reaktif);
			sb.setMARKA(sayac_marka.fromString(rs.getString(field.MARKA_2)));
			sb.setSAYAC_NO(rs.getInt(field.SAYAC_NO_2));
			sb.setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI_2)));
			sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI_2)));
			sb.setGIDEN_DURUM_KODU(sdurum.fromInteger(rs.getInt(field.GIDEN_DURUM_KODU_2)));
			sb.setIMAL_YILI(rs.getInt(field.IMAL_YILI_2));
			sayaclar.add(sb);
		}

		if (rs.getInt(field.SAYAC_NO_3) > 0) {
			sb = new SayacBilgi();
			sb.setSAYAC_KODU(SayacKodu.Kapasitif);
			sb.setMARKA(sayac_marka.fromString(rs.getString(field.MARKA_3)));
			sb.setSAYAC_NO(rs.getInt(field.SAYAC_NO_3));
			sb.setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI_3)));
			sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI_3)));
			sb.setGIDEN_DURUM_KODU(sdurum.fromInteger(rs.getInt(field.GIDEN_DURUM_KODU_3)));
			sb.setIMAL_YILI(rs.getInt(field.IMAL_YILI_3));
			sayaclar.add(sb);
		}

		kombiTespit(sayaclar);

		return sayaclar;

	}

	public static List<ISayacBilgi> fromResultSetK(ResultSet rs) throws Exception {

		SayacBilgi sb;
		List<ISayacBilgi> sayaclar = new ArrayList<ISayacBilgi>();

		if (rs.getInt(field.SAYAC_NO_K1) > 0) {
			sb = new SayacBilgi();
			sb.setSAYAC_KODU(SayacKodu.Aktif);
			sb.setMARKA(sayac_marka.fromString(rs.getString(field.MARKA_K1)));
			sb.setSAYAC_NO(rs.getInt(field.SAYAC_NO_K1));
			sb.setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI_K1)));
			sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI_K1)));
			sb.setGIDEN_DURUM_KODU(sdurum.fromInteger(rs.getInt(field.GIDEN_DURUM_KODU_K1)));
			sb.setIMAL_YILI(rs.getInt(field.IMAL_YILI_K1));
			sayaclar.add(sb);
		}
		if (rs.getInt(field.SAYAC_NO_K2) > 0) {
			sb = new SayacBilgi();
			sb.setSAYAC_KODU(SayacKodu.Reaktif);
			sb.setMARKA(sayac_marka.fromString(rs.getString(field.MARKA_K2)));
			sb.setSAYAC_NO(rs.getInt(field.SAYAC_NO_K2));
			sb.setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI_K2)));
			sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI_2)));
			sb.setGIDEN_DURUM_KODU(sdurum.fromInteger(rs.getInt(field.GIDEN_DURUM_KODU_K2)));
			sb.setIMAL_YILI(rs.getInt(field.IMAL_YILI_K2));
			sayaclar.add(sb);
		}
		if (rs.getInt(field.SAYAC_NO_K3) > 0) {
			sb = new SayacBilgi();
			sb.setSAYAC_KODU(SayacKodu.Kapasitif);
			sb.setMARKA(sayac_marka.fromString(rs.getString(field.MARKA_K3)));
			sb.setSAYAC_NO(rs.getInt(field.SAYAC_NO_K3));
			sb.setSAYAC_CINSI(SayacCinsi.fromString(rs.getString(field.SAYAC_CINSI_K3)));
			sb.setHANE_SAYISI(SayacHaneSayisi.fromInteger(rs.getInt(field.HANE_SAYISI_K3)));
			sb.setGIDEN_DURUM_KODU(sdurum.fromInteger(rs.getInt(field.GIDEN_DURUM_KODU_K3)));
			sb.setIMAL_YILI(rs.getInt(field.IMAL_YILI_K3));
			sayaclar.add(sb);
		}

		kombiTespit(sayaclar);

		return sayaclar;

	}

	public static int toStatement(int index, ISayaclar sayaclar, PreparedStatement stmt) throws Exception {

		for (ISayacBilgi sb : sayaclar.getSayaclar()) {

			SayacKodu i = (SayacKodu) sb.getSAYAC_KODU();

			
			if (i.is(SayacKodu.Aktif)) {
				stmt.setString(index + 0, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 1, sb.getSAYAC_NO());
				stmt.setString(index + 2, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				stmt.setInt(index + 3, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 4, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 5, sb.getIMAL_YILI());
			}

			if (i.is(SayacKodu.Reaktif)) {

				stmt.setString(index + 6, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 7, sb.getSAYAC_NO());
				stmt.setString(index + 8, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				stmt.setInt(index + 9, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 10, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 11, sb.getIMAL_YILI());

			}
			if (i.is(SayacKodu.Kapasitif)) {

				stmt.setString(index + 12, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 13, sb.getSAYAC_NO());
				stmt.setString(index + 14, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				stmt.setInt(index + 15, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 16, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 17, sb.getIMAL_YILI());
			}
			
		}
		return index + 18;
	}

	public static int toStatementT(int index, ISayaclar sayaclar, PreparedStatement stmt) throws Exception {

		for (ISayacBilgi sb : sayaclar.getSayaclar()) {

			SayacKodu i = (SayacKodu) sb.getSAYAC_KODU();
			if (i.is(SayacKodu.Aktif)) {
				stmt.setString(index, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 1, sb.getSAYAC_NO());
				if (sb.getSAYAC_CINSI() != null)
					stmt.setString(index + 2, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				if (sb.getHANE_SAYISI() != null)
					stmt.setInt(index + 3, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 4, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 5, sb.getIMAL_YILI());
			}

			if (i.is(SayacKodu.Reaktif)) {
				stmt.setString(index + 6, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 7, sb.getSAYAC_NO());
				if (sb.getSAYAC_CINSI() != null)
					stmt.setString(index + 8, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				if (sb.getHANE_SAYISI() != null)
					stmt.setInt(index + 9, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 10, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 11, sb.getIMAL_YILI());
			}

			if (i.is(SayacKodu.Kapasitif)) {
				stmt.setString(index + 12, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 13, sb.getSAYAC_NO());
				if (sb.getSAYAC_CINSI() != null)
					stmt.setString(index + 14, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				if (sb.getHANE_SAYISI() != null)
					stmt.setInt(index + 15, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 16, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 17, sb.getIMAL_YILI());
			}

		}

		return index + 18;
	}

	public static int toStatementTK(int index, ISayaclar sayaclar, PreparedStatement stmt) throws Exception {

		for (ISayacBilgi sb : sayaclar.getSayaclar()) {

			SayacKodu i = (SayacKodu) sb.getSAYAC_KODU();
			if (i.is(SayacKodu.Aktif)) {
				stmt.setString(index, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 1, sb.getSAYAC_NO());
				if (sb.getSAYAC_CINSI() != null)
					stmt.setString(index + 2, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				if (sb.getHANE_SAYISI() != null)
					stmt.setInt(index + 3, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 4, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 5, sb.getIMAL_YILI());
			}

			if (i.is(SayacKodu.Reaktif)) {
				stmt.setString(index + 6, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 7, sb.getSAYAC_NO());
				if (sb.getSAYAC_CINSI() != null)
					stmt.setString(index + 8, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				if (sb.getHANE_SAYISI() != null)
					stmt.setInt(index + 9, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 10, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 11, sb.getIMAL_YILI());
			}

			if (i.is(SayacKodu.Kapasitif)) {
				stmt.setString(index + 12, sb.getMARKA().getSAYAC_MARKA_KODU());
				stmt.setInt(index + 13, sb.getSAYAC_NO());
				if (sb.getSAYAC_CINSI() != null)
					stmt.setString(index + 14, String.valueOf((char) sb.getSAYAC_CINSI().getValue()));
				if (sb.getHANE_SAYISI() != null)
					stmt.setInt(index + 15, sb.getHANE_SAYISI().getValue());
				stmt.setInt(index + 16, sb.getGIDEN_DURUM_KODU().getSAYAC_DURUM_KODU());
				stmt.setInt(index + 17, sb.getIMAL_YILI());
			}
		}

		return index + 18;
	}

	

}
