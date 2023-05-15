package mobit.elec.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDb;
import com.mobit.IDetail;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.IRecordStatus;
import com.mobit.IslemGrup;
import com.mobit.IslemMaster;
import com.mobit.MobitException;
import com.mobit.RecordStatus;
import com.mobit.ServerException;
import com.mobit.Yazici;
//import com.zebra.sdk.util.fileConversion.internal.AsciiDecoderStream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mobit.android.ArrayAdapterEx;
import mobit.android.ProgressDialog2;
import mobit.android.ViewHolderEx;
import mobit.eemr.IReadResult;
import mobit.eemr.Lun_Control;
import mobit.eemr.SeletIsemriCevap;
import mobit.elec.AltEmirTuru;
import mobit.elec.Endeks;
import mobit.elec.IAtarif;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IMuhurSokme;
import mobit.elec.ITesisatMuhur;
import mobit.elec.Sayaclar;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IIslemDurum;
import mobit.elec.enums.IslemDurum;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IIslemMaster;
import mobit.elec.mbs.IslemMaster2;
import mobit.elec.mbs.IslemRapor;
import mobit.elec.mbs.MbsApplication;
import mobit.elec.mbs.MbsException;
import mobit.elec.mbs.get.Cbs;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.get.result;
import mobit.elec.mbs.medas.IslemMaster3;
import mobit.elec.mbs.server.IslemGrup2;

import static mobit.elec.mbs.IDef.ERR;
import static mobit.elec.mbs.IDef.PRN;
import static mobit.elec.mbs.IslemMaster2.selectFromDurum;
import android.location.Location;

public class SendingListActivity extends AppCompatActivity implements IForm, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    IElecApplication app;

    private ListView list;
    private Button gonder;
    private ArrayAdapterEx<IIslemGrup> arrayAdapter;
    private List<IIslemGrup> arrayList;
    private List<IIslemGrup> arrayList2;
    private IIslemGrup grup;
    public int tekil_kontrol=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_list);
        //muhammed gökkaya
        if (!(Globals.app instanceof IElecApplication)) {
            // Desteklenmiyor
            finish();
            return;
        }
        /*
        app = (IElecApplication) Globals.app;

        app.initForm(this);

        list = (ListView) findViewById(R.id.list);
        gonder = (Button)findViewById(R.id.gonder);

        gonder.setOnClickListener(this);

        arrayList = new ArrayList<IIslemGrup>();
        try {

            IIslem islem;
            IIsemri isemri;

            arrayList = (List<IIslemGrup>)(List<?>)app.getIslem(RecordStatus.Saved);
            //arrayList = app.getIslem(RecordStatus.Saved);//IslemGrup2.select(app, RecordStatus.Saved);

        }
        catch (Exception e){

        }

        arrayAdapter = new ArrayAdapterEx<IIslemGrup>(this, R.layout.row_isemri, arrayList, ViewHolder.class);
        list.setAdapter(arrayAdapter);
         */
        app = (IElecApplication) Globals.app;

        app.initForm(this);

        list = (ListView) findViewById(R.id.list);
        gonder = (Button)findViewById(R.id.gonder);

        gonder.setOnClickListener(this);

        arrayList = new ArrayList<IIslemGrup>();
        arrayList2 = new ArrayList<IIslemGrup>();
        try {
            arrayList = (List<IIslemGrup>)(List<?>)app.getIslem(RecordStatus.Saved);
            arrayList2 = (List<IIslemGrup>)(List<?>)app.getIslem(RecordStatus.Sent);
            for (IIslemGrup arr : this.arrayList2) {
                IIslemMaster master = (IIslemMaster) arr.getIslem().get(0);
                if (master.getRESULT_CODE() != 0 && !master.getRESULT_TYPE().equals(PRN)) {
                    arrayList.add(arr);
                }
            }
        } catch (Exception e) {
        }
        arrayAdapter = new ArrayAdapterEx<IIslemGrup>(this, R.layout.row_isemri, arrayList, ViewHolder.class);

        list.setAdapter(arrayAdapter);



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

            //Onur İşlem Listesinde Uyarı İçin Eklendi
            img = view.findViewById(R.id.kesikdurum);
            img.setOnClickListener(this);
            columnList.add(img);

        }



        @Override
        public void set(int position, T obj) {

            super.set(position, obj);
            img.setImageResource(R.drawable.medaslogo);
            IIslemGrup grup = (IIslemGrup) item;

            IIslemMaster master = (IIslemMaster) grup.getIslem().get(0);
            //Onur Okuma Yap'da İşlem Listesinde İşlem Durumunu Belirtmek İçin Eklendi(Düzenlenecek)
            if (master.getRESULT_CODE() != 0 && master.getRESULT_TYPE().equals(ERR)) {
                img.setImageResource(R.drawable.medaslogo);
            }

            str(grup);
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if(arg0.getId() == columnList.get(0).getId()) {
                ILocation location = null;
                try {
                    location = app.getLocation();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
                //Yazıcı bağlantısı açıksa kapat
                com.mobit.Yazici yazici=new Yazici();
                if ( yazici.bluetooth_kontrol!=0)
                    yazici.baglanti_kapat();

                if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
                    tekil_kontrol=0;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SendingListActivity.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Konum Bilgis");
                    dialog.setMessage("Konum bilgisi alınmadı! Yine de devam etmek ister misin?");
                    dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            //app.Gonder(this, arrayList, clb);
                            tekil_kontrol=1;
                        }
                    }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Action for "Cancel".
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                    if (tekil_kontrol==1){
                        //HÜSEYİN EMRE ÇEVİK 29.04.2021 TOPLU GÖNDERME EKRANINDA TEKLİ GÖNDERİRKEN ANLIK KONUM ALMA
                        grup = (IIslemGrup) arg0.getTag();
                        try {
                            ((IslemMaster3) grup.getIslem(IslemMaster3.class).get(0)).setCBS(new Cbs(app.getLocation()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        send(grup, true,0);
                        tekil_kontrol=0;
                    }
                } else {
                        //HÜSEYİN EMRE ÇEVİK 29.04.2021 TOPLU GÖNDERME EKRANINDA TEKLİ GÖNDERİRKEN ANLIK KONUM ALMA
                    grup = (IIslemGrup) arg0.getTag();
                    try {
                        ((IslemMaster3) grup.getIslem(IslemMaster3.class).get(0)).setCBS(new Cbs(app.getLocation()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    send(grup, true,0);

                }

            }
            else {
                grup = (IIslemGrup) arg0.getTag();
                PopupMenu popupMenu = new PopupMenu(adapter.getContext(), arg0);
                popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "İşlem Detay");
                popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Tekrar İncele");
                popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) adapter.getContext());
                popupMenu.show();
            }

        }

        private void str(IIslemGrup grup)
        {
            String s = "";
            try {

                TextView tv1, tv2;

                // Son işlemi alma
                String sonuc_ = "";
                IIslemMaster master = null;
                if(grup.getIslem().size() > 1) {
                    for (IIslem i : grup.getIslem()) {
                        IIslemMaster m = (IIslemMaster) i;
                        if (m.getIslem() instanceof IIsemriIslem) {
                            master = m;
                            break;
                        }
                    }
                }
                else {
                    master = (IIslemMaster)grup.getIslem().get(0);
                    sonuc_ = master.getRESULT();

                }

                IIslem islem = master.getIslem();
                IRecordStatus status = master.getDurum();
                view.setBackgroundColor(status.getStatusColor());
                Button b;
                b = (Button) columnList.get(0);
                /* muhammed
                b.setEnabled(status.equals(RecordStatus.Saved));
                */
                b.setText("Gönder");
                b.setTag(grup);
                tv2 = (TextView) columnList.get(2);
                tv1 = (TextView) columnList.get(1);

                if (islem instanceof IIsemriIslem) {
                    IIsemriIslem iislem = (IIsemriIslem) islem;
                    IIsemri isemri = iislem.getIsemri();

                    String sonuc = "";
                    if(islem instanceof ICommand) {
                        ICommand cmd = (ICommand) islem;
                        result rs = cmd.getResult();
                        //String sonuc2 = (!rs.isPrintable()) ? rs.getRACIKLAMA() : "OK";
                        //sonuc="OK";
                        /*
                        if (sonuc.equals("OK") || sonuc.equals("Ok") || sonuc.equals("İşlem Tamamlandı")){
                            b.setEnabled(status.equals(RecordStatus.Saved));
                            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                         */
                        /*
                        C2791result rs = ((ICommand) islem).getResult();

                         */
                        String sonuc2 = !rs.isPrintable() ? rs.getRACIKLAMA() : "OK";
                        if (sonuc2.equals("") && sonuc_ != null) {
                            sonuc2 = sonuc_;

                        }
                        if (!sonuc2.equals("OK") && !sonuc2.equals("Ok")) {
                            if (!sonuc2.equals("İşlem Tamamlandı")) {
                                sonuc2.equals("");
                                sonuc = sonuc2;
                            }
                        }
                        if (sonuc2.equals("OK")){
                            b.setEnabled(status.equals(RecordStatus.Saved));
                            this.view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }


                        sonuc = sonuc2;

                    }

                    String tesisatNoAciklama = "Tesisat No";//iislem.getISLEM_TIPI() == IslemTipi.SayacOkuma ? "Tesisat No" : "İş Emri No";
                    s = String.format("%s\n%s: %d\n%s\n%s",
                            iislem.getISLEM_TIPI().toString(),
                            tesisatNoAciklama,
                            iislem.getSAHA_ISEMRI_NO(),
                            Globals.dateTimeFmt.format(master.getZaman()),
                            sonuc);

                    tv1.setTag(grup);
                    tv1.setText(s);

                    tv2.setTag(grup);
                    tv2.setText("");



                }
                else if(islem instanceof IMuhurSokme){
                    IMuhurSokme ms = (IMuhurSokme)islem;
                    s = String.format("%s\nTesisat No: %d\n%s\n%s",
                            "Mühür Sökme",
                            ms.getTESISAT_NO(),
                            Globals.dateTimeFmt.format(master.getZaman()),
                            (master.getRESULT() != null) ? master.getRESULT() : "");
                    tv1.setText(s);
                    tv1.setTag(grup);
                    tv2.setText("");
                    tv2.setTag(grup);
                }
                else if(islem instanceof ITesisatMuhur){
                    ITesisatMuhur tm = (ITesisatMuhur)islem;
                    s = String.format("%s\nTesisat No: %d\n%s\n%s",
                            "Mühürleme",
                            tm.getTESISAT_NO(),
                            Globals.dateTimeFmt.format(master.getZaman()),
                            (master.getRESULT() != null) ? master.getRESULT() : "");
                    tv1.setText(s);
                    tv1.setTag(grup);
                    tv2.setText("");
                    tv2.setTag(grup);
                }
                else if(islem instanceof IAtarif){
                    IAtarif at = (IAtarif)islem;
                    s = String.format("%s\nTesisat No: %d\n%s\n%s",
                            "Adres Tarif",
                            at.getTESISAT_NO(),
                            Globals.dateTimeFmt.format(master.getZaman()),
                            (master.getRESULT() != null) ? master.getRESULT() : "");
                    tv1.setText(at.toString());
                    tv1.setTag(grup);
                    tv2.setText("");
                    tv2.setTag(grup);
                }
                else if(islem instanceof IDb){
                    IDb db = (IDb)islem;
                    switch (db.getTabloId()){
                        case 15:
                            tv1.setText(islem.toString());
                            tv2.setText("");
                            break;
                        default:
                            tv1.setText(islem.toString());
                            tv2.setText("");
                            break;
                    }
                    tv1.setTag(grup);
                    tv2.setTag(grup);
                }
                else if(islem != null){
                    tv1.setText(islem.toString());
                    tv1.setTag(grup);
                    tv2.setText("");
                    tv2.setTag(grup);
                }
                else {
                    tv1.setText(islem.toString());
                    tv1.setTag(grup);
                    tv2.setText("");
                    tv2.setTag(grup);
                    b.setEnabled(false);
                    b.setText("");
                }


            }
            catch (Exception e) {

            }

        }

    }

    private boolean send(final IIslemGrup grup, final boolean msg,int timeout)
    {
        try {
            IIslemMaster master = null;
            master = (IIslemMaster)grup.getIslem().get(0);
            IIslem islem = master.getIslem();
            //IIsemriIslem iislem = (IIsemriIslem) islem;
            SeletIsemriCevap isemriCevap = new SeletIsemriCevap();
            isemriCevap.conn=app.getConnection();
            final int finalTimeout = timeout;
            timeout+=15000;//HÜSEYİN EMRE ÇEVİK (Toplu Gönderide Çok Popup açılma sorunu çözüldü)

            app.sendIslem(SendingListActivity.this, grup, new Callback() {
                @Override
                public Object run(Object obj) {
                    // TODO Auto-generated method stub
                    arrayAdapter.notifyDataSetChanged();
                    if(app.checkException(SendingListActivity.this, obj, false)) return null;
                    app.IslemTamamlandiToplu(SendingListActivity.this, grup, false, false, finalTimeout);

                    if (finalTimeout==2){

                        AlertDialog.Builder dialog = new AlertDialog.Builder(SendingListActivity.this);
                        dialog.setCancelable(false);
                        dialog.setTitle("Tamamlandı");
                        dialog.setMessage("İşlem Tamamlandı");
                        dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }

                    return null;
                }
            }, timeout);
        }
        catch (Exception e){
            //muhammed gökkaya toplu okumada patlıyor burası exception fırlamıyor
            //app.ShowException(SendingListActivity.this, e);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View var1) {

        if (var1.getId() == R.id.gonder) {
            com.mobit.Yazici yazici=new Yazici();
           if ( yazici.bluetooth_kontrol!=0)
               yazici.baglanti_kapat();

            ILocation location = null;
            try {
                location = app.getLocation();
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }

                if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(SendingListActivity.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Konum Bilgisi");
                    dialog.setMessage("Konum bilgisi alınmadı! Yine de devam etmek ister misin?");
                    dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int timeoutplus = 0, sayac = 1;
                            //app.Gonder(this, arrayList, clb);


                            for (IIslemGrup grup : arrayList) {

                                if (arrayList.size() == sayac) {
                                    timeoutplus = 2;//işlem tamamlandı
                                }
                                if (!send(grup, false, timeoutplus)) {

                                    break;
                                }
                                timeoutplus = 1;
                                sayac++;
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


                    //HÜSEYİN EMRE ÇEVİK (Toplu Gönderide Çok Popup açılma sorunu çözüldü)
                    int timeoutplus = 0, sayac = 1;

                    for (IIslemGrup grup : arrayList) {

                        if (arrayList.size() == sayac) {
                            timeoutplus = 2;//işlem tamamlandı
                        }
                        if (!send(grup, false, timeoutplus)) {
                            break;
                        }
                        timeoutplus = 1;
                        sayac++;
                    }


                }


        }
    }

        @Override
        public boolean onMenuItemClick(MenuItem arg0) {

        // TODO Auto-generated method stub
        //arg0.getActionView().setTag(grup);
        if (arg0.getItemId() == 1) {

            IslemRapor rapor = new IslemRapor(app, 0, (List<IIslemMaster>)(List<?>)grup.getIslem());
            app.setObject(IDef.OBJ_ISLEM_RAPOR, rapor);
            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISLEM_RAPOR));
            startActivityForResult(intent, IDef.FORM_ISLEM_RAPOR);
            return true;

        }
        if (arg0.getItemId() == 2)
        {

            IIslemMaster master = null;
            master = (IIslemMaster)grup.getIslem().get(grup.getIslem().size()-1);
            Lun_Control end=new Lun_Control();
            String[] endler=new String[8];
            IIslemGrup islemGrup=null;
            IIsemri t=null;
            IIslem grp=null;
            try{
                IIslem islem = master.getIslem();
                IIsemriIslem iislem = (IIsemriIslem) islem;

                t=((IIsemriIslem) islem).getIsemri();

                endler[0]= iislem.getSAYACLAR().getEndeksler().getEndeks(EndeksTipi.Gunduz).toString();
                endler[1]= iislem.getSAYACLAR().getEndeksler().getEndeks(EndeksTipi.Puant).toString();
                endler[2]= iislem.getSAYACLAR().getEndeksler().getEndeks(EndeksTipi.Gece).toString();
                endler[3]= iislem.getSAYACLAR().getEndeksler().getEndeks(EndeksTipi.Enduktif).toString();
                endler[4]= iislem.getSAYACLAR().getEndeksler().getEndeks(EndeksTipi.Kapasitif).toString();
                endler[5]= iislem.getSAYACLAR().getEndeksler().getEndeks(EndeksTipi.Demand).toString();;
                endler[6]= master.getIsemriNo().toString();
                endler[7]= iislem.getOPTIK_DATA();
                if (endler[0].equals("0") && endler[1].equals("")
                        && endler[2].equals("") && endler[4].equals("") &&
                        endler[5].equals(""))
                {
                    for (int i=0;i<endler.length-2;i++){
                        endler[i]="";
                    }
                }
                //iislem=(IIsemriIslem)grup.getIslem(IIsemriIslem.class).get(0);
                end.setEndeksler(endler);
                app.setActiveIslem(grup.getIslem().get(grup.getIslem().size()-1));

            }catch (Exception e){
            }
            try {
                if (master.getIslemTipi()==100) {
                    app.setActiveIslem(null);
                    Intent intent = new Intent(this, app.getFormClass(IDef.FORM_OKUMA_ENDEKS));
                    intent.putExtra("TesisatNo", master.getIsemriNo().toString());
                    startActivityForResult(intent, IDef.FORM_OKUMA_ENDEKS);
                    return true;
                }
                else {

                    Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_ENDEKS));
                    intent.putExtra("KuyrukKontrol", "1");
                    startActivityForResult(intent, IDef.FORM_ISEMRI_ENDEKS);
                    return true;
                }
            }finally {
                finish();
            }
        }
        return false;
    }
}
