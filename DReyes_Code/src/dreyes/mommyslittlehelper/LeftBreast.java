package dreyes.mommyslittlehelper;

import java.util.ArrayList;

import android.app.Application;

public class LeftBreast extends Application{
	
	public ArrayList<Integer> leftBreastList;
	
	public LeftBreast()
	{
		
	}
	
	public LeftBreast(String temp)
	{
		leftBreastList = new ArrayList<Integer>();
	}
	
	public void addToList(int left)
	{
		leftBreastList.add(left);
	}
	
	public ArrayList<Integer> getList()
	{
		return leftBreastList;
	}
}
