package dreyes.mommyslittlehelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import dreyes.mommyslittlehelper.R;
import dreyes.mommyslittlehelper.R.id;
import dreyes.mommyslittlehelper.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FedBabyActivity extends Activity implements OnClickListener{

	Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fedbaby);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		case R.id.action_baby_events:
			Intent intent = new Intent(this, GreetUserActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		EditText foodDescription = (EditText)findViewById(R.id.foodDescriptionSubmit);
		EditText timeSubmit = (EditText)findViewById(R.id.timePicker);
		String food = foodDescription.getText().toString();
		String time = timeSubmit.getText().toString();
		GregorianCalendar calDate = null;
		if(time.isEmpty())
		{
			calDate = new GregorianCalendar();
		}
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Fed Baby");
		intent.putExtra(Events.DESCRIPTION, food);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis());
		intent.putExtra(Events.HAS_ALARM, false);
		startActivity(intent);
	}
	
	
	
}
