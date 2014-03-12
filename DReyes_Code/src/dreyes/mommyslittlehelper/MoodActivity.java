package dreyes.mommyslittlehelper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Events;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MoodActivity extends Activity implements OnClickListener{

	private ViewFlipper vf;
	private float oldTouchValue;
	
	public ImageButton angryButton, calmButton, happyButton, hungryButton, sickButton, sleepyButton, weepyButton, analyticsButton;
	private String eventDescription;
	private ArrayList<String> moodList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moods);
		vf = (ViewFlipper)findViewById(R.id.flipper01);
		
		angryButton = (ImageButton)findViewById(R.id.angryButton);
		calmButton = (ImageButton)findViewById(R.id.calmButton);
		happyButton = (ImageButton)findViewById(R.id.happyButton);
		hungryButton = (ImageButton)findViewById(R.id.hungryButton);
		sickButton = (ImageButton)findViewById(R.id.sickButton);
		sleepyButton = (ImageButton)findViewById(R.id.sleepyButton);
		weepyButton = (ImageButton)findViewById(R.id.weepyButton);
		analyticsButton = (ImageButton)findViewById(R.id.moodAnalyticsButton);
		angryButton.setOnClickListener(this);
		calmButton.setOnClickListener(this);
		happyButton.setOnClickListener(this);
		hungryButton.setOnClickListener(this);
		sickButton.setOnClickListener(this);
		sleepyButton.setOnClickListener(this);
		weepyButton.setOnClickListener(this);
		analyticsButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.angryButton)
		{
			eventDescription = "Mood: Angry";
			moodList.add("angry");
			createEvent();
		}
		else if(v.getId() == R.id.calmButton)
		{
			eventDescription = "Mood: Calm";
			moodList.add("calm");
			createEvent();
		}
		else if(v.getId() == R.id.happyButton)
		{
			eventDescription = "Mood: Happy";
			moodList.add("happy");
			createEvent();
		}
		else if(v.getId() == R.id.hungryButton)
		{
			eventDescription = "Mood: Hungry";
			moodList.add("hungry");
			createEvent();
		}
		else if(v.getId() == R.id.sickButton)
		{
			eventDescription = "Mood: Sick";
			moodList.add("sick");
			createEvent();
		}
		else if(v.getId() == R.id.sleepyButton)
		{
			eventDescription = "Mood: Sleepy";
			moodList.add("sleepy");
			createEvent();
		}
		else if(v.getId() == R.id.weepyButton)
		{
			eventDescription = "Mood: Weepy";
			moodList.add("weepy");
			createEvent();
		}
		else if(v.getId() == R.id.moodAnalyticsButton)
		{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			for(int i = 0; i < moodList.size(); i++)
			{
				editor.putString("item_"+i, moodList.get(i));
			}
			editor.putInt("pie_graph_size", moodList.size());
			editor.commit();
			Intent intent = new Intent(this, MoodPieGraphViewActivity.class);
			startActivity(intent);
			
		}
	}
	
	private void createEvent()
	{
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, "Mood");
		intent.putExtra(Events.DESCRIPTION, eventDescription);
		intent.putExtra(Events.HAS_ALARM, false);
		startActivity(intent);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldTouchValue = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			float currentX = event.getX();
			if(oldTouchValue < currentX)
			{
				vf.setInAnimation(inFromLeftAnimation());
				vf.setOutAnimation(outToRightAnimation());
				vf.showNext();
			}
			if(oldTouchValue > currentX)
			{
				vf.setInAnimation(inFromRightAnimation());
				vf.setOutAnimation(outToLeftAnimation());
				vf.showPrevious();
			}
			break;
		}
		return false;
	}
	
	public static Animation inFromRightAnimation()
	{
		Animation inFromRight = new TranslateAnimation
				(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(350);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}
	
	public static Animation outToLeftAnimation()
	{
		Animation outtoLeft = new TranslateAnimation
				(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(350);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}
	
	public static Animation inFromLeftAnimation()
	{
		Animation inFromLeft = new TranslateAnimation
				(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(350);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	
	public static Animation outToRightAnimation()
	{
		Animation outtoRight = new TranslateAnimation
				(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(350);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	
	
}
