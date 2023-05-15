package mobit.elec.mbs.get;

import java.text.ParseException;
import java.util.Date;

import mobit.elec.mbs.utility;
import mobit.elec.mbs.server.get_okuyucu;

public class okuyucu extends eleman {

	public okuyucu()
	{
		super();
	}
	
	public okuyucu(int ELEMAN_KODU, String ELEMAN_ADI, Date TARIH, 
			String FILLER, String SAAT, String RESET_DUR, int ELEMAN_SIFRE)
	{
		super(ELEMAN_KODU, ELEMAN_ADI, TARIH, FILLER, SAAT, RESET_DUR, ELEMAN_SIFRE);
		
	}
	
	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {

		String[] list = utility.parser(row, info);

		if (list.length < 2)
			throw new ParseException(row, list.length);

		String tarih = null, saat = null;
		
		setELEMAN_KODU(utility.MBS_FORMAT.parse(list[0]).intValue());
		setELEMAN_ADI(list[1]);
		if(list.length > 2){
			setTARIH(field.date_formatter2.parse(list[2]));
			tarih = list[2];
		}
		if(list.length > 3) setFILLER(list[3]);
		if(list.length > 4) saat = list[4];
		
		setSAAT(saat);
		if(tarih != null && saat != null)
			setTARIH(field.datetime_formatter.parse(tarih+saat));
		
		if (cmd instanceof get_okuyucu) {
			get_okuyucu ge = (get_okuyucu) cmd;
			setELEMAN_SIFRE(ge.ELEMAN_SIFRE);
		}

		return 0;
	}

}
