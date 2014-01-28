package dreyes.mommyslittlehelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;

import com.facebook.*;
import com.facebook.model.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FacebookAddPhotoActivity extends FragmentActivity 
{

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    
	    
	    try
        {
        	PackageInfo info = getPackageManager().getPackageInfo("com.facebook.samples.hellofacebook",  PackageManager.GET_SIGNATURES);
        	for(Signature signature : info.signatures)
        	{
        		MessageDigest md = MessageDigest.getInstance("SHA");
        		md.update(signature.toByteArray());
        		Log.d("KAYHASH: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        	}
        }catch(NameNotFoundException e)
        {
        	
        }catch (NoSuchAlgorithmException e) {
			// TODO: handle exception
		}
	    
	    
	    
	    setContentView(R.layout.activity_uploadwithcameraorgallery);

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
	  
//	  private interface GraphObjectWithId extends GraphObject
//	  {
//		String getId();  
//	  }
//	  
//	  
//	  private void showPublishResult(String message, GraphObject result, FacebookRequestError error)
//	  {
//		  String title = null;
//		  String alertMessage = null;
//		  if(error == null)
//		  {
//			  title = getString(R.string.success);
//			  String id = result.cast(GraphObjectWithId.class).getId();
//			  alertMessage = getString(R.string.successfully_posted_post, message, id);
//			  
//		  }
//		  else
//		  {
//			  title = getString(R.string.error);
//			  alertMessage = error.getErrorMessage();
//		  }
//		  new AlertDialog.Builder(this)
//		  .setTitle(title).setMessage(alertMessage).setPositiveButton(R.string.ok, null)
//		  .show();
//	  }
//	  
//	  private void postPhoto()
//	  {
//		  if(hasPublishPermission())
//		  {
//			  Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
//			  Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
//				
//				@Override
//				public void onCompleted(Response response) {
//					showPublishResult(getString(R.string.photo_post), response.getGraphObject(), response.getError());
//					
//				}
//			});
//			  request.executeAsync();
//		  }
//	  }
//	  
//	  
//	  private boolean hasPublishPermission()
//	  {
//		  Session session = Session.getActiveSession();
//		 return session != null && session.getPermissions().contains("publish_actions");
//	  }
//
//	  @Override
//	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	      super.onActivityResult(requestCode, resultCode, data);
//	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//	  }
//	  
//	  private void performPublish(boolean allowNoSession)
//	  {
//		  Session session = Session.getActiveSession();
//		  if(session != null)
//		  {
//			  if(hasPublishPermission())
//			  {
//				 
//			  }
//		  }
//	  }

}
