package mobit.elec.mbs.medas.android;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.mobit.DbHelper;
        import com.mobit.Globals;

        import java.util.ArrayList;
        import java.util.List;

        import mobit.eemr.OlcuDevreForm;
        import mobit.elec.IElecApplication;
        import mobit.elec.IIsemri;
        import mobit.elec.IIsemriIslem;
        import mobit.elec.IMuhur;
        import mobit.elec.ISayacMarka;
        import mobit.elec.enums.EndeksTipi;
        import mobit.elec.mbs.get.sayac_marka;
        import mobit.elec.mbs.medas.IMedasApplication;
        import mobit.elec.medas.ws.SayacZimmetBilgi;

//H. Elif 12.01.2021
public class OlcuTakilanSayacEndexFragment extends Fragment {

    Button back_btn;
    Button save_btn;

    EditText takilanSerino;
    Spinner takilanMarkasi;
    EditText takilanAkimO;
    EditText takilanGerilimO;
    EditText takilanSayacCarpani;
    EditText takilanHaneAdeti;
    EditText takilanImalTarihi;

    Spinner muhurSeri;
    EditText editMuhurNo;

    EditText editGunduzTam;
    EditText editGunduzKusurat;
    EditText editPuantTam;
    EditText editPuantKusurat;
    EditText editGeceTam;
    EditText editGeceKusurat;
    EditText editKapasitifTam;
    EditText editKapasitifKusurat;
    EditText editEnduktifTam;
    EditText editEnduktifKusurat;

    EditText editGunduzTams;
    EditText editGunduzKusurats;
    EditText editPuantTams;
    EditText editPuantKusurats;
    EditText editGeceTams;
    EditText editGeceKusurats;
    EditText editKapasitifTams;
    EditText editKapasitifKusurats;
    EditText editEnduktifTams;
    EditText editEnduktifKusurats;

    ArrayAdapter<String> spinnerAdaptersayacMarka;


    SayacTakmaActivity2 syc;
    OlcuDevreForm odf;
    IElecApplication app = null;
    IMedasApplication app2 = null;
    IIsemriIslem isemriIslem;
    List<ISayacMarka> markalar;
    ArrayList<String> marka_adi_list = new ArrayList<>();

    ArrayAdapter<CharSequence> adapter;

    public OlcuTakilanSayacEndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olcu_takilan_sayac_endex, container, false);
        odf = new OlcuDevreForm();
        back_btn = view.findViewById(R.id.back_btn);
        save_btn = view.findViewById(R.id.save_btn);

        takilanSerino = view.findViewById(R.id.takilanSerino);
        takilanMarkasi = view.findViewById(R.id.takilanMarkasi);
        takilanAkimO = view.findViewById(R.id.takilanAkimO);
        takilanGerilimO = view.findViewById(R.id.takilanGerilimO);
        takilanSayacCarpani = view.findViewById(R.id.takilanSayacCarpani);
        takilanHaneAdeti = view.findViewById(R.id.takilanHaneAdeti);
        takilanImalTarihi = view.findViewById(R.id.takilanImalTarihi);

        muhurSeri = view.findViewById(R.id.muhurSeri);
        editMuhurNo = view.findViewById(R.id.editMuhurNo);

        editGunduzTam = view.findViewById(R.id.editGunduzTam);
        editGunduzKusurat = view.findViewById(R.id.editGunduzKusurat);
        editPuantTam = view.findViewById(R.id.editPuantTam);
        editPuantKusurat = view.findViewById(R.id.editPuantKusurat);
        editGeceTam = view.findViewById(R.id.editGeceTam);
        editGeceKusurat = view.findViewById(R.id.editGeceKusurat);
        editKapasitifTam = view.findViewById(R.id.editKapasitifTam);
        editKapasitifKusurat = view.findViewById(R.id.editKapasitifKusurat);
        editEnduktifTam = view.findViewById(R.id.editEnduktifTam);
        editEnduktifKusurat = view.findViewById(R.id.editEnduktifKusurat);

        editGunduzTams = view.findViewById(R.id.editGunduzTams);
        editGunduzKusurats = view.findViewById(R.id.editGunduzKusurats);
        editPuantTams = view.findViewById(R.id.editPuantTams);
        editPuantKusurats = view.findViewById(R.id.editPuantKusurats);
        editGeceTams = view.findViewById(R.id.editGeceTams);
        editGeceKusurats = view.findViewById(R.id.editGeceKusurats);
        editKapasitifTams = view.findViewById(R.id.editKapasitifTams);
        editKapasitifKusurats = view.findViewById(R.id.editKapasitifKusurats);
        editEnduktifTams = view.findViewById(R.id.editEnduktifTams);
        editEnduktifKusurats = view.findViewById(R.id.editEnduktifKusurats);


        app = (IElecApplication) Globals.app;
        app2 = (IMedasApplication) Globals.app;
        IIsemri data = app.getActiveIsemri();

        try {
            takilanSerino.setText(odf.get_takilan_no());
            takilanMarkasi.setSelection(spinnerAdaptersayacMarka.getPosition(odf.get_takilan_marka()));
            takilanAkimO.setText(odf.get_takilan_akim());
            takilanGerilimO.setText(odf.get_takilan_gerilim());
            takilanSayacCarpani.setText(odf.get_takilan_carpan());
            takilanHaneAdeti.setText(odf.get_takilan_haneadeti());
            takilanImalTarihi.setText(odf.get_takilan_imalyili());
        } catch (Exception ex) {
        }

        try{
            ArrayAdapter<SayacZimmetBilgi.muhurBilgi> spinnerAdapter =
                    new ArrayAdapter<SayacZimmetBilgi.muhurBilgi>(getContext(), android.R.layout.simple_spinner_item, app2.getMuhurSeriler());
            muhurSeri.setAdapter(spinnerAdapter);
        }catch (Exception ex) {
        }

        try {
            markalar = DbHelper.SelectAll(app2.getConnection(), sayac_marka.class);
            for (ISayacMarka sm : markalar) {
                marka_adi_list.add(sm.getSAYAC_MARKA_ADI());
            }

            spinnerAdaptersayacMarka = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, marka_adi_list);
            spinnerAdaptersayacMarka.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            takilanMarkasi.setAdapter(spinnerAdaptersayacMarka);
            spinnerAdaptersayacMarka.notifyDataSetChanged();

            for (ISayacMarka sm : markalar) {
                String marka_adi = sm.getSAYAC_MARKA_ADI();
                if (String.valueOf(data.getSAYACLAR().getSayaclar().get(0).getMARKA()).equals(marka_adi)) {
                    takilanMarkasi.setSelection(spinnerAdaptersayacMarka.getPosition(marka_adi));
                }
            }

            takilanMarkasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    takilanMarkasi.setSelection(arg2);
                    odf.set_takilan_marka(markalar.get(arg2).getSAYAC_MARKA_KODU().toString());
                    spinnerAdaptersayacMarka.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!takilanSerino.getText().toString().equals("")) {
                    odf.set_takilan_no(takilanSerino.getText().toString());
                }

                if (!takilanAkimO.getText().toString().equals("")) {
                    odf.set_takilan_akim(takilanAkimO.getText().toString());
                }

                if (!takilanGerilimO.getText().toString().equals("")) {
                    odf.set_takilan_gerilim(takilanGerilimO.getText().toString());
                }

                if (!takilanSayacCarpani.getText().toString().equals("")) {
                    odf.set_takilan_carpan(takilanSayacCarpani.getText().toString());
                }

                if (!takilanHaneAdeti.getText().toString().equals("")) {
                    odf.set_takilan_haneadeti(takilanHaneAdeti.getText().toString());
                }

                if (!takilanImalTarihi.getText().toString().equals("") && takilanImalTarihi.getText().toString().length() == 4) {
                    odf.set_takilan_imalyili(takilanImalTarihi.getText().toString());
                } else if(!takilanImalTarihi.getText().toString().equals("") && takilanImalTarihi.getText().toString().length() != 4) {
                    takilanImalTarihi.setError("4 haneli olmalı! ");
                }

                odf.set_yeni_muhur(muhurSeri.getSelectedItem().toString() + " / " + editMuhurNo.getText().toString());

                OdfEndeks();

                if(takilanImalTarihi.getText().toString().length() == 0 || takilanImalTarihi.getText().toString().length() == 4) {
                    Send();
                }

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuAboneBilgileriFragment(), null).commit();
            }
        });
        return view;
    }

    public void OdfEndeks() {

        double t = 0;
        String t1 = "", t2 = "", t3 = "", kapasitif = "", enduktif = "";

        if(!editGunduzTam.getText().toString().isEmpty() && !editGunduzKusurat.getText().toString().isEmpty()) {
            t1 = editGunduzTam.getText().toString() + "." + editGunduzKusurat.getText().toString();
            t += Double.parseDouble(t1);
        }
        if(!editPuantTam.getText().toString().isEmpty() && !editPuantKusurat.getText().toString().isEmpty()) {
            t2 = editPuantTam.getText().toString() + "." + editPuantKusurat.getText().toString();
            t += Double.parseDouble(t2);
        }
        if(!editGeceTam.getText().toString().isEmpty() && !editGeceKusurat.getText().toString().isEmpty()) {
            t3 = editGeceTam.getText().toString() + "." + editGeceKusurat.getText().toString();
            t += Double.parseDouble(t3);
        }
        if(!editKapasitifTam.getText().toString().isEmpty() && !editKapasitifKusurat.getText().toString().isEmpty())
            kapasitif = editKapasitifTam.getText().toString() + "." + editKapasitifKusurat.getText().toString();

        if(!editEnduktifTam.getText().toString().isEmpty() && !editEnduktifKusurat.getText().toString().isEmpty())
            enduktif = editEnduktifTam.getText().toString() + "." + editEnduktifKusurat.getText().toString();

        OlcuDevreForm odf = new OlcuDevreForm();
        odf.set_takilan_t(String.format("%.3f", t));
        odf.set_takilan_t1(t1);
        odf.set_takilan_t2(t2);
        odf.set_takilan_t3(t3);
        odf.set_takilan_kapasitif(kapasitif);
        odf.set_takilan_enduktif(enduktif);

        // mevcut / sökülen
        t = 0;
        if(!editGunduzTams.getText().toString().isEmpty() && !editGunduzKusurats.getText().toString().isEmpty()) {
            t1 = editGunduzTams.getText().toString() + "." + editGunduzKusurats.getText().toString();
            t += Double.parseDouble(t1);
        }
        if(!editPuantTams.getText().toString().isEmpty() && !editPuantKusurats.getText().toString().isEmpty()) {
            t2 = editPuantTams.getText().toString() + "." + editPuantKusurats.getText().toString();
            t += Double.parseDouble(t2);
        }
        if(!editGeceTams.getText().toString().isEmpty() && !editGeceKusurats.getText().toString().isEmpty()) {
            t3 = editGeceTams.getText().toString() + "." + editGeceKusurats.getText().toString();
            t += Double.parseDouble(t3);
        }
        if(!editKapasitifTams.getText().toString().isEmpty() && !editKapasitifKusurats.getText().toString().isEmpty())
            kapasitif = editKapasitifTams.getText().toString() + "." + editKapasitifKusurats.getText().toString();

        if(!editEnduktifTams.getText().toString().isEmpty() && !editEnduktifKusurats.getText().toString().isEmpty())
            enduktif = editEnduktifTams.getText().toString() + "." + editEnduktifKusurats.getText().toString();

        odf.set_t(String.format("%.3f", t));
        odf.set_t1(t1);
        odf.set_t2(t2);
        odf.set_t3(t3);
        odf.set_kapasitif(kapasitif);
        odf.set_enduktif(enduktif);
    }

    public void Send() {
        if (odf.getguc_trafosu_dur() == 1) {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuGucTrafosuFragment(), null).commit();
        } else {
            if (odf.getakim_trafosu_dur() == 1) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu2Fragment(), null).commit();
            } else if (odf.getgerilim_trafosu_dur() == 1) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu3Fragment(), null).commit();
            } else {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuSokulenMuhurFragment(), null).commit();
            }
        }
    }
}


