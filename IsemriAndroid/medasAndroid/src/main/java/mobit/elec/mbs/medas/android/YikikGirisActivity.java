package mobit.elec.mbs.medas.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.mobit.Cbs;
import com.mobit.IForm;
import com.mobit.ILocation;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import mobit.elec.Globals;
import mobit.elec.android.IsemriListeActivity;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;
import mobit.http.Result;

public class YikikGirisActivity extends AppCompatActivity implements IForm {
    IMedasApplication app;
    SayacZimmetBilgi medasServer;
    EditText editAdresTarif;
    EditText editT1;
    EditText editT2;
    EditText editT3;
    TextView editRi;
    EditText editCi;
    EditText editTesisatNo;
    EditText editReferansTesisatNo;
    EditText editDirekNo;
    EditText editBoxNo;
    EditText editSayacNo;
    EditText editAciklama;

    Button buttonKaydet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yikik_giris);

        if (!(Globals.app instanceof IMedasApplication)) {
            finish();
            return;
        }
        app = (IMedasApplication) Globals.app;
        app.initForm(this);
        editAdresTarif = (EditText) findViewById(R.id.editAdresTarif);
        editT1 = (EditText) findViewById(R.id.editT1);
        editT2 = (EditText) findViewById(R.id.editT2);
        editT3 = (EditText) findViewById(R.id.editT3);
        editRi = (TextView) findViewById(R.id.editRi);
        editCi = (EditText) findViewById(R.id.editCi);
        editTesisatNo = (EditText) findViewById(R.id.editTesisatNo);
        editReferansTesisatNo = (EditText) findViewById(R.id.editReferansTesisatNo);
        editDirekNo = (EditText) findViewById(R.id.editDirekNo);
        editBoxNo = (EditText) findViewById(R.id.editBoxNo);
        editSayacNo = (EditText) findViewById(R.id.editSayacNo);
        editAciklama = (EditText) findViewById(R.id.editAciklama);

        medasServer =(mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
        buttonKaydet = (Button) findViewById(R.id.buttonKaydet);

        buttonKaydet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Kontrol()){
                    YikikGirisActivity.YikikBildirim asn_main = new YikikGirisActivity.YikikBildirim();
                    asn_main.execute();
                }
            }
        });
    }


    class YikikBildirim extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(YikikGirisActivity.this);
        private  String result;
        String Cbs_x,Cbs_y;
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Gönderiliyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

        }

        public YikikBildirim() {
            super();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            ILocation location;
            Cbs_x="";
            Cbs_y="";
            try {
                location = app.getLocation();
                Cbs_x = String.valueOf( location.getLatitude());
                Cbs_y = String.valueOf( location.getLongitude());
            }
            catch (Exception ex){

            }
            try {

                String Kayit_Tarihi=new SimpleDateFormat("yyyyMMdd").format(new Date());

                result = medasServer.YikikIhbarServisi(Integer.parseInt(editTesisatNo.getText().toString())
                        ,editSayacNo.getText().toString()
                        ,editDirekNo.getText().toString()
                        ,editBoxNo.getText().toString()
                        ,Integer.parseInt(editReferansTesisatNo.getText().toString())
                        ,com.mobit.Globals.app.getEleman().getELEMAN_KODU()
                        ,Cbs_x
                        ,Cbs_y
                        ,Kayit_Tarihi
                        ,editAciklama.getText().toString()
                        ,editAdresTarif.getText().toString(),editT1.getText().toString()
                        ,editT2.getText().toString()
                        ,editT3.getText().toString()
                        ,editRi.getText().toString()
                        ,editCi.getText().toString());
                if (result == null){
                    result ="İşleminiz kaydedilemedi tekrar deneyiniz.";
                }




            } catch (Exception e) {
                // TODO Auto-generated catch block
                result = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (result.equals("Success")){
                AlertDialog alertDialog = new AlertDialog.Builder(YikikGirisActivity.this)
                        .setTitle("İşlem Tamamlandı")
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                            }
                        })
                        .show();
                editAdresTarif.setText("");
                editT1.setText("");
                editT2.setText("");
                editT3.setText("");
                editRi.setText("");
                editCi.setText("");
                editTesisatNo.setText("");
                editReferansTesisatNo.setText("");
                editDirekNo.setText("");
                editBoxNo.setText("");
                editSayacNo.setText("");
                editAciklama.setText("");

            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(YikikGirisActivity.this)
                        .setTitle("Hata")
                        .setMessage(result)
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                            }
                        })
                        .show();
            }
        }
    }
    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(false);
        app.setPortCallback(null);
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean Kontrol()  {

        if (editAdresTarif.getText().toString().isEmpty()) {
            editAdresTarif.setError("Boş olamaz!");
            editAdresTarif.requestFocus();
            return false;
        }
        if (editT1.getText().toString().isEmpty()) {
            editT1.setText("");
        }
        if (editT2.getText().toString().isEmpty()) {
            editT2.setText("");
        }
        if (editT3.getText().toString().isEmpty()) {
            editT3.setText("");
        }
        if (editRi.getText().toString().isEmpty()) {
            editRi.setText("");
        }
        if (editCi.getText().toString().isEmpty()) {
            editCi.setText("");
        }
        if (editTesisatNo.getText().toString().isEmpty()) {
            editTesisatNo.setError("Boş olamaz!");
            editTesisatNo.requestFocus();
            return false;
        }
        if (editReferansTesisatNo.getText().toString().isEmpty()) {
            editReferansTesisatNo.setError("Boş olamaz!");
            editReferansTesisatNo.requestFocus();
            return false;
        }
        if (editDirekNo.getText().toString().isEmpty()) {
            editDirekNo.setError("Boş olamaz!");
            editDirekNo.requestFocus();
            return false;
        }
        if (editBoxNo.getText().toString().isEmpty()) {
            editBoxNo.setText("");
        }
        if (editSayacNo.getText().toString().isEmpty()) {
            editSayacNo.setText("");
        }
        if (editAciklama.getText().toString().isEmpty()) {
            editAciklama.setError("Açıklama Boş olamaz!");
            editAciklama.requestFocus();
            return false;
        }
        return true;
    }

}