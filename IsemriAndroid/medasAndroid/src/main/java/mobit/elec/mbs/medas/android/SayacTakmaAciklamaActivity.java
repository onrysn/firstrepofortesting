package mobit.elec.mbs.medas.android;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;

import mobit.eemr.OlcuDevreForm;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.MsgInfo;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.medas.IMedasApplication;

public class SayacTakmaAciklamaActivity extends AppCompatActivity
        implements IForm {
    // H.Elif
    IMedasApplication app;
    protected mobit.elec.medas.ws.SayacZimmetBilgi medasServer;
    IIsemri isemri;
    IIsemriIslem isemriIslem;

    Button back_btn;
    Button save_btn;

    Spinner listAciklamalar;
    EditText editAciklama;

    String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(Globals.app instanceof IMedasApplication)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }

        app = (IMedasApplication) Globals.app;
        medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);

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

        setContentView(R.layout.activity_sayac_takma_aciklama);


        FormInitParam param = app.getFormInitParam();
        param.captionText = islemTipi.toString();
        app.initForm(this, param);

        back_btn = (Button) findViewById(R.id.back_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        listAciklamalar = (Spinner) findViewById(R.id.listAciklamalar);
        editAciklama = (EditText) findViewById(R.id.editAciklama);

        try {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sokulen_muhur_list, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listAciklamalar.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        listAciklamalar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.LTGRAY);

                if (position != 0) {
                    Object item = adapterView.getItemAtPosition(position);
                    if (item != null) {
                        String s = editAciklama.getText().toString();
                        if (s.length() == 0) {
                            editAciklama.setText(item.toString());
                            return;
                        }
                        editAciklama.setText(String.format("%s\n%s", s, item.toString()));
                    }
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcuDevreForm odf = new OlcuDevreForm();
                String sayac_takma_nedeni = odf.get_aciklama();
                odf.set_aciklama(editAciklama.getText().toString() + sayac_takma_nedeni); // sayac değişimindeki açıklama sayfasınden gelen veri için eklendi
                SayacTakmaAciklamaActivity.this.setResult(RESULT_OK);
                SayacTakmaAciklamaActivity.this.finish();


            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
