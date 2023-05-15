package mobit.elec.mbs;

import java.sql.ResultSet;

import mobit.elec.IEndeks;
import mobit.elec.ISerialize;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.mbs.get.field;

public class Endeks extends mobit.elec.Endeks implements ISerialize {
	
	public Endeks(IEndeks endeks) throws Exception{
		super(endeks.getType(), endeks.getSayacHaneSayisi(), endeks.getValue());
	}
	public Endeks(IEndeksTipi type){
		super(type);
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit)
	{
		super(type, digit);
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit, Double value) throws Exception {
		super(type, digit, value);
		// TODO Auto-generated constructor stub
	}
	public Endeks(IEndeksTipi type, ResultSet rs, String columnLabel) throws Exception
	{
		super(type, rs, columnLabel);
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit, String value) throws Exception
	{
		super(type, digit, value);
	}
	
	private static final String format = "%0"+field.s_ENDEKS+"."+field.s_ENDEKS_PREC+"f";
	private static final String formatStr = "%"+field.s_ENDEKS+"s";
	
	private static final String formatDemand = "%0"+field.s_DEMAND_ENDEKS+"."+field.s_DEMAND_PREC+"f";
	private static final String formatDemandStr = "%"+field.s_DEMAND_ENDEKS+"s";
	
	@Override
	public void toSerialize(StringBuilder sb) throws Exception
	{
		String s = "";
		
		if(getType().equals(EndeksTipi.Demand)){
			if(isEmpty())
				s = String.format(utility.MBS_LOCALE, formatDemandStr, "");
			else
				s = String.format(utility.MBS_LOCALE, formatDemand, getValue());
		}
		else {
			if(isEmpty())
				s = String.format(utility.MBS_LOCALE, formatStr, "");
			else
				s = String.format(utility.MBS_LOCALE, format, getValue());
				
		}
		sb.append(s);
	}
	
	public static IEndeks fromString(EndeksTipi type, ISayacHaneSayisi digit, String value) throws Exception
	{
		return new Endeks(type, digit, value);
	}
	
	
}
