package com.app.baccoon.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageIntent {

	public static final int PICK_CAMERA = 32;
	private Activity activity;
	private String currentPath="";
	public ImageIntent(Activity activity) {
		this.activity = activity;
	}
	public ImageIntent() {
	}

	public String getImages() {
		String currentPhotoPath = null;
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
			File imagefile = createImageFile();
			if (imagefile != null) {
				currentPhotoPath = imagefile.getAbsolutePath();
				// imageCaptureUri = Uri.fromFile(imagefile);
				Intent galleryPick = new Intent();
				/*if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
				}else{
					galleryPick.setAction(Intent.ACTION_GET_CONTENT);
				}*/
				galleryPick.setAction(Intent.ACTION_PICK);
				galleryPick.setType("image/*");
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagefile));
				takePictureIntent.putExtra("return-data", false);
				Intent chooserIntent = Intent.createChooser(galleryPick, "Select or take a new Picture");
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePictureIntent });
				try {
					activity.startActivityForResult(chooserIntent, PICK_CAMERA);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return currentPhotoPath;
	}
	
	public String getRealPathFromUri(Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
			cursor.moveToFirst();
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			return cursor.getString(column_index);
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	    return null;
	}

	
	/*public File createImageFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File mFileTemp = null;
		//ContextWrapper contextWrapper=new ContextWrapper(activity);
		//String root=contextWrapper.getFilesDir().getAbsolutePath();
		String root=activity.getDir("my_sub_dir",Context.MODE_PRIVATE).getAbsolutePath();
		File myDir = new File(root + "/Img");
		if(!myDir.exists()){
			myDir.mkdirs();
		}
		try {
			mFileTemp=File.createTempFile(imageFileName,".jpg",myDir.getAbsoluteFile());
			Log.e("path",mFileTemp.getAbsolutePath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return mFileTemp;
	}*/

	public File createImageFile() {
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
				mFileTemp = File.createTempFile(imageFileName, ".jpg", activity.getFilesDir());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mFileTemp;
	}


	public String saveImageToSdcard(Bitmap currentImage) {
		String path="";
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
		return path;
	}

	public void showImageChooser() {
		final String[] items           = new String[] {"Camera", "Gallery"};
		ArrayAdapter<String> adapter  = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder     = new AlertDialog.Builder(activity);
		builder.setTitle("Select Image from");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if(item==0){
				Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file=null;
				file = createImageFile();
				if(file!=null){
				currentPath=file.getAbsolutePath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				intent.putExtra("return-data", false);
				try {
					activity.startActivityForResult(intent, Constant.PICK_CAMERA);
					((OnImageChoosenListener)activity).onImageFileCreated(currentPath);
				} catch (Exception e) {
				}
				}
			    dialog.dismiss();
				}  
				else {
					Intent in = new Intent();
					in.setType("image/*");
					in.setAction(Intent.ACTION_PICK);
					/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
						in.setAction(Intent.ACTION_PICK);
					} else {
						in.setAction(Intent.ACTION_GET_CONTENT);
						in.addCategory(Intent.CATEGORY_OPENABLE);
					}*/
					//activity.startActivityForResult(Intent.createChooser(in, "Complete action using"), PICK_FROM_FILE);
					try {
						activity.startActivityForResult(in, Constant.PICK_GALLERY);
						((OnImageChoosenListener)activity).onImageFileCreated(currentPath);
					} catch (Exception e) {
						// TODO: handle exception
					}
					dialog.dismiss();
				}
			}
		} );
		final AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public static interface OnImageChoosenListener {
		void onImageFileCreated(String path);
	}

}
