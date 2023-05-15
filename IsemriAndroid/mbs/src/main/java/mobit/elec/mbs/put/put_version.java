package mobit.elec.mbs.put;

import mobit.elec.IVersion;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.utility;

public class put_version extends ICommand implements IVersion {

	
	private static final String cmd = "PUT VERSION";
	
	private String VER;
	private String T;				/* A veya boşluk olursa ASCII mod, B ise binary */
	private String V;				/* S veya boşluk ise sabit genişlikli, D ise TAB'la ayrşlmış */
	private String MMM;				/* MOBIT (IT3000) için 001 olacak */
	private String BUILD_DATE;
	private String TYPE;
	private String MVERSION;			/* Terminal yazılımı versiyonu */
	
	public String getVER() {
		return VER;
	}

	public void setVER(String vER) {
		com.mobit.utility.check(vER, field.s_VER);
		VER = vER;
	}

	public String getT() {
		return T;
	}

	public void setT(String t) {
		com.mobit.utility.check(t, field.s_T);
		T = t;
	}

	public String getV() {
		return V;
	}

	public void setV(String v) {
		com.mobit.utility.check(v, field.s_V);
		V = v;
	}

	public String getMMM() {
		return MMM;
	}

	public void setMMM(String mMM) {
		com.mobit.utility.check(mMM, field.s_MMM);
		MMM = mMM;
	}

	public String getBUILD_DATE() {
		return BUILD_DATE;
	}

	public void setBUILD_DATE(String bUILD_DATE) {
		com.mobit.utility.check(bUILD_DATE, field.s_BUILD_DATE);
		BUILD_DATE = bUILD_DATE;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		com.mobit.utility.check(tYPE, field.s_TYPE);
		TYPE = tYPE;
	}

	public String getMVERSION() {
		return MVERSION;
	}

	public void setMVERSION(String mVERSION) {
		com.mobit.utility.check(mVERSION, field.s_MVERSION);
		MVERSION = mVERSION;
	}
	//Muhammed Gökkaya
	//Put Version

	public put_version()
	{

		//VER = "1117";
		VER = "1121";
		//VER = "1122";
		//VER = "1123";
		//VER = "1124";


		T = "B";
		V = utility.type == 1 ? "S" : "D";
		MMM = "001";
		//20300101
		BUILD_DATE = "20200114";
		TYPE = "KSM";
		MVERSION = "001";
	}
	
	public put_version(String vER, String t, String v, String mMM, String bUILD_DATE, String tYPE, String mVERSION) {
		super();
		setVER(vER);
		setT(t);
		setV(v);
		setMMM(mMM);
		setBUILD_DATE(bUILD_DATE);
		setTYPE(tYPE);
		setMVERSION(mVERSION);
	}

	@Override
	protected Class<?> getClassInfo()
	{
		return null;
	}
	
	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append(String.format(utility.MBS_LOCALE, "%s %s %s%s %s %s %s %s\n", 
				cmd, VER, T, V, MMM, BUILD_DATE, TYPE, MVERSION));
	}

}
