package com.example.constructionincidents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import android.widget.Toast;

public class SignUp extends Activity{

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private EditText name, un, pw, cpw;
	private String n,u,p,c;
	private Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		name = (EditText) findViewById(R.id.fname);
		un = (EditText) findViewById(R.id.uname);
		pw = (EditText) findViewById(R.id.pword);
		cpw = (EditText) findViewById(R.id.cpword);
		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				n=name.getText().toString().trim();
				u=un.getText().toString().trim();
				p=pw.getText().toString().trim();
				c=cpw.getText().toString().trim();

				if(p.contentEquals(c))
				{
					Thread t=new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Class.forName("com.mysql.jdbc.Driver");
								// Setup the connection with the DB
								String myDB="ese543";
								String MYSQLUSER = "root";
								String MYSQLPW = "nikhil";
								connect = DriverManager
										.getConnection("jdbc:mysql://ec2-52-5-120-73.compute-1.amazonaws.com/"+myDB,MYSQLUSER,MYSQLPW);




								preparedStatement = connect
										.prepareStatement("select username from users where UID IN (select UID from users where username=?)");
								preparedStatement.setString(1, u);

								resultSet= preparedStatement.executeQuery();

								int id=0;
								while(resultSet.next())
									id++;

								if(id==0)
								{
									preparedStatement = connect
											.prepareStatement("insert into users (name,username,password) values (?,?,MD5(?))");
									preparedStatement.setString(1, n);
									preparedStatement.setString(2, u);
									preparedStatement.setString(3,p);

									int rows=preparedStatement.executeUpdate();
									if(rows>0)
									{
										setMsg("Register Successful!");
										sharedClass.setUserName(u);
										Intent res=new Intent("com.example.constructionincidents.INPUTPAGE ");
										startActivity(res);
									}
								}
								else{
									setMsg("Username already exists. Try again.");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							finally{
								closeAll();
							}

						}
					});
					t.start();



				}

				else
				{
					Toast.makeText(getApplicationContext(), "Passowrd doesn't match", Toast.LENGTH_LONG).show();

				}
			}
		});

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

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

}
