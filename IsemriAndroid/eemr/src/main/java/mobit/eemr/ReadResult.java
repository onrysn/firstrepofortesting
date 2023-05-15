package mobit.eemr;

import com.mobit.MobitRuntimeException;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;

public class ReadResult implements IReadResult {

	protected Object m_context = null;
	protected ObisMapItem[] m_pObisCodeMap = s_kombi_detay;

	private ReadMode2 mode = ReadMode2.OTOMATIK_MOD;
	private MeterType meterType = MeterType.MBTMETERREADER_GENERICMETER;

	private int m_retry = -1;

	private Date m_okuma_zamani;
	protected MbtMeterInformation emi;
	public  String alldata="";
	protected String m_sayac_no;

	protected String m_uretim_tarih;
	protected String m_kalibrasyon_tarih;
	protected String m_birinci_faz_kesilme_sayisi;
	protected String m_ikinci_faz_kesilme_sayisi;
	protected String m_ucuncu_faz_kesilme_sayisi;
	protected String m_toplam_notr_enerji;
	protected String m_toplam_notr_faz_enerji_farki;
	protected String m_cos_l1;
	protected String m_cos_l2;
	protected String m_cos_l3;

	protected String m_sayac_saati;
	protected String m_sayac_tarihi;
	protected String m_pil_durum_kodu;
	protected String m_tarife_bilgi;
	protected String m_klemens_kapagi_acilma;
	protected String m_demand_reset;
	protected String m_demand_reset_tarih;
	//muhammed gökkaya
	protected String m_lun_manyetik;
	protected String m_manyetik;
	protected String m_govde;

	private boolean tespit;
	protected Boolean m_x5;
	protected Boolean m_kombi;
	protected Integer m_fazSayisi;
	protected Integer m_haneSayisi;
	protected Integer m_voltaj;
	protected Integer m_uretimYili;

	protected String [] m_demand_ends = new String[s_demand_ends.length];
	protected String [] m_top_ends = new String[s_top_ends.length];
	protected String [] m_gunduz_ends = new String[s_gunduz_ends.length];
	protected String [] m_puant_ends = new String[s_puant_ends.length];
	protected String [] m_gece_ends = new String[s_gece_ends.length];
	protected String [] m_enduktif_ends = new String[s_enduktif_ends.length];
	protected String [] m_kapasitif_ends = new String[s_kapasitif_ends.length];


	protected String[] m_kesintiler = new String[s_kesintiler.length];
	protected String[] m_faz1_kesintiler = new String[s_faz1_kesintiler.length];
	protected String[] m_faz2_kesintiler = new String[s_faz2_kesintiler.length];
	protected String[] m_faz3_kesintiler = new String[s_faz3_kesintiler.length];

	protected String m_faz1_akim;
	protected String m_faz2_akim;
	protected String m_faz3_akim;

	protected String m_faz1_gerilim;
	protected String m_faz2_gerilim;
	protected String m_faz3_gerilim;

	public ReadResult(Object context) {
		m_context = context;
		reset();
	}

	@Override
	public void reset()
	{

		m_okuma_zamani = null;
		emi = null;

		m_sayac_no = null;
		m_uretim_tarih = null;
		m_kalibrasyon_tarih = null;
		m_birinci_faz_kesilme_sayisi = null;
		m_ikinci_faz_kesilme_sayisi = null;
		m_ucuncu_faz_kesilme_sayisi = null;
		m_toplam_notr_enerji = null;
		m_toplam_notr_faz_enerji_farki = null;
		m_cos_l1 = null;
		m_cos_l2 = null;
		m_cos_l3 = null;

		m_sayac_saati = null;
		m_sayac_tarihi = null;
		m_pil_durum_kodu = null;
		m_tarife_bilgi = null;
		m_klemens_kapagi_acilma = null;
		m_demand_reset = null;
		m_demand_reset_tarih = null;
		m_govde= null;
		m_lun_manyetik=null;
		m_manyetik=null;

		tespit = false;
		m_x5 = null;
		m_kombi = null;
		m_fazSayisi = null;
		m_haneSayisi = null;
		m_voltaj = null;
		m_uretimYili = null;


		for(int i = 0; i < m_demand_ends.length; i++) m_demand_ends[i] = null;
		for(int i = 0; i < m_top_ends.length; i++) m_top_ends[i] = null;
		for(int i = 0; i < m_gunduz_ends.length; i++) m_gunduz_ends[i] = null;
		for(int i = 0; i < m_puant_ends.length; i++) m_puant_ends[i] = null;
		for(int i = 0; i < m_gece_ends.length; i++) m_gece_ends[i] = null;
		for(int i = 0; i < m_enduktif_ends.length; i++) m_enduktif_ends[i] = null;
		for(int i = 0; i < m_kapasitif_ends.length; i++) m_kapasitif_ends[i] = null;

		for(int i = 0; i < m_kesintiler.length; i++) m_kesintiler[i] = null;
		for(int i = 0; i < m_faz1_kesintiler.length; i++) m_faz1_kesintiler[i] = null;
		for(int i = 0; i < m_faz2_kesintiler.length; i++) m_faz2_kesintiler[i] = null;
		for(int i = 0; i < m_faz3_kesintiler.length; i++) m_faz3_kesintiler[i] = null;

		m_faz1_akim = null;
		m_faz2_akim = null;
		m_faz3_akim = null;

		m_faz1_gerilim = null;
		m_faz2_gerilim = null;
		m_faz3_gerilim = null;

	}


	@Override
	public int getRetry()
	{
		return m_retry;
	}
	@Override
	public int incRetry()
	{
		return ++m_retry;
	}

	@Override
	public void setReadMode(ReadMode2 mode) {
		this.mode = mode;
	}

	@Override
	public ReadMode2 getReadMode() {
		return mode;
	}

	public void setMeterType(MeterType meterType)
	{
		this.meterType = meterType;
	}
	public MeterType getMeterType()
	{
		return meterType;
	}

	@Override
	public ObisMapItem[] GetObisCodeMap() {
		return m_pObisCodeMap;
	}

	@Override
	public void SetObisCodeMap(ObisMapItem[] pObisCodeMap) {
		m_pObisCodeMap = pObisCodeMap;
	}

	int x = 0;

	@Override
	public boolean NextAddress(int[] ppPosition, String[] pAddress) {

		ObisMapItem pItem;
		ObisMapItem[] pItems = GetObisCodeMap();
		/*
		 * if(ppPosition[0] == 0 && x != 0){ return false; } x++;
		 */

		if (ppPosition[0] >= pItems.length)
			return false;

		if (ppPosition[0] == pItems.length)
			return false;
		pItem = pItems[ppPosition[0]];

		pAddress[0] = String.format("%s()", pItem.pCode);
		ppPosition[0]++;

		return true;

	}

	@Override
	public int QueryError(String pAddress, int tryIndex) {

		if (m_pObisCodeMap != null) {
			for (ObisMapItem item : m_pObisCodeMap) {
				if (pAddress.indexOf(item.pCode) > -1) {
					if (tryIndex < item.RetryCount - 1)
						return 0; // Tekrar iste
					if (!item.bMandatory)
						return 1; // Alınamadı ancak okumada hataya düsürülmüyor
					break;
				}
			}
		}
		return -1; // Okuma hataya düşürülüyor
	}

	@Override
	public int DataLine(String pAddress, String pDataLine) {

		Logger LOGGER = Logger.getLogger("BYL");
		LOGGER.info(pDataLine);
		String[] pObisCode = new String[1];
		String[] pObisValue = new String[1];

		//Lun_Control obs=new Lun_Control();
		alldata+=pDataLine+"**";
		if (pDataLine.length()>=4) {
			pDataLine = pDataLine.replace('&', '*');
			if (pDataLine.substring(0, 4).equals("1-0:") || pDataLine.substring(0, 4).equals("1-1:") || pDataLine.substring(0, 4).equals("0-0:")) {
				pDataLine = pDataLine.substring(4, pDataLine.length());
			} else if (pDataLine.substring(0, 2).equals("0:")) {
				pDataLine = pDataLine.substring(2, pDataLine.length());
			}
		}
		//obs.setObisCode(obs.ObisCode+";"+pDataLine);
		/*
		if (pObisCode.length>0){
			for (int i=0;i<pObisCode.length;i++)
			{
				pObisCode[i]=pObisCode[i].replace('&','*');
			}
		}
		if (pObisValue.length>0)
		{
			for (int j=0;j<pObisValue.length;j++)
			{
				pObisValue[j]=pObisValue[j].replace('&','*');
			}
		}
		*/

		if (!utility.ParseDataSet(pDataLine, pObisCode, pObisValue)) {
			return 0;
		}

		if (pObisValue[0].equals("error")) {
			return (0);
		}

		if (pObisValue[0].equals("0.0.0")) {
			int i = 0;
			for (i = 0; i < pObisValue[0].length(); i++) {
				if (!(pObisValue[0].charAt(i) == '_' || pObisValue[0].charAt(i) == ' '))
					break;
			}
			if (i > 0)
				pObisValue[0] = pObisValue[0].substring(i);
		}

		if (pObisCode[0].equals(s_sayac_no)) {
			//HÜSEYİN EMRE ÇEVİK KOHLER SAYAÇLARDA SAYAÇ SERİ NO TANIMSIZ KARAKTER GELME SORUNU ÇÖZÜMÜ
			if (pObisValue[0].contains("\u007F\u007F\u007F\u007F\u007F\u007F\u007F\u007F"))m_sayac_no="0";
			else m_sayac_no = pObisValue[0];
			return 0;
		}

		if (pObisCode[0].equals(s_uretim_tarih)) {
			//HÜSEYİN EMRE ÇEVİK KOHLER SAYAÇLARDA  s_uretim_tarih TANIMSIZ KARAKTER GELME SORUNU ÇÖZÜMÜ
			if (pObisValue[0].contains("\u007F\u007F-\u007F\u007F-\u007F\u007F"))m_uretim_tarih="0-0-0";
			else m_uretim_tarih = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_kalibrasyon_tarih)) {
			//HÜSEYİN EMRE ÇEVİK KOHLER SAYAÇLARDA s_kalibrasyon_tarih TANIMSIZ KARAKTER GELME SORUNU ÇÖZÜMÜ
			if (pObisValue[0].contains("\u007F\u007F-\u007F\u007F-\u007F\u007F"))m_kalibrasyon_tarih="0-0-0";
			else m_kalibrasyon_tarih = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_birinci_faz_kesilme_sayisi)) {
			m_birinci_faz_kesilme_sayisi = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_ikinci_faz_kesilme_sayisi)) {
			m_ikinci_faz_kesilme_sayisi = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_ucuncu_faz_kesilme_sayisi)) {
			m_ucuncu_faz_kesilme_sayisi = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_sayac_saati)) {
			//Onur YASİN KOHLER SAYAÇLARDA s_sayac_saati TANIMSIZ KARAKTER GELME SORUNU ÇÖZÜMÜ
			if (emi.flag.equals("AEL") || emi.flag.equals("ELM")){
				m_sayac_saati = "00:00:00";
				return 0;
			}else {
				m_sayac_saati = getObisValue(pObisValue[0]);
				return 0;
			}

		}

		if (pObisCode[0].equals(s_toplam_notr_enerji)) {
			//Onur YASİN
			m_toplam_notr_enerji = getObisValue(pObisValue[0]);
			return 0;
		}

		if (pObisCode[0].equals(s_toplam_notr_faz_enerji_farki)) {
			//Onur YASİN
			m_toplam_notr_faz_enerji_farki = getObisValue(pObisValue[0]);
			return 0;
		}

		if (pObisCode[0].equals(s_cos_l1)) {
			//Onur YASİN
			m_cos_l1 = getObisValue(pObisValue[0]);
			return 0;
		}

		if (pObisCode[0].equals(s_cos_l2)) {
			//Onur YASİN
			m_cos_l2 = getObisValue(pObisValue[0]);
			return 0;
		}

		if (pObisCode[0].equals(s_cos_l3)) {
			//Onur YASİN
			m_cos_l3 = getObisValue(pObisValue[0]);
			return 0;
		}

		if (pObisCode[0].equals(s_sayac_tarihi)) {
			//Onur YASİN KOHLER SAYAÇLARDA s_sayac_tarihi TANIMSIZ KARAKTER GELME SORUNU ÇÖZÜMÜ
			if (emi.flag.equals("AEL") || emi.flag.equals("ELM")){
				m_sayac_tarihi = "00-00-00";
				return 0;
			}else {
				m_sayac_tarihi = getObisValue(pObisValue[0]);
				return 0;
			}
		}
		if (pObisCode[0].equals(s_pil_durum_kodu)) {
			m_pil_durum_kodu = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_tarife_bilgi)) {
			//Onur YASİN KOHLER SAYAÇLARDA s_tarife_bilgi TANIMSIZ KARAKTER GELME SORUNU ÇÖZÜMÜ
			if (emi.flag.equals("AEL") || emi.flag.equals("ELM")){
				m_tarife_bilgi="00-00-00,00:00";
				return 0;
			}
			else {
				if (pObisValue[0].contains("\u007F\u007F-\u007F\u007F-\u007F\u007F,\u007F\u007F:\u007F\u007F"))m_tarife_bilgi="0-0-0,0:0";
				else m_tarife_bilgi = getObisValue(pObisValue[0]);
				return 0;
			}
		}
		//muhammed gökkaya
		if (pObisCode[0].equals(s_govde)) {
			//21-05-20,15:03
			//saat bilgisi 00:00 ise
			if ((getObisValue(pObisValue[0]).split(",")[1]).equals("00:00"))
				m_govde="00-00-00,00:00";
			else
				m_govde = getObisValue(pObisValue[0]);

			return 0;
		}
		if (pObisCode[0].equals(s_lun_manyetik)) {
			m_lun_manyetik = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_manyetik)) {
			m_manyetik = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_klemens_kapagi_acilma)) {
			//21-05-20,15:12:adet:02 saat 00 ise hatalı
			if ((getObisValue(pObisValue[0]).split(",|\\:adet:")[1]).equals("00:00"))
				m_klemens_kapagi_acilma="00-00-00,00:00:adet:"+(pObisValue[0]).split(",|\\:adet:")[2];
			else
				m_klemens_kapagi_acilma = getObisValue(pObisValue[0]);

			return 0;
		}

		if (pObisCode[0].equals(s_demand_reset)) {
			m_demand_reset = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_demand_reset_tarih)) {
			m_demand_reset_tarih = getObisValue(pObisValue[0]);
			return 0;
		}

		if(obisCode2Value(s_demand_ends, m_demand_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_top_ends, m_top_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_gunduz_ends, m_gunduz_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_puant_ends, m_puant_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_gece_ends, m_gece_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_enduktif_ends, m_enduktif_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_kapasitif_ends, m_kapasitif_ends, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_kesintiler, m_kesintiler, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_faz1_kesintiler, m_faz1_kesintiler, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_faz2_kesintiler, m_faz2_kesintiler, pObisCode[0], pObisValue[0])) return 0;
		if(obisCode2Value(s_faz3_kesintiler, m_faz3_kesintiler, pObisCode[0], pObisValue[0]))return 0;

		if (pObisCode[0].equals(s_faz1_akim)) {
			m_faz1_akim = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_faz2_akim)) {
			m_faz2_akim = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_faz3_akim)) {
			m_faz3_akim = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_faz1_gerilim)) {
			m_faz1_gerilim = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_faz2_gerilim)) {
			m_faz2_gerilim = getObisValue(pObisValue[0]);
			return 0;
		}
		if (pObisCode[0].equals(s_faz3_gerilim)) {
			m_faz3_gerilim = getObisValue(pObisValue[0]);
			return 0;
		}

		return 0;
	}

	private String getObisValue(String pObisValue)
	{
		int i = pObisValue.indexOf('*');
		if (i > -1) return pObisValue.substring(0, i);
		return pObisValue;
	}

	private boolean obisCode2Value(String [] obisCodes, String [] obisValues, String pObisCode, String pObisValue)
	{
		//Muhammed Gökkaya
		for (int i = 0; i < obisCodes.length; i++) {
			String[] LgzArr=obisCodes[i].split("\\*");
			String LgzObisCode=LgzArr[0]+"*0"+i;
			if (pObisCode.equals(obisCodes[i]) || pObisCode.equals(LgzObisCode)) {
				obisValues[i] = getObisValue(pObisValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public int EnergyProfile(String pAct, String pEnd, String pCap, String pTime) {
		return -1;
	}

	@Override
	public int CurrentProfile(String pIL1, String pIL2, String pIL3, String pTime) {
		return -1;
	}

	@Override
	public int VoltageProfile(String pVL1, String pVL2, String pVL3, String pTime) {
		return -1;
	}

	public void set_Information(MbtMeterInformation emi) {
		this.emi = emi;
	}

	public MbtMeterInformation get_Information() {
		return emi;
	}

	@Override
	public void set_okuma_zamani(Date okuma_zamani) {
		m_okuma_zamani = okuma_zamani;
	}

	@Override
	public Date get_okuma_zamani() {
		return m_okuma_zamani;
	}

	@Override
	public String get_sayac_no() {
		//	if (m_sayac_no.contains(" "))return "0"; HÜSEYİN EMRE ÇEVİK 12.03.2021
		return m_sayac_no;
	}

	@Override
	public String get_demand_end() {
		return get_demand_end(0);
	}

	@Override
	public String get_top_end() {
		return get_top_end(0);
	}

	@Override
	public String get_gunduz_end() {
		return get_gunduz_end(0);

	}

	@Override
	public String get_puant_end() {
		return get_puant_end(0);
	}

	@Override
	public String get_gece_end() {
		return get_gece_end(0);
	}

	@Override
	public String get_enduktif_end() {
		return get_enduktif_end(0);
	}

	@Override
	public String get_kapasitif_end() {
		return get_kapasitif_end(0);
	}

	@Override
	public String get_uretim_tarih() {
		return m_uretim_tarih;
	}

	@Override
	public String get_kalibrasyon_tarih() {
		return m_kalibrasyon_tarih;
	}

	@Override
	public String get_birinci_faz_kesilme_sayisi() {
		return m_birinci_faz_kesilme_sayisi;
	}

	@Override
	public String get_ikinci_faz_kesilme_sayisi() {
		return m_ikinci_faz_kesilme_sayisi;
	}

	@Override
	public String get_ucuncu_faz_kesilme_sayisi() {
		return m_ucuncu_faz_kesilme_sayisi;
	}

	public String get_toplam_notr_enerji() {
		return m_toplam_notr_enerji;
	}

	public String get_toplam_notr_faz_enerji_farki() {
		return m_toplam_notr_faz_enerji_farki;
	}

	public String get_cos_l1() {
		return m_cos_l1;
	}

	public String get_cos_l2() {
		return m_cos_l2;
	}

	public String get_cos_l3() {
		return m_cos_l3;
	}

	@Override
	public String get_FlagCode() {
		if(emi == null || emi.flag == null) throw new MobitRuntimeException("Sayaç flag bilgisi yok!");
		return emi.flag;
	}

	@Override
	public String get_kimlik() {

		if(emi == null || emi.identification == null) throw new MobitRuntimeException("Sayaç kimlik bilgisi yok!");

		if (emi.identification.contains("<1>")){
			emi.identification= emi.identification.replace("<1>","");
		}

		if (emi.identification.contains("STR20")){
			emi.identification = "STR20";
		}
		//LUNA sayaçlarda model numarasından sayaç numarasını silme 8.21 HÜSEYİN EMRE ÇEVİK
		if (emi.identification.contains(get_sayac_no())){
			if (emi.identification.contains("LUN") ){
				return emi.identification.replace(get_sayac_no(),"");
			}
			if (emi.identification.contains("LSM") ){
				return emi.identification.split(get_sayac_no())[0];
			}
		}



		return emi.identification;
	}

	@Override
	public String get_sayac_saati() {
		return m_sayac_saati;
	}

	@Override
	public String get_sayac_tarihi() {
		return m_sayac_tarihi;
	}

	@Override
	public Date get_sayac_zamani() {
		int yil, ay, gun, _saat, dakika, saniye = 0;

		String tarih = get_sayac_tarihi();
		String saat = get_sayac_saati();
		if (!get_FlagCode().equals("LGZ")) {
			yil = Integer.parseInt(tarih.substring(0, 2));
			ay = Integer.parseInt(tarih.substring(3, 5));
			gun = Integer.parseInt(tarih.substring(6, 8));
			if (gun > 0) {
				yil += (yil < 1000) ? 2000 : 0;
			} else {
				yil = ay = gun = 0;
			}
		}
		else {
			yil = Integer.parseInt(tarih.substring(8, 10));
			ay = Integer.parseInt(tarih.substring(3, 5));
			gun = Integer.parseInt(tarih.substring(0, 2));
			if (gun > 0) {
				yil += (yil < 1000) ? 2000 : 0;
			} else {
				yil = ay = gun = 0;
			}
		}

		_saat = Integer.parseInt(saat.substring(0, 2));
		dakika = Integer.parseInt(saat.substring(3, 5));
		if(saat.length() >= 8) saniye = Integer.parseInt(saat.substring(6, 8));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, yil);
		cal.set(Calendar.MONTH, ay - 1);
		cal.set(Calendar.DAY_OF_MONTH, gun);
		cal.set(Calendar.HOUR_OF_DAY, _saat);
		cal.set(Calendar.MINUTE, dakika);
		cal.set(Calendar.SECOND, saniye);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	@Override
	public String get_pil_durum_kodu() {
		return m_pil_durum_kodu;
	}

	@Override
	public String get_tarife_bilgi() {
		return m_tarife_bilgi;
	}

	@Override
	public String get_klemens_kapagi_acilma() {
		return m_klemens_kapagi_acilma;
	}
	//muhammed gökkaya
	@Override
	public String get_govde() {
		return m_govde;
	}
	@Override
	public String get_lun_manyetik() {
		return m_lun_manyetik;
	}
	@Override
	public String get_manyetik() {
		return m_manyetik;
	}
	@Override
	public String get_demand_reset() {
		return m_demand_reset;
	}

	@Override
	public String get_demand_reset_tarih() {
		return m_demand_reset_tarih;
	}

	@Override
	public String[] get_kesintiler() {
		return m_kesintiler;
	}

	@Override
	public String[] get_faz1_kesintiler() {
		return m_faz1_kesintiler;
	}
	@Override
	public String[] get_faz2_kesintiler() {
		return m_faz2_kesintiler;
	}
	@Override
	public String[] get_faz3_kesintiler() {
		return m_faz3_kesintiler;
	}

	public Boolean getX5() {
		tespit();
		return m_x5;
	}

	public Boolean getKombi() {
		tespit();
		return m_kombi;
	}

	public Integer getFazSayisi() {
		tespit();
		return m_fazSayisi;
	}

	public Integer getHaneSayisi() {
		tespit();
		return m_haneSayisi;
	}

	public Integer getVoltaj() {
		tespit();
		return m_voltaj;
	}

	public Integer getUretimYili() {
		tespit();
		return m_uretimYili;
	}

	@Override
	public void tespit() {
		if (tespit)
			return;
		tespit = true;

		x5_tespit(SayacBilgi.sayacListe);
		kombi_tespit(SayacBilgi.sayacListe);
		faz_tespit(SayacBilgi.sayacListe);
		hane_sayisi_tespit();
		voltaj_tespit();
		uretim_yili_tespit();

	}

	private void x5_tespit(SayacBilgi[] sayacListe) {

		m_x5 = false;

		String flag = get_FlagCode();
		if (flag.isEmpty())
			return;

		for (SayacBilgi sb : sayacListe) {
			if (flag.equals(sb.kimlik)) {
				if (sb.x5)
					m_x5 = true;
				break;
			}
		}
	}

	private void kombi_tespit(SayacBilgi[] sayacListe) {
		m_kombi = false;

		String kimlik = get_kimlik();
		if (kimlik.isEmpty())
			return;

		for (SayacBilgi sb : sayacListe) {
			if (kimlik.equals(sb.kimlik)) {
				if (sb.kombi)
					m_kombi = true;
				break;
			}
		}

		if ((m_enduktif_ends[0] != null && !m_enduktif_ends[0].isEmpty())
				|| (m_kapasitif_ends[0] != null && !m_kapasitif_ends[0].isEmpty())) {
			m_kombi = true;
		}
	}

	private void faz_tespit(SayacBilgi[] sayacListe) {

		String kimlik = get_kimlik();
		if (kimlik.isEmpty())
			return;

		for (SayacBilgi sb : sayacListe) {
			if (kimlik.equals(sb.kimlik)) {
				m_fazSayisi = sb.fazSayisi;
				return;
			}
		}

		if (m_birinci_faz_kesilme_sayisi != null && !m_birinci_faz_kesilme_sayisi.isEmpty()) {
			m_fazSayisi = 1;
			if ((m_ikinci_faz_kesilme_sayisi != null && !m_ikinci_faz_kesilme_sayisi.isEmpty())
					|| (m_ucuncu_faz_kesilme_sayisi != null && !m_ucuncu_faz_kesilme_sayisi.isEmpty())) {
				m_fazSayisi = 3;
			}
		}

		// Sayaç kombi ise ya üç fazlı kombi yada X5 sayaçtır
		if (m_kombi == true || m_x5 == true)
			m_fazSayisi = 3;

	}

	private void hane_sayisi_tespit() {
		int i1, i2;
		String s;
		s = get_top_end().trim();
		i1 = s.indexOf('.');
		i2 = s.indexOf('.');
		m_haneSayisi = (i1 == i2) ? i1 : 0;
	}

	private void voltaj_tespit() {

		if(m_fazSayisi != null){
			switch(m_fazSayisi){
				case 1:
					m_voltaj = 220;
					break;
				case 3:
					m_voltaj = (m_x5) ? 57 : 380;
					break;
			}
		}

	}

	private void uretim_yili_tespit() {
		try {
			if (get_uretim_tarih() != null) {
				int uretimYili = Integer.parseInt(get_uretim_tarih().substring(0, 2));
				if (uretimYili > 0 && uretimYili < 25)
					m_uretimYili = uretimYili + 2000;
			}
		} catch (Exception e) {

		}
	}

	@Override
	public String get_demand_end(int i) {
		return m_demand_ends[i];
	}

	@Override
	public String get_top_end(int i) {
		return m_top_ends[i];
	}

	@Override
	public String get_gunduz_end(int i) {
		return m_gunduz_ends[i];
	}

	@Override
	public String get_puant_end(int i) {
		return m_puant_ends[i];
	}

	@Override
	public String get_gece_end(int i) {
		return m_gece_ends[i];
	}

	@Override
	public String get_enduktif_end(int i) {
		return m_enduktif_ends[i];
	}

	@Override
	public String get_kapasitif_end(int i) {
		return m_kapasitif_ends[i];
	}

	@Override
	public String get_faz1_akim()
	{
		return m_faz1_akim;
	}
	@Override
	public String get_faz2_akim()
	{
		return m_faz2_akim;
	}
	@Override
	public String get_faz3_akim()
	{
		return m_faz3_akim;
	}
	@Override
	public String get_faz1_gerilim()
	{
		return m_faz1_gerilim;
	}
	@Override
	public String get_faz2_gerilim()
	{
		return m_faz2_gerilim;
	}
	@Override
	public String get_faz3_gerilim()
	{
		return m_faz3_gerilim;
	}

	public static IReadResult fromString(String obis) throws Exception
	{

		String [] lines = obis.split("\r\n|\n");
		IReadResult r = new ReadResult(null);

		boolean ident = false;
		for(int i = 0; i < lines.length; i++){
			String line = lines[i];
			if(ident == false){
				try {
					int idx = line.indexOf('/');
					if(idx > -1) {
						line = line.substring(idx + 1);
						line = line.replace("&lt;", "<");
						line = line.replace("&gt;", ">");
						idx = line.indexOf('<');
						if(idx > -1) {
							MbtMeterInformation emi = new MbtMeterInformation();
							emi.flag = line.substring(0, idx);
							emi.identification = line.substring(idx);
							r.set_Information(emi);
							ident = true;
						}
					}
				}
				catch(Exception e){
					throw new Exception("Hatalı sayaç bilgisi!", e);
				}
				continue;
			}

			r.DataLine(null, line);
		}

		return r;
	}
}
