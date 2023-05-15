package mobit.elec.mbs.medas.android;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobit.BarcodeResult;
import com.mobit.Callback;
import com.mobit.DbHelper;
import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;

import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.PopupList;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.AltEmirTuru;
import mobit.elec.Endeks;
import mobit.elec.IEndeks;
import mobit.elec.IEndeksler;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriSorular;
import mobit.elec.ISayacMarka;
import mobit.elec.ITakilanSayac;
import mobit.elec.MsgInfo;
import mobit.elec.android.AboneDurumGirisActivity;
import mobit.elec.android.IDef;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IFazSayisi;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IMulkiyet;
import mobit.elec.enums.ISayacCinsi;
import mobit.elec.enums.ISayacHaneSayisi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.IVoltaj;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.enums.Mulkiyet;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi.SayacBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.SayacEndeksBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.SayacSenkBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.muhurBilgi;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;

public class SayacTakmaActivity2 extends AppCompatActivity
        implements IForm, OnClickListener, ICallback, IPopupCallback {

    IMedasApplication app;
    IIsemri isemri;
    IIsemriIslem isemriIslem;
    ITakilanSayac sayacIslem;

    EditText editSayacNo;
    TextView textMarkaSecim;
    TextView markaSecim;
    Spinner muhurSeri;
    EditText editMuhurNo;

    TextView textSayacNo;
    TextView textMarka;
    TextView textSayacKodu;
    TextView textHaneSayisi;
    TextView textSayacCinsi;
    TextView textFazSayisi;
    TextView textGerilim;
    TextView textMulkiyet;
    TextView textImalYili;
    TextView textAkim;

    Button optikOku;
    Button barkod;
    Button sayacBilgi;
    Button buttonKaydet;

    RadioGroup radio;
    RadioButton yeniSayac;
    RadioButton eskiSayac;

    IEndeksler endeksler;

    PopupList<ISayacMarka> popupSayacMarka;

    List<ISayacMarka> markalar;

    SayacZimmetBilgi szb = new SayacZimmetBilgi();

    IBarcode barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sayac_takma2);
        //setContentView(R.layout.activity_tespit2);

        if (!(Globals.app instanceof IMedasApplication)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }

        app = (IMedasApplication) Globals.app;


        IIslem islem = app.getActiveIslem();
        if (!(islem instanceof IIslemGrup)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }

        IIslemGrup islemGrup = (IIslemGrup) islem;
        isemriIslem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
        isemri = (IIsemri) isemriIslem.getIsemri();
        IIslemTipi islemTipi = isemri.getISLEM_TIPI();

        if (!islemTipi.equals(IslemTipi.SayacTakma) && !islemTipi.equals(IslemTipi.SayacDegistir)) {
            // Bu iş emri desteklenmiyor
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }

        FormInitParam param = app.getFormInitParam();
        param.captionText = islemTipi.toString();
        app.initForm(this, param);

        sayacIslem = (ITakilanSayac) islemGrup.getIslem(ITakilanSayac.class).get(0);

        editSayacNo = (EditText) findViewById(R.id.editSayacNo);
        textMarkaSecim = (TextView) findViewById(R.id.textView5);
        markaSecim = (TextView) findViewById(R.id.markaSecim);

        muhurSeri = (Spinner) findViewById(R.id.muhurSeri);
        editMuhurNo = (EditText) findViewById(R.id.editMuhurNo);

        textSayacNo = (TextView) findViewById(R.id.textSayacNo);
        textMarka = (TextView) findViewById(R.id.textMarka);
        textSayacKodu = (TextView) findViewById(R.id.textSayacKodu);
        textHaneSayisi = (TextView) findViewById(R.id.textHaneSayisi);
        textSayacCinsi = (TextView) findViewById(R.id.textSayacCinsi);
        textFazSayisi = (TextView) findViewById(R.id.textFazSayisi);
        textGerilim = (TextView) findViewById(R.id.textGerilim);
        textMulkiyet = (TextView) findViewById(R.id.textMulkiyet);

        textImalYili = (TextView) findViewById(R.id.textImalYili);
        textAkim = (TextView) findViewById(R.id.textAkim);

        optikOku = (Button) findViewById(R.id.optikOku);
        barkod = (Button) findViewById(R.id.barkod);
        sayacBilgi = (Button) findViewById(R.id.sayacBilgi);
        buttonKaydet = (Button) findViewById(R.id.buttonKaydet);

        radio = (RadioGroup) findViewById(R.id.radio);
        yeniSayac = (RadioButton) findViewById(R.id.yeniSayac);
        eskiSayac = (RadioButton) findViewById(R.id.eskiSayac);

        yeniSayac.setOnClickListener(this);
        eskiSayac.setOnClickListener(this);
        optikOku.setOnClickListener(this);
        barkod.setOnClickListener(this);
        sayacBilgi.setOnClickListener(this);
        buttonKaydet.setOnClickListener(this);
        //muhammmed gökkaya
        //muhurSeri.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        try {
            markalar = DbHelper.SelectAll(app.getConnection(), sayac_marka.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            app.ShowException(this, e);
            finish();
            return;
        }
        popupSayacMarka = new PopupList<ISayacMarka>(markaSecim, 200, markalar, this);

        if (/*!Globals.isDeveloping() &&*/ islemTipi.equals(IslemTipi.SayacDegistir)) {
            radio.setVisibility(View.INVISIBLE);
            yeniSayac.setChecked(true);
            eskiSayac.setChecked(false);
            sayacIslem.setYENI_SAYAC(true);
        } else {
            radio.setVisibility(View.VISIBLE);
            yeniSayac.setChecked(false);
            eskiSayac.setChecked(false);
            Boolean b = sayacIslem.getYENI_SAYAC();
            if (b != null) {
                if (b) {
                    yeniSayac.setChecked(true);
                } else {
                    eskiSayac.setChecked(true);
                    textMarkaSecim.setVisibility(View.INVISIBLE);
                    markaSecim.setVisibility(View.INVISIBLE);
                }
            } else {

                textMarkaSecim.setVisibility(View.INVISIBLE);
                markaSecim.setVisibility(View.INVISIBLE);
            }

        }
        Object liste = app.getMuhurSeriler();
        ArrayAdapter<muhurBilgi> spinnerAdapter =
                new ArrayAdapter<muhurBilgi>(this, android.R.layout.simple_spinner_item, app.getMuhurSeriler());
        muhurSeri.setAdapter(spinnerAdapter);
        //muhammed gökkaya
        muhurSeri.setSelection(((ArrayAdapter) muhurSeri.getAdapter()).getPosition("N"));

        editSayacNo.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    sayacBilgi.performClick();
                    editMuhurNo.requestFocus();
                    handled = true;
                }
                return handled;
            }

        });

        editMuhurNo.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buttonKaydet.performClick();
                    handled = true;
                }
                return handled;
            }

        });

        barcode = app.newBarcodeObject();

        doldur();
    }

    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(true);
        app.setPortCallback(this);
        super.onResume();

    }

    private void doldur() {

        if (sayacIslem.getSAYAC_NO() > 0) {
            String s = Integer.toString(sayacIslem.getSAYAC_NO());
            editSayacNo.setText(s);
            textSayacNo.setText(s);
        }
        if (sayacIslem.getMARKA() != null) {

            textMarka.setTag(sayacIslem.getMARKA());
            textMarka.setText(textMarka.getTag().toString());
        }

        if (sayacIslem.getSERI_NO() != null) {
            mobit.android.utility.selectSpinnerItemByValue(muhurSeri, sayacIslem.getSERI_NO().getSeri());
            editMuhurNo.setText(Integer.toString(sayacIslem.getSERI_NO().getNo()));
        }
        if (sayacIslem.getSAYAC_KODU() != null) {
            textSayacKodu.setTag(sayacIslem.getSAYAC_KODU());
            textSayacKodu.setText(textSayacKodu.getTag().toString());
        }
        if (sayacIslem.getHANE_SAYISI() != null) {
            textHaneSayisi.setTag(sayacIslem.getHANE_SAYISI());
            textHaneSayisi.setText(textHaneSayisi.getTag().toString());
        }
        if (sayacIslem.getSAYAC_CINSI() != null) {
            textSayacCinsi.setTag(sayacIslem.getSAYAC_CINSI());
            textSayacCinsi.setText(textSayacCinsi.getTag().toString());
        }
        if (sayacIslem.getFAZ_SAYISI() != null) {
            textFazSayisi.setTag(sayacIslem.getFAZ_SAYISI());
            textFazSayisi.setText(textFazSayisi.getTag().toString());
        }
        if (sayacIslem.getVOLTAJ() != null) {
            textGerilim.setTag(sayacIslem.getVOLTAJ());
            textGerilim.setText(textGerilim.getTag().toString());
        }
        if (sayacIslem.getMULKIYET() != null) {
            textMulkiyet.setTag(sayacIslem.getMULKIYET());
            textMulkiyet.setText(textMulkiyet.getTag().toString());
        }
        if (sayacIslem.getIMAL_YILI() != 0) {
            textImalYili.setText(Integer.toString(sayacIslem.getIMAL_YILI()));
        }
        if (sayacIslem.getAMPERAJ() != 0) {
            textAkim.setText(Integer.toString(sayacIslem.getAMPERAJ()));
        }

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

    void clear() {

        textSayacNo.setTag(null);
        textSayacNo.setText("");

        textMarka.setTag(null);
        textMarka.setText("");

        textSayacKodu.setTag(null);
        textSayacKodu.setText("");

        textHaneSayisi.setTag(null);
        textHaneSayisi.setText("");

        textSayacCinsi.setTag(null);
        textSayacCinsi.setText("");

        textFazSayisi.setTag(null);
        textFazSayisi.setText("");

        textGerilim.setTag(null);
        textGerilim.setText("");

        textMulkiyet.setTag(null);
        textMulkiyet.setText("");

        textAkim.setTag(null);
        textAkim.setText("");

        textImalYili.setTag(null);
        textImalYili.setText("");

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        try {

            int id = v.getId();
            if (id == R.id.yeniSayac) {
                textMarkaSecim.setVisibility(View.VISIBLE);
                markaSecim.setVisibility(View.VISIBLE);
                sayacIslem.setYENI_SAYAC(true);
            } else if (id == R.id.eskiSayac) {
                textMarkaSecim.setVisibility(View.INVISIBLE);
                markaSecim.setVisibility(View.INVISIBLE);
                sayacIslem.setYENI_SAYAC(false);
            } else if (id == R.id.optikOku) {

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
                        if (app.checkException(SayacTakmaActivity2.this, obj))
                            return null;
                        return null;
                    }

                });

            } else if (id == R.id.barkod) {

                barcode.startScanner(this);

            } else if (id == R.id.sayacBilgi) {

                if (!yeniSayac.isChecked() && !eskiSayac.isChecked())
                    throw new MobitException("Yeni/eski sayaç seçimi yapın!");

                boolean yeni = yeniSayac.isChecked();

                if (editSayacNo.getText().toString().isEmpty())
                    throw new MobitException("Sayaç no girin!");
                final int sayac_no = Integer.parseInt(editSayacNo.getText().toString());

				/*
				if(Globals.isDeveloping()){
					EskiSayacBilgiGetir(sayac_no);
					return;
				}*/

                if (yeni) {
                    final ISayacMarka marka = (ISayacMarka) markaSecim.getTag();
                    if (marka == null)
                        throw new MobitException("Sayaç marka seçin!");
                    YeniSayacBilgiGetir(sayac_no, marka);
                } else {
                    EskiSayacBilgiGetir(sayac_no);
                }


            } else if (id == R.id.buttonKaydet) {

                    Kaydet();

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            app.ShowException(this, e);
        }
    }

    @Override
    public void Opened() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Failed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void BeginRead(IReadResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void EndRead() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Read(IProbeEvent event, IReadResult result) {
        // TODO Auto-generated method stub
        result.set_okuma_zamani(app.getTime());
        editSayacNo.setText(result.get_sayac_no());

        String satera = "STE";
        String saterambs = "STR";
		/*
		if (Globals.isDeveloping())
			editSayacNo.setText("18869400");
			*/

        try {

            String flag = result.get_FlagCode();
            //Onur satera sayaçların flag kodu mbs'ye yanlış işlendiği için marka gelmiyordu-Düzeltildi
            if (result.get_FlagCode().equals(satera)){
                flag = "STR";
            }
            for (ISayacMarka sm : markalar) {
                if (sm.getSAYAC_MARKA_KODU().equals(flag)) {
                    markaSecim.setTag(sm);
                    markaSecim.setText(markaSecim.getTag().toString());
                    break;
                }
            }

            sayacBilgi.performClick();

        } catch (Exception e) {
            app.ShowException(this, e);
        }
    }

    @Override
    public void PowerEvent(IProbeEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ConnectionResetEvent(IProbeEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnItemSelect(View anchorView, Object obj) {
        // TODO Auto-generated method stub
        if (anchorView.equals(markaSecim)) {
            markaSecim.setTag(obj);
            markaSecim.setText(obj.toString());
            editSayacNo.requestFocus();
        }
    }

    private void Kaydet() {
        // new LoadData(this, "Sayaç bilgisi gönderiliyor...",
        // kaydet).execute();
        try {

            Kontrol();
            GetOdfPrint();

//            SayacTakmaActivity2.this.setResult(RESULT_OK);
//            SayacTakmaActivity2.this.finish();

            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            app.ShowException(SayacTakmaActivity2.this, e);
            return;
        }
    }

    void Kontrol() throws Exception {

        if (textSayacNo.getText().toString().isEmpty())
            throw new MobitException("Sayaç numarası tanımlanmamış!");

        int sayac_no = Integer.parseInt(textSayacNo.getText().toString());
        sayacIslem.setSAYAC_NO(sayac_no);

        if (textSayacKodu.getTag() == null)
            throw new MobitException("Sayaç kodu tanımlanmamış!");
        sayacIslem.setSAYAC_KODU((ISayacKodu) textSayacKodu.getTag());

        if (textMarka.getTag() == null)
            throw new MobitException("Sayaç marka tanımlanmamış!");
        sayacIslem.setMARKA((ISayacMarka) textMarka.getTag());

        if (textSayacCinsi.getTag() == null)
            throw new MobitException("Sayaç cinsi tanımlanmamış!");
        sayacIslem.setSAYAC_CINSI((ISayacCinsi) textSayacCinsi.getTag());

        if (textHaneSayisi.getTag() == null)
            throw new MobitException("Sayaç hane sayısı tanımlanmamış!");
        sayacIslem.setHANE_SAYISI((ISayacHaneSayisi) textHaneSayisi.getTag());

        if (textImalYili.getText().toString().isEmpty())
            throw new MobitException("Sayaç imal yılı tanımlanmamış!");
        sayacIslem.setIMAL_YILI(Integer.parseInt(textImalYili.getText().toString()));

        if (textFazSayisi.getTag() == null)
            throw new MobitException("Sayaç faz sayısı tanımlanmamış!");
        sayacIslem.setFAZ_SAYISI((IFazSayisi) textFazSayisi.getTag());

        if (textAkim.getText().toString().isEmpty())
            throw new MobitException("Sayaç akım değeri tanımlanmamış!");
        sayacIslem.setAMPERAJ(Integer.parseInt(textAkim.getText().toString()));

        if (textGerilim.getTag() == null)
            throw new MobitException("Sayaç gerilim değeri tanımlanmamış!");
        sayacIslem.setVOLTAJ((IVoltaj) textGerilim.getTag());

        if (textMulkiyet.getTag() == null)
            throw new MobitException("Sayaç mülkiyeti tanımlanmamış!");
        sayacIslem.setMULKIYET((IMulkiyet) textMulkiyet.getTag());
		/*
		if (muhurSeri.getSelectedItem() == null)

			throw new MobitException("Mühür seri seçin!");
		if (editMuhurNo.getText().toString().isEmpty())
			throw new MobitException("Mühür no girin!");
		*/
        sayacIslem.setSERI_NO(new SeriNo(muhurSeri.getSelectedItem().toString(), editMuhurNo.getText().toString()));

        if (endeksler != null) {
            for (IEndeks endeks : endeksler.getEndeksler()) {
                sayacIslem.getENDEKSLER().add(endeks);
            }
        }

        if (app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacTakma) || app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacDegistir)){
            sayacIslem.Kontrol();
            //Intent intent = new Intent(this, app.getFormClass(mobit.elec.android.IDef.FORM_OLCU_DEVRE));
            //startActivityForResult(intent, IDef.FORM_OLCU_DEVRE);
        }else {
            sayacIslem.Kontrol();
        }

    }



    // Yeni sayaç takılması
    void YeniSayacBilgiGetir(final int sayac_no, final ISayacMarka marka) throws Exception {

        endeksler = null;
        clear();

        szb.SayacBilgiService(this, app.getEleman().getELEMAN_KODU(), sayac_no, marka.getSAYAC_MARKA_KODU(), isemri.getSAHA_ISEMRI_NO(),
                new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        if (app.checkException(SayacTakmaActivity2.this, obj))
                            return null;
                        SayacBilgi sb = (SayacBilgi) obj;

                        try {

                            if (sb == null)
                                throw new MobitException("Sayaç bilgisi alınamadı!");

                            // H.Elif gerilime göre faz değeri
                            final Integer value = Integer.valueOf(sb.getVoltaj().toString().split(" ")[0]);
                            Integer value_faz = value == 220 ? 1 : 3;


                            textSayacNo.setTag(sb.SAYAC_KODU);
                            textSayacNo.setText(sb.SAYAC_KODU);

                            textMarka.setTag(sb.getSayacMarka());
                            textMarka.setText(textMarka.getTag().toString());

                            textSayacKodu.setTag(sb.getSayacKodu());
                            textSayacKodu.setText(textSayacKodu.getTag().toString());

                            textHaneSayisi.setTag(sb.getHaneSayisi());
                            textHaneSayisi.setText(textHaneSayisi.getTag().toString());

                            textSayacCinsi.setTag(sb.getSayacCinsi());
                            textSayacCinsi.setText(textSayacCinsi.getTag().toString());

                            textFazSayisi.setTag(sb.getFazSayisi());
                            textFazSayisi.setText(textFazSayisi.getTag().toString());

                            textGerilim.setTag(sb.getVoltaj());
                            textGerilim.setText(textGerilim.getTag().toString());

                            textImalYili.setTag(sb.getImalYili());
                            textImalYili.setText(textImalYili.getTag().toString());

                            textMulkiyet.setTag(Mulkiyet.KurumSayaci);
                            textMulkiyet.setText(textMulkiyet.getTag().toString());

                            textAkim.setTag(sb.getAmperaj());
                            textAkim.setText(textAkim.getTag().toString());


                            szb.SayacSenkService(SayacTakmaActivity2.this, app.getEleman().getELEMAN_KODU(), sayac_no,
                                    marka.getSAYAC_MARKA_KODU(), new Callback() {

                                        @Override
                                        public Object run(Object obj) {
                                            // TODO Auto-generated method stub
                                            if (app.checkException(SayacTakmaActivity2.this, obj))
                                                return null;
                                            if (obj == null) return null;
                                            //Onur makel sayaçlarda garantiden gelen sayaçların seri no'su sıfırlanmadan geldiği için endeks sıfırlama eklendi.
                                            SayacSenkBilgi ssb = (SayacSenkBilgi) obj;
                                            try {
                                                endeksler = ssb.getEndeksler();
                                                if (ssb.ESKI_MARKA.equals("BYL")){
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(SayacTakmaActivity2.this);
                                                    dialog.setCancelable(false);
                                                    dialog.setTitle("Endeks Kontrol");
                                                    dialog.setMessage("Takacağınız sayacın endekslerini kontrol ediniz. Doğru ise 'Devam Et' butonuna basınız, yanlış ise 'Sıfırla ve Devam Et' butonuna basınız  \nT1:" + endeksler.getEndeks(EndeksTipi.Gunduz).getValue().toString() + " \nT2:" + endeksler.getEndeks(EndeksTipi.Puant).getValue().toString() + " \nT3:" + endeksler.getEndeks(EndeksTipi.Gece).getValue().toString() + " \nReaktif:" + endeksler.getEndeks(EndeksTipi.Enduktif).getValue().toString() + " \nKapasitif:" + endeksler.getEndeks(EndeksTipi.Kapasitif).getValue().toString() );
                                                    dialog.setPositiveButton("Devam Et", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //Action for "Delete".
                                                            GetOdfNewEndeks(endeksler);
                                                            app.ShowMessage(SayacTakmaActivity2.this, "Endeksler alındı", "");
                                                        }
                                                    }).setNegativeButton("Sıfırla ve Devam Et", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //Action for "Cancel".
                                                            Double sıfır = 0.0;
                                                            try {
                                                                endeksler.getEndeks(EndeksTipi.Gunduz).setValue(sıfır);
                                                                endeksler.getEndeks(EndeksTipi.Gece).setValue(sıfır);
                                                                endeksler.getEndeks(EndeksTipi.Puant).setValue(sıfır);
                                                                endeksler.getEndeks(EndeksTipi.Enduktif).setValue(sıfır);
                                                                endeksler.getEndeks(EndeksTipi.Kapasitif).setValue(sıfır);
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            GetOdfNewEndeks(endeksler);
                                                            app.ShowMessage(SayacTakmaActivity2.this, "Endeksler Sıfırlandı", "");
                                                        }
                                                    });
                                                    final AlertDialog alert = dialog.create();
                                                    alert.show();
                                                }else {
                                                    GetOdfNewEndeks(endeksler);
                                                    app.ShowMessage(SayacTakmaActivity2.this, "Endeksler alındı", "");
                                                }
                                            } catch (Exception e) {
                                                // TODO Auto-generated catch
                                                // block
                                                app.ShowException(SayacTakmaActivity2.this, e);
                                            }

                                            return null;
                                        }
                                    });

                            GetOdfNew(sb);

                        } catch (Exception e) {
                            app.ShowException(SayacTakmaActivity2.this, e);
                        }

                        return null;
                    }

                });
    }

    // Sökülmüş sayacın tekrar takılması
    void EskiSayacBilgiGetir(int sayac_no) throws Exception {

        clear();

        int tesisat_no = isemri.getTESISAT_NO();
		/*
		if (Globals.isDeveloping()) {
			sayac_no = 73292967;//73099340;//70041596;
			tesisat_no = 1012964;//1274770;//1017806;
		}*/

        szb.SayacEndeksServisi(this, Integer.toString(tesisat_no), Integer.toString(sayac_no), new Callback() {

            @Override
            public Object run(Object obj) {
                // TODO Auto-generated method stub
                if (app.checkException(SayacTakmaActivity2.this, obj))
                    return null;

                SayacEndeksBilgi sb = (SayacEndeksBilgi) obj;

                try {

                    if (sb == null)
                        throw new MobitException("Eski sayaç bilgisi alınamadı!");

                    textSayacNo.setTag(sb.SAYAC_NO);
                    textSayacNo.setText(sb.SAYAC_NO);

                    textMarka.setTag(sb.getSayacMarka());
                    textMarka.setText(textMarka.getTag().toString());

                    textSayacKodu.setTag(sb.getSayacKodu());
                    textSayacKodu.setText(textSayacKodu.getTag().toString());

                    textHaneSayisi.setTag(sb.getHaneSayisi());
                    textHaneSayisi.setText(textHaneSayisi.getTag().toString());

                    textSayacCinsi.setTag(sb.getSayacCinsi());
                    textSayacCinsi.setText(textSayacCinsi.getTag().toString());

                    textFazSayisi.setTag(sb.getFazSayisi());
                    textFazSayisi.setText(textFazSayisi.getTag().toString());

                    textGerilim.setTag(sb.getVoltaj());
                    textGerilim.setText(textGerilim.getTag().toString());

                    textImalYili.setTag(sb.getImalYili());
                    textImalYili.setText(textImalYili.getTag().toString());

                    textMulkiyet.setTag(Mulkiyet.KurumSayaci);
                    textMulkiyet.setText(textMulkiyet.getTag().toString());

                    textAkim.setTag(sb.getAmperaj());
                    textAkim.setText(textAkim.getTag().toString());

                    endeksler = sb.getEndeksler();

                } catch (Exception e) {
                    app.ShowException(SayacTakmaActivity2.this, e);
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
            if (view instanceof EditText) {
                EditText edit = (EditText) view;
                edit.setText(result.resultData);
            }
        }
    }


    public OlcuDevreForm GetOdfPrint() {
        // H.ELif ölçü devre ekleniyor yazıcıdan çıkarabilmek için eski sayac bil. ve yeni sayac bilg olarak aşağıda iki metoda ayrıldı.
        try {
            OlcuDevreForm odf = new OlcuDevreForm();
            odf.settesisat_no(isemri.getTESISAT_NO());
            odf.setisemri_no(isemri.getSAHA_ISEMRI_NO());

            odf.set_unvan(isemri.getUNVAN());
            odf.set_adres(isemri.getADRES());

            odf.set_user_code(app.getEleman().getELEMAN_KODU());

            odf.set_formType("olcu_devre_akim_yok_form");

            odf.set_tarife_kodu(String.valueOf(isemri.getTARIFE_KODU()));
            odf.set_cbs1(isemri.getCBS().getX().toString());
            odf.set_cbs2(isemri.getCBS().getY().toString());

            if (isemri.getALT_EMIR_TURU() == 1) {
                odf.set_aciklama(" (Sayacınız arızadan değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 2) {
                odf.set_aciklama(" (Sayacınız periyodikten değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 3) {
                odf.set_aciklama(" (Sayacınız güç arttırımından değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 9) {
                odf.set_aciklama(" (Sayacınız güç tenzilinden değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 12) {
                odf.set_aciklama(" (Sayacınız abone talebinden değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 13) {
                odf.set_aciklama(" (Sayacınız kayıp / çalıntı nedeni ile değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 14) {
                odf.set_aciklama(" (Sayacınız OSOS kapsamında değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 19) {
                odf.set_aciklama(" (Sayacınız optik port arızasından değişmiştir.)");
            } else if (isemri.getALT_EMIR_TURU() == 22) {
                odf.set_aciklama(" (Sayacınız mühürlenmiştir.)");
            }else if (isemri.getALT_EMIR_TURU() == 97) {
                odf.set_aciklama(" (8x Kontrol.)");
            }

            odf.set_yeni_muhur(muhurSeri.getSelectedItem().toString() + "/" + Integer.valueOf(sayacIslem.getSERI_NO().getNo()));

            odf.set_sayac_degisimi_dur(1); //formdaki guc trafosu ve tespitinin görünümünü kapatmak için

            odf.set_sayac_marka(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getMARKA().getSAYAC_MARKA_KODU());
            odf.set_sayac_no(String.valueOf(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getSAYAC_NO()));
            odf.set_hane_sayisi(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getHANE_SAYISI().toString().split(" ")[0]);
            odf.setcarpan(isemri.getCARPAN());
            odf.set_imal_yili(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getIMAL_YILI());
            odf.set_amperaj(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getAMPERAJ());
            odf.set_voltaj(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getVOLTAJ() != null ? isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getVOLTAJ().getValue() : 0);

            IReadResult result = app.getOptikResult();
            if (result != null) {
                odf.setsayac_akim_1(result.get_faz1_akim());
                odf.setsayac_akim_2(result.get_faz2_akim());
                odf.setsayac_akim_3(result.get_faz3_akim());
                odf.setsayac_gerili_1(result.get_faz1_gerilim());
                odf.setsayac_gerili_2(result.get_faz2_gerilim());
                odf.setsayac_gerili_3(result.get_faz3_gerilim());
            }

            return odf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public OlcuDevreForm GetOdfNew(SayacBilgi sb) {
        // H.ELif ölçü devre yeni sayac bilgisi
        try {
            OlcuDevreForm odf = new OlcuDevreForm();

            odf.set_takilan_marka(sb.getSayacMarka().getSAYAC_MARKA_ADI());
            odf.set_takilan_no(editSayacNo.getText().toString());
            odf.set_takilan_haneadeti((sb.getHaneSayisi().toString().split(" ")[0]));
            odf.set_takilan_carpan(String.valueOf(isemri.getCARPAN()));
            odf.set_takilan_imalyili(String.valueOf(sb.getImalYili()));
            odf.set_takilan_akim(String.valueOf(sb.getAmperaj()));
            odf.set_takilan_gerilim(String.valueOf(sb.getVoltaj()));

            return odf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OlcuDevreForm GetOdfNewEndeks(IEndeksler endeksler) {
        // H.ELif ölçü devre yeni sayac bilgisi
        try {
            OlcuDevreForm odf = new OlcuDevreForm();

            double t = 0;
            String t1 = "", t2 = "", t3 = "", kapasitif = "", enduktif = "", demand = "";

            if(!endeksler.getEndeks(EndeksTipi.Gunduz).isEmpty()) {
                t1 = endeksler.getEndeks(EndeksTipi.Gunduz).getValue().toString();
                t += Double.parseDouble(t1);
            }
            if(!endeksler.getEndeks(EndeksTipi.Puant).isEmpty()) {
                t2 = endeksler.getEndeks(EndeksTipi.Puant).getValue().toString();
                t += Double.parseDouble(t2);
            }
            if(!endeksler.getEndeks(EndeksTipi.Gece).isEmpty()) {
                t3 = endeksler.getEndeks(EndeksTipi.Gece).getValue().toString();
                t += Double.parseDouble(t3);
            }
            if(!endeksler.getEndeks(EndeksTipi.Enduktif).isEmpty())
                enduktif = endeksler.getEndeks(EndeksTipi.Enduktif).getValue().toString();

            if(!endeksler.getEndeks(EndeksTipi.Kapasitif).isEmpty())
                kapasitif = endeksler.getEndeks(EndeksTipi.Kapasitif).getValue().toString();

            if(!endeksler.getEndeks(EndeksTipi.Demand).isEmpty())
                demand = endeksler.getEndeks(EndeksTipi.Demand).getValue().toString();

            odf.set_takilan_t(String.format("%.3f", t));

            odf.set_takilan_t1(t1);
            odf.set_takilan_t2(t2);
            odf.set_takilan_t3(t3);
            odf.set_takilan_enduktif(enduktif);
            odf.set_takilan_kapasitif(kapasitif);
            odf.set_takilan_demand(demand);

            return odf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
