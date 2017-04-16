package com.example.constructionincidents;

public class XMLMapqData {


	private String distance=null, time=null, fuel=null;


	public void setDistance(String s)
	{
		distance=s;
	}
	public void setTime(String s)
	{
		time=s;
		sharedClass.travelTimeinSec=Integer.valueOf(s);
	}
	public void setFuel(String s)
	{
		fuel=s;
	}


	public String getDistance()
	{
		return this.distance;
	}
	public String getTime()
	{
		int mins=Integer.parseInt(time)/60;
		int hrs=mins/60;
		int cmins=mins%60;
		return hrs+" hour "+cmins+" mins";
	}
	public String getFuel()
	{
		return this.fuel;
	}
}
