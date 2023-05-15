package mobit.elec.android;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mobit.BarcodeResult;
import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.MobitException;

import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.IMuhurKod;
import mobit.elec.ITesisatMuhur;
import mobit.elec.mbs.enums.MuhurKodCins;
import mobit.elec.mbs.get.SeriNo;
import mobit.elec.mbs.put.put_tesisat_muhur;
import mobit.elec.mbs.server.IslemGrup2;

public class MuhurTakmaActivity extends AppCompatActivity implements IForm, OnClickListener {

    IElecApplication app = null;

    EditText editTesisatNo;
    EditText editMuhurSeri;
    EditText editMuhurNo;
    ListView listMuhurNeden;
    ListView listMuhurYer;
    Button buttonTamam;
    Button barkod;

    List<IMuhurKod> arrayMuhurNeden;
    ArrayAdapter<IMuhurKod> adapterMuhurNeden;

    List<IMuhurKod> arrayMuhurYer;
    ArrayAdapter<IMuhurKod> adapterMuhurYer;

    ITesisatMuhur muhurTakma;
    IBarcode barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muhur_takma);

        if (!(Globals.app instanceof IElecApplication)) {
            finish();
            return;
        }

        app = (IElecApplication) Globals.app;

        IIslem islem = app.getActiveIslem();
        if (islem != null) {
            if (!(islem instanceof IIslemGrup)) {
                finish();
                return;
            }
            IIslemGrup grup = (IIslemGrup) islem;
            List<IIslem> islemler = grup.getIslem(ITesisatMuhur.class);
            if (islemler.size() == 0) {
                muhurTakma = (ITesisatMuhur) islemler.get(0);
            }
        }


        app.initForm(this);

        editTesisatNo = (EditText) findViewById(R.id.editTesisatNo);
        editMuhurSeri = (EditText) findViewById(R.id.editMuhurSeri);
        editMuhurNo = (EditText) findViewById(R.id.editMuhurNo);
        listMuhurNeden = (ListView) findViewById(R.id.listMuhurNeden);
        listMuhurYer = (ListView) findViewById(R.id.listMuhurYer);
        buttonTamam = (Button) findViewById(R.id.buttonTamam);
        barkod = (Button) findViewById(R.id.barkod);

        buttonTamam.setOnClickListener(this);
        barkod.setOnClickListener(this);

        List<IMuhurKod> list = null;
        try {
            list = app.getMuhurKod(0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            app.ShowException(this, e);
            finish();
            return;
        }
        if (list != null) {
            arrayMuhurNeden = new ArrayList<IMuhurKod>();
            for (IMuhurKod mk : list)
                if (mk.getKOD_CINSI() == MuhurKodCins.Neden)
                    arrayMuhurNeden.add(mk);
            adapterMuhurNeden = new ArrayAdapterEx<IMuhurKod>(this, R.layout.row_with_radio, arrayMuhurNeden,
                    ViewHolder.class);
            listMuhurNeden.setAdapter(adapterMuhurNeden);

            arrayMuhurYer = new ArrayList<IMuhurKod>();
            for (IMuhurKod mk : list)
                if (mk.getKOD_CINSI() == MuhurKodCins.Yer)
                    arrayMuhurYer.add(mk);
            adapterMuhurYer = new ArrayAdapterEx<IMuhurKod>(this, R.layout.row_with_radio, arrayMuhurYer,
                    ViewHolder.class);
            listMuhurYer.setAdapter(adapterMuhurYer);
        }

        if (islem instanceof IIslemGrup) {
            IIslemGrup grup = (IIslemGrup) islem;
            List<IIslem> islemler = grup.getIslem(IIsemriIslem.class);
            if (islemler.size() > 0) {
                IIsemriIslem iislem = (IIsemriIslem) islemler.get(0);
                editTesisatNo.setText(Integer.toString(iislem.getTESISAT_NO()));
                editTesisatNo.setFocusableInTouchMode(false);
                editTesisatNo.setFocusable(false);
            }
        }

        barcode = app.newBarcodeObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.muhur_takma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public class ViewHolder<T> extends ViewHolderEx<T> {

        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

            super(adapter, view);

            View v;

            v = view.findViewById(R.id.rowTextView);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.radioButton);
            v.setOnClickListener(this);
            columnList.add(v);

        }

        @Override
        public void set(int position, T obj) {

            super.set(position, obj);

            IMuhurKod mkod = (IMuhurKod) item;

            TextView tv;
            tv = (TextView) columnList.get(0);
            tv.setText(mkod.getMUHUR_ACIKLAMA());
            tv.setTag(mkod);
            RadioButton rb;
            rb = (RadioButton) columnList.get(1);
            rb.setTag(mkod);
            rb.setChecked(mkod.getCheck());

        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            int id = arg0.getId();
            if (id == columnList.get(1).getId()) {
                IMuhurKod mk;
                for (T i : adapter.getListe()) {
                    mk = (IMuhurKod) i;
                    if (mk.getCheck()) {
                        mk.setCheck(false);
                        break;
                    }
                }
                mk = (IMuhurKod) arg0.getTag();
                mk.setCheck(true);
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.buttonTamam) {

            try {

                int tesisat_no = Integer.parseInt(editTesisatNo.getText().toString());
                List<IIsemri2> list;
                try {
                    if (tesisat_no > 0)
                        list = (List<IIsemri2>) (List<? extends IIsemri>) app.fetchTesisatIsemri(tesisat_no, null);
                    // Bölgeye yetkisi var mı kontrolü yoksa catch a düşüp yetki yok diyor H.Elif

                    if (editTesisatNo.getText().length() == 0)
                        throw new MobitException("Tesisat numarasını giriniz!");

                    IMuhurKod muhurNeden = null;
                    IMuhurKod muhurYer = null;
                    for (IMuhurKod mk : arrayMuhurNeden)
                        if (mk.getCheck()) {
                            muhurNeden = mk;
                            break;
                        }

                    for (IMuhurKod mk : arrayMuhurYer)
                        if (mk.getCheck()) {
                            muhurYer = mk;
                            break;
                        }


                    try {

                        ITesisatMuhur mt = new put_tesisat_muhur(Integer.parseInt(editTesisatNo.getText().toString()),
                                new SeriNo(editMuhurSeri.getText().toString(), editMuhurNo.getText().toString()), muhurYer,
                                muhurNeden);

                        IIslemGrup islemGrup = new IslemGrup2();
                        islemGrup.add(mt);
                        islemGrup.setIslemTipi(IDef.MUHURLEME_ISLEMI);
                        IIslem islem = app.saveIslem(islemGrup);

                        app.sendIslem(this, islem, new Callback() {
                            @Override
                            public Object run(Object obj) {
                                if (app.checkException(MuhurTakmaActivity.this, obj)) return obj;
                                app.ShowMessage(MuhurTakmaActivity.this, "Mühürleme yapıldı", "");
                                setResult(RESULT_OK, new Intent());
                                return null;
                            }
                        }, 10000);

                    } catch (Exception e) {
                        app.ShowException(MuhurTakmaActivity.this, e);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    app.ShowException(this, e);
                }
            } catch (Exception e) {
                app.ShowException(this, e);
            }
        } else if (arg0.getId() == R.id.barkod) {

            barcode.startScanner(this);
        }
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
}
