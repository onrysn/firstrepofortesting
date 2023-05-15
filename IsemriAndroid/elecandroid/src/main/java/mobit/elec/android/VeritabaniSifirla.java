package mobit.elec.android;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.ILocation;
import com.mobit.IslemMaster;
import com.mobit.RecordStatus;

import java.util.Timer;
import java.util.TimerTask;

public class VeritabaniSifirla implements Runnable {

	private Timer timer;
	IApplication app = Globals.app;
	IForm form;
	public VeritabaniSifirla(IForm form)
	{
		this.form = form;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		app.ShowMessage(form, "Veritabanı sıfırlamak istiyor musunuz?", "Dikkat",
				DialogMode.YesNo, 1, 0, cevap1);
		return;
	}



	IDialogCallback cevap1 = new IDialogCallback() {

		@Override
		public void DialogResponse(int msg_id, DialogResult result) {
			// TODO Auto-generated method stub
			if (result.equals(DialogResult.Yes)) {

				int cnt = 0;
				try {
					cnt = IslemMaster.select(app.getConnection(), RecordStatus.Saved);
				}
				catch (Exception e){
					app.ShowException(form, e);
					return;
				}
				if(cnt > 0) {
					app.ShowMessage(form, "Gönderilmemiş işlem var!!! Yinede sıfırlamak istiyor musunuz?", "Dikkat",
							DialogMode.YesNo, 1, 0, cevap2);
				}
				else {
					islem.run();
				}

			}
		}
	};
	IDialogCallback cevap2 = new IDialogCallback() {

		@Override
		public void DialogResponse(int msg_id, DialogResult result) {
			// TODO Auto-generated method stub
			if (result.equals(DialogResult.Yes)) {
				islem.run();
			}
		}
	};

	Runnable islem = new Runnable() {
		@Override
		public void run() {



			app.runAsync(form, "Veritabanı sıfırlanıyor...", "", null, new Callback() {
				@Override
				public Object run(Object obj) {
					try {

						app.clearTables(app.getClearTableClass());

					} catch (Exception e) {
						// TODO Auto-generated catch block
						return e;
					}
					return null;
				}
			}, null);


		}
	};




}
