package mobit.elec.mbs.put;

import java.util.List;

import com.mobit.IIslem;
import mobit.elec.IEndeks;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.ISerialize;
import mobit.elec.ITakilanSayac;
import mobit.elec.Sayaclar;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.utility;

public class put_isemri_sayac extends ICommand implements ISerialize, IIslem {
	
	
	private ISayaclar SAYACLAR = new Sayaclar();
	
	public ISayaclar getSAYACLAR(){ return SAYACLAR;}
	
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public put_isemri_sayac(ITakilanSayac sayac) throws Exception
	{
		SAYACLAR.add(sayac);
	}
	@Override
	public int getTESISAT_NO() {
		List<ISayacBilgi> sayaclar = SAYACLAR.getSayaclar();
		takilan_sayac ts = (takilan_sayac)sayaclar.get(0);
		return ts != null ? ts.getTESISAT_NO() : 0;
	}
	@Override
	public int getSAHA_ISEMRI_NO() {
		List<ISayacBilgi> sayaclar = SAYACLAR.getSayaclar();
		takilan_sayac ts = (takilan_sayac)sayaclar.get(0);
		return ts != null ? ts.getSAHA_ISEMRI_NO() : 0;
	}
	
	private static final String format = 
			"PUT ISEMRI_SAYAC %0"+field.s_TESISAT_NO+"d%0"+field.s_SAHA_ISEMRI_NO+"d";
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		List<ISayacBilgi> sayaclar = SAYACLAR.getSayaclar();
		for(int i = 0; i < sayaclar.size(); i++){
			takilan_sayac ts = (takilan_sayac)sayaclar.get(i);
			if(i == 0){
				b.append(String.format(utility.MBS_LOCALE, format, ts.getTESISAT_NO(), ts.getSAHA_ISEMRI_NO()));
			}
			ts.serialize_s(b);
		}
		
		IEndeks [] endeksler = takilan_sayac.endeksSirala(SAYACLAR.getEndeksler());
		for(int i = 0; i < endeksler.length; i++){
			if(i == 0) b.append("E");
			 utility.toSerialize(b, endeksler[i]);
		}
		
		b.append("\n");
	}

	

}

