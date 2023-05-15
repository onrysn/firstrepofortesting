package mobit.eemr;

public class MeterType {
	
	private static final int sMBTMETERREADER_GENERICMETER = 0;
	private static final int sMBTMETERREADER_OLDKOHLERMETER = 1;
	private static final int sMBTMETERREADER_FEDERALFTS3DMETER = 2;
	
	public static final MeterType MBTMETERREADER_GENERICMETER = new MeterType(sMBTMETERREADER_GENERICMETER);
	public static final MeterType MBTMETERREADER_OLDKOHLERMETER = new MeterType(sMBTMETERREADER_OLDKOHLERMETER);
	public static final MeterType MBTMETERREADER_FEDERALFTS3DMETER = new MeterType(sMBTMETERREADER_FEDERALFTS3DMETER);
	
	private int value;

	protected MeterType(int value) {

		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		switch (getValue()) {
		case sMBTMETERREADER_GENERICMETER:
			return "Genel";
		case sMBTMETERREADER_OLDKOHLERMETER:
			return "Köhler(2002 Öncesi)";
		case sMBTMETERREADER_FEDERALFTS3DMETER:
			return "Federal FTS3D";
		}

		return "";
	}
	
	
	private static final MeterType [] values = new MeterType[] {MBTMETERREADER_GENERICMETER, 
			MBTMETERREADER_OLDKOHLERMETER, MBTMETERREADER_FEDERALFTS3DMETER};
	
	public static MeterType [] values()
	{
		return values;
	}
};