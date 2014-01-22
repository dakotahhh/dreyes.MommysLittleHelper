package dreyes.mommyslittlehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

//imports for Picasa Web Album API
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.plus.PlusClient;
import com.google.gdata.client.*;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.photos.*;
import com.google.gdata.data.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.photos.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AddPhotoActivity extends Activity 
{
	PicasawebService myService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myService = new PicasawebService("exampleCo-exampleApp-1");
		try {
			myService.setUserCredentials("koteyec09@gmail.com", "bakashinji");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		String requestUrl = AuthSubUtil.getRequestUrl("http://www.example.com/RetrieveToken", "https://picasaweb.google.com/data", false, true);
		String sessionToken = AuthSubUtil.exchangeForSessionToken(onetimeUseToken, null);
		myService.setAuthSubToken(sessionToken,null);
		
		URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/username?kind=album");
		UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);
		boolean exists = false;
		for(AlbumEntry myAlbum :myUserFeed.getAlbumEntries())
		{
			if(myAlbum.getTitle().getPlainText().equals("MLH_Album"))
			{
				exists = true;
			}
		}
		if(exists)
		{
			createNewAlbum();
		}
		else
		{
			uploadPhoto();
		}
	}
	
	public void createNewAlbum()
	{
		AlbumEntry mlhAlbum = new AlbumEntry();
		mlhAlbum.setTitle(new PlainTextConstruct("MLH Album"));
		mlhAlbum.setDescription(new PlainTextConstruct("my baby"));
		AlbumEntry insertedEntry = myService.insert(postUrl, mlhAlbum);
	}
	
	public void uploadPhoto()
	{
		URL albumPostUrl = null;
		try {
			albumPostUrl = new URL("https://picasaweb.google.com/data/feed/api/user/koteyec09/albumid/MLH_Album");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PhotoEntry myPhoto = new PhotoEntry();
		myPhoto.setTitle(new PlainTextConstruct("look at my beautiful baby"));
		myPhoto.setDescription(new PlainTextConstruct("looka thim"));
		myPhoto.setClient("myClientName");
		
		MediaFileSource photo = new MediaFileSource(new File("aldf.jpg"), "image/jpeg");
		myPhoto.setMediaSource(photo);
		
		try {
			PhotoEntry returnedPhoto = myService.insert(albumPostUrl, myPhoto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
