package com.example.constructionincidents;

import java.util.HashMap;
import java.util.Map;


public class newSharedClass {

	static public Map<String ,XMLFlightData> airportMap = new HashMap<String, XMLFlightData>();
	static public Map<String ,XMLFlightData> flightStatusMap = new HashMap<String, XMLFlightData>();

	public static Map getAirportMap()
	{
		return airportMap;
	}

	public static Map getFlightStatusMap()
	{
		return flightStatusMap;
	}

}
