package mobit.eemr;

public class ReadMode {
	//muhammed gökkaya
	private static final int sREADOUT = 1 << 0;
	//private static final int sREADOUT = 7;
	private static final int sLONGREADOUT = 1 << 1;
	private static final int sPROGRAMMODE = 1 << 2;
	private static final int sDLMS_COSEM = 1 << 4;
	private static final int sPROFILEMODE = 1 << 3;

	public static final ReadMode READOUT = new ReadMode(sREADOUT);
	public static final ReadMode LONGREADOUT = new ReadMode(sLONGREADOUT);
	public static final ReadMode PROGRAMMODE = new ReadMode(sPROGRAMMODE);
	public static final ReadMode PROFILEMODE = new ReadMode(sPROFILEMODE);
	public static final ReadMode DLMS_COSEM = new ReadMode(sDLMS_COSEM);

	private int value;

	protected ReadMode(int value) {

		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		switch (getValue()) {
		case sDLMS_COSEM:
			return "DLMS_COSEM";
		case sREADOUT:
			return "Readout";
		case sLONGREADOUT:
			return "Uzun Readout";
		case sPROGRAMMODE:
			return "Program Mod";
		case sPROFILEMODE:
			return "Profil Mod";


		}



		return "";
	}
	
	private static final ReadMode [] values = new ReadMode[] {READOUT, LONGREADOUT, PROGRAMMODE, PROFILEMODE};
	public static ReadMode [] values()
	{
		return values;
	}
};