package dreyes.mommyslittlehelper;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WakingBabyActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Button waking = (Button)findViewById(R.id.babyWaking);
		waking.setOnClickListener(this);
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
		GregorianCalendar time = new GregorianCalendar();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Woke Up");
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
		intent.putExtra(Events.HAS_ALARM, false);
		startActivity(intent);
	}
	
}
