package com.example.constructionincidents;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlingXML extends DefaultHandler {

	private List<XMLData> info = new ArrayList<XMLData>();
	private boolean bType=false;
	private boolean bSeverity=false;
	private boolean bStartTime = false;
	private boolean bEndTime = false;
	private boolean bFullDesc = false;
	private boolean blong = false;
	private boolean blat = false;
	private static int count = 0;

	public XMLData getInformation(int i) {
		return info.get(i);
	}

	public int getCount() {
		return count;
	}
	public void resetData()
	{
		count=0;
		info.clear();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		/*
		 * if(qName.equals("status")) { bstatus=true; } else
		 */
		if (qName.equals("startTime")) {
			bStartTime = true;
		} else if (qName.equals("endTime")) {
			bEndTime = true;
		} else if (qName.equals("lat")) {
			blat = true;
		} else if (qName.equals("lng")) {
			blong = true;
		} else if (qName.equals("fullDesc")) {
			bFullDesc = true;
		} else if (qName.equals("type")) {
			bType = true;
		} else if (qName.equals("severity")) {
			bSeverity = true;
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
			if (info.size() == count) {
				info.add(count, new XMLData());
			} /*else if (info.size() == count) {
				info.add(count, new XMLData());
			}*/
			if (bStartTime) {

				info.get(count).setStartTime(new String(ch, start, length));
				bStartTime = false;

			} else if (bEndTime) {

				info.get(count).setEndTime(new String(ch, start, length));
				bEndTime = false;

			} else if (bFullDesc) {

				info.get(count).setfullDesc(new String(ch, start, length));
				bFullDesc = false;
				count++;

			} 
			else if (bType) {

				info.get(count).setType(Integer.parseInt(new String(ch, start, length)));
				bType = false;


			} 
			else if (bSeverity) {

				info.get(count).setSeverity(Integer.parseInt(new String(ch, start, length)));
				bSeverity = false;


			} else if (blat) {

				info.get(count).setLat(
						Double.parseDouble(new String(ch, start, length)));
				blat = false;

			} else if (blong) {

				info.get(count).setLong(
						Double.parseDouble(new String(ch, start, length)));
				blong = false;

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

}
