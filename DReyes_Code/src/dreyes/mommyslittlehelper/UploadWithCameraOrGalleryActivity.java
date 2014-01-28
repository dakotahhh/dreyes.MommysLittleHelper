package dreyes.mommyslittlehelper;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UploadWithCameraOrGalleryActivity extends Activity implements OnClickListener{
	
	private Uri tempUri = null;
	Button takePhoto, chooseFromGallery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		takePhoto = (Button)findViewById(R.id.take_photo);
		takePhoto.setOnClickListener(this);
		chooseFromGallery = (Button)findViewById(R.id.choose_from_gallery);
		chooseFromGallery.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.take_photo)
		{
			Toast.makeText(this, "Take Photo", Toast.LENGTH_LONG).show();
		}
		else if(v.getId() == R.id.choose_from_gallery)
		{
			Toast.makeText(this, "Choose From Gallery", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	private void startCameraActivity()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		tempUri = getTempUri();
		if(tempUri != null)
		{
			intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
		}
		startActivityForResult(intent, getRequestCode());
	}
	
	private void startGalleryActivity()
	{
		tempUri = null;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		String selectPicture = getResources().getString(R.string.select_picture);
		startActivityForResult(Intent.createChooser(intent, selectPicture), getRequestCode());
	}
	
	private Uri getTempUri()
	{
		String imgFileName = "mommys_little_helper" + System.currentTimeMillis() + ".jpg"; 
		File image = new File
				(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imgFileName);
		return Uri.fromFile(image);
	}
	

}
