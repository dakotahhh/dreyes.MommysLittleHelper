

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;

public class PicasaAddPhotoActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
		{
			HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			InputStreamContent content = new InputStreamContent();
			ContentResolver contentResolver = getContentResolver();
			Uri uri = (Uri)extras.getParcelable(Intent.EXTRA_STREAM);
			content.inputStream = contentResolver.openInputStream(uri);
			Cursor cursor = contentResolver.query(uri, null, null, null, null);
			cursor.moveToFirst();
			content.type = intent.getType();
			content.length = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media.SIZE));
			HttpRequest request = requestFactory.buildPostRequest(new GenericUrl("https://picasaweb.google.com/data/feed/api/user/default/albumid/default"), content);
			GoogleHeaders headers = new GoogleHeaders();
			request.headers = headers;
			String fileName = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME));
			headers.setSlugFromFileName(fileName);
			request.execute().ignore();
 		}catch(IOException e)
 		{
 			e.printStackTrace();
 		}
	}
}
