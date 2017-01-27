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
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSet {


	private String first, last;
	private String id;
	private String S3URL;
	private String title;
	private String url;
	private String dimensionsUrl;
	private Contact contact = new Contact();
	private Metadata metadata = new Metadata();
	private String customerFacingId;
	private int page, totalPages, itemsPerPage, startIndex, total;
	private int count;
	private List <Items> items;
	private Set <Dimension> dimensions = Collections.emptySet();

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getS3URL() {
		return S3URL;
	}

	public void setS3URL(String s3URL) {
		S3URL = s3URL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDimensionsUrl() {
		return dimensionsUrl;
	}

	public void setDimensionsUrl(String dimensionsUrl) {
		this.dimensionsUrl = dimensionsUrl;
	}

	public String getCustomerFacingId() {
		return customerFacingId;
	}

	public void setCustomerFacingId(String customerFacingId) {
		this.customerFacingId = customerFacingId;
	}

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
