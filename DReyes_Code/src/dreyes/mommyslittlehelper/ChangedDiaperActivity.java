package dreyes.mommyslittlehelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class ChangedDiaperActivity extends Activity {

	private TextView currentTime;
	private Button changeTime, submit;
	private RadioGroup pottyType;
	private RadioButton pottyNumber;
	private int year, month, day, hour, minutes;
	private final int TIME_DIALOG_ID = 000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeddiaper);
		
		currentTime = (TextView)findViewById(R.id.currentTimeDiaper);
		
		changeTime = (Button)findViewById(R.id.changeTimeDiaper);
		changeTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
				
			}
		});
		
		pottyType = (RadioGroup)findViewById(R.id.radioPottyType);
		
		
		submit = (Button)findViewById(R.id.submitDiaper);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createCalendarEvent();
				int pottyNumberId = pottyType.getCheckedRadioButtonId();
				pottyNumber = (RadioButton)findViewById(pottyNumberId);
				
			}
		});
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minutes = c.get(Calendar.MINUTE);
		
		updateTimeDisplay();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minutes, false);

		}
		return null;
	}
	
	private void updateTimeDisplay()
	{
		currentTime.setText(new StringBuilder()
		.append("Time Baby Fell Asleep: ")
		.append(pad(hour)).append(":")
		.append(minutes));
	}
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int selectedHour, int selectedMinutes) {
			hour = selectedHour;
			minutes = selectedMinutes;
			updateTimeDisplay();
		}
	};
	
	private String pad(int time)
	{
		if(time <10)
		{
			return "0" + String.valueOf(time);
		}
		else
		{
			return String.valueOf(time);
		}
	}
	
	private void createCalendarEvent()
	{
		String startDate = year+"-"+month+"-"+day;
		String startTime = hour+":"+minutes;
		Date date; 
		try {
			date = new SimpleDateFormat("yyyy-MM-dd-HH:mm").parse(startDate+"-"+startTime);
			long timeAndDate = date.getTime();
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Changed Diaper");
			intent.putExtra(Events.DESCRIPTION, "sumbitn");
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeAndDate);
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, timeAndDate);
			intent.putExtra(Events.HAS_ALARM, false);
			startActivity(intent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
