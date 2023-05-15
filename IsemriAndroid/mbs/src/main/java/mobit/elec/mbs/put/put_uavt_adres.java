package mobit.elec.mbs.put;

import com.mobit.IEnum;
import mobit.elec.IAciklama;
import mobit.elec.ISerialize;
import mobit.elec.IUavtAdres;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_uavt_adres extends ICommand implements IUavtAdres {

	private int TESISAT_NO;
	private IEnum SEVIYE;
	private String KOD;
	private IAciklama ACIKLAMA;
	
	@Override
	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	@Override
	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	@Override
	public IEnum getSEVIYE() {
		return SEVIYE;
	}
	@Override
	public void setSEVIYE(IEnum sEVIYE) {
		SEVIYE = sEVIYE;
	}
	@Override
	public String getKOD() {
		return KOD;
	}
	@Override
	public void setKOD(String kOD) {
		com.mobit.utility.check(kOD, field.s_KOD);
		KOD = kOD;
	}

	@Override
	public IAciklama getACIKLAMA() {
		return ACIKLAMA;
	}

	@Override
	public void setACIKLAMA(IAciklama aCIKLAMA) throws Exception {
		if(aCIKLAMA != null) com.mobit.utility.check(aCIKLAMA.getSTR(), field.s_ACIKLAMA);
		ACIKLAMA = aCIKLAMA;
	}
	
	public put_uavt_adres(int tESISAT_NO, IEnum sEVIYE, String kOD, IAciklama aCIKLAMA) throws Exception {
		super();
		setTESISAT_NO(tESISAT_NO);
		setSEVIYE(sEVIYE);
		setKOD(kOD);
		setACIKLAMA(aCIKLAMA);
	}

	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT UAVT_ADRES %0"+ field.s_TESISAT_NO;
	
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO));
		((ISerialize)SEVIYE).toSerialize(b);
		b.append(String.format("%"+field.s_KOD+"s", KOD));
		((ISerialize)ACIKLAMA).toSerialize(b);
		
		b.append('\n');
	}

}
