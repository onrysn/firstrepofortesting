package mobit.eemr.base;

public class CAmpProgReader extends CProgramReader {

	private static final AddressMapEntry[] m_TriFazeAddress = new AddressMapEntry[] {
			new AddressMapEntry("0.0.0()", "4082(10)"), new AddressMapEntry("1.8.1()", "4093(04)"),
			new AddressMapEntry("1.8.2()", "4097(04)"), new AddressMapEntry("1.8.3()", "409B(04)"),
			new AddressMapEntry("1.8.4()", "409F(04)"), new AddressMapEntry("5.8.0()", "40AB(04)"),
			new AddressMapEntry("8.8.0()", "40AF(04)"),

	};

	private static final AddressMapEntry[] m_MonoFazeAddress = new AddressMapEntry[] {
			new AddressMapEntry("0.0.0()", "10F0(10)"), new AddressMapEntry("1.8.0()", "1028(04)"),
			new AddressMapEntry("1.8.1()", "1010(04)"), new AddressMapEntry("1.8.2()", "1014(04)"),
			new AddressMapEntry("1.8.3()", "1018(04)"), new AddressMapEntry("1.8.4()", "101C(04)"),

	};

	public CAmpProgReader(CElecMeter pElecMeter)

	{
		super(pElecMeter);
	}

	@Override
	public void Open() throws Exception {
		char[] cmi = new char[1];
		char[] cti = new char[1];

		m_IEC62056.WriteOptionSelectMessage(0, GetElecMeter().GetInformation().baudRateId, 1);

		GetElecMeter().GetOptoProbe().IrSetConfig(GetElecMeter().GetInformation().baudRateId, 7, IDef.EVENPARITY);

		m_IEC62056.ReadProgrammingCommandMessage(cmi, cti, m_SpareBuffer, true, true);
	}

	@Override
	public boolean Read() throws Exception {
		long version;

		version = Long.parseLong(GetElecMeter().GetInformation().identification, 10);
		if (version == 5165 || version == 5192) {
			return ReadAddressList('1', m_TriFazeAddress);
		}

		m_IEC62056.WriteProgrammingCommandMessage('P', '1', "(sayo)");

		if (!m_IEC62056.ReadAcknowledgementMessage())
			return (false);

		ReadAddressList('1', m_MonoFazeAddress);
		return true;
	}

	boolean TranslateDataBlock(String pAddress, String pMappedAddress, 
			CDynamicBuffer pDataBlock) throws Exception
		{
			String address;
			int index;

			address =  pAddress.substring(0, 16);
			
			index = address.indexOf('(');
			if(index < 0) return false;
			
			if (pAddress.equals("0.0.0()"))
			{
				if (!DecodeSerialNumber(pDataBlock, m_SpareBuffer))
					return(false);

				utils.FormatBuffer(m_ValueBuffer, "%s(%s)", address, m_SpareBuffer.GetMemory());
			}
			else
			{
				if (pMappedAddress.charAt(0) == '4')
				{
					utils.FormatBuffer(m_ValueBuffer, "%s(%.3lf)", address, 
							utils.DecodeAsciiValue(new String(pDataBlock.GetMemory()), 1, 8, 16) / 1000.);
						
				}
				else
				{
					utils.FormatBuffer(m_ValueBuffer, "%s(%.2lf)", address, 
							utils.DecodeAsciiValue(new String(pDataBlock.GetMemory()), 1, 8, 10) / 100.);
				}
			}
			return(true);
		}

	boolean DecodeSerialNumber(CDynamicBuffer pDataSet, CDynamicBuffer pValue) throws Exception {
		char[] chr = new char[2 + 1];

		String s = new String(pDataSet.GetMemory());
		int index = s.indexOf('(');
		if (index < 0)
			return (false);

		index++;

		pValue.SetDataSize(0);
		while ((chr[0] = s.charAt(index++)) != ')') {
			if (chr[0] == '\0')
				return (false);

			if (!utils._istalnum(chr[0]))
				return (false);

			chr[1] = s.charAt(index++);
			if (!utils._istalnum(chr[1]))
				return (false);

			chr[2] = '\0';
			chr[0] = (char) (Long.parseLong(s, 16) & 0xff);
			pValue.Append(chr);

		}

		pValue.AppendTChar('\0');
		return true;
	}

}
