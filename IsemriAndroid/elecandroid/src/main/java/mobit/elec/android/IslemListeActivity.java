package mobit.elec.android;

import java.lang.reflect.Constructor;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.IDetail;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.IIslemMaster;
import com.mobit.MobitException;
import com.mobit.MsgInfo;
import com.mobit.Operation;

import mobit.android.ArrayAdapterEx;
import mobit.android.LoadData2;
import mobit.android.ViewHolderEx;
import mobit.eemr.IReadResult;
import mobit.eemr.Lun_Control;
import mobit.eemr.OlcuDevreForm;
import mobit.eemr.YaziciHataBildirimi;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.IIsemri2;
import mobit.elec.IIsemriIslem;
import mobit.elec.enums.IslemTipi;

public class IslemListeActivity extends AppCompatActivity implements IForm, OnClickListener, Callback {

    IElecApplication app;
    ListView listIslemler;
    Button btnEvet;
    Button btnHayir;

    int index = 0;
    ArrayAdapterEx<Operation> adapterIslemler;
    List<Operation> list = new ArrayList<Operation>();

    Operation[] operations;
    IIslemMaster master;
    IIsemriIslem islem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islem_liste);

        if (!(Globals.app instanceof IElecApplication)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }
        app = (IElecApplication) Globals.app;

        if (!(app.getActiveIslem() instanceof IIslemGrup)) {
            finish();
            Globals.app.ShowMessage(this, MsgInfo.DESTEKLENMIYOR);
            return;
        }

        IIslemGrup master = (IIslemGrup) app.getActiveIslem();

        islem = (IIsemriIslem) master.getIslem(IIsemriIslem.class).get(0);
        IIsemri isemri = islem.getIsemri();
        IIsemri2 isemri2 = (IIsemri2) islem.getIsemri();


        FormInitParam param = app.getFormInitParam();
        if (param != null) {
            param.captionText = isemri.getISLEM_TIPI().toString();
            // param.captionColor =
            // utility.toHexColor(app.getIslemRenk(isemri.getISLEM_TIPI()));
        }
        app.initForm(this, param);

        listIslemler = (ListView) findViewById(R.id.listIslemler);


        btnEvet = (Button) findViewById(R.id.btnEvet);
        btnHayir = (Button) findViewById(R.id.btnHayir);

        btnEvet.setOnClickListener(this);
        btnHayir.setOnClickListener(this);

        btnEvet.setVisibility(View.INVISIBLE);
        btnHayir.setVisibility(View.INVISIBLE);

        TesisatBilgiFragment bilgi = (TesisatBilgiFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tesisatBilgiFragment);
        bilgi.show(isemri, isemri2.getISEMRI_ACIKLAMA());

        operations = app.getOperationList(app.getActiveIsemri().getISLEM_TIPI());
        operations = Operation.clone(operations);

        Operation oper = Operation.first(operations, this);

        //---------------------------------------------------------------------
        // Optik ile okunmuş ise yapılabilecek mi sorusunun geçilmesi
        List<IReadResult> results = islem.getSAYACLAR().getOptikResult();
        if (!results.isEmpty()) {
            list.add(oper);
            // İlk soru evet cevaplanmış gibi bir sonraki soruya geçilmesi
            oper = oper.next(operations, this, Operation.Yes);
        }

        //---------------------------------------------------------------------

        push(oper);

    }

    @Override
    protected void onResume() {
        app.getMeterReader().setTriggerEnabled(false);
        app.setPortCallback(null);
        super.onResume();

    }

    @Override
    protected void onPause() {
        app.setPortCallback(null);
        super.onPause();
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {

        app.setPortCallback(null);
        if (arg0 != IDef.FORM_ISEMRI_DETAY && arg0 != IDef.FORM_ADRES_TARIF) {
            btnEvet.setVisibility(View.INVISIBLE);
            btnHayir.setVisibility(View.INVISIBLE);
            if (arg1 == Operation.Cancel) {
                cancel();
            } else {
                Operation cur = getCurrent();
                Operation oper = cur.next(operations, this, arg1);
                if (oper == null)
                    return;

                push(oper);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.islem_liste, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.isemriDetay) {

            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ISEMRI_DETAY));
            startActivityForResult(intent, IDef.FORM_ISEMRI_DETAY);

        } else if (id == R.id.adresTarif) {
            Intent intent = new Intent(this, app.getFormClass(IDef.FORM_ADRES_TARIF));
            startActivityForResult(intent, IDef.FORM_ADRES_TARIF);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    // -------------------------------------------------------------------------

    public class ViewHolder<T> extends ViewHolderEx<T> {

        private TextView textView;

        public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
            super(adapter, view);
            // checkBox = (CheckBox) view.findViewById(R.id.rowCheckBox);
            // textView = (TextView) view.findViewById(R.id.rowTextView);
            textView = (TextView) view;
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Operation oper = (Operation) arg0.getTag();
            if (oper != null) {
                reset(oper);
            }
        }

        @Override
        public void set(int position, T obj) {

            Object ob = IslemListeActivity.this.list;
            super.set(position, obj);
            if (obj != null) {
                textView.setText(obj.toString());
                textView.setTag(obj);
            }

        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        int id = arg0.getId();
        if (id == R.id.btnEvet || id == R.id.btnHayir) {

            int resultCode = Operation.No;
            if (id == R.id.btnEvet)
                resultCode = Operation.Yes;
            else if (id == R.id.btnHayir)
                resultCode = Operation.No;

            Operation cur = getCurrent();
            if (cur.isFinished(resultCode)) {

                app.IslemTamamlandi(this, islem, true, true);

                YaziciHataBildirimi yazici = new YaziciHataBildirimi();
                yazici.setIsemri_no(islem.getSAHA_ISEMRI_NO());
                yazici.setTesisat_no(islem.getTESISAT_NO());
                yazici.setGonderme_dur(1);

				/*
				app.ShowMessage(this, "İşlem tamamlandı", "", DialogMode.Ok, 1, new IDialogCallback() {

					@Override
					public void DialogResponse(int msg_id, DialogResult result) {
						// TODO Auto-generated method stub
						try {
							app.print(islem);
							finish();
						} catch (Exception e) {
							app.ShowException(IslemListeActivity.this, e);
						}
					}

				});
				return*/
            }
            Lun_Control zz = new Lun_Control();
			/*if (String.valueOf(islem.getIsemri().getISLEM_TIPI()).equals("Kesme") && cur.nodeId==1 && zz.KacakVarmi==1 && zz.ZabitTesisat==islem.getIsemri().getTESISAT_NO()){
				Class<?> clazz = app.getFormClass(IDef.FORM_ZABIT);
				if (clazz != null) {

					Intent intent = new Intent(this, clazz);
					intent.putExtra("MODE", zz.ZabitMod);
					startActivityForResult(intent, IDef.FORM_ZABIT);
				}
			}
			*/
            //if (String.valueOf(islem.getIsemri().getISLEM_TIPI()).equals("Tespit") && islem.getIsemri().getALT_EMIR_TURU()==5 && islem.getIsemri().getALT_EMIR_TURU()==6006 && zz.KacakVarmi==0 ){

            Operation oper = cur.next(operations, this, resultCode);
            if (oper == null) {

                finish();
                return;
            }
            push(oper);
        }

    }

    private Operation getCurrent() {
        if (list.size() == 0)
            return null;
        return list.get(list.size() - 1);
    }

    private void show(Operation oper) {


        btnEvet.setVisibility(View.INVISIBLE);
        btnHayir.setVisibility(View.INVISIBLE);

        if (oper == null)
            return;

        if (oper.windowId > 0) {

            Class<?> cls = null;
            cls = app.getFormClass(oper.windowId);

            if (cls == null) {
                app.ShowMessage(this, "Sonraki ekran bulunamadı!", "");
                return;
            }
            if (IForm.class.isAssignableFrom(cls)) {
                Activity activity = (Activity) oper.form;
                if (activity == null || activity.isDestroyed()) {
                    oper.form = null;
                    Intent intent = new Intent(this, cls);
                    startActivityForResult(intent, oper.windowId);
                } else {

                    activity.setVisible(true);
                }
            } else if (Runnable.class.isAssignableFrom(cls)) {

                Runnable r;

                try {
                    Constructor<?> c = cls.getConstructor(IForm.class, Callback.class);
                    r = (Runnable) c.newInstance(this, this);
                    r.run();


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    app.ShowException(this, e);
                }
            }
        } else {
            btnEvet.setVisibility(View.VISIBLE);
            btnHayir.setVisibility(View.VISIBLE);
        }
    }

    private void createAdapter() {
        adapterIslemler = new ArrayAdapterEx<Operation>(this, android.R.layout.simple_list_item_1, list,
                ViewHolder.class);
        listIslemler.setAdapter(adapterIslemler);
    }

    private void push(Operation oper) {
        if (oper == null)
            return;

        list.add(oper);
        createAdapter();
        adapterIslemler.notifyDataSetChanged();

        show(oper);
    }

    private void cancel() {
        Operation cur = getCurrent();
        if (cur == null)
            return;

        list.remove(cur);
        createAdapter();
        adapterIslemler.notifyDataSetChanged();

        show(getCurrent());
    }

    void reset(Operation oper) {
        boolean remove = false;
        for (Iterator<Operation> iter = list.iterator(); iter.hasNext(); ) {
            Operation _oper = iter.next();
            if (remove == false) {
                if (_oper == oper)
                    remove = true;
                continue;
            }
            iter.remove();
        }
        if (remove)
            adapterIslemler.notifyDataSetChanged();
        show(oper);
    }

    @Override
    public Object run(final Object _obj) {
        // TODO Auto-generated method stub
        int resultCode;

        final IslemTamamla.Param param = (IslemTamamla.Param) _obj;

        if (param.obj instanceof Throwable) {

            app.ShowException(this, (Throwable) param.obj, DialogMode.Ok, 1, new IDialogCallback() {

                @Override
                public void DialogResponse(int msg_id, DialogResult result) {
                    // TODO Auto-generated method stub
                    if (!(param.obj instanceof MobitException)) {
                        finish();
                        return;
                    }

                    MobitException ex = (MobitException) param.obj;
                    if (ex.isSuccessful() || ex.getCode() == IDef.WRN_KUYRUGA_EKLENDI) {
                        setResult(RESULT_OK);
                        finish();
                        return;
                    }
                    cancel();
                }

            });

        } else if (param.obj instanceof Integer) {
            resultCode = (Integer) param.obj;
            Operation cur = getCurrent();
            Operation oper = cur.next(operations, this, resultCode);
            if (oper == null) {
                setResult(RESULT_OK);

                    app.IslemTamamlandi(this, param.islem,  true, true);

                YaziciHataBildirimi yazici = new YaziciHataBildirimi();
                yazici.setIsemri_no(islem.getSAHA_ISEMRI_NO());
                yazici.setTesisat_no(islem.getTESISAT_NO());
                yazici.setGonderme_dur(1);
                //}
                return false;//return IslemTamamlandi();
            }
            push(oper);
        }

        return null;
    }

}
