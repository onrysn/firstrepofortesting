package mobit.elec.mbs.get;

import java.text.ParseException;
import java.util.Date;

import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.Globals;
import com.mobit.ICbs;
import com.mobit.IIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ITesisat;
import mobit.elec.enums.IslemDurum;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.put.put_endeks;

public class tesisat extends isemri_guncelle implements ITesisat {

	
	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub

		String[] list = utility.parser(row, info);

		if (list.length > 57)
			throw new ParseException(row, list.length);
		
		setKARNE_NO(utility.MBS_FORMAT.parse(list[0]).intValue());
		setSIRA_NO(utility.MBS_FORMAT.parse(list[1]).intValue());
		setSIRA_EK(utility.MBS_FORMAT.parse(list[2]).intValue());
		setTESISAT_NO(utility.MBS_FORMAT.parse(list[3]).intValue());
		setUNVAN(list[4]);
		setADRES(list[5]);
		setADRES_TARIF(list[6]);
		setSEMT(list[7]);
		setKESME_DUR(list[8]);
		setSOZLESME_DUR(list[9]);
		setCIFT_TERIM_DUR(utility.MBS_FORMAT.parse(list[10]).intValue() != 0);
		setPUANT_DUR(utility.MBS_FORMAT.parse(list[11]).intValue() != 0);
		setGIDEN_DURUM_KODU(utility.MBS_FORMAT.parse(list[12]).intValue());
		setTARIFE_KODU(utility.MBS_FORMAT.parse(list[13]).intValue());
		setKONTROL_SAYAC_DUR(utility.MBS_FORMAT.parse(list[14]).intValue() != 0);
		setCARPAN(utility.MBS_FORMAT.parse(list[15]).doubleValue());
		setSON_OKUMA_TARIHI(field.date_formatter.parse(list[16]));
		setANET_ISLETME_KODU(list[17]);
		setANET_ABONE_NO(list[18]);

		setCBS((ICbs) Cbs.fromString(list[19], list[20]));
		
		setSAHA_ISEMRI_NO(getTESISAT_NO());
		setISLEM_TIPI(IslemTipi.SayacOkuma);
		setISEMRI_DURUMU(IslemDurum.Atanmis);
		setATANMIS_GOREVLI(Globals.app.getEleman().getELEMAN_KODU());
		setISLEM_TARIHI(new Date());
		setISEMRI_TARIHI(new Date());

		if (list.length == 21)
			return 0;

		for(ISayacBilgi sb : SayacBilgi.fromString(list, 21))
			getSAYACLAR().add(sb);
		
		if (KONTROL_SAYAC_DUR == false)
			return 0;

		if (list.length == 39)
			return 0;

		for(ISayacBilgi sb : SayacBilgi.fromString(list, 39))
			getKSAYACLAR().add(sb);
		
		return 0;
	}

	// -------------------------------------------------------------------------

	//public static final String tableName = "tesisat";
	// Dökümana göre 2 hane gelmesi gereken GIDEN_DURUM_KODU 4 hane gelmektedir
	private static final int s_GIDEN_DURUM_KODU = 2;//field.s_GIDEN_DURUM_KODU_1;

	public static final FieldInfo[] info = new FieldInfo[] {

			/*new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			new FieldInfo(field.SAHA_ISEMRI_NO, DbType.INTEGER, field.s_SAHA_ISEMRI_NO, false),
			new FieldInfo(field.ISLEM_TIPI, DbType.VARCHAR, field.s_ISLEM_TIPI, false),
			new FieldInfo(field.ISEMRI_DURUMU, DbType.INTEGER, field.s_ISEMRI_DURUMU, false),
			new FieldInfo(field.ATANMIS_GOREVLI, DbType.INTEGER, field.s_ATANMIS_GOREVLI, false),
			new FieldInfo(field.ISLEM_TARIHI, DbType.DATE, field.s_ISLEM_TARIHI, false),
			new FieldInfo(field.ISEMRI_TARIHI, DbType.DATE, field.s_ISEMRI_TARIHI, false), 
			*/
			
			new FieldInfo(field.KARNE_NO, DbType.INTEGER, field.s_KARNE_NO, true),
			new FieldInfo(field.SIRA_NO, DbType.INTEGER, field.s_SIRA_NO, true),
			new FieldInfo(field.SIRA_EK, DbType.INTEGER, field.s_SIRA_EK, true),
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, true),
			new FieldInfo(field.UNVAN, DbType.VARCHAR, field.s_UNVAN, true),
			new FieldInfo(field.ADRES, DbType.VARCHAR, field.s_ADRES, true),
			new FieldInfo(field.ADRES_TARIF, DbType.VARCHAR, field.s_ADRES_TARIF, true),
			new FieldInfo(field.SEMT, DbType.VARCHAR, field.s_SEMT, true),
			new FieldInfo(field.KESME_DUR_T, DbType.VARCHAR, field.s_KESME_DUR_T, true),
			new FieldInfo(field.SOZLESME_DUR, DbType.VARCHAR, field.s_SOZLESME_DUR, true),
			new FieldInfo(field.CIFT_TERIM_DUR, DbType.INTEGER, field.s_CIFT_TERIM_DUR, true),
			new FieldInfo(field.PUANT_DUR, DbType.INTEGER, field.s_PUANT_DUR, true),
			new FieldInfo(field.GIDEN_DURUM_KODU, DbType.INTEGER, field.s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.TARIFE_KODU, DbType.INTEGER, field.s_TARIFE_KODU, true),
			new FieldInfo(field.KONTROL_SAYAC_DUR, DbType.INTEGER, field.s_KONTROL_SAYAC_DUR, true),
			new FieldInfo(field.CARPAN, DbType.REAL, field.s_CARPAN, true),
			new FieldInfo(field.SON_OKUMA_TARIHI, DbType.DATE, field.s_SON_OKUMA_TARIHI, true),
			new FieldInfo(field.ANET_ISLETME_KODU, DbType.VARCHAR, field.s_ANET_ISLETME_KODU, true),
			new FieldInfo(field.ANET_ABONE_NO, DbType.VARCHAR, field.s_ANET_ABONE_NO, true),

			new FieldInfo(field.CBS_ENLEM, DbType.REAL, field.s_CBS_ENLEM, true),
			new FieldInfo(field.CBS_BOYLAM, DbType.REAL, field.s_CBS_BOYLAM, true),

			new FieldInfo(field.MARKA_1, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_1, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_1, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_1, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_1, DbType.INTEGER, s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_1, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_2, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_2, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_2, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_2, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_2, DbType.INTEGER, s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_2, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_3, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_3, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_3, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_3, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_3, DbType.INTEGER, s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_3, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_K1, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_K1, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_K1, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_K1, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_K1, DbType.INTEGER, s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_K1, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_K2, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_K2, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_K2, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_K2, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_K2, DbType.INTEGER, s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_K2, DbType.INTEGER, field.s_IMAL_YILI, true),

			new FieldInfo(field.MARKA_K3, DbType.VARCHAR, field.s_MARKA, true),
			new FieldInfo(field.SAYAC_NO_K3, DbType.INTEGER, field.s_SAYAC_NO, true),
			new FieldInfo(field.SAYAC_CINSI_K3, DbType.VARCHAR, field.s_SAYAC_CINSI, true),
			new FieldInfo(field.HANE_SAYISI_K3, DbType.INTEGER, field.s_HANE_SAYISI, true),
			new FieldInfo(field.GIDEN_DURUM_KODU_K3, DbType.INTEGER, s_GIDEN_DURUM_KODU, true),
			new FieldInfo(field.IMAL_YILI_K3, DbType.INTEGER, field.s_IMAL_YILI, true), 
			
			
			
	};

	// -------------------------------------------------------------------------

	
}
