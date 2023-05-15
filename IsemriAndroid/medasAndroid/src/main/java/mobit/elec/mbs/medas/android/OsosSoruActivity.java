package mobit.elec.mbs.medas.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mobit.eemr.OsosSoruForm;

public class OsosSoruActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<SpinnerCevap> SorunKaynakP = new ArrayList<>();
    ArrayList<SpinnerCevap> SorunKaynak = new ArrayList<>();
    ArrayList<SpinnerCevap> YapilanIslemOto = new ArrayList<>();
    ArrayList<SpinnerCevap> YapilanIslemOto2 = new ArrayList<>();
    ArrayList<SpinnerCevap> YeniModem = new ArrayList<>();
    ArrayList<SpinnerCevap> EskiModem = new ArrayList<>();
    ArrayList<SpinnerCevap> YeniAnten = new ArrayList<>();
    ArrayList<SpinnerCevap> EskiAnten = new ArrayList<>();
    ArrayList<SpinnerCevap> YeniSim = new ArrayList<>();
    ArrayList<SpinnerCevap> EskiSim = new ArrayList<>();



    ArrayAdapter adp;
    ArrayList<String> spinnerText=new ArrayList<>();

    Spinner sorunKaynakSpinner,sorunKaynakSpinner2,YapilanIslemOtoSpinner,YapilanIslemOtoSpinner2,YeniModemSpinner,EskiModemSpinner,YeniSimSpinner,EskiSimSpinner,YeniAntenSpinner,EskiAntenSpinner;
    EditText YeniModemSeriEditText,EskiModemSeriEditText;
    Button ososGonderButton;
    LinearLayout sorunKaynaklayout2,YapilanIslemOtoLayout,sorunKaynakLayout,eskiModemLayout,eskiAntenModemLayout,eskiSimLayout,YapilanIslemLayout;

    String alt_emir_turu;
    SpinnerRequest spinnerRequest ;
    OsosSoruForm osf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osos_soru);

        osf=new OsosSoruForm();
        Intent intent = getIntent();

        osf.AboneNo = intent.getStringExtra("AboneNo");
        osf.Order_Number = intent.getIntExtra("Order_Number",0);
        osf.Ihbar_Tarihi = intent.getStringExtra("Ihbar_Tarihi");
        alt_emir_turu=intent.getStringExtra("alt_emir_turu");

//        setTitle("OSOS Soruları");
        setTitle(Html.fromHtml("<font color=\"#FFFFFF\">OSOS Soruları</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
//        setTitleColor(Color.parseColor("#ffffff"));
        setTitleColor(getResources().getColor(R.color.white));

        sorunKaynaklayout2=findViewById(R.id.sorunKaynaklayout2);
        YapilanIslemOtoLayout=findViewById(R.id.YapilanIslemOtoLayout);

        sorunKaynakSpinner=findViewById(R.id.sorunKaynakSpinner);
        sorunKaynakSpinner2=findViewById(R.id.sorunKaynakSpinner2);
        YapilanIslemOtoSpinner=findViewById(R.id.YapilanIslemOtoSpinner);
        YeniModemSpinner=findViewById(R.id.YeniModemSpinner);
        EskiModemSpinner=findViewById(R.id.EskiModemSpinner);
        EskiModemSeriEditText=findViewById(R.id.EskiModemSeriEditText);
        YeniModemSeriEditText=findViewById(R.id.YeniModemSeriEditText);
        YeniSimSpinner=findViewById(R.id.YeniSimSpinner);
        EskiSimSpinner=findViewById(R.id.EskiSimSpinner);
        YeniAntenSpinner=findViewById(R.id.YeniAntenSpinner);
        EskiAntenSpinner=findViewById(R.id.EskiAntenSpinner);
        YapilanIslemOtoSpinner2=findViewById(R.id.YapilanIslemOtoSpinner2);

        sorunKaynakLayout=findViewById(R.id.sorunKaynakLayout);
        YapilanIslemLayout=findViewById(R.id.YapilanIslemLayout);
        eskiModemLayout=findViewById(R.id.eskiModemLayout);
        eskiAntenModemLayout=findViewById(R.id.eskiAntenModemLayout);
        eskiSimLayout=findViewById(R.id.eskiSimLayout);


        ososGonderButton=findViewById(R.id.ososGonderButton);
        ososGonderButton.setOnClickListener(this);

        if (alt_emir_turu.equals("10") || alt_emir_turu.equals("37") || alt_emir_turu.equals("38") || alt_emir_turu.equals("39") || alt_emir_turu.equals("40")){
            sorunKaynakLayout.setVisibility(View.GONE);
            YapilanIslemLayout.setVisibility(View.GONE);
            eskiModemLayout.setVisibility(View.GONE);
            eskiAntenModemLayout.setVisibility(View.GONE);
            eskiSimLayout.setVisibility(View.GONE);
        }

        SorunKaynakinit();
        YapilanIslemOtoinit();
        YeniModeminit();
        EskiModeminit();
        YeniSiminit();
        EskiSiminit();
        YeniAnteninit();
        EskiAnteninit();

        spinnerRequest = new SpinnerRequest();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
     super.onBackPressed();

    }

    // -----------------------------------------------------------------------------------------------------------------------------
//    SorunKaynak
    private void SorunKaynakinit(){
        SorunKaynakilkDoldurma();
        sorunKaynakSpinner.setSelection(0, false);
        sorunKaynakSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (DescriptionListAl(SorunKaynak,SorunKaynakP.get(position).Code).size()!=0){
                        adp=new ArrayAdapter(OsosSoruActivity.this,R.layout.support_simple_spinner_dropdown_item,DescriptionListAl(SorunKaynak,SorunKaynakP.get(position).Code));
                        sorunKaynaklayout2.setVisibility(View.VISIBLE);
                        sorunKaynakSpinner2.setAdapter(adp);
                    }else {
                        sorunKaynaklayout2.setVisibility(View.GONE);
                    }


                spinnerRequest.sorunKaynakSecilen=SorunKaynakP.get(position).Code;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sorunKaynakSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                spinnerRequest.sorunKaynakSecilen2=position+1;
                spinnerRequest.sorunKaynakSecilen2=String.valueOf(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void SorunKaynakilkDoldurma(){

        ArrayList<String> KaynakText=new ArrayList<>();
        SorunKaynakP.add(new SpinnerCevap(null,"0","Seçiniz"));
        SorunKaynakP.add(new SpinnerCevap(null,"ckm","Çekim Yok"));
        SorunKaynakP.add(new SpinnerCevap(null,"hrmn","Harmonik Problem"));
        SorunKaynakP.add(new SpinnerCevap(null,"mdm","Modem Sorunu"));
        SorunKaynakP.add(new SpinnerCevap(null,"mntj","Montaj Hatası"));
        SorunKaynakP.add(new SpinnerCevap(null,"syc","Sayaç Sorunu"));
        SorunKaynakP.add(new SpinnerCevap(null,"yok","Enerji Yok / Kullanım Yok"));


        SorunKaynak.add(new SpinnerCevap("ckm","ckm1","Çekim Yok"));
        SorunKaynak.add(new SpinnerCevap("ckm","ckm2","Sim Kart Yok"));
        SorunKaynak.add(new SpinnerCevap("ckm","ckm3","Yüksek Kazançlı Anten Var"));
        SorunKaynak.add(new SpinnerCevap("ckm","ckm4","Yüksek Kazançlı Anten Denendi"));
        SorunKaynak.add(new SpinnerCevap("ckm","ckm5","Yüksek Kazançlı Anten Denenmedi"));
        SorunKaynak.add(new SpinnerCevap("ckm","ckm6","Modem Anteni Var"));
        SorunKaynak.add(new SpinnerCevap("ckm","ckm7","Anten Yok"));

        SorunKaynak.add(new SpinnerCevap("mdm","mdm1","Modem Yok"));
        SorunKaynak.add(new SpinnerCevap("mdm","mdm2","Modem Yanık"));
        SorunKaynak.add(new SpinnerCevap("mdm","mdm3","Modem Arızalı"));
        SorunKaynak.add(new SpinnerCevap("mdm","mdm4","Modem Sayacı Okuyamıyor"));

        SorunKaynak.add(new SpinnerCevap("mntj","mntj1","Modem Güç Bağlantı Hatası"));
        SorunKaynak.add(new SpinnerCevap("mntj","mntj2","Modem A-B Bağlantı Hatası"));
        SorunKaynak.add(new SpinnerCevap("mntj","mntj3","Modem Sim Kart-Anten Bağlantı Hatası"));
        SorunKaynak.add(new SpinnerCevap("mntj","mntj4","Sayaç Bağlantısı Yanlış"));
        SorunKaynak.add(new SpinnerCevap("mntj","mntj5","Anten Metal Yüzeye mi Monte Edilmiş"));

        SorunKaynak.add(new SpinnerCevap("syc","syc1","Sayaç Modemle Okunamıyor"));
        SorunKaynak.add(new SpinnerCevap("syc","syc2","Sayaç Arızalı"));
        SorunKaynak.add(new SpinnerCevap("syc","syc3","Sayaç Yok"));

        for (SpinnerCevap item:SorunKaynakP) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        sorunKaynakSpinner.setAdapter(adp);

    }
//------------------------------------------------------------------------------------------------------------------------------
//    YapilanIslemOto
    private void YapilanIslemOtoinit(){

        YapilanIslemOtoilkDoldurma();
        YapilanIslemOtoSpinner.setSelection(0, false);
        YapilanIslemOtoSpinner.setSelection(0,false);
        YapilanIslemOtoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (DescriptionListAl(YapilanIslemOto2,YapilanIslemOto.get(position).Code).size()!=0){
                    adp=new ArrayAdapter(OsosSoruActivity.this,R.layout.support_simple_spinner_dropdown_item,DescriptionListAl(YapilanIslemOto2,YapilanIslemOto.get(position).Code));
                    YapilanIslemOtoLayout.setVisibility(View.VISIBLE);
                    YapilanIslemOtoSpinner2.setAdapter(adp);
                }else {
                    YapilanIslemOtoLayout.setVisibility(View.GONE);
                }

                spinnerRequest.YapilanIslemOtoSecilen=YapilanIslemOto.get(position).Code;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        YapilanIslemOtoSpinner2.setSelection(0, false);
        YapilanIslemOtoSpinner2.setSelection(0,false);
        YapilanIslemOtoSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spinnerRequest.YapilanIslemOtoSecilen2Code=YapilanIslemOto2.get(position).Code;
              if (YapilanIslemOto2.get(position).Aux1!=null){
                  spinnerRequest.YapilanIslemOtoSecilen2Aux1=YapilanIslemOto2.get(position).Aux1;
              }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void YapilanIslemOtoilkDoldurma(){

        ArrayList<String> KaynakText=new ArrayList<>();
        YapilanIslemOto.add(new SpinnerCevap(null,"0","Seçiniz"));
        YapilanIslemOto.add(new SpinnerCevap(null,"bgln","Bağlantı Değişimi"));
        YapilanIslemOto.add(new SpinnerCevap(null,"mlzm","Malzeme Değişim"));
        YapilanIslemOto.add(new SpinnerCevap(null,"rst","Modem Reset"));

        YapilanIslemOto2.add(new SpinnerCevap("mlzm","antd","Anten Değişim","ANT"));
        YapilanIslemOto2.add(new SpinnerCevap("mlzm","mdmd","Modem Değişim","MDM"));
        YapilanIslemOto2.add(new SpinnerCevap("mlzm","mdmsd","Modem + Sim Kart Değişimi","MDMSIM"));
        YapilanIslemOto2.add(new SpinnerCevap("mlzm","mssycd","Modem+Sim+Sayaç","MDMSIM"));
        YapilanIslemOto2.add(new SpinnerCevap("mlzm","smk","Sim Kart Değişim","SIM"));
        YapilanIslemOto2.add(new SpinnerCevap("mlzm","sycd","Sayaç Değişim"));

        for (SpinnerCevap item:YapilanIslemOto) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        YapilanIslemOtoSpinner.setAdapter(adp);

    }
//------------------------------------------------------------------------------------------------------------------------------
//    YeniModem
    private void YeniModeminit(){

        YeniModemilkDoldurma();
        YeniModemSpinner.setSelection(0, false);
        YeniModemSpinner.setSelection(0,false);
        YeniModemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//            spinnerRequest.YeniModemSecilen=position+1;
            spinnerRequest.YeniModemSecilen=Integer.parseInt(YeniModem.get(position).Code);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


}
    private void YeniModemilkDoldurma(){

        ArrayList<String> KaynakText=new ArrayList<>();
        YeniModem.add(new SpinnerCevap(null,"0","Seçiniz"));
        YeniModem.add(new SpinnerCevap(null,"401","TBI Modem"));
        YeniModem.add(new SpinnerCevap(null,"402","Elektromed Modem"));
        YeniModem.add(new SpinnerCevap(null,"403","Sistemetriks Modem"));
        YeniModem.add(new SpinnerCevap(null,"404","Eclipse Modem"));
        YeniModem.add(new SpinnerCevap(null,"405","Desimal Modem"));
        YeniModem.add(new SpinnerCevap(null,"406","Baylan Modem"));
        YeniModem.add(new SpinnerCevap(null,"407","Luna Modem"));
        YeniModem.add(new SpinnerCevap(null,"408","Diğer Modem"));


        for (SpinnerCevap item:YeniModem) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        YeniModemSpinner.setAdapter(adp);
    }
//------------------------------------------------------------------------------------------------------------------------------
//    EskiModem
    private void EskiModeminit(){

        EskiModemilkDoldurma();
        EskiModemSpinner.setSelection(0, false);
        EskiModemSpinner.setSelection(0,false);
        EskiModemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//            spinnerRequest.EskiModemSecilen=position+1;
            spinnerRequest.EskiModemSecilen=Integer.parseInt(EskiModem.get(position).Code);


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


}
    private void EskiModemilkDoldurma(){

        ArrayList<String> KaynakText=new ArrayList<>();
        EskiModem.add(new SpinnerCevap(null,"0","Seçiniz"));
        EskiModem.add(new SpinnerCevap(null,"301","TBI Modem"));
        EskiModem.add(new SpinnerCevap(null,"302","Elektromed Modem"));
        EskiModem.add(new SpinnerCevap(null,"303","Sistemetriks Modem"));
        EskiModem.add(new SpinnerCevap(null,"304","Eclipse Modem"));
        EskiModem.add(new SpinnerCevap(null,"305","Desimal Modem"));
        EskiModem.add(new SpinnerCevap(null,"306","Baylan Modem"));
        EskiModem.add(new SpinnerCevap(null,"307","Luna Modem"));
        EskiModem.add(new SpinnerCevap(null,"308","Diğer Modem"));
        EskiModem.add(new SpinnerCevap(null,"309","Modem Yok"));


        for (SpinnerCevap item:EskiModem) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        EskiModemSpinner.setAdapter(adp);
    }
//------------------------------------------------------------------------------------------------------------------------------
//    YeniSim
    private void YeniSiminit(){

        YeniSimilkDoldurma();
        YeniSimSpinner.setSelection(0, false);
        YeniSimSpinner.setSelection(0,false);
        YeniSimSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            spinnerRequest.YeniSimSecilen=Integer.parseInt(YeniSim.get(position).Code);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


}
    private void YeniSimilkDoldurma(){

        ArrayList<String> KaynakText=new ArrayList<>();
        YeniSim.add(new SpinnerCevap(null,"0","Seçiniz"));
        YeniSim.add(new SpinnerCevap(null,"601","Turkcell Sim Kart"));
        YeniSim.add(new SpinnerCevap(null,"602","Vodafone Sim Kart"));
        YeniSim.add(new SpinnerCevap(null,"603","Türk Telekom Sim Kart"));

        for (SpinnerCevap item:YeniSim) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        YeniSimSpinner.setAdapter(adp);
    }
//------------------------------------------------------------------------------------------------------------------------------
//    EskiSim
    private void EskiSiminit(){

    EskiSimilkDoldurma();
    EskiSimSpinner.setSelection(0, false);
    EskiSimSpinner.setSelection(0,false);
    EskiSimSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//            spinnerRequest.EskiSimSecilen=position+1;
            spinnerRequest.EskiSimSecilen=Integer.parseInt(EskiSim.get(position).Code);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


}
    private void EskiSimilkDoldurma(){

        ArrayList<String> KaynakText=new ArrayList<>();
        EskiSim.add(new SpinnerCevap(null,"0","Seçiniz"));
        EskiSim.add(new SpinnerCevap(null,"501","Turkcell Sim Kart"));
        EskiSim.add(new SpinnerCevap(null,"502","Vodafone Sim Kart"));
        EskiSim.add(new SpinnerCevap(null,"503","Türk Telekom Sim Kart"));
        EskiSim.add(new SpinnerCevap(null,"504","Sim Kart Yok"));

        for (SpinnerCevap item:EskiSim) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        EskiSimSpinner.setAdapter(adp);
    }
//------------------------------------------------------------------------------------------------------------------------------
//    YeniAnten
private void YeniAnteninit(){

    YeniAntenilkDoldurma();
    YeniAntenSpinner.setSelection(0, false);
    YeniAntenSpinner.setSelection(0,false);
    YeniAntenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            spinnerRequest.YeniAntenSecilen=Integer.parseInt(YeniAnten.get(position).Code);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


}
    private void YeniAntenilkDoldurma(){
        ArrayList<String> KaynakText=new ArrayList<>();
        YeniAnten.add(new SpinnerCevap(null,"0","Seçiniz"));
        YeniAnten.add(new SpinnerCevap(null,"201","Modem Anteni"));
        YeniAnten.add(new SpinnerCevap(null,"202","5 dBi Anten"));
        YeniAnten.add(new SpinnerCevap(null,"203","9 dBi Anten"));

        for (SpinnerCevap item:YeniAnten) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        YeniAntenSpinner.setAdapter(adp);
    }
//------------------------------------------------------------------------------------------------------------------------------
//    EskiAnten
private void EskiAnteninit(){

    EskiAntenilkDoldurma();
    EskiAntenSpinner.setSelection(0, false);
    EskiAntenSpinner.setSelection(0,false);
    EskiAntenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            spinnerRequest.EskiAntenSecilen=Integer.parseInt(EskiAnten.get(position).Code);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


}
    private void EskiAntenilkDoldurma(){
        ArrayList<String> KaynakText=new ArrayList<>();
        EskiAnten.add(new SpinnerCevap(null,"0","Seçiniz"));
        EskiAnten.add(new SpinnerCevap(null,"101","Modem Anteni"));
        EskiAnten.add(new SpinnerCevap(null,"102","5 dBi Anten"));
        EskiAnten.add(new SpinnerCevap(null,"103","9 dBi Anten"));
        EskiAnten.add(new SpinnerCevap(null,"104","Anten Yok"));

        for (SpinnerCevap item:EskiAnten) {
            KaynakText.add(item.Description);
        }

        adp=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,KaynakText);
        EskiAntenSpinner.setAdapter(adp);
    }
//------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ososGonderButton) {
            String kosul_control=Kosullar();

            if (!YeniModemSeriEditText.getText().toString().equals(""))
                spinnerRequest.YeniModemSerigirilen= YeniModemSeriEditText.getText().toString();
            if (!EskiModemSeriEditText.getText().toString().equals(""))
                spinnerRequest.EskiModemSerigirilen= EskiModemSeriEditText.getText().toString();


            if (kosul_control!=null){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setCancelable(false);
                dialog.setTitle("HATA!");
                dialog.setMessage(kosul_control);
                dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
            else{

//            if (!osf.AboneNo.isEmpty()){
//                osf.AboneNo=osf.AboneNo.replace("0","");
//            }

            ososSoruAciklamaDialog dialog = new ososSoruAciklamaDialog(this,this,spinnerRequest,osf);
            dialog.showDialog();

            }
        }
    }

    private String Kosullar(){

        if(!alt_emir_turu.equals("10") && !alt_emir_turu.equals("37") && !alt_emir_turu.equals("38") && !alt_emir_turu.equals("39") && !alt_emir_turu.equals("40")){

        if ( spinnerRequest.sorunKaynakSecilen.equals("")||spinnerRequest.sorunKaynakSecilen.equals("0")){
            return "Sorun kaynağını giriniz!!!";
        }

        if (spinnerRequest.YapilanIslemOtoSecilen.equals("")||spinnerRequest.YapilanIslemOtoSecilen.equals("0")){
            if (spinnerRequest.YapilanIslemOtoSecilen2Code==null)
            return "Yapılan İşlemi giriniz!!!";
        }

       if ( spinnerRequest.YapilanIslemOtoSecilen2Aux1!=null &&  (spinnerRequest.YapilanIslemOtoSecilen2Aux1.equals("MDM") ||  spinnerRequest.YapilanIslemOtoSecilen2Aux1.equals("MDMSIM"))){


            if (spinnerRequest.YeniModemSecilen==0 ){
                return "Yeni Modemi Giriniz!!!";
            }
           if (!alt_emir_turu.equals("10") && !alt_emir_turu.equals("37") && !alt_emir_turu.equals("38") && !alt_emir_turu.equals("39") && !alt_emir_turu.equals("40") && spinnerRequest.EskiModemSecilen==0 ){
               return "Eski Modemi Giriniz!!!";
           }
           if (spinnerRequest.YeniModemSerigirilen.equals("") || spinnerRequest.YeniModemSerigirilen.equals("0")){
               return "Yeni Modem Seri Numarasını Giriniz!!!";
           }
           if (!alt_emir_turu.equals("10") && !alt_emir_turu.equals("37") && !alt_emir_turu.equals("38") && !alt_emir_turu.equals("39") && !alt_emir_turu.equals("40") && (spinnerRequest.EskiModemSerigirilen.equals("") ||spinnerRequest.EskiModemSerigirilen.equals("0"))){
               return "Eski Modem Seri Numarasını Giriniz!!!";
           }

       }
       if ( spinnerRequest.YapilanIslemOtoSecilen2Aux1!=null &&  (spinnerRequest.YapilanIslemOtoSecilen2Aux1.equals("SIM") ||  spinnerRequest.YapilanIslemOtoSecilen2Aux1.equals("MDMSIM"))){

            if (!alt_emir_turu.equals("10") && !alt_emir_turu.equals("37") && !alt_emir_turu.equals("38") && !alt_emir_turu.equals("39") && !alt_emir_turu.equals("40") && spinnerRequest.EskiSimSecilen==0){
                return "Eski Simkartı Giriniz!!!";
            }
            if (spinnerRequest.YeniSimSecilen==0){
                return "Yeni Simkartı Giriniz!!!";
            }

        }
       if ( spinnerRequest.YapilanIslemOtoSecilen2Aux1!=null &&  spinnerRequest.YapilanIslemOtoSecilen2Aux1.equals("ANT") ){

            if (spinnerRequest.YeniAntenSecilen==0){
                return "Yeni Anten Tipini Giriniz!!!";
            }
            if (!alt_emir_turu.equals("10") && !alt_emir_turu.equals("37") && !alt_emir_turu.equals("38") && !alt_emir_turu.equals("39") && !alt_emir_turu.equals("40") && spinnerRequest.EskiAntenSecilen==0){
                return "Eski Anten Tipini Giriniz!!!";
            }

        }


        if ( spinnerRequest.EskiModemSerigirilen.equals("") ||spinnerRequest.EskiModemSerigirilen.equals("0")||spinnerRequest.EskiModemSerigirilen.charAt(0)==' '){
            return "Eski Modem Seri Numarasını Giriniz!!!";
        }

        }

        if (alt_emir_turu.equals("10") || alt_emir_turu.equals("37") || alt_emir_turu.equals("38") || alt_emir_turu.equals("39") || alt_emir_turu.equals("40")){
            if (spinnerRequest.YeniModemSecilen==0 ){
                return "Yeni Modemi Giriniz!!!";
            }
            if (spinnerRequest.YeniAntenSecilen==0){
                return "Yeni Anten Tipini Giriniz!!!";
            }
            if (spinnerRequest.YeniModemSerigirilen.equals("") || spinnerRequest.YeniModemSerigirilen.equals("0")){
                return "Yeni Modem Seri Numarasını Giriniz!!!";
            }
            if (spinnerRequest.YeniSimSecilen==0){
                return "Yeni Simkartı Giriniz!!!";
            }
        }


       return null;
    }

    private ArrayList<String> DescriptionListAl(ArrayList<SpinnerCevap> List , String Code){

        ArrayList<String> strings = new ArrayList<>();

        for (SpinnerCevap item:List) {
            if (item.parentKontrol(Code)){
                strings.add(item.Description);
            }
        }

        return strings;
    }

    private class  SpinnerCevap{
        String Parent;
        String Code;
        String Description;
        String Aux1;

        private SpinnerCevap(String Parent,
                String Code,
                String Description){
            this.Parent=Parent;
            this.Code=Code;
            this.Description=Description;

        }

        private SpinnerCevap(String Parent,
                             String Code,
                             String Description,String Aux1){
            this.Parent=Parent;
            this.Code=Code;
            this.Description=Description;
            this.Aux1=Aux1;
        }


        private boolean parentKontrol(String code){
            if (Parent.equals(code))
                return true;
            return false;
        }





    }

    public class SpinnerRequest{
    int YeniModemSecilen=0,
            EskiModemSecilen=0,
            YeniSimSecilen=0,
            EskiSimSecilen= 0,
            YeniAntenSecilen= 0,
            EskiAntenSecilen=0;
        String YeniModemSerigirilen="";
        String EskiModemSerigirilen="";
        String sorunKaynakSecilen="";
        String sorunKaynakSecilen2="";
        String YapilanIslemOtoSecilen="";
        String YapilanIslemOtoSecilen2Code;
        String YapilanIslemOtoSecilen2Aux1;

    String Aciklama="";
    }

}