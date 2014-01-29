package dreyes.mommyslittlehelper;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;

import com.facebook.*;
import com.facebook.model.*;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FacebookAddPhotoActivity extends Activity implements OnClickListener
{

	Button takePhoto, chooseFromGallery;
	Uri tempUri = null;
	
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
			}
			else if(v.getId() == R.id.choose_from_gallery)
			{
				Toast.makeText(this, "Choose From Gallery", Toast.LENGTH_LONG).show();
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
//			startActivityForResult(intent, getRequestCode());
		}
		
		private void startGalleryActivity()
		{
			tempUri = null;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			String selectPicture = getResources().getString(R.string.select_picture);
//			startActivityForResult(Intent.createChooser(intent, selectPicture), getRequestCode());
		}
		
		private Uri getTempUri()
		{
			String imgFileName = "mommys_little_helper" + System.currentTimeMillis() + ".jpg"; 
			File image = new File
					(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imgFileName);
			return Uri.fromFile(image);
		} 
	  
	  

}
