import dreyes.mommyslittlehelper.MoodActivityPageFragment;
import dreyes.mommyslittlehelper.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


public class ScreenSlidePagerActivity extends FragmentActivity {

	private static final int NUM_PAGES = 5;
	
	private ViewPager mPager;
	
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moods);
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}
	
	@Override
	public void onBackPressed() {
		if(mPager.getCurrentItem()==0)
		{
			super.onBackPressed();
		}
		else
		{
			mPager.setCurrentItem(mPager.getCurrentItem() -1);
		}
	}
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
	{
		public ScreenSlidePagerAdapter(FragmentManager fm)
		{
			super(fm);
		}
		
		public Fragment getItem(int position)
		{
			return new MoodActivityPageFragment();
		}
		
		public int getCount()
		{
			return NUM_PAGES;
		}
	}
	
}
