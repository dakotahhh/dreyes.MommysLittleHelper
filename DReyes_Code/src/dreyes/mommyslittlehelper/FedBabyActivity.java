package dreyes.mommyslittlehelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
//		Intent intent = new Intent(Intent.ACTION_EDIT);
//		intent.setType("vnd.android.cursor.item/event");
//		intent.putExtra("title", "Fed Baby");
//		intent.putExtra("description", "ate peas");
//		startActivity(intent);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
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
		EditText foodDescription = (EditText)findViewById(R.id.foodDescriptionSubmit);
		EditText timeSubmit = (EditText)findViewById(R.id.timeSubmit);
		String food = foodDescription.getText().toString();
		String time = timeSubmit.getText().toString();
		if(time.isEmpty())
		{
			Calendar dateTime = Calendar.getInstance();
			dateTime.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("KK:mm");
			time = sdf.format(dateTime.getTime());
		}
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("title", "Fed Baby");
		intent.putExtra("description", food);
		intent.putExtra("beginTime", time);
		startActivity(intent);
	}
}
