package dreyes.mommyslittlehelper;

import android.app.Activity;

import com.facebook.*;
import com.facebook.model.*;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.content.*;

public class FacebookAddPhotoActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebookaddphoto);
		//Start facebook login
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			//callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if(session.isOpened())
				{
					//request to the /me API
					Request.newMeRequest(session, new Request.GraphUserCallback() {
						//callbacka fter graph api response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if(user!=null)
							{
								TextView welcome = (TextView)findViewById(R.id.welcome);
								welcome.setText("Hello " + user.getName() + "!");
								Log.d("USERNAME", user.getName());
							}
							else
							{
								TextView welcome = (TextView)findViewById(R.id.welcome);
								welcome.setText("Could not find user name");
								Log.d("USERNAME", "not found");
							}
						}
					}).executeAsync();
				}
				Log.d("session is not open", "hello");
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
