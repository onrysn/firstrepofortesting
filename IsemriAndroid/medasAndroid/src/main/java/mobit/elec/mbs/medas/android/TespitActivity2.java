package mobit.elec.mbs.medas.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDb;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;
import com.mobit.Operation;
import com.mobit.utility;

import mobit.android.ArrayAdapterEx;
import mobit.android.PopupList;
import mobit.android.ViewHolderEx;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.eemr.OsosSoruForm;
import mobit.eemr.SeletIsemriCevap;
import mobit.elec.Aciklama;
import mobit.elec.IAciklama;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.ISayacBilgi;
import mobit.elec.android.AboneDurumGirisActivity;
import mobit.elec.android.IsemriListeActivity;
import mobit.elec.android.SendingListActivity;
import mobit.elec.enums.ICevapTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.IIslemMaster;
import mobit.elec.mbs.IMbsApplication;
import mobit.elec.mbs.enums.CevapTipi;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.medas.IDef;
import mobit.elec.mbs.medas.IslemSendTask2;
import mobit.elec.mbs.medas.SoruSenaryo;
import mobit.elec.mbs.put.put_isemri_unvan;
import mobit.elec.mbs.server.IslemGrup2;
import mobit.elec.medas.ws.SayacZimmetBilgi;

import static mobit.elec.mbs.get.field.TC_KIMLIK_NO;
import static mobit.elec.mbs.get.field.UNVAN;
import static mobit.elec.mbs.get.field.VERGI_NO;
import static mobit.elec.mbs.get.field.s_TC_KIMLIK_NO;
import static mobit.elec.mbs.get.field.s_UNVAN;

public class TespitActivity2 extends AppCompatActivity implements IForm, OnClickListener {
    private IMbsApplication app;
    private IIslemGrup islemGrup;
    private IIsemriIslem islem;
    private IIsemri2 isemri;
    private IIsemri isemri2;
    private IAciklama isemri3;
    SayacZimmetBilgi medasServer;
    private ListView listSorular;
    private boolean UnvansizKacakServisResult = false;
    private AlertDialog dialogUnvan;

    private Button buttonTamam;
    private String value;
    private int alertMode = -1;

    private IAciklama TC_KIMLIK_NO;
    private IAciklama VERGI_NO;
    private IAciklama UNVAN;

    //muhammed gökkaya
    public int soru_sayaci = 0;
    public int soru_count = 2;
    public int soru_sayisi = 0;
    public boolean check_senaryo;
    public JsonArray SoruSen;
    public int[] tespit24filter = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    static final InputFilter[] fInteger = new InputFilter[]{new InputFilter.LengthFilter(10)};
    static final InputFilter[] fFloat = new InputFilter[]{new InputFilter.LengthFilter(20)};
    static final InputFilter[] fString = new InputFilter[]{new InputFilter.LengthFilter(field.s_CEVAP)};

    List<IIsemriSoru> arraySorular = new ArrayList<IIsemriSoru>();
    List<IIsemriSoru> arraySorularcontrol = new ArrayList<IIsemriSoru>();
    ArrayAdapterEx<IIsemriSoru> adapterSorular;

    PopupList<String> popuplist = null;
    String[] listCoktanSecmeli = null;

    private RadioGroup.LayoutParams lp;
    public String emir_turu;
    public String tespit24soru_yirmi7 = "";

    OlcuDevreForm odf = new OlcuDevreForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tespit2);

        if (!(Globals.app instanceof IMbsApplication)) {
            finish();
            return;
        }
        app = (IMbsApplication) Globals.app;
        if (!(app.getActiveIslem() instanceof IIslemGrup)) {
            finish();
            return;
        }

        medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
        islemGrup = (IIslemGrup) app.getActiveIslem();
        islem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);

        if (!islem.getISLEM_TIPI().equals(IslemTipi.Tespit) && !islem.getISLEM_TIPI().equals(IslemTipi.SayacDegistir)) {
            finish();
            return;
        }

        if (!(app.getActiveIsemri() instanceof IIsemri2)) {
            finish();
            return;
        }
        isemri = (IIsemri2) app.getActiveIsemri();
        SeletIsemriCevap isemriCevap = new SeletIsemriCevap();

        OsosSoruForm osf = new OsosSoruForm();
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit")
                && (String.valueOf(isemri.getALT_EMIR_TURU()).equals("10")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("11")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("36")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("37")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("38")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("39")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("40"))) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            osf.AboneNo = String.valueOf(isemri.getTESISAT_NO());
            osf.Order_Number = isemri.getSAHA_ISEMRI_NO();
            osf.Ihbar_Tarihi = dateFormat.format(isemri.getISEMRI_TARIHI());

            Intent i = new Intent(getApplicationContext(), OsosSoruActivity.class);
            i.putExtra("AboneNo", osf.AboneNo);
            i.putExtra("Order_Number", osf.Order_Number);
            i.putExtra("Ihbar_Tarihi", osf.Ihbar_Tarihi);
            i.putExtra("alt_emir_turu", String.valueOf(isemri.getALT_EMIR_TURU()));
            startActivityForResult(i, 42);
        }

        //Tespit sorusuna otomatik evet dicek
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit")
                && (String.valueOf(isemri.getALT_EMIR_TURU()).equals("23")
                || String.valueOf(isemri.getALT_EMIR_TURU()).equals("34"))) {
        }


        SoruSenaryo soruSenaryo = new SoruSenaryo();
        emir_turu = "";
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit")) {
            emir_turu = "6";
        }
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("KESME")) {
            emir_turu = "0";
        }
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("AÇMA")) {
            emir_turu = "1";
        }
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("SAYAÇ DEĞİŞİKLİK")) {
            emir_turu = "4";
        }
        if (String.valueOf(isemri.getISLEM_TIPI()).equals("SAYAÇ TAKMA")) {
            emir_turu = "5";
        }
        check_senaryo = soruSenaryo.CheckSoruEmirTuru(emir_turu, String.valueOf(isemri.getALT_EMIR_TURU()));
        if (check_senaryo) {
            SoruSen = soruSenaryo.GetSoruSenaryosuDetay(emir_turu, String.valueOf(isemri.getALT_EMIR_TURU()));
        }

        //muhammed gökkaya
        arraySorularcontrol.clear();
        for (IIslem islem : islemGrup.getIslem(IIsemriSoru.class)) {
            arraySorularcontrol.add((IIsemriSoru) islem);
        }
        soru_sayisi = arraySorularcontrol.size();
        arraySorular.clear();
        for (IIslem islem : islemGrup.getIslem(IIsemriSoru.class)) {
            arraySorular.add((IIsemriSoru) islem);
            if (check_senaryo) {
                soru_sayaci++;
                if (soru_sayaci == 2) {
                    break;
                }
            }
        }

        app.initForm(this);

        listSorular = (ListView) findViewById(R.id.listSorular);
        buttonTamam = (Button) findViewById(R.id.buttonTamam);

        buttonTamam.setOnClickListener(this);

        adapterSorular = new ArrayAdapterEx<IIsemriSoru>(this, R.layout.row_tespit, arraySorular, ViewHolder.class);
        listSorular.setAdapter(adapterSorular);

        Resources r = getResources();
        DisplayMetrics dm = r.getDisplayMetrics();
        lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, dm);
        lp.setMargins(0, px, 0, 0);
    }

    @Override
    protected void onDestroy() {
        if (popuplist != null)
            popuplist.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(false);
        app.setPortCallback(null);
        super.onResume();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        int id = arg0.getId();
        if (id == R.id.buttonTamam) {
            Tamamla();
        }

    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {

        if (arg0 == IDef.FORM_ZABIT) {
            if (arg1 == RESULT_OK) {
                Tamamlandi();
            }
        } else if (arg0 == 42) {
            if (arg1 == RESULT_CANCELED) {
                finish();
            }
        } else if (arg0 == IDef.FORM_OLCU_DEVRE) { // H.Elif ölçü devre tespit sayfası tamamlanınca endex sayfasının açılması için
            if (arg1 == RESULT_OK) {
                app.ShowMessage(this, "Tespit tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {
                    @Override
                    public void DialogResponse(int msg_id, DialogResult result) {
                        // TODO Auto-generated method stub
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        }
    }

    private void zabit(int mode) {
        if (mode == IDef.FORM_OLCU_DEVRE) // H.Elif ölçü devre tespit formu açılacak // Onur (8x 6097 eklendi)
        {
            Class<?> clazz = app.getFormClass(IDef.FORM_OLCU_DEVRE);
            if (clazz != null) {

                Intent intent = new Intent(this, clazz);
                intent.putExtra("MODE", mode);
                startActivityForResult(intent, IDef.FORM_OLCU_DEVRE);
            }
        } else if (String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit") &&
                (isemri.getALT_EMIR_TURU() == 5
                        || isemri.getALT_EMIR_TURU() == 6
                        || isemri.getALT_EMIR_TURU() == 13
                        || isemri.getALT_EMIR_TURU() == 6001
                        || isemri.getALT_EMIR_TURU() == 6002
                        || isemri.getALT_EMIR_TURU() == 6006
                        || isemri.getALT_EMIR_TURU() == 6097
                        || isemri.getALT_EMIR_TURU() == 22
                        || isemri.getALT_EMIR_TURU() == 25
                        || isemri.getALT_EMIR_TURU() == 26
                        || isemri.getALT_EMIR_TURU() == 19
                        || isemri.getALT_EMIR_TURU() == 16)) {
            /*
			Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
			if (clazz != null) {

				Intent intent = new Intent(this, clazz);
				intent.putExtra("MODE", mode);
				startActivityForResult(intent, IDef.FORM_ZABIT);
			}
             */

            Lun_Control zz = new Lun_Control();
            zz.setZabitDur(1);
            zz.setZabitMod(mode);
            zz.setZabitTesisat(isemri.getTESISAT_NO());
            zz.setZabitIsemriNo(isemri.getSAHA_ISEMRI_NO());

			/*
			Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
			if (clazz != null) {

				Intent intent = new Intent(this, clazz);
				intent.putExtra("MODE", mode);
				startActivityForResult(intent, IDef.FORM_ZABIT);
			}
			 */
            //HÜSEYİN EMRE ÇEVİK Unvanı boş gelen iş emirlerine Unvan Basma
            if (mode == 0) UnvanDegistir();
            else Tamamlandi();


        }else if (String.valueOf(isemri.getISLEM_TIPI()).equals("SayacTakma") && (isemri.getALT_EMIR_TURU() == 10)){
             /*
			Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
			if (clazz != null) {

				Intent intent = new Intent(this, clazz);
				intent.putExtra("MODE", mode);
				startActivityForResult(intent, IDef.FORM_ZABIT);
			}
             */

            Lun_Control zz = new Lun_Control();
            zz.setZabitDur(1);
            zz.setZabitMod(mode);
            zz.setZabitTesisat(isemri.getTESISAT_NO());
            zz.setZabitIsemriNo(isemri.getSAHA_ISEMRI_NO());

			/*
			Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
			if (clazz != null) {

				Intent intent = new Intent(this, clazz);
				intent.putExtra("MODE", mode);
				startActivityForResult(intent, IDef.FORM_ZABIT);
			}
			 */
            if (mode == 0) UnvanDegistir();
            else Tamamlandi();
        }else {
            Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
            if (clazz != null) {

                Intent intent = new Intent(this, clazz);
                intent.putExtra("MODE", mode);
                startActivityForResult(intent, IDef.FORM_ZABIT);
            }
        }
    }


    private boolean zabitTespit() {
        int mode = -1;
        int tespit26cnt = 0;
        for (IIsemriSoru soru : arraySorular) {

            ICevapTipi cevapTipi = soru.getCEVAP_TIPI();
            if (!(cevapTipi.equals(CevapTipi.CoktanSecmeli) ||
                    cevapTipi.equals(CevapTipi.Strings))) continue;

            String s = "";
            if (cevapTipi.equals(CevapTipi.CoktanSecmeli)) {
                int i = Integer.parseInt(soru.getCEVAP());
                if (i == -99) {
                    s = "Yok";
                } else {
                    s = soru.getCEVAP_FORMAT_LIST()[i];
                }
            } else if (cevapTipi.equals(CevapTipi.Strings)) {
                s = soru.getCEVAP();
            }

            //Ölçü devre tespit sayfasını aç (Evet - hiçbirşey) H.Elif
            //Ölçü devre tespit sayfasını aç (GM tespit - tüm durumlar) H.Elif
            //Alt emir türü = 13 tüm sorular evet ise ölçü devre tespit sayfası açma Onur
            if ((
                    emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 6 && arraySorular.get(1).getCEVAP().equals("1") && arraySorular.get(2).getCEVAP().equals("0"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 16 && arraySorular.get(1).getCEVAP().equals("1") && arraySorular.get(2).getCEVAP().equals("0"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 6006 && arraySorular.get(1).getCEVAP().equals("1") && arraySorular.get(2).getCEVAP().equals("0"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 6001 && arraySorular.get(1).getCEVAP().equals("1") && arraySorular.get(2).getCEVAP().equals("0"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 5 && arraySorular.get(1).getCEVAP().equals("1") && arraySorular.get(2).getCEVAP().equals("0"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 25 && arraySorular.get(5).getCEVAP().equals("0"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 26)
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 6002)
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 6097 && (arraySorular.get(1).getCEVAP().equals("1") || arraySorular.get(1).getCEVAP().equals("2")))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 22)
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 13 && !(arraySorular.get(1).getCEVAP().equals("1") && arraySorular.get(2).getCEVAP().equals("1") && arraySorular.get(3).getCEVAP().equals("1") && arraySorular.get(4).getCEVAP().equals("1") && arraySorular.get(5).getCEVAP().equals("1")
                    && arraySorular.get(6).getCEVAP().equals("1") && arraySorular.get(8).getCEVAP().equals("1") && arraySorular.get(9).getCEVAP().equals("1") && arraySorular.get(10).getCEVAP().equals("1") && arraySorular.get(11).getCEVAP().equals("1") && arraySorular.get(12).getCEVAP().equals("1") && arraySorular.get(13).getCEVAP().equals("1")))
                    || (emir_turu.equals("5") && isemri.getALT_EMIR_TURU() == 10)
            ) {

                IsemriNoServicecontrol(); // işemri no varsa ölçüyü açma yoksa aç için servis kontrolü yapılıyor
                String res = getValue();
                if (res == null) {
                    setAlertMode(IDef.FORM_ENDEKS);
                } else {
                    setAlertMode(IDef.FORM_OLCU_DEVRE);
                }
                mode = getAlertMode();
            }

            if ((emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 25 && soru.getSORU_NO() == 5 && soru.getCEVAP().equals("1"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 19 && soru.getSORU_NO() == 1 && soru.getCEVAP().equals("1"))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 6006 && arraySorular.get(1).getCEVAP().equals("1") && (arraySorular.get(2).getCEVAP().equals("1") || arraySorular.get(2).getCEVAP().equals("2")))
                    || (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 5 && arraySorular.get(1).getCEVAP().equals("1") && (arraySorular.get(2).getCEVAP().equals("1") || arraySorular.get(2).getCEVAP().equals("2")))
            ) {
                if (utility.search(s, null, IDef.muhur) != 0 && utility.search(s, null, IDef.kirma) != 0) {
                    mode = IDef.MBS_ZABIT;
                } else {
                    mode = IDef.MEDAS_ZABIT;
                }
            }

            if (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 26 && ((soru.getSORU_NO() == 1 && soru.getCEVAP().equals("2")) || (soru.getSORU_NO() == 3 && soru.getCEVAP().equals("0")))) {
                tespit26cnt += 1;
            } else {
                if (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() != 25) {
                    if (utility.search(s, null, IDef.kacak) == 0) continue;
                    if (utility.search(s, null, IDef.muhur) != 0 && utility.search(s, null, IDef.kirma) != 0) {
                        mode = IDef.MBS_ZABIT;
                    } else {
                        mode = IDef.MEDAS_ZABIT;
                    }
                    break;
                }
            }
        }

        // Deneme için eklendi
        if (false && Globals.isDeveloping()) {
            mode = 0;
        }

        if (mode > -1) {
            zabit(mode);
            return true;
        }
        return false;
    }

    private void IsemriNoServicecontrol() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    odf.settesisat_no(isemri.getTESISAT_NO());
                    odf.setisemri_no(isemri.getSAHA_ISEMRI_NO());
                    odf.set_alt_emir_turu(isemri.getALT_EMIR_TURU());
                    value = medasServer.OlcuIsemriKontrolService();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();

        try {
            thread.join();//it will kill you thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getValue() {
        return value;
    }

    public void setAlertMode(int mode) {
        alertMode = mode;
    }

    public int getAlertMode() {
        return alertMode;
    }

    private boolean Kontrol() {
        if (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 24 && arraySorular.size() < 29) {
            return false;
        }
        if (arraySorular.size() < 1)
            return false;
        for (IIsemriSoru s : arraySorular) {
            if (s.getSORU_NO() != 0 && s.getCEVAP().isEmpty()) {
                return false;
            }

            if (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 24 && s.getSORU_NO() == 12 && s.getCEVAP().equals("Yok")) {
                return false;
            }

        }

        return true;
    }

    private void Tamamla() {

        try {

            if (!Kontrol()) throw new MobitException("Cevaplanmamış soru var!");
            if (!zabitTespit()) Tamamlandi();

        } catch (Exception e) {
            app.ShowException(this, e);
        }
    }

    private void Tamamlandi() {
        if (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 24) {
            boolean control = true;
            for (IIsemriSoru s : arraySorular) {
                for (int i = 0; i < tespit24filter.length; i++) {
                    if (s.getSORU_NO() == tespit24filter[i] && s.getCEVAP().toString().equals("1")) {
                        control = false;
                    }
                }
            }
            if (control) {
                app.ShowMessage(this, "Onaylanacaktır!\nEmin misiniz?", "TESİSAT KABUL SONUCU", DialogMode.YesNo, 1, 0, new IDialogCallback() {
                    @Override
                    public void DialogResponse(int msg_id, DialogResult result) {
                        // TODO Auto-generated method stub
                        if (msg_id == 1) {
                            if (result.equals(DialogResult.Yes)) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }
                });
            } else {
                app.ShowMessage(this, "Reddedilecektir!\nEmin misiniz?", "TESİSAT KABUL SONUCU", DialogMode.YesNo, 1, 0, new IDialogCallback() {

                    @Override
                    public void DialogResponse(int msg_id, DialogResult result) {
                        // TODO Auto-generated method stub
                        if (msg_id == 1) {
                            if (result.equals(DialogResult.Yes)) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }
                });
            }
        } else {
            if (getAlertMode() != -1) {
                app.ShowMessage(this, "Ölçü Devre Tespit Formu Daha önce Doldurulmuştur. Endeks Sayfasına Yönlendirileceksiniz.", "! UYARI", DialogMode.Ok, 1, 0, new IDialogCallback() {

                    @Override
                    public void DialogResponse(int msg_id, DialogResult result) {
                        // TODO Auto-generated method stub
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            } else {
                app.ShowMessage(this, "Tespit tamamlandı.", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                    @Override
                    public void DialogResponse(int msg_id, DialogResult result) {
                        // TODO Auto-generated method stub
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        }

    }

    public IAciklama getUNVAN() { return UNVAN;}
    public void setUNVAN(IAciklama UNVAN) throws Exception {
        com.mobit.utility.check(UNVAN.getSTR(), field.s_UNVAN);
        this.UNVAN = UNVAN;
    }

    public void UnvanDegistir() {
       // if (isemri.getUNVAN().isEmpty()) {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TespitActivity2.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_unvan, null);
            final EditText dUnvan = (EditText) mView.findViewById(R.id.dUnvan);
            final EditText dTCkimlikno = (EditText) mView.findViewById(R.id.dTCkimlikno);
            final EditText dVergiNo = (EditText) mView.findViewById(R.id.dVergiNo);
            TextView txtUnvan = mView.findViewById(R.id.TxtUnvan);
            txtUnvan.setText(String.format("Tesisat bilgisi güncelleyiniz!\nTesisat No: %d\n%s", isemri.getTESISAT_NO(), isemri.getUNVAN()));
            Button btnUnvanBas = (Button) mView.findViewById(R.id.btnUnvanBas);
            mBuilder.setView(mView);
            dialogUnvan = mBuilder.create();
            dialogUnvan.show();
            dialogUnvan.setCancelable(true);
            btnUnvanBas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isemri.getUNVAN().isEmpty()) {
                        if (dUnvan.getText().toString().isEmpty() && dTCkimlikno.getText().toString().isEmpty() && dVergiNo.getText().toString().isEmpty()) {
                            //UNVAN-TC-VERGİ NO HEPSİ BOŞ İSE
                            Toast.makeText(TespitActivity2.this,
                                    "Unvan giriniz.",
                                    Toast.LENGTH_SHORT).show();
                            
                        } else if (((dUnvan.getText().toString().indexOf(" ") == 0)
                                || (dUnvan.getText().toString().toLowerCase().contains("abone"))
                                || (dUnvan.getText().toString().toLowerCase().contains("belirs")))//belirsiz
                        ) {

                            Toast.makeText(TespitActivity2.this,
                                    "Unvan giriniz.",
                                    Toast.LENGTH_SHORT).show();

                        } else if (dUnvan.getText().toString().isEmpty() && (!dTCkimlikno.getText().toString().isEmpty() || !dVergiNo.getText().toString().isEmpty())) {
                            //UNVAN BOŞ AMA TC VEYA VERGİ NO DOLU İSE
                            Toast.makeText(TespitActivity2.this,
                                    "Unvan giriniz.",
                                    Toast.LENGTH_SHORT).show();

                        } else if (!dTCkimlikno.getText().toString().isEmpty() && !TcKimlikKontrol(dTCkimlikno.getText().toString())) {
                            //TC DOLU İSE VE TC ALGORİTMASINA UYGUN DEĞİL İSE
                            Toast.makeText(TespitActivity2.this,
                                    "TC Kimlik No Geçerli Değil.",
                                    Toast.LENGTH_SHORT).show();

                        } else if (!dVergiNo.getText().toString().isEmpty() && !VergiNoKontrol(dVergiNo.getText().toString())) {
                            //VERGİ NO DOLU İSE VE VERGİ NO ALGORİTMASINA UYGUN DEĞİL İSE
                            Toast.makeText(TespitActivity2.this,
                                    "Vergi No Geçerli Değil.",
                                    Toast.LENGTH_SHORT).show();

                        } else if (!dUnvan.getText().toString().contains(" ")) {
                            //UNVAN AD SOYAD ŞEKLİNDE YAZILMADIYSA
                            Toast.makeText(TespitActivity2.this,
                                    "Unvana Ad Soyad Giriniz.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            if (dTCkimlikno.getText().toString().isEmpty() && dVergiNo.getText().toString().isEmpty()) {
                                UnvanBas(dUnvan.getText().toString(), "00000000000", "0000000000");
                            } else if (dTCkimlikno.getText().toString().isEmpty()) {
                                UnvanBas(dUnvan.getText().toString(), "00000000000", dVergiNo.getText().toString());
                            } else if (dVergiNo.getText().toString().isEmpty()) {
                                UnvanBas(dUnvan.getText().toString(), dTCkimlikno.getText().toString(), "0000000000");
                            } else {
                                UnvanBas(dUnvan.getText().toString(), dTCkimlikno.getText().toString(), dVergiNo.getText().toString());
                            }

                            app.ShowMessage(TespitActivity2.this, "Tespit tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                                @Override
                                public void DialogResponse(int msg_id, DialogResult result) {
                                    // TODO Auto-generated method stub
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                        }

                    }else {
                        if (dUnvan.getText().toString().isEmpty()){
                            if (!dTCkimlikno.getText().toString().isEmpty() || !dVergiNo.getText().toString().isEmpty()){
                                Toast.makeText(TespitActivity2.this,
                                        "ÜNVAN GİRİLMEDEN TC KİMLİK NO veya VERGİ NO GİRİLEMEZ!",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                if (dTCkimlikno.getText().toString().isEmpty() && dVergiNo.getText().toString().isEmpty()) {
                                    UnvanBas(dUnvan.getText().toString(), "00000000000", "0000000000");
                                } else if (dTCkimlikno.getText().toString().isEmpty()) {
                                    UnvanBas(dUnvan.getText().toString(), "00000000000", dVergiNo.getText().toString());
                                } else if (dVergiNo.getText().toString().isEmpty()) {
                                    UnvanBas(dUnvan.getText().toString(), dTCkimlikno.getText().toString(), "0000000000");
                                } else {
                                    UnvanBas(dUnvan.getText().toString(), dTCkimlikno.getText().toString(), dVergiNo.getText().toString());
                                }
                                app.ShowMessage(TespitActivity2.this, "Tespit tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                                    @Override
                                    public void DialogResponse(int msg_id, DialogResult result) {
                                        // TODO Auto-generated method stub
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                            }
                        }else{
                            if (dTCkimlikno.getText().toString().isEmpty() && dVergiNo.getText().toString().isEmpty()){
                                Toast.makeText(TespitActivity2.this,
                                        "ÜNVAN GİRİLDİ TC KİMLİK NO veya VERGİ NO GİRİNİZ!",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                if (!dVergiNo.getText().toString().isEmpty() && !VergiNoKontrol(dVergiNo.getText().toString())){
                                    Toast.makeText(TespitActivity2.this,
                                            "Vergi No Geçerli Değil.",
                                            Toast.LENGTH_SHORT).show();
                                }else if (!dTCkimlikno.getText().toString().isEmpty() && !TcKimlikKontrol(dTCkimlikno.getText().toString())){
                                    Toast.makeText(TespitActivity2.this,
                                            "TC No Geçerli Değil.",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                    if (dTCkimlikno.getText().toString().isEmpty() && dVergiNo.getText().toString().isEmpty()) {
                                        UnvanBas(dUnvan.getText().toString(), "00000000000", "0000000000");
                                    } else if (dTCkimlikno.getText().toString().isEmpty()) {
                                        UnvanBas(dUnvan.getText().toString(), "00000000000", dVergiNo.getText().toString());
                                    } else if (dVergiNo.getText().toString().isEmpty()) {
                                        UnvanBas(dUnvan.getText().toString(), dTCkimlikno.getText().toString(), "0000000000");
                                    } else {
                                        UnvanBas(dUnvan.getText().toString(), dTCkimlikno.getText().toString(), dVergiNo.getText().toString());
                                    }
                                    app.ShowMessage(TespitActivity2.this, "Tespit tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                                        @Override
                                        public void DialogResponse(int msg_id, DialogResult result) {
                                            // TODO Auto-generated method stub
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    });
                                }
                            }

                        }




                    }
                }

            });

       // } else {
            /*app.ShowMessage(this, "Tespit tamamlandı", "", DialogMode.Ok, 1, 0, new IDialogCallback() {

                @Override
                public void DialogResponse(int msg_id, DialogResult result) {
                    // TODO Auto-generated method stub
                    setResult(RESULT_OK);
                    finish();
                }
            });*/
        }
   // }

    public void array_update() {
        soru_sayaci = 0;
        arraySorular.clear();
        for (IIslem islem : islemGrup.getIslem(IIsemriSoru.class)) {
            arraySorular.add((IIsemriSoru) islem);
            soru_sayaci++;
            if (soru_sayaci == soru_count) {

                break;
            }
        }
        adapterSorular = new ArrayAdapterEx<IIsemriSoru>(this, R.layout.row_tespit, arraySorular, ViewHolder.class);
        listSorular.setAdapter(adapterSorular);
    }

    public JsonObject GetKosulDetay(int KosulSoru) {

        for (int i = 0; i < SoruSen.size(); i++) {

            JsonObject jsO = new JsonObject();
            String asd = SoruSen.get(i).getAsJsonObject().get("soru_no").toString();
            if (KosulSoru == Integer.parseInt(asd)) {
                return (SoruSen.get(i).getAsJsonObject());
            }
        }
        return new JsonObject();
    }

    public class ViewHolder<T> extends ViewHolderEx<T> implements TextWatcher {

        private View view;
        private TextView textView;

        private RadioGroup radioEvetHayir;
        private RadioButton radioEvet;
        private RadioButton radioHayir;
        private RadioGroup textCoktanSecmeli;
        private RelativeLayout textVeri;
        private EditText editVeri;
        private IIsemriSoru soru;

        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
            super(adapter, view);

            this.view = view;

            textView = (TextView) view.findViewById(R.id.rowTextView);

            radioEvetHayir = (RadioGroup) view.findViewById(R.id.radioEvetHayir);
            radioEvet = (RadioButton) view.findViewById(R.id.radioEvet);
            radioHayir = (RadioButton) view.findViewById(R.id.radioHayir);

            textCoktanSecmeli = (RadioGroup) view.findViewById(R.id.textCoktanSecmeli);

            textVeri = (RelativeLayout) view.findViewById(R.id.textVeri);
            editVeri = (EditText) view.findViewById(R.id.editVeri);

            radioEvet.setOnClickListener(this);
            radioHayir.setOnClickListener(this);
            textView.setOnClickListener(this);
            editVeri.addTextChangedListener(this);


        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


            int id = arg0.getId();

            if (id == R.id.radioEvet || id == R.id.radioHayir) {
                String cevap = "";
                if (radioEvet.isChecked()) cevap = "1";
                else if (radioHayir.isChecked()) cevap = "0";
                soru.setCEVAP(cevap);

                view.invalidate();
            } else if (id >= 0 && id < 1000) {
                soru.setCEVAP(Integer.toString(id));
                view.invalidate();

            }
            //muhammed gökkaya
            //Cevap kısıtı bu kısımda olmalı clikde burası çalışıyor
            String SoruNo;
            if (check_senaryo) {
                for (int i = 0; i < SoruSen.size(); i++) {
                    SoruNo = (SoruSen.get(i).getAsJsonObject().get("soru_no")).toString();

                    if (SoruNo.equals(String.valueOf(soru.getSORU_NO()))) {
                        if (SoruSen.get(i).getAsJsonObject().size() == 6) {
                            soru_count = Integer.parseInt(SoruNo) + 1;
                            SoruSen.get(i).getAsJsonObject().addProperty("ver_cevap", soru.getCEVAP());
                        } else {
                            SoruSen.get(i).getAsJsonObject().remove("ver_cevap");
                            SoruSen.get(i).getAsJsonObject().addProperty("ver_cevap", soru.getCEVAP());
                            soru_count = Integer.parseInt(SoruNo) + 1;

                        }


                    }
                }
                SoruSenaryo soruSenaryo = new SoruSenaryo();
                if ((emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 24)) {
                    if (soru_count == 2 || soru_count == 14 || soru_count == 18 || soru_count == 28) {

                        if (soru_count == 28)
                            tespit24soru_yirmi7 = soru.getCEVAP();
                        soru_count = soruSenaryo.GetSoruCount(soru_count, emir_turu, String.valueOf(isemri.getALT_EMIR_TURU()), soru.getSORU_NO(), soru.getCEVAP());
                        soru_count++;
                        if (soru_count <= soru_sayisi) {
                            array_update();
                        }
                    }
                } else {
                    soru_count = soruSenaryo.GetSoruCount(soru_count, emir_turu, String.valueOf(isemri.getALT_EMIR_TURU()), soru.getSORU_NO(), soru.getCEVAP());
                    soru_count++;
                    if (soru_count <= soru_sayisi) {
                        array_update();
                    }

                }
				/*
				if(!soru.getCEVAP().isEmpty()){

                }

				 */
                if (soru_count >= soru_sayisi) {
                    if (Kontrol()) Tamamla();

                }
            } else {
                if (Kontrol()) Tamamla();
            }
            //

        }


        @Override
        public void set(int position, T obj) {

            reset();

            super.set(position, obj);
            if (obj != null) {
                textView.setText(obj.toString());
                textView.setTag(obj);
                IIsemriSoru s = (IIsemriSoru) obj;
                secim(s);

            }

        }

        void reset() {

            radioEvetHayir.setVisibility(View.GONE);
            radioEvetHayir.clearCheck();
            radioEvet.setVisibility(View.GONE);
            radioHayir.setVisibility(View.GONE);
            textCoktanSecmeli.setVisibility(View.GONE);
            textCoktanSecmeli.clearCheck();
            textVeri.setVisibility(View.GONE);
            editVeri.setVisibility(View.GONE);
            editVeri.setText("");

            radioEvet.setOnClickListener(null);
            radioHayir.setOnClickListener(null);
            textView.setOnClickListener(null);
            editVeri.removeTextChangedListener(this);

            soru = null;
        }


        void secim(IIsemriSoru soru) {

            this.soru = soru;
            ICevapTipi cevapTipi = soru.getCEVAP_TIPI();

            if (soru.getSORU_NO() == 0) return;

            if (cevapTipi.equals(CevapTipi.CoktanSecmeli)) {

                int index = -1;
                if (soru.getCEVAP() != null && !soru.getCEVAP().isEmpty())
                    index = Integer.parseInt(soru.getCEVAP());


                String[] CevapFilter = {};
                JsonArray jsA;
                JsonObject jsO;
                String kosul_soru = "";
                if (check_senaryo) {
                    SoruSenaryo soruSenaryo = new SoruSenaryo();
                    jsA = soruSenaryo.GetSoruDetay(soru.getSORU_NO(), emir_turu, String.valueOf(isemri.getALT_EMIR_TURU()));
                    kosul_soru = (jsA.get(0).getAsJsonObject().get("kosul_soru")).toString();
                    if (!kosul_soru.equals("-1")) {
                        jsO = GetKosulDetay(Integer.parseInt(kosul_soru));
                        if (jsO.size() > 0) {
                            for (int i = 0; i < jsA.size(); i++) {
                                String kosul_cevap = jsA.get(i).getAsJsonObject().get("kosul_cevap").getAsString();
                                String ver_cevap = jsO.get("ver_cevap").getAsString();
                                if (kosul_cevap.equals(ver_cevap)) {
                                    CevapFilter = jsA.get(i).getAsJsonObject().get("cevap").getAsString().split(";");
                                }
                            }

                        }
                    }
                }


                textCoktanSecmeli.setVisibility(View.VISIBLE);
                textCoktanSecmeli.removeAllViews();
                listCoktanSecmeli = soru.getCEVAP_FORMAT_LIST();
                for (int i = 0; i < listCoktanSecmeli.length; i++) {
                    RadioButton radioButton = new RadioButton(TespitActivity2.this);
                    boolean knt = false;
                    if (CevapFilter.length == 0) {
                        if (!listCoktanSecmeli[i].equals("Yok")) {
                            radioButton.setLayoutParams(lp);
                            radioButton.setId(i);
                            radioButton.setText(listCoktanSecmeli[i]);
                            radioButton.setTag(listCoktanSecmeli[i]);
                            radioButton.setOnClickListener(this);
                            textCoktanSecmeli.addView(radioButton);
                            if (index > -1 && i == index) radioButton.setChecked(true);

                            //HÜSEYİN EMRE ÇEVİK 8.2021 OKUMA TESPİTİNDE OTOMATİK CEVAP
                            if ((String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit") && (isemri.getALT_EMIR_TURU() == 23 || isemri.getALT_EMIR_TURU() == 34))) {

                                if (isemri.getISEMRI_ACIKLAMA().contains("(EVET)") && radioButton.getText().toString().contains("Evet")) {
                                    soru.setCEVAP("0");
                                    Tamamla();
                                }
                                if (isemri.getISEMRI_ACIKLAMA().contains("(HAYIR)") && radioButton.getText().toString().contains("Hayir")) {
                                    soru.setCEVAP("1");
                                    Tamamla();
                                }

                            }
                        }
                    }
                    if (CevapFilter.length > 0) {
                        for (int j = 0; j < CevapFilter.length; j++) {
                            if (Integer.parseInt(CevapFilter[j]) == i) {
                                knt = true;
                            }
                        }


                    }


                    if (!knt && CevapFilter.length == 1) {
                        if (CevapFilter[0].equals("-1") && !kosul_soru.equals("-1"))
                            soru.setCEVAP("-99");

                        //soru_count=soru.getSORU_NO();
                        //soru_count++;
                        //array_update();
                    }
                    if (knt && CevapFilter.length == 1) {
                        soru.setCEVAP(CevapFilter[0]);
                        if (soru_count >= soru_sayisi) {
                            if (Kontrol()) Tamamla();

                        }
                        //soru_count++;
                        //array_update();
                    }
                    if (knt && CevapFilter.length > 1) {
                        radioButton.setLayoutParams(lp);
                        radioButton.setId(i);
                        radioButton.setText(listCoktanSecmeli[i]);
                        radioButton.setTag(listCoktanSecmeli[i]);
                        radioButton.setOnClickListener(this);
                        textCoktanSecmeli.addView(radioButton);
                        if (index > -1 && i == index) radioButton.setChecked(true);

                        //array_update();
                    }
                }


            } else if (cevapTipi.equals(CevapTipi.Boolean)) {
                radioEvetHayir.clearCheck();
                radioEvetHayir.setVisibility(View.VISIBLE);
                radioEvet.setVisibility(View.VISIBLE);
                radioHayir.setVisibility(View.VISIBLE);

                //HÜSEYİN EMRE ÇEVİK 8.2021 OKUMA TESPİTİNDE OTOMATİK CEVAP
                if ((String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit") && (isemri.getALT_EMIR_TURU() == 23 || isemri.getALT_EMIR_TURU() == 34))) {

                    if (isemri.getISEMRI_ACIKLAMA().contains("(EVET)")) {
                        soru.setCEVAP("1");
                        radioEvet.setChecked(true);
                        Tamamla();
                    } else if (isemri.getISEMRI_ACIKLAMA().contains("(HAYIR)")) {
                        soru.setCEVAP("0");
                        radioHayir.setChecked(true);
                        Tamamla();
                    }

                }

                if (!soru.getCEVAP().isEmpty()) {
                    if (soru.getCEVAP().equals("1"))
                        radioEvet.setChecked(true);
                    else
                        radioHayir.setChecked(true);
                }
                radioEvet.setOnClickListener(this);
                radioHayir.setOnClickListener(this);

            } else {

                textVeri.setVisibility(View.VISIBLE);
                editVeri.setVisibility(View.VISIBLE);
                if (soru.getCEVAP().toString().equals("Yok")) {
                    editVeri.setText("");
                } else {
                    editVeri.setText(soru.getCEVAP());
                }
                if (cevapTipi.equals(CevapTipi.Float)) {
                    editVeri.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editVeri.setFilters(fFloat);
                } else if (cevapTipi.equals(CevapTipi.Integer)) {
                    editVeri.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editVeri.setFilters(fInteger);
                } else if (cevapTipi.equals(CevapTipi.Strings)) {
                    editVeri.setInputType(InputType.TYPE_CLASS_TEXT);
                    editVeri.setFilters(fString);
                }


                editVeri.addTextChangedListener(this);
                editVeri.requestFocus();
                if (emir_turu.equals("6") && isemri.getALT_EMIR_TURU() == 24 && tespit24soru_yirmi7.equals("1") && soru.getSORU_NO() != 12) {
                    editVeri.setText("Yok");
                    editVeri.setVisibility(View.GONE);
                }
            }

        }


        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            return;
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            return;
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            if (soru == null || editVeri.getVisibility() != View.VISIBLE)
                return;
            ICevapTipi cevapTipi = soru.getCEVAP_TIPI();
            if ((cevapTipi.equals(CevapTipi.Float) || cevapTipi.equals(CevapTipi.Integer)
                    || cevapTipi.equals(CevapTipi.Strings))) {
                String s = arg0.toString();
                soru.setCEVAP(s);
            }
        }


    }

    public void UnvanBas(String unvanb, String TcKimlikNo, String VergiNo) {

        try {

            //send(islemGrup,true);
            //muhammed gökkaya zabit
            try {
                Aciklama unvan = new Aciklama(unvanb);
                Aciklama tc = new Aciklama(TcKimlikNo);
                Aciklama vergi = new Aciklama(VergiNo);

                put_isemri_unvan zz2 = new put_isemri_unvan(isemri.getTESISAT_NO(), isemri.getSAHA_ISEMRI_NO(),
                        tc,
                        vergi,
                        unvan);
                IIslemGrup islemGrup = new IslemGrup2();
                islemGrup.add(zz2);
                islemGrup.setIslemTipi(mobit.elec.android.IDef.ZABIT_EKLEME_ISLEMI);
                //Burada patlıyor NullPointer (int) HÜSEYİN Emre Çevik
                IIslem islem = app.saveIslem(islemGrup);

                app.sendIslem(this, islem, new Callback() {
                    @Override
                    public Object run(Object obj) {


                        app.ShowMessage(TespitActivity2.this, "Unvan  Eklendi", "Başarılı");
                        setResult(RESULT_OK);
                        finish();
                        return null;
                    }
                }, 10000);


            } catch (Exception e) {
                app.ShowException(TespitActivity2.this, e);
            }


        } catch (Exception e) {
            app.ShowException(this, e);
            return;
        }

        setResult(RESULT_OK);
        //finish();

    }

    public boolean TcKimlikKontrol(String Tc) {
        if (Tc.length() != 11) return false;
        int[] tchane = new int[Tc.length()];
        int toplam = 0;
        // Copy character by character into array
        for (int i = 0; i < Tc.length(); i++) {
            tchane[i] = Integer.parseInt(String.valueOf(Tc.charAt(i)));
            toplam += tchane[i];
        }
        toplam -= tchane[10];
        if ((toplam % 10) != tchane[10]) {
            // TC KİMLİK NUMARANIZIN İLK 10 BASAMAMAĞININ TOPLAMININ 10'A BÖLÜMÜNDEN KALAN 11. BASAMAĞI VERİR. KENDİ NUMARANIZDA DENEYEBİLİRSİNİZ.
            return false;
        }
        if (((tchane[0] + tchane[2] + tchane[4] + tchane[6] + tchane[8]) * 7 + (tchane[1] + tchane[3] + tchane[5] + tchane[7]) * 9) % 10 != tchane[9]) {
            // (1-3-5-7-9. BASAMAKLARIN TOPLAMININ 7 İLE ÇARPIMI) + (2-4-6-8. BASAMAKLARIN TOPLAMININ 9 İLE ÇARPIMINDAN 10 BÖLÜMÜNÜN KALANI 10. BASAMAĞI VERİR.
            return false;
        }
        if (((tchane[0] + tchane[2] + tchane[4] + tchane[6] + tchane[8]) * 8) % 10 != tchane[10]) {
            // 1-3-5-7-9. BASAMAKLARIN TOPLAMI 8 İLE ÇARPILIP 10'A BÖLÜNÜMÜNDEN KALAN 11. BASAMAĞI VERİR.
            return false;
        }


        return true;
    }

    public static boolean VergiNoKontrol(String vkn) {
        int tmp;
        int sum = 0;
        if (vkn != null && vkn.length() == 10 && isInt(vkn)) {
            int lastDigit = Character.getNumericValue(vkn.charAt(9));
            for (int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(vkn.charAt(i));
                tmp = (digit + 10 - (i + 1)) % 10;
                sum = (int) (tmp == 9 ? sum + tmp : sum + ((tmp * (Math.pow(2, 10 - (i + 1)))) % 9));
            }
            return lastDigit == (10 - (sum % 10)) % 10;
        }
        return false;
    }

    private static boolean isInt(String s) {
        for (int a = 0; a < s.length(); a++) {
            if (a == 0 && s.charAt(a) == '-') continue;
            if (!Character.isDigit(s.charAt(a))) return false;
        }
        return true;
    }

    private class LoadDataUnvansizKacak extends AsyncTask<Void, Void, Void> {

        public ProgressDialog progressDialog = new ProgressDialog(TespitActivity2.this);

        int Kayit_Tarihi = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Gönderiliyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                UnvansizKacakServisResult = medasServer.EndoksorUnvansizKacak(app.getActiveIsemri().getSAHA_ISEMRI_NO(), app.getActiveIsemri().getTESISAT_NO(), Kayit_Tarihi);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (UnvansizKacakServisResult) {
//				AboneDurumGirisActivity
//				Intent intent = new Intent(TespitActivity2.this, app.getFormClass(mobit.elec.mbs.medas.android.IDef.FORM_ABONE_DURUM_GIRIS));
//				startActivity(intent);
                dialogUnvan.dismiss();
                setResult(Operation.No);
                finish();


            } else {
                Toast.makeText(TespitActivity2.this,
                        "Servise yazılamadı",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
