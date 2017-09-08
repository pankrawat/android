package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.app.baccoon.R;
import com.app.baccoon.utils.Constant;
import com.app.baccoon.utils.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CropActivity extends Activity {

	private String currentPhotoPath;
	private CropImageView imageToCrop;
	private int targetSize;
	private Bitmap currentImage;
	private RelativeLayout imageProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crop_layout);
		imageToCrop = (CropImageView) findViewById(R.id.cropImageView);
		imageProgress=(RelativeLayout)findViewById(R.id.imageProgress);
		if (getIntent().getExtras() != null) {
			currentPhotoPath = getIntent().getExtras().getString(Constant.IMAGE_PATH);
			if(!getIntent().getExtras().getBoolean("mode",false)){
				imageToCrop.setCropMode(CropImageView.CropMode.RATIO_FREE);
			}
			/*if(getIntent().getExtras().containsKey("imagebitmap")){
				//byte[] byteArray = getIntent().getByteArrayExtra("imagebitmap");
				// currentImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
				currentImage= ReferenceWrapper.getInstance(this).getRecentBitmap();
			}*/
		}
		targetSize = getResources().getDisplayMetrics().widthPixels;
		if(currentPhotoPath!=null && !currentPhotoPath.equals("")){
		setImage();
		}
		else{
			if(currentImage!=null){
				imageToCrop.setImageBitmap(currentImage);
			}
		}
		findViewById(R.id.buttonCancleCrop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		findViewById(R.id.buttonSaveImage).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSavedClick();
			}
		});
		findViewById(R.id.rotate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageToCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
			}
		});
	}

	private void setImage() {
		try {
			currentImage = decodeSampledBitmap(targetSize);
			// currentImage=Bitmap.createScaledBitmap(currentImage,
			// targetSize,targetSize,false);
			// currentImage=getOrientedBitmap();
			imageToCrop.setImageBitmap(currentImage);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			Log.e("Setting bitmap", " OutOfMemoryError");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Setting bitmap", " Error");
		}
	}

	private Bitmap decodeSampledBitmap(int size) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(currentPhotoPath, options);
		options.inSampleSize = calculateInSampleSize(options, size);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(currentPhotoPath, options);
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqWidth || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqWidth && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/*private Bitmap getOrientedBitmap() {
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(currentPhotoPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Matrix matrix = new Matrix();
		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
		if (orientation == 6) {
			matrix.postRotate(90);
			Log.e("orientation", "" + 6);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, targetSize, targetSize, matrix, false);
		} else if (orientation == 3) {
			Log.e("orientation", "" + 3);
			matrix.postRotate(180);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, targetSize, targetSize, matrix, false);
		} else if (orientation == 8) {
			Log.e("orientation", "" + 8);
			matrix.postRotate(-90);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, targetSize, targetSize, matrix, false);
		}
		return currentImage;
	}
*/
	private File createImageFile() {
		String state = Environment.getExternalStorageState();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File mFileTemp = null;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			try {
				mFileTemp = File.createTempFile(imageFileName, ".jpg", Environment.getExternalStorageDirectory());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				mFileTemp = File.createTempFile(imageFileName, ".jpg", getFilesDir());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mFileTemp;
	}

	private void onSavedClick() {
		/*currentImage = imageToCrop.getCroppedBitmap();
		File file = createImageFile();

		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
			currentImage.compress(Bitmap.CompressFormat.PNG, 80, fout);
			fout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent data = new Intent();
		data.putExtra(Constants.IMAGE_PATH, file.getPath());
		setResult(RESULT_OK, data);
		if (currentImage != null) {
			currentImage.recycle();
		}
		finish();*/
		try {
			imageProgress.setVisibility(View.VISIBLE);
			currentImage = imageToCrop.getCroppedBitmap();
			((Button)findViewById(R.id.buttonSaveImage)).setEnabled(false);
			new DoBackgroungJob().execute();			
		} catch (Throwable e) {
			Log.e("Exception", "during image crop");
			((Button)findViewById(R.id.buttonSaveImage)).setEnabled(true);
			e.printStackTrace();
		}
	}
	
	/* public static Uri getUri(Context context, Bitmap bitmap) {
	        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
	        return  path!=null ? Uri.parse(path) : null;
	    }*/
	
	class DoBackgroungJob extends AsyncTask<Void, Bitmap,String> {
		@Override
		protected String doInBackground(Void... params) {
			String path=null;
			if (currentImage != null) {
				File file = createImageFile();
				FileOutputStream fout;
				try {
					fout = new FileOutputStream(file);
					currentImage.compress(Bitmap.CompressFormat.PNG, 70, fout);
					fout.flush();
					path=file.getPath();
				} catch (Exception e) {
					e.printStackTrace();
					path=null;
				}
				if (currentImage != null) {
					currentImage.recycle();
				}
			}
			return path;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			imageProgress.setVisibility(View.GONE);
			((Button)findViewById(R.id.buttonSaveImage)).setEnabled(true);
			if (result != null) {
				Intent data = new Intent();
				data.putExtra(Constant.IMAGE_PATH, result);
				setResult(RESULT_OK, data);
			} else {
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	}

	/*
	 * private void onSavedClick() { if(imageToCrop.getDrawable()==null){
	 * setResult(RESULT_CANCELED); finish(); } else{
	 * imageToCrop.setDrawingCacheEnabled(true); Bitmap mainImage =
	 * imageToCrop.getDrawingCache(); try{ int[] cord=getCoordinates();
	 * RelativeLayout v=(RelativeLayout)findViewById(111); currentImage =
	 * Bitmap.createBitmap(mainImage,cord[0],cord[1],v.getMeasuredWidth(),v.
	 * getMeasuredHeight()); } catch(OutOfMemoryError error){
	 * error.printStackTrace(); } File file=createImageFile();
	 * 
	 * FileOutputStream fout; try { fout = new FileOutputStream(file);
	 * currentImage.compress(Bitmap.CompressFormat.PNG, 80,fout); fout.flush();
	 * } catch (Exception e) { e.printStackTrace(); } Intent data = new
	 * Intent(); data.putExtra(Constants.IMAGE_PATH,file.getPath());
	 * setResult(RESULT_OK, data); if(currentImage!=null){
	 * currentImage.recycle(); } finish(); } }
	 */

	@Override
	protected void onDestroy() {
		if (currentImage != null) {
			currentImage.recycle();
			currentImage = null;
		}
		super.onDestroy();
	}
}
