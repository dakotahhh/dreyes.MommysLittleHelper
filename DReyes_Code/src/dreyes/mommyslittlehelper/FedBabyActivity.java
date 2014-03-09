package dreyes.mommyslittlehelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import dreyes.mommyslittlehelper.R;
import dreyes.mommyslittlehelper.R.id;
import dreyes.mommyslittlehelper.R.layout;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FedBabyActivity extends Activity implements OnClickListener{

	private Button submit, timeSubmit, feedingAnalytics;
	private TextView timeTitle;
	private final int TIME_DIALOG_ID = 000;
	private int hour, minutes, year, day, month;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fedbaby);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		timeSubmit = (Button)findViewById(R.id.timeSubmit);
		timeSubmit.setOnClickListener(this);
		timeTitle = (TextView)findViewById(R.id.timeTitle);
//		feedingAnalytics = (Button)findViewById(R.id.feedingAnalytics);
		feedingAnalytics.setOnClickListener(this);
		
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minutes = c.get(Calendar.MINUTE);
		year = c.get(Calendar.YEAR);
		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		
		updateTimeDisplay();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.timeSubmit)
		{
			showDialog(TIME_DIALOG_ID);
		}
		else if(v.getId() == R.id.submit)
		{
			createCalendarEvent();
		}
//		else if(v.getId() == R.id.feedingAnalytics)
//		{
//			Intent intent = new Intent(this, FedBabyGraphViewActivity.class);
//			startActivity(intent);
//		}
	}
	
	private void createCalendarEvent()
	{
		String startDate = year+"-"+month+"-"+day;
		String startTime = hour+":"+minutes;
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-d-HH:mm").parse(startDate+"-"+startTime);
			long timeAndDate = date.getTime();
			EditText foodDescription = (EditText)findViewById(R.id.foodDescriptionSubmit);
			String food = foodDescription.getText().toString();
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Fed Baby");
			intent.putExtra(Events.DESCRIPTION, food);
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeAndDate);
			intent.putExtra(Events.HAS_ALARM, false);
			startActivity(intent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minutes, false);
		}
		return null;
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
		.append("Time Fed: ")
		.append(pad(hour)).append(":")
		.append(minutes));
	}
	
	private String pad(int time)
	{
		if(time <10)
		{
			return "0" + String.valueOf(time);
		}
		else
		{
			return String.valueOf(time-12);
		}
	}
	
	
	
}
