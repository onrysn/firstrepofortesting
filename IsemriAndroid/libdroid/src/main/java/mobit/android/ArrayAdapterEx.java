package mobit.android;

import java.lang.reflect.Constructor;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mobit.Globals;
import com.mobit.IForm;

public class ArrayAdapterEx<T> extends ArrayAdapter<T> {

	private LayoutInflater inflater;
	private int layout;
	private List<T> liste = null;
	private Constructor<?> ctor = null;
	private Constructor<?> vctor = null;
	private Object parentObject;

	public List<T> getListe() {

		return liste;
	}

	public ArrayAdapterEx(Context context, int layout, List<T> liste, Object parentObject, Class<?> parentClass, Class<?> holderClass) {
		super(context, layout, liste);
		this.layout = layout;
		this.liste = liste;
		this.parentObject = parentObject;
		
		try {
			ctor = holderClass.getDeclaredConstructor(parentClass, ArrayAdapterEx.class, View.class);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//inflater = LayoutInflater.from(context);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public ArrayAdapterEx(Context context, int layout, List<T> liste, Object parentObject, Class<?> holderClass) {
		this(context, layout, liste, parentObject, parentObject.getClass(), holderClass);
	}
	
	public ArrayAdapterEx(Context context, int layout, List<T> liste, Class<?> holderClass) {
		
		this(context, layout, liste, context, holderClass);
	}


	public ArrayAdapterEx(Context context, Class<?> viewClass, List<T> liste, Object parentObject, Class<?> holderClass) {
		super(context, 0, liste);
		this.layout = 0;
		this.liste = liste;
		this.parentObject = parentObject;

		try {
			ctor = holderClass.getDeclaredConstructor(parentObject.getClass(), ArrayAdapterEx.class, View.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			vctor = viewClass.getConstructor(Context.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public int getCount() {
		return super.getCount();
		//return liste.size();
	}

	@Override
	public T getItem(int position) {
		return super.getItem(position);
		//return liste.get(position);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
		//return liste.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Object obj = null;
		
		if (convertView != null) {
			obj = convertView.getTag();
		} 
		else {
			if(vctor != null)
				try {
					convertView = (View)vctor.newInstance(getContext());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			else
				convertView = inflater.inflate(layout, null, false);
			
			try {

				obj = ctor.newInstance(parentObject, this, convertView);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
				
			}
		}
		String s = obj.toString();
		if(obj instanceof ViewHolderEx){
			@SuppressWarnings("unchecked")
			ViewHolderEx<T> viewHolder = (ViewHolderEx<T>)obj; 
			viewHolder.set(position, getItem(position));
		}
		else if(obj instanceof TextView){
			TextView textView = (TextView)obj;
			textView.setTag(getItem(position));
			textView.setText(getItem(position).toString());
		}
		return convertView;
	}
}
