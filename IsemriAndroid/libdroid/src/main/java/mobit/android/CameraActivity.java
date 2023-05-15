package mobit.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IForm;
import com.mobit.utility;

public class CameraActivity extends AppCompatActivity implements IForm, OnClickListener, PopupMenu.OnMenuItemClickListener {
	//muhammed gökkaya
	//camera düzenleme işi burda yapılacaktır...

	public class BitmapInfo {
		public File file;
		public Bitmap imageBitmap;

		public BitmapInfo(File file, Bitmap imageBitmap) {
			this.file = file;
			this.imageBitmap = imageBitmap;
		}
	};

	Context mContext;
	SurfaceTexture surfaceTexture;
	static final int REQUEST_IMAGE_CAPTURE = 100; //1
	ImageView mImageView = null;
	Button button = null;

	ListView listView;
	public ArrayList<BitmapInfo> arrayList;
	public ArrayAdapter<BitmapInfo> adapter;
	AndroidCamera camera;
	AndroidCamera mcamera;
	File path;
	int minPhotoNumber = 0;
	int maxPhotoNumber = -1;
	int photoNumber = 0;

	IApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_camera);

		if(!(Globals.app instanceof IApplication)){
			finish();
			return;
		}
		
		app = (IApplication)Globals.app;
		
		app.initForm(this);
		
		camera = AndroidCamera.getCamera(getIntent().getIntExtra("sessionId", 0));
		if(camera == null){
			mcamera = new AndroidCamera();
			camera = mcamera;
		}

		path = camera.getPath();
		minPhotoNumber = camera.getMinPhotoNumber();
		maxPhotoNumber = camera.getMaxPhotoNumber();

		/*
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(AndroidPlatform.AndroidGreen);
		*/

		RelativeLayout rl = new RelativeLayout(this);
		rl.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContext = getApplicationContext();
		surfaceTexture = new SurfaceTexture(0);

		RelativeLayout.LayoutParams params;
		
		button = new Button(this);
		params = new RelativeLayout.LayoutParams(
			    RelativeLayout.LayoutParams.MATCH_PARENT, 
			    RelativeLayout.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		button.setText("Çek");
		rl.addView(button, params);

		
		listView = new ListView(this);
		params = new RelativeLayout.LayoutParams(
			    RelativeLayout.LayoutParams.MATCH_PARENT, 
			    RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ABOVE, button.getId());
		rl.addView(listView, params);

		
		setContentView(rl);

		arrayList = new ArrayList<BitmapInfo>();

		adapter = new ArrayAdapterEx<BitmapInfo>(this, LinearLayout.class, arrayList, this, ViewHolder.class);
		listView.setAdapter(adapter);

		button.setOnClickListener(this);

		startCapture();

	}
	
	protected void onDestroy()
	{
		if(mcamera != null) mcamera.close();
		super.onDestroy();
	}

	void startCapture() {
		if (maxPhotoNumber > 0 && photoNumber == maxPhotoNumber) {
			Globals.platform.ShowMessage(this,
					String.format("Çekebileceğiniz en fazla %d adet fotografı çektiniz!", maxPhotoNumber), "Uyarı");
			return;
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			//takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Url);
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

		}
	}

	@Override
	public void onBackPressed() {

		if (photoNumber < minPhotoNumber) {
			Globals.platform.ShowMessage(this, String.format("En az %d adet fotograf çekmelisiniz!", minPhotoNumber),
					"Uyarı");
			return;
		}

		setResult(RESULT_OK);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			FileOutputStream out = null;

			try {

				File file = camera.getNewPhotoName(photoNumber);
				String ext = utility.getFileExtension(file.getAbsolutePath());
				file.createNewFile();
				out = new FileOutputStream(file);

				if(ext.compareToIgnoreCase("png") == 0){
					
					imageBitmap.compress(Bitmap.CompressFormat.PNG,100,out);

				}
				else {
					imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				}

				
				out.flush();
				out.close();
				photoNumber++;
				camera.addPhoto(file);

				this.arrayList.add(new BitmapInfo(file, imageBitmap));
				adapter.notifyDataSetChanged();
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				if (out != null)
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}
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

	
	@SuppressLint("SimpleDateFormat")
	static DateFormat fmt = new SimpleDateFormat("yyyyMMdd HHmmss");

	
	@SuppressWarnings("unused")
	private File createImageFile() throws IOException {
		// Create an image file name





		String timeStamp = fmt.format(Globals.platform.getTime());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				path /* directory */
		);

		return image;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == button.getId()) {

			startCapture();
		}
	}

	BitmapInfo selectedBitmap;

	public class ViewHolder<T> extends ViewHolderEx<T> {

		public ViewHolder(ArrayAdapterEx<T> adapter, View view) {

			super(adapter, view);

			LinearLayout l = (LinearLayout) view;
			l.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			l.setOrientation(LinearLayout.HORIZONTAL);

			View v = new ImageView(adapter.getContext());
			v.setOnClickListener(this);
			columnList.add(v);
			l.addView(v);

		}

		@Override
		public void set(int position, T obj) {

			super.set(position, obj);

			BitmapInfo info = (BitmapInfo) item;
			ImageView mImageView = (ImageView) columnList.get(0);
			mImageView.setImageBitmap(info.imageBitmap);
			mImageView.setTag(info);

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			((CameraActivity)adapter.getContext()).selectedBitmap = (BitmapInfo) arg0.getTag(); 
			PopupMenu popupMenu = new PopupMenu(adapter.getContext(), arg0);
			popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) adapter.getContext());
			popupMenu.show();
			
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		if (arg0.getItemId() == 1) {

			if (selectedBitmap != null) {
				selectedBitmap.file.delete();
				arrayList.remove(selectedBitmap);
				selectedBitmap = null;
				photoNumber--;
				adapter.notifyDataSetChanged();
				return true;
			}

		}
		return false;
	}
}
