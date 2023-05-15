package mobit.eemr;

public class ReadMode2 {
	
	private static final int sOTOMATIK_MOD = 0;
	private static final int sREADOUT_MOD = 1;
	private static final int sPROGRAM_MOD = 2;
	private static final int sLONG_READOUT_MOD =3;
	private static final int sPROFIL_MOD = 7;
	private static final int sDLMS_COSEM=4;
	
	public static final ReadMode2 OTOMATIK_MOD = new ReadMode2(sOTOMATIK_MOD);
	public static final ReadMode2 READOUT_MOD = new ReadMode2(sREADOUT_MOD);
	public static final ReadMode2 PROGRAM_MOD = new ReadMode2(sPROGRAM_MOD);
	public static final ReadMode2 LONG_READOUT_MOD = new ReadMode2(sLONG_READOUT_MOD);
	public static final ReadMode2 pPROFIL_MOD = new ReadMode2(sPROFIL_MOD);
	public static final ReadMode2 DLMS_COSEM = new ReadMode2(sDLMS_COSEM);
	private int value;

	protected ReadMode2(int value) {

		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		switch (getValue()) {
		case sDLMS_COSEM:
			return "DLMS_COSEM";
		case sOTOMATIK_MOD:
			return "Otomatik";
		case sREADOUT_MOD:
			return "Uzun Readout";
		case sPROGRAM_MOD:
			return "Program Mod";
        case sLONG_READOUT_MOD:
            return "Long Readout";
		case sPROFIL_MOD:
			return "Profil Mod";

		}

		return "";
	}
	private static final ReadMode2 [] values = new ReadMode2[] {OTOMATIK_MOD, READOUT_MOD, PROGRAM_MOD};
	public static ReadMode2 [] values()
	{
		return values;
	}
};

