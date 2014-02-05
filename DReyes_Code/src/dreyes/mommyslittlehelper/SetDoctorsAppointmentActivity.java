package dreyes.mommyslittlehelper;


import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class SetDoctorsAppointmentActivity extends Activity {

	private TextView currentDate;
	private DatePicker datePicker;
	
	private int year, month, day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setdoctorsappointment);
		
		currentDate = (TextView)findViewById(R.id.currentDate);
		datePicker = (DatePicker)findViewById(R.id.datePicker);
		
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
	
}
