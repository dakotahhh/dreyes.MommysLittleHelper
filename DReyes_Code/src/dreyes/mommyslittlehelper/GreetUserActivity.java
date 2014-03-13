package dreyes.mommyslittlehelper;

import java.io.IOException;
import java.util.GregorianCalendar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class GreetUserActivity extends Activity implements OnClickListener{

	private final String TAG = "GREETUSERACTIVITY";
	private ImageButton fedBabyButton, sleepingBabyButton, wakingBabyButton, diaperChangedButton, signingOutButton, breastPumpButton, addPhotoButton, doctorsAppointmentButton, trackMoodButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greetuser);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		fedBabyButton = (ImageButton)findViewById(R.id.fedBabyButton);
		sleepingBabyButton = (ImageButton)findViewById(R.id.sleepingBabyButton);
		wakingBabyButton = (ImageButton)findViewById(R.id.wakingBabyButton);
		diaperChangedButton = (ImageButton)findViewById(R.id.diaperChangeButton);
//		signingOutButton = (ImageButton)findViewById(R.id.signOutButton);
		breastPumpButton = (ImageButton)findViewById(R.id.breastPumpButton);
		addPhotoButton = (ImageButton)findViewById(R.id.uploadPhotoButton);
		doctorsAppointmentButton = (ImageButton)findViewById(R.id.createAppointmentButton);
		trackMoodButton = (ImageButton)findViewById(R.id.trackMoodButton);
		fedBabyButton.setOnClickListener(this);
		sleepingBabyButton.setOnClickListener(this);
		wakingBabyButton.setOnClickListener(this);
		diaperChangedButton.setOnClickListener(this);
		breastPumpButton.setOnClickListener(this);
		addPhotoButton.setOnClickListener(this);
		doctorsAppointmentButton.setOnClickListener(this);
		trackMoodButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		if(v.getId() == R.id.fedBabyButton)
		{
			intent = new Intent(this, FedBabyActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.sleepingBabyButton)
		{
			intent = new Intent(this, SleepingBabyActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.wakingBabyButton)
		{
			intent = new Intent(this, WakingBabyActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.diaperChangeButton)
		{
			intent = new Intent(this, ChangedDiaperActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.breastPumpButton)
		{
			intent = new Intent(this, BreastPumpActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.uploadPhotoButton) 
		{
			intent = new Intent(this, FacebookAddPhotoActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.createAppointmentButton)
		{
			intent = new Intent(this, SetDoctorsAppointmentActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.trackMoodButton)
		{
			intent = new Intent(this, MoodActivity.class);
			startActivity(intent);
		}
	}


}
