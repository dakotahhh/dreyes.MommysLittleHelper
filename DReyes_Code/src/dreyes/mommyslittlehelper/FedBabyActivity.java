package dreyes.mommyslittlehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class FedBabyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fedbaby);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
//		Intent intent = new Intent(Intent.ACTION_EDIT);
//		intent.setType("vnd.android.cursor.item/event");
//		intent.putExtra("title", "Fed Baby");
//		intent.putExtra("description", "ate peas");
//		intent.putExtra("beginTime", "4:03");
//		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
