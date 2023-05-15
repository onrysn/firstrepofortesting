package mobit.elec.mbs.medas.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobit.Operation;
import com.mobit.Soti;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import mobit.eemr.OsosSoruForm;
import mobit.elec.medas.ws.SayacZimmetBilgi;
import mobit.http.Result;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ososSoruAciklamaDialog {
    private Context context;
    private Activity activity;
    private OsosSoruActivity.SpinnerRequest spinnerRequest;
    SayacZimmetBilgi medasServer;
    private  int durum;
    private  String aciklama;
    private Result OsosSoruServisResult;
    private OsosSoruForm osf;
    private    String otoSecilen;

    private  Dialog dialog;
    public ososSoruAciklamaDialog(Context context, Activity activity, OsosSoruActivity.SpinnerRequest spinnerRequest,OsosSoruForm osf){
        this.context=context;
        this.activity=activity;
        this.spinnerRequest=spinnerRequest;
        this.osf=osf;

    dialog= new Dialog(activity);
        medasServer = new mobit.elec.medas.ws.android.SayacZimmetBilgi();
    }
    public void showDialog() {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_osos_soru_aciklama);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText ososDialogEditText = dialog.findViewById(R.id.ososDialogEditText);

        Button ososDialogButton = dialog.findViewById(R.id.ososDialogButton);
        ososDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ososDialogEditText.getText().toString().equals("") || ososDialogEditText.getText().toString().charAt(0)==' '|| ososDialogEditText.getText().toString().equals("0") ){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setCancelable(false);
                    dialog.setTitle("HATA!");
                    dialog.setMessage("Açıklama Boş Bırakılamaz!!!");
                    dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    });

                    dialog.show();
                }else{
                    spinnerRequest.Aciklama=ososDialogEditText.getText().toString();
                    ososSoruAciklamaDialog.LoadDataOsosSoru loadData = new ososSoruAciklamaDialog.LoadDataOsosSoru();
                    loadData.execute();
                }



            }
        });



        Button osos_diaolog_close = dialog.findViewById(R.id.osos_diaolog_close);
        osos_diaolog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private class LoadDataOsosSoru extends AsyncTask<Void, Void, Void> {

        public ProgressDialog progressDialog = new ProgressDialog(context);


        String Kayit_Tarihi=String.valueOf(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));

        int KapatanCode = com.mobit.Globals.app.getEleman().getELEMAN_KODU();

        String KapatanUnvan = String.valueOf(com.mobit.Globals.app.getEleman().getELEMAN_ADI());


        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Gönderiliyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            int AboneNo,int EskiAnten,int EskiModem,int EskiModemSeri,int EskiSim
//			,String Ihbar_Tarihi,String KapanisAciklama,String Kapanma_Tarihi
//			,int Order_Number,int SorunKaynak,int YapilanIslemOto
//			,int YeniAnten,int YeniModem,int YeniModemSeri,int YeniSim
            try {

            if(spinnerRequest.YapilanIslemOtoSecilen2Code!=null){
                otoSecilen=spinnerRequest.YapilanIslemOtoSecilen2Code;
            }else{
                otoSecilen=spinnerRequest.YapilanIslemOtoSecilen;
            }


                OsosSoruServisResult =medasServer.PushOtomasyonEmir(osf.AboneNo,
                        spinnerRequest.EskiAntenSecilen,
                        spinnerRequest.EskiModemSecilen,
                        spinnerRequest.EskiModemSerigirilen,
                        spinnerRequest.EskiSimSecilen,
                        osf.Ihbar_Tarihi,
                        spinnerRequest.Aciklama,
                        Kayit_Tarihi,
                        KapatanUnvan,
                        KapatanCode,
                        osf.Order_Number,
                        spinnerRequest.sorunKaynakSecilen+spinnerRequest.sorunKaynakSecilen2,
                        otoSecilen,
                        spinnerRequest.YeniAntenSecilen,
                        spinnerRequest.YeniModemSecilen,
                        spinnerRequest.YeniModemSerigirilen,
                        spinnerRequest.YeniSimSecilen

                );
//                        medasServer.EndoksorUnvansizKacak( app.getActiveIsemri().getSAHA_ISEMRI_NO(),app.getActiveIsemri().getTESISAT_NO(),Kayit_Tarihi);

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
            if (OsosSoruServisResult!=null &&OsosSoruServisResult.ReturnCode.equals("OK")){
//				AboneDurumGirisActivity
//				Intent intent = new Intent(TespitActivity2.this, app.getFormClass(mobit.elec.mbs.medas.android.IDef.FORM_ABONE_DURUM_GIRIS));
//				startActivity(intent);
//                dialogUnvan.dismiss();
//                setResult(Operation.No);
//                finish();
                Toast.makeText(context,
                        OsosSoruServisResult.ReturnMessage,
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                activity.setResult(RESULT_OK);
                activity.finish();
            }else {
                if (OsosSoruServisResult!=null)
                {
                    Toast.makeText(context,
                        OsosSoruServisResult.ReturnMessage,
                        Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }
                else{
                    Toast.makeText(context,
                            "Soruları Daha Önce Cevaplamışsınız!!",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }
            }

        }
    }




}
