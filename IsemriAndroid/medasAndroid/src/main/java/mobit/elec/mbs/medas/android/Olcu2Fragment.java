package mobit.elec.mbs.medas.android;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import mobit.eemr.OlcuDevreForm;


/**
 * A simple {@link Fragment} subclass.
 */
public class Olcu2Fragment extends Fragment {

    Button button;
    Button button2;
    EditText editAT_1_Oran;
    EditText editAT_1_Orankusur;
    Spinner editAT_1_gucu;
    EditText editat_1_seri;
    EditText editat_1_imalyili;
    Spinner spinnerAT_1_Marka;
    Spinner spinnerat_1_sinif;
    EditText spinnerAT_1_Markadiger;
    EditText spinnerat_1_sinifdiger;
    EditText editAT_1_gucudiger;

    EditText editAT_2_Oran;
    EditText editAT_2_Orankusur;
    Spinner editAT_2_gucu;
    EditText editat_2_seri;
    EditText editat_2_imalyili;
    Spinner spinnerAT_2_Marka;
    Spinner spinnerat_2_sinif;
    EditText spinnerAT_2_Markadiger;
    EditText spinnerat_2_sinifdiger;
    EditText editAT_2_gucudiger;

    EditText editAT_3_Oran;
    EditText editAT_3_Orankusur;
    Spinner editAT_3_gucu;
    EditText editat_3_seri;
    EditText editat_3_imalyili;
    Spinner spinnerAT_3_Marka;
    Spinner spinnerat_3_sinif;
    EditText spinnerAT_3_Markadiger;
    EditText spinnerat_3_sinifdiger;
    EditText editAT_3_gucudiger;

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

    public Olcu2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olcu2, container, false);
        odf = new OlcuDevreForm();
        button = view.findViewById(R.id.bf2);
        button2 = view.findViewById(R.id.gd1);

        sinif1baslikdigerr = view.findViewById(R.id.sinif1baslikdigerr);
        marka1baslikdigerr = view.findViewById(R.id.marka1baslikdiger);
        gucu1baslikdigerr = view.findViewById(R.id.guc1baslikdiger);

        sinif2baslikdigerr = view.findViewById(R.id.sinif2baslikdigerr);
        marka2baslikdigerr = view.findViewById(R.id.marka2baslikdiger);
        gucu2baslikdigerr = view.findViewById(R.id.guc2baslikdiger);

        sinif3baslikdigerr = view.findViewById(R.id.sinif3baslikdigerr);
        marka3baslikdigerr = view.findViewById(R.id.marka3baslikdiger);
        gucu3baslikdigerr = view.findViewById(R.id.guc3baslikdiger);
        //  TRAFO 1
        editAT_1_Oran = view.findViewById(R.id.AT_1_Oran);
        editAT_1_Orankusur = view.findViewById(R.id.AT_1_Orankusur);
        editAT_1_gucu = view.findViewById(R.id.AT_1_gucu);
        editat_1_seri = view.findViewById(R.id.at_1_seri);
        editat_1_imalyili = view.findViewById(R.id.at_1_imalyili);
        spinnerAT_1_Marka = view.findViewById(R.id.AT_1_Marka);
        spinnerat_1_sinif = view.findViewById(R.id.at_1_sinif);
        spinnerAT_1_Markadiger = view.findViewById(R.id.AT_1_Markadiger);
        spinnerat_1_sinifdiger = view.findViewById(R.id.at_1_sinifdiger);
        editAT_1_gucudiger = view.findViewById(R.id.AT_1_gucudiger);
        //  TRAFO 2
        editAT_2_Oran = view.findViewById(R.id.AT_2_Oran);
        editAT_2_Orankusur = view.findViewById(R.id.AT_2_Orankusur);
        editAT_2_gucu = view.findViewById(R.id.AT_2_gucu);
        editat_2_seri = view.findViewById(R.id.at_2_seri);
        editat_2_imalyili = view.findViewById(R.id.at_2_imalyili);
        spinnerAT_2_Marka = view.findViewById(R.id.AT_2_Marka);
        spinnerat_2_sinif = view.findViewById(R.id.at_2_sinif);
        spinnerAT_2_Markadiger = view.findViewById(R.id.AT_2_Markadiger);
        spinnerat_2_sinifdiger = view.findViewById(R.id.at_2_sinifdiger);
        editAT_2_gucudiger = view.findViewById(R.id.AT_2_gucudiger);
        //  TRAFO 3
        editAT_3_Oran = view.findViewById(R.id.AT_3_Oran);
        editAT_3_Orankusur = view.findViewById(R.id.AT_3_Orankusur);
        editAT_3_gucu = view.findViewById(R.id.AT_3_gucu);
        editat_3_seri = view.findViewById(R.id.at_3_seri);
        editat_3_imalyili = view.findViewById(R.id.at_3_imalyili);
        spinnerAT_3_Marka = view.findViewById(R.id.AT_3_Marka);
        spinnerat_3_sinif = view.findViewById(R.id.at_3_sinif);
        spinnerAT_3_Markadiger = view.findViewById(R.id.AT_3_Markadiger);
        spinnerat_3_sinifdiger = view.findViewById(R.id.at_3_sinifdiger);
        editAT_3_gucudiger = view.findViewById(R.id.AT_3_gucudiger);

        final String[] markalar = {"Diger", "ENTES", "TECON", "ATK", "ALCE", "SEZGİN", "ECS", "REDEL", "ESİT", "ESİTAŞ", "SİGMA", "RED", "SAYPORT", "GÜLMET", "ELİMSAN", "FEDERAL", "USTA", "EMGE", "BORTRANS", "UNITRANS", "NİKAR"};
        final String[] marka_kisa = {"Diger", "ENT", "TEC", "ATK", "ALC", "SEZ", "ECS", "RDL", "ESİ", "EST", "SİG", "RED", "SAY", "GÜL", "ELİ", "FED", "UST", "EMG", "BOR", "UNI", "NİK"};
        String[] siniflar = {"Diger", "0.5", "1", "2", "0.5 S", "0.1", "0.2", "0.2 S"};
        String[] guc = {"Diger", "10", "15", "30", "60"};


        //  TRAFO 1
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, markalar);
        spinnerAT_1_Marka.setAdapter(spinnerAdapter);

        ArrayAdapter<String> spinnerAdapter2 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, siniflar);
        spinnerat_1_sinif.setAdapter(spinnerAdapter2);

        ArrayAdapter<String> spinnerAdapterguc =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, guc);
        editAT_1_gucu.setAdapter(spinnerAdapterguc);
        //  TRAFO 2
        ArrayAdapter<String> spinnerAdapter3 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, markalar);
        spinnerAT_2_Marka.setAdapter(spinnerAdapter3);

        ArrayAdapter<String> spinnerAdapter4 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, siniflar);
        spinnerat_2_sinif.setAdapter(spinnerAdapter4);

        ArrayAdapter<String> spinnerAdapterguc2 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, guc);
        editAT_2_gucu.setAdapter(spinnerAdapterguc2);
        //  TRAFO 3
        ArrayAdapter<String> spinnerAdapter5 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, markalar);
        spinnerAT_3_Marka.setAdapter(spinnerAdapter5);

        ArrayAdapter<String> spinnerAdapter6 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, siniflar);
        spinnerat_3_sinif.setAdapter(spinnerAdapter6);
        ArrayAdapter<String> spinnerAdapterguc3 =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, guc);
        editAT_3_gucu.setAdapter(spinnerAdapterguc3);
        //  TRAFO 1
        if (!odf.getAT_1_Oran().equals("/")) {
            editAT_1_Oran.setText(odf.getAT_1_Oran().split("/")[0]);
            editAT_1_Orankusur.setText(odf.getAT_1_Oran().split("/")[1]);
        }
        if (!odf.getat_1_seri().equals(""))
            editat_1_seri.setText(String.valueOf(odf.getat_1_seri()));
        if (odf.getat_1_imalyili() != -1)
            editat_1_imalyili.setText(String.valueOf(odf.getat_1_imalyili()));
        int c = 0;
        if (odf.getAT_1_gucu() != -1) {
            c = 0;
            for (int i = 0; i < guc.length; i++) {
                if (guc[i].equals(String.valueOf(odf.getAT_1_gucu()))) {
                    editAT_1_gucu.setSelection(i);
                    c = 1;
                }
            }
            if (c == 0 && odf.getAT_1_gucu() != -1) {
                editAT_1_gucu.setSelection(0);
                editAT_1_gucudiger.setText(String.valueOf(odf.getAT_1_gucu()));
            }
        }
        c = 0;
        for (int i = 0; i < markalar.length; i++) {
            if (marka_kisa[i].equals(odf.getAT_1_Marka())) {
                spinnerAT_1_Marka.setSelection(i);
                c = 1;
            }
        }
        if (c == 0) {
            spinnerAT_1_Marka.setSelection(0);
            spinnerAT_1_Markadiger.setText(odf.getAT_1_Marka());
        }
        c = 0;
        for (int i = 0; i < siniflar.length; i++) {
            if (siniflar[i].equals(odf.getat_1_sinif())) {
                spinnerat_1_sinif.setSelection(i);
                c = 1;
            }
        }
        if (c == 0) {
            spinnerat_1_sinif.setSelection(0);
            spinnerat_1_sinifdiger.setText(odf.getat_1_sinif());
        }
        spinnerAT_1_Marka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = spinnerAT_1_Marka.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    spinnerAT_1_Markadiger.setVisibility(View.GONE);
                    marka1baslikdigerr.setVisibility(View.GONE);
                } else {
                    spinnerAT_1_Markadiger.setVisibility(View.VISIBLE);
                    marka1baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        spinnerat_1_sinif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = spinnerat_1_sinif.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    spinnerat_1_sinifdiger.setVisibility(View.GONE);
                    sinif1baslikdigerr.setVisibility(View.GONE);
                } else {
                    spinnerat_1_sinifdiger.setVisibility(View.VISIBLE);
                    sinif1baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        editAT_1_gucu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = editAT_1_gucu.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    editAT_1_gucudiger.setVisibility(View.GONE);
                    gucu1baslikdigerr.setVisibility(View.GONE);
                } else {
                    editAT_1_gucudiger.setVisibility(View.VISIBLE);
                    gucu1baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        //  TRAFO 2
        if (!odf.getAT_2_Oran().equals("/")) {
            editAT_2_Oran.setText(odf.getAT_2_Oran().split("/")[0]);
            editAT_2_Orankusur.setText(odf.getAT_2_Oran().split("/")[1]);
        }
        if (!odf.getat_2_seri().equals(""))
            editat_2_seri.setText(String.valueOf(odf.getat_2_seri()));
        if (odf.getat_2_imalyili() != -1)
            editat_2_imalyili.setText(String.valueOf(odf.getat_2_imalyili()));
        c = 0;
        if (odf.getAT_2_gucu() != -1) {
            c = 0;
            for (int i = 0; i < guc.length; i++) {
                if (guc[i].equals(String.valueOf(odf.getAT_2_gucu()))) {
                    editAT_2_gucu.setSelection(i);
                    c = 1;
                }
            }
            if (c == 0 && odf.getAT_2_gucu() != -1) {
                editAT_2_gucu.setSelection(0);
                editAT_2_gucudiger.setText(String.valueOf(odf.getAT_2_gucu()));
            }
        }
        c = 0;
        for (int i = 0; i < markalar.length; i++) {
            if (marka_kisa[i].equals(odf.getAT_2_Marka())) {
                spinnerAT_2_Marka.setSelection(i);
                c = 1;
            }
        }
        if (c == 0) {
            spinnerAT_2_Marka.setSelection(0);
            spinnerAT_2_Markadiger.setText(odf.getAT_2_Marka());
        }
        c = 0;
        for (int i = 0; i < siniflar.length; i++) {
            if (siniflar[i].equals(odf.getat_2_sinif())) {
                spinnerat_2_sinif.setSelection(i);
                c = 1;
            }
        }
        if (c == 0) {
            spinnerat_2_sinif.setSelection(0);
            spinnerat_2_sinifdiger.setText(odf.getat_2_sinif());
        }
        spinnerAT_2_Marka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = spinnerAT_2_Marka.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    spinnerAT_2_Markadiger.setVisibility(View.GONE);
                    marka2baslikdigerr.setVisibility(View.GONE);
                } else {
                    spinnerAT_2_Markadiger.setVisibility(View.VISIBLE);
                    marka2baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        spinnerat_2_sinif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = spinnerat_2_sinif.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    spinnerat_2_sinifdiger.setVisibility(View.GONE);
                    sinif2baslikdigerr.setVisibility(View.GONE);
                } else {
                    spinnerat_2_sinifdiger.setVisibility(View.VISIBLE);
                    sinif2baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        editAT_2_gucu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = editAT_2_gucu.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    editAT_2_gucudiger.setVisibility(View.GONE);
                    gucu2baslikdigerr.setVisibility(View.GONE);
                } else {
                    editAT_2_gucudiger.setVisibility(View.VISIBLE);
                    gucu2baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        //  TRAFO 3
        if (!odf.getAT_3_Oran().equals("/")) {
            editAT_3_Oran.setText(odf.getAT_3_Oran().split("/")[0]);
            editAT_3_Orankusur.setText(odf.getAT_3_Oran().split("/")[1]);
        }
        if (!odf.getat_3_seri().equals(""))
            editat_3_seri.setText(String.valueOf(odf.getat_3_seri()));
        if (odf.getat_3_imalyili() != -1)
            editat_3_imalyili.setText(String.valueOf(odf.getat_3_imalyili()));
        c = 0;
        if (odf.getAT_3_gucu() != -1) {
            c = 0;
            for (int i = 0; i < guc.length; i++) {
                if (guc[i].equals(String.valueOf(odf.getAT_3_gucu()))) {
                    editAT_3_gucu.setSelection(i);
                    c = 1;
                }
            }
            if (c == 0 && odf.getAT_3_gucu() != -1) {
                editAT_3_gucu.setSelection(0);
                editAT_3_gucudiger.setText(String.valueOf(odf.getAT_3_gucu()));
            }
        }
        c = 0;
        for (int i = 0; i < markalar.length; i++) {
            if (marka_kisa[i].equals(odf.getAT_3_Marka())) {
                spinnerAT_3_Marka.setSelection(i);
                c = 1;
            }
        }
        if (c == 0) {
            spinnerAT_3_Marka.setSelection(0);
            spinnerAT_3_Markadiger.setText(odf.getAT_3_Marka());
        }
        c = 0;
        for (int i = 0; i < siniflar.length; i++) {
            if (siniflar[i].equals(odf.getat_3_sinif())) {
                spinnerat_3_sinif.setSelection(i);
                c = 1;
            }
        }
        if (c == 0) {
            spinnerat_3_sinif.setSelection(0);
            spinnerat_3_sinifdiger.setText(odf.getat_3_sinif());
        }
        spinnerAT_3_Marka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = spinnerAT_3_Marka.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    spinnerAT_3_Markadiger.setVisibility(View.GONE);
                    marka3baslikdigerr.setVisibility(View.GONE);
                } else {
                    spinnerAT_3_Markadiger.setVisibility(View.VISIBLE);
                    marka3baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        spinnerat_3_sinif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = spinnerat_3_sinif.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    spinnerat_3_sinifdiger.setVisibility(View.GONE);
                    sinif3baslikdigerr.setVisibility(View.GONE);
                } else {
                    spinnerat_3_sinifdiger.setVisibility(View.VISIBLE);
                    sinif3baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
        editAT_3_gucu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String mselection = editAT_3_gucu.getSelectedItem().toString();
                if (!mselection.equals("Diger")) {
                    editAT_3_gucudiger.setVisibility(View.GONE);
                    gucu3baslikdigerr.setVisibility(View.GONE);
                } else {
                    editAT_3_gucudiger.setVisibility(View.VISIBLE);
                    gucu3baslikdigerr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SetDiger(1);
                if (!editAT_1_Oran.getText().toString().equals("") && !editAT_1_Orankusur.getText().toString().equals("")) {
                    odf.setAT_1_Oran(editAT_1_Oran.getText().toString() + "/" + editAT_1_Orankusur.getText().toString());
                } else {
                    editAT_1_Oran.setError("Boş olamaz!");
                    editAT_1_Orankusur.setError("Boş Olamaz!");
                }
                if (!editat_1_seri.getText().toString().equals("")) {
                    odf.setat_1_seri((editat_1_seri.getText().toString()));
                } else {
                    editat_1_seri.setError("Boş olamaz!");
                }
                if (!editat_1_imalyili.getText().toString().equals("") && editat_1_imalyili.getText().toString().length() == 4) {
                    odf.setat_1_imalyili(Integer.parseInt(editat_1_imalyili.getText().toString()));
                } else {
                    editat_1_imalyili.setError("4 haneli olmalı! ");
                }
                if (!editAT_1_gucu.getSelectedItem().toString().equals("Diger")) {
                    odf.setAT_1_gucu(Integer.parseInt(editAT_1_gucu.getSelectedItem().toString()));
                } else {
                    if (!editAT_1_gucudiger.getText().toString().equals("")) {
                        odf.setAT_1_gucu(Integer.parseInt(editAT_1_gucudiger.getText().toString()));
                    } else {
                        editAT_1_gucudiger.setError("Boş olamaz!");
                    }
                }
                if (!spinnerAT_1_Marka.getSelectedItem().toString().equals("Diger")) {
                    for (int i = 0; i < markalar.length; i++) {
                        if (markalar[i].equals(spinnerAT_1_Marka.getSelectedItem().toString())) {
                            odf.setAT_1_Marka(marka_kisa[i]);
                            break;
                        }
                    }
                } else {
                    if (!spinnerAT_1_Markadiger.getText().toString().equals("")) {
                        odf.setAT_1_Marka(spinnerAT_1_Markadiger.getText().toString());
                    } else {
                        spinnerAT_1_Markadiger.setError("Boş olamaz!");
                    }
                }
                //odf.setAT_1_Marka(spinnerAT_1_Marka.getSelectedItem().toString());
                if (!spinnerat_1_sinif.getSelectedItem().toString().equals("Diger")) {
                    odf.setat_1_sinif(spinnerat_1_sinif.getSelectedItem().toString());
                } else {
                    if (!spinnerat_1_sinifdiger.getText().toString().equals("")) {
                        odf.setat_1_sinif(spinnerat_1_sinifdiger.getText().toString());
                    } else {
                        spinnerat_1_sinifdiger.setError("Boş olamaz!");
                    }
                }
                if (!editAT_2_Oran.getText().toString().equals("") && !editAT_2_Orankusur.getText().toString().equals("")) {
                    odf.setAT_2_Oran(editAT_2_Oran.getText().toString() + "/" + editAT_2_Orankusur.getText().toString());
                } else {
                    editAT_2_Oran.setError("Boş olamaz!");
                    editAT_2_Orankusur.setError("Boş Olamaz!");
                }
                if (!editat_2_seri.getText().toString().equals("")) {
                    odf.setat_2_seri((editat_2_seri.getText().toString()));
                } else {
                    editat_2_seri.setError("Boş olamaz!");
                }
                if (!editat_2_imalyili.getText().toString().equals("") && editat_2_imalyili.getText().toString().length() == 4) {
                    odf.setat_2_imalyili(Integer.parseInt(editat_2_imalyili.getText().toString()));
                } else {
                    editat_2_imalyili.setError("4 haneli olmalı! ");
                }
                if (!editAT_2_gucu.getSelectedItem().toString().equals("Diger")) {
                    odf.setAT_2_gucu(Integer.parseInt(editAT_2_gucu.getSelectedItem().toString()));
                } else {
                    if (!editAT_2_gucudiger.getText().toString().equals("")) {
                        odf.setAT_2_gucu(Integer.parseInt(editAT_2_gucudiger.getText().toString()));
                    } else {
                        editAT_2_gucudiger.setError("Boş olamaz!");
                    }
                }
                if (!spinnerAT_2_Marka.getSelectedItem().toString().equals("Diger")) {
                    for (int i = 0; i < markalar.length; i++) {
                        if (markalar[i].equals(spinnerAT_2_Marka.getSelectedItem().toString())) {
                            odf.setAT_2_Marka(marka_kisa[i]);
                            break;
                        }
                    }
                } else {
                    if (!spinnerAT_2_Markadiger.getText().toString().equals("")) {
                        odf.setAT_2_Marka(spinnerAT_2_Markadiger.getText().toString());
                    } else {
                        spinnerAT_2_Markadiger.setError("Boş olamaz!");
                    }
                }
                //odf.setAT_2_Marka(spinnerAT_2_Marka.getSelectedItem().toString());
                if (!spinnerat_2_sinif.getSelectedItem().toString().equals("Diger")) {
                    odf.setat_2_sinif(spinnerat_2_sinif.getSelectedItem().toString());
                } else {
                    if (!spinnerat_2_sinifdiger.getText().toString().equals("")) {
                        odf.setat_2_sinif(spinnerat_2_sinifdiger.getText().toString());
                    } else {
                        spinnerat_2_sinifdiger.setError("Boş olamaz!");
                    }
                }

                if (!editAT_3_Oran.getText().toString().equals("") && !editAT_3_Orankusur.getText().toString().equals("")) {
                    odf.setAT_3_Oran(editAT_3_Oran.getText().toString() + "/" + editAT_3_Orankusur.getText().toString());
                } else {
                    editAT_3_Oran.setError("Boş olamaz!");
                    editAT_3_Orankusur.setError("Boş Olamaz!");
                }
                if (!editat_3_seri.getText().toString().equals("")) {
                    odf.setat_3_seri((editat_3_seri.getText().toString()));
                } else {
                    editat_3_seri.setError("Boş olamaz!");
                }
                if (!editat_3_imalyili.getText().toString().equals("") && editat_3_imalyili.getText().toString().length() == 4) {
                    odf.setat_3_imalyili(Integer.parseInt(editat_3_imalyili.getText().toString()));
                } else {
                    editat_3_imalyili.setError("4 haneli olmalı! ");
                }
                if (!editAT_3_gucu.getSelectedItem().toString().equals("Diger")) {
                    odf.setAT_3_gucu(Integer.parseInt(editAT_3_gucu.getSelectedItem().toString()));
                } else {
                    if (!editAT_3_gucudiger.getText().toString().equals("")) {
                        odf.setAT_3_gucu(Integer.parseInt(editAT_3_gucudiger.getText().toString()));
                    } else {
                        editAT_3_gucudiger.setError("Boş olamaz!");
                    }
                }
                if (!spinnerAT_3_Marka.getSelectedItem().toString().equals("Diger")) {
                    for (int i = 0; i < markalar.length; i++) {
                        if (markalar[i].equals(spinnerAT_3_Marka.getSelectedItem().toString())) {
                            odf.setAT_3_Marka(marka_kisa[i]);
                            break;
                        }
                    }
                } else {
                    if (!spinnerAT_3_Markadiger.getText().toString().equals("")) {
                        odf.setAT_3_Marka(spinnerAT_3_Markadiger.getText().toString());
                    } else {
                        spinnerAT_3_Markadiger.setError("Boş olamaz!");
                    }
                }
                //odf.setAT_3_Marka(spinnerAT_3_Marka.getSelectedItem().toString());
                if (!spinnerat_3_sinif.getSelectedItem().toString().equals("Diger")) {
                    odf.setat_3_sinif(spinnerat_3_sinif.getSelectedItem().toString());
                } else {
                    if (!spinnerat_3_sinifdiger.getText().toString().equals("")) {
                        odf.setat_3_sinif(spinnerat_3_sinifdiger.getText().toString());
                    } else {
                        spinnerat_3_sinifdiger.setError("Boş olamaz!");
                    }
                }

                // H.Elif - oran ve küsürlerin hesaplanıp çarpan ile karşılaştırılması
                double control1 = Integer.parseInt(editAT_1_Oran.getText().toString().equals("") ? "1" : editAT_1_Oran.getText().toString()) / Integer.parseInt(editAT_1_Orankusur.getText().toString().equals("") ? "1" : editAT_1_Orankusur.getText().toString());
                double control2 = Integer.parseInt(editAT_2_Oran.getText().toString().equals("") ? "1" : editAT_2_Oran.getText().toString()) / Integer.parseInt(editAT_2_Orankusur.getText().toString().equals("") ? "1" : editAT_2_Orankusur.getText().toString());
                double control3 = Integer.parseInt(editAT_3_Oran.getText().toString().equals("") ? "1" : editAT_3_Oran.getText().toString()) / Integer.parseInt(editAT_3_Orankusur.getText().toString().equals("") ? "1" : editAT_3_Orankusur.getText().toString());

                odf.set_at_1_sinif_gucu(String.valueOf(odf.getat_1_sinif() + "/" + odf.getAT_1_gucu()));
                odf.set_at_2_sinif_gucu(String.valueOf(odf.getat_2_sinif() + "/" + odf.getAT_2_gucu()));
                odf.set_at_3_sinif_gucu(String.valueOf(odf.getat_3_sinif() + "/" + odf.getAT_3_gucu()));


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
                else
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuAboneBilgileriFragment(), null).commit();

            }
        });
        return view;
    }

    public void SetDiger(final int durum, String title) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        if (durum == 1 || durum == 4 || durum == 7) {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (durum == 2 || durum == 5 || durum == 8) {
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        dialog.setView(input);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (durum == 1) {
                    odf.setAT_1_Marka(input.getText().toString());
                } else if (durum == 2) {
                    odf.setat_1_sinif(input.getText().toString());
                } else if (durum == 3) {
                    odf.setAT_1_gucu(Integer.parseInt(input.getText().toString()));
                } else if (durum == 4) {
                    odf.setAT_2_Marka(input.getText().toString());
                } else if (durum == 5) {
                    odf.setat_2_sinif(input.getText().toString());
                } else if (durum == 6) {
                    odf.setAT_2_gucu(Integer.parseInt(input.getText().toString()));
                } else if (durum == 7) {
                    odf.setAT_3_Marka(input.getText().toString());
                } else if (durum == 8) {
                    odf.setat_3_sinif(input.getText().toString());
                } else if (durum == 9) {
                    odf.setAT_3_gucu(Integer.parseInt(input.getText().toString()));
                }
            }
        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public void Send() {
        if (!odf.getAT_1_Oran().equals("/") && odf.getAT_1_gucu() != -1 && !odf.getat_1_seri().equals("") && odf.getat_1_imalyili() != -1 && !odf.getAT_1_Marka().equals("") && !odf.getat_1_sinif().equals("")
                && !odf.getAT_2_Oran().equals("/") && odf.getAT_2_gucu() != -1 && !odf.getat_2_seri().equals("") && odf.getat_2_imalyili() != -1 && !odf.getAT_2_Marka().equals("") && !odf.getat_2_sinif().equals("")
                && !odf.getAT_3_Oran().equals("/") && odf.getAT_3_gucu() != -1 && !odf.getat_3_seri().equals("") && odf.getat_3_imalyili() != -1 && !odf.getAT_3_Marka().equals("") && !odf.getat_3_sinif().equals("")) {
            if (odf.getgerilim_trafosu_dur() == 1) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu3Fragment(), null).commit();
            } else {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuSokulenMuhurFragment(), null).commit();
            }
        } else {
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
