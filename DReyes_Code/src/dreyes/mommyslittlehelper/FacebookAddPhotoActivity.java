package dreyes.mommyslittlehelper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
	private String currentPhotoPath;
	private ProgressBar progressBar;
	private int progressStatus = 0;
	private Handler hander = new Handler();
	private File directory, file;
	
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
			Log.d("FACEBOOKADDPHOTOACTIVITY", "Success!");
			
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		PackageInfo info;
//		try {
//			info = getPackageManager().getPackageInfo("dreyes.mommyslittlehelper", PackageManager.GET_SIGNATURES);
//			for(Signature signature : info.signatures)
//			{
//				MessageDigest md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				Log.d("KEYHASH LOOK HERE: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//			}
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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
				startCamera();
				
			}
		});
		
//		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		
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
//			new Thread(new Runnable() {
//							
//							@Override
//							public void run() {
//								while(progressStatus < 100)
//								{
//									progressStatus = postPhoto();
//									hander.post(new Runnable() {
//										
//										@Override
//										public void run() {
//											progressBar.setProgress(progressStatus);
//											
//										}
//									});
//								}
//								
//							}
//						});
			break;
		case START_GALLERY:
			startGallery();
			break;
		case START_CAMERA:
			startCamera();
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
			File photoFile = null;
			try
			{
				photoFile = createImageFile();
				galleryAddPic();
			}catch(IOException e)
			{
				
			}
			if(photoFile!=null)
			{
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}
	
	private void createDirectoryForPictures()
	{
		directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MommysLittleHelper");
		if(!directory.exists())
		{
			directory.mkdirs();
		}
	}
	
	private File createImageFile() throws IOException
	{
		String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		String imageFileName = "JPEG_"+timeStamp+"_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg",storageDir);
		currentPhotoPath = "file:"+image.getAbsolutePath();
		return image;
	}
	
	private void galleryAddPic()
	{
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(currentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
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
//		tellUserPhotoIsBeingUploaded();
		new AlertDialog.Builder(this)
			.setTitle("Processing")
			.setMessage("Your post is being uploaded to Facebook.")
			.setPositiveButton(R.string.ok, null)
			.show();
		if(hasPublishPermission())
		{
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
	
	private void tellUserPhotoIsBeingUploaded()
	{
		Toast.makeText(this, "Your post is being uploaded to Facebook", Toast.LENGTH_LONG).show();
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
