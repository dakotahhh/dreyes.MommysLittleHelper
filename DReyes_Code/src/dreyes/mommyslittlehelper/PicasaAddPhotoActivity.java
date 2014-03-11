package dreyes.mommyslittlehelper;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Sleeper;
import com.google.gdata.client.*;
import com.google.gdata.client.photos.*;
import com.google.gdata.data.*;
import com.google.gdata.data.extensions.Comments;
import com.google.gdata.data.media.*;
import com.google.gdata.data.photos.*;
import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Contacts.Intents.Insert;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PicasaAddPhotoActivity extends Activity
{
	private final int REQUEST_OPEN_GALLERY = 111;
	private Button upload;
	private Bitmap yourSelectedImage;
	private String filePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadwithpicasa);
		
		upload = (Button)findViewById(R.id.uploadGallery);
		upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGallery();
				
			}
		});
	}
	
	private void startGallery()
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_OPEN_GALLERY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_OPEN_GALLERY:
			if(resultCode == RESULT_OK)
			{
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				filePath = cursor.getString(columnIndex);
				cursor.close();
				yourSelectedImage = BitmapFactory.decodeFile(filePath);
				postPhoto();
			}
			break;
		}
	}
		
		private void postPhoto()
		{
			File tmpFile = new File(filePath);
			String photoUri = null;
			try
			{
				photoUri = MediaStore.Images.Media.insertImage(getContentResolver(), tmpFile.getAbsolutePath(), null, null);
				Intent intent = ShareCompat.IntentBuilder.from(this)
						.setText("My baby")
						.setType("image/jpeg")
						.setStream(Uri.parse(photoUri))
						.getIntent()
						.setPackage("com.google.adroid.apps.plus");
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		
		}

}