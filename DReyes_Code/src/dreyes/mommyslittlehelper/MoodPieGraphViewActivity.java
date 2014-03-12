package dreyes.mommyslittlehelper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.SlidingDrawer;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;

public class MoodPieGraphViewActivity extends Activity
{
	
	ArrayList<String> angrySliceList = new ArrayList<String>();
	ArrayList<String> calmSliceList = new ArrayList<String>();
	ArrayList<String> happySliceList = new ArrayList<String>();
	ArrayList<String> hungrySliceList = new ArrayList<String>();
	ArrayList<String> sickSliceList = new ArrayList<String>();
	ArrayList<String> sleepySliceList = new ArrayList<String>();
	ArrayList<String> weepySliceList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moodpiechartview);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		
		int size = preferences.getInt("pie_graph_size", 1);
		
		List<String> data = new ArrayList<String>(size);
		for(int i = 0; i < size; i++)
		{
			data.add(preferences.getString("item_"+i, null));
		}
		
		
		PieGraph pg = (PieGraph)findViewById(R.id.pieGraph);
		PieSlice angrySlice = new PieSlice();
		PieSlice calmSlice = new PieSlice();
		PieSlice happySlice = new PieSlice();
		PieSlice hungrySlice = new PieSlice();
		PieSlice sickSlice = new PieSlice();
		PieSlice sleepySlice = new PieSlice();
		PieSlice weepySlice = new PieSlice();
		angrySlice.setColor(Color.parseColor("#99CC00"));
		calmSlice.setColor(Color.parseColor("#FFBB33"));
		happySlice.setColor(Color.parseColor("#AA66CC"));
		hungrySlice.setColor(Color.parseColor("#FF9494"));
		sickSlice.setColor(Color.parseColor("#5C85FF"));
		sleepySlice.setColor(Color.parseColor("#FFFF00"));
		weepySlice.setColor(Color.parseColor("#800000"));
		
		
		
		
		
		for(String s : data)
		{
			if(s.equals("angry"))
			{
				angrySliceList.add(s);
			}
			else if(s.equals("calm"))
			{
				calmSliceList.add(s);
			}
			else if(s.equals("happy"))
			{
				happySliceList.add(s);
			}
			else if(s.equals("hungry"))
			{
				hungrySliceList.add(s);
			}
			else if(s.equals("sick"))
			{
				sickSliceList.add(s);
			}
			else if(s.equals("sleepy"))
			{
				sleepySliceList.add(s);
			}
			else if(s.equals("weepy"))
			{
				weepySliceList.add(s);
			}
		}
		
		
		
		
		
		angrySlice.setValue(angrySliceList.size());
		calmSlice.setValue(calmSliceList.size());
		happySlice.setValue(happySliceList.size());
		hungrySlice.setValue(hungrySliceList.size());
		sickSlice.setValue(sickSliceList.size());
		sleepySlice.setValue(sleepySliceList.size());
		weepySlice.setValue(weepySliceList.size());

		pg.addSlice(angrySlice);
		pg.addSlice(calmSlice);
		pg.addSlice(hungrySlice);
		pg.addSlice(sickSlice);
		pg.addSlice(sleepySlice);
		pg.addSlice(weepySlice);
	}
}
