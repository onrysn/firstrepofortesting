package mobit.eemr.base;

import java.util.Date;
import java.util.regex.Matcher;

public class CKhlProgReader extends CProgramReader {

	public CKhlProgReader(CElecMeter pElecMeter)
	{
		super(pElecMeter);
	}

	boolean Open(int baudRateId) throws Exception
	{
		m_IEC62056.WriteOptionSelectMessage(0, baudRateId, 1);
		
		if (!m_IEC62056.ReadAcknowledgementMessage())
			return(false);

		GetElecMeter().GetOptoProbe().IrSetConfig(baudRateId, 7, IDef.EVENPARITY);
		return true;
	}

	@Override
	public void Open() throws Exception
	{
		Open(GetElecMeter().GetInformation().baudRateId);
	}

	@Override
	public boolean Read() throws Exception
	{
		return(ReadAddressList(GetElecMeter().GetInformation().identification.startsWith("<1>") ? '2' : '5', null));
	}

	boolean ReadProfiles(int flags, Date pStartTime, Date pEndTime) throws Exception
	{
		int sayacYili;
		String [] pValue = new String[1];

		if (!QueryDataBlock('2', "0.9.2()", m_ValueBuffer))
			return(false);

		String s = utils.bytesToString(m_ValueBuffer.GetMemory());
		utils.SplitDataLine(s, pValue);
		
		sayacYili = Integer.parseInt(pValue[0]) + 2000;
		if ((flags & IDef.MBTMETERREADER_ENERGYPROFILE) != 0 && !ReadEnergyProfile(sayacYili))
			return(false);

		if ((flags & IDef.MBTMETERREADER_CURRENTPROFILE) != 0 && !ReadCurrentProfile(sayacYili))
			return(false);

		if ((flags & IDef.MBTMETERREADER_VOLTAGEPROFILE) != 0 && !ReadVoltageProfile(sayacYili))
			return(false);

		return(true);
	}

	boolean ReadEnergyProfile(int yil)
	{
		/*
		int oncAy, paketIndex;
		int [] paketAdedi = new int[1];
		int [] aktifIndeks = new int[1];

		QueryProfileInfo(12, paketAdedi, aktifIndeks);
		
		oncAy = -1;
		for(paketIndex = 0; paketIndex < paketAdedi[0]; paketIndex++)
		{
			TCHAR tarih[16 + 1];
			String [] pValues = new String[7];
			int queryIndex, ay10, ay01, ay, gun, saat, dakika, result;

			if (aktifIndeks[0] > paketIndex)
				queryIndex = aktifIndeks[0] - paketIndex;
			else
				queryIndex = paketAdedi[0] - (paketIndex - aktifIndeks[0]);

			utils.FormatBuffer(m_ValueBuffer, "128.12.%04d()", queryIndex);
				

			if (!QueryDataBlockEx('2', static_cast<const TCHAR*>(m_ValueBuffer.GetMemory()), 
				m_ValueBuffer))
				return(FALSE);

			utils.SplitDataLine(new String(m_ValueBuffer.GetMemory()), pValues, pValues.length);
			
			if (_stscanf(pValues[0], "%1d%1d%2d%2d%2d", &ay10, &ay01, &gun, &saat, &dakika) != 5)
				return(false);

			ay = (ay10 & 1) + ay01;
			if (oncAy != -1 && ay > oncAy)
				yil--;

			if (_sntprintf(tarih, sizeof(tarih) / sizeof(tarih[0]), "%04d-%02d-%02d %02d:%02d", 
				yil, ay, gun, saat, dakika) < 0)
				return(false);

			result = EnergyProfileHandler(pValues[1], pValues[3], pValues[5], tarih);
			if (result != 0)
				return(result > 0);

			oncAy = ay;
		} */
		return(true);
	}

	boolean ReadCurrentProfile(int yil)
	{
		/*
		int oncAy, paketIndex; 
		int [] paketAdedi = new int[1];
		int [] aktifIndeks = new int[1];

		QueryProfileInfo(11, paketAdedi, aktifIndeks);
		
		oncAy = -1;
		for(paketIndex = 0; paketIndex < paketAdedi[0]; paketIndex++)
		{
			TCHAR tarih[16 + 1];
			String [] pValues = new String[4];
			int queryIndex, ay10, ay01, ay, gun, saat, dakika, result;

			if (aktifIndeks[0] > paketIndex)
				queryIndex = aktifIndeks[0] - paketIndex;
			else
				queryIndex = paketAdedi[0] - (paketIndex - aktifIndeks[0]);

			utils.FormatBuffer(m_ValueBuffer, "128.11.%04d()", queryIndex);
			
			String s = utils.bytesToString(m_ValueBuffer.GetMemory());
			QueryDataBlockEx('2', s, m_ValueBuffer);

			utils.SplitDataLine(s, pValues);
			
			if (_stscanf(pValues[0], "%1d%1d%2d%2d%2d", &ay10, &ay01, &gun, &saat, &dakika) != 5)
				return(false);

			ay = (ay10 & 1) + ay01;
			if (oncAy != -1 && ay > oncAy)
				yil--;

			if (_sntprintf(tarih, sizeof(tarih) / sizeof(tarih[0]), "%04d-%02d-%02d %02d:%02d", 
				yil, ay, gun, saat, dakika) < 0)
				return(false);

			result = CurrentProfileHandler(pValues[1], pValues[2], pValues[3], tarih);
			if (result != 0)
				return(result > 0);

			oncAy = ay;
		}*/
		return(true);
	}

	boolean ReadVoltageProfile(int yil)
	{
		/*
		int oncAy, paketIndex;
		int [] paketAdedi = new int[1];
		int [] aktifIndeks = new int[1];

		QueryProfileInfo(10, paketAdedi, aktifIndeks);
		
		oncAy = -1;
		for(paketIndex = 0; paketIndex < paketAdedi; paketIndex++)
		{
			String tarih; 
			String [] pValues = new String[4];
			int queryIndex, ay10, ay01, ay, gun, saat, dakika, result;

			if (aktifIndeks > paketIndex)
				queryIndex = aktifIndeks - paketIndex;
			else
				queryIndex = paketAdedi - (paketIndex - aktifIndeks);

			utils.FormatBuffer(m_ValueBuffer, "128.10.%04d()", queryIndex);

			QueryDataBlockEx('2', utils.bytesToString(m_ValueBuffer.GetMemory()), m_ValueBuffer);

			utils.SplitDataLine(utils.bytesToString(m_ValueBuffer.GetMemory()), pValues);
			
			if (_stscanf(pValues[0], "%1d%1d%2d%2d%2d", &ay10, &ay01, &gun, &saat, &dakika) != 5)
				return(false);

			ay = (ay10 & 1) + ay01;
			if (oncAy != -1 && ay > oncAy)
				yil--;

			if (_sntprintf(tarih, sizeof(tarih) / sizeof(tarih[0]), "%04d-%02d-%02d %02d:%02d", 
				yil, ay, gun, saat, dakika) < 0)
				return(false);

			result = VoltageProfile(pValues[1], pValues[2], pValues[3], tarih);
			if (result != 0)
				return(result > 0);

			oncAy = ay;
		}*/
		return(true);
	}

	public static String QuerySerial(CIEC62056 pIEC62056, CDynamicBuffer pValueBuffer) throws Exception
	{
	
		pIEC62056.WriteProgrammingCommandMessage('R', '5', "0.0.0()");
			
		if (!pIEC62056.ReadDataMessage(pValueBuffer, true))
			throw new Exception("Sayaç seri alınamadı!");
		
        
		String s = utils.bytesToString(pValueBuffer.GetMemory());
		Matcher matcher = utils.parantez_ici.matcher(s);
        if(matcher.find()) return matcher.group();
        
        return null;
	}

	void QueryProfileInfo(int profileCode, int [] pPaketAdedi, int [] pAktifIndeks) throws Exception
	{
		String [] pValues = new String[2];

		utils.FormatBuffer(m_ValueBuffer, "128.%d.0000()", profileCode);

		String s = utils.bytesToString(m_ValueBuffer.GetMemory());
		QueryDataBlockEx('2', s, m_ValueBuffer);
		
		utils.SplitDataLine(s, pValues);
		
		pPaketAdedi[0] = Integer.parseInt(pValues[0]);
		pAktifIndeks[0] = Integer.parseInt(pValues[1]);
		
	}

}
