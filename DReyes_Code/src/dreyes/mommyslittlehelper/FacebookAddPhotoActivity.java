package dreyes.mommyslittlehelper;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.os.Parcelable;
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
	private final int REQUEST_GALLERY = Menu.FIRST + 2;	
	private final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath, filemanagerString, selectedImagePath;
	
	private Facebook facebook;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
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
			File photoFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			
			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
		}
		
		private void startGalleryActivity()
		{
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
			startActivityForResult(intent, REQUEST_GALLERY);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == REQUEST_GALLERY && requestCode == RESULT_OK)
			{
				Uri selectedImage = data.getData();
				filemanagerString = selectedImage.getPath();
				String[] projection = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
				if(cursor!=null)
				{
					cursor.moveToFirst();
					int column_index = cursor
							.getColumnIndex(projection[0]);
					String filePath = cursor.getString(column_index);
					cursor.close();
					Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
				}
				

			}
			else if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK)
			{
				File photo = new File(Environment.getExternalStorageDirectory().toString());
				for(File temp: photo.listFiles())
				{
					if(temp.getName().equals("temp.jpg"))
					{
						photo = temp;
						break;
					}
				}
				BitmapFactory.Options bo = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), bo);
				String path = Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
				photo.delete();
				OutputStream out = null;
				File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
				try
				{
					out = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
					out.flush();
					out.close();
				}catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}catch(IOException e)
				{
					e.printStackTrace();
				}
//				Bundle extras = data.getExtras();
//				Bitmap imageBitmap = (Bitmap)extras.get("data");
//				image.setImageBitmap(imageBitmap);
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
		


	  

}
