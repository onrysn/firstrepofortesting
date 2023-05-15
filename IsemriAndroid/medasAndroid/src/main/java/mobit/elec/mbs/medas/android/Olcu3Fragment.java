package mobit.elec.mbs.medas.android;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import mobit.eemr.OlcuDevreForm;


/**
 * A simple {@link Fragment} subclass.
 */
public class Olcu3Fragment extends Fragment {

    Button button;
    Button button2;

    EditText editgt_1_Oran;
    EditText editgt_1_Orankusur;
    Spinner editgt_1_gucu;
    EditText editgt_1_seri;
    EditText editgt_1_imalyili;
    Spinner spinnergt_1_Marka;
    Spinner spinnergt_1_sinif;
    EditText spinnergt_1_Markadiger;
    EditText spinnergt_1_sinifdiger;
    EditText editgt_1_gucudiger;

    EditText editgt_2_Oran;
    EditText editgt_2_Orankusur;
    Spinner editgt_2_gucu;
    EditText editgt_2_seri;
    EditText editgt_2_imalyili;
    Spinner spinnergt_2_Marka;
    Spinner spinnergt_2_sinif;
    EditText spinnergt_2_Markadiger;
    EditText spinnergt_2_sinifdiger;
    EditText editgt_2_gucudiger;

    EditText editgt_3_Oran;
    EditText editgt_3_Orankusur;
    Spinner editgt_3_gucu;
    EditText editgt_3_seri;
    EditText editgt_3_imalyili;
    Spinner spinnergt_3_Marka;
    Spinner spinnergt_3_sinif;
    EditText spinnergt_3_Markadiger;
    EditText spinnergt_3_sinifdiger;
    EditText editgt_3_gucudiger;

    TextView sinif1baslikdigerr;
    TextView marka1baslikdigerr;
    TextView gucu1baslikdigerr;

    TextView sinif2baslikdigerr;
    TextView marka2baslikdigerr;
    TextView gucu2baslikdigerr;

    TextView sinif3baslikdigerr;
    TextView marka3baslikdigerr;
    TextView gucu3baslikdigerr;
    OlcuDevreForm odf;
    public Olcu3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_olcu3, container, false);
        button=view.findViewById(R.id.bf3);
        button2=view.findViewById(R.id.gd2);
        odf=new OlcuDevreForm();

        sinif1baslikdigerr=view.findViewById(R.id.sinif1baslikdigerr);
        marka1baslikdigerr=view.findViewById(R.id.marka1baslikdiger);
        gucu1baslikdigerr=view.findViewById(R.id.guc1baslikdiger);

        sinif2baslikdigerr=view.findViewById(R.id.sinif2baslikdigerr);
        marka2baslikdigerr=view.findViewById(R.id.marka2baslikdiger);
        gucu2baslikdigerr=view.findViewById(R.id.guc2baslikdiger);

        sinif3baslikdigerr=view.findViewById(R.id.sinif3baslikdigerr);
        marka3baslikdigerr=view.findViewById(R.id.marka3baslikdiger);
        gucu3baslikdigerr=view.findViewById(R.id.guc3baslikdiger);

        editgt_1_Oran=view.findViewById(R.id.gt_1_Oran);
        editgt_1_Orankusur=view.findViewById(R.id.gt_1_Orankusur);
        editgt_1_gucu=view.findViewById(R.id.gt_1_gucu);
        editgt_1_seri=view.findViewById(R.id.gt_1_seri);
        editgt_1_imalyili=view.findViewById(R.id.gt_1_imalyili);
        spinnergt_1_Marka=view.findViewById(R.id.gt_1_Marka);
        spinnergt_1_sinif=view.findViewById(R.id.gt_1_sinif);
        spinnergt_1_Markadiger=view.findViewById(R.id.gt_1_Markadiger);
        spinnergt_1_sinifdiger=view.findViewById(R.id.gt_1_sinifdiger);
        editgt_1_gucudiger=view.findViewById(R.id.gt_1_gucudiger);

        editgt_2_Oran=view.findViewById(R.id.gt_2_Oran);
        editgt_2_Orankusur=view.findViewById(R.id.gt_2_Orankusur);
        editgt_2_gucu=view.findViewById(R.id.gt_2_gucu);
        editgt_2_seri=view.findViewById(R.id.gt_2_seri);
        editgt_2_imalyili=view.findViewById(R.id.gt_2_imalyili);
        spinnergt_2_Marka=view.findViewById(R.id.gt_2_Marka);
        spinnergt_2_sinif=view.findViewById(R.id.gt_2_sinif);
        spinnergt_2_Markadiger=view.findViewById(R.id.gt_2_Markadiger);
        spinnergt_2_sinifdiger=view.findViewById(R.id.gt_2_sinifdiger);
        editgt_2_gucudiger=view.findViewById(R.id.gt_2_gucudiger);

        editgt_3_Oran=view.findViewById(R.id.gt_3_Oran);
        editgt_3_Orankusur=view.findViewById(R.id.gt_3_Orankusur);
        editgt_3_gucu=view.findViewById(R.id.gt_3_gucu);
        editgt_3_seri=view.findViewById(R.id.gt_3_seri);
        editgt_3_imalyili=view.findViewById(R.id.gt_3_imalyili);
        spinnergt_3_Marka=view.findViewById(R.id.gt_3_Marka);
        spinnergt_3_sinif=view.findViewById(R.id.gt_3_sinif);
        spinnergt_3_Markadiger=view.findViewById(R.id.gt_3_Markadiger);
        spinnergt_3_sinifdiger=view.findViewById(R.id.gt_3_sinifdiger);
        editgt_3_gucudiger=view.findViewById(R.id.gt_3_gucudiger);

        final String[] markalar={"Diger","ENTES","TECON","ATK","ALCE","SEZGİN","ECS","REDEL","ESİT","ESİTAŞ","SİGMA","RED","SAYPORT","GÜLMET","ELİMSAN","FEDERAL","USTA","EMGE","BORTRANS","UNITRANS","NİKAR"};
        final String[] marka_kisa={"Diger","ENT","TEC","ATK","ALC","SEZ","ECS","RDL","ESİ","EST","SİG","RED","SAY","GÜL","ELİ","FED","UST","EMG","BOR","UNI","NİK"};
        String[] siniflar={"Diger","0.5", "1", "2", "0.5 S", "0.1", "0.2", "0.2 S"};
        String[] guc={"Diger","10","15","30","60"};

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, markalar);
        spinnergt_1_Marka.setAdapter(spinnerAdapter);
        ArrayAdapter<String> spinnerAdapter2 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, siniflar);
        spinnergt_1_sinif.setAdapter(spinnerAdapter2);
        ArrayAdapter<String> spinnerAdapterguc =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, guc);
        editgt_1_gucu.setAdapter(spinnerAdapterguc);

        ArrayAdapter<String> spinnerAdapter3 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, markalar);
        spinnergt_2_Marka.setAdapter(spinnerAdapter3);
        ArrayAdapter<String> spinnerAdapter4 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, siniflar);
        spinnergt_2_sinif.setAdapter(spinnerAdapter4);
        ArrayAdapter<String> spinnerAdapterguc2 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, guc);
        editgt_2_gucu.setAdapter(spinnerAdapterguc2);

        ArrayAdapter<String> spinnerAdapter5 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, markalar);
        spinnergt_3_Marka.setAdapter(spinnerAdapter5);
        ArrayAdapter<String> spinnerAdapter6 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, siniflar);
        spinnergt_3_sinif.setAdapter(spinnerAdapter6);
        ArrayAdapter<String> spinnerAdapterguc3 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, guc);
        editgt_3_gucu.setAdapter(spinnerAdapterguc3);
        if (!odf.getgt_1_Oran().equals("/")) {
            editgt_1_Oran.setText(odf.getgt_1_Oran().split("/")[0]);
            editgt_1_Orankusur.setText(odf.getgt_1_Oran().split("/")[1]);
        }
        if (!odf.getgt_1_seri().equals("")) editgt_1_seri.setText(String.valueOf(odf.getgt_1_seri()));
        if (odf.getgt_1_imalyili()!=-1) editgt_1_imalyili.setText(String.valueOf(odf.getgt_1_imalyili()));
        int c=0;
        if (odf.getgt_1_gucu()!=-1)
        {
            c=0;
            for (int i=0;i<guc.length;i++){
                if(guc[i].equals(String.valueOf(odf.getgt_1_gucu()))){
                    editgt_1_gucu.setSelection(i);
                    c=1;
                }
            }
            if (c==0 && odf.getgt_1_gucu()!=-1) {
                editgt_1_gucu.setSelection(0);
                editgt_1_gucudiger.setText(String.valueOf(odf.getgt_1_gucu()));
            }
        }
        c=0;
        for (int i=0;i<markalar.length;i++){
            if(marka_kisa[i].equals(odf.getgt_1_Marka())){
                spinnergt_1_Marka.setSelection(i);
                c=1;
            }
        }
        if (c==0){
            spinnergt_1_Marka.setSelection(0);
            spinnergt_1_Markadiger.setText(odf.getgt_1_Marka());
        }
        c=0;
        for (int i=0;i<siniflar.length;i++){
            if(siniflar[i].equals(odf.getgt_1_sinif())){
                spinnergt_1_sinif.setSelection(i);
                c=1;
            }
        }
        if (c==0){
            spinnergt_1_sinif.setSelection(0);
            spinnergt_1_sinifdiger.setText(odf.getgt_1_sinif());
        }
        spinnergt_1_Marka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=spinnergt_1_Marka.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    spinnergt_1_Markadiger.setVisibility(View.GONE);
                    marka1baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    spinnergt_1_Markadiger.setVisibility(View.VISIBLE);
                    marka1baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });
        spinnergt_1_sinif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=spinnergt_1_sinif.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    spinnergt_1_sinifdiger.setVisibility(View.GONE);
                    sinif1baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    spinnergt_1_sinifdiger.setVisibility(View.VISIBLE);
                    sinif1baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });
        editgt_1_gucu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=editgt_1_gucu.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    editgt_1_gucudiger.setVisibility(View.GONE);
                    gucu1baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    editgt_1_gucudiger.setVisibility(View.VISIBLE);
                    gucu1baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });


        if (!odf.getgt_2_Oran().equals("/")) {
            editgt_2_Oran.setText(odf.getgt_2_Oran().split("/")[0]);
            editgt_2_Orankusur.setText(odf.getgt_2_Oran().split("/")[1]);
        }
        if (!odf.getgt_2_seri().equals("")) editgt_2_seri.setText(String.valueOf(odf.getgt_2_seri()));
        if (odf.getgt_2_imalyili()!=-1) editgt_2_imalyili.setText(String.valueOf(odf.getgt_2_imalyili()));
        c=0;
        if (odf.getgt_2_gucu()!=-1)
        {
            c=0;
            for (int i=0;i<guc.length;i++){
                if(guc[i].equals(String.valueOf(odf.getgt_2_gucu()))){
                    editgt_2_gucu.setSelection(i);
                    c=1;
                }
            }
            if (c==0 && odf.getgt_2_gucu()!=-1) {
                editgt_2_gucu.setSelection(0);
                editgt_2_gucudiger.setText(String.valueOf(odf.getgt_2_gucu()));
            }
        }
        c=0;
        for (int i=0;i<markalar.length;i++){
            if(marka_kisa[i].equals(odf.getgt_2_Marka())){
                spinnergt_2_Marka.setSelection(i);
                c=1;
            }
        }
        if (c==0){
            spinnergt_2_Marka.setSelection(0);
            spinnergt_2_Markadiger.setText(odf.getgt_2_Marka());
        }
        c=0;
        for (int i=0;i<siniflar.length;i++){
            if(siniflar[i].equals(odf.getgt_2_sinif())){
                spinnergt_2_sinif.setSelection(i);
                c=1;
            }
        }
        if (c==0){
            spinnergt_2_sinif.setSelection(0);
            spinnergt_2_sinifdiger.setText(odf.getgt_2_sinif());
        }
        spinnergt_2_Marka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=spinnergt_2_Marka.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    spinnergt_2_Markadiger.setVisibility(View.GONE);
                    marka2baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    spinnergt_2_Markadiger.setVisibility(View.VISIBLE);
                    marka2baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });
        spinnergt_2_sinif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=spinnergt_2_sinif.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    spinnergt_2_sinifdiger.setVisibility(View.GONE);
                    sinif2baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    spinnergt_2_sinifdiger.setVisibility(View.VISIBLE);
                    sinif2baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });
        editgt_2_gucu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=editgt_2_gucu.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    editgt_2_gucudiger.setVisibility(View.GONE);
                    gucu2baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    editgt_2_gucudiger.setVisibility(View.VISIBLE);
                    gucu2baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });


        if (!odf.getgt_3_Oran().equals("/")) {
            editgt_3_Oran.setText(odf.getgt_3_Oran().split("/")[0]);
            editgt_3_Orankusur.setText(odf.getgt_3_Oran().split("/")[1]);
        }
        if (!odf.getgt_3_seri().equals("")) editgt_3_seri.setText(String.valueOf(odf.getgt_3_seri()));
        if (odf.getgt_3_imalyili()!=-1) editgt_3_imalyili.setText(String.valueOf(odf.getgt_3_imalyili()));
        c=0;
        if (odf.getgt_3_gucu()!=-1)
        {
            c=0;
            for (int i=0;i<guc.length;i++){
                if(guc[i].equals(String.valueOf(odf.getgt_3_gucu()))){
                    editgt_3_gucu.setSelection(i);
                    c=1;
                }
            }
            if (c==0 && odf.getgt_3_gucu()!=-1) {
                editgt_3_gucu.setSelection(0);
                editgt_3_gucudiger.setText(String.valueOf(odf.getgt_3_gucu()));
            }
        }
        c=0;
        for (int i=0;i<markalar.length;i++){
            if(marka_kisa[i].equals(odf.getgt_3_Marka())){
                spinnergt_3_Marka.setSelection(i);
                c=1;
            }
        }
        if (c==0){
            spinnergt_3_Marka.setSelection(0);
            spinnergt_3_Markadiger.setText(odf.getgt_3_Marka());
        }
        c=0;
        for (int i=0;i<siniflar.length;i++){
            if(siniflar[i].equals(odf.getgt_3_sinif())){
                spinnergt_3_sinif.setSelection(i);
                c=1;
            }
        }
        if (c==0){
            spinnergt_3_sinif.setSelection(0);
            spinnergt_3_sinifdiger.setText(odf.getgt_3_sinif());
        }
        spinnergt_3_Marka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=spinnergt_3_Marka.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    spinnergt_3_Markadiger.setVisibility(View.GONE);
                    marka3baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    spinnergt_3_Markadiger.setVisibility(View.VISIBLE);
                    marka3baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });
        spinnergt_3_sinif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=spinnergt_3_sinif.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    spinnergt_3_sinifdiger.setVisibility(View.GONE);
                    sinif3baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    spinnergt_3_sinifdiger.setVisibility(View.VISIBLE);
                    sinif3baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });
        editgt_3_gucu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-genergted method stub
                String  mselection=editgt_3_gucu.getSelectedItem().toString();
                if (!mselection.equals("Diger"))
                {
                    editgt_3_gucudiger.setVisibility(View.GONE);
                    gucu3baslikdigerr.setVisibility(View.GONE);
                }
                else {
                    editgt_3_gucudiger.setVisibility(View.VISIBLE);
                    gucu3baslikdigerr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-genergted method stub
                //
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editgt_1_Oran.getText().toString().equals("") && !editgt_1_Orankusur.getText().toString().equals("")){
                    odf.setgt_1_Oran(editgt_1_Oran.getText().toString()+"/"+editgt_1_Orankusur.getText().toString());
                }
                else {
                    editgt_1_Oran.setError("Boş olamaz!");
                    editgt_1_Orankusur.setError("Boş Olamaz!");
                }
                if (!editgt_1_seri.getText().toString().equals("")) {
                    odf.setgt_1_seri((editgt_1_seri.getText().toString()));
                }
                else {
                    editgt_1_seri.setError("Boş olamaz!");
                }
                if (!editgt_1_imalyili.getText().toString().equals("") && editgt_1_imalyili.getText().toString().length()==4){
                    odf.setgt_1_imalyili(Integer.parseInt(editgt_1_imalyili.getText().toString()));
                }
                else {
                    editgt_1_imalyili.setError("4 haneli olmalı! ");
                }
                if (!editgt_1_gucu.getSelectedItem().toString().equals("Diger"))
                {
                    odf.setgt_1_gucu(Integer.parseInt(editgt_1_gucu.getSelectedItem().toString()));
                }
                else {
                    if (!editgt_1_gucudiger.getText().toString().equals("")) {
                        odf.setgt_1_gucu(Integer.parseInt(editgt_1_gucudiger.getText().toString()));
                    }
                    else {
                        editgt_1_gucudiger.setError("Boş olamaz!");
                    }
                }
                if (!spinnergt_1_Marka.getSelectedItem().toString().equals("Diger")) {
                    for (int i = 0; i < markalar.length; i++) {
                        if (markalar[i].equals(spinnergt_1_Marka.getSelectedItem().toString())) {
                            odf.setgt_1_Marka(marka_kisa[i]);
                            break;
                        }
                    }
                }
                else {
                    if (!spinnergt_1_Markadiger.getText().toString().equals("")) {
                        odf.setgt_1_Marka(spinnergt_1_Markadiger.getText().toString());
                    }
                    else {
                        spinnergt_1_Markadiger.setError("Boş olamaz!");
                    }
                }
                //odf.setgt_1_Marka(spinnergt_1_Marka.getSelectedItem().toString());
                if (!spinnergt_1_sinif.getSelectedItem().toString().equals("Diger"))
                {odf.setgt_1_sinif(spinnergt_1_sinif.getSelectedItem().toString());}
                else {
                    if (!spinnergt_1_sinifdiger.getText().toString().equals("")) {
                        odf.setgt_1_sinif(spinnergt_1_sinifdiger.getText().toString());
                    }
                    else {
                        spinnergt_1_sinifdiger.setError("Boş olamaz!");
                    }
                }
                if (!editgt_2_Oran.getText().toString().equals("") && !editgt_2_Orankusur.getText().toString().equals("")){
                    odf.setgt_2_Oran(editgt_2_Oran.getText().toString()+"/"+editgt_2_Orankusur.getText().toString());
                }
                else {
                    editgt_2_Oran.setError("Boş olamaz!");
                    editgt_2_Orankusur.setError("Boş Olamaz!");
                }
                if (!editgt_2_seri.getText().toString().equals("")) {
                    odf.setgt_2_seri((editgt_2_seri.getText().toString()));
                }
                else {
                    editgt_2_seri.setError("Boş olamaz!");
                }
                if (!editgt_2_imalyili.getText().toString().equals("") && editgt_2_imalyili.getText().toString().length()==4){
                    odf.setgt_2_imalyili(Integer.parseInt(editgt_2_imalyili.getText().toString()));
                }
                else {
                    editgt_2_imalyili.setError("4 haneli olmalı! ");
                }
                if (!editgt_2_gucu.getSelectedItem().toString().equals("Diger"))
                {
                    odf.setgt_2_gucu(Integer.parseInt(editgt_2_gucu.getSelectedItem().toString()));
                }
                else {
                    if (!editgt_2_gucudiger.getText().toString().equals("")) {
                        odf.setgt_2_gucu(Integer.parseInt(editgt_2_gucudiger.getText().toString()));
                    }
                    else {
                        editgt_2_gucudiger.setError("Boş olamaz!");
                    }
                }
                if (!spinnergt_2_Marka.getSelectedItem().toString().equals("Diger")) {
                    for (int i = 0; i < markalar.length; i++) {
                        if (markalar[i].equals(spinnergt_2_Marka.getSelectedItem().toString())) {
                            odf.setgt_2_Marka(marka_kisa[i]);
                            break;
                        }
                    }
                }
                else {
                    if (!spinnergt_2_Markadiger.getText().toString().equals("")) {
                        odf.setgt_2_Marka(spinnergt_2_Markadiger.getText().toString());
                    }
                    else {
                        spinnergt_2_Markadiger.setError("Boş olamaz!");
                    }
                }
                //odf.setgt_2_Marka(spinnergt_2_Marka.getSelectedItem().toString());
                if (!spinnergt_2_sinif.getSelectedItem().toString().equals("Diger"))
                {odf.setgt_2_sinif(spinnergt_2_sinif.getSelectedItem().toString());}
                else {
                    if (!spinnergt_2_sinifdiger.getText().toString().equals("")) {
                        odf.setgt_2_sinif(spinnergt_2_sinifdiger.getText().toString());
                    }
                    else {
                        spinnergt_2_sinifdiger.setError("Boş olamaz!");
                    }
                }

                if (!editgt_3_Oran.getText().toString().equals("") && !editgt_3_Orankusur.getText().toString().equals("")){
                    odf.setgt_3_Oran(editgt_3_Oran.getText().toString()+"/"+editgt_3_Orankusur.getText().toString());
                }
                else {
                    editgt_3_Oran.setError("Boş olamaz!");
                    editgt_3_Orankusur.setError("Boş Olamaz!");
                }
                if (!editgt_3_seri.getText().toString().equals("")) {
                    odf.setgt_3_seri((editgt_3_seri.getText().toString()));
                }
                else {
                    editgt_3_seri.setError("Boş olamaz!");
                }
                if (!editgt_3_imalyili.getText().toString().equals("") && editgt_3_imalyili.getText().toString().length()==4){
                    odf.setgt_3_imalyili(Integer.parseInt(editgt_3_imalyili.getText().toString()));
                }
                else {
                    editgt_3_imalyili.setError("4 haneli olmalı! ");
                }
                if (!editgt_3_gucu.getSelectedItem().toString().equals("Diger"))
                {
                    odf.setgt_3_gucu(Integer.parseInt(editgt_3_gucu.getSelectedItem().toString()));
                }
                else {
                    if (!editgt_3_gucudiger.getText().toString().equals("")) {
                        odf.setgt_3_gucu(Integer.parseInt(editgt_3_gucudiger.getText().toString()));
                    }
                    else {
                        editgt_3_gucudiger.setError("Boş olamaz!");
                    }
                }
                if (!spinnergt_3_Marka.getSelectedItem().toString().equals("Diger")) {
                    for (int i = 0; i < markalar.length; i++) {
                        if (markalar[i].equals(spinnergt_3_Marka.getSelectedItem().toString())) {
                            odf.setgt_3_Marka(marka_kisa[i]);
                            break;
                        }
                    }
                }
                else {
                    if (!spinnergt_3_Markadiger.getText().toString().equals("")) {
                        odf.setgt_3_Marka(spinnergt_3_Markadiger.getText().toString());
                    }
                    else {
                        spinnergt_3_Markadiger.setError("Boş olamaz!");
                    }
                }
                //odf.setgt_3_Marka(spinnergt_3_Marka.getSelectedItem().toString());
                if (!spinnergt_3_sinif.getSelectedItem().toString().equals("Diger"))
                {odf.setgt_3_sinif(spinnergt_3_sinif.getSelectedItem().toString());}
                else {
                    if (!spinnergt_3_sinifdiger.getText().toString().equals("")) {
                        odf.setgt_3_sinif(spinnergt_3_sinifdiger.getText().toString());
                    }
                    else {
                        spinnergt_3_sinifdiger.setError("Boş olamaz!");
                    }
                }

                // H.Elif - oran ve küsürlerin hesaplanıp çarpan ile karşılaştırılması
                double control1 = Integer.parseInt(editgt_1_Oran.getText().toString().equals("") ? "1" : editgt_1_Oran.getText().toString()) / Integer.parseInt(editgt_1_Orankusur.getText().toString().equals("") ? "1" : editgt_1_Orankusur.getText().toString());
                double control2 = Integer.parseInt(editgt_2_Oran.getText().toString().equals("") ? "1" : editgt_2_Oran.getText().toString()) / Integer.parseInt(editgt_2_Orankusur.getText().toString().equals("") ? "1" : editgt_2_Orankusur.getText().toString());
                double control3 = Integer.parseInt(editgt_3_Oran.getText().toString().equals("") ? "1" : editgt_3_Oran.getText().toString()) / Integer.parseInt(editgt_3_Orankusur.getText().toString().equals("") ? "1" : editgt_3_Orankusur.getText().toString());

                odf.set_gt_1_sinif_gucu(String.valueOf(odf.getgt_1_sinif() + "/" + odf.getgt_1_gucu()));
                odf.set_gt_2_sinif_gucu(String.valueOf(odf.getgt_2_sinif() + "/" + odf.getgt_2_gucu()));
                odf.set_gt_3_sinif_gucu(String.valueOf(odf.getgt_3_sinif() + "/" + odf.getgt_3_gucu()));

                if (control1 == odf.getcarpan() && control2 == odf.getcarpan() && control3 == odf.getcarpan()) {
                    Send();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Uyarı!");
                    dialog.setMessage("Gelen Çarpan ile Girilen Çarpan Hesabı Farklı Bulundu. \nDevam Etmek İstediğine Emin Misin?");
                    dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Send();
                        }
                    });
                    dialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (odf.get_disaridan_olcu_dur() == 1)
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuTakilanSayacEndexFragment(), null).commit();
                else if (odf.getakim_trafosu_dur() == 1) {
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu2Fragment(), null).commit();
                } else {
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuAboneBilgileriFragment(), null).commit();
                }
            }
        });

        return view;
    }
    public void SetDiger(int durum){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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

    public void Send() {
        if (!odf.getgt_1_Oran().equals("/") && odf.getgt_1_gucu()!=-1 && !odf.getgt_1_seri().equals("") && odf.getgt_1_imalyili()!=-1 && !odf.getgt_1_Marka().equals("") && !odf.getgt_1_sinif().equals("")
                && !odf.getgt_2_Oran().equals("/") && odf.getgt_2_gucu()!=-1 && !odf.getgt_2_seri().equals("") && odf.getgt_2_imalyili()!=-1 && !odf.getgt_2_Marka().equals("") && !odf.getgt_2_sinif().equals("")
                && !odf.getgt_3_Oran().equals("/") && odf.getgt_3_gucu()!=-1 && !odf.getgt_3_seri().equals("") && odf.getgt_3_imalyili()!=-1 && !odf.getgt_3_Marka().equals("") && !odf.getgt_3_sinif().equals("")) {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuSokulenMuhurFragment(), null).commit();
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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

}
