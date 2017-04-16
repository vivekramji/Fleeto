package com.example.constructionincidents;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlingDirXML extends DefaultHandler {

	private XMLDirData info = new XMLDirData();
	private boolean boverPoly=false;
	private boolean bpoints=false;
	private StringBuilder s=new StringBuilder();
	private boolean bstartAddr = false;
	private boolean bendAddr = false;


	public XMLDirData getInformation() {
		return info;
	}


	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub

		if (qName.equals("overview_polyline")) {
			boverPoly = true;
		} else if (qName.equals("points")&&boverPoly) {
			bpoints = true;
		} else if (qName.equals("start_address")) {
			bstartAddr = true;
		} else if (qName.equals("end_address")) {
			bendAddr = true;
		}
	}
	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {



		try {

			if(boverPoly&&bpoints)
			{
				s.append(ch,start,length);

			}
			else if(bstartAddr)
			{
				info.setStartAddr(new String(ch, start, length));
				bstartAddr = false;
			}
			else if (bendAddr)
			{
				info.setEndAddr(new String(ch, start, length));
				bendAddr = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/*@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if(boverPoly&&bpoints)
		{
			info.setEncoded(new String(ch, start, length));
		}
	}*/

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (qName.equals("overview_polyline")) {
			boverPoly = false;
			info.setEncoded(s.toString().trim());
		} 

	}

}
