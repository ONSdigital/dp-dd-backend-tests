package com.ons.gov.uk.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A possible option for a dimension, such as <em>Male</em> or <em>Female</em> for the dimension <em>Sex</em>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DimensionOption implements Comparable <DimensionOption> {
	private final SortedSet <DimensionOption> options = new TreeSet <>();
	private String id = null;
	private String name = null;
	private String code = null;

	public DimensionOption() {

	}

	public DimensionOption(String code, String name, String id) {
		this.id = id;
		this.name = name;
		this.code = code;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Set <DimensionOption> getOptions() {
		return options;
	}

	public void addOption(DimensionOption option) {
		this.options.add(option);
	}

	@Override
	public int compareTo(DimensionOption that) {
		// Note: we only really need to compare IDs here, but compare everything to simplify unit test comparisons.
		return ComparisonChain.start()
				.compare(this.id, that.id, String.CASE_INSENSITIVE_ORDER)
				.compare(this.name, that.name, String.CASE_INSENSITIVE_ORDER)
				.compare(this.options, that.options, Ordering.natural().lexicographical())
				.result();
	}

	@Override
	public boolean equals(Object that) {
		return this == that || that instanceof DimensionOption && this.compareTo((DimensionOption) that) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "DimensionOption{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", options=" + options +
				'}';
	}
}
