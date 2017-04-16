package com.example.constructionincidents;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlingMapqXML extends DefaultHandler {

	private XMLMapqData info=new XMLMapqData();
	private boolean bDistance=false;
	private boolean bTime=false;
	private boolean bFuel=false;
	private boolean bBound=false;


	public XMLMapqData getInformation() {
		return info;
	}


	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub

		if (qName.equals("boundingBox")) {
			bBound = true;
		} else if (qName.equals("distance")&&bBound) {
			bDistance = true;
		} else if (qName.equals("realTime")&&bBound) {
			bTime = true;
		} else if (qName.equals("fuelUsed")&&bBound) {
			bFuel = true;
		}

	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		/*
		 * if (bstatus) {
		 * 
		 * info.setStatus(new String(ch, start, length)); bstatus = false; }
		 * 
		 * else
		 */
		try {

			if (bDistance) {

				info.setDistance(new String(ch, start, length));
				bDistance = false;

			} else if (bTime) {

				info.setTime(new String(ch, start, length));
				bTime = false;

			} else if (bFuel) {

				info.setFuel(new String(ch, start, length));
				bFuel = false;

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
		if (qName.equals("fuelUsed")) {
			bBound = false;

		} 

	}

}
