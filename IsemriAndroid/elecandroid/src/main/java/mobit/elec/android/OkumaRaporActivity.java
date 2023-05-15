package mobit.elec.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.MobitException;

import java.util.ArrayList;
import java.util.List;

import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.android.utility;

import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IOkumaRapor;

public class OkumaRaporActivity extends AppCompatActivity implements IForm, TabHost.OnTabChangeListener,
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public static final String KEY_KARNE_NO = "KARNE_NO";
    public static final String KEY_TESISAT_NO = "TESISAT_NO";

    private static final String normalOkunanlar = "normalOkunanlar";
    private static final String hicOkunmayanlar = "hicOkunmayanlar";
    private static final String kuyruktaBekleyenler = "kuyruktaBekleyenler";
    private static final String uyariliOkunanlar = "uyariliOkunanlar";
    private static final String hataliOkunanlar = "hataliOkunanlar";


    private IElecApplication app;

    private EditText karneNo;
    private Button raporla;

    private TabHost tabHost;
    private TabWidget tw;

    private ListView normalListe;
    private ListView okunmayanListe;
    private ListView kuyrukListe;
    private ListView uyariliListe;
    private ListView hataliListe;

    private TextView normalListeAdet;
    private TextView okunmayanListeAdet;
    private TextView kuyrukListeAdet;
    private TextView uyariliListeAdet;
    private TextView hataliListeAdet;

    private List<IIsemri> normalListeArray = new ArrayList<IIsemri>();
    private List<IIsemri> okunmayanListeArray = new ArrayList<IIsemri>();
    private List<IIsemri> kuyrukListeArray = new ArrayList<IIsemri>();
    private List<IIsemri> uyariliListeArray = new ArrayList<IIsemri>();
    private List<IIsemri> hataliListeArray = new ArrayList<IIsemri>();

    private ArrayAdapterEx<IIsemri> normalListeAdapter;
    private ArrayAdapterEx<IIsemri> okunmayanListeAdapter;
    private ArrayAdapterEx<IIsemri> kuyrukListeAdapter;
    private ArrayAdapterEx<IIsemri> uyariliListeAdapter;
    private ArrayAdapterEx<IIsemri> hataliListeAdapter;

    IIsemri prevIsemri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okuma_rapor);

        if(!(Globals.app instanceof IElecApplication)){
            // Desteklenmiyor
            finish();
            return;
        }

        app = (IElecApplication)Globals.app;

        prevIsemri = app.getActiveIsemri();

        app.initForm(this);

        karneNo = (EditText) findViewById(R.id.karneNo);
        raporla = (Button) findViewById(R.id.raporla);
        tabHost = (TabHost)findViewById(R.id.tabHost);
        normalListe = (ListView)findViewById(R.id.normalListe);
        okunmayanListe = (ListView)findViewById(R.id.okunmayanlListe);
        kuyrukListe = (ListView)findViewById(R.id.kuyruklListe);
        uyariliListe = (ListView)findViewById(R.id.uyariliListe);
        hataliListe = (ListView)findViewById(R.id.hataliListe);
        normalListeAdet = (TextView) findViewById(R.id.normalListeAdet);
        okunmayanListeAdet = (TextView) findViewById(R.id.okunmayanlListeAdet);
        kuyrukListeAdet = (TextView) findViewById(R.id.kuyruklListeAdet);
        uyariliListeAdet = (TextView) findViewById(R.id.uyariliListeAdet);
        hataliListeAdet = (TextView) findViewById(R.id.hataliListeAdet);

        raporla.setOnClickListener(this);

        tabHost.setup();

        utility.setNewTab(tabHost, normalOkunanlar, "Normal Okunanlar", android.R.drawable.star_on, R.id.normalOkunanlar);
        utility.setNewTab(tabHost, hicOkunmayanlar, "Okunmayanlar", android.R.drawable.star_on, R.id.hicOkunmayanlar);
        utility.setNewTab(tabHost, kuyruktaBekleyenler, "Kuyruk", android.R.drawable.star_on, R.id.kuyruktaBekleyenler);
        utility.setNewTab(tabHost, uyariliOkunanlar, "Uyarılı Okunanlar", android.R.drawable.star_on, R.id.uyariliOkunanlar);
        utility.setNewTab(tabHost, hataliOkunanlar, "Hatali Okunanlar", android.R.drawable.star_on, R.id.hataliOkunanlar);


        tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        utility.setTabTextSize(tw, mobit.android.utility.sp2px(this, 12));

        tabHost.setOnTabChangedListener(this);

        normalListeAdapter = new ArrayAdapterEx<IIsemri>(this, R.layout.row_okuma_rapor, normalListeArray, ViewHolder.class);
        okunmayanListeAdapter = new ArrayAdapterEx<IIsemri>(this, R.layout.row_okuma_rapor, okunmayanListeArray, ViewHolder.class);
        kuyrukListeAdapter = new ArrayAdapterEx<IIsemri>(this, R.layout.row_okuma_rapor, kuyrukListeArray, ViewHolder.class);
        uyariliListeAdapter = new ArrayAdapterEx<IIsemri>(this, R.layout.row_okuma_rapor, uyariliListeArray, ViewHolder.class);
        hataliListeAdapter = new ArrayAdapterEx<IIsemri>(this, R.layout.row_okuma_rapor, hataliListeArray, ViewHolder.class);

        normalListe.setAdapter(normalListeAdapter);
        okunmayanListe.setAdapter(okunmayanListeAdapter);
        kuyrukListe.setAdapter(kuyrukListeAdapter);
        uyariliListe.setAdapter(uyariliListeAdapter);
        hataliListe.setAdapter(hataliListeAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            karneNo.setText(Integer.toString(bundle.getInt(KEY_KARNE_NO)));
            raporla.performClick();
        }
        catch (Exception e){

        }

    }

    @Override
    protected void onDestroy()
    {
        if(app != null){
            app.setActiveIsemri(prevIsemri);
        }
        super.onDestroy();
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
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IDef.FORM_ISEMRI_DETAY) {
            // Önceki aktif iş emri kaydının tutulması
            app.setActiveIsemri(prevIsemri);
        }
    }

    @Override
    public void onTabChanged(String tag)
    {
        switch (tag){
            case normalOkunanlar:
                break;
            case uyariliOkunanlar:
                break;

        }
    }

    @Override
    public void onClick(View arg)
    {
        if(arg.getId() == raporla.getId()){
            try {
                String s = karneNo.getText().toString();
                if (s.isEmpty()) throw new MobitException("Karne numarasını girin");
                Callback2 raporla = new Callback2(Integer.parseInt(s));
                app.runAsync(this, "Raporlanıyor...", "", null, raporla, new Callback() {
                    @Override
                    public Object run(Object obj) {

                        normalListeAdapter.notifyDataSetChanged();
                        okunmayanListeAdapter.notifyDataSetChanged();
                        kuyrukListeAdapter.notifyDataSetChanged();
                        uyariliListeAdapter.notifyDataSetChanged();
                        hataliListeAdapter.notifyDataSetChanged();

                        normalListeAdet.setText(Integer.toString(normalListeArray.size()));
                        okunmayanListeAdet.setText(Integer.toString(okunmayanListeArray.size()));
                        kuyrukListeAdet.setText(Integer.toString(kuyrukListeArray.size()));
                        uyariliListeAdet.setText(Integer.toString(uyariliListeArray.size()));
                        hataliListeAdet.setText(Integer.toString(hataliListeArray.size()));

                        return null;
                    }
                });

            }
            catch (Exception e){
                app.ShowException(this, e);
            }
        }
    }

    private class Callback2 implements Callback
    {
        private int karne_no;

        public Callback2(int karne_no){
            this.karne_no = karne_no;
        }

        public Object run(Object obj) {

            try {

                IOkumaRapor rapor = app.getOkumaRaporu(karne_no);

                normalListeArray.clear();
                okunmayanListeArray.clear();
                kuyrukListeArray.clear();
                uyariliListeArray.clear();
                hataliListeArray.clear();

                normalListeArray.addAll(rapor.getOkunanlar());
                okunmayanListeArray.addAll(rapor.getOkunmayanlar());
                kuyrukListeArray.addAll(rapor.getKuyruktaOlanlar());
                uyariliListeArray.addAll(rapor.getUyariliOkunanlar());
                hataliListeArray.addAll(rapor.getHataliOkunanlar());


            } catch (Exception e) {
                app.ShowException(OkumaRaporActivity.this, e);
            }
            return null;
        }
    };

    public class ViewHolder<T> extends ViewHolderEx<T> {

        Button button;
        TextView aciklama;

        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

            super(adapter, view);

            button = (Button)view.findViewById(R.id.button);
            button.setOnClickListener(this);
            columnList.add(button);

            aciklama = (TextView) view.findViewById(R.id.aciklama);
            aciklama.setOnClickListener(this);
            columnList.add(aciklama);

        }


        @Override
        public void set(int position, T obj) {

            super.set(position, obj);

            button.setTag(obj);
            aciklama.setTag(obj);
            IIsemri isemri = (IIsemri)obj;
            String s = String.format("%s - %s\n%s", isemri.getTESISAT_NO(), isemri.getUNVAN(), isemri.getADRES());
            aciklama.setText(s);

        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if(arg0.getId() == button.getId()){
                Intent intent = new Intent();
                IIsemri isemri = (IIsemri) arg0.getTag();
                intent.putExtra(KEY_TESISAT_NO, isemri.getTESISAT_NO());
                setResult(RESULT_OK, intent);
                finish();
            }
            else if(arg0.getId() == aciklama.getId()){
                IIsemri isemri = (IIsemri) arg0.getTag();
                app.setActiveIsemri(isemri);
                PopupMenu popupMenu = new PopupMenu(adapter.getContext(), arg0);
                popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Tesisat Detay");
                popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) adapter.getContext());
                popupMenu.show();
            }

        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem arg0) {

        // TODO Auto-generated method stub

        try {

            IIsemri isemri = app.getActiveIsemri();
            if (isemri == null)
                throw new MobitException("Tesisat seçimi yapın!");

            if (arg0.getItemId() == 1) {
                Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
                startActivityForResult(intent, IDef.FORM_ISEMRI_DETAY);
                return true;

            }
        } catch (Exception e) {
            app.ShowException(this, e);
        }
        return false;
    }
}
