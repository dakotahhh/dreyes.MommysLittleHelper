package dreyes.mommyslittlehelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;

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
	    
	    
	    
	    setContentView(R.layout.activity_facebookaddphoto);

	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	                TextView welcome = (TextView) findViewById(R.id.welcome);
	                welcome.setText("Hello " + user.getName() + "!");
	              }
	            }
	          });
	        }
	      }
	    });
	  }

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }

}
