package mobit.elec.mbs.put;

import mobit.elec.IPasswd;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_opasswd extends ICommand implements IPasswd {

	private int OKUYUCU_KODU;
	private int ESKI_SIFRE;
	private int YENI_SIFRE;
	
	public int getOKUYUCU_KODU() {
		return OKUYUCU_KODU;
	}
	public void setOKUYUCU_KODU(int oKUYUCU_KODU) {
		com.mobit.utility.check(oKUYUCU_KODU, field.s_OKUYUCU_KODU);
		OKUYUCU_KODU = oKUYUCU_KODU;
	}
	public int getESKI_SIFRE() {
		return ESKI_SIFRE;
	}
	public void setESKI_SIFRE(int eSKI_SIFRE) {
		com.mobit.utility.check(eSKI_SIFRE, field.s_ESKI_SIFRE);
		ESKI_SIFRE = eSKI_SIFRE;
	}
	public int getYENI_SIFRE() {
		return YENI_SIFRE;
	}
	public void setYENI_SIFRE(int yENI_SIFRE) {
		com.mobit.utility.check(yENI_SIFRE, field.s_YENI_SIFRE);
		YENI_SIFRE = yENI_SIFRE;
	}
	
	public put_opasswd(int oKUYUCU_KODU, int eSKI_SIFRE, int yENI_SIFRE) {
		super();
		setOKUYUCU_KODU(oKUYUCU_KODU);
		setESKI_SIFRE(eSKI_SIFRE);
		setYENI_SIFRE(yENI_SIFRE);
	}
	@Override
	protected Class<?> getClassInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String format = "PUT OPASSWD %d %d %d\n";
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, format, OKUYUCU_KODU, ESKI_SIFRE, YENI_SIFRE));
		
	}

}
