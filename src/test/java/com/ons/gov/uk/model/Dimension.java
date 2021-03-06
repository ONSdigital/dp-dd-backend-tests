package com.ons.gov.uk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Represents metadata about a dimension of a dataset.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Dimension implements Comparable <Dimension> {
	private String id;
	private String name;
	private String url;
	private String type;
	private boolean hierarchical;
	private Set <DimensionOption> options = Collections.emptySet();

	public boolean getHierarchical() {
		return hierarchical;
	}

	public void setHierarchical(boolean hierarchical) {
		this.hierarchical = hierarchical;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set <DimensionOption> getOptions() {
		return options;
	}

	public void setOptions(Set <DimensionOption> options) {
		this.options = options;
	}

	@Override
	public int compareTo(Dimension that) {
		return Objects.compare(this.name, that.name, String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	public boolean equals(Object that) {
		return this == that || that instanceof Dimension && this.compareTo((Dimension) that) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Dimension{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				", options=" + options +
				'}';
	}

	public HashMap <String, Object> getObjectWithValues(Dimension dimension) {
		HashMap <String, Object> objToRet = new HashMap <>();
		objToRet.put("id", dimension.getId());
		objToRet.put("name", dimension.getName());
		objToRet.put("type", dimension.getType());
		objToRet.put("url", dimension.getUrl());
		objToRet.put("options", dimension.getOptions());
		return objToRet;
	}
}
