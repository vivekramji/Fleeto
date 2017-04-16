package com.example.constructionincidents;

/**
 * Created by Vivek R on 4/9/2015.
 */
public class sharedClass {
	static XMLFlightData sharedData;
	static int travelTimeinSec;
	static long diffInMillies;
	public static String FSK = null, username=null;
	public static String carrier=null, flightID=null, yr=null, mon=null, day=null, dateDD=null;
	//static String sharedLat;
	//static String sharedLon;
	//static int delayTime;

	public static void setFSK(String s)
	{
		FSK = s;
	}

	public static void setUserName(String s)
	{
		username = s;
	}

	public static String getFSK()
	{
		return FSK;
	}

	public static void sharedData(XMLFlightData newData)
	{

		sharedData = newData;
	}

	public static void setFinalScheduledGateDeparture(){
		sharedData.setestdnt(sharedData.getascheduleddnt());
	}
	public static String getFinalAirportName()
	{
		return sharedData.getaName();
	}

	public static String getFinalLatitude()
	{
		return sharedData.getaLat();
	}

	public static String getFinalLongitude()
	{
		return sharedData.getaLon();
	}
	public static String getAirLine()
	{
		return sharedData.getAirline();
	}

	public static  String getFinalScheduledGateDeparture()
	{
		return sharedData.getascheduleddnt();
	}

	public static  String getFinalEstGateDeparture()
	{
		return sharedData.getaestdnt();
	}
	public static String getFinalDepartureTerminal()
	{
		return sharedData.getDepartureT();
	}

	public static void setDelayTime(long x)
	{
		diffInMillies = x;
	}

	public static long getDelayTime()
	{
		return diffInMillies;
	}

	public static String getUserName()
	{
		return username;
	}

	/*public static void setSharedLat( String latit) { sharedLat = latit;}
    public static String getSharedLat() { return sharedLat;}

    public static void setSharedLon(String longig) { sharedLon = longig;}
    public static String getSharedLon() {return sharedLon; }*/



}
