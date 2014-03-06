package dreyes.mommyslittlehelper;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class FedBabyGraphViewActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fedbabygraphview);
		
		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[]{
				new GraphViewData(1,  2.0d)
				, new GraphViewData(2, 1.5d)
				, new GraphViewData(3, 2.5d)
				, new GraphViewData(4, 1.0d)
			});
			
			GraphView graphView = new LineGraphView(this, "GraphViewDemo");
			graphView.addSeries(exampleSeries);
			
			LinearLayout layout = (LinearLayout)findViewById(R.id.graph1);
			layout.addView(graphView);
	}

}
