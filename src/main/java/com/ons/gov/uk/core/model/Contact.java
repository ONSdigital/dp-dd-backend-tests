package com.ons.gov.uk.core.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Represents a point of contact for a dataset or data resource. There will always be exactly one of these, and we
 * embed it into the parent table to avoid an additional join.
 */
@Embeddable
public class Contact {
	@Column(name = "contact")
	private String name;

	@Column(name = "contact_email")
	private String email;

	@Column(name = "contact_phone")
	private String phoneNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Contact{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}
}
