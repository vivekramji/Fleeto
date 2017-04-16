package com.example.constructionincidents;

public class XMLWeaData {
	private String civil=null, weekday_name_abbrev=null,condition=null, icon_url=null, qpf=null, snow=null, hour=null;


	public void setHour(String c)
	{
		hour=c;
	}
	public void setCivil(String c)
	{
		civil=c;
	}
	public void setWeekday_name_abbrev(String s)
	{
		weekday_name_abbrev=s;
	}
	public void setCondition(String t)
	{

		condition=t;

	}

	public void setIcon_url(String c)
	{

		icon_url=c;

	}
	public void setQPF(String c)
	{
		qpf=c;
	}
	public void setSnow(String c)
	{
		snow=c;
	}

	public String getSnow()
	{
		return snow;
	}
	public String getQPF()
	{
		return qpf;
	}
	public String getHour()
	{
		return hour;
	}

	public String getCivil()
	{
		return civil;
	}

	public String getCondition()
	{
		return condition;
	}
	public String getIcon_url()
	{
		return icon_url;
	}
	public String getWeekday_name_abbrev()
	{
		return weekday_name_abbrev;
	}

}
