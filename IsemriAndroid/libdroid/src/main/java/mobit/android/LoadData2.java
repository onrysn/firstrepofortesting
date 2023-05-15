package mobit.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import com.mobit.Callback;
import com.mobit.DialogMode;

import java.util.logging.Logger;

public class LoadData2 extends AsyncTask<Object, Object, Object> implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {

	private ProgressDialog progressDialog = null;
	protected Activity activity = null;
	private SpannableString title = null;
	private SpannableString message = null;
	private Callback rPre = null;
	private Callback rDo = null;
	private Callback rPost = null;

	public LoadData2(Activity activity, String message, String title, Callback rPre, Callback rDo, Callback rPost) {

		
		this.activity = AndroidPlatform.getActivity(activity);
		this.rPre = rPre;
		this.rDo = rDo;
		this.rPost = rPost;
		if(activity != null && (message != null || title != null)){
			progressDialog = new ProgressDialog(activity);
			setTitleMessage(message, title);
		}

	}

	public LoadData2(Activity activity, String message, String title, Callback rDo, Callback rPost) {

		this(activity, message, title, null, rDo, rPost);
	}


	public LoadData2(Activity activity, String message, String title, Callback rDo) {

		this(activity, message, title, rDo, null);
	}

	public LoadData2(Activity activity, String message, String title) {
		this(activity, message, title, null);

	}

	public LoadData2(Activity activity, String message) {

		this(activity, message, "", null);
	}

	public LoadData2(Activity activity, String message, Callback r) {
		this(activity, message, "", r);
	}

	@Override
	protected void finalize() throws Throwable {
		if(utility.isValidDialog(progressDialog)){ 
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.finalize();
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		if(utility.isValidDialog(progressDialog)){
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
			// Bu true yapılırsa popup dışında bir yere dokunulura otomatik iptal ediyor
			progressDialog.setCancelable(false);
			progressDialog.setOnCancelListener(this);
			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"İptal", this);
			progressDialog.show();
		}

		if (rPre != null)
			rPre.run(null);

	};

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		try {

			if (rDo != null){
				Object obj = rDo.run(arg0);
				return obj;
			}
			
		} catch (Throwable e) {
			return e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		try {
			if (rPost != null)
				rPost.run(result);
		} finally {
			if(utility.isValidDialog(progressDialog)){
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	};

	public void setTitleMessage(String message, String title) {

		this.message = new SpannableString(message);
		this.title = new SpannableString(title);
		Logger LOGGER = Logger.getLogger("AK4");
		LOGGER.info(this.message.toString());
		if (message.length() > 0) {
			// this.message.setSpan(new RelativeSizeSpan(1.5f), 0,
			// message.length(), 0);
			this.message.setSpan(new ForegroundColorSpan(Color.BLACK), 0, message.length(), 0);
		}

		if (title.length() > 0) {
			// this.title.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(),
			// 0);
			this.title.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
			this.title.setSpan(new BackgroundColorSpan(AndroidPlatform.AndroidGreen.getColor()), 0, title.length(), 0);
		}

		if (Thread.currentThread().getId() != 1) {
			if(activity != null) {
				activity.runOnUiThread(new Runnable() {
					public void run() {
						if (utility.isValidDialog(progressDialog)) {
							progressDialog.setTitle(LoadData2.this.title);
							progressDialog.setMessage(LoadData2.this.message);
						}
					}
				});
			}
		} else {
			if(utility.isValidDialog(progressDialog)){
				progressDialog.setTitle(this.title);
				progressDialog.setMessage(this.message);
			}
		}
	}

	public void setMessage(String message) {
		setTitleMessage(message, "");
	}
	
	@Override
	protected void onCancelled(Object result)
	{
		super.onCancelled(result);
		if(utility.isValidDialog(progressDialog)){
			progressDialog.dismiss();
			progressDialog = null;
		}
		//AndroidPlatform.ShowMessage(activity, "İşlem iptal edildi!", "Uyarı", DialogMode.Ok, 0, null);
	}
	
	void Cancel()
	{
		if (getStatus() != AsyncTask.Status.RUNNING) return;
		cancel(true);
		
	}
	
	
	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		Cancel();
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		if(arg1 == DialogInterface.BUTTON_NEGATIVE){
			Cancel();
		}
	}
}
