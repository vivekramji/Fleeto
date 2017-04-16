package com.example.constructionincidents;

import java.util.ArrayList;
import java.util.List;

public class HistoryData {

	private static List <SQLData> data = new ArrayList<SQLData>();

	public static void addData(String fc, int fid, int mon, int day, int year, String b)
	{
		SQLData s = new SQLData();
		s.setFlightCarrier(fc);
		s.setFlightID(fid);
		s.setMon(mon);
		s.setDay(day);
		s.setYear(year);
		s.setBoarding(b);
		/*if(data.size()==0)
			data.add(s);
		else
		{
		for(int i=0;i<data.size();i++)
			if(!s.isEqual(data.get(i)))
				data.add(s);
		}*/
		data.add(s);
	}

	public static List<SQLData> getList()
	{
		return data;
	}
}
