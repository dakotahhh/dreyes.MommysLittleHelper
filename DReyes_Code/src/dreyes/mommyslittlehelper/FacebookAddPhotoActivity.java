package dreyes.mommyslittlehelper;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;

import com.facebook.*;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.IntToString;
import android.widget.*;

public class FacebookAddPhotoActivity extends Activity implements OnClickListener{

	private Uri tempUri;
	private Button takePhoto, chooseFromGallery;
	private ImageView image;
	private final int ID_INTENT_ID = Menu.FIRST + 2;	
	private final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	
	private Facebook facebook;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    code to get the keyhash of this app
//	    try
//        {
//        	PackageInfo info = getPackageManager().getPackageInfo("com.facebook.samples.hellofacebook",  PackageManager.GET_SIGNATURES);
//        	for(Signature signature : info.signatures)
//        	{
//        		MessageDigest md = MessageDigest.getInstance("SHA");
//        		md.update(signature.toByteArray());
//        		Log.d("KAYHASH: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//        	}
//        }catch(NameNotFoundException e)
//        {
//        	
//        }catch (NoSuchAlgorithmException e) {
//			// TODO: handle exception
//		}
	    setContentView(R.layout.activity_uploadwithcameraorgallery);
	    
	    takePhoto = (Button)findViewById(R.id.take_photo);
	    takePhoto.setOnClickListener(this);
	    chooseFromGallery = (Button)findViewById(R.id.choose_from_gallery);
	    chooseFromGallery.setOnClickListener(this);
	    image = (ImageView)findViewById(R.id.image);
	    
	    
	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {

	          // make request to the /me API
	          Request.newMeRequest(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	                TextView welcome = (TextView) findViewById(R.id.welcome);
	                welcome.setText("Hello " + user.getName() + "!");
	              }
	            }
	          }).executeAsync();
	        }
	      }
	    });
	  }
	  
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.take_photo)
			{
				Toast.makeText(this, "Take Photo", Toast.LENGTH_LONG).show();
				startCameraActivity();
			}
			else if(v.getId() == R.id.choose_from_gallery)
			{
				Toast.makeText(this, "Choose From Gallery", Toast.LENGTH_LONG).show();
				startGalleryActivity();
			}
			
		}
		
		
		private void startCameraActivity()
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if(intent.resolveActivity(getPackageManager()) != null)
			{
				File photoFile = null;
				try
				{
					photoFile = createImageFile();
				}catch(IOException e)
				{
					
				}
				if(photoFile != null)
				{
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				}
				startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
			}
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			tempUri = getTempUri();
//			if(tempUri != null)
//			{
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//			}
//			startActivityForResult(intent, -1);
		}
		
		private void startGalleryActivity()
		{
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, ID_INTENT_ID);
//			tempUri = null;
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			intent.setType("image/*");
//			String selectPicture = getResources().getString(R.string.select_picture);
//			startActivityForResult(Intent.createChooser(intent, selectPicture), -1);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == ID_INTENT_ID && requestCode == RESULT_OK)
			{
				Uri selectedImage = data.getData();
				String filePath = null;
				String[] columns = {MediaColumns.DATA};
				Cursor cursor = this.getContentResolver().query(selectedImage, columns, null, null, null);
				if(cursor != null)
				{
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(columns[0]);
					filePath = cursor.getString(columnIndex);
					if(!cursor.isClosed())
					{
						cursor.close();
					}
				}
				else
				{
					filePath = selectedImage.getPath();
				}
				if(filePath != null)
				{
					uploadImagePath(filePath);
				}
				else
				{
					Toast toast = Toast.makeText(this, "unable to retrieve the selected image", Toast.LENGTH_LONG);
					toast.show();
				}
//				if(data != null)
//				{
//					Log.d("ON ACTIVITY", "idButSelPic Photopicker: " + data.getDataString());
//					Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
//					cursor.moveToFirst();
//					int idx = cursor.getColumnIndex(ImageColumns.DATA);
//					String fileSource = cursor.getString(idx);
//					Log.d("PICUTRE", "Picture: " + fileSource);
//					
//					Bitmap bitmapPreview = BitmapFactory.decodeFile(fileSource);
//					BitmapDrawable bmpDrawable = new BitmapDrawable(bitmapPreview);
//					image.setBackground(bmpDrawable);
//					
//				}
			}
			else if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK)
			{
				Bundle extras = data.getExtras();
				Bitmap imageBitmap = (Bitmap)extras.get("data");
				image.setImageBitmap(imageBitmap);
			}
		}
		
		private void uploadImagePath(String filePath)
		{
			FileInputStream fileStream = null;
			try
			{
				fileStream = new FileInputStream(new File(filePath));
				byte[] bytes = convertStreamToBytes(fileStream);
				uploadImageBytes(bytes);
			}catch(Exception e)
			{
				Toast toast = Toast.makeText(this, "unable to retrieve image", Toast.LENGTH_LONG);
				toast.show();
			}finally
			{
				closeStream(fileStream);
			}
		}
		
		private void uploadImageBytes(byte[] bytes)
		{
			Bundle params = new Bundle();
			params.putString(Facebook.TOKEN, facebook.getAccessToken());
			params.putString("method", "photos.upload");
			params.putByteArray("picture", bytes);
			AsyncFacebookRunner ar = new AsyncFacebookRunner(facebook);
//			ar.request(null, params, "POST", new WallPostListener());
		}
		
		public static byte[] convertStreamToBytes(InputStream stream) throws IOException
		{
			if(stream == null)
			{
				return null;
			}
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			copyStream(stream, output);
			return output.toByteArray();
		}
		
		public static void copyStream(InputStream from, OutputStream to) throws IOException
		{
			byte data[] = new byte[8192];
			int count;
			while((count = from.read(data)) != -1)
			{
				to.write(data,0,count);
			}
			from.close();
		}
		
		public static void closeStream(Closeable stream)
		{
			try
			{
				if(stream != null)
				{
					stream.close();
				}
			}catch(Exception e)
			{
				
			}
		}
		
		private File createImageFile() throws IOException
		{
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = "JPEG_" + timeStamp + "_";
			File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File image = File.createTempFile(imageFileName, ".jpg", storageDir);
			mCurrentPhotoPath = "file:" +image.getAbsolutePath();
			return image;
		}
		
		private void galleryAddPic()
		{
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(mCurrentPhotoPath);
			Uri contentUri = Uri.fromFile(f);
			intent.setData(contentUri);
			this.sendBroadcast(intent);
		}
		
		private void setPic()
		{
			int width = image.getWidth();
			int height = image.getHeight();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, options);
			int photoW = options.outWidth;
			int photoH = options.outHeight;
			int scaleFactor = Math.min(photoW/width, photoH/height);
			options.inJustDecodeBounds = false;
			options.inSampleSize = scaleFactor;
			options.inPurgeable = true;
			Bitmap bit = BitmapFactory.decodeFile(mCurrentPhotoPath,options);
			image.setImageBitmap(bit);
		}
	
		private Uri getTempUri()
		{
			String imgFileName = "mommys_little_helper" + System.currentTimeMillis() + ".jpg"; 
			File image = new File
					(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imgFileName);
			return Uri.fromFile(image);
		} 
//	  
//		private void handleAnnounce()
//		{
//			Session session = Session.getActiveSession();
//			if(session != null && session.isOpened())
//			{
//				handleGraphApiAnnounce();
//			}
//			else
//			{
//				handleNativeShareAnnounce();
//			}
//		}
//		
//		private void handleGraphApiAnnounce()
//		{
//			Session session = Session.getActiveSession();
//			List<String> permissions = session.getPermissions();
//			if(!permissions.contains("publish_actions"))
//			{
//				requestPublishPermission(session);
//				return;
//			}
//			
//			ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getActivity.getResources.getString(R.string.progress_dialog),true);
//		}
	  

}
