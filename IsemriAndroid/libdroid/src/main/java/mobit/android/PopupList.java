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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import com.mobit.Globals;
import com.mobit.IChecked;
import mobit.android.AndroidPlatform.IPopupCallback;

public class PopupList<T> implements OnClickListener, AdapterView.OnItemClickListener,
		AdapterView.OnItemSelectedListener, OnDismissListener, AutoCloseable {

	ListPopupWindow popup;
	ArrayAdapterEx<?> adapter;
	View anchorView;
	int width;
	IPopupCallback clb;
	
	int density;

	public PopupList(View anchorView, int width, T[] list, IPopupCallback clb) {
		List<T> l = new ArrayList<T>(list.length);
		for (T obj : list)
			l.add(obj);
		init(anchorView, width, l, clb);
		
		
		
	}

	public PopupList(View anchorView, int width, List<T> list, IPopupCallback clb) {
		init(anchorView, width, list, clb);
	}


    protected void init(View anchorView, int width, List<T> list, IPopupCallback clb) {

		if (adapter != null)
			throw new RuntimeException("initPopupList daha önce çağrılmış!");

		this.anchorView = anchorView;
		this.width = width;
		this.clb = clb;
		
		adapter = new ArrayAdapterEx<T>((Context)clb, R.layout.row_popup, list, this, ViewHolder.class);
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

	@Override
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

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub

	}

	public class ViewHolder<T> extends ViewHolderEx<T> {

		TextView textView;

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {
			super(adapter, view);
			textView = (TextView) view.findViewById(R.id.rowTextView);

		}

		public void set(int position, T obj) {
			super.set(position, obj);

			if (obj != null) {
				textView.setTag(obj);
				textView.setText(obj.toString());
				if (obj instanceof IChecked && ((IChecked) obj).getCheck()) {
					textView.setBackgroundColor(Color.parseColor("#808080"));
					textView.invalidate();
				}
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

	}

}
