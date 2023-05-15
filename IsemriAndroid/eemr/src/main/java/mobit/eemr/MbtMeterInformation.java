package mobit.eemr;


public class MbtMeterInformation implements Cloneable {

	public String flag;
	public String identification;
	public int supportedModes;
	public int baudRateId;
	
	@Override
	public MbtMeterInformation clone() throws CloneNotSupportedException 
	{
		return (MbtMeterInformation)super.clone();
	}
}
