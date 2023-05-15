package mobit.elec.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mobit.BarcodeResult;
import com.mobit.Globals;
import com.mobit.IBarcode;
import com.mobit.IForm;
import com.mobit.MobitException;

import java.util.ArrayList;
import java.util.List;

import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.elec.IElecApplication;
import mobit.elec.IMuhur;
import mobit.elec.mbs.get.SeriNo;

public class MuhurSorguActivity extends AppCompatActivity implements IForm, View.OnClickListener {

    IElecApplication app = null;

    EditText editTesisatNo;
    EditText editKarneNo;
    EditText editMuhurSeri;
    EditText editMuhurNo;
    ListView list;
    Button barkod;
    Button sorgu;

    IBarcode barcode;

    List<IMuhur> arrayList = new ArrayList<IMuhur>();
    private ArrayAdapterEx<IMuhur> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muhur_sorgu);

        if(!(Globals.app instanceof IElecApplication)){
            finish();
            return;
        }
        app = (IElecApplication)Globals.app;
        app.initForm(this);

        editTesisatNo = (EditText)findViewById(R.id.editTesisatNo);
        editKarneNo = (EditText)findViewById(R.id.editKarneNo);
        editMuhurSeri = (EditText)findViewById(R.id.editMuhurSeri);
        editMuhurNo = (EditText)findViewById(R.id.editMuhurNo);
        list = (ListView)findViewById(R.id.list);
        barkod = (Button)findViewById(R.id.barkod);
        sorgu = (Button)findViewById(R.id.sorgu);

        barkod.setOnClickListener(this);
        sorgu.setOnClickListener(this);

        arrayAdapter = new ArrayAdapterEx<IMuhur>(this, R.layout.row_muhur, arrayList, ViewHolder.class);
        list.setAdapter(arrayAdapter);

        barcode = app.newBarcodeObject();

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
    public void onClick(View v){

        if(v.getId() == R.id.barkod){

            barcode.startScanner(this);
        }
        else if(v.getId() == R.id.sorgu){
            try {

                String karne_no = editKarneNo.getText().toString();
                String tesisat_no = editTesisatNo.getText().toString();
                String muhur_seri = editMuhurSeri.getText().toString();
                String muhur_no = editMuhurNo.getText().toString();
                int i = 0;
                if (!karne_no.isEmpty()) i++;
                if (!tesisat_no.isEmpty()) i++;
                if (!muhur_no.isEmpty()) i++;

                if (i != 1) throw new MobitException("Karne, tesisat ve mühür/seri alanlarından sadece bir tanesine girmeniz gerekiyor!");

                arrayList.clear();
                if(!karne_no.isEmpty()) arrayList.addAll(app.fetchKarneMuhur(Integer.parseInt(karne_no)));
                else if(!tesisat_no.isEmpty()) arrayList.addAll(app.fetchTesisatMuhur(Integer.parseInt(tesisat_no)));
                else if(!muhur_no.isEmpty()) arrayList.addAll(app.fetchMuhur(new SeriNo(muhur_seri, muhur_no)));
                arrayAdapter.notifyDataSetChanged();

            }
            catch (Exception e){
                app.ShowException(this, e);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        BarcodeResult result = barcode.getScanResult(requestCode, resultCode, data);
        if(result.resultStatus == BarcodeResult.SUCCESS){
            View view = getCurrentFocus();
            if(view instanceof EditText){
                EditText edit = (EditText)view;
                edit.setText(result.resultData);
            }
        }
    }

    public class ViewHolder<T> extends ViewHolderEx<T> {

        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

            super(adapter, view);

            View v;

            v = view.findViewById(R.id.row);
            v.setOnClickListener(this);
            columnList.add(v);

        }

        @Override
        public void set(int position, T obj) {

            super.set(position, obj);
            IMuhur muhur = (IMuhur)obj;

            String s = String.format("Tesisat No: %d  Seri/No: %s\nYer: %s  Tarih: %s\nNeden :%s  Durum: %s",
                    muhur.getTESISAT_NO(), muhur.getSERI_NO(), muhur.getMUHUR_YERI2().getMUHUR_ACIKLAMA(),
                    Globals.dateFmt.format(muhur.getMUHUR_TARIHI()),
                    muhur.getMUHUR_NEDENI2().getMUHUR_ACIKLAMA(),
                    muhur.getMUHUR_DUR2().getMUHUR_ACIKLAMA());


            ((TextView)columnList.get(0)).setText(s);
        }

        @Override
        public void onClick(View v){

        }

    }
}
