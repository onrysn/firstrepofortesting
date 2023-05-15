package mobit.elec.mbs.enums;

import java.util.Locale;

import mobit.elec.ISerialize;
import mobit.elec.enums.IOkumaMetodu;

public enum OkumaMetodu implements IOkumaMetodu, ISerialize  {

	Optik('O'),
	Manuel('M');
	
	private char value;
	
	public int getValue()
	{
		return value;
	}
	
	OkumaMetodu(final char value)
	{
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (getValue()) {
		case 'O':
			return "Optik Port";
		case 'M':
			return "Manuel";
		}
		return null;
	}

	public static OkumaMetodu fromString(String x) {
		return fromChar(x.charAt(0));
	}
	
	public static OkumaMetodu fromChar(char x) {
		switch (x) {
		case 'O':
			return Optik;
		case 'M':
			return Manuel;
		}
		return null;
	}
	
	@Override
	public void toSerialize(StringBuilder b)
	{
		b.append(String.format(Locale.ENGLISH, "%c", getValue()));
	}
}
