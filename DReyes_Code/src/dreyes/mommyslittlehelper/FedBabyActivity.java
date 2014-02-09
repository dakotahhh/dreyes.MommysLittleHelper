package dreyes.mommyslittlehelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import dreyes.mommyslittlehelper.R;
import dreyes.mommyslittlehelper.R.id;
import dreyes.mommyslittlehelper.R.layout;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FedBabyActivity extends Activity implements OnClickListener{

	private Button submit, timeSubmit;
	private TextView timeTitle;
	private final int TIME_DIALOG_ID = 000;
	private int hour, minutes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fedbaby);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		timeSubmit = (Button)findViewById(R.id.timeSubmit);
		timeSubmit.setOnClickListener(this);
		timeTitle = (TextView)findViewById(R.id.timeTitle);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.timeSubmit)
		{
			showDialog(TIME_DIALOG_ID);
		}
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
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int selectedHour, int selectedMinutes) {
			hour = selectedHour;
			minutes = selectedMinutes;
			updateTimeDisplay();
			
		}
	};
	
	private void updateTimeDisplay()
	{
		timeTitle.setText(new StringBuilder()
		.append("Appointment Time: ")
		.append(pad(hour)).append(":")
		.append(minutes));
	}
	
	private String pad(int time)
	{
		
	}
	
	
	
}
