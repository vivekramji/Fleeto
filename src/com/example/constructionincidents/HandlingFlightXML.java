package com.example.constructionincidents;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

public class HandlingFlightXML extends DefaultHandler {
	public String keyindex = null, airlines=null;
	public XMLFlightData a = new XMLFlightData();
	int count = 0, aircount=0;

	//-------------AIRPORT STUFF--------------------
	public boolean bairports = false;
	public boolean bairport = false;
	public boolean bFS = false;
	public boolean bNAME = false;
	public boolean bLAT = false;
	public boolean bLON = false;
	public boolean bairName=false;
	public boolean bairline = false;
	//---------------------------------------------

	//--------------FLIGHT STATUS STUFF---------------
	public boolean bflightStatuses = false;
	public boolean bflightStatus = false;
	public boolean bDEPARTUREAIRPORTFSCODE = false;
	public boolean bARRIVALAIRPORTFSCODE = false;
	public boolean bSCHEDULEDDEPARTUREDATE = false;
	public boolean bESTDEPARTUREDATE = false;
	public boolean bSCHEDULEDDATELOCAL = false;
	public boolean bESTDATELOCAL = false;
	public boolean bDEPARTURETERMINAL = false;
	//------------------------------------------------

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//---------------AIRPORT BOOLEANS---------------
		if (qName.equals("airports")) {
			bairports = true;
		} else if (bairports && qName.equals("airport")) {
			bairport = true;
		} else if (bairport && qName.equals("fs")) {
			bFS = true;
		} else if (bairport && qName.equals("name")) {
			bNAME = true;
		} else if (bairport && qName.equals("latitude")) {
			bLAT = true;
		} else if (bairport && qName.equals("longitude")) {
			bLON = true;
		}else if (aircount==0&&qName.equals("airline")) {
			bairline = true;
			aircount++;
		}else if (bairline && qName.equals("name")) {
			bairName = true;
		}else
			//------------------------------------------------

			//--------FLIGHT STATUS BOOLEANS-----------------
			if(qName.equals("flightStatuses"))
			{
				bflightStatuses = true;
			}else
				if(bflightStatuses && qName.equals("flightStatus"))
				{
					count++;
					bflightStatus = true;
				}else
					if(bflightStatus && qName.equals("departureAirportFsCode"))
					{
						bDEPARTUREAIRPORTFSCODE = true;
					}else
						if(bflightStatus && qName.equals("arrivalAirportFsCode"))
						{
							bARRIVALAIRPORTFSCODE = true;
						}else
							if(bflightStatus && qName.equals("scheduledGateDeparture"))
							{
								bSCHEDULEDDEPARTUREDATE = true;
							}else

								if(bflightStatus && bSCHEDULEDDEPARTUREDATE && qName.equals("dateLocal"))
								{

									bSCHEDULEDDATELOCAL = true;
								}else
									if(bflightStatus && qName.equals("estimatedGateDeparture"))
									{
										bESTDEPARTUREDATE = true;
									}else
										if(bflightStatus && bESTDEPARTUREDATE && qName.equals("dateLocal"))
										{

											bESTDATELOCAL = true;
										}else
											if(bflightStatus && qName.equals("departureTerminal"))
											{
												bDEPARTURETERMINAL = true;
											}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		try {
			if (bFS) {

				a.setaFs(new String(ch, start, length));
				bFS = false;
			}else
				if (bairName) {

					a.setAirline(new String(ch, start, length));
					airlines=new String(ch, start, length);
					bairName = false;
				}else
					if (bNAME) {
						a.setaName(new String(ch, start, length));
						bNAME = false;
					}else

						if (bLAT) {
							a.setaLat(new String(ch, start, length));
							bLAT = false;
						}else

							if (bLON) {
								a.setaLon(new String(ch, start, length));
								bLON = false;
							}else
								//----------------------------------

								if(bDEPARTUREAIRPORTFSCODE)
								{
									keyindex = (new String(ch, start, length));
									Map<String ,XMLFlightData> m = new HashMap<String, XMLFlightData>();
									m= newSharedClass.getAirportMap();
									XMLFlightData newAirport=(XMLFlightData)m.get(keyindex);
									newAirport.setadeparture(true);
									bDEPARTUREAIRPORTFSCODE = false;
								}else
									/*if(bARRIVALAIRPORTFSCODE)
            {
                f.setArrivalAirportFSCode(new String(ch, start, length));
                bARRIVALAIRPORTFSCODE = false;
            }else*/
									if(bSCHEDULEDDATELOCAL)
									{
										String tempdnt = (new String(ch, start, length));
										XMLFlightData newAirport = (XMLFlightData)newSharedClass.getAirportMap().get(keyindex);
										newAirport.setscheduleddnt(tempdnt);
										bSCHEDULEDDATELOCAL = false;
										bSCHEDULEDDEPARTUREDATE=false;
									}else
										if(bESTDATELOCAL)
										{
											String tempdnt = (new String(ch, start, length));
											XMLFlightData newAirport = (XMLFlightData)newSharedClass.getAirportMap().get(keyindex);
											newAirport.setestdnt(tempdnt);
											bESTDATELOCAL = false;
											bESTDEPARTUREDATE=false;
										}else
											if(bDEPARTURETERMINAL)
											{
												String tempdnt = (new String(ch, start, length));
												XMLFlightData newAirport = (XMLFlightData)newSharedClass.getAirportMap().get(keyindex);
												newAirport.setDepartureT(tempdnt);
												bDEPARTURETERMINAL = false;
											}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);

		}


	}


	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (bairports && qName.equals("airports")) {
			bairports = false;
		} else
			if (bairline && qName.equals("airline")) {
				bairline = false;
			} else if (bairport && qName.equals("airport")) {



				Map<String, XMLFlightData> tMap = newSharedClass.getAirportMap();
				if(a.getAirline()==null)
					a.setAirline(airlines);
				tMap.put(a.getaFs(), a);
				a=new XMLFlightData();
				bairport = false;
			}
		if(bflightStatuses && qName.equals("flightStatuses"))
		{
			bflightStatuses = false;
		}
		if(bflightStatus && qName.equals("flightStatus"))
		{

			// newSharedClass.getFlightStatusMap().put(f.getDepartureAirportFSCode(), f);
			bflightStatus = false;
		}

	}
}


