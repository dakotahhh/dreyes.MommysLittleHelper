package dreyes.mommyslittlehelper;

import java.util.ArrayList;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class FeedingBarGraphView extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fedbabybargraphview);
		
		ArrayList<Bar> points = new ArrayList<Bar>();
		
		Bar d = new Bar();
		d.setColor(Color.parseColor("#99CC00"));
		d.setName("Test1");
		d.setValue(10);
		
		Bar d2 = new Bar();
		d2.setColor(Color.parseColor("#FFBB33"));
		d2.setName("Test2");
		d2.setValue(20);
		
		points.add(d);
		points.add(d2);
		
		BarGraph g = (BarGraph)findViewById(R.id.graph);
		for(int i = 0; i < points.size(); i++)
		{
			Log.d("BARS", " " +i);
		}
//		g.setBars(points);
	}

}
