package mobit.eemr;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;

import java.util.logging.Logger;

import mobit.eemr.IMeterReader;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.ProbeEvent;
import mobit.eemr.ReadResultEx;

public class ProbeEventEx extends ProbeEvent {
	
	private static SparseArray<Activity> activityList = new SparseArray<Activity>();
	
	public static void addActivity(int sessionId, Activity activity) {
		activityList.put(sessionId, activity);
	}

	public static void removeActivity(Integer sessionId) {
		activityList.remove(sessionId);
	}

	public static Activity getActivity(Integer sessionId) {
		return activityList.get(sessionId);
	}

	protected Context context;
	protected OpticMeterReadActivity activity = null;
	public static Object sync = new Object();

	public ProbeEventEx(IMeterReader mr) {
		super(mr);
		context = (Context) super.context;
	}

	protected void finalize() throws Throwable {
		if (activity != null) {
			activity.finish();
			activity = null;
		}
		super.finalize();
	}

	private void createActivity(final Runnable r1) {

		if (activity != null) {

			if (!activity.isDestroyed()){
				r1.run();
				return;
			}
			removeActivity(sessionId);
			activity = null;

		}
		Handler handler = (Handler)mr.getHandler();

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {

					Intent intent = new Intent(context, OpticMeterReadActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("sessionId", sessionId);
					context.startActivity(intent);
					
					mr.getExecutor().submit(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							synchronized (sync) {
								try {
									//muhammed gökkaya
									sync.wait(5000);

								} catch (Exception e) { // TODO Auto-generated
														// catch block

								}
							}
							
							activity = (OpticMeterReadActivity) getActivity(sessionId);
							r1.run();							
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					synchronized (ProbeEventEx.sync) {
						ProbeEventEx.sync.notify();
					}

				}

			}
		};

		handler.post(r);

	}

	
	@Override
	public void TriggerEvent(final IReadResult _result) {
		Logger LOGGER = Logger.getLogger("AK2");
		LOGGER.info("sdsdas sdsda sdasdad adsads");

		final boolean silent = mr.getSilent();


		final Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Exception ex = null;

				final IReadResult result = (_result != null) ? _result : new ReadResultEx(activity);
				try {
					
					ProbeEventEx.this.mr.read(result);

				} catch (Exception e) {
					ex = e;
				}

				// ---------------------------------------------------------------------

				if (!silent && activity != null) {

					activity.runOnUiThread(new Runnable() {

						Exception e;
						
						Runnable init(Exception e) {
							this.e = e;
							return this;
						}

						public void run() {
							if (e != null) {
								activity.Failed(e);
								if (clb != null)
									clb.Failed();
							}
							else {

//								Program mod seçili değilse ve Yıldızlı endeks gelmediyse
								if (activity.getokumaModu()!=2 && !activity.sonObis()){

									AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
									dialog.setCancelable(false);
									dialog.setTitle("Emin Misiniz?");
									dialog.setMessage("Yıldızlı Endeksler/Kesinti Bilgisi alınmadı. Devam etmek istediğinize emin misiniz?");
									dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int id) {
											activity.setStatusText("Okuma bitti");

											View view = activity.getWindow().getDecorView().getRootView();
											view.postDelayed(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated method stub
													activity.finish();

												}

											}, 1000);

											ICallback c = clb;
											if (c != null) {
												YkpOkuma ykpOkuma = new YkpOkuma();
												if (ykpOkuma.YkpOkumaDurum == 1 && ykpOkuma.SayacMarka.equals("KOHLER (AEL)")) {
													activity.finish();

												} else {
													c.Read(ProbeEventEx.this, result);
												}

											}

										}
									});
									dialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int id) {

											activity.ObisReset();


											if (clb != null)
												clb.Failed();
											activity.finish();


											dialog.dismiss();
										}
									});
									dialog.show();


								}
								else{
								activity.setStatusText("Okuma bitti");

								View view = activity.getWindow().getDecorView().getRootView();
								view.postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										activity.finish();
										//MUHAMMED GOKKAYA
									}

								}, 1000);

								ICallback c = clb;
								if (c != null) {
									YkpOkuma ykpOkuma = new YkpOkuma();
									if (ykpOkuma.YkpOkumaDurum == 1 && ykpOkuma.SayacMarka.equals("KOHLER (AEL)")) {
										activity.finish();

									} else {
										c.Read(ProbeEventEx.this, result);
									}

								}
							}
							}
						}
					}.init(ex));

				} else {
					if (clb != null)
						clb.Read(ProbeEventEx.this, result);
				}

			}
		};

		if (!silent) {
			
			createActivity(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (activity != null) {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								activity.Starting();
								mr.getExecutor().submit(r);
								
							}
						});
					}
				}

			});
			
		} else {
			
			r.run();
		}

	}
	
	@Override
	public void PowerEvent() {

		/*
		if (mr.getSilent())
			return;

		activity.runOnUiThread(new Runnable() {
			public void run() {

				try {

					MbtProbePowerStatus ps = mr.GetPowerStatus();
					activity.setStatusText(ps.toString());
					utility2.showMessage(activity, "Optik port enerji seviyesi :" + ps);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					utility2.showMessage(activity, e.getMessage());
				}
			}
		});
		*/

	}

	
	@Override
	public void ConnectionResetEvent() {

		mr.Disconnect();

		if (mr.getSilent())
			return;

		utility2.showMessage(activity, "Optik portla bağlantı kesildi!");
		
	}
	
	
}
