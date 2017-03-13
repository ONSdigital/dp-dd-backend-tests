package com.ons.gov.uk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.HashMap;

/**
 * Represents a point of contact for a dataset or data resource. There will always be exactly one of these, and we
 * embed it into the parent table to avoid an additional join.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Contact {
	@Column(name = "contact")
	private String name;

	@Column(name = "contact_email")
	private String email;

	@Column(name = "contact_phone")
	private String phone;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Contact{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				'}';
	}

	public HashMap <String, Object> getObjectWithValues(Contact contact) {
		HashMap <String, Object> objToRet = new HashMap <>();
		objToRet.put("name", contact.getName());
		objToRet.put("phone", contact.getPhone());
		objToRet.put("email", contact.getEmail());
		return objToRet;
	}
}
