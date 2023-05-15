package mobit.elec.mbs;

import com.mobit.MobitException;
import com.mobit.ServerException;

public class MbsException extends ServerException {

	/**
	 * 
	 */
	private static String caption = "Mbs Hata";
	String errType = "";
	int tesisat_no = 0;
	int isemri_no = 0;
		
	public MbsException(String errType, int errcode, String errmsg, int tesisat_no, int isemri_no)
	{
		super(errcode, errmsg);
		this.errType = errType;
		super.caption = MbsException.caption;
		this.tesisat_no = tesisat_no;
		this.isemri_no = isemri_no;
	}
	
	public String getErrType() {return errType;}
	public int getErrCode() {return code;}
	
	
	@Override
	public String getMessage()
	{
		StringBuilder msg = new StringBuilder();
		
		if(tesisat_no != 0) msg.append(String.format("Tesisat no: %d\n", tesisat_no));
		
		if(!getErrType().isEmpty()){
			msg.append(String.format("%s: %d %s", getErrType(), getErrCode(), super.getMessage()));
		}
		else {
			Throwable throwable = getCause();
			if(throwable != null)
				msg.append(String.format("%s %s", super.getMessage(), throwable.getMessage()));
			else
				msg.append(String.format("%s", super.getMessage()));
		}
		return msg.toString();
	}

	@Override
	public boolean isSuccessful()
	{
		return (errType != null && errType.equals(IDef.WRN));	
	}
	
	@Override
	public String toString()
	{
		return getMessage();		
	}
}
