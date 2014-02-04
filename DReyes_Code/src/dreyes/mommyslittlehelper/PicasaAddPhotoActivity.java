package dreyes.mommyslittlehelper;



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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PicasaAddPhotoActivity extends Activity implements OnClickListener
{
	private final int REQUEST_CODE = Menu.FIRST+1;
	private Button upload;
	private ImageView preview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadwithpicasa);
		
		upload = (Button)findViewById(R.id.uploadGallery);
		upload.setOnClickListener(this);
		
		preview = (ImageView)findViewById(R.id.preview);
		
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
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
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
//			Uri selectedImage = data.getData();
			Toast.makeText(this, "image", Toast.LENGTH_LONG).show();
//			String[] filePath = {MediaStore.Images.Media.DATA};
			Cursor c = getContentResolver().query(data.getData(), null, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(ImageColumns.DATA);
			String picturePath = c.getString(columnIndex);
			Bitmap bitmapPreview = BitmapFactory.decodeFile(picturePath);
			BitmapDrawable bmpDrawable = new BitmapDrawable(bitmapPreview);
			preview.setBackgroundDrawable(bmpDrawable);
			
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

//	public List<AlbumEntry> getAlbums() throws IOException, ServiceException
//	{
//		return getAlbums("default");
//	}
//	
//	public List<AlbumEntry> getAlbums(String userId) throws IOException, ServiceException
//	{
//		String albumUrl = "https://picasaweb.google.com/data/feed/api/user/"+userId;
//		UserFeed userFeed = getFeed(albumUrl, UserFeed.class);
//		
//		List<GphotoEntry> entries = userFeed.getEntries();
//		List<AlbumEntry> albums = new ArrayList<AlbumEntry>();
//		for(GphotoEntry entry : entries)
//		{
//			GphotoEntry adapted = entry.getAdaptedEntry();
//			if(adapted instanceof AlbumEntry)
//			{
//				albums.add((AlbumEntry) adapted);
//			}
//		}
//		return albums;
//	}
//	
//	public AlbumEntry insertAlbum(AlbumEntry album) throws IOException, ServiceException
//	{
//		String feedUrl = "https://picasaweb.google.com/data/feed/api/user/default";
//		return service.insert(new URL(feedUrl), album);
//	}
//	
//	//Insert an entry into another entry. Because our entries are a heirarchy
//	//this lets you insert a photo into an album even if you only have the album entry and not the album feed, making it quicker to travers the heirarchy.
//	public <T extends GphotoEntry> T insert(GphotoEntry<?> parent, T entry) throws IOException, ServiceException
//	{
//		String feedUrl = getLinkByRel(parent.getLinks(), Link.Rel.FEED);
//		return service.insert(new URL(feedUrl), entry);
//	}
//	
//	public <T extends GphotoFeed> T getFeed(String feedHref, Class<T> feedClass) throws IOException, ServiceException
//	{
//		System.out.println("Get feed url: " + feedHref);
//		return service.getFeed(new URL(feedHref), feedClass);
//	}
//	
//	public String getLinkByRel(List<Link> links, String relValue)
//	{
//		for(Link link: links)
//		{
//			if(relValue.equals(link.getRel()))
//			{
//				return link.getHref();
//			}
//		}
//		throw new IllegalArgumentException("Missing " + relValue + " link.");
//	}
//	
//	public List<PhotoEntry> getPhotos(AlbumEntry album) throws IOException, ServiceException
//	{
//		String feedHref = getLinkByRel(album.getLinks(), Link.Rel.FEED);
//		AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);
//		List<GphotoEntry> entries = albumFeed.getEntries();
//		List<PhotoEntry> photos = new ArrayList<PhotoEntry>();
//		for(GphotoEntry entry : entries)
//		{
//			GphotoEntry adapted = entry.getAdaptedEntry();
//			if(adapted instanceof PhotoEntry)
//			{
//				photos.add((PhotoEntry)adapted);
//			}
//		}
//		return photos;
//	}