package mobit.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.ListPopupWindow;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import com.mobit.IChecked;
import mobit.android.AndroidPlatform.IPopupCallback;

public class CheckPopupList implements OnClickListener, AdapterView.OnItemClickListener,
		AdapterView.OnItemSelectedListener, OnDismissListener {

	ListPopupWindow popup;
	ArrayAdapterEx<?> adapter;
	View anchorView;
	int width;
	IPopupCallback clb;
	CheckList checkList;
	
	public CheckPopupList(View anchorView, int width, CheckList checkList, IPopupCallback clb) {
		
		init(anchorView, width, checkList, clb);
	}

	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}

	protected void init(View anchorView, int width, CheckList checkList, IPopupCallback clb) {

		if (adapter != null)
			throw new RuntimeException("initPopupList daha önce çağrılmış!");

		this.anchorView = anchorView;
		this.width = width;
		this.clb = clb;
		this.checkList = checkList;
		
		adapter = new ArrayAdapterEx<IChecked>((Context)clb, R.layout.row_check_popup, 
				checkList.list, this, ViewHolder.class);
		popup = new ListPopupWindow(adapter.getContext());
		popup.setAdapter(adapter);
		if (anchorView != null)
			popup.setAnchorView(anchorView);
		popup.setDropDownGravity(Gravity.CENTER);
		popup.setWidth((int)(width*anchorView.getContext().getResources().getDisplayMetrics().density));
		popup.setModal(true);

		popup.setOnItemClickListener(this);
		popup.setOnDismissListener(this);
		popup.setOnItemSelectedListener(this);
		anchorView.setOnClickListener(this);

	}

	public void close() {
		if (popup != null) {
			popup.setOnItemClickListener(null);
			popup.setOnItemSelectedListener(null);
			anchorView.setOnClickListener(null);
			popup.dismiss();
			popup.setOnDismissListener(null);
			popup = null;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		clb.OnItemSelect(anchorView, adapter.getItem(arg2));
		popup.dismiss();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		popup.show();
		ListView list = popup.getListView();
		list.setItemsCanFocus(true);
		list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
	}

	public static class CheckList {
		
		private List<IChecked> list = new ArrayList<IChecked>();
		
		public CheckList(List<IChecked> list)
		{
			this.list.addAll(list);
		}
		public CheckList(IChecked [] list)
		{
			for(int i = 0; i < list.length; i++)
				this.list.add(list[i]);
		}
		
		@Override
		public String toString()
		{
			boolean first = true;
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < list.size(); i++){
				if(!list.get(i).getCheck()) continue;
				if(!first) sb.append(", ");
				sb.append(list.get(i).toString());
				first = false;
			}
			return sb.toString();
		}
		
		public static void setCheck(CheckList checkList, Object obj, boolean checked)
		{
			for(IChecked o : checkList.list){
				if(o.equals(obj)){
					o.setCheck(checked);
				}
			}
		}
		
		public List<?> getList()
		{
			return list;
		}
		
		public int getCheckCount()
		{
			int cnt = 0;
			for(IChecked o : list) if(o.getCheck()) cnt++;
			return cnt;
		}
	}
	
	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		
		clb.OnItemSelect(anchorView, checkList);
		
	}

	public class ViewHolder extends ViewHolderEx<IChecked> implements OnCheckedChangeListener {

		CheckBox textView;

		public ViewHolder(ArrayAdapterEx<IChecked> adapter, View view) {
			super(adapter, view);
			textView = (CheckBox) view.findViewById(R.id.rowCheckedTextView);
			textView.setOnCheckedChangeListener(this);

		}

		public void set(int position, IChecked obj) {
			super.set(position, obj);

			if (obj != null) {
				textView.setTag(obj);
				textView.setText(obj.toString());
				textView.setChecked(obj.getCheck());
			}
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			Object obj;
			for (int position = 0; position < adapter.getCount(); position++) {
				obj = adapter.getItem(position);
				if (obj instanceof IChecked)
					((IChecked) obj).setCheck(false);
			}

			obj = arg0.getTag();
			if (obj instanceof IChecked)
				((IChecked) obj).setCheck(true);

			adapter.notifyDataSetChanged();

		}

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(textView == arg0){
				Object obj = textView.getTag();
				if(obj instanceof IChecked){
					((IChecked)obj).setCheck(arg1);
				}
				else {
					textView.setChecked(false);
				}
			}
			return;
		}

	}

}
