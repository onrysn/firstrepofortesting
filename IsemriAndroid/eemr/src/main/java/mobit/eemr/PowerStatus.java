package mobit.eemr;

public class PowerStatus extends MbtProbePowerStatus
{
	public static final int sizeof = 4;
	
	public PowerStatus(char [] buf)
	{
		charging = buf[0];
		batteryVoltage = ((buf[2]<<8)|buf[1]);
		batteryLifePercent = buf[3];
	}
	
};