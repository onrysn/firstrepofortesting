package mobit.android;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.mobit.FormInitParam;
import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IDef;
import com.mobit.IDetail;
import com.mobit.IForm;

public class DetayActivity extends AppCompatActivity implements IForm {

	IApplication app;
	protected IDetail detail = null;
	protected ListView list;
	protected List<String[]> arrayList = new ArrayList<String []>();
	protected ArrayAdapterEx<String[]> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detay);
		
		if(!(Globals.app instanceof IApplication)){
			finish();
			return;
		}
		
		app = (IApplication)Globals.app;
		
		Object obj = app.getObject(IDef.OBJ_DETAY);
		if(obj instanceof IDetail){
			detail = (IDetail)obj;
		}
		
		FormInitParam param = app.getFormInitParam();
		if(param != null){
			param.captionText = (detail != null) ? detail.getAciklama() : "Detay";
			
		}
		app.initForm(this, param);
		
		list = (ListView)findViewById(R.id.list);
		
		if(detail != null){
			setTitle(detail.getAciklama());
			arrayList.addAll(detail.getDetay());
		}
		else {
			setTitle("");
		}
		
		arrayAdapter = new ArrayAdapterEx<String[]>(this, R.layout.row_detay, arrayList, 
				this, DetayActivity.class, ViewHolder.class);
		
		list.setAdapter(arrayAdapter);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
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
	
	public class ViewHolder<T> extends ViewHolderEx<T> {

		
		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);
			
			View v;
			
			v = view.findViewById(R.id.textAlanAdi);
			((TextView)v).setTypeface(Typeface.MONOSPACE);
			v.setOnClickListener(this);
			columnList.add(v);
			
			v = view.findViewById(R.id.textAlanDeger);
			((TextView)v).setTypeface(Typeface.MONOSPACE);
			v.setOnClickListener(this);
			columnList.add(v);

		}

		@Override
		public void set(int position, T obj) {
			
			super.set(position, obj);
			
			String [] strs = (String [])item;
			TextView tv;
			tv = (TextView)columnList.get(0);
			//tv.setTextSize(mobit.elec.android.utility.textSize);
			tv.setText(strs.length > 0 && strs[0] != null ? strs[0] : "");
			tv.setTag(strs);
			
			tv = (TextView)columnList.get(1);
			//tv.setTextSize(mobit.elec.android.utility.textSize);
			tv.setText(strs.length > 1 && strs[1] != null ? strs[1] : "");
			tv.setTag(strs);

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
				
				
		}
	}
}
