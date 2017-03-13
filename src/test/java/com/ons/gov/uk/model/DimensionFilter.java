package com.ons.gov.uk.model;

import lombok.NonNull;

import java.util.List;


public class DimensionFilter {
	private
	@NonNull
	String id;
	private
	@NonNull
	List <String> options;

	public DimensionFilter(String id, List <String> options) {
		this.id = id;
		this.options = options;
	}

	public String getId() {
		return id;
	}

	public List <String> getOptions() {
		return options;
	}
}