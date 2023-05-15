package mobit.elec.android;

import java.util.Locale;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import com.mobit.Globals;


public class EndeksFilter implements InputFilter {

	int digit;
	int precDigits;
	Pattern pattern;
	StringBuilder builder = new StringBuilder(50);
	char decimalSepChar;
	public EndeksFilter(int digit, int precDigits)
	{
		setFilter(digit, precDigits);
	}
	
	public void setFilter(int digit, int precDigits)
	{
		this.digit = digit; 
		this.precDigits = precDigits;
		decimalSepChar = Globals.getDecimalFormatSymbols().getDecimalSeparator();
		String regex = String.format(Locale.ENGLISH, 
				"(([0-9]{1})([0-9]{0,%d})?)(\\%c[0-9]{0,%d})?", 
				digit - 1, decimalSepChar, precDigits);
		
		pattern = Pattern.compile(regex);
		
	}
	
	boolean isDecimal(char ch)
	{
		return Character.isDigit(ch) || ch == decimalSepChar;
	}
	
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		
		builder.setLength(0);
		builder.append(dest);
	
		SpannableStringBuilder ssbuilder;
		StringBuilder sbuilder;
		
		if(source instanceof SpannableStringBuilder){			
			ssbuilder = (SpannableStringBuilder)source;
			int i = 0;
			while(i < ssbuilder.length()){
				if(!isDecimal(ssbuilder.charAt(i))){
					ssbuilder.replace(i, i + 1, "");
					i = 0;
					continue;
				}
				i++;
			}
		}
		else if(source instanceof StringBuilder){
			sbuilder = (StringBuilder)source;
			int i = 0;
			while(i < sbuilder.length()){
				if(!isDecimal(sbuilder.charAt(i))){
					sbuilder.replace(i, i + 1, "");
					i = 0;
					continue;
				}
				i++;
			}
		}
		
		builder.replace(dstart, dend, source.toString());
		if (!pattern.matcher(builder.toString()).matches()) {
			return "";
		}
		return source;
		
	}

}
