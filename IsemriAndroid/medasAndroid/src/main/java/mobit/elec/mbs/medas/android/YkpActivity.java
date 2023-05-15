package mobit.elec.mbs.medas.android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobit.Globals;
import com.mobit.IApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import mobit.android.AndroidPlatform;
import mobit.eemr.SayacEslestir;
import mobit.eemr.YkpOkuma;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;
import java.time.LocalDate;

public class YkpActivity extends AppCompatActivity {
    Spinner saycMarka;
    Spinner OkumaPeriyot;
    EditText tarih1;
    EditText tarih2;
    Button Kaydet;
    IMedasApplication app;
    final Calendar myCalendar = Calendar.getInstance();
    int control=0;
    String[] mbsMarka=null;
    int tek_gun_mu;
    String activity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ykp);
        app = (IMedasApplication) Globals.app;
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor()));
        AndroidPlatform androidPlatform=null;
        androidPlatform.initForm(this);
        saycMarka = (Spinner) findViewById(R.id.SayacMarka);
        OkumaPeriyot=(Spinner) findViewById(R.id.OkumaPeriyot);
        Kaydet = (Button) findViewById(R.id.YkpSettingsKaydet);
        String[] markalar={"LUNA","MAKEL"};
        //"VI-KO","BAYLAN","KOHLER","KOHLER (AEL)","LANDIS"
        int[] markaid={1,2};
        String[] periyot={"30 dk","15 dk"};
        int[] periyotid={1,2};

        try {
            Intent iin= getIntent();
            Bundle b = iin.getExtras();
            if (b!=null){
                SayacEslestir s=new SayacEslestir();
                mbsMarka=new String[1];
                mbsMarka[0]=s.getModelAciklama ((String)b.get("sayac"));
                tek_gun_mu=(Integer) b.get("tekgun_mu");
                activity=(String)b.get("activity");
                //mbsMarka[0]="KOHLER";
                if (!mbsMarka[0].equals("LUNA") && !mbsMarka[0].equals("MAKEL")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(YkpActivity.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("UYARI!");
                    dialog.setMessage("Bu marka için yük profili desteklenmemektedir!");
                    dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Action for "Delete".
                            finish();

                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
       // mbsMarka=null;
        if (mbsMarka!=null){
            ArrayAdapter<String> spinnerAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mbsMarka);
            saycMarka.setAdapter(spinnerAdapter);
        }
        else {
            ArrayAdapter<String> spinnerAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, markalar);
            saycMarka.setAdapter(spinnerAdapter);
        }
        ArrayAdapter<String> spinnerAdapter2 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periyot);
        OkumaPeriyot.setAdapter(spinnerAdapter2);
        tarih1= (EditText) findViewById(R.id.editTarih1);
        tarih2= (EditText) findViewById(R.id.editTarih2);
        final SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
        Date date2 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        if (tek_gun_mu==1)
        {
            cal.add(Calendar.DATE,-1);
        }else{
           cal.set(Calendar.DAY_OF_MONTH,-1);
           cal.add(Calendar.DATE,-29);
        }



        Date preMonthDate = cal.getTime();
        tarih1.setText( String.valueOf(format.format(preMonthDate)));
        tarih2.setText(String.valueOf(format.format(date2)));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        tarih1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(YkpActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                control=1;
            }
        });
        tarih2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(YkpActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                control=2;
            }
        });
        Kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String kucuktarih=tarih1.getText().toString();
                final String buyuktarih=tarih2.getText().toString();
                final String smarka=saycMarka.getSelectedItem().toString();
                final String periyot=OkumaPeriyot.getSelectedItem().toString();

                if (!kucuktarih.equals("") && !buyuktarih.equals("")
                        && !smarka.equals("") && !periyot.equals("")){
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(YkpActivity.this);
                    dialog2.setCancelable(false);
                    dialog2.setMessage("BAŞARILI!");
                    dialog2.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Action for "Delete".
                            YkpOkuma ykpOkuma=new YkpOkuma();
                            ykpOkuma.setYkpOkumaDurum(1);
                            ykpOkuma.setPeriyot(periyot);
                            ykpOkuma.setSayacMarka(smarka);
                            ykpOkuma.setTarihAralık(tarih1+"-"+tarih2);
                            ykpOkuma.setYkpBuffer(BetikOlustur(kucuktarih,buyuktarih,smarka.toUpperCase(),periyot));
                            ykpOkuma.setYkpResult(null);
                            ykpOkuma.setYkpCount(1);

                            activityFinished(activity);
                        }
                    });
                    final AlertDialog alert2 = dialog2.create();
                    alert2.show();
                    //finish();

                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(YkpActivity.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("HATA!");
                    dialog.setMessage("Lütfen Tüm Alanları Doldurunuz!");
                    dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Action for "Delete".

                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                }

            }
        });
        if (tek_gun_mu==1)
        {
            Kaydet.performClick();
        }
    }
    private void updateLabel() {
        String myFormat = "yy.MM.dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (control==1)
            tarih1.setText(sdf.format(myCalendar.getTime()));
        if (control==2)
            tarih2.setText(sdf.format(myCalendar.getTime()));
    }
    private  void  activityFinished(String act){
    if(act.equals("IsemriIndexActivity2")){
        Intent returnIntent = new Intent(YkpActivity.this,IsemriIndexActivity2.class);
        returnIntent.putExtra("message","ykp1");
        startActivity(returnIntent);
        finish();
    }
    else if(act.equals("OkumaYapActivity2")){
        Intent returnIntent = new Intent(YkpActivity.this,OkumaYapActivity2.class);
        returnIntent.putExtra("message","ykp1");
        startActivity(returnIntent);
        finish();
    }




    }
    private byte[] BetikOlustur(String Tarih1,String Tarih2,String SayacMarka,String Periyot){

        if (SayacMarka.equals("LUNA")){
            //byte[] bufferbas=new byte[]{1,82,53,2,80,49,40};
            byte[] bufferbas=new byte[]{1,82,53,2,80,49,40};
            //59
            byte[] bufferson=new byte[]{41,40,41,3,80};
            byte[] virgul=new byte[]{59};
            byte[] buftar1=new byte[6];
            byte[] buftar2=new byte[6];
            int count=0;
            for (int i=0;i<Tarih1.length();i++){
                char ch=Tarih1.charAt(i);
                if(ch!='.'){
                    buftar1[count]=(byte) ch;
                    count++;
                }
            }
            count=0;
            for (int i=0;i<Tarih2.length();i++){
                char ch=Tarih2.charAt(i);
                if(ch!='.'){
                    buftar2[count]=(byte) ch;
                    count++;
                }
            }
            return merge(bufferbas,buftar1,virgul,buftar2,bufferson);

        }
        else if(SayacMarka.equals("MAKEL")){
            //byte[] buffer={1,82,50,2,80,46,48,49,40,49,57,48,52,50,52,48,48,48,48,59,49,57,48,52,50,54,48,48,48,48,41,3,38};//36
            byte[] bufferbas=new byte[]{1,82,50,2,80,46,48,49,40};
            //59
            byte[] bufferson=new byte[]{41,3};
            byte[] virgul=new byte[]{59};
            byte[] buftar1=new byte[10];
            byte[] buftar2=new byte[10];
            int count=0;
            for (int i=0;i<Tarih1.length();i++){
                char ch=Tarih1.charAt(i);
                if(ch!='.'){
                    buftar1[count]=(byte) ch;
                    count++;
                }
            }
            buftar1[6]=48;
            buftar1[7]=48;
            buftar1[8]=48;
            buftar1[9]=48;
            count=0;
            for (int i=0;i<Tarih2.length();i++){
                char ch=Tarih2.charAt(i);
                if(ch!='.'){
                    buftar2[count]=(byte) ch;
                    count++;
                }
            }
            buftar2[6]=48;
            buftar2[7]=48;
            buftar2[8]=48;
            buftar2[9]=48;
            return mergeMakel(bufferbas,buftar1,virgul,buftar2,bufferson,MakelBCCHesapla(Tarih1,Tarih2));
            // return buffer;
        }
        else if(SayacMarka.equals("KOHLER (AEL)")){
            YkpOkuma ykpOkuma=new YkpOkuma();
            String bccdata="R2x0002128.12.0000()x0003";
            byte[] ael=new byte[]{1, 82, 50, 2, 49, 50, 56, 46, 49, 50, 46, 48, 48, 48, 48, 40, 41, 3, 86};
            byte[][] aelbuufer=new byte[2048][19];
            for (int i= 2047;i>=0;i--)
            {
                int indis=i+1;
                String s=""+indis;
                if (s.length()==1){
                    bccdata="R2x0002128.12.000"+s.charAt(0)+"()x0003";
                    for (int j=0;j<ael.length;j++){
                        if (j==14){
                            aelbuufer[i][j]=(byte) s.charAt(0);
                        }
                        else {
                            aelbuufer[i][j]=ael[j];
                        }
                    }
                    aelbuufer[i][18]=VIKOBCCHesapla(bccdata)[0];
                }
                if (s.length()==2){
                    bccdata="R2x0002128.12.00"+s.charAt(0)+s.charAt(1)+"()x0003";
                    for (int j=0;j<ael.length;j++){
                        if (j==14){
                            aelbuufer[i][j]=(byte) s.charAt(0);
                        }
                        else if (j==13){
                            aelbuufer[i][j]=(byte) s.charAt(1);
                        }
                        else {
                            aelbuufer[i][j]=ael[j];
                        }
                    }
                    aelbuufer[i][18]=VIKOBCCHesapla(bccdata)[0];
                }
                if (s.length()==3){
                    bccdata="R2x0002128.12.0"+s.charAt(0)+s.charAt(1)+s.charAt(2)+"()x0003";
                    for (int j=0;j<ael.length;j++){
                        if (j==14){
                            aelbuufer[i][j]=(byte) s.charAt(0);
                        }
                        else if (j==13){
                            aelbuufer[i][j]=(byte) s.charAt(1);
                        }
                        else if (j==12){
                            aelbuufer[i][j]=(byte) s.charAt(2);
                        }
                        else {
                            aelbuufer[i][j]=ael[j];
                        }
                    }
                    aelbuufer[i][18]=VIKOBCCHesapla(bccdata)[0];
                }
                if (s.length()==4){
                    bccdata="R2x0002128.12."+s.charAt(0)+s.charAt(1)+s.charAt(2)+s.charAt(3)+"()x0003";
                    for (int j=0;j<ael.length;j++){
                        if (j==14){
                            aelbuufer[i][j]=(byte) s.charAt(0);
                        }
                        else if (j==13){
                            aelbuufer[i][j]=(byte) s.charAt(1);
                        }
                        else if (j==12){
                            aelbuufer[i][j]=(byte) s.charAt(2);
                        }
                        else if (j==11){
                            aelbuufer[i][j]=(byte) s.charAt(3);
                        }
                        else {
                            aelbuufer[i][j]=ael[j];
                        }
                    }
                    aelbuufer[i][18]=VIKOBCCHesapla(bccdata)[0];
                }


            }
            ykpOkuma.setYkpAELBuffer(aelbuufer);

            return null;
        }
        else if(SayacMarka.equals("KOHLER")){

        }
        else if(SayacMarka.equals("BAYLAN")){
            byte[] buffer=new byte[]{1,82,50,2,80,46,48,49,40,59,41,3,1};
            return buffer;
        }
        else if(SayacMarka.equals("VI-KO"))
        {
            /*
            MOO5
            Yük Profil Sorgusu  <soh>R5<stx>P.01(yy-mm-dd,hh:nn;yy-mm-dd,hh:nn)<etx><bcc>
            Yük Profil Sorgusu  <soh>R5<stx>P.02(yy-mm-dd,hh:nn;yy-mm-dd,hh:nn)<etx><bcc>
            Yük Profil Sorgusu  <soh>R5<stx>P.09(yy-mm-dd,hh:nn;yy-mm-dd,hh:nn)<etx><bcc>
            VEMC
            Sorgu: <soh>R2<stx>96.A.8.0(yy-mm-dd,hh:nn;yy-mm-dd,hh:nn)<etx><bcc>
            VEMm
            Sorgu: <soh>R2<stx>96.1.8.0(;)<etx><bcc>
             */
            YkpOkuma ykpOkuma=new YkpOkuma();
            byte[] bufferVemm=new byte[]{1,82,50,2,57,54,46,49,46,56,46,48,40,59,41,3,67};
            ykpOkuma.setYkpVEMMBuffer(bufferVemm);
            //VEMC
            byte[] bufferbasvemc=new byte[]{1,82,50,3,57,54,46,65,46,56,46,48,40};
            byte[] bufferbasmoo5=new byte[]{1,82,53,3,80,46,48,49,40};
            byte[] bufferson=new byte[]{41,3};
            byte[] virgul=new byte[]{59};
            byte[] buftar1=new byte[14];
            byte[] buftar2=new byte[14];
            int count=0;
            String bccData="";
            String strtarform1="";
            String strtarform2="";
            for (int i=0;i<Tarih1.length();i++){
                char ch=Tarih1.charAt(i);
                if(ch!='.'){
                    buftar1[count]=(byte) ch;
                    strtarform1+=ch;
                    count++;
                }
                if(ch=='.'){
                    buftar1[count]=45;
                    strtarform1+='-';
                    count++;
                }
            }
            strtarform1+=",00:00";
            buftar1[8]=44;
            buftar1[9]=48;
            buftar1[10]=48;
            buftar1[11]=58;
            buftar1[12]=48;
            buftar1[13]=48;
            count=0;
            for (int i=0;i<Tarih2.length();i++){
                char ch=Tarih2.charAt(i);
                if(ch!='.'){
                    buftar2[count]=(byte) ch;
                    strtarform2+=ch;
                    count++;
                }
                if (ch=='.'){
                    buftar2[count]=45;
                    strtarform2+="-";
                    count++;
                }
            }
            strtarform2+=",00:00";
            buftar2[8]=44;
            buftar2[9]=48;
            buftar2[10]=48;
            buftar2[11]=58;
            buftar2[12]=48;
            buftar2[13]=48;
            bccData="R2x000296.A.8.0("+strtarform1+";"+strtarform2+")x0003";
            ykpOkuma.setYkpVEMCBuffer(mergeMakel(bufferbasvemc,buftar1,virgul,buftar2,bufferson,VIKOBCCHesapla(bccData)));
            bccData="R5x0002P.01("+strtarform1+";"+strtarform2+")x0003";
            ykpOkuma.setYkpM005Buffer(mergeMakel(bufferbasmoo5,buftar1,virgul,buftar2,bufferson,VIKOBCCHesapla(bccData)));
            return null;
            //return merge(bufferbas,buftar1,virgul,buftar2,bufferson);
        }
        else if (SayacMarka.equals("LANDIS")){
            byte[] buffer=new byte[]{1,82,53,2,80,46,48,49,40,59,41,3};
            byte[] buf= new byte[buffer.length+1];
            for(int i=0; i<buffer.length; i++) {
                buf[i] = buffer[i];
            }
            buf[buf.length-1]=LandisBCCHesapla()[0];

            return buf;
        }
        return null;
    }
    public static byte[] merge(byte[] dizi1, byte[] dizi2, byte[] dizi3,byte[] dizi4,byte[] dizi5) {
        byte[] sonuc = new byte[dizi1.length+dizi2.length+dizi3.length+dizi4.length+dizi5.length];
        for(int i=0; i<dizi1.length; i++) {
            sonuc[i] = dizi1[i];
        }
        for(int i=0; i<dizi2.length; i++) {
            sonuc[dizi1.length+i] = dizi2[i];
        }
        for(int i=0; i<dizi3.length; i++) {
            sonuc[dizi1.length+dizi2.length+i] = dizi3[i];
        }
        for(int i=0; i<dizi4.length; i++) {
            sonuc[dizi1.length+dizi2.length+dizi3.length+i] = dizi4[i];
        }
        for(int i=0; i<dizi5.length; i++) {
            sonuc[dizi1.length+dizi2.length+dizi3.length+dizi4.length+i] = dizi5[i];
        }
        return sonuc;
    }
    public static byte[] mergeMakel(byte[] dizi1, byte[] dizi2, byte[] dizi3,byte[] dizi4,byte[] dizi5,byte[] dizi6) {
        byte[] sonuc = new byte[dizi1.length+dizi2.length+dizi3.length+dizi4.length+dizi5.length+dizi6.length];
        for(int i=0; i<dizi1.length; i++) {
            sonuc[i] = dizi1[i];
        }
        for(int i=0; i<dizi2.length; i++) {
            sonuc[dizi1.length+i] = dizi2[i];
        }
        for(int i=0; i<dizi3.length; i++) {
            sonuc[dizi1.length+dizi2.length+i] = dizi3[i];
        }
        for(int i=0; i<dizi4.length; i++) {
            sonuc[dizi1.length+dizi2.length+dizi3.length+i] = dizi4[i];
        }
        for(int i=0; i<dizi5.length; i++) {
            sonuc[dizi1.length+dizi2.length+dizi3.length+dizi4.length+i] = dizi5[i];
        }
        for(int i=0; i<dizi6.length; i++) {
            sonuc[dizi1.length+dizi2.length+dizi3.length+dizi4.length+dizi5.length+i] = dizi6[i];
        }
        return sonuc;
    }

    public static byte[] MakelBCCHesapla(String Tarih1,String Tarih2){
        byte num = 0;
        byte[] buffer=new byte[1];
        String[] sTarih1=Tarih1.split(Pattern.quote("."));
        String[] sTarih2=Tarih2.split(Pattern.quote("."));
        String bccData="R2x0002P.01("+sTarih1[0]+sTarih1[1]+sTarih1[2]+"0000;"+sTarih2[0]+sTarih2[1]+sTarih2[2]+"0000)x0003";
        for (int index = 0; index < bccData.length(); ++index)
            num ^= (byte) bccData.charAt(index);
        buffer[0]=num;
        return (buffer);
    }
    public static byte[] LandisBCCHesapla(){
        byte num = 0;
        byte[] buffer=new byte[1];
        String bccData="R5x0002P.01(;)x0003";
        for (int index = 0; index < bccData.length(); ++index)
            num ^= (byte) bccData.charAt(index);
        buffer[0]=num;
        return (buffer);
    }
    public static byte[] VIKOBCCHesapla(String bccData){
        byte num = 0;
        byte[] buffer=new byte[1];
        //String bccData="R5x0002P.01(;)x0003";
        for (int index = 0; index < bccData.length(); ++index)
            num ^= (byte) bccData.charAt(index);
        buffer[0]=num;
        return (buffer);
    }


}
