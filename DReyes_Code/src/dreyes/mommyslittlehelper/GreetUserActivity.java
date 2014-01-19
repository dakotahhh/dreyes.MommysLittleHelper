package dreyes.mommyslittlehelper;

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
	private Button fedBabyButton, sleepingBabyButton, wakingBabyButton, diaperChangedButton, signingOutButton, breastPumpButton;
	
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
		breastPumpButton = (Button)findViewById(R.id.breastpump);
		fedBabyButton.setOnClickListener(this);
		sleepingBabyButton.setOnClickListener(this);
		wakingBabyButton.setOnClickListener(this);
		diaperChangedButton.setOnClickListener(this);
		breastPumpButton.setOnClickListener(this);
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
			GregorianCalendar time = new GregorianCalendar();
			intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Fell Asleep");
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
			intent.putExtra(Events.HAS_ALARM, false);
			startActivity(intent);
		}
		else if(v.getId() == R.id.babyWaking)
		{
			GregorianCalendar time = new GregorianCalendar();
			intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Woke Up");
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
			intent.putExtra(Events.HAS_ALARM, false);
			startActivity(intent);
		}
		else if(v.getId() == R.id.changedDiaper)
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
		else if(v.getId() == R.id.breastpump)
		{
			intent = new Intent(this, BreastPumpActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.addPhoto) 
		{
			intent = new Intent(this, AddPhotoActivity.class);
			startActivity(intent);
		}
	}

	
	
}
