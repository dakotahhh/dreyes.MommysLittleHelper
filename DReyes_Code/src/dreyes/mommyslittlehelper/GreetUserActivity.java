package dreyes.mommyslittlehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class GreetUserActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_greetuser);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void fedBabyActivity(View view)
	{
		Intent intent = new Intent(this, FedBabyActivity.class);
		startActivity(intent);
	}
//	
//	public void sleepingBabyActivity(View view)
//	{
//		Intent intent = new Intent(this, SleepingBabyActivity.class);
//		startActivity(intent);
//	}
//	
//	public void wakingBabyActivity(View view)
//	{
//		Intent intent = new Intent(this, WakingBabyActivity.class);
//		startActivity(intent);
//	}
//	
//	public void diaperChangeActivity(View view)
//	{
//		Intent intent = new Intent(this, DiaperChangeActivity.class);
//		startActivity(intent);
//	}

	
	
}
