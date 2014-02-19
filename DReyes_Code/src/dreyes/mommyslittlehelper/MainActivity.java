package dreyes.mommyslittlehelper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.support.v4.app.ShareCompat.IntentBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener{

	private static final String TAG = "MainActivity";
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	
	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	
	private View signInButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlusClient = new PlusClient.Builder(this, this, this)
		.setActions("http://schemas.google.com/AddActivity","http://schemas.google.com/BuyActivity")
		.setScopes(Scopes.PLUS_LOGIN)
		.build();
		
		if(mPlusClient.isConnected())
		{
			Toast.makeText(this, "already signed in", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, GreetUserActivity.class);
			startActivity(intent);
		}
		else
		{
			mConnectionProgressDialog = new ProgressDialog(this);
			mConnectionProgressDialog.setMessage("Signing in...");
			
			setContentView(R.layout.activity_main);
			signInButton = (View)findViewById(R.id.sign_in_button);
			
			
			signInButton.setOnClickListener(this);
		}
		
		
		
	}
	
	@Override
	public void onClick(View view) {
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
					mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				}catch(SendIntentException e)
				{
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mPlusClient.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if(mConnectionProgressDialog.isShowing())
		{
			//The user clicked the sign-in button already. Resolve connection errors, wait until onConnected()
			if(result.hasResolution())
			{
				try
				{
					result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				}catch(SendIntentException e)
				{
					mPlusClient.connect();
				}
			}
		}
		//Save teh intent so that we can start an activity when user click sign-in
		mConnectionResult = result;
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		mConnectionProgressDialog.dismiss();
		String accountName = mPlusClient.getAccountName();
		Toast.makeText(this, accountName+ " is connected.", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, GreetUserActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if(requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK)
		{
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	
	
	
}
