package dreyes.mommyslittlehelper;

import java.io.File;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.facebook.*;
import com.facebook.model.GraphUser;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
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
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			tempUri = getTempUri();
			if(tempUri != null)
			{
				intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
			}
			startActivityForResult(intent, -1);
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
			if(requestCode == ID_INTENT_ID)
			{
				if(data != null)
				{
					Log.d("ON ACTIVITY", "idButSelPic Photopicker: " + data.getDataString());
					Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
					cursor.moveToFirst();
					int idx = cursor.getColumnIndex(ImageColumns.DATA);
					String fileSource = cursor.getString(idx);
					Log.d("PICUTRE", "Picture: " + fileSource);
					
					Bitmap bitmapPreview = BitmapFactory.decodeFile(fileSource);
					BitmapDrawable bmpDrawable = new BitmapDrawable(bitmapPreview);
					image.setBackground(bmpDrawable);
					
				}
			}
			else
			{
				Log.d("Else", "idButSelPic Photopicker cancelled");
			}
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
