package mobit.elec.android;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDialogCallback;
import com.mobit.IEleman;
import com.mobit.IForm;
import com.mobit.ILogin;
import com.mobit.IPlatform;
import com.mobit.LoginParam;
import com.mobit.MobitException;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import mobit.android.LoadData;
import mobit.eemr.IMeterReader;
import mobit.elec.IElecApplication;

public class LoginActivity extends AppCompatActivity implements IForm, OnClickListener {

    protected IElecApplication app;
    protected ILogin login;
    private Toolbar toolbar;
    IApplication app2 = Globals.app;


    Button Giris;
    EditText kullaniciKodu;
    EditText kullaniciSifre;

    TextView textVersiyon;

    private LocalBroadcastManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!(Globals.app instanceof ILogin)) {
            finish();
            return;
        }
        app = (IElecApplication) Globals.app;
        login = (ILogin) Globals.app;

        setContentView(R.layout.activity_login);

        app.initForm(this);



        kullaniciKodu = (EditText) findViewById(R.id.kullaniciKodu);
        kullaniciSifre = (EditText) findViewById(R.id.kullaniciSifre);
        textVersiyon = (TextView) findViewById(R.id.textVersiyon);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = prefs.getString("username","");
        String pwd = prefs.getString("password","");
        kullaniciKodu.setText(username);
        kullaniciSifre.setText(pwd);
        textVersiyon.setText(app.getVersiyon());
        Resources res = getResources();
        Globals.textSize = res.getDimension(R.dimen.elecTextSize);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" ");
        }
        // toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.login);

        if (Globals.isDeveloping()) {

            kullaniciKodu.setText("8888");
            kullaniciSifre.setText("123456"); // 1111
        }

        kullaniciSifre.setOnEditorActionListener(editorListener);

        Giris = (Button) findViewById(R.id.Giris);
        Giris.setOnClickListener(this);

        kullaniciKodu.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(kullaniciKodu, InputMethodManager.SHOW_IMPLICIT);


        app.requestPermission(this);

        manager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onResume() {
        IMeterReader mr = app.getMeterReader();
        if (mr != null) mr.setTriggerEnabled(false);
        app.setPortCallback(null);

        super.onResume();

        app.getPlatform().showSoftKeyboard(this, true);

    }

    @Override
    public void onPause() {
        super.onPause();
        app.getPlatform().showSoftKeyboard(this, false);
    }

    @Override
    public void onDestroy() {
        if (app != null) {
            IPlatform platform = app.getPlatform();
            if (platform != null) platform.showSoftKeyboard(this, false);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (app != null) {
            app.permissionGrant(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.SunucuAyar) {

            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_SUNUCU_AYAR));
            startActivity(intent);

        } else if (id == R.id.YaziciAyar) {

			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_YAZICI_AYAR));
		startActivity(intent);

//            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_SEND_FILE_DEMO));
  //          startActivity(intent);

        } else if (id == R.id.OptikPortAyar) {

            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_OPTIK_PORT_AYAR));
            startActivity(intent);
        } else if (id == R.id.Guncelle) {

            app.checkUpdate();
        }


        return super.onOptionsItemSelected(item);
    }


    public class LoadData2 extends LoadData {

        public LoadData2(Activity activity, String message) {
            super(activity, message);
            // TODO Auto-generated constructor stub
        }

        public int kullanici_kodu;
        public int kullanici_sifre;

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            try {
                setMessage("Sunucuya bağlanıyor...");
                IEleman eleman = login.Login(new LoginParam(kullanici_kodu, kullanici_sifre, 0));
                setMessage("Parametreler alınıyor...");
                app.fetchParameter();

                // saat 5 - 9 arası girişlerde 'veritabanı sıfırla' işlemi yapılıyor H.Elif 16.05.2022
                Date startDate = new SimpleDateFormat("HH:mm:ss").parse("05:00:00");
                Date endDate = new SimpleDateFormat("HH:mm:ss").parse("09:00:00");

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date nowDate = new SimpleDateFormat("HH:mm:ss").parse(formatter.format(date));
                final long HOUR = 3600*1000;
                long nowTime = nowDate.getTime() + 3 * HOUR;

                if(startDate.getTime() <= nowTime && endDate.getTime() >= nowTime) {
                    setMessage("Veritabanı sıfırlanıyor...");
                    app2.clearTables(app2.getClearTableClass());
                }

                @SuppressWarnings("unused")
                int formId = (true) ? IDef.FORM_ISEMRI_MENU : IDef.FORM_OKUMA_MENU;
                Intent intent = new Intent(activity, app.getFormClass(formId));
                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", Integer.toString(kullanici_kodu));
                editor.putString("password", Integer.toString(kullanici_sifre));
                editor.commit();
                startActivity(intent);
                activity.finish();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                app.ShowException(LoginActivity.this, e);
            }

            return null;
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.Giris) {

            try {

                if (app.getSettingsChanged()) {
                    app.restart();
                    return;
                }

                //Intent intent = new Intent(this, IndexActivity.class);
                //startActivity(intent);
                if (setAPN()) login();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                setProgressBarIndeterminateVisibility(false);
                app.ShowException(this, e);
            }
        }

    }

    boolean setAPN() {
        boolean ok = true;
        final String apn = app.getApn();
        if (apn != null && apn.length() > 0) {
            try {
                app.getPlatform().checkAPN(apn);
            } catch (Exception e) {
                ok = false;
                app.ShowMessage(this, e.getMessage(), "Hata", DialogMode.Ok, 0, 0, new IDialogCallback() {
                    @Override
                    public void DialogResponse(int msg_id, DialogResult result) {
                        // TODO Auto-generated method stub
                        app.getPlatform().setAPN(LoginActivity.this, apn);
                    }
                });
            }
        }
        return ok;
    }

    void login() {

        try {


            if (kullaniciKodu.getText().toString().length() == 0)
                throw new MobitException("Kullanıcı kodu girin");
            int kullanici_kodu = Integer.parseInt(kullaniciKodu.getText().toString());
            if (kullaniciSifre.getText().toString().length() == 0)
                throw new MobitException("Kullanıcı şifresi girin");
            int kullanici_sifre = Integer.parseInt(kullaniciSifre.getText().toString());




            if (false || !Globals.isDeveloping()) {

                LoadData2 load = new LoadData2(this, "Bağlanıyor...");
                load.kullanici_kodu = kullanici_kodu;
                load.kullanici_sifre = kullanici_sifre;
                load.execute();

            } else {

                /*
                 * int formId = (true) ? MbsApplication.FORM_ISEMRI_MENU :
                 * MbsApplication.FORM_OKUMA_MENU; Intent intent = new
                 * Intent(this, app.getFormClass(formId));
                 * startActivity(intent); finish();
                 */

                Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_MENU));
                startActivityForResult(intent, IDef.FORM_ISEMRI_MENU);

            }
        } catch (Exception e) {
            app.ShowException(this, e);
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        finishAffinity();
        app.close();
        Globals.platform.exit();
    }

    OnEditorActionListener editorListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Giris.performClick();
                handled = true;
            }

            return handled;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) { // RESULT_OK

            }
        }
    }


}
