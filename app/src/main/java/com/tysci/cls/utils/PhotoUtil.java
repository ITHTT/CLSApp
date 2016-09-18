package com.tysci.cls.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import com.tysci.cls.app.AppConfigInfo;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PhotoUtil {
	private Context context;
	private Uri image_uri=null;
	private String image_name=null;
	public static final int REQUEST_CODE_TAKE_PHOTO=0x0011;
	public static final int REQUEST_CODE_SELECT_PHOTO=0x00022;
	public static final int REQUEST_CODE_CROP_PHOTO=0x0033;
	
	public PhotoUtil(Context context){
		this.context=context;
	}
	
	public void takePhoto(){
		ContentValues contentValues=new ContentValues();
		long times=System.currentTimeMillis();
		image_name=times+".jpg";
		contentValues.put(Media.DISPLAY_NAME, image_name);
		contentValues.put(Media.MIME_TYPE, "image/jpeg");
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			image_uri=this.context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
		}else{
			image_uri=this.context.getContentResolver().insert(Media.INTERNAL_CONTENT_URI, contentValues);
		}
		
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
		((Activity)context).startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
	}
	
	public void selectPhoto(){
		Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		image_name=System.currentTimeMillis()+".jpg";
		((Activity)context).startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
	}

	public void cropPhoto(Uri uri){
		KLog.e("开始剪切图片...");
		if(uri==null){
			return;
		}
		//image_uri=uri;
		KLog.e("剪切图片...");
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 240);// 输出图片大小
		intent.putExtra("outputY", 240);
		intent.putExtra("return-data", false);
		String outPath= AppConfigInfo.APP_IMAGE_PATH+File.separator+System.currentTimeMillis()+".jpg";
		File outFile=new File(outPath);
		if(!outFile.exists()){
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Uri outUri=Uri.fromFile(outFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		((Activity)context).startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}

	/**
	 * 压缩图片至指定尺寸
	 * @param uri
	 * @param width
	 * @param height
	 * @return
	 */
	public  Bitmap compressBitmap(Uri uri,int width,int height) throws FileNotFoundException {
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
		final int bitmapWidth=options.outWidth;
		final int bitmapHeight=options.outHeight;
		int inSampleSize=1;
		if (width == 0 || height == 0) return null;
		if (bitmapHeight > height || bitmapWidth >width) {
			final int heightRatio = Math.round((float) bitmapHeight/ (float) height);
			final int widthRatio = Math.round((float) bitmapWidth / (float) width);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		options.inSampleSize=inSampleSize;
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
	}

	public Uri getTakePhotoUri(){
		return image_uri;
	}
	
	public Bitmap getTakePhoto(){
		return revitionImageSize();
	}
	
	public Bitmap getSelectPhoto(Intent data){
		//System.out.println("...图片内容:"+data.toURI());
		if(data!=null){
			image_uri=data.getData();
			if(image_uri!=null){
				return revitionImageSize();
			}
		}
		return null;
	}
	
	private Bitmap revitionImageSize(){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		options.inPreferredConfig = Config.RGB_565;
		try {
			Bitmap bmp=BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image_uri),null,options);
			/*int heightRatio=(int)Math.ceil(bmpFactoryOptions.outHeight/(float)dh);
			int widthRatio=(int)Math.ceil(bmpFactoryOptions.outWidth/(float)dw);
			if(heightRatio>1&&widthRatio>1){
				bmpFactoryOptions.inSampleSize=heightRatio>widthRatio?heightRatio:widthRatio;
			}
			bmpFactoryOptions.inJustDecodeBounds=false;
			bmp=BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image_uri),null,bmpFactoryOptions);
			if(bmp!=null){
				save_image_path=saveBitmap(bmp);
			}
			return bmp;
			*/
			int i = 0;
			Bitmap bitmap = null;
			BufferedInputStream in=null;
			while (true)
			{
				if ((options.outWidth >> i <= 1000)
						&& (options.outHeight >> i <= 1000))
				{
					in = new BufferedInputStream(
							context.getContentResolver().openInputStream(image_uri));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}
			return bitmap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private  Bitmap revitionImageSize(String path) throws IOException
	{
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Config.RGB_565;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true)
		{
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000))
			{
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public  static String getRealPath(Context context,Uri uri){
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	public Bitmap decodeUriAsBitmap(Uri uri){
		Bitmap bitmap = null;
		try {
			InputStream inputStream=context.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;

	}

	public void uploadBitmap(String tag,Bitmap bitmap,HttpClientUtil.ProgressResponseCallBack progressResponseCallBack){
		String userAccount=UserInfoUtils.getUserAccount(context);
		String token=UserInfoUtils.getUserToken(context);
		String url= HttpUrls.HTTP_HOST_URL+"user/modify/portrait";
        //String url="http://192.168.10.178:8081/app/api/v1/user/modify/portrait";
		if(bitmap!=null){
			Map<String,String> headers=new HashMap<>(1);
			headers.put("token",token);
			HashMap<String,String> params=new HashMap<String,String>(1);
			params.put("account",userAccount);
			HttpClientUtil.getHttpClientUtil().uploadBitmap(tag,url, headers, params, bitmap, progressResponseCallBack);
		}
	}
}
