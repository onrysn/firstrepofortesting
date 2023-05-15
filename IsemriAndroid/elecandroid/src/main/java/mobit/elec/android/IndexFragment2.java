package mobit.elec.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mobit.Globals;
import com.mobit.IForm;

import mobit.eemr.IReadResult;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.ElecApplication;
import mobit.elec.IEndeks;
import mobit.elec.IEndeksler;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.SayacCinsi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.Endeks;
import mobit.elec.mbs.get.field;

public class IndexFragment2 extends Fragment implements OnClickListener, OnFocusChangeListener, OnKeyListener {

    ElecApplication app = (ElecApplication) Globals.app;
    IIsemriIslem isemriIslem;
    ISayaclar sayaclar;

    IReadResult result = null;

    boolean readOnly = false;

    TextView textToplam;
    TextView textGunduz;
    TextView textPuant;
    TextView textGece;
    TextView textEnduktif;
    TextView textKapasitif;
    TextView textDemand;

    EditText editToplamTam;
    TextView textToplamNokta;
    EditText editToplamKusurat;

    EditText editGunduzTam;
    TextView textGunduzNokta;
    EditText editGunduzKusurat;

    EditText editPuantTam;
    TextView textPuantNokta;
    EditText editPuantKusurat;

    EditText editGeceTam;
    TextView textGeceNokta;
    EditText editGeceKusurat;

    EditText editEnduktifTam;
    TextView textEnduktifNokta;
    EditText editEnduktifKusurat;

    EditText editKapasitifTam;
    TextView textKapasitifNokta;
    EditText editKapasitifKusurat;

    EditText editDemandTam;
    TextView textDemandNokta;
    EditText editDemandKusurat;

    String decimalSep;


    public IndexFragment2() {
        decimalSep = String.valueOf(Globals.getDecimalFormatSymbols().getDecimalSeparator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_index2, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        textToplam = (TextView) view.findViewById(R.id.textToplam);
        textGunduz = (TextView) view.findViewById(R.id.textGunduz);
        textPuant = (TextView) view.findViewById(R.id.textPuant);
        textGece = (TextView) view.findViewById(R.id.textGece);
        textEnduktif = (TextView) view.findViewById(R.id.textEnduktif);
        textKapasitif = (TextView) view.findViewById(R.id.textKapasitif);
        textDemand = (TextView) view.findViewById(R.id.textDemand);

        editToplamTam = (EditText) view.findViewById(R.id.editToplamTam);
        textToplamNokta = (TextView) view.findViewById(R.id.textToplamNokta);
        editToplamKusurat = (EditText) view.findViewById(R.id.editToplamKusurat);

        editGunduzTam = (EditText) view.findViewById(R.id.editGunduzTam);
        textGunduzNokta = (TextView) view.findViewById(R.id.textGunduzNokta);
        editGunduzKusurat = (EditText) view.findViewById(R.id.editGunduzKusurat);

        editPuantTam = (EditText) view.findViewById(R.id.editPuantTam);
        textPuantNokta = (TextView) view.findViewById(R.id.textPuantNokta);
        editPuantKusurat = (EditText) view.findViewById(R.id.editPuantKusurat);

        editGeceTam = (EditText) view.findViewById(R.id.editGeceTam);
        textGeceNokta = (TextView) view.findViewById(R.id.textGeceNokta);
        editGeceKusurat = (EditText) view.findViewById(R.id.editGeceKusurat);

        editEnduktifTam = (EditText) view.findViewById(R.id.editEnduktifTam);
        textEnduktifNokta = (TextView) view.findViewById(R.id.textEnduktifNokta);
        editEnduktifKusurat = (EditText) view.findViewById(R.id.editEnduktifKusurat);

        editKapasitifTam = (EditText) view.findViewById(R.id.editKapasitifTam);
        textKapasitifNokta = (TextView) view.findViewById(R.id.textKapasitifNokta);
        editKapasitifKusurat = (EditText) view.findViewById(R.id.editKapasitifKusurat);

        editDemandTam = (EditText) view.findViewById(R.id.editDemandTam);
        textDemandNokta = (TextView) view.findViewById(R.id.textDemandNokta);
        editDemandKusurat = (EditText) view.findViewById(R.id.editDemandKusurat);

        editToplamTam.setOnFocusChangeListener(this);
        editToplamTam.setSelectAllOnFocus(true);
        editToplamKusurat.setOnFocusChangeListener(this);
        editToplamKusurat.setSelectAllOnFocus(true);

        editGunduzTam.setOnFocusChangeListener(this);
        editGunduzTam.setSelectAllOnFocus(true);
        editGunduzKusurat.setOnFocusChangeListener(this);
        editGunduzKusurat.setSelectAllOnFocus(true);

        editPuantTam.setOnFocusChangeListener(this);
        editPuantTam.setSelectAllOnFocus(true);
        editPuantKusurat.setOnFocusChangeListener(this);
        editPuantKusurat.setSelectAllOnFocus(true);

        editGeceTam.setOnFocusChangeListener(this);
        editGeceTam.setSelectAllOnFocus(true);
        editGeceKusurat.setOnFocusChangeListener(this);
        editGeceKusurat.setSelectAllOnFocus(true);

        editEnduktifTam.setOnFocusChangeListener(this);
        editEnduktifTam.setSelectAllOnFocus(true);
        editEnduktifKusurat.setOnFocusChangeListener(this);
        editEnduktifKusurat.setSelectAllOnFocus(true);

        editKapasitifTam.setOnFocusChangeListener(this);
        editKapasitifTam.setSelectAllOnFocus(true);
        editKapasitifKusurat.setOnFocusChangeListener(this);
        editKapasitifKusurat.setSelectAllOnFocus(true);

        editDemandTam.setOnFocusChangeListener(this);
        editDemandTam.setSelectAllOnFocus(true);
        editDemandKusurat.setOnFocusChangeListener(this);
        editDemandKusurat.setSelectAllOnFocus(true);


        textToplamNokta.setText(decimalSep);
        textGunduzNokta.setText(decimalSep);
        textPuantNokta.setText(decimalSep);
        textGeceNokta.setText(decimalSep);
        textEnduktifNokta.setText(decimalSep);
        textKapasitifNokta.setText(decimalSep);
        textDemandNokta.setText(decimalSep);
        //kilitleme
        //Muhammed Gökkaya

        hide();

    }

    public void clear() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                editToplamTam.setText("");
                editToplamKusurat.setText("");

                editGunduzTam.setText("");
                editGunduzKusurat.setText("");

                editPuantTam.setText("");
                editPuantKusurat.setText("");

                editGeceTam.setText("");
                editGeceKusurat.setText("");

                editEnduktifTam.setText("");
                editEnduktifKusurat.setText("");

                editKapasitifTam.setText("");
                editKapasitifKusurat.setText("");

                editDemandTam.setText("");
                editDemandKusurat.setText("");
            }
        });
    }

    public void hide() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                textToplam.setVisibility(View.INVISIBLE);
                textGunduz.setVisibility(View.INVISIBLE);
                textPuant.setVisibility(View.INVISIBLE);
                textGece.setVisibility(View.INVISIBLE);
                textEnduktif.setVisibility(View.INVISIBLE);
                textKapasitif.setVisibility(View.INVISIBLE);
                textDemand.setVisibility(View.INVISIBLE);

                editToplamTam.setVisibility(View.INVISIBLE);
                textToplamNokta.setVisibility(View.INVISIBLE);
                editToplamKusurat.setVisibility(View.INVISIBLE);

                editGunduzTam.setVisibility(View.INVISIBLE);
                textGunduzNokta.setVisibility(View.INVISIBLE);
                editGunduzKusurat.setVisibility(View.INVISIBLE);

                editPuantTam.setVisibility(View.INVISIBLE);
                textPuantNokta.setVisibility(View.INVISIBLE);
                editPuantKusurat.setVisibility(View.INVISIBLE);


                editGeceTam.setVisibility(View.INVISIBLE);
                textGeceNokta.setVisibility(View.INVISIBLE);
                editGeceKusurat.setVisibility(View.INVISIBLE);

                editEnduktifTam.setVisibility(View.INVISIBLE);
                textEnduktifNokta.setVisibility(View.INVISIBLE);
                editEnduktifKusurat.setVisibility(View.INVISIBLE);

                editKapasitifTam.setVisibility(View.INVISIBLE);
                textKapasitifNokta.setVisibility(View.INVISIBLE);
                editKapasitifKusurat.setVisibility(View.INVISIBLE);

                editDemandTam.setVisibility(View.INVISIBLE);
                textDemandNokta.setVisibility(View.INVISIBLE);
                editDemandKusurat.setVisibility(View.INVISIBLE);

                clear();
            }

        });

    }

    public void save() throws Exception {
        if (sayaclar != null) {
            ISayacBilgi sb = sayaclar.getSayac(SayacKodu.Aktif);

            if (sb != null) {
                if (sb.getSAYAC_CINSI() == SayacCinsi.Mekanik) {
                    sayaclar.addEndeks(EndeksTipi.Gunduz, editGunduzTam.getText().toString(), editGunduzKusurat.getText().toString());
                } else {
                    //if(!editToplam.getText().toString().isEmpty()) sayaclar.addEndeks(EndeksTipi.Toplam, editToplam.getText().toString());
                    sayaclar.addEndeks(EndeksTipi.Gunduz, editGunduzTam.getText().toString(), editGunduzKusurat.getText().toString());
                    sayaclar.addEndeks(EndeksTipi.Puant, editPuantTam.getText().toString(), editPuantKusurat.getText().toString());
                    sayaclar.addEndeks(EndeksTipi.Gece, editGeceTam.getText().toString(), editGeceKusurat.getText().toString());
                    sayaclar.addEndeks(EndeksTipi.Demand, editDemandTam.getText().toString(), editDemandKusurat.getText().toString());

                }
            }
            sb = sayaclar.getSayac(SayacKodu.Reaktif);
            if (sb != null) {
                sayaclar.addEndeks(EndeksTipi.Enduktif, editEnduktifTam.getText().toString(), editEnduktifKusurat.getText().toString());
            }
            sb = sayaclar.getSayac(SayacKodu.Kapasitif);
            if (sb != null) {
                sayaclar.addEndeks(EndeksTipi.Kapasitif, editKapasitifTam.getText().toString(), editKapasitifKusurat.getText().toString());
            }
        }
    }

    public void OdfEndeks() throws Exception {

        double t = 0;
        String t1 = "", t2 = "", t3 = "", kapasitif = "", enduktif = "", demand = "";

        if(!editGunduzTam.getText().toString().isEmpty() && !editGunduzKusurat.getText().toString().isEmpty()) {
            t1 = editGunduzTam.getText().toString() + "." + editGunduzKusurat.getText().toString();
            t += Double.parseDouble(t1);
        }
        if(!editGunduzTam.getText().toString().isEmpty() && editGunduzKusurat.getText().toString().isEmpty()) {
            t1 = editGunduzTam.getText().toString() + "." + "000";
            t += Double.parseDouble(t1);
        }
        if(!editPuantTam.getText().toString().isEmpty() && !editPuantKusurat.getText().toString().isEmpty()) {
            t2 = editPuantTam.getText().toString() + "." + editPuantKusurat.getText().toString();
            t += Double.parseDouble(t2);
        }
        if(!editPuantTam.getText().toString().isEmpty() && editPuantKusurat.getText().toString().isEmpty()) {
            t2 = editPuantTam.getText().toString() + "." + "000";
            t += Double.parseDouble(t2);
        }
        if(!editGeceTam.getText().toString().isEmpty() && !editGeceKusurat.getText().toString().isEmpty()) {
            t3 = editGeceTam.getText().toString() + "." + editGeceKusurat.getText().toString();
            t += Double.parseDouble(t3);
        }
        if(!editGeceTam.getText().toString().isEmpty() && editGeceKusurat.getText().toString().isEmpty()) {
            t3 = editGeceTam.getText().toString() + "." + "000";
            t += Double.parseDouble(t3);
        }
        if(!editKapasitifTam.getText().toString().isEmpty() && !editKapasitifKusurat.getText().toString().isEmpty())
            kapasitif = editKapasitifTam.getText().toString() + "." + editKapasitifKusurat.getText().toString();
        if(!editKapasitifTam.getText().toString().isEmpty() && editKapasitifKusurat.getText().toString().isEmpty())
            kapasitif = editKapasitifTam.getText().toString() + "." + "000";

        if(!editEnduktifTam.getText().toString().isEmpty() && !editEnduktifKusurat.getText().toString().isEmpty())
            enduktif = editEnduktifTam.getText().toString() + "." + editEnduktifKusurat.getText().toString();
        if(!editEnduktifTam.getText().toString().isEmpty() && editEnduktifKusurat.getText().toString().isEmpty())
            enduktif = editEnduktifTam.getText().toString() + "." + "000";

        if(!editDemandTam.getText().toString().isEmpty() && !editDemandKusurat.getText().toString().isEmpty())
            demand = editDemandTam.getText().toString() + "." + editDemandKusurat.getText().toString();
        if(!editDemandTam.getText().toString().isEmpty() && editDemandKusurat.getText().toString().isEmpty())
            demand = editDemandTam.getText().toString() + "." + "000";

        OlcuDevreForm odf = new OlcuDevreForm();
        odf.set_t(String.format("%.3f", t));
        odf.set_t1(t1);
        odf.set_t2(t2);
        odf.set_t3(t3);
        odf.set_kapasitif(kapasitif);
        odf.set_enduktif(enduktif);
        odf.set_demand(demand);
    }


    public String TamEndeks(String Endeks, int tesisat, String tesisat2) {
        try {
            if (tesisat != Integer.parseInt(tesisat2))
                return "";
            return Endeks.split(",")[0];
        } catch (Exception e) {
            return "";
        }

    }

    public String VirgulEndeks(String Endeks, int tesisat, String tesisat2) {
        try {
            if (tesisat != Integer.parseInt(tesisat2))
                return "";
            if (Endeks.split(",").length > 1) {
                return Endeks.split(",")[1];
            }
            return "";
        } catch (Exception e) {
            return "";
        }

    }

    public boolean AddOptikData(String Data, int tesisat, String tesisat2) {
        try {
            if (tesisat != Integer.parseInt(tesisat2) && Data != null)
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void show(IIsemriIslem isemriIslem, ISayaclar sayaclar, IReadResult result) {

        hide();

        EndeksFilter[] fsTam;
        EndeksFilter[] fsKusurat;
        ISayacBilgi sayac;
        IEndeksler endeksler;
        IIsemri isemri = isemriIslem.getIsemri();
        Lun_Control ln = new Lun_Control();
        String[] Endeksler = ln.Endeksler;


        sayac = sayaclar.getSayac(SayacKodu.Aktif);
        final ISayacBilgi aktifSayac = sayac;
        if (sayac != null) {
            sayac.setSAYAC_KODU(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getSAYAC_KODU());
            sayac.setSAYAC_CINSI(isemri.getSAYACLAR().getSayac(SayacKodu.Aktif).getSAYAC_CINSI());

            endeksler = sayac.getENDEKSLER();
            fsTam = new EndeksFilter[]{new EndeksFilter(sayac.getHANE_SAYISI().getValue(), 0)};
            fsKusurat = new EndeksFilter[]{new EndeksFilter(field.s_ENDEKS_PREC, 0)};
            editGunduzTam.setFilters(fsTam);
            editGunduzKusurat.setFilters(fsKusurat);
            textGunduz.setVisibility(View.VISIBLE);
            editGunduzTam.setVisibility(View.VISIBLE);
            textGunduzNokta.setVisibility(View.VISIBLE);
            editGunduzKusurat.setVisibility(View.VISIBLE);
            if (!sayac.getSAYAC_CINSI().equals(SayacCinsi.Mekanik)) {

                editPuantTam.setFilters(fsTam);
                editPuantKusurat.setFilters(fsKusurat);

                editGeceTam.setFilters(fsTam);
                editGeceKusurat.setFilters(fsKusurat);

                textPuant.setVisibility(View.VISIBLE);
                textGece.setVisibility(View.VISIBLE);
                editPuantTam.setVisibility(View.VISIBLE);
                textPuantNokta.setVisibility(View.VISIBLE);
                editPuantKusurat.setVisibility(View.VISIBLE);
                editGeceTam.setVisibility(View.VISIBLE);
                textGeceNokta.setVisibility(View.VISIBLE);
                editGeceKusurat.setVisibility(View.VISIBLE);
                textGunduz.setText("T1");
                if (!endeksler.getEndeks(EndeksTipi.Gunduz).toString().equals("") || Endeksler == null) {
                    editGunduzTam.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toStringIntPart());
                    editGunduzKusurat.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toStringDecimalPart());

                    editPuantTam.setText(endeksler.getEndeks(EndeksTipi.Puant).toStringIntPart());
                    editPuantKusurat.setText(endeksler.getEndeks(EndeksTipi.Puant).toStringDecimalPart());

                    editGeceTam.setText(endeksler.getEndeks(EndeksTipi.Gece).toStringIntPart());
                    editGeceKusurat.setText(endeksler.getEndeks(EndeksTipi.Gece).toStringDecimalPart());
                } else {
                    editGunduzTam.setText(TamEndeks(Endeksler[0], isemri.getTESISAT_NO(), Endeksler[6]));
                    editGunduzKusurat.setText(VirgulEndeks(Endeksler[0], isemri.getTESISAT_NO(), Endeksler[6]));

                    editPuantTam.setText(TamEndeks(Endeksler[1], isemri.getTESISAT_NO(), Endeksler[6]));
                    editPuantKusurat.setText(VirgulEndeks(Endeksler[1], isemri.getTESISAT_NO(), Endeksler[6]));

                    editGeceTam.setText(TamEndeks(Endeksler[2], isemri.getTESISAT_NO(), Endeksler[6]));
                    editGeceKusurat.setText(VirgulEndeks(Endeksler[2], isemri.getTESISAT_NO(), Endeksler[6]));
                }

            } else {
                textGunduz.setText("T");
                if (!endeksler.getEndeks(EndeksTipi.Gunduz).toString().equals("") || Endeksler == null) {
                    editGunduzTam.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toStringIntPart());
                    editGunduzKusurat.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toStringDecimalPart());
                } else {
                    editGunduzTam.setText(TamEndeks(Endeksler[0], isemri.getTESISAT_NO(), Endeksler[6]));
                    editGunduzKusurat.setText(VirgulEndeks(Endeksler[0], isemri.getTESISAT_NO(), Endeksler[6]));
                }
            }

        }

        if(sayaclar.getSayaclar().size() == 0) //H.Elif 11.05.2022
        {
            textGunduz.setVisibility(View.VISIBLE);
            editGunduzTam.setVisibility(View.VISIBLE);
            textGunduzNokta.setVisibility(View.VISIBLE);
            editGunduzKusurat.setVisibility(View.VISIBLE);

            textGunduz.setText("T");

        }

        else if (!sayaclar.getSayaclar().get(0).getSAYAC_KODU().equals(SayacKodu.Aktif)) {  //H.Elif 11.11.2021

            sayac = sayaclar.getSayac(SayacKodu.Reaktif);
            if (sayac != null) {
                endeksler = sayac.getENDEKSLER();
                fsTam = new EndeksFilter[]{new EndeksFilter(sayac.getHANE_SAYISI().getValue(), 0)};
                fsKusurat = new EndeksFilter[]{new EndeksFilter(field.s_ENDEKS_PREC, 0)};
                editEnduktifTam.setFilters(fsTam);
                editEnduktifKusurat.setFilters(fsKusurat);
                textEnduktif.setVisibility(View.VISIBLE);
                editEnduktifTam.setVisibility(View.VISIBLE);
                textEnduktifNokta.setVisibility(View.VISIBLE);
                editEnduktifKusurat.setVisibility(View.VISIBLE);
                if (!endeksler.getEndeks(EndeksTipi.Enduktif).toString().equals("") || Endeksler == null) {
                    editEnduktifTam.setText(endeksler.getEndeks(EndeksTipi.Enduktif).toStringIntPart());
                    editEnduktifKusurat.setText(endeksler.getEndeks(EndeksTipi.Enduktif).toStringDecimalPart());
                } else {
                    editEnduktifTam.setText(TamEndeks(Endeksler[3], isemri.getTESISAT_NO(), Endeksler[6]));
                    editEnduktifKusurat.setText(VirgulEndeks(Endeksler[3], isemri.getTESISAT_NO(), Endeksler[6]));
                }
            }

            sayac = sayaclar.getSayac(SayacKodu.Kapasitif);
            if (sayac != null) {
                endeksler = sayac.getENDEKSLER();
                fsTam = new EndeksFilter[]{new EndeksFilter(sayac.getHANE_SAYISI().getValue(), 0)};
                fsKusurat = new EndeksFilter[]{new EndeksFilter(field.s_ENDEKS_PREC, 0)};
                editKapasitifTam.setFilters(fsTam);
                textKapasitif.setVisibility(View.VISIBLE);
                editKapasitifTam.setVisibility(View.VISIBLE);
                textKapasitifNokta.setVisibility(View.VISIBLE);
                editKapasitifKusurat.setVisibility(View.VISIBLE);
                if (!endeksler.getEndeks(EndeksTipi.Kapasitif).toString().equals("") || Endeksler == null) {
                    editKapasitifTam.setText(endeksler.getEndeks(EndeksTipi.Kapasitif).toStringIntPart());
                    editKapasitifKusurat.setText(endeksler.getEndeks(EndeksTipi.Kapasitif).toStringDecimalPart());
                } else {
                    editKapasitifTam.setText(TamEndeks(Endeksler[4], isemri.getTESISAT_NO(), Endeksler[6]));
                    editKapasitifKusurat.setText(VirgulEndeks(Endeksler[4], isemri.getTESISAT_NO(), Endeksler[6]));
                }
            }

        }
        if (isemri.getCIFT_TERIM_DUR()) {
            //HÜSEYİN EMRE ÇEVİK 21.05.2021 ÇİFT TERİM OLMASI İÇİN REAKTİF OLMASI LAZIM ?
            if (sayac == null) {
                sayac = sayaclar.getSayac(SayacKodu.Aktif);
            }
            endeksler = sayac.getENDEKSLER();
            fsTam = new EndeksFilter[]{new EndeksFilter(field.s_DEMAND_ENDEKS - field.s_DEMAND_PREC - 1, 0)};
            fsKusurat = new EndeksFilter[]{new EndeksFilter(field.s_DEMAND_PREC, 0)};
            editDemandTam.setFilters(fsTam);
            editDemandKusurat.setFilters(fsKusurat);
            textDemand.setVisibility(View.VISIBLE);
            editDemandTam.setVisibility(View.VISIBLE);
            textDemandNokta.setVisibility(View.VISIBLE);
            editDemandKusurat.setVisibility(View.VISIBLE);
            if (!endeksler.getEndeks(EndeksTipi.Demand).toString().equals("") || Endeksler == null) {
                editDemandTam.setText(endeksler.getEndeks(EndeksTipi.Demand).toStringIntPart());
                editDemandKusurat.setText(endeksler.getEndeks(EndeksTipi.Demand).toStringDecimalPart());
            } else {
                editDemandTam.setText(TamEndeks(Endeksler[5], isemri.getTESISAT_NO(), Endeksler[6]));
                editDemandKusurat.setText(VirgulEndeks(Endeksler[5], isemri.getTESISAT_NO(), Endeksler[6]));
            }
        }
        if (AddOptikData(Endeksler[7], isemri.getTESISAT_NO(), Endeksler[6]) && Endeksler != null)
            isemriIslem.setOPTIK_DATA(Endeksler[7]);
        this.isemriIslem = isemriIslem;
        this.result = result;
        this.sayaclar = sayaclar;


        //String[] sifirlama=new String[6];
        //ln.setEndeksler(sifirlama);

        if (result != null) {
            optikEndeksDoldur(aktifSayac, result);
        } else {
            if (isemriIslem.getOPTIK_DATA() != null) {
                if (isemriIslem.getOPTIK_DATA().equals("")) {
                    setReadOnly(false);
                } else {
                    setReadOnly(true);
                }
            } else {
                setReadOnly(false);
            }

            //app.showSoftKeyboard((IForm)getActivity(), true);
        }

        editGunduzTam.requestFocus();

    }

    private void optikEndeksDoldur(ISayacBilgi sayac, final IReadResult result) {
        //burda doluyor..
        try {
            setReadOnly(true);

            String gunduz = result.get_gunduz_end();
            String puant = result.get_puant_end();
            String gece = result.get_gece_end();
            String enduktif = result.get_enduktif_end();
            String kapasitif = result.get_kapasitif_end();
            String demand = result.get_demand_end();

            if (!decimalSep.equals(".")) {
                if (gunduz != null) gunduz = gunduz.replace(".", decimalSep);
                if (puant != null) puant = puant.replace(".", decimalSep);
                if (gece != null) gece = gece.replace(".", decimalSep);
                if (enduktif != null) enduktif = enduktif.replace(".", decimalSep);
                if (kapasitif != null) kapasitif = kapasitif.replace(".", decimalSep);
                if (demand != null) demand = demand.replace(".", decimalSep);

            }
            if (gunduz != null) {
                IEndeks endeks = new Endeks(EndeksTipi.Gunduz, sayac.getHANE_SAYISI(), gunduz);
                editGunduzTam.setText(endeks.toStringIntPart());
                editGunduzKusurat.setText(endeks.toStringDecimalPart());
            }
            if (puant != null) {
                IEndeks endeks = new Endeks(EndeksTipi.Puant, sayac.getHANE_SAYISI(), puant);
                editPuantTam.setText(endeks.toStringIntPart());
                editPuantKusurat.setText(endeks.toStringDecimalPart());
            }
            if (gece != null) {
                IEndeks endeks = new Endeks(EndeksTipi.Gece, sayac.getHANE_SAYISI(), gece);
                editGeceTam.setText(endeks.toStringIntPart());
                editGeceKusurat.setText(endeks.toStringDecimalPart());
            }
            if (enduktif != null) {
                IEndeks endeks = new Endeks(EndeksTipi.Enduktif, sayac.getHANE_SAYISI(), enduktif);
                editEnduktifTam.setText(endeks.toStringIntPart());
                editEnduktifKusurat.setText(endeks.toStringDecimalPart());
            }
            if (kapasitif != null) {
                IEndeks endeks = new Endeks(EndeksTipi.Kapasitif, sayac.getHANE_SAYISI(), kapasitif);
                editKapasitifTam.setText(endeks.toStringIntPart());
                editKapasitifKusurat.setText(endeks.toStringDecimalPart());
            }
            if (demand != null) {
                IEndeks endeks = new Endeks(EndeksTipi.Demand, sayac.getHANE_SAYISI(), demand);
                editDemandTam.setText(endeks.toStringIntPart());
                editDemandKusurat.setText(endeks.toStringDecimalPart());
            }

            sayac.setOptikResult(result);

        } catch (Exception e) {
            app.ShowException((IForm) getActivity(), e);
        }

    }

    public void setReadOnly(boolean readonly) {

        boolean focusable = !readonly;

        editGunduzTam.setFocusableInTouchMode(focusable);
        editGunduzTam.setFocusable(focusable);

        editGunduzKusurat.setFocusableInTouchMode(focusable);
        editGunduzKusurat.setFocusable(focusable);

        editPuantTam.setFocusableInTouchMode(focusable);
        editPuantTam.setFocusable(focusable);

        editPuantKusurat.setFocusableInTouchMode(focusable);
        editPuantKusurat.setFocusable(focusable);

        editGeceTam.setFocusableInTouchMode(focusable);
        editGeceTam.setFocusable(focusable);

        editGeceKusurat.setFocusableInTouchMode(focusable);
        editGeceKusurat.setFocusable(focusable);

        editEnduktifTam.setFocusableInTouchMode(focusable);
        editEnduktifTam.setFocusable(focusable);

        editEnduktifKusurat.setFocusableInTouchMode(focusable);
        editEnduktifKusurat.setFocusable(focusable);

        editKapasitifTam.setFocusableInTouchMode(focusable);
        editKapasitifTam.setFocusable(focusable);

        editKapasitifKusurat.setFocusableInTouchMode(focusable);
        editKapasitifKusurat.setFocusable(focusable);

        editDemandTam.setFocusableInTouchMode(focusable);
        editDemandTam.setFocusable(focusable);

        editDemandKusurat.setFocusableInTouchMode(focusable);
        editDemandKusurat.setFocusable(focusable);
        readOnly = readonly;

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        int id = v.getId();
        if (!hasFocus) {

            try {
                if (id == R.id.editGunduz && editGunduzTam.getVisibility() == View.VISIBLE) {
                    sayaclar.addEndeks(EndeksTipi.Gunduz, editGunduzTam.getText().toString(), editGunduzKusurat.getText().toString());
                } else if (id == R.id.editPuant && editPuantTam.getVisibility() == View.VISIBLE) {
                    sayaclar.addEndeks(EndeksTipi.Puant, editPuantTam.getText().toString(), editPuantKusurat.getText().toString());
                } else if (id == R.id.editGece && editGeceTam.getVisibility() == View.VISIBLE) {
                    sayaclar.addEndeks(EndeksTipi.Gece, editGeceTam.getText().toString(), editGeceKusurat.getText().toString());
                } else if (id == R.id.editEnduktif && editEnduktifTam.getVisibility() == View.VISIBLE) {
                    sayaclar.addEndeks(EndeksTipi.Enduktif, editEnduktifTam.getText().toString(), editEnduktifKusurat.getText().toString());
                } else if (id == R.id.editKapasitif && editKapasitifTam.getVisibility() == View.VISIBLE) {
                    sayaclar.addEndeks(EndeksTipi.Kapasitif, editKapasitifTam.getText().toString(), editKapasitifKusurat.getText().toString());
                } else if (id == R.id.editDemand && editDemandTam.getVisibility() == View.VISIBLE) {
                    sayaclar.addEndeks(EndeksTipi.Demand, editDemandTam.getText().toString(), editDemandKusurat.getText().toString());
                }

            } catch (Exception e) {
                app.ShowException((IForm) getActivity(), e);
                return;
            }



        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return false;
    }

}
