package mobit.elec.android;


import com.mobit.Callback;
import com.mobit.IForm;
import com.mobit.IIslem;
import mobit.elec.Globals;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;

public class SayacIslemTamamla extends IslemTamamla {

	public SayacIslemTamamla(IForm form, Callback clb) {
		super(form, clb);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!(Globals.app instanceof IElecApplication))
			return;

		app = (IElecApplication) Globals.app;

		IIslem islem = app.getActiveIslem();
		if (!(islem instanceof IIsemriIslem)) {
			return;
		}
		IIsemriIslem iislem = (IIsemriIslem)islem;
		ISayaclar sayaclar = null;//iislem.getTakilanSayaclar();
		if (!sayaclar.getSayaclar().isEmpty()) {
			for(ISayacBilgi sayac : sayaclar.getSayaclar()){
				try {
					
					app.sendIslem(form, (IIslem)sayac, clb, 15000);
				}
				catch(Exception e){
					app.ShowException(form, e);
					break;
				}
			}
		}
		
	}

}
