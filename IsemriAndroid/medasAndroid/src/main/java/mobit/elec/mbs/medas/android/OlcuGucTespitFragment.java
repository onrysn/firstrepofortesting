package mobit.elec.mbs.medas.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;

import com.mobit.Globals;

import java.util.ArrayList;
import java.util.List;

import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.eemr.GucTespit;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;

public class OlcuGucTespitFragment extends Fragment { //H.Elif 10.11.2021
    Button add_btn;
    Button back_btn;
    Button save_btn;
    EditText cins_text;
    EditText adet_text;
    EditText guc_text;
    ListView lw;

    ArrayAdapter<GucTespit> arrayAdapter;
    ArrayList<GucTespit> arrayList;

    List<GucTespit> guc_tespit_list;
    List<String> guc_tespit_cins;
    List<String> guc_tespit_adet;
    List<String> guc_tespit_guc;
    GucTespit list_type;

    IElecApplication app = null;
    OlcuDevreForm odf;

    public OlcuGucTespitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_olcu_guc_tespit, container, false);
        app = (IElecApplication) Globals.app;

        odf = new OlcuDevreForm();
        back_btn = view.findViewById(R.id.back_btn);
        save_btn = view.findViewById(R.id.save_btn);
        add_btn = view.findViewById(R.id.add_btn);

        cins_text = view.findViewById(R.id.cins_text);
        adet_text = view.findViewById(R.id.adet_text);
        guc_text = view.findViewById(R.id.guc_text);

        lw = view.findViewById(R.id.listContainer);
        arrayList = new ArrayList<>();

        guc_tespit_list = new ArrayList<GucTespit>();
        guc_tespit_cins = new ArrayList<>();
        guc_tespit_adet = new ArrayList<>();
        guc_tespit_guc = new ArrayList<>();

        try {
            odf.set_guc_tespit_list(guc_tespit_list);
        } catch (Exception e) {
        }

        IIsemri data = app.getActiveIsemri();
        try {
            arrayAdapter = new ArrayAdapterEx<GucTespit>(getContext(), R.layout.row_list, arrayList, this, ViewHolder.class);
            lw.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < arrayList.size(); i++) {
                    list_type = new GucTespit();
                    list_type.set_cinsi(arrayList.get(i).cinsi);
                    list_type.set_guc(String.valueOf(arrayList.get(i).guc));
                    list_type.set_adet(Integer.valueOf(arrayList.get(i).adet));

                    guc_tespit_list.add(list_type);

                    guc_tespit_cins.add(arrayList.get(i).cinsi);
                    guc_tespit_adet.add(String.valueOf(arrayList.get(i).adet));
                    guc_tespit_guc.add(String.valueOf(arrayList.get(i).guc));
                }

                odf.set_guc_tespit_list(guc_tespit_list);

                odf.set_guc_tespit_cins(guc_tespit_cins);
                odf.set_guc_tespit_adet(guc_tespit_adet);
                odf.set_guc_tespit_guc(guc_tespit_guc);

                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu4Fragment(), null).commit();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuSokulenMuhurFragment(), null).commit();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!cins_text.getText().toString().equals("") && !adet_text.getText().toString().equals("") && !guc_text.getText().toString().equals("")) {
                    String cins = cins_text.getText().toString();
                    int adet = Integer.parseInt(adet_text.getText().toString());
                    String guc = guc_text.getText().toString();

                    GucTespit guc_tespit = new GucTespit();

                    guc_tespit.set_cinsi(cins);
                    guc_tespit.set_adet(adet);
                    guc_tespit.set_guc(guc);

                    cins_text.setText("");
                    adet_text.setText("");
                    guc_text.setText("");

                    arrayList.add(guc_tespit);
                    lw.setAdapter(arrayAdapter);
                }
            }
        });

        return view;
    }

    public void removeItem(int remove) {
        arrayList.remove(remove);
        lw.setAdapter(arrayAdapter);
    }

    public class ViewHolder<T> extends ViewHolderEx<T> {
        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
            super(adapter, view);

            View v;

            v = view.findViewById(R.id.number);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.cins);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.adet);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.guc);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(R.id.sil_btn);
            v.setOnClickListener(this);
            columnList.add(v);
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            removeItem(id - 1);
        }

        @Override
        public void set(int position, T obj) {
            item = obj;

            GucTespit guctespit = (GucTespit) item;

            String cins = guctespit.get_cinsi();
            int adet = guctespit.get_adet();
            String guc = guctespit.get_guc();
            int count = position + 1;

            TextView tv = (TextView) columnList.get(0);
            tv.setText(String.valueOf(count) + ".");
            tv.setTag(obj);

            tv = (TextView) columnList.get(1);
            tv.setText(cins);
            tv.setTag(obj);

            tv = (TextView) columnList.get(2);
            tv.setText(String.valueOf(adet));
            tv.setTag(obj);

            tv = (TextView) columnList.get(3);
            tv.setText(String.valueOf(guc));
            tv.setTag(obj);

            tv = (TextView) columnList.get(4);
            tv.setId(count);
            tv.setTag(obj);
        }
    }

}
