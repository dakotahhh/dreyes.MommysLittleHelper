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
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		
		int size = preferences.getInt("item_size", 0);
		
		List<String> data = new ArrayList<String>(size);
		for(int i = 0; i < size; i++)
		{
			data.add(preferences.getString("item_"+i, null));
		}
		
		Line line = new Line();
		LinePoint p;
		Line rightLine = new Line();
		
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
			}
		}
		
		line.setColor(Color.parseColor("#FF0000"));
		rightLine.setColor(Color.parseColor("#0000FF"));
		
		lg = (LineGraph)findViewById(R.id.graph);
		lg.addLine(line);
		lg.addLine(rightLine);
		lg.setRangeX(0, 10);
		lg.setRangeY(0, 10);
		lg.setLineToFill(0);
	
		
	}

}
