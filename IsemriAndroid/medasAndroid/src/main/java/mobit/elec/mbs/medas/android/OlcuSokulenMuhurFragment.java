package mobit.elec.mbs.medas.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobit.Globals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mobit.android.ArrayAdapterEx;
import mobit.android.ViewHolderEx;
import mobit.eemr.OlcuDevreForm;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IMuhur;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.android.SayacZimmetBilgi;


public class OlcuSokulenMuhurFragment extends Fragment { //H.Elif 10.11.2021

    Button back_btn;
    Button save_btn;

    IElecApplication app = null;
    List<Integer> sokulen_muhur_seri_no_List = new ArrayList<Integer>();
    List<String> sokulen_muhur_List = new ArrayList<String>();
    List<IMuhur> arrayList;
    OlcuDevreForm odf;
    ArrayAdapterEx<IMuhur> arrayAdapter;

    Spinner listAciklamalar;
    EditText editAciklama;

    ArrayAdapter<CharSequence> adapter;

    public OlcuSokulenMuhurFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_olcu_sokulen_muhur, container, false);

        app = (IElecApplication) Globals.app;

        odf = new OlcuDevreForm();
        back_btn = view.findViewById(R.id.back_btn);
        save_btn = view.findViewById(R.id.save_btn);
        listAciklamalar = view.findViewById(R.id.listAciklamalar);
        editAciklama = view.findViewById(R.id.editAciklama);

        try {
            editAciklama.setText(odf.get_aciklama());
        } catch (Exception e) {
        }

        IIsemri data = app.getActiveIsemri();
        try {
            adapter = ArrayAdapter.createFromResource(getContext(), R.array.sokulen_muhur_list, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listAciklamalar.setAdapter(adapter);

            arrayList = (app.fetchTesisatMuhur(data.getTESISAT_NO()));
            if (arrayList != null) {
                Collections.sort(arrayList, new Comparator<IMuhur>() {
                    public int compare(IMuhur a1, IMuhur a2) {
                        return a2.getMUHUR_TARIHI().compareTo(a1.getMUHUR_TARIHI()); // assumes you want biggest to smallest
                    }
                });

                int size = arrayList.size() > 5 ? 5 : arrayList.size();
                arrayList = arrayList.subList(0, size);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    load(view);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odf.set_sokulen_muhur_list(sokulen_muhur_List);

                odf.set_aciklama(editAciklama.getText().toString());


                for (int ii = 0; ii < adapter.getCount(); ii++)
                {
                    String aciklama = editAciklama.getText().toString();
                    String liste = adapter.getItem(ii).toString();

                    if (aciklama.contains(liste))
                    {
                        odf.set_ihbar_dur(1);
                        break;
                    }
                }

                Gonder();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (odf.getakim_trafosu_dur() == 1) {
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu2Fragment(), null).commit();
                } else if (odf.getgerilim_trafosu_dur() == 1) {
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu3Fragment(), null).commit();
                } else {
                    OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuGucTrafosuFragment(), null).commit();
                }
            }
        });

        listAciklamalar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.LTGRAY);

                if (position != 0) {
                    odf.set_ihbar_dur(1);
                    Object item = adapterView.getItemAtPosition(position);
                    if (item != null) {
                        String s = editAciklama.getText().toString();
                        if (s.length() == 0) {
                            editAciklama.setText(item.toString());
                            return;
                        }
                        editAciklama.setText(String.format("%s\n%s", s, item.toString()));
                    }
                } else {
                    odf.set_ihbar_dur(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
        return view;
    }

    public class ViewHolder<T> extends ViewHolderEx<T> {
        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
            super(adapter, view);

            View v;
            v = view.findViewById(mobit.elec.android.R.id.rowTextView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
            params.setMargins(-30, -10, 0, 5); //substitute parameters for left, top, right, bottom
            v.setLayoutParams(params);
            v.setOnClickListener(this);
            columnList.add(v);

            v = view.findViewById(mobit.elec.android.R.id.rowCheckBox);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) v.getLayoutParams();
            params2.setMargins(20, 5, 0, 0); //substitute parameters for left, top, right, bottom
            v.setLayoutParams(params2);
            v.setOnClickListener(this);
            columnList.add(v);
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            try {
                CheckBox checkBox = (CheckBox) arg0;
                IMuhur muhur = (IMuhur) checkBox.getTag();
                if (checkBox.isChecked()) {
                    int cnt = 0;
                    for (int i = 0; i < arrayList.size(); i++)
                        if (arrayList.get(i).getCheck())
                            cnt++;
                    muhur.setCheck(cnt < arrayList.size());
                    sokulen_muhur_seri_no_List.add(arg0.getId());
                    sokulen_muhur_List.add(muhur.getSERI_NO().getSeri() + "/" + muhur.getSERI_NO().getNo());

                } else {
                    muhur.setCheck(false);
                    int index = sokulen_muhur_seri_no_List.indexOf(arg0.getId());
                    sokulen_muhur_seri_no_List.remove(index);

                    index = sokulen_muhur_List.indexOf(arg0.getId());
                    sokulen_muhur_List.remove(index);
                }
                checkBox.setChecked(muhur.getCheck());
            }
            catch (Exception ex)
            {}
        }

        @Override
        public void set(int position, T obj) {
            item = obj;
            IMuhur muhur = (IMuhur) item;
            String text = String.format("Tesisat No: %d  Seri/No: %s\nYer: %s  Tarih: %s\nNeden :%s  Durum: %s",
                    muhur.getTESISAT_NO(), muhur.getSERI_NO(), muhur.getMUHUR_YERI2().getMUHUR_ACIKLAMA(),
                    Globals.dateFmt.format(muhur.getMUHUR_TARIHI()),
                    muhur.getMUHUR_NEDENI2().getMUHUR_ACIKLAMA(),
                    muhur.getMUHUR_DUR2().getMUHUR_ACIKLAMA());

            TextView tv = (TextView) columnList.get(0);
            tv.setText(text);
            tv.setTag(obj);

            CheckBox cb = (CheckBox) columnList.get(1);
            cb.setId(muhur.getSERI_NO().getNo());
            cb.setChecked(muhur.getCheck());
            cb.setTag(obj);
        }
    }

    private void load(View view) {
        ListView lw = view.findViewById(R.id.editTextContainer);
        arrayAdapter = new ArrayAdapterEx<IMuhur>(getContext(), mobit.elec.android.R.layout.row_with_check, arrayList, this, ViewHolder.class);
        lw.setAdapter(arrayAdapter);
    }

    public void Gonder() {

        if (odf.getguc_tespiti_dur() == 1) {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new OlcuGucTespitFragment(), null).commit(); //H.Elif
        } else {
            OlcuDevreActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new Olcu4Fragment(), null).commit(); //H.Elif
        }
    }


}


