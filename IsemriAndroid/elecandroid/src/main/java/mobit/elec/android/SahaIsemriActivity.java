package mobit.elec.android;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mobit.Globals;
import com.mobit.IForm;
import mobit.elec.ElecApplication;

public class SahaIsemriActivity extends AppCompatActivity implements IForm {
    WebView sahaisemri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Onur Saha işemri sistemine bağlantı oluşturuldu mobil cihazlardaki sim kartlar medaş ağında olduğu için 105 sunucusuna erişim problemi yaşamayacaklar.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saha_isemri);

        if(!(Globals.app instanceof ElecApplication)){
            finish();
            return;
        }

        SayfaTanimla();
        Yukle();
        //Onur auto fill eklendi fakat web sayfası yüklendikten sonra javascript enjekte edilebildiği için delay eklendi
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                yukle2();
            }
        }, 700);

    }

    public void SayfaTanimla(){

        sahaisemri = (WebView) findViewById(R.id.sahaisemri);
        WebView sahaisemri = (WebView) findViewById(R.id.sahaisemri);

    }

    public void Yukle(){

        sahaisemri.getSettings().setJavaScriptEnabled(true);
        sahaisemri.getSettings().setBuiltInZoomControls(true);
        sahaisemri.getSettings().setSupportZoom(true);
        sahaisemri.setWebViewClient(new WebViewClient());
        sahaisemri.loadUrl("http://meramedas.net/sahaisemri/Login.aspx");

    }

    public void yukle2(){

        String elemanKodu, elemanSifre;

        elemanKodu = String.valueOf(Globals.app.getEleman().getELEMAN_KODU());
        elemanSifre = String.valueOf(Globals.app.getEleman().getELEMAN_SIFRE());

        String username = "javascript:document.getElementById(\"Login1_UserName\").value =\""+elemanKodu+"\"";
        String password = "javascript:document.getElementById(\"Login1_Password\").value =\""+elemanSifre+"\"";
        String giris = "javascript:document.querySelector(\"input[value='Giriş']\").click()";

        sahaisemri.evaluateJavascript(username,null);
        sahaisemri.evaluateJavascript(password,null);
        sahaisemri.evaluateJavascript(giris,null);

    }


}