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

public class GreetUserActivity extends Activity implements OnClickListener{

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
		fedBabyButton.setOnClickListener(this);
		sleepingBabyButton.setOnClickListener(this);
		wakingBabyButton.setOnClickListener(this);
		diaperChangedButton.setOnClickListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		if(v.getId() == R.id.fedBaby)
		{
			intent = new Intent(this, FedBabyActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.babySleeping)
		{
			intent = new Intent(this, SleepingBabyActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.babyWaking)
		{
			intent = new Intent(this, WakingBabyActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.changedDiaper)
		{
			intent = new Intent(this, DiaperChangeActivity.class);
			startActivity(intent);
		}
	}

	
	
}
