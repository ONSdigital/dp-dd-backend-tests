package com.ons.gov.uk.core.model;


public enum FileFormat {
	CSV;

	public String getExtension() {
		return "." + name().toLowerCase();
	}
}
