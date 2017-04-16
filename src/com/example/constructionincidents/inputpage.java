package com.example.constructionincidents;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class inputpage extends Activity implements OnClickListener, OnItemSelectedListener {


	EditText flightID, flightCareer;
	Button submit, cancel;
	Spinner date;

	Context context = this;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inputpage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		submit = (Button) findViewById(R.id.fieldSubmit);
		cancel = (Button) findViewById(R.id.fieldCancel);
		flightID = (EditText) findViewById(R.id.fieldFlightID);
		flightCareer = (EditText) findViewById(R.id.fieldCarrier);

		/*  month = (EditText) findViewById(R.id.fieldMonth);
        day = (EditText) findViewById(R.id.fieldDay);
        year = (EditText) findViewById(R.id.fieldYear);*/

		date=(Spinner) findViewById(R.id.date);
		List<String> list = new ArrayList<String>();
		Calendar currentDate = Calendar.getInstance(); //Get the current date
		SimpleDateFormat formatter= new SimpleDateFormat("M/d/yyyy"); 
		for(int i=0;i<3;i++)
		{
			String dateNow = formatter.format(currentDate.getTime().getTime()+1000*24*60*60*i);
			list.add(dateNow);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		date.setAdapter(adapter);
		date.setOnItemSelectedListener(this);

		submit.setOnClickListener(this);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flightCareer.setText("");
				flightID.setText("");

			}
		});

	}

	@Override
	public void onClick(View v) {


		sharedClass.carrier= flightCareer.getText().toString();
		sharedClass.flightID =flightID.getText().toString();
		/*sharedClass.mon= month.getText().toString();
        sharedClass.day= day.getText().toString();
        sharedClass.yr= year.getText().toString();*/
		sharedClass.mon= sharedClass.dateDD.substring(0, sharedClass.dateDD.indexOf('/'));
		sharedClass.day= sharedClass.dateDD.substring(sharedClass.dateDD.indexOf('/')+1,sharedClass.dateDD.indexOf('/', sharedClass.dateDD.indexOf('/')+1));
		sharedClass.yr= sharedClass.dateDD.substring(sharedClass.dateDD.length()-4);


		Intent launchIntent = new Intent("com.example.constructionincidents.RESULTPAGE");
		startActivity(launchIntent);


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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		//stopService(new Intent(getBaseContext(), updateService.class));
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		sharedClass.dateDD=parent.getItemAtPosition(position).toString();


	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}




}


