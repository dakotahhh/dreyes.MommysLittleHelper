package dreyes.mommyslittlehelper;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.util.Sleeper;
import com.google.gdata.data.extensions.Reminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetDoctorsAppointmentActivity extends Activity {

	private TextView currentDate, currentTime;
	private Button changeDate,changeTime,createAppointment;
	private CheckBox setReminder;
	private boolean setReminderChecked = false;
	private int year, month, day, hour, minutes;

	private final int DATE_DIALOG_ID = 000;
	private final int TIME_DIALOG_ID = 111;
	private final int APPOINTMENT = 333;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setdoctorsappointment);

		currentDate = (TextView)findViewById(R.id.currentDate);
		changeDate = (Button)findViewById(R.id.changeDate);
		changeDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		currentTime = (TextView)findViewById(R.id.currentTime);
		changeTime = (Button)findViewById(R.id.changeTime);
		changeTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(TIME_DIALOG_ID);
			}
		});

		createAppointment = (Button)findViewById(R.id.createAppointment);
		createAppointment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				createCalendarAppointment();

			}
		});

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minutes = c.get(Calendar.MINUTE);

		updateDateDisplay();
		updateTimeDisplay();


	}

	private void updateDateDisplay()
	{
		currentDate.setText(new StringBuilder()
				.append("Appointment Day: ")
				.append(month + 1).append("-")
				.append(day).append("-")
				.append(year));
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
				int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			updateDateDisplay();


		}
	};


	@Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minutes, false);
		}
		return null;
	}

	private void updateTimeDisplay()
	{
		currentTime.setText(new StringBuilder()
		.append("Appointment Time: ")
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

	private void createCalendarAppointment()
	{
		String startDate = year+"-"+month+"-"+day;
		String startTime = hour+":"+minutes;
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd-HH:mm").parse(startDate+"-"+startTime);
			long timeAndDate = date.getTime();
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "Doctors Appointment");
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeAndDate);
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, timeAndDate);
			intent.putExtra(Reminders.MINUTES, 60);
			intent.putExtra(Events.HAS_ALARM, 1);
			startActivity(intent);
			Toast.makeText(this, "A reminder has been set for an hour before your appointment.", Toast.LENGTH_LONG).show();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}