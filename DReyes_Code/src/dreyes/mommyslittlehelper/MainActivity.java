package dreyes.mommyslittlehelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	PlusClient mPlusClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlusClient = new PlusClient.Builder(this,this,this).setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build();
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//notify the user that they are signing in with Google+ services
	public void notifyUserGoogleSignIn(View view)
	{
		Toast toast = Toast.makeText(getApplicationContext(), "Logging in using Google+", Toast.LENGTH_SHORT);
		toast.show();
		findViewById(R.id.sign_in_button).setOnClickListener(this);
	}
	
	public void onClick(View view)
	{
		if(view.getId() == R.id.sign_in_button && !mPlusClient.isConnected())
		{
			if(mConnectionResult == null)
			{
				mConnectionProgressDialog.show();
			}
			else
			{
				try
				{
					mConnectionResult.startResolutionForResult(this,REQUEST_COE_RESOLVE_ERR);
				}catch(SendIntentException e)
				{
					//Try connecting again
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}
	
	public void onConnected(Bundle connectionHint)
	{
		mConectionProgressDialog.dismiss{);
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}
}
