package de.app.infoapp;

import java.util.Calendar;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class LosungHandler implements ContentHandler {
	private String currentValue; 
	private Losung losung;
	
	public Losung getLosung() {
		return losung;
	}


	private boolean RightDate = false;
	private String date;
	
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch,start,length);



	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

	
		
		
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("Datum")){
			if(currentValue.equals(date)){
				losung.setDate(currentValue);
				RightDate = true;
			}
		}
		if (RightDate){
			
			if (localName.equals("Losungstext")){
				losung.setLosungsText(currentValue);
			}
			if (localName.equals("Losungsvers")){
				losung.setLosungsVers(currentValue);
			}
			if (localName.equals("Lehrtext")){
				losung.setLehrText(currentValue);
			}
			if (localName.equals("Lehrtextvers")){
				losung.setLehrtextVers(currentValue);
			}
			if(localName.equals("Losungen")){
				RightDate = false;
			}
		}
	
		
	}
	public LosungHandler(Calendar cal) {
		super();
		RightDate = false;
		if (cal == null){
			cal = Calendar.getInstance();
		}
		int Year = cal.get(Calendar.YEAR);
		int Month = 1 + cal.get(Calendar.MONTH);
		int day_of_Month = cal.get(Calendar.DAY_OF_MONTH);
		String MonthString = Integer.toString(Month);
		if(Month < 10){
			MonthString = "0"+MonthString;
		}
		String day_of_MonthString = Integer.toString(day_of_Month);
		if(day_of_Month <10){
			day_of_MonthString = "0"+day_of_MonthString;
		}
		date= //"2013-06-27T00:00:00";
				Integer.toString(Year) +"-"+
			MonthString +"-"+
			day_of_MonthString +"T00:00:00"	;
		date.trim();
		 
				
		losung = new Losung(null, null,null, null,null);
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}


	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub

	}


	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

}
