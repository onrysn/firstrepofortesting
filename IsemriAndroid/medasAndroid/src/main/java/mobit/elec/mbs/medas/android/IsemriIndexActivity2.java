package mobit.elec.mbs.medas.android;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mobit.BarcodeResult;
import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.IMap;
import com.mobit.IMapMarker;


import mobit.eemr.GucTespit;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;

import mobit.eemr.YkpEndeksDoldur;
import mobit.eemr.YkpOkuma;
import mobit.elec.Aciklama;
import mobit.elec.IAdurum;
import mobit.elec.IElecApplication;
import mobit.elec.ITakilanSayac;

import mobit.elec.MsgInfo;

import mobit.elec.Sayaclar;
import mobit.elec.android.FotografCekme;
import mobit.elec.android.IndexFragment2;
import mobit.elec.android.TesisatBilgiFragment;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.ISeriNo;
import mobit.elec.mbs.IMbsApplication;
import mobit.elec.mbs.OptikIsEmri;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.mbs.put.put_isemri_unvan;
import mobit.elec.mbs.server.IslemGrup2;
import mobit.elec.medas.ws.OsosServis;
import mobit.elec.medas.ws.SayacZimmetBilgi.muhurBilgi;
import mobit.elec.osos.viko.OperationService;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.MbtMeterInformation;
import mobit.eemr.MbtProbePowerStatus;
import mobit.eemr.ReadMode2;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;


public class IsemriIndexActivity2 extends AppCompatActivity implements IForm, OnKeyListener, ICallback, OnClickListener {
    private IMbsApplication appzabit;
    IMedasApplication app;
    boolean readOnly = false;
    private IMap map = null;

    TextView textSeri;
    Spinner muhurSeri;
    TextView textMuhurNo;
    EditText editMuhurNo;
    Button optikOku;
    Button ososOku;
    Button elleOku;
    Button aboneDurum;
    Button fotoCek;
    Button buttonTamam;
    Button haritaokuma;
    Button gps;

    Button barkod;
    Button ykp, ykpBirGunluk;
    protected mobit.elec.medas.ws.SayacZimmetBilgi medasServer;
    ProgressDialog progressDialog;
    IIsemri isemri;
    IIsemriIslem isemriIslem;

    ITakilanSayac sayacIslem;
    public static int isno;

    Lun_Control Tesisat = new Lun_Control();
    TesisatBilgiFragment bilgi;
    IndexFragment2 index;

    ArrayAdapter<muhurBilgi> spinnerAdapter;

    OperationService.Param ososParam = null;
    IElecApplication app2 = null;
    IBarcode barcode;
    List<IReadResult> n_res;
    List<IReadResult> n_res2;
    int os_cont = 0;
    int yuk_ptofil = 0;
    YkpOkuma ykpOkuma = null;
    private Sayaclar sayaclar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!(Globals.app instanceof IMedasApplication)) {
            finish();
            return;
        }

        app = (IMedasApplication) Globals.app;
        appzabit = (IMbsApplication) Globals.app;
        IIslem islem = app.getActiveIslem();

        if (app instanceof IMap)
            map = (IMap) app;

        app2 = (IElecApplication) Globals.app;

        app2.initForm(this);

        if (!(islem instanceof IIslemGrup)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }

        IIslemGrup islemGrup = (IIslemGrup) islem;
        isemriIslem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
        isemri = isemriIslem.getIsemri();

        if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }
        if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)) {
            sayacIslem = (ITakilanSayac) islemGrup.getIslem(ITakilanSayac.class).get(0);
        }

        setContentView(R.layout.activity_isemri_index2);

        Globals.app.initForm(this);
        //Optik datasi almak icin
        n_res2 = Arrays.asList(app.getOptikResult());

        Tesisat.setLuna_Control(0);

        bilgi = (TesisatBilgiFragment) getSupportFragmentManager().findFragmentById(R.id.tesisatBilgiFragment);
        bilgi.show(isemri);

        index = (IndexFragment2) getSupportFragmentManager().findFragmentById(R.id.indexFragment);

        textSeri = (TextView) findViewById(R.id.textSeri);
        muhurSeri = (Spinner) findViewById(R.id.muhurSeri);
        textMuhurNo = (TextView) findViewById(R.id.textMuhurNo);
        editMuhurNo = (EditText) findViewById(R.id.editMuhurNo);

        haritaokuma = (Button) findViewById(mobit.elec.android.R.id.haritaokuma);
        optikOku = (Button) findViewById(R.id.optikOku);
        ososOku = (Button) findViewById(R.id.ososOku);
        elleOku = (Button) findViewById(R.id.elleOku);
        aboneDurum = (Button) findViewById(R.id.aboneDurum);
        fotoCek = (Button) findViewById(R.id.fotoCek);
        buttonTamam = (Button) findViewById(R.id.buttonTamam);
        gps = (Button) findViewById(R.id.gps);

        barkod = (Button) findViewById(R.id.barkod);
        ykp = (Button) findViewById(R.id.ykp);
        ykpBirGunluk = (Button) findViewById(R.id.ykpBirGunluk);

        haritaokuma.setOnClickListener(this);
        optikOku.setOnClickListener(this);
        ososOku.setOnClickListener(this);
        elleOku.setOnClickListener(this);
        aboneDurum.setOnClickListener(this);
        fotoCek.setOnClickListener(this);
        buttonTamam.setOnClickListener(this);
        barkod.setOnClickListener(this);
        ykp.setOnClickListener(this);
        ykpBirGunluk.setOnClickListener(this);
        gps.setOnClickListener(this);

        boolean osos = isemri.isOSOS();
        //Deneme için
        if (Globals.isDeveloping()) osos = true;
        ososOku.setEnabled(osos);


        ArrayAdapter<muhurBilgi> spinnerAdapter =
                new ArrayAdapter<muhurBilgi>(this, android.R.layout.simple_spinner_item, app.getMuhurSeriler());
        muhurSeri.setAdapter(spinnerAdapter);

        if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)) {
            textSeri.setVisibility(View.INVISIBLE);
            muhurSeri.setVisibility(View.INVISIBLE);
            textMuhurNo.setVisibility(View.INVISIBLE);
            editMuhurNo.setVisibility(View.INVISIBLE);
        }

        barcode = app.newBarcodeObject();

        hide();
        show();
        ykpOkuma = new YkpOkuma();
        try {
            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            if (b != null) {
                String knt = (String) b.get("message");
                if (knt.equals("ykp1")) {
                    ykpOkuma.setTesisatNo(app.getActiveIsemri().getTESISAT_NO());
                    ykpOkuma.setIsemriNo(app.getActiveIsemri().getSAHA_ISEMRI_NO());
                    YkpOku();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
        editMuhurNo.requestFocus();
        app.showSoftKeyboard(this, true);

    }


    void clear() {

        this.runOnUiThread(new Runnable() {

            public void run() {
                editMuhurNo.getText().clear();
                index.clear();
            }
        });

    }

    void hide() {

        index.hide();

    }

    void show() {

        hide();
        bilgi.show(isemri);
        ISeriNo seriNo = isemriIslem.getSERINO();
        mobit.android.utility.selectSpinnerItemByValue(muhurSeri, seriNo.getSeri());
        //Test aşamasında boş gelmemesi için value set edildi.

        muhurSeri.setSelection(((ArrayAdapter) muhurSeri.getAdapter()).getPosition("N"));
        if (seriNo.getNo() != 0) editMuhurNo.setText(Integer.toString(seriNo.getNo()));
        ISayacBilgi sayac = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);
        IReadResult result = (sayac != null) ? sayac.getOptikResult() : null;
        index.show(isemriIslem, isemriIslem.getSAYACLAR(), result);

    }

    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(true);
        app.setPortCallback(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        app.showSoftKeyboard(this, false);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.isemri_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.durumKodGiris) {
            Lun_Control Ln = new Lun_Control();
            Ln.setDurumControl(1);
            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
            startActivity(intent);
			/*
			Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
			intent.putExtra("islemTipi", 1);
			startActivity(intent);
			 */
        } else if (id == R.id.isemriDetay) {
            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
            startActivity(intent);
        } else if (id == R.id.ykpDetay) {
            ykpOkuma = new YkpOkuma();
            String[] parse;

            if (ykpOkuma.YkpResult != null && ykpOkuma.IsemriNo == app.getActiveIsemri().getSAHA_ISEMRI_NO()) {
                AlertDialog.Builder YkpBildiri = new AlertDialog.Builder(IsemriIndexActivity2.this);
                YkpBildiri.setCancelable(false);
                parse = ykpOkuma.YkpResult.split("\\n");
                if (ykpOkuma.SayacMarka.equals("LUNA")) {
                    if (parse.length < 5) {
                        YkpBildiri.setTitle("Hata");
                        YkpBildiri.setMessage("Bu İşemri için YKP Datası mevcut değil!");
                        YkpBildiri.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Action for "Delete".
                            }
                        });
                    } else {
                        YkpBildiri.setTitle("Ykp Okuma Başarılı!");
                        YkpBildiri.setMessage("İşEmriNo:" + ykpOkuma.IsemriNo + "\nTesisatNo:" + ykpOkuma.TesisatNo + "\n" + YkpOkuma.YkpResult);
                        YkpBildiri.setPositiveButton("GÖNDER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Action for "Delete".
                                YkpEndeksDoldur ykpEndeksDoldur = new YkpEndeksDoldur();
                                ykpEndeksDoldur.setTesisat_no(isemri.getTESISAT_NO());
                                ykpEndeksDoldur.setIsemri_no(isemri.getSAHA_ISEMRI_NO());
                                ParseLunaYkp(YkpOkuma.YkpResult);
                                progressDialog = new ProgressDialog(IsemriIndexActivity2.this);
                                progressDialog.setMessage("Yükleniyor..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);

                                final Thread thread = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            String res = medasServer.YkpEndeksService();
                                            if (res.equals("OK") || res.equals("Success")) {
                                                progressDialog.dismiss();

                                                IslemiBitir(1, "Başarılı", "Yük Profili verisi başarıyla gönderildi.");


                                            } else {
                                                progressDialog.dismiss();
                                                IslemiBitir(1, "Hata", res.toString());
                                            }
                                        } catch (Exception e) {
                                            progressDialog.dismiss();
                                            IslemiBitir(1, "Hata", e.getMessage());
                                        }
                                    }
                                });

                                thread.start();
                                thread.interrupt();


                            }
                        });
                    }
                    final AlertDialog alertbildiri = YkpBildiri.create();
                    alertbildiri.show();
                }
                if (ykpOkuma.SayacMarka.equals("MAKEL")) {
                    if (parse.length < 5) {
                        YkpBildiri.setTitle("Hata");
                        YkpBildiri.setMessage("Bu İşemri için YKP Datası mevcut değil!");
                        YkpBildiri.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Action for "Delete".
                            }
                        });
                    } else {
                        YkpBildiri.setTitle("Ykp Okuma Başarılı!");
                        YkpBildiri.setMessage("İşEmriNo:" + ykpOkuma.IsemriNo + "\nTesisatNo:" + ykpOkuma.TesisatNo + "\n" + YkpOkuma.YkpResult);
                        YkpBildiri.setPositiveButton("GÖNDER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Action for "Delete".
                                YkpEndeksDoldur ykpEndeksDoldur = new YkpEndeksDoldur();
                                ykpEndeksDoldur.setTesisat_no(isemri.getTESISAT_NO());
                                ykpEndeksDoldur.setIsemri_no(isemri.getSAHA_ISEMRI_NO());
                                ParseMakelYkp(YkpOkuma.YkpResult);
                                progressDialog = new ProgressDialog(IsemriIndexActivity2.this);
                                progressDialog.setMessage("Yükleniyor..."); // Setting Message
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                progressDialog.show(); // Display Progress Dialog
                                progressDialog.setCancelable(false);

                                final Thread thread = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            String res = medasServer.YkpEndeksService();
                                            if (res.equals("OK") || res.equals("Success")) {
                                                progressDialog.dismiss();

                                                IslemiBitir(1, "Başarılı", "Yük Profili verisi başarıyla gönderildi.");


                                            } else {
                                                progressDialog.dismiss();
                                                IslemiBitir(1, "Hata", res.toString());
                                            }
                                        } catch (Exception e) {
                                            progressDialog.dismiss();
                                            IslemiBitir(1, "Hata", e.getMessage());
                                        }
                                    }
                                });

                                thread.start();
                                thread.interrupt();


                            }
                        });
                    }
                    final AlertDialog alertbildiri = YkpBildiri.create();
                    alertbildiri.show();
                }
            } else {

                AlertDialog.Builder YkpBildiri = new AlertDialog.Builder(IsemriIndexActivity2.this);
                YkpBildiri.setCancelable(false);
                YkpBildiri.setTitle("Hata");
                YkpBildiri.setMessage("Bu İşemri için YKP Datası mevcut değil!");
                YkpBildiri.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".


                    }
                });
                final AlertDialog alertbildiri = YkpBildiri.create();
                alertbildiri.show();

            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        if (ososParam != null) ososParam.canceled = true;
        app.showSoftKeyboard(this, false);
        super.onDestroy();
    }

    OnEditorActionListener editorListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                buttonTamam.performClick();
                handled = true;
            }
            return handled;
        }
    };

    @Override
    public void Opened() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Failed() {
        // TODO Auto-generated method stub
        app.ShowMessage(this, MsgInfo.OPTIK_PORT_BAGLANTI_KESILDI);
    }

    @Override
    public void BeginRead(IReadResult result) {
        // TODO Auto-generated method stub
        //Burdaki method açılınca köhrer sayaç çalışmıyor!!
        //Köhrere özel case koyuldu!
        //KHL AEL KÖHRER | EĞER KÖHRER İSE PROGRAM MOD OKUT
        //muhammed gökkaya
        index.hide();
        //ISayacBilgi sb = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);

        try {
            //ISayacBilgi sb = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);
            ISayacBilgi sb = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);

            String marka = sb.getMARKA().getSAYAC_MARKA_KODU();
            if (yuk_ptofil == 1) {
                //ykpOkuma=new YkpOkuma();
                result.setReadMode(ReadMode2.pPROFIL_MOD);
            } else {

                if (marka.equals("AEL") || marka.equals("KHL")) {
                    //result.setReadMode(ReadMode2.PROGRAM_MOD);
                    //result.setReadMode(ReadMode2.READOUT_MOD);
                } else if (marka.equals("LUN.E") || marka.equals("LUNA") || marka.equals("LUN")) {
                    result.setReadMode(ReadMode2.READOUT_MOD);

                } else {
                    result.setReadMode(ReadMode2.READOUT_MOD);
                }

                //program mod ise eğer
                if (result.getReadMode().equals(ReadMode2.PROGRAM_MOD)) {
                    // Sadece program modda anlamlı!!!
                    if (sb.getSAYAC_KODU().equals(SayacKodu.Kombi)) {
                        result.SetObisCodeMap(IReadResult.s_kombi_endeks);
                    } else {
                        result.SetObisCodeMap(IReadResult.s_aktif_endeks);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void EndRead() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Read(IProbeEvent event, final IReadResult result) {
        // TODO Auto-generated method stub

        result.set_okuma_zamani(app.getTime());
        Logger LOGGER = Logger.getLogger("AK");
        LOGGER.info("Logging an INFO-level message");
        MbtMeterInformation emi = result.get_Information();
        /*
         * // Sayaç ve hane sayısı kontrolü kapatıldı if (false &&
         * Globals.isDeveloping()) { optikEndeksDoldur(result); return; }
         */
        sayacKontrol(result);
    }

    void sayacKontrol(final IReadResult result) {
//HÜSEYİN EMRE ÇEVİK KONTROL(SAYAÇ SERİ)
        ISayacBilgi syc = null;
        int sayac_no = Integer.parseInt(result.get_sayac_no());

        for (ISayacBilgi s : isemriIslem.getSAYACLAR().getSayaclar()) {
            if (sayac_no == s.getSAYAC_NO()) {
                syc = s;
                break;
            }
        }
        n_res = Arrays.asList(result);
        if (syc != null) {
            app.setOptikResult(result);
            //eger optik ile okunmuşsa result degerini alıyoruz
            //muhammed gökkaya
            n_res = Arrays.asList(result);
            sayacHaneKontrol(syc, result);
            return;
        }

        app.ShowMessage(this, MsgInfo.FARKLI_SAYAC_OKUTTUNUZ, DialogMode.YesNo, 1, 0, new IDialogCallback() {

            @Override
            public void DialogResponse(int msg_id, DialogResult rs) {
                // TODO Auto-generated method stub
                if (msg_id == 1) {
                    if (rs.equals(DialogResult.Yes)) {
                        final ISayacBilgi _syc = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);
                        sayacHaneKontrol(_syc, result);
                    }
                }
            }

        });

    }

    public void ParseLunaYkp(String data) {
        String[] pdata = data.split("\n");
        String endeks_tarihi = "";
        String endeks_saati = "";
        String Aktif = "";
        String Enduktif = "";
        String Kapasitif = "";
        String AkimL1 = "";
        String AkimL2 = "";
        String AkimL3 = "";
        String GerilimL1 = "";
        String GerilimL2 = "";
        String GerilimL3 = "";
        for (int i = 1; i < pdata.length; i++) {
            String[] parca_endeks = pdata[i].split("\\)");
            for (int j = 0; j < parca_endeks.length; j++) {
                try {
                    //ri rc olmayan tesisatlarda
                    if (parca_endeks.length == 13) {
                        if (j == 0) {
                            String[] tarih = parca_endeks[j].split("\\(")[1].split("-");
                            if (i != pdata.length - 1) {
                                endeks_tarihi += (tarih[0] + tarih[1] + tarih[2] + "-");
                            } else {
                                endeks_tarihi += (tarih[0] + tarih[1] + tarih[2]);
                            }
                        }
                        if (j == 1) {
                            String[] saat = parca_endeks[j].split("\\(")[1].split(":");
                            if (i != pdata.length - 1) {
                                endeks_saati += (saat[0] + saat[1] + "-");
                            } else {
                                endeks_saati += (saat[0] + saat[1]);
                            }
                        }
                        if (j == 2) {
                            if (i != pdata.length - 1) {
                                Aktif += (parca_endeks[j].split("\\(")[1].split("\\*")[0] + "-");
                            } else {
                                Aktif += (parca_endeks[j].split("\\(")[1].split("\\*")[0]);
                            }
                        }
                        if (j == 3) {
                            if (i != pdata.length - 1) {
                                GerilimL1 += (parca_endeks[j].split("\\(")[1].split("V")[0] + "-");
                            } else {
                                GerilimL1 += (parca_endeks[j].split("\\(")[1].split("V")[0]);
                            }
                        }
                        if (j == 4) {
                            if (i != pdata.length - 1) {
                                AkimL1 += (parca_endeks[j].split("\\(")[1].split("A")[0] + "-");
                            } else {
                                AkimL1 += (parca_endeks[j].split("\\(")[1].split("A")[0]);
                            }
                        }
                        if (j == 6) {
                            if (i != pdata.length - 1) {
                                GerilimL2 += (parca_endeks[j].split("\\(")[1].split("V")[0] + "-");
                            } else {
                                GerilimL2 += (parca_endeks[j].split("\\(")[1].split("V")[0]);
                            }
                        }
                        if (j == 7) {
                            if (i != pdata.length - 1) {
                                AkimL2 += (parca_endeks[j].split("\\(")[1].split("A")[0] + "-");
                            } else {
                                AkimL2 += (parca_endeks[j].split("\\(")[1].split("A")[0]);
                            }
                        }
                        if (j == 9) {
                            if (i != pdata.length - 1) {
                                GerilimL3 += (parca_endeks[j].split("\\(")[1].split("V")[0] + "-");
                            } else {
                                GerilimL3 += (parca_endeks[j].split("\\(")[1].split("V")[0]);
                            }
                        }
                        if (j == 10) {
                            if (i != pdata.length - 1) {
                                AkimL3 += (parca_endeks[j].split("\\(")[1].split("A")[0] + "-");
                            } else {
                                AkimL3 += (parca_endeks[j].split("\\(")[1].split("A")[0]);
                            }
                        }
                    }
                    //kombi tesisatlarda
                    else {
                        if (j == 0) {
                            String[] tarih = parca_endeks[j].split("\\(")[1].split("-");
                            if (i != pdata.length - 1) {
                                endeks_tarihi += (tarih[0] + tarih[1] + tarih[2] + "-");
                            } else {
                                endeks_tarihi += (tarih[0] + tarih[1] + tarih[2]);
                            }
                        }
                        if (j == 1) {
                            String[] saat = parca_endeks[j].split("\\(")[1].split(":");
                            if (i != pdata.length - 1) {
                                endeks_saati += (saat[0] + saat[1] + "-");
                            } else {
                                endeks_saati += (saat[0] + saat[1]);
                            }
                        }
                        if (j == 2) {
                            if (i != pdata.length - 1) {
                                Aktif += (parca_endeks[j].split("\\(")[1].split("\\*")[0] + "-");
                            } else {
                                Aktif += (parca_endeks[j].split("\\(")[1].split("\\*")[0]);
                            }
                        }
                        if (j == 3) {
                            if (i != pdata.length - 1) {
                                Enduktif += (parca_endeks[j].split("\\(")[1].split("\\*")[0] + "-");
                            } else {
                                Enduktif += (parca_endeks[j].split("\\(")[1].split("\\*")[0]);
                            }
                        }
                        if (j == 4) {
                            if (i != pdata.length - 1) {
                                Kapasitif += (parca_endeks[j].split("\\(")[1].split("\\*")[0] + "-");
                            } else {
                                Kapasitif += (parca_endeks[j].split("\\(")[1].split("\\*")[0]);
                            }
                        }
                        if (j == 5) {
                            if (i != pdata.length - 1) {
                                GerilimL1 += (parca_endeks[j].split("\\(")[1].split("V")[0] + "-");
                            } else {
                                GerilimL1 += (parca_endeks[j].split("\\(")[1].split("V")[0]);
                            }
                        }
                        if (j == 6) {
                            if (i != pdata.length - 1) {
                                AkimL1 += (parca_endeks[j].split("\\(")[1].split("A")[0] + "-");
                            } else {
                                AkimL1 += (parca_endeks[j].split("\\(")[1].split("A")[0]);
                            }
                        }
                        if (j == 8) {
                            if (i != pdata.length - 1) {
                                GerilimL2 += (parca_endeks[j].split("\\(")[1].split("V")[0] + "-");
                            } else {
                                GerilimL2 += (parca_endeks[j].split("\\(")[1].split("V")[0]);
                            }
                        }
                        if (j == 9) {
                            if (i != pdata.length - 1) {
                                AkimL2 += (parca_endeks[j].split("\\(")[1].split("A")[0] + "-");
                            } else {
                                AkimL2 += (parca_endeks[j].split("\\(")[1].split("A")[0]);
                            }
                        }
                        if (j == 11) {
                            if (i != pdata.length - 1) {
                                GerilimL3 += (parca_endeks[j].split("\\(")[1].split("V")[0] + "-");
                            } else {
                                GerilimL3 += (parca_endeks[j].split("\\(")[1].split("V")[0]);
                            }
                        }
                        if (j == 12) {
                            if (i != pdata.length - 1) {
                                AkimL3 += (parca_endeks[j].split("\\(")[1].split("A")[0] + "-");
                            } else {
                                AkimL3 += (parca_endeks[j].split("\\(")[1].split("A")[0]);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }
        YkpEndeksDoldur ykpEndeksDoldur = new YkpEndeksDoldur();
        ykpEndeksDoldur.setEndeks_saati(endeks_saati);
        ykpEndeksDoldur.setEndeks_tarihi(endeks_tarihi);
        ykpEndeksDoldur.setAktif(Aktif);
        if (Enduktif.equals("")) {
            ykpEndeksDoldur.setEnduktif(null);
        } else {
            ykpEndeksDoldur.setEnduktif(Enduktif);
        }
        if (Kapasitif.equals("")) {
            ykpEndeksDoldur.setKapasitif(null);
        } else {
            ykpEndeksDoldur.setKapasitif(Kapasitif);
        }
        ykpEndeksDoldur.setAkimL1(AkimL1);
        ykpEndeksDoldur.setAkimL2(AkimL2);
        ykpEndeksDoldur.setAkimL3(AkimL3);
        ykpEndeksDoldur.setGerilimL1(GerilimL1);
        ykpEndeksDoldur.setGerilimL2(GerilimL2);
        ykpEndeksDoldur.setGerilimL3(GerilimL3);

    }

    public void ParseMakelYkp(String data) {
        String[] pdata = data.split("\n");
        String endeks_tarihi = "";
        String endeks_saati = "";
        String Aktif = "";
        String Enduktif = "";
        String Kapasitif = "";
        String AkimL1 = "";
        String AkimL2 = "";
        String AkimL3 = "";
        String GerilimL1 = "";
        String GerilimL2 = "";
        String GerilimL3 = "";
        for (int i = 1; i < pdata.length; i++) {
            String[] parca_endeks = pdata[i].split("\\)");
            for (int j = 0; j < parca_endeks.length; j++) {
                try {
                    //kombi olmayan sayaçlar
                    if (parca_endeks.length == 5) {
                        if (j == 0) {
                            String tarih = parca_endeks[j].split("\\(")[1].substring(0, 6);
                            String saat = parca_endeks[j].split("\\(")[1].substring(6, 10);
                            if (i != pdata.length - 2) {
                                endeks_tarihi += (tarih + "-");
                                endeks_saati += (saat + "-");
                            } else {
                                endeks_tarihi += (tarih);
                                endeks_saati += (saat);
                            }
                        }
                        //aktif
                        if (j == 1) {

                            if (i != pdata.length - 2) {
                                Aktif += (parca_endeks[j].split("\\(")[1]) + "-";
                            } else {
                                Aktif += (parca_endeks[j].split("\\(")[1]);
                            }
                        }
                        if (j == 2) {
                            if (i != pdata.length - 2) {
                                AkimL1 += parca_endeks[j].split("\\(")[1].split(",")[0] + "-";
                                AkimL2 += parca_endeks[j].split("\\(")[1].split(",")[1] + "-";
                                AkimL3 += parca_endeks[j].split("\\(")[1].split(",")[2] + "-";
                            } else {
                                AkimL1 += parca_endeks[j].split("\\(")[1].split(",")[0];
                                AkimL2 += parca_endeks[j].split("\\(")[1].split(",")[1];
                                AkimL3 += parca_endeks[j].split("\\(")[1].split(",")[2];
                            }
                        }
                        if (j == 3) {
                            if (i != pdata.length - 2) {
                                GerilimL1 += parca_endeks[j].split("\\(")[1].split(",")[0] + "-";
                                GerilimL2 += parca_endeks[j].split("\\(")[1].split(",")[1] + "-";
                                GerilimL3 += parca_endeks[j].split("\\(")[1].split(",")[2] + "-";
                            } else {
                                GerilimL1 += parca_endeks[j].split("\\(")[1].split(",")[0];
                                GerilimL2 += parca_endeks[j].split("\\(")[1].split(",")[1];
                                GerilimL3 += parca_endeks[j].split("\\(")[1].split(",")[2];
                            }
                        }


                    }
                    //ri rc sayaclar
                    else {
                        if (j == 0) {
                            String tarih = parca_endeks[j].split("\\(")[1].substring(0, 6);
                            String saat = parca_endeks[j].split("\\(")[1].substring(6, 10);
                            if (i != pdata.length - 1) {
                                endeks_tarihi += (tarih + "-");
                                endeks_saati += (saat + "-");
                            } else {
                                endeks_tarihi += (tarih);
                                endeks_saati += (saat);
                            }
                        }
                        //aktif
                        if (j == 1) {

                            if (i != pdata.length - 1) {
                                Aktif += (parca_endeks[j].split("\\(")[1]) + "-";
                            } else {
                                Aktif += (parca_endeks[j].split("\\(")[1]);
                            }
                        }
                        //endüktif
                        if (j == 2) {
                            if (i != pdata.length - 1) {
                                Enduktif += (parca_endeks[j].split("\\(")[1]) + "-";
                            } else {
                                Enduktif += (parca_endeks[j].split("\\(")[1]);
                            }
                        }
                        //kapasitif
                        if (j == 3) {
                            if (i != pdata.length - 1) {
                                Kapasitif += (parca_endeks[j].split("\\(")[1]) + "-";
                            } else {
                                Kapasitif += (parca_endeks[j].split("\\(")[1]);
                            }
                        }
                        //akımlar
                        if (j == 4) {
                            if (i != pdata.length - 1) {
                                AkimL1 += parca_endeks[j].split("\\(")[1].split(",")[0] + "-";
                                AkimL2 += parca_endeks[j].split("\\(")[1].split(",")[1] + "-";
                                AkimL3 += parca_endeks[j].split("\\(")[1].split(",")[2] + "-";
                            } else {
                                AkimL1 += parca_endeks[j].split("\\(")[1].split(",")[0];
                                AkimL2 += parca_endeks[j].split("\\(")[1].split(",")[1];
                                AkimL3 += parca_endeks[j].split("\\(")[1].split(",")[2];
                            }
                        }
                        //gerilimler
                        if (j == 5) {
                            if (i != pdata.length - 1) {
                                GerilimL1 += parca_endeks[j].split("\\(")[1].split(",")[0] + "-";
                                GerilimL2 += parca_endeks[j].split("\\(")[1].split(",")[1] + "-";
                                GerilimL3 += parca_endeks[j].split("\\(")[1].split(",")[2] + "-";
                            } else {
                                GerilimL1 += parca_endeks[j].split("\\(")[1].split(",")[0];
                                GerilimL2 += parca_endeks[j].split("\\(")[1].split(",")[1];
                                GerilimL3 += parca_endeks[j].split("\\(")[1].split(",")[2];
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
        YkpEndeksDoldur ykpEndeksDoldur = new YkpEndeksDoldur();
        ykpEndeksDoldur.setEndeks_saati(endeks_saati);
        ykpEndeksDoldur.setEndeks_tarihi(endeks_tarihi);
        ykpEndeksDoldur.setAktif(Aktif);
        if (Enduktif.equals("")) {
            ykpEndeksDoldur.setEnduktif(null);
        } else {
            ykpEndeksDoldur.setEnduktif(Enduktif);
        }
        if (Kapasitif.equals("")) {
            ykpEndeksDoldur.setKapasitif(null);
        } else {
            ykpEndeksDoldur.setKapasitif(Kapasitif);
        }
        ykpEndeksDoldur.setAkimL1(AkimL1);
        ykpEndeksDoldur.setAkimL2(AkimL2);
        ykpEndeksDoldur.setAkimL3(AkimL3);
        ykpEndeksDoldur.setGerilimL1(GerilimL1);
        ykpEndeksDoldur.setGerilimL2(GerilimL2);
        ykpEndeksDoldur.setGerilimL3(GerilimL3);

    }

    public void IslemiBitir(final int hata, String title, String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(IsemriIndexActivity2.this);
        dialog.setCancelable(false);
        if (hata == 1) {
            dialog.setTitle(title);
            dialog.setMessage(message);
        } else {
            dialog.setMessage(message);
        }

        dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                if (hata == 0) {
                    IsemriIndexActivity2.this.finish();
                }
            }
        });
        IsemriIndexActivity2.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog dlg = dialog.create();
                dlg.show();
            }
        });

    }

    void sayacHaneKontrol(final ISayacBilgi syc, final IReadResult result) {

        final ISayaclar sayaclar = isemriIslem.getSAYACLAR();

        if (sayaclar.getSayac(SayacKodu.Aktif).getHANE_SAYISI().getValue() == result.getHaneSayisi()) {
            index.show(isemriIslem, sayaclar, result);
            return;
        }


        app.ShowMessage(this, MsgInfo.SAYAC_HANE_SAYISI_FARKLI, DialogMode.YesNo, 2, 0, new IDialogCallback() {

            @Override
            public void DialogResponse(int msg_id, DialogResult rs) {
                // TODO Auto-generated method stub
                if (rs.equals(DialogResult.Yes)) {

                    index.show(isemriIslem, sayaclar, result);

                }
            }
        });

    }

    @Override
    public void PowerEvent(IProbeEvent event) {
        // TODO Auto-generated method stub
        MbtProbePowerStatus status = null;
        try {
            status = event.getMeterReader().GetPowerStatus();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (status == null)
            return;
        //app.ShowMessage(this, String.format("Optik port enerji seviyesi:%%%d", status.batteryLifePercent), "Uyarı");
    }

    @Override
    public void ConnectionResetEvent(IProbeEvent event) {
        // TODO Auto-generated method stub
        app.ShowMessage(this, MsgInfo.OPTIK_PORT_BAGLANTI_KESILDI);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        int id = arg0.getId();
        try {
            if (id == R.id.buttonTamam) {

                Lun_Control isNo = new Lun_Control();
                isno = isemri.getSAHA_ISEMRI_NO();
                isNo.setIsEmriNo(isno);
                isNo.setTesisatNo(isemri.getTESISAT_NO());
                //foto yolu
                String path = Globals.platform.getExternalStorageDirectory();
                File f = new File(path);
                File file[] = f.listFiles();
                int counter = 0;
                //foto kontrolu
                for (int i = 0; i < file.length; i++) {
                    if (file[i].getName().equals("medas")) {

                    } else {
                        if (isno == Integer.parseInt(file[i].getName().split("_")[0])) {
                            counter++;
                        }
                    }
                }

                //kacak yada kesmeyse fotosuz giremesin
						/*
		6	12	Saha Kontrol	TESPİT
		6	18	GM-Ölçü Kontrol	TESPİT
		6	19	GM-Kaçak Kontrol	TESPİT
		6	20	GM-Saha Kontrol	TESPİT


		* */
                if (counter > 0 || ((isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 5) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 10) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 11) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 6006) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 19) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 12) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 13) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 14) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 18) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 20) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 34) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 25) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 26) &&
                        (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() != 24) &&
                        (!isemri.getISLEM_TIPI().toString().equals("Açma")) &&
                        (!isemri.getISLEM_TIPI().toString().equals("Kesme")))) {
                    IsemriIndexActivity2.OrtakTrafo ortakTrafo = new IsemriIndexActivity2.OrtakTrafo();
                    ortakTrafo.execute();
                } else if (!isemri.getISLEM_TIPI().toString().equals("Tespit") && !isemri.getISLEM_TIPI().toString().equals("Kesme") && !isemri.getISLEM_TIPI().toString().equals("Açma") && !isemri.getISLEM_TIPI().toString().equals("İhbar")) {
                    IsemriIndexActivity2.OrtakTrafo ortakTrafo = new IsemriIndexActivity2.OrtakTrafo();
                    ortakTrafo.execute();
                }else if (isemri.getISLEM_TIPI().toString().equals("İhbar")){
                    if (n_res!=null){
                        isemriIslem.setOPTIK_DATA(app.getOptikData(n_res));
                    }
                    if (isemriIslem.getOPTIK_DATA()!=null){
                        IsemriIndexActivity2.OrtakTrafo ortakTrafo = new IsemriIndexActivity2.OrtakTrafo();
                        ortakTrafo.execute();
                    }else {
                        AlertDialog.Builder dialog4 = new AlertDialog.Builder(IsemriIndexActivity2.this);
                        dialog4.setCancelable(false);
                        dialog4.setTitle("Dikkat!");
                        dialog4.setMessage("Bu İşemri için " + counter + " adet Fotoğraf Çekilmiş.İş emrini kapatabilmek için Fotoğraf çekmelisiniz! veya Optik port aracılığı ile okuma yapınız!");
                        dialog4.setPositiveButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setNegativeButton("	Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                            }
                        });
                        final AlertDialog alert4 = dialog4.create();
                        alert4.show();
                    }
                }else {
                    AlertDialog.Builder dialog3 = new AlertDialog.Builder(IsemriIndexActivity2.this);
                    dialog3.setCancelable(false);
                    dialog3.setTitle("Dikkat!");
                    dialog3.setMessage("Bu İşemri için " + counter + " adet Fotoğraf Çekilmiş.İş emrini kapatabilmek için Fotoğraf çekmelisiniz!");
                    dialog3.setPositiveButton("", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).setNegativeButton("	Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Action for "Cancel".
                        }
                    });
                    final AlertDialog alert3 = dialog3.create();
                    alert3.show();
                }

                // H.Elif olcu devre girilmişse veya sayac değiştirme ise
                OlcuDevreForm odf = new OlcuDevreForm();
                if (odf.gettesisat_no() != -1) {

                    SimpleDateFormat utc = (SimpleDateFormat) Globals.dateTimeFmt.clone();
                    utc.setTimeZone(Globals.utcTimeZone);
                    String datetime = utc.format(Globals.getTime().getTime());
                    String[] date = datetime.split(" ");


                    index.OdfEndeks(); // endex bilgileri alınıyor

                    odf.setcarpan(isemri.getCARPAN());
                    String formType = "olcu_devre_akim_yok_form";
                    if (odf.getcarpan() > 1) {
                        formType = "olcu_devre_akim_var_form";
                    }

                    odf.set_date(date[0]);
                    odf.set_time(date[1]);
                    odf.set_formType(formType);

                    if (odf.get_yeni_muhur().equals("")) // sayacTakmaActivity2 -> sayac değişiminden gelenin üzerine basmasın diye
                        odf.set_yeni_muhur(muhurSeri.getSelectedItem().toString() + " / " + editMuhurNo.getText().toString());

                    odf.setteyit_dur(1);


                    if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)) {
                        odf.set_ihbar_dur(0);
                        final Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    String res = medasServer.OlcuKontrolService();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        thread.interrupt();
                    }


                }


            } else if (id == R.id.optikOku) {
                os_cont = 0;
                Lun_Control ln = new Lun_Control();
                ln.Luna_Control = 1;
                ykpOkuma = new YkpOkuma();
                ykpOkuma.setYkpResult(null);
                ykpOkuma.setYkpCount(1);
                ykpOkuma.setYkpOkumaDurum(0);
                ykpOkuma.setYkpBuffer(null);
                ykpOkuma.setTarihAralık(null);
                ykpOkuma.setSayacMarka(null);
                ykpOkuma.setPeriyot(null);
                yuk_ptofil = 0;
                app.runAsync(this, "Optik porta bağlanıyor...", "", null, new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        try {
                            app.getMeterReader().Reconnect();
                            app.getMeterReader().Trigger();

                        } catch (Exception e) {
                            return e;
                        }
                        return null;
                    }

                }, new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        if (app.checkException(IsemriIndexActivity2.this, obj))
                            return null;
                        return null;
                    }

                });


            } else if (id == R.id.ososOku) {
                os_cont = 1;
                OsosOkuma("", "", false);

            } else if (id == R.id.elleOku) {
                os_cont = 0;
                //Lun_Control l =new Lun_Control();
                //String asd=l.ObisCode;
                isemriIslem.getSAYACLAR().setOptikResult(null);
                index.show(isemriIslem, isemriIslem.getSAYACLAR(), null);
            } else if (id == R.id.aboneDurum) {
                Lun_Control Ln = new Lun_Control();
                Ln.setDurumControl(1);
                Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ABONE_DURUM_GIRIS));
                startActivity(intent);
            } else if (id == mobit.elec.android.R.id.haritaokuma) {
                List<IIsemri> list = new ArrayList<IIsemri>();
                list.add(isemri);
                Harita(list);
            } else if (id == R.id.gps) {
                Intent intent = new Intent(this, app.getFormClass(17));//17 GpsActivity
                startActivity(intent);
            } else if (id == R.id.fotoCek) {
                //İlla fotoları görsün derse bunu aktf et
                //Intent fac=new Intent(this,FotoActivity.class);
                //startActivity(fac);
                //muhammed gökkaya

                Lun_Control isNo = new Lun_Control();
                isno = isemri.getSAHA_ISEMRI_NO();
                isNo.setIsEmriNo(isno);
                isNo.setTesisatNo(isemri.getTESISAT_NO());
                //foto yolu
                String path = Globals.platform.getExternalStorageDirectory();
                File f = new File(path);
                File file[] = f.listFiles();
                int counter = 0;
                //foto kontrolu
                for (int i = 0; i < file.length; i++) {
                    if (file[i].getName().equals("medas")) {

                    } else {
                        if (isno == Integer.parseInt(file[i].getName().split("_")[0])) {
                            counter++;
                        }
                    }
                }
                //eğer öncesinde çekilmiş ise
                if (counter > 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(IsemriIndexActivity2.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Fotoğraf Çekme");
                    dialog.setMessage("Bu İşemri için " + counter + " adet Fotoğraf Çekilmiş. Yeniden çekmek ister misin?");
                    dialog.setPositiveButton("Sil/Yeniden Çek", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Action for "Delete".
                            String path = Globals.platform.getExternalStorageDirectory();

                            File f = new File(path);
                            File file[] = f.listFiles();
                            int foto_control = 0;
                            for (int i = 0; i < file.length; i++) {
                                if (file[i].getName().equals("medas")) {

                                } else {
                                    if (isno == Integer.parseInt(file[i].getName().split("_")[0])) {
                                        file[i].delete();
                                        foto_control = 1;
                                    }

                                }
                            }
                            if (foto_control == 1) {
                                FotografCek();
                            }
                        }
                    }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Action for "Cancel".
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    FotografCek();
                }

            } else if (id == R.id.barkod) {
                //Class<?> clazz = app.getFormClass(mobit.elec.android.IDef.FORM_ZABIT);
                //if (clazz != null) {

                //Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISLEM_LISTE));
                //startActivity(intent);
                //}
                barcode.startScanner(this);
            } else if (id == R.id.ykp) {
                //os_cont=0;
                //Lun_Control ln=new Lun_Control();
                //ln.Luna_Control=1;
                ISayacBilgi sb = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);

                String marka = sb.getMARKA().getSAYAC_MARKA_KODU();
                Intent intent = new Intent(this, YkpActivity.class);
                intent.putExtra("sayac", marka);
                intent.putExtra("tekgun_mu", 0);
                intent.putExtra("activity", "IsemriIndexActivity2");

                startActivity(intent);
                /*



                 */
            } else if (id == R.id.ykpBirGunluk) {

                ISayacBilgi sb = isemriIslem.getSAYACLAR().getSayac(SayacKodu.Aktif);

                String marka = sb.getMARKA().getSAYAC_MARKA_KODU();
                Intent intent = new Intent(this, YkpActivity.class);
                intent.putExtra("sayac", marka);
                intent.putExtra("tekgun_mu", 1);
                startActivity(intent);
                /*



                 */
            }

        } catch (Exception e) {

            app.ShowException(this, e);
        }
    }

    void FotografCek() {
        app.getMeterReader().setTriggerEnabled(false);
        app.setPortCallback(null);
        FotografCekme fc = new FotografCekme(this, null);
        fc.run();
    }

    public void YkpOku() {
        yuk_ptofil = 1;
        app.runAsync(this, "Optik porta bağlanıyor...", "", null, new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                try {
                    app.getMeterReader().Reconnect();
                    app.getMeterReader().Trigger();

                } catch (Exception e) {
                    return e;
                }
                return null;
            }

        }, new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (app.checkException(IsemriIndexActivity2.this, obj))
                    return null;
                return null;
            }

        });
    }

    class OrtakTrafo extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(IsemriIndexActivity2.this);
        private String result;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Ortak Trafo durumu Kontrol Ediliyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

        }

        public OrtakTrafo() {
            super();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {

                result = medasServer.OrtakTrafoKontrol(isemriIslem.getTESISAT_NO(), isemriIslem.getSAHA_ISEMRI_NO());

                if (result == null) {
                    result = "İşleminiz kaydedilemedi tekrar deneyiniz.";
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
            if (result.equals("1")) {
                LoadOrtakActivity(1);
            } else {
                //LoadOrtakActivity();
                Tamam();
            }
        }
    }

    void LoadOrtakActivity(int cont) {
        final Intent intent = new Intent(this, OrtakTrafoActivity.class);
        if (cont == 0) {
            startActivity(intent);
        } else {
            android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(IsemriIndexActivity2.this);
            adb.setTitle("Ortak Trafo Durumu");
            adb.setMessage("İşlem yaptığınız tesisatın birden fazla ortak trafolu tesisatı var mı?");
            adb.setNegativeButton("Hayır", new android.app.AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AddOrtakTrafo trafo = new AddOrtakTrafo(0);
                    trafo.execute();
                }
            });
            adb.setPositiveButton("Evet", new android.app.AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                }
            });
            adb.show();
        }
    }

    public class AddOrtakTrafo extends AsyncTask<Void, Void, Void> {

        public ProgressDialog progressDialog = new ProgressDialog(IsemriIndexActivity2.this);
        private String result;
        int teyit_dur;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Gönderiliyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        public AddOrtakTrafo(int _teyit_dur) {
            super();
            teyit_dur = _teyit_dur;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                result = medasServer.AddOrtakTrafo(app.getActiveIsemri().getTESISAT_NO(), app.getActiveIsemri().getSAHA_ISEMRI_NO(), String.valueOf(app.getActiveIsemri().getTESISAT_NO()), teyit_dur);
                if (result == null) {
                    result = "İşleminiz kaydedilemedi tekrar deneyiniz.";
                }
                if (result.equals("1")) {
                    result = "İşlem Tamamlandı";
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
            if (result.indexOf("Girdiğiniz") > -1) {
                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(IsemriIndexActivity2.this);
                adb.setTitle("Servis Sonucu");
                adb.setMessage(result);
                adb.setPositiveButton("Evet", new android.app.AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Tamam();
                        //pass
                    }
                });
                adb.setNegativeButton("Düzenle", new android.app.AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoadOrtakActivity(0);
                        //pass
                    }
                });
                adb.show();
            } else {
                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(IsemriIndexActivity2.this);
                adb.setTitle("Servis Sonucu");
                adb.setMessage(result);
                adb.setPositiveButton("Tamam", new android.app.AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Tamam();
                        //pass
                    }
                });
                adb.show();
            }
        }
    }

    void Tamam() {
        //exception' düşmesin diye "n" seçili varsayıyorum burası açılacak
        if (isemri.getISLEM_TIPI().toString().equals("Tespit") && isemri.getALT_EMIR_TURU() == 24) {
            try {
                List<IAdurum> durumListe;
                int KOD_TIPI = isemriIslem.getIsemri().getISLEM_TIPI().equals(IslemTipi.SayacOkuma) ? 1 : 0;
                durumListe = app.getAboneDurum(0, KOD_TIPI);
                for (int i = 0; i < durumListe.size(); i++) {
                    if (durumListe.get(i).getABONE_DURUM_KODU() == 39) {
                        isemriIslem.setGELEN_DURUM(durumListe.get(i), durumListe.get(i));
                    }
                }

            } catch (Exception e) {

            }
        }
        String s = muhurSeri.getSelectedItem().toString();
        try {

            index.save();
            if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)) {
                String SeriNo = sayacIslem.getSERI_NO().toString();
                String[] separated = SeriNo.split("/");
                String sayacSeri = separated[0];
                String SayacNo = separated[1];
                isemriIslem.setSERINO(new SeriNo(sayacSeri, SayacNo));

            } else {

                isemriIslem.setSERINO(new SeriNo(s, editMuhurNo.getText().toString()));
            }


            isemriIslem.IslemKontrol(app);
            isemriIslem.setIsemriTamamlanacak(true);
            isemriIslem.setELEMAN_KODU(app.getEleman().getELEMAN_KODU());

            //isemri no;
            OptikIsEmri optikIsEmri = new OptikIsEmri();
            isno = isemri.getSAHA_ISEMRI_NO();

            optikIsEmri.setIsemriNo(isno);


            List<IReadResult> results = isemriIslem.getIsemri().getSAYACLAR().getOptikResult();
            //Muhammed Gokkaya
            //optik nesnesini temsil ediyor

            //acilacak alan:

            if (n_res != null && os_cont == 0) {

                isemriIslem.setOPTIK_DATA(app.getOptikData(n_res));


            }

            //IsemriListe Optik okumasının objesi
            else if (n_res2 != null) {
                //ilkel çözüm
                //n_res2 her durum da dolu geldiği için bu ilkel yol izlendi.
                //Muhammed Gökkaya
                try {
                    isemriIslem.setOPTIK_DATA(app.getOptikData(n_res2));
                } catch (Exception e) {
                    isemriIslem.setOPTIK_DATA(app.getOptikData(results));
                }

            }
            //elle okunmuş ise
            else {
                isemriIslem.setOPTIK_DATA(app.getOptikData(results));
            }
            //muhammed gökkaya
            isemriIslem.setZAMAN(app.getTime());
            //Hafızada ki gpsi kullanmak için ara classa yazma işlemi
			/*
			SharedPreferences prefs = getSharedPreferences("GPS", MODE_PRIVATE);
			Lun_Control Gps=new Lun_Control();
			String Latitude=prefs.getString("Latitude","0.0").replace(",",".");
			String Longitude=prefs.getString("Longitude","0.0").replace(",",".");
			String Accuracy=prefs.getString("Accuracy","0.0").replace(",",".");
			Gps.setLatitude(Double.parseDouble(Latitude));
			Gps.setLongitude(Double.parseDouble(Longitude));
			Gps.setAccuracy(Double.parseDouble(Accuracy));
			Gps.setTime(prefs.getString("Time","Yok"));
			 */

            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            app.ShowException(this, e);
        }
    }


    void OsosBilgiDogrulama(OsosServis.OkumaTestYapResult result) {
        String msg = String.format("%s\n\nYukarıdaki bilgiler doğru mu?", result.toString());
        app.ShowMessage(this, msg, "OSOS Bilgi Doğrulama", DialogMode.YesNo, 1, 0, new IDialogCallback() {
            public void DialogResponse(int msg_id, DialogResult result) {
                if (result == DialogResult.No) {
                    CepTelefonGiris();
                }
            }
        });
    }

    void CepTelefonGiris() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cep Tel No/ SIM ID Girişi");
        View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_ceptelno_simid_giris,
                null, false);
        final TextView comment = (TextView) viewInflated.findViewById(R.id.comment);
        comment.setText("Cep telefon numarası yada SIM ID'sini girin:");
        comment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        final EditText ceptelno = (EditText) viewInflated.findViewById(R.id.ceptelno);
        //ceptelno.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        final EditText simid = (EditText) viewInflated.findViewById(R.id.simid);
        //simid.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

        builder.setView(viewInflated);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                OsosOkuma(ceptelno.getText().toString(), simid.getText().toString(), true);
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void OsosOkuma(String ceptelno, String ceptelid, final boolean tekrar) {

        //CepTelefonGiris(null);
        //if(true) return;

        index.clear();

        // Önceden bir okuma başlatılmışsa iptal edilmesi
        if (ososParam != null) ososParam.canceled = true;
				/*
				String tesisat_no = Integer.toString(isemri.getTESISAT_NO());
				//Deneme için
				if(Globals.isDeveloping()) tesisat_no = "1000215";//"1000024"; "11202796"
				IOperationService ososServer = (IOperationService)app.getServer(IDef.OSOS_SERVER);


				ososParam = new OperationService.Param(ososServer.getExternalCommandId());
				ososParam.clb = new Callback(){

					@Override
					public Object run(Object obj) {
						// TODO Auto-generated method stub
						ososOku.setEnabled(true);
						if (app.checkException(IsemriIndexActivity2.this, obj))
							return null;
						IReadResult result = (IReadResult)obj;
						if(result != null) sayacKontrol(result);
						return null;
					}

				};
				ososServer.OsosAktivasyon(app, this, tesisat_no, ososParam);
				*/
        List<ISayacBilgi> sayaclar = isemri.getSAYACLAR().getSayaclar();
        if (sayaclar.isEmpty()) {
            app.ShowMessage(this, "Bu tesisata ait tanımlı sayaç yok", "");
            return;
        }
        ISayacBilgi sayac = sayaclar.get(0);
        String sayac_no = Integer.toString(sayac.getSAYAC_NO());
        String flag = sayac.getMARKA().getSAYAC_MARKA_KODU();
        OsosServis osos = new OsosServis();

        // Test için kullanılıyor
        if (Globals.isDeveloping()) {
            sayac_no = "80115487";
            flag = "MSY";
        }

        osos.OsosAktivasyon(app, this, sayac_no, flag, ceptelno, ceptelid, new Callback() {
            @Override
            public Object run(Object obj) {
                ososOku.setEnabled(true);
                if (app.checkException(IsemriIndexActivity2.this, obj))
                    return null;

                if (obj instanceof OsosServis.OkumaTestYapResult) {
                    OsosServis.OkumaTestYapResult result = (OsosServis.OkumaTestYapResult) obj;
                    if (tekrar == false)
                        OsosBilgiDogrulama(result);
                    else
                        app.ShowMessage(IsemriIndexActivity2.this, result.sonuc, "OSOS Hata");
                } else if (obj instanceof IReadResult) {
                    IReadResult result = (IReadResult) obj;
                    sayacKontrol(result);
                }

                return null;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        BarcodeResult result = barcode.getScanResult(requestCode, resultCode, data);
        if (result.resultStatus == BarcodeResult.SUCCESS) {
            View view = getCurrentFocus();
            //Muhammed Gokkaya
            //barkod datasını parse edip text ve spinnerda set etme
            if (view instanceof EditText) {
                EditText edit = (EditText) view;
                //data:N00000
                char[] Muhur_data = result.resultData.toCharArray();
                String muhur_no = "";
                String muhur_seri = "";
                muhur_seri += Muhur_data[0];
                //parse ettik ilk hane seri, dizinin devamı ise numara
                for (int i = 1; i < Muhur_data.length; i++) {
                    muhur_no += Muhur_data[i];
                }
                //Muhur no set ettik
                edit.setText(muhur_no);
                //Seriyi backendden gelen seriyle eşletirip set ettik
                for (int pos = 0; pos < muhurSeri.getCount(); pos++) {
                    if (muhurSeri.getItemAtPosition(pos).toString().equalsIgnoreCase(muhur_seri)) {
                        muhurSeri.setSelection(pos);
                    }
                }


            }
        }
    }

    void UnvanBas() {

        try {

            //send(islemGrup,true);
            //muhammed gökkaya zabit
            try {
                Aciklama unvan = new Aciklama("Fatih Taşkıran");
                Aciklama tc = new Aciklama("12345678911");
                Aciklama vergi = new Aciklama("3560587035");


                double tc2 = Double.parseDouble("35605870352");
                double vergi2 = Double.parseDouble("3560587035");
                put_isemri_unvan zz2 = new put_isemri_unvan(isemri.getTESISAT_NO(), isemri.getSAHA_ISEMRI_NO(),
                        tc,
                        vergi,
                        unvan);
                IIslemGrup islemGrup = new IslemGrup2();
                islemGrup.add(zz2);
                islemGrup.setIslemTipi(mobit.elec.android.IDef.ZABIT_EKLEME_ISLEMI);
                //Burada patlıyor NullPointer (int) HÜSEYİN Emre Çevik
                IIslem islem = app2.saveIslem(islemGrup);

                app2.sendIslem(this, islem, new Callback() {
                    @Override
                    public Object run(Object obj) {


                        app2.ShowMessage(IsemriIndexActivity2.this, "Unvan  Eklendi", "Başarılı");
                        //setResult(RESULT_OK);
                        //finish();
                        return null;
                    }
                }, 10000);


            } catch (Exception e) {
                app2.ShowException(IsemriIndexActivity2.this, e);
            }


        } catch (Exception e) {
            app.ShowException(this, e);
            return;
        }

        setResult(RESULT_OK);
        //finish();

    }

    private void Harita(List<IIsemri> arrayList) {
        if (map == null)
            return;

        List<IMapMarker> list = new ArrayList<IMapMarker>();
        synchronized (arrayList) {
            for (IIsemri isemri : arrayList) {
                if (isemri instanceof IMapMarker) {
                    IMapMarker marker = (IMapMarker) isemri;
                    ILocation location = marker.getLocation();
                    if (location != null && location.isValid())
                        list.add(marker);
                }
            }
        }
        if (list.isEmpty()) {
            app.ShowMessage(this, "Konum bilgisi yok!", "");
            return;
        }

        app.getMeterReader().setTriggerEnabled(false);
        app.setPortCallback(null);

        map.setMarkerList(list);

        Class<?> cls = app.getFormClass(mobit.elec.android.IDef.FORM_MAP);
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, mobit.elec.android.IDef.FORM_MAP);

    }

}
