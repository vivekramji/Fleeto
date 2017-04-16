package com.example.constructionincidents;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vivek R on 4/9/2015.
 */
public class resultpage extends Activity {
	TextView airport, scheduled, estimated, terminal, date, flight, status, airline, forecast;
	TextView txtCurrentTime, header4;
	Button start, climate, refresh, update;
	long diffInMillies;
	long diffInMins;
	Date date1, date2;
	String s1, s2, s3, midTimeHour, finalTimeHour, midQPF, finalQPF, totalQPF, midSnow, finalSnow, totalSnow;
	Context context=this;
	int flag=1, midTimeinSec;
	static boolean onresume=false;
	List<String> popList = new ArrayList<String>();
	public static String apiURL = "https://api.flightstats.com/flex/flightstatus/rest/v2/xml/flight/status/carrier/number/dep/yyyy/mmmm/dddd?appId=d35374c5&appKey=3d8ba1413eefba82440d0d4e737890e9&utc=false";
	public static String urlString;
	private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	String staticmapq="http://www.mapquestapi.com/directions/v2/route?&outFormat=xml&key=Jmjtd%7Clu6yn907nu%2C8w%3Do5-lw8x9&narrativeType=none&shapeFormat=cmp6&generalize=0&from=srclat,srclong&to=destlat,destlong&callback=renderNarrative";
	String mapq;
	String staticUrlDirections="https://maps.googleapis.com/maps/api/directions/xml?origin=srclat,srclong&destination=destlat,destlong&key=AIzaSyBzpMfhHp1WRw1mMIh1yKHBLcmQcxRPpvE";
	String urlDirections;
	private static final String staticurl="http://api.wunderground.com/api/50f4532de367c7e9/hourly/q/srclat,srclong.xml";
	private String url=null;
	Location myLoc;
	String bestProvider;
	LocationManager locationManager;
	Criteria criteria;
	LatLng midLatLng;
	private double forecastDelay=0.0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.resultpage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		airport = (TextView) findViewById(R.id.airportV);
		scheduled=(TextView) findViewById(R.id.scheduledV);
		estimated=(TextView) findViewById(R.id.estimatedV);
		terminal=(TextView) findViewById(R.id.terminalV);
		date=(TextView) findViewById(R.id.dateV);
		flight=(TextView) findViewById(R.id.flightV);
		airline=(TextView) findViewById(R.id.airlineV);
		status=(TextView) findViewById(R.id.statusV);
		txtCurrentTime = (TextView)findViewById(R.id.displayDelay);
		start=(Button) findViewById(R.id.startbutton);
		climate=(Button) findViewById(R.id.climatebutton);
		refresh=(Button) findViewById(R.id.refresh);
		update=(Button) findViewById(R.id.update);
		header4=(TextView) findViewById(R.id.header4);
		forecast=(TextView) findViewById(R.id.forecastV);

		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.startAnimation(buttonClick);
				setUI();

			}
		});	

		update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
				flag=1;
				Thread t1 = new Thread(new Runnable() {

					@Override
					public void run() {
						try {

							URL website = new URL(urlString);
							SAXParserFactory spf = SAXParserFactory.newInstance();
							SAXParser sp = spf.newSAXParser();
							XMLReader xr = sp.getXMLReader();
							HandlingFlightXML hx = new HandlingFlightXML();
							xr.setContentHandler(hx);
							xr.parse(new InputSource(website.openStream()));

							sharedClass.sharedData((XMLFlightData)newSharedClass.getAirportMap().get

									(sharedClass.getFSK()));
							if(sharedClass.getFinalEstGateDeparture()==null)
							{
								sharedClass.setFinalScheduledGateDeparture();
							}

							setUI();
						} catch (Exception e) {
							setMsg(e.toString());
						}
					}
				});
				t1.start();
			}
		});
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sharedClass.getDelayTime()>0)
				{
					onresume=true;
					Intent startnav = new Intent("com.example.constructionincidents.MAINACTIVITY");
					startActivity(startnav);
				}
				else 
					Toast.makeText(getApplicationContext(), "You are late! No use of navigating to the airport", Toast.LENGTH_LONG).show();

			}
		});

		climate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(sharedClass.getDelayTime()/60000<=1440&&sharedClass.getDelayTime()>0)
				{
					onresume=true;
					Intent checkclimate = new Intent

							("com.example.constructionincidents.WEATHERXMLPARSING");
					startActivity(checkclimate);
				}
				else if(sharedClass.getDelayTime()<0)
					Toast.makeText(getApplicationContext(), "You are late! No use of checking the climate", Toast.LENGTH_LONG).show();	
				else Toast.makeText(getApplicationContext(), "Climate forecast will be available 24 hours before the scheduled gate departure time", Toast.LENGTH_LONG).show();

			}
		});



		//-----------------AIRPORT DETAILS-----------------------------------------------------------------------------------------------


		urlString = apiURL.replace("carrier", sharedClass.carrier);
		urlString = urlString.replace("number", sharedClass.flightID);
		urlString = urlString.replace("yyyy", sharedClass.yr);
		urlString = urlString.replace("mmmm", sharedClass.mon);
		urlString = urlString.replace("dddd", sharedClass.day);

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					URL website = new URL(urlString);
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					HandlingFlightXML hx = new HandlingFlightXML();
					xr.setContentHandler(hx);
					xr.parse(new InputSource(website.openStream()));
					if(sharedClass.getFSK()==null)
					{
						int index = 0;
						Iterator it = newSharedClass.getAirportMap().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry tempMapObj = (Map.Entry) it.next();
							XMLFlightData temp=(XMLFlightData)tempMapObj.getValue();
							if(temp.departure==true) {
								popList.add(index, (String) tempMapObj.getKey());
								index++;
							}
						}

						if(popList.isEmpty())
						{
							/*Intent ip = new Intent("com.example.constructionincidents.INPUTPAGE");
                    	startActivity(ip);*/
							setMsg("Incorrect flight details. Please try again.");
							finish();
						}


						synchronized (this) {

							getIpFromUser(popList);
							this.wait(300);
						}

					}
					try{
						Class.forName("com.mysql.jdbc.Driver");
						// Setup the connection with the DB
						String myDB="ese543";
						String MYSQLUSER = "root";
						String MYSQLPW = "nikhil";
						connect = DriverManager
								.getConnection("jdbc:mysql://ec2-52-5-120-73.compute-1.amazonaws.com/"+myDB,MYSQLUSER,MYSQLPW);




						preparedStatement = connect
								.prepareStatement("select HID from ese543.history_table h where h.flightCarrier=? and h.flightID=? and h.mon=? and h.day=? and h.year=? and h.boarding=? and h.UID IN (select UID from ese543.users where username=?)");
						preparedStatement.setString(1, sharedClass.carrier);
						preparedStatement.setString(2, sharedClass.flightID);
						preparedStatement.setString(3, sharedClass.mon);
						preparedStatement.setString(4, sharedClass.day);
						preparedStatement.setString(5, sharedClass.yr);
						preparedStatement.setString(6, sharedClass.FSK);
						preparedStatement.setString(7, sharedClass.username);

						resultSet= preparedStatement.executeQuery();

						int id=0;
						while(resultSet.next())
							id++;

						if(id==0)
						{
							preparedStatement = connect
									.prepareStatement("insert into ese543.history_table (flightCarrier, flightID, mon, day, year, boarding, UID) values (?, ?, ?, ?, ?, ?,(select UID from ese543.users where username=?))");
							preparedStatement.setString(1, sharedClass.carrier);
							preparedStatement.setString(2, sharedClass.flightID);
							preparedStatement.setString(3, sharedClass.mon);
							preparedStatement.setString(4, sharedClass.day);
							preparedStatement.setString(5, sharedClass.yr);
							preparedStatement.setString(6, sharedClass.FSK);
							preparedStatement.setString(7, sharedClass.username);

							int rows=preparedStatement.executeUpdate();
							if(rows>0)
							{
								//entry inserted
								HistoryData.addData(sharedClass.carrier, Integer.valueOf(sharedClass.flightID), Integer.valueOf(sharedClass.mon), Integer.valueOf(sharedClass.day), Integer.valueOf(sharedClass.yr), sharedClass.FSK);
							}

						}
						else{
							//entry already present
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						closeAll();
					}




					sharedClass.sharedData((XMLFlightData)newSharedClass.getAirportMap().get(sharedClass.getFSK()));
					if(sharedClass.getFinalEstGateDeparture()==null)
					{
						sharedClass.setFinalScheduledGateDeparture();
					}
					setUI();
					getTravelTime();
					directionPlot();
					try
					{
						synchronized (this) {


							while(midTimeHour==null||finalTimeHour==null||midLatLng==null)
							{
								this.wait(300);
							}
						}
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
					getClimate(midLatLng, "m");
					LatLng dest=new LatLng(Double.valueOf(sharedClass.getFinalLatitude()),Double.valueOf(sharedClass.getFinalLongitude()));
					getClimate(dest, "d");
				} catch (Exception e) {
					setMsg("No data available. Please try again with different date.");
					finish();
				}
			}
		});
		t1.start();
		//startService(new Intent(getBaseContext(), updateService.class));

		//-----------------Get Location---------------






	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onStop();
		sharedClass.setFSK(null);
		newSharedClass.getAirportMap().clear();
	}


	public void getIpFromUser(List<String> tempList) {
		final List<String> tempList1 = tempList;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {



				AlertDialog.Builder build = new AlertDialog.Builder(context);
				build.setTitle("Select your boarding point");
				ListView modelist = new ListView(context);
				ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, 

						android.R.layout.simple_list_item_1, android.R.id.text1, popList);
				modelist.setAdapter(modeAdapter);

				//build.setView(modelist);
				final CharSequence[] c = popList.toArray(new CharSequence[popList.size()]);
				build.setSingleChoiceItems(c, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Toast.makeText(getApplicationContext(), c[which], Toast.LENGTH_LONG).show();
						sharedClass.setFSK(c[which].toString());
						dialog.dismiss();

					}
				});

				final Dialog dialog = build.create();
				dialog.show();


			}


		}) ;
	}

	public void setMsg(String msg) {
		final String str = msg;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
			}
		});

	}
	public void setUI() {

		runOnUiThread(new Runnable() {
			@Override

			public void run() {


				if(flag==1)
				{
					findViewById(R.id.loadingPanel).setVisibility(View.GONE);
					flag=0;
				}
				flight.setText(sharedClass.carrier+"-"+sharedClass.flightID);
				airline.setText(sharedClass.getAirLine());
				date.setText(sharedClass.mon+"-"+sharedClass.day+"-"+sharedClass.yr);
				airport.setText(sharedClass.getFinalAirportName());
				scheduled.setText(sharedClass.getFinalScheduledGateDeparture().substring(0, sharedClass.getFinalScheduledGateDeparture().indexOf('T'))+"\nat "+
						sharedClass.getFinalScheduledGateDeparture().substring(sharedClass.getFinalScheduledGateDeparture().indexOf('T')+1,sharedClass.getFinalScheduledGateDeparture().indexOf('.')));
				estimated.setText(sharedClass.getFinalEstGateDeparture().substring(0, sharedClass.getFinalEstGateDeparture().indexOf('T'))+"\nat "+
						sharedClass.getFinalEstGateDeparture().substring(sharedClass.getFinalEstGateDeparture().indexOf('T')+1,sharedClass.getFinalEstGateDeparture().indexOf('.')));
				terminal.setText(sharedClass.getFinalDepartureTerminal());
				String est = sharedClass.getFinalEstGateDeparture();
				String sch = sharedClass.getFinalScheduledGateDeparture();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
				Date et=null,s=null;
				try{
					et = sdf.parse(est);

					s = sdf.parse(sch);

				}catch(ParseException e){
					e.printStackTrace();
				}
				long delaymilli = et.getTime()-s.getTime();
				status.setText(String.valueOf(delaymilli/60000)+" minutes");

				s1=sharedClass.getFinalEstGateDeparture();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
				try{
					date1 = simpleDateFormat.parse(s1);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					String date = df.format(Calendar.getInstance().getTime());
					date2 = simpleDateFormat.parse(date);

				}catch(ParseException e){
					e.printStackTrace();
				}
				diffInMillies = date1.getTime() - date2.getTime();
				if(diffInMillies>0)
				{

					//long diffSeconds = diffInMillies / 1000 % 60;
					long diffMinutes = diffInMillies / (60 * 1000) % 60;
					long diffHours = diffInMillies / (60 * 60 * 1000) % 24;
					long diffDays = diffInMillies / (24 * 60 * 60 * 1000);

					txtCurrentTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
					txtCurrentTime.setText(String.format("%02d", diffDays)+":"+String.format("%02d", 

							diffHours)+":"+String.format("%02d", diffMinutes));
				}
				else {
					txtCurrentTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					header4.setVisibility(TextView.INVISIBLE);
					txtCurrentTime.setText("Oops! You already missed your flight");
				}
				sharedClass.setDelayTime(diffInMillies);
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);




	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(onresume==true)
		{
			setUI();
			onresume=false;
		}


	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void closeAll() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connect != null) {
				connect.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getTravelTime()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					criteria=new Criteria();
					bestProvider = locationManager.getBestProvider(criteria,true);
					myLoc=locationManager.getLastKnownLocation(bestProvider);
					mapq=staticmapq.replace("srclat", String.valueOf(myLoc.getLatitude()));
					mapq=mapq.replace("srclong", String.valueOf(myLoc.getLongitude()));
					try{
						synchronized (this) {
							while(sharedClass.getFinalLatitude()==null)
								this.wait(300);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mapq=mapq.replace("destlat", sharedClass.getFinalLatitude());
					mapq=mapq.replace("destlong", sharedClass.getFinalLongitude());
					URL website = new URL(mapq);
					SAXParserFactory spf= SAXParserFactory.newInstance();
					SAXParser sp=spf.newSAXParser();
					XMLReader xr= sp.getXMLReader();
					HandlingMapqXML hx=new HandlingMapqXML();
					xr.setContentHandler(hx);

					xr.parse(new InputSource(website.openStream()));
					XMLMapqData info=hx.getInformation();

					int temp=sharedClass.travelTimeinSec/2;
					midTimeinSec=temp;
					Calendar currentDate = Calendar.getInstance(); //Get the current date
					SimpleDateFormat formatter= new SimpleDateFormat("H"); 

					midTimeHour = formatter.format(currentDate.getTime().getTime()+1000*midTimeinSec);
					finalTimeHour = formatter.format(currentDate.getTime().getTime()+1000*sharedClass.travelTimeinSec);




				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e);
				}

			}
		}).start();
	}
	private void decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		//PolylineOptions polyL = new PolylineOptions();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(p);
		}

		midLatLng=poly.get(poly.size()/2);
		//for(int i=0;i<poly.size();i++)
		//polyL.addAll(poly);
		//polyL.width(5).color(Color.BLUE);
		//addPoly(polyL);

	}
	public void directionPlot()
	{

		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					criteria=new Criteria();
					bestProvider = locationManager.getBestProvider(criteria,true);
					myLoc=locationManager.getLastKnownLocation(bestProvider);
					urlDirections=staticUrlDirections.replace("srclat", String.valueOf(myLoc.getLatitude()));
					urlDirections=urlDirections.replace("srclong", String.valueOf(myLoc.getLongitude()));

					urlDirections=urlDirections.replace("destlat", sharedClass.getFinalLatitude());
					urlDirections=urlDirections.replace("destlong", sharedClass.getFinalLongitude());
					URL website = new URL(urlDirections);
					SAXParserFactory spf= SAXParserFactory.newInstance();
					SAXParser sp=spf.newSAXParser();
					XMLReader xr= sp.getXMLReader();
					HandlingDirXML hxd=new HandlingDirXML();
					xr.setContentHandler(hxd);
					xr.parse(new InputSource(website.openStream()));

					XMLDirData infodir=hxd.getInformation();

					decodePoly(infodir.getEncoded());

				}
				catch(Exception e)
				{
					setMsg(e.toString());

				}
			}

		}).start();
	}


	public void getClimate(LatLng s1, String which1)
	{
		final LatLng s=s1;
		final String which=which1;
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try{

					url=staticurl.replace("srclat", String.valueOf(s.latitude));
					url=url.replace("srclong", String.valueOf(s.longitude));

					URL website = new URL(url);						 
					SAXParserFactory spf= SAXParserFactory.newInstance();
					SAXParser sp=spf.newSAXParser();
					XMLReader xr= sp.getXMLReader();
					HandlingWeaXML hx=new HandlingWeaXML();
					xr.setContentHandler(hx);
					xr.parse(new InputSource(website.openStream()));
					XMLWeaData info;
					for(int i=0;i<hx.getCount();i++)
					{
						info=hx.getInformation(i);
						if(which.equals("m"))
						{
							if(info.getHour().equals(midTimeHour))
							{
								midQPF=info.getQPF();
								midSnow=info.getSnow();
								forecastDelay=forecastDelay+Double.valueOf(midQPF)+Double.valueOf(midSnow);
								break;
							}
						}
						else {
							if(info.getHour().equals(finalTimeHour))
							{
								finalQPF=info.getQPF();
								finalSnow=info.getSnow();
								forecastDelay=forecastDelay+Double.valueOf(finalQPF)+Double.valueOf(finalSnow);
								break;
							}
						}

					}
					setForecastData();
					hx.resetCount();
					/*double tempQPF=Double.valueOf(midQPF)+Double.valueOf(finalQPF);
					setMsg(String.valueOf(tempQPF));
					double tempSnow=Double.valueOf(midSnow)+Double.valueOf(finalSnow);
					setMsg(String.valueOf(tempSnow));*/

				}
				catch(Exception e){
					setMsg(e.toString());
				}
			}

		}).start();

	}

	public void setForecastData()
	{

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				forecast.setText(String.valueOf(Math.round(forecastDelay))+" mins");
			}
		});
	}

}


