package mobit.elec.mbs.get;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import mobit.elec.mbs.IDef;
import com.mobit.IResult;
import com.mobit.MobitException;
import mobit.elec.mbs.MbsException;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.server.Base64;
import mobit.elec.mbs.utility;

public class result implements IResult, IDeserialize {

	private String RTIP = "";
	private int RKOD = 0;
	private String RACIKLAMA = "";
	private ICommand cmd = null;

	public result()
	{
		
	}
	
	public result(String RACIKLAMA)
	{
		this.RTIP = IDef.PRN;
		this.RKOD = 0;
		this.RACIKLAMA = RACIKLAMA;
		
	}
	
	@Override
	public String toString()
	{
		return getRACIKLAMA();
	}
	
	@Override
	public int deserialize(ICommand cmd, String row) throws UnsupportedEncodingException, MobitException, ParseException {

		int err = 0;
		this.cmd = cmd;
		String header = row.substring(0, row.length() > 5 ? 5 : row.length());
		if (header.contains(IDef.OK)) {
			RTIP = IDef.OK;
			RKOD = 0;
			RACIKLAMA = "";
			err = 0;
		} else if (header.contains(IDef.FTL) || header.contains(IDef.ERR) || header.contains(IDef.WRN)) {

			error_recv(row);
			err = 0;
		} else {
			RTIP = IDef.PRN;
			RKOD = 0;
			RACIKLAMA = row;
			err = 0;
		}
		return err;
	}

	void error_recv(String buf) throws MobitException, UnsupportedEncodingException, ParseException {

		int i = 0, j = 0;

		for (i = 0; i < buf.length(); i++)
			if (buf.charAt(i) == ' ')
				break;
		if (buf.charAt(i) != ' ')
			throw new MobitException(String.format("%d : %s", i, buf));
		RTIP = buf.substring(0, i);
		i++;
		for (j = i; j < buf.length(); j++)
			if (buf.charAt(j) == ' ')
				break;
		if (buf.charAt(j) != ' ')
			throw new MobitException(String.format("%d : %s", i, buf));
		RKOD = utility.MBS_FORMAT.parse(buf.substring(i, j)).intValue();
		i = j + 1;
		for (j = i; j < buf.length(); j++)
			if (buf.charAt(j) == '\n')
				break;
		if (buf.charAt(j) == '\n') {
			byte[] decoded = Base64.decode(buf.substring(i, j), 0);
			RACIKLAMA = new String(decoded, "UTF-8");
			int tesisat_no = 0;
			int isemri_no = 0;
			if(cmd != null){
				tesisat_no = cmd.getTESISAT_NO();
				isemri_no = cmd.getSAHA_ISEMRI_NO();
			}
			throw new MbsException(RTIP, RKOD, RACIKLAMA, tesisat_no, isemri_no);
		}

	}

	public String getRTIP() {
		return RTIP;
	}

	public int getRKOD() {
		return RKOD;
	}

	public String getRACIKLAMA() {
		return RACIKLAMA;
	}

	public static boolean isSuccessfull(String rtip)
	{
		return (IDef.OK.equals(rtip)  || IDef.PRN.equals(rtip)) ? true : false;
	}
	public boolean isSuccessfull()
	{
		return result.isSuccessfull(RTIP);
	}

	public static boolean isPrintable(String rtip){
		return (IDef.PRN.equals(rtip)) ? true : false;
	}

	public boolean isPrintable()
	{
		return result.isPrintable(RTIP);
	}


	@Override
	public String getRESULT_TYPE() {
		// TODO Auto-generated method stub
		return getRTIP();
	}

	@Override
	public void setRESULT_TYPE(String RESULT_TYPE) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRESULT_CODE() {
		// TODO Auto-generated method stub
		return getRKOD();
	}

	@Override
	public void setRESULT_CODE(int RESULT_CODE) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRESULT() {
		// TODO Auto-generated method stub
		return getRACIKLAMA();
	}

	@Override
	public void setRESULT(String RESULT) {
		// TODO Auto-generated method stub
		
	}

}
