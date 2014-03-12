package dreyes.mommyslittlehelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dreyes.mommyslittlehelper.R;
import dreyes.mommyslittlehelper.R.id;
import dreyes.mommyslittlehelper.R.layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class BreastPumpActivity extends Activity implements OnClickListener{

	private Button submit, timeSubmit, leftBreastButton;
	private EditText leftBreast, rightBreast;
	private TextView currentTime;
	private final int TIME_DIALOG_ID = 000;
	private int hour, minutes, year, day, month;
	private String right, left;
	
	private ArrayList<Integer> leftBreastList = new ArrayList<Integer>();
	private ArrayList<Integer> rightBreastList = new ArrayList<Integer>();
	private ArrayList<String> breastPumpList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breastpump);
		submit = (Button) findViewById(R.id.breast_submit);
		submit.setOnClickListener(this);
		timeSubmit = (Button)findViewById(R.id.timeChangeSubmit);
		timeSubmit.setOnClickListener(this);
		currentTime = (TextView)findViewById(R.id.currentTime);
		leftBreastButton = (Button)findViewById(R.id.leftBreastButton);
		leftBreastButton.setOnClickListener(this);
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
		if(v.getId() == R.id.breast_submit)
		{
			String startDate = year+"-"+month+"-"+day;
			String startTime = hour+":"+minutes;
			Date date;
			try
			{
				date = new SimpleDateFormat("yyyy-MM-d-HH:mm").parse(startDate+"-"+startTime);
				long timeAndDate = date.getTime();
				rightBreast = (EditText)findViewById(R.id.right_breast);
				leftBreast = (EditText)findViewById(R.id.left_breast);
				right = rightBreast.getText().toString();
				left = leftBreast.getText().toString();
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra(Events.TITLE, "Breast Pump");
				intent.putExtra(Events.DESCRIPTION, "RB" + right + "LB" + left);
				intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeAndDate);
				intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, timeAndDate);
				intent.putExtra(Events.HAS_ALARM, false);
				leftBreastList.add(Integer.parseInt(left));
				rightBreastList.add(Integer.parseInt(right));
				breastPumpList.add(left+" "+right);
				startActivity(intent);
			}catch(ParseException e)
			{
				e.printStackTrace();
			}
		}
		else if(v.getId() == R.id.timeChangeSubmit)
		{
			showDialog(TIME_DIALOG_ID);
		}
		else if(v.getId() == R.id.leftBreastButton)
		{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			for(int i = 0; i < breastPumpList.size(); i++)
			{
				editor.putString("item_"+i, breastPumpList.get(i));
			}
			editor.putInt("item_size", breastPumpList.size());
			editor.commit();
			Intent intent = new Intent(this, LeftBreastGraphViewActivity.class);
			startActivity(intent);
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
		currentTime.setText(new StringBuilder()
		.append("Time Pumped: ")
		.append(padhours(hour)).append(":")
		.append(padMinutes(minutes)));
	}
	
	private String padhours(int time)
	{
		String timeString = null;
		if(time <10)
		{
			timeString = "0" + String.valueOf(time);
		}
		else if(time > 12)
		{
			timeString =  String.valueOf(time-12);
		}
		return timeString;
	}
	
	private String padMinutes(int time)
	{
		if(time < 10)
		{
			return "0" + String.valueOf(time);
		}
		else 
		{
			return String.valueOf(time);
		}
	}

}
