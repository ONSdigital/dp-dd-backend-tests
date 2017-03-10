package com.ons.gov.uk.core.util;


public class AnyData {
	private String stringData;
	private int intData;
	private Object dataObject;

	public AnyData(int intData) {
		setIntData(intData);
	}

	public AnyData(String stringData) {
		setStringData(stringData);
	}

	public AnyData(Object dataObject) {
		setDataObject(dataObject);
	}

	public String getStringData() {
		return stringData;
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	public int getIntData() {
		return intData;
	}

	public void setIntData(int intData) {
		this.intData = intData;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}


}
