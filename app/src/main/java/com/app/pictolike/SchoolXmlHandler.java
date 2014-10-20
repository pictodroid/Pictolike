package com.app.pictolike;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SchoolXmlHandler extends DefaultHandler {

	boolean currentElement = false;
	String currentValue = "";
	SchoolInfo schoolInfo;
	ArrayList<SchoolInfo> schoolList;


	public ArrayList<SchoolInfo> getSchoolList() {
		return schoolList;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (qName.equals("Root")){
			schoolList = new ArrayList<SchoolInfo>();
		}
		else if (qName.equals("SchoolInfo")) {
			schoolInfo = new SchoolInfo();
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		if (qName.equalsIgnoreCase("SchoolName")){
			schoolInfo.setSchoolName(currentValue.trim());
			schoolList.add(schoolInfo);
		}
		currentValue = "";
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}
	}
}
