package com.example.constructionincidents;

public class XMLData {
	private String startTime=null, endTime=null, fullDesc=null;
	private double lat=0,lng=0;
	private int type=0, severity=0;


	public void setStartTime(String s)
	{
		startTime=s;
	}

	public void setEndTime(String n)
	{
		endTime=n;
	}
	public void setfullDesc(String v)
	{
		fullDesc=v;
	}

	public void setType(int t)
	{
		type=t;
	}

	public void setSeverity(int t)
	{
		severity=t;
	}

	public void setLat(double l)
	{

		lat=l;

	}	

	public void setLong(double l)
	{

		lng=l;

	}

	public String getStartTime()
	{
		return this.startTime;
	}
	public String getEndTime()
	{
		return this.endTime;
	}
	public String getFullDesc()
	{
		return this.fullDesc;
	}
	public int getType()
	{
		return this.type;
	}
	public int getSeverity()
	{
		return this.severity;
	}
	public double getLat()
	{
		return this.lat;
	}
	public double getLong()
	{
		return this.lng;
	}
}
