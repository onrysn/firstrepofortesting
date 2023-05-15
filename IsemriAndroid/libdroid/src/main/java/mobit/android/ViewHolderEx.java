package mobit.android;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class ViewHolderEx<T> implements OnClickListener {

	protected List<View> columnList = new ArrayList<View>();
	protected View view = null;
	protected ArrayAdapterEx<T> adapter = null;
	protected T item = null;
	
	public ViewHolderEx(ArrayAdapterEx<T> adapter, View view) {

		this.adapter = adapter;
		this.view = view;
		view.setTag(this);
	}

	public void set(int position, T obj) {
		
		item = obj;
	}
	
}
