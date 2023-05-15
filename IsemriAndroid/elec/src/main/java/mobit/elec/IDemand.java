package mobit.elec;


public interface IDemand {

	public boolean isEmpty();
	
	public double getValue() throws Exception;
	
	public void setValue(double value);
}
