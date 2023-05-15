package mobit.elec.mbs.medas.android;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobit.Callback;
import com.mobit.Globals;
import com.mobit.IForm;

import java.util.ArrayList;
import java.util.List;

import mobit.eemr.IReadResult;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.AltEmirTuru;
import mobit.elec.ElecApplication;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.android.IsemriListeActivity;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.IMbsApplication;


/**
 * A simple {@link Fragment} subclass.
 */
public class OlcuHomeFragment extends Fragment {

    private Button button;
    IElecApplication app = null;
    OlcuDevreForm odf;

    protected mobit.elec.medas.ws.SayacZimmetBilgi medasServer;
    public String emir_turu;
    private IIsemri2 isemri;
    public IIsemri isemri2;


    public OlcuHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olcu_home, container, false);
        button = view.findViewById(R.id.bf1);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio1);
        RadioGroup radioGroup2 = (RadioGroup) view.findViewById(R.id.radio2);
        RadioGroup radioGroup3 = (RadioGroup) view.findViewById(R.id.radio3);
        RadioGroup radioGroup4 = (RadioGroup) view.findViewById(R.id.radio4);
        RadioGroup radioGroup5 = (RadioGroup) view.findViewById(R.id.radio5);
        RadioGroup radioGroup6 = (RadioGroup) view.findViewById(R.id.radio6); // H.Elif 08.11.2021
        RadioGroup radioGroup7 = (RadioGroup) view.findViewById(R.id.radio7); // H.Elif 08.11.2021
        RadioGroup radioGroup8 = (RadioGroup) view.findViewById(R.id.radio8); // H.Elif 08.11.2021
        odf = new OlcuDevreForm();

        RadioButton radioButtone = (RadioButton) view.findViewById(R.id.tesis_enerji_dur_e);
        RadioButton radioButtonh = (RadioButton) view.findViewById(R.id.tesis_enerji_dur_h);
        RadioButton radioButtone2 = (RadioButton) view.findViewById(R.id.pano_muhur_dur_e);
        RadioButton radioButtonh2 = (RadioButton) view.findViewById(R.id.pano_muhur_dur_h);
        RadioButton radioButtone3 = (RadioButton) view.findViewById(R.id.sayac_muhur_dur_e);
        RadioButton radioButtonh3 = (RadioButton) view.findViewById(R.id.sayac_muhur_dur_h);
        RadioButton radioButtone4 = (RadioButton) view.findViewById(R.id.gerilim_trafosu_dur_e);
        RadioButton radioButtonh4 = (RadioButton) view.findViewById(R.id.gerilim_trafosu_dur_h);
        RadioButton radioButtone5 = (RadioButton) view.findViewById(R.id.sayac_ariza_dur_e);
        RadioButton radioButtonh5 = (RadioButton) view.findViewById(R.id.sayac_ariza_dur_h);
        final RadioButton radioButtone6 = (RadioButton) view.findViewById(R.id.akim_trafosu_dur_e); // H.Elif 08.11.2021
        RadioButton radioButtonh6 = (RadioButton) view.findViewById(R.id.akim_trafosu_dur_h); // H.Elif 08.11.2021
        RadioButton radioButtone7 = (RadioButton) view.findViewById(R.id.guc_trafosu_dur_e); // H.Elif 08.11.2021
        RadioButton radioButtonh7 = (RadioButton) view.findViewById(R.id.guc_trafosu_dur_h); // H.Elif 08.11.2021
        RadioButton radioButtone8 = (RadioButton) view.findViewById(R.id.guc_tespiti_dur_e); // H.Elif 08.11.2021
        RadioButton radioButtonh8 = (RadioButton) view.findViewById(R.id.guc_tespiti_dur_h); // H.Elif 08.11.2021

        app = (IElecApplication) Globals.app;
/*
        //Onur İLK AÇMA EMİR TÜRÜNE EKLENEN AKIM GERİLİM BİLGİSİ
        if (app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacTakma)) {
            radioButtone6.setEnabled(true);
            radioButtonh6.setEnabled(false);
            radioButtone6.setChecked(true);
            radioButtonh6.setChecked(false);

            radioButtone.setEnabled(false);
            radioButtonh.setEnabled(false);
            radioButtone2.setEnabled(false);
            radioButtonh2.setEnabled(false);
            radioButtone3.setEnabled(false);
            radioButtonh3.setEnabled(false);
            radioButtone4.setEnabled(false);
            radioButtonh4.setEnabled(false);
            radioButtone5.setEnabled(false);
            radioButtonh5.setEnabled(false);
            radioButtone7.setEnabled(false);
            radioButtonh7.setEnabled(false);
            radioButtone8.setEnabled(false);
            radioButtonh8.setEnabled(false);

            radioButtone.setChecked(false);
            radioButtonh.setChecked(true);
            radioButtone2.setChecked(false);
            radioButtonh2.setChecked(true);
            radioButtone3.setChecked(false);
            radioButtonh3.setChecked(true);
            radioButtone4.setChecked(false);
            radioButtonh4.setChecked(true);
            radioButtone5.setChecked(false);
            radioButtonh5.setChecked(true);
            radioButtone7.setChecked(false);
            radioButtonh7.setChecked(true);
            radioButtone8.setChecked(false);
            radioButtonh8.setChecked(true);

        }else if (app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacDegistir)){

            if (app.getActiveIsemri().getCARPAN() > 1) {
                radioButtone6.setEnabled(true);
                radioButtonh6.setEnabled(false);
                radioButtone6.setChecked(true);
                radioButtonh6.setChecked(false);
            } else {
                radioButtone6.setEnabled(false);
                radioButtonh6.setEnabled(true);
                radioButtone6.setChecked(false);
                radioButtonh6.setChecked(true);
            }

        }else{
            if (app.getActiveIsemri().getCARPAN() > 1) {
                radioButtone6.setChecked(true);
                radioButtonh6.setChecked(false);
                odf.setakim_trafosu_dur(1);
            } else {
                radioButtone6.setChecked(false);
                radioButtonh6.setChecked(true);
                odf.setakim_trafosu_dur(0);
            }
        }
*/

//        radioButtone4.setEnabled(false);
//        radioButtonh4.setEnabled(false);

        // og ag durumuna göre biz açıp kapatıyoruz
//        if((app.getActiveIsemri().getOG_DUR() == 0 && app.getActiveIsemri().getOLCUM_KODU() == 0) || (app.getActiveIsemri().getOG_DUR() == 1 && app.getActiveIsemri().getOLCUM_KODU() == 0)) {
//            radioButtone4.setChecked(false);
//            radioButtonh4.setChecked(true);
//            odf.setgerilim_trafosu_dur(0);
//        } else if(app.getActiveIsemri().getOG_DUR() == 1 && app.getActiveIsemri().getOLCUM_KODU() == 1) {
//            radioButtone4.setChecked(true);
//            radioButtonh4.setChecked(false);
//            odf.setgerilim_trafosu_dur(1);
//        }

        if (app.getActiveIsemri().getCARPAN() > 1) {
            radioButtone6.setChecked(true);
            radioButtonh6.setChecked(false);
            radioButtone6.setEnabled(true);
            radioButtonh6.setEnabled(false);
            odf.setakim_trafosu_dur(1);
        } else {
            radioButtone6.setChecked(false);
            radioButtonh6.setChecked(true);
            radioButtone6.setEnabled(false);
            radioButtonh6.setEnabled(true);
            odf.setakim_trafosu_dur(0);
        }

        if (odf.gettesis_enerji_dur() != -1) {
            if (odf.gettesis_enerji_dur() == 1) {
                radioButtone.setChecked(true);
            } else {
                radioButtonh.setChecked(true);
            }
        }
        if (odf.getpano_muhur_dur() != -1) {
            if (odf.getpano_muhur_dur() == 1) {
                radioButtone2.setChecked(true);
            } else {
                radioButtonh2.setChecked(true);
            }
        }
        if (odf.getsayac_muhur_dur() != -1) {
            if (odf.getsayac_muhur_dur() == 1) {
                radioButtone3.setChecked(true);
            } else {
                radioButtonh3.setChecked(true);
            }
        }
        if (odf.getgerilim_trafosu_dur() != -1) {
            if (odf.getgerilim_trafosu_dur() == 1) {
                radioButtone4.setChecked(true);
            } else {
                radioButtonh4.setChecked(true);
            }
        }
        if (odf.getsayac_ariza_dur() != -1) {
            if (odf.getsayac_ariza_dur() == 1) {
                radioButtone5.setChecked(true);
            } else {
                radioButtonh5.setChecked(true);
            }
        }
//        if (odf.getakim_trafosu_dur() != -1) {
//            if (odf.getakim_trafosu_dur() == 1) {
//                radioButtone6.setChecked(true);
//            } else {
//                radioButtonh6.setChecked(true);
//            }
//        }
        if (odf.getguc_trafosu_dur() != -1) {
            if (odf.getguc_trafosu_dur() == 1) {
                radioButtone7.setChecked(true);
            } else {
                radioButtonh7.setChecked(true);
            }
        }
        if (odf.getguc_tespiti_dur() != -1) {
            if (odf.getguc_tespiti_dur() == 1) {
                radioButtone8.setChecked(true);
            } else {
                radioButtonh8.setChecked(true);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.tesis_enerji_dur_e:
                        odf.settesis_enerji_dur(1);
                        break;
                    case R.id.tesis_enerji_dur_h:
                        odf.settesis_enerji_dur(0);
                        break;
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.pano_muhur_dur_e:
                        odf.setpano_muhur_dur(1);
                        break;
                    case R.id.pano_muhur_dur_h:
                        odf.setpano_muhur_dur(0);
                        break;
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.sayac_muhur_dur_e:
                        odf.setsayac_muhur_dur(1);
                        break;
                    case R.id.sayac_muhur_dur_h:
                        odf.setsayac_muhur_dur(0);
                        break;
                }
            }
        });

        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.gerilim_trafosu_dur_e:
                        odf.setgerilim_trafosu_dur(1);
                        break;
                    case R.id.gerilim_trafosu_dur_h:
                        odf.setgerilim_trafosu_dur(0);
                        break;
                }
            }
        });

        radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.sayac_ariza_dur_e:
                        odf.setsayac_ariza_dur(1);
                        break;
                    case R.id.sayac_ariza_dur_h:
                        odf.setsayac_ariza_dur(0);
                        break;
                }
            }
        });
        radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.akim_trafosu_dur_e:
                        odf.setakim_trafosu_dur(1);
                        break;
                    case R.id.akim_trafosu_dur_h:
                        odf.setakim_trafosu_dur(0);
                        break;
                }
            }
        });

        radioGroup7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.guc_trafosu_dur_e:
                        odf.setguc_trafosu_dur(1);
                        break;
                    case R.id.guc_trafosu_dur_h:
                        odf.setguc_trafosu_dur(0);
                        break;
                }
            }
        });
        radioGroup8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.guc_tespiti_dur_e:
                        odf.setguc_tespiti_dur(1);
                        break;
                    case R.id.guc_tespiti_dur_h:
                        odf.setguc_tespiti_dur(0);
                        break;
                }
            }
        });

        try { // H.Elif evet hayırlarda bazı maddeleri otomatik geçmek için yapıldı.
//            IIsemri isemriIslem = app.getActiveIsemri();
//            if (isemriIslem.getCARPAN() > 1) { // Akım trafosu durumu
//                odf.setakim_trafosu_dur(1);
//            } else {
//                odf.setakim_trafosu_dur(0);
//            }
//
//            String result = medasServer.OrtakTrafoKontrol(isemriIslem.getTESISAT_NO(), isemriIslem.getSAHA_ISEMRI_NO());
//            if(result == "1")
//            {
//                odf.setguc_trafosu_dur(1); // Güç trafosu durumu
//            }
//            else{
//                odf.setguc_trafosu_dur(0);
//            }
//
//            if(result == "1" && isemriIslem.getCARPAN() > 1)
//            {
//                odf.setgerilim_trafosu_dur("1"); // Gerilim trafosu durumu
//            }
//            else {
//                odf.setgerilim_trafosu_dur("0");
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((odf.gettesis_enerji_dur() != -1 && odf.getpano_muhur_dur() != -1 && odf.getsayac_muhur_dur() != -1 && odf.getsayac_ariza_dur() != -1 &&
                        odf.getgerilim_trafosu_dur() != -1) /*&& !app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacTakma) && !app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacDegistir)*/) {
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuAboneBilgileriFragment(), null).commit();
                /*} else if (app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacTakma)) { // onur ilk açma için eklendi test edilecek
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuAboneBilgileriFragment(), null).commit();
                    odf.set_disaridan_olcu_dur(1);
                    odf.settesis_enerji_dur(0);
                    odf.setpano_muhur_dur(0);
                    odf.setsayac_muhur_dur(0);
                    odf.setsayac_ariza_dur(0);
                    odf.setgerilim_trafosu_dur(0);
                    odf.setakim_trafosu_dur(1);
                    odf.setguc_trafosu_dur(0);
                    odf.setguc_tespiti_dur(0);
                }else if ((odf.gettesis_enerji_dur() != -1 && odf.getpano_muhur_dur() != -1 && odf.getsayac_muhur_dur() != -1 && odf.getsayac_ariza_dur() != -1 &&
                        odf.getgerilim_trafosu_dur() != -1) && app.getActiveIsemri().getISLEM_TIPI().equals(IslemTipi.SayacDegistir)){
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuAboneBilgileriFragment(), null).commit();
                    odf.set_disaridan_olcu_dur(1);
                    if (app.getActiveIsemri().getCARPAN() > 1){
                        odf.setakim_trafosu_dur(1);
                    }else {
                        odf.setakim_trafosu_dur(0);
                    }*/
                }else {
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
        });
        return view;
    }


}
