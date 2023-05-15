package mobit.elec.mbs.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobit.Globals;
import mobit.elec.mbs.get.ICommand;

public class get_time extends ICommand {

	final SimpleDateFormat dateFormatLocale;

	private Date date; // UTC

	public Date getUTC() {
		return date;
	}

	public get_time() {
		
		dateFormatLocale = new SimpleDateFormat("yyMMdd HHmmss", Globals.trLocale);
		
	}

	@Override
	protected Class<?> getClassInfo() {
		return null;
	}

	@Override
	public void toSerialize(StringBuilder b) {
		// TODO Auto-generated method stub
		b.append("GET TIME\n");
	}

	@Override
	public int deserialize(String b) throws ParseException {
		// TODO Auto-generated method stub
		try {
			date = dateFormatLocale.parse(b);
		}catch (Exception exx){
			date = dateFormatLocale.parse("000101 00000");
		}
		return 0;
	}

}
