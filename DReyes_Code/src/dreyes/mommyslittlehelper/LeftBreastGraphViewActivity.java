package dreyes.mommyslittlehelper;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class LeftBreastGraphViewActivity extends Activity{
	
	LineGraph lg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leftbreastgraphview);
		
		Line line = new Line();
		LinePoint p = new LinePoint();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		p.setX(0);
		p.setY(2.3);
		line.addPoint(p);
		p = new LinePoint();
		p.setX(1);
		p.setY(1.5);
		line.addPoint(p);
		p = new LinePoint();
		p.setX(2);
		p.setY(2.4);
		line.addPoint(p);
		line.setColor(Color.parseColor("#FFBB33"));
		
		Line rightLine = new Line();
		LinePoint lp = new LinePoint();
		lp.setX(0);
		lp.setY(3.3);
		rightLine.addPoint(lp);
		lp = new LinePoint();
		lp.setX(1);
		lp.setY(1.5);
		rightLine.addPoint(lp);
		lp.setX(2);
		lp.setY(2.5);
		rightLine.addPoint(lp);
		rightLine.setColor(Color.parseColor("#99CC00"));
		
		
		lg = (LineGraph)findViewById(R.id.graph);
		lg.addLine(line);
		lg.setRangeY(0, 5);
		lg.setLineToFill(0);
		lg.addLine(rightLine);
		lg.setRangeX(0, 5);
		lg.setLineToFill(0);
		
	}

}
