package mobit.elec;

import java.text.ParseException;

import com.mobit.IDetail;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacHaneSayisi;

public interface IEndeks extends IDetail {

	Object clone();
	
	boolean isEmpty();
	
	Double getValue() throws Exception;
	
	long getIntPart() throws Exception;
	
	long getDecimalPart() throws Exception;
	
	IEndeksTipi getType();
	
	ISayacHaneSayisi getSayacHaneSayisi();
	
	void setValue(Double value) throws ParseException;
	
	void setValue(String value) throws Exception;
	
	String toStringIntPart();
	
	String toStringDecimalPart();
}
