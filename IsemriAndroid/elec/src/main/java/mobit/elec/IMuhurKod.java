package mobit.elec;

import com.mobit.IEnum;

public interface IMuhurKod {

	public IEnum getKOD_CINSI();

	public int getMUHUR_KODU();

	public String getMUHUR_ACIKLAMA();
	
	public void setKOD_CINSI(IEnum durum);

	public void setMUHUR_KODU(int mUHUR_KODU);

	public void setMUHUR_ACIKLAMA(String mUHUR_ACIKLAMA);

	public void check(IEnum KOD_CINSI);
	
	public void setCheck(boolean checked);
	
	public boolean getCheck();

}
