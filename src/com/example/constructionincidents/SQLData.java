package com.example.constructionincidents;

public class SQLData {

	private int flightID;
	private String flightCarrier;
	private String boarding;
	private int mon, day, year;
	public int getFlightID() {
		return flightID;
	}
	public void setFlightID(int flightID) {
		this.flightID = flightID;
	}
	public String getFlightCarrier() {
		return flightCarrier;
	}
	public void setFlightCarrier(String flightCarrier) {
		this.flightCarrier = flightCarrier;
	}
	public String getBoarding() {
		return boarding;
	}
	public void setBoarding(String boarding) {
		this.boarding = boarding;
	}
	public int getMon() {
		return mon;
	}
	public void setMon(int mon) {
		this.mon = mon;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public boolean isEqual(SQLData s)
	{

		if((this.flightCarrier.equalsIgnoreCase(s.getFlightCarrier())&&
				this.flightID==s.getFlightID()&&this.mon==s.getMon()&&this.day==s.getDay()&&this.year==s.getYear()&&
				this.boarding.equalsIgnoreCase(s.getBoarding()))==true)
			return true;
		else return false;
	}


}
