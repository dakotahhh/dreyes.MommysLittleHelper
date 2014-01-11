package dreyes.mommyslittlehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GreetUserActivity extends Activity{

	private final String TAG = "GREETUSERACTIVITY";
	private Button fedBabyButton, sleepingBabyButton, wakingBabyButton, diaperChangedButton, signingOutButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greetuser);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		fedBabyButton = (Button)findViewById(R.id.fedBaby);
		sleepingBabyButton = (Button)findViewById(R.id.babySleeping);
		wakingBabyButton = (Button)findViewById(R.id.babyWaking);
		diaperChangedButton = (Button)findViewById(R.id.changedDiaper);
		signingOutButton = (Button)findViewById(R.id.signingOut);
		fedBabyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GreetUserActivity.this, FedBabyActivity.class);
				startActivity(intent);
				
			}
		});
		sleepingBabyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GreetUserActivity.this, SleepingBabyActivity.class);
				startActivity(intent);
				
			}
		});
		wakingBabyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GreetUserActivity.this, WakingBabyActivity.class);
				startActivity(intent);
				
			}
		});
		diaperChangedButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GreetUserActivity.this, DiaperChangeActivity.class);
				startActivity(intent);
				
			}
		});
		signingOutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(GreetUserActivity.this , "user wants to sign out", Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	private void createReferenceButtons()
	{
	}
	
	public void setOnClicks()
	{
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
//	public void fedBabyActivity(View view)
//	{
//		Intent intent = new Intent(this, FedBabyActivity.class);
//		startActivity(intent);
//	}
//	
//	public void sleepingBabyActivity(View view)
//	{
//		Log.d(TAG, "sleeping baby");
//		Intent intent = new Intent(this, SleepingBabyActivity.class);
//		Log.d(TAG, "intent created");
//		startActivity(intent);
//		Log.d(TAG, "activity started");
//	}
//	
//	public void wakingBabyActivity(View view)
//	{
//		Log.d(TAG, "waking baby");
//		Intent intent = new Intent(this, WakingBabyActivity.class);
//		startActivity(intent);
//	}
//	
//	public void diaperChangeActivity(View view)
//	{
//		Log.d(TAG, "diaper baby");
//		Intent intent = new Intent(this, DiaperChangeActivity.class);
//		startActivity(intent);
//	}

	
	
}
