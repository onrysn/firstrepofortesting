package mobit.eemr;

public class MbtProbePowerStatus implements Cloneable {

	public int charging;
	public int batteryVoltage;
	public int batteryLifePercent;

	@Override
	public MbtProbePowerStatus clone() throws CloneNotSupportedException 
	{
		return (MbtProbePowerStatus)super.clone();
	}
	
	@Override
	public String toString()
	{
		String s;
		String sarj = (charging != 0) ? "Sarj Oluyor" : "Sarj Olmuyor";
		s = String.format(" Pil Doluluk: %%%d, Pil voltaj: %.2fV, Sarj Durum: %s",
				batteryLifePercent,  batteryVoltage/100.0, sarj);
		return s;
	}
}
