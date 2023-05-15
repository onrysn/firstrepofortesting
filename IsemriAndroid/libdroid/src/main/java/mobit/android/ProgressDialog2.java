package mobit.android;

import android.app.Activity;
import android.content.Context;

public class ProgressDialog2 extends android.app.ProgressDialog {

	public ProgressDialog2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (context instanceof Activity) {
			setOwnerActivity((Activity)context);
		}
	}

	@Override
	public void show()
	{
		super.show();
	}
	
	@Override
	public void dismiss()
	{
		super.dismiss();
	}
	
	@Override
	public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // getOwnerActivity() should be defined here if called via showDialog(), so do the related init here
        Activity owner = getOwnerActivity();
        if (owner != null) {
            // owner activity defined here
        }
    }
}
