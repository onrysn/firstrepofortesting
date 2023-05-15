package mobit.elec.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.Globals;
import com.mobit.IChecked;
import com.mobit.IDetail;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.IMap;
import com.mobit.IMapMarker;
import com.mobit.Image;
import com.mobit.MobitException;
import com.mobit.utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import mobit.android.AndroidPlatform.IPopupCallback;
import mobit.android.ArrayAdapterEx;
import mobit.android.CheckPopupList;
import mobit.android.ViewHolderEx;
import mobit.eemr.ICallback;
import mobit.eemr.IProbeEvent;
import mobit.eemr.IReadResult;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.eemr.YaziciHataBildirimi;
import mobit.elec.AltEmirTuru;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.ITesisat;
import mobit.elec.IsemriFilterParam;
import mobit.elec.MsgInfo;
import mobit.elec.enums.IIslemDurum;
import mobit.elec.enums.IslemDurum;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.IMbsApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;


public class IsemriListeActivity extends AppCompatActivity implements IForm, OnClickListener, ICallback,
        PopupMenu.OnMenuItemClickListener, IPopupCallback, OnEditorActionListener {

    private Object sync = new Object();
    private IElecApplication app;
    private IMbsApplication appzabit;
    private ListView list;
    private volatile List<IIsemri> fullList = Collections.synchronizedList(new ArrayList<IIsemri>());
    private List<IIsemri> arrayList = Collections.synchronizedList(new ArrayList<IIsemri>());
    private ArrayAdapterEx<IIsemri> arrayAdapter;
    SayacZimmetBilgi medasServer;


    private TextView textIslemTipi;
    private TextView textIslemDurumu;
    private EditText editText;
    private TextView isemriAdet;
    private Button optikOku;
    Lun_Control Tesisat = new Lun_Control();
    private CheckPopupList islemTipi;
    private CheckPopupList islemDurum;

    private IReadResult result;
    private IMap map = null;

    private IIsemri isemri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isemri_liste);

        if (!(Globals.app instanceof IElecApplication)) {
            finish();
            return;
        }
        app = (IElecApplication) Globals.app;
        appzabit = (IMbsApplication) Globals.app;

        if (app instanceof IMap)
            map = (IMap) app;

        app.initForm(this);

        list = (ListView) findViewById(R.id.list);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        textIslemTipi = (TextView) findViewById(R.id.islemTipi);
        textIslemDurumu = (TextView) findViewById(R.id.islemDurumu);
        editText = (EditText) findViewById(R.id.editText);
        isemriAdet = (TextView) findViewById(R.id.isemriAdet);
        optikOku = (Button) findViewById(R.id.optikOku);
        editText.setOnEditorActionListener(this);
        optikOku.setOnClickListener(this);

        IChecked[] tipler = (IChecked[]) app.getEnumValues(IDef.ENUM_ISLEM_TIPI);
        tipler = utility.removeArrayItem(IChecked.class, tipler, IslemTipi.SayacOkuma);
        CheckPopupList.CheckList islemTipleri = new CheckPopupList.CheckList(tipler);
        CheckPopupList.CheckList.setCheck(islemTipleri, IslemTipi.Tumu, true);

        islemTipi = new CheckPopupList(textIslemTipi, 150, islemTipleri, this);
        textIslemTipi.setTag(islemTipleri);
        textIslemTipi.setText(textIslemTipi.getTag().toString());

        CheckPopupList.CheckList islemDurumlari =
                new CheckPopupList.CheckList((IChecked[]) app.getEnumValues(IDef.ENUM_ISLEM_DURUM));
        CheckPopupList.CheckList.setCheck(islemDurumlari, IslemDurum.Atanmis, true);

        islemDurum = new CheckPopupList(textIslemDurumu, 200, islemDurumlari, this);
        textIslemDurumu.setTag(islemDurumlari);
        textIslemDurumu.setText(textIslemDurumu.getTag().toString());
        medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
		/*
		IIslemDurum durum = IslemDurum.Tumu;// (!Globals.isDeveloping()) ?
											// IslemDurum.Atanmis :
											// IslemDurum.Tumu;
		textIslemDurumu.setTag(durum);
		textIslemDurumu.setText(textIslemDurumu.getTag().toString());
		*/

        arrayAdapter = new ArrayAdapterEx<IIsemri>(this, R.layout.row_isemri, arrayList, ViewHolder.class);
        list.setAdapter(arrayAdapter);

        loadList(false);

        // Activity durup yeniden başlarsa bir önceki durumun alınması
        isemri = app.getActiveIsemri();
        result = app.getOptikResult();
        Tesisat.setLuna_Control(0);


    }

    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(true);
        app.setPortCallback(this);
        super.onResume();

    }

    private void loadList(final boolean update) {

        app.runAsync(this, "Yükleniyor...", "", null, new Callback() {
                    public Object run(Object obj) {
                        try {

                            List<IIsemri> list = app.getIsemri(update);

					/*
					for(Iterator<IIsemri> iter = list.iterator(); iter.hasNext(); ){
						IIsemri isemri = iter.next();
						if(isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) iter.remove();
					}*/

                            synchronized (fullList) {
                                fullList.clear();
                                fullList.addAll(list);
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            app.ShowException(IsemriListeActivity.this, e);
                        }
                        return null;
                    }
                },
                new Callback() {
                    public Object run(Object obj) {
                        filterItems();
                        return null;
                    }
                }
        );

    }

    private void FullloadList(final boolean update) {

        app.runAsync(this, "Yükleniyor...", "", null, new Callback() {
                    public Object run(Object obj) {
                        try {

                            List<IIsemri> list = app.getFullIsemri(update);
					/*
					for(Iterator<IIsemri> iter = list.iterator(); iter.hasNext(); ){
						IIsemri isemri = iter.next();
						if(isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) iter.remove();
					}*/

                            synchronized (fullList) {
                                fullList.clear();
                                fullList.addAll(list);
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            app.ShowException(IsemriListeActivity.this, e);
                        }
                        return null;
                    }
                },
                new Callback() {
                    public Object run(Object obj) {
                        filterItems();
                        return null;
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.isemri_liste, menu);
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
        } else if (id == R.id.sirala) {

            sirala();

        } else if (id == R.id.guncelle) {

            loadList(true);

        } else if (id == R.id.hepsini_guncelle) {
            FullloadList(true);
        } else if (id == R.id.harita) {

            Harita(arrayList);
        } else if (id == R.id.gonder) {

            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_KUYRUK_LISTE));
            startActivityForResult(intent, IDef.FORM_KUYRUK_LISTE);
			/*
			****
			app.Gonder(this, fullList, new Callback() {
				@Override
				public Object run(Object obj) {
					if(!(obj instanceof Exception))loadList(false);
					return null;
				}
			});*/


        }
        return super.onOptionsItemSelected(item);
    }

    public class ViewHolder<T> extends ViewHolderEx<T> {

        private ImageView img;

        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

            super(adapter, view);

            View v;

            v = view.findViewById(R.id.radioButton);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.adres);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.durum);
            v.setOnClickListener(this);
            columnList.add(v);

            //Onur kesik Tesisatlarda Uyarı İçin Eklendi
            img = view.findViewById(R.id.kesikdurum);
            img.setOnClickListener(this);
            columnList.add(img);

        }

        @Override
        public void set(int position, T obj) {

            super.set(position, obj);
            img.setImageResource(R.drawable.eleclogo);
            IIsemri isemri = (IIsemri) item;

            if (isemri.getKESME_DUR().equals("SK") || isemri.getKESME_DUR().equals("TTK") || isemri.getKESME_DUR().equals("BTK") || isemri.getKESME_DUR().equals("ACK")
                    ||isemri.getKESME_DUR().equals("TCK")|| isemri.getKESME_DUR().equals("ASY")|| isemri.getKESME_DUR().equals("TNK")|| isemri.getKESME_DUR().equals("MYK")
                    ||isemri.getKESME_DUR().equals("SBK")|| isemri.getKESME_DUR().equals("KCK")|| isemri.getKESME_DUR().equals("FBK")){
                img.setImageResource(R.drawable.eleclogonot);
            }

            view.setBackgroundColor(app.getIslemRenk(isemri.getISLEM_TIPI()));

            // -------------------------------------------------------------------
            Button b;
            b = (Button) columnList.get(0);
            IIslemDurum islemDurum = isemri.getISEMRI_DURUMU();
            b.setEnabled(true);
            b.setText("Yap");
            if (islemDurum.equals(IslemDurum.Kuyrukta))
                b.setText("Gönder");
            else if (islemDurum.equals(IslemDurum.Tamamlandi) || islemDurum.equals(IslemDurum.Yapilamadi)) {
                if (!Globals.isDeveloping())
                    b.setEnabled(false);
            }

            // -------------------------------------------------------------------
            TextView tv;
            tv = (TextView) columnList.get(1);

            AltEmirTuru tur = null;
            for (AltEmirTuru t : app.getAltEmirTuru(isemri.getISLEM_TIPI())) {
                if (t.altEmirTuru == isemri.getALT_EMIR_TURU()) {
                    tur = t;
                    break;
                }
            }

            String stur;
            if (tur != null) stur = String.format("(%s)", tur.Tanim);
            else if (isemri.getALT_EMIR_TURU() != 0)
                stur = String.format("(%d)", isemri.getALT_EMIR_TURU());
            else stur = "";

            ISayacBilgi sb = isemri.getSAYACLAR().getSayac(SayacKodu.Aktif);
            String sayac_no = (sb != null) ? String.valueOf(sb.getSAYAC_NO()) : "yok";

            tv.setText(String.format("K: %d / T: %d / S: %s\n%s %s\n%s", isemri.getKARNE_NO(), isemri.getTESISAT_NO(), sayac_no,
                    isemri.getUNVAN(), stur, isemri.getADRES()));
            tv.setTag(isemri);

            // -------------------------------------------------------------------

            tv = (TextView) columnList.get(2);
            tv.setText(isemri.getISEMRI_DURUMU().toString());

            // -------------------------------------------------------------------

            columnList.get(0).setTag(isemri);

        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            try {

                IIsemri isemri = (IIsemri) arg0.getTag();
                int id = arg0.getId();

                if (id == view.getId()) {

                    isemri.getADRES();

                } else if (id == columnList.get(0).getId()) {

                    if (isemri.getISEMRI_DURUMU().equals(IslemDurum.Kuyrukta)) {
                        List<IIslemGrup> grupList = (List<IIslemGrup>) (List<?>) app.getIslem(isemri.getSAHA_ISEMRI_NO());
                        for (final IIslemGrup grup : grupList) {
                            app.sendIslem(IsemriListeActivity.this, grup, new Callback() {
                                @Override
                                public Object run(Object obj) {
                                    loadList(false);
                                    if (app.checkException(IsemriListeActivity.this, obj))
                                        return null;
                                    app.IslemTamamlandi(IsemriListeActivity.this, grup, false, true);
                                    return null;
                                }
                            }, 15000);
                        }

                    } else
                        Basla(isemri, result);

                } else if (id == columnList.get(1).getId()) {

                    app.setActiveIsemri(isemri);
                    PopupMenu popupMenu = new PopupMenu(adapter.getContext(), arg0);
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "İş Emri Detay");
                    popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Tekrar Yazdırma");
                    if (map != null && isemri instanceof IDetail)
                        popupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "İşlem Detay");
                    if (map != null && isemri instanceof IMapMarker)
                        popupMenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "Harita");
                    popupMenu.getMenu().add(Menu.NONE, 6, Menu.NONE, "İş Emri Al");
                    popupMenu.getMenu().add(Menu.NONE, 5, Menu.NONE, "İş Emri Bırak");
                    popupMenu.getMenu().add(Menu.NONE, 99, Menu.NONE, "Proje Dosyası İndir");
                    //Hüseyin Emre Çevik 12.04.2021
                    popupMenu.getMenu().add(Menu.NONE, 98, Menu.NONE, "Bağlantı Görüşü Evrakı");
                    popupMenu.getMenu().add(Menu.NONE, 100, Menu.NONE, "Ölçü Devre Al"); // H.Elif 12.01.2022
                    popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) adapter.getContext());
                    popupMenu.show();

                }

            } catch (Exception e) {
                app.ShowException(IsemriListeActivity.this, e);
            }
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        if (id == R.id.optikOku) {

            app.runAsync(this, "Optik porta bağlanıyor...", "", null, new Callback() {

                @Override
                public Object run(Object obj) {
                    // TODO Auto-generated method stub
                    try {
                        app.getMeterReader().Reconnect();
                        app.getMeterReader().Trigger();

                        OlcuDevreForm odf = new OlcuDevreForm(); //H.Elif 18.05.2022
                        odf.AllClear();
                    } catch (Exception e) {
                        return e;
                    }
                    return null;
                }

            }, new Callback() {

                @Override
                public Object run(Object obj) {
                    // TODO Auto-generated method stub
                    if (app.checkException(IsemriListeActivity.this, obj))
                        return null;
                    return null;
                }

            });
        }
    }

    @Override
    public void BeginRead(IReadResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ConnectionResetEvent(IProbeEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void EndRead() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Failed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Opened() {
        // TODO Auto-generated method stub

    }

    @Override
    public void PowerEvent(IProbeEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void Read(IProbeEvent arg0, final IReadResult arg1) {
        // TODO Auto-generated method stub

        arg1.set_okuma_zamani(app.getTime());
        int _sayac_no = Integer.parseInt(arg1.get_sayac_no());

        // Deneme
        //if(Globals.isDeveloping()) _sayac_no = 20156699;
        this.result = arg1;
        app.setOptikResult(result);
        //Muhammed Gokkaya
        //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        final int sayac_no = _sayac_no;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                editText.setText(Integer.toString(sayac_no));
                sayacIsemriBasla(sayac_no, arg1);
            }

        });

    }

    private void tesisatIsemriBasla(final int tesisat_no, final IReadResult r) {
        try {

            List<IIsemri> list = app.fetchTesisatIsemri(tesisat_no, null);
            if (list == null) return;

            IsemriFilterParam param = new IsemriFilterParam();
            param.tesisat_no = tesisat_no;
            for (Iterator<IIsemri> it = list.iterator(); it.hasNext(); ) {
                IIsemri isemri = it.next();
                param.isemri = isemri;
                if (!app.checkFilter(param))
                    it.remove();

            }
            app.Save(list);
            loadList(false);

            if (list.isEmpty()) {
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method
                        // stub
                        editText.setText("");
                    }
                });
                throw new MobitException("Bu tesisata ait iş emri bulunamadı!", "Uyarı");
            }

            Basla(list.get(0), r);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            app.ShowException(IsemriListeActivity.this, e);
            return;
        }

    }

    private void sayacIsemriBasla(final int sayac_no, final IReadResult r) {

        IIsemri isemri = null;
        int position = -1;
        synchronized (arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {

                isemri = arrayList.get(i);
                for (ISayacBilgi syc : isemri.getSAYACLAR().getSayaclar()) {
                    if (syc.getSAYAC_NO() == sayac_no) {
                        position = i;
                        break;
                    }
                }
                if (position > -1)
                    break;
            }
        }

        if (position < 0) {

            String msg = String.format(Globals.trLocale,
                    "%d nolu sayaca ait iş emri bulunamadı. " + "Sunucuda aratmak ister misiniz?", sayac_no);
            app.ShowMessage(this, msg, "Uyarı", DialogMode.YesNo, 1, 0, new IDialogCallback() {

                @Override
                public void DialogResponse(int msg_id, DialogResult result) {
                    // TODO Auto-generated method stub
                    if (result == DialogResult.Yes) {
                        List<ITesisat> list;
                        try {
                            list = app.fetchTesisat2(sayac_no);

                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            app.ShowException(IsemriListeActivity.this, e1);
                            return;
                        }

                        class Runnable2 implements Runnable {
                            private int tesisat_no;

                            public Runnable2(int tesisat_no) {
                                this.tesisat_no = tesisat_no;
                            }

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                tesisatIsemriBasla(tesisat_no, r);
                            }
                        }
                        ;

                        // Sayaçtan tesisat bulunması. aynı sayaç birden fazla
                        // tesisatta varsa
                        if (list.size() > 1) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(IsemriListeActivity.this);
                            builder.setTitle(app.findMessage(MsgInfo.TESISAT_SECIN));
                            final String[] strs = new String[list.size()];
                            for (int i = 0; i < list.size(); i++)
                                strs[i] = list.get(i).getADRES();
                            final List<ITesisat> _list = list;
                            builder.setItems(strs, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ITesisat t = _list.get(which);
                                    Runnable2 r = new Runnable2(t.getTESISAT_NO());
                                    r.run();
                                }
                            });
                            builder.show();
                            return;
                        } else {
                            Runnable2 r = new Runnable2(list.get(0).getTESISAT_NO());
                            r.run();
                            return;
                        }

                    }
                }

            });
            return;
        }


        try {

            if (!isemri.getISEMRI_DURUMU().YapilabilirMi()) {
                // Bu tesisata ait yapılabilir başka iş emrinin aranması
                synchronized (arrayList) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        IIsemri ii = arrayList.get(i);
                        if (ii.getTESISAT_NO() == isemri.getTESISAT_NO()) {
                            if (ii.getISEMRI_DURUMU().YapilabilirMi()) {
                                isemri = ii;
                                position = i;
                                break;
                            }
                        }
                    }
                }
            }

            list.setSelection(position);
            list.setSelector(android.R.color.holo_blue_light);

            Basla(isemri, r);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            app.ShowException(this, e);
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem arg0) {

        // TODO Auto-generated method stub

        try {

            final IIsemri isemri = app.getActiveIsemri();
            if (isemri == null)
                throw new MobitException("İşlem yapılacak iş emri seçimi yok");

            if (arg0.getItemId() == 1) {

                Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
                startActivityForResult(intent, IDef.FORM_ISEMRI_DETAY);
                return true;

            } else if (arg0.getItemId() == 2) {
                final YaziciHataBildirimi yhb = new YaziciHataBildirimi();
                app.runAsync(this, "Yazdırılıyor...", "", null, new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        try {
                            yhb.setTesisat_no(isemri.getTESISAT_NO());
                            yhb.setIsemri_no(isemri.getSAHA_ISEMRI_NO());
                            app.printIsemriIslem(isemri.getSAHA_ISEMRI_NO());
                            yhb.setHata_aciklama("Yazdirildi");
                            medasServer.YaziciKontrolService(isemri.getTESISAT_NO(), isemri.getSAHA_ISEMRI_NO(), 1, 0, "Yazdirildi");


                            OlcuDevreForm odf = new OlcuDevreForm();
                            if (odf != null && odf.getisemri_no() == isemri.getSAHA_ISEMRI_NO()) {
                                synchronized (sync) {
                                    app.printNewOdf(odf);
                                    sync.wait(5000);
                                    app.printNewOdf(odf);
                                }
                            }
                        } catch (Exception e) {
							/*
							try {
								medasServer.YaziciKontrolService(isemri.getTESISAT_NO(),isemri.getSAHA_ISEMRI_NO(),1,0,"Yazdirildi");
							}catch (Exception ex){

							}
							 */
                            return e;
                        }
                        return null;
                    }

                }, new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        if (app.checkException(IsemriListeActivity.this, obj))
                            return null;
                        return null;
                    }

                });

                return true;
            } else if (arg0.getItemId() == 3) {
                app.setObject(IDef.OBJ_ISLEM_RAPOR, isemri.getIslemRapor(app));
                Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISLEM_RAPOR));
                startActivityForResult(intent, IDef.FORM_ISLEM_RAPOR);
            } else if (arg0.getItemId() == 4) {

                List<IIsemri> list = new ArrayList<IIsemri>();
                list.add(isemri);
                Harita(list);

            } else if (arg0.getItemId() == 5) {

                try {
                    IIslemDurum durum = isemri.getISEMRI_DURUMU();
                    if (!(durum.equals(IslemDurum.Atanmis) || durum.equals(IslemDurum.Serbest)))
                        throw new MobitException("İş emri bırakılabilmesi için atanmış veya serbest olması gerekiyor!");
                    app.isemriBirak(isemri.getTESISAT_NO(), isemri.getSAHA_ISEMRI_NO());
					/*
					isemri.setISEMRI_DURUMU(IslemDurum.Serbest);
					if (isemri instanceof IDb)
						app.Save((IDb) isemri);
						*/
                    app.ShowMessage(this, "İş emri bırakıldı!", "");
                    loadList(false);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    app.ShowException(this, e);
                }
            } else if (arg0.getItemId() == 6) {

                try {

                    if (!isemri.getISEMRI_DURUMU().equals(IslemDurum.Serbest))
                        throw new MobitException("İş emri alınabilmesi için serbest olması gerekiyor");
                    List<IIsemri> list = app.fetchTesisatIsemri(isemri.getTESISAT_NO(), null);
                    if (list != null && !list.isEmpty()) {
                        app.Save(list);
                        app.ShowMessage(this, "İş emri alındı.", "");
                        loadList(false);
                    } else {
                        app.ShowMessage(this, "İş emri alınamadı!.", "");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    app.ShowException(this, e);
                }
            }
            //Hüseyin Emre Çevik 12.04.2021
            else if (arg0.getItemId() == 98) {
                //GetProjectFile asn_main = new GetProjectFile(19447798);
                GetProjectFile asn_main = new GetProjectFile(isemri.getSAHA_ISEMRI_NO(), true);
                asn_main.execute();

            } else if (arg0.getItemId() == 99) {
                //GetProjectFile asn_main = new GetProjectFile(19447798);
                GetProjectFile asn_main = new GetProjectFile(isemri.getSAHA_ISEMRI_NO(), false);
                asn_main.execute();

            } else if (arg0.getItemId() == 100) {
                OlcuDevreForm odf = new OlcuDevreForm();
                odf.AllClear();
                odf.set_disaridan_olcu_dur(1);
                String formType = "olcu_devre_akim_yok_form";
                if (odf.getcarpan() > 1) {
                    formType = "olcu_devre_akim_var_form";
                }
                odf.set_formType(formType);
                Intent intent = new Intent(this, app.getFormClass(IDef.FORM_OLCU_DEVRE));
                startActivityForResult(intent, IDef.FORM_OLCU_DEVRE);
//
                app.runAsync(this, "İşlem bekleniyor...", "", null, new Callback() {
                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        try {
                            app.IslemTamamlandi(IsemriListeActivity.this, null, true, true);
                        } catch (Exception e) {
                            return e;
                        }
                        return null;
                    }
                }, new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        if (app.checkException(IsemriListeActivity.this, obj))
                            return null;
                        return null;
                    }
                });

                return true;
            }

        } catch (Exception e) {

            app.ShowException(this, e);
        }

        return false;
    }


    @Override
    public void OnItemSelect(View anchorView, Object obj) {
        // TODO Auto-generated method stub
        if (anchorView.getId() == textIslemTipi.getId()) {
            textIslemTipi.setTag(obj);
            textIslemTipi.setText(obj.toString());
        } else if (anchorView.getId() == textIslemDurumu.getId()) {
            textIslemDurumu.setTag(obj);
            textIslemDurumu.setText(obj.toString());
        }

        filterItems();
    }

    private class FilterCallback implements Callback {

        private String filterText;
        private CheckPopupList.CheckList islemTipleri;
        private CheckPopupList.CheckList islemDurumlari;

        public FilterCallback(String filterText, CheckPopupList.CheckList islemTipleri, CheckPopupList.CheckList islemDurumlari) {
            this.filterText = filterText;
            this.islemTipleri = islemTipleri;
            this.islemDurumlari = islemDurumlari;
        }

        @Override
        public Object run(Object obj) {

            synchronized (fullList) {
                if (fullList.size() == 0)
                    return null;
            }

            int ifilter = -1;
            String[] sfilters = null;

            if (!filterText.isEmpty()) {
                try {
                    ifilter = Integer.parseInt(filterText);
                } catch (Exception e) {
                    sfilters = filterText.split(" ");
                }
            }

            synchronized (arrayList) {
                arrayList.clear();
            }

            synchronized (fullList) {
                for (IIsemri isemri : fullList) {

                    if (Thread.interrupted()) return new InterruptedException("İşlem iptal edildi");

                    boolean add = false;
                    boolean add1 = false;
                    boolean add2 = false;
                    for (IChecked islemTipi : (List<IChecked>) islemTipleri.getList()) {
                        if (!islemTipi.getCheck()) continue;
                        if (islemTipi.equals(IslemTipi.Tumu)) {
                            add1 = true;
                            break;
                        }
                        add1 |= (islemTipi.equals(isemri.getISLEM_TIPI()));
                    }

                    for (IChecked islemDurum : (List<IChecked>) islemDurumlari.getList()) {
                        if (!islemDurum.getCheck()) continue;
                        if (islemDurum.equals(IslemDurum.Tumu)) {
                            add2 = true;
                            break;
                        }
                        add2 |= (islemDurum.equals(isemri.getISEMRI_DURUMU()));
                    }

                    add = add1 & add2;

                    if (ifilter > -1) {
                        if (isemri.getKARNE_NO() == ifilter)
                            add = true;
                        else if (isemri.getTESISAT_NO() == ifilter)
                            add = true;
                        else if (isemri.getSAYACLAR().getSayac(ifilter) != null)
                            add = true;
                        else
                            add = false;
                    } else if (sfilters != null) {
                        add = false;
                        for (String ss : sfilters) {
                            ss = ss.trim().toUpperCase(Globals.locale);
                            if (isemri.getUNVAN().contains(ss))
                                add = true;
                            if (isemri.getADRES().contains(ss))
                                add = true;
                            if (add)
                                break;
                        }

                    }
                    if (add)
                        synchronized (arrayList) {
                            arrayList.add(isemri);
                        }
                }
            }

            SiralaCallback sirala = new SiralaCallback();
            sirala.msg = false;
            return sirala.run(null);

        }
    }

    ;

    private void filterItems() {

        String filterText = editText.getText().toString();
        CheckPopupList.CheckList islemTipleri = (CheckPopupList.CheckList) textIslemTipi.getTag();
        CheckPopupList.CheckList islemDurumlari = (CheckPopupList.CheckList) textIslemDurumu.getTag();
        FilterCallback filter = new FilterCallback(filterText, islemTipleri, islemDurumlari);

        app.runAsync(this, "Filtreleniyor...", "", null, filter, new Callback() {
            @Override
            public Object run(Object obj) {
                isemriAdet.setText(String.format("Adet : %d", arrayList.size()));
                arrayAdapter.notifyDataSetChanged();
                return null;
            }
        });
    }

    private class SiralaCallback implements Callback {

        public boolean msg = false;

        @Override
        public Object run(Object obj) {
            ILocation location;
            try {
                location = app.getLocation();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return e;
            }

            if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
                if (msg) app.ShowMessage(IsemriListeActivity.this, "Konum alınamadı!", "GPS");
                return null;
            }
            try {
                app.Sirala(arrayList, location);
            } catch (Exception e) {
                return e;
            }
            return null;
        }
    }

    ;

    private void sirala() {

        SiralaCallback sirala = new SiralaCallback();
        sirala.msg = true;
        app.runAsync(this, "Sıralanıyor...", "", null, sirala, new Callback() {
            @Override
            public Object run(Object obj) {
                app.checkException(IsemriListeActivity.this, obj);
                isemriAdet.setText(String.format("Adet : %d", arrayList.size()));
                arrayAdapter.notifyDataSetChanged();
                return null;
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
        // TODO Auto-generated method stub
        if (arg0.getId() == editText.getId()) {
            if (arg1 == EditorInfo.IME_ACTION_NEXT || arg1 == EditorInfo.IME_ACTION_DONE) {
                filterItems();
                return true;
            }
        }
        return false;
    }

    boolean yeniIsemriSorgulama(final IIsemri isemri) {

        if (!isemri.getISLEM_TIPI().equals(IslemTipi.Tespit))
            return false;

        //if((isemri.getISLEM_TIPI().equals(IslemTipi.Tespit) &&
        //		(isemri.getALT_EMIR_TURU()==5 || isemri.getALT_EMIR_TURU()==6006))
        //		&& Tesisat.KacakVarmi==0 && Tesisat.ZabitDur==1 )
        //{

        //}


		/*
		Tesisat.setKacakVarmi(0);
		Tesisat.setZabitMod(0);
		Tesisat.setZabitDur(0);

		 */

        app.runAsync(this, "Yeni iş emri sorgulanıyor...", "", null,
                new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        try {
                            return app.fetchTesisatIsemri(isemri.getTESISAT_NO(), null);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            return e;
                        }
                    }
                },
                new Callback() {

                    @Override
                    public Object run(Object obj) {
                        // TODO Auto-generated method stub
                        if (app.checkException(IsemriListeActivity.this, obj)) return null;
                        if (!(obj instanceof List<?>)) return null;
                        List<IIsemri> list = (List<IIsemri>) obj;
                        if (list.size() > 0) {
                            try {
                                app.Save(list);

                                tesisatIsemriBasla(isemri.getTESISAT_NO(), result);
							/*
							String s = String.format(Globals.trLocale, "%d nolu tesisat için %d adet iş emri alındı!",
									isemri.getTESISAT_NO(), list.size());
							app.ShowMessage(IsemriListeActivity.this, s, "");*/

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                app.ShowException(IsemriListeActivity.this, e);
                            }
                        }
                        return null;
                    }
                });


        return true;
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        int control = 0;

        if (arg0 == IDef.FORM_ISLEM_LISTE || arg0 == IDef.FORM_ZABIT) {
            editText.setText("");

            IIsemri isemri = app.getActiveIsemri();


            // Deneme için eklendi
            //if(Globals.isDeveloping()) arg1 = RESULT_OK;
            //Muhammed Gökkaya zabıt tutmak için geliştirilmiştir
            if (arg1 == RESULT_OK) {
                loadList(false);
                //yeniIsemriSorgulama(isemri);

                if ((String.valueOf(isemri.getISLEM_TIPI()).equals("Tespit") &&
                        (isemri.getALT_EMIR_TURU() == 5 || isemri.getALT_EMIR_TURU() == 6006 || isemri.getALT_EMIR_TURU() == 25 || isemri.getALT_EMIR_TURU() == 19 || isemri.getALT_EMIR_TURU() == 25))
                        && Tesisat.KacakVarmi == 1 && Tesisat.ZabitDur == 1) {
                    control = 1;
                    Class<?> clazz = appzabit.getFormClass(IDef.FORM_ZABIT);
                    if (clazz != null) {
                        Intent intent = new Intent(this, clazz);
                        intent.putExtra("MODE", Tesisat.ZabitMod);
                        startActivityForResult(intent, IDef.FORM_ZABIT);
                        //finish();
                    }
                } else {
                    control = 0;
                    loadList(false);
                    yeniIsemriSorgulama(isemri);
                }

            } else {
                filterItems();
            }
            if (control == 0) {
                app.setActiveIsemri(null);
                app.setActiveIslem(null);
            }

        } else if (arg0 == IDef.FORM_MAP) {
            if (arg1 == RESULT_OK) {
                List<IMapMarker> list = map.getSelectedMarkerList();
                try {

                    Basla((IIsemri) list.get(0), null);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    app.ShowException(this, e);
                }
            }
        } else if (arg0 == IDef.FORM_KUYRUK_LISTE) {
            loadList(false);
        }
    }

    Runnable r;


    private void Basla(final IIsemri iisemri, final IReadResult result) throws Exception {

        IIslemDurum durum = iisemri.getISEMRI_DURUMU();
        if (durum.equals(IslemDurum.Tamamlandi))
            if (!Globals.isDeveloping())
                throw new MobitException("İş emri tamamlanmış!");
            else if (durum.equals(IslemDurum.Iptal))
                if (!Globals.isDeveloping())
                    throw new MobitException("İş emri iptal edilmiş!");

        StringBuilder sb = new StringBuilder();
        final List<IIsemri> isemriList = new ArrayList<IIsemri>();

        Thread.sleep(50); // bu satır olmazsa fullList boş olarak görünebiliyor.
        // sorunun nedeni belli değil
        synchronized (fullList) {
            for (IIsemri ii : fullList) {
                durum = ii.getISEMRI_DURUMU();
                if (iisemri.getTESISAT_NO() == ii.getTESISAT_NO()
                        && (durum.equals(IslemDurum.Atanmis) || durum.equals(IslemDurum.Serbest))) {
                    isemriList.add(ii);
                }
            }
        }

        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Class<?> clazz = app.getFormClass(IDef.FORM_ISLEM_LISTE);
                if (clazz != null) {

                    try {
                        app.setActiveIsemri(isemri);

                        IIslemGrup islemGrup = (IIslemGrup) isemri.newIslem();
                        if (result != null) {
                            IIsemriIslem iislem = (IIsemriIslem) islemGrup.getIslem(IIsemriIslem.class).get(0);
                            ISayaclar sayaclar = iislem.getSAYACLAR();
                            ISayacBilgi sayac = sayaclar.getSayac(Integer.parseInt(result.get_sayac_no()));
                            // Farklı sayaça ait optik verisi kullanımının engellenmesi
                            if (sayac != null) sayac.setOptikResult(result);
                        }
                        app.setActiveIslem(islemGrup);

                        Intent intent = new Intent(IsemriListeActivity.this, clazz);
                        IsemriListeActivity.this.startActivityForResult(intent, IDef.FORM_ISLEM_LISTE);
                    } catch (Exception e) {
                        app.ShowException(IsemriListeActivity.this, e);
                    }
                }
            }

        };

        OlcuDevreForm odf = new OlcuDevreForm(); //H.Elif 22.06.2022
        odf.AllClear();

        if (result != null) {
            if (isemriList.size() > 1) {
                IsemriSecimi(isemriList);
            } else {
                isemri = iisemri;
                r.run();
            }
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this); // H.Elif
            dialog.setTitle("Uyarı!");
            dialog.setMessage("Lütfen Optik Port ile İşlem Yapınız!");
            dialog.setPositiveButton("Devam Et", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    IsemriSecimi(isemriList);
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();
        }
    }

    private void IsemriSecimi(final List<IIsemri> isemriList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // alt_bld.setIcon(R.drawable.icon);
        builder.setTitle("Birden fazla iş emri var. Birini seçin.");
        String[] slist = new String[isemriList.size()];
        for (int i = 0; i < isemriList.size(); i++) {
            IIsemri isemri = isemriList.get(i);
            slist[i] = (isemri instanceof IDetail) ? ((IDetail) isemri).getAciklama()
                    : isemri.getISLEM_TIPI().toString();
        }

        builder.setSingleChoiceItems(slist, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                isemri = isemriList.get(item);
                dialog.dismiss();
                r.run();
            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();

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

        Class<?> cls = app.getFormClass(IDef.FORM_MAP);
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, IDef.FORM_MAP);


    }

    class GetProjectFile extends AsyncTask<Void, Void, Void> {


        StringBuilder sb;
        String file = "";
        boolean _pdf_mi = false;
        int _isemri_no;
        int hata = 100;
        String message = "";
        String result = "";
        private ProgressDialog progressDialog = new ProgressDialog(IsemriListeActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Dosya Sorgulanıyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

        }

        public GetProjectFile(int isemri_no, boolean pdf_mi) {
            super();
            _isemri_no = isemri_no;
            _pdf_mi = pdf_mi;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://meramedas.net/dppowerdosya/api/dosya?id=" + _isemri_no);


            try {
                // Add your data


                HttpResponse httpResponse = httpclient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                JSONObject jsonObj = new JSONObject(sb.toString());
                result = (String) jsonObj.get("Result");
                //kalan_hak=(Integer) jsonObj.get("miktar");
                if (result.equals("OK")) {

                    if (_pdf_mi) {
                        file = (String) jsonObj.get("MusadeData");//PDF dosyası
                    } else {
                        file = (String) jsonObj.get("Data");//dwg dosyası
                    }

                    hata = 100;
                    message = null;
                } else {
                    hata = 0;
                    message = (String) jsonObj.get("Message");
                }


                is.close();

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (hata == 100) {
                progressDialog.dismiss();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(file));
                startActivity(browserIntent);
            } else {
                progressDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(IsemriListeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Dosya Bulunamadı")
                        .setMessage(message)
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

}
