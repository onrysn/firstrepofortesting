package mobit.elec.mbs.enums;

public class EndeksTipi extends mobit.elec.enums.EndeksTipi {

	public static final EndeksTipi Toplam = new EndeksTipi(0, EndeksTipi.Toplam);
	public static final EndeksTipi Gunduz = new EndeksTipi(1, EndeksTipi.Gunduz);
	public static final EndeksTipi Puant = new EndeksTipi(2, EndeksTipi.Puant);
	public static final EndeksTipi Gece = new EndeksTipi(3, EndeksTipi.Gece);
	public static final EndeksTipi Enduktif = new EndeksTipi(4, EndeksTipi.Enduktif);
	public static final EndeksTipi Kapasitif = new EndeksTipi(5, EndeksTipi.Kapasitif);
	public static final EndeksTipi Demand = new EndeksTipi(6, EndeksTipi.Demand);
		
	protected EndeksTipi(final Object value, final Object baseValue)
	{
		super(value, baseValue);
	}
}
