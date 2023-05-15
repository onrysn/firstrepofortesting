package mobit.eemr;

public class ObisMapItem {

	public String pCode;
	public String pText;
	public boolean bMandatory;
	public int RetryCount;
	
	public ObisMapItem(String pCode, String pText, boolean bMandatory, int RetryCount)
	{
		
		this.pCode = pCode;
		this.pText = pText;
		this.bMandatory = bMandatory;
		this.RetryCount = RetryCount;
		
	}
	
}
