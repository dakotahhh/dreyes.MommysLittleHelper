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
import android.widget.Toast;

public class GreetUserActivity extends Activity implements OnClickListener{

	private final String TAG = "GREETUSERACTIVITY";
	private Button fedBabyButton, sleepingBabyButton, wakingBabyButton, diaperChangedButton, signingOutButton, breastPumpButton, addPhotoButton, doctorsAppointmentButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greetuser);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		fedBabyButton = (Button)findViewById(R.id.fedBabyButton);
		sleepingBabyButton = (Button)findViewById(R.id.sleepingBabyButton);
		wakingBabyButton = (Button)findViewById(R.id.wakingBabyButton);
		diaperChangedButton = (Button)findViewById(R.id.diaperChangeButton);
		signingOutButton = (Button)findViewById(R.id.signOutButton);
		breastPumpButton = (Button)findViewById(R.id.breastPumpButton);
		addPhotoButton = (Button)findViewById(R.id.uploadPhotoButton);
		doctorsAppointmentButton = (Button)findViewById(R.id.createAppointmentButton);
		fedBabyButton.setOnClickListener(this);
		sleepingBabyButton.setOnClickListener(this);
		wakingBabyButton.setOnClickListener(this);
		diaperChangedButton.setOnClickListener(this);
		breastPumpButton.setOnClickListener(this);
		addPhotoButton.setOnClickListener(this);
		doctorsAppointmentButton.setOnClickListener(this);
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
			GregorianCalendar time = new GregorianCalendar();
			intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Diaper Change");
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
			intent.putExtra(Events.HAS_ALARM, false);
			startActivity(intent);
		}
		else if(v.getId() == R.id.breastPumpButton)
		{
			intent = new Intent(this, BreastPumpActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.uploadPhotoButton) 
		{
			intent = new Intent(this, ChooseUploadTypeActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.createAppointmentButton)
		{
			intent = new Intent(this, SetDoctorsAppointmentActivity.class);
			startActivity(intent);
		}
	}

	
}
