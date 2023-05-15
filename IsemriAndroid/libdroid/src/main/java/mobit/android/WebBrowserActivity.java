package mobit.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IForm;
import android.content.Intent;
import android.net.Uri;

// https://developer.chrome.com/multidevice/webview/overview

public class WebBrowserActivity extends AppCompatActivity implements IForm, OnClickListener {

	IApplication app;
	WebView webView;
	protected Uri uri = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.activity_web_browser);
		
		if(!(Globals.app instanceof IApplication)){
			finish();
			return;
		}
		app = (IApplication)Globals.app;
		
		app.initForm(this);
		//webView = (WebView)findViewById(R.id.webView);
		
		
		
		//((android.widget.Button)this.findViewById(R.id.button1)).setOnClickListener(this);
		 //WebSettings settings = webView.getSettings();
		 //settings.setJavaScriptEnabled(true);
		 //settings.setBuiltInZoomControls(true);

		 /*
		 final Activity activity = this;
		 webView.setWebChromeClient(new WebChromeClient() {
		   public void onProgressChanged(WebView view, int progress) {
		     // Activities and WebViews measure progress with different scales.
		     // The progress meter will automatically disappear when we reach 100%
		     activity.setProgress(progress * 1000);
		   }
		 });
		 webView.setWebViewClient(new WebViewClient() {
		   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		   }
		 });*/
		
		//webView.loadUrl("https://www.google.com.tr/maps/@41.0235172,28.8887951,15z");
		//webView.loadUrl("http://www.mobit.com.tr/");
		
		
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivityForResult(intent, 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		webView = new WebView(this);
		setContentView(webView);
		
	}
	
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 1) {
			finish();
		}
	}
	
	
}
