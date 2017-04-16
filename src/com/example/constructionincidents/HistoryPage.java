package com.example.constructionincidents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryPage extends ListActivity{

	Button newsearc;
	List<SQLData> data;
	Context context=this;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static boolean delFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.history);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ListView lv = getListView(); 
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final int newpos=data.size()-1-position;
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Delete action");
				alertDialogBuilder
				.setMessage("Do you want to delete this entry permanently?")
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();


						deleteEntry(newpos);
						synchronized (this) {
							while(delFlag!=true)
								try {
									this.wait(300);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							HistoryData.getList().remove(newpos);
							makeList();
							delFlag=false;
						}

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				return true;
			}

		});
		newsearc=(Button)findViewById(R.id.newsearch);
		newsearc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sharedClass.setFSK(null);
				Intent inpg=new Intent("com.example.constructionincidents.INPUTPAGE");
				startActivity(inpg);

			}
		});

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		// Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		int newpos=data.size()-position-1;
		sharedClass.carrier= data.get(newpos).getFlightCarrier();
		sharedClass.flightID =String.valueOf(data.get(newpos).getFlightID());
		sharedClass.mon= String.valueOf(data.get(newpos).getMon());
		sharedClass.day= String.valueOf(data.get(newpos).getDay());
		sharedClass.yr= String.valueOf(data.get(newpos).getYear());
		sharedClass.FSK=String.valueOf(data.get(newpos).getBoarding());
		Intent res=new Intent("com.example.constructionincidents.RESULTPAGE");
		startActivity(res);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		makeList();
	}

	public void makeList()
	{
		data=HistoryData.getList();
		List<String> l=new ArrayList<String>();
		for(int i=data.size()-1;i>=0;i--)
			l.add("\nFlight: "+data.get(i).getFlightCarrier()+"-"+data.get(i).getFlightID()+"\nDeparture Date: "+data.get(i).getMon()+"-"+data.get(i).getDay()+"-"+data.get(i).getYear()+"\nBoarding Point: "+data.get(i).getBoarding()+"\n");
		//l.add(data.get(i).getFlightCarrier());


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, l);
		setListAdapter(adapter);
	}

	public void deleteEntry(int newpos)
	{
		final int pos=newpos;
		Thread t=new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String fc=HistoryData.getList().get(pos).getFlightCarrier();
				int fid=HistoryData.getList().get(pos).getFlightID();
				int m=HistoryData.getList().get(pos).getMon();
				int d=HistoryData.getList().get(pos).getDay();
				int y=HistoryData.getList().get(pos).getYear();
				String b=HistoryData.getList().get(pos).getBoarding();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					// Setup the connection with the DB
					String myDB="ese543";
					String MYSQLUSER = "root";
					String MYSQLPW = "nikhil";
					connect = DriverManager
							.getConnection("jdbc:mysql://ec2-52-5-120-73.compute-1.amazonaws.com/"+myDB,MYSQLUSER,MYSQLPW);

					preparedStatement = connect
							.prepareStatement("delete from ese543.history_table where flightCarrier=? and flightID=? and mon=? and day=? and year=? and boarding=?");
					preparedStatement.setString(1, fc);
					preparedStatement.setInt(2, fid);
					preparedStatement.setInt(3, m);
					preparedStatement.setInt(4, d);
					preparedStatement.setInt(5, y);
					preparedStatement.setString(6, b);

					int rows= preparedStatement.executeUpdate();


					if(rows>0)
					{
						setMsg("Deleted!");
						delFlag=true;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally{
					closeAll();
				}
			}
		});
		t.start();
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

	public void setMsg(String msg) {
		final String str = msg;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			}
		});
	}
}



