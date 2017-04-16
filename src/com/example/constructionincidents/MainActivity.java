package com.example.constructionincidents;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnItemSelectedListener, OnInfoWindowClickListener, LocationListener {


	private ToggleButton icon, traffic, follow;
	private Button reroute;
	private Spinner spin;
	private TextView distTime, slider;
	private GoogleMap mMap;
	private Location myLoc=null;
	private Polyline polyline=null;
	private Marker srcMrk, destMrk;
	private Location followOff=null;
	private LocationManager locationManager;
	XMLDirData infodir;
	private String bestProvider;
	private List<Marker> m = new ArrayList<Marker>();
	private DrawerLayout mDrawerLayout;
	static int count=0, countDist=0, countStatus=0;
	int countMapq=0;
	final Context context = this;
	final LocationListener listener=this;
	private boolean isGPSready=false;
	XMLMapqData info=null;
	String url="http://www.mapquestapi.com/traffic/v2/incidents?key=Jmjtd%7Clu6yn907nu%2C8w%3Do5-lw8x9&callback=handleIncidentsResponse&boundingBox=destlat,destlong,srclat,srclong&filters=construction,incidents&inFormat=kvp&outFormat=xml";
	String staticmapq="http://www.mapquestapi.com/directions/v2/route?&outFormat=xml&key=Jmjtd%7Clu6yn907nu%2C8w%3Do5-lw8x9&narrativeType=none&shapeFormat=cmp6&generalize=0&from=srclat,srclong&to=destlat,destlong&callback=renderNarrative";
	String mapq;
	String staticUrlDirections="https://maps.googleapis.com/maps/api/directions/xml?origin=srclat,srclong&destination=destlat,destlong&key=AIzaSyBzpMfhHp1WRw1mMIh1yKHBLcmQcxRPpvE";
	String urlDirections;
	Button swipe;

	//JFK--40.642235,-73.78817

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		icon=(ToggleButton)findViewById(R.id.toggleIcon);
		traffic=(ToggleButton)findViewById(R.id.toggleTraffic);
		follow=(ToggleButton)findViewById(R.id.follow);
		distTime=(TextView) findViewById(R.id.DistTime);
		reroute=(Button) findViewById(R.id.reroute);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		slider=(TextView) findViewById(R.id.slidermenu);
		swipe = (Button) findViewById(R.id.ok);
		//long milli=sharedClass.getDelayTime();


		@SuppressWarnings("deprecation")
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				//getActionBar().setTitle("Closed");
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				//getActionBar().setTitle("Open");
				Date date1=null, date2=null;
				String s1=sharedClass.getFinalEstGateDeparture();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
				try{
					date1 = simpleDateFormat.parse(s1);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					String date = df.format(Calendar.getInstance().getTime());
					date2 = simpleDateFormat.parse(date);

				}catch(ParseException e){
					e.printStackTrace();
				}
				long milli = date1.getTime() - date2.getTime();
				if(milli>0)
				{

					sharedClass.setDelayTime(milli);
					slider.setText("Total time left:\n"+String.format("%02d", milli/(24 * 60 * 60 * 1000))+":"+String.format("%02d", milli/(60 * 60 * 1000) % 24)+":"+String.format("%02d", milli/(60 * 1000)%60)+"\nDD:HH:MM");
				}
				else slider.setText("You missed the flight!");
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		reroute.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Recalculating the route...", Toast.LENGTH_SHORT).show();
				if(polyline!=null)
				{
					polyline.remove();
					srcMrk.remove();
					destMrk.remove();
					distTimeFuel();
					directionPlot("statuschange");
				}

			}
		});
		turnGPSOn();
		if (mMap == null) {

			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			mMap.setTrafficEnabled(true);
			mMap.setMyLocationEnabled(true);

			spin = (Spinner) findViewById(R.id.spinner1);
			List<String> list = new ArrayList<String>();
			list.add("Normal");
			list.add("Satellite");
			list.add("Terrain");

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adapter);
			spin.setOnItemSelectedListener(this);

			mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

				@Override
				public void onCameraChange(CameraPosition arg0) {
					// TODO Auto-generated method stub
					followOff=mMap.getMyLocation();
				}
			});


			icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								for(int i=0;i<m.size();i++)
									m.get(i).setVisible(true);;	

							}
						});
					} else {
						// The toggle is disabled
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								for(int i=0;i<m.size();i++)
									m.get(i).setVisible(false);;	

							}
						});
					}
				}
			});

			traffic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						// The toggle is enabled
						mMap.setTrafficEnabled(true);
					} else {
						// The toggle is disabled
						mMap.setTrafficEnabled(false);
					}
				}
			});



			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			//LM=(LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			bestProvider = locationManager.getBestProvider(criteria,true);

			myLoc=locationManager.getLastKnownLocation(bestProvider);
			//double latval=myLoc.getLatitude();
			String lat=String.valueOf(myLoc.getLatitude());
			String longi=String.valueOf(myLoc.getLongitude());

			//final LocationListener listener=new LocationListener();

			if(polyline!=null)
			{
				polyline.remove();
				srcMrk.remove();
				destMrk.remove();

			}
			directionPlot("main");


			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);

			follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						// The toggle is enabled

						//locationManager.removeUpdates(listener);
						if(isGPSready==true)
							locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
						else locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
					} else {
						// The toggle is disabled
						locationManager.removeUpdates(listener);

						LatLng latLng = new LatLng(followOff.getLatitude(), followOff.getLongitude());
						CameraPosition cp= new CameraPosition(latLng, 18f, 0f, followOff.getBearing());
						CameraUpdate cameraUpdate=CameraUpdateFactory.newCameraPosition(cp);
						//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
						mMap.animateCamera(cameraUpdate);


					}
				}
			});
			url=url.replace("srclat",lat );
			url=url.replace("srclong", longi);
			url=url.replace("destlat", sharedClass.getFinalLatitude());
			url=url.replace("destlong", sharedClass.getFinalLongitude());
			mapq=staticmapq.replace("srclat", lat);
			mapq=mapq.replace("srclong", longi);
			mapq=mapq.replace("destlat", sharedClass.getFinalLatitude());
			mapq=mapq.replace("destlong", sharedClass.getFinalLongitude());



			mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
			mMap.setOnInfoWindowClickListener(this);
			constructionPlot();
			distTimeFuel();

		}
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		CameraPosition cp= new CameraPosition(latLng, 18f, 67.5f, location.getBearing());
		CameraUpdate cameraUpdate=CameraUpdateFactory.newCameraPosition(cp);
		//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
		mMap.animateCamera(cameraUpdate);




	}

	@Override
	public void onStatusChanged(String provider, int status,
			Bundle extras) {
		// TODO Auto-generated method stub
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Toast.makeText(this, "Status Changed: Out of Service",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Toast.makeText(this, "Status Changed: Temporarily Unavailable",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:

			Toast.makeText(this, "Status Changed: Available",
					Toast.LENGTH_SHORT).show();
			if(polyline!=null)
			{
				polyline.remove();
				srcMrk.remove();
				destMrk.remove();

			}
			directionPlot("statuschange");
			isGPSready=true;
			locationManager.removeUpdates(listener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);


			break;
		}

	}

	@Override
	protected void onPause()
	{

		super.onPause();
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		/*countStatus=0;

	    	if(polyline!=null)
	    	{
		    	polyline.remove();
		    	srcMrk.remove();
				destMrk.remove();
				srcMrk=destMrk=null;
				polyline=null;
	    	}
	    	finish();*/
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
	public void setDistTimeonMap(XMLMapqData info)
	{
		final XMLMapqData inf=info;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				distTime.setText("Dist: "+inf.getDistance()+" mi   Time: "+inf.getTime());

			}
		});
	}

	public void directionPlot(String callFrm)
	{
		final String callFrom=callFrm;
		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					while(true)
					{
						if(myLoc!=null||followOff!=null)
						{
							if(callFrom.equals("statuschange"))
							{
								urlDirections=staticUrlDirections.replace("srclat", String.valueOf(followOff.getLatitude()));
								urlDirections=urlDirections.replace("srclong", String.valueOf(followOff.getLongitude()));

							}
							else
							{
								urlDirections=staticUrlDirections.replace("srclat", String.valueOf(myLoc.getLatitude()));
								urlDirections=urlDirections.replace("srclong", String.valueOf(myLoc.getLongitude()));
							}
							break;
						}
					}
					urlDirections=urlDirections.replace("destlat", sharedClass.getFinalLatitude());
					urlDirections=urlDirections.replace("destlong", sharedClass.getFinalLongitude());
					URL website = new URL(urlDirections);
					SAXParserFactory spf= SAXParserFactory.newInstance();
					SAXParser sp=spf.newSAXParser();
					XMLReader xr= sp.getXMLReader();
					HandlingDirXML hxd=new HandlingDirXML();
					xr.setContentHandler(hxd);
					xr.parse(new InputSource(website.openStream()));

					infodir=hxd.getInformation();
					addSrcDestToMap(infodir,callFrom);
					decodePoly(infodir.getEncoded());

				}
				catch(Exception e)
				{
					setMsg(e.toString());

				}
			}

		}).start();
	}

	public void distTimeFuel()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					URL website = new URL(mapq);
					SAXParserFactory spf= SAXParserFactory.newInstance();
					SAXParser sp=spf.newSAXParser();
					XMLReader xr= sp.getXMLReader();
					HandlingMapqXML hx=new HandlingMapqXML();
					xr.setContentHandler(hx);
					while(true)
					{
						xr.parse(new InputSource(website.openStream()));
						info=hx.getInformation();
						if(countMapq==0)
						{
							popUpwindow(info);
							countMapq++;
						}
						setDistTimeonMap(info);
						Thread.sleep(60000);

						if(isGPSready==false)
						{
							locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
							Criteria criteria=new Criteria();
							bestProvider = locationManager.getBestProvider(criteria,true);
							myLoc=locationManager.getLastKnownLocation(bestProvider);
							mapq=staticmapq.replace("srclat", String.valueOf(myLoc.getLatitude()));
							mapq=mapq.replace("srclong", String.valueOf(myLoc.getLongitude()));
							countDist++;
						}
						else {
							mapq=staticmapq.replace("srclat", String.valueOf(followOff.getLatitude()));
							mapq=mapq.replace("srclong", String.valueOf(followOff.getLongitude()));
						}
						mapq=mapq.replace("destlat", sharedClass.getFinalLatitude());
						mapq=mapq.replace("destlong", sharedClass.getFinalLongitude());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e);
				}

			}
		}).start();
	}

	public void constructionPlot()
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//-----------------Construction Code Starts----------------------------------
				try
				{

					URL website = new URL(url);
					//setMsg(finalurl);
					SAXParserFactory spf= SAXParserFactory.newInstance();
					SAXParser sp=spf.newSAXParser();
					XMLReader xr= sp.getXMLReader();
					HandlingXML hx=new HandlingXML();
					removeMarkers();
					hx.resetData();
					xr.setContentHandler(hx);
					xr.parse(new InputSource(website.openStream()));

					for(int i=0;i<hx.getCount();i++)
					{
						XMLData info=hx.getInformation(i);
						setLatLngOnMap(info);
					}
				}
				catch(Exception e)
				{
					setMsg(e.toString());

				}
			}

		}).start();

	}
	public void popUpwindow(XMLMapqData info)
	{
		final XMLMapqData inf=info;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Trip Details");
				alertDialogBuilder
				.setMessage("Distance: "+inf.getDistance()+" miles\nTravel Time: "+inf.getTime()+"\nPlease ensure that you have at least "+inf.getFuel()+" gallons fuel in your vehicle, if personal, to reach airport on time."
						+ "\nNOTE: This fuel consumption is calculated on average basis and may not be the exact value.")
						.setCancelable(false)
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
								Toast.makeText(getApplicationContext(), "Please wait for the GPS to connect", Toast.LENGTH_LONG).show();
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			}
		});
	}

	public void addSrcDestToMap(XMLDirData info, String callfrm)
	{
		final XMLDirData inf=info;
		final String callfrom=callfrm;

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(callfrom.equals("statuschange"))
					srcMrk=mMap.addMarker(new MarkerOptions().position(new LatLng(followOff.getLatitude(),followOff.getLongitude())).title(inf.getStartAddr()));
				else srcMrk=mMap.addMarker(new MarkerOptions().position(new LatLng(myLoc.getLatitude(),myLoc.getLongitude())).title(inf.getStartAddr()));
				//destMrk=mMap.addMarker(new MarkerOptions().position(new LatLng(40.642235,-73.78817)).title(inf.getEndAddr()));
				destMrk=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(sharedClass.getFinalLatitude()),Double.valueOf(sharedClass.getFinalLongitude()))).title(inf.getEndAddr()));
			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Normal"))
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Satellite"))
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		else mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void addPoly(PolylineOptions poly)
	{
		final PolylineOptions polyLin=poly;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				polyline = mMap.addPolyline(polyLin);


			}
		});
	}

	public void setMsg(String msg) {
		final String str = msg;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			}
		});
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		turnGPSOff();
	}

	public void setLatLngOnMap(XMLData info)
	{
		final XMLData inf=info;

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				BitmapDescriptor b;
				//String toShow=inf.getName()+"\n"+inf.getVicinity();
				if(inf.getType()==1)
					b=BitmapDescriptorFactory.fromResource(R.drawable.const_mod);
				else b=BitmapDescriptorFactory.fromResource(R.drawable.incid_mod);
				m.add(mMap.addMarker(new MarkerOptions().position(new LatLng(inf.getLat(),inf.getLong())).title(inf.getFullDesc()).snippet("START TIME: "+inf.getStartTime()+"\n"+"END TIME: "+inf.getEndTime()+"\n"+"Severity: "+inf.getSeverity()).icon(b)));

			}
		});

	}
	public void removeMarkers()
	{

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<m.size();i++)
					m.get(i).remove();	
				m.clear();
			}
		});

	}
	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}
	private void decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		PolylineOptions polyL = new PolylineOptions();
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
		//for(int i=0;i<poly.size();i++)
		polyL.addAll(poly);
		polyL.width(5).color(Color.BLUE);
		addPoly(polyL);

	}
	private void turnGPSOn(){
		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if(!provider.contains("gps")){ //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3")); 
			sendBroadcast(poke);
		}
	}

	private void turnGPSOff(){
		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if(provider.contains("gps")){ //if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3")); 
			sendBroadcast(poke);


		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}


}



