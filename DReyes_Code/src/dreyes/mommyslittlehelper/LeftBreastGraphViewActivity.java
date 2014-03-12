package dreyes.mommyslittlehelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class LeftBreastGraphViewActivity extends Activity{
	
	LineGraph lg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leftbreastgraphview);
		
		LeftBreast lb = new LeftBreast();
		ArrayList<Integer> leftBreasts =  lb.getList();
		
//		if(leftBreasts != null)
//		{
//			
//			Toast.makeText(this, "not null", Toast.LENGTH_LONG).show();
//		}
//		else
//		{
//			Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
//
//		}
		
//		for(int i = 0; i < leftBreasts.size(); i++)
//		{
//			p = new LinePoint();
//			p.setX(i);
//			p.setY(leftBreasts.get(i));
//			line.addPoint(p);
//		}
//		
//		line.setColor(Color.parseColor("#FFBB33"));
//		lg = (LineGraph)findViewById(R.id.graph);
//		lg.addLine(line);
//		lg.setRangeY(0, 5);
//		lg.setLineToFill(0);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		
		int size = preferences.getInt("item_size", 0);
		
		List<String> data = new ArrayList<String>(size);
		for(int i = 0; i < size; i++)
		{
			data.add(preferences.getString("item_"+i, null));
		}
		
		data.add("5 2");
		data.add("7 8");
		
		Line line = new Line();
		LinePoint p;
		Line rightLine = new Line();
		
		p = new LinePoint();
		p.setX(0);
		p.setY(2.3);
		line.addPoint(p);
		p = new LinePoint();
		p.setX(1);
		p.setY(1.5);
		line.addPoint(p);
		
		for(String s: data)
		{
			String[] whole = s.split(" ");
			for(int i = 0; i < whole.length; i++)
			{
				int d = Integer.parseInt(whole[i]);
				if((i%2)==0)
				{
					p = new LinePoint();
					p.setX(data.indexOf(s));
					p.setY(d);
					line.addPoint(p);
				}
				else
				{
					p = new LinePoint();
					p.setX(data.indexOf(s));
					p.setY(d);
					rightLine.addPoint(p);
				}
				Log.d("STUPID", " " + line.getPoints().size());
			}
		}
		
		line.setColor(Color.parseColor("#FFBB33"));
		rightLine.setColor(Color.parseColor("#99CC00"));
		
		lg = (LineGraph)findViewById(R.id.graph);
		lg.addLine(line);
		lg.addLine(rightLine);
		lg.setRangeX(0, 10);
		lg.setRangeY(0, 10);
		lg.setLineToFill(0);
		
	
		
		
//		p.setX(0);
//		p.setY(2.3);
//		line.addPoint(p);
//		p = new LinePoint();
//		p.setX(1);
//		p.setY(1.5);
//		line.addPoint(p);
//		p = new LinePoint();
//		p.setX(2);
//		p.setY(2.4);
//		line.addPoint(p);
//		line.setColor(Color.parseColor("#FFBB33"));
//		LinePoint lp = new LinePoint();
//		lp.setX(0);
//		lp.setY(3.3);
//		rightLine.addPoint(lp);
//		lp = new LinePoint();
//		lp.setX(1);
//		lp.setY(1.5);
//		rightLine.addPoint(lp);
//		lp.setX(2);
//		lp.setY(2.5);
//		rightLine.addPoint(lp);
//		rightLine.setColor(Color.parseColor("#99CC00"));
	
		
	}

}
