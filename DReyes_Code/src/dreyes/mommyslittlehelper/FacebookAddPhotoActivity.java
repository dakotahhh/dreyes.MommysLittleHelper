package dreyes.mommyslittlehelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.facebook.*;
import com.facebook.internal.Utility;
import com.facebook.model.*;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FacebookAddPhotoActivity extends Fragment
{
	private static final String PENDING_ANNOUNCE_KEY = "pendingAnnouncement";
	private static final Uri M_FACEBOOK_URL	= Uri.parse("http://m.facebook.com");
	private static final String PERMISSION = "publish_actions";
	private static final int USER_GENERATED_MIN_SIZE = 480;
	private static final int REAUTH_ACTIVITY_CODE = 100;
	
	private ProgressDialog progressDialog;
	private boolean pendingAnnouncement;
	private Uri photoUri;
	private Button takePhoto, chooseFromGallery, postButton;
	private TextView userNameView;
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			
			onSessionStateChange(session, state, exception);
		}
	};
	
	private FacebookDialog.Callback nativeDialogCallback = new FacebookDialog.Callback() {
		
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
			new AlertDialog.Builder(getActivity())
				.setPositiveButton(R.string.error_dialog_button_text, null)
				.setTitle(R.string.error_dialog_title)
				.setMessage(error.getLocalizedMessage())
				.show();
			
		}
		
		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
			boolean resetSelections = true;
			if(FacebookDialog.getNativeDialogDidComplete(data))
			{
				if(FacebookDialog.COMPLETION_GESTURE_CANCEL.equals(FacebookDialog.getNativeDialogCompletionGesture(data)))
				{
					resetSelections = false;
					showCancelResponse();
				}
				else
				{
					showSuccessResponse(FacebookDialog.getNativeDialogPostId(data));
				}
			}
			if(resetSelections)
			{
				init(null);
			}
			
		}
	};
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    code to get the keyhash of this app
//	    try
//        {
//        	PackageInfo info = getPackageManager().getPackageInfo("com.facebook.samples.hellofacebook",  PackageManager.GET_SIGNATURES);
//        	for(Signature signature : info.signatures)
//        	{
//        		MessageDigest md = MessageDigest.getInstance("SHA");
//        		md.update(signature.toByteArray());
//        		Log.d("KAYHASH: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//        	}
//        }catch(NameNotFoundException e)
//        {
//        	
//        }catch (NoSuchAlgorithmException e) {
//			// TODO: handle exception
//		}
	    uiHelper = new UiLifecycleHelper(getActivity(), sessionCallback);
	    uiHelper.onCreate(savedInstanceState);
//	    setContentView(R.layout.activity_uploadwithcameraorgallery);
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
	  }
	  
	  
	  @Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}
	  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_uploadwithcameraorgallery, container,false);
	    userNameView = (TextView)view.findViewById(R.id.welcome);
		postButton = (Button)view.findViewById(R.id.postButton);
		postButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnnounce();
				
			}
		});
		
		init(savedInstanceState);
		
		return view;
	}  
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode >=0)
		{
			
		}
		else
		{
			uiHelper.onActivityResult(requestCode, resultCode, data, nativeDialogCallback);
		}
	}
	  
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	private void tokenUpdated()
	{
		if(pendingAnnouncement)
		{
			handleAnnouncement();
		}
	}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception)
	{
		if(session != null && session.isOpened())
		{
			if(state.equals(SessionState.OPENED_TOKEN_UPDATED))
			{
				tokenUpdated();
			}
			else
			{
				makeMeRequest(session);
			}
		}
		else
		{
			userNameView.setText("");
		}
	}
	
	private void makeMeRequest(final Session session)
	{
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if(session == Session.getActiveSession())
				{
					if(user != null)
					{
						userNameView.setText(user.getName());
					}
				}
				if(response.getError() != null)
				{
					handleError(response.getError());
				}
				
			}
		});
		request.executeAsync();
	}
	
	private void init(Bundle savedInstanceState)
	{
		postButton.setEnabled(false);
		
		if(savedInstanceState != null)
		{
			pendingAnnouncement = savedInstanceState.getBoolean(PENDING_ANNOUNCE_KEY, false);
			
		}
		Session session = Session.getActiveSession();
		if(session != null && session.isOpened())
		{
			makeMeRequest(session);
		}
	}
	
	private void handleAnnounce()
	{
		pendingAnnouncement = false;
		Session session = Session.getActiveSession();
		if(session != null && session.isOpened())
		{
			handleGraphApiAnnounce();
		}
		else
		{
			handleNativeShareAnnounce();
		}
	}
	
    private void handleGraphApiAnnounce() {
        Session session = Session.getActiveSession();

        List<String> permissions = session.getPermissions();
        if (!permissions.contains(PERMISSION)) {
            pendingAnnouncement = true;
            requestPublishPermissions(session);
            return;
        }

        // Show a progress dialog because sometimes the requests can take a while.
        progressDialog = ProgressDialog.show(getActivity(), "",
                getActivity().getResources().getString(R.string.progress_dialog_text), true);

        // Run this in a background thread so we can process the list of responses and extract errors.
        AsyncTask<Void, Void, List<Response>> task = new AsyncTask<Void, Void, List<Response>>() {

            @Override
            protected List<Response> doInBackground(Void... voids) {

                RequestBatch requestBatch = new RequestBatch();

                String photoStagingUri = null;

                if (photoUri != null) 
                {
                    try 
                    {
                        Pair<File, Integer> fileAndMinDimemsion = getImageFileAndMinDimension();
                        if (fileAndMinDimemsion != null) {
                            Request photoStagingRequest =
                                    Request.newUploadStagingResourceWithImageRequest(Session.getActiveSession(),
                                            fileAndMinDimemsion.first, null);
                            photoStagingRequest.setBatchEntryName("photoStaging");
                            requestBatch.add(photoStagingRequest);
                        }
                    } catch (FileNotFoundException e) 
                    {
                    	
                    }
                }

                return requestBatch.executeAndWait();
            }

            @Override
            protected void onPostExecute(List<Response> responses) {
            	Response finalResponse = null;
                for (Response response : responses) {
                    finalResponse = response;
                    if (response != null && response.getError() != null) 
                    {
                        break;
                    }
                }
                onPostActionResponse(finalResponse);
            }
        };

        task.execute();
    }
	
    private void handleNativeShareAnnounce()
    {
    	FacebookDialog.OpenGraphActionDialogBuilder builder = createDialogBuilder();
    	if(builder.canPresent())
    	{
    		uiHelper.trackPendingDialogCall(builder.build().present());
    	}
    	else
    	{
    		
    	}
    }
    
    private FacebookDialog.OpenGraphActionDialogBuilder createDialogBuilder()
    {
    	boolean userGenerated = false;
    	if(photoUri != null)
    	{
    		String photoUriString = photoUri.toString();
    		Pair<File, Integer> fileAndMinDimension = getImageFileAndMinDimension();
    		userGenerated = fileAndMinDimension.second >= USER_GENERATED_MIN_SIZE;
    		if(fileAndMinDimension != null && photoUri.getScheme().startsWith("content"))
    		{
    			
    		}
    	}
    }
    
    private Pair<File, Integer> getImageFileAndMiniDimension()
    {
    	File photoFile = null;
    	String photoUriString = photoUri.toString();
    	if(photoUriString.startsWith("file://"))
    	{
    		photoFile = new File(photoUri.getPath());
    	}
    	else if(photoUriString.startsWith("content://"))
    	{
    		String[] filePath = {MediaStore.Images.Media.DATA};
    		Cursor cursor = getActivity().getContentResolver().query(photoUri, filePath, null, null, null);
    		if(cursor != null)
    		{
    			cursor.moveToFirst();
    			int columnIndex = cursor.getColumnIndex(filePath[0]);
    			String filename = cursor.getString(columnIndex);
    			cursor.close();
    			
    			photoFile = new File(filename);
    		}
    	}
    	
    	if(photoFile != null)
    	{
    		InputStream is = null;
    		try
    		{
    			is = new FileInputStream(photoFile);
    			BitmapFactory.Options options = new BitmapFactory.Options();
    			options.inJustDecodeBounds = true;
    			BitmapFactory.decodeStream(is, null, options);
    			return new Pair<File, Integer>(photoFile, Math.min(options.outWidth, options.outHeight));
    			
    		}catch(Exception e)
    		{
    			return null;
    		}finally
    		{
    			Utility.closeQuietly(is);
    		}
    	}
    	return null;
    }
    
    private GraphObject getImageObject(String uri, boolean userGenerated)
    {
    	GraphObject imageObject = GraphObject.Factory.create();
    	imageObject.setProperty("url", uri);
    	if(userGenerated)
    	{
    		imageObject.setProperty("user_generated", "true");
    	}
    	return imageObject;
    }
    
    private void requestPublishPermissions(Session session)
    {
    	if(session != null)
    	{
    		Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSION)
    		.setDefaultAudience(SessionDefaultAudience.FRIENDS)
    		.setRequestCode(REAUTH_ACTIVITY_CODE);
    		session.requestNewPublishPermissions(newPermissionsRequest);
    		
    	}
    }
    
    private void onPostActionResponse(Response response)
    {
    	if(progressDialog != null)
    	{
    		progressDialog.dismiss();
    		progressDialog = null;
    	}
    	if(getActivity() == null)
    	{
    		return;
    	}
    	
    	PostResponse postResponse = response.getGraphObjectAs(PostResponse.class);
    	
    	if(postResponse != null && postResponse.getId() != null)
    	{
    		showSuccessResponse(postRespose.getId();
    		init(null);
    	}
    	else
    	{
    		handleError(response.getError());
    	}
    }
    
    private void showSuccessRespose(String postId)
    {
    	String dialogBody;
    	if(postId != null)
    	{
    		dialogBody = String.format(getString(R.string.result_dialog_text_with_id), postId);
    	}
    	else
    	{
    		dialogBody = getString(R.string.result_dialog_text_default);
    	}
    	showResultDialog(dialogBody);
    }
    
    private void showCancelResponse()
    {
    	showResultDialog(getString(R.string.result_dialog_text_cancelled));
    }
    
    private void showResultDialog(String dialogBody)
    {
    	new AlertDialog.Builder(getActivity())
    		.setPositiveButton(R.string.result_dialog_button_text, null)
    		.setTitle(R.string.result_dialog_title)
    		.setMessage(dialogBody)
    		.show();
    }
	  
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
//		
//		private void startCameraActivity()
//		{
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			tempUri = getTempUri();
//			if(tempUri != null)
//			{
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//			}
//			startActivityForResult(intent, -1);
//		}
//		
//		private void startGalleryActivity()
//		{
//			tempUri = null;
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			intent.setType("image/*");
//			String selectPicture = getResources().getString(R.string.select_picture);
//			startActivityForResult(Intent.createChooser(intent, selectPicture), -1);
//		}
//		
//		private Uri getTempUri()
//		{
//			String imgFileName = "mommys_little_helper" + System.currentTimeMillis() + ".jpg"; 
//			File image = new File
//					(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imgFileName);
//			return Uri.fromFile(image);
//		} 
//	  
//		private void handleAnnounce()
//		{
//			Session session = Session.getActiveSession();
//			if(session != null && session.isOpened())
//			{
//				handleGraphApiAnnounce();
//			}
//			else
//			{
//				handleNativeShareAnnounce();
//			}
//		}
//		
//		private void handleGraphApiAnnounce()
//		{
//			Session session = Session.getActiveSession();
//			List<String> permissions = session.getPermissions();
//			if(!permissions.contains("publish_actions"))
//			{
//				requestPublishPermission(session);
//				return;
//			}
//			
//			ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getActivity.getResources.getString(R.string.progress_dialog),true);
//		}
	  

}
