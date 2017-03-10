package com.ons.gov.uk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
public class CreateJob {
	@JsonProperty("id")
	private String dataSetId;
	private List <DimensionFilter> dimensions = Collections.emptyList();
	private Set <FileFormat> fileFormats = Collections.singleton(FileFormat.CSV);

	public String getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}

	public List <DimensionFilter> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List <DimensionFilter> dimensions) {
		this.dimensions = dimensions;
	}

	public Set <FileFormat> getFileFormats() {
		return fileFormats;
	}

	public void setFileFormats(Set <FileFormat> fileFormats) {
		this.fileFormats = fileFormats;
	}
}
