package com.example.constructionincidents;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherXMLParsing extends Activity implements LocationListener {

	private static final String staticurl="http://api.wunderground.com/api/50f4532de367c7e9/hourly/q/srclat,srclong.xml";
	private String url=null;
	private TableLayout tl;
	private LocationManager locationManager;
	private boolean available=false;
	private Context context=this;
	private HandlingWeaXML hx;
	private int flag=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.climatepage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		tl = (TableLayout) findViewById(R.id.maintable);



		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		/*submit.setOnClickListener(this);
	}

		@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 */			new Thread(new Runnable(){

			 @Override
			 public void run() {
				 // TODO Auto-generated method stub
				 URL website;
				 try{
					 while(true)
					 {
						 if(available==true)
						 {
							 website = new URL(url);
							 break;
						 }
					 }
					 SAXParserFactory spf= SAXParserFactory.newInstance();
					 SAXParser sp=spf.newSAXParser();
					 XMLReader xr= sp.getXMLReader();
					 hx=new HandlingWeaXML();
					 xr.setContentHandler(hx);
					 xr.parse(new InputSource(website.openStream()));
					 for(int i=0;i<hx.getCount();i++)
					 {
						 XMLWeaData info=hx.getInformation(i);
						 addRowsIntoTable(info, i);
					 }
					 //setMsg(info);
					 //tv.setText(info);
					 //Toast.makeText(getApplicationContext(), info.toString(), Toast.LENGTH_LONG).show();

				 }
				 catch(Exception e){
					 setMsg(e.toString());
				 }
			 }

		 }).start();

	}

	public void addRowsIntoTable(XMLWeaData inf, int c)
	{
		final XMLWeaData info=inf;
		final int current=c;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				TableRow tr = new TableRow(context);
				tr.setId(100+current);

				/*tr.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));*/   


				TextView dayTV = new TextView(context);
				dayTV.setId(200+current);
				dayTV.setText(info.getWeekday_name_abbrev());
				dayTV.setTextColor(Color.BLACK);
				/*dayTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));*/
				tr.addView(dayTV);


				TextView timeTV = new TextView(context);
				timeTV.setId(current+300);
				timeTV.setText(info.getCivil());
				timeTV.setTextColor(Color.BLACK);
				/*timeTV.setLayoutParams(	new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));*/
				tr.addView(timeTV);

				TextView condTV = new TextView(context);
				condTV.setId(current+400);
				if(info.getCondition().length()>14)
					info.setCondition(info.getCondition().replace(" ", "\n"));
				condTV.setText(info.getCondition());
				condTV.setTextColor(Color.BLACK);
				/*condTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));*/
				tr.addView(condTV);

				ImageView view = new ImageView(context);
				if(containsNegative(info.getCondition()))
					view.setImageResource(R.drawable.caution);
				else view.setImageResource(R.drawable.ok);
				tr.addView(view);
				if(flag==1)
				{
					findViewById(R.id.loadingPanel).setVisibility(View.GONE);
					flag=0;
				}
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				//setRow(tr);
			}
		});

		// Add the TableRow to the TableLayout

	}

	public boolean containsNegative(String cond)
	{
		cond=cond.toLowerCase();
		if(cond.contains("rain")||cond.contains("snow")||cond.contains("drizzle")||cond.contains("thunderstorm"))
			return true;
		else return false;
	}
	public void setRow(TableRow tr1) {
		final TableRow tr=tr1;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		url=staticurl.replace("srclat", String.valueOf(location.getLatitude()));
		url=url.replace("srclong", String.valueOf(location.getLongitude()));
		available=true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		hx.resetCount();
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}


}