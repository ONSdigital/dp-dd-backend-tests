package com.ons.gov.uk.core.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents metadata about a particular dataset.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = false)
public class DataSet {


	private String first, last;

	private int page, totalPages, itemsPerPage, startIndex, total;
	private int count;
	private List <Items> items;

	private Set <Dimension> dimensions = Collections.emptySet();


	public int getCount() {
		return count;
	}

	public void setCount(int count) {

		this.count = count;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerpage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List <Items> getItems() {
		return this.items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public Set <Dimension> getDimensions() {
		return dimensions;
	}

	public void setDimensions(Set <Dimension> dimensions) {
		this.dimensions = dimensions;
	}


}
