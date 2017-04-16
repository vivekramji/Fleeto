package com.example.constructionincidents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends Activity{

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Button ok, register;
	private TextView tv;
	private EditText un,pw;
	private String usern, passw;
	int id = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.loginpage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ok=(Button) findViewById(R.id.ok);
		register=(Button) findViewById(R.id.register);
		tv=(TextView)findViewById(R.id.tv1);
		un=(EditText)findViewById(R.id.username);
		pw=(EditText)findViewById(R.id.password);

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginpg=new Intent("com.example.constructionincidents.SIGNUP");
				startActivity(loginpg);
			}
		});

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setMsg("Please wait...");
				usern=un.getText().toString().trim();
				passw=pw.getText().toString().trim();
				sharedClass.setUserName(usern);
				if(HistoryData.getList().size()>0)
					HistoryData.getList().clear();
				Thread t=new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							// This will load the MySQL driver, each DB has its own driver
							Class.forName("com.mysql.jdbc.Driver");
							// Setup the connection with the DB
							String myDB="ese543";
							String MYSQLUSER = "root";
							String MYSQLPW = "nikhil";
							connect = DriverManager
									.getConnection("jdbc:mysql://ec2-52-5-120-73.compute-1.amazonaws.com/"+myDB,MYSQLUSER,MYSQLPW);


							/*statement = connect.createStatement();
							resultSet = statement.executeQuery("select * from users");
							writeResultSet(resultSet);*/

							preparedStatement = connect
									.prepareStatement("select u.UID from users u "
											+ "where u.username=? and u.password=MD5(?)");
							preparedStatement.setString(1, usern);
							preparedStatement.setString(2, passw);
							resultSet = preparedStatement.executeQuery();
							//writeResultSet(resultSet);
							//int id=resultSet.getInt("UID");
							while(resultSet.next())
								id=resultSet.getInt("UID");

							if(id>0)
							{



								preparedStatement = connect.prepareStatement("select h.flightCarrier, h.flightID, h.mon, h.day, h.year, h.boarding from history_table h inner join users u on h.UID=u.UID where u.UID=? order by h.HID");

								////////start here
								preparedStatement.setInt(1, id);
								resultSet = preparedStatement.executeQuery();
								writeResultSet(resultSet);

								setMsg("Successful Login!");
								Intent loginpg=new Intent("com.example.constructionincidents.HISTORYPAGE");
								startActivity(loginpg);
							}
							else{
								setMsg("Incorrect Credentials. Please try again");
							}


						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}




					}
				});
				t.start();




			}
		});

	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		HistoryData.getList().clear();
		while (resultSet.next()) {

			HistoryData.addData(resultSet.getString("flightCarrier"),
					resultSet.getInt("flightID"), 
					resultSet.getInt("mon"), 
					resultSet.getInt("day"), 
					resultSet.getInt("year"), 
					resultSet.getString("boarding"));

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
	// You need to close the resultSet
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

	private void close() {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		id=0;
		finish();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}




}
