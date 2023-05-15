package mobit.elec.mbs.medas.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;

import java.util.ArrayList;
import java.util.Arrays;

import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ITakilanSayac;
import mobit.elec.MsgInfo;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;

public class OrtakTrafoActivity extends AppCompatActivity implements IForm {
    IMedasApplication app;
    SayacZimmetBilgi medasServer;
    IIsemri isemri;
    IIsemriIslem isemriIslem;
    ArrayList<String> tesisatlar = new ArrayList<>();
    String TesisatListesi="";
    ArrayList<String> servissonuc = new ArrayList<>();
    ListView listemiz;
    EditText edittesisat;
    Button tesisatekle;
    Button gonder;
    ITakilanSayac sayacIslem;
    ArrayAdapter<String> veriAdaptoru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Globals.app instanceof IMedasApplication)) {
            finish();
            return;
        }

        app = (IMedasApplication) Globals.app;
        IIslem islem = app.getActiveIslem();
        if (!(islem instanceof IIslemGrup)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }


        IIslemGrup islemGrup = (IIslemGrup)islem;

        isemriIslem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);

        isemri = isemriIslem.getIsemri();

        if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }
        if(isemri.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)){
            sayacIslem =  (ITakilanSayac) islemGrup.getIslem(ITakilanSayac.class).get(0);
        }
        setContentView(R.layout.activity_ortak_trafo);
        Globals.app.initForm(this);
        listemiz=(ListView) findViewById(R.id.listTesisatlar);
        tesisatekle=(Button) findViewById(R.id.TesisatEkle);
        edittesisat = (EditText) findViewById(R.id.editKarneTesisatNo);
        gonder = (Button) findViewById(R.id.BtnKaydet);
        medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);

        veriAdaptoru=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, tesisatlar);

        listemiz.setAdapter(veriAdaptoru);
        tesisatekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mukerrer =0;
                for (int i = 0; i<tesisatlar.size();i++){
                    if (tesisatlar.get(i).equals(edittesisat.getText().toString())){
                        mukerrer+=1;
                    }
                }
                if (!edittesisat.getText().toString().equals("") && mukerrer == 0 && !edittesisat.getText().toString().equals(String.valueOf(app.getActiveIsemri().getTESISAT_NO()))){
                    tesisatlar.add(edittesisat.getText().toString());
                    veriAdaptoru.notifyDataSetChanged();
                    edittesisat.setText("");
                    //listemiz.setAdapter(veriAdaptoru);
                }
                else {
                    AlertDialog.Builder adb=new AlertDialog.Builder(OrtakTrafoActivity.this);
                    adb.setTitle("Ekleme Uyarısı");
                    adb.setMessage("Mevcut yada listeye eklenmiş tesisatno tekrar eklenemez!");
                    adb.setNegativeButton("Tamam", null);
                    adb.show();
                }
            }
        });
        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(OrtakTrafoActivity.this);
                adb.setTitle("Silme İşlemi");
                adb.setMessage(tesisatlar.get(position) + " nolu Tesisatı silmek istermisiniz?");
                final int positionToRemove = position;
                adb.setNegativeButton("Hayır", null);
                adb.setPositiveButton("Evet", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tesisatlar.remove(positionToRemove);
                        veriAdaptoru.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });
        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tesisatlar.size() >0){
                    try {
                        TesisatListesi ="";
                        for (int i=0;i<tesisatlar.size();i++){
                            if (i==tesisatlar.size()-1) {
                                TesisatListesi += tesisatlar.get(i);
                            }
                            else {
                                TesisatListesi += tesisatlar.get(i) + ",";
                            }
                        }
                        OrtakTrafoActivity.OrtakTrafo ortakTrafo = new OrtakTrafoActivity.OrtakTrafo(0);
                        ortakTrafo.execute();
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }

                }
            }
        });

    }
    public class OrtakTrafo extends AsyncTask<Void, Void, Void> {

        public ProgressDialog progressDialog = new ProgressDialog(OrtakTrafoActivity.this);
        private  String result;
        int teyit_dur;
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Gönderiliyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        public OrtakTrafo(int _teyit_dur) {
            super();
            gonder.setEnabled(false);
            teyit_dur = _teyit_dur;
        }



        @Override
        protected Void doInBackground(Void... voids) {

            try {
                    if (teyit_dur == 0) {
                        TesisatListesi += "," + app.getActiveIsemri().getTESISAT_NO();
                    }
                    result = medasServer.AddOrtakTrafo(app.getActiveIsemri().getTESISAT_NO(), app.getActiveIsemri().getSAHA_ISEMRI_NO(),TesisatListesi,teyit_dur);
                    if (result == null) {
                        result = "İşleminiz kaydedilemedi tekrar deneyiniz.";
                    }
                    if (result.equals("1")){
                        result ="İşlem Tamamlandı";
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
            gonder.setEnabled(true);
            if (result.indexOf("Girdiğiniz")>-1){
                android.app.AlertDialog.Builder adb=new android.app.AlertDialog.Builder(OrtakTrafoActivity.this);
                adb.setTitle("Servis Sonucu");
                adb.setMessage(result);
                adb.setPositiveButton("Evet", new android.app.AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        OrtakTrafoActivity.OrtakTrafo ortakTrafo = new OrtakTrafoActivity.OrtakTrafo(1);
                        ortakTrafo.execute();
                        //pass
                    }});
                adb.setNegativeButton("Düzenle", new android.app.AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //pass
                    }});
                adb.show();
            }
            else {
                AlertDialog.Builder adb = new AlertDialog.Builder(OrtakTrafoActivity.this);
                adb.setTitle("Servis Sonucu");
                adb.setMessage(result);
                adb.setPositiveButton("Tamam", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //pass
                    }
                });
                adb.show();
            }
        }
    }
}