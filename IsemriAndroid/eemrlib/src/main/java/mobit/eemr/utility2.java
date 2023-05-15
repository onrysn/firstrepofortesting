package mobit.eemr;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;

public class utility2 {

	private static Class<?> activityThreadClass = null;
	private static Object activityThread = null;
	private static Field activitiesField = null;
	private static Method currentActivityThread = null;
	private static Class<?> activityRecordClass = null;
	private static Field pausedField = null;
	private static Field activityField = null;
	
	public static Activity getActivity() throws Exception {

		if (activityThreadClass == null) {
			activityThreadClass = Class.forName("android.app.ActivityThread");
			activitiesField = activityThreadClass.getDeclaredField("mActivities");
			activitiesField.setAccessible(true);
			currentActivityThread = activityThreadClass.getMethod("currentActivityThread");
		}
		activityThread = currentActivityThread.invoke(null);

		@SuppressWarnings("unchecked")
		Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
		if (activities == null)
			return null;

		for (Object activityRecord : activities.values()) {
			if (activityRecordClass == null) {
				activityRecordClass = activityRecord.getClass();
				pausedField = activityRecordClass.getDeclaredField("paused");
				pausedField.setAccessible(true);
				activityField = activityRecordClass.getDeclaredField("activity");
				activityField.setAccessible(true);
			}

			if (!pausedField.getBoolean(activityRecord)) {

				Activity activity = (Activity) activityField.get(activityRecord);
				return activity;
			}
		}
		return null;
	}
	public static Activity getActivity(Activity activity)
	{
		if(activity != null && !(activity.isDestroyed() || activity.isFinishing()))
			return activity;
		
		activity = null;
		
		try {
			activity = (Activity)getActivity();
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		if (activity == null) return null;
		if(activity.isDestroyed() || activity.isFinishing())
			return null;
		
		return activity;
	}
	public static void showMessage(Activity activity, String msg)
	{
		
		activity = getActivity(activity);
		if(activity == null) return;
		
		AlertDialog.Builder builder = null;
		try {
			builder = new AlertDialog.Builder(activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(builder != null){
			builder.setTitle("");
			builder.setMessage(msg);
			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}
	
}
