package mobit.eemr.base;

public interface IDef extends mobit.eemr.IDef {

	int MAX_PATH = 260;
	
	public static final int NOPARITY          =  0;
	public static final int ODDPARITY         =  1;
	public static final int EVENPARITY        =  2;
	public static final int MARKPARITY        =  3;
	public static final int SPACEPARITY       =  4;

	public static final int ONESTOPBIT        =  0;
	public static final int ONE5STOPBITS      =  1;
	public static final int TWOSTOPBITS       =  2;
}
