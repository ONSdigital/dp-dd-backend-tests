package com.ons.gov.uk.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsObj {
	@JsonProperty("metadata")
	public Metadata metadata = new Metadata();
	public HashMap <String, Object> itemObject = new HashMap <>();
	private String id, S3URL, title, url, dimensionsUrl;
	private int page, totalPages, itemsPerPage, startIndex, total;

	public String getDimensionsUrl() {
		return dimensionsUrl;
	}

	public void setDimensionsUrl(String dimensionsUrl) {
		this.dimensionsUrl = dimensionsUrl;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
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

	public void setItemsPerPage(int itemsPerPage) {
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

	public HashMap <String, Object> getObjectWithValues(ItemsObj item) {
		HashMap <String, Object> objToRet = new HashMap <>();
		objToRet.put("id", item.getId());
		objToRet.put("url", item.getUrl());
		objToRet.put("metadata", item.getMetadata());
		objToRet.put("s3url", item.getS3URL());
		objToRet.put("dimensionUrl", item.getDimensionsUrl());
		objToRet.put("title", item.getTitle());
		itemObject = objToRet;
		return objToRet;
	}

	public Object getValueForKey(String key) {
		return itemObject.get(key);
	}
}
