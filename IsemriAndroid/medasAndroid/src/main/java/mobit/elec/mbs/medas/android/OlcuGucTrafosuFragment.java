package mobit.elec.mbs.medas.android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mobit.Globals;

import java.util.ArrayList;
import java.util.List;

import mobit.eemr.OlcuDevreForm;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacMarka;

//H. Elif 06.12.2021
public class OlcuGucTrafosuFragment extends Fragment { //H.Elif 10.11.2021

    Button back_btn;
    Button save_btn;

    EditText trafoMarkasi_txt;
    EditText trafoSerino_txt;
    EditText trafoGucu_txt;
    EditText trafoGerilimi_txt;
    EditText trafoImalYili_txt;

    OlcuDevreForm odf;
    IElecApplication app = null;
    IIsemriIslem isemriIslem;
    List<ISayacMarka> markalar;
    ArrayList<String> marka_adi_list = new ArrayList<>();

    public OlcuGucTrafosuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olcu_guc_trafosu, container, false);
        odf = new OlcuDevreForm();
        back_btn = view.findViewById(R.id.back_btn);
        save_btn = view.findViewById(R.id.save_btn);

        trafoMarkasi_txt = view.findViewById(R.id.trafoMarkasi_text);
        trafoSerino_txt = view.findViewById(R.id.trafoSerino_text);
        trafoGucu_txt = view.findViewById(R.id.trafoGucu_text);
        trafoGerilimi_txt = view.findViewById(R.id.trafoGerilimi_text);
        trafoImalYili_txt = view.findViewById(R.id.trafoImalYili_text);

        try {
            trafoMarkasi_txt.setText(odf.get_trafoMarkasi());
            trafoSerino_txt.setText(odf.get_trafoSerino() == -1 ? "": String.valueOf(odf.get_trafoSerino()));
            trafoGucu_txt.setText(odf.get_trafoGucu() == -1 ? "": String.valueOf(odf.get_trafoGucu()));
            trafoGerilimi_txt.setText(odf.get_trafoGerilimi() == -1 ? "": String.valueOf(odf.get_trafoGerilimi()));
            trafoImalYili_txt.setText(odf.get_trafoImalYili() == -1 ? "": String.valueOf(odf.get_trafoImalYili()));
        } catch (Exception ex) {
        }


        app = (IElecApplication) Globals.app;
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!trafoMarkasi_txt.getText().toString().equals("")) {
                    odf.set_trafoMarkasi(trafoMarkasi_txt.getText().toString());
                }

                if (!trafoSerino_txt.getText().toString().equals("")) {
                    odf.set_trafoSerino(Integer.parseInt(trafoSerino_txt.getText().toString()));
                }

                if (!trafoGucu_txt.getText().toString().equals("")) {
                    odf.set_trafoGucu(Integer.parseInt(trafoGucu_txt.getText().toString()));
                }

                if (!trafoGerilimi_txt.getText().toString().equals("")) {
                    odf.set_trafoGerilimi(Integer.parseInt(trafoGerilimi_txt.getText().toString()));
                }

                if (!trafoImalYili_txt.getText().toString().equals("") && trafoImalYili_txt.getText().toString().length() == 4) {
                    odf.set_trafoImalYili(Integer.parseInt(trafoImalYili_txt.getText().toString()));
                } else if(!trafoImalYili_txt.getText().toString().equals("") && trafoImalYili_txt.getText().toString().length() != 4) {
                    trafoImalYili_txt.setError("4 haneli olmalı! ");
                }


                if (odf.get_trafoMarkasi().equals("") && odf.get_trafoSerino() == -1 && odf.get_trafoGucu() == -1
                        && odf.get_trafoGerilimi() == -1 && odf.get_trafoImalYili() == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Uyarı!");
                    dialog.setMessage("Güç Trafosu Bilgilerini Boş Geçiyorsunuz!");
                    dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Send();
                        }
                    });
                    dialog.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    if(trafoImalYili_txt.getText().toString().length() == 0 || trafoImalYili_txt.getText().toString().length() == 4) {
                        Send();
                    }
                }

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
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

    public void Send() {
        if (odf.getakim_trafosu_dur() == 1) {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu2Fragment(), null).commit();
        } else if (odf.getgerilim_trafosu_dur() == 1) {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu3Fragment(), null).commit();
        } else {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuSokulenMuhurFragment(), null).commit();
        }
    }

}