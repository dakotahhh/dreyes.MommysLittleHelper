package dreyes.mommyslittlehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ChooseUploadTypeActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseuploadtype);
		Button picasaUpload = (Button)findViewById(R.id.uploadusinggooglephoto);
		picasaUpload.setOnClickListener(this);
		Button facebookUpload = (Button)findViewById(R.id.uploadusingfacebook);
		facebookUpload.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		if(v.getId() == R.id.uploadusinggooglephoto)
		{
			intent = new Intent(this, PicasaAddPhotoActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.uploadusingfacebook)
		{
			intent = new Intent(this, FacebookAddPhotoActivity.class);
			startActivity(intent);
		}
		
	}

}
