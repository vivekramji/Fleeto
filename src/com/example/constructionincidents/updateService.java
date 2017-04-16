package com.example.constructionincidents;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class updateService  extends Service {
	Context context=this;
	public static String apiURL = "https://api.flightstats.com/flex/flightstatus/rest/v2/xml/flight/status/carrier/number/dep/yyyy/mmmm/dddd?appId=d35374c5&appKey=3d8ba1413eefba82440d0d4e737890e9&utc=false";
	public static String urlString;
	Date date1, date2;
	long diffInMillies;
	String s1;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Let it continue running until it is stopped.

		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		urlString = apiURL.replace("carrier", sharedClass.carrier);
		urlString = urlString.replace("number", sharedClass.flightID);
		urlString = urlString.replace("yyyy", sharedClass.yr);
		urlString = urlString.replace("mmmm", sharedClass.mon);
		urlString = urlString.replace("dddd", sharedClass.day);
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					while(true)
					{

						URL website = new URL(urlString);
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						XMLReader xr = sp.getXMLReader();
						HandlingFlightXML hx = new HandlingFlightXML();
						xr.setContentHandler(hx);
						xr.parse(new InputSource(website.openStream()));
						sharedClass.sharedData((XMLFlightData)newSharedClass.getAirportMap().get(sharedClass.getFSK()));

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

						sharedClass.setDelayTime(diffInMillies);
						Thread.sleep(5000);





					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		return START_NOT_STICKY;
	}
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		super.onDestroy();

	}

}
