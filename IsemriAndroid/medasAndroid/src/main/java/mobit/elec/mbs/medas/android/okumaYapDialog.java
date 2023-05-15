package mobit.elec.mbs.medas.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class okumaYapDialog {
    private Context context;
    private Activity activity;
    IDialogListener listener;
    public okumaYapDialog(Context context, Activity activity){
        this.context=context;
        this.activity=activity;

        listener=(IDialogListener)context;
    }
    public void showDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_okumayap);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button islemListe=dialog.findViewById(R.id.islemListe);
        Button elleOku=dialog.findViewById(R.id.elleOku);
        Button aboneDurum=dialog.findViewById(R.id.aboneDurum);
        Button tekrarYazdirma2=dialog.findViewById(R.id.tekrarYazdirma2);
        Button gps=dialog.findViewById(R.id.gps);
        Button adressTarif=dialog.findViewById(R.id.adressTarif);
        Button haritaokuma=dialog.findViewById(R.id.haritaokuma);
        Button ykpBirGunluk=dialog.findViewById(R.id.ykpBirGunluk);

        Button dialog_OK=dialog.findViewById(R.id.dialog_OK);

        islemListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(1);
                dialog.dismiss();
            }
        });
        elleOku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(2);
                dialog.dismiss();
            }
        });
        aboneDurum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(3);
                dialog.dismiss();
            }
        });
        tekrarYazdirma2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(4);
                dialog.dismiss();
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(5);
                dialog.dismiss();
            }
        });
        adressTarif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(6);
                dialog.dismiss();
            }
        });
        haritaokuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Cancel" ,Toast.LENGTH_SHORT).show();
                listener.dialogClickListener(7);
                dialog.dismiss();
            }
        });
        ykpBirGunluk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogClickListener(8);
                dialog.dismiss();
            }
        });
        dialog_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}



