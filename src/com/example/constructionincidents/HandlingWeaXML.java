package com.example.constructionincidents;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlingWeaXML extends DefaultHandler{

	private List<XMLWeaData> info=new ArrayList<XMLWeaData>();
	private boolean bcivil=false;
	private boolean bweekday_name_abbrev=false;
	private boolean bcondition=false;
	private boolean bqpf=false;
	private boolean bqpfval=false;
	private boolean bsnow=false;
	private boolean bsnowval=false;
	private boolean bhour=false;
	private boolean bicon_url=false;
	private static int count=0;


	public XMLWeaData getInformation(int i)
	{
		return info.get(i);
	}

	public int getCount()
	{
		return count;
	}

	public void resetCount()
	{
		count=0;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("civil"))
		{

			bcivil=true;
		}

		else if(qName.equals("weekday_name_abbrev"))
		{

			bweekday_name_abbrev=true;
		}
		else if(qName.equals("wx"))
		{

			bcondition=true;
		}
		else if(qName.equals("icon_url"))
		{
			bicon_url=true;
		}
		else if(qName.equals("qpf"))
		{
			bqpf=true;
		}
		else if(bqpf&&qName.equals("metric"))
		{
			bqpfval=true;
		}
		else if(qName.equals("snow"))
		{
			bsnow=true;
		}
		else if(bsnow&&qName.equals("metric"))
		{
			bsnowval=true;
		}
		else if(qName.equals("hour"))
		{
			bhour=true;
		}

	}

	public void characters(char ch[], int start, int length) throws SAXException {

		try
		{
			if (info.size() == count) {
				info.add(count, new XMLWeaData());
			}

			if (bcivil) {

				info.get(count).setCivil(new String(ch, start, length));
				bcivil = false;
			}
			else if (bweekday_name_abbrev) {

				info.get(count).setWeekday_name_abbrev(new String(ch, start, length));
				bweekday_name_abbrev = false;
			}
			else if (bcondition) {

				info.get(count).setCondition(new String(ch, start, length));
				bcondition = false;

			}
			else if (bicon_url) {

				info.get(count).setIcon_url(new String(ch, start, length));
				bicon_url = false;
			}
			else if (bqpfval) {

				info.get(count).setQPF(new String(ch, start, length));
				bqpfval = false;
			}
			else if (bhour) {

				info.get(count).setHour(new String(ch, start, length));
				bhour = false;
			}
			else if (bsnowval) {

				info.get(count).setSnow(new String(ch, start, length));
				bsnowval = false;
				count++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (qName.equals("qpf")) {
			bqpf = false;
		} else if (qName.equals("snow")) {
			bsnow = false;
		} 
	}




}
