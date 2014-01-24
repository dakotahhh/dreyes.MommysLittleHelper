package dreyes.mommyslittlehelper;

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

public class BreastPumpActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breastpump);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Button submit = (Button) findViewById(R.id.breast_submit);
		submit.setOnClickListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
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
		EditText rightBreast = (EditText)findViewById(R.id.right_breast);
		EditText leftBreast = (EditText)findViewById(R.id.left_breast);
		String right = rightBreast.getText().toString();
		String left = leftBreast.getText().toString();
		GregorianCalendar time = new GregorianCalendar();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Breast Pump");
		intent.putExtra(Events.DESCRIPTION, "RB" + right + "LB" + left);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
		intent.putExtra(Events.HAS_ALARM, false);
		startActivity(intent);
	}

}
