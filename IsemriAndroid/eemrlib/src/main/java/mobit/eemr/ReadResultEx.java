package mobit.eemr;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import mobit.eemr.ReadResult;
import mobit.eemr.ReadResultEx;

public class ReadResultEx extends ReadResult {

	
	protected OpticMeterReadActivity activity = null;
	
	protected Object m_context = null;	

	public ReadResultEx(Object context) {
		super(context);

		if(context instanceof OpticMeterReadActivity)
			activity = (OpticMeterReadActivity)context;
	}
	
	@Override
	public int DataLine(String pAddress, String pDataLine) {

		super.DataLine(pAddress, pDataLine);
		YkpOkuma ykpOkuma = new YkpOkuma();
		if(activity != null){
			pDataLine=pDataLine.replace('&','*');
			if (ykpOkuma.YkpOkumaDurum==1 && !ykpOkuma.SayacMarka.equals("KOHLER (AEL)"))
            {
                String[] log=pDataLine.split("\n");
                ykpOkuma.setYkpResult(pDataLine);
                for (int i=0;i<log.length;i++){
                    activity.addItem(pAddress,log[i]);
                }

                try {
					Thread.sleep(2000);
				}catch (Exception e){

				}

                activity.finish();


            }
			else  if (ykpOkuma.YkpOkumaDurum==1 && ykpOkuma.SayacMarka.equals("KOHLER (AEL)")){
				ykpOkuma.setYkpResult(ykpOkuma.getYkpResult()+"--"+pDataLine);
				activity.addItem(pAddress, pDataLine);
			}
			else
            {
                activity.addItem(pAddress, pDataLine);
            }

		}

		return 0;
	}

}
