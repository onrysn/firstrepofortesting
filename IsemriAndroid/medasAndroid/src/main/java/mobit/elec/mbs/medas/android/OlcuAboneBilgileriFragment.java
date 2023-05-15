package mobit.elec.mbs.medas.android;

import android.annotation.SuppressLint;
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

import com.mobit.DbHelper;
import com.mobit.Globals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobit.eemr.OlcuDevreForm;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacMarka;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.get.sayac_marka;
import mobit.elec.mbs.medas.IMedasApplication;

public class OlcuAboneBilgileriFragment extends Fragment { //H.Elif 10.11.2021
    Button back_btn;
    Button save_btn;

    TextView isemri_no_vw;
    TextView islem_tarihi_vw;
    TextView tesisat_no_vw;
    TextView karne_sıra_no_vw;
    TextView tarife_kodu_vw;
    TextView carpan_vw;
    TextView cbs1_vw;
    TextView cbs2_vw;

    EditText unvan_txt;
    EditText adres_txt;
    EditText sayac_no_txt;
    Spinner sayac_marka_spn;
    EditText hane_sayisi_txt;
    EditText imal_yili_txt;
    EditText faz_txt;
    EditText amperaj_txt;
    EditText voltaj_txt;

    TextView tcNo_txt;
    TextView telNo_txt;
    TextView eposta_txt;

    ArrayAdapter<String> spinnerAdaptersayacMarka;

    OlcuDevreForm odf;
    IElecApplication app = null;
    IMedasApplication app2 = null;
    IIsemriIslem isemriIslem;
    List<ISayacMarka> markalar;
    ArrayList<String> marka_adi_list = new ArrayList<>();

    public OlcuAboneBilgileriFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olcu_abone_bilgileri, container, false);
        odf = new OlcuDevreForm();
        back_btn = view.findViewById(R.id.back_btn);
        save_btn = view.findViewById(R.id.save_btn);

        isemri_no_vw = view.findViewById(R.id.isemriNo);
        islem_tarihi_vw = view.findViewById(R.id.islemTarihi);
        tesisat_no_vw = view.findViewById(R.id.tesisatNo);
        karne_sıra_no_vw = view.findViewById(R.id.karneSiraNo);
        tarife_kodu_vw = view.findViewById(R.id.tarifeKodu);
        carpan_vw = view.findViewById(R.id.carpan);
        cbs1_vw = view.findViewById(R.id.cbs1);
        cbs2_vw = view.findViewById(R.id.cbs2);

        tcNo_txt = view.findViewById(R.id.tcNo_text);
        telNo_txt = view.findViewById(R.id.telNo_text);
        eposta_txt = view.findViewById(R.id.eposta_text);
        unvan_txt = view.findViewById(R.id.unvan_text);
        adres_txt = view.findViewById(R.id.adres_text);
        sayac_no_txt = view.findViewById(R.id.sayacNo_text);
        sayac_marka_spn = view.findViewById(R.id.sayacMarka_spn);
        hane_sayisi_txt = view.findViewById(R.id.haneSayisi_text);
        imal_yili_txt = view.findViewById(R.id.imalyili_text);
        faz_txt = view.findViewById(R.id.faz_text);
        amperaj_txt = view.findViewById(R.id.amperaj_text);
        voltaj_txt = view.findViewById(R.id.voltaj_text);

        try {
            tcNo_txt.setText(odf.get_tcNo());
            telNo_txt.setText(odf.get_telNo());
            eposta_txt.setText(odf.get_eposta());
            unvan_txt.setText(odf.get_unvan());
            adres_txt.setText(odf.get_adres());
            sayac_no_txt.setText(odf.get_sayac_no());
            sayac_marka_spn.setSelection(spinnerAdaptersayacMarka.getPosition(odf.get_sayac_marka()));
            hane_sayisi_txt.setText(odf.get_hane_sayisi());
            imal_yili_txt.setText(odf.get_imal_yili());
            faz_txt.setText(odf.get_faz());
            amperaj_txt.setText(odf.get_amperaj());
            voltaj_txt.setText(odf.get_voltaj());
        } catch (Exception e) {
        }


        app = (IElecApplication) Globals.app;
        app2 = (IMedasApplication) Globals.app;
        IIsemri data = app.getActiveIsemri();
        try {
            odf.settesisat_no(data.getTESISAT_NO());
            odf.setisemri_no(data.getSAHA_ISEMRI_NO());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            isemri_no_vw.setText(String.valueOf(data.getSAHA_ISEMRI_NO()));
            islem_tarihi_vw.setText(sdf.format(new Date()));
            tesisat_no_vw.setText(String.valueOf(data.getTESISAT_NO()));
            karne_sıra_no_vw.setText(String.valueOf(data.getKARNE_NO()) + "/" + String.valueOf(data.getSIRA_NO()));
            tarife_kodu_vw.setText(String.valueOf(data.getTARIFE_KODU()));
            carpan_vw.setText(String.valueOf(data.getCARPAN()));
            odf.setcarpan(data.getCARPAN());

            cbs1_vw.setText(String.format("%.7f", app.getLocation().getLatitude()));
            cbs2_vw.setText(String.format("%.7f", app.getLocation().getLongitude()));

            unvan_txt.setText(String.valueOf(data.getUNVAN()));
            adres_txt.setText(String.valueOf(data.getADRES()));
            sayac_no_txt.setText(String.valueOf(data.getSAYACLAR().getSayac(SayacKodu.Aktif).getSAYAC_NO()));

            odf.set_tarife_kodu(String.valueOf(data.getTARIFE_KODU()));
            odf.set_cbs1(String.format("%.7f", app.getLocation().getLatitude()));
            odf.set_cbs2(String.format("%.7f", app.getLocation().getLongitude()));

            odf.set_user_code(app2.getEleman().getELEMAN_KODU());

            try {
                markalar = DbHelper.SelectAll(app2.getConnection(), sayac_marka.class);
                for (ISayacMarka sm : markalar) {
                    marka_adi_list.add(sm.getSAYAC_MARKA_ADI());
                }

                spinnerAdaptersayacMarka = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, marka_adi_list);
                spinnerAdaptersayacMarka.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sayac_marka_spn.setAdapter(spinnerAdaptersayacMarka);
                spinnerAdaptersayacMarka.notifyDataSetChanged();

                for (ISayacMarka sm : markalar) {
                    String marka_adi = sm.getSAYAC_MARKA_ADI();
                    if (String.valueOf(data.getSAYACLAR().getSayaclar().get(0).getMARKA()).equals(marka_adi)) {
                        sayac_marka_spn.setSelection(spinnerAdaptersayacMarka.getPosition(marka_adi));
                    }
                }

                sayac_marka_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        sayac_marka_spn.setSelection(arg2);
                        odf.set_sayac_marka(markalar.get(arg2).getSAYAC_MARKA_KODU().toString());
                        spinnerAdaptersayacMarka.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        //
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            hane_sayisi_txt.setText(String.valueOf(data.getSAYACLAR().getSayac(SayacKodu.Aktif).getHANE_SAYISI()).split(" ")[0]);
            imal_yili_txt.setText(String.valueOf(data.getSAYACLAR().getSayac(SayacKodu.Aktif).getIMAL_YILI()));


        } catch (Exception e) {
            e.printStackTrace();
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TcKimlikKontrol(tcNo_txt.getText().toString())) {
                    odf.set_tcNo(tcNo_txt.getText().toString());
                }

                if (!telNo_txt.getText().toString().equals("")) {
                    odf.set_telNo(telNo_txt.getText().toString());
                }

                if (!eposta_txt.getText().toString().equals("")) {
                    odf.set_eposta(eposta_txt.getText().toString());
                }

                if (!unvan_txt.getText().toString().equals("")) {
                    odf.set_unvan(unvan_txt.getText().toString());
                } else {
                    unvan_txt.setError("Boş olamaz!");
                }

                if (!adres_txt.getText().toString().equals("")) {
                    odf.set_adres(adres_txt.getText().toString());
                } else {
                    adres_txt.setError("Boş olamaz!");
                }

                if (!sayac_no_txt.getText().toString().equals("")) {
                    odf.set_sayac_no(sayac_no_txt.getText().toString());
                } else {
                    sayac_no_txt.setError("Boş olamaz!");
                }

                if (!hane_sayisi_txt.getText().toString().equals("")) {
                    odf.set_hane_sayisi(hane_sayisi_txt.getText().toString());
                } else {
                    hane_sayisi_txt.setError("Boş olamaz!");
                }

                if (!imal_yili_txt.getText().toString().equals("")) {
                    odf.set_imal_yili(Integer.parseInt(imal_yili_txt.getText().toString()));
                }

                if (!faz_txt.getText().toString().equals("")) {
                    odf.set_faz(Integer.parseInt(faz_txt.getText().toString()));
                }

                if (!amperaj_txt.getText().toString().equals("")) {
                    odf.set_amperaj(Integer.parseInt(amperaj_txt.getText().toString()));
                }

                if (!voltaj_txt.getText().toString().equals("")) {
                    odf.set_voltaj(Integer.parseInt(voltaj_txt.getText().toString()));
                }

                if (!odf.get_unvan().equals("") && !odf.get_adres().equals("") && !odf.get_sayac_no().equals("")
                        && !odf.get_hane_sayisi().equals("") && odf.get_imal_yili() != -1) {

                    SimpleDateFormat utc = (SimpleDateFormat) Globals.dateTimeFmt.clone();
                    utc.setTimeZone(Globals.utcTimeZone);
                    String datetime = utc.format(Globals.getTime().getTime());
                    String[] date = datetime.split(" ");

                    odf.set_date(date[0]);
                    odf.set_time(date[1]);

                    if(odf.get_disaridan_olcu_dur() == 1)
                    {
                        OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuTakilanSayacEndexFragment(), null).commit();
                    }
                    else {
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
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuHomeFragment(), null).commit();
            }
        });
        return view;
    }

    public boolean TcKimlikKontrol(String Tc) {
        if (Tc.length() == 0) return true;
        if (Tc.length() != 11) return false;
        int[] tchane = new int[Tc.length()];
        int toplam = 0;
        // Copy character by character into array
        for (int i = 0; i < Tc.length(); i++) {
            tchane[i] = Integer.parseInt(String.valueOf(Tc.charAt(i)));
            toplam += tchane[i];
        }
        toplam -= tchane[10];
        if ((toplam % 10) != tchane[10]) {
            // TC KİMLİK NUMARANIZIN İLK 10 BASAMAMAĞININ TOPLAMININ 10'A BÖLÜMÜNDEN KALAN 11. BASAMAĞI VERİR. KENDİ NUMARANIZDA DENEYEBİLİRSİNİZ.
            return false;
        }
        if (((tchane[0] + tchane[2] + tchane[4] + tchane[6] + tchane[8]) * 7 + (tchane[1] + tchane[3] + tchane[5] + tchane[7]) * 9) % 10 != tchane[9]) {
            // (1-3-5-7-9. BASAMAKLARIN TOPLAMININ 7 İLE ÇARPIMI) + (2-4-6-8. BASAMAKLARIN TOPLAMININ 9 İLE ÇARPIMINDAN 10 BÖLÜMÜNÜN KALANI 10. BASAMAĞI VERİR.
            return false;
        }
        if (((tchane[0] + tchane[2] + tchane[4] + tchane[6] + tchane[8]) * 8) % 10 != tchane[10]) {
            // 1-3-5-7-9. BASAMAKLARIN TOPLAMI 8 İLE ÇARPILIP 10'A BÖLÜNÜMÜNDEN KALAN 11. BASAMAĞI VERİR.
            return false;
        }
        return true;
    }

}

