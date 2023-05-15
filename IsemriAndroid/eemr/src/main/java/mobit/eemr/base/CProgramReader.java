package mobit.eemr.base;

public abstract class CProgramReader extends CMeterReader {

	public CProgramReader(CElecMeter pElecMeter)
	{
		super(pElecMeter);
	}

	@Override
	public void Close()
	{
		int index;

		for(index = 0; index < 5; index++)
		{
			try {
				m_IEC62056.WriteBreakMessage();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
