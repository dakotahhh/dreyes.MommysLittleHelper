package dreyes.mommyslittlehelper;



import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Sleeper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PicasaAddPhotoActivity extends Activity implements OnClickListener
{
	private final int REQUEST_CODE = 5;
	private Button upload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadwithpicasa);
		
		upload = (Button)findViewById(R.id.uploadGallery);
		upload.setOnClickListener(this);
		
		
//		try
//		{
//			HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
//			Intent intent = getIntent();
//			Bundle extras = intent.getExtras();
//			InputStreamContent content = new InputStreamContent();
//			ContentResolver contentResolver = getContentResolver();
//			Uri uri = (Uri)extras.getParcelable(Intent.EXTRA_STREAM);
//			content.inputStream = contentResolver.openInputStream(uri);
//			Cursor cursor = contentResolver.query(uri, null, null, null, null);
//			cursor.moveToFirst();
//			content.type = intent.getType();
//			content.length = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
//			HttpRequest request = requestFactory.buildPostRequest(new GenericUrl("https://picasaweb.google.com/data/feed/api/user/default/albumid/default"), content);
//			GoogleHeaders headers = new GoogleHeaders();
//			request.headers = headers;
//			String fileName = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
//			headers.setSlugFromFileName(fileName);
//			request.execute().ignore();
// 		}catch(IOException e)
// 		{
// 			e.printStackTrace();
// 		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.uploadGallery)
		{
			startGalleryActivity();
		}
		
	}
	
	private void startGalleryActivity()
	{
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_CODE);
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		Toast.makeText(this, "GETTING CONTENT", Toast.LENGTH_LONG).show();
//		startActivityForResult(intent, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE && requestCode == RESULT_OK)
		{
			Uri selectedImage = data.getData();
			Toast.makeText(this, "image", Toast.LENGTH_LONG).show();
			String[] filePath = {MediaStore.Images.Media.DATA};
			Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePath[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
//			Intent temp = new Intent(Intent.ACTION_SEND);
//			temp.setType("image/png");
//			temp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//			temp.putExtra(Intent.EXTRA_STREAM, selectedImage);
//			temp.setComponent(new ComponentName(
//					"com.google.android.apps.uploader",
//					"com.google.android.uploader.clients.picasa.PicasaSettingsActivity"));
//			try
//			{
//				startActivity(temp);
//				Toast.makeText(this, "TEMP ACTIVITY STARTING", Toast.LENGTH_LONG).show();
//			}catch(ActivityNotFoundException e)
//			{
//				Toast.makeText(this, "PICASA FAILED", Toast.LENGTH_LONG).show();
//			}
		}
	}

}
