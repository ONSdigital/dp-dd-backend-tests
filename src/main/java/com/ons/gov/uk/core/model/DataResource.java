package com.ons.gov.uk.core.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResource {
	private String dataResourceID;
	private String title;
	private String metadata;

	public DataResource() {

	}

	public DataResource(String dataResourceID, String title, String metadata) {
		setDataResourceID(dataResourceID);
		setTitle(title);
		setMetadata(metadata);
	}

	public String getDataResourceID() {
		return dataResourceID;
	}

	public DataResource setDataResourceID(String dataResourceID) {
		this.dataResourceID = dataResourceID;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public DataResource setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getMetadata() {
		return metadata;
	}

	public DataResource setMetadata(String metadata) {
		this.metadata = metadata;
		return this;
	}


}
