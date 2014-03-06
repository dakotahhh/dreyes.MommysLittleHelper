package dreyes.mommyslittlehelper;

import com.jjoe64.graphview.GraphViewDataInterface;

public class GraphViewData implements GraphViewDataInterface{
	private double x,y;
	
	public GraphViewData(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return this.x;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return this.y;
	}

}
