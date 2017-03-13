package com.ons.gov.uk.model;


public enum FileFormat {
	CSV;

	public String getExtension() {
		return "." + name().toLowerCase();
	}
}
