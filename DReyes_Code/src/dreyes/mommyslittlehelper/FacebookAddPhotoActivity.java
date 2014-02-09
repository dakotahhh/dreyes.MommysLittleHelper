package dreyes.mommyslittlehelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.internal.cu;
import com.google.api.client.util.Sleeper;

import android.app.AlertDialog;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FacebookAddPhotoActivity extends FragmentActivity
{
	private final String PERMISSION = "publish_actions";
	private final String PENDING_ACTION_BUNDLE_KEY = "";
	
	private Button postPhotoButton, startGalleryButton, takePhotoButton;
	private LoginButton loginButton;
	private ProfilePictureView profilePictureView;
	private TextView greeting;
	private PendingAction pendingAction = PendingAction.NONE;
	private ViewGroup controlsContainer;
	private GraphUser user;
	private boolean canPresentShareDialog;
	private Bitmap yourSelectedImage;
	
	private final int REQUEST_IMAGE_CAPTURE = 000;
	private final int REQUEST_OPEN_GALLERY = 111;
	
	private enum PendingAction
	{
		NONE,
		POST_PHOTO,
		START_GALLERY,
		START_CAMERA
	}
	
	private UiLifecycleHelper uiHelper;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
			
		}
	};
	
	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		
		@Override
		public void onError(PendingCall pendingCall, Exception error, Bundle data) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(PendingCall pendingCall, Bundle data) {
			Log.d("HelloFacebook", "Success!");
			
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		try
		{
			PackageInfo info = getPackageManager().getPackageInfo("dreyes.mommyslittlehelper", PackageManager.GET_SIGNATURES);
			for(Signature signature : info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				
			}
			
		}catch(NameNotFoundException e)
		{
			
		}catch(NoSuchAlgorithmException e)
		{
			
		}
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		if(savedInstanceState != null)
		{
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}
		
		setContentView(R.layout.activity_uploadwithcameraorgallery);
		
		loginButton = (LoginButton)findViewById(R.id.login_button);
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			
			@Override
			public void onUserInfoFetched(GraphUser user) {
				FacebookAddPhotoActivity.this.user = user;
				updateUI();
				handlePendingAction();
			}
		});
		
		profilePictureView = (ProfilePictureView)findViewById(R.id.profilePicture);
		greeting = (TextView)findViewById(R.id.greeting);
		
		postPhotoButton = (Button)findViewById(R.id.postPhotoButton);
		postPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickPostPhoto();
				
			}
		});
		
		startGalleryButton = (Button)findViewById(R.id.startGallery);
		startGalleryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startGallery();
				
			}
		});
		
		takePhotoButton = (Button)findViewById(R.id.takePhoto);
		takePhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		controlsContainer = (ViewGroup)findViewById(R.id.main_ui_container);
		
		final FragmentManager fm = getSupportFragmentManager();
		
		fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				if(fm.getBackStackEntryCount() == 0)
				{
					controlsContainer.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		canPresentShareDialog = FacebookDialog.canPresentShareDialog(this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);
		updateUI();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
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
				String filePath = cursor.getString(columnIndex);
				cursor.close();
				yourSelectedImage = BitmapFactory.decodeFile(filePath);
				postPhoto();
			}
			break;
		case REQUEST_IMAGE_CAPTURE:
			if(resultCode == RESULT_OK)
			{
				Bundle extras = data.getExtras();
				yourSelectedImage = (Bitmap)extras.get("data");
				postPhoto();
			}
			break;
		default:
			break;
		}
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		if(pendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException))
		{
			new AlertDialog.Builder(FacebookAddPhotoActivity.this)
				.setTitle(R.string.cancelled)
				.setMessage(R.string.permission_not_granted)
				.setPositiveButton(R.string.ok, null)
				.show();
			pendingAction = PendingAction.NONE;
		}
		else if(state == SessionState.OPENED_TOKEN_UPDATED)
		{
			handlePendingAction();
		}
		updateUI();
	}
	
	private void updateUI()
	{
		Session session = Session.getActiveSession();
		boolean enableButton = (session != null && session.isOpened());
		postPhotoButton.setEnabled(enableButton);
		
		if(enableButton && user!= null)
		{
			profilePictureView.setProfileId(user.getId());
			greeting.setText(getString(R.string.hello_user, user.getFirstName()));
		}
		else
		{
			profilePictureView.setProfileId(null);
			greeting.setText(null);
		}
	}
	
	private void handlePendingAction()
	{
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;
		switch(previouslyPendingAction)
		{
		case POST_PHOTO:
			postPhoto();
			break;
		case START_GALLERY:
			startGallery();
			break;
		case START_CAMERA:
			break;
		}
	}
	
	private void startGallery()
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_OPEN_GALLERY);
		
	}
	
	private void startCamera()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(intent.resolveActivity(getPackageManager())!=null)
		{
			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
		}
	}
	
	private interface GraphObjectWithId extends GraphObject
	{
		String getId();
	}
	
	private void showPublishResult(String message, GraphObject result, FacebookRequestError error)
	{
		String title = null;
		String alertMessage = null;
		if(error == null)
		{
			title = getString(R.string.success);
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = getString(R.string.successfully_posted_post, message, id);
		}
		else
		{
			title = getString(R.string.error);
			alertMessage = error.getErrorMessage();
		}
		
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(alertMessage)
			.setPositiveButton(R.string.ok, null)
			.show();
	}
	
	private void onClickPostPhoto()
	{
		performPublish(PendingAction.POST_PHOTO, false);
	}
	
	private void postPhoto()
	{
		if(hasPublishPermission())
		{
//			Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.feels);
			Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), yourSelectedImage, new Request.Callback() {
				
				@Override
				public void onCompleted(Response response) {
					showPublishResult(getString(R.string.photo_post), response.getGraphObject(), response.getError());
					
				}
			});
			request.executeAsync();
		}
		else
		{
			pendingAction = pendingAction.POST_PHOTO;
		}
	}
	
	private boolean hasPublishPermission()
	{
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().contains("publish_actions");
	}
	
	private void performPublish(PendingAction action, boolean allowNoSession)
	{
		Session session = Session.getActiveSession();
		if(session != null)
		{
			pendingAction = action;
			if(hasPublishPermission())
			{
				handlePendingAction();
				return;
			}
			else if(session.isOpened())
			{
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSION));
				return;
			}
		}
		if(allowNoSession)
		{
			pendingAction = action;
			handlePendingAction();
		}
	}
}






























//package dreyes.mommyslittlehelper;
//
//import java.io.ByteArrayOutputStream;
//import java.io.Closeable;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//import android.R.integer;
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.BitmapFactory.Options;
//import android.graphics.drawable.BitmapDrawable;
//
//import com.facebook.*;
//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.Facebook;
//import com.facebook.model.GraphUser;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Parcelable;
//import android.provider.MediaStore;
//import android.provider.MediaStore.Images.ImageColumns;
//import android.provider.MediaStore.MediaColumns;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewDebug.IntToString;
//import android.widget.*;
//
//public class FacebookAddPhotoActivity extends Activity implements OnClickListener{
//
//	private Uri tempUri;
//	private Button takePhoto, chooseFromGallery;
//	private ImageView image;
//	private final int REQUEST_GALLERY = Menu.FIRST + 2;	
//	private final int REQUEST_IMAGE_CAPTURE = 1;
//	private String mCurrentPhotoPath, filemanagerString, selectedImagePath;
//	
//	private Facebook facebook;
//	
//	  @Override
//	  public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.activity_uploadwithcameraorgallery);
//	    
//	    takePhoto = (Button)findViewById(R.id.take_photo);
//	    takePhoto.setOnClickListener(this);
//	    chooseFromGallery = (Button)findViewById(R.id.choose_from_gallery);
//	    chooseFromGallery.setOnClickListener(this);
//	    image = (ImageView)findViewById(R.id.image);
//	    
//	    
//	    // start Facebook Login
//	    Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//	      // callback when session changes state
//	      @Override
//	      public void call(Session session, SessionState state, Exception exception) {
//	        if (session.isOpened()) {
//
//	          // make request to the /me API
//	          Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//	            // callback after Graph API response with user object
//	            @Override
//	            public void onCompleted(GraphUser user, Response response) {
//	              if (user != null) {
//	                TextView welcome = (TextView) findViewById(R.id.welcome);
//	                welcome.setText("Hello " + user.getName() + "!");
//	              }
//	            }
//	          }).executeAsync();
//	        }
//	      }
//	    });
//	  }
//	  
//		@Override
//		public void onClick(View v) {
//			if(v.getId() == R.id.take_photo)
//			{
//				Toast.makeText(this, "Take Photo", Toast.LENGTH_LONG).show();
//				startCameraActivity();
//			}
//			else if(v.getId() == R.id.choose_from_gallery)
//			{
//				Toast.makeText(this, "Choose From Gallery", Toast.LENGTH_LONG).show();
//				startGalleryActivity();
//			}
//			
//		}
//		
//		private void performPulish(PendingAction action, boolean allowNoSession)
//		{
//			Session session = Session.getActiveSession();
//			if(session != null)
//			{
//				
//			}
//		}
//		
//		private void startCameraActivity()
//		{
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			File photoFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//			
//			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//		}
//		
//		private void startGalleryActivity()
//		{
//			Intent intent = new Intent(Intent.ACTION_PICK);
//			intent.setType("image/*");
//			intent.setAction(Intent.ACTION_GET_CONTENT);
//			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//			startActivityForResult(intent, REQUEST_GALLERY);
//		}
//		
//		@Override
//		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//			super.onActivityResult(requestCode, resultCode, data);
//			if(requestCode == REQUEST_GALLERY && requestCode == RESULT_OK)
//			{
//				Uri selectedImage = data.getData();
//				filemanagerString = selectedImage.getPath();
//				String[] projection = {MediaStore.Images.Media.DATA};
//				Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
//				if(cursor!=null)
//				{
//					cursor.moveToFirst();
//					int column_index = cursor
//							.getColumnIndex(projection[0]);
//					String filePath = cursor.getString(column_index);
//					cursor.close();
//					Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//				}
//				
//
//			}
//			else if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK)
//			{
//				File photo = new File(Environment.getExternalStorageDirectory().toString());
//				for(File temp: photo.listFiles())
//				{
//					if(temp.getName().equals("temp.jpg"))
//					{
//						photo = temp;
//						break;
//					}
//				}
//				BitmapFactory.Options bo = new BitmapFactory.Options();
//				Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), bo);
//				String path = Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
//				photo.delete();
//				OutputStream out = null;
//				File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//				try
//				{
//					out = new FileOutputStream(file);
//					bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
//					out.flush();
//					out.close();
//				}catch(FileNotFoundException e)
//				{
//					e.printStackTrace();
//				}catch(IOException e)
//				{
//					e.printStackTrace();
//				}
////				Bundle extras = data.getExtras();
////				Bitmap imageBitmap = (Bitmap)extras.get("data");
////				image.setImageBitmap(imageBitmap);
//			}
//		}
//		
//		
//		private void uploadImagePath(String filePath)
//		{
//			FileInputStream fileStream = null;
//			try
//			{
//				fileStream = new FileInputStream(new File(filePath));
//				byte[] bytes = convertStreamToBytes(fileStream);
//				uploadImageBytes(bytes);
//			}catch(Exception e)
//			{
//				Toast toast = Toast.makeText(this, "unable to retrieve image", Toast.LENGTH_LONG);
//				toast.show();
//			}finally
//			{
//				closeStream(fileStream);
//			}
//		}
//		
//		private void uploadImageBytes(byte[] bytes)
//		{
//			Bundle params = new Bundle();
//			params.putString(Facebook.TOKEN, facebook.getAccessToken());
//			params.putString("method", "photos.upload");
//			params.putByteArray("picture", bytes);
//			AsyncFacebookRunner ar = new AsyncFacebookRunner(facebook);
////			ar.request(null, params, "POST", new WallPostListener());
//		}
//		
//		public static byte[] convertStreamToBytes(InputStream stream) throws IOException
//		{
//			if(stream == null)
//			{
//				return null;
//			}
//			ByteArrayOutputStream output = new ByteArrayOutputStream();
//			copyStream(stream, output);
//			return output.toByteArray();
//		}
//		
//		public static void copyStream(InputStream from, OutputStream to) throws IOException
//		{
//			byte data[] = new byte[8192];
//			int count;
//			while((count = from.read(data)) != -1)
//			{
//				to.write(data,0,count);
//			}
//			from.close();
//		}
//		
//		public static void closeStream(Closeable stream)
//		{
//			try
//			{
//				if(stream != null)
//				{
//					stream.close();
//				}
//			}catch(Exception e)
//			{
//				
//			}
//		}
//		
//		private File createImageFile() throws IOException
//		{
//			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//			String imageFileName = "JPEG_" + timeStamp + "_";
//			File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//			File image = File.createTempFile(imageFileName, ".jpg", storageDir);
//			mCurrentPhotoPath = "file:" +image.getAbsolutePath();
//			return image;
//		}
		


	  

//}
