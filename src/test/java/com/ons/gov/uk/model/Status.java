package com.ons.gov.uk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;


public enum Status {
	COMPLETE, PENDING;

	@JsonCreator
	public static Status fromString(String value) {
		return valueOf(value.toUpperCase(Locale.UK));
	}

	@Override
	@JsonValue
	public String toString() {
		return name().substring(0, 1) + name().substring(1).toLowerCase(Locale.UK);
	}
}