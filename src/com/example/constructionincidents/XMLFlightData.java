package com.example.constructionincidents;


public class XMLFlightData {
	String aFs = null;
	String aName = null;
	String aCity = null;
	String aLat = null;
	String aLon = null;
	String aAirline = null;
	Boolean departure = false;
	String scheduleddnt = null;
	String estdnt = null;
	String departureT = null;

	//---------------Setters for Airport Details-------------------------

	public void setaFs(String temp_afs){
		this.aFs = temp_afs;
	}
	public void setaName(String temp_aName){
		this.aName = temp_aName;
	}
	public void setaCity(String temp_aCity){
		this.aCity = temp_aCity;
	}
	public void setaLat(String temp_aLat){
		this.aLat = temp_aLat;
	}
	public void setaLon(String temp_aLon){
		this.aLon = temp_aLon;
	}
	public void setAirline(String temp){
		this.aAirline = temp;
	}
	public void setadeparture(Boolean val){this.departure = val;}
	public void setscheduleddnt(String dnt){this.scheduleddnt = dnt;}
	public void setestdnt(String dnt){this.estdnt = dnt;}
	public void setDepartureT(String dt){this.departureT = dt;}

	//---------------------------------------------------------

	//--------------Getters for Airport Details---------------------------

	public String getaFs(){
		return this.aFs;
	}
	public String getaName(){
		return this.aName;
	}
	public String getaCity(){
		return this.aCity;
	}
	public String getaLat(){
		return this.aLat ;
	}
	public String getaLon(){
		return this.aLon;
	}
	public String getAirline(){
		return this.aAirline;
	}
	public Boolean getadeparture(){return this.departure;}
	public String getascheduleddnt(){return this.scheduleddnt;}
	public String getaestdnt(){return this.estdnt;}
	public String getDepartureT(){return this.departureT;}

	//-------------------------------------------------------------------

}
