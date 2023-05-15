package mobit.eemr.base;

public class CAelProgReader extends CKhlProgReader {
	
	public CAelProgReader(CElecMeter pElecMeter)
	{
		super(pElecMeter);
	}

	@Override
	public void Open() throws Exception
	{
		int baudRateId;

	// AEL serisinin isik sidddeti cok yuksek, yuksek hizlarda okunamiyor
		baudRateId = GetElecMeter().GetInformation().baudRateId;
		if (baudRateId > CIEC62056.IEC62056_BR4800)
			baudRateId = CIEC62056.IEC62056_BR4800;

	// 2011 uretim AEL.MF.07 ve 2015 uretim AEL.TF.22 modellerinin cevaplari 2 saniyeden uzun surebiliyor
		GetElecMeter().GetOptoProbe().GetStream().SetReadTimeout(3 * 1000);

		Open(baudRateId);
	}

	@Override
	public boolean Read() throws Exception
	{
		return(ReadAddressList('2', null));
	}

}
