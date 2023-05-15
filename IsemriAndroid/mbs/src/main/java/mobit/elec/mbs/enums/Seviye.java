package mobit.elec.mbs.enums;

import java.util.Locale;

import mobit.elec.ISerialize;
import mobit.elec.enums.ISeviye;

public enum Seviye implements ISeviye, ISerialize  {

	Tanimsiz(0), Ilce(1), Bucak(2), Koy(3), Mahalle(4), CaddeSokak(5), Bina(6), BagimsizBolum(7);

	private final int value;

	Seviye(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (this) {
		case Tanimsiz:
			return "Tanımsız";
		case Ilce:
			return "İlçe";
		case Bucak:
			return "Bucak";
		case Koy:
			return "Köy";
		case Mahalle:
			return "Mahalle";
		case CaddeSokak:
			return "Cadde/Sokak";
		case Bina:
			return "Bina";
		case BagimsizBolum:
			return "Bağımsız Bölüm";
		}

		return null;
	}
	
	@Override
	public void toSerialize(StringBuilder b)
	{
		b.append(String.format(Locale.ENGLISH, "%d", getValue()));
	}
}
