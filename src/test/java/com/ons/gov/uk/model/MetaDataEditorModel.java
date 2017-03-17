package com.ons.gov.uk.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaDataEditorModel {
	private String jsonMetadata;
	private String datasetId;
	private String dataResource;
	private String majorVersion;
	private String minorVersion;
	private String majorLabel;
	private String revisionNotes;
	private String revisionReason;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJsonMetadata() {
		return jsonMetadata;
	}

	public void setJsonMetadata(String jsonMetadata) {
		this.jsonMetadata = jsonMetadata;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public String getDataResource() {
		return dataResource;
	}

	public void setDataResource(String dataResource) {
		this.dataResource = dataResource;
	}

	public String getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(String majorVersion) {
		this.majorVersion = majorVersion;
	}

	public String getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(String minorVersion) {
		this.minorVersion = minorVersion;
	}

	public String getRevisionNotes() {
		return revisionNotes;
	}

	public void setRevisionNotes(String revisionNotes) {
		this.revisionNotes = revisionNotes;
	}

	public String getRevisionReason() {
		return revisionReason;
	}

	public void setRevisionReason(String revisionReason) {
		this.revisionReason = revisionReason;
	}

	public String getMajorLabel() {
		return majorLabel;
	}

	public void setMajorLabel(String majorLabel) {
		this.majorLabel = majorLabel;
	}


}
