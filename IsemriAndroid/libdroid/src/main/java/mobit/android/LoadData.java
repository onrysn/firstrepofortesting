package mobit.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import com.mobit.DialogMode;

public class LoadData extends AsyncTask<Void, Void, Void>
		implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {

	private ProgressDialog progressDialog = null;
	protected Activity activity = null;
	private SpannableString title = null;
	private SpannableString message = null;
	private Runnable rPre = null;
	private Runnable rDo = null;
	private Runnable rPost = null;

	public LoadData(Activity activity, String message, String title, Runnable rPre, Runnable rDo, Runnable rPost) {

		this.activity = activity;
		this.rPre = rPre;
		this.rDo = rDo;
		this.rPost = rPost;
		
		setTitleMessage(message, title);
		
		activity = AndroidPlatform.getActivity(activity);
		if(activity == null) return;
		
		progressDialog = new ProgressDialog(activity);
		

	}

	public LoadData(Activity activity, String message, String title, Runnable rDo, Runnable rPost) {

		this(activity, message, title, null, rDo, rPost);
	}

	public LoadData(Activity activity, String message, String title, Runnable rDo) {

		this(activity, message, title, rDo, null);
	}

	public LoadData(Activity activity, String message, String title) {
		this(activity, message, title, null);

	}

	public LoadData(Activity activity, String message) {

		this(activity, message, "", null);
	}

	public LoadData(Activity activity, String message, Runnable r) {
		this(activity, message, "", r);
	}

	@Override
	protected void finalize() throws Throwable {

		if (utility.isValidDialog(progressDialog)){
			progressDialog.dismiss();
			progressDialog = null;
		}

		super.finalize();
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		if (isCancelled())
			return;

		if (utility.isValidDialog(progressDialog)) {
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
			rPre.run();

	};

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		try {
			if (isCancelled()) {
				if (utility.isValidDialog(progressDialog)){
					progressDialog.dismiss();
					progressDialog = null;
				}
				return null;
			}

			if (rDo != null)
				rDo.run();
		} catch (Throwable e) {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		try {
			if (rPost != null)
				rPost.run();
		} finally {
			if (utility.isValidDialog(progressDialog)){
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	};

	@Override
	protected void onCancelled(Void result) {
		super.onCancelled(result);
		if (utility.isValidDialog(progressDialog)){
			progressDialog.dismiss();
			progressDialog = null;
		}
		//AndroidPlatform.ShowMessage(activity, "İşlem iptal edildi!", "Uyarı", DialogMode.Ok, 0, null);
	}

	void Cancel() {
		if (getStatus() != AsyncTask.Status.RUNNING)
			return;
		cancel(true);

	}

	public void setTitleMessage(String message, String title) {

		this.message = new SpannableString(message);
		this.title = new SpannableString(title);
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
			activity = AndroidPlatform.getActivity(activity);
			if(activity != null){
				activity.runOnUiThread(new Runnable() {
					public void run() {
						if (utility.isValidDialog(progressDialog)) {
							progressDialog.setTitle(LoadData.this.title);
							progressDialog.setMessage(LoadData.this.message);
						}
					}
				});
			}
		} else {
			if (utility.isValidDialog(progressDialog)) {
				progressDialog.setTitle(this.title);
				progressDialog.setMessage(this.message);
			}
		}
	}

	public void setMessage(String message) {
		setTitleMessage(message, "");
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
