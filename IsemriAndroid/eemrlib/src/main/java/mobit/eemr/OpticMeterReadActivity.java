package mobit.eemr;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class OpticMeterReadActivity extends Activity implements OnClickListener {

    int sessionId = 0;

    private TextView Status;
    private TextView powerStatus;
    private ListView obisList;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    private Spinner okumaModu;
    private Spinner sayacMarka;
    private Button tekrarOku;

    private IProbeEvent event;
    private IMeterReader mr;
    private int spinner_kontrol = 0;

    private ArrayAdapter<ReadMode2> adapter1;
    private ArrayAdapter<MeterType> adapter2;

    //static final ColorDrawable AndroidGreen = new ColorDrawable(Color.parseColor("#A4C639"));
    static final ColorDrawable AndroidBlue = new ColorDrawable(Color.parseColor("#008AD1"));

    private float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        density = getResources().getDisplayMetrics().density;
        sessionId = getIntent().getIntExtra("sessionId", 0);
        event = ProbeEvent.getEvent(sessionId);
        mr = event.getMeterReader();

        // ---------------------------------------------------------------------

        // RelativeLayout relativeLayout = new RelativeLayout(this);
        LinearLayout vl = new LinearLayout(this);
        vl.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        vl.setOrientation(LinearLayout.VERTICAL);

        // ---------------------------------------------------------------------

        LinearLayout hl = new LinearLayout(this);
        hl.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (density * 50)));
        hl.setOrientation(LinearLayout.HORIZONTAL);
        hl.setBackgroundColor(AndroidBlue.getColor());

        ViewGroup.LayoutParams params;

        okumaModu = new Spinner(this);
        okumaModu.setEnabled(false);
        hl.addView(okumaModu);
        params = okumaModu.getLayoutParams();
        params.width = (int) (150 * density);
        okumaModu.setLayoutParams(params);

        sayacMarka = new Spinner(this);
        hl.addView(sayacMarka);
        params = sayacMarka.getLayoutParams();
        params.width = (int) (150 * density);
        sayacMarka.setLayoutParams(params);

        tekrarOku = new Button(this);

        tekrarOku.setText("Oku");
        tekrarOku.setOnClickListener(this);
        hl.addView(tekrarOku);

        vl.addView(hl);

        // ---------------------------------------------------------------------

        // Power status
        powerStatus = new TextView(this);
        powerStatus.setText("");
        powerStatus.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
        powerStatus.setBackgroundColor(AndroidBlue.getColor());
        vl.addView(powerStatus);

        // ---------------------------------------------------------------------
        // Connection status
        Status = new TextView(this);
        Status.setText("");
        Status.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12);
        Status.setBackgroundColor(AndroidBlue.getColor());
        vl.addView(Status);

        // ---------------------------------------------------------------------

        obisList = new ListView(this);
        // obisList.setMinimumHeight(500);
        vl.addView(obisList);

        // ---------------------------------------------------------------------

        setContentView(vl);
        /*
         * ActionBar actionBar = getSupportActionBar(); if(actionBar != null){
         * actionBar.setDisplayHomeAsUpEnabled(true);
         * actionBar.setBackgroundDrawable(AndroidGreen); }
         */

        adapter1 = new ArrayAdapter<ReadMode2>(this, android.R.layout.simple_spinner_item, ReadMode2.values());

        okumaModu.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<MeterType>(this, android.R.layout.simple_spinner_item, MeterType.values());

        sayacMarka.setAdapter(adapter2);

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        obisList.setAdapter(adapter);


        MbtProbePowerStatus ps = null;
        try {
            ps = mr.GetPowerStatus();
            if (ps != null) powerStatus.setText(ps.toString());
        } catch (Exception e) {

        }

        ProbeEventEx.addActivity(sessionId, this);
        synchronized (ProbeEventEx.sync) {
            ProbeEventEx.sync.notify();
        }

    }

    @Override
    protected void onDestroy() {
        ProbeEventEx.removeActivity(sessionId);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.optic_meter_read, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void Starting() {

        tekrarOku.setEnabled(false);
        okumaModu.setEnabled(false);

        IReadResult result = mr.getReadResult();

        if (result != null && result.getRetry() == 0) {
            ReadMode2 readMode = result.getReadMode();
            for (int i = 0; i < adapter1.getCount(); i++) {
                if (adapter1.getItem(i).equals(readMode)) {
                    okumaModu.setSelection(i);
                    break;
                }
            }

            MeterType meterType = result.getMeterType();
            for (int i = 0; i < adapter2.getCount(); i++) {
                if (adapter2.getItem(i).equals(meterType)) {
                    sayacMarka.setSelection(i);
                    break;
                }
            }
        }

        Clear();

        setStatusText("Okuma başladı...");

    }

    public void setStatusText(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                Status.setText(text);
            }
        });
    }

    public void addItem(final String pAddress, final String pDataLine) {

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (pAddress != null)
                        arrayList.add(String.format("%s-%s", pAddress, pDataLine));

                    else
                        arrayList.add(String.format("%s", pDataLine));

                    adapter.notifyDataSetChanged();
                    int count = obisList.getCount();
                    if (count > 0)
                        obisList.setSelection(count - 1);
                } catch (Exception ex) {

					Exception e = ex;

                }
            }
        });
    }

    public void Clear() {

        Runnable r = new Runnable() {
            public void run() {
                arrayList.clear();
                adapter.notifyDataSetChanged();
                synchronized (this) {
                    this.notify();
                }
            }
        };
        obisList.post(r);

        synchronized (r) {
            runOnUiThread(r);
            try {
                //muhammed gökkaya luna deneme yıldız endeks
                r.wait(1000);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // unlocks myRunable while waiting
        }
    }

    public void Failed(final Exception e) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (e != null) setStatusText(e.getMessage());
                //HÜSEYİN EMRE ÇEVİK SPİNNERDAN MANUEL SEÇİMİ KALDIRDIK ( OTOMATİK - READMODE -PROGRAM )
                if (spinner_kontrol > 0) okumaModu.setEnabled(true);
//if ((e.getMessage().equals("Sayaç okunamadı!" ) || e.getMessage().equals("Sayaç okumaya açılamadı!")) && spinner_kontrol==0)
                if (spinner_kontrol == 0) {
                    spinner_kontrol++;
                    okumaModu.setSelection(2);
                    tekrarOku.performClick();
                } else
                    tekrarOku.setEnabled(true);

            }
        });
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        setStatusText("");
        tekrarOku.setEnabled(false);
        okumaModu.setEnabled(false);
        IReadResult result = mr.getReadResult();
        YkpOkuma ykpOkuma = new YkpOkuma();
        ykpOkuma.setYkpCount(1);
        if (result != null) {
            result.setReadMode(adapter1.getItem(okumaModu.getSelectedItemPosition()));
            result.setMeterType(adapter2.getItem(sayacMarka.getSelectedItemPosition()));
        }
        mr.Retry(result);


    }


    public boolean sonObis() {
//		Son gelen Obis Kodunun sonunda ünlem olup olmadığına bakmak için
//		String kontrol= arrayList.get(arrayList.size()-1);
//		if (kontrol.substring(kontrol.length() - 1, kontrol.length()).equals("!")){
//			return true;
//		}

//		Yıldızlı endeks var mı kontrol ediyor
        for (String item : arrayList) {

            if (item.contains("1.8.3*1(") || item.contains("96.77.0")) {
                return true;
            }
        }

        return false;
    }

    public void ObisReset() {
        arrayList.clear();
    }

    public int getokumaModu() {

        return okumaModu.getSelectedItemPosition();
    }


}
