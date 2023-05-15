package mobit.elec.mbs.medas.android;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobit.ICallbackEx;

import mobit.eemr.OlcuDevreForm;
import mobit.elec.Globals;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Olcu4SonFragment extends Fragment {

    IMedasApplication app;
    SayacZimmetBilgi szb;
    protected mobit.elec.medas.ws.SayacZimmetBilgi medasServer;
    Button button;
    Button button2;
    ProgressDialog progressDialog;
    int hata = 1;
    String title = "";
    String message = "";


    EditText editprimer_akim_1;
    EditText editsekonde_akim_1;
    EditText editsayac_akim_1;
    EditText editsayac_gerili_1;

    EditText editprimer_akim_2;
    EditText editsekonde_akim_2;
    EditText editsayac_akim_2;
    EditText editsayac_gerili_2;

    EditText editprimer_akim_3;
    EditText editsekonde_akim_3;
    EditText editsayac_akim_3;
    EditText editsayac_gerili_3;

    Spinner editpolarite_1;
    Spinner editpolarite_2;
    Spinner editpolarite_3;

    TextView tmarka1baslik;
    TextView toran1baslik;
    TextView tguc1baslik;
    TextView tsinif1baslik;

    TextView tmarka2baslik;
    TextView toran2baslik;
    TextView tguc2baslik;
    TextView tsinif2baslik;

    TextView tmarka3baslik;
    TextView toran3baslik;
    TextView tguc3baslik;
    TextView tsinif3baslik;

    TextView polaritesinif1baslik;
    TextView polaritesinif2baslik;
    TextView polaritesinif3baslik;

    String res = "";

    OlcuDevreForm odf;

    public Olcu4SonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olcu4_son, container, false);
        if (!(Globals.app instanceof IMedasApplication)) {
            getActivity().finish();
            return null;
        }
        app = (IMedasApplication) Globals.app;
        medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
        szb = new SayacZimmetBilgi();
        odf = new OlcuDevreForm();
        button = view.findViewById(R.id.bf4);
        button2 = view.findViewById(R.id.gd3);
        tmarka1baslik = view.findViewById(R.id.marka1baslik);
        toran1baslik = view.findViewById(R.id.oran1baslik);
        tguc1baslik = view.findViewById(R.id.guc1baslik);
        tsinif1baslik = view.findViewById(R.id.sinif1baslik);

        tmarka2baslik = view.findViewById(R.id.marka2baslik);
        toran2baslik = view.findViewById(R.id.oran2baslik);
        tguc2baslik = view.findViewById(R.id.guc2baslik);
        tsinif2baslik = view.findViewById(R.id.sinif2baslik);

        tmarka3baslik = view.findViewById(R.id.marka3baslik);
        toran3baslik = view.findViewById(R.id.oran3baslik);
        tguc3baslik = view.findViewById(R.id.guc3baslik);
        tsinif3baslik = view.findViewById(R.id.sinif3baslik);

        polaritesinif1baslik = view.findViewById(R.id.polaritesinif1baslik);
        polaritesinif2baslik = view.findViewById(R.id.polaritesinif2baslik);
        polaritesinif3baslik = view.findViewById(R.id.polaritesinif3baslik);


        tmarka1baslik.setText("Son " + tmarka1baslik.getText().toString());
        toran1baslik.setText("Son " + toran1baslik.getText().toString());
        tguc1baslik.setText("Son " + tguc1baslik.getText().toString());
        tsinif1baslik.setText("Son " + tsinif1baslik.getText().toString());

        tmarka2baslik.setText("Son " + tmarka2baslik.getText().toString());
        toran2baslik.setText("Son " + toran2baslik.getText().toString());
        tguc2baslik.setText("Son " + tguc2baslik.getText().toString());
        tsinif2baslik.setText("Son " + tsinif2baslik.getText().toString());

        tmarka3baslik.setText("Son " + tmarka3baslik.getText().toString());
        toran3baslik.setText("Son " + toran3baslik.getText().toString());
        tguc3baslik.setText("Son " + tguc3baslik.getText().toString());
        tsinif3baslik.setText("Son " + tsinif3baslik.getText().toString());

        editpolarite_1 = view.findViewById(R.id.polarite_1);
        editpolarite_2 = view.findViewById(R.id.polarite_2);
        editpolarite_3 = view.findViewById(R.id.polarite_3);

        editprimer_akim_1 = view.findViewById(R.id.primer_akim_1);
        editsekonde_akim_1 = view.findViewById(R.id.sekonde_akim_1);
        editsayac_akim_1 = view.findViewById(R.id.sayac_akim_1);
        editsayac_gerili_1 = view.findViewById(R.id.sayac_gerili_1);

        editprimer_akim_1.setText(odf.getson_primer_akim_1());
        editsekonde_akim_1.setText(odf.getson_sekonde_akim_1());
        editsayac_akim_1.setText(odf.getson_sayac_akim_1());
        editsayac_gerili_1.setText(odf.getson_sayac_gerili_1());


        editprimer_akim_2 = view.findViewById(R.id.primer_akim_2);
        editsekonde_akim_2 = view.findViewById(R.id.sekonde_akim_2);
        editsayac_akim_2 = view.findViewById(R.id.sayac_akim_2);
        editsayac_gerili_2 = view.findViewById(R.id.sayac_gerili_2);


        editprimer_akim_2.setText(odf.getson_primer_akim_2());
        editsekonde_akim_2.setText(odf.getson_sekonde_akim_2());
        editsayac_akim_2.setText(odf.getson_sayac_akim_2());
        editsayac_gerili_2.setText(odf.getson_sayac_gerili_2());


        editprimer_akim_3 = view.findViewById(R.id.primer_akim_3);
        editsekonde_akim_3 = view.findViewById(R.id.sekonde_akim_3);
        editsayac_akim_3 = view.findViewById(R.id.sayac_akim_3);
        editsayac_gerili_3 = view.findViewById(R.id.sayac_gerili_3);

        editprimer_akim_3.setText(odf.getson_primer_akim_3());
        editsekonde_akim_3.setText(odf.getson_sekonde_akim_3());
        editsayac_akim_3.setText(odf.getson_sayac_akim_3());
        editsayac_gerili_3.setText(odf.getson_sayac_gerili_3());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.polarite_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editpolarite_1.setAdapter(adapter);
        editpolarite_2.setAdapter(adapter);
        editpolarite_3.setAdapter(adapter);

        if(Double.valueOf(odf.getcarpan()) > 1) {
            editprimer_akim_1.setText(odf.getson_primer_akim_1());
            editsekonde_akim_1.setText(odf.getson_sekonde_akim_1());

            editprimer_akim_2.setText(odf.getson_primer_akim_2());
            editsekonde_akim_2.setText(odf.getson_sekonde_akim_2());

            editprimer_akim_3.setText(odf.getson_primer_akim_3());
            editsekonde_akim_3.setText(odf.getson_sekonde_akim_3());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odf.setteyit_dur(0);
                Gonder();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu4Fragment(), null).commit();
            }
        });

        return view;
    }

    public void Gonder() {
        if (!editsayac_akim_1.getText().toString().equals("")) {
            odf.setson_sayac_akim_1(editsayac_akim_1.getText().toString());
        } else {
            editsayac_akim_1.setError("Boş olamaz!");
        }

        if (!editsayac_gerili_1.getText().toString().equals("")) {
            odf.setson_sayac_gerili_1(editsayac_gerili_1.getText().toString());
        } else {
            editsayac_gerili_1.setError("Boş olamaz!");
        }

        if (!editsayac_akim_2.getText().toString().equals("")) {
            odf.setson_sayac_akim_2(editsayac_akim_2.getText().toString());
        }

        if (!editsayac_gerili_2.getText().toString().equals("")) {
            odf.setson_sayac_gerili_2(editsayac_gerili_2.getText().toString());
        }

        if (!editsayac_akim_3.getText().toString().equals("")) {
            odf.setson_sayac_akim_3(editsayac_akim_3.getText().toString());
        }

        if (!editsayac_gerili_3.getText().toString().equals("")) {
            odf.setson_sayac_gerili_3(editsayac_gerili_3.getText().toString());
        }



        if(Double.valueOf(odf.getcarpan()) > 1) {

        odf.setson_primer_akim_1(editprimer_akim_1.getText().toString());
        odf.setson_sekonde_akim_1(editsekonde_akim_1.getText().toString());

        odf.setson_primer_akim_2(editprimer_akim_2.getText().toString());
        odf.setson_sekonde_akim_2(editsekonde_akim_2.getText().toString());

        odf.setson_primer_akim_3(editprimer_akim_3.getText().toString());
        odf.setson_sekonde_akim_3(editsekonde_akim_3.getText().toString());

        odf.setson_polarite_1(editpolarite_1.getSelectedItem().toString());
        if(!editprimer_akim_2.getText().toString().equals(""))
            odf.setson_polarite_2(editpolarite_2.getSelectedItem().toString());
        else
            odf.setson_polarite_2("");
        if(!editprimer_akim_3.getText().toString().equals(""))
            odf.setson_polarite_3(editpolarite_3.getSelectedItem().toString());
        else
            odf.setson_polarite_3("");
    }

        if (!odf.getson_sayac_akim_1().equals("") && !odf.getson_sayac_gerili_1().equals("")) {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Yükleniyor..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);


            final Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        res = medasServer.OlcuKontrolService();
                        if (res.equals("OK") || res.equals("Success")) {
                            progressDialog.dismiss();
                            hata = 0;
                            message = "Ölçü Devre Tamamlandı!";
                            odf.setservisdur(1);
                            IslemiBitir(hata, title, message);
                            //odf.AllClear();
                        } else {
                            progressDialog.dismiss();
                            String[] parse = res.split("\\+");
                            if (parse.length > 1) {
                                hata = 1;
                                title = parse[0];
                                message = parse[1] + "\nYinede Onaylıyor musunuz?";
                                TYTIslemi(hata, title, message);
                            } else {
                                hata = 1;
                                title = "Hata";
                                message = "Servis hataya uğradı!";
                                IslemiBitir(hata, title, message);
                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        hata = 1;
                        title = "Exception Hatası";
                        message = e.getMessage();
                        IslemiBitir(hata, title, message);
                    }
                }
            });

            thread.start();
            thread.interrupt();

        } else {
            IslemiBitir(1, "Hata", "Lütfem tüm alanları doldurunuz!");
        }
    }

    public void IslemiBitir(final int hata, String title, String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        if (hata == 1) {
            dialog.setTitle(title);
            dialog.setMessage(message);
        } else {
            dialog.setMessage(message);
        }

        dialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                if (hata == 0) {
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                }
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog dlg = dialog.create();
                dlg.show();
            }
        });

    }

    public void TYTIslemi(final int hata, String title, String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        if (hata == 1) {
            dialog.setTitle(title);
            dialog.setMessage(message);
        } else {
            dialog.setMessage(message);
        }

        dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                odf.setteyit_dur(1);
                Gonder();

            }
        });
        dialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".

            }
        });
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog dlg = dialog.create();
                dlg.show();
            }
        });

    }

    public void ThereadBitir(Thread thread) {
        thread.interrupt();
    }


}
