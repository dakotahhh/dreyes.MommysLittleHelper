package dreyes.mommyslittlehelper;


import java.util.Calendar;

import com.google.api.client.util.Sleeper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class SetDoctorsAppointmentActivity extends Activity {

	private TextView currentDate;
	private DatePicker datePicker;
	private Button changeDate;
	
	private int year, month, day;
	
	private final int DATE_DIALOG_ID = 000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setdoctorsappointment);
		
		currentDate = (TextView)findViewById(R.id.currentDate);
		datePicker = (DatePicker)findViewById(R.id.datePicker);
		changeDate = (Button)findViewById(R.id.changeDate);
		changeDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(), 0);
				
			}
		});
		
		setCurrentDateOnView();
		
		
	}
	
	private void setCurrentDateOnView()
	{
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		currentDate.setText(new StringBuilder()
				.append(month + 1)
				.append("-")
				.append(day)
				.append("-")
				.append(year)
				.append(" "));
		datePicker.init(year, month, day, null);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			new DatePickerDialog(this, datePickerListener, year, month, day);
			break;

		default:
			break;
		}
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
				int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			currentDate.setText(new StringBuilder()
				.append(month + 1)
				.append("-")
				.append(day)
				.append("-")
				.append(year)
				.append(" "));
			datePicker.init(year, month, day, null);
			
		}
	};
}
