package com.example.constructionincidents;

public class XMLDirData {

	private double lat=0,lng=0;
	private String encoded=null, startAddr=null, endAddr=null;

	public void setLat(double l)
	{

		lat=l;

	}	

	public void setLong(double l)
	{

		lng=l;

	}
	public void setEncoded(String s)
	{
		encoded=s;
	}
	public void setStartAddr(String s)
	{
		startAddr=s;
	}
	public void setEndAddr(String s)
	{
		endAddr=s;
	}

	public double getLat()
	{
		return this.lat;
	}
	public double getLong()
	{
		return this.lng;
	}
	public String getEncoded()
	{
		return this.encoded;
	}
	public String getStartAddr()
	{
		return this.startAddr;
	}
	public String getEndAddr()
	{
		return this.endAddr;
	}
}
