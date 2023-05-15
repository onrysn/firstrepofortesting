package mobit.elec;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mobit.MobitException;
import com.mobit.utility;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacHaneSayisi;

public class Endeks implements IEndeks {
	
	protected IEndeksTipi type = null;
	protected ISayacHaneSayisi digit = null;
	protected Double value = null;
	
	protected static final String msg = "%s hatalı sayısal veri!";
	
	public Endeks(IEndeksTipi type){
		this.type = type;
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit)
	{
		this.type = type;
		this.digit = digit;
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit, Double value)
	{
		this.type = type;
		this.digit = digit;
		setValue(value);
		
	}
	public Endeks(IEndeksTipi type, ResultSet rs, String columnLabel) throws Exception
	{
		this.type = type;
		if(rs.wasNull()) return;
		try {
		 setValue(rs.getDouble(columnLabel));
		}
		catch(Exception e){
			throw new MobitException(columnLabel);
		}
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit, String value) throws Exception
	{
		this.type = type;
		this.digit = digit;
		setValue(value);
		
	}
	public Endeks(IEndeksTipi type, ISayacHaneSayisi digit, String tam, String kusurat) throws Exception
	{
	
		this(type, digit, kusurat != null && !kusurat.isEmpty() ?  
				tam + Globals.getDecimalFormatSymbols().getDecimalSeparator() + kusurat : tam);
		
	}
	
	@Override
	public Object clone()
	{
		return new Endeks(type, digit, value);
	}
	
	static final DecimalFormat nf1 = new DecimalFormat("#.###");
	@Override
	public String toString()
	{
		if(isEmpty()) return "";
		
		nf1.setDecimalFormatSymbols(Globals.getDecimalFormatSymbols());
		return nf1.format(getValue());	
	}
	
	String s;
	@Override
	public String toStringIntPart()
	{
		if(isEmpty()) return "";
		s = Long.toString(getIntPart());	
		return s;
	}
	
	static final DecimalFormat nf2 = new DecimalFormat("000");
	@Override
	public String toStringDecimalPart()
	{
		
		if(isEmpty()) return "";
		long l = getDecimalPart();
		if(l == 0) return "";
		s = nf2.format(l);	
		return s;
	}
	
	@Override
	public boolean isEmpty()
	{
		return value == null;
	}
	@Override
	public IEndeksTipi getType()
	{
		return type;
	}
	@Override
	public Double getValue()
	{
		//if(isEmpty()) throw new MobitException("Endeks tanımlı değil");
		return value;
	}
	
	long l;
	@Override
	public long getIntPart()
	{
		//if(isEmpty()) throw new MobitException("Endeks tanımlı değil");
		l = (long)(double)value;
		return l;
	}
	double d;
	@Override
	public long getDecimalPart()
	{
		//if(isEmpty()) throw new MobitException("Endeks tanımlı değil");
		//Hüseyin Emre Çevik
		long l = (long)Math.round(((value - (long)(double)value)*1000));//SAYI KÜSÜRATLI GELDİĞİ İÇİN YUVARLAMA YAPILIYOR
		return l;
	}
	
	@Override
	public ISayacHaneSayisi getSayacHaneSayisi()
	{
		return digit;
	}
	
	@Override
	public void setValue(Double value) {
		
		this.value = null;
		if(value != null && digit != null) 
			utility.check(value, digit.getValue());
		this.value = value;
		
	}
	
	@Override
	public void setValue(String value) throws Exception {
		
		this.value = null;
		if(value == null || value.isEmpty()) return;
		try {
			this.value = Globals.getNumberFormat().parse(value).doubleValue();
		}
		catch(NumberFormatException e){
			throw new MobitException(String.format(msg, value));
		}
		
	}
	
	public static IEndeks fromString(IEndeksTipi type, ISayacHaneSayisi digit, String value) throws Exception
	{
		return new Endeks(type, digit, value);
	}
	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return "Endeks";
	}
	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub
		List<String[]> list = new ArrayList<String[]>();
		list.add(new String[] { getType().toString(), toString()});
		return list;
	}
	
}

