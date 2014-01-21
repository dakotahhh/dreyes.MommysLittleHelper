package dreyes.mommyslittlehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

//imports for Picasa Web Album API
import java.io.File;
import java.net.URL;

import com.google.android.gms.plus.PlusClient;
import com.google.gdata.client.*;
import com.google.gdata.client.photos.*;
import com.google.gdata.data.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.photos.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AddPhotoActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
		setLogging(settings.getBoolean("logging", false));
	
	}
//		super.onCreate(savedInstanceState);
//		PicasawebService myService = new PicasawebService("exampleCo-exampleApp-1");
//		//somehow get the account name do i really need this? probably not
//		URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/username?kind=album");
//		UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);
//		boolean exists = false;
//		for(AlbumEntry myAlbum: myUserFeed.getAlbumEntries())
//		{
//			if(myAlbum.getName().equals("MLH Album"))
//			{
//				exists = true;
//			}
//		}
//		if(exists)
//		{
//			createNewAlbum();
//		}
//	}
//	
//	public void createNewAlbum()
//	{
//		AlbumEntry mlhAlbum = new AlbumEntry();
//		mlhAlbum.setName("MLH Album");
//		mlhAlbum.setDescription("Baby pictures");
//		
//		
//	}
//	
//	public void uploadPhoto()
//	{
//		URL albumPostUrl = new URL("https://picasaweb.google.com/data/feed/api/user/username/albumid/albumid");
//		
//		PhotoEntry myPhoto = new PhotoEntry();
//		
//		myPhoto.setDescription("yay a baby");
//		myPhoto.setClient("myClientName");
//		
//		MediaFileSource photo = new MediaFileSource(new File("aldf.jpg"), "image/jpeg");
//		myPhoto.setMediaSource(photo)
//	}
}
