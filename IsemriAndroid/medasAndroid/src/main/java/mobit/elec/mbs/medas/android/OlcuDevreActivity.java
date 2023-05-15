package mobit.elec.mbs.medas.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mobit.Globals;
import com.mobit.IForm;

import mobit.android.AndroidPlatform;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.mbs.medas.IMedasApplication;

public class OlcuDevreActivity extends AppCompatActivity {
    public  static FragmentManager fragmentManager;

    IMedasApplication app;
    OlcuDevreForm odf;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olcu_devre);
        app = (IMedasApplication) Globals.app;
        odf = new OlcuDevreForm();
        setTitle("Ölçü Devre Tespit Formu");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor()));
        AndroidPlatform androidPlatform=null;
        androidPlatform.initForm(this);
        fragmentManager=getSupportFragmentManager();
        odf.AllClear(); // H.Elif
        if (findViewById(R.id.fragment_container)!=null){
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            OlcuHomeFragment olcuHomeFragment=new OlcuHomeFragment();
            fragmentTransaction.add(R.id.fragment_container,olcuHomeFragment,null);
            fragmentTransaction.commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() { // H.Elif
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UYARI");
        builder.setMessage("Ölçü Devre Tespit Formundan Çıkmak İstediğinize Emin Misiniz?");
        builder.setNegativeButton("Hayır", null);
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OlcuDevreActivity.super.onBackPressed();
            }
        });
        builder.show();
    }
}
