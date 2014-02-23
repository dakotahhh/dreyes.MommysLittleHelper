package dreyes.mommyslittlehelper;


import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.internal.u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class FacebookFragment extends Fragment {

	private static final String TAG = "MAINFRAGMENT";
	private UiLifecycleHelper uiHelper;
	GraphUser user;
	private TextView greeting;
	private ProfilePictureView profilePictureView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if(session != null && (session.isOpened() ||session.isClosed()))
		{
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_uploadwithcameraorgallery, container, false);
		LoginButton authButton = (LoginButton)view.findViewById(R.id.login_button);
		
		profilePictureView = (ProfilePictureView)view.findViewById(R.id.profilePicture);
		greeting = (TextView)view.findViewById(R.id.greeting);
		
		authButton.setReadPermissions(Arrays.asList("basic_info"));
		authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			
			@Override
			public void onUserInfoFetched(GraphUser user) {
				// TODO Auto-generated method stub
				FacebookFragment.this.user = user;
				updateUI();
			}
		});
		authButton.setFragment(this);
		return view;
	}
	
	private void updateUI()
	{
		Session session = Session.getActiveSession();
		boolean enableButton = (session != null && session.isOpened());
		if(enableButton && user!=null)
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
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception){
		if(state.isOpened())
		{
			Log.d(TAG, "Logged in...");
		}
		else if(state.isClosed())
		{
			Log.d(TAG, "Logged out...");
		}
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
			
		}
	};

}
